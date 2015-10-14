package com.springtrail.etilqs.Builder.TableConstraint;

import com.springtrail.etilqs.Builder.TableColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirk on 9/24/15.
 *
 TableColumn.addForeignKey(new TableConstraintForeignKey(tableColumn)
 .addRelatedTable("relatedTable")
 .addRelatedColumn("id")
 .addColumn("relatedtable_id")
 .addAction(TableConstraintForeignKey.Trigger.ON_DELETE, TableConstraintForeignKey.Actions.CASCADE)
 );
 *
 */
public class TableConstraintForeignKey extends TableConstraint {

    public enum Actions {
        NO_ACTION,
        RESTRICT,
        SET_NULL,
        SET_DEFAULT,
        CASCADE
    }

    public enum Trigger {
        ON_UPDATE,
        ON_DELETE
    }

    private Trigger trigger;

    private Actions action;

    private String relatedTable;

    protected List<String> relatedColumns = new ArrayList<String>();

    public TableConstraintForeignKey(String relatedTable, TableColumn tableColumn) {
        super(tableColumn);
        this.relatedTable = relatedTable;
//        relatedColumns.add(tableColumn.getName());
    }

//    public TableConstraintForeignKey addRelatedTable(String name){
//        this.relatedTable = name;
//        return this;
//    }

    public TableConstraintForeignKey addRelatedColumn(String name){
        relatedColumns.add(name);
        return this;
    }

    public TableConstraintForeignKey addColumn(String name){
        columns.add(name);
        return this;
    }

    //Can only add one action per foreign key
    public TableConstraintForeignKey addAction(Trigger trigger, Actions action){
        this.trigger = trigger;
        this.action = action;
        return this;
    }

    @Override
    public String getPrefix() {
        return "FK";
    }

    @Override
    public String toString() {
        String result = "";

        if(columns.size() == 0){return result;}

        result = ",CONSTRAINT " + getName() + "  FOREIGN KEY (";
        for (int i = 0; i < columns.size(); i ++) {
            result += columns.get(i);
            if(i < columns.size() - 1){
                result += ",";
            }
        }
        result += ") REFERENCES " + relatedTable + "(" ;
        for (int i = 0; i < relatedColumns.size(); i ++) {
            result += relatedColumns.get(i);
            if(i < relatedColumns.size() - 1){
                result += ",";
            }
        }
        result += ")";
        if(null != trigger){
            switch (trigger){
                case ON_DELETE:
                    result += " ON DELETE";
                    break;
                case ON_UPDATE:
                    result += " ON UPDATE";
                    break;
            }

            switch (action){
                case CASCADE:
                    result += " CASCADE";
                    break;
                case NO_ACTION:
                    result += " NO ACTION";
                    break;
                case RESTRICT:
                    result += " RESTRICT";
                    break;
                case SET_DEFAULT:
                    result += " SET DEFAULT";
                    break;
                case SET_NULL:
                    result += " SET NULL";
                    break;
            }

        }
        return result;
    }
}
