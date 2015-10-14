package com.springtrail.etilqs.Query;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kirk on 9/25/15.
 */
public class WhereSelection extends WhereBase<WhereSelection> {
    private String selection;
    private String[] selectionArgs;

    public WhereSelection(String selection, String[] selectionArgs){
        if(TextUtils.isEmpty(selection))
            throw new IllegalArgumentException("arguments invalid.");

        this.selection = selection;
        this.selectionArgs = selectionArgs;
    }

    @Override
    public String getClause() {
        return selection;
    }

    @Override
    public List<String> getSelectionArgs() {
        if(selectionArgs == null)
            return null;
        return Arrays.asList(selectionArgs);
    }
}
