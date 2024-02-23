package BusinessLogic;


import DAO.AdvertiserDao;
import DomainModel.Advertiser;
import DomainModel.Private;

import java.util.ArrayList;
import java.util.List;

public class PrivateController extends AdvertiserController<Private> {
    private AdvertiserDao dao;

    public PrivateController(AdvertiserDao dao) {
        super(dao);
        this.dao = dao;
    }

    public void addPrivate(int id, String name, String lastName) {
        Private newprivate = new Private(id, 0, name, lastName);
        try {
            dao.insert(newprivate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Private getPrivate(int id) {
        try {
            return (Private) dao.get(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Private[] getAllPrivate() {
        List<Advertiser> advertisers=new ArrayList<>();
        try {
            advertisers = dao.getAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<Private> privates = new ArrayList<>();

        for (Advertiser advertiser : advertisers) {
            if (advertiser instanceof Private) {
                privates.add((Private) advertiser);
            }
        }

        return privates.toArray(new Private[0]);
    }
}