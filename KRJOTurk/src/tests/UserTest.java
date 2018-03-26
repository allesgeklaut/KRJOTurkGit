package tests;

import org.junit.Before;
import org.junit.Test;
import service.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

import static org.junit.Assert.*;

public class UserTest {
    User user;


    @Before
    public void  init() throws ParseException {
        String expectedPattern = "MM-dd-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        Date date = new Date(formatter.parse("09-12-2009").getTime());
        user = new User(1, "testuser", "testsurname", "testuser@testuser.at", date, false, false);
    }

    @Test
    public void setAdmin() {
        user.setAdmin(true);
        assertTrue("Test : user became admin", user.isAdmin());
    }

    @Test
    public void setCreator() {
        user.setCreator(true);
        assertTrue("Test : user became creator", user.isCreator());
    }

    @Test
    public void setUser_id() {
        user.setUser_id(2);
        assertEquals(2, user.getUser_id());
    }

    @Test
    public void setUser_name() {
        user.setUser_name("user2");
        assertEquals("user2", user.getUser_name());
    }

    @Test
    public void setUser_surname() {
        user.setUser_surname("user2");
        assertEquals("user2", user.getUser_surname());
    }

    @Test
    public void setEmail() {
        user.setEmail("asd@asd.at");
        assertEquals("asd@asd.at", user.getEmail());
    }

    @Test
    public void setBirthdate() throws ParseException {
        String expectedPattern = "MM-dd-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        Date date = new Date(formatter.parse("01-01-1992").getTime());
        user.setBirthdate(date);
        assertEquals(date, user.getBirthdate());
    }


    @Test
    public void testToString() {
        String expectedPattern = "MM-dd-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        try {
            Date date = new Date(formatter.parse("09-12-2009").getTime());
            user = new User(2, "user", "usersurname", "email@email.at", date);
            String exceptedString = "User{" +
                "user_id=2" +
                ", user_name='user'" +
                ", user_surname='usersurname'" +
                ", email='email@email.at'"+
                ", birthdate=" + date +
                ", gender=" +
                '}';

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}