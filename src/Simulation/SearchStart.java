/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Simulation;

import MobileAgent.AgentManager;
import MobileAgent.RetrievalAgent;
import NetWork.DataNode;
import NetWork.TopologyManager;

/**
 *
 * @author nariatsu
 */
public class SearchStart extends NodeEvent {
	public SearchStart(double time, DataNode node, RetrievalAgent agent){
		super(time,node,agent);
	}
	@Override
	public void Occur(TopologyManager topology,AgentManager agent_manager,EventManager event_manage){
		targetAgent.setStartWorkTime(eventTime);
		double time_for_one_data = targetAgent.getRestSize()/agent_manager.getResourceOfAgent(targetAgent);
		SearchFinish seach_fin = new SearchFinish(eventTime+time_for_one_data,targetNode,targetAgent);
		//event_manage.addEvent(targetNode, seach_fin);
		event_manage.addSerchFinish(targetNode, seach_fin);
	}

}
