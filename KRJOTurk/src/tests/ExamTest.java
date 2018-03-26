package tests;

/*import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;*/
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import service.Exam;

import static org.junit.Assert.*;

public class ExamTest {

    Exam exam;

   @Before
    public void  init() {
       exam = new Exam("name", 1, "lu");
    }

    @Test
    public void setName() {
       exam.setName("newName");
       assertTrue("Test setName", exam.getName().equals("newName"));
    }

    @Test
    public void setId() {
        exam.setId(2);
        assertTrue("Test setId", exam.getId() == (2));
    }

    @Test
    public void setLu() {
        exam.setLu("newLu");
        assertTrue("Test setLu", exam.getLu().equals("newLu"));
    }

    @Test
    public void toString1(){
       String name = exam.toString();

       assertTrue("Test toString", name.equals(exam.getName()));
    }

}
