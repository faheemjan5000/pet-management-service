package it.pippo.petmanagement.service;

import it.pippo.petmanagement.dto.PetRequestDto;
import it.pippo.petmanagement.dto.PetResponseDto;
import it.pippo.petmanagement.exceptions.PetNotFoundException;
import it.pippo.petmanagement.mapper.PetMapper;
import it.pippo.petmanagement.model.Pet;
import it.pippo.petmanagement.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    private PetMapper petMapper = new PetMapper();

    private PetService petService;

    @BeforeEach
    void setUp() {
        petService = new PetService(petRepository, petMapper);
    }

    @Test
    void addPet_shouldSaveCorrectData() {
        PetRequestDto request = new PetRequestDto("Fido", "Dog", 3, "Alice");

        when(petRepository.save(any(Pet.class)))
                .thenAnswer(invocation -> {
                    Pet pet = invocation.getArgument(0);
                    pet.setId(1L);
                    return pet;
                });

        PetResponseDto result = petService.addPet(request);

        assertEquals(1L, result.getId());
        assertEquals("Fido", result.getName());
        assertEquals("Dog", result.getSpecies());
        assertEquals(3, result.getAge());
        assertEquals("Alice", result.getOwnerName());

        verify(petRepository).save(argThat(pet ->
                pet.getName().equals("Fido") &&
                        pet.getSpecies().equals("Dog") &&
                        pet.getAge() == 3 &&
                        pet.getOwnerName().equals("Alice")
        ));
    }

    @Test
    void updatePet_shouldUpdateExistingPet() throws PetNotFoundException {
        Long petId = 1L;

        Pet existingPet = new Pet();
        existingPet.setId(petId);
        existingPet.setName("Old");
        existingPet.setSpecies("Dog");
        existingPet.setAge(2);
        existingPet.setOwnerName("Alice");

        PetRequestDto updateRequest = new PetRequestDto("Leo", "Cat", 5, "Bob");

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
        when(petRepository.save(any(Pet.class))).thenAnswer(i -> i.getArgument(0));

        PetResponseDto result = petService.updatePet(petId, updateRequest);

        assertEquals("Leo", result.getName());
        assertEquals("Cat", result.getSpecies());
        assertEquals(5, result.getAge());
        assertEquals("Bob", result.getOwnerName());

        verify(petRepository).findById(petId);
        verify(petRepository).save(existingPet);
    }

    @Test
    void updatePet_shouldThrowWhenNotFound() {
        Long petId = 999L;
        PetRequestDto updateRequest = new PetRequestDto("Leo", "Cat", 5, "Bob");

        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        PetNotFoundException ex = assertThrows(
                PetNotFoundException.class,
                () -> petService.updatePet(petId, updateRequest)
        );

        assertTrue(ex.getMessage().contains("Pet with ID"));
        verify(petRepository).findById(petId);
        verify(petRepository, never()).save(any());
    }

    @Test
    void getPetById_shouldReturnPet() throws PetNotFoundException {
        Long petId = 10L;

        Pet pet = new Pet();
        pet.setId(petId);
        pet.setName("Milo");
        pet.setSpecies("Cat");
        pet.setAge(4);
        pet.setOwnerName("Eve");

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));

        PetResponseDto result = petService.getPetById(petId);

        assertEquals(petId, result.getId());
        assertEquals("Milo", result.getName());

        verify(petRepository).findById(petId);
    }

    @Test
    void getPetById_shouldThrowWhenNotFound() {
        Long petId = 111L;

        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class,
                () -> petService.getPetById(petId));

        verify(petRepository).findById(petId);
    }

    @Test
    void getAllPets_shouldReturnList() {
        Pet p1 = new Pet();
        p1.setId(1L);
        p1.setName("A");

        Pet p2 = new Pet();
        p2.setId(2L);
        p2.setName("B");

        when(petRepository.findAll()).thenReturn(List.of(p1, p2));

        List<PetResponseDto> result = petService.getAllPets();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getName());
        assertEquals("B", result.get(1).getName());

        verify(petRepository).findAll();
    }

    @Test
    void deletePet_shouldDeleteWhenExists() throws PetNotFoundException {
        Long petId = 5L;

        Pet pet = new Pet();
        pet.setId(petId);

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));

        petService.deletePet(petId);

        verify(petRepository).findById(petId);
        verify(petRepository).deleteById(petId);
    }

    @Test
    void deletePet_shouldThrowWhenNotFound() {
        Long petId = 404L;

        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class,
                () -> petService.deletePet(petId));

        verify(petRepository).findById(petId);
        verify(petRepository, never()).deleteById(any());
    }
}