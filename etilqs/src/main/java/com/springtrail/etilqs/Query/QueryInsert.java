package com.springtrail.etilqs.Query;

import android.content.ContentValues;
import android.database.SQLException;

import com.springtrail.etilqs.DataSource;

/**
 * Created by kirk on 9/22/15.
 */
public class QueryInsert extends QueryBase<QueryInsert> {

    protected String table;
    protected ContentValues contentValues = new ContentValues();

    public QueryInsert(DataSource dataSource) {
        super(dataSource, DataSource.OpenMode.WRITE);
    }

    public QueryInsert insert(String table, ContentValues values){
        this.table = table;
        this.contentValues = values;
        return this;
    }

    public Long execute() throws SQLException {
        return getDatabase().insertOrThrow(table, null, contentValues);
    }

}
