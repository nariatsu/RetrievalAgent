/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package distribution;

/**
 *
 * @author nariatsu
 */
public interface Dist {
	public double getValue();//return value followed to the distribution
	public double getAverage();//return average of the distribution
	public double getVariance();//return variance of the distribution

	public void setPDF();//probability density function
}
