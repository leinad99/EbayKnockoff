The knockoff ebay application that allows the user to perform the following tasks: 

A-	 Login: Given a username and password, eGCC should check if the user exists in the database or not. 

B-	Change password: allow the user to changer her password.

C-	View Items I bid on: List all the items (all attributes) that the user has bid on. 

D-	View my items: List all the items (all attributes) that the user has put up for auction. 

E-	View my purchases: list all the items (title, description, price, categoryID, dateSold and dateShipped) that the user has purchased.

F-	Search by keyword: Display all the items (all attributes) whose description contains the keyword specified by the user. 

G-	View seller rating: Find the average rating of the seller who is selling an item.

H-	Put a new item up for auction: the user needs to specify the information about the item. Then, eGCC adds the new item to the database. 

I-	Ship an item: Change the status of item whose ID is specified by the user to “shipped”. Also update the purchase table to indicate that the item was shipped today. 

J-	View highest bid: Find the highest bid on the item whose ID is specified by the user. 

K-	Place a bid: Place a bid on an item whose ID and new bid amount is specified by the user.  

L-	Rate a seller: Place a rating for a seller.

M-	Close an auction: Close the auction for item whose ID is specified by the user.

N-	Exit the application: this should allow the user to cleanly exit the application properly. This should close the connection and any prepared statements.
