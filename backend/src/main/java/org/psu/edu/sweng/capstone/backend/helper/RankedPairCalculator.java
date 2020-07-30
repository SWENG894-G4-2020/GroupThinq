package org.psu.edu.sweng.capstone.backend.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankedPairCalculator {
	
	public static void main(String[]args) {
		ArrayList<String> options = establishBallotOptions();
		
		ArrayList<ArrayList<String>> votes = establishVotes();		
		
		ArrayList<UniquePair> uniquePairs = createUniquePairs(options);
				
		ArrayList<RankedPairWinner> winners = determinePairWinners(uniquePairs, votes);
		
		ArrayList<UniquePair> lockedWinners = sortAndLockWinners(winners);
		
		String winner = calculateWinner(lockedWinners, options.size());
		
		System.out.println("\nWinner is " + winner + "!");
	}

	private static ArrayList<String> establishBallotOptions() {
		ArrayList<String> options = new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie"));
		
		System.out.println("Ballot Options:");
		options.forEach(option -> System.out.println(option));
		
		return options;
	}

	private static ArrayList<ArrayList<String>> establishVotes() {
		ArrayList<ArrayList<String>> votes = new ArrayList<>();
		
		// Scenario #1
//		votes.add(new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie")));
//		votes.add(new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie")));
//		votes.add(new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie")));
//		votes.add(new ArrayList<>(Arrays.asList("Bob", "Charlie", "Alice")));
//		votes.add(new ArrayList<>(Arrays.asList("Bob", "Charlie", "Alice")));
//		votes.add(new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob")));
//		votes.add(new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob")));
//		votes.add(new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob")));
//		votes.add(new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob")));
		
		// Scenario #2
		votes.add(new ArrayList<>(Arrays.asList("Alice", "Charlie", "Bob")));
		votes.add(new ArrayList<>(Arrays.asList("Alice", "Charlie", "Bob")));
		votes.add(new ArrayList<>(Arrays.asList("Bob", "Alice", "Charlie")));
		votes.add(new ArrayList<>(Arrays.asList("Bob", "Alice", "Charlie")));
		votes.add(new ArrayList<>(Arrays.asList("Bob", "Alice", "Charlie")));
		votes.add(new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob")));
		votes.add(new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob")));
		votes.add(new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob")));
		votes.add(new ArrayList<>(Arrays.asList("Charlie", "Bob", "Alice")));
		
		System.out.println("\nNumber of Votes: " + votes.size());
		votes.forEach(vote -> System.out.println(vote));
		
		return votes;
	}

	/** Creates unique pairs based upon the a list of Ballot Options.
	 * @param options The list of Ballot options (sorted alphabetically).
	 */
	private static ArrayList<UniquePair> createUniquePairs(ArrayList<String> options) {
		ArrayList<UniquePair> uniquePairs = new ArrayList<>();
		
		for (int x = 0; x < options.size(); x++) {
			if (x < options.size() - 1) {
				int y = 1;
				
				while (y < options.size()) {
					int nextPotentialOption = x + y;
					if (nextPotentialOption < options.size()) {
						UniquePair up = new UniquePair(options.get(x), options.get(nextPotentialOption));						
						uniquePairs.add(up);
					}
					
					y++;
				}
			}
		}
		
		System.out.println("\nUnique Pairs:");
		uniquePairs.forEach(pair -> System.out.println(pair));
		
		return uniquePairs;
	}
	
	/** Calculates the winner between each unique pair.
	 * @param uniquePairs A list of all unique pairs.
	 * @param votes A list of all the votes casted.
	 * @return A HashMap containing the winning difference between one unique pair.
	 */
	private static ArrayList<RankedPairWinner> determinePairWinners(ArrayList<UniquePair> uniquePairs, 
			ArrayList<ArrayList<String>> votes) {
		
		ArrayList<RankedPairWinner> winners = new ArrayList<>();
		
		uniquePairs.forEach(pair -> {			
			int firstOptionTallies = 0;
			int secondOptionTallies = 0;
			
			String firstOption = pair.getOptionOne();
			String secondOption = pair.getOptionTwo();
			
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
			
			String winningOption = (firstOptionTallies > secondOptionTallies) ? firstOption : secondOption;
			
			int difference = (winningOption.equals(firstOption) ? 
					(firstOptionTallies - secondOptionTallies) : (secondOptionTallies - firstOptionTallies));
			
			winners.add(new RankedPairWinner(pair, winningOption, difference));
		});
		
		System.out.println();
		winners.forEach(winner -> {
			System.out.println("Winner between " + winner.getUniquePair() + " is " + winner.getWinningOption() +
					" by a margin of " + winner.getVoteDifference() + " votes.");
		});
		
		System.out.println();
		
		return winners;
	}
	
	/** Sorts and locks the winners of the one on one matchups for each unique pair. 
	 * 
	 * @param winners A list of winner objects, containing the unique pair, the winner, and margin of votes in victory.
	 * @return A list of the locked winners, sorted in order.
	 */
	private static ArrayList<UniquePair> sortAndLockWinners(ArrayList<RankedPairWinner> winners) {
		Collections.sort(winners, new Comparator<RankedPairWinner>() {
		    public int compare(RankedPairWinner o1, RankedPairWinner o2) {
		        return o2.getVoteDifference() - o1.getVoteDifference();
		    }
		});
		
		ArrayList<UniquePair> lockedPairs = new ArrayList<>();
		
		winners.forEach(winner -> {
			String winningOption;
			String losingOption;
			
			if (winner.getWinningOption() == winner.getUniquePair().getOptionTwo()) {
				winningOption = winner.getWinningOption();
				losingOption = winner.getUniquePair().getOptionOne();
				
				lockedPairs.add(new UniquePair(winningOption, losingOption));

			}
			else {
				winningOption = winner.getUniquePair().getOptionOne();
				losingOption = winner.getUniquePair().getOptionTwo();
				
				lockedPairs.add(new UniquePair(winningOption, losingOption));
			}
		});
		
		System.out.println("Locked in:");
		lockedPairs.forEach(pair -> {
			System.out.println(pair.getOptionOne() + " over " + pair.getOptionTwo());
		});
		
		return lockedPairs;
	}
	
	/** Calculates the winner from the locked sorted list of unique pair winners.
	 * 
	 * @param lockedWinners A list containing all the locked winners
	 * @param optionListSize An integer containing the list size of all ballot options.
	 * @return The winning Ballot Option
	 */
	private static String calculateWinner(ArrayList<UniquePair> lockedWinners, int optionListSize) {
		List<String> sourceList = new ArrayList<>();
		List<String> uniqueDestinations = new ArrayList<>();
		
		for (UniquePair up : lockedWinners) {
			uniqueDestinations.add(up.getOptionTwo());

			if (uniqueDestinations.size() < optionListSize) {
				sourceList.add(up.getOptionOne());
			}
		}
		
		List<String> winners = new ArrayList<>(sourceList);
		for (String s : winners) {
			if (uniqueDestinations.contains(s)) {
				winners.remove(s);
			}
		}
		
		return winners.get(0);
	}
}
