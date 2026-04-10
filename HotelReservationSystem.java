import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class HotelReservationSystem extends JFrame {

    // Room Class
    static class Room {
        int roomNumber;
        String category;
        double price;
        boolean isAvailable;

        Room(int roomNumber, String category, double price) {
            this.roomNumber = roomNumber;
            this.category = category;
            this.price = price;
            this.isAvailable = true;
        }
    }

    // Reservation Class
    static class Reservation {
        String customerName;
        int roomNumber;
        String category;
        double amount;

        Reservation(String customerName, int roomNumber,
                    String category, double amount) {
            this.customerName = customerName;
            this.roomNumber = roomNumber;
            this.category = category;
            this.amount = amount;
        }

        public String toFileString() {
            return customerName + "," + roomNumber + "," +
                    category + "," + amount;
        }
    }

    // Data Structures
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();
    private final String FILE_NAME = "bookings.txt";

    // GUI Components
    private JTable table;
    private DefaultTableModel model;
    private JTextField nameField, roomField;

    // Constructor
    public HotelReservationSystem() {
        setTitle("🏨 Hotel Reservation System");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeRooms();
        loadReservations();
        createGUI();
    }

    // Initialize Rooms
    private void initializeRooms() {
        rooms.add(new Room(101, "Standard", 2000));
        rooms.add(new Room(102, "Standard", 2000));
        rooms.add(new Room(201, "Deluxe", 3500));
        rooms.add(new Room(202, "Deluxe", 3500));
        rooms.add(new Room(301, "Suite", 5000));
    }

    // Create GUI
    private void createGUI() {
        setLayout(new BorderLayout());

        // Header
        JLabel title = new JLabel("Hotel Reservation System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 153));
        title.setPreferredSize(new Dimension(100, 50));
        add(title, BorderLayout.NORTH);

        // Table
        String[] columns = {"Room No", "Category", "Price", "Available"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Reservation Details"));

        inputPanel.add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Room Number:"));
        roomField = new JTextField();
        inputPanel.add(roomField);

        add(inputPanel, BorderLayout.WEST);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton searchBtn = new JButton("Search Rooms");
        JButton bookBtn = new JButton("Book Room");
        JButton cancelBtn = new JButton("Cancel Reservation");
        JButton viewBtn = new JButton("View Bookings");
        JButton exitBtn = new JButton("Exit");

        buttonPanel.add(searchBtn);
        buttonPanel.add(bookBtn);
        buttonPanel.add(cancelBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(exitBtn);

        add(buttonPanel, BorderLayout.EAST);

        // Button Actions
        searchBtn.addActionListener(e -> refreshTable());
        bookBtn.addActionListener(e -> bookRoom());
        cancelBtn.addActionListener(e -> cancelReservation());
        viewBtn.addActionListener(e -> viewReservations());
        exitBtn.addActionListener(e -> System.exit(0));
    }

    // Refresh Table
    private void refreshTable() {
        model.setRowCount(0);
        for (Room room : rooms) {
            model.addRow(new Object[]{
                    room.roomNumber,
                    room.category,
                    "₹" + room.price,
                    room.isAvailable ? "Yes" : "No"
            });
        }
    }

    // Book Room
    private void bookRoom() {
        try {
            String name = nameField.getText().trim();
            int roomNo = Integer.parseInt(roomField.getText().trim());

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter customer name!");
                return;
            }

            for (Room room : rooms) {
                if (room.roomNumber == roomNo && room.isAvailable) {
                    room.isAvailable = false;

                    double amount = room.price;
                    JOptionPane.showMessageDialog(this,
                            "Payment of ₹" + amount + " Successful!");

                    Reservation res = new Reservation(
                            name, roomNo, room.category, amount);

                    reservations.add(res);
                    saveReservation(res);
                    refreshTable();

                    JOptionPane.showMessageDialog(this,
                            "Room Booked Successfully!");
                    clearFields();
                    return;
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Room not available!", "Error",
                    JOptionPane.ERROR_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Enter a valid room number!");
        }
    }

    // Cancel Reservation
    private void cancelReservation() {
        try {
            int roomNo = Integer.parseInt(roomField.getText().trim());

            for (Reservation res : reservations) {
                if (res.roomNumber == roomNo) {
                    reservations.remove(res);

                    for (Room room : rooms) {
                        if (room.roomNumber == roomNo) {
                            room.isAvailable = true;
                        }
                    }

                    updateFile();
                    refreshTable();
                    JOptionPane.showMessageDialog(this,
                            "Reservation Cancelled!");
                    clearFields();
                    return;
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Reservation not found!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Enter a valid room number!");
        }
    }

    // View Reservations
    private void viewReservations() {
        if (reservations.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No Reservations Found.");
            return;
        }

        StringBuilder sb = new StringBuilder("Booking Details:\n\n");
        for (Reservation res : reservations) {
            sb.append("Customer: ").append(res.customerName)
              .append("\nRoom No: ").append(res.roomNumber)
              .append("\nCategory: ").append(res.category)
              .append("\nAmount: ₹").append(res.amount)
              .append("\n----------------------\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this,
                new JScrollPane(textArea),
                "Reservations",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Save Reservation to File
    private void saveReservation(Reservation res) {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(FILE_NAME, true))) {
            bw.write(res.toFileString());
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving reservation.");
        }
    }

    // Load Reservations from File
    private void loadReservations() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(
                new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Reservation res = new Reservation(
                        data[0],
                        Integer.parseInt(data[1]),
                        data[2],
                        Double.parseDouble(data[3])
                );
                reservations.add(res);

                for (Room room : rooms) {
                    if (room.roomNumber == res.roomNumber) {
                        room.isAvailable = false;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading reservations.");
        }
    }

    // Update File After Cancellation
    private void updateFile() {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(FILE_NAME))) {
            for (Reservation res : reservations) {
                bw.write(res.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error updating file.");
        }
    }

    // Clear Input Fields
    private void clearFields() {
        nameField.setText("");
        roomField.setText("");
    }

    // Main Method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HotelReservationSystem().setVisible(true);
        });
    }
}