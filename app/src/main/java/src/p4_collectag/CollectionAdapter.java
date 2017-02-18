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

/**
 * @author Julien Amalaberque
 */
class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {
    List<ListItem> displayedList;
    List<ListItem> selectedList;
    private Context context;

    CollectionAdapter(Context contextIn, List<ListItem> displayedListIn, List<ListItem> selectedListIn) {
        context = contextIn;
        displayedList = displayedListIn;
        selectedList = selectedListIn;
    }

    @Override
    public CollectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        //TODO set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // set the data in items
        holder.mTextView.setText(displayedList.get(position).getDisplayText());
        holder.mImageView.setImageResource(displayedList.get(position).getDisplayImage());

        // implement setOnClickListener event on item view.
        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayedList.get(holder.getAdapterPosition()).onClick(view);
            }
        });
        */

        if (selectedList.contains(displayedList.get(position)))
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
