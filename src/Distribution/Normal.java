/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Distribution;

import FuncRandom.RandManager;
import java.util.Random;
import org.apache.commons.math.analysis.integration.RombergIntegrator;
import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.math.random.RandomGenerator;

/**
 *
 * @author nariatsu
 */
public class Normal extends BaseOfDistribution {
	private double average;
	private double variance;

	private BaseOfDistribution BaseDist = new BaseOfDistribution();
        protected RombergIntegrator romberg;


	public Normal(RandManager rm, int max, int min, double avr, double var){
		super(rm, max,min);
		average = avr;
		variance = var;
		Exponential expo = new Exponential(rm, max, min, avr);
		realfunction = new ExponentialFunctionImpl(avr, expo);
	}
	/*public double f(double x){
		return 1/Math.sqrt(2*Math.PI*variance*variance)*Math.exp(-Math.pow(x-average,2)/2/Math.pow(average, 2));
	}

	public double F(){
		return average;
	}実際のスコアは正規分布、制御は指数分布*/
	public double f(double x, double estimate_average) {
		return Math.exp(-(x / estimate_average)) / estimate_average;
	}

	public double F(double x, double estimate_average) {//累積確率
		return 1 - Math.exp(-(x / estimate_average));
	}
	public int getScore(){
		RandomData randdata = new RandomDataImpl();
		int gausrand = (int)randdata.nextGaussian(average, variance);
		if(gausrand < 0) return 0;
		else if (gausrand > 10000) return 10000;
		return gausrand;
	}

	public double getAverage(){
		return average;
	}

	public double getVariance(){
		return variance;
	}
}
