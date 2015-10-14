package com.springtrail.etilqs.Builder.TableConstraint;

import com.springtrail.etilqs.Builder.TableColumn;

/**
 * Created by kirk on 9/24/15.
 */

//TODO: PK can consist of more than one column.
public class TableConstraintPrimary extends TableConstraint {

    public TableConstraintPrimary(TableColumn tableColumn) {
        super("primary", tableColumn);
    }

    @Override
    public String getPrefix() {
        return "PK";
    }

    @Override
    public String toString() {
        String result = "";

        if(columns.size() == 0){return result;}

        result = ",CONSTRAINT " + getName() + " PRIMARY KEY (";
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
