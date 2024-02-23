package DomainModel.search;


import DomainModel.Activity;

import java.util.ArrayList;
import java.util.List;

public class DecoratorSearchAddress extends BaseDecoratorSearch {
    private final String address;

    public DecoratorSearchAddress(Search search, String address) {
        super(search);
        this.address = address;
    }

    public Activity[] searchActivity() {
        Activity[] baseQuery = search.searchActivity();
        List<Activity> filteredActivites = new ArrayList<>();

        for (Activity activity : baseQuery) {
            if (activity.getAddress().equals(address)) {
                filteredActivites.add(activity);
            }
        }


        return filteredActivites.toArray(new Activity[0]);
    }
}