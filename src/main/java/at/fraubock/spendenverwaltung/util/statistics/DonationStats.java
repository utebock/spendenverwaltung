package at.fraubock.spendenverwaltung.util.statistics;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;

public class DonationStats {
	
	private static final Logger log = Logger.getLogger(DonationStats.class);

	/**
	 * @param donationList List containing all donations of used filter
	 * @return Mean of donation amounts
	 */
	public double getMean(List<Donation> donationList){
		DescriptiveStatistics stat = new DescriptiveStatistics();
		List<Double> statsList = new ArrayList<Double>();
		for(int i = 0; i < donationList.size(); i++){
			statsList.add((donationList.get(i).getAmount()).doubleValue());
		}
		
		double[] array = new double[statsList.size()];
		
		for(int i = 0; i < statsList.size(); i++){
			array[i] = statsList.get(i);
			stat.addValue(array[i]);
		}
		log.info("Mean: "+stat.getMean()+"\n");
		return stat.getMean();
	}
	
	/**
	 * @param donationList List containing all donations of used filter
	 * @return Sum of donation amounts
	 */
	public double getSum(List<Donation> donationList){
		DescriptiveStatistics stat = new DescriptiveStatistics();
		List<Double> statsList = new ArrayList<Double>();
		
		for(int i = 0; i < donationList.size(); i++){
			statsList.add((donationList.get(i).getAmount()).doubleValue());
		}
		
		double[] array = new double[statsList.size()];
		
		for(int i = 0; i < statsList.size(); i++){
			array[i] = statsList.get(i);
			stat.addValue(array[i]);
		}
		
		log.info("Sum: "+stat.getSum()+"\n");
		return stat.getSum();
	}
	
	/**
	 * @param donationList List containing all donations of used filter
	 * @return Minimum of donation amounts
	 */
	public double getMin(List<Donation> donationList){
		DescriptiveStatistics stat = new DescriptiveStatistics();
		List<Double> statsList = new ArrayList<Double>();
		for(int i = 0; i < donationList.size(); i++){
			statsList.add((donationList.get(i).getAmount()).doubleValue());
		}
		
		double[] array = new double[statsList.size()];
		
		for(int i = 0; i < statsList.size(); i++){
			array[i] = statsList.get(i);
			stat.addValue(array[i]);
		}
		log.info("Minimum: "+stat.getMin()+"\n");
		return stat.getMin();
	}
	
	/**
	 * @param donationList List containing all donations of used filter
	 * @return Maximum of donation amounts
	 */
	public double getMax(List<Donation> donationList){
		DescriptiveStatistics stat = new DescriptiveStatistics();
		List<Double> statsList = new ArrayList<Double>();
		for(int i = 0; i < donationList.size(); i++){
			statsList.add((donationList.get(i).getAmount()).doubleValue());
		}
		
		double[] array = new double[statsList.size()];
		
		for(int i = 0; i < statsList.size(); i++){
			array[i] = statsList.get(i);
			stat.addValue(array[i]);
		}
		log.info("Maximum: "+stat.getMax()+"\n");
		return stat.getMax();
	}
	
	/**
	 * @param donationList List containing all donations of used filter
	 * @return Number of donation amounts
	 */
	public double getCount(List<Donation> donationList){
		log.info("ListSize: "+donationList.size()+"\n");
		return donationList.size();
	}
	
	/**
	 * @param donationList List containing all donations of used filter
	 * @return Median of donation amounts
	 */
	public double getMedian(List<Donation> donationList){
		DescriptiveStatistics stat = new DescriptiveStatistics();
		List<Double> statsList = new ArrayList<Double>();
		for(int i = 0; i < donationList.size(); i++){
			statsList.add((donationList.get(i).getAmount()).doubleValue());
		}
		
		double[] array = new double[statsList.size()];
		
		for(int i = 0; i < statsList.size(); i++){
			array[i] = statsList.get(i);
			stat.addValue(array[i]);
		}
		log.info("Median: "+stat.getPercentile(50)+"\n");
		return stat.getPercentile(50);
	}
}
