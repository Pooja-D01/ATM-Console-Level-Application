public class User extends Account {
    long bal = 0;

    public User(String user_id, String user_pass) {
        super(user_id, user_pass);
    }
    public long getBal() {
        return bal;
    }

    public void setBal(long bal) {
        this.bal = bal;
    }
}