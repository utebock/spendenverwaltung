package at.fraubock.spendenverwaltung.util.statistics;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;

public class DonationStats {
	
	private IDonationService donationService;
	
	public DonationStats(IDonationService donationService){
		this.donationService = donationService;
	}
	
	public IDonationService getDonationService(){
		return donationService;
	}
	
	public void setDonationService(IDonationService donationService){
		this.donationService = donationService;
	}
	
	/**
	 * @param donationList List containing all donations of used filter
	 * @return Mean of donation amounts
	 */
	public double getMean(List<Donation> donationList){
		DescriptiveStatistics stat = new DescriptiveStatistics();
		List<Double> statsList = new ArrayList<Double>();
		double[] array = new double[0];
		
		for(int i = 0; i < donationList.size(); i++){
			statsList.add((donationList.get(i).getAmount()).doubleValue());
			array[i] = statsList.get(i);
			stat.addValue(array[i]);
		}
		return stat.getMean();
	}
	
	/**
	 * @param donationList List containing all donations of used filter
	 * @return Sum of donation amounts
	 */
	public double getSum(List<Donation> donationList){
		DescriptiveStatistics stat = new DescriptiveStatistics();
		List<Double> statsList = new ArrayList<Double>();
		double[] array = new double[0];
		
		for(int i = 0; i < donationList.size(); i++){
			statsList.add((donationList.get(i).getAmount()).doubleValue());
			array[i] = statsList.get(i);
			stat.addValue(array[i]);
		}
		return stat.getSum();
	}
	
	/**
	 * @param donationList List containing all donations of used filter
	 * @return Minimum of donation amounts
	 */
	public double getMin(List<Donation> donationList){
		DescriptiveStatistics stat = new DescriptiveStatistics();
		List<Double> statsList = new ArrayList<Double>();
		double[] array = new double[0];
		
		for(int i = 0; i < donationList.size(); i++){
			statsList.add((donationList.get(i).getAmount()).doubleValue());
			array[i] = statsList.get(i);
			stat.addValue(array[i]);
		}
		return stat.getMin();
	}
	
	/**
	 * @param donationList List containing all donations of used filter
	 * @return Maximum of donation amounts
	 */
	public double getMax(List<Donation> donationList){
		DescriptiveStatistics stat = new DescriptiveStatistics();
		List<Double> statsList = new ArrayList<Double>();
		double[] array = new double[0];
		
		for(int i = 0; i < donationList.size(); i++){
			statsList.add((donationList.get(i).getAmount()).doubleValue());
			array[i] = statsList.get(i);
			stat.addValue(array[i]);
		}
		return stat.getMax();
	}
	
	/**
	 * @param donationList List containing all donations of used filter
	 * @return Number of donation amounts
	 */
	public double getCount(List<Donation> donationList){
		return donationList.size();
	}
	
	/**
	 * @param donationList List containing all donations of used filter
	 * @return Median of donation amounts
	 */
	public double getMedian(List<Donation> donationList){
		DescriptiveStatistics stat = new DescriptiveStatistics();
		List<Double> statsList = new ArrayList<Double>();
		double[] array = new double[0];
		
		for(int i = 0; i < donationList.size(); i++){
			statsList.add((donationList.get(i).getAmount()).doubleValue());
			array[i] = statsList.get(i);
			stat.addValue(array[i]);
		}
		return stat.getPercentile(50);
	}
}
