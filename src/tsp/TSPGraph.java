package tsp;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class TSPGraph {
    // Package visible attributes - default modifier
    List<String> cities;
    int[][] matrix;

    // Public constructors
    public TSPGraph (List<String> cities) {
        this.cities = cities;
        matrix = fillZeros(new int[this.cities.size()][this.cities.size()]);
    }
    public TSPGraph (String[] cities) {
        // Convert to a list
        this.cities = Arrays.asList(cities);
        matrix = fillZeros(new int[this.cities.size()][this.cities.size()]);
    }
    public TSPGraph (String[] cities, int[][] matrix) throws IllegalArgumentException{
        this.cities = Arrays.asList(cities);
        // Checks if matrix is valid
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].length != cities.length) {
                throw new IllegalArgumentException();
            } else {
                for (int j = 0; j < matrix[i].length; j++) {
                    if (matrix[i][j] != matrix[j][i] && matrix[i][j] >= 0) {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
        this.matrix = matrix.clone();
    }

    // Function that adds an edge with weight between the two cities
    // returns true if the edge didn't exist previously.
    // If the edge already exists, return false and do nothing
    public boolean addEdge(String cityA, String cityB, int cost) {
        int cityAIndex = cities.indexOf(cityA);
        int cityBIndex = cities.indexOf(cityB);
        if (cost <= 0 || cityAIndex < 0 || cityBIndex < 0 || matrix[cityAIndex][cityBIndex] != 0) {
            return false;
        }
        matrix[cityAIndex][cityBIndex] = cost;
        matrix[cityBIndex][cityAIndex] = cost;
        return true;
    }

    private List<String> convertFromIndices(List<Integer> indices) {
        List<String> route = new ArrayList<>();
        for (Integer index : indices) {
            route.add(cities.get(index));
        }
        return route;
    }

    public boolean setEdge(String cityA, String cityB, int cost) {
        int cityAIndex = cities.indexOf(cityA);
        int cityBIndex = cities.indexOf(cityB);
        if (cost <= 0 || cityAIndex < 0 || cityBIndex < 0) {
            return false;
        }
        matrix[cityAIndex][cityBIndex] = cost;
        matrix[cityBIndex][cityAIndex] = cost;
        return true;
    }

    public boolean removeEdge(String cityA, String cityB) {
        int cityAIndex = cities.indexOf(cityA);
        int cityBIndex = cities.indexOf(cityB);
        if (cityAIndex < 0 || cityBIndex < 0 || matrix[cityAIndex][cityBIndex] == 0) {
            return false;
        }
        matrix[cityAIndex][cityBIndex] = 0;
        matrix[cityBIndex][cityAIndex] = 0;
        return true;
    }

    public boolean isNeighbour(String cityA, String cityB) {
        int cityAIndex = cities.indexOf(cityA);
        int cityBIndex = cities.indexOf(cityB);
        if (cityAIndex < 0 || cityBIndex < 0 || matrix[cityAIndex][cityBIndex] <= 0) {
            return false;
        }
        return true;
    }

    public int getCost(String cityA, String cityB) {
        int cityAIndex = cities.indexOf(cityA);
        int cityBIndex = cities.indexOf(cityB);
        if (cityAIndex < 0 || cityBIndex < 0) {
            throw new IllegalArgumentException("Invalid city!");
        }
        return matrix[cityAIndex][cityBIndex];
    }

    public List<String> getNeighbours(String city) {
        List<String> neighbours = new ArrayList<>();
        int cityIndex = cities.indexOf(city);
        if (cityIndex < 0) {
            throw new IllegalArgumentException("Invalid city!");
        }
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][cityIndex] > 0 && i != cityIndex) {
                neighbours.add(cities.get(i));
            }
        }
        return neighbours;
    }

    public List<Integer> getNeighbours(Integer cityIndex) {
        List<Integer> neighbours = new ArrayList<>();
        if (cityIndex < 0 || cityIndex >= cities.size()) {
            throw new IllegalArgumentException("Invalid city!");
        }
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][cityIndex] > 0 && i != cityIndex) {
                neighbours.add(i);
            }
        }
        return neighbours;
    }
    /*
     * EXACT ALGORITHMS
    */
    /**
     * Brute Force algorithms, trying all permutations to find which is the cheapest.
     * Complexity O(n!), the factorial of the number of cities. This solution becomes impractical
     * even for only 20 cities
     * @return The list of cities in visited order to obtain a minimal round trip,
     * @return null if such a route doesn't exist, the graph doesn't contain a hamiltonian cycle.
     */
    public List<String> getShortestRoundTripBrute() {
        // Get all permutations
        Set<List<Integer>> allPermutations = IntegerPermutation.allPermutationsBackTrack(cities.size());
        int minCost = Integer.MAX_VALUE;
        List<Integer> optimalRoute = null;
        for (List<Integer> route : allPermutations) {
            // Get the cost of the route
            List<String> cityRoute = convertFromIndices(route);
            int cost = getRoundTripCost(cityRoute);
            if (cost < minCost) {
                minCost = cost;
                optimalRoute = route;
            }
        }
        // No route found
        if (optimalRoute == null) {
            return null;
        }
        List<String> trip = new ArrayList<>();
        for (int city : optimalRoute) {
            trip.add(cities.get(city));
        }
        return trip;
    }

    /**
     * Gets the cost of a given route, assuming the last city on the route will go the first.
     * @param route a given route
     * @return {@code Integer.MAX_VALUE} if invalid route
     * @return the cost of the route if a valid route
     */
    public int getRoundTripCost(List<String> route) {
        int cost = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            int weight = getCost(route.get(i), route.get(i+1));
            if (weight == 0) {
                return Integer.MAX_VALUE;
            } else {
                cost += weight;
            }
        }
        int firstLastCost = getCost(route.get(route.size()-1), route.get(0));
        if (firstLastCost == 0) {
            return Integer.MAX_VALUE;
        } else {
            return cost += firstLastCost;
        }
    }

    /**
     * Similar implementation to the Hamiltoninan Cycle Problem, but if the current cost is greater than the optimal cost then we can prune.
     * @return the optimal trip as a list of integers representing the cities
     */
    public List<String> getShortestRoundTripBacktrack() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 1; i < matrix.length; i++) {
            indices.add(i);
        }
        List<Integer> optimalCost = new ArrayList<>();
        optimalCost.add(Integer.MAX_VALUE);
        // It does not matter which vertex we use to start the cycle.
        // By default we use as a starting vertex the vertex 0.
        List<Integer> route = getShortestRoundTripBacktrack(matrix, indices, Arrays.asList(new Integer[] { 0 }), 0,
                optimalCost);
        return convertFromIndices(route);
    }

    private static List<Integer> getShortestRoundTripBacktrack(int[][] matrix, List<Integer> vertices,
            List<Integer> currentPath, int currentCost, List<Integer> optimalCost) {

        // Prune the branch as we have already found a better route than this
        // one.
        if (currentCost >= optimalCost.get(0)) {
            return null;
        }

        // We have visited all vertices, so we have to check if it is a cycle
        // and if the cost of the cycle is optimal or not.
        if (vertices.isEmpty()) {
            // Ensure it is a cycle and it is optimal
            if (matrix[0][currentPath.get(currentPath.size() - 1)] != 0
                    && currentCost + matrix[0][currentPath.get(currentPath.size() - 1)] < optimalCost.get(0)) {
                optimalCost.set(0, currentCost + matrix[0][currentPath.get(currentPath.size() - 1)]); // new optimal
                                                                                                      // cost
                return currentPath;
            } else { // not a cycle or not optimal
                return null;
            }
        }
        List<Integer> route = null;
        for (Integer vertex : vertices) {
            if (matrix[currentPath.get(currentPath.size() - 1)][vertex] != 0) {
                List<Integer> augmentedPath = new ArrayList<Integer>(currentPath);
                augmentedPath.add(vertex);
                List<Integer> remainingVertices = new ArrayList<>(vertices);
                remainingVertices.remove(vertex);
                List<Integer> newRoute = getShortestRoundTripBacktrack(matrix, remainingVertices, augmentedPath,
                        currentCost + matrix[currentPath.get(currentPath.size() - 1)][vertex], optimalCost);
                if (newRoute != null) {
                    route = newRoute;
                }
            }
        }
        return route;
    }

    // Heuristic Algorithms //
    // The Nearest Neighbour algorithm (greedy)
    public List<String> getGreedyRoundTrip(String start) throws IllegalArgumentException {
        Integer startInd = cities.indexOf(start);
        if (startInd < 0) {
            throw new IllegalArgumentException("Invalid starting index");
        }

        List<String> route = new ArrayList<>();
        // boolean array to indicate which city has been visited
        boolean[] visited = new boolean[cities.size()];
        Queue<Integer> toProcess = new LinkedList<>();
        toProcess.add(startInd);

        while(!toProcess.isEmpty()) {
            Integer vertex = toProcess.poll();
            visited[vertex] = true;
            route.add(cities.get(vertex));

            List<Integer> neighbours = getNeighbours(vertex);
            Integer nextToVisit = -1;   // Assume the end
            for (Integer n : neighbours) {
                // If not visited
                if (!visited[n]) {
                    // haven't got a neighbour to visit yet
                    if (nextToVisit < 0) {
                        nextToVisit = n;
                    }
                    else {
                        if (matrix[vertex][n] < matrix[vertex][nextToVisit]) {
                        // Change next to visit to the neighbour which has smallest edge
                        nextToVisit = n;
                        }
                    }
                }
            }           
            if (nextToVisit >= 0) {
                toProcess.add(nextToVisit);
            }
        }
        if (route.size() != cities.size()
            || matrix[cities.indexOf(route.get(0))][cities.indexOf(route.get(route.size()-1))] == 0) {
                // Invalid route as it doesn't visit all the cities and doesn't go back to the beginning
                return null;
            }
        return route;
    }
    // HELPER FUNCTIONS //
    // Helper function to take a matrix and fill it with zeros
    private static int[][] fillZeros (int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = 0;
            }
        }
        return matrix;
    }

    public static void main(String[] args) {
        TSPGraph network = new TSPGraph(new String[]{"1", "2", "3", "4"});
        network.addEdge("1", "2", 10);
        network.addEdge("1", "3", 15);
        network.addEdge("1", "4", 20);
        network.addEdge("3", "2", 35);
        network.addEdge("4", "2", 25);
        network.addEdge("4", "3", 30);

        List<String> optimalRoute;
        optimalRoute = network.getShortestRoundTripBrute();
        System.out.println("Brute Force:  " + network.getRoundTripCost(optimalRoute) + " : " + optimalRoute);
        optimalRoute = network.getShortestRoundTripBacktrack();
        System.out.println("Backtrack:  " + network.getRoundTripCost(optimalRoute) + " : " + optimalRoute);
        List<String> suboptimalRoute = network.getGreedyRoundTrip("1");
        System.out.println("Greedy:  " + network.getRoundTripCost(suboptimalRoute) + " : " + suboptimalRoute);
    }
}
