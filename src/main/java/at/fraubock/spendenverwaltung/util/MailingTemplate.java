package at.fraubock.spendenverwaltung.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BadPdfFormatException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;


import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.velocity.internal.VelocityTemplateEngine;

import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

public class MailingTemplate {
	public static List<String> template(File file, List<Person> personList) throws FileNotFoundException, IOException, XDocReportException, ServiceException, DocumentException{
		int i = 0;
		List<String> files = new ArrayList<String>();
		String path;
		
		
		IXDocReport report = XDocReportRegistry.getRegistry().loadReport(new FileInputStream(file), TemplateEngineKind.Velocity);
		IContext context;
		OutputStream out;
		context= report.createContext();
		
		for(Person p : personList){
		
			context.put("person", p);
			path = "Output"+i+++".pdf";
			files.add(path);
			out = new FileOutputStream(new File(path));
			Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
			report.convert(context, options, out);
		}
		
		return files;

	}
	
	public static void mergePdfs(List<String> pdfs) throws ServiceException{
		Document pdfDocument = new Document();
		PdfCopy copy;
		PdfReader pdfReader;
		int pages;
		
		try{
		//Combine pdf files to one
		copy = new PdfCopy(pdfDocument, new FileOutputStream("Output.pdf"));

		pdfDocument.open();
		
		for (String pdf : pdfs) {
			pdfReader = new PdfReader(pdf);
			pages = pdfReader.getNumberOfPages();
			for (int page = 0; page < pages; ) {
				copy.addPage(copy.getImportedPage(pdfReader, ++page));
			}
		}
		pdfDocument.close();
		}
		catch (IOException  | DocumentException e){
			throw new ServiceException(e);
		}
	}
}
