/**
 * Abstract base class for power-up actors that combines Actor and PowerUp functionality
 */
public abstract class PowerUpActor extends Actor implements PowerUp {
    protected long spawnTime;

    public PowerUpActor(Cell inLoc) {
        super(inLoc);
        this.spawnTime = System.currentTimeMillis();
    }

    /**
     * Default implementation for power-up expiration
     */
    @Override
    public boolean isExpired() {
        return System.currentTimeMillis() - spawnTime > getExpirationTime();
    }

    /**
     * Get the time in milliseconds after which this power-up should expire
     */
    protected abstract long getExpirationTime();
}