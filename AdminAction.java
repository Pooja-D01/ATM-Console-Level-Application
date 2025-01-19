import Notes.Notes;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminAction {

    public static Account adminlogin(Scanner sc) {//method for Admin login
        System.out.println("Enter Admin ID:");
        String ids = sc.nextLine(); //gets the admin id form the user
        System.out.println("Enter Admin Password:");
        String password = sc.nextLine();  //gets the password from the user
        for (Account admin : ATM.getAccounts()) {//For loop for processing all the objects in the arraylist
            if (admin instanceof Admin) {
                if (admin.getId().equals(ids) && admin.getPass().equals(password)) {//checks for the admin id and password
                    System.out.println("Admin Login Successfully");
                    return admin;  //returns admin object
                }
            }
        }
        System.out.println("Wrong Admin id or Password");
        return null;   //if the admin id or password does not match it returns null
    }

    public static void add_user(Scanner sc) {  //This method is used to add the user into the list

        System.out.println("Enter the user name to add:");
        String us_id = sc.nextLine(); //gets the user id as input
        for (Account user: ATM.getAccounts()) {  //iterates through thr accounts
            if (user instanceof User) {  //checks if user instance of user
                if (user.getId().equals(us_id)) {  //checks whether the user already exists or not
                    System.out.println("User id already exist");
                    return;
                }
            }
        }
        System.out.println("Enter the password for the user:");
        String us_pass = sc.nextLine(); //gets the user password as the input
        User addnewuser = new User(us_id, us_pass);
        ATM.getAccounts().add(addnewuser); //adds the user id , user password and balance to the user arraylist
        System.out.println("User added Successfully");
    }

    public static void del_user(Scanner sc) { //This method is used for deleting a user form the list
        System.out.println("Enter the user name to delete:");
        String us_id = sc.nextLine();//gets the user id as the input
        for (Account user : ATM.getAccounts()) { // iterate through accounts
            if (user instanceof User) { // checks whether user instance of user
                if (user.getId().equals(us_id)) {
                    ATM.getAccounts().remove(user);//user id is deleted from the arraylist
                    System.out.println("User id removed successfully ");
                    return;
                }
            }
        }
        System.out.println("User id doesn't exist");
    }

    public static void deposit(Scanner sc, Admin admin) {

        System.out.println("Enter the amount to deposit to the ATM:");
        int depositAmount = Integer.parseInt(sc.nextLine()); // Gets the deposit amount from the user
        System.out.print("Enter the no. of 2000rs notes: ");
        int notes2000Count = Integer.parseInt(sc.nextLine()); // Gets the 2000rs note count
        System.out.print("Enter no. of 500rs notes: ");
        int notes500Count = Integer.parseInt(sc.nextLine()); // Gets the 500rs note count
        System.out.print("Enter no. of 200rs notes: ");
        int notes200Count = Integer.parseInt(sc.nextLine()); // Gets the 200rs note count
        System.out.print("Enter no. of 100rs notes: ");
        int notes100Count = Integer.parseInt(sc.nextLine()); // Gets the 100rs note count
        double totalDepositAmount = (notes2000Count * 2000) + (notes500Count * 500) +
                (notes200Count * 200) + (notes100Count * 100); // Calculate total deposit
        if (depositAmount == totalDepositAmount) {  //To check whether the amount given by the user and the total amount after denomination are the same
            ArrayList<Notes> updatedNotesList = new ArrayList<>(ATM.getNotesArrayList());
            for (Notes note : updatedNotesList) {
                long type = note.getNotes(); // Get the note type
                switch ((int) type) {
                    case 2000:
                        note.setCount(note.getCount() + notes2000Count);//updates the note count of 2000
                        break;
                    case 500:
                        note.setCount(note.getCount() + notes500Count);//updates the note count of 500
                        break;
                    case 200:
                        note.setCount(note.getCount() + notes200Count);//updates the note count of 200
                        break;
                    case 100:
                        note.setCount(note.getCount() + notes100Count);//updates the note count of 100
                        break;
                    default:
                        System.out.println("Wrong Denomination");
                }
            }
            double currentATMBalance = ATM.getATMbalance();
            double updatedBalance = currentATMBalance + depositAmount;//updates the atm balance
            ATM.setATMbalance(updatedBalance);

            System.out.println("Successfully deposited " + depositAmount);
            System.out.println("Updated ATM balance: " + updatedBalance);
            for (Notes notes : ATM.getNotesArrayList()) { //iterates through the notes arraylist
                System.out.println(notes.getNotes() + " " + notes.getCount());
            }
            admin.getTransactions().add(new Transaction(admin.getId(), depositAmount, "Deposit"));  //admin transaction is added to the admin arraylist
        } else {
            System.out.println("Invalid amount entered. Please enter a positive amount.");
        }
    }


    public static void add_admin(Scanner sc) {
        System.out.println("Enter the Admin id to add");
        String ad_id = sc.nextLine();     //gets the new admin id
        System.out.println("Enter the Admin Password");
        String ad_pass = sc.nextLine();   //gets the new password
        ATM.getAccounts().add(new Admin(ad_id, ad_pass)); //adds the new id and password to the arraylist
        System.out.println("Admin added successfully");
    }

    public static void viewTransaction(Account admin, Scanner sc) {
        System.out.println("Enter your choice \n1.Admin transaction\n2.User transaction\n3.for all");
        int choice = Integer.parseInt(sc.nextLine());
        switch (choice) {
            case 1:
                if(admin.getTransactions().isEmpty()){  //checks if the transaction is empty or not
                    System.out.println("No transaction found for the admin");
                }
                else {
                    for (Transaction adminTransaction : admin.getTransactions()) {   //iterates through the arraylist
                        System.out.println(adminTransaction.getPerformedby() + " has " + adminTransaction.getType() + " Rs." + adminTransaction.getAmount());//prints the admin transactions
                    }
                }
                break;

            case 2:
                System.out.println("Available users:");
                boolean userFound = false;
                for (Account user : ATM.getAccounts()) {//iterates through accounts
                    if (user instanceof User) {  //checks whether user instance on user
                        System.out.println("- " + user.getId());
                        userFound = true;
                    }
                }
                if (!userFound) { //if user does not exist the statement is displayed
                    System.out.println("No users found.");
                    break;
                }
                System.out.println("Enter the user ID:");
                String userId = sc.nextLine();
                boolean transactionFound = false;
                for (Account user : ATM.getAccounts()) { //iterates through accounts
                    if (user instanceof User && user.getId().equals(userId)) {  //checks if the user instance of user and the user id
                        if (user.getTransactions().isEmpty()) { //checks whether the transaction is empty or not
                            System.out.println("No transactions found for user: " + userId);
                        } else {
                            for (Transaction userTransaction : user.getTransactions()) {  //iterates through transaction
                                System.out.println(userTransaction.getPerformedby() + " has " + userTransaction.getType() + " Rs." + userTransaction.getAmount());
                            }
                        }
                        transactionFound = true;
                        break;
                    }
                }
                if (!transactionFound) {
                    System.out.println("User not found.");
                }
                break;
            case 3:
                System.out.println("Admin Transactions:");
                for (Transaction adminTransaction : admin.getTransactions()) { //iterates through transaction
                    System.out.println(adminTransaction.getPerformedby() + " has " + adminTransaction.getType() + " Rs." + adminTransaction.getAmount()); //prints the admin transaction
                }
                System.out.println("User Transactions:");
                for (Account user : ATM.getAccounts()) { //iterates through accounts
                    if (user instanceof User) { //checks whether user instance of user
                        for (Transaction userTransaction : user.getTransactions()) { //iterates through transaction
                            if (userTransaction != null) {  //prints the user transaction if it is not equal to null
                                System.out.println(userTransaction.getPerformedby() + " has " + userTransaction.getType() + " Rs." + userTransaction.getAmount());
                            }
                        }
                    }
                }
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }
}
