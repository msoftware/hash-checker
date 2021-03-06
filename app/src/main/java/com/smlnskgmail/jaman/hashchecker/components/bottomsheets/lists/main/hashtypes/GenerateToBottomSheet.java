package com.smlnskgmail.jaman.hashchecker.components.bottomsheets.lists.main.hashtypes;

import androidx.annotation.NonNull;

import com.smlnskgmail.jaman.hashchecker.components.bottomsheets.lists.base.BaseListBottomSheet;
import com.smlnskgmail.jaman.hashchecker.components.bottomsheets.lists.base.adapter.BaseBottomSheetListAdapter;

public class GenerateToBottomSheet extends BaseListBottomSheet {

    private HashTypeSelectTarget hashTypeSelectListener;

    @Override
    public BaseBottomSheetListAdapter getItemsAdapter() {
        return new HashesBottomSheetListAdapter(getItems(), this, hashTypeSelectListener);
    }

    public void setHashTypeSelectListener(@NonNull HashTypeSelectTarget hashTypeSelectListener) {
        this.hashTypeSelectListener = hashTypeSelectListener;
    }

}
