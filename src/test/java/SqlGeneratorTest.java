import com.quickschools.SqlValidation;
import com.quickschools.processor.*;
import com.quickschools.entity.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.quickschools.schema.EntityLookup;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SqlGeneratorTest {

    @Test(expected = SqlValidation.class)
    public void testZeroFields(){
        new SqlGenerator().generate(null, null);
    }

    @Test(expected = SqlValidation.class)
    public void testIntraTableFieldsWithoutJoin(){
        Entity gradeEntity = new Entity("grade",null, null);
        List<Field> fields = Collections.singletonList(
                new Field("grade", gradeEntity));
        new SqlGenerator().generate(fields, null);
    }

    @Test(expected = SqlValidation.class)
    public void testIncorrectFieldNames(){
        Entity gradeEntity = new Entity("grade1",null, null);
        List<Field> fields = Collections.singletonList(
                new Field("grade1", gradeEntity));

        new SqlGenerator().generate(fields, null);
    }


    @Test
    public void testStudentTableSQl(){
        Entity studentEntity = new Entity("student",null, null);
        List<Field> fields = Arrays.asList(
                new Field("id",studentEntity),
                new Field("name",studentEntity),
                new Field("gender",studentEntity));
        final String sql = new SqlGenerator().generate(fields, null);
        assertEquals( "select id, name, gender from student",sql);
    }

    @Test
    public void testGradeTableSQl(){
        Entity gradeEntity = new Entity("grade",null, null);
        List<Field> fields = Arrays.asList(
                new Field("id",gradeEntity),
                new Field("name",gradeEntity));
        final String sql = new SqlGenerator().generate(fields, null);
        assertEquals( "select id, name from grade",sql);
    }

    @Test
    public void testStudentToGradeSQl(){
        Entity gradeEntity = new Entity("grade",null, null);
        List<Field> fields = Arrays.asList(
                new Field("id",gradeEntity),
                new Field("name",gradeEntity));

        Join StudentToGrade = EntityLookup.getInstance().getJoin("StudentToGrade");

        List<Join> joins = Collections.singletonList(StudentToGrade);
        final String sql = new SqlGenerator().generate(fields, joins);
        assertEquals( "select id, name " +
                "from studenttograde " +
                "inner join grade on studenttograde.gradeid == grade.id " +
                "inner join student on studenttograde.studentid == student.id",sql);
    }

}