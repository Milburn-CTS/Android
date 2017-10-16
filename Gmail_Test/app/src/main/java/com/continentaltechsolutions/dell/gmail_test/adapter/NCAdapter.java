package com.continentaltechsolutions.dell.gmail_test.adapter;

import android.content.Context;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.continentaltechsolutions.dell.gmail_test.R;
import com.continentaltechsolutions.dell.gmail_test.model.NotificationConfig;

import java.util.List;

/**
 * Created by DELL on 15-Oct-17.
 */

public class NCAdapter extends RecyclerView.Adapter<NCAdapter.MyViewHolder>{
    private Context mContext;
    private List<NotificationConfig> notificationConfigList;
    private NCAdapterListner listener;
    private SparseBooleanArray selectedItems;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_config_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NotificationConfig  notificationConfig = notificationConfigList.get(position);
        holder.eventID.setText(String.valueOf(notificationConfig.getEventID()));
        holder.EnN.setText(notificationConfig.getEnabledNotifications());
        holder.DOW.setText(notificationConfig.getDaysOfWeek());
        holder.FromTime.setText(notificationConfig.getFromTime());
        holder.ToTime.setText(notificationConfig.getFromTime());
    }

    @Override
    public int getItemCount() {
        return notificationConfigList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        public TextView eventID, EnN, DOW, iconText, FromTime, ToTime;
        public ImageView iconImp, imgProfile;
        public LinearLayout messageContainer;
        public RelativeLayout iconContainer, iconBack, iconFront;

        public MyViewHolder(View itemView) {
            super(itemView);
            eventID = (TextView) itemView.findViewById(R.id.tvEventID);
            EnN =(TextView) itemView.findViewById(R.id.tvEnabledNotifications);
            DOW = (TextView) itemView.findViewById(R.id.tvDaysOfWeek);
            iconText = (TextView) itemView.findViewById(R.id.icon_text);
            FromTime = (TextView) itemView.findViewById(R.id.tvFromTime);
            ToTime = (TextView) itemView.findViewById(R.id.tvToTime);

            iconBack = (RelativeLayout) itemView.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) itemView.findViewById(R.id.icon_front);
            iconImp = (ImageView) itemView.findViewById(R.id.icon_star);
            imgProfile = (ImageView) itemView.findViewById(R.id.icon_profile);
            messageContainer = (LinearLayout) itemView.findViewById(R.id.message_container);
            iconContainer = (RelativeLayout) itemView.findViewById(R.id.icon_container);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    public NCAdapter(Context mContext, List<NotificationConfig> messages, NCAdapterListner listener){
        this.mContext = mContext;
        this.notificationConfigList = messages;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    public interface NCAdapterListner{
        void onIconClicked(int positon);
        void onIconImportantClicked(int position);
        void onMessageRowClicked(int position);
        void onRowLongClicked(int position);
    }
}
