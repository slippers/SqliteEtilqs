package com.springtrail.etilqs.Builder.TableConstraint;

import com.springtrail.etilqs.Builder.TableColumn;

/**
 * Created by kirk on 9/24/15.
 */

/*
* From: sqlite.org
* https://www.sqlite.org/lang_createtable.html#ckconst
* A CHECK constraint may be attached to a column definition or specified as a table constraint.
* In practice it makes no difference. Each time a new row is inserted into the table or an existing row is updated,
* the expression associated with each CHECK constraint is evaluated and cast to a NUMERIC value in the same way as a CAST expression.
* If the result is zero (integer value 0 or real value 0.0), then a constraint violation has occurred.
* If the CHECK expression evaluates to NULL, or any other non-zero value, it is not a constraint violation.
* The expression of a CHECK constraint may not contain a subquery.
*
*
* */
public class TableConstraintCheck extends TableConstraint {

    String mExpression;

    public TableConstraintCheck(TableColumn tableColumn) {
        super(tableColumn);
        mExpression = tableColumn.getCheckConstraint();
    }

    @Override
    public TableConstraint addColumn(String name) {
        throw new UnsupportedOperationException("There can only be one column defined in a check constraint.");
    }

    @Override
    public String getPrefix() {
        return "CK";
    }

    @Override
    public String toString() {
        String result = ",CONSTRAINT " + getName() + " CHECK ("+ mExpression +")";
        return result;
    }
}
