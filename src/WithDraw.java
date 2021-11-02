import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class WithDraw extends JFrame implements ActionListener {
    static int winX = 400;
    static int winY = 150;
    static int windowWidth = 370;
    static int windowHeight = 400;
    String assetsPath = "E:\\Java\\ATM_BOOTH\\assets";

    public static void main(String[] args) {
        WithDraw withDraw = new WithDraw();
        withDraw.setLocation(winX, winY);
        withDraw.setSize(windowWidth, windowHeight);
        withDraw.setResizable(false);
        withDraw.setVisible(true);
    }

    JPasswordField txtPinNo = new JPasswordField(25);
    JTextField txtWithdrawAmount = new JTextField(25);

    JLabel lblPinNo = new JLabel("Pin Number ", JLabel.RIGHT);
    JLabel lblWithdrawAmount = new JLabel("WithDraw Amount", JLabel.RIGHT);

    JButton btnBack2Menu = new JButton(new ImageIcon(assetsPath + "/menu.png"));
    JButton btnWithdraw = new JButton(new ImageIcon(assetsPath + "/withdraw.png"));

    Connection connection;
    Statement statement;
    PreparedStatement preparedStatement;

    public WithDraw() {
        super("ATM Booth");

        JPanel panel = new JPanel();
        panel.setLayout(null);
        //----Adding Components into your Frame
        panel.add(txtPinNo);
        panel.add(txtWithdrawAmount);

        panel.add(lblPinNo);
        panel.add(lblWithdrawAmount);

        panel.add(btnBack2Menu);
        panel.add(btnWithdraw);

        //-----Setting the location of the components
        lblPinNo.setBounds(40, 85, 80, 20);
        lblWithdrawAmount.setBounds(20, 125, 115, 20);

        txtPinNo.setBounds(140, 80, 150, 25);
        txtWithdrawAmount.setBounds(140, 120, 150, 25);

        btnBack2Menu.setBounds(50, 220, 65, 25);
        btnWithdraw.setBounds(180, 220, 100, 25);

        //-----Adding the an actionlistener to the buttons
        btnBack2Menu.addActionListener(this);
        btnWithdraw.addActionListener(this);
        btnBack2Menu.setToolTipText("Click To Back to Menu");

        lblPinNo.setOpaque(true);
        lblPinNo.setBackground(Color.black);
        lblPinNo.setForeground(Color.yellow);

        lblWithdrawAmount.setOpaque(true);
        lblWithdrawAmount.setBackground(Color.black);
        lblWithdrawAmount.setForeground(Color.yellow);
//place holder


        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Withdraw Transaction"));

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

        } else if (source == btnWithdraw) {
            try {
                //
                String spinNo = txtPinNo.getText();
                String sdepositamount = txtWithdrawAmount.getText();

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
                            int b = Integer.parseInt(txtWithdrawAmount.getText());
                            if (a >= b) {
                                if (b < 50) {
                                    JOptionPane.showMessageDialog(null, "The Minimum cash you can withdraw is 50$", "ATM", JOptionPane.INFORMATION_MESSAGE);
                                    txtWithdrawAmount.setText("");
                                } else {
                                    int n = JOptionPane.showConfirmDialog(null, "Are you Confirm to Withdraw " + b + "$", "Ok", JOptionPane.YES_NO_OPTION);
                                    if (n == JOptionPane.YES_OPTION) {
                                        int sub = a - b;
                                        JOptionPane.showMessageDialog(null, "Your Withdraw " + b + "$ is successful", "ATM", JOptionPane.INFORMATION_MESSAGE);
                                        preparedStatement = connection.prepareStatement("UPDATE UserAccountInfo SET Amount = '" + sub + "'WHERE AccountNo = '" + strAccNo + "'");
                                        preparedStatement.executeUpdate();
                                        txtPinNo.requestFocus(true);
                                        AddWithDrawHistory(b);
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
                                JOptionPane.showMessageDialog(null, "You have no sufficient Amount!", "Warning", JOptionPane.WARNING_MESSAGE);
                            }
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

    private void AddWithDrawHistory(int b) {

        try {
            DateAndTime dateAndTime = new DateAndTime();
            Statement statement1;
            PreparedStatement preparedStatement1;

            statement1 = connection.createStatement();
            preparedStatement1 = connection.prepareStatement("INSERT INTO TransactionHistory " + " (Time,Date,AccountNo,Type,Amount,ReceiverAccountNo) " + " VALUES(?,?,?,?,?,?)");
            preparedStatement1.setString(1, dateAndTime.getCurrentTime());
            preparedStatement1.setString(2, dateAndTime.getCurrentDate());
            preparedStatement1.setString(3, Login.currentLoginAccountNo);
            preparedStatement1.setString(4, "Withdraw");
            preparedStatement1.setString(5, String.valueOf(b));
            preparedStatement1.setString(6, "Owner");
            preparedStatement1.executeUpdate();
            statement1.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}