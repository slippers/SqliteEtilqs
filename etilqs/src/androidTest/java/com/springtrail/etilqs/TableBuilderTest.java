package com.springtrail.etilqs;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.springtrail.etilqs.Builder.Column;
import com.springtrail.etilqs.Builder.DataSourceBuilder;
import com.springtrail.etilqs.Builder.TableBuilder;
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
public class TableBuilderTest {

    String dbName = "test_" + Long.toString((new Date()).getTime()) + ".db";

    Integer dbVersion = 1;

    DataSourceBuilder dataSource;

    @Before
    public void setUp() throws Exception {
        Util.PurgeDatabases();
    }

    @Test
    public void TableBuilder() {

        Context context = InstrumentationRegistry.getContext();
        dataSource = new DataSourceBuilder(context, dbName, dbVersion);

        test();

    }

    private void test(){

        //copied and pasted from
        String expected_table = "CREATE TABLE DataTypeTable(\n" +
                "_id INTEGER ,\n" +
                "f_boolean NUMERIC ,\n" +
                "f_integer_1 INTEGER ,\n" +
                "f_integer_2 INTEGER DEFAULT (5) ,\n" +
                "f_integer_3 INTEGER ,\n" +
                "f_integer_4 INTEGER ,\n" +
                "f_integer_5 INTEGER ,\n" +
                "f_integer_check INTEGER ,\n" +
                "f_integer_primary INTEGER ,\n" +
                "f_integer_unique INTEGER NOT NULL,\n" +
                "f_long_1 NUMERIC ,\n" +
                "f_long_2 NUMERIC DEFAULT (5) ,\n" +
                "f_long_3 NUMERIC ,\n" +
                "f_long_4 NUMERIC ,\n" +
                "f_string_1 TEXT ,\n" +
                "f_string_2 TEXT DEFAULT ('test') ,\n" +
                "f_string_3 TEXT ,\n" +
                "f_string_4 TEXT \n" +
                ",CONSTRAINT PK_primary PRIMARY KEY (_id) \n" +
                ",CONSTRAINT CK_f_integer_5 CHECK (f_integer_5 > 0)\n" +
                ",CONSTRAINT CK_f_integer_check CHECK (f_integer_check > 0)\n" +
                ",CONSTRAINT UN_f_integer_unique UNIQUE (f_integer_unique) \n" +
                ")";

        class DataTypeTable extends TableModel {

            @Column(name="f_integer_primary", type = Column.columnTypeEnum.INTEGER)
            public Integer f_integer_primary;

            @Column(name="f_integer_unique", type = Column.columnTypeEnum.INTEGER)
            public Integer f_integer_unique;

            @Column(name="f_integer_check", type = Column.columnTypeEnum.INTEGER)
            public Integer f_integer_check;

            @Column(name="f_integer_1", type = Column.columnTypeEnum.INTEGER)
            public Integer f_integer_1;

            @Column(name="f_integer_2", type = Column.columnTypeEnum.INTEGER, defaultValue = "5")
            public Integer f_integer_2;

            @Column(name="f_integer_3", type = Column.columnTypeEnum.INTEGER, nullable = false)
            public Integer f_integer_3;

            @Column(name="f_integer_4", type = Column.columnTypeEnum.INTEGER, nullable = true)
            public Integer f_integer_4;

            @Column(name="f_integer_5", type = Column.columnTypeEnum.INTEGER, check = "f_integer_5 > 0")
            public Integer f_integer_5;

            @Column(name="f_string_1", type = Column.columnTypeEnum.TEXT)
            public String f_string_1;

            @Column(name="f_string_2", type = Column.columnTypeEnum.TEXT, defaultValue = "test")
            public String f_string_2;

            @Column(name="f_string_3", type = Column.columnTypeEnum.TEXT, nullable = false)
            public String f_string_3;

            @Column(name="f_string_4", type = Column.columnTypeEnum.TEXT, nullable = true)
            public String f_string_4;

            @Column(name="f_long_1", type = Column.columnTypeEnum.NUMERIC)
            public Long f_long_1;

            @Column(name="f_long_2", type = Column.columnTypeEnum.NUMERIC, defaultValue = "5")
            public Long f_long_2;

            @Column(name="f_long_3", type = Column.columnTypeEnum.NUMERIC, nullable = false)
            public Long f_long_3;

            @Column(name="f_long_4", type = Column.columnTypeEnum.NUMERIC, nullable = true)
            public Long f_long_4;

            @Column(name="f_boolean", type = Column.columnTypeEnum.NUMERIC)
            public Boolean f_boolean;

            @Override
            public void configureTableModel(TableBuilder builder) {
                super.configureTableModel(builder);

                builder.getTableColumn("f_integer_unique").setUnique();

                builder.getTableColumn("f_integer_check").setCheckConstraint("f_integer_check > 0");

            }
        }

        dataSource.addBuilder(new DataTypeTable());

        dataSource.open();

        SQLiteDatabase sqLiteDatabase = dataSource.getSqLiteDatabase(DataSource.OpenMode.WRITE);

        String new_table = Util.getTableInfo(sqLiteDatabase, DataTypeTable.class.getSimpleName());

        assertThat(expected_table.compareTo(new_table), is(0));

        Util.finish(dataSource, sqLiteDatabase);
    }


}
