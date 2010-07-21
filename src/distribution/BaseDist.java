/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package distribution;

/**
 *
 * @author nariatsu
 */
public abstract class BaseDist implements Dist{
	protected double random_value,average,variance;
	protected double min,max;


	public double getValue(){
		return random_value;
	}
	public double getAverage(){
		return average;
	}
	public double getVariance(){
		return variance;
	}
}
