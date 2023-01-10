/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzarando;

/**
 *
 * @author dominikknaup
 */
public class Adresse {
    private String straße;
    private int hausnummer;
    private int plz;
    private String ort;

    public Adresse(String straße, int hausnummer, int plz, String ort) {
        this.straße = straße;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
    }

    public String getStraße() {
        return straße;
    }

    public int getHausnummer() {
        return hausnummer;
    }

    public int getPlz() {
        return plz;
    }

    public String getOrt() {
        return ort;
    }
    
    public String getVolleAdresse() {
        return straße + ", " + hausnummer + ", " + plz + ", " + ort;
    }
}
