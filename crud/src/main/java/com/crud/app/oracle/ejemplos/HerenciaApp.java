package com.crud.app.oracle.ejemplos;

import com.crud.app.oracle.model.Estudiante;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class HerenciaApp extends JFrame {

    private final EntityManagerFactory emf;

    private JTextField txtNombre, txtEdad, txtMatricula, txtCarrera, txtPromedio;
    private JTextArea outputArea;

    public HerenciaApp() {
        emf = Persistence.createEntityManagerFactory("oracle-pu");

        setTitle("Oracle - Herencia de Tipos (Persona → Estudiante)");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        JLabel infoLabel = new JLabel("<html><b>📄 Archivo SQL:</b> tipos_con_herencia.sql | " +
                "<b>Concepto:</b> Herencia (tipo_estudiante UNDER tipo_persona)</html>");
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

        cargarEstudiantes();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nuevo Estudiante"));
        panel.setPreferredSize(new Dimension(350, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField(15);
        txtEdad = new JTextField(15);
        txtMatricula = new JTextField(15);
        txtCarrera = new JTextField(15);
        txtPromedio = new JTextField(15);

        addField(panel, gbc, "Nombre:", txtNombre, 0);
        addField(panel, gbc, "Edad:", txtEdad, 1);
        addField(panel, gbc, "Matrícula:", txtMatricula, 2);
        addField(panel, gbc, "Carrera:", txtCarrera, 3);
        addField(panel, gbc, "Promedio (0-10):", txtPromedio, 4);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JLabel noteLabel = new JLabel("<html><i>💡 Beca automática si promedio ≥ 8.5</i></html>");
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        noteLabel.setForeground(new Color(41, 128, 185));
        panel.add(noteLabel, gbc);

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

        JButton btnGuardar = new JButton("💾 Guardar Estudiante");
        JButton btnRefrescar = new JButton("🔄 Refrescar");
        JButton btnLimpiar = new JButton("🗑️ Limpiar");
        JButton btnEstadisticas = new JButton("📊 Estadísticas");

        btnGuardar.addActionListener(e -> guardarEstudiante());
        btnRefrescar.addActionListener(e -> cargarEstudiantes());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());

        panel.add(btnGuardar);
        panel.add(btnRefrescar);
        panel.add(btnLimpiar);
        panel.add(btnEstadisticas);

        return panel;
    }

    private void guardarEstudiante() {
        try {
            String nombre = txtNombre.getText().trim();
            Integer edad = Integer.parseInt(txtEdad.getText().trim());
            String matricula = txtMatricula.getText().trim();
            String carrera = txtCarrera.getText().trim();
            Double promedio = Double.parseDouble(txtPromedio.getText().trim());

            if (nombre.isEmpty() || matricula.isEmpty() || carrera.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Complete todos los campos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (promedio < 0 || promedio > 10) {
                JOptionPane.showMessageDialog(this,
                        "El promedio debe estar entre 0 y 10",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            EntityManager em = emf.createEntityManager();
            Long nuevoId = 1L;
            try {
                // CONSULTA CORREGIDA ↓
                Object maxId = em.createQuery("SELECT MAX(e.id) FROM EstudianteOracle e").getSingleResult();
                if (maxId != null) {
                    nuevoId = ((Number) maxId).longValue() + 1;
                }
            } catch (Exception ex) {
                // Primera inserción
            }

            Estudiante estudiante = new Estudiante(nuevoId, nombre, edad, matricula, carrera, promedio);

            try {
                em.getTransaction().begin();
                em.persist(estudiante);
                em.getTransaction().commit();

                String mensaje = "✓ Estudiante guardado exitosamente";
                if (estudiante.tieneBeca().equals("SÍ")) {
                    mensaje += "\n🎓 ¡Felicidades! Califica para beca (promedio ≥ 8.5)";
                }

                JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarEstudiantes();

            } catch (Exception ex) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw ex;
            } finally {
                em.close();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Edad o Promedio inválido",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void cargarEstudiantes() {
        EntityManager em = emf.createEntityManager();
        try {
            // CONSULTA CORREGIDA ↓
            List<Estudiante> estudiantes = em.createQuery(
                    "SELECT e FROM EstudianteOracle e ORDER BY e.promedio DESC",
                    Estudiante.class
            ).getResultList();

            StringBuilder sb = new StringBuilder();
            sb.append("╔══════════════════════════════════════════════════════════════════════════════╗\n");
            sb.append("║         ESTUDIANTES - HERENCIA DE TIPOS (tipos_con_herencia.sql)            ║\n");
            sb.append("╠══════════════════════════════════════════════════════════════════════════════╣\n");
            sb.append("║ Concepto: tipo_estudiante UNDER tipo_persona                                ║\n");
            sb.append("║ • Hereda: nombre (de tipo_persona)                                          ║\n");
            sb.append("║ • Agrega: edad, matricula, carrera, promedio                                ║\n");
            sb.append("║ • Sobrescribe: presentarse() [OVERRIDING]                                   ║\n");
            sb.append("║ • Método nuevo: tiene_beca()                                                ║\n");
            sb.append("╚══════════════════════════════════════════════════════════════════════════════╝\n\n");

            if (estudiantes.isEmpty()) {
                sb.append("  ⚠️  No hay estudiantes registrados.\n\n");
                sb.append("  💡 Ejecuta el archivo SQL primero:\n");
                sb.append("     sql/oracle/tipos_con_herencia.sql\n\n");
                sb.append("  O agrega estudiantes usando el formulario.\n");
            } else {
                int conBeca = 0;
                for (Estudiante e : estudiantes) {
                    String icono = e.tieneBeca().equals("SÍ") ? "🎓" : "📚";
                    if (e.tieneBeca().equals("SÍ")) conBeca++;

                    sb.append(String.format("  %s %s\n", icono, e.presentarse()));
                    sb.append(String.format("     Edad: %d años | Beca: %s\n", e.getEdad(), e.tieneBeca()));
                    sb.append("  " + "─".repeat(74) + "\n");
                }
                sb.append(String.format("\n  📊 Total: %d estudiantes | Con beca: %d (%.1f%%)\n",
                        estudiantes.size(), conBeca, (conBeca * 100.0 / estudiantes.size())));
            }

            outputArea.setText(sb.toString());

        } catch (Exception ex) {
            outputArea.setText("❌ Error al cargar datos:\n\n" + ex.getMessage() +
                    "\n\nAsegúrate de haber ejecutado: sql/oracle/tipos_con_herencia.sql");
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
                    "SELECT e FROM EstudianteOracle e",
                    Estudiante.class
            ).getResultList();

            if (estudiantes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay estudiantes para analizar",
                        "Sin datos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            double sumaPromedios = 0;
            double maxPromedio = 0;
            double minPromedio = 10;
            int conBeca = 0;

            for (Estudiante e : estudiantes) {
                sumaPromedios += e.getPromedio();
                if (e.getPromedio() > maxPromedio) maxPromedio = e.getPromedio();
                if (e.getPromedio() < minPromedio) minPromedio = e.getPromedio();
                if (e.tieneBeca().equals("SÍ")) conBeca++;
            }

            double promedioGeneral = sumaPromedios / estudiantes.size();

            String mensaje = String.format(
                    "📊 ESTADÍSTICAS DE ESTUDIANTES\n\n" +
                            "Total de estudiantes: %d\n" +
                            "Promedio general: %.2f\n" +
                            "Promedio más alto: %.2f\n" +
                            "Promedio más bajo: %.2f\n" +
                            "Estudiantes con beca: %d (%.1f%%)",
                    estudiantes.size(),
                    promedioGeneral,
                    maxPromedio,
                    minPromedio,
                    conBeca,
                    (conBeca * 100.0 / estudiantes.size())
            );

            JOptionPane.showMessageDialog(this, mensaje,
                    "Estadísticas", JOptionPane.INFORMATION_MESSAGE);

        } finally {
            em.close();
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtEdad.setText("");
        txtMatricula.setText("");
        txtCarrera.setText("");
        txtPromedio.setText("");
        txtNombre.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HerenciaApp().setVisible(true);
        });
    }
}