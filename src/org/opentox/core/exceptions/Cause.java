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
package org.opentox.core.exceptions;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum Cause {

    // CONFIG:
    XA1, XA2, XA3,

    //BATCH PROCESSOR:
    XBP1, XBP2, XBP3, XBP7,

    // PARALLEL PROCESSOR:
    XPP1, XPP2, XPP3, XPP7,

    // PIPELINE
    XPIP1001,

    // DB PROCESSOR
    XDB4, XDB9,
    XDB10, XDB11, XDB12, XDB13, XDB14, XDB15, XDB16, XDB17, XDB18, XDB19,
    XDB20, XDB21,
    XDB321,
    XDB490,
    XDB624,
    XDB800,
    XDB1002, XDB1003, XDB1004, XDB1005,
    XDB3235, XDB3236, XDB3237, XDB3238,
    XDB4001, XDB4002, XDB4003, XDB4004, XDB4005,
    XDB5001, XDB5002, XDB5003, XDB5004, XDB5005, XDB5006, XDB5007, XDB5008,
    XDB4300, XDB4301,
    XDB5870, XDB5871, XDB5872, XDB5873, XDB5874, XDB5875,
    XDB7701, XDB7702, XDB7703,
    XDB9090, XDB9091, XDB9092, XDB9093, XDB9094, XDB9095,

    // HANDLERS:
    XDH0, XDH00, XDH1, XDH2, XDH3, XDH4, XDH5, XDH6, XDH7, XDH8,
    XDH100, XDH101, XDH102, XDH103, XDH104, XDH105, XDH106, XDH107, XDH108, XDH109,
    XDH110, XDH111, XDH112,

    // IMPROPER ENTITY:
    XIE0, XIE1, XIE2,

    // ONTOLOGICAL:
    XONT0, XONT1, XONT2, XONT3, XONT4, XONT5, XONT6, XONT7, XONT8, XONT9,
    XONT517, XONT518, XONT519, XONT520, XONT521, XONT522,

    // INPUT/OUTPUT
    XIO54, XIO75, XIO76, XIO77, XIO90,

    // PUBLISHABLE
    XPDF18, XRDF99, XTTL700,

    // GENERAL PURPOSE
    XTC743,

    // QSAR - Regression
    XQReg0, XQReg00, XQReg000, XQReg1, XQReg2, XQReg3, XQReg4,
    XQReg200, XQReg201, XQReg202,
    XQReg500, XQReg501,
    XQReg711, XQReg712,
    XQReg120, XQReg121, XQReg350,
    XQReg3001, XQReg3002,XQReg3003,XQReg3004,XQReg3005,XQReg3006,XQReg3007,XQReg3008,
     XQReg3009, XQReg3010,XQReg3011,XQReg3012,

    // QSAR - Classification
    XQC4040, XQC4041, XQC4042, XQC4043, XQC4044, XQC4045,

    // QSAR - Filters
    XQF1, XQF11, XQF111, XQF212, XQF535, XQF913, XQF412
            
}
