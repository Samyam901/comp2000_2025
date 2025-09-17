import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A generic collection class for managing game entities.
 * T must be a subclass of Actor to ensure all entities can be painted and managed.
 */
public class GameCollection<T extends Actor> {
    private List<T> entities;
    
    public GameCollection() {
        entities = new ArrayList<>();
    }
    
    public void add(T entity) {
        entities.add(entity);
    }
    
    public void remove(T entity) {
        entities.remove(entity);
    }
    
    public void clear() {
        entities.clear();
    }
    
    public Optional<T> findAt(Cell cell) {
        return entities.stream()
            .filter(e -> e.currentCell.equals(cell))
            .findFirst();
    }
    
    public List<T> getAll() {
        return new ArrayList<>(entities);
    }
    
    public void paintAll(Graphics g) {
        entities.forEach(entity -> entity.paint(g));
    }
}