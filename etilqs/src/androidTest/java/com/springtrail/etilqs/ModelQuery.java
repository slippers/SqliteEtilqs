package com.springtrail.etilqs;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.text.TextUtils;

import com.springtrail.etilqs.Builder.Column;
import com.springtrail.etilqs.Builder.DataSourceBuilder;
import com.springtrail.etilqs.Builder.Query.QueryModelDelete;
import com.springtrail.etilqs.Builder.Query.QueryModelUpdate;
import com.springtrail.etilqs.Builder.TableBuilder;
import com.springtrail.etilqs.Builder.TableModel;
import com.springtrail.etilqs.Query.WhereBase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by kirk on 10/8/15.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ModelQuery {

    String dbName = "test_" + Long.toString((new Date()).getTime()) + ".db";

    Integer dbVersion = 1;

    DataSourceBuilder dataSource;

    class Cats extends TableModel {

        public static final String UUID = "uuid";

        @Column(name=UUID, type = Column.columnTypeEnum.TEXT, nullable = false)
        String uuid = "";

        @Column(name="name", type = Column.columnTypeEnum.TEXT)
        public String name;

        @Column(name="color", type = Column.columnTypeEnum.TEXT)
        public String color;

        @Override
        public void configureTableModel(TableBuilder builder) {
            super.configureTableModel(builder);
            builder.getTableColumn(UUID).setUnique();
            builder.getTableColumn("name").isUnique();
        }

        @Override
        public void update(QueryModelUpdate queryModelUpdate) {
            if(!TextUtils.isEmpty(uuid))
                queryModelUpdate.where(UUID, WhereBase.Operator.EQUAL, uuid);
        }

        @Override
        public void delete(QueryModelDelete queryModelDelete) {
            if(!TextUtils.isEmpty(uuid))
                queryModelDelete.where(UUID, WhereBase.Operator.EQUAL, uuid);
        }

        @Override
        public void insert() {
            this.getColumns().get(UUID).setValue(java.util.UUID.randomUUID().toString());
        }
    }

    @Before
    public void setUp() throws Exception {
        Util.PurgeDatabases();
    }

    @Test
    public void test() {

        Context context = InstrumentationRegistry.getContext();
        dataSource = new DataSourceBuilder(context, dbName, dbVersion);

        dataSource.addBuilder(new Cats());

        dataSource.open();

        insert();

        select();

        delete();

        update();

        dataSource.close();

    }

    private void insert(){
        Long row;

        Cats dirty = new Cats();

        dirty.color = "red";
        dirty.name = "flash";

        row = dataSource.getInsert(dirty).execute();

        assertThat(row, is(1L));

        row = dataSource.getInsert(dirty).execute();

        assertThat(row, is(2L));

        row = dataSource.getInsert(dirty).execute();

        assertThat(row, is(3L));


    }

    private void select() {

        Cursor cursor = dataSource.getSelect("Cats").select(new String[]{"_id","name","color"}).execute();

        String actual = Util.getCursorText(cursor);

        String expected = "1,flash,red,\n2,flash,red,\n3,flash,red,\n";

        assertThat(actual, is(expected));

    }

    private void delete(){
        Integer rows;

        Cats felix = new Cats();
        felix.name = "felix";
        felix.color = "astro dynamic blue";

        Long row = dataSource.getInsert(felix).execute();

        assertThat(row, notNullValue());

        Cats dead = new Cats();

        dead.setId(4);

        rows = dataSource.getDelete(dead).execute();

    }

    private void update(){
        Cats toonces = new Cats();
        toonces.name = "toonces";
        toonces.color = "orange with red all over.";

        Long row = dataSource.getInsert(toonces).execute();

        toonces.setId(Util.safeLongToInt(row));
        toonces.color = "gray and black stripes";
        dataSource.getUpdate(toonces).execute();

        Cursor cursor = dataSource.getSelect(toonces).execute();

        String actual = Util.getCursorText(cursor);
    }

}
