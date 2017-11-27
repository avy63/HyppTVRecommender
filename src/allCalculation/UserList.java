package allCalculation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserList {
	public List<Integer> userList;
	
	public UserList(Connection coneConnection) throws Exception{
		userList=new ArrayList<Integer>();
		String query = "SELECT  DISTINCT Userid from Channel_usage_new";
		PreparedStatement preparedStatement = coneConnection
				.prepareStatement(query);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			userList.add(resultSet.getInt("Userid"));
		}
		System.out.println("User List size" + userList.size());
	}
	public List<Integer> getUserList(){
		return userList;
	}
}
