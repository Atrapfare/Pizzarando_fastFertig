/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzarando;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author dominikknaup
 */
public class Bestellung {

    private final Warenkorb warenkorb;
    private final Benutzer benutzer;
    private double betrag;
    private final String coupon;
    private final String anmerkung;
    private final String bestellzeit;
    private int bestellID;

    public Bestellung(Warenkorb warenkorb, Benutzer benutzer, double betrag, String coupon, String anmerkung, String bestellzeit, Datenbank db) {
        this.warenkorb = warenkorb;
        this.benutzer = benutzer;
        this.coupon = coupon;
        this.anmerkung = anmerkung;
        this.betrag = betrag;
        this.bestellzeit = bestellzeit;
        sendeBestellung(db);
    }

    private boolean sendeBestellung(Datenbank db) {
        String bestellDetails = generiereBestelldetails();
        String query = "INSERT INTO bestellung (benutzer, betrag, coupon, bestelldetails, anmerkung, zeit) VALUES ('" + benutzer.getId() + "', '" + this.betrag + "', '" + this.coupon + "', '" + bestellDetails + "', '" + this.anmerkung + "', '" + this.bestellzeit + "')";
        int bestellId = db.update(query);
        if (bestellId != 0) {
            this.bestellID = bestellId;
            System.out.println("DB: Eine neue Bestellung mit der ID " + bestellId + " wurde angelegt: " + this.benutzer.getEmail() + ", " + this.betrag + ", " + this.coupon);
            return true;
        } else {
            return false;
        }
    }

    private String generiereBestelldetails() {
        String bestellDetails = "";
        int slot = 1;

        for (int i = 0; i < warenkorb.getWarenkorbItems().size(); i++) {
            if (!warenkorb.getWarenkorbItems().get(i).getTyp().equals("noch frei")) {
                bestellDetails += "Pizzaslot " + slot + ": " + warenkorb.getWarenkorbItems().get(i).getAnzahl() + "x " + warenkorb.getWarenkorbItems().get(i).getTyp() + "<br>";
                slot++;
            }
        }

        return bestellDetails;
    }
    
    public void speichereBestellung() throws IOException {
        FileWriter myWriter = new FileWriter("bestellung.txt");
        myWriter.write(this.bestellID);
        myWriter.close();
        System.out.println("LOKAL: Bestellung erfolgreich gesichert.");
    }

    public int getBestellID() {
        return bestellID;
    }
    
    
}
