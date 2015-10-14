package com.springtrail.etilqs.Builder;

import android.database.sqlite.SQLiteDatabase;

import com.springtrail.etilqs.Builder.Query.QueryModelDelete;
import com.springtrail.etilqs.Builder.Query.QueryModelInsert;
import com.springtrail.etilqs.Builder.Query.QueryModelSelect;
import com.springtrail.etilqs.Builder.Query.QueryModelUpdate;
import com.springtrail.etilqs.DataSource;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by kirk on 9/21/15.
 */
public abstract class ModelBuilder<T extends ColumnType> {
    /*
    Knows how to take a model and build it in the target system.

    Then the builder will generate the necessary create/upgrade/drop statements

    */

    private Map<String, ColumnType<? extends T>>  columns = new LinkedHashMap<>();

    private Model model;

    private Class<Model> modelClass;

    public static ModelBuilder createBuilder(Model model){
        ModelBuilder builder = model.getBuilder();
        builder.configure();
        return builder;
    }

    public ModelBuilder(Model model) {
        this.model = model;
        this.modelClass = (Class<Model>) model.getClass();
    }

    public void configure(){

        //build columns from annotated model fields
        for (Field field : modelClass.getSuperclass().getDeclaredFields()) {
            extractColumn(field);
        }

        for (Field field : modelClass.getDeclaredFields()) {
            extractColumn(field);
        }

        //pass control to model for further configuration
        this.model.configureModel(this);
    }

    private void extractColumn(Field field) {
        Column column;
        // check if field has annotation
        if ((column = field.getAnnotation(Column.class)) != null) {
            ColumnType columnType = addColumn(column.name(), column.type(), field.getType());

            columnType.configureColumn(column);

            columns.put(column.name(), columnType);
        }
    }

    public Model getModel(){return model;}

    public ColumnType getColumn(String name){
        return columns.get(name);
    }

    public Map<String, ColumnType<? extends T>> getColumns() {return columns;}

    public Map<String, ColumnType<? extends T>> copyColums(){
        Map<String, ColumnType<? extends T>>  copy = new LinkedHashMap<>();
        copy.putAll(columns);
        return copy;
    }

    public String[] getColumnNames(){
        Set<String> keys = columns.keySet();
        String[] array = keys.toArray(new String[keys.size()]);
        return array;
    }

    public String getAppendColumns(String delimiter){
        String tempDelimiter = "";
        StringBuilder sb = new StringBuilder();
        for(String s : getColumnNames()){
            sb.append(tempDelimiter).append(s);
            tempDelimiter = delimiter;
        }
        return sb.toString();
    }

    //called after setup to add computed constraints
    public abstract void finalized();

    //compose the create sql object as string
    public abstract String create(String name);

    //compose the drop sql object as string
    public abstract String drop();

    //called from the datasource when the dbhelper is setting up.
    public abstract void executeCreate(SQLiteDatabase database);

    public abstract void executeDrop(SQLiteDatabase database);

    public abstract void executeUpdate(SQLiteDatabase database, int oldVersion, int newVersion);

    public abstract ColumnType addColumn(String name, Column.columnTypeEnum type, Object boundField);

    public QueryModelSelect getSelect(DataSource dataSource){
        return new QueryModelSelect(dataSource, this);
    }

    public QueryModelSelect getSelect(DataSource dataSource, TableModel model){
        return new QueryModelSelect(dataSource, this, model);
    }

    public QueryModelInsert getInsert(DataSource dataSource, TableModel model){
        return new QueryModelInsert(dataSource, this, model);
    }

    public QueryModelDelete getDelete(DataSource dataSource, TableModel model){
        return new QueryModelDelete(dataSource, this, model);
    }

    public QueryModelUpdate getUpdate(DataSource dataSource, TableModel model){
        return new QueryModelUpdate(dataSource, this, model);
    }

}
