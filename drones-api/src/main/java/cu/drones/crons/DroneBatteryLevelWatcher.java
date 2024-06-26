package cu.drones.crons;

import cu.drones.persistence.Drone;
import cu.drones.persistence.model.State;
import cu.drones.services.impl.DroneService;
import cu.drones.services.impl.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DroneBatteryLevelWatcher {

    @Autowired
    private LogService logService;

    @Autowired
    private DroneService droneService;

    /**
     * Cron that checks and download drone battery periodically.
     * Every drone-cron-delay seconds downloads in 1% the battery level and logs its status to database.
     * If battery level is 0 is recharged to 100%.
     */
    @Scheduled(fixedRateString = "${drone-cron-delay}")
    public void updateDroneBatteryLevel() {
        if (!droneService.listDrones().isEmpty()) {

            for (Drone drone : droneService.listDrones()) {
                if (drone.getState() != State.IDLE) {
                    if (drone.getBatteryLevel() > 0) drone.setBatteryLevel(drone.getBatteryLevel() - 1);
                    else drone.setBatteryLevel(100);
                    logService.logMessage("Drone %s battery level is %d%%".formatted(drone.getSerialNumber(), drone.getBatteryLevel()));
                    droneService.save(drone);
                }
            }

        }
    }

}
