import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
    private JLabel lblEditMeGusta;
    private JButton btnGuardarCambios;
    private JButton btnMeGusta;

    private String ultimaBusqueda = null;

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

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollLista, BorderLayout.CENTER);
        add(lblSeleccion, BorderLayout.SOUTH);

        JPanel panelEdicion = new JPanel(new BorderLayout(8, 8));
        panelEdicion.setBorder(BorderFactory.createTitledBorder("Búsqueda por Código y Edición"));

        JPanel panelBuscarCodigo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscarCodigo.add(new JLabel("Código:"));
        txtBuscarCodigo = new JTextField(8);
        btnBuscarCodigo = new JButton("Buscar por código");
        panelBuscarCodigo.add(txtBuscarCodigo);
        panelBuscarCodigo.add(btnBuscarCodigo);

        JPanel panelCampos = new JPanel(new GridLayout(6, 2, 6, 6));
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
        spnEditAnio = new JSpinner(new SpinnerNumberModel(CatalogoPeliculas.ANIO_MAX,
                CatalogoPeliculas.ANIO_MIN, CatalogoPeliculas.ANIO_MAX, 1));
        spnEditAnio.setEditor(new JSpinner.NumberEditor(spnEditAnio, "#"));
        panelCampos.add(spnEditAnio);

        panelCampos.add(new JLabel("Me gusta:"));
        lblEditMeGusta = new JLabel("-");
        panelCampos.add(lblEditMeGusta);

        btnGuardarCambios = new JButton("Guardar cambios");
        btnMeGusta = new JButton("Me gusta +1");
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 6, 0));
        panelBotones.add(btnGuardarCambios);
        panelBotones.add(btnMeGusta);

        panelEdicion.add(panelBuscarCodigo, BorderLayout.NORTH);
        panelEdicion.add(panelCampos, BorderLayout.CENTER);
        panelEdicion.add(panelBotones, BorderLayout.SOUTH);

        add(panelEdicion, BorderLayout.EAST);
    }

    private void configurarEventos() {
        btnBuscar.addActionListener(e -> ejecutarBusqueda());
        txtBuscar.addActionListener(e -> ejecutarBusqueda());
        btnBuscarCodigo.addActionListener(e -> buscarPorCodigo());
        txtBuscarCodigo.addActionListener(e -> buscarPorCodigo());
        btnGuardarCambios.addActionListener(e -> guardarCambios());
        btnMeGusta.addActionListener(e -> darMeGusta());

        listaResultados.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Pelicula seleccionada = listaResultados.getSelectedValue();
                if (seleccionada != null) {
                    cargarPeliculaEnFormulario(seleccionada);
                } else {
                    limpiarFormularioEdicion();
                }
            }
        });
    }

    public void ejecutarBusqueda() {
        try {
            ArrayList<Pelicula> coincidencias = catalogo.buscarCoincidencias(txtBuscar.getText());
            ultimaBusqueda = txtBuscar.getText();
            mostrarResultados(coincidencias);

            if (coincidencias.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron coincidencias",
                        "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscarPorCodigo() {
        try {
            int codigo = Integer.parseInt(txtBuscarCodigo.getText().trim());
            int indice = catalogo.buscarIndicePorCodigo(codigo);
            if (indice == -1) {
                listaResultados.clearSelection();
                limpiarFormularioEdicion();
                JOptionPane.showMessageDialog(this, "No se encontró ninguna película con el código " + codigo,
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Pelicula p = catalogo.peliculas.get(indice);
            if (!modeloLista.contains(p)) {
                ultimaBusqueda = null;
                ArrayList<Pelicula> unica = new ArrayList<>();
                unica.add(p);
                mostrarResultados(unica);
            }
            listaResultados.setSelectedValue(p, true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un código numérico válido.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void guardarCambios() {
        int codigo = codigoSeleccionado();
        if (codigo == -1) {
            JOptionPane.showMessageDialog(this, "Primero busque o seleccione una película.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String titulo = txtEditTitulo.getText();
            String director = txtEditDirector.getText();
            String genero = (String) cmbEditGenero.getSelectedItem();
            int anio = (int) spnEditAnio.getValue();

            catalogo.editarPelicula(codigo, titulo, director, genero, anio);
            refrescarResultados();
            JOptionPane.showMessageDialog(this, "Película actualizada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al editar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void darMeGusta() {
        int codigo = codigoSeleccionado();
        if (codigo == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una película antes de dar Me gusta",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            catalogo.darMeGusta(codigo);
            listaResultados.repaint();
            Pelicula seleccionada = listaResultados.getSelectedValue();
            lblEditMeGusta.setText(String.valueOf(seleccionada.getMeGusta()));
            lblSeleccion.setText("Película seleccionada: " + seleccionada);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refrescarResultados() {
        if (ultimaBusqueda == null) {
            listaResultados.repaint();
            Pelicula seleccionada = listaResultados.getSelectedValue();
            if (seleccionada != null) {
                cargarPeliculaEnFormulario(seleccionada);
            }
            return;
        }

        int codigoAnterior = codigoSeleccionado();
        try {
            mostrarResultados(catalogo.buscarCoincidencias(ultimaBusqueda));
        } catch (Exception ex) {
            return;
        }
        for (int i = 0; i < modeloLista.size(); i++) {
            if (modeloLista.get(i).getCodigo() == codigoAnterior) {
                listaResultados.setSelectedIndex(i);
                break;
            }
        }
    }

    private void mostrarResultados(ArrayList<Pelicula> peliculas) {
        modeloLista.clear();
        for (Pelicula p : peliculas) {
            modeloLista.addElement(p);
        }
        lblCantidad.setText("Resultados encontrados: " + peliculas.size());
        listaResultados.clearSelection();
        limpiarFormularioEdicion();
    }

    private int codigoSeleccionado() {
        if (txtEditCodigo.getText().isEmpty()) {
            return -1;
        }
        return Integer.parseInt(txtEditCodigo.getText());
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
        lblEditMeGusta.setText(String.valueOf(p.getMeGusta()));
        lblSeleccion.setText("Película seleccionada: " + p);
    }

    private void limpiarFormularioEdicion() {
        txtEditCodigo.setText("");
        txtEditTitulo.setText("");
        txtEditDirector.setText("");
        cmbEditGenero.setSelectedIndex(0);
        spnEditAnio.setValue(CatalogoPeliculas.ANIO_MAX);
        lblEditMeGusta.setText("-");
        lblSeleccion.setText("Película seleccionada: Ninguna");
    }
}
