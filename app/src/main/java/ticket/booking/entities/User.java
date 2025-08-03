package ticket.booking.entities;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String hashedPassword;
    private String phoneNumber;
    private String registrationDate;
    private List<Ticket> bookedTickets;

    //constructor
    public User(String userId, String name, String email, String password, String hashedPassword, String phoneNumber, String registrationDate, List<Ticket> bookedTickets) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.hashedPassword = hashedPassword;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
        this.bookedTickets = bookedTickets;
    }

    //default constructor
    public User() {
        // Default constructor
        this.registrationDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }
    
    // Getters and Setters
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getHashedPassword() {
        return hashedPassword;
    }
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
    public List<Ticket> getBookedTickets() {
        return bookedTickets;
    }
    public void setBookedTickets(List<Ticket> bookedTickets) {
        this.bookedTickets = bookedTickets;
    }
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", bookedTickets=" + bookedTickets +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!userId.equals(user.userId)) return false;
        if (!name.equals(user.name)) return false;
        if (!email.equals(user.email)) return false;
        if (!password.equals(user.password)) return false;
        if (!hashedPassword.equals(user.hashedPassword)) return false;
        if (!phoneNumber.equals(user.phoneNumber)) return false;
        if (!registrationDate.equals(user.registrationDate)) return false;
        return bookedTickets.equals(user.bookedTickets);
    }
    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + hashedPassword.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + registrationDate.hashCode();
        result = 31 * result + bookedTickets.hashCode();
        return result;
    }

    public void printTickets() {
        if (bookedTickets.isEmpty()) {
            System.out.println("No tickets booked.");
        } else {
            for (Ticket ticket : bookedTickets) {
                System.out.println(ticket.getTicketDetails());
            }
        }
    }
}
