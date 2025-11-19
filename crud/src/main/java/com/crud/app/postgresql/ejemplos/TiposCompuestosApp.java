package com.crud.app.postgresql.ejemplos;

import com.crud.app.postgresql.model.InfoProducto;
import com.crud.app.postgresql.model.ProductoCompuesto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.List;

public class TiposCompuestosApp extends JFrame {

    private final EntityManagerFactory emf;

    private JTextField txtCodigo, txtNombre, txtPrecio, txtStock, txtCategoria;
    private JTextArea outputArea;

    public TiposCompuestosApp() {
        emf = Persistence.createEntityManagerFactory("postgres-pu");

        setTitle("PostgreSQL - Tipos Compuestos (info_producto)");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        JLabel infoLabel = new JLabel("<html><b>📄 Archivo SQL:</b> tipos_compuestos.sql | " +
                "<b>Concepto:</b> CREATE TYPE info_producto + función precio_con_iva()</html>");
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

        cargarProductos();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nuevo Producto"));
        panel.setPreferredSize(new Dimension(320, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigo = new JTextField(15);
        txtNombre = new JTextField(15);
        txtPrecio = new JTextField(15);
        txtStock = new JTextField(15);
        txtCategoria = new JTextField(15);

        addField(panel, gbc, "Código:", txtCodigo, 0);
        addField(panel, gbc, "Nombre:", txtNombre, 1);
        addField(panel, gbc, "Precio ($):", txtPrecio, 2);
        addField(panel, gbc, "Stock:", txtStock, 3);
        addField(panel, gbc, "Categoría:", txtCategoria, 4);

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

        JButton btnGuardar = new JButton("💾 Guardar Producto");
        JButton btnRefrescar = new JButton("🔄 Refrescar");
        JButton btnLimpiar = new JButton("🗑️ Limpiar");

        btnGuardar.addActionListener(e -> guardarProducto());
        btnRefrescar.addActionListener(e -> cargarProductos());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panel.add(btnGuardar);
        panel.add(btnRefrescar);
        panel.add(btnLimpiar);

        return panel;
    }

    private void guardarProducto() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());
            Integer stock = Integer.parseInt(txtStock.getText().trim());
            String categoria = txtCategoria.getText().trim();

            if (codigo.isEmpty() || nombre.isEmpty() || categoria.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Complete todos los campos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            InfoProducto info = new InfoProducto(codigo, nombre, precio, stock);
            ProductoCompuesto producto = new ProductoCompuesto(info, categoria);

            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                em.persist(producto);
                em.getTransaction().commit();

                JOptionPane.showMessageDialog(this,
                        "✓ Producto guardado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarProductos();

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
                    "Precio o Stock inválido",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void cargarProductos() {
        EntityManager em = emf.createEntityManager();
        try {
            // CONSULTA CORREGIDA ↓
            List<ProductoCompuesto> productos = em.createQuery(
                    "SELECT p FROM ProductoPostgres p ORDER BY p.producto.precio DESC",
                    ProductoCompuesto.class
            ).getResultList();

            StringBuilder sb = new StringBuilder();
            sb.append("╔══════════════════════════════════════════════════════════════════════════════╗\n");
            sb.append("║         PRODUCTOS - TIPOS COMPUESTOS (tipos_compuestos.sql)                 ║\n");
            sb.append("╠══════════════════════════════════════════════════════════════════════════════╣\n");
            sb.append("║ Concepto: CREATE TYPE info_producto AS (codigo, nombre, precio, stock)      ║\n");
            sb.append("║ Función: precio_con_iva(precio_base) RETURNS NUMERIC                        ║\n");
            sb.append("╚══════════════════════════════════════════════════════════════════════════════╝\n\n");

            if (productos.isEmpty()) {
                sb.append("  ⚠️  No hay productos registrados.\n\n");
                sb.append("  💡 Ejecuta el archivo SQL primero:\n");
                sb.append("     sql/postgresql/tipos_compuestos.sql\n\n");
                sb.append("  O agrega productos usando el formulario.\n");
            } else {
                for (ProductoCompuesto p : productos) {
                    InfoProducto info = p.getProducto();
                    sb.append(String.format("  📦 [%s] %s - %s\n",
                            p.getCategoria(), info.getCodigo(), info.getNombre()));
                    sb.append(String.format("     Stock: %d unidades\n", info.getStock()));
                    sb.append(String.format("     💵 Precio base: $%.2f\n", info.getPrecio()));
                    sb.append(String.format("     💰 Precio con IVA (21%%): $%.2f\n", info.getPrecioConIva()));
                    sb.append(String.format("     Estado: %s\n", p.getActivo() ? "✓ Activo" : "✗ Inactivo"));
                    sb.append("  " + "─".repeat(74) + "\n");
                }
                sb.append(String.format("\n  Total de productos: %d\n", productos.size()));
            }

            outputArea.setText(sb.toString());

        } catch (Exception ex) {
            outputArea.setText("❌ Error al cargar datos:\n\n" + ex.getMessage() +
                    "\n\nAsegúrate de haber ejecutado: sql/postgresql/tipos_compuestos.sql");
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtCategoria.setText("");
        txtCodigo.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TiposCompuestosApp().setVisible(true);
        });
    }
}