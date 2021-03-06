package com.smlnskgmail.jaman.hashchecker.components.bottomsheets.lists.settings.themes;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.smlnskgmail.jaman.hashchecker.R;
import com.smlnskgmail.jaman.hashchecker.components.bottomsheets.lists.base.BaseListBottomSheet;
import com.smlnskgmail.jaman.hashchecker.components.bottomsheets.lists.base.ListMarker;
import com.smlnskgmail.jaman.hashchecker.components.bottomsheets.lists.base.adapter.BaseBottomSheetListAdapter;
import com.smlnskgmail.jaman.hashchecker.components.bottomsheets.lists.base.adapter.BaseBottomSheetListHolder;
import com.smlnskgmail.jaman.hashchecker.components.dialogs.system.AppAlertDialog;
import com.smlnskgmail.jaman.hashchecker.support.prefs.SettingsHelper;
import com.smlnskgmail.jaman.hashchecker.utils.AppUtils;

import java.util.List;

public class ThemesBottomSheetListAdapter extends BaseBottomSheetListAdapter {

    private final Theme selectedTheme;

    ThemesBottomSheetListAdapter(@NonNull List<ListMarker> items,
                                 @NonNull BaseListBottomSheet bottomSheet) {
        super(items, bottomSheet);
        selectedTheme = Theme.getThemeFromPreferences(getBottomSheet().getContext());
    }

    @Override
    public BaseBottomSheetListHolder getItemsHolder(@NonNull View view, @NonNull Context themeContext) {
        return new ThemesBottomSheetListHolder(view, themeContext);
    }

    private class ThemesBottomSheetListHolder extends BaseBottomSheetListHolder {

        private Theme themeAtPosition;

        ThemesBottomSheetListHolder(@NonNull View itemView, @NonNull Context themeContext) {
            super(itemView, themeContext);
        }

        @Override
        protected void callItemClick() {
            if (themeAtPosition == selectedTheme) {
                dismissBottomSheet();
            } else {
                showThemeApplyDialog();
            }
        }

        @Override
        protected void bind(@NonNull ListMarker listMarker) {
            themeAtPosition = (Theme) getItems().get(getAdapterPosition());
            super.bind(listMarker);
        }

        private void showThemeApplyDialog() {
            AppAlertDialog.show(getContext(), R.string.title_warning_dialog,
                    R.string.message_change_theme, R.string.common_ok, (dialog, which) -> {
                        configureNewTheme();
                        dialog.dismiss();
                        AppUtils.restartApp(getBottomSheet().getActivity());
                    });
        }

        private void configureNewTheme() {
            SettingsHelper.saveTheme(getContext(), themeAtPosition);
        }

        @Override
        protected boolean getConditionToPrimaryIconVisibleState() {
            return true;
        }

        @Override
        protected boolean getConditionToAdditionalIconVisibleState() {
            return themeAtPosition == selectedTheme;
        }

    }

}

