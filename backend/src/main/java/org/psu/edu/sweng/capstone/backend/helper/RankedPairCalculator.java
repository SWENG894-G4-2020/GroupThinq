package org.psu.edu.sweng.capstone.backend.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.psu.edu.sweng.capstone.backend.dao.BallotOptionDAO;
import org.psu.edu.sweng.capstone.backend.dao.RankedPairWinnerDAO;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.RankedPairWinner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RankedPairCalculator {
	
	@Autowired
	private BallotOptionDAO ballotOptionDao;
	
	@Autowired
	private RankedPairWinnerDAO rankedPairWinnerDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RankedPairCalculator.class);
	
	public Long runAlgorithm(final Ballot ballot, final List<Long> options, final List<ArrayList<Long>> votes) {	
		determinePairWinners(ballot, createUniquePairs(options), votes);
		return calculateWinner(sortAndLockWinners(ballot), options.size());
	}

	/** Creates unique pairs based upon the a list of Ballot Options.
	 * @param options The list of Ballot options (sorted alphabetically).
	 */
	protected ArrayList<UniquePair> createUniquePairs(final List<Long> options) {
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
		
		LOGGER.debug("Unique Pairs:");
		uniquePairs.forEach(pair -> LOGGER.debug(pair.toString()));
		
		return uniquePairs;
	}
	
	/** Calculates the winner between each unique pair.
	 * @param uniquePairs A list of all unique pairs.
	 * @param votes A list of all the votes casted.
	 * @return A HashMap containing the winning difference between one unique pair.
	 */
	protected void determinePairWinners(final Ballot ballot, final ArrayList<UniquePair> uniquePairs, final List<ArrayList<Long>> votes) {
		ArrayList<RankedPairWinner> winners = new ArrayList<>();
		
		uniquePairs.forEach(pair -> {			
			int firstOptionTallies = 0;
			int secondOptionTallies = 0;
			
			final Long firstOption = pair.getOptionOne();
			final Long secondOption = pair.getOptionTwo();
			
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
			
			boolean firstOptionWins = false;
			if (firstOptionTallies > secondOptionTallies) { firstOptionWins = true; }
			
			final Long winningOption = (firstOptionWins) ? firstOption : secondOption;
			final Long losingOption = (firstOptionWins) ? secondOption : firstOption;
			final int margin = (firstOptionWins) ? (firstOptionTallies - secondOptionTallies) : (secondOptionTallies - firstOptionTallies);
			
			Optional<BallotOption> winningBallotOption = ballotOptionDao.findById(winningOption);
			Optional<BallotOption> losingBallotOption = ballotOptionDao.findById(losingOption);
			
			if (winningBallotOption.isPresent() && losingBallotOption.isPresent()) {
				RankedPairWinner rpw = new RankedPairWinner(ballot, winningBallotOption.get(), losingBallotOption.get(), Long.valueOf(margin));
				winners.add(rpw);
			}
		});
		
		winners.forEach(winner -> LOGGER.debug("Winner is {} over {} by a margin of {} votes.", 
				winner.getWinner(), winner.getLoser(), winner.getMargin()));
		
		rankedPairWinnerDao.saveAll(winners);
	}
	
	/** Sorts and locks the winners of the one on one matchups for each unique pair. 
	 * 
	 * @param winners A list of winner objects, containing the unique pair, the winner, and margin of votes in victory.
	 * @return A list of the locked winners, sorted in order.
	 */
	@SuppressWarnings("unchecked")
	protected ArrayList<UniquePair> sortAndLockWinners(final Ballot ballot) {
		ArrayList<RankedPairWinner> winners = rankedPairWinnerDao.findAllByBallotOrderByMarginDesc(ballot);
		
		ArrayList<UniquePair> lockedGraph = new ArrayList<>();
		
		winners.forEach(winner -> {
			ArrayList<UniquePair> potentialGraph = (ArrayList<UniquePair>) lockedGraph.clone();
			
			final Long winningOption = winner.getWinner().getId();
			final Long losingOption = winner.getLoser().getId();
			
			UniquePair up = new UniquePair(winningOption, losingOption);
			
			potentialGraph.add(up);
			
			if (isAcyclic(potentialGraph)) { lockedGraph.add(up); }
		});
		
		LOGGER.debug("Locked in:");
		lockedGraph.forEach(pair -> LOGGER.debug("{} over {}", pair.getOptionOne(), pair.getOptionTwo()));
		
		return lockedGraph;
	}
	
	/** Calculates the winner from the locked sorted list of unique pair winners.
	 * 
	 * @param lockedWinners A list containing all the locked winners
	 * @param optionListSize An integer containing the list size of all ballot options.
	 * @return The winning Ballot Option
	 */
	protected Long calculateWinner(final ArrayList<UniquePair> lockedWinners, int optionListSize) {
		List<Long> sourceList = new ArrayList<>();
		List<Long> uniqueDestinations = new ArrayList<>();
		
		for (UniquePair up : lockedWinners) {
			uniqueDestinations.add(up.getOptionTwo());

			if (uniqueDestinations.size() < optionListSize) {
				sourceList.add(up.getOptionOne());
			}
		}
		
		List<Long> winners = Collections.synchronizedList(new ArrayList<>(sourceList));
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
	protected boolean isAcyclic(final ArrayList<UniquePair> graph) {
		// A Graph containing 0 or 1 edges is by nature acyclic
		if (graph.isEmpty() || graph.size() == 1) { return true; }

		// A graph with non-zero edges but no leafs is by nature cyclic
		Optional<Long> leaf = findLeaf(graph);
		if (!leaf.isPresent()) { return false; }

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
	protected Optional<Long> findLeaf(final ArrayList<UniquePair> graph) {
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
