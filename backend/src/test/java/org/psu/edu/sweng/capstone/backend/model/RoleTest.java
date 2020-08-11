package org.psu.edu.sweng.capstone.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class RoleTest {

    private Role role = new Role();

    //null test
    @Test
    void defaultConstructor_worksProperly(){
        assertNull(role.getId());
        assertNull(role.getName());
    }

}
