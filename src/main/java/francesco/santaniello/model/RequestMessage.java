package francesco.santaniello.model;

import java.util.HashSet;
import java.util.Set;

public class RequestMessage {
    private Set<Coordinate> points = new HashSet<>();
    private short radius;
    
    public Set<Coordinate> getPoints() {
        return points;
    }

    public void setPoints(Coordinate points) {
        this.points.add(points);
    }

    public short getRadius() {
        return radius;
    }

    public void setRadius(short radius) {
        this.radius = radius;
    }
}
