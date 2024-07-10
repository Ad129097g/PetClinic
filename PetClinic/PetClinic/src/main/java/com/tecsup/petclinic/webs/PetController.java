package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.domain.PetTO;
import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.exception.PetNotFoundException;
import com.tecsup.petclinic.services.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

	@Autowired
	private PetService petService;

	private final ObjectMapper mapper = new ObjectMapper();

	@GetMapping
	public ResponseEntity<List<Pet>> getAllPets() {
		List<Pet> pets = petService.getAllPets();
		return ResponseEntity.ok().body(pets);
	}

	@PostMapping
	public ResponseEntity<Pet> create(@RequestBody Pet pet) {
		Pet createdPet = petService.create(pet);
		return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pet> getPetById(@PathVariable("id") Long id) {
		Pet pet = petService.getPetById(id);
		if (pet != null) {
			return ResponseEntity.ok().body(pet);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePet(@PathVariable("id") Long id) {
		if (petService.deletePet(id)) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	//Crear -> prueba de integracion
	@PostMapping(value = "/pets")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<PetTO> createPet(@RequestBody PetTO petTO) {
		Pet newPet = this.mapper.convertValue(petTO, Pet.class);
		PetTO newPetTO = this.mapper.convertValue(petService.create(newPet), PetTO.class);

		return ResponseEntity.status(HttpStatus.CREATED).body(newPetTO);
	}
	//Elimnar -> prueba de integracion

	/**
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/pets/{id}")
	ResponseEntity<String> delete(@PathVariable Integer id){
		try{
			petService.delete(Long.valueOf(id));
			return ResponseEntity.ok("Delete ID:" +  id);
		}catch (PetNotFoundException e){
			return ResponseEntity.notFound().build();
		}
	}
}
