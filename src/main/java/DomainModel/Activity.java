package DomainModel;

public class Activity { //annuncio
    private final Integer id;
    private String title;
    private String description;
    private String type;
    private final int advertiserId;
    private String address;
    private String city;
    private int duration;
    private int price;

    // Costruttore
    public Activity(int id, String title, String description, String type, int advertiserId, String address, String city, int duration, int price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.city = city;
        this.price = price;
        this.advertiserId = advertiserId;
        this.address=address;
        this.duration=duration;
    }

    // Metodi getter
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getCity() {
        return city;
    }

    public int getPrice() {
        return price;
    }

    public int getAdvertiserId() {
        return advertiserId;
    }

    public String getAddress(){return address;}

    public int getDuration(){return duration;}


    //Metodi setter
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setAddress(String address){this.address=address;}

    public void setDuration(int duration){this.duration=duration;}


    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", price=" + price +
                ", advertiser=" + advertiserId +
                ", duration='" + duration + '\'' +
                '}';
    }


}
