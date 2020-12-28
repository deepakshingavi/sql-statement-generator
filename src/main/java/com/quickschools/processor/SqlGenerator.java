package com.quickschools.processor;

import com.quickschools.SqlValidation;
import com.quickschools.entity.Field;
import com.quickschools.entity.Join;
import com.quickschools.schema.EntityLookup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SqlGenerator {

    public String generate(List<Field> fields, List<Join> joins) {

        if(fields == null || fields.isEmpty()) {
            throw new SqlValidation("Fields cannot be empty!!!");
        }

        final EntityLookup lookup = EntityLookup.getInstance();

        List<Field> fieldsWithInfo = fields.stream()
                .map(field -> lookup.getField(field.getTableName() +"."+field.getName().toLowerCase()))
                .collect(Collectors.toList());
        Set<String> tableNames;
        try {
            tableNames = fieldsWithInfo.stream().map(Field::getTableName).collect(Collectors.toSet());
        } catch (NullPointerException npex){
            throw new SqlValidation("Incorrect table or field names.");
        }

        int noOfTables = tableNames.size();
        int noOfJoins = joins==null ? 0 : joins.size();

        if(noOfTables > 1 && noOfTables != (noOfJoins + 1)) {
            throw new SqlValidation("Insufficient joins provided."+noOfTables+" should have at least "+(noOfTables-1));
        }

        String sql;
        String selectClause = fieldsWithInfo.stream().map(Field::getName).collect(Collectors.joining(", "));
        if(joins == null || joins.isEmpty()) {
            sql = "select " +
                    selectClause +
                    " from " +
                    String.join(", ", tableNames);
        } else {

            Set<String> joinableTables = joins.stream().flatMap(join -> join.getFieldMapping().values().stream().map(Field::getTableName)).collect(Collectors.toSet());
            if(!joinableTables.containsAll(tableNames)) {
                Set<String> missingTables = new HashSet<>(joinableTables);
                missingTables.removeAll(tableNames);
                throw new SqlValidation("Insufficient joins provided.[ "+missingTables+"] are the tables with missing joins.");
            }

            sql = "select " +
                    selectClause +
                    " from " +
            joins.stream().map(join -> join.getJoinEntity().getTableName() + " inner join " +
            join.getFieldMapping().keySet().stream().map( field -> {
                Field primeField = join.getFieldMapping().get(field);
                String tableName = primeField.getTableName();
                return tableName + " on " + field.getFieldAlias() + " == " + primeField.getFieldAlias();
            }).collect(Collectors.joining(" inner join "))).collect(Collectors.joining(" ,"));;
        }

        return sql;
    }
}
