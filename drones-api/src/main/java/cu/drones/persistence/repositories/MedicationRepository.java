package cu.drones.persistence.repositories;

import cu.drones.persistence.Drone;
import cu.drones.persistence.Medication;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MedicationRepository extends CrudRepository<Medication, Long> {

    Optional<Drone> findByCode(String scode);

}
