package com.springtrail.etilqs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.springtrail.etilqs.Builder.Column;
import com.springtrail.etilqs.Builder.DataSourceBuilder;
import com.springtrail.etilqs.Builder.TableBuilder;
import com.springtrail.etilqs.Builder.TableModel;
import com.springtrail.etilqs.Query.WhereBase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by kirk on 10/5/15.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BasicQuery  {

    String dbName = "test_" + Long.toString((new Date()).getTime()) + ".db";

    Integer dbVersion = 1;

    DataSourceBuilder dataSource;

    @Before
    public void setUp() throws Exception {
        Util.PurgeDatabases();
    }

    @Test
    public void test() {

        Context context = InstrumentationRegistry.getContext();
        dataSource = new DataSourceBuilder(context, dbName, dbVersion);

        class Cats extends TableModel {

            @Column(name="name", type = Column.columnTypeEnum.TEXT)
            public String name;

            @Column(name="color", type = Column.columnTypeEnum.TEXT)
            public String color;

            @Override
            public void configureTableModel(TableBuilder builder) {
                super.configureTableModel(builder);
                builder.getTableColumn("name").isUnique();
            }
        }

        dataSource.addBuilder(new Cats());

        dataSource.open();

        insert();

        update();

        insertBatch();

        delete();

        dataSource.getSelect("Cats").select()
                .where("_id", WhereBase.Operator.EQUAL, "2")
                .execute();

        dataSource.close();

    }

    private void insertBatch(){

        int id = 100;

        String expected = "turtle,green,\n" +
                "cow,brown,\n" +
                "toto,purple,\n" +
                "yeti,white,\n" +
                "hazmat,orange,\n";

        dataSource.getInsertBatch()
                .startBatch("Cats", new String[]{"_id", "name", "color"})
                .values(new String[]{Integer.toString(id++), "turtle", "green"})
                .values(new String[]{Integer.toString(id++), "cow", "brown"})
                .values(new String[]{Integer.toString(id++), "toto", "purple"})
                .values(new String[]{Integer.toString(id++), "yeti", "white"})
                .values(new String[]{Integer.toString(id++), "hazmat", "orange"})
                .executeBatch();

        Cursor cursor = dataSource.getSelect()
                .select("Cats", new String[]{"name", "color"})
                .where("_id >= 100", null)
                .execute();

        String actual = Util.getCursorText(cursor);

        assertThat(expected, is(actual));

    }


    private void update(){

        String expected = "kitty,black,\nspot,red,\njabs,white,\n";

        ContentValues update = new ContentValues();
        update.put("color", "red");

        dataSource.getUpdate().update("Cats", update)
                .where("name", WhereBase.Operator.EQUAL, "spot")
                .and()
                .where("color", WhereBase.Operator.EQUAL, "brown").execute();

        Cursor cursor_update = dataSource.getSelect()
                .select("Cats", new String[]{"name","color"}).execute();

        String actual = Util.getCursorText(cursor_update);

        assertThat(expected, is(actual));

    }

    private void insert(){

        int id = 10;

        String expected = "kitty,black,\nspot,brown,\njabs,white,\n";

        dataSource.getInsert().insert("Cats", setCats(id++, "kitty", "black")).execute();

        dataSource.getInsert().insert("Cats", setCats(id++, "spot", "brown")).execute();

        dataSource.getInsert().insert("Cats", setCats(id++, "jabs", "white")).execute();

        Cursor cursor_expected = dataSource.getSelect().select("Cats", new String[]{"name", "color"}).execute();

        String actual = Util.getCursorText(cursor_expected);

        assertThat(expected, is(actual));

    }

    public ContentValues setCats(int id, String name, String color){
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", id);
        contentValues.put("name", name);
        contentValues.put("color", color);

        return contentValues;

    }


    private void delete(){

        String expected = "spot,red,\n" +
                "jabs,white,\n";

        dataSource.getDelete().delete("Cats")
                .where("name", WhereBase.Operator.EQUAL, "kitty")
                .execute();

        Cursor cursor_delete = dataSource.getSelect().select("Cats", new String[]{"name", "color"})
                .where("_id", WhereBase.Operator.GREATER_THAN_EQUAL, "10")
                .and()
                .where("_id", WhereBase.Operator.LESS_THAN, "100")
                .execute();

        String actual = Util.getCursorText(cursor_delete);

        assertThat(expected, is(actual));

    }



}
