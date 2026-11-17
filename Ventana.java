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

        // Al entrar en la pestaña Ranking se recalcula con los contadores actuales
        tabbedPane1.addChangeListener(e -> {
            if (tabbedPane1.getSelectedComponent() == panelRanking) {
                panelRanking.actualizar();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ventana");
        frame.setContentPane(new Ventana().Principal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
