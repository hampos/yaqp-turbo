package org.opentox.db.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.table.StandardTables;

/**
 *
 * @author chung
 */
public class TheDbConnectorTest {

    public TheDbConnectorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
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
        TheDbConnector.init();
        PreparedStatement  ps = TheDbConnector.DB.getConnection().prepareStatement("SELECT * FROM ALGORITHM_ONTOLOGIES WHERE UID = ?");
        ps.setInt(1, 1);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
        //    System.out.println(rs.getMetaData().);
        }
    }

    //@Test
    public void Prepared_Deletion() throws SQLException{

        PreparedStatement ds = TheDbConnector.DB.getConnection().prepareStatement(StandardTables.ALGORITHM_ONTOL_RELATION.getTable().getDeletionSQL());
        ds.executeUpdate();
    }
}
