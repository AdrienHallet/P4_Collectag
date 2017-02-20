package src.p4_collectag;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Category extends ExpandableGroup<BaseItem>{

    public Category(String title, List<BaseItem> items) {
        super(title, items);
    }
}
