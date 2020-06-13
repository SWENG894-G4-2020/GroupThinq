package org.psu.edu.sweng.capstone.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DecisionTest {
	
	private Decision testDecision = new Decision();
	
	private DecisionUser testUser1 = new DecisionUser(testDecision, 
			new User("jsmith", "fakepw", "Smith", "John", "jsmith@foo.bar", new Date(1337L)));
	
	private DecisionUser testUser2 = new DecisionUser(testDecision, 
			new User("mboyer", "fakepw", "Boyer", "Matt", "mboyer87@gmail.com", new Date(1337L)));
	
	private DecisionUser testUser3 = new DecisionUser(testDecision, 
			new User("testuser", "fakepw", "User", "Test", "testuser@dev.gov", new Date(1337L)));
	
	private Set<DecisionUser> decisionUsers = new HashSet<>();
	
	@BeforeEach
	void setUp() {
		decisionUsers.add(testUser1);
		decisionUsers.add(testUser2);
		decisionUsers.add(testUser3);
		
		testDecision.setName("Test Decision");
		testDecision.setDecisionUsers(decisionUsers);
		testDecision.setOwnerId(new User("treyob", "fakepw", "Reyob", "Ttam", "TttamReyob@gmail.com", new Date(1337L)));
		testDecision.setCreatedDate(new Date(1337L));
		testDecision.setExpirationDate(new Date(7859L));
	}
	
	@Test
	void constructor_worksProperly() {
		// given
		Long id = 1337L;
		String name = "Leetest Decision";
		User owner = new User("mboyer", "fakepw", "Boyer", "Matthew", "mboyer87@gmail.com", new Date());
		Date expirationDate = new Date(11111L);
		
		// when
		Decision newDecision = new Decision(id, name, owner, expirationDate);
		
		// then
		assertEquals(1337L, newDecision.getId());
		assertEquals("Leetest Decision", newDecision.getName());
		assertEquals("mboyer", newDecision.getOwnerId().getUserName());
		assertEquals(new Date(11111L), newDecision.getExpirationDate());
	}
	
	@Test
	void getters_workProperly() {
		assertEquals("Test Decision", testDecision.getName());
		assertEquals(3, testDecision.getDecisionUsers().size());
		assertEquals("treyob", testDecision.getOwnerId().getUserName());
		assertEquals(new Date(1337L), testDecision.getCreatedDate());
		assertEquals(new Date(7859L), testDecision.getExpirationDate());
	}
	
	@Test
	void setters_workProperly() {
		// when
		DecisionUser newUser = new DecisionUser(testDecision, 
				new User("treyob", "fakepw", "Reyob", "Ttam", "TttamReyob@gmail.com", new Date(1337L)));
		
		decisionUsers.add(newUser);
		
		testDecision.setName("New Test Decision");	
		testDecision.setDecisionUsers(decisionUsers);
		testDecision.setOwnerId(new User("mboyer87", "fakepw", "Reyob", "Ttam", "TttamReyob@gmail.com", new Date(1337L)));
		testDecision.setCreatedDate(new Date(2222L));
		testDecision.setExpirationDate(new Date(3333L));		
		
		// then
		assertEquals("New Test Decision", testDecision.getName());
		assertEquals(4, testDecision.getDecisionUsers().size());
		assertEquals("mboyer87", testDecision.getOwnerId().getUserName());
		assertEquals(new Date(2222L), testDecision.getCreatedDate());
		assertEquals(new Date(3333L), testDecision.getExpirationDate());
	}
}
