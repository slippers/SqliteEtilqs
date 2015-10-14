package com.springtrail.etilqs.Builder.Query;

import android.database.SQLException;

import com.springtrail.etilqs.Builder.ModelBuilder;
import com.springtrail.etilqs.Builder.TableModel;
import com.springtrail.etilqs.DataSource;
import com.springtrail.etilqs.Query.QuerySelect;

/**
 * Created by kirk on 10/6/15.
 */
public class QueryModelSelect extends QuerySelect {

    public QueryModelSelect(DataSource dataSource, ModelBuilder builder) {
        super(dataSource);
        columns = builder.getColumnNames();
        table = builder.getModel().getName();
    }

    public QueryModelSelect(DataSource dataSource, ModelBuilder builder, TableModel model) {
        this(dataSource, builder);
        select();
    }

    //an example query
    public QueryModelSelect select() throws SQLException {
        return this;
    }

    public QuerySelect select(String[] columns) {
        this.columns = columns;
        return this;
    }

}
