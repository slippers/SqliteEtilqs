package com.springtrail.etilqs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.springtrail.etilqs.Builder.Column;
import com.springtrail.etilqs.Builder.DataSourceBuilder;
import com.springtrail.etilqs.Builder.TableBuilder;
import com.springtrail.etilqs.Builder.TableConstraint.TableConstraintForeignKey;
import com.springtrail.etilqs.Builder.TableModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by kirk on 10/3/15.
 */
@RunWith(AndroidJUnit4.class)
public class ForeignKeys {

    String dbName = "test_" + Long.toString((new Date()).getTime()) + ".db";

    Integer dbVersion = 1;

    @Before
    public void setUp() throws Exception {
        Util.PurgeDatabases();
    }

    @Test
    public void TableBuilder() {

        Context context = InstrumentationRegistry.getContext();
        DataSourceBuilder dataSource = new DataSourceBuilder(context, dbName, dbVersion);

        test(dataSource);

    }

    private void test(DataSourceBuilder dataSource){

        String parent_expected ="CREATE TABLE Parent(\n" +
                "_id INTEGER ,\n" +
                "f_string_1 TEXT \n" +
                ",CONSTRAINT PK_primary PRIMARY KEY (_id) \n" +
                ")";

        String child_expected = "CREATE TABLE Child(\n" +
                "_id INTEGER ,\n" +
                "parent INTEGER \n" +
                ",CONSTRAINT FK_parent  FOREIGN KEY (parent) REFERENCES Parent(_id) ON DELETE CASCADE\n" +
                ",CONSTRAINT PK_primary PRIMARY KEY (_id) \n" +
                ")";

        //create several models
        class Parent extends TableModel {
            @Column(name="f_string_1", type = Column.columnTypeEnum.TEXT)
            public String f_string_1;
        }

        class Child extends TableModel{

            public static final String PARENT = "parent";
            @Column(name=PARENT, type = Column.columnTypeEnum.INTEGER)
            public Integer f_integer_primary;

            @Override
            public void configureTableModel(TableBuilder builder) {
                super.configureTableModel(builder);


                TableConstraintForeignKey tableConstraintForeignKey = new TableConstraintForeignKey(Parent.class.getSimpleName(), builder.getTableColumn(PARENT) );

                tableConstraintForeignKey.addRelatedColumn(Parent.ID)
                        .addAction(TableConstraintForeignKey.Trigger.ON_DELETE, TableConstraintForeignKey.Actions.CASCADE);

                builder.addTableConstraint(tableConstraintForeignKey);

            }

        }

        //add to builder
        dataSource.addBuilder(new Parent());

        dataSource.addBuilder(new Child());

        dataSource.open();

        SQLiteDatabase sqLiteDatabase = dataSource.getSqLiteDatabase(DataSource.OpenMode.WRITE);

        String parent_table = Util.getTableInfo(sqLiteDatabase, Parent.class.getSimpleName());

        String child_table = Util.getTableInfo(sqLiteDatabase, Child.class.getSimpleName());

        assertThat(parent_expected.compareTo(parent_table), is(0));

        assertThat(child_expected.compareTo(child_table), is(0));

        Util.finish(dataSource, sqLiteDatabase);
    }

}
