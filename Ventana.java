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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ventana");
        frame.setContentPane(new Ventana().Principal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
