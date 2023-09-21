package cu.drones.persistence.model;

public enum State {
    IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING;

    public State next() {
        if (ordinal() == values().length - 1) return values()[0];
        return values()[ordinal() + 1];
    }
}
