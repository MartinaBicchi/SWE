package BusinessLogic;


import DAO.AdvertiserDao;
import DomainModel.Advertiser;

public abstract class AdvertiserController<T extends Advertiser> {
    private AdvertiserDao dao;

    public AdvertiserController(AdvertiserDao dao) {
        this.dao = dao;
    }

    public void removeAdvertiser(int id) throws Exception {
        // Remove the advertiser with the given ID from the database
        dao.remove(id);
    }
}