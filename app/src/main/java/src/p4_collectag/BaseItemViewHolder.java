package src.p4_collectag;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * @author Julien
 */
class BaseItemViewHolder extends ChildViewHolder {
    TextView mTextView;
    ImageView mImageView;
    FrameLayout mLayout;
    private MainActivity mActivity;

    BaseItemViewHolder(View viewIn, MainActivity activity) {
        super(viewIn);
        mActivity = activity;
        mTextView = (TextView) viewIn.findViewById(R.id.item_name);
        mImageView = (ImageView) viewIn.findViewById(R.id.item_image);
        mLayout = (FrameLayout) viewIn.findViewById(R.id.list_item_base);
        viewIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BaseItemViewHolder.this.handleClick();
            }
        });
        viewIn.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                BaseItemViewHolder.this.handleLongClick();
                return true;
            }
        });
    }

    private void handleClick() {
        int flatPosition = getLayoutPosition();
        if (mActivity.isMultiSelect) {
            mActivity.mAdapter.toggleSelection(flatPosition);
        } else {
            mActivity.mAdapter.onItemClick(flatPosition);
        }
    }

    private void handleLongClick() {
        mActivity.mAdapter.mActivity.enableSelectionMode();
    }

}
