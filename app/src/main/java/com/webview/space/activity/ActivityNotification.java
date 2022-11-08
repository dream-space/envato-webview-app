package com.webview.space.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.webview.space.R;
import com.webview.space.adapter.AdapterNotification;
import com.webview.space.databinding.ActivityNotificationBinding;
import com.webview.space.room.AppDatabase;
import com.webview.space.room.DAO;
import com.webview.space.room.table.NotificationEntity;
import com.webview.space.utils.Tools;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ActivityNotification extends AppCompatActivity {

    public static void navigate(Activity activity) {
        Intent i = new Intent(activity, ActivityNotification.class);
        activity.startActivity(i);
    }

    public ActivityNotificationBinding binding;

    public AdapterNotification adapter;
    private DAO dao;
    static ActivityNotification activityNotification;

    public static ActivityNotification getInstance() {
        return activityNotification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activityNotification = this;
        dao = AppDatabase.getDb(this).get();

        initToolbar();
        iniComponent();
    }

    private void initToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        binding.toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        binding.toolbarMenuDelete.setOnClickListener(v -> {
            if (adapter.getItemCount() == 0) {
                Snackbar.make(binding.getRoot(), R.string.msg_notif_empty, Snackbar.LENGTH_SHORT).show();
                return;
            }
            dialogDeleteConfirmation();
        });

    }

    private void iniComponent() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set data and list adapter
        adapter = new AdapterNotification(this, binding.recyclerView, new ArrayList<>());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((view, obj, pos) -> {
            obj.read = true;
            dao.insertNotification(obj);
            adapter.notifyItemChanged(pos);
            showDetailsDialog(obj);
        });

        startLoadMoreAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void dialogDeleteConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_delete_confirm);
        builder.setMessage(getString(R.string.content_delete_confirm) + getString(R.string.title_activity_notification));
        builder.setPositiveButton(R.string.YES, (di, i) -> {
            di.dismiss();
            dao.deleteAllNotification();
            startLoadMoreAdapter();
            Snackbar.make(binding.getRoot(), R.string.delete_success, Snackbar.LENGTH_SHORT).show();
        });
        builder.setNegativeButton(R.string.CANCEL, null);
        builder.show();
    }

    private void startLoadMoreAdapter() {
        adapter.resetListData();
        List<NotificationEntity> items = dao.getNotificationByPage(10, 0);
        adapter.insertData(items);
        showNoItemView(false);
        final int item_count = dao.getNotificationCount();
        showNoItemView(item_count == 0);
        // detect when scroll reach bottom
        adapter.setOnLoadMoreListener(current_page -> {
            if (item_count > adapter.getItemCount() && current_page != 0) {
                displayDataByPage(current_page);
            } else {
                adapter.setLoaded();
            }
        });
    }

    private void displayDataByPage(final int next_page) {
        adapter.setLoading();
        new Handler().postDelayed(() -> {
            List<NotificationEntity> items = dao.getNotificationByPage(10, (next_page * 10));
            adapter.insertData(items);
        }, 500);
    }

    private void showNoItemView(boolean show) {
        ((TextView) findViewById(R.id.failed_text)).setText(getString(R.string.empty_state_no_data));
        binding.lytFailed.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDetailsDialog(NotificationEntity entity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notification);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.title)).setText(entity.title);
        ((TextView) dialog.findViewById(R.id.content)).setText(entity.content);
        ((TextView) dialog.findViewById(R.id.date)).setText(Tools.getFormattedDateSimple(entity.created_at));

        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        View lyt_image = dialog.findViewById(R.id.lyt_image);
        View bt_open = dialog.findViewById(R.id.bt_open);

        bt_open.setVisibility(TextUtils.isEmpty(entity.getLink()) ? View.GONE : View.VISIBLE);
        if (TextUtils.isEmpty(entity.getImage())) {
            lyt_image.setVisibility(View.GONE);
        } else {
            Tools.displayImage(this, image, entity.getImage());
        }

        (dialog.findViewById(R.id.bt_delete)).setOnClickListener(v -> {
            dao.deleteNotification(entity.getId());
            startLoadMoreAdapter();
            dialog.dismiss();
        });

        bt_open.setOnClickListener(v -> {
            Intent intent = ActivityMain.navigate(ActivityNotification.this, entity.title, entity.getLink());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}

