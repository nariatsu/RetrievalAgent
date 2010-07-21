/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Distribution;

/**
 *
 * @author nariatsu
 */
public class ExponentialFunctionImpl extends ExponentialFunction{
    private double average;
    private Exponential exponential;
    private double estimate;
    private int datanum;

    public ExponentialFunctionImpl(double avr, Exponential expo){
        super();
        average = avr;
        exponential = expo;
    }

    public void setPara(int data, double est_score){
        estimate=est_score;
        datanum=data;
    }
    public double value(double x){
        return datanum * x * (exponential.F_under(x, datanum - 1, estimate)) * exponential.f(x,estimate);
    }
}
