package src.p4_collectag;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;


/**
 * @author Julien
 */
class BaseItemViewHolder extends ChildViewHolder<BaseItem> {
    private static final int DEFAULT_ITEM_ICON = R.drawable.ic_menu_slideshow;
    private TextView mTextView;
    private ImageView mImageView;
    private FrameLayout mLayout;
    private MainActivity mActivity;

    BaseItemViewHolder(View viewIn, MainActivity activity) {
        super(viewIn);
        mActivity = activity;
        mTextView = (TextView) viewIn.findViewById(R.id.item_name);
        mImageView = (ImageView) viewIn.findViewById(R.id.item_image);
        mLayout = (FrameLayout) viewIn.findViewById(R.id.list_item_base);
        viewIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mActivity.isMultiSelect) {
                    if (mActivity.selectedItems.contains(getChild())) {
                        mActivity.selectedItems.remove(getChild());
                    } else {
                        mActivity.selectedItems.add(getChild());
                    }
                    mActivity.notifySelectionChanged();
                } else {
                    Toast.makeText(mActivity, "Data : " + getChild().getDisplayText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewIn.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                mActivity.enableSelectionMode();
                mActivity.notifySelectionChanged();
                return true;
            }
        });
    }

    void bind(@NonNull BaseItem child) {
        mTextView.setText(child.getDisplayText());
        if (child.getDisplayImage() != null) {
            mImageView.setImageBitmap(child.getDisplayImage());
        } else {
            mImageView.setImageResource(DEFAULT_ITEM_ICON);
        }
        if (mActivity.selectedItems.contains(child))
            mLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.list_item_selected_state));
        else
            mLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.list_item_normal_state));
    }
}
