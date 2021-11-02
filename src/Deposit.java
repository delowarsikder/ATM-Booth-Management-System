import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Deposit extends JFrame implements ActionListener {
    static int winX = 400;
    static int winY = 150;
    static int windowWidth = 370;
    static int windowHeight = 400;
    String  assetsPath= "E:\\Java\\ATM_BOOTH\\assets";
    public static void main(String[] args) {
        Deposit deposit = new Deposit();
        deposit.setLocation(winX, winY);
        deposit.setSize(windowWidth, windowHeight);
        deposit.setResizable(false);
        deposit.setVisible(true);
    }

    JPasswordField txtPinNo = new JPasswordField(25);
    JTextField txtDepositAmount = new JTextField(25);

    JLabel lblPinNo = new JLabel("Pin Number ", JLabel.RIGHT);
    JLabel lblDepositAmount = new JLabel("Deposit Amount ", JLabel.RIGHT);

    JButton btnBack2Menu = new JButton(new ImageIcon(assetsPath + "/menu.png"));
    JButton btnDeposit = new JButton(new ImageIcon(assetsPath + "/deposit.png"));

    Connection connection;
    Statement statement;
    PreparedStatement ps;

    public Deposit() {
        super("ATM Booth");

        JPanel panel = new JPanel();
        panel.setLayout(null);
        //----Adding Components into your Frame

        panel.add(txtPinNo);
        panel.add(txtDepositAmount);

        panel.add(lblPinNo);
        panel.add(lblDepositAmount);

        panel.add(btnBack2Menu);
        panel.add(btnDeposit);

        //-----Setting the location of the components
        lblPinNo.setBounds(40, 85, 80, 20);
        lblDepositAmount.setBounds(20, 125, 115, 20);

        txtPinNo.setBounds(140, 80, 150, 25);
        txtDepositAmount.setBounds(140, 120, 150, 25);

        btnBack2Menu.setBounds(50, 220, 65, 25);
        btnDeposit.setBounds(180, 220, 80, 25);

        //-----Adding the an actionlistener to the buttons
        btnBack2Menu.addActionListener(this);
        btnDeposit.addActionListener(this);
        btnBack2Menu.setToolTipText("Click To Back to Menu");

        lblPinNo.setOpaque(true);
        lblPinNo.setBackground(Color.black);
        lblPinNo.setForeground(Color.yellow);

        lblDepositAmount.setOpaque(true);
        lblDepositAmount.setBackground(Color.black);
        lblDepositAmount.setForeground(Color.yellow);

        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Deposit Transaction"));

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

        } else if (source == btnDeposit) {
            try {
                //
                String spinNo = txtPinNo.getText();
                String sdepositamount = txtDepositAmount.getText();

                if (spinNo.length() == 0 || sdepositamount.length() == 0) {
                    JOptionPane.showMessageDialog(null, "Some Field Empty", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {

                    statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT * FROM UserAccountInfo WHERE AccountNo ='" + Login.currentLoginAccountNo + "'");

                    String strAccNo = "";
                    String strPinNo = "";
                    String strBalance = "";

                    while (rs.next()) {
                        strAccNo = rs.getString("AccountNo");
                        strBalance = rs.getString("Amount");
                        strPinNo = rs.getString("Password");
                    }
                    if (strAccNo.length() != 0) {

                        if (strPinNo.equals(txtPinNo.getText())) {

                            int a = Integer.parseInt(strBalance);
                            int b = Integer.parseInt(txtDepositAmount.getText());

                            if (b < 50) {
                                JOptionPane.showMessageDialog(null, "The Minimum cash you can deposit is 50$", "ATM", JOptionPane.INFORMATION_MESSAGE);
                                txtDepositAmount.setText("");
                            } else {
                                int n = JOptionPane.showConfirmDialog(null, "Are you Confirm to Deposit " + b + "$", "Ok", JOptionPane.YES_NO_OPTION);
                                if (n == JOptionPane.YES_OPTION) {
                                    int sum = a + b;
                                    JOptionPane.showMessageDialog(null, "Your Deposit " + b + "$ is successful", "ATM", JOptionPane.INFORMATION_MESSAGE);
                                    ps = connection.prepareStatement("UPDATE UserAccountInfo SET Amount = '" + sum + "'WHERE AccountNo = '" + strAccNo + "'");
                                    ps.executeUpdate();
                                    txtPinNo.requestFocus(true);

                                    AddDepositHistory(b);

                                    //goto
                                    QuerySection querySection = new QuerySection();
                                    querySection.setSize(windowWidth, windowHeight);
                                    querySection.setVisible(true);
                                    querySection.setResizable(false);
                                    querySection.setLocation(winX, winY);
                                    dispose();
                                }
                            }

                            statement.close();
                        } else {
                            JOptionPane.showMessageDialog(null, "Enter Correct pin!!", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "You must need login", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (SQLException sqlEx) {
                JOptionPane.showMessageDialog(null, "General error", "ATM", JOptionPane.INFORMATION_MESSAGE);
                System.out.println(sqlEx);
            }
        }
    }

    private void AddDepositHistory(int b) {
        try {
            DateAndTime dateAndTime = new DateAndTime();
            Statement statement1;
            PreparedStatement preparedStatement1;

            statement1 = connection.createStatement();
            preparedStatement1 = connection.prepareStatement("INSERT INTO TransactionHistory " + " (Time,Date,AccountNo,Type,Amount,ReceiverAccountNo) " + " VALUES(?,?,?,?,?,?)");
            preparedStatement1.setString(1, dateAndTime.getCurrentTime());
            preparedStatement1.setString(2, dateAndTime.getCurrentDate());
            preparedStatement1.setString(3, Login.currentLoginAccountNo);
            preparedStatement1.setString(4, "Deposit");
            preparedStatement1.setString(5, String.valueOf(b));
            preparedStatement1.setString(6, "Owner");
            preparedStatement1.executeUpdate();
            statement1.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


}