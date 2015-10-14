package com.springtrail.etilqs.Query;

import com.springtrail.etilqs.DataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirk on 10/6/15.
 */
public class QueryInsertBatch extends QueryBase<QueryInsertBatch> {
    private String table;
    private String[] columns;
    private List<String[]> batch;

//    http://www.sqlite.org/lang_insert.html

    public QueryInsertBatch(DataSource dataSource) {
        super(dataSource, DataSource.OpenMode.WRITE);
    }

    public QueryInsertBatch startBatch(String table, String[] columns){
        this.table = table;
        this.columns = columns;
        this.batch = new ArrayList<>();
        return this;
    }

    public QueryInsertBatch values(String[] values){
        this.batch.add(values);
        return this;
    }

    public void executeBatch(){

        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO '").append(table).append("' (");

        sql.append(ArrayToString(columns)).append(")");

        sql.append(System.getProperty("line.separator"));

        sql.append("VALUES ");

        for(String[] values : batch){
            sql.append("(").append(ArrayToString(values)).append("),");
        }

        sql.deleteCharAt(sql.length() - 1);

        sql.append(";");

        getDatabase().beginTransaction();
        try {
            getDatabase().execSQL(sql.toString());

            getDatabase().setTransactionSuccessful();
        } finally {
            getDatabase().endTransaction();
        }

    }

    public static String ArrayToString(String[] name){
        if (name.length > 0) {
            StringBuilder nameBuilder = new StringBuilder();

            for (String n : name) {
                nameBuilder.append("'").append(n.replace("'", "\\'")).append("',");
            }

            nameBuilder.deleteCharAt(nameBuilder.length() - 1);

            return nameBuilder.toString();
        } else {
            return "";
        }
    }

}
