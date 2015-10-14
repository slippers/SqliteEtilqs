package com.springtrail.etilqs.Builder;

import android.util.Log;

import com.springtrail.etilqs.Builder.Query.QueryModelDelete;
import com.springtrail.etilqs.Builder.Query.QueryModelUpdate;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by kirk on 5/26/15.
 */


public abstract class TableModel extends Model {

    private Map<String, TableColumn> columns;

    public static final String ID = "_id";

    @Column(name=ID, type = Column.columnTypeEnum.INTEGER)
    Integer id = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TableModel() {
        super();
    }

    @Override
    public void configureModel(ModelBuilder builder) {
        configureTableModel(((TableBuilder) builder));
    }

    public void configureTableModel(TableBuilder builder) {
        builder.getTableColumn(ID).setPrimary();
    }

    @Override
    public final ModelBuilder getBuilder() {
        return new TableBuilder(this);
    }

    public Map<String, TableColumn> getColumns() {
        return columns;
    }

    //get data from a model(this) and set columtypes
    public void getModelData(ModelBuilder builder) {
        //is the model passed in the same class as the builder has?
        if(!builder.getModel().getClass().equals(this.getClass())){
            throw new IllegalArgumentException("models do not match");
        }

        //refresh columns from builder
        this.columns = builder.copyColums();

        for (Field field : getClass().getSuperclass().getDeclaredFields()) {
            setColumnValue(field);
        }

        for (Field field : getClass().getDeclaredFields()) {
            setColumnValue(field);
        }

    }

    private void setColumnValue(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column != null) {
            try {
                getColumns().get(column.name()).setValue(field.get(this));
            } catch (IllegalAccessException e) {
                Log.e(getClass().getSimpleName(), "getModelData", e);
            }
        }
    }

    public void update(QueryModelUpdate queryModelUpdate){}

    public void delete(QueryModelDelete queryModelDelete){}

    public void insert(){}


}
