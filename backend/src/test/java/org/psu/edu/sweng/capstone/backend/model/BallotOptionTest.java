package org.psu.edu.sweng.capstone.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BallotOptionTest {

    private static final User USER = new User("mboyer87", "password", "Boyer", "Matt", "mboyer87@gmail.com", new Date());
    private Ballot ballot = new Ballot();
    private BallotOption ballotOption = new BallotOption("BK Lounge", "get those cheeze borger", ballot, USER);

    @BeforeEach
    void setUp() {
        ballotOption.setUpdatedDate(new Date(2222L));
    }

    @Test
    void constructor_worksProperly() {

        assertNotNull(ballotOption.getCreatedDate());
        assertEquals("BK Lounge", ballotOption.getTitle());
        assertEquals("get those cheeze borger", ballotOption.getDescription());
        assertEquals(new Date(2222L), ballotOption.getUpdatedDate());
        assertEquals(ballot, ballotOption.getBallot());
        assertEquals(USER, ballotOption.getUser());
    }

    @Test
    void getters_setters_workProperly() {
        User USER = new User("zzzzzach", "dorwssap", "Beimford", "Zach", "email@fake.com", new Date());
        Ballot ballot = new Ballot();


        ballotOption.setId(2L);
        ballotOption.setTitle("subway");
        ballotOption.setDescription("it's a sandwhich");
        ballotOption.setCreatedDate(new Date(3333L));
        ballotOption.setUpdatedDate(new Date(4444L));
        ballotOption.setUser(USER);
        ballotOption.setBallot(ballot);

        assertEquals(2L, ballotOption.getId());
        assertEquals("subway", ballotOption.getTitle());
        assertEquals("it's a sandwhich", ballotOption.getDescription());
        assertEquals(new Date(3333L), ballotOption.getCreatedDate());
        assertEquals(new Date(4444L), ballotOption.getUpdatedDate());
        assertEquals(ballot, ballotOption.getBallot());
        assertEquals(USER, ballotOption.getUser());
    }

    @Test
    void defaultConstructor_worksProperly(){
        BallotOption ballotOption = new BallotOption();

        assertNull(ballotOption.getId());
        assertNull(ballotOption.getBallot());
        assertNull(ballotOption.getTitle());
        assertNull(ballotOption.getDescription());
        assertNull(ballotOption.getUpdatedDate());
        assertNull(ballotOption.getUser());
    }

}
