/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Simulation;

import MobileAgent.AgentManager;
import MobileAgent.RetrievalAgent;
import NetWork.DataNode;
import NetWork.TopologyManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

/**
 *
 * @author nariatsu
 */
public class WorkingStart extends NodeEvent{
	public WorkingStart(double time, DataNode node){
		eventTime=time;
		targetNode=node;
	}
	public void Occur(TopologyManager topology,AgentManager agent_manager,EventManager event_manage){
		agent_manager.setResourceEachAgent(targetNode.getResource(), topology.WorkingAgentList(targetNode));
		if(topology.getControlTargetList(targetNode).size()>1){
            try {
                this.ControlAgent(topology, agent_manager, event_manage);
            } catch (MaxIterationsExceededException ex) {
                Logger.getLogger(WorkingStart.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FunctionEvaluationException ex) {
                Logger.getLogger(WorkingStart.class.getName()).log(Level.SEVERE, null, ex);
            }
		}
		agent_manager.setResourceEachAgent(targetNode.getResource(), topology.WorkingAgentList(targetNode));
		for(RetrievalAgent element : topology.WorkingAgentList(targetNode)){
			SearchStart search_start = new SearchStart(eventTime, targetNode, element);
			event_manage.addEvent(targetNode, search_start);
		}
	}

}
