package francesco.santaniello.model;

import java.util.Objects;

public class Carburante {
    private int id;
    private int fuelId;
    private String name;
    private boolean isSelf;
    private float price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFuelId() {
        return fuelId;
    }

    public void setFuelId(int fuelId) {
        this.fuelId = fuelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setIsSelf(boolean self) {
        this.isSelf = self;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Carburante that)) return false;
        return id == that.id && fuelId == that.fuelId && isSelf == that.isSelf && Float.compare(price, that.price) == 0 && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fuelId, name, isSelf, price);
    }
}
