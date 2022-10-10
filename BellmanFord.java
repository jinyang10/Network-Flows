import java.util.*;

public class BellmanFord{

    private int[] distances = null;
    private int[] predecessors = null;
    private int source;

    class BellmanFordException extends Exception{
        public BellmanFordException(String str){
            super(str);
        }
    }

    class NegativeWeightException extends BellmanFordException{
        public NegativeWeightException(String str){
            super(str);
        }
    }

    class PathDoesNotExistException extends BellmanFordException{
        public PathDoesNotExistException(String str){
            super(str);
        }
    }

    BellmanFord(WGraph g, int source) throws NegativeWeightException{
        /* Constructor, input a graph and a source
         * Computes the Bellman Ford algorithm to populate the
         * attributes 
         *  distances - at position "n" the distance of node "n" to the source is kept
         *  predecessors - at position "n" the predecessor of node "n" on the path
         *                 to the source is kept
         *  source - the source node
         *
         *  If the node is not reachable from the source, the
         *  distance value is Integer.MAX_VALUE
         */
    	//initialization
    	int nbNodes = g.getNbNodes();
    	this.distances = new int[nbNodes];
    	this.predecessors = new int[nbNodes];
    	this.source = source;
    	
    	ArrayList<Edge> edges = g.getEdges();
    	
    	for (int i = 0; i < nbNodes; i++) {
    		distances[i] = Integer.MAX_VALUE;
    		predecessors[i] = -1;
    	}
    	
    	distances[source] = 0;
    	
    	//relax each edge in graph g, #vertices - 1 times
    	for (int i=0; i<nbNodes - 1; ++i) {
    		for (int j = 0; j < edges.size(); j++) {
    			int u = g.getEdges().get(j).nodes[0];
    			int v = g.getEdges().get(j).nodes[1];
    			int weight = g.getEdges().get(j).weight;
    			
    			if (distances[u] + weight < distances[v]) {
    				distances[v] = distances[u] + weight;
    				predecessors[v] = u;
    			}
    		}
    	}

        //last iteration to check for negative cycle
    	for (int j = 0; j<edges.size(); j++) {
    		int u = g.getEdges().get(j).nodes[0];
    		int v = g.getEdges().get(j).nodes[1];
    		int weight = g.getEdges().get(j).weight;
    		
    		if (distances[u] + weight < distances[v]) {
    			throw new NegativeWeightException("There can't be any negative weights");
    		}
    	}
    	
    	

    }

    public int[] shortestPath(int destination) throws PathDoesNotExistException{
        /* Returns the list of nodes along the shortest path from 
         * the object source to the input destination
         * If not path exists an Error is thrown
         */
        ArrayList<Integer> revpath = new ArrayList<>(this.predecessors.length);
        revpath.add(destination);
        int pred = this.predecessors[destination];
        
        while (pred != source) {
        	revpath.add(pred);
        	pred = this.predecessors[pred];
        	if (pred == -1) {
        		throw new PathDoesNotExistException("No path exists");
        	}
        }
        
        revpath.add(pred);
        int[] shortestPath = new int [revpath.size()];
        int j = 0;
        for (int i = revpath.size() -1; i >=0; i--) {
        	shortestPath[j] = revpath.get(i);
        	j++;
        }
        
        return shortestPath;
    }

    public void printPath(int destination){
        /* Print the path in the format s->n1->n2->destination
         * if the path exists, else catch the Error and 
         * prints it
         */
        try {
            int[] path = this.shortestPath(destination);
            for (int i = 0; i < path.length; i++){
                int next = path[i];
                if (next == destination){
                    System.out.println(destination);
                }
                else {
                    System.out.print(next + "-->");
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){
        String file = args[0];
        WGraph g = new WGraph(file);
        try{
            BellmanFord bf = new BellmanFord(g, g.getSource());
            bf.printPath(g.getDestination());
        }
        catch (Exception e){
            System.out.println(e);
        }

   } 
}

