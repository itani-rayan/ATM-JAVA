
/** 
 * 
 * @author  Hassan Artail 
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
//import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
import java.net.ServerSocket;
import java.sql.*;
import java.util.HashMap;


public class SkillsServer {
	public int loggedin = 0;
	public String cardno = "";
	int error = 0;
	private int port = 1928;
	private ServerSocket serverSocket;
	HashMap<String, String> hm;

	// URL of DB server and authentication string\cf0\f0
	static final String dbURL = "jdbc:mysql://localhost:3306/atm?" + "user=root&password=";

	// In the constructor, load the Java driver for the MySQL DB server
	// to enable this program to communicate with the database \cf0\f0
	public SkillsServer() throws ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
	}

	public void acceptConnections() {

		try {
//similar to the WelcomeSocket in the PowerPoint slides\cf2 
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("ServerSocket instantiation failure");
			e.printStackTrace();
			System.exit(0);
		}

		// Entering the infinite loop\cf0\f0
		while (true) {
			try {
				// wait for a TCP handshake initialization (arrival of a "SYN" packet)
				Socket newConnection = serverSocket.accept();
				System.out.println("accepted connection");

				// Now, pass the connection socket created above to a thread and run it in it
				// First create the thread and pass the connection socket to it
				// This is a non-blocking function: constructor of the class ServerThread\cf2
				ServerThread st = new ServerThread(newConnection);

				// Then, start the thread, and go back to waiting for another TCP connection
				// This also is not blocking\cf2
				new Thread(st).start();
			} catch (IOException ioe) {
				System.err.println("server accept failed");
			}
		}
	}

	public static void main(String args[]) {
		String KEY = "ON";
		SkillsServer server = null;
		try {
			// Instantiate an object of this class. This will load the JDBC database
			// driver\cf0\f0
			server = new SkillsServer();
		} catch (ClassNotFoundException e) {
			System.out.println("unable to load JDBC driver");
			e.printStackTrace();
			System.exit(1);
		}

//call this function, which will start it all...\cf0\f0
		if (KEY.equals("ON")){
			server.acceptConnections();
		}
		else {
			System.out.println("SERVER IS OFF");
		}

	}

	// Internal class\cf0\f0
	class ServerThread implements Runnable {
		private Socket socket;
		private DataInputStream datain;
		private DataOutputStream dataout;

		public ServerThread(Socket socket) {
			// Inside the constructor: store the passed object in the data member\cf2
			this.socket = socket;
		}

//This is where you place the code you want to run in a thread 
//Every instance of a ServerThread will handle one client (TCP connection)\cf2 
		public void run() {
			try {
				// Input and output streams, obtained from the member socket object\cf2
				datain = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				dataout = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			} catch (IOException e) {
				return;
			}
			byte[] ba = new byte[30];
			boolean conversationActive = true;

			while (conversationActive) {
				String skill = null;
				try {
					// read from the input stream buffer (read a message from client)\cf0\f0
					datain.read(ba, 0, 30);
					skill = new String(ba);
					String[] first = skill.split(",");
					// Exit when a "Q" is received from a client\cf0\f0
					if ((skill.length() == 1) && (skill.toUpperCase().charAt(0) == 'Q')) {
						conversationActive = false;
					} else {
						if (first[0].equals("1")) {
							if (loggedin == 1 && cardno.equals(first[1])) {
								dataout.writeUTF("User Already loggedin\n");
								dataout.flush();
							} else {
								System.out.println("requested skill = " + skill);
								String login = Login(first[1], first[2], first[3].trim());
								System.out.println(login);
								System.out.println("writing " + login.length() + " bytes");
								// Write to the output stream buffer (send a message to client)\cf0\f0
								dataout.writeUTF(login);
								dataout.flush();
							}
						} else if (first[0].equals("2")) {
							System.out.println("requested skill = " + skill);
							String withdraw = Withdrawl(first[1].trim(), first[2].trim(), first[3].trim());
							System.out.println(withdraw);
							System.out.println("writing " + withdraw.length() + " bytes");
////       Write to the output stream buffer (send a message to client)\cf0\f0 
							dataout.writeUTF(withdraw);
							dataout.flush();
						} else if (first[0].equals("3")) {
							System.out.println("requested skill = " + skill);
							String deposit = Deposit(first[1].trim(), first[2].trim(), first[3].trim());
							System.out.println(deposit);
							System.out.println("writing " + deposit.length() + " bytes");
////       Write to the output stream buffer (send a message to client)\cf0\f0 
							dataout.writeUTF(deposit);
							dataout.flush();
						} else if (first[0].equals("4")) {
							System.out.println("requested skill = " + skill);
							String transfer = Transfer(first[1].trim(), first[2].trim(), first[3].trim(),
									first[4].trim());
							System.out.println(transfer);
							System.out.println("writing " + transfer.length() + " bytes");
////       Write to the output stream buffer (send a message to client)\cf0\f0 
							dataout.writeUTF(transfer);
							dataout.flush();
						} else if(first[0].equals("5")) {
							loggedin=0;
							System.out.println("requested skill = " + skill);
							String logout = Logout(first[1].trim());
							System.out.println(logout);
							System.out.println("writing " + logout.length() + " bytes");
////       Write to the output stream buffer (send a message to client)\cf0\f0 
							dataout.writeUTF(logout);
							dataout.flush();
			        	  } else if(first[0].equals("6")) {
								System.out.println("requested skill = " + skill);
								
								String logout = Balance(first[1].trim(), first[2].trim());
								System.out.println(logout);
								System.out.println("writing " + logout.length() + " bytes");
								System.out.println("CARD IS OUT");
////	       Write to the output stream buffer (send a message to client)\cf0\f0 
								dataout.writeUTF(logout);
								dataout.flush();
				        	  }
					}
				} catch (IOException ioe) {
					conversationActive = false;
				}
			}
			try {
				System.out.println("closing socket");
				loggedin = 0;
				cardno = "";
				datain.close();
				dataout.close();
				// When the server receives a "Q", we arrive here\cf0\f0
				socket.close();
			} catch (IOException e) {
			}
		}

		private String Balance(String CardNo, String AccountNo) {
			String result = "None available";
			Connection conn = null;
			String test =null;
			try {
				conn = DriverManager.getConnection(dbURL);
				Statement stmt = conn.createStatement();
				String query = "SELECT Amount FROM account WHERE CardNo = " + CardNo + " and Account ="
						+ AccountNo;
				ResultSet rs = stmt.executeQuery(query);
				StringBuffer sb = new StringBuffer();
				Timestamp sdate = new Timestamp(System.currentTimeMillis());
			    sb.append(sdate);
			    sb.append("       "+AccountNo+"       ");
			    sb.append("    Available Amount:    ");
				while (rs.next()) {
					test = rs.getString(1);
					sb.append(rs.getString(1));
				}
				if (rs.next() == false && test == null) {
					result = "Invalid Account Number "+AccountNo;
				}
				else {
					result=sb.toString().trim();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
	return result+"\n";
}

		private String Logout(String ATMNum) {
			String result = "None available";
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(dbURL);
				Statement stmt8 = conn.createStatement();
				String query8 = "UPDATE `atm number` SET valide = " + 1 + " WHERE `idATM Number` = " + ATMNum;
				@SuppressWarnings("unused")
				int rs8 = stmt8.executeUpdate(query8);
				result= "CARD IS OUT";
			} catch (SQLException e) {
				e.printStackTrace();
			}
	return result+"\n";
}

		// This function is for communicating with the database server
		// using API provided by the JDBC driver loaded at the top\cf0\f0
		private String Transfer(String AccountSend, String AccountReceive, String Transfer_Amount, String CardNo) {
			String result = "None available";
			Connection conn = null;
			double ta = Integer.parseInt(Transfer_Amount);
			String amount_available_sender = null;
			String amount_available_receiver = null;
			if (!AccountSend.equals(AccountReceive)) {
				try {
					conn = DriverManager.getConnection(dbURL);
					Statement stmt2 = conn.createStatement();
					String query2 = "SELECT Amount FROM account WHERE CardNo = " + CardNo + " and Account ="
							+ AccountSend;
					ResultSet rs2 = stmt2.executeQuery(query2);
					while (rs2.next()) {
						amount_available_sender = rs2.getString(1);
					}
					if (rs2.next() == false && amount_available_sender == null) {
						result = "Invalid Account Number";
					}

					else {
						conn = DriverManager.getConnection(dbURL);
						Statement stmt3 = conn.createStatement();
						String query3 = "SELECT Amount FROM account WHERE CardNo = " + CardNo + " and Account ="
								+ AccountReceive;
						ResultSet rs3 = stmt3.executeQuery(query3);
						while (rs3.next()) {
							amount_available_receiver = rs3.getString(1);
						}
						if (rs3.next() == false && amount_available_receiver == null) {
							result = "Invalid Account Number";
						} else {
							double sa = Double.parseDouble(amount_available_sender.trim());
							double ra = Double.parseDouble(amount_available_receiver.trim());
							if (ta > 0 && sa>ta) {
								double newSenderAmount = sa - ta;
								Statement stmt4 = conn.createStatement();
								String query4 = "UPDATE account SET Amount = " + newSenderAmount + " WHERE CardNo = "
										+ CardNo + " and Account =" + AccountSend;
								@SuppressWarnings("unused")
								int rs4 = stmt4.executeUpdate(query4);
								double newReceiverAmount = ra + ta;
								Statement stmt5 = conn.createStatement();
								String query5 = "UPDATE account SET Amount = " + newReceiverAmount + " WHERE CardNo = "
										+ CardNo + " and Account =" + AccountReceive;
								@SuppressWarnings("unused")
								int rs5 = stmt5.executeUpdate(query5);
								result = "Successful";
								
								Statement stmt6 = conn.createStatement();
								String query6 = "Insert into bank_transaction (CardNo,From_Acc,To_Acc,ATM_id,Amount)"
										+ " values(" + CardNo + "," + AccountSend + "," + AccountReceive + "," + 1 + ","
										+ newSenderAmount + ")";
								@SuppressWarnings("unused")
								int rs6 = stmt6.executeUpdate(query6);
								try {
									Timestamp sdate = new Timestamp(System.currentTimeMillis());
								     System.out.println(sdate);

									String query7 = "INSERT INTO atm.history(`id`, Card_No, Date, type) VALUES ('"
											+ AccountSend + "', '" + CardNo + "', '" + sdate + "', 'T');";

									PreparedStatement stmt7 = conn.prepareStatement(query7);

									@SuppressWarnings("unused")
									int set = stmt7.executeUpdate(query7);
								} catch (Exception e) {
									System.out.println(e);
								}
							}

							else {
								result = "Invalid Input";
							}

						}

					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
					result = "server error";
				}
			} else {
				result = "ERROR: Same Account Number";
			}
			return result + "\n";
		}

//This function is for communicating with the database server 
//using API provided by the JDBC driver loaded at the top\cf0\f0 
		private String Deposit(String CardNo, String Account_Number, String Deposit_Amount) {
			String result = "None available";
			Connection conn = null;
			int da = Integer.parseInt(Deposit_Amount);
			System.out.println("Deposit ammount = " + Deposit_Amount);
			String amount_available = null;
			try {
				conn = DriverManager.getConnection(dbURL);
				Statement stmt2 = conn.createStatement();
				String query2 = "SELECT Amount FROM account WHERE CardNo = " + CardNo + " and Account ="
						+ Account_Number;
				ResultSet rs2 = stmt2.executeQuery(query2);
				while (rs2.next()) {
					amount_available = rs2.getString(1);
				}
				if (rs2.next() == false && amount_available == null) {
					result = "Invalid Account Number";
				} else {
					double va = Double.parseDouble(amount_available.trim());
					System.out.println("Available ammount = " + va);
					if (da > 0) {
						double newAmount = va + da;
						System.out.println("New ammount = " + newAmount);
						Statement stmt3 = conn.createStatement();
						String query3 = "UPDATE account SET Amount =" + newAmount + " WHERE CardNo = " + CardNo
								+ " and Account =" + Account_Number;
						@SuppressWarnings("unused")
						int rs3 = stmt3.executeUpdate(query3);
						result = "Successful";
						Statement stmt4 = conn.createStatement();
						String query4 = "Insert into bank_transaction (CardNo,From_Acc,ATM_id,Amount)" + "values("
								+ CardNo + "," + Account_Number + "," + 1 + "," + newAmount + ")";
						@SuppressWarnings("unused")
						int rs4 = stmt4.executeUpdate(query4);
						try {
							Timestamp sdate = new Timestamp(System.currentTimeMillis());
	    				    System.out.println(sdate);

							String query5 = "INSERT INTO atm.history(`id`, Card_No, Date, type) VALUES ('"
									+ Account_Number + "', '" + CardNo + "', '" + sdate + "', 'D');";

							PreparedStatement stmt5 = conn.prepareStatement(query5);

							@SuppressWarnings("unused")
							int set = stmt5.executeUpdate(query5);
						} catch (Exception e) {
							System.out.println(e);
						}
					} else {
						result = "Invalid Input";
					}
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				result = "server error";
			}
			return result + "\n";

		}

//This function is for communicating with the database server 
//using API provided by the JDBC driver loaded at the top\cf0\f0 
		private String Withdrawl(String CardNo, String Account_Number, String Withdraw_Amount) {
			String result = "None available";
			Connection conn = null;
			int wa = Integer.parseInt(Withdraw_Amount);
			String amount_available = null;
			try {
				conn = DriverManager.getConnection(dbURL);
				Statement stmt2 = conn.createStatement();
				String query2 = "SELECT Amount FROM atm.account WHERE CardNo = " + CardNo + " AND Account = "
						+ Account_Number;
				ResultSet rs2 = stmt2.executeQuery(query2);
				while (rs2.next()) {
					amount_available = rs2.getString(1);
				}
				if (rs2.next() == false && amount_available == null) {
					result = "Invalid Account Number";
				} else {
					double va = Double.parseDouble(amount_available.trim());
					if (wa < va && wa>0) {
						if (wa % 20 == 0) {
							double newAmount = va - wa;
							Statement stmt3 = conn.createStatement();
							String query3 = "UPDATE atm.account SET Amount =" + newAmount + " WHERE CardNo = " + CardNo
									+ " and Account = " + Account_Number;
							@SuppressWarnings("unused")
							int rs3 = stmt3.executeUpdate(query3);
							result = "Successful";
							Statement stmt4 = conn.createStatement();
							String query4 = "Insert into bank_transaction (CardNo,From_Acc,ATM_id,Amount)" + "values("
									+ CardNo + "," + Account_Number + "," + 1 + "," + newAmount + ")";
							@SuppressWarnings("unused")
							int rs4 = stmt4.executeUpdate(query4);
							try {
		
								Timestamp sdate = new Timestamp(System.currentTimeMillis());
		    				    System.out.println(sdate);


								String query5 = "INSERT INTO atm.history(`id`, Card_No, Date, type) VALUES ('"
										+ Account_Number + "', '" + CardNo + "', '" + sdate + "', 'W');";

								PreparedStatement stmt5 = conn.prepareStatement(query5);

								@SuppressWarnings("unused")
								int set = stmt5.executeUpdate(query5);
							} catch (Exception e) {
								System.out.println(e);
							}
						} else {
							result = "Multiple of 20 Only";
						}
					} else {
						result = "Withdrawl Ammount is Greater Than Your Balance";
					}
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				result = "server error";
			}
			return result + "\n";
		}

//This function is for communicating with the database server 
//using API provided by the JDBC driver loaded at the top\cf0\f0 
		public String Login(String CardNo, String PIN, String ATMNum) {
			String result = "Account Blocked";
			Connection conn = null;
			String valide = null;
			String available = null;
			try {
				HashMap<String, String> hm = new HashMap<String, String>();
				conn = DriverManager.getConnection(dbURL);
				String query = "SELECT valide FROM `atm number` WHERE `idATM Number` = " + ATMNum;
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query); 
				while (rs.next()) {
					available = rs.getString(1);
				}
				if (available.equals("1")) {

					Statement stmt1 = conn.createStatement();
					String query1 = "SELECT CardNo, PIN " + "FROM cardno";
					ResultSet rs1 = stmt1.executeQuery(query1);
					while (rs1.next()) {
						// String hash = getSHA(rs1.getString(2));
						hm.put(rs1.getString(1), rs1.getString(2));
					}
					Statement stmt2 = conn.createStatement();
					String query2 = "SELECT valide FROM cardno WHERE CardNo = " + CardNo;
					ResultSet rs2 = stmt2.executeQuery(query2);
					while (rs2.next()) {
						valide = rs2.getString(1);
					}
					if (hm.containsKey(CardNo)) {
						if (valide.equals("1")) {
							if (hm.get(CardNo).equals(PIN)) {
								Statement stmt8 = conn.createStatement();
								String query8 = "UPDATE `atm number` SET valide = " + 0 + " WHERE `idATM Number` = " + ATMNum;
								@SuppressWarnings("unused")
								int rs8 = stmt8.executeUpdate(query8);
								loggedin = 1;
								cardno = CardNo;
								result = "Login Succesfully";
								error=0;
							} else {
								error += 1;
								if (error >= 3) {
									Statement stmt3 = conn.createStatement();
									String query3 = "UPDATE cardno SET Valide = " + 0 + " WHERE CardNo = " + CardNo;
									@SuppressWarnings("unused")
									int rs3 = stmt3.executeUpdate(query3);	
								} else {
									result = "Incorrect Pin";
								}
							}
						} else {
							result = "Account Blocked Contact Administrator";
						}
						}else {
							result = "Failed to Login";
					}
				}
				else {
					result="ATM is Already in USE";
				}
			} 
			catch (SQLException e) {
				System.out.println(e.getMessage());
				result = "server error";
				
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
					}
				}
			}
			return result + "\n";//the buffer only read the result if the line finish
		}

////This function is for communicating with the database server 
////		 using API provided by the JDBC driver loaded at the top\cf0\f0
//    private String getNames(String skill) { 
//      String result = "None available"; 
//      Connection conn = null; 
//      try { 
//        conn = DriverManager.getConnection(dbURL); 
// 
//        Statement stmt = conn.createStatement(); 
//        String query = "SELECT CardNo, PIN " + 
//                       "FROM cardno "; 
//        System.out.println("query = " + query); 
//        ResultSet rs = stmt.executeQuery(query); 
//        StringBuffer sb = new StringBuffer(); 
//        while (rs.next()) { 
//          sb.append(rs.getString(1)+ " "); 
//          sb.append(", "); 
//          sb.append(rs.getString(2)+" ");
//        } 
//        result = sb.toString(); 
//      } 
//      catch (SQLException e) { 
//        System.out.println(e.getMessage()); 
//        result = "server error"; 
//      } 
//      finally { 
//        if (conn != null) { 
//          try { 
//            conn.close(); 
//          } 
//          catch (SQLException e) { 
//          } 
//        } 
//      } 
//      return result; 
//    } 
		 }
public static String getSHA(String input) { 
	
	    try { 
	
	        // Static getInstance method is called with hashing SHA 
	        MessageDigest md = MessageDigest.getInstance("SHA-256"); 
	
	        // digest() method called 
	        // to calculate message digest of an input 
	        // and return array of byte 
	        byte[] messageDigest = md.digest(input.getBytes()); 
	
	        // Convert byte array into signum representation 
	        BigInteger no = new BigInteger(1, messageDigest); 
	
	        // Convert message digest into hex value 
	        String hashtext = no.toString(16); 
	
	        while (hashtext.length() < 32) { 
	            hashtext = "0" + hashtext; 
	        } 
	
	        return hashtext; 
	    } 
	
	    // For specifying wrong message digest algorithms 
	    catch (NoSuchAlgorithmException e) { 
	        System.out.println("Exception thrown"
	                           + " for incorrect algorithm: " + e); 
	
	        return null; 	    } 
	}
}
