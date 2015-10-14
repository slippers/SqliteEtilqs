package com.springtrail.etilqs.Query;

import java.util.List;

/**
 * Created by kirk on 5/1/15.
 */
public abstract class WhereBase<T> {

    public enum Predicate {
        NONE, AND, OR
    }

    public enum Operator {
        EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_THAN_EQUAL, LESS_THAN, LESS_THAN_EQUAL, IN, NOT_IN, BETWEEN, IS, IS_NOT
    }

    private static final String EQUAL = "=";
    private static final String NOT_EQUAL = "!=";
    private static final String GREATER_THAN = ">";
    private static final String GREATER_THAN_EQUAL = ">=";
    private static final String LESS_THAN = "<";
    private static final String LESS_THAN_EQUAL = "<=";
    private static final String IN = "IN";
    private static final String NOT_IN = "NOT IN";
    private static final String BETWEEN = "BETWEEN";
    private static final String IS = "IS";
    private static final String IS_NOT = "IS NOT";

    //Here the enum is converted into the related operator value.
//    public String getOperator(Operator operator) {
//        try {
//            return (String)WhereBase.class.getDeclaredField(operator.name()).get(null);
//        } catch (NoSuchFieldException ex) {
//            return null;
//        } catch (IllegalAccessException ex){
//            return null;
//        }
//    }

    public String getOperator(Operator operator){
        switch (operator) {
            case EQUAL:
                return EQUAL;
            case NOT_EQUAL:
                return NOT_EQUAL;
            case GREATER_THAN:
                return GREATER_THAN;
            case GREATER_THAN_EQUAL:
                return GREATER_THAN_EQUAL;
            case LESS_THAN:
                return LESS_THAN;
            case LESS_THAN_EQUAL:
                return LESS_THAN_EQUAL;
            case IN:
                return IN;
            case NOT_IN:
                return NOT_IN;
            case BETWEEN:
                return BETWEEN;
            case IS:
                return IS;
            case IS_NOT:
                return IS_NOT;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public Operator operator = Operator.EQUAL;

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public Predicate predicate = Predicate.NONE;


    public abstract String getClause();

    public abstract  List<String> getSelectionArgs();

}
