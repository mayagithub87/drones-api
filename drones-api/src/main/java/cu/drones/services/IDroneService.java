package cu.drones.services;

import cu.drones.persistence.Drone;
import cu.drones.persistence.model.State;

import java.util.List;
import java.util.Optional;

public interface IDroneService {

    List<Drone> listDrones();

    List<Drone> listDronesByState(State sstate);

    Optional<Drone> byId(Long id);

    Optional<Drone> bySerialNumber(String serialNumber);

    Drone save(Drone drone);

    void delete(Long id);

}
