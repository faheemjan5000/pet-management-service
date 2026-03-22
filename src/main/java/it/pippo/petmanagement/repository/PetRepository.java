package it.pippo.petmanagement.repository;



import it.pippo.petmanagement.model.Pet;

import java.util.List;
import java.util.Optional;

public interface PetRepository  {

    Pet save(Pet pet) ;
    Optional<Pet> findById(Long petId);
    List<Pet> findAll();
    void deleteById(Long id);
}
