package com.smlnskgmail.jaman.hashchecker.navigation.fragments.history;

import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smlnskgmail.jaman.hashchecker.R;
import com.smlnskgmail.jaman.hashchecker.components.containers.AdaptiveRecyclerView;
import com.smlnskgmail.jaman.hashchecker.components.dialogs.system.AppAlertDialog;
import com.smlnskgmail.jaman.hashchecker.db.HelperFactory;
import com.smlnskgmail.jaman.hashchecker.navigation.fragments.BaseFragment;
import com.smlnskgmail.jaman.hashchecker.navigation.fragments.history.entities.HistoryItem;
import com.smlnskgmail.jaman.hashchecker.navigation.fragments.history.entities.HistoryPortion;
import com.smlnskgmail.jaman.hashchecker.navigation.fragments.history.listadapter.HistoryItemsAdapter;
import com.smlnskgmail.jaman.hashchecker.navigation.fragments.history.loader.HistoryItemsLoader;
import com.smlnskgmail.jaman.hashchecker.navigation.fragments.history.loader.LoaderTarget;

import java.util.List;

public class HistoryFragment extends BaseFragment implements LoaderTarget<HistoryItem> {

    private FrameLayout flHistory;
    private AdaptiveRecyclerView rvHistoryItems;
    private ProgressBar pbHistory;

    private final HistoryPortion historyPortion = new HistoryPortion();

    private boolean isLoading;

    @Override
    public void initializeUI(@NonNull View contentView) {
        flHistory = contentView.findViewById(R.id.fl_history);
        pbHistory = contentView.findViewById(R.id.pb_history);
        rvHistoryItems = contentView.findViewById(R.id.rv_history_items);
        rvHistoryItems.setEmptyMessageView(contentView.findViewById(R.id.ll_history_empty_view));
        rvHistoryItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (canLoad(totalItemCount, lastVisibleItem)) {
                    isLoading = true;
                    load();
                }
            }

            private boolean canLoad(int totalItemCount, int lastVisibleItem) {
                return !isLoading && !historyPortion.isLoaded()
                        && totalItemCount <= lastVisibleItem + 2;
            }
        });
        resetHistoryAdapter();
        load();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_clean_history) {
            AppAlertDialog.show(getContext(), R.string.title_warning_dialog,
                    R.string.message_delete_all_history_items, R.string.common_ok, (dialog, which) -> {
                HelperFactory.getHelper().deleteAllHistoryItems();
                resetHistoryAdapter();
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetHistoryAdapter() {
        rvHistoryItems.setAdapter(new HistoryItemsAdapter(flHistory));
    }

    private void load() {
        if (!historyPortion.isLoaded()) {
            pbHistory.setVisibility(View.VISIBLE);
            rvHistoryItems.setVisibility(View.GONE);
            rvHistoryItems.getEmptyMessage().setVisibility(View.GONE);
            new HistoryItemsLoader(this).execute();
        }
    }

    @Override
    public void postLoad(List<HistoryItem> items) {
        pbHistory.setVisibility(View.GONE);
        rvHistoryItems.setVisibility(View.VISIBLE);
        ((HistoryItemsAdapter) rvHistoryItems.getAdapter()).addHistoryItems(items);
    }

    @Override
    public HistoryPortion dataPortion() {
        return historyPortion;
    }

    @Override
    public int getMenuResId() {
        return R.menu.menu_history;
    }

    @Override
    public int[] getMenuItemsIds() {
        return new int[0];
    }

    @Override
    public int getActionBarTitleResId() {
        return R.string.menu_title_history;
    }

    @Override
    public boolean setBackActionIcon() {
        return true;
    }

    @Override
    public int getBackActionIconResId() {
        return R.drawable.ic_arrow_back;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragmant_history;
    }

}
