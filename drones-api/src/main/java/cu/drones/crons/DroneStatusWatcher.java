package cu.drones.crons;

import cu.drones.persistence.Drone;
import cu.drones.persistence.Medication;
import cu.drones.persistence.model.State;
import cu.drones.services.impl.DroneService;
import cu.drones.services.impl.LogService;
import cu.drones.services.impl.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DroneStatusWatcher {
    @Autowired
    private LogService logService;

    @Autowired
    private DroneService droneService;

    @Autowired
    private MedicationService medicationService;

    /***
     * Cron that updates drones status periodically.
     */
    @Scheduled(fixedRateString = "${drone-status-cron-delay}")
    public void updateDroneStatus() {
        if (!droneService.listDrones().isEmpty()) {
            List<Drone> drones = droneService.listDrones();
            drones.forEach(drone -> {
                State state = drone.getState();
                if (state == State.LOADED) {
                    if (!drone.hasAvailableWeightForLoading()) {
                        drone.setState(state.next());
                        droneService.save(drone);
                        logService.logMessage("Drone %s state is %s".formatted(drone.getSerialNumber(), drone.getState()));
                    }
                } else if (state != State.IDLE) {
//                    if state is delivered empty drone medications
                    if (state == State.DELIVERED) {
                        for (Medication medication : drone.getMedicationList()) {
                            medicationService.delete(medication);
                        }
                    }
                    drone.setState(state.next());
                    droneService.save(drone);
                    logService.logMessage("Drone %s state is %s".formatted(drone.getSerialNumber(), drone.getState()));
                }
            });

        }
    }
}
