package com.quickschools.schema;

import com.quickschools.entity.*;

import java.util.*;

public class EntityLookup {

    private final Map<String, Entity> tableLookup;
    private final Map<String,Field> fieldLookup;
    private final Map<String,Join> joinLookup;

    private static class EntityLookupInner{
        private final static EntityLookup entityLookup = new EntityLookup();
    }

    private EntityLookup(){
        this.tableLookup = new HashMap<>();
        this.fieldLookup = new HashMap<>();
        this.joinLookup = new HashMap<>();
        initERModel();
    }

    private void initERModel() {
        createTable("Student",Arrays.asList("ID","Name","Gender"),"ID");
        createTable("Grade",Arrays.asList("ID","Name"),"ID");
        Entity entity = createTable("StudentToGrade",Arrays.asList("studentID","gradeID"),null);
        createJoin(entity);
    }

    private void createJoin(Entity entity) {
        final Field studentPrimaryKey = tableLookup.get("Student".toLowerCase()).getPrimaryKey();
        final Field gradePrimaryKey = tableLookup.get("Grade".toLowerCase()).getPrimaryKey();
        final Field studentForeignKey =fieldLookup.get(entity.getTableName()+"."+"StudentId".toLowerCase());
        final Field gradeForeignKey = fieldLookup.get(entity.getTableName()+"."+"GradeId".toLowerCase());
        Map<Field,Field> fieldMapping = new HashMap<>();
        fieldMapping.put(studentForeignKey, studentPrimaryKey);
        fieldMapping.put(gradeForeignKey, gradePrimaryKey);

        Join studentToGrade =  new Join(entity,Collections.unmodifiableMap(fieldMapping));
        joinLookup.put("StudentToGrade".toLowerCase(),studentToGrade);
    }

    private Entity createTable(String tableName,List<String> fieldNames,String primaryKey) {
        Entity entity = new Entity(tableName,fieldNames,primaryKey);
        tableLookup.put(entity.getTableName(),entity);
        entity.getFields().forEach(field -> fieldLookup.put(field.getFieldAlias(),field));
        return entity;
    }

    public static EntityLookup getInstance(){
        return EntityLookupInner.entityLookup;
    }

    public Entity getEntity(String entityName){
        return tableLookup.get(entityName.toLowerCase());
    }

    public Field getField(String fieldAlias){
        return fieldLookup.get(fieldAlias.toLowerCase());
    }

    public Join getJoin(String joinName){
        return joinLookup.get(joinName.toLowerCase());
    }

}
