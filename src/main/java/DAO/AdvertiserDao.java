package DAO;

import DomainModel.Advertiser;

import java.sql.SQLException;

public interface AdvertiserDao extends DAO <Advertiser, Integer> {

    void updateAdvertiser(Advertiser idAdvertiser, int newBalance);

    public Integer getNextAdvertiserId() throws SQLException;
}