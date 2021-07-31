import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Registration extends JFrame implements ActionListener {
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


        Registration registration = new Registration();
        registration.setSize(windowWidth, windowHeight);
        registration.setVisible(true);
        registration.setResizable(false);
        registration.setLocation(winX, winY);
    }

    Font f1 = new Font("", Font.BOLD, 10);
    JLabel lblAccountNo = new JLabel("Account No ", JLabel.RIGHT);
    JLabel lblFName = new JLabel("First Name ", JLabel.RIGHT);
    JLabel lblLName = new JLabel("Last Name ", JLabel.RIGHT);

    JLabel lblPass = new JLabel("Pin Number ", JLabel.RIGHT);
    JLabel lblConfirmPass = new JLabel("Verify Pin Number ", JLabel.RIGHT);

    JLabel lblBday = new JLabel("Date of Birth ", JLabel.RIGHT);
    JLabel lblDay = new JLabel("Day");
    JLabel lblMonth = new JLabel("Month");
    JLabel lblYear = new JLabel("Year");

    JLabel lblCash = new JLabel("Cash Deposit ", JLabel.RIGHT);

    JTextField txtAccountNo = new JTextField(20);
    JTextField txtFName = new JTextField(20);
    JTextField txtLName = new JTextField(20);

    JPasswordField txtPassword = new JPasswordField(20);
    JPasswordField txtConfirmPass = new JPasswordField(20);

    JTextField txtDay = new JTextField(20);
    JTextField txtMonth = new JTextField(20);
    JTextField txtYear = new JTextField(20);
    JTextField txtCash = new JTextField(20);

    JButton btnRegistration = new JButton(new ImageIcon(assetsPath + "/reg.jpg"));

    Connection connection;
    Statement statement;
    PreparedStatement ps;
    ResultSet rs;

    public void clear() {
        txtAccountNo.setText("");
        txtFName.setText("");
        txtPassword.setText("");
        txtLName.setText("");
        txtConfirmPass.setText("");
    }

    public Registration() {
        super("ATM Booth");

        JPanel panel = new JPanel();
        panel.setLayout(null);

        lblYear.setFont(f1);
        lblDay.setFont(f1);
        lblMonth.setFont(f1);
        lblAccountNo.setBounds(5, 50, 120, 25);
        panel.add(lblAccountNo);
        txtAccountNo.setBounds(125, 50, 150, 25);
        panel.add(txtAccountNo);

        lblFName.setBounds(5, 85, 120, 25);
        panel.add(lblFName);
        txtFName.setBounds(125, 85, 150, 25);
        panel.add(txtFName);

        lblLName.setBounds(5, 120, 120, 25);
        panel.add(lblLName);
        txtLName.setBounds(125, 120, 150, 25);
        panel.add(txtLName);

        lblPass.setBounds(5, 155, 120, 25);
        panel.add(lblPass);
        txtConfirmPass.setBounds(125, 155, 150, 25);
        panel.add(txtConfirmPass);

        lblConfirmPass.setBounds(5, 190, 120, 25);
        panel.add(lblConfirmPass);
        txtPassword.setBounds(125, 190, 150, 25);
        panel.add(txtPassword);

        lblBday.setBounds(5, 225, 120, 25);
        panel.add(lblBday);

        txtDay.setBounds(125, 225, 50, 25);
        panel.add(txtDay);
        txtMonth.setBounds(175, 225, 50, 25);
        panel.add(txtMonth);
        txtYear.setBounds(225, 225, 50, 25);
        panel.add(txtYear);

        lblDay.setBounds(130, 210, 20, 20);
        panel.add(lblDay);
        lblMonth.setBounds(180, 210, 30, 20);
        panel.add(lblMonth);
        lblYear.setBounds(230, 210, 30, 20);
        panel.add(lblYear);

        lblCash.setBounds(5, 260, 120, 25);
        panel.add(lblCash);
        txtCash.setBounds(125, 260, 100, 25);
        panel.add(txtCash);

        //color of label
        lblAccountNo.setOpaque(true);
        lblAccountNo.setBackground(Color.black);
        lblAccountNo.setForeground(Color.yellow);

        lblFName.setOpaque(true);
        lblFName.setBackground(Color.black);
        lblFName.setForeground(Color.yellow);

        lblLName.setOpaque(true);
        lblLName.setBackground(Color.black);
        lblLName.setForeground(Color.yellow);

        lblPass.setOpaque(true);
        lblPass.setBackground(Color.black);
        lblPass.setForeground(Color.yellow);

        lblConfirmPass.setOpaque(true);
        lblConfirmPass.setBackground(Color.black);
        lblConfirmPass.setForeground(Color.yellow);

        lblBday.setOpaque(true);
        lblBday.setBackground(Color.black);
        lblBday.setForeground(Color.yellow);

        lblDay.setOpaque(true);
        lblDay.setBackground(Color.black);
        lblDay.setForeground(Color.yellow);

        lblMonth.setOpaque(true);
        lblMonth.setBackground(Color.black);
        lblMonth.setForeground(Color.yellow);

        lblYear.setOpaque(true);
        lblYear.setBackground(Color.black);
        lblYear.setForeground(Color.yellow);

        lblCash.setOpaque(true);
        lblCash.setBackground(Color.black);
        lblCash.setForeground(Color.yellow);

        btnRegistration.setBounds(129, 310, 120, 35);
        panel.add(btnRegistration);
        btnRegistration.addActionListener(this);

        JLabel lblBgImage = new JLabel(new ImageIcon(assetsPath + "/atmimage.jpg"));
        lblBgImage.setBounds(0, 0, windowWidth, windowHeight);
        panel.add(lblBgImage);

        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Registration Form"));
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

        if (source == btnRegistration) {
            String saccountno = txtAccountNo.getText();
            String sfname = txtFName.getText();
            String slname = txtLName.getText();
            String spass = txtPassword.getText();
            String sconfirmpass = txtConfirmPass.getText();
            String sday = txtDay.getText();
            String smon = txtMonth.getText();
            String syear = txtYear.getText();
            String Date = syear + "-" + smon + "-" + sday;
            String sAmount = txtCash.getText();
            try {
                if ((saccountno.length() == 0 || sfname.length() == 0 || spass.length() == 0 || slname.length() == 0 || sconfirmpass.length() == 0)) {
                    JOptionPane.showMessageDialog(null, "Some Fields are empty", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (spass.equals(sconfirmpass)) {

                        statement = connection.createStatement();
                        rs = statement.executeQuery("SELECT * FROM UserAccountInfo WHERE AccountNO = '" + saccountno + "'");
                        String strAcconutNO = "";
                        while (rs.next()) {
                            strAcconutNO = rs.getString(9);
                        }
                        //check account exist
                        if (strAcconutNO.equals(saccountno)) {
                            JOptionPane.showMessageDialog(null, "Account already exist !", "EXIST!", JOptionPane.WARNING_MESSAGE);
                        } else {
                            String strPassword = "";
                            rs = statement.executeQuery("SELECT * FROM UserAccountInfo WHERE Password = '" + spass + "'");
                            while (rs.next()) {
                                strPassword = rs.getString(4);
                            }
                            //check same pass
                            if (strPassword.equals(spass)) {
                                JOptionPane.showMessageDialog(null, "Please try with another password !", "EXIST!", JOptionPane.WARNING_MESSAGE);
                            } else {
                                //insert data in data base

                                //check date later
                                try {
                                    if (isValid(Date)) {
                                        int amount = Integer.parseInt(sAmount);
                                        if (amount >= 100) {
                                            statement = connection.createStatement();
                                            ps = connection.prepareStatement("INSERT INTO UserAccountInfo " + " (AccountNO,FirstName,LastName,Password,ConfirmPassword,BirthDay,BirthMonth,BirthYear,Amount) " + " VALUES(?,?,?,?,?,?,?,?,?)");
                                            ps.setString(1, txtAccountNo.getText());
                                            ps.setString(2, txtFName.getText());
                                            ps.setString(3, txtLName.getText());
                                            ps.setString(4, txtPassword.getText());
                                            ps.setString(5, txtConfirmPass.getText());
                                            ps.setString(6, txtDay.getText());
                                            ps.setString(7, txtMonth.getText());
                                            ps.setString(8, txtYear.getText());
                                            ps.setString(9, txtCash.getText());

                                            ps.executeUpdate();

                                            JOptionPane.showMessageDialog(null, "Your Account has been successfully Created.", "ATM", JOptionPane.INFORMATION_MESSAGE);
                                            txtAccountNo.requestFocus(true);
                                            statement.close();
                                            clear();

                                            Login log = new Login();
                                            log.setLocation(winX, winY);
                                            log.setSize(windowWidth, windowHeight);
                                            log.setTitle("Log-In");
                                            log.setResizable(false);
                                            log.setVisible(true);
                                            dispose();
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Your Deposit must be at least 100$.", "Warning", JOptionPane.WARNING_MESSAGE);
                                        }

                                    } else {
                                        JOptionPane.showMessageDialog(null, "Please Enter your Valid Birthdate", "Error", JOptionPane.WARNING_MESSAGE);
                                    }
                                } catch (DateTimeParseException ex) {
                                    System.out.println(ex.getErrorIndex());
                                }
                            }
                        }
                        statement.close();

                    } else {
                        JOptionPane.showMessageDialog(null, "Please Enter Both same password.", "ATM", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            } catch (SQLException sqlEx) {
                JOptionPane.showMessageDialog(null, "General error", "ATM", JOptionPane.INFORMATION_MESSAGE);
//                System.out.println(sqlEx);
            }

        }
    }

    //valid date
    public static boolean isValid(final String date) {
        boolean valid = false;

        try {

            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("uuuu-M-d")
                            .withResolverStyle(ResolverStyle.STRICT)
            );

            valid = true;

        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return valid;
    }

    //check valid date


}

		
		
					
					
  
 
 