package francesco.santaniello.model;

import java.util.Objects;
import java.util.Set;

public class Benzinaio {
    private int id;
    private String name;
    private Set<Carburante> fuels;
    private Coordinate location;
    private String insertDate;
    private String address;
    private String brand;
    private String distance;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Benzinaio benzinaio)) return false;
        return id == benzinaio.id && Objects.equals(name, benzinaio.name) && Objects.equals(fuels, benzinaio.fuels) && Objects.equals(location, benzinaio.location) && Objects.equals(insertDate, benzinaio.insertDate) && Objects.equals(address, benzinaio.address) && Objects.equals(brand, benzinaio.brand) && Objects.equals(distance, benzinaio.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fuels, location, insertDate, address, brand, distance);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Carburante> getFuels() {
        return fuels;
    }

    public void setFuels(Set<Carburante> fuels) {
        this.fuels = fuels;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
