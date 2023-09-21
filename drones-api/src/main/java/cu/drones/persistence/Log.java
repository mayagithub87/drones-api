package cu.drones.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Data
@Entity
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDateTime;

    @NotBlank
    private String message;

    public Log() {
    }

    public Log(String message) {
        this.message = message;
    }

    @PrePersist
    public void prePersist() {
        creationDateTime = new Date();
    }
}
