package org.yearup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class OnlineStoreApp
{
    private Product[] products = new Product[10];
    public void run()
    {
        loadInventory("Inventory.csv");
        System.out.println();


    }
    public void loadInventory(String fileName)
    {
        ArrayList<String[]> inventorys = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) !=null)
            {
                System.out.println(line);

            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e)
        {
            System.out.println("Error reading file: " + e.getMessage());
        }


    }
    private void displayInventory()
    {


    }
}
