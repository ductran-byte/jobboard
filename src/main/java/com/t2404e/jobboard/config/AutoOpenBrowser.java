package com.t2404e.jobboard.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

/**
 * 🔹 Khi ứng dụng Spring Boot khởi động xong,
 * lớp này sẽ tự động mở trình duyệt mặc định đến URL mong muốn.
 */
@Component
public class AutoOpenBrowser {

    private static final String APP_URL = "http://localhost:8080/home";

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        new Thread(() -> {
            try {
                Thread.sleep(1500); // ⏱ Chờ 1.5s cho server khởi động ổn định

                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(new URI(APP_URL));
                        System.out.println("🌐 Browser opened automatically: " + APP_URL);
                        return;
                    }
                }

                // ✅ Fallback cho các hệ thống không hỗ trợ Desktop API
                String os = System.getProperty("os.name").toLowerCase();
                Runtime runtime = Runtime.getRuntime();

                if (os.contains("win")) {
                    runtime.exec("rundll32 url.dll,FileProtocolHandler " + APP_URL);
                } else if (os.contains("mac")) {
                    runtime.exec("open " + APP_URL);
                } else if (os.contains("nix") || os.contains("nux")) {
                    runtime.exec("xdg-open " + APP_URL);
                } else {
                    System.out.println("⚠️ Unsupported OS. Please open manually: " + APP_URL);
                }

            } catch (IOException | InterruptedException e) {
                System.err.println("❌ Failed to open browser automatically.");
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start(); // 🚀 Chạy trong luồng riêng để không chặn Spring Boot
    }
}
