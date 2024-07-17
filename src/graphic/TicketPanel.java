package graphic;

import commands.ExitAccount;
import main.client.Client;
import main.client.Requestor;
import ticket.*;
import utils.LanguageChangeListener;
import utils.LanguageManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

public class TicketPanel extends JPanel implements LanguageChangeListener {
    private BufferedImage ticketImage = ImageIO.read(new File("ticket.jpg"));
    private List<Ticket> tickets;
    private Requestor requestor;
    private Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA};
    private int ticketWidth = 200;
    private int ticketHeight = 60;
    private int spacingX = 50;
    private int spacingY = 30;
    private int addedTicket = 0;
    private int animationStep = 0;
    private int animationMaxSteps = 20;
    private boolean isAnimating = false;

    public TicketPanel(List<Ticket> tickets, Requestor requestor) throws IOException {
        setPreferredSize(new Dimension(700, 600));
        this.requestor = requestor;
        setBackground(Color.WHITE);
        this.tickets = tickets;
        setLayout(new BorderLayout());

        // Add language selection combo box
        String[] languages = {"English (Australia)", "Русский", "Македонски", "Polski"};
        JComboBox<String> languageComboBox = new JComboBox<>(languages);
        languageComboBox.setSelectedIndex(getLanguageIndex(LanguageManager.getCurrentLocale()));
        languageComboBox.addActionListener(e -> setLocale((String) languageComboBox.getSelectedItem()));

        add(languageComboBox, BorderLayout.NORTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Ticket ticket = getTicketAt(e.getX(), e.getY());
                if (ticket != null) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        showTicketInfo(ticket);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        showActionDialog(ticket);
                    }
                }
            }
        });

        // Add buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addButton = createButton("Add", e -> addTicket());
        JButton removeButton = createButton("Remove by ID", e -> removeById());
        JButton printTypesButton = createButton("Print Types", e -> printTypes());
        JButton infoButton = createButton("Info", e -> showInfo());
        JButton clearButton = createButton("Clear", e -> clear());
        JButton executeScriptButton = createButton("Execute Script", e -> executeScript());
        JButton shuffleButton = createButton("Shuffle", e -> shuffle());
        JButton helpButton = createButton("Help", e -> help());
        JButton logoutButton = createButton("Logout", e -> logout());

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(printTypesButton);
        buttonPanel.add(infoButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(executeScriptButton);
        buttonPanel.add(shuffleButton);
        buttonPanel.add(helpButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.SOUTH);

        LanguageManager.addLanguageChangeListener(this);
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    actionListener.actionPerformed(new ActionEvent(e.getSource(), e.getID(), e.paramString()));
                }
            }
        });
        return button;
    }

    private void setLocale(String language) {
        switch (language) {
            case "English (Australia)":
                LanguageManager.setLocale(new Locale("en", "AU"));
                break;
            case "Русский":
                LanguageManager.setLocale(new Locale("ru"));
                break;
            case "Македонски":
                LanguageManager.setLocale(new Locale("mk"));
                break;
            case "Polski":
                LanguageManager.setLocale(new Locale("pl"));
                break;
        }
        Client.updateDataForGraphic();
    }

    private int getLanguageIndex(Locale locale) {
        if (locale.equals(new Locale("en", "AU"))) {
            return 0;
        } else if (locale.equals(new Locale("ru"))) {
            return 1;
        } else if (locale.equals(new Locale("mk"))) {
            return 2;
        } else if (locale.equals(new Locale("pl"))) {
            return 3;
        }
        return 0; // default to English (Australia)
    }

    private void editTicket(Ticket ticket) {
        JTextField nameField = new JTextField(ticket.getName());
        JTextField priceField = new JTextField(String.valueOf(ticket.getPrice()));
        JTextField typeField = new JTextField(ticket.getType().toString());
        JTextField xField = new JTextField(String.valueOf(ticket.getCoordinates().getX()));
        JTextField yField = new JTextField(String.valueOf(ticket.getCoordinates().getY()));

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel(LanguageManager.getString("name")));
        panel.add(nameField);
        panel.add(new JLabel(LanguageManager.getString("price")));
        panel.add(priceField);
        panel.add(new JLabel(LanguageManager.getString("type")));
        panel.add(typeField);
        panel.add(new JLabel(LanguageManager.getString("coordinate_x")));
        panel.add(xField);
        panel.add(new JLabel(LanguageManager.getString("coordinate_y")));
        panel.add(yField);

        int result = JOptionPane.showConfirmDialog(this, panel, LanguageManager.getString("edit_ticket"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String newName = nameField.getText().trim();
                long newPrice = Long.parseLong(priceField.getText().trim());
                TicketType newType = TicketType.valueOf(typeField.getText().trim());
                int newX = Integer.parseInt(xField.getText().trim());
                int newY = Integer.parseInt(yField.getText().trim());
                Ticket ticket1 = new Ticket(ticket.getId(), newName, new Coordinates(newX, newY), ticket.getCreationDate(), newPrice, newType, ticket.getVenue());
                String[] command = new String[]{"update", String.valueOf(ticket1.getId())};
                String answer = requestor.queryForGraphic(Client.getCurrentUser(), command, ticket1);
                JOptionPane.showMessageDialog(this, answer, LanguageManager.getString("result_of_operation"), JOptionPane.INFORMATION_MESSAGE);
                Client.updateDataForGraphic();
            } catch (IllegalArgumentException | IOException exception) {
                JOptionPane.showMessageDialog(this, LanguageManager.getString("invalid_data"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showActionDialog(Ticket ticket) {
        String[] options = {LanguageManager.getString("edit"), LanguageManager.getString("delete"), LanguageManager.getString("cancel")};
        int choice = JOptionPane.showOptionDialog(this, LanguageManager.getString("ticket_action"),
                LanguageManager.getString("ticket_action"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[2]);

        if (choice == 0) {
            editTicket(ticket);
        } else if (choice == 1) {
            deleteTicket(ticket);
        }
    }

    private void deleteTicket(Ticket ticket) {
        int confirm = JOptionPane.showConfirmDialog(this, LanguageManager.getString("confirm_deletion"),
                LanguageManager.getString("confirm_deletion"), JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String[] command = new String[]{"removeById", String.valueOf(ticket.getId())};
            try {
                String answer = requestor.queryForGraphic(Client.getCurrentUser(), command, null);
                JOptionPane.showMessageDialog(this, answer);
                Client.updateDataForGraphic();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateTableData(List<Ticket> newCollection) {
        this.tickets = newCollection;
        repaint();
    }

    private Ticket getTicketAt(int x, int y) {
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            int ticketX = (int) ticket.getCoordinates().getX();
            int ticketY = ticket.getCoordinates().getY();
            int scaledX = (int) (ticketX * (getWidth() / 100.0));
            int scaledY = (int) (ticketY * (getHeight() / 100.0));
            if (x >= scaledX && x <= scaledX + ticketWidth && y >= scaledY && y <= scaledY + ticketHeight) {
                return ticket;
            }
        }
        return null;
    }

    private void showTicketInfo(Ticket ticket) {
        JOptionPane.showMessageDialog(this, LanguageManager.getString("ticket_info") + ":\n" +
                "ID: " + ticket.getId() + "\n" +
                LanguageManager.getString("name") + ": " + ticket.getName() + "\n" +
                LanguageManager.getString("price") + ": " + ticket.getPrice() + "\n" +
                LanguageManager.getString("type") + ": " + ticket.getType() + "\n" +
                "Venue: " + ticket.getVenue().getName() + "\n" +
                "Owner: " + ticket.getOwner());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            int colorIndex = Math.abs(ticket.getOwner().hashCode()) % colors.length;
            g.setColor(colors[colorIndex]);

            // Получение координат из билета
            int ticketX = (int) ticket.getCoordinates().getX();
            int ticketY = ticket.getCoordinates().getY();

            // Масштабирование координат для соответствия размеру панели
            int scaledX = (int) (ticketX * (getWidth() / 100.0));
            int scaledY = (int) (ticketY * (getHeight() / 100.0));

            if (isAnimating && i == tickets.size() - 1) {
                double scale = (double) animationStep / animationMaxSteps; // Scale from 0 to 1
                int width = (int) (ticketWidth * scale);
                int height = (int) (ticketHeight * scale);
                g.drawImage(ticketImage, scaledX, scaledY, width, height, this);
            } else {
                g.drawImage(ticketImage, scaledX, scaledY, ticketWidth, ticketHeight, this);
            }

            g.setColor(Color.BLACK);
            g.drawString(LanguageManager.getString("ticket_number") + " " + ticket.getId(), scaledX + 50, scaledY - 5);
        }
        g.drawString(LanguageManager.getString("current_user") + " " + Client.getCurrentUser(), 20, 500);
    }

    @Override
    public void onLanguageChange(Locale newLocale) {
        repaint();
    }

    // Methods for button actions
    private void addTicket() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        String[] ticketTypes = {"VIP", "USUAL", "BUDGETARY", "CHEAP"};
        JComboBox<String> typeComboBox = new JComboBox<>(ticketTypes);
        JTextField xField = new JTextField();
        JTextField yField = new JTextField();
        JTextField venueNameField = new JTextField();
        JTextField venueCapacityField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel(LanguageManager.getString("name")));
        panel.add(nameField);
        panel.add(new JLabel(LanguageManager.getString("price")));
        panel.add(priceField);
        panel.add(new JLabel(LanguageManager.getString("type")));
        panel.add(typeComboBox);
        panel.add(new JLabel(LanguageManager.getString("coordinate_x")));
        panel.add(xField);
        panel.add(new JLabel(LanguageManager.getString("coordinate_y")));
        panel.add(yField);
        panel.add(new JLabel(LanguageManager.getString("venue_name")));
        panel.add(venueNameField);
        panel.add(new JLabel(LanguageManager.getString("venue_capacity")));
        panel.add(venueCapacityField);

        int result = JOptionPane.showConfirmDialog(this, panel, LanguageManager.getString("add_ticket"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String newName = nameField.getText().trim();
                long newPrice = Long.parseLong(priceField.getText().trim());
                TicketType newType = TicketType.valueOf(typeComboBox.getSelectedItem().toString());
                int newX = Integer.parseInt(xField.getText().trim());
                int newY = Integer.parseInt(yField.getText().trim());
                String venueName = venueNameField.getText().trim();
                int venueCapacity = Integer.parseInt(venueCapacityField.getText().trim());

                Coordinates coordinates = new Coordinates(newX, newY);
                Venue venue = new Venue(0L, venueName, venueCapacity, null);
                Ticket newTicket = new Ticket(0, newName, coordinates, ZonedDateTime.now(), newPrice, newType, venue);
                newTicket.setOwner(Client.getCurrentUser());
                String[] command = new String[]{"add"};
                String answer = requestor.queryForGraphic(Client.getCurrentUser(), command, newTicket);
                JOptionPane.showMessageDialog(this, answer, LanguageManager.getString("result_of_operation"), JOptionPane.INFORMATION_MESSAGE);
                Client.updateDataForGraphic();
                animateLastTicket();
            } catch (IllegalArgumentException | IOException exception) {
                JOptionPane.showMessageDialog(this, LanguageManager.getString("invalid_data"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeById() {
        // Запросить у пользователя ID билета
        String idStr = JOptionPane.showInputDialog(this, LanguageManager.getString("enter_ticket_id"), LanguageManager.getString("remove_ticket"), JOptionPane.PLAIN_MESSAGE);
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                long ticketId = Long.parseLong(idStr.trim());
                int confirm = JOptionPane.showConfirmDialog(this, LanguageManager.getString("confirm_deletion"), LanguageManager.getString("confirm_deletion"), JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String[] command = new String[]{"removeById", String.valueOf(ticketId)};
                    try {
                        String answer = requestor.queryForGraphic(Client.getCurrentUser(), command, null);
                        JOptionPane.showMessageDialog(this, answer, LanguageManager.getString("result_of_operation"), JOptionPane.INFORMATION_MESSAGE);
                        Client.updateDataForGraphic();
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, LanguageManager.getString("operation_failed"), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, LanguageManager.getString("invalid_id"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void printTypes() {
        String[] command = new String[]{"printTypes"};
        try {
            String answer = requestor.queryForGraphic(Client.getCurrentUser(), command, null);
            JOptionPane.showMessageDialog(this, answer, LanguageManager.getString("result_of_operation"), JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, LanguageManager.getString("operation_failed"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showInfo() {
        String[] command = new String[]{"info"};
        try {
            String answer = requestor.queryForGraphic(Client.getCurrentUser(), command, null);
            JOptionPane.showMessageDialog(this, answer, LanguageManager.getString("result_of_operation"), JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, LanguageManager.getString("operation_failed"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clear() {
        String[] command = new String[]{"clear"};
        int confirm = JOptionPane.showConfirmDialog(this, LanguageManager.getString("confirm_clear"), LanguageManager.getString("clear_tickets"), JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String answer = requestor.queryForGraphic(Client.getCurrentUser(), command, null);
                JOptionPane.showMessageDialog(this, answer, LanguageManager.getString("result_of_operation"), JOptionPane.INFORMATION_MESSAGE);
                Client.updateDataForGraphic();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, LanguageManager.getString("operation_failed"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void executeScript() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(LanguageManager.getString("select_script_file"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null) {
                String filePath = selectedFile.getAbsolutePath();
                String[] command = new String[]{"executeScript", filePath};
                try {
                    String answer = requestor.queryForGraphic(Client.getCurrentUser(), command, null);
                    JOptionPane.showMessageDialog(this, answer, LanguageManager.getString("result_of_operation"), JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, LanguageManager.getString("operation_failed"), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void shuffle() {
        String[] command = new String[]{"shuffle"};
        try {
            String answer = requestor.queryForGraphic(Client.getCurrentUser(), command, null);
            JOptionPane.showMessageDialog(this, answer, LanguageManager.getString("result_of_operation"), JOptionPane.INFORMATION_MESSAGE);
            Client.updateDataForGraphic();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, LanguageManager.getString("operation_failed"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void help() {
        String[] command = new String[]{"help"};
        try {
            String answer = requestor.queryForGraphic(Client.getCurrentUser(), command, null);
            JOptionPane.showMessageDialog(this, answer, LanguageManager.getString("result_of_operation"), JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, LanguageManager.getString("operation_failed"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Client.logout();
        }
    }

    private void animateLastTicket() {
        isAnimating = true;
        animationStep = 0;

        Timer timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationStep++;
                if (animationStep <= animationMaxSteps) {
                    repaint(); // Repaint the panel to show the animation step
                } else {
                    ((Timer) e.getSource()).stop();
                    isAnimating = false;
                    repaint(); // Final repaint to ensure the final state is shown
                }
            }
        });

        timer.start();
    }
}
