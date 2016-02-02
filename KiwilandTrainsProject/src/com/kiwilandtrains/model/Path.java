package com.kiwilandtrains.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.text.Utilities;

import com.kiwilandtrains.manager.CityGraphManager;

/**
 * 
 * Desc: Class to represent path between two vertices. By default, nodes forming this path/route
 * 		 are in the reversed order as they inserted recursively and in a reverse manner.
 * 
 * @author zekigu
 *
 */
public class Path {

	// list should maintain insertion order. this is essential
	private ArrayList<CityVertex> mNodeList;
	private String mOrderedPathString;
	
	Path() {
		mNodeList = new ArrayList<CityVertex>();		
	}
	
	Path(Path p) {
		ArrayList<CityVertex> pathListOfp = p.getNodeList();
		mNodeList = new ArrayList<CityVertex>(pathListOfp);			
	}
	
	/**
	 * 
	 * Desc: Method to create and return path recursively by looking prevmap
	 *       This rec. takes O(k) where k in the number of nodes from dest to root/pivot
	 * 
	 * @param cityPrevMap
	 * @param current
	 */
	public void createPath(HashMap<String, String> cityPrevMap, CityVertex current) {
				
		if(cityPrevMap.get(current.getName()) != null) {			
			String prevVertexStr = cityPrevMap.get(current.getName());
			CityVertex prevVertex = CityGraphManager.getInstance().getCityGraph().getVertex(prevVertexStr, false);
			mNodeList.add(prevVertex);
			createPath(cityPrevMap, prevVertex);
		}
	}
	
	/**
	 * 
	 * Desc: Returns path description as A-B-C..
	 * 
	 * @return
	 */
	public String getPathDesc() {

		String path = "";
		for(int i=0; i<mNodeList.size();i++){
			if(i == mNodeList.size()-1) {
				path += mNodeList.get(i).getName();
			} else {
				path += mNodeList.get(i).getName() + "-";
			}
		}
		
		return path;
	}
	
	/**
	 * 
	 * Desc: Method to sort and return Path string. This string will be used for comparisons.
	 * 	     We cache this value since it's frequently used and involve re-ordering all items (O(n))
	 * 
	 * @return
	 */
	public String getPathOriginalOrderAsAString() {
		
		if(mOrderedPathString == null) {
			ArrayList<CityVertex> ordered = new ArrayList<CityVertex>(mNodeList);
			Collections.reverse(ordered);
			
			String pathString = "";
			for(int i=0; i<ordered.size(); i++) {
				pathString += ordered.get(i).getName();
			}
					
			mOrderedPathString = pathString;
			
		}
		
		return mOrderedPathString;
	}
	
	/**
	 * 
	 * Desc: Method to return last vertex in the original order (A-B-C...)
	 * 
	 * @return
	 */
	public CityVertex getPathLastVertexInOriginalOrder() {
		
		ArrayList<CityVertex> ordered = new ArrayList<CityVertex>(mNodeList);
		Collections.reverse(ordered);
		
		int lastIndex = ordered.size() -1;
		if(lastIndex >= 0) {
			return ordered.get(lastIndex);
		}
		
		return null; 
	}
	
	/**
	 * 
	 * Desc: Simply get the reversed yet original order of items
	 * 
	 * @return
	 */
	public ArrayList<CityVertex> getOrderedPath() {
		
		ArrayList<CityVertex> ordered = new ArrayList<CityVertex>(mNodeList);
		Collections.reverse(ordered);
		
		return ordered;				
	}
	
	/**
	 * 
	 * Desc: Appends the given path to from newly created Path. No concurrency issues might happen here.
	 * 
	 * @param newPath
	 */
	public void append(Path newPath) {
		
		ArrayList<CityVertex> nodeList = new ArrayList<CityVertex>(getOrderedPath());				
		for(CityVertex p : newPath.getOrderedPath()) {
			nodeList.add(p);
		}
		
		mNodeList = null;	
		mOrderedPathString = null;
		Collections.reverse(nodeList);
		mNodeList = nodeList;
	}
	
	/**
	 * 
	 * Desc: Check if this path contains another
	 * 
	 * @param path
	 * @return
	 */
	public boolean containsPath(Path path) {
		
		// sort this path
		String thisPath = getPathOriginalOrderAsAString();

		// sort the given path
		String pathPath = path.getPathOriginalOrderAsAString();
        
        return (thisPath.contains(pathPath) || pathPath.contains(thisPath));				
	}
		
	public ArrayList<CityVertex> getNodeList() {
		return mNodeList;
	}

	public void setNodeList(ArrayList<CityVertex> nodeList) {
		this.mNodeList = nodeList;
	}

}
