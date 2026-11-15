package GUI;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import DAO.ReporteDAO;

public class InterfazReportes extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea txtResultado;

    public InterfazReportes() {
        setTitle("BiancaTex - Reportes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 620, 450);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Reportes");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitulo.setBounds(20, 10, 200, 30);
        contentPane.add(lblTitulo);

        JButton btnEmpleado = new JButton("Empleado + ventas");
        btnEmpleado.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtResultado.setText(ReporteDAO.empleadoConMasVentas());
            }
        });
        btnEmpleado.setBounds(20, 50, 180, 30);
        contentPane.add(btnEmpleado);

        JButton btnProductos = new JButton("Top 5 productos");
        btnProductos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtResultado.setText(ReporteDAO.productosMasVendidos());
            }
        });
        btnProductos.setBounds(215, 50, 180, 30);
        contentPane.add(btnProductos);

        JButton btnBalance = new JButton("Balance");
        btnBalance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtResultado.setText(ReporteDAO.balance());
            }
        });
        btnBalance.setBounds(410, 50, 180, 30);
        contentPane.add(btnBalance);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 95, 570, 260);
        contentPane.add(scrollPane);

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 13));
        scrollPane.setViewportView(txtResultado);

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnVolver.setBounds(490, 370, 100, 30);
        contentPane.add(btnVolver);
    }
}