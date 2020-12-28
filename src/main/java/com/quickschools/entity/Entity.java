package com.quickschools.entity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Entity {

    private final String tableName;
    private final List<Field> fields;

    public Entity(String tableName, List<String> fields, String primaryKey) {
        this.tableName = tableName.toLowerCase();
        this.fields = Optional.ofNullable(fields)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .map(name -> {
                    boolean isPrimaryKey = name.equalsIgnoreCase(primaryKey);
                    return new Field(name, this,isPrimaryKey);
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return tableName.equals(entity.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName);
    }

    public List<Field> getFields() {
        return fields;
    }

    public String getTableName() {
        return tableName;
    }

    public Field getPrimaryKey(){
        return fields.stream().filter(Field::isPrimaryKey).findFirst().orElse(null);
    }
}
