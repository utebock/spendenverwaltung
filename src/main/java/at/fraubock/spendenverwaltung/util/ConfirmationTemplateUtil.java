/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.util;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.util.IOUtils;
import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTAltChunk;

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

import at.fraubock.spendenverwaltung.gui.views.CreateMailingsView.SupportedFileFormat;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

/**
 * 
 * @author romanvoglhuber
 *
 */
public class ConfirmationTemplateUtil {

	private static final Logger log = Logger.getLogger(ConfirmationTemplateUtil.class);
	  private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";  
	
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
	public static void createMailingWithDocxTemplate(File templateFile, List<Donation> donationList, String outputName) throws IOException, ServiceException{
		List<File> files = new ArrayList<File>();
		IXDocReport report;
		File tempFile;
		IContext context;
		OutputStream out;
		
		//Check Arguments
		if(templateFile==null||donationList==null||outputName==null){
			throw new IllegalArgumentException("Argument is null");
		}
		
		
		if(outputName.endsWith(".pdf")){
			
			log.info("Create pdf mailing with template "+templateFile.getName());
			
			try {
				//Load Template file
				report = XDocReportRegistry.getRegistry().loadReport(new FileInputStream(templateFile),
						TemplateEngineKind.Velocity);
				

				//Create context to put person data into the template fields
				context= report.createContext();

				//Create a pdf for each person
				for(Donation d : donationList){

					context.put("donation", d);
					tempFile = File.createTempFile("ConfirmationTemp", ".docx");
					
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
		} else if(outputName.endsWith(".docx")){
			
			log.info("Create docx confirmation with template "+templateFile.getName());
			
			try {
				//Load Template file
				report = XDocReportRegistry.getRegistry().loadReport(new FileInputStream(templateFile),
						TemplateEngineKind.Velocity);
				

				//Create context to put person data into the template fields
				context= report.createContext();
				int counter = 0;
				
				//Create a docx for each person
				for(Donation d : donationList){

				      context.put("donation", d);

				      //Generate report by merging Java model with the Docx
				      out = new FileOutputStream(new File("confirmation-temp"+counter+".docx"));
				      report.process(context, out);		
				      files.add(new File("confirmation-temp"+counter+".docx"));
				      counter++;
				}
			
				
			} catch (XDocReportException | NullPointerException e) {
				log.error(e.getMessage());
				throw new ServiceException(e);
			}
			
			//Merge into one docx and delete temp files
			mergeDocx(files, new File(outputName));
		}
		
		Desktop.getDesktop().open(new File(outputName));
	}
	
	private static void mergeDocx(List<File> files, File outputFile){
        DocxService docxService = new DocxService();

        // Create a list of streams to merge
        List<InputStream> streams = new ArrayList<InputStream>();
        for (File file : files) {
            try {
                streams.add(new FileInputStream(file));
                file.delete();
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            }
        }
        // Merge streams
        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(IOUtils.toByteArray(docxService.mergeDocx(streams, outputFile)));
            fos.close();
        } catch (Docx4JException d4je) {
            d4je.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
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
