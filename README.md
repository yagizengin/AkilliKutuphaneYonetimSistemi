# 📚 Akıllı Kütüphane Yönetim Sistemi

Modern ve kullanıcı dostu bir kütüphane yönetim sistemi. Bu proje ile kütüphane işlemlerinizi dijital ortamda kolayca yönetebilirsiniz.

![Screenshot](src/main/resources/static/docs/screenshots/homepage.jpeg)

## 📋 İçindekiler

- [Özellikler](#-özellikler)
- [Kullanılan Teknolojiler](#-kullanılan-teknolojiler)
- [Önkoşullar](#-önkoşullar)
- [Kurulum](#-kurulum)
- [Kullanım](#-kullanım)
- [Ekran Görüntüleri](#-ekran-görüntüleri)

## ✨ Özellikler

- 👤 Kullanıcı yönetimi ve kimlik doğrulama
- 📖 Kitap katalog yönetimi
- 📝 Ödünç alma ve iade işlemleri
- 🔖 Kitap rezervasyon sistemi
- 💰 Gecikme cezası takibi
- 📊 Admin paneli ve raporlama
- 🔍 Gelişmiş arama ve filtreleme
- 🔒 JWT tabanlı güvenli kimlik doğrulama

## 🛠️ Kullanılan Teknolojiler

### Frontend
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)

### Backend
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)

### Database
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

### Build Tool
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

## Gereksinimler
- **Java JDK 17** veya üzeri
- **Maven 3.6+**
- **PostgreSQL 13** veya üzeri
## 🚀 Kurulum

### 1. Repository'yi Klonlayın

```bash
git clone https://github.com/yagizengin/AkilliKutuphaneYonetimSistemi.git
cd AkilliKutuphaneYonetimSistemi
```

### 2. Veritabanını Oluşturun

PostgreSQL'de yeni bir veritabanı oluşturun:

```sql
CREATE DATABASE akys;
```

### 3. Uygulama Yapılandırmasını Düzenleyin

`application.properties` dosyasını açın ve veritabanı bilgilerinizi güncelleyin.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/akys
spring.datasource.username=kullanici_adiniz
spring.datasource.password=sifreniz
```
### 4. Gerekli Tabloları ve Varsayılan kullanıcıyı oluşturun
Veritabanina gerekli tablolari ve varsayilan kullaniciyi eklemek icin PostgreSQL içinde:
```bash
\i src/main/resources/db/database_init.sql
```
### 4. Trigger'ları Yükleyin

Veritabanına trigger'ları eklemek için PostgreSQL içinde:

```bash
\i src/main/resources/db/triggers.sql
```

### 5. Uygulamayı Çalıştırın

```bash
mvn spring-boot:run
```

### 6. Tarayıcıda Açın

Uygulama başlatıldıktan sonra tarayıcınızda şu adresi açın:

```
http://localhost:8080
```

## 💻 Kullanım

### Varsayılan Kullanıcı

Sistem ilk kurulumda aşağıdaki varsayılan kullanıcıyı içerir:

- E-mail: `admin@example.com`
- Şifre: `admin123`

### Ana Özellikler

1. **Kullanıcı Paneli:**
   - Kitap arama ve görüntüleme
   - Kitap rezervasyonu yapma
   - Aktif ödünç alınan kitapları görüntüleme
   - Profil yönetimi

2. **Admin Paneli:**
   - Kullanıcı yönetimi
   - Kitap, yazar ve kategori yönetimi
   - Ödünç verme işlemleri
   - Gecikme cezası takibi
   - Sistem raporları

## 📸 Ekran Görüntüleri

### Ana Sayfa
![Ana Sayfa](src/main/resources/static/docs/screenshots/homepage.jpeg)

### Giriş Sayfası
![Giriş](src/main/resources/static/docs/screenshots/login.jpeg)

### Kullanıcı Paneli
![Kullanıcı Paneli](src/main/resources/static/docs/screenshots/user-dashboard.jpeg)

### Kitap Listesi
![Kitap Listesi](src/main/resources/static/docs/screenshots/books.jpeg)

### Admin Paneli
![Admin Paneli](src/main/resources/static/docs/screenshots/admin-dashboard.jpeg)

### Ödünç İşlemleri
![Ödünç İşlemleri](src/main/resources/static/docs/screenshots/loans.jpeg)


<div align="center">
  
[Yağız Engin](https://github.com/yagizengin)

</div>
