import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelBusqueda extends JPanel {

    private final CatalogoPeliculas catalogo;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnMeGusta;
    private DefaultListModel<Pelicula> modeloLista;
    private JList<Pelicula> listaResultados;
    private JLabel lblCantidad;
    private JLabel lblSeleccion;

    public PanelBusqueda(CatalogoPeliculas catalogo) {
        this.catalogo = catalogo;
        inicializarComponentes();
        configurarEventos();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelSuperior = new JPanel(new BorderLayout(8, 8));

        JPanel panelBarra = new JPanel(new BorderLayout(8, 0));
        panelBarra.add(new JLabel("Buscar por título o director:"), BorderLayout.WEST);

        txtBuscar = new JTextField();
        panelBarra.add(txtBuscar, BorderLayout.CENTER);

        btnBuscar = new JButton("Buscar coincidencias");
        panelBarra.add(btnBuscar, BorderLayout.EAST);

        lblCantidad = new JLabel("Resultados encontrados: 0");
        lblCantidad.setFont(lblCantidad.getFont().deriveFont(Font.BOLD));

        panelSuperior.add(panelBarra, BorderLayout.NORTH);
        panelSuperior.add(lblCantidad, BorderLayout.SOUTH);

        modeloLista = new DefaultListModel<>();
        listaResultados = new JList<>(modeloLista);
        listaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaResultados);

        lblSeleccion = new JLabel("Película seleccionada: Ninguna");
        btnMeGusta = new JButton("Me gusta +1");

        JPanel panelInferior = new JPanel(new BorderLayout(8, 0));
        panelInferior.add(lblSeleccion, BorderLayout.CENTER);
        panelInferior.add(btnMeGusta, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollLista, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnBuscar.addActionListener(e -> ejecutarBusqueda());
        txtBuscar.addActionListener(e -> ejecutarBusqueda());
        btnMeGusta.addActionListener(e -> darMeGusta());

        // Seleccionar solo muestra los datos; no incrementa el contador
        listaResultados.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarSeleccion();
            }
        });
    }

    public void ejecutarBusqueda() {
        try {
            ArrayList<Pelicula> coincidencias = catalogo.buscarCoincidencias(txtBuscar.getText());

            // clear() también limpia la selección anterior
            modeloLista.clear();
            for (Pelicula p : coincidencias) {
                modeloLista.addElement(p);
            }

            lblCantidad.setText("Resultados encontrados: " + coincidencias.size());
            if (coincidencias.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron coincidencias",
                        "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        mostrarSeleccion();
    }

    private void darMeGusta() {
        Pelicula seleccionada = listaResultados.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una película antes de dar Me gusta",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            catalogo.darMeGusta(seleccionada.getCodigo());
            // La JList contiene los mismos objetos del catálogo: basta con repintar
            listaResultados.repaint();
            mostrarSeleccion();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarSeleccion() {
        Pelicula seleccionada = listaResultados.getSelectedValue();
        if (seleccionada != null) {
            lblSeleccion.setText("Película seleccionada: " + seleccionada);
        } else {
            lblSeleccion.setText("Película seleccionada: Ninguna");
        }
    }

    public Pelicula getPeliculaSeleccionada() {
        return listaResultados.getSelectedValue();
    }
}
