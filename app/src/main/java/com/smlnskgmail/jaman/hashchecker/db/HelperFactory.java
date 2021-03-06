package com.smlnskgmail.jaman.hashchecker.db;

import android.content.Context;

import androidx.annotation.NonNull;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.smlnskgmail.jaman.hashchecker.db.helper.DatabaseHelper;

public class HelperFactory {

    private static DatabaseHelper databaseHelper;

    public static DatabaseHelper getHelper() {
        return databaseHelper;
    }

    public static void setHelper(@NonNull Context context) {
        if (databaseHelper != null) {
            releaseHelper();
        }
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    public static void releaseHelper() {
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
    }

}
