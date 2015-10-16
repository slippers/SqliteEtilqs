package com.springtrail.etilqs.Query;


import android.database.sqlite.SQLiteDatabase;

import com.springtrail.etilqs.DataSource;

/**
 * Created by kirk on 9/22/15.
 */
public abstract class QueryBase<T> {

    DataSource dataSource;
    SQLiteDatabase sqLiteDatabase;
    DataSource.OpenMode openMode;

    //used by both table and view
    private String groupBy;
    private String having;
    private String orderBy;
    private String limit;

    protected WhereBaseCollection whereFilter = new WhereBaseCollection();

    public WhereBaseCollection getWhereFilter(){ return whereFilter;}

    public QueryBase(DataSource dataSource, DataSource.OpenMode openMode) {
        this.dataSource = dataSource;
        this.openMode = openMode;
    }

    public SQLiteDatabase getDatabase(){
        if(sqLiteDatabase == null)
            sqLiteDatabase = dataSource.getSqLiteDatabase(openMode);
        return sqLiteDatabase;
    }

    public T where(WhereBase whereClauseBase) {
        whereFilter.add(whereClauseBase);
        return (T)this;
    }

    public T where(String selection, String[] selectionArgs){
        WhereSelection where = new WhereSelection(selection, selectionArgs);
        whereFilter.add(where);
        return (T)this;
    }

    public T where(String column, WhereBase.Operator operator, String value){
        WhereColumn where = new WhereColumn();
        where.simple(column, operator, value);
        whereFilter.add(where);
        return (T)this;
    }

    public T and(){
        whereFilter.getLast().setPredicate(WhereBase.Predicate.AND);
        return (T)this;
    }

    public T or(){
        whereFilter.getLast().setPredicate(WhereBase.Predicate.OR);
        return (T)this;
    }

    public T between(String column, String value1, String value2){
        WhereColumn where = new WhereColumn();
        whereFilter.add(where);
        where.between(column, value1, value2);
        return (T)this;
    }

    public T isNull(String column){
        WhereColumn where = new WhereColumn();
        whereFilter.add(where);
        where.isNull(column);
        return (T)this;
    }

    public T isNotNull(String column){
        WhereColumn where = new WhereColumn();
        whereFilter.add(where);
        where.isNotNull(column);
        return (T)this;
    }

    public T in(String column, String[] set){
        WhereColumn where = new WhereColumn();
        where.in(column, set);
        whereFilter.add(where);
        return (T)this;
    }

    public T notIn(String column, String[] set){
        WhereColumn where = new WhereColumn();
        whereFilter.add(where);
        where.notIn(column, set);
        return (T)this;
    }

    public T like(String column, String like_clause){
        WhereColumn where = new WhereColumn();
        whereFilter.add(where);
        where.like(column, like_clause);
        return (T)this;
    }

    public T clear(){
        whereFilter.clear();
        return (T)this;
    }

    /*
     * @param having A filter declare which row groups to include in the cursor,
     *            if row grouping is being used, formatted as an SQL HAVING
     *            clause (excluding the HAVING itself). Passing null will cause
     *            all row groups to be included, and is required when row
     *            grouping is not being used.
    */
    public T having(String having){
        this.having = having;
        return (T)this;
    }

    public String getHaving(){return having;}

    /*
     * @param limit Limits the number of rows returned by the query,
     *            formatted as LIMIT clause. Passing null denotes no LIMIT clause.
     */
    public T limit(String limit){
        this.limit = limit;
        return (T)this;
    }

    public String getLimit(){return limit;}

    /*
     * @param groupBy A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     */
    public T groupBy(String groupBy){
        this.groupBy = groupBy;
        return (T)this;
    }

    public String getGroupBy(){return groupBy;}
    /*
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     */
    public T orderBy(String orderBy){
        this.orderBy = orderBy;
        return (T)this;
    }

    public String getOrderBy(){return orderBy;}

}
