# BP2 – Bookstore Management System
<hr> 
The Bookstore Management System is a platform where every part cassiers of bookstores go through has been made easier for them. 
<br/>

## Project Overview
<hr>
The **Bookstore Management System** is a desktop application built with **JavaFX** and **MySQL**, designed to make the daily tasks of bookstore employees faster, easier, and more reliable.

This system helps bookstore staff efficiently manage key operations such as:
- **Customer Management (Klantgegevens)** – Add, search, update, and delete customer information  
- **Book Management (Boekgegevens)** – Keep track of book details such as author, ISBN, price, and stock  
- **Sales Transactions (Verkoop & Transacties)** – Register and review sales for each customer  
- **Inventory Control (Voorraad)** – Manage book quantities and restock when needed  

The goal of this project is to **streamline bookstore operations** by combining all these features into one clear, user-friendly desktop application.  
For users of all ages!

## Installation Instructions
<hr>
Follow the steps below and after you’ll be ready to use this system.
<br/>

### Prerequisites
Make sure you have the following installed:
-	Any operating software that supports the required software (Windows, macOS, Linux)

### Requirements
-	Java 17 or higher
-	JavaFX 21+ SDK
-	MySQL Server (local or remote)
-	IntelliJ IDEA (recommended)
-	Git for cloning the repository

### Installation Steps
Repository: https://github.com/joers2/bookstore_management_system.git
1.	Download the repository as a zip file or clone it using GIT. 
2.	If you downloaded it as a zip file, extract the content. 
3.	Open the file and navigate to the following location: BP2-Jinte/out/artifacts/BP2_Jinte_jar
5.	Inside this folder, you'll find a file called BP2-Jinte.jar
6.	Make sure you have Java 17 or higher installed. You can check your version by typing:
java -version
In the terminal
7. Double-click the BP2-Jinte.jar file to start the application.
8. Ensure your MySQL server is running, and that the credentials in
DatabaseConnection.java match your setup (usually root / root on port 8889 for MAMP)
9. The Bookstore Management System should now open and display the login screen!

#### Configuration
Before running the application, make sure your database connection is correctly configured.  
You can find this in:

`src/main/java/com/example/bp2jinte/DatabaseConnection.java`

By default, the application uses a local MySQL setup (for MAMP):

```java
this.connection = DriverManager.getConnection(
    "jdbc:mysql://localhost:8889/portfolio_jinte",
    "root",
    "root"
);
```

If you use a different database setup, update the following details:
<br/>
Port: Change 8889 to your MySQL port (often 3306 on Windows)
<br/>
Database name: Replace portfolio_jinte with your own schema name
<br/>
Username and password: Match them with your MySQL credentials
</br>
Make sure your database contains all the required tables before running the app.
</br>
If you are using MAMP, ensure your server is running before launching the JAR file.
<br/>

## Usage Guide
<hr>
Once the project is installed and configured, follow these steps to use it:

1. Launch the application
   - Run the `BP2-Jinte.jar` file by double-clicking it  
     *(or use the command `java -jar BP2-Jinte.jar` in the terminal)*

2. Login Screen
   - Enter your username and password to log in.  
   - New users can click **“Registreren”** to create an account.

3. Home Screen
  - The home screen acts as the dashboard. From here, you can navigate to:
     - **Klantgegevens** – Add, search, edit or delete customer data  
     - **Boekgegevens** – Manage book information  
     - **Verkoop** – Register new sales transactions  
     - **Voorraad** – Check and update stock levels  
     - **Transacties** – View and filter all sales transactions

4. **Data Handling**
   - All data is stored in a **MySQL database**.  
   - CRUD (Create, Read, Update, Delete) operations are supported for all entities.

5. **Logout**
   - You can safely log out from the sidebar via the **"Uitloggen"** button.
  
## Roadmap / Timeline
<hr>
As a programmer, there's always room for expansion and there's a lot of possibilities in how I can make this even better for the users.
<br/>
That being said, the future additions I would like to add are the following:

### Future Additions
- Hashing, to make logging in and using the application in general more safe
- Role based access
- Whenever books come in to add them to the current "Voorraad"
- Notification system - for example when books are out of stock 

---
For now, have fun using this application! 
<br/>
--- 
Developed by **Jinte van Oers**








