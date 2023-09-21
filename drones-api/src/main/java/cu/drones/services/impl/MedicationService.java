package cu.drones.services.impl;

import cu.drones.persistence.Drone;
import cu.drones.persistence.Medication;
import cu.drones.persistence.repositories.MedicationRepository;
import cu.drones.services.IMedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService implements IMedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Medication> listMedications() {
        return (List<Medication>) medicationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Medication> byId(Long id) {
        return medicationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Drone> byCode(String code) {
        return medicationRepository.findByCode(code);
    }

    @Override
    @Transactional
    public Medication save(Medication medication) {
        return medicationRepository.save(medication);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        medicationRepository.deleteById(id);
    }
}
