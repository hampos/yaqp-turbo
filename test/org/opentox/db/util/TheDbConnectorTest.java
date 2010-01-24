package org.opentox.db.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.db.table.StandardTables;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class TheDbConnectorTest {

    public TheDbConnectorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        assertFalse(TheDbConnector.DB.isInitialized());
        TheDbConnector.init();
        assertTrue(TheDbConnector.DB.isInitialized());
        TheDbConnector.init();
        assertTrue(TheDbConnector.DB.isInitialized());

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getLock method, of class DbConnector.
     */
    @Test
    public void InstantiationTest() throws Exception {
        System.out.println("-- instantiation test --");
        PreparedStatement ps = TheDbConnector.DB.getConnection().prepareStatement("SELECT * FROM ALGORITHM_ONTOLOGIES WHERE UID = ?");
        ps.setInt(1, 1);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            //    System.out.println(rs.getMetaData().);
        }
    }

    @Test
    public void Prepared_Deletion() throws SQLException {

        PreparedStatement ds = TheDbConnector.DB.getConnection().prepareStatement(
                StandardTables.ALGORITHM_ONTOL_RELATION.getTable().getDeletionSQL());
        ds.executeUpdate();
    }

    @Test
    public void preparedSelection() throws SQLException {
        PreparedStatement ps = TheDbConnector.DB.getConnection().prepareStatement("SELECT * FROM USERS WHERE LASTNAME LIKE ?");
        ps.setString(1, "%sak%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString("USERNAME") + ", " + rs.getString("PASS"));
        }
    }
}
