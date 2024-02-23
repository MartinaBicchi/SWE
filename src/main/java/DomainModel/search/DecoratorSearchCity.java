package DomainModel.search;


import DomainModel.Activity;

import java.util.ArrayList;
import java.util.List;

public class DecoratorSearchCity extends BaseDecoratorSearch {
    private final String city;

    public DecoratorSearchCity(Search search, String city) {
        super(search);
        this.city = city;
    }

    public Activity[] searchActivity() {
        Activity[] baseQuery = search.searchActivity();
        List<Activity> filteredActivites = new ArrayList<>();

        for (Activity activity : baseQuery) {
            if (activity.getCity().equals(city)) {
                filteredActivites.add(activity);
            }
        }


        return filteredActivites.toArray(new Activity[0]);
    }
}