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

public class Withdraw_Page {

	private JFrame frame;
	private JTextField withdraw_amount;
	static ATM_machine atm;
	static OutputStream out;
	static PrintWriter p;
	static Withdraw_Page window;
	private JTextField Account_Number;
	
	static final String dbURL = 
		    "jdbc:mysql://localhost:3306/atm?" +  
		    "user=root&password=";
	
	/**
	 * Launch the application.
	 */
	public static void newWithdrawPage() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Withdraw_Page();
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
	public Withdraw_Page() throws ClassNotFoundException {
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
		
		JLabel lblWithdrawPage = new JLabel("Withdraw Page");
		lblWithdrawPage.setHorizontalAlignment(SwingConstants.CENTER);
		lblWithdrawPage.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblWithdrawPage.setBounds(83, 22, 315, 20);
		frame.getContentPane().add(lblWithdrawPage);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 53, 464, 5);
		frame.getContentPane().add(separator);
		
		JLabel lblNewLabel = new JLabel("Please enter the amount you would like to withdraw");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(97, 69, 301, 14);
		frame.getContentPane().add(lblNewLabel);
		
		withdraw_amount = new JTextField();
		withdraw_amount.setBounds(202, 128, 86, 20);
		frame.getContentPane().add(withdraw_amount);
		withdraw_amount.setColumns(10);
		
		Account_Number = new JTextField();
		Account_Number.setBounds(202, 94, 86, 20);
		frame.getContentPane().add(Account_Number);
		Account_Number.setColumns(10);
		
		JLabel lblAccountNum = new JLabel("Account Num");
		lblAccountNum.setBounds(83, 94, 95, 14);
		frame.getContentPane().add(lblAccountNum);
		
		JLabel lblAmmount = new JLabel("Ammount");
		lblAmmount.setBounds(83, 131, 95, 14);
		frame.getContentPane().add(lblAmmount);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(412, 131, 376, 157);
		frame.getContentPane().add(textArea);
		
		textArea.setText("  Date                                type          Amount\n");
		
		JButton Submit_Button = new JButton("Submit Transaction");
		Submit_Button.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String response = null;
			//JOptionPane.showMessageDialog(null, "Please wait while we complete your transaction...");
			String send = "2"+","+ATM_machine.cardno+","+ Account_Number.getText()+ "," + withdraw_amount.getText().trim()+","+Account_Number.getText().trim();
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
	    			    textArea.append(withdraw_amount.getText() + "\n");
	    			    System.out.println("");
	    			}
				}
				catch(Exception e1)
				{
					System.out.println(e1);
				}
				JOptionPane.showMessageDialog(null, response.trim());
				@SuppressWarnings("unused")
				Transaction_Selection_Page page2=new Transaction_Selection_Page();
				Transaction_Selection_Page.NewScreen();
				window.frame.setVisible(false);
			}
			else {
				JOptionPane.showMessageDialog(null, response.trim());
				withdraw_amount.setText(null);
				Account_Number.setText(null);
				@SuppressWarnings("unused")
				Transaction_Selection_Page page2=new Transaction_Selection_Page();
				Transaction_Selection_Page.NewScreen();
				window.frame.setVisible(false);
			}
			}
//					Transaction_Selection_Page nw= new Transaction_Selection_Page();;
		
		});
		Submit_Button.setBounds(171, 173, 150, 23);
		frame.getContentPane().add(Submit_Button);
		
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
