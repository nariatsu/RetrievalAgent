/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MobileAgent;

import Distribution.BaseOfDistribution;
import Distribution.Exponential;
import Distribution.Normal;
import Distribution.Uniform;
import FuncRandom.RandManager;
import NetWork.DataNode;
import NetWork.Node;
import NetWork.TopologyManager;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.commons.math.stat.StatUtils;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.StatisticalSummary;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;

/**
 *
 * @author nariatsu
 */
public class AgentManager {

	RandManager rand;

	public AgentManager(RandManager Rand) {
		rand = Rand;
	}
	private HashMap<Integer, LinkedList<Node>> ArrivedNodeList = new HashMap<Integer, LinkedList<Node>>();
	private HashMap<MobileAgent, Double> GivenResourceMap = new HashMap<MobileAgent, Double>();
	private HashMap<MobileAgent, Double> ArriveTimeMap = new HashMap<MobileAgent, Double>();
	private HashMap<RetrievalAgent, Double> RealAverageOfDist = new HashMap<RetrievalAgent, Double>();
	private HashMap<Integer, LinkedList<Node>> WorkingNodeList = new HashMap<Integer, LinkedList<Node>>();
	private HashMap<Integer, LinkedList<Node>> CloningNodeList = new HashMap<Integer, LinkedList<Node>>();
	private HashMap<Integer, Integer> MaxScoreMap = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> ReadDataNum = new HashMap<Integer, Integer>();
	private HashMap<RetrievalAgent, Integer> ReadDataNumOfOneAgent = new HashMap<RetrievalAgent, Integer>();
	private HashMap<RetrievalAgent, BaseOfDistribution> DistributionMap = new HashMap<RetrievalAgent, BaseOfDistribution>();
	private HashMap<Integer, LinkedList<DataNode>> ExistNodeList = new HashMap<Integer, LinkedList<DataNode>>();
	private HashMap<Integer, Double> LimitTimeMap = new HashMap<Integer, Double>();
	private HashMap<RetrievalAgent, LinkedList<Integer>> GetScoreMap = new HashMap<RetrievalAgent, LinkedList<Integer>>();
	private HashMap<Integer, DataNode> FirstArriveNodeMap = new HashMap<Integer, DataNode>();
	private HashMap<Integer, Double> ProcTimeMap = new HashMap<Integer, Double>();
	//list for agent that departured
	private LinkedList<Integer> ListOfScore = new LinkedList<Integer>();
	//HashMap AgentInfo = new HashMap<MobileAgent, LinkedList>();
	private LinkedList<Integer> DataNumOfUser = new LinkedList<Integer>();
	private LinkedList<Integer> FinishAgentList = new LinkedList<Integer>();
	private LinkedList<Integer> GetScore = new LinkedList<Integer>();
	private LinkedList<Double> ProcTIme = new LinkedList<Double>();

	public void InitAgentManager(RetrievalAgent agent) {
		LinkedList<Node> arriveNodeList = new LinkedList<Node>();
		ArrivedNodeList.put(agent.getID(), arriveNodeList);
		LinkedList<Node> workNodeList = new LinkedList<Node>();
		WorkingNodeList.put(agent.getID(), workNodeList);
		LinkedList<Node> cloneNodeList = new LinkedList<Node>();
		CloningNodeList.put(agent.getID(), cloneNodeList);
		MaxScoreMap.put(agent.getID(), 0);
		ReadDataNum.put(agent.getID(), 0);
		LinkedList<DataNode> existnode = new LinkedList<DataNode>();
		ExistNodeList.put(agent.getID(), existnode);
		ProcTimeMap.put(agent.getID(), 0.0);
	}

	public void DeleteAgentManager(RetrievalAgent agent) {
		ArrivedNodeList.remove(agent.getID());
		WorkingNodeList.remove(agent.getID());
		CloningNodeList.remove(agent.getID());
		ExistNodeList.remove(agent.getID());
                if(!MaxScoreMap.containsKey(agent.getID())){
                    //System.out.println("agent: "+agent.getID()+" already removed");
                }
//                System.out.println("agent: "+agent.getID()+" have "+this.getMaxScore(agent.getID()));
		MaxScoreMap.remove(agent.getID());
		ReadDataNum.remove(agent.getID());
		ProcTimeMap.remove(agent.getID());
	}

	public void DeleteAgent(RetrievalAgent agent) {
		GivenResourceMap.remove(agent);
		RealAverageOfDist.remove(agent);
		ReadDataNumOfOneAgent.remove(agent);
		DistributionMap.remove(agent);
		ArriveTimeMap.remove(agent);
		if (!GetScoreMap.containsKey(agent)) {
			//System.out.println("agent:"+agent.getID()+" already removed");
		}
		GetScoreMap.remove(agent);
	}


	public void addArrivedNode(int agent_id, Node node) {
		if (!ArrivedNodeList.get(agent_id).contains(node)) {
			ArrivedNodeList.get(agent_id).add(node);
		}
	}

	public LinkedList<Node> knowArrivedNode(int agent_id) {
		LinkedList<Node> tmplist = new LinkedList<Node>();
		tmplist = ArrivedNodeList.get(agent_id);
		/*if(ArrivedNodeList.get(agent_id).size()>100){
			System.out.println("Error node nume over 100");
		}*/
		return ArrivedNodeList.get(agent_id);
	}

	public LinkedList<DataNode> knowNextGoNode(MobileAgent agent, DataNode node, TopologyManager topology) {
		LinkedList<DataNode> nextgo_node_list = new LinkedList<DataNode>();
		nextgo_node_list.addAll(topology.getLinkedNodeList(node));
		nextgo_node_list.removeAll(this.knowArrivedNode(agent.getID()));
		/*
		for (Node element : topology.getLinkedNodeList(node)) {
			nextgo_node_list.add((DataNode) element);
		}*/
		//nextgo_node_list = topology.getLinkedNodeList(node);
		//LinkedList<DataNode> already_go_node_list = new LinkedList<DataNode>();
		//already_go_node_list = knowArrivedNode(agent);
		//for (Node element : knowArrivedNode(agent)) {
			//for(Node element : already_go_node_list){
		//	nextgo_node_list.remove(element);
		//}
		return nextgo_node_list;
	}

	public void setResourceTargetAgent(RetrievalAgent agent) {
		GivenResourceMap.put(agent, 0.0);
	}

	public void setResourceEachAgent(double node_resource, LinkedList<RetrievalAgent> agent_list) {
		int num_of_agent_in_the_node = agent_list.size();
		for (RetrievalAgent element : agent_list) {
			GivenResourceMap.put(element, node_resource / num_of_agent_in_the_node);
		}
	}

	public double getResourceOfAgent(RetrievalAgent agent) {
		return GivenResourceMap.get(agent);
	}

	public void setRealAverage(RetrievalAgent agent) {
		double real_avr = 100;
		RealAverageOfDist.put(agent, real_avr);
	}

	public double getRealAverage(RetrievalAgent agent) {
		return RealAverageOfDist.get(agent);
	}

        public void setMaxScore(int agent_id, int score){
            MaxScoreMap.put(agent_id, score);
        }
	/*public void setMaxScore(int agentID, int get_score) {
		if (MaxScoreMap.get(agentID) < get_score) {
			MaxScoreMap.remove(agentID);
			MaxScoreMap.put(agentID, get_score);
		}
	}*/

	public int getMaxScore(int agentID) {
		return MaxScoreMap.get(agentID);
	}

	public HashMap<Integer, Integer> getMaxScoreMap() {
		return MaxScoreMap;
	}

	public void setDist(RetrievalAgent agent) {
		int average = rand.getIntRand(10, 1000);
		//int average = 500;
		//BaseOfDistribution dist = new Exponential(rand, 10000, 0, average);
		BaseOfDistribution dist = new Normal(rand, 10000, 0, average,average*average);
		//BaseOfDistribution dist = new Uniform(rand, 10000,0);
		DistributionMap.put(agent, dist);
	}

	public BaseOfDistribution getDist(RetrievalAgent agent) {
		return DistributionMap.get(agent);
	}

	public void addWorkingNode(RetrievalAgent agent, DataNode node) {
		if (!WorkingNodeList.get(agent.getID()).contains(node)) {
			WorkingNodeList.get(agent.getID()).add(node);
		}
	}

	public void delWorkingNode(RetrievalAgent agent, DataNode node) {
		WorkingNodeList.get(agent.getID()).remove(node);
	}

	public LinkedList<Node> getWorkingNodeList(RetrievalAgent agent) {
		return WorkingNodeList.get(agent.getID());
	}

	public void addCloningNode(RetrievalAgent agent, DataNode node) {
		if (!CloningNodeList.get(agent.getID()).contains(node)) {
			CloningNodeList.get(agent.getID()).add(node);
		}
	}

	public void delCloningNode(RetrievalAgent agent, DataNode node) {
		CloningNodeList.get(agent.getID()).remove(node);
	}

	public LinkedList<Node> getCloningNodeList(RetrievalAgent agent) {
		return CloningNodeList.get(agent.getID());
	}

	public boolean NoAgentWorkingInTopology(RetrievalAgent agent) {
		if (ExistNodeList.get(agent.getID()).isEmpty()) {
			//if(WorkingNodeList.get(agent.getID()).size()==0 && CloningNodeList.get(agent.getID()).size()==0){
			return true;
		} else {
			return false;
		}
	}

	public void IncrementReadDataNum(RetrievalAgent agent) {
		int total_num;
		total_num = ReadDataNum.get(agent.getID());
		++total_num;
		ReadDataNum.put(agent.getID(), total_num);
		int stilreadnum;
		stilreadnum = ReadDataNumOfOneAgent.get(agent);
		++stilreadnum;
		ReadDataNumOfOneAgent.put(agent, stilreadnum);
	}

	public void addReadDataNum(RetrievalAgent agent) {
		int total_num;
		total_num = ReadDataNum.get(agent.getID());
		total_num += agent.getReadDataNum();
		if (ReadDataNum.get(agent.getID()) > total_num) {
			System.out.println("Error!!!!!!!!!!!!!!!!");
		}
		ReadDataNum.put(agent.getID(), total_num);
	}

	public int getReadDataNum(RetrievalAgent agent) {
		return ReadDataNum.get(agent.getID());
	}

	public void setReadDataNumOfThisAgent(RetrievalAgent agent) {
		ReadDataNumOfOneAgent.put(agent, 0);
	}

	public int getReadDataNumOfThisAgent(RetrievalAgent agent) {
		return ReadDataNumOfOneAgent.get(agent);
	}

	public double getAverageOfReadDataNum(int num_of_user) {
		double sum = 0;
		double average;
		int pop_num;
		LinkedList<Integer> CopyOfDataNumOfUser = new LinkedList<Integer>();
		CopyOfDataNumOfUser.addAll(DataNumOfUser);
		pop_num = num_of_user / 10;
		for (int i = 0; i < pop_num; ++i) {
			CopyOfDataNumOfUser.poll();
		}
		for (Integer element : CopyOfDataNumOfUser) {
			sum += element;
		}
		/*for(int user_id=100;user_id<1100;++user_id){
		sum+=ReadDataNum.get(user_id);
		}*/
		average = sum / num_of_user;
		return average;
	}

	public int getNoReadUserNum(int num_of_user) {
		int user_num = 0;
		int pop_num = num_of_user / 10;
		LinkedList<Integer> CopyOfDataNumOfUser = new LinkedList<Integer>();
		CopyOfDataNumOfUser.addAll(DataNumOfUser);
		for (int i = 0; i < pop_num; ++i) {
			CopyOfDataNumOfUser.poll();
		}
		for (Integer element : CopyOfDataNumOfUser) {
			if (element == 0) {
				++user_num;
			}
		}
		return user_num;
	}

	public LinkedList<Integer> getScoreList(int num_of_user) {
		LinkedList<Integer> ScoreList = new LinkedList<Integer>();
		ScoreList.addAll(ListOfScore);
		int pop_num = num_of_user / 10;

		for (int i = 0; i < pop_num; ++i) {
			ScoreList.pop();
		}
		return ScoreList;
	}

	public double getAverageOfMaxScore(int num_of_user) {
		double sum = 0;
		double average;
		int pop_num;
		LinkedList<Integer> ScoreList = new LinkedList<Integer>();
		ScoreList.addAll(ListOfScore);
		pop_num = num_of_user / 10;

		for (int i = 0; i < pop_num; ++i) {
			ScoreList.poll();
		}

		for (int element : ScoreList) {
			sum += element;
		}

		average = sum / num_of_user;
		return average;
	}

	public double getVarianceOfMaxScore(int num_of_user) {
//		double sum = 0;
//		double sum2 = 0;
//		double average;
//		double average2;
//		double variance;
		int pop_num;
		SummaryStatistics statistics = new SummaryStatistics();

		LinkedList<Integer> ScoreList = new LinkedList<Integer>();
		ScoreList.addAll(ListOfScore);
		pop_num = num_of_user / 10;

		for (int i = 0; i < pop_num; ++i) {
			ScoreList.poll();
		}
		for(int element : ScoreList){
			statistics.addValue(element);
		}
		return statistics.getVariance();

//		for (int element : ScoreList) {
//			sum += element;
//		}
//		for (int element : ScoreList) {
//			sum2 += element * element;
//		}
//		average = sum / num_of_user;
//		average2 = sum2 / num_of_user;
//		variance = average2 - average * average;
//		return variance;

	}

	public void MakeExistNodeList(RetrievalAgent agent) {
		LinkedList<DataNode> list = new LinkedList<DataNode>();
		ExistNodeList.put(agent.getID(), list);
	}

	public void addExistNodeList(int agent_id, DataNode node) {
		if (ExistNodeList.get(agent_id).contains(node)) {
			//System.out.println("agent:" + agent_id + " already came node:" + node.getID());
		} else {
			ExistNodeList.get(agent_id).add(node);
		}
	}

	public void delExistNodeList(int agent_id, DataNode node) {
		if (ExistNodeList.get(agent_id).contains(node)) {
			ExistNodeList.get(agent_id).remove(node);
		} else {
			System.out.println("agent:" + agent_id + " already left node:" + node.getID());
		}
	}

	public LinkedList<DataNode> getExistNodeList(int agent_id) {
		return ExistNodeList.get(agent_id);
	}

	public void setLimitTime(RetrievalAgent agent, double limit_time) {
		LimitTimeMap.put(agent.getID(), limit_time);
	}

	public double getLimitTime(RetrievalAgent agent) {
		return LimitTimeMap.get(agent.getID());
	}

	public void putGetScoreMap(RetrievalAgent agent) {
		LinkedList<Integer> scorelist = new LinkedList<Integer>();
		GetScoreMap.put(agent, scorelist);
	}

	public void addGetScoreToList(RetrievalAgent agent, int get_score) {
		GetScoreMap.get(agent).add(get_score);
	}

	public int getMaxScorefromList(RetrievalAgent agent) {
		int max = 0;
		for (int element : GetScoreMap.get(agent)) {
			if (max < element) {
				max = element;
			}
		}
		return max;
	}

	public void delGetScoreMap(int agentid) {
		MaxScoreMap.remove(agentid);
	}

	public double getEstimateAverage(RetrievalAgent agent) {
		int sum = 0;
		double average;
		for (Integer element : GetScoreMap.get(agent)) {
			sum += element;
		}
		average = (double) sum / GetScoreMap.get(agent).size();
		return average;
	}
	public void initProcTime(int agentid){
		ProcTimeMap.put(agentid, 0.0);
	}
	public void setArriveTime(MobileAgent agent,double eventtime){
		ArriveTimeMap.put(agent,eventtime);
	}
	public double getArriveTime(MobileAgent agent){
		return ArriveTimeMap.get(agent);
	}
	public void addProcTime(int agentid, double proctime){
		double otherproctime = ProcTimeMap.get(agentid);
		ProcTimeMap.put(agentid, otherproctime+proctime);
	}
	public double getProcTime(int agentid){
		return ProcTimeMap.get(agentid);
	}
	public void addProcTimeList(double proctime){
		ProcTIme.add(proctime);
	}
	public double getAverageOfProcTime(int num_of_user) {
		double sum = 0;
		double average;
		int pop_num;
		LinkedList<Double> ProcTimeList = new LinkedList<Double>();
		ProcTimeList.addAll(ProcTIme);
		pop_num = num_of_user / 10;

		for (int i = 0; i < pop_num; ++i) {
			ProcTimeList.poll();
		}

		for (double element : ProcTimeList) {
			sum += element;
		}

		average = sum / num_of_user;
		return average;
	}

	public void putFirstArriveNode(RetrievalAgent agent, DataNode node) {
		FirstArriveNodeMap.put(agent.getID(), node);
	}

	public DataNode getFirstArriveNode(RetrievalAgent agent) {
		DataNode return_node = FirstArriveNodeMap.get(agent.getID());
		FirstArriveNodeMap.remove(agent.getID());
		return return_node;
	}

	public int getNumAgentDepartured() {
		return ListOfScore.size();
	}

	public void addScoreToList(int max_score) {
		ListOfScore.add(max_score);
	}

	public void addDataNumOfUser(int data_num) {
		DataNumOfUser.add(data_num);
	}

	public void addFinishAgentList(int agent_id) {
		FinishAgentList.add(agent_id);
	}

	public LinkedList<Integer> getFinishAgentList() {
		return FinishAgentList;
	}

	public void addGetScore(int get_score) {
		GetScore.add(get_score);
	}

	public LinkedList<Integer> getGetScore() {
		return GetScore;
	}
}
