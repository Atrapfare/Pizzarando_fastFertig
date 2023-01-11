package pizzarando;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Datenbank {

    private final String host;
    private final String database;
    private final String user;
    private final String passwort;
    private final String port;
    public Connection connection;

    public Datenbank() {
        host = "mysql31.1blu.de";
        database = "db306883x3410316";
        user = "s306883_3410316";
        passwort = "Pizzarando#123";
        port = "3306";
        connect();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, passwort);
            System.out.println("Datenbankverbindung erfolgreich hergestellt");
        } catch (SQLException ex) {
            System.out.println("Datenbank - Fehler: " + ex.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Datenbankverbindung beendet!");
            }
        } catch (SQLException ex) {
            System.out.println("Datenbankverbindung nicht beendet | Fehler: " + ex.getMessage());
        }
    }

    public int update(String query) {
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = st.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            st.close();
            return id;
        } catch (SQLException ex) {
            connect();
            System.err.println(ex);
        }
        return 0;
    }

    public ArrayList query(String query, int selectedColumnsInQuery) { // Datenbankabfrage mit anschließender Rückgabe String Array List
        ResultSet rs;
        ArrayList<String> StringList = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                for (int i = 1; i <= selectedColumnsInQuery; i++) {
                    StringList.add(rs.getString(i));
                }
            }

        } catch (SQLException ex) {
            connect();
            System.err.println(ex);
        }
        return StringList;
    }

    public ArrayList queryMehrereBestellungen(String query) { // Datenbankabfrage mit anschließender Rückgabe String Array List
        ResultSet rs;
        ArrayList<Bestellungsinfo> alleBestellungen = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Bestellungsinfo bestellinfo = new Bestellungsinfo(Integer.parseInt(rs.getString(1)), Integer.parseInt(rs.getString(2)), Double.parseDouble(rs.getString(3)), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
                alleBestellungen.add(bestellinfo);
            }
        } catch (SQLException ex) {
            connect();
            System.err.println(ex);
        }
        return alleBestellungen;
    }

    public boolean existsInDB(String query) {
        ResultSet rs;
        int id = 0;
        try {
            Statement st = connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException ex) {
            connect();
            System.err.println(ex);
        }
        if (id != 0) {
            return true;
        } else {
            return false;
        }
    }

    public Benutzer getBenutzer(String email) throws SQLException, IOException, NoSuchAlgorithmException {
        String query = "SELECT id FROM benutzer WHERE email = " + "\"" + email + "\"";

        if (this.existsInDB(query)) {
            query = "SELECT vorname, nachname, email, emailHash, passwort, straße, hausnummer, plz, ort FROM benutzer WHERE email = " + "\"" + email + "\"";
            ArrayList<String> result = this.query(query, 9); // Benutzer mit einzelnen Fremdschlüsseln

            String vornameDB = result.get(0);
            String nachnameDB = result.get(1);
            String emailDB = result.get(2);
            String emailHashDB = result.get(3);
            String passwortDB = result.get(4);
            String straßeDB = result.get(5);
            String hausnummerDB = result.get(6);
            String plzDB = result.get(7);
            String ortDB = result.get(8);

            Adresse adresse1 = new Adresse(straßeDB, Integer.parseInt(hausnummerDB), Integer.parseInt(plzDB), ortDB);
            Passwort passwort1 = new Passwort(passwortDB, false);
            Benutzer benutzer1 = new Benutzer(vornameDB, nachnameDB, emailDB, passwort1, adresse1, this, false);
            System.out.println("DB: Benutzer mit der Emailadresse " + benutzer1.getEmail() + " erfolgreich geladen.");

            return benutzer1;
        } else {
            System.out.println("DB: Benutzer existiert nicht.");
            return null;
        }
    }

    public Benutzer getBenutzerByHash(String emailHash) throws SQLException, IOException, NoSuchAlgorithmException {
        String query = "SELECT id FROM benutzer WHERE emailHash = " + "\"" + emailHash + "\"";

        if (this.existsInDB(query)) {
            query = "SELECT vorname, nachname, email, emailHash, passwort, straße, hausnummer, plz, ort FROM benutzer WHERE emailHash = " + "\"" + emailHash + "\"";
            ArrayList<String> result = this.query(query, 9); // Benutzer mit einzelnen Fremdschlüsseln

            String vornameDB = result.get(0);
            String nachnameDB = result.get(1);
            String emailDB = result.get(2);
            String emailHashDB = result.get(3);
            String passwortDB = result.get(4);
            String straßeDB = result.get(5);
            String hausnummerDB = result.get(6);
            String plzDB = result.get(7);
            String ortDB = result.get(8);

            Adresse adresse1 = new Adresse(straßeDB, Integer.parseInt(hausnummerDB), Integer.parseInt(plzDB), ortDB);
            Passwort passwort1 = new Passwort(passwortDB, false);
            Benutzer benutzer1 = new Benutzer(vornameDB, nachnameDB, emailDB, passwort1, adresse1, this, false);
            System.out.println("DB: Benutzer mit der Emailadresse " + benutzer1.getEmail() + " erfolgreich geladen.");

            return benutzer1;
        } else {
            System.out.println("DB: Benutzer existiert nicht.");
            return null;
        }
    }

    public String ladeEmailHashLokal() throws IOException {
        File myObj = new File("benutzer.txt");
        if (myObj.exists()) {
            Scanner myReader = new Scanner(myObj);
            String email = "";
            while (myReader.hasNextLine()) {
                email = myReader.nextLine();
            }
            if (!email.equals("")) {
                System.out.println("LOKAL: Emailadresse " + email + " erfolgreich ausgelesen.");
                return email;
            }
        }
        return null;
    }

    public String ladeBestellungLokal() throws FileNotFoundException {
        File myObj = new File("bestellung.txt");
        if (myObj.exists()) {
            Scanner myReader = new Scanner(myObj);
            String bestellungsId = "";
            while (myReader.hasNextLine()) {
                bestellungsId = myReader.nextLine();
            }
            if (!bestellungsId.equals("")) {
                System.out.println("LOKAL: Bestellung " + bestellungsId + " erfolgreich ausgelesen.");
                return bestellungsId;
            }
        }
        return "0";
    }

    public String getBestellungDetails(int id) throws SQLException, IOException, NoSuchAlgorithmException {
        System.err.println(id);
        String query = "SELECT id, benutzer, betrag, coupon, bestelldetails, anmerkung, zeit FROM bestellung WHERE id = " + id;
        ArrayList<String> result = this.query(query, 7);

        String idDB = result.get(0);
        String benutzerIDDB = result.get(1);
        String betragDB = result.get(2);
        String couponDB = result.get(3);
        String bestelldetailsDB = result.get(4);
        String anmerkungDB = result.get(5);
        String zeitDB = result.get(6);

        query = "SELECT email FROM benutzer WHERE id = " + benutzerIDDB;
        result = query(query, 1);
        String email = result.get(0);

        System.out.println("DB: Bestellung " + idDB + " erfolgreich geladen.");
        return "<b>BestellID:</b> " + idDB + "" + "<br>" + "Adresse: " + getBenutzer(email).getAdresse().getVolleAdresse() + "<br>" + "<b>Betrag:</b> " + betragDB + "<br>" + "<b>Coupon:</b> " + couponDB + "<br>" + "<b>Bestelldetails:</b><br>" + bestelldetailsDB + "<br>" + "<b>Anmerkungen:</b> " + anmerkungDB + "<br>" + "<b>Bestellzeit:</b> " + zeitDB;
    }

    public Benutzer anmeldenBenutzer(String email, String passwort) throws SQLException, IOException, NoSuchAlgorithmException {

        String query = "SELECT id FROM benutzer WHERE email = " + "\"" + email + "\"";

        if (this.existsInDB(query)) {
            Passwort passwort1 = new Passwort(passwort, true);
            query = "SELECT id FROM benutzer WHERE email = " + "\"" + email + "\" AND passwort = " + "\"" + passwort1.getHashedPasswort() + "\"";
            if (this.existsInDB(query)) {
                System.out.println("Benutzer angemeldet.");
                return getBenutzer(email);
            }
        }
        return null;
    }

    public void reconnect() {
        if (connection == null) {
            connect();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
