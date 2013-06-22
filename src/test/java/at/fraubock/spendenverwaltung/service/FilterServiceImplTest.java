package at.fraubock.spendenverwaltung.service;

import org.junit.Before;

public class FilterServiceImplTest extends AbstractFilterServiceTest {


	@Before
	public void setUpBeforeClass() {
		FilterServiceImplemented filterService = new FilterServiceImplemented();
		filterService.setFilterDAO(filterDAO);
		filterService.setDonationService(donationService);
		filterService.setMountedCritDAO(mCriterionDAO);
		super.filterService = filterService;
	}

}
