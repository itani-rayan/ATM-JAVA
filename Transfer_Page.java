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
public class Transfer_Page {

	private JFrame frame;
	private JTextField Destination_Account;
	private JTextField Amount;
	static ATM_machine atm;
	static OutputStream out;
	static PrintWriter p;
	static Transfer_Page window;
	private JTextField Source_Account;
	
	static final String dbURL = 
		    "jdbc:mysql://localhost:3306/atm?" +  
		    "user=root&password=";
	/**
	 * Launch the application.
	 */
	public static void newTransferPage() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Transfer_Page();
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
	public Transfer_Page() throws ClassNotFoundException {
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
		
		JLabel lblTransferPage = new JLabel("Transfer Page");
		lblTransferPage.setHorizontalAlignment(SwingConstants.CENTER);
		lblTransferPage.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTransferPage.setBounds(78, 11, 315, 20);
		frame.getContentPane().add(lblTransferPage);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 42, 464, 5);
		frame.getContentPane().add(separator);
		
		JLabel lblNewLabel = new JLabel("Source Account Number");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(97, 61, 136, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Destination Account Number");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(97, 114, 161, 14);
		frame.getContentPane().add(lblNewLabel_1);

		Destination_Account = new JTextField();
		Destination_Account.setBounds(307, 108, 86, 20);
		frame.getContentPane().add(Destination_Account);
		Destination_Account.setColumns(10);
		
		JLabel lblTransferAmount = new JLabel("Transfer Amount");
		lblTransferAmount.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTransferAmount.setBounds(97, 171, 102, 14);
		frame.getContentPane().add(lblTransferAmount);
		
		Amount = new JTextField();
		Amount.setBounds(307, 165, 86, 20);
		frame.getContentPane().add(Amount);
		Amount.setColumns(10);
		
		Source_Account = new JTextField();
		Source_Account.setBounds(307, 58, 86, 20);
		frame.getContentPane().add(Source_Account);
		Source_Account.setColumns(10);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(412, 131, 464, 157);
		frame.getContentPane().add(textArea);
		
		textArea.setText("  Date                                type          Amount\n");
		
		JButton Confirm_Button = new JButton("Confirm");
		Confirm_Button.setBounds(194, 227, 89, 23);
		frame.getContentPane().add(Confirm_Button);
		Confirm_Button.addActionListener(new ActionListener() { //Set OnClickListener
		public void actionPerformed(ActionEvent e) {
			String response = null;
			String Account_Send = Source_Account.getText();
			String Account_Reiceive = Destination_Account.getText();
			String amount = Amount.getText();
			String send = "4"+"," + Account_Send+","+ Account_Reiceive+ ","+ amount +","+ ATM_machine.cardno;
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
			if (response.trim().equals("Successful"))
			{
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
	    			    textArea.append(Amount.getText() + "\n");
	    			    System.out.println("");
	    			}
					JOptionPane.showMessageDialog(null, response.trim());
					@SuppressWarnings("unused")
					Transaction_Selection_Page page2=new Transaction_Selection_Page();
					Transaction_Selection_Page.NewScreen();
					window.frame.setVisible(false);
				}
				catch(Exception e1)
				{
					System.out.println(e1);
				}
	
			} 
			else 
				{
				JOptionPane.showMessageDialog(null, response.trim());
				Destination_Account.setText(null);
				Source_Account.setText(null);
				Amount.setText(null);
				@SuppressWarnings("unused")
				Transaction_Selection_Page page2=new Transaction_Selection_Page();
				Transaction_Selection_Page.NewScreen();
				window.frame.setVisible(false);
				
				}
		}
		});
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 211, 464, 5);
		frame.getContentPane().add(separator_1);
		
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
		
		
	}
}
