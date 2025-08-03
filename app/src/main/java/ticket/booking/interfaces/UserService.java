package ticket.booking.interfaces;

import ticket.booking.entities.User;
import ticket.booking.entities.Ticket;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing user operations including authentication,
 * booking management, and profile operations.
 */
public interface UserService {
    
    // ==================== AUTHENTICATION ====================
    
    /**
     * Register a new user in the system
     * @param user The user object to register
     * @return true if successful, false otherwise
     */
    boolean registerUser(User user);
    
    /**
     * Authenticate a user with username/email and password
     * @param username The username or email
     * @param password The password
     * @return The authenticated user object, or null if authentication fails
     */
    User loginUser(String username, String password);
    
    /**
     * Logout a user from the system
     * @param userId The ID of the user to logout
     * @return true if successful, false otherwise
     */
    boolean logoutUser(String userId);
    
    /**
     * Reset password for a user using email
     * @param email The email address of the user
     * @return true if reset email sent successfully, false otherwise
     */
    boolean resetPassword(String email);
    
    // ==================== BOOKING ====================
    
    /**
     * Book a ticket for a user
     * @param userId The ID of the user
     * @param trainId The ID of the train
     * @param journeyDate The date of journey
     * @param seatType The type of seat (WINDOW, AISLE, etc.)
     * @param passengerDetails Map containing passenger details
     * @return The created ticket object, or null if booking fails
     */
    Ticket bookTicket(String userId, String trainId, LocalDate journeyDate, 
                     String seatType, Map<String, Object> passengerDetails);
    
    /**
     * Cancel a ticket
     * @param ticketId The ID of the ticket to cancel
     * @return true if successful, false otherwise
     */
    boolean cancelTicket(String ticketId);
    
    /**
     * Reschedule a ticket to a new date
     * @param ticketId The ID of the ticket to reschedule
     * @param newDate The new date for the journey
     * @return The updated ticket object, or null if rescheduling fails
     */
    Ticket rescheduleTicket(String ticketId, LocalDate newDate);
    
    /**
     * Get all tickets for a user
     * @param userId The ID of the user
     * @return List of tickets for the user
     */
    List<Ticket> getUserTickets(String userId);
    
    /**
     * Get a specific ticket by its ID
     * @param ticketId The ID of the ticket
     * @return The ticket object, or null if not found
     */
    Ticket getTicketById(String ticketId);
    
    /**
     * Get booking history for a user
     * @param userId The ID of the user
     * @return List of booking history entries
     */
    List<Map<String, Object>> getBookingHistory(String userId);
    
    // ==================== USER PROFILE ====================
    
    /**
     * Get user profile information
     * @param userId The ID of the user
     * @return The user profile object, or null if not found
     */
    User getUserProfile(String userId);
    
    /**
     * Update user profile information
     * @param userId The ID of the user
     * @param updatedProfile Map containing updated profile information
     * @return true if successful, false otherwise
     */
    boolean updateUserProfile(String userId, Map<String, Object> updatedProfile);
    
    /**
     * Delete a user account
     * @param userId The ID of the user to delete
     * @return true if successful, false otherwise
     */
    boolean deleteUserAccount(String userId);
    
    // ==================== ADDITIONAL UTILITY METHODS ====================
    
    /**
     * Get user by email
     * @param email The email address
     * @return The user object, or null if not found
     */
    User getUserByEmail(String email);
    
    /**
     * Check if a user exists
     * @param userId The ID of the user
     * @return true if user exists, false otherwise
     */
    boolean userExists(String userId);
    
    /**
     * Check if an email is already registered
     * @param email The email address to check
     * @return true if email is registered, false otherwise
     */
    boolean isEmailRegistered(String email);
    
    /**
     * Get user statistics
     * @param userId The ID of the user
     * @return Map containing user statistics
     */
    Map<String, Object> getUserStatistics(String userId);
    
    /**
     * Get active tickets for a user
     * @param userId The ID of the user
     * @return List of active tickets
     */
    List<Ticket> getActiveTickets(String userId);
    
    /**
     * Get cancelled tickets for a user
     * @param userId The ID of the user
     * @return List of cancelled tickets
     */
    List<Ticket> getCancelledTickets(String userId);
    
    /**
     * Get completed journeys for a user
     * @param userId The ID of the user
     * @return List of completed journey tickets
     */
    List<Ticket> getCompletedJourneys(String userId);
    
    /**
     * Update user preferences
     * @param userId The ID of the user
     * @param preferences Map containing user preferences
     * @return true if successful, false otherwise
     */
    boolean updateUserPreferences(String userId, Map<String, Object> preferences);
    
    /**
     * Get user preferences
     * @param userId The ID of the user
     * @return Map containing user preferences
     */
    Map<String, Object> getUserPreferences(String userId);
    
    /**
     * Validate user credentials
     * @param username The username or email
     * @param password The password
     * @return true if credentials are valid, false otherwise
     */
    boolean validateCredentials(String username, String password);
    
    /**
     * Change user password
     * @param userId The ID of the user
     * @param oldPassword The old password
     * @param newPassword The new password
     * @return true if successful, false otherwise
     */
    boolean changePassword(String userId, String oldPassword, String newPassword);
    
    /**
     * Get user session information
     * @param userId The ID of the user
     * @return Map containing session information
     */
    Map<String, Object> getUserSession(String userId);
    
    /**
     * Invalidate user session
     * @param userId The ID of the user
     * @return true if successful, false otherwise
     */
    boolean invalidateUserSession(String userId);

    /**
     * Update user information
     * @param userId The ID of the user to update
     * @param updatedUser The updated user object
     * @return true if successful, false otherwise
     */
    boolean updateUser(String userId, User updatedUser);
} 