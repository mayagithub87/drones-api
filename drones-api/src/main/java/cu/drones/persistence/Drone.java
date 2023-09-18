package cu.drones.persistence;

import cu.drones.persistence.model.Model;
import cu.drones.persistence.model.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "drones")
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Size(max = 100)
    @Column(unique = true)
    @NotBlank
    private String serialNumber;//100 characters max
    @NotBlank
    private Model model;
    @Max(500)
    private float weight;//500gr max
    private float batteryCapacity;//percentage
    @NotBlank
    private State state;


}
