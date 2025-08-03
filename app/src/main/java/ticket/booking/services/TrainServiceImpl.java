package ticket.booking.services;

import ticket.booking.interfaces.TrainService;
import ticket.booking.entities.Train;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of TrainService interface providing comprehensive train management functionality.
 */
public class TrainServiceImpl implements TrainService {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAINS_FILE_PATH = "app/src/main/resources/trains.json";
    private List<Train> trainList;
    
    public TrainServiceImpl() {
        this.trainList = loadTrainsFromFile();
    }

    private void reloadTrains() {
        this.trainList = loadTrainsFromFile();
    }
    
    // ==================== TRAIN MANAGEMENT ====================
    
    @Override
    public boolean addTrain(Train train) {
        try {
            reloadTrains();
            if (train != null && train.getTrainId() != null) {
                // Check if train already exists
                if (getTrainById(train.getTrainId()) != null) {
                    return false; // Train already exists
                }
                trainList.add(train);
                saveTrainsToFile();
                reloadTrains();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updateTrain(String trainId, Train updatedTrain) {
        try {
            reloadTrains();
            Train existingTrain = getTrainById(trainId);
            if (existingTrain != null && updatedTrain != null) {
                int index = trainList.indexOf(existingTrain);
                updatedTrain.setTrainId(trainId); // Ensure ID remains the same
                trainList.set(index, updatedTrain);
                saveTrainsToFile();
                reloadTrains();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteTrain(String trainId) {
        try {
            reloadTrains();
            Train train = getTrainById(trainId);
            if (train != null) {
                boolean removed = trainList.remove(train);
                if (removed) {
                    saveTrainsToFile();
                    reloadTrains();
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
    public Train getTrainById(String trainId) {
        reloadTrains();
        return trainList.stream()
                .filter(train -> train.getTrainId().equals(trainId))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Train> getAllTrains() {
        reloadTrains();
        return new ArrayList<>(trainList);
    }
    
    // ==================== SEARCH & AVAILABILITY ====================
    
    @Override
    public List<Train> searchTrains(String source, String destination, LocalDate date) {
        reloadTrains();
        return trainList.stream()
                .filter(train -> train.getSource().equalsIgnoreCase(source) &&
                               train.getDestination().equalsIgnoreCase(destination))
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, String> getTrainSchedule(String trainId) {
        Train train = getTrainById(trainId);
        if (train != null) {
            return train.getStationArrivalTimes();
        }
        return new HashMap<>();
    }
    
    @Override
    public int getAvailableSeats(String trainId, LocalDate date, String classType) {
        Train train = getTrainById(trainId);
        if (train != null) {
            // This is a simplified implementation
            // In a real system, you would check actual bookings for the date
            return train.getAvailableSeats();
        }
        return 0;
    }
    
    @Override
    public List<String> getBookedSeats(String trainId, LocalDate date) {
        // This would typically query a booking database
        // For now, return empty list
        return new ArrayList<>();
    }
    
    @Override
    public Map<String, Object> getTrainRoute(String trainId) {
        Train train = getTrainById(trainId);
        Map<String, Object> routeInfo = new HashMap<>();
        
        if (train != null) {
            routeInfo.put("trainId", train.getTrainId());
            routeInfo.put("trainNumber", train.getTrainNumber());
            routeInfo.put("trainName", train.getTrainName());
            routeInfo.put("source", train.getSource());
            routeInfo.put("destination", train.getDestination());
            routeInfo.put("departureTime", train.getDepartureTime());
            routeInfo.put("arrivalTime", train.getArrivalTime());
            routeInfo.put("stations", train.getStations());
            routeInfo.put("stationArrivalTimes", train.getStationArrivalTimes());
        }
        
        return routeInfo;
    }
    
    // ==================== SEAT & COACH HANDLING ====================
    
    @Override
    public List<List<Integer>> getCoachLayout(String trainId) {
        Train train = getTrainById(trainId);
        if (train != null) {
            return train.getSeats();
        }
        return new ArrayList<>();
    }
    
    @Override
    public boolean checkSeatAvailability(String trainId, LocalDate journeyDate, String seatType) {
        // This would check actual seat availability
        // For now, return true if train exists
        return getTrainById(trainId) != null;
    }
    
    @Override
    public List<List<Integer>> getSeatMap(String trainId, LocalDate journeyDate) {
        Train train = getTrainById(trainId);
        if (train != null) {
            return train.getSeats();
        }
        return new ArrayList<>();
    }
    
    // ==================== ADDITIONAL UTILITY METHODS ====================
    
    @Override
    public List<Train> getTrainsByRoute(String source, String destination) {
        return trainList.stream()
                .filter(train -> train.getSource().equalsIgnoreCase(source) &&
                               train.getDestination().equalsIgnoreCase(destination))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Train> getTrainsByType(String trainType) {
        return trainList.stream()
                .filter(train -> train.getTrainType() != null &&
                               train.getTrainType().equalsIgnoreCase(trainType))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Train> getTrainsByDepartureStation(String station) {
        return trainList.stream()
                .filter(train -> train.getSource().equalsIgnoreCase(station))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Train> getTrainsByArrivalStation(String station) {
        return trainList.stream()
                .filter(train -> train.getDestination().equalsIgnoreCase(station))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean updateSeatAvailability(String trainId, String seatNumber, boolean isAvailable) {
        Train train = getTrainById(trainId);
        if (train != null) {
            // This would update the seat availability in the train's seat map
            // For now, just return true
            return true;
        }
        return false;
    }
    
    @Override
    public Map<String, Object> getTrainStatistics(String trainId) {
        Train train = getTrainById(trainId);
        Map<String, Object> stats = new HashMap<>();
        
        if (train != null) {
            stats.put("trainId", train.getTrainId());
            stats.put("trainName", train.getTrainName());
            stats.put("totalSeats", train.getTotalSeats());
            stats.put("availableSeats", train.getAvailableSeats());
            stats.put("occupancyRate", calculateOccupancyRate(train));
            stats.put("route", train.getSource() + " to " + train.getDestination());
            stats.put("trainType", train.getTrainType());
        }
        
        return stats;
    }
    
    // ==================== PRIVATE HELPER METHODS ====================
    
    private List<Train> loadTrainsFromFile() {
        try {
            File file = new File(TRAINS_FILE_PATH);
            if (file.exists()) {
                Map<String, List<Train>> data = objectMapper.readValue(file, 
                    new TypeReference<Map<String, List<Train>>>() {});
                return data.getOrDefault("trains", new ArrayList<>());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    private void saveTrainsToFile() {
        try {
            Map<String, List<Train>> data = new HashMap<>();
            data.put("trains", trainList);
            objectMapper.writeValue(new File(TRAINS_FILE_PATH), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private double calculateOccupancyRate(Train train) {
        if (train.getTotalSeats() > 0) {
            int occupiedSeats = train.getTotalSeats() - train.getAvailableSeats();
            return (double) occupiedSeats / train.getTotalSeats() * 100;
        }
        return 0.0;
    }
} 