import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;

public class TransactionHistory extends JFrame implements ActionListener {
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

        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setLocation(winX, winY);
        transactionHistory.setSize(windowWidth, windowHeight);
        transactionHistory.setTitle("Transaction History");
        transactionHistory.setResizable(false);
        transactionHistory.setVisible(true);
    }

    Connection connection;
    //ResultSet rs;
    Statement statement;
    PreparedStatement preparedStatement;

    public TransactionHistory() {
        super("ATM Booth");
        DateAndTime dateAndTime = new DateAndTime();
//        System.out.println(dateAndTime.getCurrentDate());
//        System.out.println(dateAndTime.getCurrentTime());
        GlobalVariable globalVariable = new GlobalVariable();

        System.out.println("Height: " + globalVariable.getWindowWidth());

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblBgImage = new JLabel(new ImageIcon(assetsPath + "/atmimage.jpg"));
        lblBgImage.setBounds(0, 0, windowWidth, windowHeight);
        panel.add(lblBgImage);

        setContentPane(panel);
//        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close when click cross
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Transaction History"));

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

        //add data into database

        try {
//            DateAndTime dateAndTime = new DateAndTime();
            Statement statement1;
            PreparedStatement preparedStatement1;

            statement1 = connection.createStatement();
            preparedStatement1 = connection.prepareStatement("INSERT INTO TransactionHistory " + " (Time,Date,AccountNo,Type,Amount,ReceiverAccountNo) " + " VALUES(?,?,?,?,?,?)");
            preparedStatement1.setString(1, dateAndTime.getCurrentTime());
            preparedStatement1.setString(2, dateAndTime.getCurrentDate());
            preparedStatement1.setString(3, Login.currentLoginAccountNo);
            preparedStatement1.setString(4, "Deposit");
            preparedStatement1.setString(5, String.valueOf(100));
            preparedStatement1.setString(6, "");
            preparedStatement1.executeUpdate();
            statement1.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
