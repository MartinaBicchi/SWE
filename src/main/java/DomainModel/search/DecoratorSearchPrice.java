package DomainModel.search;


import DomainModel.Activity;

import java.util.ArrayList;
import java.util.List;

public class DecoratorSearchPrice extends BaseDecoratorSearch {
    private final int maxPrice;

    public DecoratorSearchPrice(Search search, int maxPrice) {
        super(search);
        this.maxPrice = maxPrice;
    }

    public Activity[] searchActivity() {
        Activity[] baseQuery = search.searchActivity();
        List<Activity> filteredActivites = new ArrayList<>();

        for (Activity activity : baseQuery) {
            if (activity.getPrice() <= maxPrice) {
                filteredActivites.add(activity);
            }
        }


        return filteredActivites.toArray(new Activity[0]);
    }
}