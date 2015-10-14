package com.springtrail.etilqs.Builder;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by kirk on 7/21/15.
 */
public class ColumnType<T> {

    private String name;
    protected T value;
    public Class<?> genericType;
    private Column.dataTypeEnum dataType;
    private Column.columnTypeEnum columnType;
    private boolean isDirty = false;

    // Copy constructor
    public ColumnType(ColumnType<? extends T> columnType) {
        this.name = columnType.name;
        this.value = columnType.value;
        this.columnType = columnType.columnType;
        this.genericType = columnType.genericType;
        this.dataType = columnType.dataType;
    }

    //new instance
    public ColumnType(String name, Column.columnTypeEnum columnType, Class<T> genericType){
        this.name = name;
        this.columnType = columnType;
        this.genericType = genericType;
        this.dataType = Column.dataTypeEnum.valueOf(this.genericType.getSimpleName().toUpperCase());
    }

    public void configureColumn(Column column){}

    public Class<?> getGenericType() { return genericType; }

    public String getName() {return this.name; }

    public T getValue(){
        return value;
    }

    public void setValue(T value) {

//        if(dataType == dataTypeEnum.BOOLEAN ){
//            String s = "";
//            //so when is a boolean type dirty?
//            //starts out null so when it changes to non null then dirty
//        }
//
//        if(null == this.value) {
//            this.value = value;
//            this.isDirty = true;
//            return;
//        }
//
        //no change in value
//        if(null != this.value && this.value.equals(value))
//            return;

        this.value = value;
        this.isDirty = true;

    }

    public Object getValueFromString(String value){
        switch (this.dataType){
            case INTEGER:
                return Integer.parseInt(value);
            case STRING:
                return value;
            case LONG:
                return Long.parseLong(value);
            case SHORT:
                return Short.parseShort(value);
            case DOUBLE:
                return Double.parseDouble(value);
            case FLOAT:
                return Float.parseFloat(value);
            case BYTE:
                return Byte.parseByte(value);
            case BOOLEAN:
                if(value == null)
                    return 0;
                else
                    return Boolean.parseBoolean(value);
            default:
                throw new UnsupportedOperationException();
        }
    }

    public String getStringValue(){
        return  String.valueOf(value);
    }

    public Column.columnTypeEnum getColumnType(){ return columnType; }

    public Column.dataTypeEnum getDataTypeEnum(){ return dataType; }

    public boolean isDirty(){return isDirty;}

    public void updated(){
        isDirty = false;
    }

    public Object getValueTyped(){
        switch (this.dataType){
            case INTEGER:
                return (Integer)value;
            case STRING:
                return (String)value;
            case LONG:
                return (Long)value;
            case SHORT:
                return (Short)value;
            case DOUBLE:
                return (Double)value;
            case FLOAT:
                return (Float)value;
            case BYTE:
                return (Byte)value;
            case BOOLEAN:
                if(value == null)
                    return 0;
                else
                    return (Boolean)value ? 1 : 0;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public void setContentValues(ContentValues values){
        switch (this.dataType){
            case INTEGER:
                values.put(this.name, (Integer)value);
                break;
            case STRING:
                values.put(this.name, (String)value);
                break;
            case LONG:
                values.put(this.name, (Long)value);
                break;
            case SHORT:
                values.put(this.name, (Short)value);
                break;
            case DOUBLE:
                values.put(this.name, (Double)value);
                break;
            case FLOAT:
                values.put(this.name, (Float)value);
                break;
            case BYTE:
                values.put(this.name, (Byte)value);
                break;
            case BOOLEAN:
                if(value == null)
                    values.put(this.name, 0);
                else
                    values.put(this.name, (Boolean)value ? 1 : 0);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public Object getValueFromCursor(Cursor cursor){
        Integer index = cursor.getColumnIndex(this.name);
        switch (this.dataType){
            case INTEGER:
                return cursor.getInt(index);
            case STRING:
                return cursor.getString(index);
            case LONG:
                return cursor.getLong(index);
            case SHORT:
                return cursor.getShort(index);
            case DOUBLE:
                return cursor.getDouble(index);
            case FLOAT:
                return cursor.getFloat(index);
            case BYTE:
                return cursor.getBlob(index);
            case BOOLEAN:
                return cursor.getInt(index) > 0;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public String toString(){
        return this.name + " " + columnType.name();
    }

//    public static long getId(List<? extends ColumnType> columns){
//        return (Long)(columns.get(0)).getValue();
//    }

//    public static ColumnType getColumn(String name, List<? extends ColumnType> columns){
//        for(ColumnType<?> col : columns){
//            if(col.getName().equals(name)){ return col;}
//        }
//        throw new IllegalArgumentException("Column not found:" + name);
//    }

    public static Class<?> getBoundFieldType(Object boundField) {
        switch (Column.dataTypeEnum.valueOf(((Class) boundField).getSimpleName().toUpperCase())){
            case INTEGER:
                return Integer.class;
            case STRING:
                return String.class;
            case LONG:
                return Long.class;
            case SHORT:
                return Short.class;
            case DOUBLE:
                return Double.class;
            case FLOAT:
                return Float.class;
            case BYTE:
                return Byte.class;
            case BOOLEAN:
                return Boolean.class;
            default:
                throw new UnsupportedOperationException();
        }
    }

}
