/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulation;

import MobileAgent.AgentManager;
import MobileAgent.MobileAgent;
import MobileAgent.RetrievalAgent;
import NetWork.DataNode;
import NetWork.TopologyManager;
import java.util.LinkedList;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

/**
 *
 * @author N_Atsushi
 */
public class NodeEvent {

	protected NodeEvent() {
	}

	protected NodeEvent(double time, RetrievalAgent agent) {
		eventTime = time;
		targetAgent = agent;
	}

	protected NodeEvent(double time, DataNode node, RetrievalAgent agent) {
		eventTime = time;
		targetAgent = agent;
		targetNode = node;
	}
	protected double eventTime;
	protected RetrievalAgent targetAgent;
	protected DataNode targetNode;
	static int user_num;
	static int need_data_for_estimate;
	static String estimate_type;

	public double getTime() {
		return eventTime;
	}

	public DataNode getNode() {
		return targetNode;
	}

	public MobileAgent getAgent() {
		return targetAgent;
	}

	public void Occur(TopologyManager topology, AgentManager agent_manager, EventManager event_manage) {
	}

	public void ControlAgent(TopologyManager topology, AgentManager agent_manager, EventManager event_manager) throws MaxIterationsExceededException, FunctionEvaluationException {
		LinkedList<RetrievalAgent> CopyTargetList = new LinkedList<RetrievalAgent>();
		LinkedList<RetrievalAgent> CopyWorkList = new LinkedList<RetrievalAgent>();
		LinkedList<RetrievalAgent> controltargetlist = new LinkedList<RetrievalAgent>();
		//CopyWorkingList.addAll(topology.WorkingAgentList(targetNode));
		CopyTargetList.addAll(topology.getControlTargetList(targetNode));
		CopyWorkList.addAll(topology.getWorkingAgentList(targetNode));
		double exp_sum = 0;
		double exp_sum2 = 0;
		double miniexp = 999999;
		int flg = 0;
		RetrievalAgent miniexpagent = null;
		RetrievalAgent miniexpagent2 = null;
		for (RetrievalAgent element : CopyTargetList) {
			double max_score = agent_manager.getMaxScore(element.getID());
			double est_avr = element.getEstimateAverage();
			int restnum = targetNode.getDataNum() - element.getReadDataNum();
			double rest_time = agent_manager.getLimitTime(element) - eventTime;
			double resource = agent_manager.getResourceOfAgent(element);
			double node_resource = targetNode.getResource();
			double datanum_parsec = node_resource / 100;
			int readnum = (int) (rest_time * resource / node_resource * datanum_parsec);
			int readablenum = Math.min(restnum, readnum);
			//double agentexp = agent_manager.getDist(element).cal_expect(max_score, readablenum, 1, est_avr);
			double agentexp = agent_manager.getDist(element).cal_expect(max_score, est_avr, readablenum);
			if (agentexp < miniexp) {
				miniexp = agentexp;
				miniexpagent = element;
			}
			exp_sum += agentexp;
		}
		while (flg == 0) {
			if (!CopyTargetList.remove(miniexpagent) || !CopyWorkList.remove(miniexpagent)) {
				System.out.println("NoAgentInWorkingList " + targetNode.getID());
			}
			miniexp = 9999999;
			for (RetrievalAgent element : CopyTargetList) {
				double max_score = agent_manager.getMaxScore(element.getID());
				double est_avr = element.getEstimateAverage();
				int restnum = targetNode.getDataNum() - element.getReadDataNum();
				double rest_time = agent_manager.getLimitTime(element) - eventTime;
				int readnum = (int) (rest_time * agent_manager.getResourceOfAgent(element) / 100 * (CopyWorkList.size() + 1) / CopyWorkList.size());
				int readablenum = Math.min(restnum, readnum);
				//double agentexp = agent_manager.getDist(element).cal_expect(max_score, readablenum, 1, est_avr);
				double agentexp = agent_manager.getDist(element).cal_expect(max_score, est_avr, readablenum);
				if (agentexp < 0) {
					System.out.println(" Minus exp");
				}
				if (agentexp < miniexp) {
					miniexp = agentexp;
					miniexpagent2 = element;
				}
				exp_sum2 += agentexp;
			}
			if (exp_sum < exp_sum2) {
				controltargetlist.add(miniexpagent);
				exp_sum = exp_sum2;
				miniexpagent = miniexpagent2;
				exp_sum2 = 0;
				miniexp = 9999999;
			} else {
				flg = 1;
			}
		}
		for (RetrievalAgent element : controltargetlist) {
			double proc_time = eventTime - agent_manager.getArriveTime(element);
			/*if (proc_time == 0.0) {
				System.out.println("eventTime " + eventTime + " num " + agent_manager.getReadDataNum(element));
			}*/
			agent_manager.addProcTime(element.getID(), proc_time);
			topology.WorkingAgentList(targetNode).remove(element);
			topology.getControlTargetList(targetNode).remove(element);
			agent_manager.delExistNodeList(element.getID(), targetNode);
			if (agent_manager.NoAgentWorkingInTopology(element)) {
				double proctime = agent_manager.getProcTime(element.getID());
				agent_manager.addProcTimeList(proctime);
				/*set max score of all agents*/
				int max_score = (int) agent_manager.getMaxScore(element.getID());
				agent_manager.addScoreToList(max_score);
				/*set read data num */
				int read_data_num = agent_manager.getReadDataNum(element);
				agent_manager.addDataNumOfUser(read_data_num);
				agent_manager.delGetScoreMap(element.getID());
				/*delete information about all agents*/
				agent_manager.DeleteAgentManager(element);
				/*erase time out event*/
				DataNode firstnode = agent_manager.getFirstArriveNode(element);
				event_manager.RemoveEventUsingID(firstnode, element);
			}
			event_manager.RemoveEvent(targetNode, element);
			agent_manager.DeleteAgent(element);
		}
	}

	public void AgentDeparture(TopologyManager topology, AgentManager agentmanager, EventManager eventmanager, RetrievalAgent agent, DataNode node) {
		/*remove agent from workinglist and cloninglist*/
		double proc_time = eventTime - agentmanager.getArriveTime(agent);
		/*}if (proc_time == 0.0) {
			System.out.println("eventTime " + eventTime + " num " + agentmanager.getReadDataNum(agent));
		}*/
		agentmanager.addProcTime(agent.getID(), proc_time);
		topology.RemoveAgent(node, agent);
		agentmanager.delExistNodeList(agent.getID(), node);
		/*delete infomation about agent*/
		agentmanager.DeleteAgent(agent);
		if (agentmanager.NoAgentWorkingInTopology(agent)) {
			double proctime = agentmanager.getProcTime(agent.getID());
			agentmanager.addProcTimeList(proctime);
			/*set max score of all agents*/
			int max_score = agentmanager.getMaxScore(agent.getID());
			agentmanager.addScoreToList(max_score);
			/*set read data num of all agents*/
			int read_data_num = agentmanager.getReadDataNum(agent);
			agentmanager.addDataNumOfUser(read_data_num);

			/*delete information about all agents*/
			agentmanager.DeleteAgentManager(agent);
			//erase time out event
			DataNode firstnode = agentmanager.getFirstArriveNode(agent);
			eventmanager.RemoveEventUsingID(firstnode, agent);
			int agent_id = agent.getID();
			agentmanager.addFinishAgentList(agent_id);

		}
		for (RetrievalAgent element : topology.WorkingAgentList(node)) {
			double power = agentmanager.getResourceOfAgent(element);
			element.StopWork(eventTime, power);
			agentmanager.setResourceTargetAgent(element);
		}
		//eventmanager.RemoveSearchFinish(node);
		eventmanager.delSearchFinish(node);
		WorkingStart workstart = new WorkingStart(eventTime, node);
		eventmanager.addEvent(node, workstart);

	}
}
