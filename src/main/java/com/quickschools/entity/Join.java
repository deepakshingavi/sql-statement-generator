package com.quickschools.entity;

import java.util.Collections;
import java.util.Map;

public class Join {

    private final Entity joinEntity;
    private final Map<Field, Field> fieldMapping;


    public Join(Entity joinEntity, Map<Field,Field> fieldMapping) {
        this.joinEntity=joinEntity;
        this.fieldMapping= Collections.unmodifiableMap(fieldMapping);
    }

    public Entity getJoinEntity() {
        return joinEntity;
    }

    public Map<Field, Field> getFieldMapping() {
        return fieldMapping;
    }
}
