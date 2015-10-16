package com.springtrail.etilqs.Builder;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by kirk on 9/23/15.
 */
public class ViewBuilder extends ModelBuilder<ViewColumn> {
    private final String TAG = getClass().getSimpleName();
    private String from;

    public ViewBuilder(ViewModel model) {
        super(model);
    }

    @Override
    public void finalized() {

    }

    public void setFrom(String from){
        this.from = from;
    }

    @Override
    public ColumnType addColumn(String name, Column.columnTypeEnum type, Object boundField) {
        return ViewColumn.addColumn(name, type, boundField);
    }

    @Override
    public String create(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("line.separator"));
        sb.append("CREATE VIEW IF NOT EXISTS \"");
        sb.append(name);
        sb.append("\" AS");
        sb.append(System.getProperty("line.separator"));
        sb.append("SELECT ");

        Iterator<Map.Entry<String, ColumnType<? extends ViewColumn>>> entries = getColumns().entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, ColumnType<? extends ViewColumn>> entry = entries.next();
            ViewColumn viewColumn = (ViewColumn)entry.getValue();

            sb.append(viewColumn.toString());

            if(entries.hasNext()){
                sb.append(",");
                sb.append(System.getProperty("line.separator"));
            }
        }

        sb.append(System.getProperty("line.separator"));

        sb.append("from ").append(from);
        Log.d(TAG, "create:" + sb.toString());
        return sb.toString();
    }

    @Override
    public String drop() {
        return "DROP VIEW IF EXISTS \"" + getModel().getName() + "\";";
    }

    @Override
    public void executeCreate(SQLiteDatabase database) {
        database.execSQL(create(getModel().getName()));
    }

    @Override
    public void executeDrop(SQLiteDatabase database) {
        database.execSQL(drop());
    }

    @Override
    public void executeUpdate(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(drop());
        database.execSQL(create(getModel().getName()));
    }
}
