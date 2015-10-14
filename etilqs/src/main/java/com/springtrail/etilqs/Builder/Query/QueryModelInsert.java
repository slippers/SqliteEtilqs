package com.springtrail.etilqs.Builder.Query;

import com.springtrail.etilqs.Builder.ModelBuilder;
import com.springtrail.etilqs.Builder.TableColumn;
import com.springtrail.etilqs.Builder.TableModel;
import com.springtrail.etilqs.DataSource;
import com.springtrail.etilqs.Query.QueryInsert;

/**
 * Created by kirk on 10/8/15.
 */
public class QueryModelInsert extends QueryInsert {

    ModelBuilder builder;

    public QueryModelInsert(DataSource dataSource, ModelBuilder builder) {
        super(dataSource);
        this.builder = builder;
        this.table = builder.getModel().getName();
    }

    public QueryModelInsert(DataSource dataSource, ModelBuilder builder, TableModel model) {
        this(dataSource, builder);
        insert(model);
    }

    public QueryModelInsert insert(TableModel model){

        //transfer the model to the model columns
        model.getModelData(builder);

        TableColumn column_id = model.getColumns().get(model.ID);
        if(column_id.getValue().equals(0)){
            column_id.setValue(null);
        }

        model.insert();

        for( TableColumn tableColumn : model.getColumns().values()){
            tableColumn.setContentValues(contentValues);
        }

        return this;
    }

}
