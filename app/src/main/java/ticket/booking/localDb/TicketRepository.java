package ticket.booking.localDb;

import ticket.booking.entities.Ticket;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistent storage and retrieval of Ticket objects from tickets.json
 */
public class TicketRepository {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TICKETS_FILE_PATH = "app/src/main/resources/tickets.json";

    public static List<Ticket> loadTicketsFromFile() {
        try {
            File file = new File(TICKETS_FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Ticket>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveTicketsToFile(List<Ticket> tickets) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(TICKETS_FILE_PATH), tickets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
