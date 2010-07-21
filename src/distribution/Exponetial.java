/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package distribution;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

/**
 *
 * @author nariatsu
 */
public class Exponetial extends BaseDist {
	public double getValue(){
		RandomData rand = new RandomDataImpl();
		return rand.nextExponential(average);
	}
	public double getAverage(){
		return average;
	}

	public void setPDF(){

	}

}
