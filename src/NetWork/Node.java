/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package NetWork;

import MobileAgent.MobileAgent;
import java.util.LinkedList;

/**
 *
 * @author nariatsu
 */
public abstract class Node {
	Node(){
	}
	Node(String id){
		nodeID=id;
		cpuResource=50;
		status=0;
	}
	String nodeID;
	double cpuResource;
	int status;

	public String getID(){
		return nodeID;
	}
	public double getResource(){
		return cpuResource;
	}
	public boolean NodeIsCloning(){
		if(status==1){
			return true;
		}
		else return false;
	}
	public void setCloning(){
		status=1;
	}
	public void setNotCloning(){
		status=0;
	}
}
