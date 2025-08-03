package ticket.booking.services;

import ticket.booking.interfaces.UserService;
import ticket.booking.entities.User;
import ticket.booking.entities.Ticket;
import ticket.booking.utils.UserServiceUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of UserService interface providing comprehensive user management functionality.
 */
public class UserServiceImpl implements UserService {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_FILE_PATH = "app/src/main/resources/users.json";
    private List<User> userList;
    private Map<String, User> activeSessions;
    
    public UserServiceImpl() {
        this.userList = loadUsersFromFile();
        this.activeSessions = new HashMap<>();
    }

    private void reloadUsers() {
        this.userList = loadUsersFromFile();
    }
    
    // ==================== AUTHENTICATION ====================
    
    @Override
    public boolean registerUser(User user) {
        try {
            reloadUsers();
            if (user != null && user.getEmail() != null) {
                // Check if email already exists
                if (isEmailRegistered(user.getEmail())) {
                    return false; // Email already registered
                }
                // Hash the password before storing
                if (user.getPassword() != null) {
                    user.setHashedPassword(UserServiceUtil.hashPassword(user.getPassword()));
                }
                userList.add(user);
                saveUsersToFile();
                reloadUsers();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public User loginUser(String username, String password) {
        try {
            User user = getUserByEmail(username);
            if (user != null && UserServiceUtil.checkPassword(password, user.getHashedPassword())) {
                // Create session
                activeSessions.put(user.getUserId(), user);
                return user;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean logoutUser(String userId) {
        try {
            User user = activeSessions.remove(userId);
            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean resetPassword(String email) {
        try {
            User user = getUserByEmail(email);
            if (user != null) {
                // In a real system, this would send a reset email
                // For now, just return true
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== BOOKING ====================
    
    @Override
    public Ticket bookTicket(String userId, String trainId, LocalDate journeyDate, 
                           String seatType, Map<String, Object> passengerDetails) {
        try {
            User user = getUserProfile(userId);
            if (user == null) {
                return null;
            }
            
            // Create a new ticket
            Ticket ticket = new Ticket();
            ticket.setTicketId(UUID.randomUUID().toString());
            ticket.setUserId(userId);
            ticket.setDateOfTravel(journeyDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            ticket.setSeatNumber(generateSeatNumber());
            ticket.setTicketStatus("CONFIRMED");
            ticket.setPrice(85.50); // Default price
            ticket.setPaymentMethod("CREDIT_CARD");
            ticket.setBookingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            
            // Add ticket to user's booked tickets
            user.getBookedTickets().add(ticket);
            saveUsersToFile();
            
            return ticket;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean cancelTicket(String ticketId) {
        try {
            for (User user : userList) {
                boolean removed = user.getBookedTickets().removeIf(ticket -> 
                    ticket.getTicketId().equals(ticketId));
                if (removed) {
                    saveUsersToFile();
                    reloadUsers();
                }
                return removed;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Ticket rescheduleTicket(String ticketId, LocalDate newDate) {
        try {
            for (User user : userList) {
                for (Ticket ticket : user.getBookedTickets()) {
                    if (ticket.getTicketId().equals(ticketId)) {
                        ticket.setDateOfTravel(newDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        saveUsersToFile();
                        reloadUsers();
                        return ticket;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Ticket> getUserTickets(String userId) {
        User user = getUserProfile(userId);
        if (user != null) {
            return new ArrayList<>(user.getBookedTickets());
        }
        return new ArrayList<>();
    }
    
    @Override
    public Ticket getTicketById(String ticketId) {
        for (User user : userList) {
            for (Ticket ticket : user.getBookedTickets()) {
                if (ticket.getTicketId().equals(ticketId)) {
                    return ticket;
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Map<String, Object>> getBookingHistory(String userId) {
        List<Ticket> tickets = getUserTickets(userId);
        List<Map<String, Object>> history = new ArrayList<>();
        
        for (Ticket ticket : tickets) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("ticketId", ticket.getTicketId());
            entry.put("dateOfTravel", ticket.getDateOfTravel());
            entry.put("status", ticket.getTicketStatus());
            entry.put("price", ticket.getPrice());
            entry.put("bookingDate", ticket.getBookingDate());
            history.add(entry);
        }
        
        return history;
    }
    
    // ==================== USER PROFILE ====================
    
    @Override
    public boolean deleteUserAccount(String userId) {
        return deleteUser(userId);
    }
    
    @Override
    public User getUserProfile(String userId) {
        reloadUsers();
        return userList.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public boolean updateUser(String userId, User updatedUser) {
        try {
            reloadUsers();
            for (int i = 0; i < userList.size(); i++) {
                User user = userList.get(i);
                if (user.getUserId().equals(userId)) {
                    updatedUser.setUserId(userId); // Ensure ID remains the same
                    userList.set(i, updatedUser);
                    saveUsersToFile();
                    reloadUsers();
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updateUserProfile(String userId, Map<String, Object> updatedProfile) {
        try {
            reloadUsers();
            User user = getUserProfile(userId);
            if (user != null) {
                // Update user fields based on the provided map
                if (updatedProfile.containsKey("name")) {
                    user.setName((String) updatedProfile.get("name"));
                }
                if (updatedProfile.containsKey("email")) {
                    user.setEmail((String) updatedProfile.get("email"));
                }
                if (updatedProfile.containsKey("phoneNumber")) {
                    user.setPhoneNumber((String) updatedProfile.get("phoneNumber"));
                }
                
                saveUsersToFile();
                reloadUsers();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // This method is not in the interface, so make it private
    private boolean deleteUser(String userId) {
        try {
            reloadUsers();
            boolean removed = userList.removeIf(user -> user.getUserId().equals(userId));
            if (removed) {
                saveUsersToFile();
                reloadUsers();
            }
            return removed;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== ADDITIONAL UTILITY METHODS ====================
    
    @Override
    public User getUserByEmail(String email) {
        reloadUsers();
        return userList.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public boolean userExists(String userId) {
        reloadUsers();
        return getUserProfile(userId) != null;
    }
    
    @Override
    public boolean isEmailRegistered(String email) {
        reloadUsers();
        return getUserByEmail(email) != null;
    }
    
    @Override
    public Map<String, Object> getUserStatistics(String userId) {
        reloadUsers();
        User user = getUserProfile(userId);
        Map<String, Object> stats = new HashMap<>();
        
        if (user != null) {
            List<Ticket> tickets = user.getBookedTickets();
            stats.put("totalBookings", tickets.size());
            stats.put("activeBookings", tickets.stream()
                .filter(t -> "CONFIRMED".equals(t.getTicketStatus())).count());
            stats.put("cancelledBookings", tickets.stream()
                .filter(t -> "CANCELLED".equals(t.getTicketStatus())).count());
            stats.put("totalSpent", tickets.stream()
                .mapToDouble(Ticket::getPrice).sum());
        }
        
        return stats;
    }
    
    @Override
    public List<Ticket> getActiveTickets(String userId) {
        reloadUsers();
        return getUserTickets(userId).stream()
                .filter(ticket -> "CONFIRMED".equals(ticket.getTicketStatus()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Ticket> getCancelledTickets(String userId) {
        reloadUsers();
        return getUserTickets(userId).stream()
                .filter(ticket -> "CANCELLED".equals(ticket.getTicketStatus()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Ticket> getCompletedJourneys(String userId) {
        reloadUsers();
        return getUserTickets(userId).stream()
                .filter(ticket -> "COMPLETED".equals(ticket.getTicketStatus()))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean updateUserPreferences(String userId, Map<String, Object> preferences) {
        reloadUsers();
        User user = getUserProfile(userId);
        if (user != null) {
            // Update user preferences
            // This would typically update a preferences field in the User entity
            saveUsersToFile();
            reloadUsers();
            return true;
        }
        return false;
    }
    
    @Override
    public Map<String, Object> getUserPreferences(String userId) {
        reloadUsers();
        User user = getUserProfile(userId);
        Map<String, Object> preferences = new HashMap<>();
        
        if (user != null) {
            // Return user preferences
            // This would typically return preferences from the User entity
        }
        
        return preferences;
    }
    
    @Override
    public boolean validateCredentials(String username, String password) {
        reloadUsers();
        User user = getUserByEmail(username);
        return user != null && UserServiceUtil.checkPassword(password, user.getHashedPassword());
    }
    
    @Override
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        try {
            reloadUsers();
            User user = getUserProfile(userId);
            if (user != null && UserServiceUtil.checkPassword(oldPassword, user.getHashedPassword())) {
                user.setHashedPassword(UserServiceUtil.hashPassword(newPassword));
                saveUsersToFile();
                reloadUsers();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Map<String, Object> getUserSession(String userId) {
        reloadUsers();
        User user = activeSessions.get(userId);
        Map<String, Object> session = new HashMap<>();
        
        if (user != null) {
            session.put("userId", user.getUserId());
            session.put("email", user.getEmail());
            session.put("name", user.getName());
            session.put("isActive", true);
        }
        
        return session;
    }
    
    @Override
    public boolean invalidateUserSession(String userId) {
        return logoutUser(userId);
    }
    
    // ==================== PRIVATE HELPER METHODS ====================
    
    private List<User> loadUsersFromFile() {
        try {
            File file = new File(USERS_FILE_PATH);
            if (file.exists()) {
                Map<String, List<User>> data = objectMapper.readValue(file, 
                    new TypeReference<Map<String, List<User>>>() {});
                return data.getOrDefault("users", new ArrayList<>());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    private void saveUsersToFile() {
        try {
            Map<String, List<User>> data = new HashMap<>();
            data.put("users", userList);
            objectMapper.writeValue(new File(USERS_FILE_PATH), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String generateSeatNumber() {
        // Generate a random seat number
        Random random = new Random();
        char row = (char) ('A' + random.nextInt(26));
        int seat = random.nextInt(50) + 1;
        return row + String.valueOf(seat);
    }
} 