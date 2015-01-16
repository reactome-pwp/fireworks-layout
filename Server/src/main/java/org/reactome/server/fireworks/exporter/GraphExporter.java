package org.reactome.server.fireworks.exporter;

import com.itextpdf.text.PageSize;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.preview.PDFExporter;
import org.gephi.io.exporter.preview.PNGExporter;
import org.gephi.io.exporter.preview.SVGExporter;
import org.openide.util.Lookup;

import java.io.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@Deprecated
public class GraphExporter {

    public static void export2PDF(String file){
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        try {
            ec.exportFile(new File(file + ".pdf"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        PDFExporter pdfExporter = (PDFExporter) ec.getExporter("pdf");
        pdfExporter.setPageSize(PageSize.A4);
        pdfExporter.setLandscape(true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ec.exportStream(baos, pdfExporter);
        byte[] pdf = baos.toByteArray();
    }

    public static void export2PNG(String file){
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        try {
            ec.exportFile(new File(file + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        PNGExporter pngExporter = (PNGExporter) ec.getExporter("png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ec.exportStream(baos, pngExporter);
    }

    public static void export2SVG(String file){
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        try {
            File f = new File(file + ".svg");
            ec.exportFile(f);
            SVGExporter svgExporter = (SVGExporter) ec.getExporter("svg");
            Writer writer = new FileWriter(f);
            ec.exportWriter(writer, svgExporter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
