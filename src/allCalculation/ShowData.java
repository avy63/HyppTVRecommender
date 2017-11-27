package allCalculation;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;





import java.awt.Font;

import javax.swing.JList;

public class ShowData extends JFrame {

	private JPanel contentPane;
	private JLabel lblName;
	private JLabel lblWellcome;
	private int userId;
	JList list;
	Connection coneConnection = null;
	public RecommendationCalculation recommendationCalculation;
	public ContextualRecommendation contextualRecommendation;
	private JList list_newUserrec;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShowData frame = new ShowData();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws SQLException
	 */

	public ShowData(int x,Connection connection) throws SQLException {
		this();
		this.userId = x;
		coneConnection=connection;
		String query="SELECT * FROM Channel_usage_new where userId=? ";
		PreparedStatement preparedStatement=coneConnection.prepareStatement(query);
		preparedStatement.setInt(1, userId);
		ResultSet resultSet=preparedStatement.executeQuery();
		String name="";
		while(resultSet.next()){
			name=resultSet.getString("NAME");
		}
		lblName.setText(" "+name);
		try {
			recommendationCalculation = new RecommendationCalculation(userId,coneConnection);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			contextualRecommendation=new ContextualRecommendation(userId,connection);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,Double> topitembasedondemographic=contextualRecommendation.getPrefofUser();
		Map<String,Double> channlreco=recommendationCalculation.getPrefofUser();
		if (channlreco != null && channlreco.size() > 0) {
			int count = 0;
			DefaultListModel dlm = new DefaultListModel();
			for (Map.Entry<String,Double> entry : channlreco.entrySet()) {

				dlm.addElement(count+1+")"+" "+entry.getKey() + " "+" "+entry.getValue());
				count++;
				if(count==5){
					break;
				}
			}
			list.setModel(dlm);
		}
		if(topitembasedondemographic !=null && topitembasedondemographic.size()>0){
			int count = 0;
			DefaultListModel dlm = new DefaultListModel();
			for (Map.Entry<String,Double> entry : topitembasedondemographic.entrySet()) {

				dlm.addElement(count+1+")"+" "+entry.getKey() + " "+" "+entry.getValue());
				count++;
				if(count==5){
					break;
				}
			}
			list_newUserrec.setModel(dlm);
		}
	}

	public ShowData() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 761, 465);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblWellcome = new JLabel("Welcome");
		lblWellcome.setBounds(29, 11, 68, 14);
		contentPane.add(lblWellcome);

		lblName = new JLabel("New label");
		lblName.setBounds(107, 11, 288, 14);
		contentPane.add(lblName);

		JLabel lblTopRecommended = new JLabel("TOP 5 recommended item:");
		lblTopRecommended.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTopRecommended.setBounds(10, 29, 240, 14);
		contentPane.add(lblTopRecommended);

		list = new JList();
		list.setBounds(29, 54, 297, 308);
		contentPane.add(list);
		
		JLabel lblPeopleWithSame = new JLabel("Recommendation based on demographic Information:");
		lblPeopleWithSame.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPeopleWithSame.setBounds(349, 30, 351, 14);
		contentPane.add(lblPeopleWithSame);
		
		list_newUserrec = new JList();
		list_newUserrec.setBounds(349, 54, 311, 308);
		contentPane.add(list_newUserrec);
	}
}
