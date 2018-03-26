package tests;

//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import service.LearningApplication;

import static org.junit.Assert.*;


public class LearningApplicationTest {
    LearningApplication learningApplication;
    @Before
    public void init(){
        learningApplication = new LearningApplication(1, "name", "description", 3, 2);

    }

    @Test
    public void getApprovedFlag() {
        assertTrue("Test getApprovedFlag", learningApplication.getApprovedFlag() == 3);
    }

    @Test
    public void setApprovedFlag() {
        learningApplication.setApprovedFlag(5);
        assertTrue("Test setApprovedFlag", learningApplication.getApprovedFlag() == 5);
    }

    @Test
    public void getCreatedBy() {
        assertTrue("Test getCreatedBy", learningApplication.getCreatedBy() == 2);
    }

    @Test
    public void setCreatedBy() {
        learningApplication.setCreatedBy(26);
        assertTrue("Test setCreatedBy", learningApplication.getCreatedBy() == 26);
    }

    @Test
    public void toString1() {

        assertTrue("Test toString", learningApplication.getName().equals("name"));
    }
}
