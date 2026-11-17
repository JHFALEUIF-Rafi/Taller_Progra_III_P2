import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelRanking extends JPanel {

    private final CatalogoPeliculas catalogo;
    private DefaultListModel<Pelicula> modeloRanking;
    private JList<Pelicula> listaRanking;
    private JButton btnActualizar;

    public PanelRanking(CatalogoPeliculas catalogo) {
        this.catalogo = catalogo;
        inicializarComponentes();
        configurarEventos();
        actualizar();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel("Ranking de popularidad (me gusta descendente)");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD));

        modeloRanking = new DefaultListModel<>();
        listaRanking = new JList<>(modeloRanking);
        // Muestra posición, código, título y contador de cada película
        listaRanking.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Pelicula p = (Pelicula) value;
                String texto = (index + 1) + ". " + p.getCodigo() + " | " + p.getTitulo()
                        + " | Me gusta: " + p.getMeGusta();
                return super.getListCellRendererComponent(list, texto, index, isSelected, cellHasFocus);
            }
        });

        btnActualizar = new JButton("Actualizar");

        add(lblTitulo, BorderLayout.NORTH);
        add(new JScrollPane(listaRanking), BorderLayout.CENTER);
        add(btnActualizar, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnActualizar.addActionListener(e -> actualizar());
    }

    // Se llama al pulsar Actualizar y al entrar en la pestaña
    public void actualizar() {
        ArrayList<Pelicula> ranking = catalogo.obtenerRanking();
        modeloRanking.clear();
        for (Pelicula p : ranking) {
            modeloRanking.addElement(p);
        }
    }
}
