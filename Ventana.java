import javax.swing.*;

public class Ventana {
    private JPanel Principal;
    private JTabbedPane tabbedPane1;
    private JLabel lblCodigo;
    private JTextField txtCodigo;
    private JTextField txtTitulo;
    private JLabel lblTitulo;
    private JTextField txtDirector;
    private JLabel lblDirector;
    private JComboBox cboGenero;
    private JLabel lblAnio;
    private JLabel lblGenero;
    private JSpinner spinAnio;
    private JButton REGISTRARButton;
    private JButton TOTALPELICULASButton;

    // Un solo catálogo compartido por todas las pestañas (se carga una vez)
    private final CatalogoPeliculas catalogo = new CatalogoPeliculas();
    private PanelBusqueda panelBusqueda;
    private PanelRanking panelRanking;

    public Ventana() {
        panelBusqueda = new PanelBusqueda(catalogo);
        panelRanking = new PanelRanking(catalogo);
        tabbedPane1.setComponentAt(1, panelBusqueda);
        tabbedPane1.setComponentAt(2, panelRanking);

        // Año entre 1 y el año actual, sin separador de miles
        spinAnio.setModel(new SpinnerNumberModel(CatalogoPeliculas.ANIO_MAX,
                CatalogoPeliculas.ANIO_MIN, CatalogoPeliculas.ANIO_MAX, 1));
        spinAnio.setEditor(new JSpinner.NumberEditor(spinAnio, "#"));
        actualizarTotal();

        REGISTRARButton.addActionListener(e -> registrarPelicula());
        TOTALPELICULASButton.addActionListener(e ->
                JOptionPane.showMessageDialog(Principal, "Total de películas: " + catalogo.getTotal()));

        // Al entrar en la pestaña Ranking se recalcula con los contadores actuales
        tabbedPane1.addChangeListener(e -> {
            if (tabbedPane1.getSelectedComponent() == panelRanking) {
                panelRanking.actualizar();
            }
        });
    }

    // R2: registra una película nueva con cero me gusta
    private void registrarPelicula() {
        try {
            String textoCodigo = txtCodigo.getText().trim();
            if (textoCodigo.isEmpty())
                throw new Exception("El código es obligatorio");

            int codigo;
            try {
                codigo = Integer.parseInt(textoCodigo);
            } catch (NumberFormatException ex) {
                throw new Exception("El código debe ser un número entero");
            }

            String titulo = txtTitulo.getText().trim();
            String director = txtDirector.getText().trim();
            String genero = (String) cboGenero.getSelectedItem();
            int anio = (int) spinAnio.getValue();

            catalogo.registrar(new Pelicula(codigo, titulo, director, genero, anio, 0));

            JOptionPane.showMessageDialog(Principal, "Película registrada correctamente");
            limpiarRegistro();
            actualizarTotal();
            panelBusqueda.refrescarResultados();
            panelRanking.actualizar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(Principal, ex.getMessage(),
                    "Error al registrar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarRegistro() {
        txtCodigo.setText("");
        txtTitulo.setText("");
        txtDirector.setText("");
        cboGenero.setSelectedIndex(0);
        spinAnio.setValue(CatalogoPeliculas.ANIO_MAX);
    }

    private void actualizarTotal() {
        TOTALPELICULASButton.setText("TOTAL PELICULAS: " + catalogo.getTotal());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ventana");
        frame.setContentPane(new Ventana().Principal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
