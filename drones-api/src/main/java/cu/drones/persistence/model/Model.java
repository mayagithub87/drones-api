package cu.drones.persistence.model;

public enum Model {
    LIGHT_WEIGHT, MIDDLE_WEIGHT, CRUISER_WEIGHT, HEAVY_WEIGHT;

    /*
        light <150
        middle >150<250
        cruiser >250<350
        heavy >350<500
    */
    public boolean isModelValid(float weight) {
        switch (this) {
            case LIGHT_WEIGHT -> {
                return (weight <= 150);
            }
            case MIDDLE_WEIGHT -> {
                return (weight > 150 && weight <= 250);
            }
            case CRUISER_WEIGHT -> {
                return (weight > 250 && weight <= 350);
            }
            case HEAVY_WEIGHT -> {
                return (weight > 350 && weight <= 500);
            }
        }
        return false;
    }
}
