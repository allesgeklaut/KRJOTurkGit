package tests;

//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import service.LearningCategory;

import static org.junit.Assert.*;

public class LearningCategoryTest {
    LearningCategory learningCategory;
    @Before
    public void init(){
        learningCategory = new LearningCategory(1, "name", "description", 2);
    }

    @Test
    public void getLa_id() {
        assertTrue("Test getla_id", learningCategory.getLa_id() == 2);
    }

    @Test
    public void setLa_id() {
        learningCategory.setLa_id(10);
        assertTrue("Test setla_id", learningCategory.getLa_id() == 10);
    }

    @Test
    public void toString1() {
        assertTrue("Test toString", learningCategory.getName().equals("name"));
    }
}
