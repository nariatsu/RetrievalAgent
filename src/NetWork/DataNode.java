/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package NetWork;


/**
 *
 * @author nariatsu
 */
public final class DataNode extends Node {
	DataNode(){}
	DataNode(String id){
		super(id);
		number_of_data = 50;
	}
	int number_of_data;

	public int getDataNum(){
		return number_of_data;
	}
}
