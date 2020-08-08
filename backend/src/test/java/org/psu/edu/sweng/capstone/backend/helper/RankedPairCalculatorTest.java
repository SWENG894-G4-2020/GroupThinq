package org.psu.edu.sweng.capstone.backend.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.psu.edu.sweng.capstone.backend.dao.BallotOptionDAO;
import org.psu.edu.sweng.capstone.backend.dao.RankedPairWinnerDAO;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.BallotType;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.RankedPairWinner;
import org.psu.edu.sweng.capstone.backend.model.User;

@SuppressWarnings({"unchecked", "deprecation"})
@ExtendWith(MockitoExtension.class)
public class RankedPairCalculatorTest {

	@Mock
	private BallotOptionDAO ballotOptionDao;
	
	@Mock
	private RankedPairWinnerDAO rankedPairWinnerDao;
	
	@InjectMocks
	private static final RankedPairCalculator CALCULATOR = new RankedPairCalculator();
	
	private static final User USER = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
	
	private Ballot testBallot;
	
	@BeforeEach
	void setUp() {
		testBallot = createBallot();
	}

	@Test
	void createUniquePairs() {
		// given
		ArrayList<Long> ballotOptionIds = createBallotOptions(new Long[]{1L, 2L, 3L});
		
		// when
		ArrayList<UniquePair> uniquePairs = CALCULATOR.createUniquePairs(ballotOptionIds);
		
		// then
		assertEquals("[1, 2]", uniquePairs.get(0).toString());
		assertEquals("[1, 3]", uniquePairs.get(1).toString());
		assertEquals("[2, 3]", uniquePairs.get(2).toString());
	}
	
	@Test
	void sortAndLockWinners() {
		// given
		ArrayList<RankedPairWinner> rankedPairWinners = new ArrayList<>();
		BallotOption winningOption = new BallotOption("Winner", testBallot, USER);
		BallotOption losingOption = new BallotOption("Winner", testBallot, USER);

		rankedPairWinners.add(new RankedPairWinner(testBallot, winningOption, losingOption, 3L));
		rankedPairWinners.add(new RankedPairWinner(testBallot, winningOption, losingOption, 4L));
		
		// when
		when(rankedPairWinnerDao.findAllByBallotOrderByMarginDesc(testBallot)).thenReturn(rankedPairWinners);
		ArrayList<UniquePair> uniquePairList = CALCULATOR.sortAndLockWinners(testBallot);
		
		// then
		assertEquals(1, uniquePairList.size());
	}
	
	@Test
	void calculateWinner_noRemovalFromUniqueDestination() {
		// given
		ArrayList<UniquePair> uniquePairs = new ArrayList<>();
		uniquePairs.add(new UniquePair(1L, 2L));
		uniquePairs.add(new UniquePair(1L, 3L));
		uniquePairs.add(new UniquePair(2L, 3L));
		
		// when
		final Long winningOptionId = CALCULATOR.calculateWinner(uniquePairs, 3);
		
		// then
		assertEquals(1L, winningOptionId); // Alice wins in this scenario
	}
	
	@Test
	void calculateWinner_removeFromUniqueDestination() {
		// given
		ArrayList<UniquePair> uniquePairs = new ArrayList<>();
		uniquePairs.add(new UniquePair(1L, 2L));
		uniquePairs.add(new UniquePair(3L, 1L));
		uniquePairs.add(new UniquePair(2L, 3L));
		
		// when
		final Long winningOptionId = CALCULATOR.calculateWinner(uniquePairs, 3);
		
		// then
		assertEquals(3L, winningOptionId); // Charlie wins in this scenario
	}
	
	@Test
	void findLeaf_sinkAndSource() {
		// given
		ArrayList<UniquePair> graph = new ArrayList<>();
		graph.add(new UniquePair(1L, 2L));
		graph.add(new UniquePair(3L, 1L));
		graph.add(new UniquePair(2L, 3L));
		
		// when
		final Optional<Long> leaf = CALCULATOR.findLeaf(graph);
		
		// then
		assertFalse(leaf.isPresent());
	}
	
	@Test
	void findLeaf_sinkButNotSource() {
		// given
		ArrayList<UniquePair> graph = new ArrayList<>();
		graph.add(new UniquePair(1L, 2L));
		graph.add(new UniquePair(3L, 1L));
		graph.add(new UniquePair(3L, 2L));
		
		// when
		final Optional<Long> leaf = CALCULATOR.findLeaf(graph);
		
		// then
		assertTrue(leaf.isPresent());
	}
	
	@Test
	void isAcyclic_emptyGraph() {
		// given
		ArrayList<UniquePair> graph = new ArrayList<>();
		
		// when
		final Boolean acyclic = CALCULATOR.isAcyclic(graph);
		
		// then
		assertTrue(acyclic);
	}
	
	@Test
	void isAcyclic_oneElementInGraph() {
		// given
		ArrayList<UniquePair> graph = new ArrayList<>();
		graph.add(new UniquePair(1L, 2L));
		
		// when
		final Boolean acyclic = CALCULATOR.isAcyclic(graph);
		
		// then
		assertTrue(acyclic);
	}
	
	@Test
	void isAcyclic_returnsFalse_notEmpty() {
		// given
		ArrayList<UniquePair> graph = new ArrayList<>();
		graph.add(new UniquePair(1L, 2L));
		graph.add(new UniquePair(3L, 1L));
		graph.add(new UniquePair(3L, 2L));
		
		// when
		final Boolean acyclic = CALCULATOR.isAcyclic(graph);
		
		// then
		assertTrue(acyclic);
	}
	
	@Test
	void isAcyclic_returnsFalse_notPresent() {
		// given
		ArrayList<UniquePair> graph = new ArrayList<>();
		graph.add(new UniquePair(1L, 2L));
		graph.add(new UniquePair(3L, 1L));
		graph.add(new UniquePair(2L, 3L));
		
		// when
		final Boolean acyclic = CALCULATOR.isAcyclic(graph);
		
		// then
		assertFalse(acyclic);
	}
	
	@Test
	void determinePairWinners_firstOptionWins() {
		// given
		Ballot testBallot = createBallot();
		ArrayList<UniquePair> uniquePairs = new ArrayList<>();
		ArrayList<ArrayList<Long>> votes = new ArrayList<>();
		
		ArrayList<Long> vote = new ArrayList<>();
		vote.add(1L);
		vote.add(2L);
		vote.add(3L);
		
		votes.add(vote);
		
		uniquePairs.add(new UniquePair(1L, 2L));
		
		BallotOption winningBallotOption = new BallotOption("winner winner", testBallot, USER);
		BallotOption losingBallotOption = new BallotOption("chicken dinner", testBallot, USER);
		
		// when
		when(ballotOptionDao.findById(Mockito.anyLong())).thenReturn(Optional.of(winningBallotOption), Optional.of(losingBallotOption));
		CALCULATOR.determinePairWinners(testBallot, uniquePairs, votes);
		
		// then
		verify(rankedPairWinnerDao, times(1)).saveAll(Mockito.anyListOf(RankedPairWinner.class));
	}
	
	@Test
	void determinePairWinners_secondOptionWins() {
		// given
		Ballot testBallot = createBallot();
		ArrayList<UniquePair> uniquePairs = new ArrayList<>();
		ArrayList<ArrayList<Long>> votes = new ArrayList<>();
		
		ArrayList<Long> vote = new ArrayList<>();
		vote.add(1L);
		vote.add(2L);
		vote.add(3L);
		
		votes.add(vote);
		
		uniquePairs.add(new UniquePair(2L, 1L));
		
		BallotOption losingBallotOption = new BallotOption("chicken dinner", testBallot, USER);
		
		// when
		when(ballotOptionDao.findById(Mockito.anyLong())).thenReturn(Optional.empty(), Optional.of(losingBallotOption));
		CALCULATOR.determinePairWinners(testBallot, uniquePairs, votes);
		
		// then
		verify(rankedPairWinnerDao, times(1)).saveAll(Mockito.anyListOf(RankedPairWinner.class));
	}
	
	@Test
	void determinePairWinners_secondOption_emptyLosingOption() {
		// given
		Ballot testBallot = createBallot();
		ArrayList<UniquePair> uniquePairs = new ArrayList<>();
		ArrayList<ArrayList<Long>> votes = new ArrayList<>();
		
		ArrayList<Long> vote = new ArrayList<>();
		vote.add(1L);
		vote.add(2L);
		vote.add(3L);
		
		votes.add(vote);
		
		uniquePairs.add(new UniquePair(2L, 1L));
		
		BallotOption winnerBallotOption = new BallotOption("winner winner", testBallot, USER);
		
		// when
		when(ballotOptionDao.findById(Mockito.anyLong())).thenReturn(Optional.of(winnerBallotOption), Optional.empty());
		CALCULATOR.determinePairWinners(testBallot, uniquePairs, votes);
		
		// then
		verify(rankedPairWinnerDao, times(1)).saveAll(Mockito.anyListOf(RankedPairWinner.class));
	}
	
	@Test
	void determinePairWinners_secondOption_emptyOptions() {
		// given
		Ballot testBallot = createBallot();
		ArrayList<UniquePair> uniquePairs = new ArrayList<>();
		ArrayList<ArrayList<Long>> votes = new ArrayList<>();
		
		ArrayList<Long> vote = new ArrayList<>();
		vote.add(1L);
		vote.add(2L);
		vote.add(3L);
		
		votes.add(vote);
		
		uniquePairs.add(new UniquePair(2L, 1L));
		
		// when
		when(ballotOptionDao.findById(Mockito.anyLong())).thenReturn(Optional.empty(), Optional.empty());
		CALCULATOR.determinePairWinners(testBallot, uniquePairs, votes);
		
		// then
		verify(rankedPairWinnerDao, times(1)).saveAll(Mockito.anyListOf(RankedPairWinner.class));
	}
	
	@Test
	void runAlgorithm() {
		// given
		Ballot testBallot = createBallot();
		ArrayList<Long> ballotOptionIds = createBallotOptions(new Long[]{1L, 2L, 3L});
		ArrayList<ArrayList<Long>> votes = new ArrayList<>();
		
		ArrayList<Long> voteOne = new ArrayList<>();
		voteOne.add(1L);
		voteOne.add(2L);
		voteOne.add(3L);
		
		votes.add(voteOne);
		
		ArrayList<Long> voteTwo = new ArrayList<>();
		voteTwo.add(3L);
		voteTwo.add(2L);
		voteTwo.add(1L);
		
		votes.add(voteTwo);
		
		BallotOption winningBallotOption = new BallotOption("winner winner", testBallot, USER);
		BallotOption losingBallotOption = new BallotOption("chicken dinner", testBallot, USER);
		
		winningBallotOption.setId(1L);
		losingBallotOption.setId(2L);
		
		ArrayList<RankedPairWinner> rankedPairWinners = new ArrayList<>();
		
		RankedPairWinner rpw1 = new RankedPairWinner(testBallot, winningBallotOption, losingBallotOption, 3L);
		RankedPairWinner rpw2 = new RankedPairWinner(testBallot, losingBallotOption, winningBallotOption, 4L);
		
		rankedPairWinners.add(rpw1);
		rankedPairWinners.add(rpw2);
		
		// when
		when(rankedPairWinnerDao.findAllByBallotOrderByMarginDesc(testBallot)).thenReturn(rankedPairWinners);
		when(ballotOptionDao.findById(Mockito.anyLong())).thenReturn(Optional.of(winningBallotOption), Optional.of(losingBallotOption));
		final Long winningOptionId = CALCULATOR.runAlgorithm(testBallot, ballotOptionIds, votes);
		
		// then
		verify(rankedPairWinnerDao, times(1)).saveAll(Mockito.anyListOf(RankedPairWinner.class));
		assertEquals(1L, winningOptionId);
	}
	
	private Ballot createBallot() {
		Decision testDecision = new Decision("Test Decision", USER);
		BallotType testBallotType = new BallotType(1L, "Single-Choice");
		Ballot testBallot = new Ballot(testDecision, testBallotType, new Date(1337));
		
		return testBallot;
	}
	
	private ArrayList<Long> createBallotOptions(Long[] longs) {
		ArrayList<Long> ballotOptions = new ArrayList<>();
		
		for (int x = 0; x < longs.length; x++) { ballotOptions.add(longs[x]); }
		
		return ballotOptions;
	}
}
