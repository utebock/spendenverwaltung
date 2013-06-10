package at.fraubock.spendenverwaltung.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;


import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

/**
 * 
 * @author romanvoglhuber
 *
 */
public class MailingTemplate {

	private static final Logger log = Logger.getLogger(MailingTemplate.class);
	
	/**
	 * Takes a docx template and a person list and creates a pdf file.
	 * Before the final pdf file is created, there will be a pdf file for each 
	 * person. After merging all pdf files together, the temp files will be removed.
	 * @param file
	 * 			Docx template file with Velocity syntax in MergeFields (MS Word)
	 * @param personList
	 * 			List of person to merge with the template file
	 * @param outputName
	 * 			Name of the final pdf file
	 * @throws IOException
	 * @throws ServiceException
	 */
	public static void createMailingWithDocxTemplate(File file, List<Person> personList, String outputName) throws IOException, ServiceException{
		int i = 0;
		List<String> files = new ArrayList<String>();
		String path;
		
		//Check Arguments
		if(file==null||personList==null||outputName==null){
			throw new IllegalArgumentException("Argument is null");
		}
		

		log.info("Create mailing with template "+file.getName());
		
		try {
			//Load Template file
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(new FileInputStream(file), TemplateEngineKind.Velocity);
			IContext context;
			OutputStream out;

			//Create context to put person data into the template fields
			context= report.createContext();

			//Create a pdf for each person
			for(Person p : personList){

				context.put("person", p);
				//names are temp0.pdf, temp1.pdf, ... , tempN.pdf
				path = "temp"+i+++".pdf";
				files.add(path);
				out = new FileOutputStream(new File(path));
				Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
				report.convert(context, options, out);
				log.info("Created pdf: "+path);
			}
		} catch (XDocReportException e) {
			throw new ServiceException(e);
		}

		//Merge into one pdf and delete temp files
		mergePdfs(files, outputName);
	}
	
	/**
	 * Merges all pdf files into one pdf file and deletes all temp pdf files after that
	 * @param pdfs
	 * 		List of pdf file names
	 * @param outputName
	 * 		Name of the final pdf file
	 * @throws IOException
	 * @throws ServiceException
	 */
	private static void mergePdfs(List<String> pdfs, String outputName) throws IOException, ServiceException{
		Document pdfDocument = new Document();
		PdfCopy copy;
		PdfReader pdfReader;
		File f;
		int pages;
		
		if(pdfs==null){
			throw new IllegalArgumentException("Argument is null");
		}
		
		try{
			//Add .pdf if needed
			if(!outputName.endsWith(".pdf")){
				outputName += ".pdf";
			}
			//Combine pdf files to one
			copy = new PdfCopy(pdfDocument, new FileOutputStream(outputName));

			pdfDocument.open();

			//Read all pages and add them to the final pdf file
			for (String pdf : pdfs) {
				pdfReader = new PdfReader(pdf);
				pages = pdfReader.getNumberOfPages();
				for (int page = 0; page < pages; ) {
					copy.addPage(copy.getImportedPage(pdfReader, ++page));
				}
			}
			pdfDocument.close();
		}
		catch (DocumentException e){
			throw new ServiceException(e);
		}

		//Delete tmp files
		for(String pdf : pdfs){
			f = new File(pdf);
			f.delete();
		}
	}
}
