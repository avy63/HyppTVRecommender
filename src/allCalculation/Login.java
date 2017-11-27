package allCalculation;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;



import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;

import dbConnect.DBConnection;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login {

	private JFrame frame;
    public DBConnection dbConnection;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
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
	Connection coneConnection=null;
	private JTextField textName;
	private JPasswordField passwordField;
	private JButton btnSignUp;
	public Login() {
		initialize();
		coneConnection=dbConnection.sqliteConncetion();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		dbConnection=new DBConnection();
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("User id");
		lblNewLabel.setBounds(101, 74, 46, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setBounds(101, 116, 65, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		textName = new JTextField();
		textName.setBounds(192, 71, 201, 20);
		frame.getContentPane().add(textName);
		textName.setColumns(10);
		
		JButton btnlogin = new JButton("Login");
		btnlogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					String query="SELECT * FROM Channel_usage_new where userId=? ";
					PreparedStatement preparedStatement=coneConnection.prepareStatement(query);
					preparedStatement.setString(1,textName.getText());
					ResultSet resultSet=preparedStatement.executeQuery();
					int count=0;
					while(resultSet.next()){
						count++;
					}
					if(count>0){
						JOptionPane.showMessageDialog(null,"Login successful");
						frame.dispose();
						ShowData showData=new ShowData(Integer.parseInt(textName.getText()),coneConnection);
						showData.setVisible(true);
					}else{
						JOptionPane.showMessageDialog(null,"User name and password not correct");
					}
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null,ex);
				}
			}
		});
		btnlogin.setBounds(141, 178, 137, 23);
		frame.getContentPane().add(btnlogin);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(192, 113, 201, 20);
		frame.getContentPane().add(passwordField);
		
		btnSignUp = new JButton("Sign UP");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				/*SignUP signUP=new  SignUP();
				signUP.setVisible(true);*/
			}
		});
		btnSignUp.setBounds(141, 212, 137, 22);
		frame.getContentPane().add(btnSignUp);
	}
}
