/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FuncRandom;

import java.util.Calendar;
import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

/**
 *
 * @author N_Atsushi
 */
public class RandManager {
    RandomData randdata;
	public RandManager() {
		Calendar cal = Calendar.getInstance();
		int minute = cal.get(Calendar.MINUTE);
                randdata = new RandomDataImpl();
		//mt.initBySeed(minute);
	}

	public int getIntRand(int min, int max) {//generate int random from min to max
            return randdata.nextInt(min, max);
		//return (int) (mt.generateRandInt32() % (max - min) + min);
	}

	public double genExpRand(double mu) {//genarate exp random having avr
		if (mu == 0) {
			System.out.println("Alart mu =0");
		}
                double return_x = randdata.nextExponential(1/mu);
		return return_x;
	}
}
