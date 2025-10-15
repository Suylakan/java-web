import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * WebSunucu - Basit bir HTTP Web Sunucusu
 * 
 * Bu sınıf, Java Socket programlama kullanarak 1989 portunda çalışan
 * basit bir web sunucusu oluşturur. Herhangi bir 3. parti kütüphane kullanmadan
 * sadece Java'nın standart kütüphaneleri ile çalışır.
 * 
 * Önemli Kavramlar:
 * - Socket: İki bilgisayar arasında ağ üzerinden iletişim kurmayı sağlayan uç nokta
 * - Port: Bir bilgisayarda çalışan farklı uygulamaları birbirinden ayıran sayısal değer (0-65535)
 * - IP: Internet Protocol - Ağ üzerindeki cihazların benzersiz adresi
 * - ServerSocket: Sunucu tarafında istemci bağlantılarını kabul eden socket
 */
public class WebSunucu {
    
    // Port numarası - Sunucunun hangi portta dinleyeceğini belirler
    private static final int PORT = 1989;
    
    // Öğrenci bilgileri - HTML sayfasında gösterilecek
    private static final String AD_SOYAD = "Harun Berke Öztürk";
    private static final String OGRENCI_NO = "1240505010";
    
    /**
     * Ana metod - Programın başlangıç noktası
     * Sunucuyu başlatır ve sürekli olarak istemci bağlantılarını dinler
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           JAVA WEB SUNUCU - SOCKET PROGRAMLAMA            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // ServerSocket nesnesi - İstemci bağlantılarını kabul eder
        ServerSocket serverSocket = null;
        
        try {
            // ServerSocket oluşturma - Belirtilen portta dinlemeye başlar
            // Bu nesne, istemcilerden gelen bağlantı isteklerini kabul eder
            serverSocket = new ServerSocket(PORT);
            
            System.out.println("✓ Sunucu başlatıldı!");
            System.out.println("✓ Port: " + PORT);
            System.out.println("✓ IP Adresi: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println();
            System.out.println("Tarayıcınızdan şu adreslere bağlanabilirsiniz:");
            System.out.println("  → http://localhost:" + PORT);
            System.out.println("  → http://127.0.0.1:" + PORT);
            System.out.println("  → http://" + InetAddress.getLocalHost().getHostAddress() + ":" + PORT);
            System.out.println();
            System.out.println("Sunucu çalışıyor... İstemci bağlantıları bekleniyor...");
            System.out.println("(Durdurmak için Ctrl+C basın)");
            System.out.println("─────────────────────────────────────────────────────────────");
            System.out.println();
            
            // Sonsuz döngü - Sunucu sürekli çalışır
            while (true) {
                // accept() metodu - Yeni bir istemci bağlantısını bekler ve kabul eder
                // Bu metod bloklanır (bekler) ta ki bir istemci bağlanana kadar
                Socket clientSocket = serverSocket.accept();
                
                // Bağlantı bilgilerini logla
                String clientIP = clientSocket.getInetAddress().getHostAddress();
                int clientPort = clientSocket.getPort();
                String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
                
                System.out.println("🔔 [" + timestamp + "] Yeni bağlantı!");
                System.out.println("   İstemci IP: " + clientIP);
                System.out.println("   İstemci Port: " + clientPort);
                
                // İstemciyi işle - HTTP isteğini al ve yanıt gönder
                isltemciyiIsle(clientSocket);
                
                System.out.println("   ✓ İstek tamamlandı");
                System.out.println();
            }
            
        } catch (IOException e) {
            // Hata yönetimi
            System.err.println("❌ Sunucu hatası: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Kaynakları temizle - ServerSocket'i kapat
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                    System.out.println("Sunucu kapatıldı.");
                } catch (IOException e) {
                    System.err.println("ServerSocket kapatma hatası: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * İstemciyi işle metodu
     * 
     * Bu metod, bağlanan istemciden HTTP isteğini okur ve
     * HTML formatında yanıt gönderir.
     * 
     * @param clientSocket İstemci ile bağlantı kuran Socket nesnesi
     */
    private static void isltemciyiIsle(Socket clientSocket) {
        // BufferedReader - İstemciden veri okumak için kullanılır
        BufferedReader in = null;
        // PrintWriter - İstemciye veri göndermek için kullanılır
        PrintWriter out = null;
        
        try {
            // Input Stream - İstemciden gelen veriyi okumak için
            // getInputStream(): Socket üzerinden veri almayı sağlar
            in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream())
            );
            
            // Output Stream - İstemciye veri göndermek için
            // getOutputStream(): Socket üzerinden veri göndermeyi sağlar
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            // HTTP isteğini oku
            String requestLine = in.readLine();
            if (requestLine != null) {
                System.out.println("   İstek: " + requestLine);
            }
            
            // Geri kalan başlıkları oku (boş satıra kadar)
            String headerLine;
            while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
                // HTTP başlıklarını sessizce işle
            }
            
            // HTTP Yanıt Başlıkları
            // Status Line - HTTP/1.1 200 OK (Başarılı yanıt)
            out.println("HTTP/1.1 200 OK");
            
            // Content-Type - Gönderilen içeriğin türü (HTML ve karakter seti)
            out.println("Content-Type: text/html; charset=UTF-8");
            
            // Connection - Bağlantı türü (kapalı)
            out.println("Connection: close");
            
            // Boş satır - Başlıklar ile içerik arasında zorunlu
            out.println();
            
            // HTML içeriğini oluştur ve gönder
            String htmlContent = htmlOlustur();
            out.println(htmlContent);
            out.flush(); // Buffer'ı temizle ve veriyi gönder
            
        } catch (IOException e) {
            System.err.println("   ❌ İstemci işleme hatası: " + e.getMessage());
        } finally {
            // Kaynakları temizle
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                System.err.println("   Kaynakları kapatma hatası: " + e.getMessage());
            }
        }
    }
    
    /**
     * HTML içeriğini oluştur metodu
     * 
     * Öğrenci bilgilerini içeren, biçimlendirilmiş HTML sayfası oluşturur.
     * H1, H2 başlıkları, renkler, fontlar ve stil özellikleri içerir.
     * 
     * @return String HTML formatında sayfa içeriği
     */
    private static String htmlOlustur() {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"tr\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Öğrenci Bilgileri - Java Web Sunucu</title>\n");
        html.append("    <style>\n");
        html.append("        /* CSS Stilleri - Sayfanın görünümünü düzenler */\n");
        html.append("        body {\n");
        html.append("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n");
        html.append("            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n");
        html.append("            margin: 0;\n");
        html.append("            padding: 20px;\n");
        html.append("            min-height: 100vh;\n");
        html.append("            display: flex;\n");
        html.append("            justify-content: center;\n");
        html.append("            align-items: center;\n");
        html.append("        }\n");
        html.append("        .container {\n");
        html.append("            background-color: white;\n");
        html.append("            border-radius: 20px;\n");
        html.append("            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);\n");
        html.append("            padding: 40px;\n");
        html.append("            max-width: 800px;\n");
        html.append("            width: 100%;\n");
        html.append("            animation: fadeIn 0.8s ease-in;\n");
        html.append("        }\n");
        html.append("        @keyframes fadeIn {\n");
        html.append("            from { opacity: 0; transform: translateY(20px); }\n");
        html.append("            to { opacity: 1; transform: translateY(0); }\n");
        html.append("        }\n");
        html.append("        h1 {\n");
        html.append("            color: #667eea;\n");
        html.append("            font-size: 2.5em;\n");
        html.append("            margin-bottom: 10px;\n");
        html.append("            text-align: center;\n");
        html.append("            font-weight: bold;\n");
        html.append("            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);\n");
        html.append("        }\n");
        html.append("        h2 {\n");
        html.append("            color: #764ba2;\n");
        html.append("            font-size: 1.8em;\n");
        html.append("            margin-bottom: 30px;\n");
        html.append("            text-align: center;\n");
        html.append("            font-weight: 600;\n");
        html.append("        }\n");
        html.append("        .biyografi {\n");
        html.append("            background-color: #f8f9fa;\n");
        html.append("            border-left: 5px solid #667eea;\n");
        html.append("            padding: 25px;\n");
        html.append("            border-radius: 10px;\n");
        html.append("            margin-top: 20px;\n");
        html.append("            line-height: 1.8;\n");
        html.append("            font-size: 1.1em;\n");
        html.append("        }\n");
        html.append("        .biyografi h3 {\n");
        html.append("            color: #764ba2;\n");
        html.append("            margin-top: 0;\n");
        html.append("            font-size: 1.5em;\n");
        html.append("        }\n");
        html.append("        .biyografi p {\n");
        html.append("            color: #333;\n");
        html.append("            margin: 15px 0;\n");
        html.append("            text-align: justify;\n");
        html.append("        }\n");
        html.append("        .highlight {\n");
        html.append("            background-color: #fff3cd;\n");
        html.append("            padding: 2px 8px;\n");
        html.append("            border-radius: 4px;\n");
        html.append("            font-weight: bold;\n");
        html.append("            color: #856404;\n");
        html.append("        }\n");
        html.append("        .info-box {\n");
        html.append("            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n");
        html.append("            color: white;\n");
        html.append("            padding: 15px;\n");
        html.append("            border-radius: 10px;\n");
        html.append("            margin-top: 20px;\n");
        html.append("            text-align: center;\n");
        html.append("            font-size: 0.9em;\n");
        html.append("        }\n");
        html.append("        .badge {\n");
        html.append("            display: inline-block;\n");
        html.append("            background-color: #28a745;\n");
        html.append("            color: white;\n");
        html.append("            padding: 5px 15px;\n");
        html.append("            border-radius: 20px;\n");
        html.append("            font-size: 0.9em;\n");
        html.append("            margin: 5px;\n");
        html.append("            font-weight: bold;\n");
        html.append("        }\n");
        html.append("        .skills {\n");
        html.append("            margin-top: 20px;\n");
        html.append("            text-align: center;\n");
        html.append("        }\n");
        html.append("        ul {\n");
        html.append("            list-style-type: none;\n");
        html.append("            padding: 0;\n");
        html.append("        }\n");
        html.append("        li {\n");
        html.append("            padding: 8px;\n");
        html.append("            margin: 5px 0;\n");
        html.append("            background-color: #e9ecef;\n");
        html.append("            border-radius: 5px;\n");
        html.append("            color: #495057;\n");
        html.append("        }\n");
        html.append("        li:before {\n");
        html.append("            content: '✓ ';\n");
        html.append("            color: #28a745;\n");
        html.append("            font-weight: bold;\n");
        html.append("            margin-right: 10px;\n");
        html.append("        }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"container\">\n");
        
        // H1 başlık - Ad Soyad
        html.append("        <h1>").append(AD_SOYAD).append("</h1>\n");
        
        // H2 başlık - Öğrenci No
        html.append("        <h2>Öğrenci No: ").append(OGRENCI_NO).append("</h2>\n");
        
        // Biyografi bölümü
        html.append("        <div class=\"biyografi\">\n");
        html.append("            <h3>📝 Biyografi</h3>\n");
        html.append("            <p>\n");
        html.append("                Merhaba! Ben <span class=\"highlight\">").append(AD_SOYAD).append("</span>, ");
        html.append("                Yazılım Mühendisliği öğrencisiyim. Yazılım geliştirme ve ");
        html.append("                <strong style=\"color: #667eea;\">web teknolojileri</strong> konusunda ");
        html.append("                kendimi geliştiriyorum.\n");
        html.append("            </p>\n");
        html.append("            <p>\n");
        html.append("                <em style=\"color: #764ba2;\">Java programlama</em> dili ile ");
        html.append("                <span style=\"background-color: #d1ecf1; padding: 2px 6px; border-radius: 3px; color: #0c5460;\">");
        html.append("                socket programlama</span>, ağ iletişimi ve web sunucu geliştirme ");
        html.append("                konularında çalışmalar yapıyorum. Bu proje, <u>3. parti kütüphane kullanmadan</u> ");
        html.append("                sadece Java'nın standart kütüphaneleri ile basit bir HTTP sunucusu oluşturmayı ");
        html.append("                amaçlamaktadır.\n");
        html.append("            </p>\n");
        html.append("            <p style=\"font-style: italic; color: #6c757d;\">\n");
        html.append("                💡 <strong>İlgi Alanlarım:</strong> Yazılım mimarisi, ");
        html.append("                backend geliştirme, veritabanı yönetimi, algoritma tasarımı ve ");
        html.append("                <span style=\"color: #e83e8c; font-weight: bold;\">sistem programlama</span>.\n");
        html.append("            </p>\n");
        
        // Yetenekler
        html.append("            <div class=\"skills\">\n");
        html.append("                <h4 style=\"color: #667eea; margin-bottom: 15px;\">🎯 Teknik Yetenekler</h4>\n");
        html.append("                <ul style=\"text-align: left; display: inline-block;\">\n");
        html.append("                    <li><strong>Java</strong> - Object Oriented Programming</li>\n");
        html.append("                    <li><strong>Socket Programming</strong> - Network Communication</li>\n");
        html.append("                    <li><strong>HTML/CSS</strong> - Web Frontend</li>\n");
        html.append("                    <li><strong>HTTP Protocol</strong> - Web Server Development</li>\n");
        html.append("                </ul>\n");
        html.append("            </div>\n");
        
        html.append("        </div>\n");
        
        // Bilgi kutusu
        html.append("        <div class=\"info-box\">\n");
        html.append("            <strong>🚀 Bu sayfa Java Socket Programlama ile oluşturulmuştur</strong><br>\n");
        html.append("            <small>Port: ").append(PORT).append(" | Protocol: HTTP/1.1 | ");
        html.append("Charset: UTF-8</small>\n");
        html.append("        </div>\n");
        
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>");
        
        return html.toString();
    }
}
