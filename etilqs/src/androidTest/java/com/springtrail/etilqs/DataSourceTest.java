package com.springtrail.etilqs;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.springtrail.etilqs.Builder.Column;
import com.springtrail.etilqs.Builder.DataSourceBuilder;
import com.springtrail.etilqs.Builder.TableModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by kirk on 9/28/15.
 */
@RunWith(AndroidJUnit4.class)
public class DataSourceTest {

    String dbName = "test_" + Long.toString((new Date()).getTime()) + ".db";

    Integer dbVersion = 1;

    class TestModelOne extends TableModel {

        public static final String NAME = "name";
        public static final String COLOR = "color";

        //data field
        @Column(name=NAME)
        public String name;

        //data field
        @Column(name=COLOR)
        public String color;

    }

    @Before
    public void setUp() throws Exception {
        Util.PurgeDatabases();
    }


    @Test
    public void DatabaseTest() {
        createDatabase();
        upgradeDatabase();
        setForeignKeyConstraintsEnabled();
    }

    private void setForeignKeyConstraintsEnabled(){

        Context context = InstrumentationRegistry.getContext();

        DataSource dataSource = new DataSource(context, dbName, dbVersion);

        dataSource.open();

        SQLiteDatabase sqLiteDatabase = dataSource.getSqLiteDatabase(DataSource.OpenMode.WRITE);

        assertThat(getForeign_keyPragma(sqLiteDatabase), is(1));

        DataSource.setForeignKeyConstraintsEnabled(sqLiteDatabase, false);

        assertThat(getForeign_keyPragma(sqLiteDatabase), is(0));

        dataSource.close();


    }

    private Integer getForeign_keyPragma(SQLiteDatabase database){
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("PRAGMA foreign_keys", null);
            cursor.moveToFirst();
            return cursor.getInt(0);
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    private void createDatabase() {

        Context context = InstrumentationRegistry.getContext();

        DataSourceBuilder dataSource = new DataSourceBuilder(context, dbName, dbVersion);

        dataSource.addBuilder(new TestModelOne());

        dataSource.open();

        SQLiteDatabase sqLiteDatabase = dataSource.getSqLiteDatabase(DataSource.OpenMode.WRITE);

        assertThat(sqLiteDatabase.isOpen(), is(true));

        dataSource.close();

        assertThat(sqLiteDatabase.isOpen(), is(false));
    }


    private void upgradeDatabase() {

        dbVersion++;

        Context context = InstrumentationRegistry.getContext();

        //cause the upgrade to happen by incrementing the version
        DataSourceBuilder dataSource = new DataSourceBuilder(context, dbName, dbVersion);

        dataSource.addBuilder(new TestModelOne());

        dataSource.open();

        SQLiteDatabase sqLiteDatabase = dataSource.getSqLiteDatabase(DataSource.OpenMode.WRITE);

        assertThat(sqLiteDatabase.isOpen(), is(true));

        dataSource.close();

        assertThat(sqLiteDatabase.isOpen(), is(false));
    }


}