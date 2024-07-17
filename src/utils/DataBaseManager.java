package utils;

import ticket.Ticket;

import java.sql.*;

public class DataBaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/studs";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private static Connection connection;

    public static String[] addTicket(Ticket ticket) throws SQLException {
        String sqlTicket = "INSERT INTO Ticket (name, coordinates_id, creation_Date, price, type, venue_id,owner) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlCoordinates = "INSERT INTO Coordinates (x, y) VALUES (?, ?) RETURNING id";
        String sqlVenue = "INSERT INTO Venue (name, capacity, address_id) VALUES (?, ?, ?) RETURNING id";
        String sqlAddress = "INSERT INTO Address (street, zip_Code) VALUES (?, ?) RETURNING id";
        try (PreparedStatement addressStmt = connection.prepareStatement(sqlAddress, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement venueStmt = connection.prepareStatement(sqlVenue, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement coordinatesStmt = connection.prepareStatement(sqlCoordinates, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement ticketStmt = connection.prepareStatement(sqlTicket, Statement.RETURN_GENERATED_KEYS)) {
            Long addressId = null;
            if (ticket.getVenue().getAddress() != null) {
                addressStmt.setString(1, ticket.getVenue().getAddress().getStreet());
                addressStmt.setString(2, ticket.getVenue().getAddress().getZipCode());
                addressStmt.executeUpdate();
                addressId = getGeneratedKey(addressStmt);
            }
            venueStmt.setString(1, ticket.getVenue().getName());
            venueStmt.setInt(2, (int) ticket.getVenue().getCapacity());
            if (addressId != null) {
                venueStmt.setLong(3, addressId);
            } else {
                venueStmt.setNull(3, Types.INTEGER);
            }
            venueStmt.executeUpdate();
            long venueId = getGeneratedKey(venueStmt);
            coordinatesStmt.setLong(1, ticket.getCoordinates().getX());
            coordinatesStmt.setInt(2, ticket.getCoordinates().getY());
            coordinatesStmt.executeUpdate();
            long coordinatesId = getGeneratedKey(coordinatesStmt);
            ticketStmt.setString(1, ticket.getName());
            ticketStmt.setLong(2, coordinatesId);
            ticketStmt.setTimestamp(3, Timestamp.valueOf(ticket.getCreationDate().toLocalDateTime()));
            if (ticket.getPrice() != null) {
                ticketStmt.setLong(4, ticket.getPrice());
            } else {
                ticketStmt.setNull(4, java.sql.Types.BIGINT);
            }
            if (ticket.getType() != null) {
                ticketStmt.setString(5, ticket.getType().name());
            } else {
                ticketStmt.setNull(5, java.sql.Types.VARCHAR);
            }
            ticketStmt.setLong(6, venueId);
            ticketStmt.setString(7, ticket.getOwner());
            ticketStmt.executeUpdate();
            long ticketId = getGeneratedKey(ticketStmt);
            return new String[]{String.valueOf(venueId), String.valueOf(ticketId)};
        }
    }

    public static void startConnection() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static long getGeneratedKey(PreparedStatement stmt) throws SQLException {
        try (var rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new SQLException("No ID obtained.");
            }
        }
    }

    public static void deleteAllTickets(String user) throws SQLException, ClassNotFoundException {

        String selectTicketsQuery = "SELECT id, coordinates_id, venue_id FROM ticket WHERE owner = ?";
        String selectVenueQuery = "SELECT address_id FROM venue WHERE id = ?";
        String deleteTicketQuery = "DELETE FROM ticket WHERE id = ?";
        String deleteCoordinatesQuery = "DELETE FROM coordinates WHERE id = ?";
        String deleteVenueQuery = "DELETE FROM venue WHERE id = ?";
        String deleteAddressQuery = "DELETE FROM address WHERE id = ?";
        PreparedStatement selectTicketsStmt = connection.prepareStatement(selectTicketsQuery);
        selectTicketsStmt.setString(1, user);
        ResultSet ticketResultSet = selectTicketsStmt.executeQuery();

        try {
            while (ticketResultSet.next()) {
                long ticketId = ticketResultSet.getLong("id");
                long coordinatesId = ticketResultSet.getLong("coordinates_id");
                long venueId = ticketResultSet.getLong("venue_id");

                // Получаем данные о venue
                PreparedStatement selectVenueStmt = connection.prepareStatement(selectVenueQuery);
                selectVenueStmt.setLong(1, venueId);
                ResultSet venueResultSet = selectVenueStmt.executeQuery();

                Long addressId = null;
                if (venueResultSet.next()) {
                    addressId = venueResultSet.getLong("address_id");
                }

                // Удаляем билет
                PreparedStatement deleteTicketStmt = connection.prepareStatement(deleteTicketQuery);
                deleteTicketStmt.setLong(1, ticketId);
                deleteTicketStmt.executeUpdate();

                // Удаляем координаты
                PreparedStatement deleteCoordinatesStmt = connection.prepareStatement(deleteCoordinatesQuery);
                deleteCoordinatesStmt.setLong(1, coordinatesId);
                deleteCoordinatesStmt.executeUpdate();

                // Удаляем venue
                PreparedStatement deleteVenueStmt = connection.prepareStatement(deleteVenueQuery);
                deleteVenueStmt.setLong(1, venueId);
                deleteVenueStmt.executeUpdate();

                // Удаляем адрес, если он существует
                if (addressId != null) {
                    PreparedStatement deleteAddressStmt = connection.prepareStatement(deleteAddressQuery);
                    deleteAddressStmt.setLong(1, addressId);
                    deleteAddressStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {

        }
    }


    public static void addUser(String login, String password) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO users (login, password) VALUES (?, ?)";
        startConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, login);
        pstmt.setString(2, password);
        pstmt.executeUpdate();
    }

    public static boolean validateUser(String login, String password) throws SQLException, ClassNotFoundException {
        String sql = "SELECT password FROM users WHERE login = ?";
        startConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, login);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            String storedPassword = rs.getString("password");
            return storedPassword.equals(password);
        }
        return false;
    }

    public static void deleteTicket(Integer id) throws SQLException {
        startConnection();
        String selectTicketQuery = "SELECT coordinates_id, venue_id FROM ticket WHERE id = ?";
        String selectVenueQuery = "SELECT address_id FROM venue WHERE id = ?";
        String deleteCoordinatesQuery = "DELETE FROM coordinates WHERE id = ?";
        String deleteVenueQuery = "DELETE FROM venue WHERE id = ?";
        String deleteAddressQuery = "DELETE FROM address WHERE id = ?";

        PreparedStatement selectTicketStmt = connection.prepareStatement(selectTicketQuery);
        selectTicketStmt.setLong(1, id);
        ResultSet ticketResultSet = selectTicketStmt.executeQuery();
        if (ticketResultSet.next()) {
            long coordinatesId = ticketResultSet.getLong("coordinates_id");
            long venueId = ticketResultSet.getLong("venue_id");

            // Получаем данные о venue
            PreparedStatement selectVenueStmt = connection.prepareStatement(selectVenueQuery);
            selectVenueStmt.setLong(1, venueId);
            ResultSet venueResultSet = selectVenueStmt.executeQuery();

            Long addressId = null;
            if (venueResultSet.next()) {
                addressId = venueResultSet.getLong("address_id");
            }
            PreparedStatement deleteCoordinatesStmt = connection.prepareStatement(deleteCoordinatesQuery);
            deleteCoordinatesStmt.setLong(1, coordinatesId);
            deleteCoordinatesStmt.executeUpdate();

            PreparedStatement deleteVenueStmt = connection.prepareStatement(deleteVenueQuery);
            deleteVenueStmt.setLong(1, venueId);
            deleteVenueStmt.executeUpdate();

            if (addressId != null) {
                PreparedStatement deleteAddressStmt = connection.prepareStatement(deleteAddressQuery);
                deleteAddressStmt.setLong(1, addressId);
                deleteAddressStmt.executeUpdate();
            }
        }
    }

    public static void updateTicket(long ticketId, Ticket ticket) throws SQLException {
        String sqlFindTicket = "SELECT coordinates_id, venue_id FROM Ticket WHERE id = ?";
        String sqlFindVenue = "SELECT address_id FROM Venue WHERE id = ?";
        String sqlUpdateTicket = "UPDATE Ticket SET name = ?, coordinates_id = ?, creation_Date = ?, price = ?, type = ?, venue_id = ?, owner = ? WHERE id = ?";
        String sqlUpdateCoordinates = "UPDATE Coordinates SET x = ?, y = ? WHERE id = ?";
        String sqlUpdateVenue = "UPDATE Venue SET name = ?, capacity = ?, address_id = ? WHERE id = ?";
        String sqlUpdateAddress = "UPDATE Address SET street = ?, zip_Code = ? WHERE id = ?";
        String sqlInsertAddress = "INSERT INTO Address (street, zip_Code) VALUES (?, ?) RETURNING id";

        Long coordinatesId = null;
        Long venueId = null;
        Long addressId = null;

        // Step 1: Find coordinates_id and venue_id for the given ticket id
        try (PreparedStatement findTicketStmt = connection.prepareStatement(sqlFindTicket);
             PreparedStatement findVenueStmt = connection.prepareStatement(sqlFindVenue)) {

            findTicketStmt.setLong(1, ticketId);
            ResultSet ticketRs = findTicketStmt.executeQuery();
            if (ticketRs.next()) {
                coordinatesId = ticketRs.getLong("coordinates_id");
                venueId = ticketRs.getLong("venue_id");
            }

            // Step 2: Find address_id for the given venue id
            if (venueId != null) {
                findVenueStmt.setLong(1, venueId);
                ResultSet venueRs = findVenueStmt.executeQuery();
                if (venueRs.next()) {
                    addressId = venueRs.getLong("address_id");
                }
            }
        }

        // Step 3: Update or insert address if exists
        if (ticket.getVenue().getAddress() != null) {
            if (addressId != null) {
                // Update existing address
                try (PreparedStatement updateAddressStmt = connection.prepareStatement(sqlUpdateAddress)) {
                    updateAddressStmt.setString(1, ticket.getVenue().getAddress().getStreet());
                    updateAddressStmt.setString(2, ticket.getVenue().getAddress().getZipCode());
                    updateAddressStmt.setLong(3, addressId);
                    updateAddressStmt.executeUpdate();
                }
            } else {
                // Insert new address
                try (PreparedStatement insertAddressStmt = connection.prepareStatement(sqlInsertAddress, Statement.RETURN_GENERATED_KEYS)) {
                    insertAddressStmt.setString(1, ticket.getVenue().getAddress().getStreet());
                    insertAddressStmt.setString(2, ticket.getVenue().getAddress().getZipCode());
                    insertAddressStmt.executeUpdate();
                    try (ResultSet generatedKeys = insertAddressStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            addressId = generatedKeys.getLong(1);
                        }
                    }
                }
            }
        }
        if (addressId == 0){
            addressId = null;
        }
        // Step 4: Update venue
        try (PreparedStatement updateVenueStmt = connection.prepareStatement(sqlUpdateVenue)) {
            updateVenueStmt.setString(1, ticket.getVenue().getName());
            updateVenueStmt.setInt(2, (int) ticket.getVenue().getCapacity());
            if (addressId != null) {
                updateVenueStmt.setLong(3, addressId);
            } else {
                updateVenueStmt.setNull(3, Types.INTEGER);
            }
            updateVenueStmt.setLong(4, venueId);
            updateVenueStmt.executeUpdate();
        }

        // Step 5: Update coordinates
        try (PreparedStatement updateCoordinatesStmt = connection.prepareStatement(sqlUpdateCoordinates)) {
            updateCoordinatesStmt.setLong(1, ticket.getCoordinates().getX());
            updateCoordinatesStmt.setInt(2, ticket.getCoordinates().getY());
            updateCoordinatesStmt.setLong(3, coordinatesId);
            updateCoordinatesStmt.executeUpdate();
        }

        // Step 6: Update ticket
        try (PreparedStatement updateTicketStmt = connection.prepareStatement(sqlUpdateTicket)) {
            updateTicketStmt.setString(1, ticket.getName());
            updateTicketStmt.setLong(2, coordinatesId);
            updateTicketStmt.setTimestamp(3, Timestamp.valueOf(ticket.getCreationDate().toLocalDateTime()));
            if (ticket.getPrice() != null) {
                updateTicketStmt.setLong(4, ticket.getPrice());
            } else {
                updateTicketStmt.setNull(4, java.sql.Types.BIGINT);
            }
            if (ticket.getType() != null) {
                updateTicketStmt.setString(5, ticket.getType().name());
            } else {
                updateTicketStmt.setNull(5, java.sql.Types.VARCHAR);
            }
            updateTicketStmt.setLong(6, venueId);
            updateTicketStmt.setString(7, ticket.getOwner());
            updateTicketStmt.setLong(8, ticketId);
            updateTicketStmt.executeUpdate();
        }
    }



    public static Connection getConnection() {
        return connection;
    }


}
