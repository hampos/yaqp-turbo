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
package org.opentox;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.config.Configuration;
import org.opentox.db.util.TheDbConnector;
import org.opentox.util.monitoring.Jennifer;
import org.opentox.www.rest.Applecation;
import org.opentox.www.rest.components.YaqpApplication;
import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class YAQP {

    private static void fancyPrint(String str, int n) throws InterruptedException {
        char[] ch = str.toCharArray();
        for (char i : ch) {
            System.out.print(i);
            Thread.sleep(n);
        }
    }
    private static Thread showDashDot = new Thread("dashdot") {

        @Override
        public void run() {
            try {
                fancyPrint("-.--.-...", 200);
            } catch (InterruptedException ex) {
                Logger.getLogger(YAQP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    private static void logo() throws InterruptedException {
        System.out.print("\n\n*   ._.  ._.      ,__,   ._____.\n");
        Thread.sleep(20);
        System.out.print("*    \\ \\ | |/\\   /    \\ /  ___ |\n");
        Thread.sleep(20);
        System.out.print("*     \\ \\| /  \\ |  ()  |  _____|\n");
        Thread.sleep(20);
        System.out.print("*      \\  / /\\ \\|  {}  | |\n");
        Thread.sleep(20);
        System.out.print("*       || .--. |  [] \\\\ |\n");
        Thread.sleep(20);
        System.out.print("*       ||_|  |_|\\____/\\\\|\n*");
    }

    private static void load() throws InterruptedException {


        System.out.print("\n* Loading YAQP modules       : ");
        fancyPrint("--.-.--.--.-.--.-...-..--..-.\t [ DONE ]\n", 10);


        System.out.print("* Initializing the database  : ");
        fancyPrint("--.--.-.--.", 60);
        showDashDot.start();
        Jennifer.INSTANCE.start();
        while (showDashDot.isAlive() || !Jennifer.INSTANCE.isDbInit()) {
            Thread.sleep(20);
        }
        fancyPrint("-..--..-.", 40);
        System.out.print("\t [ Connected at " + TheDbConnector.DB.getDatabaseUrl() + ";user=" + TheDbConnector.DB.getDatabaseUser() + "  ]\n");


        System.out.print("* Loading Services           : ");
        fancyPrint("--..-..-.-.--.-.--..--..-....\t ", 10);
    }

    
    private static final void startHttpServer() throws Exception{
         Logger L = Logger.getLogger("grizzly");
         L.setLevel(Level.SEVERE);
         int PORT = Configuration.PORT;
        YaqpApplication application = new Applecation();
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, PORT);
        application.setContext(component.getContext().createChildContext());
        component.getDefaultHost().attach("", application);
        component.start();
        System.out.print("[ DONE ] \n");
        fancyPrint("*\n* Server is up and accepts connections on port " + PORT + "!\n*\n", 10);
    }

    public static void main(String args[]) throws Exception {
        logo();
        load();
        startHttpServer();
       // Thread.sleep(3000);
       // Jennifer.INSTANCE.ressurect();

    }
}
