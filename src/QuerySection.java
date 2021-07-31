import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class QuerySection extends JFrame implements ActionListener {
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

        QuerySection querySection = new QuerySection();
        querySection.setSize(windowWidth, windowHeight);
        querySection.setVisible(true);
        querySection.setResizable(false);
        querySection.setLocation(winX, winY);
    }


    JButton btnBalanceEnquiry = new JButton("Balance Inquiry");
    JButton btnDeposit = new JButton("Deposit");
    JButton btnWithdraw = new JButton("Withdraw");
    JButton btnTransferBalance = new JButton("Balance Transfer");
    JButton btnPinChange = new JButton("Pin Change");
    JButton btnHistory = new JButton("Transaction History");

    JButton btnLogOut = new JButton(new ImageIcon(assetsPath + "/logout.png"));

    Connection connection;

    public QuerySection() {
        super("ATM Booth");

        JPanel panel = new JPanel();
        panel.setLayout(null);
        btnBalanceEnquiry.setBounds(15, 40, 150, 25);
        panel.add(btnBalanceEnquiry);
        btnBalanceEnquiry.addActionListener(this);
        btnBalanceEnquiry.setForeground(Color.yellow);
        btnBalanceEnquiry.setBackground(Color.black);

        btnTransferBalance.setBounds(15, 100, 150, 25);
        panel.add(btnTransferBalance);
        btnTransferBalance.addActionListener(this);
        btnTransferBalance.setForeground(Color.yellow);
        btnTransferBalance.setBackground(Color.black);

        btnDeposit.setBounds(200, 40, 150, 25);
        panel.add(btnDeposit);
        btnDeposit.addActionListener(this);
        btnDeposit.setForeground(Color.yellow);
        btnDeposit.setBackground(Color.black);

        btnWithdraw.setBounds(200, 100, 150, 25);
        panel.add(btnWithdraw);
        btnWithdraw.addActionListener(this);
        btnWithdraw.setForeground(Color.yellow);
        btnWithdraw.setBackground(Color.black);

        btnPinChange.setBounds(15, 150, 150, 25);
        panel.add(btnPinChange);
        btnPinChange.addActionListener(this);
        btnPinChange.setForeground(Color.yellow);
        btnPinChange.setBackground(Color.black);

        btnHistory.setBounds(200, 150, 150, 25);
        panel.add(btnHistory);
        btnHistory.addActionListener(this);
        btnHistory.setForeground(Color.yellow);
        btnHistory.setBackground(Color.black);


        btnLogOut.setBounds(125, 250, 90, 30);
        panel.add(btnLogOut);
        btnLogOut.addActionListener(this);

        JLabel lblBgImage = new JLabel(new ImageIcon(assetsPath + "/atmimage.jpg"));//back image
        lblBgImage.setBounds(0, 0, windowWidth, windowHeight);
        panel.add(lblBgImage);

        setContentPane(panel);
//        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close when click cross
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Select Transaction"));

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

        if (source == btnBalanceEnquiry) {

            BalanceEnquiry balanceEnquiry = new BalanceEnquiry();

            balanceEnquiry.setLocation(winX, winY);
            balanceEnquiry.setSize(windowWidth, windowHeight);
            balanceEnquiry.setTitle("Balance Inquiry");
            balanceEnquiry.setResizable(false);
            balanceEnquiry.setVisible(true);
            dispose();
        } else if (source == btnDeposit) {
            Deposit deposit = new Deposit();
            deposit.setLocation(winX, winY);
            deposit.setSize(windowWidth, windowHeight);
            deposit.setResizable(false);
            deposit.setVisible(true);
            dispose();
        } else if (source == btnWithdraw) {
            WithDraw withDraw = new WithDraw();
            withDraw.setLocation(winX, winY);
            withDraw.setSize(windowWidth, windowHeight);
            withDraw.setResizable(false);
            withDraw.setVisible(true);
            dispose();
        } else if (source == btnTransferBalance) {
            BalanceTransfer balanceTransfer = new BalanceTransfer();
            balanceTransfer.setLocation(winX, winY);
            balanceTransfer.setSize(windowWidth, windowHeight);
            balanceTransfer.setResizable(false);
            balanceTransfer.setVisible(true);
            dispose();
        } else if (source == btnPinChange) {
            PinChange pinChange = new PinChange();
            pinChange.setLocation(winX, winY);
            pinChange.setSize(windowWidth, windowHeight);
            pinChange.setResizable(false);
            pinChange.setVisible(true);
            dispose();
        } else if (source == btnHistory) {
            TransactionHistory transactionHistory = new TransactionHistory();
            transactionHistory.setLocation(winX, winY);
            transactionHistory.setSize(windowWidth, windowHeight);
            transactionHistory.setResizable(false);
            transactionHistory.setVisible(true);
            dispose();
        }

        if (source == btnLogOut) {

            int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
//                JOptionPane.showMessageDialog(null, "Good Bye", "ATM", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);

                //never enter here
                Login login = new Login();
                login.setLocation(winX, winY);
                login.setSize(windowWidth, windowHeight);
                login.setTitle("Log-In");
                login.setResizable(false);
                login.setVisible(true);
                dispose();
            }
        }
    }
}