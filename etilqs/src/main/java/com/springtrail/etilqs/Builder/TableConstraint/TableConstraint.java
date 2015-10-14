package com.springtrail.etilqs.Builder.TableConstraint;

import com.springtrail.etilqs.Builder.TableColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirk on 9/24/15.
 */
public abstract class TableConstraint {
    private String name;
    protected List<String> columns = new ArrayList<>();

    public TableConstraint(String name, TableColumn tableColumn) {
        this.name = getPrefix() + "_" + name;
        columns.add(tableColumn.getName());
    }

    public TableConstraint(TableColumn tableColumn) {
        name = getPrefix() + "_" + tableColumn.getName();
        columns.add(tableColumn.getName());
    }

    public void setName(String name){
        this.name = name;
    }

    public TableConstraint addColumn(String name){
        columns.add(name);
        return this;
    }

    public String getName(){
        return name;
    }

    public abstract String getPrefix();

    public abstract String toString();
}
