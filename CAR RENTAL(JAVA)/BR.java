import java.util.*;
class Bike{
    private String bikeId;
    private String bikeName;
    private String bikeModel;
    private double bikeRentalPrice;
    private boolean bikeAvailable;
    public Bike(String bikeId,String bikeModel,String bikeName,double bikeRentalPrice){
        this.bikeId=bikeId;
        this.bikeModel=bikeModel;
        this.bikeName=bikeName;
        this.bikeRentalPrice=bikeRentalPrice;
        this.bikeAvailable=true;
    }
    public String getBikeID(){
        return bikeId;
    }
    public String getBikeName(){
        return bikeName;
    }
    public String getBikeModel(){
        return bikeModel;
    }
    public double getBikeRentalPrice(int rentalDays){
        return bikeRentalPrice*rentalDays;
    }
    public boolean bikeAvailable(){
        return bikeAvailable;
    }
    public void bikeRented(){
        bikeAvailable=false;
    }
    public void bikeReturned(){
        bikeAvailable=true;
    }
}
class Customer{
    private String customerName;
    private String customerId;
    public Customer(String customerName,String customerId){
        this.customerId=customerId;
        this.customerName=customerName;
    }
    public String getCustomerId(){
        return customerId;
    }
    public String getCustomerName(){
        return customerName;
    }
}

class Rentals{
    private Bike bike;
    private Customer customer;
    private int days;
    public Rentals(Bike bike,Customer customer,int days){
        this.bike=bike;
        this.customer=customer;
        this.days=days;
    }
    public Bike getBike(){
        return bike;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}
class bikeRentalSystem{
    private List<Bike> bikes;
    private List<Customer> customers;
    private List<Rentals> rentals;

    public bikeRentalSystem(){
        bikes=new ArrayList<>();
        customers=new ArrayList<>();
        rentals=new ArrayList<>();
    }
    public void addBike(Bike bike){
        bikes.add(bike);
    }
    public void addCustomer(Customer customer){
        customers.add(customer);
    }
    public void rentBike(Bike bike,Customer customer,int days){
        if(bike.bikeAvailable()){
            bike.bikeRented();
            rentals.add(new Rentals(bike,customer,days));
        }
        else{
            System.out.println("Bike is not available. ");
        }
    }
    public void returnBike(Bike bike){
        bike.bikeReturned();
        Rentals temp=null;
        for(Rentals rent:rentals){
            if(rent.getBike()==bike){
                temp=rent;
                break;
            }
        }
        if(temp!=null){
            rentals.remove(temp);
        }
        else{
            System.out.println("Car was not rented. ");
        }
    }
    public void menu(){
        Scanner in =new Scanner(System.in);

        while(true){
            System.out.println("--- Bike Rental System ---");
            System.out.println("1. Rent a Bike");
            System.out.println("2. Return a Bike");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice=in.nextInt();
            in.nextLine();
            if(choice==1){
                System.out.println("\n-- Rent a Bike --\n");
                System.out.print("Enter your name: ");
                String customerName = in.nextLine();

                System.out.println("\nAvailable Bikes:");
                for(Bike avail:bikes){
                    if(avail.bikeAvailable()){
                        System.out.println(avail.getBikeID() +"-"+ avail.getBikeModel()+"-"+avail.getBikeName());
                    }
                }
                System.out.print("\nEnter the bike ID you want to rent: ");
                String bikeId = in.nextLine();

                System.out.print("Enter the number of days for rental: ");
                int rentalDays = in.nextInt();
                in.nextLine();

                Customer newCustomer = new Customer( customerName,"CUS" + (customers.size() + 1));
                addCustomer(newCustomer);

                Bike temp=null;
                for (Bike select:bikes){
                    if(select.getBikeID().equals(bikeId) && select.bikeAvailable()){
                        temp=select;
                        break;
                    }
                }
                if(temp!=null){
                    double price=temp.getBikeRentalPrice(rentalDays);
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getCustomerName());
                    System.out.println("Bike: " + temp.getBikeName() + " " + temp.getBikeModel());
                    System.out.println("Rental Days: " + rentalDays);
                    System.out.printf("Total Price: $%.2f%n", price);

                    System.out.print("\nSelect Yes(Y) or No(N) to Confirm rental : ");
                    String confirm = in.nextLine();

                    if(confirm.equalsIgnoreCase("Y")){
                        rentBike(temp,newCustomer,rentalDays);
                        System.out.println("\nBike rented successfully.");
                    }
                    else{
                        System.out.println("\nRental canceled.");
                    }
                }
                else{
                    System.out.println("\nInvalid bike selection or bike not available for rent.");
                }
            }
            else if(choice==2){
                System.out.println("\n-- Return a Bike --\n");
                System.out.print("Enter the bike ID you want to return: ");
                String bikeId = in.nextLine();

                Bike returnTobike=null;
                for(Bike temp:bikes){
                    if(temp.getBikeID().equals(bikeId) && !temp.bikeAvailable()){
                        returnTobike=temp;
                        break;
                    }
                }
                if(returnTobike!=null){
                    Customer customer=null;
                    for(Rentals rent:rentals){
                        if(rent.getBike()==returnTobike){
                            customer=rent.getCustomer();
                            break;
                        }
                    }
                    if(customer!=null){
                        returnBike(returnTobike);
                        System.out.println("Bike returned successfully by " + customer.getCustomerName());
                    } else {
                        System.out.println("Bike was not rented or rental information is missing.");
                    }
                }
                else{
                    System.out.println("Invalid bike ID or bike is not rented.");
                }
            }else if(choice==3){
                break;
            }
            else{
                System.out.println("Invalid choice. Please enter a valid option.");
            }

        }
        System.out.println("\nThank you for using the Bike Rental System!\n");
    }

}
public class BR {
    public static void main(String[] args) {
        bikeRentalSystem brs = new bikeRentalSystem();

        Bike bike1 = new Bike("B01", "HERO", "Splendor", 25);
        Bike bike2 = new Bike("B02", "KTM", "Duke 200", 45);
        Bike bike3 = new Bike("B03", "ROYAL ENFIELD", "Himalayan", 40);
        Bike bike4 = new Bike("B04", "YAMAHA", "R15", 35);

        brs.addBike(bike1);
        brs.addBike(bike2);
        brs.addBike(bike3);
        brs.addBike(bike4);

        brs.menu();
    }
}
