import com.util.ConnectionConfiguration;

import java.sql.*;
import java.util.Scanner;

/**
 * Created by Joppeloppe on 2015-05-13.
 */
public class App {
    public static void main (String [] args)    {
        Connection connection = null;

        try {
            connection = ConnectionConfiguration.getConnection();
            if (connection != null) {
                System.out.println("Connection established!");

                GetMenu();

            }   else    {
                System.out.println("Connection failed!");
            }

        }   catch (Exception e)  {
            System.err.print("Oh no!\n\n");
            e.printStackTrace();
        }   finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.print("Oh no!\n\n");
                    e.printStackTrace();
                }
            }
        }

    }

    public static void GetMenu ()   {
        int menuSelect;

        System.out.println("\n\nHej k�ra kund!");
        System.out.print("1 - S�k varutyp.\n2 - S�k varumodell.\n3 - Se kampanjvaror.\n\n0 - St�ng programmet.\n\n");

        Scanner in = new Scanner(System.in);

        menuSelect = in.nextInt();

        if (menuSelect == 1)    {
            SelectTyp();
        }

        if (menuSelect == 2)    {
            GetVara();
        }

        if (menuSelect == 3)    {
            GetKampanj();
        }


        if (menuSelect == 0)  {
            System.exit(0);
        }
    }

    public static void SelectTyp()  {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result;

            System.out.println("\nH�r �r alla olika typer av produkt som finns.");

            result = statement.executeQuery("Select Typ from Produkt group by Typ");

            while (result.next())   {
                String typ = result.getString(1);

                System.out.println(typ);
            }

            System.out.println("V�lj en typ av produkt du vill se.");

            String typSelect;

            Scanner in = new Scanner(System.in);

            typSelect = in.next();

            GetProduktByTyp(typSelect);

        }   catch (Exception e) {
            System.err.print("Oh no!\n\n");
            e.printStackTrace();
        }

    }

    public static void GetVara()    {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result;

            result = statement.executeQuery("Select Modell from Produkt order by Modell;");

            System.out.println("\nH�r �r v�ra produkter: ");

            while (result.next())   {
                String modell = result.getString(1);

                System.out.println(modell);
            }

            System.out.println("V�lj en modell du vill se.");

            String modellSelect;

            Scanner in = new Scanner(System.in);

            modellSelect = in.next();

            GetProduktByModell(modellSelect);

        }   catch (Exception e)   {
            System.err.print("Oh no!\n\n");
            e.printStackTrace();
        }
    }

    public static void GetProduktByModell(String modellText)   {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result;

            result = statement.executeQuery("Select Modell, Energiklass, Forsaljningspris, Beskrivning, Typ, Tillverkare from Produkt where Modell = '" + modellText + "'");

            System.out.println("\nH�r �r v�ra produkter: ");

            while (result.next())   {
                String modell = result.getString(1);
                String energiklass = result.getString(2);
                String pris = result.getString(3);
                String beskrivning = result.getString(4);
                String typ = result.getString(5);
                String tillverkare = result.getString(6);

                System.out.println(modell + ", " + energiklass + "\n" + pris + "\n"  + beskrivning + "\n" + typ + " fr�n " + tillverkare + "\n\n");
            }

            GetLiknelse(modellText);

        }   catch (Exception e)   {
            System.err.print("Oh no!\n\n");
            e.printStackTrace();
        }
    }
    
    public static void GetProduktByTyp(String typText)   {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result;

            result = statement.executeQuery("Select Modell, Energiklass, Forsaljningspris, Beskrivning, Typ, Tillverkare from Produkt where Typ = '" + typText + "'");

            System.out.println("\nH�r �r v�ra produkter: ");

            while (result.next())   {
                String modell = result.getString(1);
                String energiklass = result.getString(2);
                String pris = result.getString(3);
                String beskrivning = result.getString(4);
                String typ = result.getString(5);
                String tillverkare = result.getString(6);

                System.out.println(modell + ", " + energiklass + "\n" + pris + "\n"  + beskrivning + "\n" + typ + " fr�n " + tillverkare + "\n\n");
                }
            
            }   catch (Exception e)   {
                System.err.print("Oh no!\n\n");
                e.printStackTrace();
            }
    }

    public static void GetKampanj() {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result;

            System.out.println("Dessa produkter har vi kampanj p� just nu:\n");

            result = statement.executeQuery("Select Modell from Produkt where Rabatt != 0");

            while (result.next())   {
                String produkt = result.getString(1);

                System.out.println(produkt);
            }

        }   catch (Exception e) {
            System.err.print("Oh no!\n\n");
            e.printStackTrace();
        }
    }

    public static void GetLiknelse(String modellText)    {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result;

            System.out.println("Dessa produkter liknar varandra:\n");

            result = statement.executeQuery("select Liknar.Produkt2 from" +
                    " (Produkt inner join Liknar on Produkt.Modell = Liknar.Produkt1)" +
                    " where Produkt.Modell = '" + modellText + "'");

            while (result.next())   {
                String produkt = result.getString(1);

                System.out.println(produkt);
            }

        }   catch (Exception e) {
            System.err.print("Oh no!\n\n");
            e.printStackTrace();
        }
    }
}

