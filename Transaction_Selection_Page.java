import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.swing.SwingConstants;
import javax.swing.JButton;


public class Transaction_Selection_Page {

	private JFrame frame;
	private static Transaction_Selection_Page window;
	static ATM_machine atm;
	static OutputStream out;
	static PrintWriter p;
	/**
	 * Launch the application.
	 */
	public static void NewScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Transaction_Selection_Page();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			out = ATM_machine.socket.getOutputStream();
	        p = new PrintWriter(out,true);
	        
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public Transaction_Selection_Page() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 200, 500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTransactionSelector = new JLabel("Transaction Selector");
		lblTransactionSelector.setHorizontalAlignment(SwingConstants.CENTER);
		lblTransactionSelector.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTransactionSelector.setBounds(84, 23, 315, 20);
		frame.getContentPane().add(lblTransactionSelector);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 54, 464, 5);
		frame.getContentPane().add(separator);
		
		JButton Withdraw_Button = new JButton("Withdraw");
		Withdraw_Button.setBounds(146, 62, 199, 23);
		frame.getContentPane().add(Withdraw_Button);
		Withdraw_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				try {
					@SuppressWarnings("unused")
					Withdraw_Page nw=new Withdraw_Page();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				Withdraw_Page.newWithdrawPage();
				window.frame.setVisible(false);
			}
			});
		
		JButton Deposit_Button = new JButton("Deposit");
		Deposit_Button.setBounds(146, 96, 199, 23);
		frame.getContentPane().add(Deposit_Button);
		Deposit_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				try {
					@SuppressWarnings("unused")
					Deposit_Page nw=new Deposit_Page();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				Deposit_Page.newDepositPage();
				window.frame.setVisible(false);
				
			}
			});
		
		JButton Transfer_Button = new JButton("Transfer");
		Transfer_Button.setBounds(146, 130, 199, 23);
		frame.getContentPane().add(Transfer_Button);
		Transfer_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				try {
					@SuppressWarnings("unused")
					Transfer_Page nw=new Transfer_Page();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				Transfer_Page.newTransferPage();
				window.frame.setVisible(false);
			}
			});
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 202, 464, 5);
		frame.getContentPane().add(separator_1);
		
		JButton Exit_Button = new JButton("EXIT");
		Exit_Button.setBounds(194, 218, 108, 23);
		frame.getContentPane().add(Exit_Button);
		
		JButton Balance = new JButton("Balance");
		Balance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				@SuppressWarnings("unused")
				Balance_page nw = new Balance_page();
				Balance_page.newHistoryPage();
				window.frame.setVisible(false);
			}
		});
		Balance.setBounds(146, 164, 199, 23);
		frame.getContentPane().add(Balance);

		Exit_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				String response =null;
				System.out.println("this is: "+ ATM_machine.ATMNum.getText());
				//JOptionPane.showMessageDialog(null, "Please wait while we complete your transaction...");
				String send = "5"+","+ ATM_machine.ATMNum.getText().trim() +"," +ATM_machine.cardno;
				
				p.println(send);
				BufferedReader in;
				try {
					in = new BufferedReader(new InputStreamReader(ATM_machine.socket.getInputStream()));
			        response = in.readLine();
			        System.out.println(response.trim());
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
				@SuppressWarnings("unused")
				ATM_machine page2=new ATM_machine();
				ATM_machine.ATMachine();
				window.frame.setVisible(false);
			}
			});
}	
}
