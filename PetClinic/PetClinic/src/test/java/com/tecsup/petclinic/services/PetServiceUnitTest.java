package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.exception.PetNotFoundException;
import com.tecsup.petclinic.repositories.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class PetServiceUnitTest {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetService petService;

    @Test
    public void testFindById_PetFound() throws PetNotFoundException {
        Pet foundPet = petService.findById(1L);
        assertNotNull(foundPet);
        assertEquals("Leo", foundPet.getName());
        assertEquals(1, foundPet.getTypeId());
        assertEquals(1, foundPet.getOwnerId());
    }

    @Test
    public void testFindById_PetNotFound() {
        assertThrows(PetNotFoundException.class, () -> {
            petService.findById(999L);
        });
    }
}

