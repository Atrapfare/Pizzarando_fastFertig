package pizzarando;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Benutzer {

    private String vorname;
    private String nachname;
    private String email;
    private String emailHash;
    private Passwort passwort;
    private Adresse adresse;
    private int id;

    public Benutzer(String vorname, String nachname, String email, Passwort passwort, Adresse adresse, Datenbank db, boolean sichern) throws IOException, NoSuchAlgorithmException {
        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
        this.emailHash = hash(email);
        this.passwort = passwort;
        this.adresse = adresse;
        if (sichern) {
            this.sichereBenutzer(db);
        } else {
            this.sichereBenuterLokal(email);
        }
    }

    public boolean sichereBenutzer(Datenbank db) throws IOException, NoSuchAlgorithmException {

        if (this.sichereBenutzerDB(db)) {
            this.sichereBenuterLokal(email);
            return true;
        }
        return false;
    }

    public boolean sichereBenutzerDB(Datenbank db) {
        // Benutzer mit allen Infos in DB und LOKAL speichern
        String query = "SELECT id FROM benutzer WHERE email = " + "\"" + this.email + "\"";

        if (!db.existsInDB(query)) {
            query = "INSERT INTO benutzer (id, vorname, nachname, email, emailHash, passwort, straße, hausnummer, plz, ort) VALUES (NULL, '" + this.vorname + "', '" + this.nachname + "', '" + this.email + "', '" + this.emailHash + "', '" + this.passwort.getHashedPasswort() + "', '" + this.adresse.getStraße() + "', '" + this.adresse.getHausnummer() + "', '" + this.adresse.getPlz() + "', '" + this.adresse.getOrt() + "')";
            int benutzerId = db.update(query);
            System.out.println("DB: Eine neuer Benutzer mit der ID " + benutzerId + " wurde angelegt: " + this.vorname + ", " + this.nachname + ", " + this.email + ", " + this.passwort.getHashedPasswort() + ", " + this.adresse.getVolleAdresse());
            return true;
        } else {
            System.out.println("DB: Emailadresse bereits vorhanden!");
            return false;
        }
    }

    public boolean updateBenutzer(String vorname, String nachname, String email, String passwort, Adresse adresse, Datenbank db) throws SQLException, IOException, NoSuchAlgorithmException {
        // Benutzer Infos updaten
        // Benutzer mit allen Infos in DB speichern
        String query = "SELECT id FROM benutzer WHERE email = " + "\"" + this.email + "\"";

        if (!db.existsInDB(query)) {
            query = "UPDATE benutzer SET vorname='" + vorname + "', nachname ='" + nachname + "', email ='" + email + "', emailHash ='" + hash(email) + "', passwort ='" + passwort.hashCode() + "', straße ='" + adresse.getStraße() + "', hausnummer ='" + adresse.getHausnummer() + "' , plz ='" + adresse.getPlz() + "', ort ='" + adresse.getOrt() + "' WHERE email = " + this.email;
            int benutzerId = db.update(query);

            System.out.println("DB: Benutzerinformationen geupdatet.");
            db.getBenutzer(email);
            sichereBenuterLokal(email);
            return true;
        } else {
            System.out.println("DB: Emailadresse existiert nicht!");
            return false;
        }
    }

    public String getEmail() {
        return email;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean sichereBenuterLokal(String email) throws IOException, NoSuchAlgorithmException {
        FileWriter myWriter = new FileWriter("benutzer.txt");
        myWriter.write(hash(email));
        myWriter.close();
        System.out.println("LOKAL: Benutzer erfolgreich gesichert.");
        return true;
    }

    public void abmelden() throws IOException, URISyntaxException {
        File myFile = new File("benutzer.txt");
        if (myFile.exists()) {
            myFile.delete();
        }
        Runtime.getRuntime().exec("java -jar pizzarando.jar");
        System.exit(0);
    }

    private String hash(String email) throws NoSuchAlgorithmException {
        return Integer.toString(email.hashCode());
    }
}
