/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulation;

import MobileAgent.RetrievalAgent;
import NetWork.DataNode;
import NetWork.Node;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author N_Atsushi
 */
public class EventManager {

	private LinkedList<DataNode> NodeListOnTopology = new LinkedList<DataNode>();
	private HashMap<Node, LinkedList<NodeEvent>> NodeEventList = new HashMap<Node, LinkedList<NodeEvent>>();
	private HashMap<Node,LinkedList<SearchFinish>> SearchFinList = new HashMap<Node, LinkedList<SearchFinish>>();

	public EventManager(LinkedList<DataNode> node_list) {
		NodeListOnTopology = node_list;
		for (Node element : node_list) {
			LinkedList<NodeEvent> event_list = new LinkedList<NodeEvent>();
			NodeEventList.put(element, event_list);
			LinkedList<SearchFinish> search_fin_list = new LinkedList<SearchFinish>();
			SearchFinList.put(element, search_fin_list);
		}
	}

	public void addEvent(Node node, NodeEvent node_event) {
		if (NodeEventList.get(node).isEmpty()) {
			NodeEventList.get(node).push(node_event);
		} else {
			int index;
			for (NodeEvent element : NodeEventList.get(node)) {
				if (element.getTime() > node_event.getTime()) {
					index = NodeEventList.get(node).indexOf(element);
					NodeEventList.get(node).add(index, node_event);
					break;
				}
				if (element.equals(NodeEventList.get(node).getLast())) {
					NodeEventList.get(node).addLast(node_event);
					break;
				}
			}
		}
	}

	public void addSerchFinish(Node node, SearchFinish searchfin){
		SearchFinList.get(node).add(searchfin);
		if (NodeEventList.get(node).isEmpty()) {
			NodeEventList.get(node).push(searchfin);
		} else {
			int index;
			for (NodeEvent element : NodeEventList.get(node)) {
				if (element.getTime() > searchfin.getTime()) {
					index = NodeEventList.get(node).indexOf(element);
					NodeEventList.get(node).add(index, searchfin);
					break;
				}
				if (element.equals(NodeEventList.get(node).getLast())) {
					NodeEventList.get(node).addLast(searchfin);
					break;
				}
			}
		}
	}

	public void delSearchFinish(Node node){
		NodeEventList.get(node).removeAll(SearchFinList.get(node));
		SearchFinList.get(node).clear();
	}
	public void delSearchFinishFromDeleteList(Node node,NodeEvent event){
		SearchFinList.get(node).remove(event);
	}

	public NodeEvent popNextEvent() {
		//double firstEventTime=NodeEventList.get(NodeListOnTopology.getFirst()).getFirst().getTime();
		double firstEventTime = 99999999;
		Node firstEventNode = null;
		NodeEvent firstEvent = null;
		for (Node element : NodeListOnTopology) {
			if (!NodeEventList.get(element).isEmpty()) {
				if (firstEventTime >= NodeEventList.get(element).getFirst().getTime()) {
					firstEventTime = NodeEventList.get(element).getFirst().getTime();
					firstEventNode = element;
					firstEvent = NodeEventList.get(element).getFirst();
				}
			}
		}

		NodeEventList.get(firstEventNode).removeFirst();
		return firstEvent;
	}

	public boolean EventListIsEmpty() {
		for (Node element : NodeListOnTopology) {
			if (!NodeEventList.get(element).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public LinkedList<NodeEvent> getEventList(Node node) {
		return NodeEventList.get(node);
	}

	public void RemoveEvent(DataNode node, RetrievalAgent agent) {
		LinkedList<NodeEvent> deleteeventlist = new LinkedList<NodeEvent>();
		for (NodeEvent element : NodeEventList.get(node)) {
			if (element instanceof WorkingStart || element instanceof TimeOver || element instanceof UserGenerate) {
			} else {
				if (element.getAgent().getID() == agent.getID()) {
					deleteeventlist.add(element);
				}
			}
		}
                NodeEventList.get(node).removeAll(deleteeventlist);
		/*for (NodeEvent element : deleteeventlist) {
			NodeEventList.get(node).remove(element);
		}*/
	}

	public void RemoveEventUsingID(DataNode node, RetrievalAgent agent) {
		LinkedList<NodeEvent> deleteList = new LinkedList<NodeEvent>();
		for (NodeEvent element : NodeEventList.get(node)) {
			if (element instanceof WorkingStart || element instanceof UserGenerate) {
			} else {
				if (element.getAgent().getID() == agent.getID()) {
					deleteList.add(element);
				}
			}
		}
                NodeEventList.get(node).removeAll(deleteList);
		/*for (NodeEvent element : deleteList) {
			NodeEventList.get(node).remove(element);
		}*/
	}

}
