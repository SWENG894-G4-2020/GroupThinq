package org.psu.edu.sweng.capstone.backend.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.User;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class BallotOptionDTOTest {

    private static final User USER = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
    private Ballot ballot = new Ballot(null, null, new Date(1337L));
    private BallotOption ballotOption = new BallotOption("BK Lounge", ballot, USER);
    private BallotOptionDTO testDTO;

    @BeforeEach
    void setUp() {
        ballot.setId(1L);
        ballotOption.setId(2L);
        ballotOption.setCreatedDate(new Date(333L));
        ballotOption.setUpdatedDate(new Date(444L));
        testDTO = BallotOptionDTO.build(ballotOption);
    }

    @Test
    void getters_workProperly() {
        assertNotNull(testDTO.getCreatedDate());
        assertEquals(1L, testDTO.getBallotId());
        assertEquals(2L, testDTO.getId());
        assertEquals("pop pop", testDTO.getUserName());
        assertEquals(new Date(333L), testDTO.getCreatedDate());
        assertEquals(new Date(444L), testDTO.getUpdatedDate());
    }

    @Test
    void setters_workProperly() {
        // when
        testDTO.setId(2L);
        testDTO.setBallotId(3L);
        testDTO.setCreatedDate(new Date(1111L));
        testDTO.setUpdatedDate(new Date(2222L));
        testDTO.setUserName("username");
        testDTO.setTitle("Mickey D's");

        // then
        assertEquals(2L, testDTO.getId());
        assertEquals(3L, testDTO.getBallotId());
        assertEquals(new Date(1111L), testDTO.getCreatedDate());
        assertEquals(new Date(2222L), testDTO.getUpdatedDate());
        assertEquals("username", testDTO.getUserName());
        assertEquals("Mickey D's", testDTO.getTitle());
    }

    @Test
    void build_handlesNullsProperly() {
        // given
        BallotOption testOption = new BallotOption(null, null, null);
        testOption.setCreatedDate(null);

        // when
        BallotOptionDTO testDTO = BallotOptionDTO.build(testOption);

        // then
        assertNull(testDTO.getCreatedDate());
        assertNull(testDTO.getUpdatedDate());
        assertNull(testDTO.getUserName());
        assertNull(testDTO.getId());
        assertNull(testDTO.getBallotId());
        assertNull(testDTO.getTitle());
    }

}
