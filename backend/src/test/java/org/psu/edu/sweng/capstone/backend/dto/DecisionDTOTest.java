package org.psu.edu.sweng.capstone.backend.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.model.Decision;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DecisionDTOTest {


    private DecisionDTO testDecision;

    @BeforeEach
    void setUp() {
        Decision decision = new Decision(8675309L, "why is gamora?", new Date(1124L));

        decision.setCreatedDate(new Date(1101L));
        decision.setUpdatedDate(new Date(1011L));

        testDecision = DecisionDTO.buildDTO(decision);
    }

    @Test
    void getters_worksProperly() {
        assertEquals(8675309L, testDecision.getId());
        assertEquals("why is gamora?", testDecision.getDecisionName());
        assertEquals(new Date(1124L), testDecision.getExpirationDate());
        assertEquals(new Date(1101L), testDecision.getCreatedDate());
        assertEquals(new Date(1011L), testDecision.getUpdatedDate());
    }

    @Test
    void setters_worksProperly() {
        // when
        testDecision.setId(8675309L);
        testDecision.setDecisionName("why is gamora?");
        testDecision.setExpirationDate(new Date(9000L));
        testDecision.setCreatedDate(new Date(8008L));
        testDecision.setUpdatedDate(new Date(8383L));

        // then
        assertEquals(8675309L, testDecision.getId());
        assertEquals("why is gamora?", testDecision.getDecisionName());
        assertEquals(new Date(9000L), testDecision.getExpirationDate());
        assertEquals(new Date(8008L), testDecision.getCreatedDate());
        assertEquals(new Date(8383L), testDecision.getUpdatedDate());
    }

    @Test
    void buildDTO_returnsNullValues() {
        // given
        Decision decision = new Decision(null, null, null);

        // when
        DecisionDTO dto = DecisionDTO.buildDTO(decision);

        // then
        assertNull(dto.getId());
        assertNull(dto.getDecisionName());
        assertNull(dto.getExpirationDate());
        assertNull(dto.getCreatedDate());
        assertNull(dto.getUpdatedDate());
    }

}
