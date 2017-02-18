package src.p4_collectag;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

/**
 * @author Julien Amalaberque
 */
class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {
    private final List<ListItem> displayedList;
    private final Set<ListItem> selectedItems;
    private Context context;

    CollectionAdapter(Context contextIn, List<ListItem> displayedListIn, Set<ListItem> selectedItemsIn) {
        context = contextIn;
        displayedList = displayedListIn;
        selectedItems = selectedItemsIn;
    }

    @Override
    public CollectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // set the data in items
        holder.mTextView.setText(displayedList.get(position).getDisplayText());
        holder.mImageView.setImageResource(displayedList.get(position).getDisplayImage());

        ListItem item = displayedList.get(position);
        if (selectedItems.contains(item))
            holder.mLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_selected_state));
        else
            holder.mLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_normal_state));
    }

    @Override
    public int getItemCount() {
        return displayedList.size();
    }

    /**
     * Provide a reference to the views for each data item
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        LinearLayout mLayout;

        ViewHolder(View viewIn) {
            super(viewIn);
            mTextView = (TextView) viewIn.findViewById(R.id.name);
            mImageView = (ImageView) viewIn.findViewById(R.id.image);
            mLayout = (LinearLayout) viewIn.findViewById(R.id.item_row);
        }
    }
}
