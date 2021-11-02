import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;

public class TransactionHistory extends JFrame implements ActionListener {
    static int winX = 400;
    static int winY = 150;
    static int windowWidth = 370;
    static int windowHeight = 400;
    String  assetsPath= "E:\\Java\\ATM_BOOTH\\assets";
    public static void main(String[] args) {
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setLocation(winX, winY);
        transactionHistory.setSize(windowWidth, windowHeight);
        transactionHistory.setTitle("Transaction History");
        transactionHistory.setResizable(false);
        transactionHistory.setVisible(true);
    }

    Connection connection;
    //ResultSet resultSet;
    Statement statement;
    PreparedStatement preparedStatement;

    public TransactionHistory() {
        super("ATM Booth");
        DateAndTime dateAndTime = new DateAndTime();
        GlobalVariable globalVariable = new GlobalVariable();


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


    }

    @Override
    public void actionPerformed(ActionEvent e) {
//do some thing here
    }


}
