package org.psu.edu.sweng.capstone.backend.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.User;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DecisionDTOTest {

    private DecisionDTO testDecision;
    private User testUser = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));


    @BeforeEach
    void setUp() {
        Decision decision = new Decision("why is gamora?", "what is gamora?", testUser);

        decision.setId(8675309L);
        decision.setCreatedDate(new Date(1101L));
        decision.setUpdatedDate(new Date(1011L));
        
        DecisionUser du = new DecisionUser(decision, new User("tuser", "pw", "User", "Test", "testuser@foo.bar", new Date(1337L)));
        decision.getDecisionUsers().add(du);
        
        testDecision = DecisionDTO.build(decision);
    }

    @Test
    void getters_worksProperly() {
        assertEquals(8675309L, testDecision.getId());
        assertEquals("why is gamora?", testDecision.getName());
        assertEquals("what is gamora?", testDecision.getDescription());
        assertEquals(new Date(1101L), testDecision.getCreatedDate());
        assertEquals(new Date(1011L), testDecision.getUpdatedDate());
        assertEquals(testUser.getUserName(), testDecision.getOwnerUsername());
    }

    @Test
    void setters_worksProperly() {
        // when
        testDecision.setId(8675309L);
        testDecision.setName("why is gamora?");
        testDecision.setDescription("testdescription");
        testDecision.setCreatedDate(new Date(8008L));
        testDecision.setUpdatedDate(new Date(8383L));
        testDecision.setOwnerUsername(testUser.getUserName());

        // then
        assertEquals(8675309L, testDecision.getId());
        assertEquals("why is gamora?", testDecision.getName());
        assertEquals("testdescription", testDecision.getDescription());
        assertEquals(new Date(8008L), testDecision.getCreatedDate());
        assertEquals(new Date(8383L), testDecision.getUpdatedDate());
        assertEquals(testUser.getUserName(), testDecision.getOwnerUsername());
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
    	assertEquals(1, testDecision.getIncludedUsers().size());
    }
}
