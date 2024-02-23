package DomainModel.search;

import DomainModel.Activity;

public abstract class BaseDecoratorSearch implements Search {
    protected Search search;

    public BaseDecoratorSearch(Search search) {
        this.search = search;
    }

    @Override
    public abstract Activity[] searchActivity();
}