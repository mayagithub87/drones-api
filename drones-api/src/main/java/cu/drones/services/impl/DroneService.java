package cu.drones.services.impl;

import cu.drones.persistence.Drone;
import cu.drones.persistence.model.State;
import cu.drones.repositories.DroneRepository;
import cu.drones.services.IDroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DroneService implements IDroneService {

    @Autowired
    private DroneRepository droneRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Drone> listDrones() {
        return (List<Drone>) droneRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Drone> listDronesByState(State sstate) {
        return droneRepository.findAllByState(sstate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Drone> byId(Long id) {
        return droneRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Drone> bySerialNumber(String serialNumber) {
        return droneRepository.findBySerialNumber(serialNumber);
    }

    @Override
    @Transactional
    public Drone save(Drone drone) {
        return droneRepository.save(drone);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        droneRepository.deleteById(id);
    }

    @Override
    public List<Drone> listDronesForLoading() {
        List<Drone> idle = droneRepository.findAllByState(State.IDLE);
        List<Drone> loaded = droneRepository.findAllByState(State.LOADED);
        idle.addAll(loaded);
        return idle;
    }

    @Override
    public Drone setDroneStatus(Drone drone, State state) {
        if (drone != null) {
            drone.setState(state);
            return this.save(drone);
        }
        return null;
    }
}
