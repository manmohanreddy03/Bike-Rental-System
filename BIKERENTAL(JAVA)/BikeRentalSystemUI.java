import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Bike {
    private String bikeId;
    private String bikeName;
    private String bikeModel;
    private double bikeRentalPrice;
    private boolean bikeAvailable;

    public Bike(String bikeId, String bikeModel, String bikeName, double bikeRentalPrice) {
        this.bikeId = bikeId;
        this.bikeModel = bikeModel;
        this.bikeName = bikeName;
        this.bikeRentalPrice = bikeRentalPrice;
        this.bikeAvailable = true;
    }

    public String getBikeID() {
        return bikeId;
    }

    public String getBikeName() {
        return bikeName;
    }

    public String getBikeModel() {
        return bikeModel;
    }

    public double getBikeRentalPrice(int rentalDays) {
        return bikeRentalPrice * rentalDays;
    }

    public boolean bikeAvailable() {
        return bikeAvailable;
    }

    public void bikeRented() {
        bikeAvailable = false;
    }

    public void bikeReturned() {
        bikeAvailable = true;
    }
}

class Customer {
    private String customerName;
    private String customerId;

    public Customer(String customerName, String customerId) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }
}

class Rentals {
    private Bike bike;
    private Customer customer;
    private int days;

    public Rentals(Bike bike, Customer customer, int days) {
        this.bike = bike;
        this.customer = customer;
        this.days = days;
    }

    public Bike getBike() {
        return bike;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class BikeRentalSystem {
    private List<Bike> bikes;
    List<Customer> customers;
    List<Rentals> rentals;

    public BikeRentalSystem() {
        bikes = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addBike(Bike bike) {
        bikes.add(bike);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentBike(Bike bike, Customer customer, int days) {
        if (bike.bikeAvailable()) {
            bike.bikeRented();
            rentals.add(new Rentals(bike, customer, days));
        } else {
            JOptionPane.showMessageDialog(null, "Bike is not available.");
        }
    }

    public void returnBike(Bike bike) {
        bike.bikeReturned();
        Rentals temp = null;
        for (Rentals rent : rentals) {
            if (rent.getBike() == bike) {
                temp = rent;
                break;
            }
        }
        if (temp != null) {
            rentals.remove(temp);
        } else {
            JOptionPane.showMessageDialog(null, "Bike was not rented.");
        }
    }

    public List<Bike> getAvailableBikes() {
        List<Bike> availableBikes = new ArrayList<>();
        for (Bike bike : bikes) {
            if (bike.bikeAvailable()) {
                availableBikes.add(bike);
            }
        }
        return availableBikes;
    }

    public Bike getBikeById(String bikeId) {
        for (Bike bike : bikes) {
            if (bike.getBikeID().equals(bikeId)) {
                return bike;
            }
        }
        return null;
    }

    public void addInitialBikes() {
        addBike(new Bike("B01", "HERO", "Splendor", 25));
        addBike(new Bike("B02", "KTM", "Duke 200", 45));
        addBike(new Bike("B03", "ROYAL ENFIELD", "Himalayan", 40));
        addBike(new Bike("B04", "YAMAHA", "R15", 35));
    }
}

public class BikeRentalSystemUI extends JFrame {
    private BikeRentalSystem bikeRentalSystem;
    private JTextArea outputArea;

    public BikeRentalSystemUI() {
        bikeRentalSystem = new BikeRentalSystem();
        bikeRentalSystem.addInitialBikes();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Bike Rental System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel buttonPanel = new JPanel();
        JButton rentButton = new JButton("Rent a Bike");
        JButton returnButton = new JButton("Return a Bike");
        JButton exitButton = new JButton("Exit");

        rentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rentBike();
            }
        });

        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnBike();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(rentButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(exitButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void rentBike() {
        String customerName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (customerName == null || customerName.isEmpty()) return;

        List<Bike> availableBikes = bikeRentalSystem.getAvailableBikes();
        if (availableBikes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No bikes available.");
            return;
        }

        String[] bikeOptions = new String[availableBikes.size()];
        for (int i = 0; i < availableBikes.size(); i++) {
            Bike bike = availableBikes.get(i);
            bikeOptions[i] = bike.getBikeID() + " - " + bike.getBikeModel() + " - " + bike.getBikeName();
        }

        String bikeChoice = (String) JOptionPane.showInputDialog(this, "Select a bike to rent:", "Rent a Bike",
                JOptionPane.QUESTION_MESSAGE, null, bikeOptions, bikeOptions[0]);

        if (bikeChoice == null) return;

        String bikeId = bikeChoice.split(" - ")[0];
        Bike selectedBike = bikeRentalSystem.getBikeById(bikeId);

        String daysStr = JOptionPane.showInputDialog(this, "Enter number of days for rental:");
        if (daysStr == null || daysStr.isEmpty()) return;

        int rentalDays;
        try {
            rentalDays = Integer.parseInt(daysStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number of days.");
            return;
        }

        Customer newCustomer = new Customer(customerName, "CUS" + (bikeRentalSystem.customers.size() + 1));
        bikeRentalSystem.addCustomer(newCustomer);

        double price = selectedBike.getBikeRentalPrice(rentalDays);
        int confirm = JOptionPane.showConfirmDialog(this, String.format("Total Price: $%.2f%nConfirm rental?", price),
                "Confirm Rental", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            bikeRentalSystem.rentBike(selectedBike, newCustomer, rentalDays);
            outputArea.append(String.format("Bike rented successfully by %s (%s).%n", newCustomer.getCustomerName(), newCustomer.getCustomerId()));
        } else {
            outputArea.append("Rental canceled.\n");
        }
    }

    private void returnBike() {
        String bikeId = JOptionPane.showInputDialog(this, "Enter the bike ID you want to return:");
        if (bikeId == null || bikeId.isEmpty()) return;

        Bike bike = bikeRentalSystem.getBikeById(bikeId);
        if (bike == null || bike.bikeAvailable()) {
            JOptionPane.showMessageDialog(this, "Invalid bike ID or bike is not rented.");
            return;
        }

        Customer customer = null;
        for (Rentals rent : bikeRentalSystem.rentals) {
            if (rent.getBike() == bike) {
                customer = rent.getCustomer();
                break;
            }
        }

        if (customer != null) {
            bikeRentalSystem.returnBike(bike);
            outputArea.append(String.format("Bike returned successfully by %s (%s).%n", customer.getCustomerName(), customer.getCustomerId()));
        } else {
            JOptionPane.showMessageDialog(this, "Bike was not rented or rental information is missing.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BikeRentalSystemUI().setVisible(true);
            }
        });
    }
}
