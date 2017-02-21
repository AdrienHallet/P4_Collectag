package src.p4_collectag;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;

import java.util.List;

/**
 * Represent the displayed list.
 *
 * @author Julien Amalaberque
 */
public class CollectionAdapter extends ExpandableRecyclerAdapter<Category, BaseItem, CategoryViewHolder, BaseItemViewHolder> {
    private final List<Category> selectedCategories;
    private final List<BaseItem> selectedItems;
    public MainActivity mActivity;

    CollectionAdapter(MainActivity contextIn, List<Category> groups, List<Category> selectedCategories, List<BaseItem> selectedItems) {
        super(groups);
        mActivity = contextIn;
        this.selectedCategories = selectedCategories;
        this.selectedItems = selectedItems;
    }

    @Override
    public CategoryViewHolder onCreateParentViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category, parent, false);
        return new CategoryViewHolder(view, mActivity);
    }

    @Override
    public BaseItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_base, parent, false);
        return new BaseItemViewHolder(view, mActivity);
    }

    @Override
    public void onBindChildViewHolder(@NonNull BaseItemViewHolder holder, int parentPosition, int childPosition, @NonNull BaseItem child) {
        holder.bind(child);
    }

    @Override
    public void onBindParentViewHolder(@NonNull CategoryViewHolder holder, int parentPosition, @NonNull Category group) {
        holder.bind(group);
    }
}
