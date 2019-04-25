import java.util.Scanner;

public class Software{
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
        }
    }

    //Connect to the database
    public static void connect()
    {

    }

    //Create a sprint in the database
    public static void createSprint()
    {

    }

    //Create a project in the database
    public static void createProject()
    {

    }

    //Add user stories to sprint backlog
    public static void addStories()
    {

    }

    //Display developer(s) and/or Sprint(s)
    public static void displayDevsSprints()
    {

    }
    
    //List developers that are part of a Sprint
    public static void listDevs()
    {

    }

    //CRUD operations for management members and sprint team members
    public static void membersCRUD()
    {

    }

    //CRUD operations for user stories to project/product backlog
    public static void storiesCRUD()
    {

    }


}