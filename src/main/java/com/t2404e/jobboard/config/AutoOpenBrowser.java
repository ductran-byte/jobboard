package com.t2404e.jobboard.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URI;

@Component
public class AutoOpenBrowser {

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        try {
            String url = "http://localhost:8080/home";
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
                System.out.println("🌐 Browser opened: " + url);
            } else {
                System.out.println("⚠️ Desktop browsing not supported. Please open manually: " + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
