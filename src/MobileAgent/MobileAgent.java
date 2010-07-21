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
public abstract class MobileAgent {
	MobileAgent(RandManager rm,int user_num){
		rand_manager = rm;
		agentID=user_num;
	}
	MobileAgent(MobileAgent OriginalAgent){
		rand_manager = OriginalAgent.getRand();
		agentID=OriginalAgent.getID();
		user_type=OriginalAgent.getUserType();
	}
	RandManager rand_manager;
	int agentID;
	int WorkingNode;
	int agentSTATUS;//0で稼動状態、1で停止状態（クローン中orクローン待ち）
	int user_type;//0:exponential,1:normal
	protected double proc_start_time;

	public RandManager getRand(){
		return rand_manager;
	}
	public int getID(){return agentID;}
	public int getNodeID(){
		return WorkingNode;
	}
	public void knowNodeID(int NODEID){
		WorkingNode=NODEID;
	}
	public boolean Working(){
		if(agentSTATUS==0){
			return true;
		}
		else
			return false;
	}
	public Object makeClone(){
		/*Object CloneAgent = new Object(this);
		 */
		Object CloneAgent = new Object();
		return CloneAgent;

	}
	public int getUserType(){
		return user_type;
	}
	public void setProcStartTime(double time){
		proc_start_time=time;
	}
	public double getProcTime(double time){
		return time - proc_start_time;
	}
}
