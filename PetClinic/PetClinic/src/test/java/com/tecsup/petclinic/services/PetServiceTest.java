package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.exception.PetNotFoundException;
import com.tecsup.petclinic.repositories.PetRepository;
import com.tecsup.petclinic.services.PetService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class PetServiceTest {

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private PetService petService;


	@Test
	public void testFindById_PetFound() throws PetNotFoundException {

		Pet foundPet = petService.findById(1L);

		assertNotNull(foundPet);
		assertEquals("Lucky", foundPet.getName());
		assertEquals(1, foundPet.getTypeId());
		assertEquals(1, foundPet.getOwnerId());
	}
	@Test
	public void testFindPetByTypeId() {

		int TYPE_ID = 5;
		int SIZE_EXPECTED = 2;

		List<Pet> pets = this.petService.findByTypeId(TYPE_ID);

		assertEquals(SIZE_EXPECTED, pets.size());
	}

	@Test
	public void testFindById_PetNotFound() {
		assertThrows(PetNotFoundException.class, () -> {
			petService.findById(999L);
		});
	}

	@Test
	public void testFindByOwnerId() {

		Iterable<Pet> pets = petService.findByOwnerId(1);

		assertNotNull(pets);
		assertEquals(2, pets.spliterator().getExactSizeIfKnown());
	}
	@Test
	public void testFindPetByOwnerId() {

		int OWNER_ID = 10;
		int SIZE_EXPECTED = 2;

		List<Pet> pets = this.petService.findByOwnerId(OWNER_ID);

		assertEquals(SIZE_EXPECTED, pets.size());

	}
	@Test
	public void testFindPetById() {

		Integer ID = 1;
		String NAME = "Leo";
		Pet pet = null;

		try {
			pet = this.petService.findById(ID);
		} catch (PetNotFoundException e) {
			fail(e.getMessage());
		}

		log.info("" + pet);
		assertEquals(NAME, pet.getName());

	}
}
