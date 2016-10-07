package com.zetcode;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class DrawCircleInGUI extends JFrame {

    public DrawCircleInGUI() {

        initUI();
    }

    private void initUI() {
        
        setTitle("Simple example");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            DrawCircleInGUI ex = new DrawCircleInGUI();
            ex.setVisible(true);
        });
    }
}