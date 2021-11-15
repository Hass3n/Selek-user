package minya.salek.salekapp.Model;

public class userTrip {
    private  String captainId;
    private  String  endTrip;
    private  String  rate;
    private  String tripDate;
    private  String tripPrice;
    private  String userId;


    public userTrip() {
    }

    public userTrip(String captainId, String endTrip, String rate, String tripDate, String tripPrice, String userId) {
        this.captainId = captainId;
        this.endTrip = endTrip;
        this.rate = rate;
        this.tripDate = tripDate;
        this.tripPrice = tripPrice;
        this.userId = userId;
    }

    public String getCaptainId() {
        return captainId;
    }

    public void setCaptainId(String captainId) {
        this.captainId = captainId;
    }

    public String getEndTrip() {
        return endTrip;
    }

    public void setEndTrip(String endTrip) {
        this.endTrip = endTrip;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getTripPrice() {
        return tripPrice;
    }

    public void setTripPrice(String tripPrice) {
        this.tripPrice = tripPrice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
