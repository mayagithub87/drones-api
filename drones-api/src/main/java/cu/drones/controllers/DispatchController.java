package cu.drones.controllers;


import cu.drones.persistence.Drone;
import cu.drones.persistence.Medication;
import cu.drones.persistence.model.State;
import cu.drones.services.impl.DroneService;
import cu.drones.services.impl.MedicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/drones")
public class DispatchController {

    @Autowired
    private DroneService droneService;

    @Autowired
    private MedicationService medicationService;

    @GetMapping
    public ResponseEntity<List<Drone>> list() {
        return ResponseEntity.ok(droneService.listDrones());
    }

    @GetMapping("/meds/{droneSerialNumber}")
    public ResponseEntity<?> getDroneMedications(@PathVariable String droneSerialNumber) {
        Optional<Drone> droneDb = droneService.bySerialNumber(droneSerialNumber);
        if (droneDb.isEmpty()) return ResponseEntity.notFound().build();

        Drone drone = droneDb.get();
        return ResponseEntity.ok(drone.getMedicationList());
    }

    @GetMapping("/forloading")
    public ResponseEntity<?> getAvailableDronesForLoading() {
        return ResponseEntity.ok(droneService.listDronesForLoading());
    }

    @GetMapping("/battery//{droneSerialNumber}")
    public ResponseEntity<?> getDroneBatteryLevel(@PathVariable String droneSerialNumber) {
        Optional<Drone> droneDb = droneService.bySerialNumber(droneSerialNumber);
        if (droneDb.isEmpty()) return ResponseEntity.notFound().build();

        Drone drone = droneDb.get();
        return ResponseEntity.ok(drone.getBatteryLevel());
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Drone drone, BindingResult result) {
        if (result.hasErrors()) return validate(result);

        if (!drone.getModel().isModelValid(drone.getWeight())) {
            result.addError(new ObjectError("model and weight", "Model and weight value doesn't match"));
            return validate(result);
        }

        Optional<Drone> dronedb = droneService.bySerialNumber(drone.getSerialNumber());
        if (dronedb.isPresent()) {
            result.addError(new ObjectError("serialNumber", "A drone already exist with that serialNumber"));
            return validate(result);
        }

        Drone savedb = droneService.save(drone);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedb);
    }

    @PutMapping("/{droneSerialNumber}")
    public ResponseEntity<?> save(@Valid @RequestBody Drone drone, BindingResult result, @PathVariable String droneSerialNumber) {
        if (result.hasErrors()) return validate(result);

        Optional<Drone> droneDb = droneService.bySerialNumber(droneSerialNumber);
        if (droneDb.isEmpty()) return ResponseEntity.notFound().build();

        if (!drone.getModel().isModelValid(drone.getWeight())) {
            result.addError(new ObjectError("model and weight", "Model and weight value doesn't match"));
            return validate(result);
        }

        Drone savedb = droneService.save(drone);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedb);
    }

    @PostMapping("/meds/{droneSerialNumber}")
    public ResponseEntity<?> loadMedications(@Valid @RequestBody List<Medication> medications, BindingResult result, @PathVariable String droneSerialNumber) {
        if (result.hasErrors()) return validate(result);

        Optional<Drone> droneDb = droneService.bySerialNumber(droneSerialNumber);
        if (droneDb.isEmpty()) return ResponseEntity.notFound().build();

        if (medications.isEmpty() || medications == null) return ResponseEntity.badRequest().build();

        Drone drone = droneDb.get();
        State state = drone.getState();
        switch (state) {
            case LOADING, DELIVERING, DELIVERED, RETURNING -> {
                result.addError(new ObjectError("drone", "Can't load drone with medications 'cause it's status is %s".formatted(state)));
                return validate(result);
            }
            default -> {
                if (drone.getBatteryLevel() <= 25) {
                    droneService.setDroneStatus(drone, State.IDLE);
                    result.addError(new ObjectError("drone", "Can't load drone with medications 'cause battery capacity is lower than 25 percent"));
                    return validate(result);
                }
                drone = droneService.setDroneStatus(drone, State.LOADING);
            }
        }

        List<Medication> medicationList = drone.getMedicationList();
        float droneWeight = drone.getWeight();
        final float[] totalWeight = {0, 0};
        medicationList.forEach(medication -> {
            totalWeight[0] += medication.getWeight();
        });
        medications.forEach(medication -> {
            totalWeight[1] += medication.getWeight();
        });
        if (totalWeight[0] + totalWeight[1] > droneWeight) {
            droneService.setDroneStatus(drone, State.IDLE);
            result.addError(new ObjectError("medications", "Specified medications exceeds drone's capacity"));
            return validate(result);
        } else {
            Drone finalDrone = drone;
            medications.forEach(medication -> {
                medication.setDrone(finalDrone);
                medicationService.save(medication);
            });
            droneService.setDroneStatus(drone, State.LOADED);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }

    private ResponseEntity<?> validate(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), "At field %s %s".formatted(fieldError.getField(), fieldError.getDefaultMessage()));
        });
        result.getAllErrors().forEach(error -> {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
