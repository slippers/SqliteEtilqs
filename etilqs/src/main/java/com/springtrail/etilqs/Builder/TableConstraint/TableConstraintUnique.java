package com.springtrail.etilqs.Builder.TableConstraint;

import com.springtrail.etilqs.Builder.TableColumn;

/**
 * Created by kirk on 9/24/15.
 */
public class TableConstraintUnique extends TableConstraint {

    public TableConstraintUnique(TableColumn tableColumn) {
        super(tableColumn);
    }

    @Override
    public String getPrefix() {
        return "UN";
    }

    @Override
    public String toString() {
        String result = "";

        if(columns.size() == 0){return result;}

        result = ",CONSTRAINT " + getName() + " UNIQUE (";
        for (int i = 0; i < columns.size(); i ++) {
            result += columns.get(i);
            if(i < columns.size() - 1){
                result += ",";
            }
        }
        result += ") ";

        return result;
    }

}