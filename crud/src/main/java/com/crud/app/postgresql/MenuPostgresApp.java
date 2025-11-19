package com.crud.app.postgresql;

import com.crud.app.postgresql.ejemplos.*;

import javax.swing.*;
import java.awt.*;

/**
 * Menú Principal para Ejemplos de PostgreSQL
 *
 * Ejemplos basados en los archivos SQL:
 * - ejemplo.sql → Tipos compuestos
 * - tipos_compuestos.sql → info_producto con funciones
 * - arrays_json.sql → Arrays (TEXT[], INTEGER[]) y JSONB
 * - herencia_tablas.sql → INHERITS (Persona → Estudiante)
 *
 * @author isYeibby
 * @version 1.0
 */
public class MenuPostgresApp extends JFrame {

    public MenuPostgresApp() {
        setTitle("PostgreSQL - Características Avanzadas | BD Avanzadas");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));

        // Color scheme (diferente a Oracle)
        Color bgColor = new Color(236, 240, 241);
        Color primaryColor = new Color(41, 128, 185);
        Color accentColor = new Color(142, 68, 173);

        getContentPane().setBackground(bgColor);

        // Panel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Título
        JLabel titleLabel = new JLabel("Características Avanzadas de PostgreSQL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        mainPanel.add(titleLabel, gbc);

        // Espacio
        gbc.gridy = 2;
        mainPanel.add(Box.createVerticalStrut(20), gbc);

        // Botones para cada ejemplo
        JButton btn1 = createButton("📋 Ejemplo Simple",
                "Tipos Compuestos (CREATE TYPE direccion)",
                primaryColor);
        JButton btn2 = createButton("📦 Tipos Compuestos",
                "info_producto con funciones (precio_con_iva)",
                primaryColor);
        JButton btn3 = createButton("🗂️ Arrays y JSONB",
                "Arrays (TEXT[], INTEGER[]) y JSONB",
                new Color(230, 126, 34));
        JButton btn4 = createButton("🎓 Herencia de Tablas",
                "INHERITS (Persona → Estudiante)",
                accentColor);

        // Agregar listeners
        btn1.addActionListener(e -> abrirEjemploSimple());
        btn2.addActionListener(e -> abrirTiposCompuestos());
        btn3.addActionListener(e -> abrirArraysJson());
        btn4.addActionListener(e -> abrirHerencia());

        // Agregar botones al panel
        gbc.gridy = 3;
        mainPanel.add(btn1, gbc);
        gbc.gridy = 4;
        mainPanel.add(btn2, gbc);
        gbc.gridy = 5;
        mainPanel.add(btn3, gbc);
        gbc.gridy = 6;
        mainPanel.add(btn4, gbc);

        // Espacio
        gbc.gridy = 7;
        mainPanel.add(Box.createVerticalStrut(10), gbc);

        // Botón de salir
        JButton btnSalir = new JButton("❌ Salir");
        btnSalir.setBackground(new Color(231, 76, 60));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setPreferredSize(new Dimension(0, 45));
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> System.exit(0));

        // Hover effect para botón salir
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalir.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalir.setBackground(new Color(231, 76, 60));
            }
        });

        gbc.gridy = 8;
        mainPanel.add(btnSalir, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createButton(String text, String tooltip, Color bgColor) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(500, 55));
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Efecto hover
        Color hoverColor = bgColor.darker();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void abrirEjemploSimple() {
        SwingUtilities.invokeLater(() -> {
            EjemploSimpleApp app = new EjemploSimpleApp();
            app.setVisible(true);
        });
    }

    private void abrirTiposCompuestos() {
        SwingUtilities.invokeLater(() -> {
            TiposCompuestosApp app = new TiposCompuestosApp();
            app.setVisible(true);
        });
    }

    private void abrirArraysJson() {
        SwingUtilities.invokeLater(() -> {
            ArraysJsonApp app = new ArraysJsonApp();
            app.setVisible(true);
        });
    }

    private void abrirHerencia() {
        SwingUtilities.invokeLater(() -> {
            HerenciaApp app = new HerenciaApp();
            app.setVisible(true);
        });
    }

    public static void main(String[] args) {
        // Configurar Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MenuPostgresApp menu = new MenuPostgresApp();
            menu.setVisible(true);
        });
    }
}