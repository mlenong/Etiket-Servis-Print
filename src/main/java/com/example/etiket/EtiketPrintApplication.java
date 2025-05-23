package com.example.etiket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.net.ServerSocket;

@SpringBootApplication
public class EtiketPrintApplication {

    private static final int SINGLE_INSTANCE_PORT = 8081;

    public static void main(String[] args) {

        // Cek apakah aplikasi sudah jalan
        try {
            new ServerSocket(SINGLE_INSTANCE_PORT); 
        } catch (IOException e) {
            // Port sudah digunakan: aplikasi sudah jalan
            JOptionPane.showMessageDialog(null, "Aplikasi sudah berjalan!", "Info", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        SpringApplication.run(EtiketPrintApplication.class, args);
    }
}
