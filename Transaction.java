
public class Transaction extends  Account{
    String performedby;
    long amount;
    String type;
    public Transaction(String performedby,long amount,String type){ //constructor for initializing the variables
        this.performedby=performedby;
        this.amount=amount;
        this.type=type;
    }
    public String getPerformedby(){
        return performedby;
    } //getter for performed by
    public long getAmount(){
        return amount;
    }  //getter for amount
    public String getType(){
        return type;
    }  //getter for type
}


