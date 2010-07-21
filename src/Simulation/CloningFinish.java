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
public class CloningFinish extends NodeEvent {
	public CloningFinish(double time, DataNode node, RetrievalAgent agent){
		super(time,node,agent);
	}
	public void Occur(TopologyManager topology,AgentManager agent_manager,EventManager event_manage){
		targetNode.setNotCloning();
		agent_manager.delCloningNode(targetAgent, targetNode);
		agent_manager.addWorkingNode(targetAgent, targetNode);
		topology.addWorkingAgent(targetNode, targetAgent);
		targetAgent.setDataSize();
		agent_manager.setResourceTargetAgent(targetAgent);

		if(topology.NoAgentForWaitingClone(targetNode)){
			WorkingStart workstart = new WorkingStart(eventTime, targetNode);
			event_manage.addEvent(targetNode, workstart);
		}
		else {
			RetrievalAgent next_clone_agent = topology.popCloneWaitAgent(targetNode);
			CloningStart clonstart = new CloningStart(eventTime, targetNode, next_clone_agent);
			event_manage.addEvent(targetNode, clonstart);
		}
	}

}
