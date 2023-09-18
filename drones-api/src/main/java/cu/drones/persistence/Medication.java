package cu.drones.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank(message = "Name is mandatory")
    @Column(unique = true)
    private String name;//allowed only letters, numbers, ‘-‘, ‘_’
    @Positive
    private float weight;
    @NotBlank(message = "Code is mandatory, upper case letters, underscore and numbers")
    @Column(unique = true)
    private String code;//allowed only upper case letters, underscore and numbers
    @NotBlank
    private byte[] image;

}
