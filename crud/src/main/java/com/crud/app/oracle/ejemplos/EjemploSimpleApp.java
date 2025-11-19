package com.crud.app.oracle.ejemplos;

import com.crud.app.oracle.model.Direccion;
import com.crud.app.oracle.model.Empleado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class EjemploSimpleApp extends JFrame {

    private final EntityManagerFactory emf;

    private JTextField txtNombre, txtCalle, txtNumero, txtCiudad, txtCP, txtSalario;
    private JTextArea outputArea;

    public EjemploSimpleApp() {
        emf = Persistence.createEntityManagerFactory("oracle-pu");

        setTitle("Oracle - Tipos de Objeto (Dirección + Empleado)");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        JLabel infoLabel = new JLabel("<html><b>📄 Archivo SQL:</b> ejemplo.sql | " +
                "<b>Concepto:</b> Tipos de Objeto Incrustados (tipo_direccion)</html>");
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

        cargarEmpleados();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nuevo Empleado"));
        panel.setPreferredSize(new Dimension(320, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField(15);
        txtCalle = new JTextField(15);
        txtNumero = new JTextField(15);
        txtCiudad = new JTextField(15);
        txtCP = new JTextField(15);
        txtSalario = new JTextField(15);

        addField(panel, gbc, "Nombre:", txtNombre, 0);
        addField(panel, gbc, "Calle:", txtCalle, 1);
        addField(panel, gbc, "Número:", txtNumero, 2);
        addField(panel, gbc, "Ciudad:", txtCiudad, 3);
        addField(panel, gbc, "Código Postal:", txtCP, 4);
        addField(panel, gbc, "Salario (€):", txtSalario, 5);

        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton btnGuardar = new JButton("💾 Guardar Empleado");
        JButton btnRefrescar = new JButton("🔄 Refrescar");
        JButton btnLimpiar = new JButton("🗑️ Limpiar");

        btnGuardar.addActionListener(e -> guardarEmpleado());
        btnRefrescar.addActionListener(e -> cargarEmpleados());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panel.add(btnGuardar);
        panel.add(btnRefrescar);
        panel.add(btnLimpiar);

        return panel;
    }

    private void guardarEmpleado() {
        try {
            String nombre = txtNombre.getText().trim();
            String calle = txtCalle.getText().trim();
            Integer numero = Integer.parseInt(txtNumero.getText().trim());
            String ciudad = txtCiudad.getText().trim();
            String codigoPostal = txtCP.getText().trim();
            Double salario = Double.parseDouble(txtSalario.getText().trim());

            if (nombre.isEmpty() || calle.isEmpty() || ciudad.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Complete todos los campos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Direccion direccion = new Direccion(calle, numero, ciudad, codigoPostal);

            EntityManager em = emf.createEntityManager();
            Long nuevoId = 1L;
            try {
                // CONSULTA CORREGIDA ↓
                Object maxId = em.createQuery("SELECT MAX(e.idEmpleado) FROM EmpleadoOracle e").getSingleResult();
                if (maxId != null) {
                    nuevoId = ((Number) maxId).longValue() + 1;
                }
            } catch (Exception ex) {
                // Primera inserción
            }

            Empleado empleado = new Empleado(nuevoId, nombre, direccion, salario);

            try {
                em.getTransaction().begin();
                em.persist(empleado);
                em.getTransaction().commit();

                JOptionPane.showMessageDialog(this,
                        "✓ Empleado guardado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarEmpleados();

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
                    "Número o Salario inválido",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void cargarEmpleados() {
        EntityManager em = emf.createEntityManager();
        try {
            // CONSULTA CORREGIDA ↓
            List<Empleado> empleados = em.createQuery(
                    "SELECT e FROM EmpleadoOracle e ORDER BY e.idEmpleado",
                    Empleado.class
            ).getResultList();

            StringBuilder sb = new StringBuilder();
            sb.append("╔══════════════════════════════════════════════════════════════════════════════╗\n");
            sb.append("║         EMPLEADOS - TIPOS DE OBJETO ORACLE (ejemplo.sql)                    ║\n");
            sb.append("╠══════════════════════════════════════════════════════════════════════════════╣\n");
            sb.append("║ Concepto: Tipos de objeto con métodos (tipo_direccion.direccion_completa)   ║\n");
            sb.append("╚══════════════════════════════════════════════════════════════════════════════╝\n\n");

            if (empleados.isEmpty()) {
                sb.append("  ⚠️  No hay empleados registrados.\n\n");
                sb.append("  💡 Ejecuta el archivo SQL primero:\n");
                sb.append("     sql/oracle/ejemplo.sql\n\n");
                sb.append("  O agrega empleados usando el formulario.\n");
            } else {
                for (Empleado e : empleados) {
                    sb.append(String.format("  👤 ID: %d\n", e.getIdEmpleado()));
                    sb.append(String.format("     Nombre: %s\n", e.getNombre()));
                    sb.append(String.format("     📍 Dirección: %s\n", e.getDireccion().getDireccionCompleta()));
                    sb.append(String.format("     💰 Salario: €%.2f\n", e.getSalario()));
                    sb.append("  " + "─".repeat(74) + "\n");
                }
                sb.append(String.format("\n  Total de empleados: %d\n", empleados.size()));
            }

            outputArea.setText(sb.toString());

        } catch (Exception ex) {
            outputArea.setText("❌ Error al cargar datos:\n\n" + ex.getMessage() +
                    "\n\nAsegúrate de haber ejecutado: sql/oracle/ejemplo.sql");
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtCalle.setText("");
        txtNumero.setText("");
        txtCiudad.setText("");
        txtCP.setText("");
        txtSalario.setText("");
        txtNombre.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EjemploSimpleApp().setVisible(true);
        });
    }
}