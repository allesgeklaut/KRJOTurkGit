package tests;


/*import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;*/
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import service.DBConnector;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentNavigableMap;

import static org.junit.Assert.*;

public class DBConnectorTest {

    DBConnector connector;

    @Before
    public void init() {
       connector = new DBConnector();
    }

    @Test
    public void connect() throws SQLException {

        connector.connect("jdbc:mysql://localhost:3306/OADTurk4?autoReconnect=true&useSSL=false", "root", "root");

        DatabaseMetaData dmd = connector.getConnection().getMetaData();
        String url = dmd.getURL();
        assertTrue("Test CONNECT", url == "jdbc:mysql://localhost:3306/OADTurk4?autoReconnect=true&useSSL=false");
    }

    @Test
    public void connectToDB() {

        connector.connectToDB();
        assertTrue("Test CONNECT", connector.getConnection() != null);
    }


}

