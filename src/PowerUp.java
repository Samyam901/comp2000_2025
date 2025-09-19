/**
 * Interface for power-up items in the game.
 * Provides contract for power-up behavior and lifecycle.
 */
public interface PowerUp {
    /**
     * Check if the power-up has expired
     * @return true if the power-up should be removed
     */
    boolean isExpired();
    
    /**
     * Apply the power-up effect
     * @param snake The snake to apply the effect to
     */
    void applyEffect(Snake snake);
    
    /**
     * Get the duration of the power-up effect in milliseconds
     * @return Duration in milliseconds
     */
    long getEffectDuration();
    
    /**
     * Get the points awarded for collecting this power-up
     * @return Points value
     */
    int getPointsValue();
    
    /**
     * Get a description of the power-up effect
     * @return Description string
     */
    String getEffectDescription();
}