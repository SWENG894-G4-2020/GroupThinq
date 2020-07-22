package org.psu.edu.sweng.capstone.backend.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

	private static final User USER = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
	private Ballot ballot = new Ballot(null, null, new Date(1337L));
	private BallotOption ballotOption = new BallotOption("BK Lounge", ballot, USER);
	private BallotResult ballotResult = new BallotResult(ballot, ballotOption, USER);
	private Decision decision = new Decision("why is gamora?", USER);
	private DecisionUser decisionUser = new DecisionUser(decision, USER);

	private User testUser = new User();
	private HashSet<UserRole> userRoles = new HashSet<>();
	private Set<BallotResult> ballotResults = new HashSet<>();
	private Set<DecisionUser> decisionUsers = new HashSet<>();
	
	@BeforeEach
	void setup() {
		UserRole userRole = new UserRole(testUser, new Role());
		userRoles.add(userRole);
		ballotResults.add(ballotResult);
		decisionUsers.add(decisionUser);

		testUser.setDecisions(decisionUsers);
		testUser.setVotes(ballotResults);
		testUser.setRoles(userRoles);
		testUser.setUserName("testUser");
		testUser.setPassword("testPassword");
		testUser.setLastName("user");
		testUser.setFirstName("test");
		testUser.setEmailAddress("testUser@foo.bar");
		testUser.setBirthDate(new Date(1337L));
		testUser.setCreatedDate(new Date(1L));
		testUser.setUpdatedDate(new Date(2L));
		testUser.setLastLoggedIn(new Date(3L));
		testUser.setId(1L);
	}
	
	@Test
	void constructor_worksProperly() {
		// given
		String username = "mwboyer";
		String password = "fakepw";
		String lastName = "Boyer";
		String firstName = "Matt";
		String emailAddress = "mboyer87@gmail.com";
		Date birthDate = new Date(1337);
		
		// when
		User newUser = new User(username, password, lastName, firstName, emailAddress, birthDate);
		
		// then
		assertEquals("mwboyer", newUser.getUserName());
		assertEquals("fakepw", newUser.getPassword());
		assertEquals("Boyer", newUser.getLastName());
		assertEquals("Matt", newUser.getFirstName());
		assertEquals("mboyer87@gmail.com", newUser.getEmailAddress());
		assertEquals(birthDate, newUser.getBirthDate());
	}

	//null test
	@Test
	void defaultConstructor_worksProperly(){
		User user = new User();

		assertNull(user.getUserName());
		assertNull(null, user.getPassword());
		assertNull(user.getLastName());
		assertNull(user.getFirstName());
		assertNull(user.getEmailAddress());
		assertNull(user.getBirthDate());
		assertNull(user.getCreatedDate());
		assertNull(user.getUpdatedDate());
		assertNull(user.getLastLoggedIn());
		assertEquals(0, user.getRoles().size());
		assertEquals(0, user.getDecisions().size());
		assertEquals(0, user.getVotes().size());
	}

	@Test
	void getters_worksProperly() {
		assertEquals("testUser", testUser.getUserName());
		assertEquals("testPassword", testUser.getPassword());
		assertEquals("user", testUser.getLastName());
		assertEquals("test", testUser.getFirstName());
		assertEquals("testUser@foo.bar", testUser.getEmailAddress());
		assertEquals(new Date(1337L), testUser.getBirthDate());
		assertEquals(new Date(1L), testUser.getCreatedDate());
		assertEquals(new Date(2L), testUser.getUpdatedDate());
		assertEquals(new Date(3L), testUser.getLastLoggedIn());
		assertEquals(1L, testUser.getId());
		assertEquals(1, testUser.getRoles().size());
		assertEquals(1, testUser.getDecisions().size());
		assertEquals(1, testUser.getVotes().size());
	}
	
	@Test
	void setters_worksProperly() {
		// when
		UserRole newRole = new UserRole(testUser, new Role());
		testUser.getRoles().add(newRole);
		
		testUser.setUserName("mwboyer");
		testUser.setPassword("newpassword");
		testUser.setLastName("Boyer");
		testUser.setFirstName("Matt");
		testUser.setEmailAddress("mboyer87@gmail.com");
		testUser.setBirthDate(new Date(7859L));
		testUser.setCreatedDate(new Date(9587L));
		testUser.setUpdatedDate(new Date(1111L));
		testUser.setLastLoggedIn(new Date(2222L));
		
		// then
		assertEquals("Boyer", testUser.getLastName());
		assertEquals("Matt", testUser.getFirstName());
		assertEquals("mwboyer", testUser.getUserName());
		assertEquals("mboyer87@gmail.com", testUser.getEmailAddress());
		assertEquals(new Date(7859L), testUser.getBirthDate());
		assertEquals(new Date(9587L), testUser.getCreatedDate());
		assertEquals(new Date(1111L), testUser.getUpdatedDate());
		assertEquals(new Date(2222L), testUser.getLastLoggedIn());
		assertEquals(2, testUser.getRoles().size());
	}
	
	@Test
	void getFullname_returnsFullName() {
		assertEquals("test user", testUser.getFullName());
	}
}
