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
 * @author nariatsu
 */
public class Arrive extends NodeEvent {

    public Arrive(double time, DataNode node, RetrievalAgent agent) {
        super(time, node, agent);
    }

    @Override
    public void Occur(TopologyManager topology, AgentManager agent_manager, EventManager event_manager) {
        if (!agent_manager.knowArrivedNode(targetAgent.getID()).contains(targetNode)) {
            targetAgent.setDataSize();
	    targetAgent.setProcStartTime(eventTime);
	    //agent_manager.initProcTime(targetAgent.getID());
	    agent_manager.setArriveTime(targetAgent, eventTime);
            agent_manager.addArrivedNode(targetAgent.getID(), targetNode);
            agent_manager.addExistNodeList(targetAgent.getID(), targetNode);
            agent_manager.setDist(targetAgent);
            agent_manager.putGetScoreMap(targetAgent);
            agent_manager.setReadDataNumOfThisAgent(targetAgent);
            event_manager.delSearchFinish(targetNode);


            if (need_data_for_estimate == 0) {
                targetAgent.setEstimateAverage(agent_manager.getDist(targetAgent).getAverage());
                //for random 0
                //int random_ave = (int)Math.random() % 990+10;
                //targetAgent.setEstimateAverage(random_ave);
                 // uemade
                topology.addControlTargetAgent(targetNode, targetAgent);
            }

            for (RetrievalAgent element : topology.WorkingAgentList(targetNode)) {
                double power = agent_manager.getResourceOfAgent(element);
                element.StopWork(eventTime, power);
                agent_manager.setResourceTargetAgent(element);
            }

            if (!agent_manager.knowNextGoNode(targetAgent, targetNode, topology).isEmpty()) {
                agent_manager.addCloningNode(targetAgent, targetNode);
                if (targetNode.NodeIsCloning()) {
                    topology.addCloneWaitAgent(targetNode, targetAgent);
                } else {
                    CloningStart cloning = new CloningStart(eventTime, targetNode, targetAgent);
                    event_manager.addEvent(targetNode, cloning);
                }
            } else {
                topology.addWorkingAgent(targetNode, targetAgent);
                agent_manager.addWorkingNode(targetAgent, targetNode);
                agent_manager.setResourceTargetAgent(targetAgent);
                if (targetNode.NodeIsCloning()) {
                } else {
                    WorkingStart workstart = new WorkingStart(eventTime, targetNode);
                    event_manager.addEvent(targetNode, workstart);
                }
            }
        }
    }
}
