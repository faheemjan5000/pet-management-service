package it.pippo.petmanagement.service;

import it.pippo.petmanagement.dto.PetRequestDto;
import it.pippo.petmanagement.dto.PetResponseDto;
import it.pippo.petmanagement.exceptions.PetNotFoundException;
import it.pippo.petmanagement.mapper.PetMapper;
import it.pippo.petmanagement.model.Pet;
import it.pippo.petmanagement.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetMapper petMapper;

    @InjectMocks
    private PetService petService;

    @Test
    void addPet_shouldSaveAndReturnResponseDto() {
        PetRequestDto request = new PetRequestDto("Fido", "Dog", 3, "Alice");
        Pet mappedPet = Pet.builder()
                .name("Fido")
                .species("Dog")
                .age(3)
                .ownerName("Alice")
                .build();
        Pet savedPet = Pet.builder()
                .id(1L)
                .name("Fido")
                .species("Dog")
                .age(3)
                .ownerName("Alice")
                .build();
        PetResponseDto mappedResponse = new PetResponseDto(1L, "Fido", "Dog", 3, "Alice");

        when(petMapper.petDtoRequestToEntity(request)).thenReturn(mappedPet);
        when(petRepository.save(mappedPet)).thenReturn(savedPet);
        when(petMapper.entityToPetResponseDto(savedPet)).thenReturn(mappedResponse);

        PetResponseDto result = petService.addPet(request);

        assertEquals(1L, result.getId());
        assertEquals("Fido", result.getName());
        assertEquals("Dog", result.getSpecies());
        assertEquals(3, result.getAge());
        assertEquals("Alice", result.getOwnerName());
        verify(petMapper).petDtoRequestToEntity(request);
        verify(petRepository, times(1)).save(mappedPet);
        verify(petMapper).entityToPetResponseDto(savedPet);
    }

    @Test
    void updatePet_shouldUpdateExistingPetAndReturnResponseDto() throws PetNotFoundException {
        Long petId = 1L;
        PetRequestDto updateRequest = new PetRequestDto("Leo", "Cat", 5, "Bob");
        Pet existingPet = Pet.builder()
                .id(petId)
                .name("OldName")
                .species("Dog")
                .age(2)
                .ownerName("Alice")
                .build();

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
        doAnswer(invocation -> {
            Pet target = invocation.getArgument(0);
            PetRequestDto dto = invocation.getArgument(1);
            target.setName(dto.getName());
            target.setSpecies(dto.getSpecies());
            target.setAge(dto.getAge());
            target.setOwnerName(dto.getOwnerName());
            return null;
        }).when(petMapper).updateEntityFromRequestDto(same(existingPet), same(updateRequest));
        when(petRepository.save(existingPet)).thenReturn(existingPet);
        when(petMapper.entityToPetResponseDto(existingPet))
                .thenReturn(new PetResponseDto(petId, "Leo", "Cat", 5, "Bob"));

        PetResponseDto result = petService.updatePet(petId, updateRequest);

        assertEquals(petId, result.getId());
        assertEquals("Leo", result.getName());
        assertEquals("Cat", result.getSpecies());
        assertEquals(5, result.getAge());
        assertEquals("Bob", result.getOwnerName());
        verify(petRepository).findById(petId);
        verify(petMapper).updateEntityFromRequestDto(existingPet, updateRequest);
        verify(petRepository).save(existingPet);
    }

    @Test
    void updatePet_shouldThrowPetNotFoundExceptionWhenPetNotFound() {
        Long petId = 999L;
        PetRequestDto updateRequest = new PetRequestDto("Leo", "Cat", 5, "Bob");

        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        PetNotFoundException exception = assertThrows(PetNotFoundException.class,
                () -> petService.updatePet(petId, updateRequest));

        assertTrue(exception.getMessage().contains("Pet with ID " + petId + " not found"));
        verify(petRepository).findById(petId);
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void getPetById_shouldReturnPetWhenFound() throws PetNotFoundException {
        Long petId = 10L;
        Pet existingPet = Pet.builder()
                .id(petId)
                .name("Milo")
                .species("Cat")
                .age(4)
                .ownerName("Eve")
                .build();

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
        when(petMapper.entityToPetResponseDto(existingPet))
                .thenReturn(new PetResponseDto(petId, "Milo", "Cat", 4, "Eve"));

        PetResponseDto result = petService.getPetById(petId);

        assertEquals(petId, result.getId());
        assertEquals("Milo", result.getName());
        verify(petRepository).findById(petId);
        verify(petMapper).entityToPetResponseDto(existingPet);
    }

    @Test
    void getPetById_shouldThrowPetNotFoundExceptionWhenNotFound() {
        Long petId = 111L;
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        PetNotFoundException exception = assertThrows(PetNotFoundException.class,
                () -> petService.getPetById(petId));

        assertTrue(exception.getMessage().contains("Pet with ID=" + petId));
        verify(petRepository).findById(petId);
    }

    @Test
    void getAllPets_shouldReturnMappedList() {
        List<Pet> pets = List.of(
                Pet.builder().id(1L).name("A").species("Dog").age(2).ownerName("O1").build(),
                Pet.builder().id(2L).name("B").species("Cat").age(3).ownerName("O2").build()
        );

        when(petRepository.findAll()).thenReturn(pets);
        when(petMapper.entityToPetResponseDto(pets.get(0)))
                .thenReturn(new PetResponseDto(1L, "A", "Dog", 2, "O1"));
        when(petMapper.entityToPetResponseDto(pets.get(1)))
                .thenReturn(new PetResponseDto(2L, "B", "Cat", 3, "O2"));

        List<PetResponseDto> result = petService.getAllPets();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getName());
        assertEquals("B", result.get(1).getName());
        verify(petRepository).findAll();
    }

    @Test
    void deletePet_shouldDeleteWhenPetExists() throws PetNotFoundException {
        Long petId = 8L;
        Pet existingPet = Pet.builder().id(petId).name("Rocky").species("Dog").age(6).ownerName("Dan").build();

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));

        petService.deletePet(petId);

        verify(petRepository).findById(petId);
        verify(petRepository).deleteById(petId);
    }

    @Test
    void deletePet_shouldThrowPetNotFoundExceptionWhenPetMissing() {
        Long petId = 404L;
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        PetNotFoundException exception = assertThrows(PetNotFoundException.class,
                () -> petService.deletePet(petId));

        assertTrue(exception.getMessage().contains("Pet with ID=" + petId));
        verify(petRepository).findById(petId);
        verify(petRepository, never()).deleteById(any(Long.class));
    }
}
