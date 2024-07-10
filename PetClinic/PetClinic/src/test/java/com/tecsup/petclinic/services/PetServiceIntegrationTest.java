package com.tecsup.petclinic.services;


import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.exception.PetNotFoundException;
import com.tecsup.petclinic.repositories.PetRepository;
import com.tecsup.petclinic.util.TObjectCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PetServiceIntegrationTest {

    private PetService petService;

    @Mock //Bean
    private PetRepository repository;

    @BeforeEach
    void setUp() {
        this.petService = new PetServiceImpl(this.repository);
    }

    @Test
    public void testCreatePet() {

        String PET_NAME = "Ponky";
        int OWNER_ID = 1;
        int TYPE_ID = 1;

        Pet newPet = TObjectCreator.newPet();
        Pet newCreatePet = TObjectCreator.newPetCreated();

        Mockito.when(this.repository.save(newPet))
                .thenReturn(newCreatePet);

        Pet petCreated = this.petService.create(newPet);

        log.info("Pet created : {}" , petCreated);

        assertNotNull(petCreated.getId());
        assertEquals(newCreatePet.getName(), petCreated.getName());
        assertEquals(newCreatePet.getOwnerId(), petCreated.getOwnerId());
        assertEquals(newCreatePet.getTypeId(), petCreated.getTypeId());

    }


    /**
     *
     */
    @Test
    public void testUpdatePet() {

        String UP_PET_NAME = "Bear2";
        int UP_OWNER_ID = 2;
        int UP_TYPE_ID = 2;

        Pet newPet = TObjectCreator.newPetForUpdate();
        Pet newPetCreate = TObjectCreator.newPetCreatedForUpdate();

        // ------------ Create ---------------

        Mockito.when(this.repository.save(newPet))
                .thenReturn(newPetCreate);

        Pet petCreated = this.petService.create(newPet);
        log.info("{}" , petCreated);

        // ------------ Update ---------------

        // Prepare data for update
        petCreated.setName(UP_PET_NAME);
        petCreated.setOwnerId(UP_OWNER_ID);
        petCreated.setTypeId(UP_TYPE_ID);

        // Create
        Mockito.when(this.repository.save(petCreated))
                .thenReturn(petCreated);

        // Execute update
        Pet upgradePet = this.petService.update(petCreated);
        log.info("{}" + upgradePet);

        //            EXPECTED           ACTUAL
        assertEquals(UP_PET_NAME, upgradePet.getName());
        assertEquals(UP_OWNER_ID, upgradePet.getTypeId());
        assertEquals(UP_TYPE_ID, upgradePet.getOwnerId());
    }

    /**
     *
     */
    @Test
    public void testDeletePet() {

        Pet newPet = TObjectCreator.newPetForDelete();
        Pet newPetCreate = TObjectCreator.newPetCreatedForDelete();

        // ------------ Create ---------------

        Mockito.when(this.repository.save(newPet))
                .thenReturn(newPetCreate);

        Pet petCreated = this.petService.create(newPet);
        log.info("{}" ,petCreated);

        // ------------ Delete ---------------

        Mockito.doNothing().when(this.repository).delete(newPetCreate);
        Mockito.when(this.repository.findById(Long.valueOf(newPetCreate.getId())))
                .thenReturn(Optional.of(newPetCreate));

        try {
            this.petService.delete(Long.valueOf(petCreated.getId()));
        } catch (PetNotFoundException e) {
            fail(e.getMessage());
        }

        // ------------ Validate ---------------

        Mockito.when(this.repository.findById(Long.valueOf(newPetCreate.getId())))
                .thenReturn(Optional.ofNullable(null));

        try {
            this.petService.findById(petCreated.getId());
            assertTrue(false);
        } catch (PetNotFoundException e) {
            assertTrue(true);
        }

    }

}