package graphic;

import main.client.Client;
import main.client.Requestor;
import ticket.Coordinates;
import ticket.Ticket;
import ticket.TicketType;
import utils.LanguageChangeListener;
import utils.LanguageManager;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

class TicketTableForm extends AbstractTableModel {
    protected List<Ticket> tickets;
    private List<Ticket> filteredTickets;

    public TicketTableForm(List<Ticket> tickets) {
        this.tickets = tickets;
        this.filteredTickets = tickets;
    }

    @Override
    public int getRowCount() {
        return filteredTickets.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return LanguageManager.getString("ID");
            case 1:
                return LanguageManager.getString("Name");
            case 2:
                return LanguageManager.getString("Coordinates");
            case 3:
                return LanguageManager.getString("Creation_Date");
            case 4:
                return LanguageManager.getString("Price");
            case 5:
                return LanguageManager.getString("Type");
            case 6:
                return LanguageManager.getString("Venue");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ticket ticket = filteredTickets.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return ticket.getId();
            case 1:
                return ticket.getName();
            case 2:
                return ticket.getCoordinates();
            case 3:
                return ticket.getCreationDate();
            case 4:
                return ticket.getPrice();
            case 5:
                return ticket.getType();
            case 6:
                return ticket.getVenue().getName();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Long.class;
            case 3:
                return ZonedDateTime.class;
            case 4:
                return Long.class;
            default:
                return String.class;
        }
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
        this.filteredTickets = tickets;
        fireTableDataChanged();
    }

    public void filterTickets(String query) {
        filteredTickets = tickets.stream()
                .filter(ticket -> ticket.getName().toLowerCase().contains(query.toLowerCase()) ||
                        ticket.getCoordinates().toString().toLowerCase().contains(query.toLowerCase()) ||
                        ticket.getVenue().getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        fireTableDataChanged();
    }

    public void sortTickets(int columnIndex, boolean ascending) {
        Comparator<Ticket> comparator;
        switch (columnIndex) {
            case 0:
                comparator = Comparator.comparing(Ticket::getId);
                break;
            case 1:
                comparator = Comparator.comparing(Ticket::getName);
                break;
            case 2:
                comparator = Comparator.comparing(ticket -> ticket.getCoordinates().toString());
                break;
            case 3:
                comparator = Comparator.comparing(Ticket::getCreationDate);
                break;
            case 4:
                comparator = Comparator.comparing(Ticket::getPrice);
                break;
            case 5:
                comparator = Comparator.comparing(Ticket::getType);
                break;
            case 6:
                comparator = Comparator.comparing(ticket -> ticket.getVenue().getName());
                break;
            default:
                return;
        }
        if (!ascending) {
            comparator = comparator.reversed();
        }
        filteredTickets = filteredTickets.stream().sorted(comparator).collect(Collectors.toList());
        fireTableDataChanged();
    }
}


public class TicketTable extends JFrame implements LanguageChangeListener {
    private TicketTableForm model;
    private Requestor requestor;
    private JTable table;
    private JTextField filterTextField;

    public TicketTable(List<Ticket> collection, Requestor requestor) {
        this.requestor = requestor;
        model = new TicketTableForm(collection);
        table = new JTable(model);
        TableRowSorter<TicketTableForm> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        filterTextField = new JTextField();
        filterTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = filterTextField.getText();
                model.filterTickets(text);
            }
        });

        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                boolean ascending = true;
                if (table.getRowSorter().getSortKeys().size() > 0 &&
                        table.getRowSorter().getSortKeys().get(0).getColumn() == col) {
                    ascending = table.getRowSorter().getSortKeys().get(0).getSortOrder() == SortOrder.DESCENDING;
                }
                model.sortTickets(col, ascending);
            }
        });

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(filterTextField, BorderLayout.NORTH);

        setTitle(LanguageManager.getString("title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        showActionDialog(table, row);
                    } else {
                        table.clearSelection();
                    }
                }
            }
        });

        LanguageManager.addLanguageChangeListener(this);
    }

    private void showActionDialog(JTable table, int row) {
        String[] options = {LanguageManager.getString("edit"), LanguageManager.getString("delete"), LanguageManager.getString("cancel")};
        int choice = JOptionPane.showOptionDialog(this, LanguageManager.getString("ticket_action"),
                LanguageManager.getString("ticket_action"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[2]);

        if (choice == 0) {
            editTicket(row);
        } else if (choice == 1) {
            deleteTicket(row);
        }
    }

    private void editTicket(int row) {
        Ticket ticket = model.tickets.get(row);
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
                try {
                    String answer = requestor.queryForGraphic(Client.getCurrentUser(), command, ticket1);
                    JOptionPane.showMessageDialog(this, answer, LanguageManager.getString("result_of_operation"), JOptionPane.INFORMATION_MESSAGE);
                    Client.updateDataForGraphic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, LanguageManager.getString("invalid_data"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteTicket(int row) {
        Ticket ticket = model.tickets.get(row);

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
        model.setTickets(newCollection);
    }

    @Override
    public void onLanguageChange(Locale newLocale) {
        setTitle(LanguageManager.getString("title"));
        model.fireTableStructureChanged();
    }
}

