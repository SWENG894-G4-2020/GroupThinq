package org.psu.edu.sweng.capstone.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DecisionTest {
	
	private Decision testDecision = new Decision();
	
	private DecisionUser testUser1 = new DecisionUser(testDecision, new User("jsmith", "fakepw", "Smith", "John", "jsmith@foo.bar", 
			new Date(1337L)));
	private DecisionUser testUser2 = new DecisionUser(testDecision, new User("mboyer", "fakepw", "Boyer", "Matt", "mboyer87@gmail.com", 
			new Date(1337L)));
	private DecisionUser testUser3 = new DecisionUser(testDecision, new User("testuser", "fakepw", "User", "Test", "testuser@dev.gov", 
			new Date(1337L)));
	
	private Ballot testBallot = new Ballot(testDecision, new Date(), new HashSet<>());
	
	private Set<DecisionUser> decisionUsers = new HashSet<>();
	private Set<Ballot> decisionBallots = new HashSet<>();

	private User testUser = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
	
	@BeforeEach
	void setUp() {
		decisionUsers.add(testUser1);
		decisionUsers.add(testUser2);
		decisionUsers.add(testUser3);
		
		decisionBallots.add(testBallot);
		
		testDecision.setId(1L);
		testDecision.setName("Test Decision");
		testDecision.setDescription("The Leetest Decision Description");
		testDecision.setOwnerId(testUser);
		testDecision.setCreatedDate(new Date(1111L));
		testDecision.setUpdatedDate(new Date(2222L));
		testDecision.setDecisionUsers(decisionUsers);
		testDecision.setBallots(decisionBallots);
	}
	
	@Test
	void constructor_worksProperly() {
		// given
		String name = "Leetest Decision";
		String description = "The description of this Decision";
		
		// when
		Decision newDecision = new Decision(name, description, testUser);
		
		// then
		assertEquals("Leetest Decision", newDecision.getName());
		assertEquals("The description of this Decision", description);
		assertEquals(testUser, newDecision.getOwnerId());
	}
	
	@Test
	void getters_workProperly() {
		assertEquals(1L, testDecision.getId());
		assertEquals("Test Decision", testDecision.getName());
		assertEquals("The Leetest Decision Description", testDecision.getDescription());
		assertEquals(testUser, testDecision.getOwnerId());
		assertEquals(new Date(1111L), testDecision.getCreatedDate());
		assertEquals(new Date(2222L), testDecision.getUpdatedDate());
		assertEquals(3, testDecision.getDecisionUsers().size());
		assertEquals(1, testDecision.getBallots().size());
	}
	
	@Test
	void setters_workProperly() {
		// when
		DecisionUser newUser = new DecisionUser(testDecision, new User("treyob", "fakepw", "Reyob", "Ttam", "TttamReyob@gmail.com", 
				new Date(1337L)));
		
		Ballot newBallot = new Ballot(testDecision, new Date(), new HashSet<>());
		
		decisionUsers.add(newUser);
		decisionBallots.add(newBallot);
		
		testDecision.setName("New Test Decision");
		testDecision.setOwnerId(testUser);
		testDecision.setDescription("New Test Description");
		testDecision.setCreatedDate(new Date(4444L));
		testDecision.setUpdatedDate(new Date(5555L));
		testDecision.setDecisionUsers(decisionUsers);
		testDecision.setBallots(decisionBallots);
		
		// then
		assertEquals("New Test Decision", testDecision.getName());
		assertEquals("New Test Description", testDecision.getDescription());
		assertEquals(testUser, testDecision.getOwnerId());
		assertEquals(new Date(4444L), testDecision.getCreatedDate());
		assertEquals(new Date(5555L), testDecision.getUpdatedDate());
		assertEquals(4, testDecision.getDecisionUsers().size());
		assertEquals(2, testDecision.getBallots().size());
	}
}
