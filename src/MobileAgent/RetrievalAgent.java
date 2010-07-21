/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MobileAgent;

import FuncRandom.RandManager;

/**
 *
 * @author nariatsu
 */
public class RetrievalAgent extends MobileAgent{
	public RetrievalAgent(RandManager rm,int user_num){
		super(rm,user_num);
		read_data_number_in_the_node=0;
		if(rm.getIntRand(0, 100)%2==0){
			user_type=0;
		}
		else user_type=1;
	}
	public RetrievalAgent(MobileAgent OriginalAgent){
		super(OriginalAgent);
		read_data_number_in_the_node=0;

	}
	double one_data_size;
	double rest_data_size;
	double scoreAVR;//(実際のor予測のスコア分布の平均)
	double last_start_time=0;
	int read_data_number_in_the_node;
	//int user_type;//0:exponential,1:normal
	@Override
	public RetrievalAgent makeClone(){
		RetrievalAgent cloneAgent = new RetrievalAgent(this);
		return cloneAgent;
	}
	public void setDataSize(){
		double one_data_size_average = 100;
		one_data_size = rand_manager.genExpRand(1/one_data_size_average);
		rest_data_size=one_data_size;
		if(rest_data_size<0){
			System.out.println("rest_data_size<0 ");
		}
	}
	public void setRestSize(double stoppedtime,double resource){
		double read_data_size = (stoppedtime - last_start_time)*resource;
		rest_data_size -= read_data_size;
	}
	public double getRestSize(){
		return rest_data_size;
	}
	public boolean ScoringEndDisposal(double max_score,double get_score){
		++read_data_number_in_the_node;
		setDataSize();
		if(get_score > max_score){
			return true;
		}
		else return false;
	}
	public void addReadDataNum(){
		++read_data_number_in_the_node;
	}
	public int getReadDataNum(){
		return read_data_number_in_the_node;
	}
	public void StopWork(double time,double resource){
		rest_data_size -= (time-last_start_time)*resource;
		if(rest_data_size < 0){
			System.out.println("Data size is Minus");
		}
	}
	public void setStartWorkTime(double time){
		last_start_time=time;
	}
	public void setEstimateAverage(double average){
		scoreAVR=average;
	}
	public double getEstimateAverage(){
		return scoreAVR;
	}
}
