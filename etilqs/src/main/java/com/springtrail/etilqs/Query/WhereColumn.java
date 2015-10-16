package com.springtrail.etilqs.Query;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kirk on 10/6/15.
 */
public class WhereColumn extends WhereBase<WhereColumn> {

    public void setColumn(String column) {
        if(TextUtils.isEmpty(column))
            throw new IllegalArgumentException("column arguments invalid.");
        this.column = column;
    }

    private String column;

    private Operator operator;

    private List<String> args;

    public WhereColumn() {
    }

    public void simple(String column, WhereBase.Operator operator, String value){

        setColumn(column);

        if(TextUtils.isEmpty(value))
            throw new IllegalArgumentException("value arguments invalid.");

        this.args = new ArrayList<>();
        this.args.add(value);

        if(operator == null)
            throw new IllegalArgumentException("operator arguments invalid.");

        this.operator = operator;
    }

    public void between(String column, String value1, String value2){
        setColumn(column);
        operator = Operator.BETWEEN;
        args.add(value1);
        args.add(value2);
    }

    public void isNull(String column){
        setColumn(column);
        operator = Operator.IS;
    }

    public void isNotNull(String column){
        setColumn(column);
        operator = Operator.IS_NOT;
    }

    public void in(String column, String[] set){
        List<String> newList = Arrays.asList(set);
        args.addAll(newList);
        setColumn(column);
        operator = Operator.IN;
    }

    public void notIn(String column, String[] set){
        List<String> newList = Arrays.asList(set);
        args.addAll(newList);
        setColumn(column);
        operator = Operator.NOT_IN;
    }

    String like_clause;

    //like_clause = "%?"
    public void like(String column, String like_clause){
        this.like_clause = like_clause;
        operator = Operator.LIKE;
        setColumn(column);
    }

    @Override
    public String getClause() {
        switch (operator) {
            case EQUAL:
            case NOT_EQUAL:
            case GREATER_THAN:
            case GREATER_THAN_EQUAL:
            case LESS_THAN:
            case LESS_THAN_EQUAL:
                return column + " " + getOperator(operator) + " ?";
            case IN:
                return column + " " + getOperator(operator) + " ("+ WhereColumn.questionsFromSet(args) +")";
            case NOT_IN:
                return column + " " + getOperator(operator) + " ("+ WhereColumn.questionsFromSet(args) +")";
            case BETWEEN:
                return column + " " + getOperator(operator) + " ? AND ?";
            case IS:
                return column + " " + getOperator(operator) + " NULL";
            case IS_NOT:
                return column + " " + getOperator(operator) + " NOT NULL";
            case LIKE:
                return column + " " + getOperator(operator) + " \"" + like_clause + "\"";
            default:
                throw new UnsupportedOperationException();
        }
    }

    private static String questionsFromSet(List<String> set){
        StringBuilder sb = new StringBuilder();
        for(String value : set){
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public List<String> getSelectionArgs() {
        return args;
    }

}
