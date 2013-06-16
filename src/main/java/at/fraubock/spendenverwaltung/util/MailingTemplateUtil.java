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
import fr.opensagres.xdocreport.converter.OptionsHelper;
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
public class MailingTemplateUtil {

	private static final Logger log = Logger.getLogger(MailingTemplateUtil.class);
	
	/**
	 * Creates a pdf file from a template in docx and a person list.
	 * The template gets filled for each person and added to one pdf file. 
	 * @param templateFile
	 * 			Docx template file with Velocity syntax in MergeFields 
	 * @param personList
	 * 			List of person to merge with the template file
	 * @param outputName
	 * 			Name of final pdf file
	 * @throws IOException
	 * @throws ServiceException
	 */
	public static void createMailingWithDocxTemplate(File templateFile, List<Person> personList, String outputName) throws IOException, ServiceException{
		List<File> files = new ArrayList<File>();
		IXDocReport report;
		File tempFile;
		IContext context;
		OutputStream out;
		
		//Check Arguments
		if(templateFile==null||personList==null||outputName==null){
			throw new IllegalArgumentException("Argument is null");
		}
		
		log.info("Create mailing with template "+templateFile.getName());
		
		try {
			//Load Template file
			report = XDocReportRegistry.getRegistry().loadReport(new FileInputStream(templateFile),
					TemplateEngineKind.Velocity);
			

			//Create context to put person data into the template fields
			context= report.createContext();

			//Create a pdf for each person
			for(Person p : personList){

				context.put("person", p);
				tempFile = File.createTempFile("MailingTemp", ".docx");
				
				files.add(tempFile);
				out = new FileOutputStream(tempFile);
				Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
				//set encoding for ä,ö,ü, etc.
				OptionsHelper.setFontEncoding(options, "ISO-8859-1");
				report.convert(context, options, out);

				log.info("Created pdf: "+tempFile);				
			}
		
			
		} catch (XDocReportException | NullPointerException e) {
			log.error(e.getMessage());
			throw new ServiceException(e);
		}
		
		//Merge into one pdf and delete temp files
		mergePdfs(files, new File(outputName));
	}
	
	/**
	 * Merges all pdf files into one pdf file and deletes all temp pdf files after that
	 * @param pdfs
	 * 		List of pdf files
	 * @param mergedPDF
	 * 		Final pdf file
	 * @throws IOException
	 * @throws ServiceException
	 */
	private static void mergePdfs(List<File> pdfs,File mergedPDF) throws IOException, ServiceException{
		Document pdfDocument = new Document();
		PdfCopy copy;
		PdfReader pdfReader;
		int pages;
		
		if(pdfs == null || mergedPDF == null){
			throw new IllegalArgumentException("Argument is null");
		}
		
		try{
			//Combine pdf files to one
			copy = new PdfCopy(pdfDocument, new FileOutputStream(mergedPDF));

			pdfDocument.open();

			//Read all pages and add them to the final pdf file
			for (File pdf : pdfs) {
				pdfReader = new PdfReader(new FileInputStream(pdf));
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
		for(File tempPDF : pdfs){
			tempPDF.delete();
		}
	}
}
