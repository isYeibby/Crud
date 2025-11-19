package com.crud.app.postgresql.ejemplos;

import com.crud.app.postgresql.model.Proyecto;
import com.crud.app.postgresql.model.ProductoTech;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArraysJsonApp extends JFrame {

    private final EntityManagerFactory emf;

    private JTabbedPane tabbedPane;
    private JTextArea outputArea;

    private JTextField txtNombreProyecto;
    private JTextArea txtTecnologias, txtMiembros;

    private JTextField txtNombreProducto, txtMarca, txtRam;
    private JTextArea txtJsonCompleto;

    public ArraysJsonApp() {
        emf = Persistence.createEntityManagerFactory("postgres-pu");

        setTitle("PostgreSQL - Arrays y JSONB");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        JLabel infoLabel = new JLabel("<html><b>📄 Archivo SQL:</b> arrays_json.sql | " +
                "<b>Concepto:</b> Arrays (TEXT[], INTEGER[]) y JSONB</html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(infoLabel, BorderLayout.CENTER);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("📊 Arrays (Proyectos)", createArraysPanel());
        tabbedPane.addTab("📱 JSONB (Productos Tech)", createJsonbPanel());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(outputArea);

        JPanel buttonPanel = createButtonPanel();

        add(infoPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                emf.close();
            }
        });

        cargarDatos();
    }

    private JPanel createArraysPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nuevo Proyecto (Arrays)"));
        panel.setPreferredSize(new Dimension(400, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        txtNombreProyecto = new JTextField(20);
        txtTecnologias = new JTextArea(4, 20);
        txtTecnologias.setLineWrap(true);
        txtMiembros = new JTextArea(2, 20);
        txtMiembros.setLineWrap(true);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.weighty = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtNombreProyecto, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.weighty = 1;
        panel.add(new JLabel("<html>Tecnologías:<br>(una por línea)</html>"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(new JScrollPane(txtTecnologias), gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.weighty = 0.5;
        panel.add(new JLabel("<html>IDs Miembros:<br>(uno por línea)</html>"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(new JScrollPane(txtMiembros), gbc);

        JButton btnGuardar = new JButton("💾 Guardar Proyecto");
        btnGuardar.addActionListener(e -> guardarProyecto());
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weighty = 0;
        panel.add(btnGuardar, gbc);

        return panel;
    }

    private JPanel createJsonbPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nuevo Producto Tech (JSONB)"));
        panel.setPreferredSize(new Dimension(400, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        txtNombreProducto = new JTextField(20);
        txtMarca = new JTextField(20);
        txtRam = new JTextField(20);
        txtJsonCompleto = new JTextArea(6, 20);
        txtJsonCompleto.setLineWrap(true);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.weighty = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtNombreProducto, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtMarca, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("RAM (GB):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtRam, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.weighty = 1;
        panel.add(new JLabel("<html>JSON Adicional:<br>(opcional)</html>"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(new JScrollPane(txtJsonCompleto), gbc);

        JButton btnGuardar = new JButton("💾 Guardar Producto");
        btnGuardar.addActionListener(e -> guardarProductoTech());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.weighty = 0;
        panel.add(btnGuardar, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton btnRefrescar = new JButton("🔄 Refrescar");
        JButton btnLimpiar = new JButton("🗑️ Limpiar");

        btnRefrescar.addActionListener(e -> cargarDatos());
        btnLimpiar.addActionListener(e -> limpiarFormularios());

        panel.add(btnRefrescar);
        panel.add(btnLimpiar);

        return panel;
    }

    private void guardarProyecto() {
        try {
            String nombre = txtNombreProyecto.getText().trim();
            String[] tecArray = txtTecnologias.getText().trim().split("\n");
            String[] miemArray = txtMiembros.getText().trim().split("\n");

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Integer[] miembros = new Integer[miemArray.length];
            for (int i = 0; i < miemArray.length; i++) {
                miembros[i] = Integer.parseInt(miemArray[i].trim());
            }

            Proyecto proyecto = new Proyecto(nombre, tecArray, miembros);

            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                em.persist(proyecto);
                em.getTransaction().commit();

                JOptionPane.showMessageDialog(this, "✓ Proyecto guardado",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormularios();
                cargarDatos();

            } finally {
                em.close();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void guardarProductoTech() {
        try {
            String nombre = txtNombreProducto.getText().trim();
            String marca = txtMarca.getText().trim();
            Integer ram = Integer.parseInt(txtRam.getText().trim());

            if (nombre.isEmpty() || marca.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Marca son obligatorios",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map<String, Object> specs = new HashMap<>();
            specs.put("marca", marca);
            specs.put("ram", ram);

            ProductoTech producto = new ProductoTech(nombre, specs);

            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                em.persist(producto);
                em.getTransaction().commit();

                JOptionPane.showMessageDialog(this, "✓ Producto guardado",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormularios();
                cargarDatos();

            } finally {
                em.close();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void cargarDatos() {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sb = new StringBuilder();

            // CONSULTA CORREGIDA ↓
            List<Proyecto> proyectos = em.createQuery(
                    "SELECT p FROM ProyectoPostgres p",
                    Proyecto.class
            ).getResultList();

            sb.append("╔══════════════════════════════════════════════════════════════════════════════╗\n");
            sb.append("║         PROYECTOS - ARRAYS (TEXT[], INTEGER[])                              ║\n");
            sb.append("╚══════════════════════════════════════════════════════════════════════════════╝\n\n");

            for (Proyecto p : proyectos) {
                sb.append(String.format("  📊 %s\n", p.getNombre()));
                sb.append(String.format("     Tecnologías (%d): %s\n",
                        p.getNumTecnologias(), p.getTecnologiasString()));
                sb.append(String.format("     Miembros: %d\n", p.getNumMiembros()));
                sb.append("  " + "─".repeat(74) + "\n");
            }

            // CONSULTA CORREGIDA ↓
            List<ProductoTech> productos = em.createQuery(
                    "SELECT p FROM ProductoTechPostgres p",
                    ProductoTech.class
            ).getResultList();

            sb.append("\n╔══════════════════════════════════════════════════════════════════════════════╗\n");
            sb.append("║         PRODUCTOS TECH - JSONB                                               ║\n");
            sb.append("╚══════════════════════════════════════════════════════════════════════════════╝\n\n");

            for (ProductoTech p : productos) {
                sb.append(String.format("  📱 %s\n", p.getNombre()));
                sb.append(String.format("     Marca: %s | RAM: %d GB\n", p.getMarca(), p.getRam()));
                sb.append(String.format("     JSON: %s\n", p.getEspecificaciones()));
                sb.append("  " + "─".repeat(74) + "\n");
            }

            outputArea.setText(sb.toString());

        } finally {
            em.close();
        }
    }

    private void limpiarFormularios() {
        txtNombreProyecto.setText("");
        txtTecnologias.setText("");
        txtMiembros.setText("");
        txtNombreProducto.setText("");
        txtMarca.setText("");
        txtRam.setText("");
        txtJsonCompleto.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ArraysJsonApp().setVisible(true);
        });
    }
}