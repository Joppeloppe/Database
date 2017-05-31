import com.util.ConnectionConfiguration;

import java.sql.*;
import java.util.Scanner;

/**
 * Created by Joppeloppe on 2015-05-13.
 */
public class App {
    public static void main (String [] args)    {
        Connection connection = null;
        boolean exitApplication = false;

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

        System.out.println("\n\nHej Lars! Vad vill du göra?");
        System.out.print("1 - Lägg till ny kund.\n2 - Lägg till nytt köp.\n3 - Lägg till ny produkt.\n4 - Lägg till liknelse på produkter.\n5 - Se kunds totala köpsumma.\n\n0 - Stäng programmet.\n\n");

        Scanner in = new Scanner(System.in);

        menuSelect = in.nextInt();

        if (menuSelect == 1)    {
            AddClient();
        }

        if (menuSelect == 2)    {
            AddPurchase();
        }

        if (menuSelect == 3)    {
            AddProduct();
        }

        if (menuSelect == 4)    {
            AddLiknar();
        }

        if (menuSelect == 5)    {
            GetTotalSumma();
        }

        if (menuSelect == 0)  {
            System.exit(0);
        }
    }

    private static void AddLiknar() {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            Scanner in = new Scanner (System.in);

            String query = "insert into Liknar values(?, ?)";

            PreparedStatement prepStmnt = connection.prepareStatement(query);

            GetVara();

            System.out.println("Vilken är första produkten?\n");
            prepStmnt.setString(1, in.nextLine());

            GetVara();

            System.out.println("Vilken är den andra produkten som den första liknar?\n");
            prepStmnt.setString(2, in.nextLine());

            prepStmnt.execute();

            System.out.println("Liknelse tillagd!");

        }   catch (Exception e) {
            System.err.print("Oh no!\n\n");
            e.printStackTrace();
        }
    }

    private static void AddProduct() {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            Scanner in = new Scanner (System.in);

            float discount;
            float inFloat;

            String query = "insert into Produkt values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement prepStmnt = connection.prepareStatement(query);

            System.out.println("Vilken är modellen?");
            prepStmnt.setString(1, in.nextLine());

            System.out.println("Vilken energiklass?");
            prepStmnt.setString(2, in.nextLine());

            System.out.println("Vad är inköpspriset?");
            prepStmnt.setString(3, in.nextLine());

            System.out.println("Vad är försäljningspris?");
            prepStmnt.setString(4, in.nextLine());

            System.out.println("Beskrivning av produkten.");
            prepStmnt.setString(5, in.nextLine());

            System.out.println("Vad är produkttypen?");
            prepStmnt.setString(6, in.nextLine());

            System.out.println("Vilken tillverkare?");
            prepStmnt.setString(7, in.nextLine());

            System.out.println("Antal i lager.");
            prepStmnt.setString(8, in.nextLine());

            System.out.println("Produktens rabatt (som heltal utan procenttecken).");
            inFloat = in.nextFloat();

            discount = (inFloat / 100);

            prepStmnt.setString(9, String.valueOf(discount));

            prepStmnt.execute();

            System.out.println("Produkt tillagd!");

        }   catch (Exception e) {
            System.err.print("Oh no!\n\n");
            e.printStackTrace();
        }
    }

    private static void AddPurchase() {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            Scanner in = new Scanner (System.in);
            Statement statement = connection.createStatement();
            ResultSet result;

            int kundID;
            int antal;
            int pris = 0;
            int kopSumma;
            String modell;
            String forsaljningspris = "";

            String query = "insert into Kop values(?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement prepStmnt = connection.prepareStatement(query);

            System.out.println("Vad är ordernummret?");
            prepStmnt.setString(1, in.nextLine());

            System.out.println("Vad är datumet?");
            prepStmnt.setString(2, in.nextLine());

            System.out.println("Vad är kundens id?");
            GetKund();
            kundID = in.nextInt();
            prepStmnt.setString(3, String.valueOf(kundID));

            System.out.println("Vilken produkt?\n");
            GetVara();
            in.nextLine();
            modell = in.nextLine();
            prepStmnt.setString(4, modell);

            System.out.println("Hur många produkter?");
            antal = in.nextInt();
            prepStmnt.setString(5, String.valueOf(antal));

            result = statement.executeQuery("Select Forsaljningspris from Produkt where Modell = '" + modell + "'");

            while (result.next())   {
                forsaljningspris = result.getString(1);
                pris = Integer.parseInt(forsaljningspris);
            }

            prepStmnt.setString(6, forsaljningspris);

            kopSumma = pris * antal;

            prepStmnt.setString(7, String.valueOf(kopSumma));

            prepStmnt.execute();

            System.out.println("Köp tillagt!");

        }   catch (Exception e) {
            System.err.print("Oh no!\n\n");
            e.printStackTrace();
        }
    }

    public static void GetKund()  {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result;

            System.out.println("\nHere are the members:");

            result = statement.executeQuery("Select ID, Namn from Kund");

            while (result.next())   {
                String name = result.getString(2);
                String kundID = result.getString(1);

                System.out.println("Kundens ID: " + kundID + ", Namn: " + name);
            }

        }   catch (Exception e) {
            System.err.print("Oh no!\n\n");
            e.printStackTrace();
        }

    }


    public static void GetVara()  {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result;

            System.out.println("\nHär är alla våra olika produkter.\n");

            result = statement.executeQuery("Select Modell, Typ from Produkt order by Typ");

            while (result.next())   {
                String modell = result.getString(1);
                String typ = result.getString(2);

                System.out.println(modell + ", " + typ);
            }

        }   catch (Exception e) {
            System.err.print("Oh no!\n\n");
            e.printStackTrace();
        }

    }

    public static void AddClient() {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            Scanner in = new Scanner (System.in);

            String query = "insert into Kund (Namn, Email, Adress) values(?, ?, ?)";

            PreparedStatement prepStmnt = connection.prepareStatement(query);

            System.out.println("Vad är kundens namn?");
            prepStmnt.setString(1, in.nextLine());

            System.out.println("Vad är kundens e-mail?");
            prepStmnt.setString(2, in.nextLine());

            System.out.println("Vad är kundens adress?");
            prepStmnt.setString(3, in.nextLine());


            prepStmnt.execute();

            System.out.println("Kund tillagd!");

        }   catch (Exception e) {
            System.err.print("Oh no!\n\n");
            e.printStackTrace();
        }
    }

    public static void GetTotalSumma()  {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result;
            Scanner in = new Scanner (System.in);
            int kundID;

            System.out.println("Välj en kund.");

            GetKund();

            kundID = in.nextInt();

            result = statement.executeQuery("Select Kund.Namn, sum(Summa) from (Kop inner join Kund on Kund.ID = Kop.KundID) where KundID = '" + kundID + "' group by Kund.Namn");

            while (result.next())   {
                String namn = result.getString(1);
                String summa = result.getString(2);

                System.out.println(namn + ", " + summa + " kr.");
            }

        }   catch (Exception e) {
            System.err.print("Oh no!\n\n");
            e.printStackTrace();
        }
    }
}

