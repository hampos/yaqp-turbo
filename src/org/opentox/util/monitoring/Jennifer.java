/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of the National Technical University of Athens. Please read README for more
 * information.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 */
package org.opentox.util.monitoring;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.exceptions.DuplicateKeyException;
import org.opentox.db.handlers.WriterHandler;
import org.opentox.db.table.StandardTables;
import org.opentox.db.table.collection.UsersTable;
import org.opentox.db.util.TheDbConnector;
import org.opentox.ontology.components.Algorithm;
import org.opentox.ontology.components.AlgorithmOntology;
import org.opentox.ontology.components.User;
import org.opentox.ontology.components.UserGroup;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.namespaces.OTAlgorithmTypes;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.*;

/**
 *
 * Let us introduce Jennifer. Jennifer is the head of YAQP. She controls and monitors
 * the whole system. She's a singleton that periofically checks if the datbase server
 * (apache derby) is up and running, if the application is properly connected in the database and
 * if the HTTP server is alive. If some of these operations does not work properly
 * , Jennifer attemts to fix it (e.g. she restarts the derby server). What is more,
 * Jenny initializes the database and populates it with valuable data such as an
 * administrator user, adds algorithms, algorithm ontologies and algorithm-ontology
 * relations in the corresponding tables of the database.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Jennifer extends Thread {

    private static Jennifer instanceOfThis = null;
    public static Jennifer INSTANCE = getInstance();
    // <editor-fold defaultstate="collapsed" desc="CONSTANTS for Jennifer">
    private final int PERIOD = 5;
    private final TimeUnit SCHEDULE_UNIT = TimeUnit.SECONDS;
    private final int DERBY_RESTART_ATTEMPTS = 5,
            DERBY_RESTART_DELAY_ms = 500,
            HTTP_PING_ATTEMPTS = 3;
    // </editor-fold>
    private boolean isDatabaseInitialized = false;

    public boolean isDbInit() {
        return isDatabaseInitialized;
    }

    // <editor-fold defaultstate="collapsed" desc="get the single instance for Jennifer">
    private static Jennifer getInstance() {
        if (instanceOfThis == null) {
            instanceOfThis = new Jennifer();
        }
        return instanceOfThis;
    }// </editor-fold>

    private Jennifer() {
        setName("jenifer");
    }

    @Override
    public void run() {
        initializedDatabase();
        isDatabaseInitialized = true;
        Timer timer = new Timer("YAQP Janitor");
        long period = TimeUnit.MILLISECONDS.convert(PERIOD, SCHEDULE_UNIT);
        timer.schedule(new MONITOR(), period, period);
    }

    // <editor-fold defaultstate="collapsed" desc="A scheduled monitor for the whole application">
    private class MONITOR extends TimerTask {

        @Override
        public void run() {
            checkDbConnectivity();
            checkHttpConnectivity(HTTP_PING_ATTEMPTS);
        }
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Initialized the database">
    private void initializedDatabase() {
        try {
            TheDbConnector.init();
            populateAlgorithmOntologies();
            populateAlgorithms();
            populateUserGroups();
            populateUsers();
        } catch (DbException ex) {
            System.out.println(ex);
            // What to do???
        } catch (ExceptionInInitializerError ex) {
            System.out.println("\n\nIt seems someone is already connected in the database with the same username");
            System.exit(3);
        }
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="populate the ontologies in the database">
    private void populateAlgorithmOntologies() throws DbException {
        try {
            ArrayList<OTAlgorithmTypes> otlist = OTAlgorithmTypes.getAllAlgorithmTypes();
            for (OTAlgorithmTypes ot : otlist) {
                try {
                    WriterHandler.add(new AlgorithmOntology(ot));
                } catch (DbException ex) {
                    if (!(ex instanceof DuplicateKeyException)) {
                        throw ex;
                    }
                }
            }
        } catch (YaqpOntException ex) {/* do nonthing */ }
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="populate the algorithms in the database">
    private void populateAlgorithms() throws DbException {
        for (Algorithm alg : YaqpAlgorithms.getAllAlgorithms()) {
            try {
                WriterHandler.add(alg);
            } catch (DbException ex) {
                /* do nonthing */
                if (!(ex instanceof DuplicateKeyException)) {
                    throw ex;
                }
            } catch (YaqpOntException ex) {/* do nonthing */ }
        }
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="register the standard user groups in the DB">
    private void populateUserGroups() throws DbException {
        try {
            try {
                WriterHandler.add(new UserGroup("GUEST", 10,"CCC", "CCC", "CCC" , "CCC", 3000));
            } catch (DbException ex) {
                if (!(ex instanceof DuplicateKeyException)) {
                    YaqpLogger.LOG.log(new ScrewedUp(getClass(),
                            "UserGroup 'GUEST' cannot be added in the database"));
                    throw ex;
                }

            } catch (ImproperEntityException ex) {
            }
            try {
                WriterHandler.add(new UserGroup("SIMPLE", 100,"CCC", "CCC", "CCC" , "CCC", 3000));
            } catch (DbException ex) {
                if (!(ex instanceof DuplicateKeyException)) {
                    YaqpLogger.LOG.log(new ScrewedUp(getClass(),
                            "UserGroup 'SIMPLE' cannot be added in the database"));
                    throw ex;
                }
            } catch (ImproperEntityException ex) {
            }
            try {
                WriterHandler.add(new UserGroup("ADMINISTRATOR", 1000,"CCC", "CCC", "CCC" , "CCC", 3000));
            } catch (DbException ex) {
                if (!(ex instanceof DuplicateKeyException)) {
                    YaqpLogger.LOG.log(new ScrewedUp(getClass(),
                            "UserGroup 'ADMINISTRATOR' cannot be added in the database"));
                    throw ex;
                }
            } catch (ImproperEntityException ex) {
            }
            try {
                WriterHandler.add(new UserGroup("JANITOR", 10000,"CCC", "CCC", "CCC" , "CCC", 3000));
            } catch (DbException ex) {
                if (!(ex instanceof DuplicateKeyException)) {
                    YaqpLogger.LOG.log(new ScrewedUp(getClass(),
                            "UserGroup 'JANITOR' cannot be added in the database"));
                    throw ex;
                }
            }
        } catch (ImproperEntityException ex) {
        }
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Write the janitor information in the DB">
    private void populateUsers() {
        String pass = "2331f5f83cdf42c1b08eaead3a1f1e44d7988676faebc2a7f90832ff496da8d6e1527738d29c552a07c1348098570459573925d178415751f211bc485d331d2e";
        User u = new User(
                "chung", pass, "Pantelis", "Sopasakis",
                "makis@foo.goo.gr", null, "Greece",
                "Athens", "9, Iroon Politechniou St..", "https://opentox.ntua.gr/new", null, 
                new UserGroup("JANITOR", 10000,"CCC", "CCC", "CCC" , "CCC", 3000));
        try {
            WriterHandler.add(u);
        } catch (DbException ex) {
            if (ex instanceof DuplicateKeyException) {
                YaqpLogger.LOG.log(new Trace(getClass(), "Janitor user already in the database"));
            } else {
                throw new RuntimeException("Unexpected condition while trying to add janitor user.", ex);
            }
        } catch (ImproperEntityException ex) {
            Logger.getLogger(Jennifer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="check the connectivity to the DB">
    private void checkDbConnectivity() {
        //  WAIT A LITTLE BEFORE CHECKING IF THE DERBY SERVER IS UP.
        try {
            Thread.sleep(DERBY_RESTART_DELAY_ms);
        } catch (InterruptedException ex) {
        }

        // CHECK IF DERBY IS RUNNING. IF NOT, ATTEMPT TO RESTART IT...
        int i = 0;
        if (!TheDbConnector.DB.isDerbyRunning() && i < DERBY_RESTART_ATTEMPTS) {
            try {
                TheDbConnector.DB.startDerbyServer();
                Thread.sleep(DERBY_RESTART_DELAY_ms);
            } catch (Exception ex) { /* Exception while trying to start the DB server */ }
            i++;
        }

        // CHECK IF IT IS POSSIBLE TO RETRIEVE THE USER 'chung' FROM THE DATABASE:
        // IF NOT, REESTABLISH THE DB CONNECTION:
        try {
            Statement stat = TheDbConnector.DB.getConnection().createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM "
                    + UsersTable.TABLE.getTableName()
                    + " WHERE "
                    + UsersTable.USERNAME.getColumnName() + " = 'chung'");
            while (rs.next()) {
                assert(rs.getString(1).equals("chung"));
            }
        } catch (SQLException ex) { /* SQLException is thrown if the sql statement is
                                     * uncompilable or there is no db connection
                                     */
            refreshDbConnection();
        } catch (AssertionError ae){/*
                                     * This assertion error is thrown if there was no
                                     * user with username 'chung' in the database.
                                     */
            refreshDbConnection();
        }

        if (TheDbConnector.DB == null || TheDbConnector.DB.getConnection() == null) {
            refreshDbConnection();
        }
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Disconnect from and reconnect to the database">
    private void refreshDbConnection() {
        YaqpLogger.LOG.log(new Info(getClass(), "Refreshing database connection"));
        TheDbConnector.DB.disconnect();
        try {
            TheDbConnector.DB.connect();
        } catch (Exception ex) {
            // After all what???
        }
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Check if the Web Server is Alive by pinging localhost">
    private void checkHttpConnectivity(int attempts) {
        java.net.Socket soc = null;
        int i = 1;
        boolean isalive = false;
        while (isalive == false) {
            if (i == attempts) {
                break;
            }
            try {

                soc = new Socket(InetAddress.getLocalHost(), 80);
                isalive = true;
            } catch (java.io.IOException e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Jennifer.class.getName()).log(Level.SEVERE, null, ex);
                }
                i++;
                isalive = false;
            }
        }
        if (!(soc == null)) {
            try {
                soc.close();
            } catch (IOException ex) {
            }
        }

//        System.out.println("x" + isalive);
        if (!isalive) {
            System.out.println("NOT ALIVE");
        }
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Clear all DB entries without deleting the tables">
    public void resetDBContent() throws Exception {

        StandardTables[] tablesForCleanup = StandardTables.values();

        for (int i = tablesForCleanup.length - 1; i >= 0; i--) {
            try {
                Statement statement = TheDbConnector.DB.getConnection().createStatement();
                String sql = "DELETE FROM " + tablesForCleanup[i].getTable().getTableName();
                //System.out.println(sql);
                statement.executeUpdate(sql);
            } catch (SQLException ex) {
                throw ex;
            }
        }
    }// </editor-fold>

    public void ressurect(){
        try {
            // THE RESSURECTION SCRIPT WAITS FOR 5s BEFORE STARTING YAQP AGAIN.
            // IN THE MEANWHILE, THE PARENT APPLICATION HAS ENOUGH TIME TO SUICIDE
            // SO THAT IT WILL BE DIFFICULT THAT TWO INSTANCES OF YAQP RUN AT THE
            // SAME TIME. IF THE RESSURECTION SCRIPT IS NOT FOUND, THE APPLICATION
            // JUST EXITS.
            String[] ressurection = {"sh", "ressurection.sh"};
            Runtime.getRuntime().exec(ressurection);
            Thread.sleep(100);
            System.exit(4);
        } catch (InterruptedException ex) {
            Logger.getLogger(Jennifer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Jennifer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.exit(4);
        }
    }
}
