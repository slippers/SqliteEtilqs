package com.springtrail.etilqs.Query;

import android.database.Cursor;
import android.database.SQLException;

import com.springtrail.etilqs.DataSource;

/**
 * Created by kirk on 9/22/15.
 */
public class QuerySelect extends QueryBase<QuerySelect> {

    protected String table;
    protected String[] columns;

    public QuerySelect(DataSource dataSource) {
        super(dataSource, DataSource.OpenMode.READ);
    }

    //an example query
    public QuerySelect select(String table, String[] columns) {
        this.table = table;
        this.columns = columns;
        return this;
    }

    public Cursor execute() throws SQLException {

        return getDatabase().query(table,
                columns,
                getWhereFilter().getSelection(),
                getWhereFilter().getSelectionArgs(),
                getGroupBy(),
                getHaving(),
                getOrderBy(),
                getLimit());
    }

}
