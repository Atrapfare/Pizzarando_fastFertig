/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzarando;

import java.util.ArrayList;

/**
 *
 * @author dominikknaup
 */
public class Warenkorb {

    private ArrayList<Pizza> warenkorb;
    private String anmerkungen;
    private double preis;

    public Warenkorb(Pizza leererPizzSlot) {
        this.warenkorb = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            this.warenkorb.add(leererPizzSlot);
        }
        this.preis = 0;
    }

    public int addPizza(Pizza pizza) {
        boolean voll = true;
        for (int i = 0; i < warenkorb.size(); i++) {
            if (warenkorb.get(i).getTyp().equals("noch frei")) {
                voll = false;
            }
        }
        if (voll) {
            System.out.println("Pizza nicht hinzugefügt.");
        } else {
            for (int i = 0; i < warenkorb.size(); i++) {
                if (warenkorb.get(i).getTyp().equals("noch frei")) {
                    warenkorb.set(i, pizza);
                    System.out.println("Pizza " + pizza.getTyp() + " wurde dem Warenkorb an Stelle " + (i + 1) + " hinzugefügt.");
                    return i;
                }
            }
        }
        return 0;
    }

    public ArrayList<Pizza> getWarenkorbItems() {
        return warenkorb;
    }

    public void setWarenkorb(ArrayList<Pizza> warenkorb) {
        this.warenkorb = warenkorb;
    }

    public void setPizza(Pizza pizza, int slot) {
        this.warenkorb.set(slot, pizza);
    }

    public String getAnmerkungen() {
        return anmerkungen;
    }

    public void setAnmerkungen(String anmerkungen) {
        this.anmerkungen = anmerkungen;
    }

    public boolean isVoll() {
        if (getNaechsterFreierSlot() != 4) {
            return false;
        }
        return true;
    }

    public boolean isLeer() {
        if (warenkorb.get(0).getTyp().equals("noch frei")) {
            if (warenkorb.get(1).getTyp().equals("noch frei")) {
                if (warenkorb.get(2).getTyp().equals("noch frei")) {
                    if (warenkorb.get(3).getTyp().equals("noch frei")) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public int getNaechsterFreierSlot() {
        for (int i = 0; i < warenkorb.size(); i++) {
            if (warenkorb.get(i).getTyp().equals("noch frei")) {
                return i;
            }
        }
        return 4;
    }
}
