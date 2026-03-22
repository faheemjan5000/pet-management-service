package it.pippo.petmanagement.mapper;


import it.pippo.petmanagement.dto.PetRequestDto;
import it.pippo.petmanagement.dto.PetResponseDto;
import it.pippo.petmanagement.model.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

    public Pet petDtoRequestToEntity(PetRequestDto petDto){
        Pet pet = new Pet();
        pet.setName(petDto.getName());
        pet.setAge(petDto.getAge());
        pet.setSpecies(petDto.getSpecies());
        pet.setOwnerName(petDto.getOwnerName());

        return pet;
    }

    public PetResponseDto entityToPetResponseDto(Pet pet){
        PetResponseDto petDto = new PetResponseDto();
        petDto.setId(pet.getId());
        petDto.setName(pet.getName());
        petDto.setSpecies(pet.getSpecies());
        petDto.setAge(pet.getAge());
        petDto.setOwnerName(pet.getOwnerName());

        return petDto;
    }

    public void updateEntityFromRequestDto(Pet petEntity, PetRequestDto petDto){

        if(petDto.getName()!=null){
            petEntity.setName(petDto.getName());
        }
        if(petDto.getOwnerName()!=null){
            petEntity.setOwnerName(petDto.getOwnerName());
        }
        if(petDto.getAge()!=null){
            petEntity.setAge(petDto.getAge());
        }
        if(petDto.getSpecies()!=null){
            petEntity.setSpecies(petDto.getSpecies());
        }

    }

    public void updateDtoFromEntity(PetRequestDto petDto, Pet petEntity){

        if(petEntity.getName()!=null){
            petDto.setName(petEntity.getName());
        }
        if(petEntity.getOwnerName()!=null){
            petDto.setOwnerName(petEntity.getOwnerName());
        }
        if(petEntity.getAge()!=null){
            petDto.setAge(petEntity.getAge());
        }
        if(petEntity.getSpecies()!=null){
            petDto.setSpecies(petEntity.getSpecies());
        }

    }
}
