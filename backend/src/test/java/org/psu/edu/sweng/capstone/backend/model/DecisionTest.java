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
	
	private Set<DecisionUser> decisionUsers = new HashSet<>();

	private User testUser = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
	
	@BeforeEach
	void setUp() {
		decisionUsers.add(testUser1);
		decisionUsers.add(testUser2);
		decisionUsers.add(testUser3);
		
		testDecision.setName("Test Decision");
		testDecision.setDecisionUsers(decisionUsers);
		testDecision.setOwnerId(testUser);
	}
	
	@Test
	void constructor_worksProperly() {
		// given
		String name = "Leetest Decision";
		String description = "The description of this Decision";
		Date expiration = new Date();
		
		// when
		Decision newDecision = new Decision(name, description, expiration, testUser);
		
		// then
		assertEquals("Leetest Decision", newDecision.getName());
		assertEquals(testUser, newDecision.getOwnerId());
	}
	
	@Test
	void getters_workProperly() {
		assertEquals("Test Decision", testDecision.getName());
		assertEquals(3, testDecision.getDecisionUsers().size());
		assertEquals(testUser, testDecision.getOwnerId());
	}
	
	@Test
	void setters_workProperly() {
		// when
		DecisionUser newUser = new DecisionUser(testDecision, new User("treyob", "fakepw", "Reyob", "Ttam", "TttamReyob@gmail.com", 
				new Date(1337L)));
		decisionUsers.add(newUser);
		
		testDecision.setName("New Test Decision");	
		testDecision.setDecisionUsers(decisionUsers);
		testDecision.setOwnerId(testUser);
		
		// then
		assertEquals("New Test Decision", testDecision.getName());
		assertEquals(4, testDecision.getDecisionUsers().size());
		assertEquals(testUser, testDecision.getOwnerId());
	}
}
