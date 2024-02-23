package DomainModel;

public abstract class Advertiser {
    private final int id;
    private int balance;


    public Advertiser(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public abstract void displayInformation();
}