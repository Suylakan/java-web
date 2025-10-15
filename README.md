# Java Web Sunucu - Socket Programlama Projesi

## 📋 Proje Hakkında

Bu proje, **3. parti bir bileşen kullanmadan** sadece Java'nın standart kütüphaneleri ile basit bir HTTP web sunucusu oluşturmayı amaçlamaktadır. Sunucu, socket programlama kullanarak 1989 portunda çalışır ve tarayıcıdan gelen HTTP isteklerini yanıtlar.

## 🎯 Proje Gereksinimleri

- ✅ Java programlama dili kullanımı
- ✅ Socket programlama ile web sunucu oluşturma
- ✅ 1989 portunda çalışma
- ✅ H1 boyutunda Ad Soyad gösterimi
- ✅ H2 boyutunda Öğrenci no gösterimi
- ✅ HTML formatında biçimlendirilmiş biyografi (renk, font, stil özellikleri)
- ✅ Hiçbir 3. parti kütüphane kullanılmamıştır

## 🔑 Önemli Kavramlar

### 1. **Socket Nedir?**

**Socket**, ağ üzerinde iki bilgisayar arasında iletişim kurmayı sağlayan bir **uç nokta** (endpoint) yapısıdır. Bir sokete benzer şekilde, iki cihazın "takılıp" veri alışverişi yapabileceği bir bağlantı noktasıdır.

**Socket'in Özellikleri:**
- Her socket bir **IP adresi** ve **port numarası** kombinasyonuna sahiptir
- İki yönlü iletişim sağlar (veri gönderme ve alma)
- TCP veya UDP protokolleri ile çalışabilir
- Bu projede **TCP socket** kullanılmıştır (güvenilir, sıralı veri iletimi)

**Projede Kullanımı:**
```java
// ServerSocket - Sunucu tarafında istemci bağlantılarını kabul eder
ServerSocket serverSocket = new ServerSocket(PORT);

// Socket - İstemci ile bağlantı kurar
Socket clientSocket = serverSocket.accept();
```

### 2. **Port Nedir?**

**Port**, bir bilgisayarda çalışan farklı uygulamaları birbirinden ayıran **sayısal bir değerdir** (0-65535 arası).

**Port Türleri:**
- **0-1023**: Sistem portları (HTTP: 80, HTTPS: 443, FTP: 21)
- **1024-49151**: Kayıtlı portlar (uygulama portları)
- **49152-65535**: Dinamik/özel portlar

**Projede Kullanımı:**
- **1989 portu** kullanılmıştır
- Bu port üzerinden tarayıcı ile sunucu iletişim kurar
- `http://localhost:1989` adresinden erişilir

**Port Benzetmesi:**
Bir apartman düşünün:
- IP adresi = Apartmanın sokak adresi
- Port numarası = Daire numarası
- Her dairede farklı bir uygulama çalışır

### 3. **IP Adresi Nedir?**

**IP (Internet Protocol)**, ağ üzerindeki her cihazın **benzersiz adresidir**. Mektup gönderirken adres gerektiği gibi, internet üzerinde veri göndermek için de IP adresi gerekir.

**IP Türleri:**
- **IPv4**: 192.168.1.1 formatında (32 bit)
- **IPv6**: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 formatında (128 bit)

**Özel IP Adresleri:**
- **127.0.0.1 / localhost**: Kendi bilgisayarınız (loopback)
- **192.168.x.x**: Yerel ağ adresleri
- **0.0.0.0**: Tüm ağ arayüzlerini dinle

**Projede Kullanımı:**
```java
InetAddress.getLocalHost().getHostAddress() // Yerel IP'yi öğren
clientSocket.getInetAddress() // İstemcinin IP'sini öğren
```

---

## 🛠️ Kodun Çalışma Prensibi

### **1. ServerSocket Oluşturma**

```java
ServerSocket serverSocket = new ServerSocket(PORT);
```

**Açıklama:**
- `ServerSocket` nesnesi, sunucu tarafında istemci bağlantılarını kabul eden özel bir socket türüdür
- Belirtilen port numarasında (1989) dinlemeye başlar
- İşletim sisteminden bu portu kullanma izni alır

**Ne İşe Yarar?**
- Gelen bağlantı isteklerini bekler ve kabul eder
- Her yeni istemci için ayrı bir `Socket` nesnesi oluşturur

---

### **2. İstemci Bağlantısını Kabul Etme**

```java
Socket clientSocket = serverSocket.accept();
```

**Açıklama:**
- `accept()` metodu **bloklanır** (bekler) - yani bir istemci bağlanana kadar kod burada durur
- Bir tarayıcı bağlandığında, bu metod yeni bir `Socket` nesnesi döndürür
- Bu `Socket` nesnesi, o belirli istemci ile iletişim kurmak için kullanılır

**Ne İşe Yarar?**
- Tarayıcı `http://localhost:1989` adresine bağlandığında tetiklenir
- İstemci ile sunucu arasında veri alışverişi için kanal açar

---

### **3. Veri Okuma (Input Stream)**

```java
BufferedReader in = new BufferedReader(
    new InputStreamReader(clientSocket.getInputStream())
);
```

**Açıklama:**
- `getInputStream()`: Socket üzerinden gelen veriyi okumak için bir akış (stream) sağlar
- `InputStreamReader`: Byte'ları karakterlere dönüştürür
- `BufferedReader`: Satır satır okuma yaparak performansı artırır

**Ne İşe Yarar?**
- Tarayıcıdan gelen HTTP isteğini okur
- Örnek: `GET / HTTP/1.1`

---

### **4. Veri Gönderme (Output Stream)**

```java
PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
```

**Açıklama:**
- `getOutputStream()`: Socket üzerinden veri göndermek için bir akış sağlar
- `PrintWriter`: Metin verisini kolayca yazmayı sağlar
- `true` parametresi: Otomatik flush (tampondan gönderme) aktif

**Ne İşe Yarar?**
- HTTP yanıt başlıklarını gönderir
- HTML içeriğini tarayıcıya iletir

---

### **5. HTTP Protokolü**

**HTTP İstek Formatı (Tarayıcıdan Gelen):**
```
GET / HTTP/1.1
Host: localhost:1989
User-Agent: Mozilla/5.0...
...
```

**HTTP Yanıt Formatı (Sunucudan Giden):**
```
HTTP/1.1 200 OK
Content-Type: text/html; charset=UTF-8
Connection: close

<!DOCTYPE html>
<html>
...
```

**Önemli Bileşenler:**
- **Status Line**: `HTTP/1.1 200 OK` (durum kodu)
- **Headers**: Yanıt hakkında bilgiler (içerik türü, bağlantı durumu)
- **Boş Satır**: Başlıklar ile içerik arasında **zorunlu**
- **Body**: HTML içeriği

---

### **6. Kullanılan Nesneler ve Özellikleri**

| Nesne | Özellikler | Ne İşe Yarar? |
|-------|-----------|---------------|
| `ServerSocket` | - Port numarası<br>- Bağlantı kuyruğu | İstemci bağlantılarını dinler ve kabul eder |
| `Socket` | - IP adresi<br>- Port numarası<br>- Input/Output stream'ler | İki bilgisayar arasında veri alışverişi sağlar |
| `BufferedReader` | - Tampon bellek<br>- Satır okuma | Verimli veri okuma işlemi yapar |
| `PrintWriter` | - Otomatik flush<br>- Metin yazma | Kolay metin gönderme sağlar |
| `InetAddress` | - IP adresi<br>- Host adı | Ağ adresi bilgilerini temsil eder |

---

## 📁 Proje Yapısı

```
java web/
│
├── WebSunucu.java          # Ana sunucu sınıfı
└── README.md               # Bu dosya (dokümantasyon)
```

---

## 🚀 Nasıl Çalıştırılır?

### **Adım 1: Java Kurulumunu Kontrol Edin**

```bash
java -version
```

Java 8 veya üzeri bir sürüm yüklü olmalıdır.

### **Adım 2: Kodu Derleyin**

PowerShell'de proje klasörüne gidin ve şu komutu çalıştırın:

```powershell
cd "c:\Users\HARUN BERKE ÖZTÜRK\OneDrive\Masaüstü\java web"
javac WebSunucu.java
```

Bu komut, `WebSunucu.class` dosyasını oluşturur.

### **Adım 3: Sunucuyu Başlatın**

```powershell
java WebSunucu
```

**Beklenen Çıktı:**
```
╔════════════════════════════════════════════════════════════╗
║           JAVA WEB SUNUCU - SOCKET PROGRAMLAMA            ║
╚════════════════════════════════════════════════════════════╝

✓ Sunucu başlatıldı!
✓ Port: 1989
✓ IP Adresi: 192.168.1.100

Tarayıcınızdan şu adreslere bağlanabilirsiniz:
  → http://localhost:1989
  → http://127.0.0.1:1989
  → http://192.168.1.100:1989

Sunucu çalışıyor... İstemci bağlantıları bekleniyor...
(Durdurmak için Ctrl+C basın)
─────────────────────────────────────────────────────────────
```

### **Adım 4: Tarayıcıdan Bağlanın**

Herhangi bir web tarayıcıda şu adreslerden birine gidin:
- `http://localhost:1989`
- `http://127.0.0.1:1989`

### **Adım 5: Sunucuyu Durdurun**

Terminalde **Ctrl+C** tuş kombinasyonuna basın.

---

## 🎨 HTML Çıktısı

Tarayıcıda gösterilecek içerik:

- **H1 Başlık**: Ad Soyad (Harun Berke Öztürk)
- **H2 Başlık**: Öğrenci No (202112345)
- **Biyografi Bölümü**:
  - Renkli ve biçimlendirilmiş metin
  - Vurgulu kelimeler
  - Liste formatında yetenekler
  - Gradient arkaplan
  - Animasyonlar

---

## 🔍 Kod Akış Diyagramı

```
┌─────────────────────────────────────┐
│   Program Başlangıcı (main)        │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  ServerSocket Oluştur (Port 1989)  │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│     Sonsuz Döngü Başla (while)     │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│    İstemci Bağlantısı Bekle        │
│         (accept() bloğu)           │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│   Tarayıcı Bağlandı! Socket Oluştu │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│     İstemciyi İşle Fonksiyonu      │
│      (isltemciyiIsle)              │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│   Input Stream Aç - İstek Oku      │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  Output Stream Aç - Yanıt Hazırla  │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│   HTTP Başlıkları Gönder           │
│   (200 OK, Content-Type, vs.)      │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│     HTML İçeriği Oluştur           │
│      (htmlOlustur)                 │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│    HTML'i Tarayıcıya Gönder        │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│   Socket Kapat, Kaynakları Temizle │
└──────────────┬──────────────────────┘
               │
               ▼
         Döngüye Dön
     (Yeni istemci bekle)
```

---

## 📊 Socket İletişim Diyagramı

```
┌──────────────┐                          ┌──────────────┐
│   Tarayıcı   │                          │  Web Sunucu  │
│  (İstemci)   │                          │   (Sunucu)   │
└──────┬───────┘                          └──────┬───────┘
       │                                         │
       │  1. TCP Bağlantı İsteği                │
       │────────────────────────────────────────>│
       │    (SYN Paketi)                        │
       │                                         │
       │  2. Bağlantı Kabul                     │
       │<────────────────────────────────────────│
       │    (SYN-ACK Paketi)                    │
       │                                         │
       │  3. Bağlantı Onay                      │
       │────────────────────────────────────────>│
       │    (ACK Paketi)                        │
       │                                         │
       │  ═══ TCP Bağlantısı Kuruldu ═══       │
       │                                         │
       │  4. HTTP GET İsteği                    │
       │────────────────────────────────────────>│
       │    GET / HTTP/1.1                      │
       │    Host: localhost:1989                │
       │                                         │
       │                                    5. İsteği İşle
       │                                    6. HTML Oluştur
       │                                         │
       │  7. HTTP Yanıt (200 OK)                │
       │<────────────────────────────────────────│
       │    HTTP/1.1 200 OK                     │
       │    Content-Type: text/html             │
       │    [HTML İçeriği]                      │
       │                                         │
       │  8. Bağlantı Sonlandır                 │
       │────────────────────────────────────────>│
       │    (FIN Paketi)                        │
       │                                         │
       │  9. Sonlandırma Onayı                  │
       │<────────────────────────────────────────│
       │    (ACK ve FIN Paketleri)              │
       │                                         │
       │  ═══ Bağlantı Kapatıldı ═══           │
       │                                         │
┌──────┴───────┐                          ┌──────┴───────┐
│   Tarayıcı   │                          │  Web Sunucu  │
│   HTML'i     │                          │ Yeni İstemci │
│   Gösterir   │                          │   Bekliyor   │
└──────────────┘                          └──────────────┘
```

---

## ⚠️ Olası Sorunlar ve Çözümleri

### **1. Port Zaten Kullanımda Hatası**

**Hata:**
```
java.net.BindException: Address already in use: bind
```

**Çözüm:**
- Başka bir uygulama 1989 portunu kullanıyor olabilir
- Portu değiştirin veya çalışan uygulamayı kapatın
- Windows'ta port kontrolü:
  ```powershell
  netstat -ano | findstr :1989
  ```

### **2. Güvenlik Duvarı Engeli**

**Semptom:**
- Sunucu başlıyor ama tarayıcı bağlanamıyor

**Çözüm:**
- Windows Güvenlik Duvarı'nda Java'ya izin verin
- Ayarlar → Güvenlik → Windows Güvenlik Duvarı

### **3. Java Bulunamadı Hatası**

**Hata:**
```
'java' is not recognized as an internal or external command
```

**Çözüm:**
- Java JDK'yı yükleyin
- Sistem PATH değişkenine Java ekleyin

---

## 📚 Öğrenilen Kavramlar

Bu projede şu konular öğrenilmiştir:

✅ **Socket Programlama**: TCP socket ile ağ iletişimi  
✅ **ServerSocket**: Sunucu tarafı bağlantı yönetimi  
✅ **Input/Output Streams**: Veri okuma ve yazma  
✅ **HTTP Protokolü**: Web iletişim standartları  
✅ **Port ve IP**: Ağ adresleme kavramları  
✅ **HTML Oluşturma**: Dinamik web içeriği üretme  
✅ **Exception Handling**: Hata yönetimi  
✅ **Resource Management**: Kaynakları temizleme (try-finally)  

---

## 🎥 Video Anlatım İçin Kontrol Listesi

Video çekerken şu konuları mutlaka açıklayın:

### **Socket Kavramları (30 Puan)**
- ✅ Socket nedir? Neden kullanılır?
- ✅ Port nedir? 1989 portu neden seçildi?
- ✅ IP adresi nedir? localhost ne demek?
- ✅ TCP/IP protokolü nasıl çalışır?

### **Kod Açıklaması (30 Puan)**
- ✅ ServerSocket nasıl oluşturulur?
- ✅ accept() metodu ne işe yarar?
- ✅ InputStream ve OutputStream nedir?
- ✅ BufferedReader ve PrintWriter neden kullanıldı?
- ✅ HTTP protokolü nasıl uygulandı?
- ✅ HTML içeriği nasıl oluşturuldu?
- ✅ try-finally bloğu neden önemli?

### **Çıktı Gösterimi (40 Puan)**
- ✅ Programı derle ve çalıştır
- ✅ Sunucu loglarını göster
- ✅ Tarayıcıda sayfayı aç
- ✅ H1, H2 başlıklarını göster
- ✅ Biçimlendirilmiş biyografiyi göster
- ✅ Renk, font özelliklerini vurgula

---

## 👨‍💻 Geliştirici

**Ad Soyad**: Harun Berke Öztürk  
**Öğrenci No**: 202112345  
**Proje**: Java Socket Programlama - Web Sunucu

---

## 📄 Lisans

Bu proje eğitim amaçlıdır ve serbestçe kullanılabilir.

---

## 🌟 Sonuç

Bu proje, Java'nın temel socket programlama özelliklerini kullanarak basit ama işlevsel bir web sunucusu oluşturmayı göstermektedir. Hiçbir 3. parti kütüphane kullanmadan, sadece JDK'nın standart kütüphaneleri ile HTTP protokolünü uygulayarak web tarayıcılarına HTML içeriği sunabilmekteyiz.

**Önemli Çıkarımlar:**
- Socket programlama, ağ iletişiminin temelidir
- HTTP protokolü basit ama güçlü bir standarttır
- Java'nın standart kütüphaneleri güçlü ve yeterlidir
- İyi kaynak yönetimi (try-finally) kritik öneme sahiptir

---

**🚀 İyi Kodlamalar!**
