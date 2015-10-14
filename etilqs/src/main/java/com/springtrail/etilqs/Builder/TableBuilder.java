package com.springtrail.etilqs.Builder;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.springtrail.etilqs.Builder.TableConstraint.TableConstraint;
import com.springtrail.etilqs.Builder.TableConstraint.TableConstraintCheck;
import com.springtrail.etilqs.Builder.TableConstraint.TableConstraintPrimary;
import com.springtrail.etilqs.Builder.TableConstraint.TableConstraintUnique;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by kirk on 9/23/15.
 */
public class TableBuilder extends ModelBuilder<TableColumn> {

    private final String TAG = getClass().getSimpleName();

    private Map<String, TableConstraint>  tableConstraints = new LinkedHashMap<>();

    private TableConstraintPrimary tableConstraintPrimary;

    public TableBuilder(TableModel model) {
        super(model);
    }

    public TableColumn<?> getTableColumn(String name){
        return (TableColumn<?>)this.getColumn(name);
    }

    @Override
    public void finalized() {
        Iterator<Map.Entry<String, ColumnType<? extends TableColumn>>> entries = getColumns().entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, ColumnType<? extends TableColumn>> entry = entries.next();
            addColumnConstraints((TableColumn) entry.getValue());
        }
    }

    private void addColumnConstraints(TableColumn tableColumn){

        //can only have one primary key
        if (tableColumn.isPrimary()) {
            TableConstraintPrimary tableConstraintPrimary = (TableConstraintPrimary) tableConstraints.get(TableConstraintPrimary.class.getSimpleName());

            if(tableConstraintPrimary == null)
                tableConstraints.put(TableConstraintPrimary.class.getSimpleName(), new TableConstraintPrimary(tableColumn));
            else
                tableConstraintPrimary.addColumn(tableColumn.getName());
        }

        if (tableColumn.isUnique()) {
            addTableConstraint(new TableConstraintUnique(tableColumn));
        }

        if (tableColumn.getCheckConstraint() != null) {
            addTableConstraint(new TableConstraintCheck(tableColumn));
        }
    }

    public TableConstraint addTableConstraint(TableConstraint tableConstraint){
        tableConstraints.put(tableConstraint.getName(), tableConstraint);
        return tableConstraints.get(tableConstraint.getName());
    }

    @Override
    public ColumnType addColumn(String name, Column.columnTypeEnum type, Object boundField) {
        return TableColumn.addColumn(name, type, boundField);
    }

    @Override
    public String create(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(name);
        sb.append("(");
        sb.append(System.getProperty("line.separator"));

        Iterator<Map.Entry<String, ColumnType<? extends TableColumn>>> entries = getColumns().entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<String, ColumnType<? extends TableColumn>> entry = entries.next();
            TableColumn tableColumn = (TableColumn)entry.getValue();
            sb.append(tableColumn.toString());

            if(entries.hasNext()){
                sb.append(",");
                sb.append(System.getProperty("line.separator"));
            }
        }

        sb.append(System.getProperty("line.separator"));

        for(Map.Entry<String, TableConstraint> constraints : tableConstraints.entrySet()){
            sb.append(constraints.getValue().toString());
            sb.append(System.getProperty("line.separator"));
        }

        sb.append(");");

        Log.d(TAG, "create:" + sb.toString());

        return sb.toString();
    }

    @Override
    public String drop(){
        return "DROP TABLE IF EXISTS \"" + getModel().getName() + "\";";
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
        // https://www.sqlite.org/lang_altertable.html
        // If version is newer than last version in version table : rename table, create new table and copy data over.
        // http://stackoverflow.com/questions/3505900/sqliteopenhelper-onupgrade-confusion-android

        String new_table = "new_" + getModel().getName();

        database.beginTransaction();
        try {
            database.execSQL(create(new_table));

            database.execSQL("INSERT INTO " + new_table + " SELECT " + getAppendColumns(",") + " FROM " + getModel().getName() + ";");

            database.execSQL("DROP TABLE " + getModel().getName() + ";");

            database.execSQL("ALTER TABLE " + new_table + " RENAME TO " + getModel().getName() + ";");

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }



}
