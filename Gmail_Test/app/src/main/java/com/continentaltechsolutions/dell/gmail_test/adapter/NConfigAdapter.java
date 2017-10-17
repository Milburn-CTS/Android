package com.continentaltechsolutions.dell.gmail_test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.continentaltechsolutions.dell.gmail_test.R;
import com.continentaltechsolutions.dell.gmail_test.helper.CircleTransform;
import com.continentaltechsolutions.dell.gmail_test.helper.FlipAnimator;
import com.continentaltechsolutions.dell.gmail_test.model.NotificationConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 15-Oct-17.
 */

public class NConfigAdapter extends RecyclerView.Adapter<NConfigAdapter.MyViewHolder1> {
    private Context mContext;
    private List<NotificationConfig> notificationConfigList;
    private NotificationConfigAdapterListener listener;
    private SparseBooleanArray selectedItems;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    public class MyViewHolder1 extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView tvEventID, tvEnabledNotifications, tvDaysOfWeek, iconText, tvFromTime, tvToTime;
        public ImageView iconImp, imgProfile;
        public LinearLayout messageContainer;
        public RelativeLayout iconContainer, iconBack, iconFront;

        public MyViewHolder1(View view1) {
            super(view1);

            tvEventID = (TextView) view1.findViewById(R.id.tvEventID);
            tvEnabledNotifications = (TextView) view1.findViewById(R.id.tvEnabledNotifications);
            tvDaysOfWeek = (TextView) view1.findViewById(R.id.tvDaysOfWeek);
            tvFromTime = (TextView) view1.findViewById(R.id.tvFromTime);
            tvToTime = (TextView) view1.findViewById(R.id.tvToTime);

            iconText = (TextView) view1.findViewById(R.id.icon_text);
            iconBack = (RelativeLayout) view1.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) view1.findViewById(R.id.icon_front);
            //iconImp = (ImageView) view1.findViewById(R.id.icon_star);
            imgProfile = (ImageView) view1.findViewById(R.id.icon_profile);
            messageContainer = (LinearLayout) view1.findViewById(R.id.message_container1);
            iconContainer = (RelativeLayout) view1.findViewById(R.id.icon_container1);
            view1.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }


    public NConfigAdapter(Context mContext, List<NotificationConfig> messages, NotificationConfigAdapterListener listener) {
        this.mContext = mContext;
        this.notificationConfigList = messages;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    @Override
    public MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_config_row, parent, false);

        return new MyViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder1 holder, int position) {
        NotificationConfig message = notificationConfigList.get(position);

        // displaying text view data
        holder.tvEventID.setText("Event ID: " + String.valueOf(message.getEventID()));
        holder.tvEnabledNotifications.setText("Notify: " + message.getEnabledNotifications());
        holder.tvDaysOfWeek.setText("DOW: " + message.getDaysOfWeek());
        holder.tvFromTime.setText("From: " + message.getFromTime());
        holder.tvToTime.setText("To: " + message.getToTime());

        // displaying the first letter of From in icon text
        holder.iconText.setText(message.getFrom().substring(0, 1));

        // change the row state to activated
        holder.itemView.setActivated(selectedItems.get(position, false));

        // change the font style depending on message read status
        applyReadStatus(holder, message);

        // handle message star
        applyImportant(holder, message);

        // handle icon animation
        applyIconAnimation(holder, position);

        // display profile image
        applyProfilePicture(holder, message);

        // apply click events
        applyClickEvents(holder, position);
    }

    private void applyClickEvents(MyViewHolder1 holder, final int position) {
        holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconClicked(position);
            }
        });

       /* holder.iconImp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconImportantClicked(position);
            }
        });*/

        holder.messageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMessageRowClicked(position);
            }
        });

        holder.messageContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }

    private void applyProfilePicture(NConfigAdapter.MyViewHolder1 holder, NotificationConfig message) {
        /*if (!TextUtils.isEmpty(message.getPicture())) {
            Glide.with(mContext).load(message.getPicture())
                    .thumbnail(0.5f)
                    .crossFade()
                    .transform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgProfile);
            holder.imgProfile.setColorFilter(null);
            holder.iconText.setVisibility(View.GONE);
        } else {
            holder.imgProfile.setImageResource(R.drawable.bg_circle);
            holder.imgProfile.setColorFilter(message.getColor());
            holder.iconText.setVisibility(View.VISIBLE);
        }*/
        /*Glide.with(mContext).load(message.get())
                .thumbnail(0.5f)
                .crossFade()
                .transform(new CircleTransform(mContext))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgProfile);
        holder.imgProfile.setColorFilter(null);
        holder.iconText.setVisibility(View.GONE);*/

        holder.imgProfile.setImageResource(R.drawable.bg_circle);
        holder.imgProfile.setColorFilter(message.getColor());
        holder.iconText.setVisibility(View.VISIBLE);
    }

    private void applyIconAnimation(NConfigAdapter.MyViewHolder1 holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
    }


    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    @Override
    public long getItemId(int position) {
        return notificationConfigList.get(position).getEventID(); //Wrong
    }

    private void applyImportant(NConfigAdapter.MyViewHolder1 holder, NotificationConfig message) {
       /* if (message.isImportant()) {
            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_black_24dp));
            holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_selected));
        } else {
            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_border_black_24dp));
            holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_normal));
        }*/
    }

    private void applyReadStatus(NConfigAdapter.MyViewHolder1 holder, NotificationConfig message) {
       /* if (message.isRead()) {
            holder.from.setTypeface(null, Typeface.NORMAL);
            holder.subject.setTypeface(null, Typeface.NORMAL);
            holder.from.setTextColor(ContextCompat.getColor(mContext, R.color.subject));
            holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.message));
        } else {
            holder.from.setTypeface(null, Typeface.BOLD);
            holder.subject.setTypeface(null, Typeface.BOLD);
            holder.from.setTextColor(ContextCompat.getColor(mContext, R.color.from));
            holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.subject));
        }*/
    }

    @Override
    public int getItemCount() {
        return notificationConfigList.size();
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        notificationConfigList.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public interface NotificationConfigAdapterListener {
        void onIconClicked(int position);

        void onIconImportantClicked(int position);

        void onMessageRowClicked(int position);

        void onRowLongClicked(int position);
    }

}

