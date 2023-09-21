package cu.drones.persistence.repositories;

import cu.drones.persistence.model.State;
import cu.drones.persistence.Drone;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends CrudRepository<Drone, Long> {

    Optional<Drone> findBySerialNumber(String snumber);

    List<Drone> findAllByState(State sstate);

}
