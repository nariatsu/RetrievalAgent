/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Simulation;

import MobileAgent.AgentManager;
import MobileAgent.RetrievalAgent;
import NetWork.DataNode;
import NetWork.Node;
import NetWork.TopologyManager;
import java.util.LinkedList;

/**
 *
 * @author nariatsu
 */
public class TimeOver extends NodeEvent{
	public TimeOver(double time, RetrievalAgent agent){
		super(time,agent);
	}
	public void Occur(TopologyManager topology,AgentManager agent_manager,EventManager event_manage){
		LinkedList<Node> checknode = new LinkedList<Node>();
		checknode.addAll(agent_manager.getExistNodeList(targetAgent.getID()));
		for(Node element : checknode){
			RetrievalAgent delAgent = topology.ReturnSameIDAgent((DataNode)element, targetAgent);
			this.AgentDeparture(topology, agent_manager, event_manage, delAgent, (DataNode)element);
			
			LinkedList<NodeEvent> deleteEvent = new LinkedList<NodeEvent>();
			for(NodeEvent checkevent : event_manage.getEventList(element)){
				if(checkevent instanceof WorkingStart || checkevent instanceof UserGenerate){
				}
				else{
					if(checkevent.getAgent().getID()==targetAgent.getID()){
						deleteEvent.add(checkevent);
					}
				}
			}
                        event_manage.getEventList(element).removeAll(deleteEvent);
		}
	}
}
