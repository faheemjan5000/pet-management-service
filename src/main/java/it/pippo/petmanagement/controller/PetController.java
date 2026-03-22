package it.pippo.petmanagement.controller;

import it.pippo.petmanagement.dto.PetRequestDto;
import it.pippo.petmanagement.dto.PetResponseDto;
import it.pippo.petmanagement.exceptions.PetNotFoundException;
import it.pippo.petmanagement.service.PetService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<PetResponseDto> createPet(@Valid @RequestBody PetRequestDto newPet)  {
        log.info("PetController.createPet() - creating new pet {}",newPet);
        return new ResponseEntity<>(petService.addPet(newPet),HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDto> getPetById(@PathVariable("id") Long petId) throws PetNotFoundException {
        log.info("PetController.getPetById() - getting Pet with ID = {}",petId);
        return new ResponseEntity<>(petService.getPetById(petId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PetResponseDto>> getAllPets() {
        log.info("PetController.getAllPets() - getting all pets");
        return new ResponseEntity<>(petService.getAllPets(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removePet(@PathVariable("id") Long petId) throws PetNotFoundException {
        log.info("PetController.removePet() -  removing pet by ID {}",petId);
        petService.deletePet(petId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDto> updatePet(@PathVariable("id") Long petId, @Valid @RequestBody PetRequestDto petDto) throws PetNotFoundException {
        log.info("Updating pet with id={} - data={}", petId, petDto);
        return new ResponseEntity<>(petService.updatePet(petId,petDto),HttpStatus.OK);
    }




}
