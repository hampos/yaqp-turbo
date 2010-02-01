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
package org.opentox.ontology.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.opentox.config.Configuration;
import org.opentox.ontology.util.vocabulary.Audience;
import org.restlet.data.MediaType;

/**
 * Meta Data in general based on the Dublin Core Metadata Modeling ontology.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Meta {

    /**
     *
     * A name given to the resource. Typically, a Title will be a name by which
     * the resource is formally known.
     */
    public String title = "";
    /**
     *
     * An account of the resource. Description may include but is not limited to:
     * an abstract, a table of contents, a graphical representation, or a
     * free-text account of the resource.
     */
    public String description = "";
    /**
     *
     * The topic of the resource. Typically, the subject will be represented using keywords,
     * key phrases, or classification codes. Recommended best practice is to use a controlled
     * vocabulary. To describe the spatial or temporal topic of the resource, use the Coverage element.
     */
    public String subject = "";
    /**
     *
     * The nature or genre of the resource. Recommended best practice is to use a
     * controlled vocabulary such as the DCMI Type Vocabulary [DCMITYPE]. To describe
     * the file format, physical medium, or dimensions of the resource, use the Format element.
     * @see [DCMITYPE] http://dublincore.org/documents/dcmi-type-vocabulary/
     */
    public String type = "";
    /**
     *
     * A related resource from which the described resource is derived. The described
     * resource may be derived from the related resource in whole or in part. Recommended
     * best practice is to identify the related resource by means of a string conforming
     * to a formal identification system. By default this is set to the URI of the server
     * specified in the <code>server.properties</code> or <code>macos.server.properties</code>
     * file.
     */
    public String source = 
            "http://"+Configuration.getProperties().getProperty("server.domainName", "opentox.ntua.gr")+
            Configuration.getProperties().getProperty("server.port", "3000");
    /**
     *
     * A related resource. Recommended best practice is to identify the related
     * resource by means of a string conforming to a formal identification system.
     * By default this is set to <code>http://opentox.org</code>
     */
    public String relation = "http://opentox.org";
    /**
     *
     * Information about rights held in and over the resource. Typically, rights
     * information includes a statement about various property rights associated
     * with the resource, including intellectual property rights. By default this is
     * set to <code>http://www.gnu.org/licenses/</code>.
     */
    public String rights = "http://www.gnu.org/licenses/";
    /**
     *
     * A statement of any changes in ownership and custody of the resource since
     * its creation that are significant for its authenticity, integrity and
     * interpretation. The statement may include a description of any changes
     * successive custodians made to the resource.
     */
    public String provenance = "";
    /**
     *
     * A class of entity for whom the resource is intended or useful. A class of
     * entity may be determined by the creator or the publisher or by a third party.
     * Audience terms are best utilized in the context of formal or informal
     * controlled vocabularies. None are presently recommended or registered by DCMI,
     * but several communities of interest are engaged in setting up audience
     * vocabularies. In the absence of recommended controlled vocabularies,
     * implementors are encouraged to develop local lists of values, and to use
     * them consistently.
     */
    public ArrayList<Audience> audience = new ArrayList<Audience>();
    /**
     *
     * An unambiguous reference to the resource within a given context. Recommended best
     * practice is to identify the resource by means of a string conforming to a
     * formal identification system.
     */
    public String identifier = "";
    /**
     *
     * A point or period of time associated with an event in the lifecycle of the resource.
     * Date may be used to express temporal information at any level of granularity.
     * Recommended best practice is to use an encoding scheme, such as the
     * W3CDTF profile of ISO 8601 [W3CDTF].
     * @see http://www.w3.org/TR/NOTE-datetime
     */
    public Date date;
    /**
     *
     * The file format, physical medium, or dimensions of the resource. Examples of
     * dimensions include size and duration. Recommended best practice is to use a
     * controlled vocabulary such as the list of Internet Media Types [MIME].
     */
    public ArrayList<MediaType> format =  new ArrayList<MediaType>();
    /**
     * A language of the resource. Recommended best practice is to use a
     * controlled vocabulary such as RFC 4646 [RFC4646].
     * @see [RFC4646] http://www.ietf.org/rfc/rfc4646.txt
     */
    public String language = "en";
    /**
     *
     * An entity primarily responsible for making the resource.
     * Examples of a Creator include a person, an organization, or a
     * service. Typically, the name of a Creator should be used to indicate
     * the entity.
     */
    public String creator = source;
    /**
     *
     * An entity responsible for making the resource available. Examples of a Publisher
     * include a person, an organization, or a service. Typically, the name of a
     * Publisher should be used to indicate the entity.
     */
    public String publisher = source;
    /**
     *
     * An entity responsible for making contributions to the resource.
     */
    public String contributor = relation;
    /**
     *
     * The spatial or temporal topic of the resource, the spatial applicability
     * of the resource, or the jurisdiction under which the resource is relevant.
     * Spatial topic and spatial applicability may be a named place or a location
     * specified by its geographic coordinates. Temporal topic may be a named period,
     * date, or date range. A jurisdiction may be a named administrative entity or
     * a geographic place to which the resource applies. Recommended best practice
     * is to use a controlled vocabulary such as the Thesaurus of Geographic
     * Names [TGN]. Where appropriate, named places or time periods can be used
     * in preference to numeric identifiers such as sets of coordinates or date ranges.
     *
     * [TGN] http://www.getty.edu/research/tools/vocabulary/tgn/index.html
     */
    public String coverage = "";
}
