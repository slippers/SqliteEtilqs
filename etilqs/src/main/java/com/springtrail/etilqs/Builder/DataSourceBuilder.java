package com.springtrail.etilqs.Builder;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.springtrail.etilqs.Builder.Query.QueryModelDelete;
import com.springtrail.etilqs.Builder.Query.QueryModelInsert;
import com.springtrail.etilqs.Builder.Query.QueryModelSelect;
import com.springtrail.etilqs.Builder.Query.QueryModelUpdate;
import com.springtrail.etilqs.DataSource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by kirk on 10/2/15.
 */
public class DataSourceBuilder extends DataSource {

    //maintain insertion order
    private Map<String, ModelBuilder> builders = new LinkedHashMap<>();

    public DataSourceBuilder(Context context, String databaseName, Integer version) {
        super(context, databaseName, version);
    }

    @Override
    public void open() throws SQLException {

        for (ModelBuilder builder : builders.values()) {
            builder.finalized();
        }

        super.open();
    }

    //chose to pass a instantiated class because of model reuse.
    //for instance the same model could be created with a different name
    public void addBuilder(Model model){
        builders.put(model.getName(), ModelBuilder.createBuilder(model));
    }

    public ModelBuilder getBuilder(String name) {
        return builders.get(name);
    }

    //DatabaseHelper will call into this
    public void onCreateFirstTime(SQLiteDatabase database){
        for (ModelBuilder builder : builders.values()) {
            builder.executeCreate(database);
        }
    }

    //DatabaseHelper will call into this
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        for (ModelBuilder builder : builders.values()) {
            builder.executeUpdate(database, oldVersion, newVersion);
        }
    }

    public QueryModelSelect getSelect(String name){
        return getBuilder(name).getSelect(this);
    }

    public QueryModelSelect getSelect(TableModel model){
        return getBuilder(model.getName()).getSelect(this, model);
    }

    public QueryModelInsert getInsert(TableModel model){
        return getBuilder(model.getName()).getInsert(this, model);
    }

    public QueryModelDelete getDelete(TableModel model){
        return getBuilder(model.getName()).getDelete(this, model);
    }

    public QueryModelUpdate getUpdate(TableModel model){
        return getBuilder(model.getName()).getUpdate(this, model);
    }

}
