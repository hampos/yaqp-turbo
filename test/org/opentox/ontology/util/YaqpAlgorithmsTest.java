package org.opentox.ontology.util;

import com.hp.hpl.jena.rdf.model.Resource;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.ontology.components.Algorithm;
import org.opentox.ontology.namespaces.OTAlgorithmTypes;
import org.opentox.ontology.util.vocabulary.Audience;
import org.restlet.data.MediaType;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class YaqpAlgorithmsTest {

    public YaqpAlgorithmsTest() {
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

    public void writePDF(Algorithm algorithm) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("/home/chung/Desktop/" + algorithm.metadata.name + ".pdf"));
        document.open();
        document.addAuthor("Sopasakis Pantelis");
        document.addCreator("iText");
        document.addTitle("OpenTox Algorithm");
        document.addSubject("Algorithm Representation");
        document.addCreationDate();
        document.addProducer();
        document.addKeywords(algorithm.getMeta().subject);

        Paragraph p1 = new Paragraph(new Chunk(
                "OpenTox - " + algorithm.getMeta().identifier + "\n\n",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        document.add(p1);


        PdfPTable table = new PdfPTable(2);
        table.setWidths(new int[]{10, 50});
        PdfPCell cell = new PdfPCell(new Paragraph("Algorithm Presentation - General"));
        cell.setColspan(2);
        cell.setBackgroundColor(new BaseColor(0xC0, 0xC0, 0xC0));
        table.addCell(cell);

        table.addCell("Name");
        table.addCell(algorithm.metadata.name);

        table.addCell("Title");
        table.addCell(algorithm.metadata.title);

        table.addCell("Subject");
        table.addCell(algorithm.metadata.subject);

        table.addCell("Description");
        table.addCell(algorithm.metadata.description);

        table.addCell("Identifier");
        table.addCell(algorithm.metadata.identifier);


        document.add(table);
        document.add(new Paragraph("\n\n\n"));


        table = new PdfPTable(2);
        table.setWidths(new int[]{10, 50});
        cell = new PdfPCell(new Paragraph("General Meta Information"));
        cell.setColspan(2);
        cell.setBackgroundColor(new BaseColor(0xC0, 0xC0, 0xC0));
        table.addCell(cell);

        table.addCell("Type");
        table.addCell(algorithm.metadata.type);

        table.addCell("Creator");
        table.addCell(algorithm.metadata.creator);

        table.addCell("Publisher");
        table.addCell(algorithm.metadata.publisher);

        table.addCell("Relation");
        table.addCell(algorithm.metadata.relation);

        table.addCell("Rights");
        table.addCell(algorithm.metadata.rights);

        table.addCell("Source");
        table.addCell(algorithm.metadata.source);

        table.addCell("Provenance");
        table.addCell(algorithm.metadata.provenance);

        table.addCell("Contributor");
        table.addCell(algorithm.metadata.contributor);

        table.addCell("Language");
        table.addCell(algorithm.metadata.language.getDisplayLanguage());

        table.addCell("Created on");
        table.addCell(algorithm.metadata.date.toString());

        table.addCell("Formats");
        ArrayList<MediaType> listMedia = algorithm.getMeta().format;
        String formatTableEntry = "";
        for (int i = 0; i < listMedia.size(); i++) {
            formatTableEntry += listMedia.get(i).toString();
            if (i < listMedia.size() - 1) {
                formatTableEntry += "\n";
            }
        }
        table.addCell(formatTableEntry);



        table.addCell("Audience");
        ArrayList<Audience> audiences = algorithm.metadata.audience;
        String auds = "";
        for (int i = 0; i < audiences.size(); i++) {
            auds += audiences.get(i).getName();
            if (i < audiences.size() - 1) {
                auds += ",";
            }
        }
        table.addCell(auds);

        document.add(table);


        document.add(new Paragraph("\n\n\n"));

        table = new PdfPTable(4);
        table.setWidths(new int[]{30, 30, 30, 30});
        cell = new PdfPCell(new Paragraph("Algorithm Parameters"));
        cell.setColspan(4);
        cell.setBackgroundColor(new BaseColor(0xC0, 0xC0, 0xC0));
        table.addCell(cell);

        table.addCell("Parameter Name");
        table.addCell("XSD DataType");
        table.addCell("Default Value");
        table.addCell("Scope");

        ArrayList<AlgorithmParameter> paramList = algorithm.getMeta().Parameters;
        for (int i = 0; i < paramList.size(); i++) {
            table.addCell(paramList.get(i).paramName);
            table.addCell(paramList.get(i).dataType.getURI());
            table.addCell(paramList.get(i).paramValue.toString());
            table.addCell(paramList.get(i).paramScope);
        }

        document.add(table);

        document.add(new Paragraph("\n\n\n"));

        table = new PdfPTable(1);
        cell = new PdfPCell(new Paragraph("Ontologies"));
        cell.setBackgroundColor(new BaseColor(0xC0, 0xC0, 0xC0));
        table.addCell(cell);
        OTAlgorithmTypes type = algorithm.getMeta().algorithmType;
        table.addCell(type.getURI());

        Set<Resource> superOntologies = type.getSuperEntities();
        Iterator<Resource> it = superOntologies.iterator();

        while (it.hasNext()) {
            table.addCell(it.next().getURI());
        }

        document.add(table);


        document.close();

    }

//    @Test
    public void testMlrPdf() throws Exception {
        YaqpAlgorithmsTest a = new YaqpAlgorithmsTest();
        a.writePDF(YaqpAlgorithms.MLR);
    }

    //  @Test
    public void testSvmPdf() throws Exception {
        YaqpAlgorithmsTest a = new YaqpAlgorithmsTest();
        a.writePDF(YaqpAlgorithms.SVM);
    }

    //@Test
    public void testSvcPdf() throws Exception {
        YaqpAlgorithmsTest a = new YaqpAlgorithmsTest();
        a.writePDF(YaqpAlgorithms.SVC);
    }

    @Test
    public void testGetAllAlgorithms() throws Exception {
        YaqpAlgorithmsTest a = new YaqpAlgorithmsTest();
        ArrayList<Algorithm> list = YaqpAlgorithms.getAllAlgorithms();
        for (int i = 0; i < list.size(); i++) {
            a.writePDF(list.get(i));
        }
    }
}
