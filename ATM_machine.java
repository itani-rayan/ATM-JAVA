import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;


public class ATM_machine {

	private JFrame frame;
	static JTextField txtCard_Number;
	private JPasswordField txt_Password;
	static Socket socket;
	static DataInputStream datain;
	static DataOutputStream dataout;
	static OutputStream out;
	static PrintWriter p;
	static ATM_machine window;
	static String cardno;
	static JTextField ATMNum;
	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) {
		ATMachine();
		try {
			socket = new Socket("192.168.0.108",1928);
	        out = socket.getOutputStream();
	        p = new PrintWriter(out,true);
	        
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void ATMachine() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new ATM_machine();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ATM_machine() {
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
		
		JLabel lblNewLabel = new JLabel("Welcome to American University Bank");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(81, 26, 315, 20);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblCardNumber = new JLabel("Card Number");
		lblCardNumber.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCardNumber.setBounds(116, 91, 81, 14);
		frame.getContentPane().add(lblCardNumber);

		JLabel lblPinNumber = new JLabel("PIN Number");
		lblPinNumber.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPinNumber.setBounds(116, 150, 81, 14);
		frame.getContentPane().add(lblPinNumber);
		
		txtCard_Number = new JTextField();
		txtCard_Number.setBounds(287, 88, 86, 20);
		frame.getContentPane().add(txtCard_Number);
		txtCard_Number.setColumns(10);
		
		txt_Password = new JPasswordField();
		txt_Password.setBounds(287, 147, 86, 20);
		frame.getContentPane().add(txt_Password);
		
		ATMNum = new JTextField();
		ATMNum.setBounds(429, 28, 30, 20);
		frame.getContentPane().add(ATMNum);
		ATMNum.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String response = null;
				String CardNo_input = txtCard_Number.getText();
				@SuppressWarnings("deprecation")
				String PIN_input = txt_Password.getText();
				String ATM_Number= ATMNum.getText();
				String send = "1"+","+ CardNo_input+"," + PIN_input + ","+ATM_Number;
				if(CardNo_input.equals("") || PIN_input.equals("") ) {
					JOptionPane.showMessageDialog(null, "Please Enter Your Card Number and Your PIN ");
				}
				else {
					p.println(send);
					BufferedReader in;
					try {
						in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				        response = in.readLine();
				        System.out.println(response.trim());
					}
					catch (IOException e1) {
						e1.printStackTrace();
					}
					if (response.trim().equals("Login Succesfully"))
					{
						cardno = txtCard_Number.getText();
						txt_Password.setText(null);
						txtCard_Number.setText(null);
						@SuppressWarnings("unused")
						Transaction_Selection_Page page2=new Transaction_Selection_Page();
						Transaction_Selection_Page.NewScreen();
						window.frame.setVisible(false);
					} 
					else 
						{
						JOptionPane.showMessageDialog(null, response.trim());
						txt_Password.setText(null);
						txtCard_Number.setText(null);
						}
				}
			}
		        
		});
		btnLogin.setBounds(195, 215, 89, 23);
		frame.getContentPane().add(btnLogin);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 199, 464, 5);
		frame.getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 64, 464, 5);
		frame.getContentPane().add(separator_1);
		
		txtCard_Number.setText(null);
		txt_Password.setText(null);
		
		
	}
}
