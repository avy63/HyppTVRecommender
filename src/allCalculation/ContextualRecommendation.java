package allCalculation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import dbConnect.DBConnection;

public class ContextualRecommendation {

	Connection coneConnection = null;
	public DBConnection dbConnection;
	public int userID;
	public String city;
	public KmeansForTM kmeansForTM;
	public Map<Double, Double> ratingMap;
	public UserList userListobJ;
	public List<Integer> userList;
	public static Map<Integer, Integer> numberofchanellwatch = null;
	public static Map<Integer, Double> averageUserRating = null;
	public Map<Integer, List<String>> listofchannelwatchbyUser;
	public Map<Integer, Integer> similaritybetweenusr;
	public Map<Integer, Integer> KmostSimilarUser;
	Map<String, Double> prefOfUser = new HashMap<String, Double>();
	public ContextualRecommendation(int usrIDtemp, Connection connection) throws Exception {
		this.userID = usrIDtemp;
		System.out.println(userID);
		//dbConnection = new DBConnection();
		coneConnection = connection;
		userList = new ArrayList<Integer>();
		ratingMap = new HashMap<Double, Double>();
		KmostSimilarUser=new HashMap<Integer, Integer>();

		numberofchanellwatch = new HashMap<Integer, Integer>();
		averageUserRating = new HashMap<Integer, Double>();
		listofchannelwatchbyUser = new HashMap<Integer, List<String>>();
		similaritybetweenusr = new HashMap<Integer, Integer>();

		kmeansForTM = new KmeansForTM(coneConnection);
		ratingMap = kmeansForTM.getRatingMap();
		// System.out.println(ratingMap.toString());
		userListobJ = new UserList(coneConnection);
		userList = userListobJ.getUserList();

		String query = "SELECT  * from Channel_usage_new where Userid="
				+ userID;
		System.out.println(query);
		PreparedStatement preparedStatement = coneConnection
				.prepareStatement(query);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			city = resultSet.getString("CITY");
		}

		System.out.println(city);
		calculateuserStatistics();
		if(KmostSimilarUser.size()>0){
		CalculateUserpreferences();
		}else{
			JOptionPane.showMessageDialog(null, "You donot have any similar user");
		}
	}

	private void CalculateUserpreferences() throws Exception{
		// TODO Auto-generated method stub
		List<String> notwatched = new ArrayList<String>();
		notwatched = getchannelsnotWatchbyuser(userID);
		Set<String> hs = new HashSet<>();
		hs.addAll(notwatched);
		notwatched.clear();
		notwatched.addAll(hs);
		for (int i = 0; i < notwatched.size(); i++) {
			double prefs = 0.0;
			double sumofsim = 0.0;
			for (Map.Entry<Integer, Integer> entry : KmostSimilarUser.entrySet()) {
				int simiuser = entry.getKey();
				List<String> chnllist2 = listofchannelwatchbyUser
						.get(simiuser);
				System.out.println("Sim: "+simiuser+" "+chnllist2.toString());
				if (chnllist2.contains(notwatched.get(i))) {
					String chnl=notwatched.get(i);
					
					String query = "SELECT Chnnal_usage FROM Channel_usage_new where Userid=? and chnlID=? ";
					PreparedStatement preparedStatement = coneConnection
							.prepareStatement(query);
					preparedStatement.setString(1,simiuser+"");
					preparedStatement.setString(2,notwatched.get(i));
					ResultSet resultSet = preparedStatement.executeQuery();

					if (resultSet.next()
							&& !Double.isNaN(resultSet
									.getDouble("Chnnal_usage"))) {
						double tempresult = resultSet.getDouble("Chnnal_usage");
						tempresult=ratingMap.get(tempresult);
						prefs = prefs
								+ ((tempresult - averageUserRating
										.get(simiuser)) * KmostSimilarUser
										.get(simiuser));
						sumofsim = sumofsim + KmostSimilarUser.get(simiuser);
					}
				}
			}
			prefs = prefs / sumofsim;
			prefs = prefs + averageUserRating.get(userID);
		
			if (prefs > 5.0) {
				prefOfUser.put(notwatched.get(i),
						Math.round(5.0 * 100.0) / 100.0);
			} else {
				prefOfUser.put(notwatched.get(i),
						Math.round(prefs * 100.0) / 100.0);
			}

		}
		prefOfUser = sortByValue1(prefOfUser);
		printTOP5iten(prefOfUser);
	}
	public Map<String,Double> getPrefofUser(){
		return prefOfUser;
	}
	private void printTOP5iten(Map<String, Double> prefOfUser2) {
		// TODO Auto-generated method stub
		int count = 0;

		for (Map.Entry<String, Double> en : prefOfUser2.entrySet()) {
			if (count > 5) {
				break;
			}
			System.out.println("Top item: " + en.getKey() + " "
					+ "preference value:" + Math.round(en.getValue() * 100.0)
					/ 100.0);

			count++;

		}
	}
	private static Map<String, Double> sortByValue1(
			Map<String, Double> prefOfUser) {
		// TODO Auto-generated method stub
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(
				prefOfUser.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		;
		return sortedMap;
	}
	private List<String> getchannelsnotWatchbyuser(int userID) {
		// TODO Auto-generated method stub
		List<String> alreadyWatched = listofchannelwatchbyUser.get(userID);
		List<String> possibleMoviesTowatch = new ArrayList<String>();
		for (Map.Entry<Integer, Integer> entry : KmostSimilarUser.entrySet()) {
			List<String> watchBysimilarUSer = listofchannelwatchbyUser
					.get(entry.getKey());
			// System.out.println(watchBysimilarUSer.size());
			possibleMoviesTowatch.addAll(watchBysimilarUSer);
			// System.out.println(possibleMoviesTowatch.size());
		}
		// System.out.println("New "+possibleMoviesTowatch.size());
		possibleMoviesTowatch.removeAll(alreadyWatched);

		if (possibleMoviesTowatch.size() > 200) {
			return possibleMoviesTowatch.subList(0, 199);
		} else
			return possibleMoviesTowatch;
	}
	private void calculateuserStatistics() throws Exception {
		// TODO Auto-generated method stub
		int similarityVal = 0;
		for (int i = 0; i < userList.size(); i++) {
			String query = "SELECT  * from Channel_usage_new where Userid="
					+ userList.get(i);
			PreparedStatement preparedStatement = coneConnection
					.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			int count = 0;
			double pref = 0.0;
			List<String> ls = new ArrayList<String>();
			while (resultSet.next()) {
				similarityVal = 0;
				pref = pref
						+ ratingMap.get(resultSet.getDouble("Chnnal_usage"));
				// pref = pref + resultSet.getDouble("rating");
				ls.add(resultSet.getString("chnlID"));
				count++;
				/*System.out.println("UserID: " + userList.get(i) + " City: "
						+ resultSet.getString("CITY"));*/
				if (resultSet.getString("CITY") != null
						&& resultSet.getString("CITY").equalsIgnoreCase(city)
						&& userID != userList.get(i)) {
					similarityVal++;
				} else {
					similarityVal = 0;
				}

			}
			similaritybetweenusr.put(userList.get(i), similarityVal);
			System.out.println(userList.get(i) + " " + similarityVal);
			// System.out.println("USER: "+userList.get(i)+" "+pref+" count : "+count);
			listofchannelwatchbyUser.put(userList.get(i), ls);
			/*
			 * System.out.println("USER id: " + userList.get(i) + " " +
			 * ls.toString());
			 */
			numberofchanellwatch.put(userList.get(i), count);
			/* System.out.println("USER: " + userList.get(i) + " " + count); */
			pref = pref / count;
			averageUserRating.put(userList.get(i),
					Math.round(pref * 100.0) / 100.0);

		}
		int count = 0;
		for (Map.Entry<Integer, Integer> entry : similaritybetweenusr
				.entrySet()) {
			if(entry.getValue()==1){
			KmostSimilarUser.put(entry.getKey(), entry.getValue());
			count++;
			}
			if (count ==5) {
				break;
			}
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*try {
			ContextualRecommendation contextualRecommendation = new ContextualRecommendation(1000896702);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
