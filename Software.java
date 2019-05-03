import java.util.Scanner;
import java.sql.*;
import java.text.SimpleDateFormat;



public class Software{

    public static String dbURL = "jdbc:mysql://192.168.137.130:3306/scrum?useSSL=false&serverTimezone=UTC";
	private static String username="cecs323b";
	private static String password="cecs323";
	private static Connection conn = null;
	private static Statement stmnt = null;
	private static PreparedStatement statement = null;
	static Scanner scan = new Scanner(System.in);
	public static void main(String[] args) throws Exception
	{
        System.out.println("Welcome to webuildsoftware.com!");
        connect();
	}

	public static void menu() throws Exception
	{
        boolean choose = true;
        while(choose)
        {
           	System.out.println("\nPlease choose an operation:");
           	System.out.println("1. Create a Sprint for a project\n2. Create a project\n3. Add user stories to sprint backlog" +
           	"\n4. Display developer(s) and/or Sprint(s)\n5. List developers that are part of a Sprint\n6. CRUD operations for management members and sprint team members" +
           	"\n7. CRUD operations for user stories to project/product backlog\n8. Exit");
           	//CRUD - Create Read Update Delete
          	 
           	int choice = scan.nextInt();
               	choose = false;
               	switch(choice){
                   	case 1: createSprint();
                           	break;
                   	case 2: createProject();
                           	break;
                   	case 3: addStories();
                           	break;
                   	case 4: displayDevsSprints();
                           	break;
                   	case 5: listDevs();
                           	break;
                   	case 6: membersCRUD();
                           	break;
                   	case 7: storiesCRUD();
						break;
					case 8: System.out.println("Goodbye!");
							System.exit(0);
							break;
                   	default:System.out.println("That was not a choice!1\n");
                           	choose = true;
               	}
        }
	}

	//Connect to the database
	public static void connect() throws Exception
	{
        try {
               	conn = DriverManager.getConnection(dbURL, username, password);
               	if( conn != null)
                       	menu();
        } catch (SQLException e)
        {
               	System.out.println("SQLException: " + e.getMessage());
               	System.out.println("SQLState: " + e.getSQLState());
               	System.out.println("VendorError: " + e.getErrorCode());
        }
	}

	//Create a sprint in the database
	public static void createSprint() throws Exception
	{
        System.out.println("Please enter the name of the project you would wish to create a sprint for: ");
        scan.nextLine();
        String user_projectName = scan.nextLine();
        if(verifyProject(user_projectName))
        {
               	System.out.println("Please enter the start date of the sprint in yyyyMMdd format: ");
              	String user_sStartDate = scan.nextLine();
               	System.out.println("Please enter the name of the sprint: ");
               	String user_sprintName = scan.nextLine();
               	System.out.println("Please enter the end date of the sprint in yyyyMMdd format: ");
               	String user_sEndDate = scan.nextLine() ;
                String sql = "INSERT into Sprints (meetingDate, projectName, sStartDate, sprintName, sEndDate)"
               	+ "VALUES (NULL, ?, ?, ?, ?)";
               	 
                try
               	{
                   	PreparedStatement stmnt = conn.prepareStatement(sql);
                   	stmnt.setString(1, user_projectName);
                   	stmnt.setString(2, user_sStartDate);
                   	stmnt.setString(3, user_sprintName);
                   	stmnt.setString(4, user_sEndDate);
                   	stmnt.executeUpdate();
              	}catch(SQLException e) {
                   	System.out.println("SQLException: " + e.getMessage());
                   	System.out.println("SQLState: " + e.getSQLState());
                   	System.out.println("VendorError: " + e.getErrorCode());
               	}
        }
        else
        {
               	System.out.println("That project does not exist, please try again.\n\n");
        }
        menu();
	}

	//Verifies that a project exists by project name
	public static boolean verifyProject(String user_projectName) throws SQLException
	{
    	String sql = "SELECT * FROM Projects WHERE projectName ='" + user_projectName + "'";
    	try {
            	stmnt = conn.createStatement();
            	ResultSet result = stmnt.executeQuery(sql);
            	if(result.next())
                    	return true;
    	}catch(SQLException e) {
            	System.out.println("SQLException: " + e.getMessage());
            	System.out.println("SQLState: " + e.getSQLState());
            	System.out.println("VendorError: " + e.getErrorCode());
    	}
    	return false;
	}

	//Verifies that a project exists by projectID
	public static boolean verifyProject(int user_projectID) throws SQLException
	{
    	String sql = "SELECT * FROM Projects WHERE projectID ='" + user_projectID + "'";
    	try {
            	stmnt = conn.createStatement();
            	ResultSet result = stmnt.executeQuery(sql);
            	if(result.next())
                    	return true;
    	}catch(SQLException e) {
            	System.out.println("SQLException: " + e.getMessage());
            	System.out.println("SQLState: " + e.getSQLState());
            	System.out.println("VendorError: " + e.getErrorCode());
    	}
    	return false;
	}

	//Create a project in the database
	public static void createProject() throws Exception
	{
        System.out.println("Please enter the name of the project you wish to create: ");
        scan.nextLine();
        String user_projectName = scan.nextLine();
        while(verifyProject(user_projectName))
        {
               	System.out.println("This project name already exists, please enter a unique name:");
               	user_projectName = scan.nextLine();
        }
        System.out.println("Please enter a four digit ID for this project:");
        int user_projectID = scan.nextInt();
        while(verifyProject(user_projectID) || String.valueOf(user_projectID).length() != 4)
        {
               	if( String.valueOf(user_projectID).length() != 4)
               	{
                       	System.out.println("Project ID is more or less than 4 digits, enter again: ");
               	}
               	if( verifyProject(user_projectID) )
               	{
                       	System.out.println("Project ID is not unique, enter again: ");
               	}
            	 
               	user_projectID = scan.nextInt();
        }
       	
        System.out.println("Please enter the team name for the project, if you would like to see a list of all available teams, type list: ");
        scan.nextLine();
        String user_teamName = scan.nextLine();
        while(user_teamName.equals("list") || !verifyTeam(user_teamName))
        {
           	if(user_teamName.equals("list"))
           	{
          		 listTeams();
          		 System.out.println("Please enter the team name for the project: ");
          		 user_teamName = scan.nextLine();
           	}
           	else
           	{
          		 System.out.println("Team does not exist");
          		 System.out.println("Please enter the team name for the project: ");
          		 user_teamName = scan.nextLine();
           	}
        }
       	 
       	String sql = "INSERT into Projects(projectName, projectID, teamName) "
        	+ "VALUES(?, ?, ?)";
       	 
        try
        {
           	PreparedStatement stmnt = conn.prepareStatement(sql);
           	stmnt.setString(1, user_projectName);
           	stmnt.setInt(2, user_projectID);
           	stmnt.setString(3, user_teamName);
           	stmnt.executeUpdate();
        }catch(SQLException e) {
           	System.out.println("SQLException: " + e.getMessage());
           	System.out.println("SQLState: " + e.getSQLState());
           	System.out.println("VendorError: " + e.getErrorCode());
        }

        menu();
	}
    
	//Lists all available teams
	public static void listTeams() throws Exception
	{
   	 	stmnt = conn.createStatement();
    	ResultSet result = stmnt.executeQuery("SELECT * FROM ScrumTeams");
    	ResultSetMetaData rsmd = result.getMetaData();
    	int numberCols = rsmd.getColumnCount();
    	for( int i=1; i<=numberCols; i++)
    	{
            	//prints column names
            	System.out.print(rsmd.getColumnLabel(i) + "\t");
    	}

    	System.out.println("\n-------------------------------------------");

    	while(result.next())
    	{
          	 
            	String teamName = result.getString(1);
            	int teamID = result.getInt(2);
            	System.out.format("%n%-25s%-25s", teamName, teamID);
    	}
		System.out.print("\n");
	}
 
	//Verifies that a team exists by teamName
	public static boolean verifyTeam(String user_teamName) throws SQLException
	{
        String sql = "SELECT * FROM ScrumTeams WHERE teamName ='" + user_teamName + "'";
        try {
               	stmnt = conn.createStatement();
               	ResultSet result = stmnt.executeQuery(sql);
               	if(result.next())
                       	return true;
        }catch(SQLException e) {
               	System.out.println("SQLException: " + e.getMessage());
               	System.out.println("SQLState: " + e.getSQLState());
               	System.out.println("VendorError: " + e.getErrorCode());
        }
        return false;
	}

	// Add user stories to sprint backlog
	public static void addStories() throws Exception 
	{
		System.out.println("Please enter the name of the project for the user story you would like to add to the sprint backlog:");
		scan.nextLine();
		String user_projectName = scan.nextLine();
		while (!verifyProject(user_projectName)) {
			System.out.println("Project does not exist. Please enter a valid project:");
			user_projectName = scan.nextLine();
		}
		System.out.println("Please assign an ID for this user story:");
		int user_usID = scan.nextInt();
		
		//have to verify start date for sprint backlog
		//date cannot be null, and must be found in table
		System.out.println("Please enter the start date for the sprint backlog:");
		scan.nextLine();
		String user_sSD = scan.nextLine();
		Date user_sSDate = (Date)new SimpleDateFormat("yyyy-MM-dd").parse(user_sSD);
		java.sql.Date user_sStartDate = new java.sql.Date(user_sSDate.getTime());;
		//
		
		System.out.println("Please enter the role of the user story:");
		String user_role = scan.nextLine();
		System.out.println("Please enter the goal of the user story:");
		String user_goal = scan.nextLine();
		System.out.println("Please enter the benefit of the user story:");
		String user_benefit = scan.nextLine();
		System.out.println("Please enter the priority number of the user story:");
		int user_priority = scan.nextInt();

		String sql = "INSERT into UserStories(projectName, sStartDate, usID, role, goal, benefit, priority) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?)";

		try {
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setString(1, user_projectName);
			stmnt.setDate(2, user_sStartDate);
			stmnt.setInt(3, user_usID);
			stmnt.setString(4, user_role);
			stmnt.setString(5, user_goal);
			stmnt.setString(6, user_benefit);
			stmnt.setInt(7, user_priority);
			stmnt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		menu();
	}

	//Display developer(s) and/or Sprint(s)
	public static void displayDevsSprints() throws Exception
	{
    	System.out.println("1. List the developers working on a sprint"
    	+ "\n2. List the sprints");
  	 
    	int choice = scan.nextInt();

    	if(choice == 1)
    	{
        	stmnt = conn.createStatement();
        	ResultSet result = stmnt.executeQuery("SELECT e.employeeID, e.firstName, e.lastName FROM Employees e INNER JOIN  TeamRoles t on e.employeeID = t.employeeID WHERE teamRole = 'developer'");
        	ResultSetMetaData rsmd = result.getMetaData();
        	int numberCols = rsmd.getColumnCount();
        	for( int i=1; i<=numberCols; i++)
        	{
                	//prints column names
                	System.out.print(rsmd.getColumnLabel(i) + "\t");
        	}

        	System.out.println("\n-------------------------------------------");

        	while(result.next())
        	{
                	int employeeID = result.getInt(1);
                	String employeeFN = result.getString(2);
                	String employeeLN = result.getString(3);
                	System.out.format("%n%-25s%-25s%-25s", employeeID, employeeFN, employeeLN);
        	}
    	}

    	if(choice == 2)
    	{
        	stmnt = conn.createStatement();
        	ResultSet result = stmnt.executeQuery("SELECT * FROM Sprints");
        	ResultSetMetaData rsmd = result.getMetaData();
        	int numberCols = rsmd.getColumnCount();
        	for( int i=1; i<=numberCols; i++)
        	{
                	//prints column names
                	System.out.print(rsmd.getColumnLabel(i) + "\t");
        	}

        	System.out.println("\n-------------------------------------------");

        	while(result.next())
        	{
                	Date meeting = result.getDate(1);
                	String projectName = result.getString(2);
                	Date startDate = result.getDate(3);
                	int teamName = result.getInt(3);
                	Date endDate = result.getDate(4);
                	System.out.format("%n%-25s%-25s%-25s%-25s%-25s", meeting, projectName, startDate, teamName, endDate);
        	}
    	}

    	menu();
	}
 
	//List developers that are part of a Sprint
	public static void listDevs() throws Exception
	{
		System.out.println("Listing all developers that are part of a Sprint");
		stmnt = conn.createStatement();
        ResultSet result = stmnt.executeQuery("SELECT e.employeeID, e.firstName, e.lastName FROM Sprints s INNER JOIN Projects p on s.projectName = p.projectName INNER JOIN ScrumTeams st on p.teamName = st.teamName INNER JOIN TeamRoles tr on st.teamName = tr.teamName INNER JOIN Employees e on tr.employeeID = e.employeeID");
        ResultSetMetaData rsmd = result.getMetaData();
        int numberCols = rsmd.getColumnCount();
		for( int i=1; i<=numberCols; i++)
        {
               	//prints column names
               	System.out.print(rsmd.getColumnLabel(i) + "\t");
        }
        System.out.println("\n-------------------------------------------");

        while(result.next())
        {
               	int employeeID = result.getInt(1);
               	String employeeFN = result.getString(2);
               	String employeeLN = result.getString(3);
             	System.out.format("%n%-25s%-25s%-25s", employeeID, employeeFN, employeeLN);
        }
        menu();
	}

	//CRUD operations for management members and sprint team members
	public static void membersCRUD()
	{
        boolean choose = true;
        while(choose)
        {
           	System.out.println("Please choose an operation:");
           	System.out.println("1. Create a member\n2. View all members\n3. Update a member" +
           	"\n4. Delete a member");
           	//CRUD - Create Read Update Delete
           	try
           	{
               	int choice = scan.nextInt();
               	choose = false;
               	switch(choice){
                   	case 1: createMember();
                           	break;
                   	case 2: readMember();
                           	break;
                   	case 3: updateMember();
                           	break;
                   	case 4: deleteMember();
                           	break;
                   	default:System.out.println("That was not a choice!\n");
                           	choose = true;
               	}
           	} catch(Exception i)
           	{
               	System.out.println("That was not a choice!\n");
               	scan.nextLine();
           	}
        }
	}

	//Verifies that a project exists by projectID
	public static boolean verifyEmpID(int user_employeeID)
	{
        String sql = "SELECT * FROM Employees WHERE employeeID ='" + user_employeeID + "'";
        try {
            	stmnt = conn.createStatement();
               	ResultSet result = stmnt.executeQuery(sql);
               	if(result.next())
                       	return true;
        }catch(SQLException e) {
               	System.out.println("SQLException: " + e.getMessage());
               	System.out.println("SQLState: " + e.getSQLState());
               	System.out.println("VendorError: " + e.getErrorCode());
        }
        return false;
	}

	public static void createMember() throws Exception
    {
        System.out.println("Please enter the employee ID for the new employee:");
        int user_employeeID = scan.nextInt();
        while(verifyEmpID(user_employeeID))
        {
                System.out.println("This ID is already taken, please choose another:");
                user_employeeID = scan.nextInt();
        }
        scan.nextLine();
        System.out.println("Please enter the employees first name: ");
        String user_firstName = scan.nextLine();
        System.out.println("Please enter the employees last name: ");
        String user_lastName = scan.nextLine();
        String sql = "INSERT into Employees(employeeID, firstName, lastName) VALUES (?, ?, ?)";
        try {
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, user_employeeID);
            stmnt.setString(2, user_firstName);
            stmnt.setString(3, user_lastName);
            stmnt.executeUpdate();
            System.out.println(user_firstName + " " + user_lastName + " is now an employee!");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        boolean user_a = true;
        while(user_a)
        {
            System.out.println("Enter the name of the team " + user_firstName + " " + user_lastName + " is going to join:");
            String user_teamName = scan.nextLine();
            while(!verifyTeam(user_teamName))
            {
                System.out.println("That team does not exist! Please try again: ");
                user_teamName = scan.nextLine();
            }
            System.out.println("Enter the name of the role " + user_firstName + " " + user_lastName + " will perform in the team:");
            String user_teamRole = scan.nextLine();
            System.out.println("Confirm " + user_firstName + " " + user_lastName + " joining " + user_teamName + " as " + user_teamRole + ". (y/n)");
            String user_answer = scan.nextLine();
            boolean chose = true;
            while(chose)
            {
                if(user_answer.charAt(0) == 'y' || user_answer.charAt(0) == 'Y')
                {
                    chose = false;
                    user_a = false;
                    String s = "INSERT into TeamRoles(employeeID, teamName, teamRole) VALUES(?, ?, ?)";
                    try {
                        PreparedStatement stmnt = conn.prepareStatement(s);
                        stmnt.setInt(1, user_employeeID);
                        stmnt.setString(2, user_teamName);
                        stmnt.setString(3, user_teamRole);
                        stmnt.executeUpdate();
                        System.out.println(user_firstName + " " + user_lastName + " is now a part of " + user_teamName + " as " + user_teamRole + "!");
                    } catch (SQLException e) {
                        System.out.println("SQLException: " + e.getMessage());
                        System.out.println("SQLState: " + e.getSQLState());
                        System.out.println("VendorError: " + e.getErrorCode());
                    }
                }else if(user_answer.charAt(0) == 'n' || user_answer.charAt(0) == 'N')
                {
                    chose = false;
                    System.out.println("");
                }
                else
                {
                    System.out.println("Please enter y to confirm or n to try again:");
                    user_answer = scan.nextLine();
                }
            }
        }
        System.out.println("");
        menu();
    }

	public static void readMember() throws Exception
	{
   	 	stmnt = conn.createStatement();
    	ResultSet result = stmnt.executeQuery("SELECT e.employeeID, e.firstName, e.lastName FROM Employees e INNER JOIN  TeamRoles t on e.employeeID = t.employeeID WHERE teamRole = 'developer'");
    	ResultSetMetaData rsmd = result.getMetaData();
    	int numberCols = rsmd.getColumnCount();
    	for( int i=1; i<=numberCols; i++)
    	{
            	//prints column names
            	System.out.print(rsmd.getColumnLabel(i) + "\t");
    	}

    	System.out.println("\n-------------------------------------------");

    	while(result.next())
    	{
            	int employeeID = result.getInt(1);
            	String employeeFN = result.getString(2);
            	String employeeLN = result.getString(3);
            	System.out.format("%n%-25s%-25s%-25s", employeeID, employeeFN, employeeLN);
    	}
    	menu();
	}
    
	public static void updateMember()
	{
   	 
	}
    
	public static void deleteMember() throws Exception
	{
		System.out.println("Please enter the employee number for the employee you wish to delete, to see a list, type list: ");
		scan.nextLine();
		String toDelete = scan.nextLine();
		int delete = 0;
		if(toDelete.equals("list"))
		{
			
			displayDevsSprints();
			System.out.println("Please enter employee ID to delete: ");
			scan.nextLine();
			delete = scan.nextInt();
		}
		else
		{
			delete = Integer.parseInt(toDelete);
		}
		
		while(!verifyEmpID(delete))
		{
			System.out.println("This user ID does not exist, please enter another");
			delete = scan.nextInt();

		}
		if(verifyTeamRoles(delete))
		{
			String sql = "DELETE * FROM TeamRoles WHERE employeeID = ?";
			
			statement = conn.prepareStatement(sql);
			statement.setInt(1, Integer.parseInt(toDelete));
			statement.executeUpdate();
		}
		String sql = "DELETE * FROM Employees WHERE employeeID = ?";
		
		statement = conn.prepareStatement(sql);
		statement.setInt(1, Integer.parseInt(toDelete));
		statement.executeUpdate();
	}
    
  //Verifies that a project exists by projectID
	public static boolean verifyTeamRoles(int user_employeeID) throws SQLException
	{
        String sql = "SELECT * FROM TeamRoles WHERE employeeID ='" + user_employeeID + "'";
        try {
            	stmnt = conn.createStatement();
    	       	ResultSet result = stmnt.executeQuery(sql);
            	if(result.next())
                    	return true;
    	}catch(SQLException e) {
            	System.out.println("SQLException: " + e.getMessage());
            	System.out.println("SQLState: " + e.getSQLState());
            	System.out.println("VendorError: " + e.getErrorCode());
    	}
       	 
    	return false;
	}


	//CRUD operations for user stories to project/product backlog
	public static void storiesCRUD() throws Exception 
	{
        System.out.println("\nPlease choice an operation:");
        System.out.println("1. Create a user story for a product\n2. Read all user stories from a product" +
            "\n3. Update a user story from a project\n4. Delete a user story from a project" +
            "\n5. Return to main menu");
        int choice = 0;
        choice = scan.nextInt();
        boolean choose = true;
        while (choose) {
            choose = false;
            switch (choice) {
                case 1:
                    createUserStory();
                    break;
                case 2:
                    readUserStory();
                    break;
                case 3:
                    updateUserStory();
                    break;
                case 4:
                    deleteUserStory();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("That was not a choice!\n");
                    choose = false;
            }
        }
        menu();
    }

	public static void createUserStory() throws Exception
	{
        System.out.print("\nPlease enter project name: ");
        String projectName = scan.nextLine();
        System.out.print("Please enter user story ID: ");
        int usID = scan.nextInt();

        try {
            stmnt = conn.createStatement();
            ResultSet results = stmnt.executeQuery("select * from UserStories where projectName = '" + projectName +
                "' and usID = " + usID);
            if (results.isBeforeFirst()) {
            	System.out.println("There already is a user story with usID: " + usID);
            	return;
            } 
            scan.nextLine();
            System.out.print("Please enter role: ");
            String role = scan.nextLine();
            System.out.print("Please enter goal: ");
            String goal = scan.nextLine();
            System.out.print("Please enter benefit: ");
            String benefit = scan.nextLine();
            System.out.print("Please enter priority: ");
            int priority = scan.nextInt();
            
            String sql = "INSERT INTO UserStories (projectName, sStartDate, usID, role, goal, benefit, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, projectName);
            statement.setDate(2, null);
            statement.setInt(3, usID);
            statement.setString(4, role);
            statement.setString(5, goal);
            statement.setString(6, benefit);
            statement.setInt(7, priority);
            
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
				System.out.println("User Story inserted!\n\n");
				menu();
            } else {
				System.out.println("User Story not inserted.\n\n");
				menu();
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

	public static String readUserStory() 
	{
        try {
            stmnt = conn.createStatement();
            System.out.print("\nPlease enter project name: ");
            String uProjectName = scan.nextLine();
            
            ResultSet results = stmnt.executeQuery("select * from UserStories where projectName = '" + uProjectName + "'");
            if (!results.isBeforeFirst()) {
                System.out.println("No data available, there are no user stories for project " + uProjectName + ".");
                return "";
            }
            
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            int displaySize = 0;
            int totalSize = 0;
            for (int i = 1; i <= numberCols; i++) {
                displaySize = rsmd.getColumnDisplaySize(i) + 3;
                totalSize = totalSize + displaySize;
                System.out.printf("%-" + displaySize + "s", rsmd.getColumnLabel(i));
            }
            
            System.out.println(String.format("\n%-" + totalSize + "s", "").replace(' ', '-'));
            while (results.next()) {
                int projectNameSize = rsmd.getColumnDisplaySize(1) + 3;
                int sStartDateSize = rsmd.getColumnDisplaySize(2) + 3;
                int usIDSize = rsmd.getColumnDisplaySize(3) + 3;
                int roleSize = rsmd.getColumnDisplaySize(4) + 3;
                int goalSize = rsmd.getColumnDisplaySize(5) + 3;
                int benefitSize = rsmd.getColumnDisplaySize(6) + 3;
                int prioritySize = rsmd.getColumnDisplaySize(7) + 3;
                String projectName = results.getString(1);
                Date sStartDate = results.getDate(2);
                int usID = results.getInt(3);
                String role = results.getString(4);
                String goal = results.getString(5);
                String benefit = results.getString(6);
                int priority = results.getInt(7);

                System.out.printf("%-" + projectNameSize + "s%-" + sStartDateSize + "s%-" + usIDSize +
                    "s%-" + roleSize + "s%-" + goalSize + "s%-" + benefitSize + "s%-" + prioritySize +
                    "s\n", projectName, sStartDate, usID, role, goal, benefit, priority);
            }
            System.out.println();
            return uProjectName;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return "";
        }
    }

	public static void updateUserStory() throws Exception
	{
    	String projectName = readUserStory();
    	if(projectName == "")
    		return;
    	System.out.print("Enter user story ID to modify: ");
    	int usID = scan.nextInt();
    	scan.nextLine();
    	
    	try {
    		ResultSet results = stmnt.executeQuery("select * from UserStories where usID = " + usID);
    		if (!results.isBeforeFirst()) {
    			System.out.println(usID + " is not a valid user story ID.");
    			return;
    		}
    		
    		System.out.print("Please enter new role: ");
    		String role = scan.nextLine();
    		System.out.print("Please enter new goal: ");
    		String goal = scan.nextLine();
    		System.out.print("Please enter mew benefit: ");
    		String benefit = scan.nextLine();
    		System.out.print("Please enter new priority: ");
    		int priority = scan.nextInt();
    		scan.nextLine();
    		
    		String sql = "UPDATE UserStories SET role = ?, goal = ?, benefit = ?, priority = ? WHERE usID = ?";
			statement = conn.prepareStatement(sql);
			statement.setString(1, role);
			statement.setString(2, goal);
			statement.setString(3, benefit);
			statement.setInt(4, priority);
			statement.setInt(5,  usID);
			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("User story updated.\n\n");
				menu();
			}
			else {
				System.out.println("User story not updated.\n\n");
				menu();
			}
    		
    	} catch(SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
    	}
    }

	public static void deleteUserStory() throws Exception
	{
    	String projectName = readUserStory();
    	if(projectName == "")
    		return;
    	System.out.print("Enter user story ID to delete: ");
    	int usID = scan.nextInt();
    	scan.nextLine();
    	try {
    		ResultSet results = stmnt.executeQuery("select * from UserStories where usID = " + usID);
    		if (!results.isBeforeFirst()) {
    			System.out.println(usID + " is not a valid user story ID.\n");
    			return;
    		}
    		
    		System.out.print("Are you sure you want to delete this user story? (Y/N): ");
    		String choice = scan.nextLine();
    		if(choice.equals("Y") || choice.equals("y")) {
    			String sql = "DELETE FROM UserStories WHERE usID = ?";
				statement = conn.prepareStatement(sql);
				statement.setInt(1,  usID);
				int rowsInserted = statement.executeUpdate();
				System.out.println("User story deleted.\n\n");
				menu();
    		}
    		else {
				System.out.println("User story not deleted.\n\n");
				menu();
    		}
    		
    	} catch(SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
    	}
    }
 }