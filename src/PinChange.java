import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PinChange extends JFrame implements ActionListener {
    static int winX = 400;
    static int winY = 150;
    static int windowWidth = 370;
    static int windowHeight = 400;
    String assetsPath = "E:\\Java\\ATM_BOOTH\\assets";

    public static void main(String[] args) {

        PinChange pinChange = new PinChange();
        pinChange.setSize(windowWidth, windowHeight);
        pinChange.setVisible(true);
        pinChange.setResizable(false);
        pinChange.setLocation(winX, winY);
    }


    Font f1 = new Font("", Font.BOLD, 10);

    JLabel lblCurrentPass = new JLabel("Current Pin ", JLabel.RIGHT);
    JLabel lblNewPass = new JLabel("New Pin ", JLabel.RIGHT);
    JLabel lblNewConfirmPass = new JLabel("Confirm New Pin ", JLabel.RIGHT);

    JButton btnPinChange = new JButton(new ImageIcon(assetsPath + "/pinchange.png"));
    JButton btnBack2Menu = new JButton(new ImageIcon(assetsPath + "/menu.png"));

    JPasswordField txtCurrentPass = new JPasswordField(20);
    JPasswordField txtNewPass = new JPasswordField(20);
    JPasswordField txtNewConfirmPass = new JPasswordField(20);

    Connection connection;
    Statement statement;
    PreparedStatement preparedStatement;

    public void clear() {
        txtCurrentPass.setText("");
        txtNewPass.setText("");
        txtNewConfirmPass.setText("");
    }

    public PinChange() {
        super("ATM Booth");

        JPanel panel = new JPanel();
        panel.setLayout(null);

        lblCurrentPass.setBounds(5, 90, 120, 25);
        panel.add(lblCurrentPass);
        txtCurrentPass.setBounds(140, 90, 150, 25);
        panel.add(txtCurrentPass);

        lblNewPass.setBounds(5, 130, 120, 25);
        panel.add(lblNewPass);
        txtNewPass.setBounds(140, 130, 150, 25);
        panel.add(txtNewPass);

        lblNewConfirmPass.setBounds(5, 160, 120, 25);
        panel.add(lblNewConfirmPass);
        txtNewConfirmPass.setBounds(140, 160, 150, 25);
        panel.add(txtNewConfirmPass);

        btnPinChange.setBounds(190, 275, 120, 25);
        btnBack2Menu.setBounds(20, 275, 60, 25);
//label color
        lblCurrentPass.setOpaque(true);
        lblCurrentPass.setBackground(Color.black);
        lblCurrentPass.setForeground(Color.yellow);

        lblNewPass.setOpaque(true);
        lblNewPass.setBackground(Color.black);
        lblNewPass.setForeground(Color.yellow);

        lblNewConfirmPass.setOpaque(true);
        lblNewConfirmPass.setBackground(Color.black);
        lblNewConfirmPass.setForeground(Color.yellow);

//pin change button event
        panel.add(btnPinChange);
        btnBack2Menu.setForeground(Color.yellow);
        btnBack2Menu.setBackground(Color.black);
        btnBack2Menu.addActionListener(this);

        panel.add(btnBack2Menu);
        btnPinChange.setForeground(Color.yellow);
        btnPinChange.setBackground(Color.black);
        btnPinChange.addActionListener(this);

        JLabel lblBgImage = new JLabel(new ImageIcon(assetsPath + "/atmimage.jpg")); //background image
        lblBgImage.setBounds(0, 0, windowWidth, windowHeight);
        panel.add(lblBgImage);

        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Pin Change"));

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

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnBack2Menu) {
            QuerySection panel = new QuerySection();
            panel.setSize(windowWidth, windowHeight);
            panel.setVisible(true);
            panel.setResizable(false);
            panel.setLocation(winX, winY);
            dispose();
        }
//read user pass and then update pass
        if (source == btnPinChange) {
            try {
                String scurrentpass = txtCurrentPass.getText();
                String snewpass = txtNewPass.getText();
                String snewconfirmpass = txtNewConfirmPass.getText();

                if ((scurrentpass.length() == 0 || snewpass.length() == 0 || snewconfirmpass.length() == 0)) {
                    JOptionPane.showMessageDialog(null, "Some Fields are empty", "WARNING", JOptionPane.WARNING_MESSAGE);
//                    clear();
                } else {
                    statement = connection.createStatement();
                    String strAccNo = "";
                    String strPass = "";

                    ResultSet rs = statement.executeQuery("SELECT * FROM UserAccountInfo WHERE AccountNo='" + Login.currentLoginAccountNo + "'");
                    while (rs.next()) {
                        strAccNo = rs.getString("AccountNo");
                        strPass = rs.getString("Password");
                    }
                    ResultSet rs1 = statement.executeQuery("SELECT * FROM UserAccountInfo WHERE Password='" + snewpass + "'");
                    String strRepeatPass = "";
                    while (rs1.next()) {
                        strRepeatPass = rs1.getString("Password");
                    }

                    if (!(strAccNo.equals(""))) {//accno match
                        if (strPass.equals(scurrentpass)) {
                            if (snewpass.equals(snewconfirmpass)) {
                                if (snewpass.equals(strPass)) {
                                    JOptionPane.showMessageDialog(null, "new password same as old password!!", "Warning", JOptionPane.WARNING_MESSAGE);
                                } else {
                                    if (snewpass.equals(strRepeatPass)) {
                                        JOptionPane.showMessageDialog(null, "Please Enter another new password!", "Warning", JOptionPane.WARNING_MESSAGE);
                                    } else {
                                        int n = JOptionPane.showConfirmDialog(null, "Are you sure want to change your pin?", "Confirm", JOptionPane.YES_NO_OPTION);
                                        if (n == JOptionPane.YES_OPTION) {
//                                    clear();
                                            JOptionPane.showMessageDialog(null, "Your Pin has been successfully Changed.", "ATM", JOptionPane.INFORMATION_MESSAGE);

                                            String sql = "update UserAccountInfo SET Password='" + txtNewPass.getText() + "',ConfirmPassword='" + txtNewConfirmPass.getText() + "'WHERE AccountNo='" + Login.currentLoginAccountNo + "'";
                                            preparedStatement = connection.prepareStatement(sql);
                                            preparedStatement.executeUpdate();
                                            txtNewPass.requestFocus(true);

                                            //move to login window
                                            Login log = new Login();
                                            log.setLocation(winX, winY);
                                            log.setSize(windowWidth, windowHeight);
                                            log.setTitle("Log-In");
                                            log.setResizable(false);
                                            log.setVisible(true);
                                            dispose();
                                        }
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Both Password! Not match", "Security Pass", JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Enter valid password!", "Security Pass", JOptionPane.WARNING_MESSAGE);
                            txtCurrentPass.requestFocus(true);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, " You must login first!!!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    statement.close();
                }
            } catch (SQLException sqlEx) {
                JOptionPane.showMessageDialog(null, "General error", "ATM", JOptionPane.INFORMATION_MESSAGE);
//                System.out.println(sqlEx);
            }
        }
    }
}

