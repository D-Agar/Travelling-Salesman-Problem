package tsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IntegerPermutation {
    public static List<Integer> nextPermutation (List<Integer> sequence) {
        // See if the next element, going down, is larger than the previous
        // Identify the longest suffix non-increasing
        int i = sequence.size() - 1, j = sequence.size() - 1;
        while (i > 0 && sequence.get(i-1) >= sequence.get(i)) {
            i--;
        }
        // Check if it's the last permutation
        if (i <= 0) {
            return null;
        }
        // Find the largest index j
        // index i-1 is the pivot
        while (sequence.get(j) <= sequence.get(i-1)) {
            j--;
        }
        sequence = swap(sequence, i-1, j);
        // Reverse the suffix starting at sequence[i]
        j = sequence.size() - 1;
        while (i < j) {
            sequence = swap(sequence, i, j);
            i++; j--;
        }
        return sequence;
    }

    // Function to swap two values in a given sequence
    private static List<Integer> swap (List<Integer> sequence, int i, int j) {
        // Convert to array
        Integer[] seqArr = new Integer[sequence.size()];   
        for (int index = 0; index < sequence.size(); index++) {
            seqArr[index] = sequence.get(index);
        }
        int tmp = seqArr[i];
        seqArr[i] = seqArr[j];
        seqArr[j] = tmp;
        // Convert to List<Integer> again
        return (List<Integer>) Arrays.asList(seqArr);
    }

    // Function to generate all permutations of a sequence 0 ... n
    public static Set<List<Integer>> allPermutations (int n) throws IllegalArgumentException {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        Set<List<Integer>> permutations = new HashSet<>();
        // Create the starting sequence 0, 1, ..., n
        List<Integer> sequence = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            sequence.add(i);
        }
        permutations.add(sequence);
        // Get factorial
        // Generate all permutations, taking O(n!) time
        while (sequence != null) {
            sequence = nextPermutation(sequence);
            permutations.add(sequence);
        }
        return permutations;
    }


    // Function to return all the permutations of a sequence 0 ... n using a backtracking algorithm
    public static Set<List<Integer>> allPermutationsBackTrack (int n) throws IllegalArgumentException {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        Set<List<Integer>> permutations = new HashSet<>();
        // Create the starting sequence 0, 1, ..., n
        List<Integer> sequence = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            sequence.add(i);
        }
        backTrackHelper(n, 0, permutations, sequence);
        return permutations;
    }

    // Private function to use recursion and memoization
    // Takes n, the current index we're recursing through and the set of permutations and the current sequence
    private static void backTrackHelper (int n, int index, Set<List<Integer>> permutations, List<Integer> sequence) {
        // Base case: permutation generated
        if (index == n - 1) {
            permutations.add(sequence);
        }
        // General case: try all possible solutions from here
        else {
            for (int i = index; i < n; i++) {
                // Swap sequence[index] and sequence[i]
                swap(sequence, index, i);
                backTrackHelper(n, index + 1, permutations, sequence);
                // swap the values again to continue the backtracking
                swap(sequence, index, i);
            }
        }
    }

    public static void main(String[] args) {
        // List<Integer> sequence = new ArrayList<Integer>();
        // for (int i = 0; i < 4; i++) {
        //     sequence.add(i);
        // }
        // System.out.println(sequence);
        // for (int i = 0; i < 4; i++) {
        //     sequence = nextPermutation(sequence);
        //     System.out.println(sequence);
        // }
        Set<List<Integer>> permutations = allPermutations(4);
        System.out.println(permutations);
        // Set<List<Integer>> permutationsBackTracking = allPermutationsBackTrack(4);
        // System.out.println(permutations.equals(permutationsBackTracking));
    }
}
