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
public class SearchFinish extends NodeEvent {

    public SearchFinish(double time, DataNode node, RetrievalAgent agent) {
        super(time, node, agent);
    }

    @Override
    public void Occur(TopologyManager topology, AgentManager agent_manager, EventManager event_manage) {
        targetAgent.addReadDataNum();
        agent_manager.IncrementReadDataNum(targetAgent);
        event_manage.delSearchFinishFromDeleteList(targetNode, this);
        int get_score = 0;
        get_score = agent_manager.getDist(targetAgent).getScore();

        if (agent_manager.getMaxScore(targetAgent.getID()) < get_score) {
            agent_manager.setMaxScore(targetAgent.getID(), get_score);

        }
        //if(agent_manager.getReadDataNumOfThisAgent(targetAgent) < need_data_for_estimate){//for not update estimate average
        agent_manager.addGetScoreToList(targetAgent, get_score);
        //}
            if (agent_manager.getReadDataNumOfThisAgent(targetAgent) >= need_data_for_estimate) {//for update estimate average
            //if(agent_manager.getReadDataNumOfThisAgent(targetAgent)==need_data_for_estimate){//for not update estimate average
            double average;
            if (need_data_for_estimate > 0) {
		    if(estimate_type.equals("estimate")){
                average =agent_manager.getEstimateAverage(targetAgent);// for estimate average
		    }
		    else {
                average = agent_manager.getDist(targetAgent).getAverage();//for not estimate average
		    }
                targetAgent.setEstimateAverage(average);
                topology.addControlTargetAgent(targetNode, targetAgent);
            }
        }
        if (targetNode.getDataNum() > targetAgent.getReadDataNum()) {
            targetAgent.setDataSize();
            SearchStart search = new SearchStart(eventTime, targetNode, targetAgent);
            event_manage.addEvent(targetNode, search);
        } else {
            this.AgentDeparture(topology, agent_manager, event_manage, targetAgent, targetNode);
        }
    }
}
