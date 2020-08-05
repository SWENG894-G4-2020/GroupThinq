package org.psu.edu.sweng.capstone.backend.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.psu.edu.sweng.capstone.backend.dao.BallotOptionDAO;
import org.psu.edu.sweng.capstone.backend.dao.RankedPairWinnerDAO;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.BallotType;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.RankedPairWinner;
import org.psu.edu.sweng.capstone.backend.model.User;

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
