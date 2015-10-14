package com.springtrail.etilqs.Builder;


import android.text.TextUtils;

/**
 * Created by kirk on 4/25/2015.
 */
public class TableColumn<T> extends ColumnType<T> {

    private boolean nullable = true;
    private boolean primary = false;
    private boolean unique = false;
    private T defaultConstraint;
    private String checkConstraint = null;

    // Copy constructor
    public TableColumn(TableColumn<? extends T> column) {
        super(column);
        this.nullable = column.nullable;
        this.primary = column.primary;
        this.unique = column.unique;
        this.defaultConstraint = column.defaultConstraint;
        this.checkConstraint = column.checkConstraint;
    }

    //new instance
    public TableColumn(String name, Column.columnTypeEnum columnType, Class<T> genericType) {
        super(name, columnType, genericType);
    }

    @Override
    public void configureColumn(Column column) {
        if(column.nullable())
            this.setNullable();

        this.setCheckConstraint(column.check());

        this.setDefault(column.defaultValue());
    }

    public TableColumn setPrimary(){
        this.primary = true;
        return this;
    }

    public boolean isPrimary(){return this.primary;}

    public TableColumn setUnique(){
        this.unique = true;
        this.primary = false;
        this.nullable = false;
        return this;
    }

    public TableColumn setCheckConstraint(String expression){
        if(!TextUtils.isEmpty(expression))
            checkConstraint = expression;

        return this;
    }

    public String getCheckConstraint(){
        return checkConstraint;
    }

    public boolean isUnique(){return this.unique;}

    public TableColumn setNullable(){
        this.nullable = true;
        return this;
    }

    @SuppressWarnings("unchecked")
    public TableColumn setDefault(T value){
        this.defaultConstraint = value;
        this.value = value;
        return this;
    }

    public TableColumn setDefault(String value){
        if(!TextUtils.isEmpty(value)){
            Object o = getValueFromString(value);
            this.defaultConstraint = (T)o;
            this.value = (T)o;

        }
        return this;
    }

    protected static TableColumn addColumn(String name, Column.columnTypeEnum columnType, Object boundField) {
        return new TableColumn(name, columnType, getBoundFieldType(boundField));
    }

    private void defaultConstraint(StringBuilder sb){
        if(null != defaultConstraint){
            sb.append("DEFAULT");
            sb.append(" (");
            switch (getColumnType()){
                case INTEGER:
                case NUMERIC:
                case REAL:
                    //there is not a boolean data type in sqlite. convert to int
                    if(getDataTypeEnum() == Column.dataTypeEnum.BOOLEAN){
                        sb.append(String.valueOf((Boolean) defaultConstraint ? 1 : 0));
                    } else {
                        sb.append(String.valueOf(defaultConstraint));
                    }
                    break;
                case TEXT:
                case BLOB:
                    sb.append("\'");
                    sb.append(String.valueOf(defaultConstraint));
                    sb.append("\'");
                    break;
                default:
                    throw new UnsupportedOperationException();
            }

            sb.append(") ");
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName());
        sb.append(" ");
        sb.append(getColumnType().name());
        sb.append(" ");

        defaultConstraint(sb);

        if(!this.nullable)
            sb.append("NOT NULL");

        return sb.toString();
    }


}
