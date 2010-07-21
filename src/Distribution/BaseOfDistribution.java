/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution;

import FuncRandom.RandManager;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.integration.LegendreGaussIntegrator;
import org.apache.commons.math.analysis.integration.RombergIntegrator;
import org.apache.commons.math.optimization.fitting.HarmonicFunction;

/**
 *
 * @author nariatsu
 */
public class BaseOfDistribution {

    double interval = 1;
    protected int maximum;
    protected int minimum;
    protected RandManager random;
    protected RombergIntegrator romberg;
    //protected UnivariateRealFunction realfunction;
    protected ExponentialFunction realfunction;


    public BaseOfDistribution() {
    }

    public BaseOfDistribution(RandManager rm, int max, int min) {
        random = rm;
        maximum = max;
        minimum = min;
        romberg = new RombergIntegrator();
        romberg.setRelativeAccuracy(1.0);
    }

    public double f(double x, double estimate) {
        return x;
    }

    public double F(double x, double estimate) {
        return x;
    }

    public double F_under(double x, int e, double estimate) {
        double lower = 0.00;
        if (e == 0) {
            return 1.0;
        } else {
            if (x != lower) {
                return Math.pow(F(x, estimate) - f(x, estimate) - F(lower, estimate), (double) e);
            } else {
                return Math.pow(f(x, estimate), (double) e);
            }
        }

    }

    public double cal_expect(double got_score, double estimate, int data_num) throws MaxIterationsExceededException, FunctionEvaluationException{
        realfunction.setPara(data_num, estimate);
        return romberg.integrate(realfunction, got_score, 10001);
    }
    public double cal_expect(double l_p, int n, int k, double estimate) {
        double sum = 0.0;
        for (double x = l_p; x <= maximum; x += interval) {
            sum += n * interval * (x - l_p) * (F_under(x, n - k, estimate)) * f(x, estimate);
        }
        return sum;
    }

    public int getScore() {
        return maximum;
    }

    public double getAverage() {
        return maximum;
    }
}
