package cu.drones.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cu.drones.persistence.model.Model;
import cu.drones.persistence.model.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "drones")
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long droneid;
    @Size(max = 100)
    @Column(unique = true)
    @NotBlank
    private String serialNumber;//100 characters max
    @NotNull
    private Model model;
    @Max(500)
    private float weight;//500gr max
    @Max(100)
    private float batteryLevel;//percentage
    @NotNull
    private State state;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "drone")
    private List<Medication> medicationList = new ArrayList<>();

}
