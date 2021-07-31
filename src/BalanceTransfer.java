import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class BalanceTransfer extends JFrame implements ActionListener {
    static int windowWidth, windowHeight;
    static int winX, winY;
    static String assetsPath;
    public static void main(String[] args) {
        GlobalVariable globalVariable = new GlobalVariable();
        windowWidth=globalVariable.getWindowWidth();
        windowHeight=globalVariable.getWindowHeight();
        winX=globalVariable.getWinX();
        winY=globalVariable.getWinY();
        assetsPath=globalVariable.getAssetsPath();

        BalanceTransfer balanceTransfer = new BalanceTransfer();
        balanceTransfer.setLocation(winX, winY);
        balanceTransfer.setSize(windowWidth, windowHeight);
        balanceTransfer.setResizable(false);
        balanceTransfer.setVisible(true);
    }

    JPasswordField txtSenderPin = new JPasswordField(25);
    JTextField txtTransferAmount = new JTextField(25);
    JTextField txtReceiverAccountNo = new JTextField(25);

    //sender
    JLabel lblSenderPin = new JLabel("Your Pin Number ", JLabel.RIGHT);
    JLabel lblTransferAmount = new JLabel("Transfer Amount ", JLabel.RIGHT);

    //receiver
    JLabel lblReceiverAccountNo = new JLabel("Receiver Account No ", JLabel.RIGHT);

    JButton btnBack2Menu = new JButton(new ImageIcon(assetsPath + "/menu.png"));
    JButton btnTransferBalance = new JButton(new ImageIcon(assetsPath + "/transferbalance.png"));

    JLabel lblSenderTxt = new JLabel("Your Account");
    JLabel lblReceiverTxt = new JLabel("Receiver Account");

    Connection connection;
    //ResultSet rs;
    Statement statement;
    PreparedStatement ps;

    public BalanceTransfer() {
        super("ATM Booth");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        //----Adding Components into your Frame
        panel.add(lblSenderPin);
        panel.add(txtSenderPin);
        panel.add(lblTransferAmount);
        panel.add(txtTransferAmount);
        panel.add(lblReceiverAccountNo);
        panel.add(txtReceiverAccountNo);
        panel.add(btnBack2Menu);
        panel.add(btnTransferBalance);
        panel.add(lblSenderTxt);
        panel.add(lblReceiverTxt);

        //-----Setting the location of the components
        lblSenderTxt.setBounds(10, 20, 100, 20);
        lblReceiverTxt.setBounds(10, 110, 100, 20);

        lblSenderPin.setBounds(20, 50, 100, 20);
        txtSenderPin.setBounds(150, 50, 140, 25);

        lblTransferAmount.setBounds(20, 80, 100, 20);
        txtTransferAmount.setBounds(150, 80, 140, 25);

        lblReceiverAccountNo.setBounds(20, 150, 125, 20);
        txtReceiverAccountNo.setBounds(150, 150, 140, 25);

        btnTransferBalance.setBounds(150, 250, 170, 25);
        btnBack2Menu.setBounds(50, 250, 60, 25);

        //------//
        lblSenderPin.setOpaque(true);
        lblSenderPin.setBackground(Color.black);
        lblSenderPin.setForeground(Color.yellow);

        lblTransferAmount.setOpaque(true);
        lblTransferAmount.setBackground(Color.black);
        lblTransferAmount.setForeground(Color.yellow);

        lblReceiverAccountNo.setOpaque(true);
        lblReceiverAccountNo.setBackground(Color.black);
        lblReceiverAccountNo.setForeground(Color.yellow);

        //-----Adding the an actionlistener to the buttons
        btnBack2Menu.addActionListener(this);
        btnTransferBalance.addActionListener(this);

        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Transfer Balance"));

        btnTransferBalance.setToolTipText("Click First then Enter Your Money to Transfer");
        btnBack2Menu.setToolTipText("Click To Back to Menu");

        //background
        lblSenderTxt.setOpaque(true);
        lblSenderTxt.setBackground(Color.black);
        lblSenderTxt.setForeground(Color.red);

        lblReceiverTxt.setOpaque(true);
        lblReceiverTxt.setBackground(Color.black);
        lblReceiverTxt.setForeground(Color.red);

        JLabel lblBgImage = new JLabel(new ImageIcon(assetsPath + "/atmimage.jpg"));
        panel.add(lblBgImage);
        lblBgImage.setBounds(0, 0, windowWidth, windowHeight);

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
        } else if (source == btnTransferBalance) {
            try {
                String sSenderPin = txtSenderPin.getText();
                String sTransferMoney = txtTransferAmount.getText();
                String sReceiverAccountNo = txtReceiverAccountNo.getText();

                //check all field is empty or not
                //then start process
                if (sSenderPin.length() == 0 || sTransferMoney.length() == 0 || sReceiverAccountNo.length() == 0) {
                    JOptionPane.showMessageDialog(null, "Some Fields are empty", "WARNING", JOptionPane.WARNING_MESSAGE);
//                    clear();
                } else {
                    //Read Check then update
                    String strSenderAccNo = "";
                    String strReadSenderPin = "";
                    String strReadTransferAmount = "";
                    statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT * FROM UserAccountInfo WHERE AccountNO ='" + Login.currentLoginAccountNo + "'");

                    while (rs.next()) {
                        strSenderAccNo = rs.getString(9);
                        strReadSenderPin = rs.getString(4);
                        strReadTransferAmount = rs.getString(8);
                    }
                    if (strSenderAccNo.length() != 0) {
                        if (strReadSenderPin.equals(txtSenderPin.getText())) {

                            int a = Integer.parseInt(sTransferMoney); //want to send
                            int b = Integer.parseInt(strReadTransferAmount); //actual account balance
                            if ((a < b) && a >= 0) {
                                //sending process
                                //check user exist or not
                                String strReceiverAccNo = "";
                                String strReceiverCurrentBalance = "";
                                ResultSet rs1 = statement.executeQuery("SELECT * FROM UserAccountInfo WHERE AccountNO ='" + txtReceiverAccountNo.getText() + "'");
                                while (rs1.next()) {
                                    strReceiverAccNo = rs1.getString(9);
                                    strReceiverCurrentBalance = rs1.getString(8);
                                }
                                //check receiver
                                if (strSenderAccNo.equals(strReceiverAccNo)) {

                                    JOptionPane.showMessageDialog(null, "You Can not transfer own account", "Warning", JOptionPane.WARNING_MESSAGE);
                                } else {
                                    if (strReceiverAccNo.equals(txtReceiverAccountNo.getText())) {
                                        //update both balance
                                        int currentSenderBalance = b - a;
                                        int currentReceiverBalance = Integer.parseInt(strReceiverCurrentBalance) + a;
                                        //confirmation sending
                                        int n = JOptionPane.showConfirmDialog(null, "Are you sure want to Transfer " + a + "$ ?", "Confirm", JOptionPane.YES_NO_OPTION);
                                        if (n == JOptionPane.YES_OPTION) {
//                                    clear();
                                            JOptionPane.showMessageDialog(null, "Your has been successfully Transfered " + a + "$", "ATM", JOptionPane.INFORMATION_MESSAGE);

                                            String sql1 = "update UserAccountInfo SET Amount='" + currentSenderBalance + "'WHERE AccountNO='" + strSenderAccNo + "'";
                                            String sql2 = "update UserAccountInfo SET Amount='" + currentReceiverBalance + "'WHERE AccountNO='" + strReceiverAccNo + "'";

                                            //updater sender account
                                            PreparedStatement ps1 = connection.prepareStatement(sql1);
                                            ps1.executeUpdate();
                                            txtSenderPin.requestFocus(true);
                                            //update receiver account
                                            PreparedStatement ps2 = connection.prepareStatement(sql2);
                                            ps2.executeUpdate();
                                            txtReceiverAccountNo.requestFocus(true);
                                            AddTransferHistory(a, strReceiverAccNo);
                                            //back to menu
                                            QuerySection querySection = new QuerySection();
                                            querySection.setSize(windowWidth, windowHeight);
                                            querySection.setVisible(true);
                                            querySection.setResizable(false);
                                            querySection.setLocation(winX, winY);
                                            dispose();
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Receiver Not Found!!", "Warning", JOptionPane.WARNING_MESSAGE);
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "You have no sufficient Balance", "Warning", JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Please Enter your correct pin!", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "You must need login", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    statement.close();
                    //end process
                }
            } catch (SQLException sqlEx) {
                JOptionPane.showMessageDialog(null, "General error", "ATM", JOptionPane.INFORMATION_MESSAGE);
                System.out.println(sqlEx);
            }
        }
    }

    private void AddTransferHistory(int a, String strReceiverAccNo) {

        try {
            DateAndTime dateAndTime = new DateAndTime();
            Statement statement1;
            PreparedStatement preparedStatement1;

            statement1 = connection.createStatement();
            preparedStatement1 = connection.prepareStatement("INSERT INTO TransactionHistory " + " (Time,Date,AccountNo,Type,Amount,ReceiverAccountNo) " + " VALUES(?,?,?,?,?,?)");
            preparedStatement1.setString(1, dateAndTime.getCurrentTime());
            preparedStatement1.setString(2, dateAndTime.getCurrentDate());
            preparedStatement1.setString(3, Login.currentLoginAccountNo);
            preparedStatement1.setString(4, "Transfer");
            preparedStatement1.setString(5, String.valueOf(a));
            preparedStatement1.setString(6, strReceiverAccNo);
            preparedStatement1.executeUpdate();
            statement1.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}