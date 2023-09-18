package cu.drones.services;

import cu.drones.persistence.Drone;
import cu.drones.persistence.Medication;

import java.util.List;
import java.util.Optional;

public interface IMedicationService {

    List<Medication> listMedications();

    Optional<Medication> byId(Long id);

    Optional<Drone> byCode(String code);

    Medication save(Medication medication);

    void delete(Long id);

}
