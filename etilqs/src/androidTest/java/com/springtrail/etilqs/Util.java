package com.springtrail.etilqs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by kirk on 10/1/15.
 */
public class Util {

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }


    public static String getTableInfo(SQLiteDatabase database, String table){
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select sql from sqlite_master where tbl_name = ?", new String[]{table} );
            cursor.moveToFirst();
            return cursor.getString(0);
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public static void PurgeDatabases() {
        Context context = InstrumentationRegistry.getContext();

        String[] list = context.databaseList();

        for(String db : context.databaseList()){

            File file = context.getDatabasePath(db);

            file.delete();
        }
    }

    public static void finish(DataSource dataSource, SQLiteDatabase sqLiteDatabase){
        assertThat(sqLiteDatabase.isOpen(), is(true));

        dataSource.close();

        assertThat(sqLiteDatabase.isOpen(), is(false));
    }

    public static String getCursorText(Cursor cursor){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                String value;

                for(String column : cursor.getColumnNames()){
                    int index = cursor.getColumnIndex(column);

                    if(cursor.getType(index) == Cursor.FIELD_TYPE_STRING){
                        value = cursor.getString(index);
                    }

                    if(cursor.getType(index) == Cursor.FIELD_TYPE_INTEGER){
                        value = Integer.toString(cursor.getInt(index));
                    }

                    if(cursor.getType(index) == Cursor.FIELD_TYPE_FLOAT){
                        value = Float.toString(cursor.getFloat(index));
                    }

                    stringBuilder.append(cursor.getString(index)).append(",");
                }

                stringBuilder.append(System.getProperty("line.separator"));

                cursor.moveToNext();
            }


        } finally {
            if (cursor != null) cursor.close();
        }

        return stringBuilder.toString();
    }


}
