/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulation;

import FuncRandom.RandManager;
import MobileAgent.AgentManager;
import NetWork.TopologyManager;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 * @author N_Atsushi
 */
public class Main {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		double arrivalrate = Double.parseDouble(args[0]);//first arrival_rate
		int user_num = Integer.parseInt(args[1]);//secont para is number of user
		int needdatanum = Integer.parseInt(args[2]);//third para is number of data for estimate average
		String estimatetype = args[3];

		RandManager rand = new RandManager();
		TopologyManager topology = new TopologyManager();
		EventManager eventmanager = new EventManager(topology.getNodeList());
		AgentManager agentmanager = new AgentManager(rand);

		SimulationInit start = new SimulationInit(rand, arrivalrate, needdatanum, user_num,estimatetype);
		eventmanager.addEvent(topology.RandNextNode(), start);

		while (agentmanager.getNumAgentDepartured() < (user_num * 1.1)) {
			NodeEvent next_event = eventmanager.popNextEvent();
			next_event.Occur(topology, agentmanager, eventmanager);
		}
		FileOutputStream fos = new FileOutputStream("score_average.txt", true);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "MS932");
		BufferedWriter bw = new BufferedWriter(osw);
		String msg = arrivalrate + " " + agentmanager.getAverageOfMaxScore(user_num) + "\n";

		bw.write(msg);

		bw.close();
		osw.close();
		fos.close();

		FileOutputStream rna = new FileOutputStream("read_num_average.txt", true);
		OutputStreamWriter osw2 = new OutputStreamWriter(rna, "MS932");
		BufferedWriter bw2 = new BufferedWriter(osw2);
		String msg2 = arrivalrate + " " + agentmanager.getAverageOfReadDataNum(user_num) + "\n";
		bw2.write(msg2);
		bw2.close();
		osw2.close();
		rna.close();

		FileOutputStream scv = new FileOutputStream("score_variance.txt", true);
		OutputStreamWriter osw3 = new OutputStreamWriter(scv, "MS932");
		BufferedWriter bw3 = new BufferedWriter(osw3);
		String msg3 = arrivalrate + " " + agentmanager.getVarianceOfMaxScore(user_num) + "\n";
		bw3.write(msg3);
		bw3.close();
		osw3.close();
		scv.close();

		FileOutputStream nru = new FileOutputStream("no_read_user_num.txt", true);
		OutputStreamWriter osw4 = new OutputStreamWriter(nru, "MS932");
		BufferedWriter bw4 = new BufferedWriter(osw4);
		String msg4 = arrivalrate + " " + agentmanager.getNoReadUserNum(user_num) + "\n";
		bw4.write(msg4);
		bw4.close();
		osw4.close();
		nru.close();

		FileOutputStream fos5 = new FileOutputStream("proc_time.txt", true);
		OutputStreamWriter osw5 = new OutputStreamWriter(fos5, "MS932");
		BufferedWriter bw5 = new BufferedWriter(osw5);
		String msg5 = arrivalrate + " " + agentmanager.getAverageOfProcTime(user_num) + "\n";

		bw5.write(msg5);

		bw5.close();
		osw5.close();
		fos5.close();
		/*LinkedList<Integer> templist = new LinkedList<Integer>();
		templist.addAll(agentmanager.getScoreList(user_num));
		for(int element : agentmanager.getGetScore()){
		FileOutputStream usl = new FileOutputStream("get_score.txt",true);
		OutputStreamWriter osw5 = new OutputStreamWriter(usl , "MS932");
		BufferedWriter bw5 = new BufferedWriter(osw5);
		String msg5 =  element + "\n";
		bw5.write(msg5);
		bw5.close();
		osw5.close();
		usl.close();
		}*/
		/*for(Map.Entry<Integer,Integer> entry : agentmanager.getMaxScoreMap().entrySet()){

			FileOutputStream usl = new FileOutputStream("user_score_list.txt", true);
			OutputStreamWriter osw5 = new OutputStreamWriter(usl, "MS932");
			BufferedWriter bw5 = new BufferedWriter(osw5);
			String msg5 = entry.getKey() + ":" + entry.getValue() + "\n";
			bw5.write(msg5);
			bw5.close();
			osw5.close();
			usl.close();
		}*/

		/*LinkedList<Integer> templist = new LinkedList<Integer>();
		templist.addAll(agentmanager.getScoreList(user_num));
		for (int element : templist) {
			FileOutputStream usl = new FileOutputStream("user_score_list.txt", true);
			OutputStreamWriter osw5 = new OutputStreamWriter(usl, "MS932");
			BufferedWriter bw5 = new BufferedWriter(osw5);
			String msg5 = element + "\n";
			bw5.write(msg5);
			bw5.close();
			osw5.close();
			usl.close();
		}*/
	}
}
