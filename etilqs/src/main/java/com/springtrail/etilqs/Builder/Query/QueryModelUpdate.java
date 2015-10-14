package com.springtrail.etilqs.Builder.Query;

import com.springtrail.etilqs.Builder.ModelBuilder;
import com.springtrail.etilqs.Builder.TableColumn;
import com.springtrail.etilqs.Builder.TableModel;
import com.springtrail.etilqs.DataSource;
import com.springtrail.etilqs.Query.QueryUpdate;
import com.springtrail.etilqs.Query.WhereBase;

/**
 * Created by kirk on 10/12/15.
 */
public class QueryModelUpdate extends QueryUpdate {

    ModelBuilder builder;

    public QueryModelUpdate(DataSource dataSource, ModelBuilder modelBuilder) {
        super(dataSource);
        this.builder =  modelBuilder;
        this.table = modelBuilder.getModel().getName();
    }

    public QueryModelUpdate(DataSource dataSource, ModelBuilder builder, TableModel model) {
        this(dataSource, builder);
        update(model);
    }

    public QueryModelUpdate update(TableModel model){
        //transfer the model to the model columns
        model.getModelData(builder);

        if(model.getId() > 0)
            where(TableModel.ID, WhereBase.Operator.EQUAL, model.getId().toString());

        //pass control to the model for update
        model.update(this);

        for( TableColumn tableColumn : model.getColumns().values()){
            tableColumn.setContentValues(contentValues);
        }

        return this;
    }

}
