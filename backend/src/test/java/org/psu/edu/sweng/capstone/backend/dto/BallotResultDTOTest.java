package org.psu.edu.sweng.capstone.backend.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.BallotVote;
import org.psu.edu.sweng.capstone.backend.model.User;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BallotResultDTOTest {

    private static final User USER = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
    private Ballot ballot = new Ballot(null, null, new Date(1337L));
    private BallotOption ballotOption = new BallotOption("BK Lounge", ballot, USER);
    private BallotVote ballotResult = new BallotVote(ballot, ballotOption, USER);
    private BallotResultDTO testDTO;

    @BeforeEach
    void setUp() {
        ballot.setId(1L);
        ballotOption.setId(2L);
        ballotResult.setRank(3L);
        ballotResult.setVoteDate(new Date(333L));
        ballotResult.setVoteUpdatedDate(new Date(444L));
        testDTO = BallotResultDTO.build(ballotResult);
    }

    @Test
    void getters_workProperly() {
        assertEquals(1L, testDTO.getBallotId());
        assertEquals(2L, testDTO.getBallotOptionId());
        assertEquals(3L, testDTO.getRank());
        assertEquals("pop pop", testDTO.getUserName());
        assertEquals(new Date(333L), testDTO.getVoteDate());
        assertEquals(new Date(444L), testDTO.getVoteUpdatedDate());
    }

    @Test
    void setters_workProperly() {
        // when
        testDTO.setBallotOptionId(2L);
        testDTO.setBallotId(3L);
        testDTO.setRank(4L);
        testDTO.setVoteDate(new Date(1111L));
        testDTO.setVoteUpdatedDate(new Date(2222L));
        testDTO.setUserName("username");

        // then
        assertEquals(2L, testDTO.getBallotOptionId());
        assertEquals(3L, testDTO.getBallotId());
        assertEquals(4L, testDTO.getRank());
        assertEquals(new Date(1111L), testDTO.getVoteDate());
        assertEquals(new Date(2222L), testDTO.getVoteUpdatedDate());
        assertEquals("username", testDTO.getUserName());
    }

    @Test
    void build_handlesNullsProperly() {
        // given
        BallotVote testResult = new BallotVote(null, null, null);
        testResult.setRank(null);
        testResult.setVoteDate(null);
        // when
        BallotResultDTO testDTO = BallotResultDTO.build(testResult);

        // then
        assertNull(testDTO.getRank());
        assertNull(testDTO.getBallotOptionId());
        assertNull(testDTO.getVoteDate());
        assertNull(testDTO.getUserName());
        assertNull(testDTO.getVoteUpdatedDate());
        assertNull(testDTO.getBallotId());
    }


}
