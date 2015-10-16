package com.springtrail.etilqs.Builder;

/**
 * Created by kirk on 9/23/15.
 */
public abstract class ViewModel extends Model {


    public ViewModel() {
        super();
    }

    @Override
    public void configureModel(ModelBuilder builder) {
        configureTableModel(((ViewBuilder) builder));
    }

    public void configureTableModel(ViewBuilder builder) {

    }

    @Override
    public ModelBuilder getBuilder() {
        return new ViewBuilder(this);
    }


}
