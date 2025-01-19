import Notes.Notes;
import java.util.ArrayList;
import java.util.Scanner;
public class UserAction {

    public static Account userlogin(Scanner sc) {  //method for user login , returns user object
        int attempt = 0;
        while (attempt <= 3) {  //While loop to stop the user after 3 attempts
            System.out.println("Enter your User id ");
            String ids = sc.nextLine(); //gets the user id from the user
            System.out.println("Enter your password");
            String password = sc.nextLine(); //gets the password from the user
            User currentUser = null; //Initialing assigning currentuser to null
            for (Account user : ATM.getAccounts()) {//Enhanced for loop to verify the users presence in the arraylist
                if (user instanceof User ) {
                    if (user.getId().equals(ids) && user.getPass().equals(password)) { //checks whether the user id given by the user is present in the arraylist or not
                        currentUser = (User) user;//if present,the user id is saved in the currentuser
                        break;
                    }
                }
            }
            if (currentUser != null) {
                System.out.println("User Login Successfully");
                return currentUser;  //returns the object
            } else {
                System.out.println("Invalid userid or password. Please try again.");
                attempt++; //if the user id or password is not correct then attempt is incremented
            }
        }
        return null; //returns null if the user is not found
    }
    public static void check_balance(User currentUser) { //method for checking the balance of the current user
        System.out.println("Your account balance is: " + currentUser.getBal());
    }

    public static void change_pin(User currentUser,Scanner sc) { //method  to change the current pin of the user
        System.out.println("Enter your current PIN:");
        String currentPin = sc.nextLine();//get the current pin to verify the user
        if (currentPin.equals(currentUser.getPass())) {  //if the current pin is correct ,get the new pin form the user
            System.out.println("Enter new PIN:");
            String newPin =sc.nextLine(); //gets the new pin form the user
            currentUser.setPass(newPin); //sets the new pin for the current user
            System.out.println("PIN changed successfully!");
        } else {
            System.out.println("Incorrect current PIN.");
        }
    }
    public static void deposit_cash(Scanner sc, User currentUser)  {//method for depositing the cash into the atm by the user
        for (Account user : ATM.getAccounts()) {  // iterate through accounts
            if (user.equals(currentUser)) {
                System.out.println("Enter the amount to be deposited");
                long amt = Long.parseLong(sc.nextLine());
                if (amt % 100 == 0) {  // Ensure the deposit amount is in multiples of 100
                    long amt1 = currentUser.getBal() + amt;
                    currentUser.setBal((int) amt1);// Update user's balance
                    double atm1 = ATM.getATMbalance() + amt;  // Update ATM balance
                    ATM.setATMbalance(atm1);
                    System.out.println("Enter the denomination ypu want");
                    System.out.println("2000:");
                    long no2000 = Long.parseLong(sc.nextLine());
                    System.out.println("500:");
                    long no500 = Long.parseLong(sc.nextLine());
                    System.out.println("200:");
                    long no200 = Long.parseLong(sc.nextLine());
                    System.out.println("100:");
                    long no100 = Long.parseLong(sc.nextLine());
                    long total = ((no2000 * 2000) + (no500 * 500) + (no200 * 200) + (no100 * 100));// Calculate total deposit based on denominations
                    if (total == amt) { // Ensure the total matches the deposited amount
                        for (Notes notes : ATM.getNotesArrayList()) {

                            long type = notes.getNotes();
                            switch ((int) type) {  // Update notes count in ATM
                                case 2000:
                                    notes.setCount(notes.getCount() + no2000);
                                    break;
                                case 500:
                                    notes.setCount(notes.getCount() + no500);
                                    break;
                                case 200:
                                    notes.setCount(notes.getCount() + no200);
                                    break;
                                case 100:
                                    notes.setCount(notes.getCount() + no100);
                                    break;
                                default:
                                    System.out.println("Invalid choice");
                            }
                        }
                        System.out.println("Deposited successfully");
                        currentUser.getTransactions().add(new Transaction(currentUser.getId(),amt,"Deposited")); //deposit is added to the arraylist
                        for (Notes note : ATM.getNotesArrayList()) {  // Display updated notes count in ATM
                            System.out.println(note.getNotes() + " " + note.getCount());
                        }

                    }
                }
            }
        }
    }
    public static void withdraw_cash(Scanner sc,User currentUser) throws CloneNotSupportedException { // withdraw method
        System.out.println("Enter your withdraw amount ");
        long amount = Long.parseLong(sc.nextLine());
        ArrayList<String> denomation = new ArrayList<>();// To store the denomination
        ArrayList<Notes> copy_note = new ArrayList<>();  //create new duplicate list to store withdrawal
        if(amount<=currentUser.getBal()) {  //checks for the users balance
            if(amount<=ATM.getATMbalance()) {  //checks for the atm balance
                for (Notes notes : ATM.getNotesArrayList()) { //iterate through the notes
                    copy_note.add(notes.clone());
                }
                long original_amount=amount;  // Store the original withdrawal amount
                while (amount != 0) {  // Loop until the amount is reduced to zero
                    for (Notes notes : copy_note) { //iterate through the copy notes
                        long not_e = notes.getNotes();
                        switch ((int) not_e) {
                            case 2000, 500, 200, 100:
                                amount = (int) perform_Withdrawal(amount, denomation, notes);   // Perform withdrawal and update the remaining amount
                                break;
                        }
                    }
                    if (amount == 0) {  // If the amount becomes zero, complete the withdrawal
                        ATM.setNotesArrayList(copy_note);//  copy note will set to an note
                        currentUser.setBal(currentUser.getBal() - original_amount);  // Deduct the withdrawn amount from user's balance
                        ATM.setATMbalance(ATM.getATMbalance() - amount);  // Deduct the withdrawn amount from the ATM balance
                        for (String notes : denomation) { //iterates through denomination
                            System.out.println(" * " + notes);
                        }
                        currentUser.getTransactions().add(new Transaction(currentUser.getId(), original_amount, "withdraw"));// added to transation
                        break;
                    } else {
                        System.out.println("There is no denomination in ATM mechine ! Reenter the amount");
                        break;
                    }
                }
                for (Notes notes : ATM.getNotesArrayList()) {   // Display the updated count of notes in the ATM
                    System.out.println(notes.getNotes() + " " + notes.getCount());
                }
            }
        }
        else{
            System.out.println("No balance");
        }
    }
    public static double perform_Withdrawal(double Amount, ArrayList<String> denomation, Notes note) { //perform withdrawal method
        int count = (int) (Amount / note.getNotes());//withdraw amount will div to notes and stored to count
        if (note.getNotes() <= Amount && count <= note.getCount()) {
            Amount = Amount - (count * note.getNotes());  // Reduce the withdrawal amount by the value of used notes
            note.setCount(note.getCount() - count);  // Update the count of notes in the ATM
            denomation.add(note.getNotes() + " " + count);
            return Amount; // Return the remaining withdrawal amount

        }
        return Amount;// Return the unchanged amount if denomination can't be used
    }
    public static void transaction_history(User currentUser) {
        System.out.println("Transaction History for User ID: " + currentUser.getId()); // Display the logged-in user's ID
        ArrayList<Transaction> userTransactions = new ArrayList<>(); // List to store the user's transactions
        for (Transaction transaction : currentUser.getTransactions()) {
            if (transaction.getPerformedby().equals(currentUser.getId())) {   // Check if the transaction was performed by the current user
                userTransactions.add(transaction); // Add user transactions to the list
            }
        }
        if (userTransactions.isEmpty()) {  // Check if no transactions are found
            System.out.println("No transactions available for User ID: " + currentUser.getId());
        } else {
            for (Transaction transaction : userTransactions) {  // Display the user's transaction details
                System.out.println("Performed By: " + transaction.getPerformedby() +
                        ", Amount: " + transaction.getAmount() +
                        ", Type: " + transaction.getType());
            }
        }
    }
}
