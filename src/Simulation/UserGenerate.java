/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulation;

import FuncRandom.RandManager;
import MobileAgent.AgentManager;
import MobileAgent.RetrievalAgent;
import NetWork.DataNode;
import NetWork.TopologyManager;

/**
 *
 * @author N_Atsushi
 */
public class UserGenerate extends NodeEvent {

	RandManager rand_manager;
	int UserNumber;
	double arrival_rate;
	int user_type = 0;// 0:exponential_dist,1:normal_dist

	UserGenerate(RandManager rm, double arr, double time, int UserNum) {
		eventTime = time;
		UserNumber = UserNum;
		rand_manager = rm;
		arrival_rate = arr;
	}

	UserGenerate(double time, DataNode node, RetrievalAgent agent) {
		super(time, node, agent);
	}

	@Override
	public void Occur(TopologyManager topology, AgentManager agent_manager, EventManager event_manage) {
		RetrievalAgent firstAgent = new RetrievalAgent(rand_manager, UserNumber);
		agent_manager.InitAgentManager(firstAgent);
		DataNode firstNode = topology.RandNextNode();
		agent_manager.putFirstArriveNode(firstAgent, firstNode);
		TimeOver timeover = new TimeOver(eventTime + 500, firstAgent);
		event_manage.addEvent(firstNode, timeover);
		agent_manager.setLimitTime(firstAgent, eventTime + 500);
		Arrive agent_arrive = new Arrive(eventTime, firstNode, firstAgent);
		event_manage.addEvent(firstNode, agent_arrive);
		/*next user generate event*/
		double nextEventTime = eventTime + rand_manager.genExpRand(arrival_rate);
		DataNode nextGenNode = topology.RandNextNode();
		UserNumber += 1;
		UserGenerate newUser = new UserGenerate(rand_manager, arrival_rate, nextEventTime, UserNumber);
		event_manage.addEvent(nextGenNode, newUser);
	}
}
