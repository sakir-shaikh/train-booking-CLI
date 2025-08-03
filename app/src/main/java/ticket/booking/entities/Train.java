package ticket.booking.entities;

import java.util.List;
import java.util.Map;

public class Train {
    private String trainId;
    private String trainNumber;
    private String trainName;
    private String source;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private List<List<Integer>> seats;
    private List<String> stations;
    private Map<String, String> stationArrivalTimes;
    private String trainType;
    private int totalSeats;
    private int availableSeats;
    private double basePrice;

    // Constructor
    public Train(String trainId, String trainNumber, String trainName, String source, String destination,
                 String departureTime, String arrivalTime, List<List<Integer>> seats,
                 List<String> stations, Map<String, String> stationArrivalTimes, String trainType,
                 int totalSeats, int availableSeats, double basePrice) {
        this.trainId = trainId;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.seats = seats;
        this.stations = stations;
        this.stationArrivalTimes = stationArrivalTimes;
        this.trainType = trainType;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.basePrice = basePrice;
    }
    
    // Default constructor
    public Train() {
        // Default constructor
    }
    
    // Getters and Setters
    public String getTrainId() {
        return trainId;
    }
    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }
    public String getTrainNumber() {
        return trainNumber;
    }
    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }
    public String getTrainName() {
        return trainName;
    }
    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
    public String getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public List<List<Integer>> getSeats() {
        return seats;
    }
    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }
    public List<String> getStations() {
        return stations;
    }
    public void setStations(List<String> stations) {
        this.stations = stations;
    }
    public Map<String, String> getStationArrivalTimes() {
        return stationArrivalTimes;
    }
    public void setStationArrivalTimes(Map<String, String> stationArrivalTimes) {
        this.stationArrivalTimes = stationArrivalTimes;
    }
    public String getTrainType() {
        return trainType;
    }
    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }
    public int getTotalSeats() {
        return totalSeats;
    }
    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
    public int getAvailableSeats() {
        return availableSeats;
    }
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    public double getBasePrice() {
        return basePrice;
    }
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
    
    @Override
    public String toString() {
        return "Train{" +
                "trainId='" + trainId + '\'' +
                ", trainNumber='" + trainNumber + '\'' +
                ", trainName='" + trainName + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", seats=" + seats +
                ", stations=" + stations +
                ", stationArrivalTimes=" + stationArrivalTimes +
                ", trainType='" + trainType + '\'' +
                ", totalSeats=" + totalSeats +
                ", availableSeats=" + availableSeats +
                ", basePrice=" + basePrice +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Train)) return false;

        Train train = (Train) o;

        if (!trainId.equals(train.trainId)) return false;
        if (!trainNumber.equals(train.trainNumber)) return false;
        if (!trainName.equals(train.trainName)) return false;
        if (!source.equals(train.source)) return false;
        if (!destination.equals(train.destination)) return false;
        if (!departureTime.equals(train.departureTime)) return false;
        if (!arrivalTime.equals(train.arrivalTime)) return false;
        if (!seats.equals(train.seats)) return false;
        if (!stations.equals(train.stations)) return false;
        if (!stationArrivalTimes.equals(train.stationArrivalTimes)) return false;
        if (!trainType.equals(train.trainType)) return false;
        if (totalSeats != train.totalSeats) return false;
        if (availableSeats != train.availableSeats) return false;
        return Double.compare(train.basePrice, basePrice) == 0;
    }
    @Override
    public int hashCode() {
        int result = trainId.hashCode();
        result = 31 * result + trainNumber.hashCode();
        result = 31 * result + trainName.hashCode();
        result = 31 * result + source.hashCode();
        result = 31 * result + destination.hashCode();
        result = 31 * result + departureTime.hashCode();
        result = 31 * result + arrivalTime.hashCode();
        result = 31 * result + seats.hashCode();
        result = 31 * result + stations.hashCode();
        result = 31 * result + stationArrivalTimes.hashCode();
        result = 31 * result + trainType.hashCode();
        result = 31 * result + totalSeats;
        result = 31 * result + availableSeats;
        result = 31 * result + Double.hashCode(basePrice);
        return result;
    }
    public String getTrainInfo(){
        return "Train ID: " + trainId + ", Train Number: " + trainNumber + ", Train Name: " + trainName +
               ", Source: " + source + ", Destination: " + destination +
               ", Departure Time: " + departureTime + ", Arrival Time: " + arrivalTime +
               ", Type: " + trainType + ", Price: $" + basePrice;
    }
}
