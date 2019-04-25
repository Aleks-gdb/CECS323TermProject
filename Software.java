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
                    case 1: break;
                    case 2: break;
                    case 3: break;
                    case 4: break;
                    case 5: break;
                    case 6: break;
                    case 7: break;
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

}