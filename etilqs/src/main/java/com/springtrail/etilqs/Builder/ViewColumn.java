package com.springtrail.etilqs.Builder;

import android.text.TextUtils;

/**
 * Created by kirk on 9/25/15.
 */
public class ViewColumn<T> extends ColumnType<T> {

    private String expr;

    public ViewColumn(ColumnType<? extends T> columnType) {
        super(columnType);
    }

    public ViewColumn(String name, Column.columnTypeEnum columnType, Class<T> genericType) {
        super(name, columnType, genericType);
    }

    @Override
    public void configureColumn(Column column) {
        this.setExpr(column.expression());
    }

    protected static ViewColumn addColumn(String name, Column.columnTypeEnum columnType, Object boundField) {
        return new ViewColumn(name, columnType, getBoundFieldType(boundField));
    }

    public ViewColumn setExpr(String value){
        expr = value;
        return this;
    }

    public String toString(){
        //() as name
        StringBuilder sb = new StringBuilder();

        if(TextUtils.isEmpty(expr)){
            sb.append(this.getName());
        } else {
            sb.append("(");
            sb.append(expr);
            sb.append(") AS ");
            sb.append(this.getName());
        }
        return sb.toString();
    }
}
