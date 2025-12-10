-- ===========================================================

DROP TRIGGER IF EXISTS trigger_validate_loan_creation ON loans;
DROP TRIGGER IF EXISTS trigger_decrease_book_copies ON loans;
DROP TRIGGER IF EXISTS trigger_handle_loan_return ON loans;
DROP TRIGGER IF EXISTS trigger_update_loan_status ON loans;
DROP TRIGGER IF EXISTS trigger_create_fine ON loans;
DROP TRIGGER IF EXISTS trigger_update_fine_on_return ON loans;

-- ===========================================================

CREATE OR REPLACE FUNCTION calculate_fine_amount(p_due_date DATE, p_return_date DATE DEFAULT NULL)
RETURNS NUMERIC AS $$
BEGIN
    IF p_return_date IS NULL THEN
        RETURN GREATEST(0, (CURRENT_DATE - p_due_date) * 25.00);
    ELSE
        RETURN GREATEST(0, (p_return_date - p_due_date) * 25.00);
    END IF;
END;
$$ LANGUAGE plpgsql;

-- ===========================================================

CREATE OR REPLACE FUNCTION validate_loan_rules()
RETURNS TRIGGER AS $$
DECLARE
    v_active_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO v_active_count 
    FROM loans 
    WHERE user_id = NEW.user_id AND status IN ('ACTIVE', 'OVERDUE');
    
    IF v_active_count >= 5 THEN
        RAISE EXCEPTION 'Kullanıcı maksimum 5 kitap ödünç alabilir. Mevcut: %', v_active_count;
    END IF;

    IF EXISTS (
        SELECT 1 FROM loans 
        WHERE user_id = NEW.user_id AND book_id = NEW.book_id AND status IN ('ACTIVE', 'OVERDUE')
    ) THEN
        RAISE EXCEPTION 'Kullanıcı bu kitabı zaten ödünç almış.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_loan_rules
    BEFORE INSERT ON loans
    FOR EACH ROW EXECUTE FUNCTION validate_loan_rules();

-- ===========================================================

CREATE OR REPLACE FUNCTION decrease_inventory_on_loan()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE books 
    SET available_copies = GREATEST(0, available_copies - 1) 
    WHERE id = NEW.book_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_decrease_inventory
    AFTER INSERT ON loans
    FOR EACH ROW EXECUTE FUNCTION decrease_inventory_on_loan();

-- ===========================================================

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conname = 'uq_fines_loan'
    ) THEN
        ALTER TABLE fines
        ADD CONSTRAINT uq_fines_loan UNIQUE (loan_id);
    END IF;
END $$;

-- ===========================================================

CREATE OR REPLACE FUNCTION process_return()
RETURNS TRIGGER AS $$
DECLARE
    v_fine_amount NUMERIC;
    v_days_late INTEGER;
BEGIN
    IF NEW.return_date IS NOT NULL AND OLD.return_date IS NULL THEN
        
        NEW.status := 'RETURNED';

        UPDATE books 
        SET available_copies = available_copies + 1 
        WHERE id = NEW.book_id;

        IF NEW.return_date > NEW.due_date THEN
            v_fine_amount := calculate_fine_amount(NEW.due_date, NEW.return_date);
            v_days_late := NEW.return_date - NEW.due_date;
            
            INSERT INTO fines (loan_id, amount, reason, payment_status)
            VALUES (
                NEW.id, 
                v_fine_amount, 
                'Gecikmiş iade - ' || v_days_late || ' gün gecikme', 
                'UNPAID'
            )
            ON CONFLICT (loan_id) 
            DO UPDATE SET 
                amount = EXCLUDED.amount,
                reason = EXCLUDED.reason;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_process_return
    BEFORE UPDATE ON loans
    FOR EACH ROW EXECUTE FUNCTION process_return();

-- ===========================================================
CREATE OR REPLACE FUNCTION daily_overdue_check()
RETURNS void AS $$
DECLARE
    rec RECORD;
    v_fine NUMERIC;
    v_days_late INTEGER;
BEGIN
    UPDATE loans 
    SET status = 'OVERDUE' 
    WHERE status = 'ACTIVE' 
      AND due_date < CURRENT_DATE 
      AND return_date IS NULL;

    FOR rec IN 
        SELECT id, due_date FROM loans 
        WHERE status = 'OVERDUE' AND return_date IS NULL
    LOOP
        v_fine := calculate_fine_amount(rec.due_date, CURRENT_DATE);
        v_days_late := CURRENT_DATE - rec.due_date;

        INSERT INTO fines (loan_id, amount, reason, payment_status)
        VALUES (
            rec.id, 
            v_fine, 
            'Gecikmiş iade - ' || v_days_late || ' gün (Güncel)', 
            'UNPAID'
        )
        ON CONFLICT (loan_id) 
        DO UPDATE SET 
            amount = EXCLUDED.amount,
            reason = EXCLUDED.reason;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- ===========================================================

CREATE OR REPLACE FUNCTION create_loan_from_reservation()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO loans (user_id, book_id, checkout_date, due_date, status)
    VALUES (NEW.user_id, NEW.book_id, NEW.reservation_date, NEW.reservation_date + INTERVAL '14 days', 'ACTIVE');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_create_loan_from_reservation
    AFTER UPDATE ON reservations
    FOR EACH ROW
    WHEN (OLD.status = 'PENDING' AND NEW.status = 'FULFILLED')
    EXECUTE FUNCTION create_loan_from_reservation();