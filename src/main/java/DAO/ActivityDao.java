package DAO;

import DomainModel.Activity;

import java.sql.SQLException;

public interface ActivityDao extends DAO <Activity, Integer> {

    public void updateActivity(Activity activity, int newPrice) throws Exception;

    Activity[] getActivitybyAdvertiser(int idAdvertiser) throws Exception;

    public Integer getNextActivityId() throws SQLException;


}