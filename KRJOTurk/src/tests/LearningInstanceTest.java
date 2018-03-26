package tests;

import org.junit.Before;
import org.junit.Test;
import service.LearningUnit;

import static org.junit.Assert.*;

public class LearningInstanceTest {
    LearningUnit learningUnit;

    @Before
    public void  init(){
        learningUnit = new LearningUnit(1, "testlu", "testdescription");
    }

    @Test
    public void setId() {
        learningUnit.setId(2);
        assertEquals(2, learningUnit.getId());
    }

    @Test
    public void setName() {
        learningUnit.setName("lutest");
        assertEquals("lutest", learningUnit.getName());
    }

    @Test
    public void setDescription() {
        learningUnit.setDescription("thisisadesc");
        assertEquals("thisisadesc", learningUnit.getDescription());
    }
}