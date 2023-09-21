package cu.drones.repositories;

import cu.drones.persistence.Log;
import org.springframework.data.repository.CrudRepository;

public interface LogRepository extends CrudRepository<Log, Long> {
}
