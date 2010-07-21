/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Simulation;

import MobileAgent.AgentManager;
import MobileAgent.RetrievalAgent;
import NetWork.DataNode;
import NetWork.TopologyManager;
import java.util.LinkedList;

/**
 *
 * @author nariatsu
 */
public class CloningStart extends NodeEvent{
	public CloningStart(double time, DataNode node, RetrievalAgent agent) {
		super(time,node,agent);
	}

	public void Occur(TopologyManager topology,AgentManager agent_manager,EventManager event_manage){
		targetNode.setCloning();
		double now_time = eventTime;
		for(DataNode element : agent_manager.knowNextGoNode(targetAgent, targetNode, topology)){
			RetrievalAgent agent = targetAgent.makeClone();
			now_time+=0;
			agent_manager.addExistNodeList(agent.getID(), element);//
			Arrive arrive = new Arrive(now_time, element, agent);
			event_manage.addEvent(element, arrive);
		}
		CloningFinish clonefinish = new CloningFinish(now_time, targetNode, targetAgent);
		event_manage.addEvent(targetNode, clonefinish);
	}
}