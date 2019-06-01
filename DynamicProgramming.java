import java.util.*;


public class DynamicProgramming {

	private Hashtable<Integer, Integer> minCoins = new Hashtable<Integer, Integer>();
	
	public int solve(int[] denominations, int goal) {
		if (goal == 0) {
			return 0;
		}
		int minSolution = Integer.MAX_VALUE;
		for(int denomination : denominations) {
			if (goal >= denomination) {
				Integer solved = minCoins.get(goal - denomination);
				if (solved == null) {
					solved = solve(denominations, goal - denomination);
				}
				minSolution = Math.min(minSolution, solved + 1);
			}
		}
		minCoins.put(goal, minSolution);
		return minSolution;
	}
}
