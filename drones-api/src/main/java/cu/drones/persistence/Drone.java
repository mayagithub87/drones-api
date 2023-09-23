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
    private int batteryLevel;//percentage
    @NotNull
    private State state;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "drone", fetch = FetchType.EAGER)
    private List<Medication> medicationList = new ArrayList<>();

    public Drone() {
    }

    public Drone(String serialNumber, Model model, float weight, int batteryLevel, State state) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.weight = weight;
        this.batteryLevel = batteryLevel;
        this.state = state;
    }

    public boolean hasAvailableWeightForLoading() {
        if (medicationList.isEmpty()) return true;
        else {
            final float[] totalWeight = {0};
            medicationList.forEach(medication -> totalWeight[0] += medication.getWeight());
            return (totalWeight[0] < weight);
        }
    }
}
