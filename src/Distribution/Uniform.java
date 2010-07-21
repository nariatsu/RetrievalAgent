/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Distribution;

import FuncRandom.RandManager;
import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

/**
 *
 * @author nariatsu
 */
public class Uniform extends BaseOfDistribution {
	private double max,min;
	private double average;

	public Uniform(RandManager rm, int max, int min){
		super(rm, max,min);
		double average = (max+min)/2;
		Exponential expo = new Exponential(rm, max, min, average);
		realfunction = new ExponentialFunctionImpl(average, expo);
	}
	public int getScore(){
		RandomData rand = new RandomDataImpl();
		return (int)rand.nextUniform(0, 10000);
	}
	public double getAverage(){
		return average;
	}
}
