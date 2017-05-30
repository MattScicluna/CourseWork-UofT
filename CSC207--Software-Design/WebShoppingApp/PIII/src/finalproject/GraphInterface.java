package finalproject;

import java.util.Queue;
/**
 * The graph interface is used as a template for the graph object. Graph is used to store the cities containing distribution centers and customers
 * and paths between cities. The distances of these paths are represented by weights on the edges.
 * 
 * @author Gal
 * @version 1.0
 */
public interface GraphInterface<T>
{
	/**
	 * Checks if graph is is empty
	 * @return -> true if this graph is empty; otherwise, returns false
	 */
	boolean isEmpty();

	/**
	 * adds vertex to graph or throws VertexExistaException. Preconditions are as follows: 
	 * Vertex is not already in this graph.
	 * Vertex is not null.
	 * @param vertex -> The vertex to be added to the graph
	 * @throws VertexExistsException
	 */
	boolean addVertex(T vertex) throws VertexExistsException;

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
	void addEdge(T fromVertex, T toVertex, double weight);

	/**
	 * Returns a queue of the vertices that are adjacent to vertex. 
	 * Otherwise, return empty Queue.
	 * @param vertex -> the selected vertex
	 * @return -> queue of adjacent vertices
	 */
	Queue<T> getToVertices(T vertex);

}