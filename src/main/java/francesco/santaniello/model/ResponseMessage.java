package francesco.santaniello.model;

import java.util.Set;

public class ResponseMessage {
    private boolean success;
    private Coordinate center;
    private Set<Benzinaio> results;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Coordinate getCenter() {
        return center;
    }

    public void setCenter(Coordinate center) {
        this.center = center;
    }

    public Set<Benzinaio> getResults() {
        return results;
    }

    public void setResults(Set<Benzinaio> results) {
        this.results = results;
    }
}
