package DomainModel;

public class Private extends Advertiser {
    private String name;
    private String lastName;

    public Private(int id, int balance, String name, String lastName) {
        super(id, balance);
        this.name = name;
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public void displayInformation() {
        System.out.println("Private Information:");
        System.out.println("ID: " + getId());
        System.out.println("Balance: " + getBalance());
        System.out.println("Name: " + name);
        System.out.println("Last Name: " + lastName);
    }
}
