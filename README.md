# TravellingSalesmanProblem
 Learning to Implement Solutions to the Travelling Salesman Problem. This was done in 3 main stages:
 1. Learning how to generate the Next Lexicographical Permutation of a Numerical Sequence
 2. Determing whether a graph is a Hamiltonian Cycle or not
 3. Creating a class to solve the TSP by different algorithms (Exact, Heuristic)

## IntegerPermutation.java
 This class solves The Next Lexicographical Permutation of a Numerical Sequence Problem.

## Hamiltonian.java
 This class determines whether a given graph contains a Hamiltonian Cycle.
 It does this through either generating all possible permutations (learnt in the `IntegerPermutation.java` class) and checking them, or by using a backtracking algorithm and pruning when a partial solution is invalid.

## TSPGraph.java
 I solved the TSP using 2 different algorithm approaches: Exact and Heuristic.
 For the Exact algorithms, I used a Brute Force approach (trying all permutations and seeing which one is the cheapest) and a Backtracking approach (using a similar concept to the Hamiltonian Cycle Problem in the `Hamiltonian.java` class).
 For the Heuristic algorithm, I used a Nearest Neighbour approach, which chooses the nearest neighbouring city as the next move.
 This approach may find a quick solution, however it may not always find the optimal route due to the algorithm being greedy.