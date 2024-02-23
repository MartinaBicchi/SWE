package BusinessLogic;


import DAO.AdvertiserDao;
import DomainModel.Advertiser;
import DomainModel.Agency;

import java.util.ArrayList;
import java.util.List;

public class AgencyController extends AdvertiserController<Agency> {
    private AdvertiserDao dao;

    public AgencyController(AdvertiserDao dao) {
        super(dao);
        this.dao = dao;
    }

    public void addAgency(int id, String name, int agencyCosts) {
        Agency newAgency = new Agency(id, 0, name, agencyCosts);
        try {
            dao.insert(newAgency);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Agency getAgency(int id) {
        try {
            return (Agency) dao.get(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Agency[] getAllAgencies() {
        List<Advertiser> advertisers=new ArrayList<>();
        try {
            advertisers = dao.getAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<Agency> agencies = new ArrayList<>();

        for (Advertiser advertiser : advertisers) {
            if (advertiser instanceof Agency) {
                agencies.add((Agency) advertiser);
            }
        }

        return agencies.toArray(new Agency[0]);
    }
}