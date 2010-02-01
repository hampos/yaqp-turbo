/*
 * YAQP - Yet Another QSAR Project: Machine Learning algorithms designed for
 * the prediction of toxicological features of chemical compounds become
 * available on the Web. Yaqp is developed under OpenTox (http://opentox.org)
 * which is an FP7-funded EU research project.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
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
 */
package org.opentox.core.interfaces;

import java.util.List;

/**
 *
 * A Multi-Processor is a processor that consists of one or more subprocessors
 * that run in series, in parallel or in some other topology. This collection of
 * processors is said to be fail-sensitive if the failure of one processor results
 * in the failure of the whole structure. Generally in-series processors (like
 * pipelines) are fail-sensitive unless otherwise stated.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JMultiProcessor<Input, Output, P extends JProcessor> extends JTurboProcessor<Input, Output>, List<P>{

    boolean isfailSensitive();

    void setfailSensitive(boolean failSensitive);

    JMultiProcessorStatus getStatus();

}
