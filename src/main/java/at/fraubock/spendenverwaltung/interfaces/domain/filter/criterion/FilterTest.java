package at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterToSqlBuilder;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class FilterTest {

	public static void main(String[] args) {

//		PropertyCriterion donProp = new PropertyCriterion(FilterType.DONATION,FilterProperty.DONATION_AMOUNT, RelationalOperator.GREATER_EQ);
//		donProp.setNumValue(100D);
//
//		Filter donFilter = new Filter(FilterType.DONATION,donProp);
//
//		PropertyCriterion prop1 = new PropertyCriterion(FilterType.PERSON,FilterProperty.PERSON_GIVENNAME, RelationalOperator.EQUALS);
//		prop1.setStrValue("hans");
//
//		PropertyCriterion prop2 = new PropertyCriterion(FilterType.PERSON,FilterProperty.PERSON_EMAIL, RelationalOperator.EQUALS);
//		prop2.setStrValue("c");
//
//		ConnectedCriterion log = new ConnectedCriterion(FilterType.PERSON,LogicalOperator.AND,prop1);
//		log.setOperand2(prop2);
//
//		MountedFilterCriterion filterForDon = new MountedFilterCriterion(FilterType.PERSON,donFilter,RelationalOperator.LESS);
//		filterForDon.setCount(5);
//
//		ConnectedCriterion log2 = new ConnectedCriterion(FilterType.PERSON,LogicalOperator.AND, filterForDon);
//		log2.setOperand2(log);
//
//		Filter personFilter = new Filter(FilterType.PERSON, log2);

//		ApplicationContext context = new ClassPathXmlApplicationContext(
//				"/spring.xml");
//		IPersonDAO personDAO = context.getBean("personDao", IPersonDAO.class);
//
//		try {
//			List<Person> persons = personDAO.getByFilter(personFilter);
//			for (Person p : persons) {
//				System.out.println(p.getId());
//			}
//		} catch (PersistenceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		System.out.println(FilterToSqlBuilder.createSqlStatement(personFilter));

	}

}
