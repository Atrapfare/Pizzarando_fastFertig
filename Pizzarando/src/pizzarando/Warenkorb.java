package pizzarando;

import java.util.ArrayList;

public class Warenkorb {

    private ArrayList<Pizza> warenkorbInhalt;
    private String anmerkungen;
    private double preis;

    public Warenkorb(Pizza leererPizzSlot) {
        this.warenkorbInhalt = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            this.warenkorbInhalt.add(leererPizzSlot);
        }
        this.preis = 0;
    }

    public int addPizza(Pizza pizza) {
        boolean voll = true;
        for (int i = 0; i < warenkorbInhalt.size(); i++) {
            if (warenkorbInhalt.get(i).getTyp().equals("noch frei")) {
                voll = false;
            }
        }
        if (voll) {
            System.out.println("Pizza nicht hinzugefügt.");
        } else {
            for (int i = 0; i < warenkorbInhalt.size(); i++) {
                if (warenkorbInhalt.get(i).getTyp().equals("noch frei")) {
                    warenkorbInhalt.set(i, pizza);
                    System.out.println("Pizza " + pizza.getTyp() + " wurde dem Warenkorb an Stelle " + (i + 1) + " hinzugefügt.");
                    return i;
                }
            }
        }
        return 0;
    }

    public ArrayList<Pizza> getWarenkorbItems() {
        return warenkorbInhalt;
    }

    public void setWarenkorbInhalt(ArrayList<Pizza> warenkorbInhalt) {
        this.warenkorbInhalt = warenkorbInhalt;
    }

    public void setPizza(Pizza pizza, int slot) {
        this.warenkorbInhalt.set(slot, pizza);
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
        if (warenkorbInhalt.get(0).getTyp().equals("noch frei")) {
            if (warenkorbInhalt.get(1).getTyp().equals("noch frei")) {
                if (warenkorbInhalt.get(2).getTyp().equals("noch frei")) {
                    if (warenkorbInhalt.get(3).getTyp().equals("noch frei")) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public int getNaechsterFreierSlot() {
        for (int i = 0; i < warenkorbInhalt.size(); i++) {
            if (warenkorbInhalt.get(i).getTyp().equals("noch frei")) {
                return i;
            }
        }
        return 4;
    }
}
