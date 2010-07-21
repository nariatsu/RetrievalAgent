/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Simulation;

import FuncRandom.RandManager;
import MobileAgent.AgentManager;
import NetWork.DataNode;
import NetWork.TopologyManager;

/**
 *
 * @author nariatsu
 */
public class SimulationInit extends NodeEvent{
//	SimulationInit(double time,DataNode node,RetrievalAgent agent){
//		super(time,node,agent);
//	}
	RandManager randmanager;
	double arrivalrate;
	public SimulationInit(RandManager rm,double arr,int data_num,int user_number, String estimatetype){
		randmanager = rm;
		arrivalrate=arr;
		need_data_for_estimate = data_num;
		user_num=user_number;
		estimate_type=estimatetype;
	}

	public void Occur(TopologyManager topology,AgentManager agent_manager,EventManager event_manager){
		
		double start_time=0;
		int user_ID=0;

		DataNode nextNode = topology.RandNextNode();
		//RetrievalAgent newAgent = new RetrievalAgent(0);

		UserGenerate generate_user = new UserGenerate(randmanager,arrivalrate, start_time, user_ID);
		//UserGenerate generate_event = new UserGenerate(start_time, nextNode, newAgent);
		event_manager.addEvent(nextNode, generate_user);
	}
}
