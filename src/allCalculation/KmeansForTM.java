package allCalculation;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

import dbConnect.DBConnection;




public class KmeansForTM {

	public  List<Double> chnlUsge;
	public  double  m1, m2,m3,m4,m5, a, b,c,d,e;
	public  double m[]=new double[5];
	public  int n = 0,i;
	
	
	 public  double sum1, sum2,sum3,sum4,sum5;
	 public static BufferedReader br = null;
	 public Map<Double, Double> ratingMap;
	 public KmeansForTM(Connection connection){
		
		 ratingMap=new HashMap<Double, Double>();
		 chnlUsge=new ArrayList<Double>();
		 try{
		 String query="SELECT * FROM Channel_usage_new";
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			ResultSet resultSet=preparedStatement.executeQuery();
			while(resultSet.next()){
				chnlUsge.add(resultSet.getDouble("Chnnal_usage"));
			}
		 }catch(Exception ex){
			 JOptionPane.showMessageDialog(null,ex);
		 }
			//System.out.println(chnlUsge.toString());
			
			//System.out.println();
			// TODO Auto-generated method stub
			
			
			 boolean flag;
			 a=chnlUsge.get(0);
			 b=chnlUsge.get(2);
			 c=chnlUsge.get(5);
			 d=chnlUsge.get(7);
			 e=chnlUsge.get(10);
			 m[0]=a;
			 m[1]=b;
			 m[2]=c;
			 m[3]=d;
			 m[4]=e;
			 List<Double>cluster1=new ArrayList<Double>();
			 List<Double>cluster2=new ArrayList<Double>();
			 List<Double>cluster3=new ArrayList<Double>();
			 List<Double>cluster4=new ArrayList<Double>();
			 List<Double>cluster5=new ArrayList<Double>();
			 do{
				 sum1=0;
				 sum2=0;
				 sum3=0;
				 sum4=0;
				 sum5=0;
				 cluster1=new ArrayList<Double>();
				 cluster2=new ArrayList<Double>();
				 cluster3=new ArrayList<Double>();
				 cluster4=new ArrayList<Double>();
				 cluster5=new ArrayList<Double>();
				// n++;
				int j=0;
				int tempJ=0;
				 for(i=0;i<chnlUsge.size();i++){
					 double diff=1000000000;
					 double temp=0.0;
					 for(j=0;j<5;j++){
						 temp=Math.abs(chnlUsge.get(i)-m[j]);
						 if(temp<diff){
							 diff=temp;
							 tempJ=j;
						 }
					}
					 if(tempJ==0){
						 cluster1.add(chnlUsge.get(i));
					 }else if(tempJ==1){
						 cluster2.add(chnlUsge.get(i));
					 }else if(tempJ==2){
						 cluster3.add(chnlUsge.get(i));
					 }else if(tempJ==3){
						 cluster4.add(chnlUsge.get(i));
					 }else {
						 cluster5.add(chnlUsge.get(i));
					 }
				 }
				 for(i=0;i<cluster1.size();i++){
					 sum1=sum1+cluster1.get(i);
				 }
				 for(i=0;i<cluster2.size();i++){
					 sum2=sum2+cluster2.get(i);
				 }
				 for(i=0;i<cluster3.size();i++){
					 sum3=sum3+cluster3.get(i);
				 }
				 for(i=0;i<cluster4.size();i++){
					 sum4=sum4+cluster4.get(i);
				 }
				 for(i=0;i<cluster5.size();i++){
					 sum5=sum5+cluster5.get(i);
				 }
				 a=m[0];
				 b=m[1];
				 c=m[2];
				 d=m[3];
				 e=m[4];
				 m[0] = (sum1 / cluster1.size());
				 m[1] = (sum2 / cluster2.size());
				 m[2] = (sum3 / cluster3.size());
				 m[3] = (sum4 / cluster4.size());
				 m[4] = (sum5 / cluster5.size());
				 n++;
				 
				 if(n>10 ||(m[0]==a && m[1]==b&& m[2]==c && m[3]==d && m[4]==e)){
					/*System.out.println(Math.round(m[0] * 100.0) / 100.0+" " +Math.round(a * 100.0) / 100.0);
					System.out.println(Math.round(m[1] * 100.0) / 100.0+" " +Math.round(b * 100.0) / 100.0);
					System.out.println(Math.round(m[2] * 100.0) / 100.0+" " +Math.round(c * 100.0) / 100.0);
					System.out.println(Math.round(m[3] * 100.0) / 100.0+" " +Math.round(d * 100.0) / 100.0);
					System.out.println(Math.round(m[4] * 100.0) / 100.0+" " +Math.round(e * 100.0) / 100.0);*/
					 flag=false; 
				 }else{
					 flag=true;
				 }
			 }while(flag);
			// System.out.println("Cluster 1"+ " " +n);
			 for(i=0;i<cluster1.size();i++){
				// System.out.print(cluster1.get(i)+" ");
				 ratingMap.put(cluster1.get(i), 0.2);
			 }
			//// System.out.println();
			 //System.out.println("Cluster 2");
			 for(i=0;i<cluster2.size();i++){
				// System.out.print(cluster2.get(i)+" ");
				 ratingMap.put(cluster2.get(i), 0.4);
			 }
			 //System.out.println();
			 //System.out.println("Cluster 3");
			 for(i=0;i<cluster3.size();i++){
				// System.out.print(cluster3.get(i)+" ");
				 ratingMap.put(cluster3.get(i), 0.6);
			 }
			 //System.out.println();
			 //System.out.println("Cluster 4");
			 for(i=0;i<cluster4.size();i++){
				 //System.out.print(cluster4.get(i)+" ");
				 ratingMap.put(cluster4.get(i), 0.8);
			 }
			// System.out.println();
			// System.out.println("Cluster 5");
			 for(i=0;i<cluster5.size();i++){
				// System.out.print(cluster5.get(i)+" ");
				 ratingMap.put(cluster5.get(i), 1.0);
			 }
			// System.out.println();
			 Arrays.sort(m);
			 for(i=0;i<m.length;i++){
				 //System.out.println(Math.round(m[i] * 100.0) / 100.0);
			 }
			// printMap(ratingMap);
	 }
	 private void printMap(Map<Double, Double> ratingMap2) {
		// TODO Auto-generated method stub
		for(Map.Entry<Double, Double>m: ratingMap2.entrySet()){
			System.out.println(m.getKey()+" "+m.getValue());
		}
	}
	public Map<Double, Double>getRatingMap(){
		 return ratingMap;
	 }
	public static void main(String[] args) throws Exception {
		//KmeansForTM kmeansForTM=new KmeansForTM();
	}

}
