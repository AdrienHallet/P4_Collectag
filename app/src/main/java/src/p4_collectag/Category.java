package src.p4_collectag;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class Category implements Parent<BaseItem> {
    private final String title;
    private final List<BaseItem> items;

    public Category(String title, List<BaseItem> items) {
        this.title = title;
        this.items = items;
    }

    @Override
    public List<BaseItem> getChildList() {
        return this.items;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public String getTitle() {
        return title;
    }
}
