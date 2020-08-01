package org.psu.edu.sweng.capstone.backend.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RankedPairCalculator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RankedPairCalculator.class);
	
	public Long runAlgorithm(List<Long> options, List<ArrayList<Long>> votes) {
		ArrayList<UniquePair> uniquePairs = createUniquePairs(options);
		ArrayList<RankedPairWinner> winners = determinePairWinners(uniquePairs, votes);
		ArrayList<UniquePair> lockedWinners = sortAndLockWinners(winners);
		
		return calculateWinner(lockedWinners, options.size());
	}

	/** Creates unique pairs based upon the a list of Ballot Options.
	 * @param options The list of Ballot options (sorted alphabetically).
	 */
	private static ArrayList<UniquePair> createUniquePairs(List<Long> options) {
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
		
		LOGGER.info("Unique Pairs:");
		uniquePairs.forEach(pair -> LOGGER.info(pair.toString()));
		
		return uniquePairs;
	}
	
	/** Calculates the winner between each unique pair.
	 * @param uniquePairs A list of all unique pairs.
	 * @param votes A list of all the votes casted.
	 * @return A HashMap containing the winning difference between one unique pair.
	 */
	private static ArrayList<RankedPairWinner> determinePairWinners(ArrayList<UniquePair> uniquePairs, List<ArrayList<Long>> votes) {
		ArrayList<RankedPairWinner> winners = new ArrayList<>();
		
		uniquePairs.forEach(pair -> {			
			int firstOptionTallies = 0;
			int secondOptionTallies = 0;
			
			Long firstOption = pair.getOptionOne();
			Long secondOption = pair.getOptionTwo();
			
			for (ArrayList<Long> vote : votes) {
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
			
			Long winningOption = (firstOptionTallies > secondOptionTallies) ? firstOption : secondOption;
			
			int difference = (winningOption.equals(firstOption) ? 
					(firstOptionTallies - secondOptionTallies) : (secondOptionTallies - firstOptionTallies));
			
			winners.add(new RankedPairWinner(pair, winningOption, difference));
		});
		
		winners.forEach(winner -> LOGGER.info("Winner between {} is {} by a margin of {} votes.", 
				winner.getUniquePair(), winner.getWinningOption(), winner.getVoteDifference()));
		
		return winners;
	}
	
	/** Sorts and locks the winners of the one on one matchups for each unique pair. 
	 * 
	 * @param winners A list of winner objects, containing the unique pair, the winner, and margin of votes in victory.
	 * @return A list of the locked winners, sorted in order.
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<UniquePair> sortAndLockWinners(ArrayList<RankedPairWinner> winners) {	
		Collections.sort(winners, (RankedPairWinner r1, RankedPairWinner r2) -> r2.getVoteDifference() - r1.getVoteDifference());
		
		ArrayList<UniquePair> lockedGraph = new ArrayList<>();
		
		winners.forEach(winner -> {
			Long winningOption;
			Long losingOption;
			
			ArrayList<UniquePair> potentialGraph = (ArrayList<UniquePair>) lockedGraph.clone();
			
			if (winner.getWinningOption().equals(winner.getUniquePair().getOptionTwo())) {
				winningOption = winner.getWinningOption();
				losingOption = winner.getUniquePair().getOptionOne();
			} else {
				winningOption = winner.getUniquePair().getOptionOne();
				losingOption = winner.getUniquePair().getOptionTwo();
			}

			potentialGraph.add(new UniquePair(winningOption, losingOption));
			if (isAcyclic(potentialGraph)) { lockedGraph.add(new UniquePair(winningOption, losingOption)); }

		});
		
		LOGGER.info("Locked in:");
		lockedGraph.forEach(pair -> LOGGER.info("{} over {}", pair.getOptionOne(), pair.getOptionTwo()));
		
		return lockedGraph;
	}
	
	/** Calculates the winner from the locked sorted list of unique pair winners.
	 * 
	 * @param lockedWinners A list containing all the locked winners
	 * @param optionListSize An integer containing the list size of all ballot options.
	 * @return The winning Ballot Option
	 */
	private static Long calculateWinner(ArrayList<UniquePair> lockedWinners, int optionListSize) {
		List<Long> sourceList = new ArrayList<>();
		List<Long> uniqueDestinations = new ArrayList<>();
		
		for (UniquePair up : lockedWinners) {
			uniqueDestinations.add(up.getOptionTwo());

			if (uniqueDestinations.size() < optionListSize) {
				sourceList.add(up.getOptionOne());
			}
		}
		
		List<Long> winners = new ArrayList<>(sourceList);
		for (Long s : winners) {
			if (uniqueDestinations.contains(s)) {
				winners.remove(s);
			}
		}
		
		return winners.get(0);
	}

	/** Determines if a potential locked graph contains a cycle.
	 *
	 * @param graph A graph to test for cyclicity, represented by an ArrayList of edges
	 * @return If graph is acyclic (does not contain any cycles)
	 */
	@SuppressWarnings("unchecked")
	private static boolean isAcyclic(ArrayList<UniquePair> graph) {
		// A Graph containing 0 or 1 edges is by nature acyclic
		if (graph.isEmpty() || graph.size() == 1) { return true; }

		// A graph with non-zero edges but no leafs is by nature cyclic
		Optional<Long> leaf = findLeaf(graph);
		if (!graph.isEmpty() && !leaf.isPresent()) {
			return false;
		}

		// Otherwise, find and remove a leaf and test again
		ArrayList<UniquePair> reducedGraph = (ArrayList<UniquePair>) graph.clone();
		reducedGraph.removeIf(edge -> edge.getOptionTwo().equals(leaf.orElse(null)));

		return isAcyclic(reducedGraph);
	}

	/** Determines a leaf node for a given graph, if one exists.
	 *
	 * @param graph The graph for which a leaf node should be found from.
	 * @return An Optional containing a String of a leaf node, if it exists.
	 */
	private static Optional<Long> findLeaf(ArrayList<UniquePair> graph) {
		// A leaf is a node which has incoming edges but no outgoing edges (ie. only a sink)
		// First, collect all sources and sinks
		Set<Long> sources = new HashSet<>();
		Set<Long> sinks = new HashSet<>();
		
		graph.forEach(pair -> {
			sources.add(pair.getOptionOne());
			sinks.add(pair.getOptionTwo());
		});

		// Then, find a node which is a sink but not a source, that is a leaf
		Long leaf = null;
		for (Long sink : sinks) {
			if (!sources.contains(sink)) { leaf = sink; }
		}

		return Optional.ofNullable(leaf);
	}
}
