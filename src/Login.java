import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    static String currentLoginAccountNo = "";//storage for all class to validate accurate user
    static int windowWidth, windowHeight;
    static int winX, winY;
    String  assetsPath= "E:\\Java\\ATM_BOOTH\\assets";
    public static void main(String[] args) {
        GlobalVariable globalVariable = new GlobalVariable();
        windowWidth=globalVariable.getWindowWidth();
        windowHeight=globalVariable.getWindowHeight();
        winX=globalVariable.getWinX();
        winY=globalVariable.getWinY();

        Login login = new Login();
        login.setLocation(winX, winY);
        login.setSize(windowWidth, windowHeight);
        login.setTitle("Log-In");
        login.setResizable(false);
        login.setVisible(true);
    }

    JTextField txtAccountNo = new JTextField(25);
    JPasswordField txtPassword = new JPasswordField(25);
    JLabel lblAccountNo = new JLabel("Account No ");
    JLabel lblPassword = new JLabel("Pin Number ");
    JButton btnLogin = new JButton(new ImageIcon(assetsPath + "/login.png"));
    JButton btnRegister = new JButton(new ImageIcon(assetsPath + "/registration.png"));

    Connection connection;
    Statement statement;

    public Login() {
        super("ATM Booth");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        //----Adding Components into your Frame
        panel.add(txtAccountNo);
        panel.add(txtPassword);
        panel.add(lblAccountNo);
        panel.add(lblPassword);
        panel.add(btnLogin);
        panel.add(btnRegister);


        //-----Setting the location of the components
        int x = 40, y = 100;
        lblAccountNo.setBounds(10 + x, 20 + y, 80, 20);
        txtAccountNo.setBounds(100 + x, 15 + y, 160, 25);

        lblPassword.setBounds(10 + x, 50 + y, 80, 20);
        txtPassword.setBounds(100 + x, 45 + y, 160, 25);

        btnLogin.setBounds(170 + x, 120 + y, 65, 30);
        btnRegister.setBounds(20 + x, 120 + y, 125, 30);

        //color
        lblAccountNo.setOpaque(true);
        lblAccountNo.setBackground(Color.black);
        lblAccountNo.setForeground(Color.yellow);

        lblPassword.setOpaque(true);
        lblPassword.setBackground(Color.black);
        lblPassword.setForeground(Color.yellow);
        //-----Adding the an actionlistener to the buttons
        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);

        //add place holder
        PlaceHolder txtAccountPlaceholder = new PlaceHolder("Enter Account no", txtAccountNo);
        txtAccountPlaceholder.changeAlpha(0.75f);
        txtAccountPlaceholder.changeStyle(Font.ITALIC);

        PlaceHolder txtPasswordPlaceholder = new PlaceHolder("Enter password", txtPassword);
        txtPasswordPlaceholder.changeAlpha(0.75f);
        txtPasswordPlaceholder.changeStyle(Font.ITALIC);


        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblBgImage = new JLabel(new ImageIcon(assetsPath + "/atmimage.jpg"));
        lblBgImage.setBounds(0, 0, windowWidth, windowHeight);
        panel.add(lblBgImage);

        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Login"));
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
        if (source == btnLogin) {
            try {
                String str1 = txtAccountNo.getText();
                String str2 = txtPassword.getText();
                if ((str1.length() == 0 || str2.length() == 0)) {
                    JOptionPane.showMessageDialog(null, "Please fill all field", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    statement = connection.createStatement();
                    String strAccount = "";
                    String strPass = "";
                    String strFname = "";
                    String strLname = "";

                    ResultSet rs = statement.executeQuery("SELECT * FROM UserAccountInfo WHERE AccountNo='" + str1 + "'");
                    while (rs.next()) {
                        strAccount = rs.getString("AccountNo");
                        strPass = rs.getString("Password");
                        strFname = rs.getString("FirstName");
                        strLname = rs.getString("LastName");
                    }

                    statement.close();

                    if (strAccount.equals(str1)) {
                        if (strPass.equals(str2)) {
                            currentLoginAccountNo = strAccount;
                            JOptionPane.showMessageDialog(null, "Welcome " + strFname + " " + strLname, "Welcome", JOptionPane.INFORMATION_MESSAGE);
                            QuerySection querySection = new QuerySection();
                            querySection.setSize(windowWidth, windowHeight);
                            querySection.setVisible(true);
                            querySection.setResizable(false);
                            querySection.setLocation(winX, winY);
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "Please Enter your correct password!", "Security Pass", JOptionPane.WARNING_MESSAGE);
                            txtPassword.requestFocus(true);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Account Not Found!", "Security Pass", JOptionPane.WARNING_MESSAGE);
                        txtAccountNo.requestFocus(true);
                    }
                }
            } catch (SQLException ignored) {
            }
        } else if (source == btnRegister) {
            Registration panel = new Registration();
            panel.setSize(windowWidth, windowHeight);
            panel.setVisible(true);
            panel.setResizable(false);
            panel.setLocation(winX, winY);
            dispose();
        }
    }


}
