package com.springtrail.etilqs.Builder.Query;

import com.springtrail.etilqs.Builder.ModelBuilder;
import com.springtrail.etilqs.Builder.TableModel;
import com.springtrail.etilqs.DataSource;
import com.springtrail.etilqs.Query.QueryDelete;
import com.springtrail.etilqs.Query.WhereBase;

/**
 * Created by kirk on 10/12/15.
 */
public class QueryModelDelete extends QueryDelete {

    ModelBuilder builder;

    public QueryModelDelete(DataSource dataSource, ModelBuilder modelBuilder) {
        super(dataSource);
        this.builder = modelBuilder;
        this.table = modelBuilder.getModel().getName();
    }

    public QueryModelDelete(DataSource dataSource, ModelBuilder builder, TableModel model) {
        this(dataSource, builder);
        delete(model);
    }

    public QueryModelDelete delete(TableModel model){
        //transfer the model to the model columns
        model.getModelData(builder);

        if(model.getId() > 0)
            where(TableModel.ID, WhereBase.Operator.EQUAL, model.getId().toString());

        model.delete(this);

        return this;
    }

}
