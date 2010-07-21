/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NetWork;

import MobileAgent.RetrievalAgent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collections;

/**
 *
 * @author nariatsu
 */
public class TopologyManager {

	private HashMap<Node, LinkedList> TopologyInfo = new HashMap<Node, LinkedList>();
	private HashMap<String, DataNode> NodeMap = new HashMap<String, DataNode>();
	private LinkedList<DataNode> NodeList = new LinkedList<DataNode>();
	private HashMap<DataNode, Double> ResourceInfo = new HashMap<DataNode, Double>();
	private HashMap<DataNode, LinkedList<RetrievalAgent>> WorkingAgentListinNode = new HashMap<DataNode, LinkedList<RetrievalAgent>>();//infomation about agent working in node.
	private HashMap<DataNode, LinkedList<RetrievalAgent>> ControlTargetAgentList = new HashMap<DataNode, LinkedList<RetrievalAgent>>();
	private HashMap<DataNode, LinkedList<RetrievalAgent>> CloneWaitAgentList = new HashMap<DataNode, LinkedList<RetrievalAgent>>();

	public DataNode getNode(int key) {
		return NodeMap.get(key);
	}

	public TopologyManager() throws FileNotFoundException, IOException {
		File INPUTFile = new File("scalefree.txt");
		FileReader INPUTData = new FileReader(INPUTFile);
		BufferedReader in = new BufferedReader(INPUTData);
		String one_line;
		LinkedList<String> alreadyGenNode = new LinkedList<String>();
		while ((one_line = in.readLine()) != null) {
			String[] node_num = one_line.split("  ");
                        if(!alreadyGenNode.contains(node_num[0])){
				//if(Collections.frequency(alreadyGenNode, node_num[0])==0){
				DataNode edge_node = new DataNode(node_num[0]);
				alreadyGenNode.push(node_num[0]);
				NodeMap.put(node_num[0], edge_node);
				LinkedList<RetrievalAgent> workingagent = new LinkedList<RetrievalAgent>();
				WorkingAgentListinNode.put(edge_node, workingagent);
				LinkedList<RetrievalAgent> controlagent = new LinkedList<RetrievalAgent>();
				ControlTargetAgentList.put(edge_node, controlagent);
				NodeList.push(edge_node);
				makeCloneWatiAgentList(edge_node);
				LinkedList<Node> linkednodes = new LinkedList<Node>();
				TopologyInfo.put(edge_node, linkednodes);
			}
			int line_position = 1;
			while (line_position < node_num.length) {
				DataNode node = new DataNode(node_num[line_position]);
				alreadyGenNode.push(node_num[line_position]);
				NodeMap.put(node_num[line_position], node);
				LinkedList<RetrievalAgent> workingagent = new LinkedList<RetrievalAgent>();
				WorkingAgentListinNode.put(node, workingagent);
				LinkedList<RetrievalAgent> controlagent = new LinkedList<RetrievalAgent>();
				ControlTargetAgentList.put(node, controlagent);
				NodeList.push(node);
				makeCloneWatiAgentList(node);
				LinkedList<Node> linked_nodes = new LinkedList<Node>();
				TopologyInfo.put(node, linked_nodes);
				TopologyInfo.get(NodeMap.get(node_num[0])).push(node);
				linked_nodes.push(NodeMap.get(node_num[0]));
				++line_position;
			}
		}
	}

	public LinkedList<DataNode> getLinkedNodeList(DataNode node) {
		return TopologyInfo.get(node);
	}

	public void addWorkingAgent(DataNode node, RetrievalAgent agent) {
		if (!WorkingAgentListinNode.get(node).contains(agent)) {
			WorkingAgentListinNode.get(node).push(agent);
		}
	}

	public void delWorkingAgent(DataNode node, RetrievalAgent agent) {
		WorkingAgentListinNode.get(node).remove(agent);
	}

	public void delWorkingAgentUsingID(DataNode node, RetrievalAgent agent) {
		LinkedList<RetrievalAgent> deleteagent = new LinkedList<RetrievalAgent>();
		for (RetrievalAgent element : WorkingAgentListinNode.get(node)) {
			if (element.getID() == agent.getID()) {
				deleteagent.add(element);
			}
		}
                WorkingAgentListinNode.get(node).removeAll(deleteagent);
	}

	public void delCloneWaitAgentUsingID(DataNode node, RetrievalAgent agent) {
		LinkedList<RetrievalAgent> deleteagent = new LinkedList<RetrievalAgent>();
		if (!CloneWaitAgentList.get(node).isEmpty()) {
			for (RetrievalAgent element : CloneWaitAgentList.get(node)) {
				if (element.getID() == agent.getID()) {
					deleteagent.add(element);
				}
			}
                        CloneWaitAgentList.get(node).removeAll(deleteagent);
		}
	}

	public int NumofWorkingAgent(DataNode node) {
		return WorkingAgentListinNode.get(node).size();
	}

	public LinkedList<RetrievalAgent> WorkingAgentList(DataNode node) {
		return WorkingAgentListinNode.get(node);
	}

	public void addCloneWaitAgent(DataNode node, RetrievalAgent agent) {
		if (!CloneWaitAgentList.get(node).contains(agent)) {
			CloneWaitAgentList.get(node).push(agent);
		}
	}

	public RetrievalAgent popCloneWaitAgent(DataNode node) {
		return (RetrievalAgent) CloneWaitAgentList.get(node).pollFirst();
	}

	public boolean NoAgentForWaitingClone(DataNode node) {
		if (CloneWaitAgentList.get(node).isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public void makeCloneWatiAgentList(DataNode node) {
		LinkedList list = new LinkedList();
		CloneWaitAgentList.put(node, list);
	}

	public LinkedList<DataNode> getNodeList() {
		return NodeList;
	}

	public DataNode RandNextNode() {
		Collections.shuffle(NodeList);
		return NodeList.getFirst();
	}

	public void RemoveAgent(DataNode node, RetrievalAgent agent) {
		LinkedList<RetrievalAgent> deleteagent = new LinkedList<RetrievalAgent>();
		for (RetrievalAgent element : WorkingAgentListinNode.get(node)) {
			if (element.getID() == agent.getID()) {
				deleteagent.add(element);
			}
		}
		if(deleteagent.size()>1){System.out.println("same ID in node");}
		for (RetrievalAgent element : deleteagent) {
			WorkingAgentListinNode.get(node).remove(element);
		}

		LinkedList<RetrievalAgent> deletecont = new LinkedList<RetrievalAgent>();
		for (RetrievalAgent element : ControlTargetAgentList.get(node)) {
			if (element.getID() == agent.getID()) {
				deletecont.add(element);
			}
		}
		for (RetrievalAgent element : deletecont) {
			ControlTargetAgentList.get(node).remove(element);
		}

		LinkedList<RetrievalAgent> deleteclon = new LinkedList<RetrievalAgent>();
		for (RetrievalAgent element : CloneWaitAgentList.get(node)) {
			if (element.getID() == agent.getID()) {
				deleteclon.add(element);
			}
		}
		for (RetrievalAgent element : deleteclon) {
			CloneWaitAgentList.get(node).remove(element);
		}
	}

	public boolean NoWorkingAgentInNode(DataNode node) {
		if (WorkingAgentListinNode.get(node).isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public LinkedList<RetrievalAgent> getControlTargetList(DataNode node) {
		return ControlTargetAgentList.get(node);
	}

	public LinkedList<RetrievalAgent> getWorkingAgentList(DataNode node) {
		return WorkingAgentListinNode.get(node);
	}

	public void addControlTargetAgent(DataNode node, RetrievalAgent agent) {
		if (!ControlTargetAgentList.get(node).contains(agent)) {
			ControlTargetAgentList.get(node).add(agent);
		}
	}

	public void delControlTargetAgent(DataNode node, RetrievalAgent agent) {
		ControlTargetAgentList.get(node).remove(agent);
	}

	public void delControlTargetAgentUsingID(DataNode node, RetrievalAgent agent) {
		LinkedList<RetrievalAgent> deleteagent = new LinkedList<RetrievalAgent>();
		for (RetrievalAgent element : ControlTargetAgentList.get(node)) {
			if (element.getID() == agent.getID()) {
				deleteagent.add(element);
			}
		}
		for (RetrievalAgent element : deleteagent) {
			ControlTargetAgentList.get(node).remove(element);
		}
	}

	public RetrievalAgent ReturnSameIDAgent(DataNode node, RetrievalAgent agent) {
		for (RetrievalAgent element : WorkingAgentListinNode.get(node)) {
			if (element.getID() == agent.getID()) {
				return element;
			}
		}
		for (RetrievalAgent element : CloneWaitAgentList.get(node)) {
			if (element.getID() == agent.getID()) {
				return element;
			}
		}
		return null;
	}
}
