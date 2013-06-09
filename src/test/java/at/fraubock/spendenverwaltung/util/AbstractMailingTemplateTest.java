package at.fraubock.spendenverwaltung.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.lowagie.text.DocumentException;

import fr.opensagres.xdocreport.core.XDocReportException;

import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

public abstract class AbstractMailingTemplateTest {
	private static List<Person> persons;
	@Test
	public void trySimple() throws FileNotFoundException, IOException, XDocReportException, DocumentException, ServiceException {
		MailingTemplate.template(new File("src/test/resources/examplemailing.docx"), persons);
	}
	
	@Test
	public void tryMergePDFs() throws ServiceException{
		List<String> pdfs = new ArrayList<String>();
		pdfs.add("Output0.pdf");
		pdfs.add("Output1.pdf");
		
		MailingTemplate.mergePdfs(pdfs);
	}
	
	protected static void init(){
		persons = new ArrayList<Person>();
		
		Person person1;
		person1 = new Person();
		person1.setTitle("Herr");
		person1.setSurname("Maier");
		
		persons.add(person1);
		
		Person person2;
		person2 = new Person();
		person2.setTitle("Frau");
		person2.setSurname("Bauer");
		
		persons.add(person2);
	}

}
