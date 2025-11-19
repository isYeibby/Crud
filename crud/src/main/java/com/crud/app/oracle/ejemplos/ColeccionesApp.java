package com.crud.app.oracle.ejemplos;

import com.crud.app.oracle.model.Persona;
import com.crud.app.oracle.model.Telefono;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class ColeccionesApp extends JFrame {

    private final EntityManagerFactory emf;

    private JTextField txtNombre, txtEmail;
    private JTextArea txtTelefonos;
    private JTextArea outputArea;

    public ColeccionesApp() {
        emf = Persistence.createEntityManagerFactory("oracle-pu");

        setTitle("Oracle - Colecciones (Nested Tables)");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        JLabel infoLabel = new JLabel("<html><b>📄 Archivo SQL:</b> colecciones.sql | " +
                "<b>Concepto:</b> Nested Tables (lista_telefonos)</html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(infoLabel, BorderLayout.CENTER);

        JPanel inputPanel = createInputPanel();

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(outputArea);

        JPanel buttonPanel = createButtonPanel();

        add(infoPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                emf.close();
            }
        });

        cargarPersonas();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nueva Persona"));
        panel.setPreferredSize(new Dimension(350, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        txtNombre = new JTextField(15);
        txtEmail = new JTextField(15);
        txtTelefonos = new JTextArea(5, 15);
        txtTelefonos.setLineWrap(true);
        JScrollPane scrollTel = new JScrollPane(txtTelefonos);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.weighty = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.weighty = 1;
        panel.add(new JLabel("<html>Teléfonos:<br>(uno por línea)</html>"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(scrollTel, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weighty = 0;
        JLabel helpLabel = new JLabel("<html><i>Formato: Tipo,Número<br>Ej: Móvil,612345678</i></html>");
        helpLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        panel.add(helpLabel, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton btnGuardar = new JButton("💾 Guardar Persona");
        JButton btnRefrescar = new JButton("🔄 Refrescar");
        JButton btnLimpiar = new JButton("🗑️ Limpiar");

        btnGuardar.addActionListener(e -> guardarPersona());
        btnRefrescar.addActionListener(e -> cargarPersonas());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panel.add(btnGuardar);
        panel.add(btnRefrescar);
        panel.add(btnLimpiar);

        return panel;
    }

    private void guardarPersona() {
        try {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            String telefonosTexto = txtTelefonos.getText().trim();

            if (nombre.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nombre y Email son obligatorios",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            EntityManager em = emf.createEntityManager();
            Long nuevoId = 1L;
            try {
                // CONSULTA CORREGIDA ↓
                Object maxId = em.createQuery("SELECT MAX(p.id) FROM PersonaOracle p").getSingleResult();
                if (maxId != null) {
                    nuevoId = ((Number) maxId).longValue() + 1;
                }
            } catch (Exception ex) {
                // Primera inserción
            }

            Persona persona = new Persona(nuevoId, nombre, email);

            if (!telefonosTexto.isEmpty()) {
                String[] lineas = telefonosTexto.split("\n");
                for (String linea : lineas) {
                    String[] partes = linea.split(",");
                    if (partes.length == 2) {
                        persona.agregarTelefono(new Telefono(partes[0].trim(), partes[1].trim()));
                    }
                }
            }

            try {
                em.getTransaction().begin();
                em.persist(persona);
                em.getTransaction().commit();

                JOptionPane.showMessageDialog(this,
                        "✓ Persona guardada con " + persona.getTelefonos().size() + " teléfono(s)",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarPersonas();

            } catch (Exception ex) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw ex;
            } finally {
                em.close();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void cargarPersonas() {
        EntityManager em = emf.createEntityManager();
        try {
            // CONSULTA CORREGIDA ↓
            List<Persona> personas = em.createQuery(
                    "SELECT p FROM PersonaOracle p ORDER BY p.id",
                    Persona.class
            ).getResultList();

            StringBuilder sb = new StringBuilder();
            sb.append("╔══════════════════════════════════════════════════════════════════════════════╗\n");
            sb.append("║         PERSONAS - NESTED TABLES (colecciones.sql)                          ║\n");
            sb.append("╠══════════════════════════════════════════════════════════════════════════════╣\n");
            sb.append("║ Concepto: Colecciones anidadas (lista_telefonos AS TABLE OF tipo_telefono)  ║\n");
            sb.append("╚══════════════════════════════════════════════════════════════════════════════╝\n\n");

            if (personas.isEmpty()) {
                sb.append("  ⚠️  No hay personas registradas.\n\n");
                sb.append("  💡 Ejecuta el archivo SQL primero:\n");
                sb.append("     sql/oracle/colecciones.sql\n\n");
                sb.append("  O agrega personas usando el formulario.\n");
            } else {
                for (Persona p : personas) {
                    sb.append(String.format("  👤 ID: %d | %s\n", p.getId(), p.getNombre()));
                    sb.append(String.format("     📧 Email: %s\n", p.getEmail()));
                    sb.append(String.format("     📞 Teléfonos (%d):\n", p.getTelefonos().size()));

                    if (p.getTelefonos().isEmpty()) {
                        sb.append("        (sin teléfonos)\n");
                    } else {
                        for (Telefono tel : p.getTelefonos()) {
                            sb.append(String.format("        • %s\n", tel.toString()));
                        }
                    }
                    sb.append("  " + "─".repeat(74) + "\n");
                }
                sb.append(String.format("\n  Total de personas: %d\n", personas.size()));
            }

            outputArea.setText(sb.toString());

        } catch (Exception ex) {
            outputArea.setText("❌ Error al cargar datos:\n\n" + ex.getMessage() +
                    "\n\nAsegúrate de haber ejecutado: sql/oracle/colecciones.sql");
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtEmail.setText("");
        txtTelefonos.setText("");
        txtNombre.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ColeccionesApp().setVisible(true);
        });
    }
}