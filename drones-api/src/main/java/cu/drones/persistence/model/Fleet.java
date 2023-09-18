package cu.drones.persistence.model;

import lombok.Data;
import cu.drones.persistence.Drone;

import java.util.ArrayList;
import java.util.List;

@Data
public class Fleet {

    private List<Drone> droneList = new ArrayList<Drone>(10);

}
