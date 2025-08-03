package ticket.booking.interfaces;

import ticket.booking.entities.Ticket;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing ticket operations including CRUD operations,
 * payment processing, and ticket status management.
 */
public interface TicketService {
    
    // ==================== TICKET MANAGEMENT ====================
    
    /**
     * Create a new ticket
     * @param ticket The ticket object to create
     * @return The created ticket object, or null if creation fails
     */
    Ticket createTicket(Ticket ticket);
    
    /**
     * Get a ticket by its ID
     * @param ticketId The ID of the ticket
     * @return The ticket object, or null if not found
     */
    Ticket getTicketById(String ticketId);
    
    /**
     * Update ticket information
     * @param ticketId The ID of the ticket to update
     * @param updatedTicket The updated ticket object
     * @return true if successful, false otherwise
     */
    boolean updateTicket(String ticketId, Ticket updatedTicket);
    
    /**
     * Delete a ticket
     * @param ticketId The ID of the ticket to delete
     * @return true if successful, false otherwise
     */
    boolean deleteTicket(String ticketId);
    
    /**
     * Get all tickets in the system
     * @return List of all tickets
     */
    List<Ticket> getAllTickets();
    
    // ==================== TICKET STATUS MANAGEMENT ====================
    
    /**
     * Confirm a ticket
     * @param ticketId The ID of the ticket to confirm
     * @return true if successful, false otherwise
     */
    boolean confirmTicket(String ticketId);
    
    /**
     * Cancel a ticket
     * @param ticketId The ID of the ticket to cancel
     * @return true if successful, false otherwise
     */
    boolean cancelTicket(String ticketId);
    
    /**
     * Reschedule a ticket
     * @param ticketId The ID of the ticket to reschedule
     * @param newDate The new date for the journey
     * @return The updated ticket object, or null if rescheduling fails
     */
    Ticket rescheduleTicket(String ticketId, LocalDate newDate);
    
    /**
     * Update ticket status
     * @param ticketId The ID of the ticket
     * @param status The new status (CONFIRMED, CANCELLED, PENDING, etc.)
     * @return true if successful, false otherwise
     */
    boolean updateTicketStatus(String ticketId, String status);
    
    /**
     * Get tickets by status
     * @param status The status to filter by
     * @return List of tickets with the specified status
     */
    List<Ticket> getTicketsByStatus(String status);
    
    // ==================== PAYMENT PROCESSING ====================
    
    /**
     * Process payment for a ticket
     * @param ticketId The ID of the ticket
     * @param paymentMethod The payment method (CREDIT_CARD, DEBIT_CARD, etc.)
     * @param amount The payment amount
     * @return true if payment successful, false otherwise
     */
    boolean processPayment(String ticketId, String paymentMethod, double amount);
    
    /**
     * Get payment status for a ticket
     * @param ticketId The ID of the ticket
     * @return The payment status
     */
    String getPaymentStatus(String ticketId);
    
    /**
     * Refund a ticket payment
     * @param ticketId The ID of the ticket to refund
     * @return true if refund successful, false otherwise
     */
    boolean refundTicket(String ticketId);
    
    /**
     * Get ticket price
     * @param ticketId The ID of the ticket
     * @return The ticket price
     */
    double getTicketPrice(String ticketId);
    
    // ==================== SEARCH & FILTERING ====================
    
    /**
     * Get tickets by user ID
     * @param userId The ID of the user
     * @return List of tickets for the user
     */
    List<Ticket> getTicketsByUserId(String userId);
    
    /**
     * Get tickets by train ID
     * @param trainId The ID of the train
     * @return List of tickets for the train
     */
    List<Ticket> getTicketsByTrainId(String trainId);
    
    /**
     * Get tickets by date range
     * @param startDate The start date
     * @param endDate The end date
     * @return List of tickets in the date range
     */
    List<Ticket> getTicketsByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get tickets by source and destination
     * @param source The source station
     * @param destination The destination station
     * @return List of tickets for the route
     */
    List<Ticket> getTicketsByRoute(String source, String destination);
    
    /**
     * Search tickets with multiple criteria
     * @param criteria Map containing search criteria
     * @return List of tickets matching the criteria
     */
    List<Ticket> searchTickets(Map<String, Object> criteria);
    
    // ==================== TICKET VALIDATION ====================
    
    /**
     * Validate ticket for travel
     * @param ticketId The ID of the ticket
     * @return true if ticket is valid for travel, false otherwise
     */
    boolean validateTicket(String ticketId);
    
    /**
     * Check if ticket is expired
     * @param ticketId The ID of the ticket
     * @return true if ticket is expired, false otherwise
     */
    boolean isTicketExpired(String ticketId);
    
    /**
     * Check if ticket can be cancelled
     * @param ticketId The ID of the ticket
     * @return true if ticket can be cancelled, false otherwise
     */
    boolean canCancelTicket(String ticketId);
    
    /**
     * Check if ticket can be rescheduled
     * @param ticketId The ID of the ticket
     * @return true if ticket can be rescheduled, false otherwise
     */
    boolean canRescheduleTicket(String ticketId);
    
    // ==================== TICKET STATISTICS ====================
    
    /**
     * Get ticket statistics
     * @param ticketId The ID of the ticket
     * @return Map containing ticket statistics
     */
    Map<String, Object> getTicketStatistics(String ticketId);
    
    /**
     * Get booking statistics for a date range
     * @param startDate The start date
     * @param endDate The end date
     * @return Map containing booking statistics
     */
    Map<String, Object> getBookingStatistics(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get revenue statistics
     * @param startDate The start date
     * @param endDate The end date
     * @return Map containing revenue statistics
     */
    Map<String, Object> getRevenueStatistics(LocalDate startDate, LocalDate endDate);
    
    // ==================== TICKET GENERATION ====================
    
    /**
     * Generate ticket PDF
     * @param ticketId The ID of the ticket
     * @return Byte array containing the PDF data
     */
    byte[] generateTicketPDF(String ticketId);
    
    /**
     * Generate ticket QR code
     * @param ticketId The ID of the ticket
     * @return Byte array containing the QR code image
     */
    byte[] generateTicketQRCode(String ticketId);
    
    /**
     * Send ticket to user email
     * @param ticketId The ID of the ticket
     * @param email The email address
     * @return true if email sent successfully, false otherwise
     */
    boolean sendTicketEmail(String ticketId, String email);
    
    /**
     * Send ticket SMS
     * @param ticketId The ID of the ticket
     * @param phoneNumber The phone number
     * @return true if SMS sent successfully, false otherwise
     */
    boolean sendTicketSMS(String ticketId, String phoneNumber);
    
    // ==================== ADDITIONAL UTILITY METHODS ====================
    
    /**
     * Get ticket details for display
     * @param ticketId The ID of the ticket
     * @return Map containing ticket details
     */
    Map<String, Object> getTicketDetails(String ticketId);
    
    /**
     * Get ticket summary
     * @param ticketId The ID of the ticket
     * @return String containing ticket summary
     */
    String getTicketSummary(String ticketId);
    
    /**
     * Get ticket history
     * @param ticketId The ID of the ticket
     * @return List of ticket history entries
     */
    List<Map<String, Object>> getTicketHistory(String ticketId);
    
    /**
     * Add ticket comment/note
     * @param ticketId The ID of the ticket
     * @param comment The comment to add
     * @return true if successful, false otherwise
     */
    boolean addTicketComment(String ticketId, String comment);
    
    /**
     * Get ticket comments
     * @param ticketId The ID of the ticket
     * @return List of ticket comments
     */
    List<String> getTicketComments(String ticketId);
} 