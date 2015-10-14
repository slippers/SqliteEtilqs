package com.springtrail.etilqs.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirk on 9/25/15.
 */
public class WhereBaseCollection {
    private List<WhereBase> whereBases = new ArrayList<>();

    public void add(WhereBase filter){
        whereBases.add(filter);
    }

    public void clear(){
        whereBases.clear();
    }

    public int size() { return whereBases.size(); }

    public WhereBase getLast(){
        if(whereBases.size() > 0)
            return whereBases.get(whereBases.size() -1);
        else
            return null;
    }

    public String[] getSelectionArgs(){

        if(whereBases.size() == 0)
            return null;

        List<String>  selectionArgs = new ArrayList<String>();

        for(WhereBase filter : whereBases){
            if(filter.getSelectionArgs() != null)
                selectionArgs.addAll(filter.getSelectionArgs());
        }

        if(selectionArgs.size() > 0)
            return selectionArgs.toArray(new String[selectionArgs.size()]);
        else
            return null;
    }

    public String getSelection(){

        if(whereBases.size() == 0)
            return null;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < whereBases.size(); i++) {

            sb.append(whereBases.get(i).getClause());

            if(i == whereBases.size() - 1)
                continue;

            if(whereBases.get(i).predicate != WhereBase.Predicate.NONE){
                sb.append(" ")
                        .append(whereBases.get(i).predicate.name().toUpperCase())
                        .append(" ");
            } else {
                sb.append(" ")
                        .append(WhereBase.Predicate.OR.name().toUpperCase())
                        .append(" ");
            }
        }

        return sb.toString();
    }

}
