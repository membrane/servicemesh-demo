package de.predic8.example.servicemesh.c.c.controller;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import de.predic8.example.servicemesh.c.c.model.BillingData;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resources;
import java.io.*;
import java.nio.charset.StandardCharsets;

@RestController
public class Controller {

    @PostMapping(path="/v1/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody
    ResponseEntity<InputStreamResource> generatePDF(@RequestBody BillingData data) throws IOException, InterruptedException {

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache m = mf.compile(new InputStreamReader(this.getClass().getResourceAsStream("/template.html"),StandardCharsets.UTF_8), "template.mustache");
        StringWriter result = new StringWriter();
        m.execute(result, data);

        String html = result.toString();

        File tempHtmlFile = File.createTempFile("servicemesh-c", ".html");
        try (FileOutputStream fos = new FileOutputStream(tempHtmlFile)) {
            try (PrintWriter pw = new PrintWriter(fos)) {
                pw.println(html);
            }
        }

        try (FileOutputStream fos = new FileOutputStream(new File(tempHtmlFile.getParentFile(), "logo.png"))) {
            IOUtils.copy(getClass().getResourceAsStream("/logo.png"), fos);
        }


        Pdf pdf = new Pdf();

        pdf.addPageFromFile(tempHtmlFile.getAbsolutePath());
        pdf.addParam(new Param("-s", "A4"));
        //pdf.addParam(new Param("--zoom", "2"));

// The `wkhtmltopdf` shell command accepts different types of options such as global, page, headers and footers, and toc. Please see `wkhtmltopdf -H` for a full explanation.
// All options are passed as array, for example:
//        pdf.addParam(new Param("--no-footer-line"), new Param("--header-html", "file:///header.html"));
//        pdf.addParam(new Param("--enable-javascript"));

// Add styling for Table of Contents
//        pdf.addTocParam(new Param("--xsl-style-sheet", "my_toc.xsl"));

        File tempFile = File.createTempFile("servicemesh-c", ".pdf");
        pdf.saveAs(tempFile.getPath());


        InputStreamResource resource = new InputStreamResource(new FileInputStream(tempFile));

        return ResponseEntity.ok()
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + data.filenameBase + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(tempFile.length())
                .body(resource);
    }

}
