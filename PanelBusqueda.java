import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelBusqueda extends JPanel {

    private final CatalogoPeliculas catalogo;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private DefaultListModel<Pelicula> modeloLista;
    private JList<Pelicula> listaResultados;
    private JLabel lblCantidad;
    private JLabel lblSeleccion;

    public PanelBusqueda(CatalogoPeliculas catalogo) {
        this.catalogo = catalogo;
        inicializarComponentes();
        configurarEventos();
        ejecutarBusqueda();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelSuperior = new JPanel(new BorderLayout(8, 8));

        JPanel panelBarra = new JPanel(new BorderLayout(8, 0));
        panelBarra.add(new JLabel("Buscar por título:"), BorderLayout.WEST);

        txtBuscar = new JTextField();
        panelBarra.add(txtBuscar, BorderLayout.CENTER);

        btnBuscar = new JButton("Buscar");
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

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollLista, BorderLayout.CENTER);
        add(lblSeleccion, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnBuscar.addActionListener(e -> ejecutarBusqueda());
        txtBuscar.addActionListener(e -> ejecutarBusqueda());

        listaResultados.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Pelicula seleccionada = listaResultados.getSelectedValue();
                if (seleccionada != null) {
                    lblSeleccion.setText("Película seleccionada: " + seleccionada.toString());
                } else {
                    lblSeleccion.setText("Película seleccionada: Ninguna");
                }
            }
        });
    }

    public void ejecutarBusqueda() {
        String texto = txtBuscar.getText();
        List<Pelicula> coincidencias = catalogo.buscarCoincidencias(texto);

        modeloLista.clear();
        for (Pelicula p : coincidencias) {
            modeloLista.addElement(p);
        }

        lblCantidad.setText("Resultados encontrados: " + coincidencias.size());
        lblSeleccion.setText("Película seleccionada: Ninguna");
    }

    public Pelicula getPeliculaSeleccionada() {
        return listaResultados.getSelectedValue();
    }
}