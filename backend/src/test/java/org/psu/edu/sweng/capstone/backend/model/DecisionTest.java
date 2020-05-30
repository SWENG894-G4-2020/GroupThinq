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
			new Date(1337L), new Date()));
	private DecisionUser testUser2 = new DecisionUser(testDecision, new User("mboyer", "fakepw", "Boyer", "Matt", "mboyer87@gmail.com", 
			new Date(1337L), new Date()));
	private DecisionUser testUser3 = new DecisionUser(testDecision, new User("testuser", "fakepw", "User", "Test", "testuser@dev.gov", 
			new Date(1337L), new Date()));
	
	private Set<DecisionUser> decisionUsers = new HashSet<>();
	
	@BeforeEach
	void setUp() {
		decisionUsers.add(testUser1);
		decisionUsers.add(testUser2);
		decisionUsers.add(testUser3);
		
		testDecision.setName("Test Decision");
		testDecision.setDecisionUsers(decisionUsers);
	}
	
	@Test
	void constructor_worksProperly() {
		// given
		Long id = 1337L;
		String name = "Leetest Decision";
		
		// when
		Decision newDecision = new Decision(id, name);
		
		// then
		assertEquals(1337L, newDecision.getId());
		assertEquals("Leetest Decision", newDecision.getName());
	}
	
	@Test
	void getters_workProperly() {
		assertEquals("Test Decision", testDecision.getName());
		assertEquals(3, testDecision.getDecisionUsers().size());
	}
	
	@Test
	void setters_workProperly() {
		// when
		DecisionUser newUser = new DecisionUser(testDecision, new User("treyob", "fakepw", "Reyob", "Ttam", "TttamReyob@gmail.com", 
				new Date(1337L), new Date()));
		decisionUsers.add(newUser);
		
		testDecision.setName("New Test Decision");	
		testDecision.setDecisionUsers(decisionUsers);
		
		// then
		assertEquals("New Test Decision", testDecision.getName());
		assertEquals(4, testDecision.getDecisionUsers().size());
	}
}
