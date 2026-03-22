package it.pippo.petmanagement.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Pet {

    private Long id;
    private String name;
    private String species;
    private Integer age;
    private String ownerName;

}
