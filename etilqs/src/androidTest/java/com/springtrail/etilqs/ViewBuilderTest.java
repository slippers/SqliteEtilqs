package com.springtrail.etilqs;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.springtrail.etilqs.Builder.Column;
import com.springtrail.etilqs.Builder.DataSourceBuilder;
import com.springtrail.etilqs.Builder.TableBuilder;
import com.springtrail.etilqs.Builder.TableModel;
import com.springtrail.etilqs.Builder.ViewBuilder;
import com.springtrail.etilqs.Builder.ViewModel;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Created by kirk on 10/16/15.
 */
public class ViewBuilderTest {
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

            @Column(name = "name", type = Column.columnTypeEnum.TEXT)
            public String name;

            @Column(name = "color", type = Column.columnTypeEnum.TEXT)
            public String color;

            @Override
            public void configureTableModel(TableBuilder builder) {
                super.configureTableModel(builder);
                builder.getTableColumn("name").isUnique();
            }
        }


        class CatView extends ViewModel {

            @Column(name="count", type = Column.columnTypeEnum.TEXT, expression = "count(*)")
            public String count;

            @Column(name="name", type = Column.columnTypeEnum.TEXT)
            public String name;

            @Column(name="color", type = Column.columnTypeEnum.TEXT)
            public String color;

            @Override
            public void configureTableModel(ViewBuilder builder) {
                super.configureTableModel(builder);
                builder.setFrom(Cats.class.getSimpleName());
            }
        }

        dataSource.addBuilder(new Cats());

        dataSource.addBuilder(new CatView());

        dataSource.open();

        dataSource.getInsertBatch()
                .startBatch("Cats", new String[]{"name", "color"})
                .values(new String[]{"turtle 1", "green"})
                .values(new String[]{"turtle 2", "green"})
                .values(new String[]{"turtle 3", "green"})
                .values(new String[]{"turtle 4", "green"})
                .values(new String[]{"cow", "brown"})
                .values(new String[]{"toto", "purple"})
                .values(new String[]{"yeti", "white"})
                .values(new String[]{"hazmat", "orange"})
                .executeBatch();

        String actual = Util.getCursorText(dataSource
                        .getSelect()
                        .select(Cats.class.getSimpleName(), null)
                        .like("color", "%n")
                        .execute()
        );

        String view_actual = Util.getCursorText(dataSource
                        .getSelect()
                        .select(CatView.class.getSimpleName(), null)
                        .execute()
        );


        dataSource.close();

    }

}
