package com.springtrail.etilqs.Query;

import android.database.SQLException;

import com.springtrail.etilqs.DataSource;

/**
 * Created by kirk on 9/22/15.
 */
public class QueryDelete extends QueryBase<QueryDelete> {

    protected String table;

    public QueryDelete(DataSource dataSource) {
        super(dataSource, DataSource.OpenMode.WRITE);
    }

    public QueryDelete delete(String table){
        this.table = table;
        return this;
    }

    public int execute() throws SQLException {
        if(getWhereFilter().size() == 0)
            throw new UnsupportedOperationException("where clause is required");

        int rowAffected = getDatabase().delete(table,
                getWhereFilter().getSelection(),
                getWhereFilter().getSelectionArgs());

        return rowAffected;
    }
}
