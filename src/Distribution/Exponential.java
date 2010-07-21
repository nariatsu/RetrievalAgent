/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution;

import FuncRandom.RandManager;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.integration.RombergIntegrator;

/**
 *
 * @author nariatsu
 */
public class Exponential extends BaseOfDistribution {

	private BaseOfDistribution BaseDist = new BaseOfDistribution();
	private double average;
        protected RombergIntegrator romberg;

	public Exponential(RandManager rm, int max, int min, double avr) {
		super(rm, max, min);
		average = avr;
                realfunction = new ExponentialFunctionImpl(avr, this);
	}
        /*UnivariateRealFunction realfunction = new UnivariateRealFunction() {

        public double value(double x) throws FunctionEvaluationException {
         //   throw new UnsupportedOperationException("Not supported yet.");
            return 1/average * Math.exp(-1/average*x);
        }

    };*/
//exp_dist::exp_dist(int max,int min, double avr):dist_base(max,min)
//{
//	avr_score = avr;
//	int plus_minus=node_rand(9);
//	if(plus_minus<=4){estimate_avr=avr_score*0.60;}
//	else estimate_avr=avr_score*1.40;
//}

	public double f(double x, double estimate_average) {
		return Math.exp(-(x / estimate_average)) / estimate_average;
	}

	public double F(double x, double estimate_average) {//累積確率
		return 1 - Math.exp(-(x / estimate_average));
	}

	public int getScore() {
		double x;
		int tmp_x;
		x = random.genExpRand(1 / average);
		tmp_x = (int) x;
		if (tmp_x > 0 && tmp_x <= maximum) {
			return tmp_x;
		} else if (tmp_x > maximum){
			if (maximum == 1) {
				System.out.println("maximum is 1");
			}
			return maximum;
		}
		else {
			return 1;
		}
	}
    @Override

	public double getAverage() {
		return average;
	}
}