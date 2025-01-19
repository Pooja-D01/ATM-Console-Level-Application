import java.util.ArrayList;

public class Account {
    private String id;
    private String pass;
    public  ArrayList<Transaction> transactions=new ArrayList<>();
    public Account(String id,String pass){
        this.id=id;
        this.pass=pass;
    }
    public Account(){
    }
    public String getId() {
        return id;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public  ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}
