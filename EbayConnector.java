import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EgccConnector {

	// Object that stores the connection to the database
	Connection conn;
	// userID of the user once they login
	int userID;
	static String format[] = {"%-10s", "%-25s", "%-70s", "%-15s", "%-15s", "%-15s","%-10s", "%-10s",};
	static String header [] = {"ItemID", "Title","Description","Starting Bid","Highest Bid","End Date","Seller ID", "Status"};

	public EbayConnector(String username, String password, String schema) {
		// Fill in code here to initialize conn so it connects to the database
		// using the provided parameters

		try {

			Properties info = new Properties();
			info.put( "user", username );
			info.put( "password", password );
			//connect to the database
			conn = DriverManager.getConnection("jdbc:mysql://COMPDBS300/"+ schema, info);
			//System.out.println("Login successful, welcome " + username + ".");	

		} catch (SQLException ex) {

			System.out.print(ex.getMessage());
		}	

	}


	public boolean login(String username, String password) throws SQLException {
		// Fill in code here that checks if the user exists in the database or not.  
		// make sure you retrieve the user ID and store it in the member variable
		//returns true if operation succeeded, false otherwise

		try {

			conn.setAutoCommit(false);

			// create a PreparedStatement Object
			PreparedStatement pstmt = conn.prepareStatement(
					"select userID from user where binary username = ? and password = ?");

			pstmt.setString(1, username);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();
			rs.next();
			userID = rs.getInt("userID"); //UserID might not be in column 1, double check


			pstmt.close();
			System.out.println("Login successful. Welcome, " + username + ".");

			return true;
		} catch (SQLException exception) {
			System.out.println("Invalid login credentials.");
			return false;	
		}



	}

	public boolean changePassword( String username, String newPassword) {
		// Fill in code here that allows the user to change their password to the new password
		//returns true if operation succeeded, false otherwise

		try {

			conn.setAutoCommit(false);
			// create a PreparedStatement Object
			PreparedStatement pstmt = conn.prepareStatement(
					"update user set password = ? where username = ?");

			pstmt.setString(1, newPassword);
			pstmt.setString(2, username);
			int rows = pstmt.executeUpdate();

			if (rows == 0) {
				System.out.print("shouldve thrown error, check changePassword");
			} else {
				System.out.print("Your password was successfully changed.");
			}

			conn.commit();
			pstmt.close();

			return true;
		} catch (SQLException exception) {
			System.out.println("Invalid Username.");
			return false;	
		}
	}

	public void viewMyBiddingItems () {
		// Fill in code here that displays all the items (all attributes) that the user has bid on.
		try {

			conn.setAutoCommit(false);
			// create a PreparedStatement Object
			PreparedStatement pstmt = conn.prepareStatement(
					"select * from item where itemID in (select itemID from bid where BuyerID=?)");
			pstmt.setInt(1, userID);
			ResultSet rs = pstmt.executeQuery();

			printHeader();
			while(rs.next()) {
				for(int i = 1; i <= 8; i++) {
					System.out.printf(format[i-1], rs.getString(i));
				}
				System.out.println("");
			}
			pstmt.close();

		} catch (SQLException exception) {
			System.out.println("Failed to find items the user has bid on.");
		}
	}

	public void viewMyItems() {
		// Fill in code here that displays all the items (all attributes) that the user has put up for auction. 
		try {

			conn.setAutoCommit(false);
			// create a PreparedStatement Object
			PreparedStatement pstmt = conn.prepareStatement(
					"select * from item where sellerID = ?");
			pstmt.setInt(1, userID);
			ResultSet rs = pstmt.executeQuery();

			printHeader();

			while(rs.next()) {
				for(int i = 1; i <= 8; i++) {
					System.out.printf(format[i-1], rs.getString(i));
				}

				System.out.println("");
			}
			pstmt.close();

		} catch (SQLException exception) {
			System.out.println("Failed to find items the user has put up for auction.");
		}
	}

	public void viewMyPurchases() {
		// Fill in code here that displays all the items (title, description, price, categoryID, dateSold and dateShipped) that the user has purchased
		//"select * from item where itemID in ( select itemID from purchase where buyerID = ?)"
		try {

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"select * from item where itemID in ( select itemID from purchase where buyerID = ?)");
			pstmt.setInt(1, userID);
			ResultSet rs = pstmt.executeQuery();

			printHeader();
			while(rs.next()) {
				for(int i = 1; i <= 8; i++) {
					System.out.printf(format[i-1], rs.getString(i));
				}
				System.out.println("");
			}
			pstmt.close();

		} catch (SQLException exception) {
			System.out.println("Failed to find any purchases for the user..");
		}
	}

	public void searchByKeyword(String keyword) {
		// fill in code here that displays all the items whose description contains the keyword
		try {

			conn.setAutoCommit(false);
			// create a PreparedStatement Object
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from item where description like '%" + keyword + "%';");

			printHeader();
			while(rs.next()) {
				for(int i = 1; i <= 8; i++) {
					System.out.printf(format[i-1], rs.getString(i));
				}
				System.out.println("");
			}

			conn.commit();
			stmt.close();

		} catch (SQLException exception) {
			System.out.println("No items with descption containing that keyword.");
		}


	}

	public double viewSellerRating(int itemID) {
		// return the rating of the seller that is selling the item
		try {

			conn.setAutoCommit(false);
			// create a PreparedStatement Object
			PreparedStatement pstmt0 = conn.prepareStatement(
					"select sellerID from item where itemID = ?;");
			pstmt0.setInt(1, itemID);
			ResultSet rs0 = pstmt0.executeQuery();
			rs0.next();


			PreparedStatement pstmt1 = conn.prepareStatement(
					"select rating from sellerrating where sellerID = ?;");
			pstmt1.setInt(1, rs0.getInt(1));
			ResultSet rs1 = pstmt1.executeQuery();
			rs1.next();
			rs1.getInt(1);



			PreparedStatement pstmt2 = conn.prepareStatement(
					"select avg(rating) from sellerrating where SellerID in (select sellerID from item where itemID=?);");
			pstmt2.setInt(1, itemID);
			ResultSet rs2 = pstmt2.executeQuery();
			rs2.next();


			return(rs2.getDouble(1));
		} catch (SQLException exception) {
			return -1;
		}
	}

	public boolean putItem(String title, String description, double startingBid, String endDate, String categories[]) {
		// put the item specified up for auction 
		//returns true if operation succeeded, false otherwise

		try {
			Scanner dateScn = new Scanner(endDate);
			dateScn.useDelimiter("-");
			int year = dateScn.nextInt();
			int month = dateScn.nextInt();
			int day = dateScn.nextInt();
			@SuppressWarnings("deprecation")
			Date d = new Date(year - 1900, month, day);
			Date cur = new Date();
			dateScn.close();
			if(d.before(cur)) {
				System.out.print("End date is ealrier then today's date.");
				return false;
			}
			//find out if the given categories are listed
			/*
			for (int i = 0; i < categories.length; i++) {
				PreparedStatement pstmt0 = conn.prepareStatement(
						"select * from category where ID = ?;");
				pstmt0.setString(1, categories[i]);
				pstmt0.executeQuery();
			}
			 */

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select max(itemID) from item;");
			rs.next();
			int max = rs.getInt(1);
			stmt.close();

			conn.setAutoCommit(false);
			// create a PreparedStatement Object
			PreparedStatement pstmt1 = conn.prepareStatement(
					"insert into item values(?, ?, ?, ?, 0, ?, ?, 'open');");
			pstmt1.setInt(1, max+1);
			pstmt1.setString(2, title);
			pstmt1.setString(3, description);
			pstmt1.setDouble(4, startingBid);
			pstmt1.setString(5, endDate);
			pstmt1.setInt(6, userID);
			pstmt1.executeUpdate();
			pstmt1.close();

			PreparedStatement pstmt2 = conn.prepareStatement("insert into itemcategory values(?,(select ID from category where description = ?));");

			for (int i = 0; i < categories.length; i++) {
				pstmt2.setInt(1, max+1);
				pstmt2.setString(2, categories[i]);
				pstmt2.executeUpdate();
			}
			pstmt2.close();
			conn.commit();

			System.out.println("Item successfully added to auction.");
			return true;
		} catch (SQLException exception) {
			System.out.println("Category does not exist.");
			return false;
		}



	}

	public boolean shipItem(int itemID) {
		//ships the item specified
		//returns true if operation succeeded, false otherwise

		try {

			conn.setAutoCommit(false);
			// create a PreparedStatement Object
			PreparedStatement pstmt0 = conn.prepareStatement(
					"select SellerID from item where ItemID = ?;");
			pstmt0.setInt(1, itemID);
			ResultSet rs0 = pstmt0.executeQuery();
			rs0.next();
			if(userID != rs0.getInt(1)) {
				System.out.println("User is not the seller of the item.");
				return false;
			}



			PreparedStatement pstmt1 = conn.prepareStatement(
					"update item set status ='shipped' where itemID = ?;");
			pstmt1.setInt(1, itemID);


			PreparedStatement pstmt2 = conn.prepareStatement(
					"update purchase set dateshipped = sysdate() where itemID = ?;");
			pstmt2.setInt(1, itemID);
			pstmt1.executeUpdate();
			pstmt2.executeUpdate();

			conn.commit();
			pstmt1.close();
			pstmt2.close();



			System.out.println("Item has been shipped.");
			return true;
		} catch (SQLException exception) {
			System.out.println("Invalid itemID.");
			return false;
		}
	}

	public double viewHighestBid (int itemID) {
		//returns the value of the highest bid. You can assume that the trigger is working properly.
		try {
			conn.setAutoCommit(false);
			// create a PreparedStatement Object
			PreparedStatement pstmt = conn.prepareStatement(
					"select max(currentBid) from bid where itemID = ?;");
			pstmt.setInt(1, itemID);


			ResultSet rs = pstmt.executeQuery();
			rs.next();
			return rs.getDouble(1);

		} catch (SQLException exception) {
			System.out.println("Invalid ItemID.");
			return -1;
		}
	}

	public boolean placeBid(int itemID, double bidValue) {
		//place the bid on the item 
		//returns true if operation succeeded, false otherwise

		try {
			PreparedStatement pstmt1 = conn.prepareStatement(
					"select status from item where ItemID = ?;");
			pstmt1.setInt(1, itemID);
			ResultSet rs1 = pstmt1.executeQuery();
			rs1.next();
			String status = rs1.getString(1); 

			if(!status.equals("open")) {
				System.out.println("Status of the item is not open");
				return false;
			}


			PreparedStatement pstmt0 = conn.prepareStatement(
					"select highestBid from item where ItemID = ?;");
			pstmt0.setInt(1, itemID);
			ResultSet rs0 = pstmt0.executeQuery();
			rs0.next();
			double highestBid = rs0.getDouble(1); 

			if(highestBid > bidValue) {
				System.out.println("Please make a higher bid.");
				return false;
			}


			Date cur = new Date();

			@SuppressWarnings("deprecation")
			String curTime = cur.getHours() + ":" + cur.getMinutes() + ":" + cur.getSeconds();

			conn.setAutoCommit(false);
			// create a PreparedStatement Object
			PreparedStatement pstmt = conn.prepareStatement(
					"insert into bid values (?, ?, sysdate(), ?, ?);");
			pstmt.setInt(1, userID);
			pstmt.setInt(2, itemID);
			pstmt.setString(3, curTime);
			pstmt.setDouble(4, bidValue);
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Successfully placed bid.");
			return true;

		} catch (SQLException exception) {
			System.out.println("Invalid itemID.");
			return false;
		}

	}

	public boolean rateSeller(int sellerID, double rating) {
		// place a rating on the seller specified
		//returns true if operation succeeded, false otherwise

		try {
			PreparedStatement pstmt0 = conn.prepareStatement(
					"select userID from user where userID = ?;");
			pstmt0.setInt(1, sellerID);
			ResultSet rs0 = pstmt0.executeQuery();
			rs0.next();
			rs0.getString(1);
			pstmt0.close();
		}catch(SQLException except) {
			System.out.println("The given sellerID is not a valid ID for the seller.");
			return false;
		}
		try {

			PreparedStatement pstmt1 = conn.prepareStatement("insert into sellerrating values (?, ?, ?, null, sysdate());");
			pstmt1.setInt(1, sellerID);
			pstmt1.setInt(2, userID);
			pstmt1.setDouble(3, rating);
			pstmt1.executeUpdate();

			conn.commit();
			pstmt1.close();
			System.out.println("Successfully rated seller.");
			return true;
		} catch (SQLException except){
			System.out.println("Buyer has already rated seller.");
			return false;
		}

	}	

	public boolean closeAuction (int itemID) {
		// close the auction on the item whose ID is specified
		//returns true if operation succeeded, false otherwise
		try {

			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("select * from purchase where itemID = ?;");
			pstmt.setInt(1, itemID);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			pstmt.close();
			PreparedStatement pstmt1 = conn.prepareStatement("update item set status ='sold' where itemID = ?;");
			pstmt1.setInt(1, itemID);
			int rs1 = pstmt1.executeUpdate();

			if (rs1 > 0) {
				System.out.println("Item's auction has been closed and the status has been changed to 'sold'.");
			}

			PreparedStatement pstmt2 = conn.prepareStatement("insert into purchase values ((select buyerID from bid where itemID = ? and currentbid in (select max(currentbid) from bid where itemID = ?)), ?, (select max(currentbid) from bid where itemID = ?), sysdate(), null);");
			pstmt2.setInt(1, itemID);
			pstmt2.setInt(2, itemID);
			pstmt2.setInt(3, itemID);
			pstmt2.setInt(4, itemID);




			int rs2 = pstmt2.executeUpdate();

			if (rs2  > 0) {
				System.out.println("Purchase completed.");
			}

			conn.commit();
			pstmt1.close();
			pstmt2.close();
			return true;
		} catch (SQLException exception) {
			System.out.println("Not a valid itemID.");
		}
		return false;
	}

	public boolean closeConnection() throws SQLException { 
		// close the connection here and any preparedstatements you added
		//returns true if operation succeeded, false otherwise
		try {
			conn.close();
			return true;
		} catch (SQLException except){
			return false;
		}
	}

	public static void printHeader() {
		for (int i = 0; i < 8; i++) {
			System.out.printf(format[i], header[i]);
		}
		System.out.println("");

	}

}
