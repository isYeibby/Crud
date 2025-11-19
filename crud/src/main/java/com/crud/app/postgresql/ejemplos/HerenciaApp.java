package com.crud.app.postgresql.ejemplos;

import com.crud.app.postgresql.model.Persona;
import com.crud.app.postgresql.model.Estudiante;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.List;

public class HerenciaApp extends JFrame {

    private final EntityManagerFactory emf;

    private JTabbedPane tabbedPane;
    private JTextArea outputArea;

    private JTextField txtNombrePersona, txtEdadPersona;

    private JTextField txtNombreEst, txtEdadEst, txtMatricula, txtCarrera, txtPromedio;

    public HerenciaApp() {
        emf = Persistence.createEntityManagerFactory("postgres-pu");

        setTitle("PostgreSQL - Herencia de Tablas (INHERITS)");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        JLabel infoLabel = new JLabel("<html><b>📄 Archivo SQL:</b> herencia_tablas.sql | " +
                "<b>Concepto:</b> CREATE TABLE estudiantes INHERITS (personas)</html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(infoLabel, BorderLayout.CENTER);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("👤 Personas", createPersonaPanel());
        tabbedPane.addTab("🎓 Estudiantes", createEstudiantePanel());

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

    private JPanel createPersonaPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nueva Persona"));
        panel.setPreferredSize(new Dimension(350, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombrePersona = new JTextField(20);
        txtEdadPersona = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtNombrePersona, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Edad:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtEdadPersona, gbc);

        JButton btnGuardar = new JButton("💾 Guardar Persona");
        btnGuardar.addActionListener(e -> guardarPersona());
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(btnGuardar, gbc);

        return panel;
    }

    private JPanel createEstudiantePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nuevo Estudiante"));
        panel.setPreferredSize(new Dimension(350, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombreEst = new JTextField(20);
        txtEdadEst = new JTextField(20);
        txtMatricula = new JTextField(20);
        txtCarrera = new JTextField(20);
        txtPromedio = new JTextField(20);

        addField(panel, gbc, "Nombre:", txtNombreEst, 0);
        addField(panel, gbc, "Edad:", txtEdadEst, 1);
        addField(panel, gbc, "Matrícula:", txtMatricula, 2);
        addField(panel, gbc, "Carrera:", txtCarrera, 3);
        addField(panel, gbc, "Promedio (0-10):", txtPromedio, 4);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JLabel noteLabel = new JLabel("<html><i>💡 Beca si promedio ≥ 8.5</i></html>");
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        noteLabel.setForeground(new Color(41, 128, 185));
        panel.add(noteLabel, gbc);

        JButton btnGuardar = new JButton("💾 Guardar Estudiante");
        btnGuardar.addActionListener(e -> guardarEstudiante());
        gbc.gridy = 6;
        panel.add(btnGuardar, gbc);

        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton btnRefrescar = new JButton("🔄 Refrescar");
        JButton btnLimpiar = new JButton("🗑️ Limpiar");
        JButton btnEstadisticas = new JButton("📊 Estadísticas");

        btnRefrescar.addActionListener(e -> cargarDatos());
        btnLimpiar.addActionListener(e -> limpiarFormularios());
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());

        panel.add(btnRefrescar);
        panel.add(btnLimpiar);
        panel.add(btnEstadisticas);

        return panel;
    }

    private void guardarPersona() {
        try {
            String nombre = txtNombrePersona.getText().trim();
            Integer edad = Integer.parseInt(txtEdadPersona.getText().trim());

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Persona persona = new Persona(nombre, edad);

            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                em.persist(persona);
                em.getTransaction().commit();

                JOptionPane.showMessageDialog(this, "✓ Persona guardada",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormularios();
                cargarDatos();

            } finally {
                em.close();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Edad inválida",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void guardarEstudiante() {
        try {
            String nombre = txtNombreEst.getText().trim();
            Integer edad = Integer.parseInt(txtEdadEst.getText().trim());
            String matricula = txtMatricula.getText().trim();
            String carrera = txtCarrera.getText().trim();
            BigDecimal promedio = new BigDecimal(txtPromedio.getText().trim());

            if (nombre.isEmpty() || matricula.isEmpty() || carrera.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (promedio.compareTo(BigDecimal.ZERO) < 0 || promedio.compareTo(BigDecimal.TEN) > 0) {
                JOptionPane.showMessageDialog(this, "El promedio debe estar entre 0 y 10",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Estudiante estudiante = new Estudiante(nombre, edad, matricula, carrera, promedio);

            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                em.persist(estudiante);
                em.getTransaction().commit();

                String mensaje = "✓ Estudiante guardado exitosamente";
                if (estudiante.getEstadoBeca().equals("Con Beca")) {
                    mensaje += "\n🎓 ¡Felicidades! Califica para beca (promedio ≥ 8.5)";
                }

                JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormularios();
                cargarDatos();

            } finally {
                em.close();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Edad o Promedio inválido",
                    "Error", JOptionPane.ERROR_MESSAGE);
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
            List<Persona> personas = em.createQuery(
                    "SELECT p FROM PersonaPostgres p WHERE TYPE(p) = PersonaPostgres",
                    Persona.class
            ).getResultList();

            sb.append("╔══════════════════════════════════════════════════════════════════════════════╗\n");
            sb.append("║         PERSONAS (herencia_tablas.sql)                                       ║\n");
            sb.append("╠══════════════════════════════════════════════════════════════════════════════╣\n");
            sb.append("║ Concepto: Tabla base (personas)                                             ║\n");
            sb.append("║ Consulta SQL: SELECT * FROM ONLY personas                                   ║\n");
            sb.append("╚══════════════════════════════════════════════════════════════════════════════╝\n\n");

            if (personas.isEmpty()) {
                sb.append("  ⚠️  No hay personas registradas (solo en tabla base).\n");
            } else {
                for (Persona p : personas) {
                    sb.append(String.format("  👤 %s\n", p.toString()));
                    sb.append("  " + "─".repeat(74) + "\n");
                }
            }

            // CONSULTA CORREGIDA ↓
            List<Estudiante> estudiantes = em.createQuery(
                    "SELECT e FROM EstudiantePostgres e ORDER BY e.promedio DESC",
                    Estudiante.class
            ).getResultList();

            sb.append("\n╔══════════════════════════════════════════════════════════════════════════════╗\n");
            sb.append("║         ESTUDIANTES (INHERITS personas)                                      ║\n");
            sb.append("╠══════════════════════════════════════════════════════════════════════════════╣\n");
            sb.append("║ Concepto: CREATE TABLE estudiantes (...) INHERITS (personas)                ║\n");
            sb.append("║ Hereda: id, nombre, edad                                                    ║\n");
            sb.append("║ Agrega: matricula, carrera, promedio                                        ║\n");
            sb.append("╚══════════════════════════════════════════════════════════════════════════════╝\n\n");

            if (estudiantes.isEmpty()) {
                sb.append("  ⚠️  No hay estudiantes registrados.\n\n");
                sb.append("  💡 Ejecuta el archivo SQL:\n");
                sb.append("     sql/postgresql/herencia_tablas.sql\n");
            } else {
                int conBeca = 0;
                for (Estudiante e : estudiantes) {
                    String icono = e.getEstadoBeca().equals("Con Beca") ? "🎓" : "📚";
                    if (e.getEstadoBeca().equals("Con Beca")) conBeca++;

                    sb.append(String.format("  %s %s\n", icono, e.toString()));
                    sb.append("  " + "─".repeat(74) + "\n");
                }
                sb.append(String.format("\n  📊 Total: %d estudiantes | Con beca: %d (%.1f%%)\n",
                        estudiantes.size(), conBeca, (conBeca * 100.0 / estudiantes.size())));
            }

            outputArea.setText(sb.toString());

        } catch (Exception ex) {
            outputArea.setText("❌ Error al cargar datos:\n\n" + ex.getMessage() +
                    "\n\nAsegúrate de haber ejecutado: sql/postgresql/herencia_tablas.sql");
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void mostrarEstadisticas() {
        EntityManager em = emf.createEntityManager();
        try {
            // CONSULTA CORREGIDA ↓
            List<Estudiante> estudiantes = em.createQuery(
                    "SELECT e FROM EstudiantePostgres e",
                    Estudiante.class
            ).getResultList();

            if (estudiantes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay estudiantes para analizar",
                        "Sin datos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            BigDecimal suma = BigDecimal.ZERO;
            BigDecimal max = BigDecimal.ZERO;
            BigDecimal min = BigDecimal.TEN;
            int conBeca = 0;

            for (Estudiante e : estudiantes) {
                suma = suma.add(e.getPromedio());
                if (e.getPromedio().compareTo(max) > 0) max = e.getPromedio();
                if (e.getPromedio().compareTo(min) < 0) min = e.getPromedio();
                if (e.getEstadoBeca().equals("Con Beca")) conBeca++;
            }

            BigDecimal promedio = suma.divide(new BigDecimal(estudiantes.size()), 2, BigDecimal.ROUND_HALF_UP);

            String mensaje = String.format(
                    "📊 ESTADÍSTICAS DE ESTUDIANTES\n\n" +
                            "Total de estudiantes: %d\n" +
                            "Promedio general: %.2f\n" +
                            "Promedio más alto: %.2f\n" +
                            "Promedio más bajo: %.2f\n" +
                            "Estudiantes con beca: %d (%.1f%%)",
                    estudiantes.size(),
                    promedio,
                    max,
                    min,
                    conBeca,
                    (conBeca * 100.0 / estudiantes.size())
            );

            JOptionPane.showMessageDialog(this, mensaje,
                    "Estadísticas", JOptionPane.INFORMATION_MESSAGE);

        } finally {
            em.close();
        }
    }

    private void limpiarFormularios() {
        txtNombrePersona.setText("");
        txtEdadPersona.setText("");
        txtNombreEst.setText("");
        txtEdadEst.setText("");
        txtMatricula.setText("");
        txtCarrera.setText("");
        txtPromedio.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HerenciaApp().setVisible(true);
        });
    }
}