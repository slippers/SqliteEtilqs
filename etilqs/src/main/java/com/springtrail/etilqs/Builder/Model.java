package com.springtrail.etilqs.Builder;

/**
 * Created by kirk on 10/2/15.
 */
public abstract class Model {
    String name;

    public Model() {
        this.name = this.getClass().getSimpleName();
    }

    //The model must decide which builder to use
    public abstract ModelBuilder getBuilder();

    public final String getName() {
        return name;
    }

    //Configuration method configureModel() called to post process the columns.
    //use this to add columns and constraints for the table
    public void configureModel(ModelBuilder builder){}
}
