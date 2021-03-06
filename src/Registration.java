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
    static int winX = 400;
    static int winY = 150;
    static int windowWidth = 370;
    static int windowHeight = 400;
    String  assetsPath= "E:\\Java\\ATM_BOOTH\\assets";

    public static void main(String[] args) {
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
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public void clear() {
        txtAccountNo.setText("");
        txtFName.setText("");
        txtLName.setText("");
        txtPassword.setText("");
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
        DateAndTime dateAndTime = new DateAndTime();
        GlobalVariable globalVariable = new GlobalVariable();

        Object source = e.getSource();

        if (source == btnRegistration) {
            String sAccountNo = txtAccountNo.getText();
            String sfname = txtFName.getText();
            String slname = txtLName.getText();
            String spass = txtPassword.getText();
            String sconfirmpass = txtConfirmPass.getText();
            String sday = txtDay.getText();
            String smon = txtMonth.getText();
            String syear = txtYear.getText();
            String birthDate = sday + "-" + smon + "-" + syear;
            String sAmount = txtCash.getText();
            try {
                if ((sAccountNo.length() == 0 || sfname.length() == 0 || spass.length() == 0 || slname.length() == 0 || sconfirmpass.length() == 0)) {
                    JOptionPane.showMessageDialog(null, "Some Fields are empty", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (spass.equals(sconfirmpass)) {

                        statement = connection.createStatement();
                        resultSet = statement.executeQuery("SELECT * FROM UserAccountInfo WHERE AccountNo = '" + sAccountNo + "'");
                        String strAcconutNO = "";
                        while (resultSet.next()) {
                            strAcconutNO = resultSet.getString("AccountNo");
                        }
                        //check account exist
                        if (strAcconutNO.equals(sAccountNo)) {
                            JOptionPane.showMessageDialog(null, "Account already exist !", "EXIST!", JOptionPane.WARNING_MESSAGE);
                        } else {
                            String strPassword = "";
                            resultSet = statement.executeQuery("SELECT * FROM UserAccountInfo WHERE Password = '" + spass + "'");
                            while (resultSet.next()) {
                                strPassword = resultSet.getString("Password");
                            }
                            //check same pass
                            if (strPassword.equals(spass)) {
                                JOptionPane.showMessageDialog(null, "Please try with another password !", "EXIST!", JOptionPane.WARNING_MESSAGE);
                            } else {
                                //insert data in data base

                                //check date later
                                try {
                                    if (isValid(birthDate)) {
                                        int amount = Integer.parseInt(sAmount);
                                        if (amount >= 100) {

                                            statement = connection.createStatement();
                                            preparedStatement = connection.prepareStatement("INSERT INTO UserAccountInfo " + " (Time,Date,AccountNo,FirstName,LastName,Password,ConfirmPassword,BirthDate,Amount) " + " VALUES(?,?,?,?,?,?,?,?,?)");
                                            preparedStatement.setString(1, dateAndTime.getCurrentTime());
                                            preparedStatement.setString(2, dateAndTime.getCurrentDate());
                                            preparedStatement.setString(3, txtAccountNo.getText());
                                            preparedStatement.setString(4, txtFName.getText());
                                            preparedStatement.setString(5, txtLName.getText());
                                            preparedStatement.setString(6, txtPassword.getText());
                                            preparedStatement.setString(7, txtConfirmPass.getText());
                                            preparedStatement.setString(8, birthDate);
                                            preparedStatement.setString(9, txtCash.getText());

                                            preparedStatement.executeUpdate();

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
                System.out.println(sqlEx);
            }

        }
    }

    //valid date
    public static boolean isValid(final String date) {
        boolean valid = false;

        try {

            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("d-M-uuuu")
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

		
		
					
					
  
 
 