package ticket.booking.interfaces;

import ticket.booking.entities.Train;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing train operations including CRUD operations,
 * search functionality, availability checks, and seat management.
 */
public interface TrainService {
    
    // ==================== TRAIN MANAGEMENT ====================
    
    /**
     * Add a new train to the system
     * @param train The train object to add
     * @return true if successful, false otherwise
     */
    boolean addTrain(Train train);
    
    /**
     * Update an existing train's information
     * @param trainId The ID of the train to update
     * @param updatedTrain The updated train object
     * @return true if successful, false otherwise
     */
    boolean updateTrain(String trainId, Train updatedTrain);
    
    /**
     * Delete a train from the system
     * @param trainId The ID of the train to delete
     * @return true if successful, false otherwise
     */
    boolean deleteTrain(String trainId);
    
    /**
     * Get a train by its ID
     * @param trainId The ID of the train
     * @return The train object, or null if not found
     */
    Train getTrainById(String trainId);
    
    /**
     * Get all trains in the system
     * @return List of all trains
     */
    List<Train> getAllTrains();
    
    // ==================== SEARCH & AVAILABILITY ====================
    
    /**
     * Search for trains between source and destination on a specific date
     * @param source The departure station
     * @param destination The arrival station
     * @param date The date of travel
     * @return List of available trains
     */
    List<Train> searchTrains(String source, String destination, LocalDate date);
    
    /**
     * Get the schedule for a specific train
     * @param trainId The ID of the train
     * @return Map of station names to arrival times
     */
    Map<String, String> getTrainSchedule(String trainId);
    
    /**
     * Get available seats for a train on a specific date and class type
     * @param trainId The ID of the train
     * @param date The date of travel
     * @param classType The class type (ECONOMY, BUSINESS, FIRST)
     * @return Number of available seats
     */
    int getAvailableSeats(String trainId, LocalDate date, String classType);
    
    /**
     * Get booked seats for a train on a specific date
     * @param trainId The ID of the train
     * @param date The date of travel
     * @return List of booked seat numbers
     */
    List<String> getBookedSeats(String trainId, LocalDate date);
    
    /**
     * Get the complete route information for a train
     * @param trainId The ID of the train
     * @return Map containing route details
     */
    Map<String, Object> getTrainRoute(String trainId);
    
    // ==================== SEAT & COACH HANDLING ====================
    
    /**
     * Get the coach layout for a train
     * @param trainId The ID of the train
     * @return 2D array representing seat layout
     */
    List<List<Integer>> getCoachLayout(String trainId);
    
    /**
     * Check availability of a specific seat type
     * @param trainId The ID of the train
     * @param journeyDate The date of journey
     * @param seatType The type of seat (WINDOW, AISLE, etc.)
     * @return true if available, false otherwise
     */
    boolean checkSeatAvailability(String trainId, LocalDate journeyDate, String seatType);
    
    /**
     * Get the seat map showing available and booked seats
     * @param trainId The ID of the train
     * @param journeyDate The date of journey
     * @return 2D array representing seat map (1=available, 0=booked)
     */
    List<List<Integer>> getSeatMap(String trainId, LocalDate journeyDate);
    
    // ==================== ADDITIONAL UTILITY METHODS ====================
    
    /**
     * Get trains by route (source to destination)
     * @param source The departure station
     * @param destination The arrival station
     * @return List of trains on this route
     */
    List<Train> getTrainsByRoute(String source, String destination);
    
    /**
     * Get trains by type (EXPRESS, REGIONAL, etc.)
     * @param trainType The type of train
     * @return List of trains of the specified type
     */
    List<Train> getTrainsByType(String trainType);
    
    /**
     * Get trains departing from a specific station
     * @param station The station name
     * @return List of trains departing from the station
     */
    List<Train> getTrainsByDepartureStation(String station);
    
    /**
     * Get trains arriving at a specific station
     * @param station The station name
     * @return List of trains arriving at the station
     */
    List<Train> getTrainsByArrivalStation(String station);
    
    /**
     * Update seat availability for a train
     * @param trainId The ID of the train
     * @param seatNumber The seat number
     * @param isAvailable Whether the seat is available
     * @return true if successful, false otherwise
     */
    boolean updateSeatAvailability(String trainId, String seatNumber, boolean isAvailable);
    
    /**
     * Get train statistics
     * @param trainId The ID of the train
     * @return Map containing train statistics
     */
    Map<String, Object> getTrainStatistics(String trainId);
} 