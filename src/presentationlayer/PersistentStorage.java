package presentationlayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import pos.domainlayer.ItemID;
import pos.domainlayer.Money;
import pos.domainlayer.ProductCatalog;
import pos.domainlayer.ProductDescription;
import pos.domainlayer.Register;
import pos.domainlayer.Store;

public class PersistentStorage {
	
	// db연결을 위한 변수들
	private Connection myConnection;
	private Statement myStatement;
	private ResultSet myResultSet;
	public ArrayList<ItemID> idList = new ArrayList<ItemID>();
	public ArrayList<Money> priceList = new ArrayList<Money>();
	public ArrayList<String> descList = new ArrayList<String>();

	
	public PersistentStorage(String dbFileName){
		// db에 연결
				try {
					// connect to database
					myConnection = DriverManager.getConnection("jdbc:ucanaccess://" + dbFileName);
							   
					// create Statement for executing SQL
					myStatement = myConnection.createStatement();
				} catch(SQLException exception) {
					exception.printStackTrace();
				}
	}
	
	public ArrayList<ItemID> loadItemIds() {
		// TODO Auto-generated method stub
		try {
			myResultSet = myStatement.executeQuery("SELECT itemId FROM ProductDescriptions");
			while(myResultSet.next()) {
				ItemID id = new ItemID(myResultSet.getString("itemId"));
				idList.add(id);
			}
			myResultSet.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return idList;
	}
	
	public ArrayList<Money> loadPriceList(){
		Money price = null;
		try {
			myResultSet = myStatement.executeQuery("SELECT price FROM ProductDescriptions");
			while(myResultSet.next()) {
				price = new Money(myResultSet.getInt("price"));
				priceList.add(price);
			}
			myResultSet.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return priceList;
	}
	
	public ArrayList<String> loadDescList(){
		String desc = null;
		try {
			myResultSet = myStatement.executeQuery("SELECT description FROM ProductDescriptions");
			while(myResultSet.next()) {
				desc = myResultSet.getString("description");
				descList.add(desc);
			}
			myResultSet.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return descList;
	}
	
	public Money getPrice(String id) {
		Money price = null;
		try {
			myResultSet = myStatement.executeQuery("SELECT price FROM ProductDescriptions WHERE itemID = " + id);
			if(myResultSet.next()) {
				price = new Money(myResultSet.getInt("price"));
			}
			myResultSet.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return price;
	}
	
	public String getDesc(String id) {
		String desc = null;
		try {
			myResultSet = myStatement.executeQuery("SELECT description FROM ProductDescriptions WHERE itemID = " + id);
			if(myResultSet.next()) {
				desc = myResultSet.getString("description");
			}
			myResultSet.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return desc;
	}
}
