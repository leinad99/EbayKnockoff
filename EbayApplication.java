import java.sql.SQLException;
import java.util.*;


public class EbayApplication {

	private static EbayConnector conn = new EbayConnector("u250956", "asdfasdf", "schema250956");
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws SQLException {
		String username = "";
		String password = "";
		int itemID = 0;
		boolean run = false;
		int input = 0;

		System.out.println("Welcome to knockoff Ebay\n------------\nPlease login below: ");

		while (!run) {

			
			System.out.print("Username: ");
			username = sc.nextLine();
			System.out.print("Password: ");
			password = sc.nextLine();

			if (conn.login(username, password)) {
				run = true;
			} 
		}
		
		while (run) {

			printOptions();
			input = sc.nextInt();
			sc.nextLine();

			switch (input) {
			case 0:
				System.out.print("Username: ");
				username = sc.nextLine();
				System.out.print("New password: ");
				password = sc.nextLine();
				conn.changePassword(username, password);
				break;

			case 1:
				conn.viewMyBiddingItems();
				break;

			case 2:
				conn.viewMyItems();
				break;

			case 3:
				conn.viewMyPurchases();
				break;

			case 4:
				String keyword = "";
				System.out.print("Enter keyword: ");
				keyword = sc.nextLine();
				conn.searchByKeyword(keyword);
				break;

			case 5:
				System.out.print("Input the item's ID: ");
				itemID = sc.nextInt();
				sc.nextLine();
				double rate = conn.viewSellerRating(itemID);
				if(rate != -1) {
					System.out.println("Seller rating: " + rate);
				}
				else {
					System.out.println("Not a valid itemID.");
				}
				break;

			case 6:
				addNewItem();	
				break;

			case 7:
				System.out.print("Input the item's ID: ");
				itemID = sc.nextInt();
				sc.nextLine();
				conn.shipItem(itemID);
				break;

			case 8:
				System.out.print("Input the item's ID: ");
				itemID = sc.nextInt();
				sc.nextLine();
				System.out.println("Highest bid: " + conn.viewHighestBid(itemID));
				break;

			case 9:
				Double bidValue= 0.0;
				System.out.print("Input the item's ID: ");
				itemID = sc.nextInt();
				sc.nextLine();
				System.out.print("Input the bid value: ");
				bidValue = sc.nextDouble();
				sc.nextLine();
				conn.placeBid(itemID, bidValue);
				break;

			case 10:
				int sellerID = 0;
				double rating = 0.0;
				System.out.print("Input the sellerID: ");
				sellerID = sc.nextInt();
				sc.nextLine();
				System.out.print("Input the rating: ");
				rating = sc.nextDouble();
				sc.nextLine();
				conn.rateSeller(sellerID, rating);
				break;

			case 11:
				System.out.print("Input the item's ID: ");
				itemID = sc.nextInt();
				sc.nextLine();
				conn.closeAuction(itemID);
				break;

			case 12: 
				conn.closeConnection();
				run = false;
				break;

			default:
				break;
			}



		}

		sc.close();
		System.out.println("Thanks for using knockoff Ebay.");

	}

	private static void addNewItem() {
		String title = "";
		double startingBid = 0.0;
		String endDate = "";
		String categories [];
		String description = "";
		int catNum = 0;

		System.out.print("Item title: ");
		title = sc.nextLine();
		System.out.print("Item description: ");
		description = sc.nextLine();
		System.out.print("Staring bid: ");
		startingBid = sc.nextDouble();
		sc.nextLine();
		System.out.print("Auction end date: ");
		endDate = sc.nextLine();
		System.out.print("How many categories will it have?: ");
		catNum = sc.nextInt();
		sc.nextLine();
		categories = new String [catNum];
		for (int i = 0; i < catNum; i++) {
			System.out.print("Enter the category name: ");
			categories[i] = sc.nextLine();
		}

		conn.putItem(title, description, startingBid, endDate, categories);
	}

	private static void printOptions() {
		System.out.println("0: Change my password");
		System.out.println("1: View items I bid on");
		System.out.println("2: View my items");
		System.out.println("3: View my purhases");
		System.out.println("4: Search by keyword");
		System.out.println("5: View seller rating");
		System.out.println("6: Put up a new item for auction");
		System.out.println("7: Ship an item");
		System.out.println("8: View highest bid");
		System.out.println("9: Place a bid");
		System.out.println("10: Rate a seller");
		System.out.println("11: Close an auction");
		System.out.println("12: Exit application");
		System.out.print("Input a number: ");
	}




}