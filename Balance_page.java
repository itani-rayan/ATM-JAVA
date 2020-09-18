import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Balance_page {

	private JFrame frame;
	static Balance_page window;
	static ATM_machine atm;
	static OutputStream out;
	static PrintWriter p;
	private JTextField AccountNo;
	/**
	 * Launch the application.
	 */
	public static void newHistoryPage() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Balance_page();
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
	public Balance_page() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton button = new JButton("<");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				Transaction_Selection_Page page2=new Transaction_Selection_Page();
				Transaction_Selection_Page.NewScreen();
				window.frame.setVisible(false);
			}
		});
		button.setBounds(10, 11, 41, 23);
		frame.getContentPane().add(button);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(10, 77, 414, 98);
		frame.getContentPane().add(textArea);
		
		JLabel lblAccountNumber = new JLabel("Account Number");
		lblAccountNumber.setBounds(61, 15, 115, 14);
		frame.getContentPane().add(lblAccountNumber);
		
		AccountNo = new JTextField();
		AccountNo.setBounds(175, 12, 86, 20);
		frame.getContentPane().add(AccountNo);
		AccountNo.setColumns(10);
		
		JButton get_balance = new JButton("Get Balance");
		get_balance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String response = null;
				String bal = "Balan";
				String send = "6"+","+ATM_machine.cardno+","+ AccountNo.getText()+ "," +bal+","+AccountNo.getText().trim();
				p.println(send);
				BufferedReader in;
				try {
					in = new BufferedReader(new InputStreamReader(ATM_machine.socket.getInputStream()));
					response = in.readLine();
					System.out.println(response.trim());
					 textArea.append(response.trim() + "\n");
					 System.out.println("");
					}
				catch (IOException e1) {
					e1.printStackTrace();
					}
				AccountNo.setText(null);
			}
		});
		get_balance.setBounds(291, 11, 115, 23);
		frame.getContentPane().add(get_balance);
	}
}
