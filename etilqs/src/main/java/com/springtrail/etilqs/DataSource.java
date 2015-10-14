package com.springtrail.etilqs;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.springtrail.etilqs.Query.QueryDelete;
import com.springtrail.etilqs.Query.QueryInsert;
import com.springtrail.etilqs.Query.QueryInsertBatch;
import com.springtrail.etilqs.Query.QuerySelect;
import com.springtrail.etilqs.Query.QueryUpdate;

/**
 * Created by kirk on 9/22/15.
 */
public class DataSource {

    private SQLiteDatabase sqLiteDatabase;
    private SQLiteOpenHelper sqLiteOpenHelper;

    Context mContext;
    String mDatabaseName;
    Integer mVersion;

    public enum OpenMode {
        READ, WRITE
    }

    public QuerySelect getSelect(){
        return new QuerySelect(this);
    }

    public QueryInsert getInsert(){
        return new QueryInsert(this);
    }

    public QueryInsertBatch getInsertBatch(){
        return new QueryInsertBatch(this);
    }

    public QueryDelete getDelete(){
        return new QueryDelete(this);
    }

    public QueryUpdate getUpdate(){
        return new QueryUpdate(this);
    }

    public DataSource(Context context, String databaseName,
                      Integer version) {
        mContext = context;
        mDatabaseName = databaseName;
        mVersion = version;
    }

    public SQLiteOpenHelper getSQLiteOpenHelper(){
        return sqLiteOpenHelper;
    }

    public SQLiteDatabase getSqLiteDatabase(OpenMode openMode){
        if(openMode == OpenMode.READ){
            return sqLiteOpenHelper.getReadableDatabase();
        }
        else
            return sqLiteOpenHelper.getWritableDatabase();
    }

    public void open() throws SQLException {

        final DataSource dataSource = this;

        sqLiteOpenHelper = new SQLiteOpenHelper(mContext, mDatabaseName, null, mVersion) {

            @Override
            public void onCreate(SQLiteDatabase db) {
                dataSource.onCreateFirstTime(db);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                dataSource.onUpgrade(db, oldVersion, newVersion);
            }

            @Override
            public void onConfigure(SQLiteDatabase db) {
                super.onConfigure(db);
                DataSource.setForeignKeyConstraintsEnabled(db, true);
            }

            @Override
            public void onOpen(SQLiteDatabase db) {
                super.onOpen(db);
                sqLiteDatabase = db;
            }
        };

    }

    public void close() {
        sqLiteOpenHelper.close();
    }

    //DatabaseHelper will call into this
    public void onCreateFirstTime(SQLiteDatabase database){

    }

    //DatabaseHelper will call into this
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){

    }

    public static void setForeignKeyConstraintsEnabled(SQLiteDatabase db, Boolean enabled) {
        if (db.isReadOnly()) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db, enabled);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db, enabled);
        }
    }

    private static void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db, Boolean enabled) {
        if(enabled)
            db.execSQL("PRAGMA foreign_keys=ON;");
        else
            db.execSQL("PRAGMA foreign_keys=OFF;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db, Boolean enabled) {
        db.setForeignKeyConstraintsEnabled(enabled);
    }
}
