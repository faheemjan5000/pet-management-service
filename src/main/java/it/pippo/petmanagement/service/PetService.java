package it.pippo.petmanagement.service;

import it.pippo.petmanagement.dto.PetRequestDto;
import it.pippo.petmanagement.dto.PetResponseDto;
import it.pippo.petmanagement.model.Pet;
import it.pippo.petmanagement.exceptions.PetNotFoundException;
import it.pippo.petmanagement.mapper.PetMapper;
import it.pippo.petmanagement.repository.PetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    public PetService(PetRepository petRepository,PetMapper petMapper) {

        this.petRepository = petRepository;
        this.petMapper= petMapper;
    }

    public PetResponseDto addPet(PetRequestDto petDto)  {
        log.info("PetService.savePet() - Saving pet: {}", petDto);
        Pet petSaved = petRepository.save(petMapper.petDtoRequestToEntity(petDto));
        log.info("Pet Saved successfully - {}",petSaved);
        return petMapper.entityToPetResponseDto(petSaved);
    }

    public PetResponseDto updatePet(Long petId , PetRequestDto petDto) throws PetNotFoundException {
        log.info("PetService.updatePet() - Updating pet with ID : {}",petId);
        Pet existingPet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet with ID " + petId + " not found"));

        log.info("Pet to be updated found : {} ",existingPet);
        petMapper.updateEntityFromRequestDto(existingPet,petDto);
        Pet petSaved = petRepository.save(existingPet);
        log.info("pet updated successfully : {}",petSaved);
        return petMapper.entityToPetResponseDto(petSaved);
    }

    public PetResponseDto getPetById(Long petId) throws PetNotFoundException {
        log.info("PetService.getPetById() - retrieving pet by ID : {}",petId);
        Pet existingPet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet with ID=" + petId + " not found"));
        log.info("pet retrieved - {}",existingPet);
        return petMapper.entityToPetResponseDto(existingPet);
    }

    public List<PetResponseDto> getAllPets(){
        log.info("PetService.getAllPets() - Retrieving all pets");
        List<Pet> allPetsRetrieved = petRepository.findAll();
        log.info("Total pets retrieved: {}", allPetsRetrieved.size());
        return allPetsRetrieved.stream()
                .map(petMapper::entityToPetResponseDto)
                .toList();
    }

    public void deletePet(Long petId) throws PetNotFoundException {
        log.info("PetService.deletePet() - deleting pet with ID={}",petId);
        Pet petRetrieved = petRepository.findById(petId)
                .orElseThrow(() -> {
                    log.warn("Pet with id {} not found -> deletion failed!", petId);
                    return new PetNotFoundException("Pet with ID="+petId+" not found ");
                });
        log.info("Pet to be deleted - {} ",petRetrieved);
        petRepository.deleteById(petId);
        log.info("pet deleted successfully!");
    }
}
