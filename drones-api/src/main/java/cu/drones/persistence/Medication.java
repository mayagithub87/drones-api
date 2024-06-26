package cu.drones.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long medid;
    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "(\\w|-|_)*$")
    private String name;//allowed only letters, numbers, ‘-‘, ‘_’
    @Positive
    private float weight;
    @NotBlank(message = "Code is mandatory, upper case letters, underscore and numbers")
    @Column(unique = true)
    @Pattern(regexp = "([A-Z]|[0-9]|_)*$")
    private String code;//allowed only upper case letters, underscore and numbers
    private byte[] image;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "droneid", nullable = false)
    private Drone drone;

    public Medication() {
    }

    public Medication(String name, float weight, String code, byte[] image) {
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.image = image;
    }
}
