/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzarando;

/**
 *
 * @author dominikknaup
 */
public class Pizza {

    // Objekt wird erstellt, sobald Pizza in Bestellscreen "zum Warenkorb" hinzugefügt wurde
    // Wird nochmal draufgedrückt, wird ein weiteres Objekt hinzugefgt
    private String typ;
    private int anzahl;
    private int toppingAnzahl;
    
    private boolean tomatensoße;
    private boolean kaese;
    private boolean rucula;
    private boolean salami;
    private boolean zwiebeln;

    public Pizza(String typ, int anzahl, boolean tomatensoße, boolean kaese, boolean rucula, boolean salami, boolean zwiebeln) {
        this.typ = typ;
        this.anzahl = anzahl;
        this.tomatensoße = tomatensoße;
        this.kaese = kaese;
        this.rucula = rucula;
        this.salami = salami;
        this.zwiebeln = zwiebeln;
        berechneToppingAnzahl();
    }

    public void edit(int anzahl, boolean tomatensoße, boolean kaese, boolean rucula, boolean salami, boolean zwiebeln) {
        this.anzahl = anzahl;
        this.tomatensoße = tomatensoße;
        this.kaese = kaese;
        this.rucula = rucula;
        this.salami = salami;
        this.zwiebeln = zwiebeln;
        berechneToppingAnzahl();
    }

    public String getTyp() {
        return typ;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public boolean isTomatensoße() {
        return tomatensoße;
    }

    public boolean isKaese() {
        return kaese;
    }

    public boolean isRucula() {
        return rucula;
    }

    public boolean isSalami() {
        return salami;
    }

    public boolean isZwiebeln() {
        return zwiebeln;
    }
    
    private void berechneToppingAnzahl() {
        int toppingAnzahl = 0;
        if (tomatensoße) {
            toppingAnzahl++;
        }
        if (kaese) {
            toppingAnzahl++;
        }
        if (rucula) {
            toppingAnzahl++;
        }
        if (salami) {
            toppingAnzahl++;
        }
        if (zwiebeln) {
            toppingAnzahl++;
        }
        this.toppingAnzahl = toppingAnzahl;
    }

    public int getToppingAnzahl() {
        return toppingAnzahl;
    }
    
    
}
