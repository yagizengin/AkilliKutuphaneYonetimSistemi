package io.github.yagizengin.akys.Config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OverdueLoanScheduler {
    private final JdbcTemplate jdbcTemplate;
    public OverdueLoanScheduler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void runDailyCheck() {
        System.out.println("Starting daily overdue check.\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        jdbcTemplate.execute("SELECT daily_overdue_check()");
        System.out.println("Daily overdue check finished.");
    }
}
