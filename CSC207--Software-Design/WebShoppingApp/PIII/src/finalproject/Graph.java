package finalproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;


public class Graph<T> implements GraphInterface<T>, Serializable {
	/**
	 * Implements the graph interface. This is used to store the cities containing distribution centers and customers
	 * and paths between cities. The distances of these paths are represented by weights on the edges.
	 * 
	 * @author Gal
	 * @version 1.0
	 */
	private static final long serialVersionUID = 703028660723501536L;
	public ArrayList<T> vertices;
	public ArrayList<ArrayList<Double>> edges;

	public Graph() {
		vertices = new ArrayList<T>();
		edges = new ArrayList<ArrayList<Double>>();
	}


	/**
	 * Checks if graph is is empty
	 * @return -> true if this graph is empty; otherwise, returns false
	 */
	public boolean isEmpty()
	{
		return (vertices.size() == 0);
	}

	/**
	 * adds vertex to graph or throws VertexExistaException. Preconditions are as follows: 
	 * Vertex is not already in this graph.
	 * Vertex is not null.
	 * @param vertex -> The vertex to be added to the graph
	 * @throws VertexExistsException
	 */
	public boolean addVertex(T vertex){
		if(vertices.contains(vertex))
			return false;
		else{
			/* Otherwise adds vertex to this graph. */
			vertices.add(vertex);
			/* add vertex to the edges matrix */
			ArrayList<Double> v = new ArrayList<Double>();
			v.add(0.0);
			for(int i=0; i<edges.size(); i++){
				edges.get(i).add(0.0);
				v.add(0.0);
			}
			edges.add(v);
			return true;
		}
	}

	public ArrayList<T> getVertices(){
		return vertices;
	}

	/**
	 * Adds edge to graph between two designated vertices
	 * 
	 * <p>
	 * Adds an edge with the specified weight from fromVertex to toVertex. 
	 * Returns null if source or target doesn't exist.
	 * 
	 * @param fromVertex -> one of the vertices to be connected with an edge
	 * @param toVertex -> the other vertex
	 * @param weight -> weight of the vertices
	 */
	public void addEdge(T fromVertex, T toVertex, double weight){
		int fv = vertices.indexOf(fromVertex);
		int tv = vertices.indexOf(toVertex);
		if(fv==-1 || tv==-1){
			return;
		}
		else{
			edges.get(fv).set(tv, weight);
			edges.get(tv).set(fv, weight);
		}
	}

	/**
	 * Returns a queue of the vertices that are adjacent to vertex. 
	 * Otherwise, return empty Queue.
	 * @param vertex -> the selected vertex
	 * @return -> queue of adjacent vertices
	 */
	public Queue<T> getToVertices(T vertex){
		Queue<T> adjacent = new LinkedList<T>();

		int v = vertices.indexOf(vertex);

		if(v==-1)
			return adjacent;

		int jcounter = 0;
		while(jcounter<vertices.size()){
			if(edges.get(v).get(jcounter)>0.0)
				adjacent.add(vertices.get(jcounter));
			jcounter += 1;
		}
		return adjacent;
	}

	/**
	 * Implements Dijkstra algorithm to find shortest path between source and target nodes
	 * @param source -> starting vertex
	 * @param target -> target vertex
	 * @return -> arrayList of vertices which make the shortest path
	 */
	public ArrayList<Integer> Dijkstra(T source, T target){
		int done = 0;
		boolean[] m = new boolean[vertices.size()];
		double[] d = new double[vertices.size()];
		int[] p = new int[vertices.size()];
		Arrays.fill(p, -1);
		Arrays.fill(d, Double.POSITIVE_INFINITY);
		int s = vertices.indexOf(source);
		d[s] = 0;

		while(done<vertices.size()){
			int min = 0;
			for(int i=0; i<d.length; i++){
				if(m[min])
					min++;
			}
			for(int i=0; i<d.length; i++){
				if(!m[i] && d[i]<d[min])
					min = i;
			}
			m[min] = true;
			done++;
			Queue<T> adjacent = getToVertices(vertices.get(min));
			while(!adjacent.isEmpty()){
				int v = vertices.indexOf(adjacent.poll());
				if(d[v]>d[min]+edges.get(min).get(v)){
					d[v] = d[min] + edges.get(min).get(v);
					p[v] = min;
				}
			}
		}

		ArrayList<Integer> path = new ArrayList<Integer>();
		int currentNode = vertices.indexOf(target);

		while (currentNode != -1) {
			path.add(currentNode);
			currentNode = p[currentNode];
		}
		return path;
	}

	/**
	 * Computes distance of path
	 * @param path -> a valid path from starting vertex to target vertex (cities)
	 * @return -> the distance of the path
	 */
	public double getPathDistance(ArrayList<Integer> path){
		double runningSum = 0.0;
		for(int i=1; i<path.size(); i++){
			runningSum += edges.get(path.get(i)).get(path.get(i-1));
		}
		return runningSum;
	}

	/**
	 * Find shortest path between source and allowed, using Dijkstra algorithm.
	 * @param source -> starting vertex
	 * @param target -> target vertex
	 * @return -> arrayList of vertices which make the shortest path.
	 */
	public ArrayList<Integer> findShortestPair(T source, ArrayList<T> allowed){
		int s = vertices.indexOf(source);
		if(allowed.contains(source)){
			ArrayList<Integer> one = new ArrayList<Integer>();
			one.add(s);
			return one;
		}
		ArrayList<Integer> result = Dijkstra(source, allowed.get(0));
		double closest = getPathDistance(result);
		for(int i=1; i<allowed.size(); i++){
			ArrayList<Integer> route = Dijkstra(source, allowed.get(i));
			double distance = getPathDistance(route);
			if(distance<closest){
				result = route;
				closest = distance;
			}	
		}

		return result;
	}
}


