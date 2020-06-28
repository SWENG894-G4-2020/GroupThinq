package org.psu.edu.sweng.capstone.backend.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DecisionDTOTest {

    private DecisionDTO testDecisionDTO;
    private User testUser = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));


    @BeforeEach
    void setUp() {
        Decision decision = new Decision("why is gamora?", "what is gamora?", testUser);

        decision.setId(8675309L);
        decision.setCreatedDate(new Date(1101L));
        decision.setUpdatedDate(new Date(1011L));
        
        DecisionUser du = new DecisionUser(decision, new User("tuser", "pw", "User", "Test", "testuser@foo.bar", new Date(1337L)));
        decision.getDecisionUsers().add(du);
        
        Ballot ballot = new Ballot(decision, new Date());
        decision.getBallots().add(ballot);
        
        testDecisionDTO = DecisionDTO.build(decision);
    }

    @Test
    void getters_worksProperly() {
        assertEquals(8675309L, testDecisionDTO.getId());
        assertEquals("why is gamora?", testDecisionDTO.getName());
        assertEquals("what is gamora?", testDecisionDTO.getDescription());
        assertEquals(new Date(1101L), testDecisionDTO.getCreatedDate());
        assertEquals(new Date(1011L), testDecisionDTO.getUpdatedDate());
        assertEquals(testUser.getUserName(), testDecisionDTO.getOwnerUsername());
        assertEquals(1, testDecisionDTO.getBallots().size());
    }

    @Test
    void setters_worksProperly() {
    	// given
        Decision decision = new Decision("why is gamora?", "what is gamora?", testUser);

    	List<UserDTO> includedUsers = new ArrayList<>();
    	UserDTO userDto = UserDTO.build(testUser);
    	includedUsers.add(userDto);
    	
    	List<BallotDTO> ballots = new ArrayList<>();
    	BallotDTO ballotDTO = BallotDTO.build(new Ballot(decision, new Date()));
    	ballots.add(ballotDTO);
    	
        // when
        testDecisionDTO.setId(8675309L);
        testDecisionDTO.setName("why is gamora?");
        testDecisionDTO.setDescription("testdescription");
        testDecisionDTO.setCreatedDate(new Date(8008L));
        testDecisionDTO.setUpdatedDate(new Date(8383L));
        testDecisionDTO.setOwnerUsername(testUser.getUserName());
        testDecisionDTO.setIncludedUsers(includedUsers);
        testDecisionDTO.setBallots(ballots);

        // then
        assertEquals(8675309L, testDecisionDTO.getId());
        assertEquals("why is gamora?", testDecisionDTO.getName());
        assertEquals("testdescription", testDecisionDTO.getDescription());
        assertEquals(new Date(8008L), testDecisionDTO.getCreatedDate());
        assertEquals(new Date(8383L), testDecisionDTO.getUpdatedDate());
        assertEquals(testUser.getUserName(), testDecisionDTO.getOwnerUsername());
        assertEquals(1, testDecisionDTO.getIncludedUsers().size());
        assertEquals(1, testDecisionDTO.getBallots().size());
    }

    @Test
    void buildDTO_returnsNullValues() {
        // given
        Decision decision = new Decision(null, null, null);
        decision.setCreatedDate(null);

        // when
        DecisionDTO dto = DecisionDTO.build(decision);

        // then
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getCreatedDate());
        assertNull(dto.getUpdatedDate());
        assertNull(dto.getOwnerUsername());
    }
    
    @Test
    void buildDTO_returnsDecisionUsers() {
    	assertEquals(1, testDecisionDTO.getIncludedUsers().size());
    }
}
