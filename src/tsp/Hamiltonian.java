package tsp;

import java.util.ArrayList;
import java.util.List;

public class Hamiltonian {
    public static boolean isHamiltonian(int[][] adjacencyMatrix)  throws IllegalArgumentException {
        // Create sequence of vertices
        int n = adjacencyMatrix.length;
        List<Integer> sequence = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            sequence.add(i);
        }
        // Need to test all so assume false initially then prove true
        // Continues until there are no more permutations or valid graph graph
        boolean isHamiltonianCycle = false;
        while (sequence != null && !isHamiltonianCycle) {
            // Test the sequence
            boolean hamiltonianPath = true;
            int i = 0;
            while (hamiltonianPath && i < n-1) {
                int distanceA = adjacencyMatrix[sequence.get(i)][sequence.get(i+1)];
                // Test for no edge - we can skip this sequence if true
                if (distanceA == 0) {
                    hamiltonianPath = false;
                } else {
                    // Check if it's undirected
                    int distanceB = adjacencyMatrix[sequence.get(i+1)][sequence.get(i)];
                    // If they're not equal throw exception
                    if (distanceA != distanceB) {
                        throw new IllegalArgumentException();
                    }
                }
                // Try next vertex
                i++;
            }
            // Check to see if it's a hamiltonian cycle
            if (hamiltonianPath) {
                int firstVertex = sequence.get(0);
                int lastVertex = sequence.get(n-1);
                int distanceA = adjacencyMatrix[firstVertex][lastVertex];
                int distanceB = adjacencyMatrix[lastVertex][firstVertex];
                if (distanceA != 0 && distanceA == distanceB) {
                    isHamiltonianCycle = true;
                }
            }
            // Generate next permutation of sequence
            sequence = IntegerPermutation.nextPermutation(sequence);
        }
        return isHamiltonianCycle;
    }

    public static boolean isHamiltonianBacktrack (int[][] adjacencyMatrix) {
        List<Integer> sequence = new ArrayList<>();
        List<Integer> unvisited = new ArrayList<>();
        // No vertices have been visited as of now
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            unvisited.add(i);
        }
        sequence.add(unvisited.get(0));     // Sequence = {0}
        unvisited.remove(0);                // Unvisited = {1, ..., n-1}
        return backtrackRec(adjacencyMatrix, sequence, unvisited);
    }

    private static boolean backtrackRec (int[][] adjMatrix, List<Integer> sequence, List<Integer> unvisited) {
        // Base case: no nodes left in unvisited - made a sequence
        if (unvisited.isEmpty()) {
            return true;
        }
        // General case: test all possible solutions
        for (int i = 0; i < unvisited.size(); i++) {
            int vertex = unvisited.get(i);
            // It is a valid choice (edge between and it's undirected) and not already in the sequence
            if (validEdge(adjMatrix, sequence.get(sequence.size()-1), vertex) && !sequence.contains(vertex)) {
                sequence.add(vertex);
                unvisited.remove(i);
                // Go further down the branch
                if (backtrackRec(adjMatrix, sequence, unvisited)) {
                    return true;
                }
                // Add vertex back to unvisited
                unvisited.add(vertex);
            }
        }
        // No vertex added - no solution possible
        return false;
    }

    private static boolean validEdge (int[][] adjMatrix, int vertexA, int vertexB) throws IllegalArgumentException {
        // No edge
        if (adjMatrix[vertexA][vertexB] == 0) {
            return false;
        // Check if directed graph
        } else if (adjMatrix[vertexB][vertexA] != adjMatrix[vertexA][vertexB]) {
            throw new IllegalArgumentException("This isn't a undirected graph");
        } else {
            return true;
        }
    }



    public static void main(String[] args) {
        int[][] matrixA = {
            {0, 1, 0, 1, 0},
            {1, 0, 1, 1, 1},
            {0, 1, 0, 0, 1},
            {1, 1, 0, 0, 1},
            {0, 1, 1, 1, 0}
        };
        int[][] matrixB = {
            {0, 1, 0, 1, 0},
            {1, 0, 1, 1, 1},
            {0, 1, 0, 0, 1},
            {1, 1, 0, 0, 0},
            {0, 1, 1, 0, 0}
        };
        // System.out.println(isHamiltonian(matrixA));
        // System.out.println(isHamiltonian(matrixB));
        System.out.println(isHamiltonianBacktrack(matrixA));
        System.out.println(isHamiltonianBacktrack(matrixB));
    }
}
