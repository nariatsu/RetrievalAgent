/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Distribution;

import org.apache.commons.math.analysis.UnivariateRealFunction;

/**
 *
 * @author nariatsu
 */
public abstract class NormalDistFunction implements UnivariateRealFunction{

 public NormalDistFunction(){}

    public void setPara(int datanum,double est){

    }
    public double value(double x){
        return x;
    }
}
