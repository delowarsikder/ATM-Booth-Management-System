import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class BalanceEnquiry extends JFrame implements ActionListener {
    static int winX = 400;
    static int winY = 150;
    static int windowWidth = 370;
    static int windowHeight = 400;

    String assetsPath = "E:\\Java\\ATM_BOOTH\\assets";
    public static void main(String[] args) {
        BalanceEnquiry balanceEnquiry = new BalanceEnquiry();
        balanceEnquiry.setLocation(winX, winY);
        balanceEnquiry.setSize(windowWidth, windowHeight);
        balanceEnquiry.setTitle("Balance Inquiry");
        balanceEnquiry.setResizable(false);
        balanceEnquiry.setVisible(true);
    }
    JPasswordField txtAccPass = new JPasswordField(25);
    JTextField txtBalance = new JTextField(25);
    JLabel lblAccPass = new JLabel("Pin Number ", JLabel.RIGHT);
    JLabel lblBalance = new JLabel("Balance ", JLabel.RIGHT);

    JButton btnBack2Menu = new JButton(new ImageIcon(assetsPath + "/menu.png"));
    JButton btnBalanceInquiry = new JButton(new ImageIcon(assetsPath + "/balanceenquiry.png"));

    Connection connection;
    //ResultSet resultSet;
    Statement statement;

    public BalanceEnquiry() {
        super("ATM Booth");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        //----Adding Components into your Frame
        panel.add(txtAccPass);
        panel.add(txtBalance);
        panel.add(lblAccPass);
        panel.add(lblBalance);
        panel.add(btnBack2Menu);
        panel.add(btnBalanceInquiry);

        //-----Setting the location of the components
        lblAccPass.setBounds(50, 80, 80, 20);
        lblBalance.setBounds(50, 120, 80, 20);

        txtAccPass.setBounds(135, 80, 150, 25);
        txtBalance.setBounds(135, 120, 150, 25);

        btnBalanceInquiry.setBounds(170, 230, 160, 25);
        btnBack2Menu.setBounds(30, 230, 60, 25);

        //set place holder
        PlaceHolder placeholder = new PlaceHolder("Enter password", txtAccPass);
        placeholder.changeAlpha(0.75f);
        placeholder.changeStyle(Font.ITALIC);

        //backgound color
        lblAccPass.setOpaque(true);
        lblAccPass.setBackground(Color.black);
        lblAccPass.setForeground(Color.yellow);

        lblBalance.setOpaque(true);
        lblBalance.setBackground(Color.black);
        lblBalance.setForeground(Color.yellow);

        //-----Adding the an actionlistener to the buttons
        btnBack2Menu.addActionListener(this);
        btnBalanceInquiry.addActionListener(this);

        txtBalance.setEditable(false);
        btnBack2Menu.setToolTipText("Click To Back to Menu");

        btnBack2Menu.setForeground(Color.yellow);
        btnBalanceInquiry.setForeground(Color.yellow);
        btnBack2Menu.setBackground(Color.black);
        btnBalanceInquiry.setBackground(Color.black);

        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblBgImage = new JLabel(new ImageIcon(assetsPath + "/atmimage.jpg"));
        lblBgImage.setBounds(0, 0, windowWidth, windowHeight);
        panel.add(lblBgImage);

        //connection
        String databaseUrl = assetsPath + "/ATM_BOOTH_DB.accdb";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");//Loading Driver
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + databaseUrl);
            System.out.println("Connected Successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load driver");
            e.printStackTrace();

        } catch (SQLException e) {
            System.err.println("Unable to connect");
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnBack2Menu) {
            QuerySection querySection = new QuerySection();
            querySection.setSize(windowWidth, windowHeight);
            querySection.setVisible(true);
            querySection.setResizable(false);
            querySection.setLocation(winX, winY);
            dispose();

        } else if (source == btnBalanceInquiry) {
            try {
                statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM UserAccountInfo WHERE AccountNo ='" + Login.currentLoginAccountNo + "'");
                String strCurrentAcc = "";
                String strPin = "";
                String strBalance = "";
                while (rs.next()) {
                    strCurrentAcc = rs.getString("AccountNo");
                    strPin = rs.getString("Password");
                    strBalance = rs.getString("Amount");
                }
                if (txtAccPass.getText().length() != 0) {
                    if (strCurrentAcc.length() != 0) {
                        if (strPin.equals(txtAccPass.getText())) {
                            txtBalance.setText(strBalance);
                            JOptionPane.showMessageDialog(null, "Your Current Balance is " + strBalance + "$");
                        } else {
                            JOptionPane.showMessageDialog(null, "Please Enter correct password!!", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "You must need Login", "Warning", JOptionPane.WARNING_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Password Field is Empty!!", "Warning", JOptionPane.WARNING_MESSAGE);
                }

                statement.close();

            } catch (SQLException s) {
                System.out.println("No record found!\n\n\n");
                System.out.println("SQL Error" + s.toString() + " " + s.getErrorCode() + " " + s.getSQLState());
            } catch (Exception x) {
                System.out.println("Error" + x.toString() + " " + x.getMessage());
            }
        }
    }

}