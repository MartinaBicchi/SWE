package DomainModel;

public class Client{
    private final String fiscalCode;
    private String name;
    private String lastName;


    public Client(String fiscalCode, String name, String lastName) {
        this.fiscalCode = fiscalCode;
        this.name = name;
        this.lastName = lastName;
    }

    public String getFiscalCode() {
        return fiscalCode;
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
    public String toString() {
        return "Client{" +
                "fiscalCode='" + fiscalCode + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }


}


