package org.yearup;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class OnlineStoreApp
{
    // Global variables
    final static public Scanner scanner = new Scanner(System.in);
    final static HashMap<String, Product> inventory = new HashMap<>();
    final static ArrayList<Product> cart = new ArrayList<>();

    public void run()
    {
        loadInventory();
        homeScreen();
    }

    public void loadInventory()
    {
        System.out.println("Welcome to Barns Shop!");
        System.out.println();

        try (Scanner reader = new Scanner(new File("inventory.csv")))
        {
            while (reader.hasNextLine())
            {
                String line = reader.nextLine();
                String[] split = line.split("\\|");

                String id = split[0];
                String name = split[1];
                double price = Double.parseDouble(split[2]);

                Product product = new Product(id, name, price);

                inventory.put(String.valueOf(id), product);
            }
        } catch (FileNotFoundException e)
        {
            System.out.println("Error: " + e);
        }
    }

    public void homeScreen()
    {
        while (true)
        {
            System.out.println();
            System.out.println("Please select from the options below");
            System.out.println("1) Show products");
            System.out.println("2) Show cart");
            System.out.println("0) Exit");
            System.out.print("Enter: ");
            int response = scanner.nextInt();
            scanner.nextLine();

            switch (response)
            {
                case 1 ->
                {
                    System.out.println("Showing the products...\n");
                    displayProducts();
                }
                case 2 ->
                {
                    System.out.println("Showing cart...\n");
                    displayCart();
                    return;
                }
                case 0 ->
                {
                    System.out.println("Exit");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.\n");
            }
        }

    }

    public void displayProducts()
    {
        System.out.println();

        for (Product product : inventory.values())
        {
            System.out.println(product);
        }
        respondProduct();
    }

    public void respondProduct()
    {
        {
            String respond;
            do
            {
                System.out.println("Enter the product ID or enter '0' to return to home screen.");
                System.out.print("Enter: ");

                respond = scanner.nextLine();
                if (respond.matches("[A-Z0-9]+"))
                {
                    if (inventory.get(respond) != null)
                    {
                        // Add item to cart
                        Product product = inventory.get(respond);
                        System.out.println("This " + product.getName() + " has been added to your cart.\n");
                        cart.add(product);

                    } else if (respond.equals("0"))
                    {
                        // Return to home screen
                        System.out.println("Returning to home screen...\n");
                        homeScreen();
                        return;

                    } else
                    {
                        System.out.println("Invalid input. Please try again.");
                    }
                } else
                {
                    System.out.println("Please enter a valid number or '0' to return to home screen.");
                }
            } while (!respond.equals("0"));

            displayCart();
        }
    }

    private void displayCart()
    {
        System.out.println("Your Current Cart: ");

        double total = 0;
        for (Product product : cart)
        {
            System.out.println(product.getName() + " - $" + product.getPrice());
            total += product.getPrice();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("Total: $" + df.format(total) + "\n");

        boolean continueToCheckout = true;
        while (continueToCheckout)
        {
            continueToCheckout = respondCart(total);
        }

        homeScreen();
    }

    public boolean respondCart(double total)
    {
        System.out.println("Please enter 'checkout' or 'x' to return to the home screen: ");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("x"))
        {
            return false;
        } else if (input.equalsIgnoreCase("checkout"))
        {
            System.out.println("Checking out...");
            System.out.println("Thank you for your purchase! Your total is $" + total);
            checkOut(total);
            cart.clear(); 
            return false;
        } else
        {
            System.out.println("Invalid input. Please try again.");
            return respondCart(total);
        }

    }
    public static void checkOut(double total)
    {
        double respond = 0;
        boolean validInput = false;
        do
        {
            System.out.print("Enter amount paid: $");
            try
            {
                respond = scanner.nextDouble();
                validInput = true;
            } catch (InputMismatchException e)
            {
                System.out.println("Invalid input. Please enter a valid number.\n");
                scanner.nextLine();
            }
        } while (!validInput);

        BigDecimal totalDecimal = BigDecimal.valueOf(total);
        BigDecimal respondDecimal = BigDecimal.valueOf(respond);
        BigDecimal remainderDecimal = totalDecimal.subtract(respondDecimal).setScale(2, RoundingMode.HALF_DOWN);

        double remainder = remainderDecimal.doubleValue();

        if (remainder <= 0)
        {
            // Change due
            remainder = Math.abs(remainder);
            System.out.println("Receipt");
            for (Product product : cart)
            {
                System.out.println("-" + product.getName());
            }
            System.out.println("Change due: " + remainder + "\n");
            cart.clear();

        } else
        {
            System.out.println("Not enough for total payment. Please enter a larger amount.\n");
            checkOut(total);
        }
    }
}



