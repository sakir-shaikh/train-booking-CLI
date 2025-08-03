package ticket.booking.services;

import ticket.booking.interfaces.TicketService;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.User;
import ticket.booking.localDb.TicketRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors; // Added TicketRepository for ticket persistence

/**
 * Implementation of TicketService interface providing comprehensive ticket management functionality.
 */
public class TicketServiceImpl implements TicketService {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_FILE_PATH = "app/src/main/resources/users.json";
    private List<User> userList;
    private List<Ticket> ticketList;
    
    public TicketServiceImpl() {
        this.userList = loadUsersFromFile();
        this.ticketList = TicketRepository.loadTicketsFromFile();
    }
    
    // ==================== TICKET MANAGEMENT ====================
    
    @Override
    public Ticket createTicket(Ticket ticket) {
        try {
            if (ticket != null && ticket.getTicketId() != null) {
                // Add to tickets.json
                this.ticketList.add(ticket);
                TicketRepository.saveTicketsToFile(this.ticketList);
                // Also add to user's bookedTickets
                User user = getUserById(ticket.getUserId());
                if (user != null) {
                    user.getBookedTickets().add(ticket);
                    saveUsersToFile();
                }
                return ticket;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Ticket getTicketById(String ticketId) {
        for (Ticket ticket : ticketList) {
            if (ticket.getTicketId().equals(ticketId)) {
                return ticket;
            }
        }
        return null;
    }
    
    @Override
    public boolean updateTicket(String ticketId, Ticket updatedTicket) {
        try {
            for (int i = 0; i < ticketList.size(); i++) {
                Ticket ticket = ticketList.get(i);
                if (ticket.getTicketId().equals(ticketId)) {
                    updatedTicket.setTicketId(ticketId); // Ensure ID remains the same
                    ticketList.set(i, updatedTicket);
                    TicketRepository.saveTicketsToFile(ticketList);
                    // Also update in user's bookedTickets
                    User user = getUserById(updatedTicket.getUserId());
                    if (user != null) {
                        for (int j = 0; j < user.getBookedTickets().size(); j++) {
                            if (user.getBookedTickets().get(j).getTicketId().equals(ticketId)) {
                                user.getBookedTickets().set(j, updatedTicket);
                                saveUsersToFile();
                                break;
                            }
                        }
                    }
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
    public boolean deleteTicket(String ticketId) {
        try {
            boolean removed = ticketList.removeIf(ticket -> ticket.getTicketId().equals(ticketId));
            TicketRepository.saveTicketsToFile(ticketList);
            // Also remove from user's bookedTickets
            for (User user : userList) {
                boolean userRemoved = user.getBookedTickets().removeIf(ticket -> ticket.getTicketId().equals(ticketId));
                if (userRemoved) {
                    saveUsersToFile();
                }
            }
            return removed;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<Ticket> getAllTickets() {
        return new ArrayList<>(ticketList);
    }
    
    // ==================== TICKET STATUS MANAGEMENT ====================
    
    @Override
    public boolean confirmTicket(String ticketId) {
        return updateTicketStatus(ticketId, "CONFIRMED");
    }
    
    @Override
    public boolean cancelTicket(String ticketId) {
        return updateTicketStatus(ticketId, "CANCELLED");
    }
    
    @Override
    public Ticket rescheduleTicket(String ticketId, LocalDate newDate) {
        try {
            for (User user : userList) {
                for (Ticket ticket : user.getBookedTickets()) {
                    if (ticket.getTicketId().equals(ticketId)) {
                        ticket.setDateOfTravel(newDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        saveUsersToFile();
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
    public boolean updateTicketStatus(String ticketId, String status) {
        try {
            for (User user : userList) {
                for (Ticket ticket : user.getBookedTickets()) {
                    if (ticket.getTicketId().equals(ticketId)) {
                        ticket.setTicketStatus(status);
                        saveUsersToFile();
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<Ticket> getTicketsByStatus(String status) {
        List<Ticket> tickets = new ArrayList<>();
        for (User user : userList) {
            for (Ticket ticket : user.getBookedTickets()) {
                if (ticket.getTicketStatus().equals(status)) {
                    tickets.add(ticket);
                }
            }
        }
        return tickets;
    }
    
    // ==================== PAYMENT PROCESSING ====================
    
    @Override
    public boolean processPayment(String ticketId, String paymentMethod, double amount) {
        try {
            Ticket ticket = getTicketById(ticketId);
            if (ticket != null) {
                ticket.setPaymentMethod(paymentMethod);
                ticket.setPrice(amount);
                ticket.setTicketStatus("PAID");
                saveUsersToFile();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public String getPaymentStatus(String ticketId) {
        Ticket ticket = getTicketById(ticketId);
        if (ticket != null) {
            return ticket.getTicketStatus();
        }
        return "NOT_FOUND";
    }
    
    @Override
    public boolean refundTicket(String ticketId) {
        try {
            Ticket ticket = getTicketById(ticketId);
            if (ticket != null) {
                ticket.setTicketStatus("REFUNDED");
                saveUsersToFile();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public double getTicketPrice(String ticketId) {
        Ticket ticket = getTicketById(ticketId);
        if (ticket != null) {
            return ticket.getPrice();
        }
        return 0.0;
    }
    
    // ==================== SEARCH & FILTERING ====================
    
    @Override
    public List<Ticket> getTicketsByUserId(String userId) {
        User user = getUserById(userId);
        if (user != null) {
            return new ArrayList<>(user.getBookedTickets());
        }
        return new ArrayList<>();
    }
    
    @Override
    public List<Ticket> getTicketsByTrainId(String trainId) {
        List<Ticket> tickets = new ArrayList<>();
        for (User user : userList) {
            for (Ticket ticket : user.getBookedTickets()) {
                if (ticket.getTrain() != null && ticket.getTrain().getTrainId().equals(trainId)) {
                    tickets.add(ticket);
                }
            }
        }
        return tickets;
    }
    
    @Override
    public List<Ticket> getTicketsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Ticket> tickets = new ArrayList<>();
        for (User user : userList) {
            for (Ticket ticket : user.getBookedTickets()) {
                try {
                    LocalDate ticketDate = LocalDate.parse(ticket.getDateOfTravel());
                    if (!ticketDate.isBefore(startDate) && !ticketDate.isAfter(endDate)) {
                        tickets.add(ticket);
                    }
                } catch (Exception e) {
                    // Skip tickets with invalid dates
                }
            }
        }
        return tickets;
    }
    
    @Override
    public List<Ticket> getTicketsByRoute(String source, String destination) {
        List<Ticket> tickets = new ArrayList<>();
        for (User user : userList) {
            for (Ticket ticket : user.getBookedTickets()) {
                if (ticket.getSource().equalsIgnoreCase(source) && 
                    ticket.getDestination().equalsIgnoreCase(destination)) {
                    tickets.add(ticket);
                }
            }
        }
        return tickets;
    }
    
    @Override
    public List<Ticket> searchTickets(Map<String, Object> criteria) {
        List<Ticket> allTickets = getAllTickets();
        List<Ticket> filteredTickets = new ArrayList<>();
        
        for (Ticket ticket : allTickets) {
            boolean matches = true;
            
            for (Map.Entry<String, Object> entry : criteria.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                
                switch (key) {
                    case "userId":
                        if (!ticket.getUserId().equals(value)) {
                            matches = false;
                        }
                        break;
                    case "status":
                        if (!ticket.getTicketStatus().equals(value)) {
                            matches = false;
                        }
                        break;
                    case "source":
                        if (!ticket.getSource().equalsIgnoreCase((String) value)) {
                            matches = false;
                        }
                        break;
                    case "destination":
                        if (!ticket.getDestination().equalsIgnoreCase((String) value)) {
                            matches = false;
                        }
                        break;
                }
                
                if (!matches) break;
            }
            
            if (matches) {
                filteredTickets.add(ticket);
            }
        }
        
        return filteredTickets;
    }
    
    // ==================== TICKET VALIDATION ====================
    
    @Override
    public boolean validateTicket(String ticketId) {
        Ticket ticket = getTicketById(ticketId);
        if (ticket != null) {
            return "CONFIRMED".equals(ticket.getTicketStatus()) && !isTicketExpired(ticketId);
        }
        return false;
    }
    
    @Override
    public boolean isTicketExpired(String ticketId) {
        Ticket ticket = getTicketById(ticketId);
        if (ticket != null) {
            try {
                LocalDate ticketDate = LocalDate.parse(ticket.getDateOfTravel());
                return ticketDate.isBefore(LocalDate.now());
            } catch (Exception e) {
                return true; // Invalid date format
            }
        }
        return true;
    }
    
    @Override
    public boolean canCancelTicket(String ticketId) {
        Ticket ticket = getTicketById(ticketId);
        if (ticket != null) {
            try {
                LocalDate ticketDate = LocalDate.parse(ticket.getDateOfTravel());
                LocalDate now = LocalDate.now();
                // Allow cancellation if journey is more than 24 hours away
                return ticketDate.isAfter(now.plusDays(1));
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
    @Override
    public boolean canRescheduleTicket(String ticketId) {
        Ticket ticket = getTicketById(ticketId);
        if (ticket != null) {
            try {
                LocalDate ticketDate = LocalDate.parse(ticket.getDateOfTravel());
                LocalDate now = LocalDate.now();
                // Allow rescheduling if journey is more than 1 day away
                return ticketDate.isAfter(now.plusDays(1));
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
    // ==================== TICKET STATISTICS ====================
    
    @Override
    public Map<String, Object> getTicketStatistics(String ticketId) {
        Ticket ticket = getTicketById(ticketId);
        Map<String, Object> stats = new HashMap<>();
        
        if (ticket != null) {
            stats.put("ticketId", ticket.getTicketId());
            stats.put("status", ticket.getTicketStatus());
            stats.put("price", ticket.getPrice());
            stats.put("bookingDate", ticket.getBookingDate());
            stats.put("dateOfTravel", ticket.getDateOfTravel());
            stats.put("isExpired", isTicketExpired(ticketId));
            stats.put("canCancel", canCancelTicket(ticketId));
            stats.put("canReschedule", canRescheduleTicket(ticketId));
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getBookingStatistics(LocalDate startDate, LocalDate endDate) {
        List<Ticket> tickets = getTicketsByDateRange(startDate, endDate);
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalBookings", tickets.size());
        stats.put("confirmedBookings", tickets.stream()
            .filter(t -> "CONFIRMED".equals(t.getTicketStatus())).count());
        stats.put("cancelledBookings", tickets.stream()
            .filter(t -> "CANCELLED".equals(t.getTicketStatus())).count());
        stats.put("totalRevenue", tickets.stream()
            .mapToDouble(Ticket::getPrice).sum());
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getRevenueStatistics(LocalDate startDate, LocalDate endDate) {
        List<Ticket> tickets = getTicketsByDateRange(startDate, endDate);
        Map<String, Object> stats = new HashMap<>();
        
        double totalRevenue = tickets.stream()
            .filter(t -> "CONFIRMED".equals(t.getTicketStatus()))
            .mapToDouble(Ticket::getPrice).sum();
        
        stats.put("totalRevenue", totalRevenue);
        stats.put("averageTicketPrice", tickets.isEmpty() ? 0 : totalRevenue / tickets.size());
        stats.put("totalTickets", tickets.size());
        
        return stats;
    }
    
    // ==================== TICKET GENERATION ====================
    
    @Override
    public byte[] generateTicketPDF(String ticketId) {
        // This would generate a PDF ticket
        // For now, return empty byte array
        return new byte[0];
    }
    
    @Override
    public byte[] generateTicketQRCode(String ticketId) {
        // This would generate a QR code for the ticket
        // For now, return empty byte array
        return new byte[0];
    }
    
    @Override
    public boolean sendTicketEmail(String ticketId, String email) {
        // This would send ticket via email
        // For now, return true
        return true;
    }
    
    @Override
    public boolean sendTicketSMS(String ticketId, String phoneNumber) {
        // This would send ticket via SMS
        // For now, return true
        return true;
    }
    
    // ==================== ADDITIONAL UTILITY METHODS ====================
    
    @Override
    public Map<String, Object> getTicketDetails(String ticketId) {
        Ticket ticket = getTicketById(ticketId);
        Map<String, Object> details = new HashMap<>();
        
        if (ticket != null) {
            details.put("ticketId", ticket.getTicketId());
            details.put("userId", ticket.getUserId());
            details.put("source", ticket.getSource());
            details.put("destination", ticket.getDestination());
            details.put("dateOfTravel", ticket.getDateOfTravel());
            details.put("seatNumber", ticket.getSeatNumber());
            details.put("ticketStatus", ticket.getTicketStatus());
            details.put("price", ticket.getPrice());
            details.put("paymentMethod", ticket.getPaymentMethod());
            details.put("bookingDate", ticket.getBookingDate());
        }
        
        return details;
    }
    
    @Override
    public String getTicketSummary(String ticketId) {
        Ticket ticket = getTicketById(ticketId);
        if (ticket != null) {
            return String.format("Ticket %s: %s to %s on %s - Status: %s", 
                ticket.getTicketId(), ticket.getSource(), ticket.getDestination(), 
                ticket.getDateOfTravel(), ticket.getTicketStatus());
        }
        return "Ticket not found";
    }
    
    @Override
    public List<Map<String, Object>> getTicketHistory(String ticketId) {
        // This would return the history of changes for a ticket
        // For now, return empty list
        return new ArrayList<>();
    }
    
    @Override
    public boolean addTicketComment(String ticketId, String comment) {
        // This would add a comment to the ticket
        // For now, return true
        return true;
    }
    
    @Override
    public List<String> getTicketComments(String ticketId) {
        // This would return comments for the ticket
        // For now, return empty list
        return new ArrayList<>();
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
    
    private User getUserById(String userId) {
        return userList.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
} 