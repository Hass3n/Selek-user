package minya.salek.salekapp.Model;

public class OldTripModel {
    private String tripDate, tripPlace;

    public OldTripModel(String tripDate, String tripPlace) {
        this.tripDate = tripDate;
        this.tripPlace = tripPlace;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getTripPlace() {
        return tripPlace;
    }

    public void setTripPlace(String tripPlace) {
        this.tripPlace = tripPlace;
    }
}
