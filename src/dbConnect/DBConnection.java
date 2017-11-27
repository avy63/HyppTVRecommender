package dbConnect;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.DataSource;
import javax.swing.JOptionPane;

public class DBConnection {

	Connection connection=null;
	public Connection sqliteConncetion(){
		try{
			Class.forName("org.sqlite.JDBC");
			connection=DriverManager.getConnection("jdbc:sqlite:HyppTVdatabase.sqlite");
			JOptionPane.showMessageDialog(null,"Connection successfull");
			return connection;
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
	}
}
