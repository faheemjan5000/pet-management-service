package it.pippo.petmanagement.repository.impl;

import it.pippo.petmanagement.model.Pet;
import it.pippo.petmanagement.repository.PetRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
public class PetRepositoryImpl implements PetRepository {

    private final Map<Long,Pet> pets = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public Pet save(Pet pet) {
        if(pet.getId()==null) {
            pet.setId(idGenerator.getAndIncrement());
        }
        pets.put(pet.getId(),pet);
        return pets.get(pet.getId());
    }

    @Override
    public Optional<Pet> findById(Long petId) {
        return Optional.ofNullable(pets.get(petId));
    }

    @Override
    public List<Pet> findAll() {
        return new ArrayList<>(pets.values());
    }

    @Override
    public void deleteById(Long id) {
        pets.remove(id);
    }

    @PostConstruct
    public void initializeDB(){
        Pet dog = Pet.builder()
                .name("Milo")
                .species("Dog")
                .age(3)
                .ownerName("Federica")
                .build();

        Pet cat = Pet.builder()
                .name("cici")
                .age(5)
                .ownerName("luca")
                .species("Cat")
                .build();

        Pet rabbit = Pet.builder()
                .name("Snowy")
                .species("Rabbit")
                .age(1)
                .ownerName("Emma")
                .build();

        this.save(dog);
        this.save(cat);
        this.save(rabbit);
        log.info("initialized database with the following pets : {} ",pets);

    }

}
