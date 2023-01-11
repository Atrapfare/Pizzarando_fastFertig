/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzarando;

/**
 *
 * @author dominikknaup
 */
public class Bestellungsinfo {

    private int id;
    private int benutzer;
    private double betrag;
    private String coupon;
    private String bestelldetails;
    private String anmerkungen;
    private String zeit;
    private boolean fertig;

    public Bestellungsinfo(int id, int benutzer, double betrag, String coupon, String bestelldetails, String anmerkungen, String zeit, String fertig) {
        this.id = id;
        this.benutzer = benutzer;
        this.betrag = betrag;
        this.coupon = coupon;
        this.bestelldetails = bestelldetails;
        this.anmerkungen = anmerkungen;
        this.zeit = zeit;
        if (fertig.equals("0")) {
            this.fertig = false;
        } else {
            this.fertig = true;
        }
    }

    public int getId() {
        return id;
    }

    public int getBenutzer() {
        return benutzer;
    }

    public double getBetrag() {
        return betrag;
    }

    public String getCoupon() {
        return coupon;
    }

    public String getBestelldetails() {
        return bestelldetails;
    }

    public String getAnmerkungen() {
        return anmerkungen;
    }

    public String getZeit() {
        return zeit;
    }

    public boolean isFertig() {
        return fertig;
    }

    public void setBestellungAlsFertig(Datenbank db) {
        if (!fertig) {
            fertig = true;
            String query = "UPDATE bestellung SET fertig = 1 WHERE id = " + id;
            int bestellID = db.update(query);
            System.out.println("Bestellung " + bestellID + " wurde als fertig markiert.");
        }
    }

}
