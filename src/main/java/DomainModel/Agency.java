package DomainModel;

public class Agency extends Advertiser {
    private String name;
    private int agencyCosts;

    public Agency(int id, int balance, String name, int agencyCosts) {
        super(id, balance);
        this.name = name;
        this.agencyCosts = agencyCosts;

    }

    public String getName() {
        return name;
    }

    public int getAgencyCosts() {
        return agencyCosts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAgencyCosts(int agencyCosts) {
        this.agencyCosts = agencyCosts;
    }

    @Override
    public void displayInformation() {
        System.out.println("Agency Information:");
        System.out.println("ID: " + getId());
        System.out.println("Balance: " + getBalance());
        System.out.println("Name: " + name);
        System.out.println("Agency Costs: " + agencyCosts);
    }

}

