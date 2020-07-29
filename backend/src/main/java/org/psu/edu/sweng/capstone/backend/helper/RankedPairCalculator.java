package org.psu.edu.sweng.capstone.backend.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class RankedPairCalculator {
	
	public static void main(String[]args) {
		ArrayList<String> options = establishBallotOptions();
		
		ArrayList<ArrayList<String>> votes = establishVotes();		
		
		ArrayList<ArrayList<String>> uniquePairs = createUniquePairs(options);
		
		HashMap<ArrayList<String>, HashMap<String, Long>> winners = determinePairWinners(uniquePairs, votes);
	}

	private static ArrayList<String> establishBallotOptions() {
		ArrayList<String> options = new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie"));
		
		System.out.println("Ballot Options: \n");
		options.forEach(option -> System.out.println(option));
		
		return options;
	}

	private static ArrayList<ArrayList<String>> establishVotes() {
		ArrayList<ArrayList<String>> votes = new ArrayList<>();
		
		votes.add(new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie")));
		votes.add(new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie")));
		votes.add(new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie")));
		votes.add(new ArrayList<>(Arrays.asList("Bob", "Charlie", "Alice")));
		votes.add(new ArrayList<>(Arrays.asList("Bob", "Charlie", "Alice")));
		votes.add(new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob")));
		votes.add(new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob")));
		votes.add(new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob")));
		votes.add(new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob")));
		
		System.out.println("\nNumber of Votes: " + votes.size() + "\n");
		votes.forEach(vote -> System.out.println(vote));
		
		return votes;
	}

	/** Creates unique pairs based upon the a list of Ballot Options.
	 * @param options The list of Ballot options (sorted alphabetically).
	 */
	private static ArrayList<ArrayList<String>> createUniquePairs(ArrayList<String> options) {
		ArrayList<ArrayList<String>> uniquePairs = new ArrayList<>();
		
		for (int x = 0; x < options.size(); x++) {
			if (x < options.size() - 1) {
				int y = 1;
				
				while (y < options.size()) {
					ArrayList<String> pair = new ArrayList<>();
					
					int nextPotentialOption = x + y;
					if (nextPotentialOption < options.size()) {
						pair.add(options.get(x));
						pair.add(options.get(nextPotentialOption));
						
						uniquePairs.add(pair);
					}
					
					y++;
				}
			}
		}
		
		System.out.println("\nUnique Pairs: \n");
		uniquePairs.forEach(pair -> System.out.println(pair));
		System.out.println();
		
		return uniquePairs;
	}
	
	/** Calculates the winner between each unique pair.
	 * @param uniquePairs A list of all unique pairs.
	 * @param votes A list of all the votes casted.
	 * @return A HashMap containing the winning difference between one unique pair.
	 */
	private static HashMap<ArrayList<String>, HashMap<String, Long>> determinePairWinners(ArrayList<ArrayList<String>> uniquePairs, 
			ArrayList<ArrayList<String>> votes) {
		
		HashMap<ArrayList<String>, HashMap<String, Long>> winners = new HashMap<>();
		
		uniquePairs.forEach(pair -> {
			HashMap<String, Long> winner = new HashMap<>();
			
			int firstOptionTallies = 0;
			int secondOptionTallies = 0;
			
			String firstOption = pair.get(0);
			String secondOption = pair.get(1);
			
			for (ArrayList<String> vote : votes) {
				int firstOptionIndex = 0;
				int secondOptionIndex = 0;
				
				for (int x = 0; x < vote.size(); x++) {
					if (vote.get(x).equals(firstOption)) {
						firstOptionIndex = x;
					}
					
					if (vote.get(x).equals(secondOption)) {
						secondOptionIndex = x;
					}
				}
				
				if (firstOptionIndex < secondOptionIndex) {
					firstOptionTallies++;
				}
				else {
					secondOptionTallies++;
				}
			}
			
			int difference;
			if (firstOptionTallies > secondOptionTallies) {
				difference = firstOptionTallies - secondOptionTallies;
				winner.put(firstOption, Long.valueOf(difference));
			}
			else {
				difference = secondOptionTallies - firstOptionTallies;
				winner.put(secondOption, Long.valueOf(difference));
			}
			
			winners.put(pair, winner);
		});
		
		for (Entry<ArrayList<String>, HashMap<String, Long>> winner : winners.entrySet()) {
			System.out.println("Winner between " + winner.getKey() + " is " + winner.getValue().keySet() + 
					" with a difference of " + winner.getValue().values());
		}
		
		return winners;
	}
}
