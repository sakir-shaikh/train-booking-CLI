package ticket.booking.entities;

import java.time.LocalDateTime;

public class Ticket {
    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private String dateOfTravel;
    private String seatNumber;
    private String ticketStatus;
    private Double price;
    private String paymentMethod;
    private String bookingDate;
    private Train train;

    //constructor
    public Ticket(String ticketId, String userId, String source, String destination, String dateOfTravel, Train train) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.train = train;
    }
    
    //constructor with all fields
    public Ticket(String ticketId, String userId, String source, String destination, String dateOfTravel, 
                  String seatNumber, String ticketStatus, Double price, String paymentMethod, String bookingDate, Train train) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.seatNumber = seatNumber;
        this.ticketStatus = ticketStatus;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.bookingDate = bookingDate;
        this.train = train;
    }
    
    //default constructor
    public Ticket() {
        // Default constructor
    }
    // Getters and Setters
    public String getTicketId() {
        return ticketId;
    }
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
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
    public String getDateOfTravel() {
        return dateOfTravel;
    }
    public void setDateOfTravel(String dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }
    public String getSeatNumber() {
        return seatNumber;
    }
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    public String getTicketStatus() {
        return ticketStatus;
    }
    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getBookingDate() {
        return bookingDate;
    }
    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
    public Train getTrain() {
        return train;
    }
    public void setTrain(Train train) {
        this.train = train;
    }
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", userId='" + userId + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", dateOfTravel='" + dateOfTravel + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", ticketStatus='" + ticketStatus + '\'' +
                ", price=" + price +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", bookingDate='" + bookingDate + '\'' +
                ", train=" + train +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;

        Ticket ticket = (Ticket) o;

        if (!ticketId.equals(ticket.ticketId)) return false;
        if (!userId.equals(ticket.userId)) return false;
        if (!source.equals(ticket.source)) return false;
        if (!destination.equals(ticket.destination)) return false;
        if (!dateOfTravel.equals(ticket.dateOfTravel)) return false;
        return train.equals(ticket.train);
    }
    @Override
    public int hashCode() {
        int result = ticketId.hashCode();
        result = 31 * result + userId.hashCode();
        result = 31 * result + source.hashCode();
        result = 31 * result + destination.hashCode();
        result = 31 * result + dateOfTravel.hashCode();
        result = 31 * result + train.hashCode();
        return result;
    }
    // Additional methods can be added as needed
    public String getTicketDetails() {
        return "Ticket ID: " + ticketId + ", User ID: " + userId + ", Source: " + source +
               ", Destination: " + destination + ", Date of Travel: " + dateOfTravel +
               ", Train: " + (train != null ? train.getTrainNumber() : "N/A");
    }

    public String getTicketSummary() {
        return "Ticket Summary: " + source + " to " + destination + " on " + dateOfTravel +
               " with Train No: " + (train != null ? train.getTrainNumber() : "N/A");
    }
}
