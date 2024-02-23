import BusinessLogic.*;
import DAO.*;
import DomainModel.Activity;
import DomainModel.Advertiser;
import DomainModel.Client;
import DomainModel.search.DecoratorSearchAddress;
import DomainModel.search.DecoratorSearchCity;
import DomainModel.search.DecoratorSearchPrice;
import DomainModel.search.SearchConcrete;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws Exception {

        Database.setDatabase("main.db");
        Database.initDatabase();

       //creo i database
        ActivityDao activityDao = new SQLiteActivityDao();
        AdvertiserDao advertiserDao = new SQLiteAdvertiserDao();
        BookingDao bookingDao = new SQLiteBookingDao();
        ClientDao clientDao = new SQLiteClientDao();
        FavoriteDao favoriteDao = new SQLiteFavoriteDao();

        //creo i Controller
        ActivityController activityController = new ActivityController(activityDao);
        AgencyController agencyController = new AgencyController(advertiserDao);
        PrivateController privateController = new PrivateController(advertiserDao);
        BookingController bookingController = new BookingController(bookingDao);
        ClientController clientController = new ClientController(clientDao, activityController);

        // Client di prova
        clientController.addClient("Gianni", "Rossi", "RSSMRA00A00A000A");
        clientController.addClient("Mario", "Verdi", "VRDLGI00A00A000A");
        clientController.addClient("Alessandro", "Neri", "GVNNBN00A00A000A");

        //advertiser di prova
        agencyController.addAgency(1, "Get Your Guide", 100);
        privateController.addPrivate(2, "Stefano", "Tani");
        privateController.addPrivate(3, "Giovanni", "Gini");
        privateController.addPrivate(4, "Giovanni", "Bicchi");
        privateController.addPrivate(5, "Aldo", "Miglio");
        agencyController.addAgency(6, "Attivita for Life", 500);

        //creazione attività
        activityController.createActivity("Giro in mongolfiera", "Giro sopra la citta di Firenze in mongolfiera", "adrenalinico",1, "Via Gianni 3", "Firenze", 1, 100 );
        activityController.createActivity("Corso di cucina", "Corso di cucina tipica toscana", "cucina",3, "Viale della libertà", "Firenze", 2, 50 );
        activityController.createActivity("Cucito", "Corso per imparare le basi del cucito", "svago",5, "Via 20 settembre 25", "Bologna", 1, 20 );
        activityController.createActivity("Corso di pasticceria", "Corso approfondito di pasticceria", "cucina",6, "Via Duca 9", "Torino", 3, 250 );
        activityController.createActivity("Giro in super car", "Giro in supercar all'autodromo del mugello", "adrenalinico",1, "Autodromo del mugello", "Scarperia e San Piero", 4, 350 );
        activityController.createActivity("Guida turistica", "Giro nel centro con guida turistica a Roma", "culturale",2, "Via Montale 3", "Roma", 1, 100 );
        activityController.createActivity("Ingresso Duomo", "Biglietto ingresso per visita al duomo di Milano", "culturale",1, "Piazza Duomo", "Milano", 2, 200 );
        activityController.createActivity("Corso ceramica", "Corso per imparare le basi della lavorazione della ceramica", "svago",4, "Via della colombella 8", "Firenze", 1, 50 );

        activityController.printAllAds();
        System.out.println();


        //Uso dei decoratori !!ATTENZIONE!!
        System.out.println("Uso dei Decoratori:");
       Activity[] ac=new DecoratorSearchPrice(new DecoratorSearchCity(new DecoratorSearchAddress(new SearchConcrete("cucina"), "Via Duca 9"), "Torino"), 300).searchActivity();
        for(Activity acs : ac){
            System.out.println(acs);
        }

        System.out.println();

        Advertiser[] allAdvertisers = advertiserDao.getAll().toArray(new Advertiser[0]);
        for (Advertiser advertiser : allAdvertisers) {
            advertiser.displayInformation();
            System.out.println("\n");
        }

        agencyController.removeAdvertiser(3);
        privateController.removeAdvertiser(4);
        privateController.removeAdvertiser(5);

        System.out.println("Dopo l'eliminazione di alcuni advertiser");
        allAdvertisers = advertiserDao.getAll().toArray(new Advertiser[0]);
        for (Advertiser advertiser : allAdvertisers) {
            advertiser.displayInformation();
            System.out.println("\n");
        }


        activityController.addToFavourites("RSSMRA00A00A000A", 1);
        activityController.addToFavourites("RSSMRA00A00A000A", 7);
        activityController.addToFavourites("VRDLGI00A00A000A", 6);
        activityController.addToFavourites("VRDLGI00A00A000A", 3);
        activityController.addToFavourites("GVNNBN00A00A000A", 2);
        activityController.addToFavourites("GVNNBN00A00A000A", 4);
        activityController.addToFavourites("GVNNBN00A00A000A", 6);

        System.out.println();

        //prova corretto funzionamento Favourite
        Activity[] activities=activityController.getFavouriteAds("RSSMRA00A00A000A");
        System.out.println("Le attività preferite di RSSMRA00A00A000A");
        for(Activity a:activities){
            System.out.println(a);
        }

        //prova funzionamento observer
        System.out.println();
        activityController.getFavouriteAds("RSSMRA00A00A000A");
        activityController.deleteActivity(1);
        System.out.println();
        activities=activityController.getFavouriteAds("RSSMRA00A00A000A");
        System.out.println("Le attività preferite di RSSMRA00A00A000A dopo aver eliminato l'attività con id=1: ");
        for(Activity a:activities){
            System.out.println(a);
        }


    }
}