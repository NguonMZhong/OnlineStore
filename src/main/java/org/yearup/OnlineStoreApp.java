package org.yearup;

import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OnlineStoreApp
{
    //Global variables
    Scanner scanner = new Scanner(System.in);
    //private final Product[] products = new Product[10];
    public static ArrayList<Product> cart = new ArrayList<>();

    public void run()
    {
        //ArrayList<Product> cart = loadInventory();

        homeScreen();

        //for (Product product : cart)
        {
            //System.out.println(product.getId());
            //System.out.println(product.getName());
            //System.out.println(product.getPrice());
        }
    }

    public static ArrayList<Product> loadInventory()
    {
        //ArrayList<Product> cart = new ArrayList<>();

        FileInputStream stream;
        Scanner fileScanner = null;
        try
        {

            stream = new FileInputStream("inventory.csv");
            fileScanner = new Scanner(stream);

            System.out.println("Welcomes to Barns Online Store! ");
            System.out.println();

            System.out.println("ID\t\tName\t\t\t\t\t\t\tPrice");
            fileScanner.nextLine();

            while (fileScanner.hasNext())
            {
                String line = fileScanner.nextLine();

                String[] columns = line.split("\\|");
                String id = columns[0];
                String name = columns[1];
                double price = Double.parseDouble(columns[2]);
                System.out.printf("%s\t%-30s\t%.2f\n", id, name, price);


                //create book and add to ArrayList
                Product product = new Product(id, name, price);
                cart.add(product);
            }

        } catch (IOException e)
        {
            System.out.println("Error reading file: " + e.getMessage());
        } finally
        {
            if (fileScanner != null)
            {
                fileScanner.close();
            }
        }
        return cart;

    }
    public void homeScreen()
    {
        System.out.println("Please select from the options below");
        System.out.println("1) Show products");
        System.out.println("2) Show cart");
        System.out.println("0) Exit");
        System.out.print("Enter: ");
        int response = scanner.nextInt();
        scanner.nextLine();

        while (true)
        {
            switch (response)
            {
                case 1:
                    System.out.println("Showing the products...\n");
                    loadInventory();
                    displayProduct();

                    return;
                case 2:
                    System.out.println("Showing cart...\n");
                    displayCart();

                    return;
                case 0:
                    System.out.println("Exit");

                    return;
                default:
                    System.out.println("Invalid option. Please try again.\n");
            }
        }
    }
    private void displayProduct()
    //if product ID selected, should add the product to cart and then display home screen again
    {
        System.out.println();
        System.out.println("Enter the ID of the product you wish to put in your cart: ");
        String input = scanner.nextLine();

        //If user enter X, return to home screen
        if (input.equalsIgnoreCase("x"))
        {
            return;

            // Find product with matching ID
        }
        Product selectedProduct = null;
        for (Product product : cart.toArray(new Product[0]))
        {
            if (product.getId().equals(input))
            {
                selectedProduct = product;
                break;
            }
        }
        if (selectedProduct != null)
        {
            cart.add(selectedProduct);
            System.out.println(selectedProduct.getName() + " has been added to your cart.");
            homeScreen();
        } else
        {
            System.out.println("Invalid input. Please try again.");
            displayProduct();
        }

    }

    public void displayCart()
    { //maybe use hashmap
        System.out.println("Cart: ");

        double total = 0;
        for (Product product : cart)
        {
            System.out.println(product.getName() + " - $" + product.getPrice());
            total += product.getPrice();
        }
        System.out.println("Total: $" + total);

    }
}
