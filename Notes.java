package Notes;

public class Notes implements Cloneable {
    private long notes;
    private long count;
    protected  Notes(long notes,long count){
        this.notes=notes;
        this.count=count;
    }
    public long getNotes() {
        return notes;
    }
    public long getCount() {
        return count;
    }
    public void setCount(long count) {
        this.count = count;
    }
    public Notes clone() throws CloneNotSupportedException {
        return (Notes) super.clone();
    }
}
