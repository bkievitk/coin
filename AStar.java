import java.util.*;

public class AStar {

	public HashSet<State> closedSet = new HashSet<State>();
	public HashSet<State> openSet = new HashSet<State>();
	public Hashtable<State, Integer> gValue = new Hashtable<State, Integer>();
	public PriorityQueue<State> fValue = new PriorityQueue<State>(1000, new Comparator<State>() {
		@Override
		public int compare(State arg0, State arg1) {
			if (arg0.fScore < arg1.fScore) {
				return -1;
			} else {
				return 1;
			}
		}
	});
	
	public static void main(String[] args) {
		long start1 = (new Date()).getTime();
		for(int i=0;i<10000;i++) {
			AStar astar = new AStar();
			int[] coins = createCoins(5);
			int goal = 0;
			goal += coins[0] * (int)(Math.random() * 2);
			goal += coins[1] * (int)(Math.random() * 2);
			goal += coins[2] * (int)(Math.random() * 2);
			goal += coins[3] * (int)(Math.random() * 2);
			goal += coins[4] * (int)(Math.random() * 2);			
			astar.solve(coins, goal);
		}
		long stop1 = (new Date()).getTime();
		System.out.println("AStar Complete");
		long start2 = (new Date()).getTime();
		for(int i=0;i<10000;i++) {
			DynamicProgramming dp = new DynamicProgramming();
			int[] coins = createCoins(5);
			int goal = 0;
			goal += coins[0] * (int)(Math.random() * 2);
			goal += coins[1] * (int)(Math.random() * 2);
			goal += coins[2] * (int)(Math.random() * 2);
			goal += coins[3] * (int)(Math.random() * 2);
			goal += coins[4] * (int)(Math.random() * 2);
			dp.solve(coins, goal);
		}
		long stop2 = (new Date()).getTime();
		System.out.println("AStar: " + (stop1 - start1));
		System.out.println("DynamicProgramming: " + (stop2 - start2));
	}
	
	public static int[] createCoins(int size) {
		int[] coins = new int[size];
		for(int i=0;i<size;i++) {
			coins[i] = (int)(Math.random() * 50) + 1;
		}
		Arrays.sort(coins);
		reverse(coins);	
		return coins;
	}
	
	public static void reverse(int[] input) {
		int last = input.length - 1;
		int middle = input.length / 2;
		for (int i = 0; i <= middle; i++) {
			int temp = input[i];
			input[i] = input[last - i];
			input[last - i] = temp;
		}
	}
	
	// Highest denomination at 0.
	public int solve(int[] denominations, int goal) {
		State start = new State(new int[denominations.length], denominations, goal);
		openSet.add(start);
		gValue.put(start, 0);
		fValue.add(start);
		
		while (!openSet.isEmpty()) {	
			State current = fValue.poll();			
			if (current.complete(goal)) {
				return current.numCoins();
			}
			openSet.remove(current);
			closedSet.add(current);
			
			for(int i=0;i<denominations.length;i++) {
				int[] newCounts = new int[current.counts.length];
				for(int j=0;j<newCounts.length;j++) {
					newCounts[j] = current.counts[j];
				}
				newCounts[i] ++;
				State newState = new State(newCounts, denominations, goal);
				if (newState.past(goal) || closedSet.contains(newState)) {
					continue;
				}
				
				int tentativeGScore = gValue.get(current) + 1;
				if (!openSet.contains(newState)) {
					openSet.add(newState);
				} else if (tentativeGScore >= gValue.get(newState)) {
					continue;
				}
				gValue.put(newState, tentativeGScore);
				fValue.add(newState);
			}
		}
		return -1;
	}
	
	class State {
		public int[] counts;
		public double fScore;
		public int sum = 0;
		public int count = 0;
		
		public State(int[] counts, int[] denominations, int goal) {
			this.counts = counts;
			for (int i=0;i<denominations.length; i++) {
				goal -= denominations[i] * counts[i];
				sum += denominations[i] * counts[i];
				count ++;
			}
			if (goal == 0) {
				// Force float to 0.
				fScore = 0;
			} else {
				fScore = goal / (double)denominations[0];
			}
		}
		public int numCoins() {
			int sum = 0;
			for(int count : counts) {
				sum += count;
			}
			return sum;
		}
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for(int count : counts) {
				sb.append(count + ",");
			}
			sb.append("; fScore: " + fScore);
			
			
			return sb.toString();
		}
		
		public boolean complete(int goal) {
			return goal <= sum;
		}
		
		public boolean past(int goal) {
			return goal < sum;
		}
		
		public int hashCode() {
			// Ignore the specific ways to make coins and use count and sum to differentiate states.
			//return Arrays.hashCode(counts);
			return Objects.hash(sum, count);
		}
		
		public boolean equals(Object obj) {
			if (obj instanceof State) {
				State state = (State)obj;
				// Ignore the specific ways to make coins and use count and sum to differentiate states.
				//return Arrays.equals(counts, state).counts);
				return state.counts == counts && state.sum == sum;
			}
			return false;
		}
	}
}
