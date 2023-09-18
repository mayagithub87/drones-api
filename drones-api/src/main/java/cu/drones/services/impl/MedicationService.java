package cu.drones.services.impl;

import cu.drones.persistence.Drone;
import cu.drones.persistence.Medication;
import cu.drones.repositories.MedicationRepository;
import cu.drones.services.IMedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService implements IMedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    @Override
    public List<Medication> listMedications() {
        return (List<Medication>) medicationRepository.findAll();
    }

    @Override
    public Optional<Medication> byId(Long id) {
        return medicationRepository.findById(id);
    }

    @Override
    public Optional<Drone> byCode(String code) {
        return medicationRepository.findByCode(code);
    }

    @Override
    public Medication save(Medication medication) {
        return medicationRepository.save(medication);
    }

    @Override
    public void delete(Long id) {
        medicationRepository.deleteById(id);
    }
}
