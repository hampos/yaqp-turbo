/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.qsar.processors.trainers.regression;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.db.util.TheDbConnector;
import org.opentox.io.util.ServerList;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.util.AlgorithmParameter;


/**
 *
 * @author chung
 */
public class MLRTrainerTest {

    public MLRTrainerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TheDbConnector.init();
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

    @Test
    public void testTrain() throws Exception {
        Map<String, AlgorithmParameter> params = new HashMap<String, AlgorithmParameter>();
        params.put("prediction_feature", new AlgorithmParameter<String>(ServerList.ambit + "/feature/12111"));
        params.put("dataset_uri", new AlgorithmParameter<String>("http://localhost/8"));

        final TrainingPipeline pipe = new TrainingPipeline(new MLRTrainer(params));

        Thread train = new Thread() {

            @Override
            public void run() {
                QSARModel model;
                try {
                    model = pipe.process(new URI("http://localhost/8"));
                    System.out.println(model.getCode());
                    System.out.println(model.getId());
                } catch (Exception ex) {
                    Logger.getLogger(MLRTrainerTest.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };


        train.run();

    }




}
