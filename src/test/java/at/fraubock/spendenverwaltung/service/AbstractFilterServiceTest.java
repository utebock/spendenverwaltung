package at.fraubock.spendenverwaltung.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IMountedFilterCriterionDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter.FilterPrivacyStatus;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.to.FilterTO;
import at.fraubock.spendenverwaltung.interfaces.exceptions.FilterInUseException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.util.Pair;
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.filter.LogicalOperator;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;

public abstract class AbstractFilterServiceTest {

	protected IFilterService filterService;

	protected final IFilterDAO filterDAO = mock(IFilterDAO.class);
	protected final IDonationService donationService = mock(IDonationService.class);
	protected final IMountedFilterCriterionDAO mCriterionDAO = mock(IMountedFilterCriterionDAO.class);

	private FilterTO filterTO;
	private Filter createdFilter;

	/*
	 * testing create
	 */

	@Test(expected = ServiceException.class)
	public void createWithNullParameter_ThrowsException() throws Exception {
		filterService.create(null);
	}

	@Test(expected = ServiceException.class)
	public void createWithInvalidStateParameter_ThrowsException()
			throws Exception {
		filterService.create(new FilterTO());
	}

	@Test
	public void createWithValidParameter_ReturnsSavedFilter() throws Exception {
		// change id so that invocation count doesnt fail this test
		// (both create and update tests call insert on the DAO mock
		// so they should not equal each other)
		// newFilter.setId(3);
		Filter returnedFilter = filterService.create(filterTO);

		assertEquals(returnedFilter, createdFilter);
		verify(filterDAO).insert(createdFilter);

	}

	/*
	 * testing update
	 */

	@Test(expected = ServiceException.class)
	public void updateWithNullParameter_ThrowsException() throws Exception {
		filterService.update(null, null);
	}

	@Test(expected = ServiceException.class)
	public void updateWithInvalidStateParameter_ThrowsException()
			throws Exception {
		filterService.update(new Filter(), new FilterTO());
	}

	@Test
	public void updateWithValidParameters_ReturnsUpdatedFilter()
			throws Exception {
		Filter f = new Filter();
		f.setName("oldfilter");
		f.setAnonymous(true);
		f.setType(FilterType.DONATION);
		f.setPrivacyStatus(filterTO.getPrivacyStatus());
		PropertyCriterion propCrit = new PropertyCriterion();
		propCrit.compare(FilterProperty.DONATION_NOTE, RelationalOperator.LIKE, "testnote");
		propCrit.setId(0);
		f.setCriterion(propCrit);
		f.setId(0);
		
//		doAnswer(new Answer<Filter>() {
//
//            public Filter answer(InvocationOnMock invocation)
//                    throws Throwable {
//                Object[] args = invocation.getArguments();
//                if (args[0] instanceof Filter) {
//                	Filter filter = (Filter) args[0];
//                    filter.setId(1);
//                    
//                    if (args[1] instanceof Criterion) {
//                    	Criterion criterion = (Criterion) args[1];
//                    	criterion.setId(1);
//                    }
//                    
//                    return filter;
//                }
//
//                return null;
//            }
//        }).when(this.filterDAO).update(any(Filter.class),eq(f.getCriterion()));
		
		
		Filter returnedFilter = filterService.update(f, filterTO);
		
		
		createdFilter.setId(returnedFilter.getId());
		createdFilter.getCriterion().setId(returnedFilter.getCriterion().getId());
		assertEquals(createdFilter, returnedFilter);

//		verify(filterDAO).update(any(Filter.class),f.getCriterion());
	}

	/*
	 * testing delete
	 */

	@Test(expected = ServiceException.class)
	public void deleteWithNullParameter_ThrowsException() throws Exception {
		filterService.delete(null);
	}

	@Test(expected = ServiceException.class)
	public void deleteWithInvalidParameter_ThrowsException() throws Exception {
		filterService.delete(new Filter());
	}

	@Test(expected = FilterInUseException.class)
	public void deleteFilterInUse_ThrowsException() throws Exception {
		when(mCriterionDAO.getAllMountedFilterIds()).thenReturn(Arrays.asList(new Integer[]{1}));
		createdFilter.setId(1);
		filterService.delete(createdFilter);

		verify(mCriterionDAO).getAllMountedFilterIds();
	}

	@Test
	public void deleteFilter_RemovesEntity() throws Exception {
		createdFilter.setId(1);
		filterService.delete(createdFilter);

		verify(filterDAO).delete(createdFilter);

	}

	/*
	 * testing find
	 */

	@Test
	public void getAll_ReturnsAllEntities() throws Exception {

		List<Filter> allFilteres = new ArrayList<Filter>();
		allFilteres.add(createdFilter);
		Filter filter2 = new Filter();
		filter2.setName("name2");
		allFilteres.add(filter2);
		when(filterDAO.getAll()).thenReturn(allFilteres);
		when(filterDAO.getCurrentUserName()).thenReturn("ubadministrative");

		Pair<List<Filter>, String> result = filterService.getAll();
		assertNotNull(result);
		assertEquals(2, result.a.size());
		assertEquals(createdFilter, result.a.get(0));
		assertEquals(filter2, result.a.get(1));
		assertEquals("ubadministrative", result.b);

		verify(filterDAO).getAll();

	}

	@Test(expected = ServiceException.class)
	public void getWithNegativeId_ThrowsException() throws Exception {

		when(filterDAO.getById(-1)).thenThrow(new PersistenceException());

		filterService.getByID(-1);
	}

	@Test
	public void getWithValidId_ReturnsEntity() throws Exception {

		when(filterDAO.getById(1)).thenReturn(createdFilter);

		Filter foundFilter = filterService.getByID(1);

		assertEquals(foundFilter, createdFilter);

		verify(filterDAO).getById(1);

	}
	
	@Test(expected = ServiceException.class)
	public void getAllByFilterTypeWithNull_ThrowsException() throws Exception {
		filterService.getAllByFilter(null);
	}
	
	@Test
	public void getAllByFilterType_ReturnsFilters() throws Exception {
		List<Filter> allFilteres = new ArrayList<Filter>();
		allFilteres.add(createdFilter);
		Filter filter2 = new Filter();
		filter2.setName("name2");
		filter2.setType(FilterType.ADDRESS);
		allFilteres.add(filter2);

		when(filterDAO.getAll()).thenReturn(allFilteres);

		Pair<List<Filter>,String> foundFilters = filterService.getAllByFilter(FilterType.ADDRESS);

		assertEquals(foundFilters.a.get(0), filter2);
		assertEquals(foundFilters.a.size(), 1);

		verify(filterDAO).getAll();
		verify(filterDAO).getCurrentUserName();

	}
	
	@Test
	public void getAllByAnonymous_ReturnsFilters() throws Exception {
		List<Filter> allFilteres = new ArrayList<Filter>();
		allFilteres.add(createdFilter);
		Filter filter2 = new Filter();
		filter2.setName("name2");
		filter2.setAnonymous(false);
		allFilteres.add(filter2);

		when(filterDAO.getAll()).thenReturn(allFilteres);

		Pair<List<Filter>,String> foundFilters = filterService.getAllByAnonymous(false);

		assertEquals(foundFilters.a.get(0), createdFilter);
		assertEquals(foundFilters.a.get(1), filter2);
		assertEquals(foundFilters.a.size(), 2);

		verify(filterDAO).getAll();
		verify(filterDAO).getCurrentUserName();

	}
	
	@Test
	public void convertResultsToCSVById_ReturnsCSVString() throws Exception {
		when(filterDAO.getById(1)).thenReturn(createdFilter);
		List<Donation> dons = new ArrayList<Donation>();
		dons.add(new Donation());
		when(donationService.getByFilter(createdFilter)).thenReturn(dons);
		createdFilter.setType(FilterType.DONATION);
		
		filterService.convertResultsToCSVById(1);

		verify(donationService).getByFilter(createdFilter);
		verify(donationService).convertToCSV(dons);

	}
	
	@Test
	public void saveAsCSVById_ReturnsBoolean() throws Exception {
		when(filterDAO.getById(1)).thenReturn(createdFilter);
		List<Donation> dons = new ArrayList<Donation>();
		dons.add(new Donation());
		File csvFile = new File("csv");
		when(donationService.getByFilter(createdFilter)).thenReturn(dons);
		createdFilter.setType(FilterType.DONATION);
		
		filterService.saveResultsAsCSVById(1,csvFile);

		verify(donationService).getByFilter(createdFilter);
		verify(donationService).saveAsCSV(dons, csvFile);

	}

	@Before
	public void init() {
		filterTO = new FilterTO();
		filterTO.setAnonymous(false);
		filterTO.setName("testname");
		filterTO.setType(FilterType.PERSON);
		filterTO.setPrivacyStatus(FilterPrivacyStatus.PRIVATE);
		filterTO.setOperator(LogicalOperator.AND);

		PropertyCriterion propCrit1 = new PropertyCriterion();
		propCrit1.compare(FilterProperty.PERSON_WANTS_EMAIL, true);

		PropertyCriterion propCrit2 = new PropertyCriterion();
		propCrit2.compare(FilterProperty.PERSON_GIVENNAME,
				RelationalOperator.EQUALS, "testname");

		List<Criterion> crits = new ArrayList<Criterion>();
		crits.add(propCrit1);
		crits.add(propCrit2);

		filterTO.setCriterions(crits);

		// creating outcome of this TO
		createdFilter = new Filter();
		createdFilter.setName(filterTO.getName());
		createdFilter.setAnonymous(filterTO.isAnonymous());
		createdFilter.setType(filterTO.getType());
		createdFilter.setPrivacyStatus(filterTO.getPrivacyStatus());
		ConnectedCriterion conCrit = new ConnectedCriterion();
		conCrit.connect(filterTO.getCriterions().get(0),
				filterTO.getOperator(), filterTO.getCriterions().get(1));
		createdFilter.setCriterion(conCrit);
	}

}
