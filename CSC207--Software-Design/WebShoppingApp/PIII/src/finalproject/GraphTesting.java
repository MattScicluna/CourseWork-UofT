package finalproject;

import java.util.ArrayList;

public class GraphTesting {

	public static void main(String[] args) {
		City toronto = new City("Toronto");
		City ottawa = new City("Ottawa");
		City edmonton = new City("Edmonton");
		
		Graph<City> G = new Graph();
		G.addVertex(toronto);
		G.addVertex(ottawa);
		G.addVertex(edmonton);
	
		
		G.addEdge(toronto, ottawa, 1.0);
		G.addEdge(ottawa, edmonton, 3.0);
		G.addEdge(toronto, edmonton, 1.0);
		

		System.out.println(G.Dijkstra(toronto, edmonton));
		System.out.println(G.edges.get(2).get(0));
		System.out.println(G.edges.get(0).get(2));
		ArrayList<Integer> path = G.Dijkstra(toronto, edmonton);
		double runningSum = 0.0;
		for(int i=1; i<path.size(); i++){
			System.out.println("I'm looping!");
			System.out.println(path);
			System.out.println(G.edges.get(path.get(i)));
			System.out.println(G.edges.get(path.get(i)).get(path.get(i-1)));
			runningSum += G.edges.get(path.get(i)).get(i-1);
			
		}
		System.out.println(runningSum);
		System.out.println(G.getPathDistance(G.Dijkstra(toronto, edmonton)));
		

	}

}
