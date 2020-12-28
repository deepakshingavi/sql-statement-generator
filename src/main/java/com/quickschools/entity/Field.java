package com.quickschools.entity;

import com.quickschools.SqlValidation;

import java.util.Objects;

public class Field {
    private final String name;
    private final Entity entity;
    private final boolean isPrimaryKey;

    public Field(String name, Entity entity) {
        this(name,entity,false);
    }

    public Field(String name, Entity entity,boolean isPrimaryKey) {
        this.name = name.toLowerCase();
        this.isPrimaryKey=isPrimaryKey;
        if(entity==null) {
            throw new SqlValidation("Field's entity cant be null or empty.");
        }
        this.entity = entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return name.equals(field.name) &&
                entity.getTableName().equals(field.entity.getTableName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, entity.getTableName());
    }

    public String getName() {
        return name;
    }

    public String getTableName() {
        return entity.getTableName();
    }

    public String getFieldAlias() {
        return entity.getTableName()+"."+getName();
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }
}
