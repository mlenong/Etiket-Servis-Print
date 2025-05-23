package com.example.etiket;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.logging.Logger;

@RestController
@RequestMapping("/print")
public class PrintController {

    private static final Logger logger = Logger.getLogger(PrintController.class.getName());
    // private static final String PRINTER_NAME = "ETIKET";
    private static final String PRINTER_NAME_ETIKET = "ETIKET";
    private static final String PRINTER_NAME_NOTA = "NOTA";

    @GetMapping("/list")
    public ResponseEntity<String> listPrinters() {
        StringBuilder sb = new StringBuilder("Daftar Printer Terdeteksi:\n");
        for (PrintService ps : PrintServiceLookup.lookupPrintServices(null, null)) {
            sb.append(ps.getName()).append("\n");
        }
        return ResponseEntity.ok(sb.toString());
    }

    @PostMapping("/etiket")
    public ResponseEntity<String> printEtiket(@RequestParam("file") MultipartFile file) {
        File tempFile = null;

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File tidak boleh kosong");
            }

            // Simpan file ke disk sementara
            tempFile = File.createTempFile("etiket_", ".pdf");
            FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);

            // Buka PDF
            PDDocument document = PDDocument.load(tempFile);

            // Cari printer
            PrinterJob job = PrinterJob.getPrinterJob();
            PrintService printer = findPrinterByName(PRINTER_NAME_ETIKET);
            if (printer == null) {
                document.close();
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Printer '" + PRINTER_NAME_ETIKET + "' tidak ditemukan.");
            }

            job.setPrintService(printer);
            job.setPageable(new PDFPageable(document));

            // Cetak PDF
            job.print();
            document.close();

            logger.info("PDF berhasil dicetak ke printer: " + printer.getName());
            return ResponseEntity.ok("Berhasil mencetak ke printer: " + printer.getName());

        } catch (Exception e) {
            logger.severe("Gagal mencetak: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mencetak: " + e.getMessage());

        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @PostMapping("/nota")
    public ResponseEntity<String> printNota(@RequestParam("file") MultipartFile file) {
        return printPdfToPrinter(file, PRINTER_NAME_NOTA);
    }

    private ResponseEntity<String> printPdfToPrinter(MultipartFile file, String printerName) {
        File tempFile = null;

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File tidak boleh kosong");
            }

            // Simpan file ke disk sementara
            tempFile = File.createTempFile("print_", ".pdf");
            FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);

            // Buka PDF
            PDDocument document = PDDocument.load(tempFile);

            // Cari printer
            PrinterJob job = PrinterJob.getPrinterJob();
            PrintService printer = findPrinterByName(printerName);
            if (printer == null) {
                document.close();
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Printer '" + printerName + "' tidak ditemukan.");
            }

            job.setPrintService(printer);
            job.setPageable(new PDFPageable(document));

            // Cetak PDF
            job.print();
            document.close();

            logger.info("PDF berhasil dicetak ke printer: " + printer.getName());
            return ResponseEntity.ok("Berhasil mencetak ke printer: " + printer.getName());

        } catch (Exception e) {
            logger.severe("Gagal mencetak: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mencetak: " + e.getMessage());

        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private PrintService findPrinterByName(String name) {
        //golek sik plek ketiplek sik
        for (PrintService ps : PrintServiceLookup.lookupPrintServices(null, null)) {
            if (ps.getName().equalsIgnoreCase(name)) {
                return ps;
            }
        }
        //golek sik printer e podo tapi sharingan
        for (PrintService ps : PrintServiceLookup.lookupPrintServices(null, null)) {
            String printerName = ps.getName();
            String[] parts = printerName.split("\\\\"); 
            String lastSegment = parts.length > 0 ? parts[parts.length - 1] : "";
            if (lastSegment.equalsIgnoreCase(name)) {
                return ps;
            }
        }

        //golek sik mirip sitik
         for (PrintService ps : PrintServiceLookup.lookupPrintServices(null, null)) {
            if (ps.getName().toLowerCase().contains(name.toLowerCase())) {
                return ps;
            }
        }
        return null;
    }
    // private PrintService findPrinterByName(String name) {
    //     for (PrintService ps : PrintServiceLookup.lookupPrintServices(null, null)) {
    //         // Lebih fleksibel: cek apakah nama printer MENGANDUNG kata kunci
    //         if (ps.getName().toLowerCase().contains(name.toLowerCase())) {
    //             return ps;
    //         }
    //     }
    //     return null;
    // }
}
