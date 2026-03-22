package it.pippo.petmanagement.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PetRequestDto {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Species is mandatory")
    private String species;

    @Min(value = 0, message = "Age must be greater than or equal to 0")
    private Integer age;

    private String ownerName;
}
