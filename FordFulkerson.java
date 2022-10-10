import java.util.*;

public class FordFulkerson {


	//returns path between source and destination thru DFS traversal
	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
		ArrayList<Integer> path = new ArrayList<>();
		ArrayList<Integer> visited = new ArrayList<Integer>();
		ArrayList<Integer> stack = new ArrayList<Integer>();
		
		stack.add(source);
		
		while (!stack.isEmpty()) {
			
			Integer a = stack.get(stack.size() - 1);
			stack.remove(stack.size() - 1);
			visited.add(a);
			
			//get last element of path, get edge, remove if edge is 0 or null
			//confirming there's a path between the last node in the path, to its neighbor 'a'
			while (!path.isEmpty()) {
				int psize = path.size() - 1;
				Integer b = path.get(psize);
				Edge e = graph.getEdge(b, a);
				
				if (e == null || e.weight == 0) {
					path.remove(psize);
					
				} else {
					break;
				}

			}
			
			path.add(a);
			// loop through all edges, add every valid node to stack
			for (Edge e : graph.getEdges()) {
				if ((e.nodes[0] == a && e.weight > 0) && (!visited.contains(e.nodes[1]))) {
					
					if (e.nodes[1] == destination) {
						path.add(destination);
						stack.clear();
					} else {
						stack.add(e.nodes[1]);
					}
				}
			}
		}

		return path;
	}


	public static String fordfulkerson(WGraph graph){
		String answer = "";
		int maxFlow = 0;
		
		WGraph residualG = new WGraph(graph);

		//capacity graph holds graph's original weights; therefore the capacities of each weight
		WGraph capacity = new WGraph(graph);

		//set all edge weights in original graph to 0s
		for (Edge e : graph.getEdges()) {
			e.weight = 0;
		}

		// If source and destination both in pathDFS then success
		int source = graph.getSource();
		int destination = graph.getDestination();

		while (pathDFS(source, destination, residualG).contains(source) &&
				pathDFS(source, destination, residualG).contains(destination)) {

			//paths = path from source to destination in the *residual* graph
			ArrayList<Integer> paths = pathDFS(source, destination, residualG);
			int bottleneck = Integer.MAX_VALUE;
			
			// find bottleneck
			for (int i = 0; i < paths.size() - 1; i++) {
				Edge e = residualG.getEdge(paths.get(i), paths.get(i+1));
				if (e != null && e.weight < bottleneck) {
					bottleneck = e.weight;
					
				}
			}
			
			//augment flow in original graph
			for (int i = 0; i < paths.size() - 1; i++) {
				Integer v1 = paths.get(i);
				Integer v2 = paths.get(i + 1);
				Edge e = graph.getEdge(v1, v2);

				//if e == null in original graph, means it's a back edge in residual graph, must subtract its flow
				if (e == null) {
					graph.setEdge(v1, v2, e.weight - bottleneck);
				} else {
					graph.setEdge(v1, v2, e.weight + bottleneck);
				}
				
			}
			//update residual graph
			for (int i = 0; i<paths.size() - 1; i++) {
				Integer v1 = paths.get(i);
				Integer v2 = paths.get(i + 1);

				//e = edge in original graph which was augmented
				Edge e = graph.getEdge(v1, v2);
				Edge capacityE = capacity.getEdge(v1, v2);

				if (e.weight <= capacityE.weight) {
					residualG.setEdge(v1, v2, capacityE.weight - e.weight);
					
				} else if (e.weight > 0) {
					Edge residualE = residualG.getEdge(v1, v2);

					if (residualE != null) {
						residualG.setEdge(v2, v1, e.weight);


					} else {
						Edge backE = new Edge(v1, v2, e.weight);
						residualG.addEdge(backE);
					}
				}
			}
			
			maxFlow += bottleneck;
		}
		
		

		answer += maxFlow + "\n" + graph.toString();	
		return answer;
	}
	

	 public static void main(String[] args){
		 //String file = args[0];
		 WGraph g = new WGraph("ff2.txt");
		 System.out.println(fordfulkerson(g));
	 }
}

