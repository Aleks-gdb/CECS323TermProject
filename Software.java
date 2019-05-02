import java.util.Scanner;
import java.sql.*;



public class Software{

        public static String dbURL = "jdbc:mysql://192.168.56.138:3306/termProject?useSSL=false&serverTimezone=UTC";
        private static String username="cecs323b";
        private static String password="cecs323";
        private static Connection conn = null;
        private static Statement stmnt = null;
        static Scanner scan = new Scanner(System.in);
        public static void main(String[] args)
        {
                System.out.println("Welcome to webuildsoftware.com!");
                menu();
        }
     
        public static void menu()
        {
                boolean choose = true;
                while(choose)
                {
                    System.out.println("Please choose an operation:");
                    System.out.println("1. Create a Sprint for a project\n2. Create a project\n3. Add user stories to sprint backlog" +
                    "\n4 Display developer(s) and/or Sprint(s)\n5. List developers that are part of a Sprint\n6. CRUD operations for management members and sprint team members" +
                    "\n7. CRUD operations for user stories to project/product backlog");
                    //CRUD - Create Read Update Delete
                    try
                    {
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
                            default:System.out.println("That was not a choice!\n");
                                    choose = true;
                        }
                    } catch(Exception i)
                    {
                        System.out.println("That was not a choice!\n");
                        scan.nextLine();
                    }
                    scan.close();
                }
        }
     
        //Connect to the database
        public static void connect()
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
        public static void createSprint()
        {
                System.out.println("Please enter the name of the project you would wish to create a sprint for: ");
                String user_projectName = scan.nextLine();
                if(verifyProject(user_projectName))
                {
                        System.out.println("Please enter the start date of the sprint in mmDDyyyy format: ");
                        int user_sStartDate = scan.nextInt();
                        System.out.println("Please enter the name of the sprint: ");
                        String user_sprintName = scan.nextLine();
                        System.out.println("Please enter the end date of the sprint in mmDDyyyy format: ");
                        int user_sEndDate = scan.nextInt() ;
     
                        String sql = "INSERT into Sprints (meetingDate, projectName, sStartDate, sprintName, sEndDate)"
                        + "VALUES (NULL, ?, ?, ?, ?)";
                }
                else
                {
                        System.out.println("That project does not exist, please try again.");
                }
                menu();
        }
     
        //Verifies that a project exists by project name
        public static boolean verifyProject(String user_projectName)
        {
                String sql = "SELECT * FROM PROJECTS WHERE projectName ='" + user_projectName + "'";
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
                menu();
                return false;
        }
     
        //Verifies that a project exists by projectID
        public static boolean verifyProject(int user_projectID)
        {
                String sql = "SELECT * FROM PROJECTS WHERE projectID ='" + user_projectID + "'";
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
                menu();
                return false;
        }
     
        //Create a project in the database
        public static void createProject()
        {
                System.out.println("Please enter the name of the project you wish to create: ");
                String user_projectName = scan.nextLine();
                while(verifyProject(user_projectName))
                {
                        System.out.println("This project name already exists, please enter a unique name.");
                        user_projectName = scan.nextLine();
                }
                System.out.println("Please enter a four digit ID for this project");
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
                String sql = "INSERT into Projects(projectName, projectID, teamName) "
                + "VALUES(?, ?, ?)";

                menu();
        }
     
        //Add user stories to sprint backlog
        public static void addStories()
        {
                String sql = "UPDATE UserStories SET sStartDate = “sStartDate” WHERE UserStoriesID = user_usID";

                menu();
        }
     
        //Display developer(s) and/or Sprint(s)
        public static void displayDevsSprints()
        {
            System.out.println("Please enter 1 if you would like to list the developers, "
            + "2 if you would like to list the sprints");
            
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
                        String employeeID = result.getInt(1);
                        String employeeFN = result.getString(2);
                        String employeeLN = result.getString(3);
                        System.out.format("%n%-25s%-25s%-25s%", employeeID, employeeFN, employeeLN);
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
                        System.out.format("%n%-25s%-25s%-25s%-25s%-25s%", meeting, projectName, startDate, teamName, endDate);
                }
            }

            menu();
        }
      
        //List developers that are part of a Sprint
        public static void listDevs()
        {

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
                    "\n4 Delete a member");
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


                menu();
        }

        //Verifies that a project exists by projectID
        public static boolean verifyEmpID(int user_employeeID)
        {
                String sql = "SELECT * FROM EMPLOYEES WHERE employeeID ='" + user_employeeID + "'";
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
                menu();
                return false;
        }

        public static void createMember()
        {
                System.out.println("Please enter the employee ID for the new employee");
                int user_employeeID = scan.nextInt();
                while(verifyEmpID(user_employeeID))
                {
                        System.out.println("This ID is already taken, please choose another");
                        user_employeeID = scan.nextInt();
                }

                System.out.println("Please enter the employees first name: ");
                String user_firstName = scan.nextLine();
                System.out.println("Please enter the employees last name: ");
                String user_lastName = scan.nextLine();

                String sql = "INSERT into Employees(employeeID, firstName, lastName) VALUES (" + user_employeeID + ", " + user_firstName + ", " + user_lastName + ")";

                //INSERT into TeamRoles(employeeID, teamName, teamRole) VALUES((SELECT employeeID FROM Employees WHERE employeeID = user_id), (SELECT teamName FROM ScrumTeams WHERE teamName = user_teamName), user_teamRole)
                
                menu();
        }
     
        //CRUD operations for user stories to project/product backlog
        public static void storiesCRUD()
        {
                boolean choose = true;
                while(choose)
                {
                    System.out.println("Please choose an operation:");
                    System.out.println("1. Create a story\n2. View all stories\n3. Update a story" +
                    "\n4 Delete a member");
                    //CRUD - Create Read Update Delete
                    try
                    {
                        int choice = scan.nextInt();
                        choose = false;
                        switch(choice){
                            case 1: createStory();
                                    break;
                            case 2: readStory();
                                    break;
                            case 3: updateStory();
                                    break;
                            case 4: deleteStory();
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

                menu();
        }
     }
     
     
     
     
     
     