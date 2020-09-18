
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.SwingConstants;

import com.mysql.jdbc.ResultSetMetaData;

import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Deposit_Page {

	private JFrame frame;
	private JTextField Deposit_amount;
	static ATM_machine atm;
	static OutputStream out;
	static PrintWriter p;
	static Deposit_Page window;
	private JTextField Account_Number;
	

	
	static final String dbURL = 
		    "jdbc:mysql://localhost:3306/atm?" +  
		    "user=root&password=";
	

	/**
	 * Launch the application.
	 */
	public static void newDepositPage() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Deposit_Page();
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
	 * @throws ClassNotFoundException 
	 */
	public Deposit_Page() throws ClassNotFoundException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws ClassNotFoundException 
	 */
	private void initialize() throws ClassNotFoundException {

		Class.forName("com.mysql.jdbc.Driver");

		frame = new JFrame();
		frame.setBounds(200, 200, 1000, 388);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblDepositPage = new JLabel("Deposit Page");
		lblDepositPage.setHorizontalAlignment(SwingConstants.CENTER);
		lblDepositPage.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblDepositPage.setBounds(78, 21, 315, 20);
		frame.getContentPane().add(lblDepositPage);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 52, 464, 5);
		frame.getContentPane().add(separator);
		
		JLabel lblPleaseEnterThe = new JLabel("Please enter the amount you wish to deposit");
		lblPleaseEnterThe.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPleaseEnterThe.setBounds(120, 68, 257, 14);
		frame.getContentPane().add(lblPleaseEnterThe);
		
		Deposit_amount = new JTextField();
		Deposit_amount.setBounds(205, 124, 86, 20);
		frame.getContentPane().add(Deposit_amount);
		Deposit_amount.setColumns(10);
		
		JButton Deposit_Button = new JButton("Submit Transaction");
		Deposit_Button.setBounds(171, 168, 150, 23);
		frame.getContentPane().add(Deposit_Button);
		
		Account_Number = new JTextField();
		Account_Number.setBounds(205, 93, 86, 20);
		frame.getContentPane().add(Account_Number);
		Account_Number.setColumns(10);
		
		JLabel lblAccountNumber = new JLabel("Account Number");
		lblAccountNumber.setBounds(92, 93, 103, 14);
		frame.getContentPane().add(lblAccountNumber);
		
		JLabel lblAmmount = new JLabel("Ammount");
		lblAmmount.setBounds(92, 127, 103, 14);
		frame.getContentPane().add(lblAmmount);
		
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(412, 131, 464, 157);
		frame.getContentPane().add(textArea);
		
		textArea.setText("  Date                                type          Amount\n");
		
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
		Deposit_Button.addActionListener(new ActionListener() {
		@SuppressWarnings("unused")
		public void actionPerformed(ActionEvent e){
				String response = null;
				//JOptionPane.showMessageDialog(null, "Please wait while we complete your transaction...");
				String send = "3"+","+ATM_machine.cardno+","+ Account_Number.getText()+ "," + Deposit_amount.getText().trim()+","+Account_Number.getText().trim();
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
				if (response.trim().equals("Successful")){
					java.sql.Connection conn = null;
					try {
						conn=DriverManager.getConnection(dbURL);
						
						Statement stmt = conn.createStatement();
		    			String query = "select * from atm.history ORDER BY Transactionid DESC LIMIT 1;";
		    			ResultSet rs3 = stmt.executeQuery(query);
		    			
		    			ResultSetMetaData rsmd = (ResultSetMetaData) rs3.getMetaData();
		    			int columnsNumber = rsmd.getColumnCount();
		    			while (rs3.next()) {
		    			    for (int i = 1; i <= columnsNumber; i++) {
		    			        if (i > 1) System.out.print(",  ");
		    			        String columnValue = rs3.getString(i);
		    			        if(rsmd.getColumnName(i).equals("Date"))
		    			        {
		    			        	textArea.append(columnValue + "       ");
		    			        }
		    			        else if(rsmd.getColumnName(i).equals("type"))
		    			        {
		    			        	textArea.append(columnValue + "           ");
		    			        }	        
		    			    }
		    			    textArea.append(Deposit_amount.getText() + "\n");
		    			    System.out.println("");
		    			}
						
					}
					catch(Exception e1)
					{
						System.out.println(e1);
					}
					JOptionPane.showMessageDialog(null, response.trim());
					Transaction_Selection_Page page2=new Transaction_Selection_Page();
					Transaction_Selection_Page.NewScreen();
					window.frame.setVisible(false);
					}
				else {
					JOptionPane.showMessageDialog(null, response.trim());
					Deposit_amount.setText(null);
					Account_Number.setText(null);
					Transaction_Selection_Page page2=new Transaction_Selection_Page();
					Transaction_Selection_Page.NewScreen();
					window.frame.setVisible(false);
				}
			}
		});
	}

}
