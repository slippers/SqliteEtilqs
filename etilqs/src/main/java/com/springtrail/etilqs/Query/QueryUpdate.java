package com.springtrail.etilqs.Query;

import android.content.ContentValues;
import android.database.SQLException;

import com.springtrail.etilqs.DataSource;

/**
 * Created by kirk on 9/22/15.
 */
public class QueryUpdate extends QueryBase<QueryUpdate> {

    protected String table;
    protected ContentValues contentValues = new ContentValues();

    public QueryUpdate(DataSource dataSource) {
        super(dataSource, DataSource.OpenMode.WRITE);
    }

    public QueryUpdate update(String table, ContentValues values){
        this.table = table;
        this.contentValues = values;
        return this;
    }

    public int execute() throws SQLException {

        if(getWhereFilter().size() == 0)
            throw new UnsupportedOperationException("where clause is required");

        return getDatabase().update(table,
                contentValues,
                getWhereFilter().getSelection(),
                getWhereFilter().getSelectionArgs());

    }
}
