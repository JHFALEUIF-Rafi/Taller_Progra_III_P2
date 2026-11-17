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

    private JTextField txtBuscarCodigo;
    private JButton btnBuscarCodigo;
    private JTextField txtEditCodigo;
    private JTextField txtEditTitulo;
    private JTextField txtEditDirector;
    private JComboBox<String> cmbEditGenero;
    private JSpinner spnEditAnio;
    private JButton btnGuardarCambios;

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
        panelBarra.add(new JLabel("Buscar por titulo:"), BorderLayout.WEST);

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

        lblSeleccion = new JLabel("Pelicula seleccionada: Ninguna");

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollLista, BorderLayout.CENTER);
        add(lblSeleccion, BorderLayout.SOUTH);

        JPanel panelEdicion = new JPanel(new BorderLayout(8, 8));
        panelEdicion.setBorder(BorderFactory.createTitledBorder("Búsqueda por Código y Edición"));

        JPanel panelBuscarCodigo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscarCodigo.add(new JLabel("Código:"));
        txtBuscarCodigo = new JTextField(8);
        btnBuscarCodigo = new JButton("Buscar código");
        panelBuscarCodigo.add(txtBuscarCodigo);
        panelBuscarCodigo.add(btnBuscarCodigo);

        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 6, 6));
        panelCampos.add(new JLabel("Código:"));
        txtEditCodigo = new JTextField();
        txtEditCodigo.setEditable(false);
        panelCampos.add(txtEditCodigo);

        panelCampos.add(new JLabel("Título:"));
        txtEditTitulo = new JTextField();
        panelCampos.add(txtEditTitulo);

        panelCampos.add(new JLabel("Director:"));
        txtEditDirector = new JTextField();
        panelCampos.add(txtEditDirector);

        panelCampos.add(new JLabel("Género:"));
        cmbEditGenero = new JComboBox<>(CatalogoPeliculas.GENEROS);
        panelCampos.add(cmbEditGenero);

        panelCampos.add(new JLabel("Año:"));
        spnEditAnio = new JSpinner(new SpinnerNumberModel(2024, CatalogoPeliculas.ANIO_MIN, CatalogoPeliculas.ANIO_MAX, 1));
        panelCampos.add(spnEditAnio);

        btnGuardarCambios = new JButton("Guardar cambios");

        panelEdicion.add(panelBuscarCodigo, BorderLayout.NORTH);
        panelEdicion.add(panelCampos, BorderLayout.CENTER);
        panelEdicion.add(btnGuardarCambios, BorderLayout.SOUTH);

        add(panelEdicion, BorderLayout.EAST);
    }

    private void configurarEventos() {
        btnBuscar.addActionListener(e -> ejecutarBusqueda());
        txtBuscar.addActionListener(e -> ejecutarBusqueda());

        listaResultados.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Pelicula seleccionada = listaResultados.getSelectedValue();
                if (seleccionada != null) {
                    lblSeleccion.setText("Pelicula seleccionada: " + seleccionada.toString());
                    cargarPeliculaEnFormulario(seleccionada);
                } else {
                    lblSeleccion.setText("Pelicula seleccionada: Ninguna");
                }
            }
        });

        btnBuscarCodigo.addActionListener(e -> {
            try {
                int codigo = Integer.parseInt(txtBuscarCodigo.getText().trim());
                int indice = catalogo.buscarIndicePorCodigo(codigo);
                if (indice == -1) {
                    limpiarFormularioEdicion();
                    JOptionPane.showMessageDialog(this, "No se encontró ninguna película con el código " + codigo);
                } else {
                    cargarPeliculaEnFormulario(catalogo.peliculas.get(indice));
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un código numérico válido.");
            }
        });

        btnGuardarCambios.addActionListener(e -> {
            if (txtEditCodigo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Primero busque o seleccione una película.");
                return;
            }
            try {
                int codigo = Integer.parseInt(txtEditCodigo.getText());
                String titulo = txtEditTitulo.getText();
                String director = txtEditDirector.getText();
                String genero = (String) cmbEditGenero.getSelectedItem();
                int anio = (int) spnEditAnio.getValue();

                catalogo.editarPelicula(codigo, titulo, director, genero, anio);
                ejecutarBusqueda();
                JOptionPane.showMessageDialog(this, "Película actualizada correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al editar", JOptionPane.ERROR_MESSAGE);
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
        lblSeleccion.setText("Pelicula seleccionada: Ninguna");
    }

    public Pelicula getPeliculaSeleccionada() {
        return listaResultados.getSelectedValue();
    }

    private void cargarPeliculaEnFormulario(Pelicula p) {
        txtEditCodigo.setText(String.valueOf(p.getCodigo()));
        txtEditTitulo.setText(p.getTitulo());
        txtEditDirector.setText(p.getDirector());
        cmbEditGenero.setSelectedItem(p.getGenero());
        spnEditAnio.setValue(p.getAnio());
    }

    private void limpiarFormularioEdicion() {
        txtEditCodigo.setText("");
        txtEditTitulo.setText("");
        txtEditDirector.setText("");
        cmbEditGenero.setSelectedIndex(0);
        spnEditAnio.setValue(2024);
    }
}