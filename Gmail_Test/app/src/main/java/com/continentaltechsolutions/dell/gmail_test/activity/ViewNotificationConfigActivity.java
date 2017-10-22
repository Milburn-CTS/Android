package com.continentaltechsolutions.dell.gmail_test.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.continentaltechsolutions.dell.gmail_test.R;
import com.continentaltechsolutions.dell.gmail_test.adapter.NConfigAdapter;
import com.continentaltechsolutions.dell.gmail_test.helper.Constants;
import com.continentaltechsolutions.dell.gmail_test.helper.DividerItemDecoration;
import com.continentaltechsolutions.dell.gmail_test.model.NotificationConfig;
import com.continentaltechsolutions.dell.gmail_test.network.ApiClient;
import com.continentaltechsolutions.dell.gmail_test.network.NotificationConfigInterface;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewNotificationConfigActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, NConfigAdapter.NotificationConfigAdapterListener {
    private List<NotificationConfig> notificationConfigList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NConfigAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    private static final String TAG = "ViewNotificationConfigActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_notification_config_view);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new NConfigAdapter(this, notificationConfigList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        actionModeCallback = new ActionModeCallback();

        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getNotificationConfig();
                    }
                }
        );
    }

    /**
     * Fetches mail messages by making HTTP request
     * url: http://api.androidhive.info/json/inbox.json
     */
    private void getNotificationConfig() {
        swipeRefreshLayout.setRefreshing(true);

        NotificationConfigInterface apiService =
                ApiClient.getClient().create(NotificationConfigInterface.class);

        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("DeviceID", String.valueOf(Constants.DEVICE_ID));
        params.put("EventID", String.valueOf(Constants.EVENT_ID));
        Call<List<NotificationConfig>> call = apiService.getNotificationConfig(params, Constants.JWT_TOKEN);
        call.enqueue(new Callback<List<NotificationConfig>>() {
            @Override
            public void onResponse(Call<List<NotificationConfig>> call, Response<List<NotificationConfig>> response) {
                // clear the inbox
                notificationConfigList.clear();

                // add all the messages
                // messages.addAll(response.body());

                // TODO - avoid looping
                // the loop was performed to add colors to each message
                if (response.body() != null) {
                    for (NotificationConfig notificationConfig : response.body()) {
                        // generate a random color
                        notificationConfig.setColor(getRandomMaterialColor("400"));
                        notificationConfigList.add(notificationConfig);
                    }

                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), "Not Found... Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<NotificationConfig>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * chooses a random color from array.xml
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    @Override
    public void onRefresh() {
        getNotificationConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotificationConfig();
    }

    @Override
    public void onIconClicked(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);
    }

    @Override
    public void onIconImportantClicked(int position) {

    }

//    @Override
//    public void onIconImportantClicked(int position) {
//        // Star icon is clicked,
//        // mark the message as important
//        NotificationConfig message = notificationConfigList.get(position);
//        //message.setImportant(!message.isImportant());
//        notificationConfigList.set(position, message);
//        mAdapter.notifyDataSetChanged();
//    }

    @Override
    public void onMessageRowClicked(int position) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
       /* if (mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            // read the message which removes bold from the row
            NotificationConfig message = notificationConfigList.get(position);
            //message.setRead(true);
            notificationConfigList.set(position, message);
            mAdapter.notifyDataSetChanged();

            Toast.makeText(getApplicationContext(), "Clicked...", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onRowLongClicked(int position) {
// long press is performed, enable action mode
        enableActionMode(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
           actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            /*mAdapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });*/
        }

        // deleting the messages from recycler view
        private void deleteMessages() {
            mAdapter.resetAnimationIndex();
            List<Integer> selectedItemPositions =
                    mAdapter.getSelectedItems();
            for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                mAdapter.removeData(selectedItemPositions.get(i));
                deleteNotificationConfigList(notificationConfigList);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    //Rest Call to delete notificationConfigList
    private void deleteNotificationConfigList(List<NotificationConfig> pNotificationConfig)
    {
        swipeRefreshLayout.setRefreshing(true);

        NotificationConfigInterface apiService =
                ApiClient.getClient().create(NotificationConfigInterface.class);

        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("id", String.valueOf(Constants.DEVICE_ID));
        Call<ResponseBody> call = apiService.putNotificationConfigDelete(params, pNotificationConfig, Constants.JWT_TOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), "Deleted Notification Config Successfully", Toast.LENGTH_LONG).show();
                getNotificationConfig();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to Delete Notification Config. Please try again. " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Toast.makeText(getApplicationContext(), "Add Notification Config...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),NotificationConfigActivity.class);
            intent.putExtra("NotificationConfigList", (Serializable) notificationConfigList);
            intent.putExtra("EventID",Constants.EVENT_ID);
            startActivity(intent);
            //
            // finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
