package  com.crud.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Aplicación Gráfica (GUI) que usa JPA/Hibernate.
 * NO CONTIENE CÓDIGO SQL.
 */
public class App extends JFrame implements ActionListener {

    // --- Componentes Gráficos (GUI) ---
    private final JTextField userField;
    private final JTextField emailField;
    private final JTextArea outputArea;
    private final JButton saveOracleButton;
    private final JButton savePostgresButton;
    private final JButton refreshButton;

    // --- Componentes de JPA ---
    private final EntityManagerFactory emfOracle;
    private final EntityManagerFactory emfPostgres;

    public App() {
        // --- Conectar a las "Unidades de Persistencia" del XML ---
        // Esto es costoso, se hace UNA SOLA VEZ al inicio.
        emfOracle = Persistence.createEntityManagerFactory("oracle-pu");
        emfPostgres = Persistence.createEntityManagerFactory("postgres-pu");

        // --- Configuración de la Ventana (Igual que antes) ---
        setTitle("Gestor de Usuarios (Con JPA / Hibernate)");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Controlamos el cierre
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Panel de Entradas (Formulario) ---
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("Usuario:"));
        userField = new JTextField();
        inputPanel.add(userField);
        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);
        saveOracleButton = new JButton("Guardar en Oracle");
        savePostgresButton = new JButton("Guardar en Postgres");
        inputPanel.add(saveOracleButton);
        inputPanel.add(savePostgresButton);

        // --- Área de Salida (Resultados) ---
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // --- Panel de Acciones (Refrescar) ---
        JPanel actionPanel = new JPanel(new FlowLayout());
        refreshButton = new JButton("Refrescar Listas");
        actionPanel.add(refreshButton);

        // --- Registrar "Listeners" ---
        saveOracleButton.addActionListener(this);
        savePostgresButton.addActionListener(this);
        refreshButton.addActionListener(this);

        // --- Añadir paneles a la ventana ---
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        // --- IMPORTANTE: Cerrar conexiones al cerrar la ventana ---
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Cerrando EntityManagerFactories...");
                emfOracle.close();
                emfPostgres.close();
                System.out.println("Cerrado. Saliendo.");
                System.exit(0);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == saveOracleButton) {
            saveUserToDB(emfOracle, "Oracle");
        } else if (source == savePostgresButton) {
            saveUserToDB(emfPostgres, "PostgreSQL");
        } else if (source == refreshButton) {
            loadUsersFromDB();
        }
    }

    /**
     * Guarda el usuario (como Objeto) en la DB seleccionada.
     */
    private void saveUserToDB(EntityManagerFactory emf, String dbType) {
        String user = userField.getText();
        String email = emailField.getText();

        if (user.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y Email no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1. Crear el OBJETO Java
        Usuario nuevoUsuario = new Usuario(user, email);

        EntityManager em = null;
        try {
            // 2. Obtener un "Manager" (similar a la conexión)
            em = emf.createEntityManager();

            // 3. Iniciar Transacción
            em.getTransaction().begin();

            // 4. PERSISTIR EL OBJETO (¡La magia!)
            em.persist(nuevoUsuario);

            // 5. Confirmar Transacción
            em.getTransaction().commit();

            outputArea.setText("¡Usuario guardado en " + dbType + "!\n");
            userField.setText("");
            emailField.setText("");
            loadUsersFromDB(); // Recargar la lista

        } catch (Exception ex) {
            // Si algo falla, hacer rollback
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            JOptionPane.showMessageDialog(this, "Error al guardar en " + dbType + ":\n" + ex.getMessage(), "Error de JPA", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            // Cerrar SIEMPRE el manager
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Carga los usuarios (como Objetos) de AMBAS bases de datos.
     */
    private void loadUsersFromDB() {
        StringBuilder allUsers = new StringBuilder();
        EntityManager emOracle = null;
        EntityManager emPostgres = null;

        try {
            // --- Cargar Oracle ---
            allUsers.append("--- Usuarios en ORACLE ---\n");
            emOracle = emfOracle.createEntityManager();

            // Usamos HQL (Lenguaje de Consulta de Hibernate) - "Consulta el Objeto Usuario"
            List<Usuario> usuariosOracle = emOracle.createQuery("FROM Usuario", Usuario.class).getResultList();

            for (Usuario u : usuariosOracle) {
                allUsers.append(u.toString()).append("\n");
            }

        } catch (Exception ex) {
            allUsers.append("Error al cargar Oracle: ").append(ex.getMessage()).append("\n");
            ex.printStackTrace();
        } finally {
            if (emOracle != null) emOracle.close();
        }

        try {
            // --- Cargar PostgreSQL ---
            allUsers.append("\n--- Usuarios en POSTGRESQL ---\n");
            emPostgres = emfPostgres.createEntityManager();

            List<Usuario> usuariosPostgres = emPostgres.createQuery("FROM Usuario", Usuario.class).getResultList();

            for (Usuario u : usuariosPostgres) {
                allUsers.append(u.toString()).append("\n");
            }
        } catch (Exception ex) {
            allUsers.append("Error al cargar Postgres: ").append(ex.getMessage()).append("\n");
            ex.printStackTrace();
        } finally {
            if (emPostgres != null) emPostgres.close();
        }

        outputArea.setText(allUsers.toString());
    }

    /**
     * Método Main: El punto de entrada de la aplicación.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.setVisible(true);
            app.loadUsersFromDB(); // Carga inicial
        });
    }
}