package pizzarando;

import java.awt.Color;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author felix
 */
public class Pizzarando extends javax.swing.JFrame {

    Color hover_main = new Color(255, 164, 92);
    Color main = new Color(253, 150, 68);
    Color pizza_main = new Color(250, 130, 49);
    Color standart = UIManager.getColor("Panel.background");
    Color hover_del = new Color(168, 94, 89);
    Color del = new Color(170, 74, 68);
    Color disabled = new Color(125, 125, 125);

    public int orderSalami = 1, orderSpeciale = 1, orderDiavolo = 1, orderStagioni = 1, orderRucula = 1, orderCaprese = 1, orderFunghi = 1, orderMargherita = 1, orderPepperoni = 1, bearbeiten = 1;
    private boolean jP_landing_Cart_kasse_Order_enable = false, isLogin;

    private Datenbank db;
    private Benutzer angemeldeterBenutzer = null;
    private Warenkorb warenkorb;

    private int aktuellerWarenkorbIndex = 0;

    private double warenkorbGesamtBetrag = 0;
    private double kassenGesamtBetrag = 0;
    private boolean couponHinzugefügt = false;
    private boolean couponWarningVerstanden = false;
    private Bestellungsinfo aktuellAusgewählteBestellungImAdminMenu;

    private ArrayList<Bestellungsinfo> alleNochOffenenBestellungen = new ArrayList();

    public Pizzarando() {

        initComponents();
        customizeGUI();

        ImageIcon img = new ImageIcon("/Img/logo -125.png");
        this.setIconImage(img.getImage());

        status_Home(true);
        status_Bestellen(false);
        status_Benutzer(false);
        status_Admin(false);
        status_login(false);

        // Datenbankverbindung herstellen
        this.db = new Datenbank();
        Pizza leerePizza = new Pizza("noch frei", 1, false, false, false, false, false);
        this.warenkorb = new Warenkorb(leerePizza);

        try {
            // Benutzer ggf. lokal und anschließend aus DB laden
            if ((angemeldeterBenutzer = db.getBenutzerByHash(db.ladeEmailHashLokal())) != null) {
                status_login(true);
                updateGUI();
                aktualisiereHome();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Fehler bei einer lokalen Dateiabfrage.");
            System.err.println(ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Fehler bei einer Datenbankabfrage.");
            System.err.println(ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(rootPane, "Interner Fehler.");
            System.err.println(ex.getMessage());
        }

    }

    private void customizeGUI() {
        txt_user_passwort.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txt_user_nachname.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txt_user_straße.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txt_user_ort.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txt_user_vorname.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txt_user_email.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txt_login_email.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txt_user_hausnummer.setHorizontalAlignment(txt_user_hausnummer.CENTER);
        txt_user_plz.setHorizontalAlignment(txt_user_plz.CENTER);
        txt_user_passwort.setEchoChar('\u2022');
        txt_login_passwort.setEchoChar('\u2022');

        jP_landing_Cart_kasse_Order.setEnabled(false);
        jP_landing_Cart_kasse_Order_enable = false;
        jP_landing_Cart_kasse_Order.setBackground(disabled);

        btn_benutzer_ausloggen.setVisible(false);
        btn_benutzer_ausloggen.setEnabled(false);
    }

    private void status_Home(boolean status) //Enable or Disable Home
    {
        jP_landing_Home.setVisible(status);
        jP_landing_Home.setEnabled(status);
    }

    private void status_Bestellen(boolean status) //Enable or Disable Bestellen
    {
        jP_landing_Bestellen.setVisible(status);
        jP_landing_Bestellen.setEnabled(status);
    }

    private void status_Benutzer(boolean status) //Enable or Disable Benutzer
    {
        jP_landing_Benutzer.setVisible(status);
        jP_landing_Benutzer.setEnabled(status);
    }

    private void status_Admin(boolean status) //Enable or Disable Admin
    {
        jP_landing_Admin.setVisible(status);
        jP_landing_Admin.setEnabled(status);
    }

    private void status_Cart(boolean status) //Enable or Disable Cart
    {
        jP_landing_Cart.setVisible(status);
        jP_landing_Cart.setEnabled(status);
    }

    private void status_Bestellen_home(boolean status) //Enable or Disable
    {
        jP_landing_Bestellen_home.setVisible(status);
        jP_landing_Bestellen_home.setEnabled(status);
    }

    private void status_Bestellen_salami(boolean status) //Enable or Disable
    {
        jP_landing_Bestellen_salami.setVisible(status);
        jP_landing_Bestellen_salami.setEnabled(status);
    }

    private void status_Bestellen_speciale(boolean status) //Enable or Disable
    {
        jP_landing_Bestellen_speciale.setVisible(status);
        jP_landing_Bestellen_speciale.setEnabled(status);
    }

    private void status_Bestellen_diavolo(boolean status) //Enable or Disable
    {
        jP_landing_Bestellen_diavolo.setVisible(status);
        jP_landing_Bestellen_diavolo.setEnabled(status);
    }

    private void status_Bestellen_stagioni(boolean status) //Enable or Disable
    {
        jP_landing_Bestellen_stagioni.setVisible(status);
        jP_landing_Bestellen_stagioni.setEnabled(status);
    }

    private void status_Bestellen_rucula(boolean status) //Enable or Disable
    {
        jP_landing_Bestellen_rucula.setVisible(status);
        jP_landing_Bestellen_rucula.setEnabled(status);
    }

    private void status_Bestellen_caprese(boolean status) //Enable or Disable
    {
        jP_landing_Bestellen_caprese.setVisible(status);
        jP_landing_Bestellen_caprese.setEnabled(status);
    }

    private void status_Bestellen_funghi(boolean status) //Enable or Disable
    {
        jP_landing_Bestellen_funghi.setVisible(status);
        jP_landing_Bestellen_funghi.setEnabled(status);
    }

    private void status_Bestellen_margherita(boolean status) //Enable or Disable
    {
        jP_landing_Bestellen_margherita.setVisible(status);
        jP_landing_Bestellen_margherita.setEnabled(status);
    }

    private void status_Bestellen_pepperoni(boolean status) //Enable or Disable
    {
        jP_landing_Bestellen_pepperoni.setVisible(status);
        jP_landing_Bestellen_pepperoni.setEnabled(status);
    }

    private void status_Cart_main(boolean status) {
        jP_landing_Cart_main.setVisible(status);
        jP_landing_Cart_main.setEnabled(status);
    }

    private void status_Cart_kasse(boolean status) {
        jP_landing_Cart_kasse.setVisible(status);
        jP_landing_Cart_kasse.setEnabled(status);
    }

    private void satuts_Cart_bearbeiten(boolean status) {
        jP_landing_Cart_bearbeiten.setVisible(status);
        jP_landing_Cart_bearbeiten.setEnabled(status);
    }

    private void status_Admin_main(boolean status) {
        jP_landing_Admin_main.setVisible(status);
        jP_landing_Admin_main.setEnabled(status);
    }

    private void status_Admin_info(boolean status) {
        //jP_landing_Admin_info.setVisible(status);
        //jP_landing_Admin_info.setEnabled(status);
    }

    private void status_login(boolean eingeloggt) {
        jP_landing_Home_login.setVisible(!eingeloggt);
        jP_landing_Home_login.setEnabled(!eingeloggt);
        isLogin = eingeloggt;

        jP_order.setEnabled(eingeloggt);
        jP_cart.setEnabled(eingeloggt);
        jP_admin.setEnabled(eingeloggt);

        if (!eingeloggt) {
            txt_Benutzer.setText("Registrieren");
            txt_Home.setText("Login");

            jP_order.setBackground(disabled);
            jP_cart.setBackground(disabled);
            jP_admin.setBackground(disabled);
        } else {
            txt_Benutzer.setText("Benutzer");
            txt_Home.setText("Home");
            status_Home_main(true);

            jP_order.setBackground(main);
            jP_cart.setBackground(main);
            jP_admin.setBackground(main);
        }
    }

    private void status_Home_main(boolean status) {
        jP_landing_Home_main.setVisible(status);
        jP_landing_Home_main.setEnabled(status);
    }

    private void status_Cart_ordered(boolean status) {
        jP_landing_Cart_ordered.setVisible(status);
        jP_landing_Cart_ordered.setEnabled(status);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel42 = new javax.swing.JLabel();
        bG_landing_Cart_kasse_Zahlungsmethoden = new javax.swing.ButtonGroup();
        jP_control = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jP_home = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_Home = new javax.swing.JLabel();
        jP_order = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jP_user = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txt_Benutzer = new javax.swing.JLabel();
        jP_admin = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jP_cart = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jP_main = new javax.swing.JPanel();
        jP_landing_Benutzer = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jP_user_login = new javax.swing.JPanel();
        txt_user_hausnummer = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txt_user_plz = new javax.swing.JTextField();
        txt_user_ort = new javax.swing.JTextField();
        txt_user_straße = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txt_user_nachname = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txt_user_vorname = new javax.swing.JTextField();
        lbl_landing_benutzer_passwort = new javax.swing.JLabel();
        txt_user_passwort = new javax.swing.JPasswordField();
        txt_user_email = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        btn_benutzer_ausloggen = new javax.swing.JLabel();
        jP_user_btn = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jP_landing_Home = new javax.swing.JPanel();
        jP_landing_Home_login = new javax.swing.JPanel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        cB_login_showPassword = new javax.swing.JCheckBox();
        txt_login_passwort = new javax.swing.JPasswordField();
        jLabel85 = new javax.swing.JLabel();
        txt_login_email = new javax.swing.JTextField();
        jLabel86 = new javax.swing.JLabel();
        jP_login_btn = new javax.swing.JPanel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        txt_login_gotoRegister = new javax.swing.JLabel();
        jP_landing_Home_main = new javax.swing.JPanel();
        jLabel134 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        jLabel136 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        jLabel139 = new javax.swing.JLabel();
        jLabel148 = new javax.swing.JLabel();
        txt_landing_home_aktuelleBestellung = new javax.swing.JLabel();
        jP_landing_Admin = new javax.swing.JPanel();
        jP_landing_Admin_main = new javax.swing.JPanel();
        jLabel140 = new javax.swing.JLabel();
        txt_admin_Infos_Bestellung = new javax.swing.JScrollPane();
        list_admin_bestellungen = new javax.swing.JList<>();
        lbl_landing_admin_bestellinfos = new javax.swing.JLabel();
        jP_admin_bestellung_aktualisieren = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jP_admin_bestellung_check = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jP_landing_Cart = new javax.swing.JPanel();
        jP_landing_Cart_main = new javax.swing.JPanel();
        jP_landing_Cart_pizza_zurKasse = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jP_landing_Cart_pizza1 = new javax.swing.JPanel();
        jP_landing_Cart_pizza_Loeschen1 = new javax.swing.JPanel();
        jp_landing_Cart_pizza_Loeschen1_Label = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Bearbeiten1 = new javax.swing.JPanel();
        jp_landing_Cart_Edit1_Label = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Icon1 = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Name1 = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Anzahl1 = new javax.swing.JPanel();
        jP_landing_Cart_pizza_Anzahl_nummer1 = new javax.swing.JLabel();
        jP_landing_Cart_pizza2 = new javax.swing.JPanel();
        jP_landing_Cart_pizza_Loeschen2 = new javax.swing.JPanel();
        jp_landing_Cart_pizza_Loeschen2_Label = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Bearbeiten2 = new javax.swing.JPanel();
        jp_landing_Cart_Edit2_Label = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Icon2 = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Name2 = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Anzahl2 = new javax.swing.JPanel();
        jP_landing_Cart_pizza_Anzahl_nummer2 = new javax.swing.JLabel();
        jP_landing_Cart_pizza3 = new javax.swing.JPanel();
        jP_landing_Cart_pizza_Loeschen3 = new javax.swing.JPanel();
        jp_landing_Cart_pizza_Loeschen3_Label = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Bearbeiten3 = new javax.swing.JPanel();
        jp_landing_Cart_Edit3_Label = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Icon3 = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Name3 = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Anzahl3 = new javax.swing.JPanel();
        jP_landing_Cart_pizza_Anzahl_nummer3 = new javax.swing.JLabel();
        jP_landing_Cart_pizza4 = new javax.swing.JPanel();
        jP_landing_Cart_pizza_Loeschen4 = new javax.swing.JPanel();
        jp_landing_Cart_pizza_Loeschen4_Label = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Bearbeiten4 = new javax.swing.JPanel();
        jp_landing_Cart_Edit4_Label = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Icon4 = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Name4 = new javax.swing.JLabel();
        jP_landing_Cart_pizza_Anzahl4 = new javax.swing.JPanel();
        jP_landing_Cart_pizza_Anzahl_nummer4 = new javax.swing.JLabel();
        lbl_landing_Cart_Warenkorbbetrag = new javax.swing.JLabel();
        jP_landing_Cart_kasse = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        jP_landing_Cart_kasse_main = new javax.swing.JPanel();
        jLabel81 = new javax.swing.JLabel();
        jP_landing_Cart_kasse_Back = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        jP_landing_Cart_kasse_Order = new javax.swing.JPanel();
        jLabel70 = new javax.swing.JLabel();
        jP_landing_Cart_kasse_Adresse = new javax.swing.JPanel();
        lbl_landing_Cart_pizza_Adresse = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        lbl_landing_Cart_kasse_Gesamtbetrag = new javax.swing.JLabel();
        txt_landing_kasse_coupon = new javax.swing.JTextField();
        cB_Cart_Bar = new javax.swing.JCheckBox();
        jLabel98 = new javax.swing.JLabel();
        jP_landing_Cart_kasse_Coupon_Prüfen = new javax.swing.JPanel();
        lbl_landing_Cart_kasse_Coupon_Prüfen = new javax.swing.JLabel();
        lbl_for_kassegesamt = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txa_landing_kasse_Extra = new javax.swing.JTextArea();
        jP_landing_Cart_ordered = new javax.swing.JPanel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jP_landing_Cart_ordermore = new javax.swing.JPanel();
        jLabel89 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jP_landing_Cart_bearbeiten = new javax.swing.JPanel();
        jp_landing_cart_bearbeiten_icon = new javax.swing.JLabel();
        jLabel142 = new javax.swing.JLabel();
        cB_bearbeiten_Rucola = new javax.swing.JCheckBox();
        cB_bearbeiten_Salami = new javax.swing.JCheckBox();
        cB_bearbeiten_Kaese = new javax.swing.JCheckBox();
        cB_bearbeiten_Zwiebeln = new javax.swing.JCheckBox();
        cB_bearbeiten_Tomatensosse = new javax.swing.JCheckBox();
        jP_landing_Cart_bearbeiten_Back = new javax.swing.JPanel();
        jLabel144 = new javax.swing.JLabel();
        jP_landing_Cart_bearbeiten_toCart = new javax.swing.JPanel();
        jLabel145 = new javax.swing.JLabel();
        jP_landing_Cart_bearbeiten_Plus = new javax.swing.JPanel();
        jLabel146 = new javax.swing.JLabel();
        jP_landing_Cart_bearbeiten_Count = new javax.swing.JPanel();
        jP_landing_Cart_bearbeiten_Counter = new javax.swing.JLabel();
        jP_landing_Cart_bearbeiten_Minus = new javax.swing.JPanel();
        jLabel147 = new javax.swing.JLabel();
        jp_landing_cart_bearbeiten_title = new javax.swing.JLabel();
        jP_landing_Bestellen = new javax.swing.JPanel();
        jP_landing_Bestellen_home = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jP_Bestellen_salami = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jP_Bestellen_speciale = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jP_Bestellen_diavolo = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jP_Bestellen_stagioni = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jP_Bestellen_rucula = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jP_Bestellen_caprese = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jP_Bestellen_pepperoni = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jP_Bestellen_margherita = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jP_Bestellen_funghi = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jP_landing_Bestellen_salami = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        cB_salami_Rucola = new javax.swing.JCheckBox();
        cB_salami_Salami = new javax.swing.JCheckBox();
        cB_salami_Kaese = new javax.swing.JCheckBox();
        cB_salami_Zwiebeln = new javax.swing.JCheckBox();
        cB_salami_Tomatensosse = new javax.swing.JCheckBox();
        jLabel44 = new javax.swing.JLabel();
        jP_landing_Bestellen_salami_Back = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jP_landing_Bestellen_salami_toCart = new javax.swing.JPanel();
        jp_landing_Bestellen_salami_toCart_Label = new javax.swing.JLabel();
        jP_landing_Bestellen_salami_Plus = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jP_landing_Bestellen_salami_Count = new javax.swing.JPanel();
        jP_landing_Bestellen_salami_Counter = new javax.swing.JLabel();
        jP_landing_Bestellen_salami_Minus = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jP_landing_Bestellen_speciale = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        cB_speciale_Rucola = new javax.swing.JCheckBox();
        cB_speciale_Salami = new javax.swing.JCheckBox();
        cB_speciale_Kaese = new javax.swing.JCheckBox();
        cB_speciale_Zwiebeln = new javax.swing.JCheckBox();
        cB_speciale_Tomatensosse = new javax.swing.JCheckBox();
        jLabel51 = new javax.swing.JLabel();
        jP_landing_Bestellen_speciale_Back = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jP_landing_Bestellen_speciale_toCart = new javax.swing.JPanel();
        jp_landing_Bestellen_speciale_toCart_Label = new javax.swing.JLabel();
        jP_landing_Bestellen_speciale_Plus = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jP_landing_Bestellen_speciale_Count = new javax.swing.JPanel();
        jP_landing_Bestellen_speciale_Counter = new javax.swing.JLabel();
        jP_landing_Bestellen_speciale_Minus = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jP_landing_Bestellen_diavolo = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        cB_diavolo_Rucola = new javax.swing.JCheckBox();
        cB_diavolo_Salami = new javax.swing.JCheckBox();
        cB_diavolo_Kaese = new javax.swing.JCheckBox();
        cB_diavolo_Zwiebeln = new javax.swing.JCheckBox();
        cB_diavolo_Tomatensosse = new javax.swing.JCheckBox();
        jLabel59 = new javax.swing.JLabel();
        jP_landing_Bestellen_diavolo_Back = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jP_landing_Bestellen_diavolo_toCart = new javax.swing.JPanel();
        jp_landing_Bestellen_diavolo_toCart_Label = new javax.swing.JLabel();
        jP_landing_Bestellen_diavolo_Plus = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        jP_landing_Bestellen_diavolo_Count = new javax.swing.JPanel();
        jP_landing_Bestellen_diavolo_Counter = new javax.swing.JLabel();
        jP_landing_Bestellen_diavolo_Minus = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        TEXT9999999 = new javax.swing.JLabel();
        jP_landing_Bestellen_stagioni = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        cB_stagioni_Rucola = new javax.swing.JCheckBox();
        cB_stagioni_Salami = new javax.swing.JCheckBox();
        cB_stagioni_Kaese = new javax.swing.JCheckBox();
        cB_stagioni_Zwiebeln = new javax.swing.JCheckBox();
        cB_stagioni_Tomatensosse = new javax.swing.JCheckBox();
        jLabel73 = new javax.swing.JLabel();
        jP_landing_Bestellen_stagioni_Back = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        jP_landing_Bestellen_stagioni_toCart = new javax.swing.JPanel();
        jp_landing_Bestellen_stagioni_toCart_Label = new javax.swing.JLabel();
        jP_landing_Bestellen_stagioni_Plus = new javax.swing.JPanel();
        jLabel76 = new javax.swing.JLabel();
        jP_landing_Bestellen_stagioni_Count = new javax.swing.JPanel();
        jP_landing_Bestellen_stagioni_Counter = new javax.swing.JLabel();
        jP_landing_Bestellen_stagioni_Minus = new javax.swing.JPanel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jP_landing_Bestellen_rucula = new javax.swing.JPanel();
        jLabel94 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        cB_rucula_Rucola = new javax.swing.JCheckBox();
        cB_rucula_Salami = new javax.swing.JCheckBox();
        cB_rucula_Kaese = new javax.swing.JCheckBox();
        cB_rucula_Zwiebeln = new javax.swing.JCheckBox();
        cB_rucula_Tomatensosse = new javax.swing.JCheckBox();
        jLabel96 = new javax.swing.JLabel();
        jP_landing_Bestellen_rucula_Back = new javax.swing.JPanel();
        jLabel97 = new javax.swing.JLabel();
        jP_landing_Bestellen_rucula_toCart = new javax.swing.JPanel();
        jp_landing_Bestellen_rucula_toCart_Label = new javax.swing.JLabel();
        jP_landing_Bestellen_rucula_Plus = new javax.swing.JPanel();
        jLabel99 = new javax.swing.JLabel();
        jP_landing_Bestellen_rucula_Count = new javax.swing.JPanel();
        jP_landing_Bestellen_rucula_Counter = new javax.swing.JLabel();
        jP_landing_Bestellen_rucula_Minus = new javax.swing.JPanel();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jP_landing_Bestellen_caprese = new javax.swing.JPanel();
        jLabel102 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        cB_caprese_Rucola = new javax.swing.JCheckBox();
        cB_caprese_Salami = new javax.swing.JCheckBox();
        cB_caprese_Kaese = new javax.swing.JCheckBox();
        cB_caprese_Zwiebeln = new javax.swing.JCheckBox();
        cB_caprese_Tomatensosse = new javax.swing.JCheckBox();
        jLabel104 = new javax.swing.JLabel();
        jP_landing_Bestellen_caprese_Back = new javax.swing.JPanel();
        jLabel105 = new javax.swing.JLabel();
        jP_landing_Bestellen_caprese_toCart = new javax.swing.JPanel();
        jp_landing_Bestellen_caprese_toCart_Label = new javax.swing.JLabel();
        jP_landing_Bestellen_caprese_Plus = new javax.swing.JPanel();
        jLabel107 = new javax.swing.JLabel();
        jP_landing_Bestellen_caprese_Count = new javax.swing.JPanel();
        jP_landing_Bestellen_caprese_Counter = new javax.swing.JLabel();
        jP_landing_Bestellen_caprese_Minus = new javax.swing.JPanel();
        jLabel108 = new javax.swing.JLabel();
        jLabel109 = new javax.swing.JLabel();
        jP_landing_Bestellen_funghi = new javax.swing.JPanel();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        cB_funghi_Rucola = new javax.swing.JCheckBox();
        cB_funghi_Salami = new javax.swing.JCheckBox();
        cB_funghi_Kaese = new javax.swing.JCheckBox();
        cB_funghi_Zwiebeln = new javax.swing.JCheckBox();
        cB_funghi_Tomatensosse = new javax.swing.JCheckBox();
        jLabel112 = new javax.swing.JLabel();
        jP_landing_Bestellen_funghi_Back = new javax.swing.JPanel();
        jLabel113 = new javax.swing.JLabel();
        jP_landing_Bestellen_funghi_toCart = new javax.swing.JPanel();
        jp_landing_Bestellen_funghi_toCart_Label = new javax.swing.JLabel();
        jP_landing_Bestellen_funghi_Plus = new javax.swing.JPanel();
        jLabel115 = new javax.swing.JLabel();
        jP_landing_Bestellen_funghi_Count = new javax.swing.JPanel();
        jP_landing_Bestellen_funghi_Counter = new javax.swing.JLabel();
        jP_landing_Bestellen_funghi_Minus = new javax.swing.JPanel();
        jLabel116 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        jP_landing_Bestellen_margherita = new javax.swing.JPanel();
        jLabel118 = new javax.swing.JLabel();
        jLabel119 = new javax.swing.JLabel();
        cB_margherita_Rucola = new javax.swing.JCheckBox();
        cB_margherita_Salami = new javax.swing.JCheckBox();
        cB_margherita_Kaese = new javax.swing.JCheckBox();
        cB_margherita_Zwiebeln = new javax.swing.JCheckBox();
        cB_margherita_Tomatensosse = new javax.swing.JCheckBox();
        jLabel120 = new javax.swing.JLabel();
        jP_landing_Bestellen_margherita_Back = new javax.swing.JPanel();
        jLabel121 = new javax.swing.JLabel();
        jP_landing_Bestellen_margherita_toCart = new javax.swing.JPanel();
        jp_landing_Bestellen_margherita_toCart_Label = new javax.swing.JLabel();
        jP_landing_Bestellen_margherita_Plus = new javax.swing.JPanel();
        jLabel123 = new javax.swing.JLabel();
        jP_landing_Bestellen_margherita_Count = new javax.swing.JPanel();
        jP_landing_Bestellen_margherita_Counter = new javax.swing.JLabel();
        jP_landing_Bestellen_margherita_Minus = new javax.swing.JPanel();
        jLabel124 = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        jP_landing_Bestellen_pepperoni = new javax.swing.JPanel();
        jLabel126 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        cB_pepperoni_Rucola = new javax.swing.JCheckBox();
        cB_pepperoni_Salami = new javax.swing.JCheckBox();
        cB_pepperoni_Kaese = new javax.swing.JCheckBox();
        cB_pepperoni_Zwiebeln = new javax.swing.JCheckBox();
        cB_pepperoni_Tomatensosse = new javax.swing.JCheckBox();
        jLabel128 = new javax.swing.JLabel();
        jP_landing_pepperoni_salami_Back = new javax.swing.JPanel();
        jLabel129 = new javax.swing.JLabel();
        jP_landing_pepperoni_salami_toCart = new javax.swing.JPanel();
        jp_landing_Bestellen_pepperoni_toCart_Label = new javax.swing.JLabel();
        jP_landing_pepperoni_salami_Plus = new javax.swing.JPanel();
        jLabel131 = new javax.swing.JLabel();
        jP_landing_pepperoni_salami_Count = new javax.swing.JPanel();
        jP_landing_Bestellen_pepperoni_Counter = new javax.swing.JLabel();
        jP_landing_pepperoni_salami_Minus = new javax.swing.JPanel();
        jLabel132 = new javax.swing.JLabel();
        jLabel133 = new javax.swing.JLabel();

        jLabel42.setText("jLabel42");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pizzarando");

        jP_control.setBackground(new java.awt.Color(250, 130, 49));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/logo -125.png"))); // NOI18N
        jLabel1.setText("img_logo");

        jP_home.setBackground(new java.awt.Color(253, 150, 68));
        jP_home.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_homeMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_homeMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_homeMouseEntered(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8-house-48-white.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        txt_Home.setBackground(new java.awt.Color(0, 0, 0));
        txt_Home.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        txt_Home.setForeground(new java.awt.Color(250, 250, 250));
        txt_Home.setText("Home");
        txt_Home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_HomeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jP_homeLayout = new javax.swing.GroupLayout(jP_home);
        jP_home.setLayout(jP_homeLayout);
        jP_homeLayout.setHorizontalGroup(
            jP_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_homeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Home)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jP_homeLayout.setVerticalGroup(
            jP_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_homeLayout.createSequentialGroup()
                .addGroup(jP_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_homeLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2))
                    .addGroup(jP_homeLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(txt_Home)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_order.setBackground(new java.awt.Color(253, 150, 68));
        jP_order.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_order.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_orderMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_orderMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_orderMouseEntered(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rsz_icons8-pizza-five-eighths-50_white.png"))); // NOI18N
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(250, 250, 250));
        jLabel5.setText("Bestellen");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jP_orderLayout = new javax.swing.GroupLayout(jP_order);
        jP_order.setLayout(jP_orderLayout);
        jP_orderLayout.setHorizontalGroup(
            jP_orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_orderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jP_orderLayout.setVerticalGroup(
            jP_orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_orderLayout.createSequentialGroup()
                .addGroup(jP_orderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_orderLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4))
                    .addGroup(jP_orderLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel5)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_user.setBackground(new java.awt.Color(253, 150, 68));
        jP_user.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_user.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_userMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_userMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_userMouseExited(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rsz_icons8-user-60.png"))); // NOI18N
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        txt_Benutzer.setBackground(new java.awt.Color(0, 0, 0));
        txt_Benutzer.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        txt_Benutzer.setForeground(new java.awt.Color(250, 250, 250));
        txt_Benutzer.setText("Benutzer");
        txt_Benutzer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_BenutzerMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jP_userLayout = new javax.swing.GroupLayout(jP_user);
        jP_user.setLayout(jP_userLayout);
        jP_userLayout.setHorizontalGroup(
            jP_userLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_userLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Benutzer)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jP_userLayout.setVerticalGroup(
            jP_userLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_userLayout.createSequentialGroup()
                .addGroup(jP_userLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_userLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6))
                    .addGroup(jP_userLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(txt_Benutzer)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_admin.setBackground(new java.awt.Color(253, 150, 68));
        jP_admin.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_admin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_adminMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_adminMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_adminMouseEntered(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rsz_icons8-in-transit-50.png"))); // NOI18N
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(250, 250, 250));
        jLabel9.setText("Für Lieferanten");
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jP_adminLayout = new javax.swing.GroupLayout(jP_admin);
        jP_admin.setLayout(jP_adminLayout);
        jP_adminLayout.setHorizontalGroup(
            jP_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_adminLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jP_adminLayout.setVerticalGroup(
            jP_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_adminLayout.createSequentialGroup()
                .addGroup(jP_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_adminLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8))
                    .addGroup(jP_adminLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel9)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_cart.setBackground(new java.awt.Color(253, 150, 68));
        jP_cart.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_cart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_cartMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_cartMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_cartMouseEntered(evt);
            }
        });

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rsz_icons8-shopping-cart-64_2.png"))); // NOI18N
        jLabel37.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel37MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel37MouseEntered(evt);
            }
        });

        jLabel38.setBackground(new java.awt.Color(0, 0, 0));
        jLabel38.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(250, 250, 250));
        jLabel38.setText("Warenkorb");
        jLabel38.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel38MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel38MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_cartLayout = new javax.swing.GroupLayout(jP_cart);
        jP_cart.setLayout(jP_cartLayout);
        jP_cartLayout.setHorizontalGroup(
            jP_cartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_cartLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel38)
                .addContainerGap(64, Short.MAX_VALUE))
        );
        jP_cartLayout.setVerticalGroup(
            jP_cartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_cartLayout.createSequentialGroup()
                .addGroup(jP_cartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_cartLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel37))
                    .addGroup(jP_cartLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel38)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jP_controlLayout = new javax.swing.GroupLayout(jP_control);
        jP_control.setLayout(jP_controlLayout);
        jP_controlLayout.setHorizontalGroup(
            jP_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_controlLayout.createSequentialGroup()
                .addGroup(jP_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jP_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jP_user, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jP_order, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jP_home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jP_admin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jP_cart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 25, Short.MAX_VALUE))
        );
        jP_controlLayout.setVerticalGroup(
            jP_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_controlLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jP_home, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jP_order, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jP_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jP_cart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addComponent(jP_admin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        jP_main.setLayout(new java.awt.CardLayout());

        jLabel36.setBackground(new java.awt.Color(0, 0, 0));
        jLabel36.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(250, 130, 49));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("Benutzer");

        txt_user_hausnummer.setBorder(null);

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(125, 125, 125));
        jLabel3.setText("Straße:");

        jLabel11.setBackground(new java.awt.Color(0, 0, 0));
        jLabel11.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(125, 125, 125));
        jLabel11.setText("Nr:");

        jLabel12.setBackground(new java.awt.Color(0, 0, 0));
        jLabel12.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(125, 125, 125));
        jLabel12.setText("PLZ:");

        jLabel13.setBackground(new java.awt.Color(0, 0, 0));
        jLabel13.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(125, 125, 125));
        jLabel13.setText("Ort:");

        txt_user_plz.setBorder(null);
        txt_user_plz.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_user_ort.setBorder(null);

        txt_user_straße.setBorder(null);
        txt_user_straße.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel14.setBackground(new java.awt.Color(0, 0, 0));
        jLabel14.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(125, 125, 125));
        jLabel14.setText("Name:");

        txt_user_nachname.setBorder(null);

        jLabel15.setBackground(new java.awt.Color(0, 0, 0));
        jLabel15.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(125, 125, 125));
        jLabel15.setText("Nachname:");

        txt_user_vorname.setBorder(null);
        txt_user_vorname.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_landing_benutzer_passwort.setBackground(new java.awt.Color(0, 0, 0));
        lbl_landing_benutzer_passwort.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        lbl_landing_benutzer_passwort.setForeground(new java.awt.Color(125, 125, 125));
        lbl_landing_benutzer_passwort.setText("Passwort:");

        txt_user_email.setBorder(null);

        jLabel67.setBackground(new java.awt.Color(0, 0, 0));
        jLabel67.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(125, 125, 125));
        jLabel67.setText("E-Mail:");

        btn_benutzer_ausloggen.setBackground(new java.awt.Color(0, 0, 0));
        btn_benutzer_ausloggen.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 18)); // NOI18N
        btn_benutzer_ausloggen.setForeground(new java.awt.Color(250, 130, 49));
        btn_benutzer_ausloggen.setText("ausloggen");
        btn_benutzer_ausloggen.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_benutzer_ausloggen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_benutzer_ausloggenMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_benutzer_ausloggenMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_benutzer_ausloggenMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_user_loginLayout = new javax.swing.GroupLayout(jP_user_login);
        jP_user_login.setLayout(jP_user_loginLayout);
        jP_user_loginLayout.setHorizontalGroup(
            jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_user_loginLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_benutzer_ausloggen)
                .addGap(126, 126, 126))
            .addGroup(jP_user_loginLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_user_loginLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addGap(80, 80, 80))
                    .addGroup(jP_user_loginLayout.createSequentialGroup()
                        .addComponent(txt_user_vorname, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jP_user_loginLayout.createSequentialGroup()
                        .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel67, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_landing_benutzer_passwort, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_user_loginLayout.createSequentialGroup()
                        .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_user_email, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_user_passwort))
                        .addContainerGap())))
            .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jP_user_loginLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jP_user_loginLayout.createSequentialGroup()
                            .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(txt_user_straße, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jP_user_loginLayout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(jP_user_loginLayout.createSequentialGroup()
                                    .addComponent(txt_user_hausnummer, javax.swing.GroupLayout.PREFERRED_SIZE, 54, Short.MAX_VALUE)
                                    .addContainerGap())))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_user_loginLayout.createSequentialGroup()
                            .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jP_user_loginLayout.createSequentialGroup()
                                    .addGap(0, 184, Short.MAX_VALUE)
                                    .addComponent(txt_user_nachname, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jP_user_loginLayout.createSequentialGroup()
                                    .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txt_user_plz, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12))
                                    .addGap(18, 18, 18)
                                    .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jP_user_loginLayout.createSequentialGroup()
                                            .addComponent(jLabel13)
                                            .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(txt_user_ort, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))))
                            .addContainerGap()))))
        );
        jP_user_loginLayout.setVerticalGroup(
            jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_user_loginLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_user_vorname, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                .addComponent(jLabel67)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_user_email, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_landing_benutzer_passwort)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_user_passwort, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_benutzer_ausloggen)
                .addGap(15, 15, 15))
            .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jP_user_loginLayout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(txt_user_nachname, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel11))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_user_hausnummer, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_user_straße, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(jLabel13))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jP_user_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_user_ort, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_user_plz, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(186, Short.MAX_VALUE)))
        );

        jP_user_btn.setBackground(new java.awt.Color(253, 150, 68));
        jP_user_btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jP_user_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_user_btnMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_user_btnMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_user_btnMouseEntered(evt);
            }
        });

        jLabel17.setBackground(new java.awt.Color(0, 0, 0));
        jLabel17.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(250, 250, 250));
        jLabel17.setText("Registrieren");

        javax.swing.GroupLayout jP_user_btnLayout = new javax.swing.GroupLayout(jP_user_btn);
        jP_user_btn.setLayout(jP_user_btnLayout);
        jP_user_btnLayout.setHorizontalGroup(
            jP_user_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_user_btnLayout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jP_user_btnLayout.setVerticalGroup(
            jP_user_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_user_btnLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout jP_landing_BenutzerLayout = new javax.swing.GroupLayout(jP_landing_Benutzer);
        jP_landing_Benutzer.setLayout(jP_landing_BenutzerLayout);
        jP_landing_BenutzerLayout.setHorizontalGroup(
            jP_landing_BenutzerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_BenutzerLayout.createSequentialGroup()
                .addGap(277, 277, 277)
                .addComponent(jLabel36)
                .addContainerGap(278, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_BenutzerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jP_landing_BenutzerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jP_user_login, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jP_user_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(208, 208, 208))
        );
        jP_landing_BenutzerLayout.setVerticalGroup(
            jP_landing_BenutzerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_BenutzerLayout.createSequentialGroup()
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jP_user_login, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jP_user_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        jP_main.add(jP_landing_Benutzer, "card2");

        jP_landing_Home.setLayout(new java.awt.CardLayout());

        jLabel83.setBackground(new java.awt.Color(0, 0, 0));
        jLabel83.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(250, 130, 49));
        jLabel83.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel83.setText("<html><p style=\"text-align:center;\">Bitte melde dich zuerst an, um PIZZARANDO nutzen zu können </p></html>");

        jLabel84.setBackground(new java.awt.Color(0, 0, 0));
        jLabel84.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel84.setForeground(new java.awt.Color(250, 130, 49));
        jLabel84.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel84.setText("Willkommen!");

        cB_login_showPassword.setText("Zeige Passwort");
        cB_login_showPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cB_login_showPasswordActionPerformed(evt);
            }
        });

        jLabel85.setBackground(new java.awt.Color(0, 0, 0));
        jLabel85.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(125, 125, 125));
        jLabel85.setText("Passwort:");

        txt_login_email.setBorder(null);

        jLabel86.setBackground(new java.awt.Color(0, 0, 0));
        jLabel86.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(125, 125, 125));
        jLabel86.setText("E-Mail:");

        jP_login_btn.setBackground(new java.awt.Color(253, 150, 68));
        jP_login_btn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_login_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_login_btnMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_login_btnMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_login_btnMouseEntered(evt);
            }
        });

        jLabel87.setBackground(new java.awt.Color(0, 0, 0));
        jLabel87.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel87.setForeground(new java.awt.Color(250, 250, 250));
        jLabel87.setText("Login");
        jLabel87.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel87MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_login_btnLayout = new javax.swing.GroupLayout(jP_login_btn);
        jP_login_btn.setLayout(jP_login_btnLayout);
        jP_login_btnLayout.setHorizontalGroup(
            jP_login_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_login_btnLayout.createSequentialGroup()
                .addGap(129, 129, 129)
                .addComponent(jLabel87)
                .addGap(129, 129, 129))
        );
        jP_login_btnLayout.setVerticalGroup(
            jP_login_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_login_btnLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel87)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cB_login_showPassword, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jP_login_btn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(123, 123, 123))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(122, 122, 122)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel85)
                            .addGap(378, 378, 378))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel86)
                                .addComponent(txt_login_email, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txt_login_passwort, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addGap(123, 123, 123)))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(177, Short.MAX_VALUE)
                .addComponent(cB_login_showPassword)
                .addGap(26, 26, 26)
                .addComponent(jP_login_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addComponent(jLabel86)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txt_login_email, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel85)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(txt_login_passwort, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(146, Short.MAX_VALUE)))
        );

        jLabel88.setBackground(new java.awt.Color(0, 0, 0));
        jLabel88.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel88.setForeground(new java.awt.Color(125, 125, 125));
        jLabel88.setText("Du Besitzt noch kein Account, dann regestriere dich");

        txt_login_gotoRegister.setBackground(new java.awt.Color(0, 0, 0));
        txt_login_gotoRegister.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 18)); // NOI18N
        txt_login_gotoRegister.setForeground(new java.awt.Color(250, 130, 49));
        txt_login_gotoRegister.setText("HIER");
        txt_login_gotoRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txt_login_gotoRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_login_gotoRegisterMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txt_login_gotoRegisterMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txt_login_gotoRegisterMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Home_loginLayout = new javax.swing.GroupLayout(jP_landing_Home_login);
        jP_landing_Home_login.setLayout(jP_landing_Home_loginLayout);
        jP_landing_Home_loginLayout.setHorizontalGroup(
            jP_landing_Home_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Home_loginLayout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addGroup(jP_landing_Home_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Home_loginLayout.createSequentialGroup()
                .addContainerGap(215, Short.MAX_VALUE)
                .addGroup(jP_landing_Home_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Home_loginLayout.createSequentialGroup()
                        .addComponent(jLabel84)
                        .addGap(229, 229, 229))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Home_loginLayout.createSequentialGroup()
                        .addComponent(jLabel88)
                        .addGap(204, 204, 204))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Home_loginLayout.createSequentialGroup()
                        .addComponent(txt_login_gotoRegister)
                        .addGap(350, 350, 350))))
        );
        jP_landing_Home_loginLayout.setVerticalGroup(
            jP_landing_Home_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Home_loginLayout.createSequentialGroup()
                .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel88)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_login_gotoRegister)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        jP_landing_Home.add(jP_landing_Home_login, "card2");

        jLabel134.setBackground(new java.awt.Color(0, 0, 0));
        jLabel134.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel134.setForeground(new java.awt.Color(255, 173, 109));
        jLabel134.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel134.setText("<html><p style=\"text-align:center;\">Gutscheincodes der Woche: 5%: Pizza5<br></p></html>");

        jLabel135.setBackground(new java.awt.Color(0, 0, 0));
        jLabel135.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel135.setForeground(new java.awt.Color(250, 130, 49));
        jLabel135.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel135.setText("Home");

        jLabel136.setBackground(new java.awt.Color(0, 0, 0));
        jLabel136.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel136.setForeground(new java.awt.Color(250, 130, 49));
        jLabel136.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel136.setText("<html><p style=\"text-align:center;\">Willkommen bei PIZZARANDO <br> Klicke auf \"Bestellen\" um deine Pizzawünsche aufzugeben </p></html>");

        jLabel137.setBackground(new java.awt.Color(0, 0, 0));
        jLabel137.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel137.setForeground(new java.awt.Color(125, 125, 125));
        jLabel137.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel137.setText("Dieses Pizza-Bestell Programm wurde im Rahmen des CT-Projektes von");
        jLabel137.setToolTipText("");

        jLabel138.setBackground(new java.awt.Color(0, 0, 0));
        jLabel138.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel138.setForeground(new java.awt.Color(255, 173, 109));
        jLabel138.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel138.setText("<html><p style=\"text-align:center;\">Dominik Knaup, Felix Möhrke, Patrik Fittipaldi, Philipp Hartwig</p></html>");

        jLabel139.setBackground(new java.awt.Color(0, 0, 0));
        jLabel139.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel139.setForeground(new java.awt.Color(125, 125, 125));
        jLabel139.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel139.setText("erstellt.");
        jLabel139.setToolTipText("");

        jLabel148.setBackground(new java.awt.Color(0, 0, 0));
        jLabel148.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 20)); // NOI18N
        jLabel148.setForeground(new java.awt.Color(250, 130, 49));
        jLabel148.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel148.setText("<html><p style=\"text-align:center;\">Letzte Bestellung:</p></html>");

        txt_landing_home_aktuelleBestellung.setBackground(new java.awt.Color(0, 0, 0));
        txt_landing_home_aktuelleBestellung.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        txt_landing_home_aktuelleBestellung.setForeground(new java.awt.Color(250, 130, 49));
        txt_landing_home_aktuelleBestellung.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_landing_home_aktuelleBestellung.setText("<html><p style=\"text-align:center;\">Keine letzte Bestellung gefunden!</p></html>");
        txt_landing_home_aktuelleBestellung.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jP_landing_Home_mainLayout = new javax.swing.GroupLayout(jP_landing_Home_main);
        jP_landing_Home_main.setLayout(jP_landing_Home_mainLayout);
        jP_landing_Home_mainLayout.setHorizontalGroup(
            jP_landing_Home_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Home_mainLayout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addGroup(jP_landing_Home_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Home_mainLayout.createSequentialGroup()
                        .addComponent(jLabel135)
                        .addGap(316, 316, 316))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Home_mainLayout.createSequentialGroup()
                        .addComponent(jLabel138, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(229, 229, 229))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Home_mainLayout.createSequentialGroup()
                        .addGroup(jP_landing_Home_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel137)
                            .addComponent(txt_landing_home_aktuelleBestellung, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jP_landing_Home_mainLayout.createSequentialGroup()
                                .addComponent(jLabel136, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)))
                        .addGap(79, 79, 79))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Home_mainLayout.createSequentialGroup()
                        .addComponent(jLabel139)
                        .addGap(365, 365, 365))))
            .addGroup(jP_landing_Home_mainLayout.createSequentialGroup()
                .addGroup(jP_landing_Home_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Home_mainLayout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(jLabel134, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jP_landing_Home_mainLayout.createSequentialGroup()
                        .addGap(296, 296, 296)
                        .addComponent(jLabel148, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jP_landing_Home_mainLayout.setVerticalGroup(
            jP_landing_Home_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Home_mainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel135, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel136, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(jLabel148, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_landing_home_aktuelleBestellung, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel134, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel137, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel138, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel139, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );

        jP_landing_Home.add(jP_landing_Home_main, "card2");

        jP_main.add(jP_landing_Home, "card3");

        jP_landing_Admin.setLayout(new java.awt.CardLayout());

        jLabel140.setBackground(new java.awt.Color(0, 0, 0));
        jLabel140.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel140.setForeground(new java.awt.Color(250, 130, 49));
        jLabel140.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel140.setText("Für Lieferanten");

        list_admin_bestellungen.setBackground(new java.awt.Color(250, 130, 49));
        list_admin_bestellungen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 130, 49), 3));
        list_admin_bestellungen.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 18)); // NOI18N
        list_admin_bestellungen.setForeground(new java.awt.Color(255, 255, 255));
        list_admin_bestellungen.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list_admin_bestellungenValueChanged(evt);
            }
        });
        txt_admin_Infos_Bestellung.setViewportView(list_admin_bestellungen);

        lbl_landing_admin_bestellinfos.setBackground(new java.awt.Color(0, 0, 0));
        lbl_landing_admin_bestellinfos.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        lbl_landing_admin_bestellinfos.setForeground(new java.awt.Color(125, 125, 125));
        lbl_landing_admin_bestellinfos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_landing_admin_bestellinfos.setText("<html><p style=\"text-align:left;\">Wählen Sie eine Bestellung aus.<br> </p></html>");

        jP_admin_bestellung_aktualisieren.setBackground(new java.awt.Color(89, 144, 227));
        jP_admin_bestellung_aktualisieren.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_admin_bestellung_aktualisierenMouseClicked(evt);
            }
        });

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/aktualisieren.png"))); // NOI18N
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jP_admin_bestellung_aktualisierenLayout = new javax.swing.GroupLayout(jP_admin_bestellung_aktualisieren);
        jP_admin_bestellung_aktualisieren.setLayout(jP_admin_bestellung_aktualisierenLayout);
        jP_admin_bestellung_aktualisierenLayout.setHorizontalGroup(
            jP_admin_bestellung_aktualisierenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_admin_bestellung_aktualisierenLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addContainerGap())
        );
        jP_admin_bestellung_aktualisierenLayout.setVerticalGroup(
            jP_admin_bestellung_aktualisierenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_admin_bestellung_aktualisierenLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(14, 14, 14))
        );

        jP_admin_bestellung_check.setBackground(new java.awt.Color(51, 255, 51));
        jP_admin_bestellung_check.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_admin_bestellung_checkMouseClicked(evt);
            }
        });

        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8-checkmark-yes-50.png"))); // NOI18N
        jLabel46.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel46MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jP_admin_bestellung_checkLayout = new javax.swing.GroupLayout(jP_admin_bestellung_check);
        jP_admin_bestellung_check.setLayout(jP_admin_bestellung_checkLayout);
        jP_admin_bestellung_checkLayout.setHorizontalGroup(
            jP_admin_bestellung_checkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_admin_bestellung_checkLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel46)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jP_admin_bestellung_checkLayout.setVerticalGroup(
            jP_admin_bestellung_checkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_admin_bestellung_checkLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel46)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout jP_landing_Admin_mainLayout = new javax.swing.GroupLayout(jP_landing_Admin_main);
        jP_landing_Admin_main.setLayout(jP_landing_Admin_mainLayout);
        jP_landing_Admin_mainLayout.setHorizontalGroup(
            jP_landing_Admin_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Admin_mainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_admin_Infos_Bestellung, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jP_landing_Admin_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel140)
                    .addGroup(jP_landing_Admin_mainLayout.createSequentialGroup()
                        .addComponent(lbl_landing_admin_bestellinfos, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jP_admin_bestellung_aktualisieren, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
            .addGroup(jP_landing_Admin_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Admin_mainLayout.createSequentialGroup()
                    .addContainerGap(690, Short.MAX_VALUE)
                    .addComponent(jP_admin_bestellung_check, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(17, 17, 17)))
        );
        jP_landing_Admin_mainLayout.setVerticalGroup(
            jP_landing_Admin_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Admin_mainLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel140, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jP_landing_Admin_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Admin_mainLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jP_landing_Admin_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_admin_Infos_Bestellung, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                            .addComponent(lbl_landing_admin_bestellinfos))
                        .addGap(33, 33, 33))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Admin_mainLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jP_admin_bestellung_aktualisieren, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(114, 114, 114))))
            .addGroup(jP_landing_Admin_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Admin_mainLayout.createSequentialGroup()
                    .addContainerGap(552, Short.MAX_VALUE)
                    .addComponent(jP_admin_bestellung_check, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(42, 42, 42)))
        );

        jP_landing_Admin.add(jP_landing_Admin_main, "card2");

        jP_main.add(jP_landing_Admin, "card5");

        jP_landing_Cart.setLayout(new java.awt.CardLayout());

        jP_landing_Cart_pizza_zurKasse.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_pizza_zurKasse.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Cart_pizza_zurKasse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_zurKasseMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_zurKasseMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_zurKasseMouseEntered(evt);
            }
        });

        jLabel68.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(255, 255, 255));
        jLabel68.setLabelFor(jP_landing_Cart_pizza_zurKasse);
        jLabel68.setText("Kasse");
        jLabel68.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel68MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel68MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_pizza_zurKasseLayout = new javax.swing.GroupLayout(jP_landing_Cart_pizza_zurKasse);
        jP_landing_Cart_pizza_zurKasse.setLayout(jP_landing_Cart_pizza_zurKasseLayout);
        jP_landing_Cart_pizza_zurKasseLayout.setHorizontalGroup(
            jP_landing_Cart_pizza_zurKasseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_pizza_zurKasseLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel68)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jP_landing_Cart_pizza_zurKasseLayout.setVerticalGroup(
            jP_landing_Cart_pizza_zurKasseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_pizza_zurKasseLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jLabel39.setBackground(new java.awt.Color(0, 0, 0));
        jLabel39.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(250, 130, 49));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("Warenkorb");

        jP_landing_Cart_pizza1.setBackground(new java.awt.Color(250, 130, 49));

        jP_landing_Cart_pizza_Loeschen1.setBackground(new java.awt.Color(170, 74, 68));
        jP_landing_Cart_pizza_Loeschen1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 74, 68), 3, true));
        jP_landing_Cart_pizza_Loeschen1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jp_landing_Cart_pizza_Loeschen1_Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8-delete-70.png"))); // NOI18N
        jp_landing_Cart_pizza_Loeschen1_Label.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_landing_Cart_pizza_Loeschen1_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_pizza_Loeschen1_LabelMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_pizza_Loeschen1_LabelMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_pizza_Loeschen1_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_pizza_Loeschen1Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza_Loeschen1);
        jP_landing_Cart_pizza_Loeschen1.setLayout(jP_landing_Cart_pizza_Loeschen1Layout);
        jP_landing_Cart_pizza_Loeschen1Layout.setHorizontalGroup(
            jP_landing_Cart_pizza_Loeschen1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_landing_Cart_pizza_Loeschen1_Label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
        );
        jP_landing_Cart_pizza_Loeschen1Layout.setVerticalGroup(
            jP_landing_Cart_pizza_Loeschen1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_landing_Cart_pizza_Loeschen1_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jP_landing_Cart_pizza_Bearbeiten1.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_pizza_Bearbeiten1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(253, 150, 68), 3, true));
        jP_landing_Cart_pizza_Bearbeiten1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Cart_pizza_Bearbeiten1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Bearbeiten1MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Bearbeiten1MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Bearbeiten1MouseEntered(evt);
            }
        });

        jp_landing_Cart_Edit1_Label.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jp_landing_Cart_Edit1_Label.setForeground(new java.awt.Color(255, 255, 255));
        jp_landing_Cart_Edit1_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jp_landing_Cart_Edit1_Label.setText("Bearbeiten");
        jp_landing_Cart_Edit1_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_Edit1_LabelMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_Edit1_LabelMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_Edit1_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_pizza_Bearbeiten1Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza_Bearbeiten1);
        jP_landing_Cart_pizza_Bearbeiten1.setLayout(jP_landing_Cart_pizza_Bearbeiten1Layout);
        jP_landing_Cart_pizza_Bearbeiten1Layout.setHorizontalGroup(
            jP_landing_Cart_pizza_Bearbeiten1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_landing_Cart_Edit1_Label, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
        );
        jP_landing_Cart_pizza_Bearbeiten1Layout.setVerticalGroup(
            jP_landing_Cart_pizza_Bearbeiten1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza_Bearbeiten1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jp_landing_Cart_Edit1_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        jP_landing_Cart_pizza_Icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_leer.png"))); // NOI18N

        jP_landing_Cart_pizza_Name1.setBackground(new java.awt.Color(0, 0, 0));
        jP_landing_Cart_pizza_Name1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 35)); // NOI18N
        jP_landing_Cart_pizza_Name1.setForeground(new java.awt.Color(255, 255, 255));
        jP_landing_Cart_pizza_Name1.setText("noch frei");
        jP_landing_Cart_pizza_Name1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Name1MouseClicked(evt);
            }
        });

        jP_landing_Cart_pizza_Anzahl1.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_pizza_Anzahl1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(253, 150, 68), 3, true));

        jP_landing_Cart_pizza_Anzahl_nummer1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jP_landing_Cart_pizza_Anzahl_nummer1.setForeground(new java.awt.Color(255, 255, 255));
        jP_landing_Cart_pizza_Anzahl_nummer1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jP_landing_Cart_pizza_Anzahl_nummer1.setText("0x");

        javax.swing.GroupLayout jP_landing_Cart_pizza_Anzahl1Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza_Anzahl1);
        jP_landing_Cart_pizza_Anzahl1.setLayout(jP_landing_Cart_pizza_Anzahl1Layout);
        jP_landing_Cart_pizza_Anzahl1Layout.setHorizontalGroup(
            jP_landing_Cart_pizza_Anzahl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_pizza_Anzahl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_landing_Cart_pizza_Anzahl_nummer1, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_Cart_pizza_Anzahl1Layout.setVerticalGroup(
            jP_landing_Cart_pizza_Anzahl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jP_landing_Cart_pizza_Anzahl_nummer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jP_landing_Cart_pizza1Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza1);
        jP_landing_Cart_pizza1.setLayout(jP_landing_Cart_pizza1Layout);
        jP_landing_Cart_pizza1Layout.setHorizontalGroup(
            jP_landing_Cart_pizza1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jP_landing_Cart_pizza_Loeschen1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jP_landing_Cart_pizza_Bearbeiten1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jP_landing_Cart_pizza_Icon1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jP_landing_Cart_pizza_Name1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(jP_landing_Cart_pizza_Anzahl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jP_landing_Cart_pizza1Layout.setVerticalGroup(
            jP_landing_Cart_pizza1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jP_landing_Cart_pizza1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Cart_pizza_Loeschen1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jP_landing_Cart_pizza_Bearbeiten1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jP_landing_Cart_pizza_Anzahl1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jP_landing_Cart_pizza_Icon1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jP_landing_Cart_pizza1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jP_landing_Cart_pizza_Name1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );

        jP_landing_Cart_pizza2.setBackground(new java.awt.Color(250, 130, 49));

        jP_landing_Cart_pizza_Loeschen2.setBackground(new java.awt.Color(170, 74, 68));
        jP_landing_Cart_pizza_Loeschen2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 74, 68), 3, true));
        jP_landing_Cart_pizza_Loeschen2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jp_landing_Cart_pizza_Loeschen2_Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8-delete-70.png"))); // NOI18N
        jp_landing_Cart_pizza_Loeschen2_Label.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_landing_Cart_pizza_Loeschen2_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_pizza_Loeschen2_LabelMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_pizza_Loeschen2_LabelMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_pizza_Loeschen2_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_pizza_Loeschen2Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza_Loeschen2);
        jP_landing_Cart_pizza_Loeschen2.setLayout(jP_landing_Cart_pizza_Loeschen2Layout);
        jP_landing_Cart_pizza_Loeschen2Layout.setHorizontalGroup(
            jP_landing_Cart_pizza_Loeschen2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_landing_Cart_pizza_Loeschen2_Label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
        );
        jP_landing_Cart_pizza_Loeschen2Layout.setVerticalGroup(
            jP_landing_Cart_pizza_Loeschen2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_landing_Cart_pizza_Loeschen2_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jP_landing_Cart_pizza_Bearbeiten2.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_pizza_Bearbeiten2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(253, 150, 68), 3, true));
        jP_landing_Cart_pizza_Bearbeiten2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Cart_pizza_Bearbeiten2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Bearbeiten2MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Bearbeiten2MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Bearbeiten2MouseEntered(evt);
            }
        });

        jp_landing_Cart_Edit2_Label.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jp_landing_Cart_Edit2_Label.setForeground(new java.awt.Color(255, 255, 255));
        jp_landing_Cart_Edit2_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jp_landing_Cart_Edit2_Label.setText("Bearbeiten");
        jp_landing_Cart_Edit2_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_Edit2_LabelMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_Edit2_LabelMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_Edit2_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_pizza_Bearbeiten2Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza_Bearbeiten2);
        jP_landing_Cart_pizza_Bearbeiten2.setLayout(jP_landing_Cart_pizza_Bearbeiten2Layout);
        jP_landing_Cart_pizza_Bearbeiten2Layout.setHorizontalGroup(
            jP_landing_Cart_pizza_Bearbeiten2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_landing_Cart_Edit2_Label, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
        );
        jP_landing_Cart_pizza_Bearbeiten2Layout.setVerticalGroup(
            jP_landing_Cart_pizza_Bearbeiten2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza_Bearbeiten2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jp_landing_Cart_Edit2_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        jP_landing_Cart_pizza_Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_leer.png"))); // NOI18N

        jP_landing_Cart_pizza_Name2.setBackground(new java.awt.Color(0, 0, 0));
        jP_landing_Cart_pizza_Name2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 35)); // NOI18N
        jP_landing_Cart_pizza_Name2.setForeground(new java.awt.Color(255, 255, 255));
        jP_landing_Cart_pizza_Name2.setText("noch frei");
        jP_landing_Cart_pizza_Name2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Name2MouseClicked(evt);
            }
        });

        jP_landing_Cart_pizza_Anzahl2.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_pizza_Anzahl2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(253, 150, 68), 3, true));

        jP_landing_Cart_pizza_Anzahl_nummer2.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jP_landing_Cart_pizza_Anzahl_nummer2.setForeground(new java.awt.Color(255, 255, 255));
        jP_landing_Cart_pizza_Anzahl_nummer2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jP_landing_Cart_pizza_Anzahl_nummer2.setText("0x");

        javax.swing.GroupLayout jP_landing_Cart_pizza_Anzahl2Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza_Anzahl2);
        jP_landing_Cart_pizza_Anzahl2.setLayout(jP_landing_Cart_pizza_Anzahl2Layout);
        jP_landing_Cart_pizza_Anzahl2Layout.setHorizontalGroup(
            jP_landing_Cart_pizza_Anzahl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza_Anzahl2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_landing_Cart_pizza_Anzahl_nummer2, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_Cart_pizza_Anzahl2Layout.setVerticalGroup(
            jP_landing_Cart_pizza_Anzahl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jP_landing_Cart_pizza_Anzahl_nummer2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jP_landing_Cart_pizza2Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza2);
        jP_landing_Cart_pizza2.setLayout(jP_landing_Cart_pizza2Layout);
        jP_landing_Cart_pizza2Layout.setHorizontalGroup(
            jP_landing_Cart_pizza2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jP_landing_Cart_pizza_Loeschen2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jP_landing_Cart_pizza_Bearbeiten2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jP_landing_Cart_pizza_Icon2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jP_landing_Cart_pizza_Name2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jP_landing_Cart_pizza_Anzahl2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jP_landing_Cart_pizza2Layout.setVerticalGroup(
            jP_landing_Cart_pizza2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jP_landing_Cart_pizza2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Cart_pizza_Loeschen2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jP_landing_Cart_pizza_Bearbeiten2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jP_landing_Cart_pizza_Anzahl2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jP_landing_Cart_pizza_Icon2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jP_landing_Cart_pizza2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jP_landing_Cart_pizza_Name2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );

        jP_landing_Cart_pizza3.setBackground(new java.awt.Color(250, 130, 49));

        jP_landing_Cart_pizza_Loeschen3.setBackground(new java.awt.Color(170, 74, 68));
        jP_landing_Cart_pizza_Loeschen3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 74, 68), 3, true));
        jP_landing_Cart_pizza_Loeschen3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jp_landing_Cart_pizza_Loeschen3_Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8-delete-70.png"))); // NOI18N
        jp_landing_Cart_pizza_Loeschen3_Label.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_landing_Cart_pizza_Loeschen3_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_pizza_Loeschen3_LabelMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_pizza_Loeschen3_LabelMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_pizza_Loeschen3_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_pizza_Loeschen3Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza_Loeschen3);
        jP_landing_Cart_pizza_Loeschen3.setLayout(jP_landing_Cart_pizza_Loeschen3Layout);
        jP_landing_Cart_pizza_Loeschen3Layout.setHorizontalGroup(
            jP_landing_Cart_pizza_Loeschen3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_landing_Cart_pizza_Loeschen3_Label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
        );
        jP_landing_Cart_pizza_Loeschen3Layout.setVerticalGroup(
            jP_landing_Cart_pizza_Loeschen3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_landing_Cart_pizza_Loeschen3_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jP_landing_Cart_pizza_Bearbeiten3.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_pizza_Bearbeiten3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(253, 150, 68), 3, true));
        jP_landing_Cart_pizza_Bearbeiten3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Cart_pizza_Bearbeiten3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Bearbeiten3MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Bearbeiten3MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Bearbeiten3MouseEntered(evt);
            }
        });

        jp_landing_Cart_Edit3_Label.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jp_landing_Cart_Edit3_Label.setForeground(new java.awt.Color(255, 255, 255));
        jp_landing_Cart_Edit3_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jp_landing_Cart_Edit3_Label.setText("Bearbeiten");
        jp_landing_Cart_Edit3_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_Edit3_LabelMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_Edit3_LabelMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_Edit3_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_pizza_Bearbeiten3Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza_Bearbeiten3);
        jP_landing_Cart_pizza_Bearbeiten3.setLayout(jP_landing_Cart_pizza_Bearbeiten3Layout);
        jP_landing_Cart_pizza_Bearbeiten3Layout.setHorizontalGroup(
            jP_landing_Cart_pizza_Bearbeiten3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_landing_Cart_Edit3_Label, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
        );
        jP_landing_Cart_pizza_Bearbeiten3Layout.setVerticalGroup(
            jP_landing_Cart_pizza_Bearbeiten3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza_Bearbeiten3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jp_landing_Cart_Edit3_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        jP_landing_Cart_pizza_Icon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_leer.png"))); // NOI18N

        jP_landing_Cart_pizza_Name3.setBackground(new java.awt.Color(0, 0, 0));
        jP_landing_Cart_pizza_Name3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 35)); // NOI18N
        jP_landing_Cart_pizza_Name3.setForeground(new java.awt.Color(255, 255, 255));
        jP_landing_Cart_pizza_Name3.setText("noch frei");
        jP_landing_Cart_pizza_Name3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Name3MouseClicked(evt);
            }
        });

        jP_landing_Cart_pizza_Anzahl3.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_pizza_Anzahl3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(253, 150, 68), 3, true));

        jP_landing_Cart_pizza_Anzahl_nummer3.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jP_landing_Cart_pizza_Anzahl_nummer3.setForeground(new java.awt.Color(255, 255, 255));
        jP_landing_Cart_pizza_Anzahl_nummer3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jP_landing_Cart_pizza_Anzahl_nummer3.setText("0x");

        javax.swing.GroupLayout jP_landing_Cart_pizza_Anzahl3Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza_Anzahl3);
        jP_landing_Cart_pizza_Anzahl3.setLayout(jP_landing_Cart_pizza_Anzahl3Layout);
        jP_landing_Cart_pizza_Anzahl3Layout.setHorizontalGroup(
            jP_landing_Cart_pizza_Anzahl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza_Anzahl3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_landing_Cart_pizza_Anzahl_nummer3, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_Cart_pizza_Anzahl3Layout.setVerticalGroup(
            jP_landing_Cart_pizza_Anzahl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jP_landing_Cart_pizza_Anzahl_nummer3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jP_landing_Cart_pizza3Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza3);
        jP_landing_Cart_pizza3.setLayout(jP_landing_Cart_pizza3Layout);
        jP_landing_Cart_pizza3Layout.setHorizontalGroup(
            jP_landing_Cart_pizza3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jP_landing_Cart_pizza_Loeschen3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jP_landing_Cart_pizza_Bearbeiten3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jP_landing_Cart_pizza_Icon3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jP_landing_Cart_pizza_Name3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jP_landing_Cart_pizza_Anzahl3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jP_landing_Cart_pizza3Layout.setVerticalGroup(
            jP_landing_Cart_pizza3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jP_landing_Cart_pizza3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Cart_pizza_Loeschen3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jP_landing_Cart_pizza_Bearbeiten3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jP_landing_Cart_pizza_Anzahl3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jP_landing_Cart_pizza_Icon3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jP_landing_Cart_pizza3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jP_landing_Cart_pizza_Name3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );

        jP_landing_Cart_pizza4.setBackground(new java.awt.Color(250, 130, 49));

        jP_landing_Cart_pizza_Loeschen4.setBackground(new java.awt.Color(170, 74, 68));
        jP_landing_Cart_pizza_Loeschen4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 74, 68), 3, true));
        jP_landing_Cart_pizza_Loeschen4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jp_landing_Cart_pizza_Loeschen4_Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8-delete-70.png"))); // NOI18N
        jp_landing_Cart_pizza_Loeschen4_Label.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jp_landing_Cart_pizza_Loeschen4_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_pizza_Loeschen4_LabelMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_pizza_Loeschen4_LabelMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_pizza_Loeschen4_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_pizza_Loeschen4Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza_Loeschen4);
        jP_landing_Cart_pizza_Loeschen4.setLayout(jP_landing_Cart_pizza_Loeschen4Layout);
        jP_landing_Cart_pizza_Loeschen4Layout.setHorizontalGroup(
            jP_landing_Cart_pizza_Loeschen4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_landing_Cart_pizza_Loeschen4_Label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
        );
        jP_landing_Cart_pizza_Loeschen4Layout.setVerticalGroup(
            jP_landing_Cart_pizza_Loeschen4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_landing_Cart_pizza_Loeschen4_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jP_landing_Cart_pizza_Bearbeiten4.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_pizza_Bearbeiten4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(253, 150, 68), 3, true));
        jP_landing_Cart_pizza_Bearbeiten4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Cart_pizza_Bearbeiten4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Bearbeiten4MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Bearbeiten4MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Bearbeiten4MouseEntered(evt);
            }
        });

        jp_landing_Cart_Edit4_Label.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jp_landing_Cart_Edit4_Label.setForeground(new java.awt.Color(255, 255, 255));
        jp_landing_Cart_Edit4_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jp_landing_Cart_Edit4_Label.setText("Bearbeiten");
        jp_landing_Cart_Edit4_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_Edit4_LabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Cart_Edit4_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_pizza_Bearbeiten4Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza_Bearbeiten4);
        jP_landing_Cart_pizza_Bearbeiten4.setLayout(jP_landing_Cart_pizza_Bearbeiten4Layout);
        jP_landing_Cart_pizza_Bearbeiten4Layout.setHorizontalGroup(
            jP_landing_Cart_pizza_Bearbeiten4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_landing_Cart_Edit4_Label, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
        );
        jP_landing_Cart_pizza_Bearbeiten4Layout.setVerticalGroup(
            jP_landing_Cart_pizza_Bearbeiten4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza_Bearbeiten4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jp_landing_Cart_Edit4_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        jP_landing_Cart_pizza_Icon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_leer.png"))); // NOI18N

        jP_landing_Cart_pizza_Name4.setBackground(new java.awt.Color(0, 0, 0));
        jP_landing_Cart_pizza_Name4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 35)); // NOI18N
        jP_landing_Cart_pizza_Name4.setForeground(new java.awt.Color(255, 255, 255));
        jP_landing_Cart_pizza_Name4.setText("noch frei");
        jP_landing_Cart_pizza_Name4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_pizza_Name4MouseClicked(evt);
            }
        });

        jP_landing_Cart_pizza_Anzahl4.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_pizza_Anzahl4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(253, 150, 68), 3, true));

        jP_landing_Cart_pizza_Anzahl_nummer4.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jP_landing_Cart_pizza_Anzahl_nummer4.setForeground(new java.awt.Color(255, 255, 255));
        jP_landing_Cart_pizza_Anzahl_nummer4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jP_landing_Cart_pizza_Anzahl_nummer4.setText("0x");

        javax.swing.GroupLayout jP_landing_Cart_pizza_Anzahl4Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza_Anzahl4);
        jP_landing_Cart_pizza_Anzahl4.setLayout(jP_landing_Cart_pizza_Anzahl4Layout);
        jP_landing_Cart_pizza_Anzahl4Layout.setHorizontalGroup(
            jP_landing_Cart_pizza_Anzahl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza_Anzahl4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_landing_Cart_pizza_Anzahl_nummer4, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_Cart_pizza_Anzahl4Layout.setVerticalGroup(
            jP_landing_Cart_pizza_Anzahl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jP_landing_Cart_pizza_Anzahl_nummer4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jP_landing_Cart_pizza4Layout = new javax.swing.GroupLayout(jP_landing_Cart_pizza4);
        jP_landing_Cart_pizza4.setLayout(jP_landing_Cart_pizza4Layout);
        jP_landing_Cart_pizza4Layout.setHorizontalGroup(
            jP_landing_Cart_pizza4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jP_landing_Cart_pizza_Loeschen4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jP_landing_Cart_pizza_Bearbeiten4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jP_landing_Cart_pizza_Icon4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jP_landing_Cart_pizza_Name4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jP_landing_Cart_pizza_Anzahl4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jP_landing_Cart_pizza4Layout.setVerticalGroup(
            jP_landing_Cart_pizza4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_pizza4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jP_landing_Cart_pizza4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Cart_pizza_Loeschen4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jP_landing_Cart_pizza_Bearbeiten4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jP_landing_Cart_pizza_Anzahl4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jP_landing_Cart_pizza_Icon4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jP_landing_Cart_pizza4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jP_landing_Cart_pizza_Name4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );

        lbl_landing_Cart_Warenkorbbetrag.setBackground(new java.awt.Color(0, 0, 0));
        lbl_landing_Cart_Warenkorbbetrag.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        lbl_landing_Cart_Warenkorbbetrag.setForeground(new java.awt.Color(250, 130, 49));
        lbl_landing_Cart_Warenkorbbetrag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_landing_Cart_Warenkorbbetrag.setText("0,00€");

        javax.swing.GroupLayout jP_landing_Cart_mainLayout = new javax.swing.GroupLayout(jP_landing_Cart_main);
        jP_landing_Cart_main.setLayout(jP_landing_Cart_mainLayout);
        jP_landing_Cart_mainLayout.setHorizontalGroup(
            jP_landing_Cart_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_mainLayout.createSequentialGroup()
                .addGroup(jP_landing_Cart_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Cart_mainLayout.createSequentialGroup()
                        .addGap(245, 245, 245)
                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jP_landing_Cart_mainLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(jP_landing_Cart_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jP_landing_Cart_mainLayout.createSequentialGroup()
                                .addComponent(lbl_landing_Cart_Warenkorbbetrag, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(366, 366, 366)
                                .addComponent(jP_landing_Cart_pizza_zurKasse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jP_landing_Cart_pizza1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Cart_pizza2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Cart_pizza3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Cart_pizza4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jP_landing_Cart_mainLayout.setVerticalGroup(
            jP_landing_Cart_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_mainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jP_landing_Cart_pizza1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jP_landing_Cart_pizza2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jP_landing_Cart_pizza3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jP_landing_Cart_pizza4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jP_landing_Cart_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Cart_pizza_zurKasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_landing_Cart_Warenkorbbetrag, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jP_landing_Cart.add(jP_landing_Cart_main, "card2");

        jLabel80.setBackground(new java.awt.Color(0, 0, 0));
        jLabel80.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(250, 130, 49));
        jLabel80.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel80.setText("Warenkorb");

        jLabel81.setBackground(new java.awt.Color(0, 0, 0));
        jLabel81.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 30)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(253, 150, 68));
        jLabel81.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel81.setText("Barzahlung");

        jP_landing_Cart_kasse_Back.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_kasse_Back.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Cart_kasse_Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_kasse_BackMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_kasse_BackMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_kasse_BackMouseEntered(evt);
            }
        });

        jLabel69.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(255, 255, 255));
        jLabel69.setText("Zurück");
        jLabel69.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel69MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel69MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_kasse_BackLayout = new javax.swing.GroupLayout(jP_landing_Cart_kasse_Back);
        jP_landing_Cart_kasse_Back.setLayout(jP_landing_Cart_kasse_BackLayout);
        jP_landing_Cart_kasse_BackLayout.setHorizontalGroup(
            jP_landing_Cart_kasse_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_kasse_BackLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel69)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jP_landing_Cart_kasse_BackLayout.setVerticalGroup(
            jP_landing_Cart_kasse_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_kasse_BackLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jP_landing_Cart_kasse_Order.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_kasse_Order.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Cart_kasse_Order.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_kasse_OrderMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_kasse_OrderMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_kasse_OrderMouseEntered(evt);
            }
        });

        jLabel70.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(255, 255, 255));
        jLabel70.setText("Bestellen");
        jLabel70.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel70MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel70MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_kasse_OrderLayout = new javax.swing.GroupLayout(jP_landing_Cart_kasse_Order);
        jP_landing_Cart_kasse_Order.setLayout(jP_landing_Cart_kasse_OrderLayout);
        jP_landing_Cart_kasse_OrderLayout.setHorizontalGroup(
            jP_landing_Cart_kasse_OrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_kasse_OrderLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(jLabel70)
                .addGap(21, 21, 21))
        );
        jP_landing_Cart_kasse_OrderLayout.setVerticalGroup(
            jP_landing_Cart_kasse_OrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_kasse_OrderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_landing_Cart_kasse_Adresse.setBackground(new java.awt.Color(255, 255, 255));

        lbl_landing_Cart_pizza_Adresse.setBackground(new java.awt.Color(0, 0, 0));
        lbl_landing_Cart_pizza_Adresse.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        lbl_landing_Cart_pizza_Adresse.setForeground(new java.awt.Color(125, 125, 125));
        lbl_landing_Cart_pizza_Adresse.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_landing_Cart_pizza_Adresse.setText("<html><p style=\"text-align:left;\"> Straße: Vivaldiweg 36 <br> Ort: Stuttgart 70195 <br> Name: Benjamin Knaup <br> E-Mail: Benjmin.Knaup@beispiel.de <br> </p></html>");

        javax.swing.GroupLayout jP_landing_Cart_kasse_AdresseLayout = new javax.swing.GroupLayout(jP_landing_Cart_kasse_Adresse);
        jP_landing_Cart_kasse_Adresse.setLayout(jP_landing_Cart_kasse_AdresseLayout);
        jP_landing_Cart_kasse_AdresseLayout.setHorizontalGroup(
            jP_landing_Cart_kasse_AdresseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_kasse_AdresseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_landing_Cart_pizza_Adresse, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_Cart_kasse_AdresseLayout.setVerticalGroup(
            jP_landing_Cart_kasse_AdresseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_kasse_AdresseLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lbl_landing_Cart_pizza_Adresse, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel82.setBackground(new java.awt.Color(0, 0, 0));
        jLabel82.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 30)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(253, 150, 68));
        jLabel82.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel82.setText("Gesamtbetrag:");

        lbl_landing_Cart_kasse_Gesamtbetrag.setBackground(new java.awt.Color(0, 0, 0));
        lbl_landing_Cart_kasse_Gesamtbetrag.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 29)); // NOI18N
        lbl_landing_Cart_kasse_Gesamtbetrag.setForeground(new java.awt.Color(253, 150, 68));
        lbl_landing_Cart_kasse_Gesamtbetrag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_landing_Cart_kasse_Gesamtbetrag.setText("0,00");

        cB_Cart_Bar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        cB_Cart_Bar.setForeground(new java.awt.Color(253, 150, 68));
        cB_Cart_Bar.setText("Hiermit bestätige ich, den Barbetrag dem Lieferanten zu übergeben");
        cB_Cart_Bar.setActionCommand("Ich bestätige dass ich das angegebene Geld bar mit mir trage");
        cB_Cart_Bar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cB_Cart_BarActionPerformed(evt);
            }
        });

        jLabel98.setBackground(new java.awt.Color(0, 0, 0));
        jLabel98.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(253, 150, 68));
        jLabel98.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel98.setText("Coupon");

        jP_landing_Cart_kasse_Coupon_Prüfen.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_kasse_Coupon_Prüfen.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Cart_kasse_Coupon_Prüfen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_kasse_Coupon_PrüfenMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_kasse_Coupon_PrüfenMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_kasse_Coupon_PrüfenMouseEntered(evt);
            }
        });

        lbl_landing_Cart_kasse_Coupon_Prüfen.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        lbl_landing_Cart_kasse_Coupon_Prüfen.setForeground(new java.awt.Color(255, 255, 255));
        lbl_landing_Cart_kasse_Coupon_Prüfen.setText("Prüfe Coupon");
        lbl_landing_Cart_kasse_Coupon_Prüfen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_landing_Cart_kasse_Coupon_PrüfenMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_landing_Cart_kasse_Coupon_PrüfenMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_kasse_Coupon_PrüfenLayout = new javax.swing.GroupLayout(jP_landing_Cart_kasse_Coupon_Prüfen);
        jP_landing_Cart_kasse_Coupon_Prüfen.setLayout(jP_landing_Cart_kasse_Coupon_PrüfenLayout);
        jP_landing_Cart_kasse_Coupon_PrüfenLayout.setHorizontalGroup(
            jP_landing_Cart_kasse_Coupon_PrüfenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_kasse_Coupon_PrüfenLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(lbl_landing_Cart_kasse_Coupon_Prüfen)
                .addGap(14, 14, 14))
        );
        jP_landing_Cart_kasse_Coupon_PrüfenLayout.setVerticalGroup(
            jP_landing_Cart_kasse_Coupon_PrüfenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_kasse_Coupon_PrüfenLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_landing_Cart_kasse_Coupon_Prüfen)
                .addGap(19, 19, 19))
        );

        lbl_for_kassegesamt.setBackground(new java.awt.Color(0, 0, 0));
        lbl_for_kassegesamt.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 29)); // NOI18N
        lbl_for_kassegesamt.setForeground(new java.awt.Color(253, 150, 68));
        lbl_for_kassegesamt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_for_kassegesamt.setText("€");

        jLabel7.setText("Extrawünsche:");

        txa_landing_kasse_Extra.setColumns(20);
        txa_landing_kasse_Extra.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txa_landing_kasse_Extra.setRows(5);
        jScrollPane1.setViewportView(txa_landing_kasse_Extra);

        javax.swing.GroupLayout jP_landing_Cart_kasse_mainLayout = new javax.swing.GroupLayout(jP_landing_Cart_kasse_main);
        jP_landing_Cart_kasse_main.setLayout(jP_landing_Cart_kasse_mainLayout);
        jP_landing_Cart_kasse_mainLayout.setHorizontalGroup(
            jP_landing_Cart_kasse_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_kasse_mainLayout.createSequentialGroup()
                .addComponent(jP_landing_Cart_kasse_Adresse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                .addGroup(jP_landing_Cart_kasse_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jP_landing_Cart_kasse_mainLayout.createSequentialGroup()
                .addGroup(jP_landing_Cart_kasse_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Cart_kasse_mainLayout.createSequentialGroup()
                        .addGroup(jP_landing_Cart_kasse_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel81)
                            .addGroup(jP_landing_Cart_kasse_mainLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel98, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_landing_kasse_coupon, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jP_landing_Cart_kasse_Coupon_Prüfen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jP_landing_Cart_kasse_mainLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(cB_Cart_Bar)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jP_landing_Cart_kasse_mainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jP_landing_Cart_kasse_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jP_landing_Cart_kasse_Order, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jP_landing_Cart_kasse_mainLayout.createSequentialGroup()
                        .addGroup(jP_landing_Cart_kasse_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jP_landing_Cart_kasse_mainLayout.createSequentialGroup()
                                .addGap(288, 288, 288)
                                .addComponent(lbl_landing_Cart_kasse_Gesamtbetrag)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_for_kassegesamt))
                            .addGroup(jP_landing_Cart_kasse_mainLayout.createSequentialGroup()
                                .addGap(224, 224, 224)
                                .addComponent(jLabel82)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jP_landing_Cart_kasse_mainLayout.setVerticalGroup(
            jP_landing_Cart_kasse_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_kasse_mainLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel81)
                .addGap(20, 20, 20)
                .addGroup(jP_landing_Cart_kasse_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jP_landing_Cart_kasse_Coupon_Prüfen, javax.swing.GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE)
                    .addGroup(jP_landing_Cart_kasse_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_landing_kasse_coupon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel98)))
                .addGap(18, 18, 18)
                .addComponent(cB_Cart_Bar)
                .addGap(65, 65, 65)
                .addGroup(jP_landing_Cart_kasse_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jP_landing_Cart_kasse_mainLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addComponent(jP_landing_Cart_kasse_Adresse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel82)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jP_landing_Cart_kasse_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_landing_Cart_kasse_Gesamtbetrag, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_for_kassegesamt, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jP_landing_Cart_kasse_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Cart_kasse_Order, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_landing_Cart_kasse_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jP_landing_Cart_kasseLayout = new javax.swing.GroupLayout(jP_landing_Cart_kasse);
        jP_landing_Cart_kasse.setLayout(jP_landing_Cart_kasseLayout);
        jP_landing_Cart_kasseLayout.setHorizontalGroup(
            jP_landing_Cart_kasseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_kasseLayout.createSequentialGroup()
                .addGroup(jP_landing_Cart_kasseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Cart_kasseLayout.createSequentialGroup()
                        .addGap(245, 245, 245)
                        .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jP_landing_Cart_kasseLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jP_landing_Cart_kasse_main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jP_landing_Cart_kasseLayout.setVerticalGroup(
            jP_landing_Cart_kasseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_kasseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jP_landing_Cart_kasse_main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jP_landing_Cart.add(jP_landing_Cart_kasse, "card2");

        jLabel90.setBackground(new java.awt.Color(0, 0, 0));
        jLabel90.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel90.setForeground(new java.awt.Color(250, 130, 49));
        jLabel90.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel90.setText("DANKE!");

        jLabel91.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 22)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(255, 173, 109));
        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel91.setText("<html><p style=\"text-align:center;\">Für dein Vertrauen</p></html>");

        jP_landing_Cart_ordermore.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_ordermore.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Cart_ordermore.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_ordermoreMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_ordermoreMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_ordermoreMouseEntered(evt);
            }
        });

        jLabel89.setBackground(new java.awt.Color(0, 0, 0));
        jLabel89.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel89.setForeground(new java.awt.Color(250, 250, 250));
        jLabel89.setText("Home");
        jLabel89.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel89MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel89MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_ordermoreLayout = new javax.swing.GroupLayout(jP_landing_Cart_ordermore);
        jP_landing_Cart_ordermore.setLayout(jP_landing_Cart_ordermoreLayout);
        jP_landing_Cart_ordermoreLayout.setHorizontalGroup(
            jP_landing_Cart_ordermoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_ordermoreLayout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addComponent(jLabel89)
                .addContainerGap(144, Short.MAX_VALUE))
        );
        jP_landing_Cart_ordermoreLayout.setVerticalGroup(
            jP_landing_Cart_ordermoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_ordermoreLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel89)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel92.setBackground(new java.awt.Color(0, 0, 0));
        jLabel92.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(250, 130, 49));
        jLabel92.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel92.setText("Bestellung aufgegeben");

        jLabel93.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 22)); // NOI18N
        jLabel93.setForeground(new java.awt.Color(255, 173, 109));
        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel93.setText("<html><p style=\"text-align:center;\">Die Bestellung wurde nun aufgegeben und sollte bei Ihnen in der nächsten halben Stunde ankommen.</p></html>");

        javax.swing.GroupLayout jP_landing_Cart_orderedLayout = new javax.swing.GroupLayout(jP_landing_Cart_ordered);
        jP_landing_Cart_ordered.setLayout(jP_landing_Cart_orderedLayout);
        jP_landing_Cart_orderedLayout.setHorizontalGroup(
            jP_landing_Cart_orderedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_orderedLayout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addGroup(jP_landing_Cart_orderedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_orderedLayout.createSequentialGroup()
                        .addComponent(jLabel92)
                        .addGap(139, 139, 139))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_orderedLayout.createSequentialGroup()
                        .addGroup(jP_landing_Cart_orderedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jP_landing_Cart_orderedLayout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(jLabel90))
                            .addComponent(jP_landing_Cart_ordermore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jP_landing_Cart_orderedLayout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(218, 218, 218))))
        );
        jP_landing_Cart_orderedLayout.setVerticalGroup(
            jP_landing_Cart_orderedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_orderedLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79)
                .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jP_landing_Cart_ordermore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(226, Short.MAX_VALUE))
        );

        jP_landing_Cart.add(jP_landing_Cart_ordered, "card4");

        jp_landing_cart_bearbeiten_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_salami.png"))); // NOI18N

        jLabel142.setBackground(new java.awt.Color(0, 0, 0));
        jLabel142.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel142.setForeground(new java.awt.Color(250, 130, 49));
        jLabel142.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel142.setText("Anzahl:");

        cB_bearbeiten_Rucola.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_bearbeiten_Rucola.setForeground(new java.awt.Color(255, 173, 109));
        cB_bearbeiten_Rucola.setText("Rucola");

        cB_bearbeiten_Salami.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_bearbeiten_Salami.setForeground(new java.awt.Color(255, 173, 109));
        cB_bearbeiten_Salami.setSelected(true);
        cB_bearbeiten_Salami.setText("Salami");

        cB_bearbeiten_Kaese.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_bearbeiten_Kaese.setForeground(new java.awt.Color(255, 173, 109));
        cB_bearbeiten_Kaese.setSelected(true);
        cB_bearbeiten_Kaese.setText("Käse");

        cB_bearbeiten_Zwiebeln.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_bearbeiten_Zwiebeln.setForeground(new java.awt.Color(255, 173, 109));
        cB_bearbeiten_Zwiebeln.setText("Zwiebeln");

        cB_bearbeiten_Tomatensosse.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_bearbeiten_Tomatensosse.setForeground(new java.awt.Color(255, 173, 109));
        cB_bearbeiten_Tomatensosse.setSelected(true);
        cB_bearbeiten_Tomatensosse.setText("Tomatensoße");

        jP_landing_Cart_bearbeiten_Back.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_bearbeiten_Back.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Cart_bearbeiten_Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_BackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_BackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_BackMouseExited(evt);
            }
        });

        jLabel144.setBackground(new java.awt.Color(0, 0, 0));
        jLabel144.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel144.setForeground(new java.awt.Color(250, 250, 250));
        jLabel144.setText("Zurück");
        jLabel144.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel144MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel144MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_bearbeiten_BackLayout = new javax.swing.GroupLayout(jP_landing_Cart_bearbeiten_Back);
        jP_landing_Cart_bearbeiten_Back.setLayout(jP_landing_Cart_bearbeiten_BackLayout);
        jP_landing_Cart_bearbeiten_BackLayout.setHorizontalGroup(
            jP_landing_Cart_bearbeiten_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_bearbeiten_BackLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel144)
                .addGap(28, 28, 28))
        );
        jP_landing_Cart_bearbeiten_BackLayout.setVerticalGroup(
            jP_landing_Cart_bearbeiten_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_bearbeiten_BackLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel144)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Cart_bearbeiten_toCart.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_bearbeiten_toCart.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Cart_bearbeiten_toCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_toCartMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_toCartMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_toCartMouseEntered(evt);
            }
        });

        jLabel145.setBackground(new java.awt.Color(0, 0, 0));
        jLabel145.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel145.setForeground(new java.awt.Color(250, 250, 250));
        jLabel145.setText("Bearbeiten");
        jLabel145.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel145MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel145MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_bearbeiten_toCartLayout = new javax.swing.GroupLayout(jP_landing_Cart_bearbeiten_toCart);
        jP_landing_Cart_bearbeiten_toCart.setLayout(jP_landing_Cart_bearbeiten_toCartLayout);
        jP_landing_Cart_bearbeiten_toCartLayout.setHorizontalGroup(
            jP_landing_Cart_bearbeiten_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_bearbeiten_toCartLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel145)
                .addGap(28, 28, 28))
        );
        jP_landing_Cart_bearbeiten_toCartLayout.setVerticalGroup(
            jP_landing_Cart_bearbeiten_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_bearbeiten_toCartLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel145)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Cart_bearbeiten_Plus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_bearbeiten_Plus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Cart_bearbeiten_Plus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_PlusMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_PlusMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_PlusMouseEntered(evt);
            }
        });

        jLabel146.setBackground(new java.awt.Color(0, 0, 0));
        jLabel146.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel146.setForeground(new java.awt.Color(250, 250, 250));
        jLabel146.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel146.setText("+");
        jLabel146.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel146MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel146MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel146MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_bearbeiten_PlusLayout = new javax.swing.GroupLayout(jP_landing_Cart_bearbeiten_Plus);
        jP_landing_Cart_bearbeiten_Plus.setLayout(jP_landing_Cart_bearbeiten_PlusLayout);
        jP_landing_Cart_bearbeiten_PlusLayout.setHorizontalGroup(
            jP_landing_Cart_bearbeiten_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_bearbeiten_PlusLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel146, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );
        jP_landing_Cart_bearbeiten_PlusLayout.setVerticalGroup(
            jP_landing_Cart_bearbeiten_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_bearbeiten_PlusLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel146)
                .addContainerGap())
        );

        jP_landing_Cart_bearbeiten_Count.setBackground(new java.awt.Color(255, 206, 168));
        jP_landing_Cart_bearbeiten_Count.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Cart_bearbeiten_Count.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_CountMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_CountMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_CountMouseEntered(evt);
            }
        });

        jP_landing_Cart_bearbeiten_Counter.setBackground(new java.awt.Color(0, 0, 0));
        jP_landing_Cart_bearbeiten_Counter.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jP_landing_Cart_bearbeiten_Counter.setForeground(new java.awt.Color(250, 250, 250));
        jP_landing_Cart_bearbeiten_Counter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jP_landing_Cart_bearbeiten_Counter.setText("1");
        jP_landing_Cart_bearbeiten_Counter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_CounterMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_CounterMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_bearbeiten_CountLayout = new javax.swing.GroupLayout(jP_landing_Cart_bearbeiten_Count);
        jP_landing_Cart_bearbeiten_Count.setLayout(jP_landing_Cart_bearbeiten_CountLayout);
        jP_landing_Cart_bearbeiten_CountLayout.setHorizontalGroup(
            jP_landing_Cart_bearbeiten_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_bearbeiten_CountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_landing_Cart_bearbeiten_Counter, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_Cart_bearbeiten_CountLayout.setVerticalGroup(
            jP_landing_Cart_bearbeiten_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_bearbeiten_CountLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jP_landing_Cart_bearbeiten_Counter)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_landing_Cart_bearbeiten_Minus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Cart_bearbeiten_Minus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Cart_bearbeiten_Minus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_MinusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_MinusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Cart_bearbeiten_MinusMouseExited(evt);
            }
        });

        jLabel147.setBackground(new java.awt.Color(0, 0, 0));
        jLabel147.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel147.setForeground(new java.awt.Color(250, 250, 250));
        jLabel147.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel147.setText("-");
        jLabel147.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel147MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel147MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel147MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Cart_bearbeiten_MinusLayout = new javax.swing.GroupLayout(jP_landing_Cart_bearbeiten_Minus);
        jP_landing_Cart_bearbeiten_Minus.setLayout(jP_landing_Cart_bearbeiten_MinusLayout);
        jP_landing_Cart_bearbeiten_MinusLayout.setHorizontalGroup(
            jP_landing_Cart_bearbeiten_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_bearbeiten_MinusLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel147, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );
        jP_landing_Cart_bearbeiten_MinusLayout.setVerticalGroup(
            jP_landing_Cart_bearbeiten_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_bearbeiten_MinusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel147)
                .addContainerGap())
        );

        jp_landing_cart_bearbeiten_title.setBackground(new java.awt.Color(0, 0, 0));
        jp_landing_cart_bearbeiten_title.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 60)); // NOI18N
        jp_landing_cart_bearbeiten_title.setForeground(new java.awt.Color(250, 130, 49));
        jp_landing_cart_bearbeiten_title.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jp_landing_cart_bearbeiten_title.setText("Pizza Salami");

        javax.swing.GroupLayout jP_landing_Cart_bearbeitenLayout = new javax.swing.GroupLayout(jP_landing_Cart_bearbeiten);
        jP_landing_Cart_bearbeiten.setLayout(jP_landing_Cart_bearbeitenLayout);
        jP_landing_Cart_bearbeitenLayout.setHorizontalGroup(
            jP_landing_Cart_bearbeitenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_bearbeitenLayout.createSequentialGroup()
                .addGroup(jP_landing_Cart_bearbeitenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jP_landing_Cart_bearbeitenLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jp_landing_cart_bearbeiten_title, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jP_landing_Cart_bearbeitenLayout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addGroup(jP_landing_Cart_bearbeitenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jP_landing_Cart_bearbeitenLayout.createSequentialGroup()
                                .addGroup(jP_landing_Cart_bearbeitenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cB_bearbeiten_Tomatensosse)
                                    .addComponent(cB_bearbeiten_Kaese)
                                    .addComponent(cB_bearbeiten_Rucola)
                                    .addComponent(cB_bearbeiten_Zwiebeln)
                                    .addGroup(jP_landing_Cart_bearbeitenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jp_landing_cart_bearbeiten_icon)
                                        .addComponent(cB_bearbeiten_Salami)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
                                .addComponent(jLabel142)
                                .addGap(18, 18, 18)
                                .addComponent(jP_landing_Cart_bearbeiten_Minus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_Cart_bearbeiten_Count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_Cart_bearbeiten_Plus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jP_landing_Cart_bearbeitenLayout.createSequentialGroup()
                                .addComponent(jP_landing_Cart_bearbeiten_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 321, Short.MAX_VALUE)
                                .addComponent(jP_landing_Cart_bearbeiten_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(43, 43, 43))
        );
        jP_landing_Cart_bearbeitenLayout.setVerticalGroup(
            jP_landing_Cart_bearbeitenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Cart_bearbeitenLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jP_landing_Cart_bearbeitenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_bearbeitenLayout.createSequentialGroup()
                        .addComponent(jp_landing_cart_bearbeiten_title, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Cart_bearbeitenLayout.createSequentialGroup()
                        .addComponent(jp_landing_cart_bearbeiten_icon)
                        .addGap(48, 48, 48)))
                .addComponent(cB_bearbeiten_Salami)
                .addGap(19, 19, 19)
                .addComponent(cB_bearbeiten_Zwiebeln)
                .addGap(18, 18, 18)
                .addComponent(cB_bearbeiten_Rucola)
                .addGap(18, 18, 18)
                .addComponent(cB_bearbeiten_Kaese)
                .addGap(18, 18, 18)
                .addComponent(cB_bearbeiten_Tomatensosse)
                .addGroup(jP_landing_Cart_bearbeitenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Cart_bearbeitenLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jP_landing_Cart_bearbeitenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jP_landing_Cart_bearbeiten_Count, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Cart_bearbeiten_Minus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Cart_bearbeiten_Plus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Cart_bearbeitenLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel142, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jP_landing_Cart_bearbeitenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Cart_bearbeiten_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_landing_Cart_bearbeiten_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );

        jP_landing_Cart.add(jP_landing_Cart_bearbeiten, "card3");

        jP_main.add(jP_landing_Cart, "card6");

        jP_landing_Bestellen.setLayout(new java.awt.CardLayout());

        jLabel35.setBackground(new java.awt.Color(0, 0, 0));
        jLabel35.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(250, 130, 49));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Bestellen");

        jP_Bestellen_salami.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_Bestellen_salami.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_Bestellen_salamiMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_Bestellen_salamiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_Bestellen_salamiMouseExited(evt);
            }
        });

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_salami.png"))); // NOI18N
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel16MouseEntered(evt);
            }
        });

        jLabel18.setBackground(new java.awt.Color(0, 0, 0));
        jLabel18.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Salami");
        jLabel18.setToolTipText("");
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel18MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_Bestellen_salamiLayout = new javax.swing.GroupLayout(jP_Bestellen_salami);
        jP_Bestellen_salami.setLayout(jP_Bestellen_salamiLayout);
        jP_Bestellen_salamiLayout.setHorizontalGroup(
            jP_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_salamiLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jP_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        jP_Bestellen_salamiLayout.setVerticalGroup(
            jP_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_salamiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addContainerGap())
        );

        jP_Bestellen_speciale.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_Bestellen_speciale.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_Bestellen_specialeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_Bestellen_specialeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_Bestellen_specialeMouseExited(evt);
            }
        });

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_speciale.png"))); // NOI18N
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel19MouseEntered(evt);
            }
        });

        jLabel20.setBackground(new java.awt.Color(0, 0, 0));
        jLabel20.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Speciale");
        jLabel20.setToolTipText("");
        jLabel20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel20MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel20MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_Bestellen_specialeLayout = new javax.swing.GroupLayout(jP_Bestellen_speciale);
        jP_Bestellen_speciale.setLayout(jP_Bestellen_specialeLayout);
        jP_Bestellen_specialeLayout.setHorizontalGroup(
            jP_Bestellen_specialeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_specialeLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jP_Bestellen_specialeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        jP_Bestellen_specialeLayout.setVerticalGroup(
            jP_Bestellen_specialeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_specialeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addContainerGap())
        );

        jP_Bestellen_diavolo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_Bestellen_diavolo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_Bestellen_diavoloMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_Bestellen_diavoloMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_Bestellen_diavoloMouseExited(evt);
            }
        });

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_diavolo.png"))); // NOI18N
        jLabel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel21MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel21MouseEntered(evt);
            }
        });

        jLabel22.setBackground(new java.awt.Color(0, 0, 0));
        jLabel22.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Diavolo");
        jLabel22.setToolTipText("");
        jLabel22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel22MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel22MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_Bestellen_diavoloLayout = new javax.swing.GroupLayout(jP_Bestellen_diavolo);
        jP_Bestellen_diavolo.setLayout(jP_Bestellen_diavoloLayout);
        jP_Bestellen_diavoloLayout.setHorizontalGroup(
            jP_Bestellen_diavoloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_diavoloLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jP_Bestellen_diavoloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        jP_Bestellen_diavoloLayout.setVerticalGroup(
            jP_Bestellen_diavoloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_diavoloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addContainerGap())
        );

        jP_Bestellen_stagioni.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_Bestellen_stagioni.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_Bestellen_stagioniMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_Bestellen_stagioniMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_Bestellen_stagioniMouseExited(evt);
            }
        });

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_quattrostagioni.png"))); // NOI18N
        jLabel23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel23MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel23MouseEntered(evt);
            }
        });

        jLabel24.setBackground(new java.awt.Color(0, 0, 0));
        jLabel24.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Stagioni");
        jLabel24.setToolTipText("");
        jLabel24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel24MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel24MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_Bestellen_stagioniLayout = new javax.swing.GroupLayout(jP_Bestellen_stagioni);
        jP_Bestellen_stagioni.setLayout(jP_Bestellen_stagioniLayout);
        jP_Bestellen_stagioniLayout.setHorizontalGroup(
            jP_Bestellen_stagioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_stagioniLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jP_Bestellen_stagioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        jP_Bestellen_stagioniLayout.setVerticalGroup(
            jP_Bestellen_stagioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_stagioniLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addContainerGap())
        );

        jP_Bestellen_rucula.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_Bestellen_rucula.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_Bestellen_ruculaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_Bestellen_ruculaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_Bestellen_ruculaMouseExited(evt);
            }
        });

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_rucula.png"))); // NOI18N
        jLabel25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel25MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel25MouseEntered(evt);
            }
        });

        jLabel26.setBackground(new java.awt.Color(0, 0, 0));
        jLabel26.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("Rucula");
        jLabel26.setToolTipText("");
        jLabel26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel26MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel26MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_Bestellen_ruculaLayout = new javax.swing.GroupLayout(jP_Bestellen_rucula);
        jP_Bestellen_rucula.setLayout(jP_Bestellen_ruculaLayout);
        jP_Bestellen_ruculaLayout.setHorizontalGroup(
            jP_Bestellen_ruculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_ruculaLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jP_Bestellen_ruculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        jP_Bestellen_ruculaLayout.setVerticalGroup(
            jP_Bestellen_ruculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_ruculaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addContainerGap())
        );

        jP_Bestellen_caprese.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_Bestellen_caprese.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_Bestellen_capreseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_Bestellen_capreseMouseExited(evt);
            }
        });

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_caprese.png"))); // NOI18N
        jLabel27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel27MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel27MouseEntered(evt);
            }
        });

        jLabel28.setBackground(new java.awt.Color(0, 0, 0));
        jLabel28.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Caprese");
        jLabel28.setToolTipText("");
        jLabel28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel28MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel28MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_Bestellen_capreseLayout = new javax.swing.GroupLayout(jP_Bestellen_caprese);
        jP_Bestellen_caprese.setLayout(jP_Bestellen_capreseLayout);
        jP_Bestellen_capreseLayout.setHorizontalGroup(
            jP_Bestellen_capreseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_capreseLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jP_Bestellen_capreseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        jP_Bestellen_capreseLayout.setVerticalGroup(
            jP_Bestellen_capreseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_capreseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28)
                .addContainerGap())
        );

        jP_Bestellen_pepperoni.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_Bestellen_pepperoni.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_Bestellen_pepperoniMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_Bestellen_pepperoniMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_Bestellen_pepperoniMouseExited(evt);
            }
        });

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_pepperoni.png"))); // NOI18N
        jLabel33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel33MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel33MouseEntered(evt);
            }
        });

        jLabel34.setBackground(new java.awt.Color(0, 0, 0));
        jLabel34.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 20)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Pepperoni");
        jLabel34.setToolTipText("");
        jLabel34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel34MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel34MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_Bestellen_pepperoniLayout = new javax.swing.GroupLayout(jP_Bestellen_pepperoni);
        jP_Bestellen_pepperoni.setLayout(jP_Bestellen_pepperoniLayout);
        jP_Bestellen_pepperoniLayout.setHorizontalGroup(
            jP_Bestellen_pepperoniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_pepperoniLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jP_Bestellen_pepperoniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        jP_Bestellen_pepperoniLayout.setVerticalGroup(
            jP_Bestellen_pepperoniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_pepperoniLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel34)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jP_Bestellen_margherita.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_Bestellen_margherita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_Bestellen_margheritaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_Bestellen_margheritaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_Bestellen_margheritaMouseExited(evt);
            }
        });

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_margherita.png"))); // NOI18N
        jLabel31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel31MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel31MouseEntered(evt);
            }
        });

        jLabel32.setBackground(new java.awt.Color(0, 0, 0));
        jLabel32.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 19)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Margherita");
        jLabel32.setToolTipText("");
        jLabel32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel32MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel32MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_Bestellen_margheritaLayout = new javax.swing.GroupLayout(jP_Bestellen_margherita);
        jP_Bestellen_margherita.setLayout(jP_Bestellen_margheritaLayout);
        jP_Bestellen_margheritaLayout.setHorizontalGroup(
            jP_Bestellen_margheritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_margheritaLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jP_Bestellen_margheritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        jP_Bestellen_margheritaLayout.setVerticalGroup(
            jP_Bestellen_margheritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_margheritaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_Bestellen_funghi.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_Bestellen_funghi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_Bestellen_funghiMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_Bestellen_funghiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_Bestellen_funghiMouseExited(evt);
            }
        });

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_funghi.png"))); // NOI18N
        jLabel29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel29MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel29MouseEntered(evt);
            }
        });

        jLabel30.setBackground(new java.awt.Color(0, 0, 0));
        jLabel30.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Funghi");
        jLabel30.setToolTipText("");
        jLabel30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel30MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel30MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_Bestellen_funghiLayout = new javax.swing.GroupLayout(jP_Bestellen_funghi);
        jP_Bestellen_funghi.setLayout(jP_Bestellen_funghiLayout);
        jP_Bestellen_funghiLayout.setHorizontalGroup(
            jP_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_funghiLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jP_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        jP_Bestellen_funghiLayout.setVerticalGroup(
            jP_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_Bestellen_funghiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30)
                .addContainerGap())
        );

        javax.swing.GroupLayout jP_landing_Bestellen_homeLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_home);
        jP_landing_Bestellen_home.setLayout(jP_landing_Bestellen_homeLayout);
        jP_landing_Bestellen_homeLayout.setHorizontalGroup(
            jP_landing_Bestellen_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_homeLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jP_landing_Bestellen_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jP_landing_Bestellen_homeLayout.createSequentialGroup()
                        .addComponent(jP_Bestellen_stagioni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jP_Bestellen_rucula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jP_Bestellen_caprese, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jP_landing_Bestellen_homeLayout.createSequentialGroup()
                        .addComponent(jP_Bestellen_funghi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                        .addComponent(jP_Bestellen_margherita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                        .addComponent(jP_Bestellen_pepperoni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jP_landing_Bestellen_homeLayout.createSequentialGroup()
                        .addComponent(jP_Bestellen_salami, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jP_Bestellen_speciale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jP_Bestellen_diavolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_homeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(262, 262, 262))
        );
        jP_landing_Bestellen_homeLayout.setVerticalGroup(
            jP_landing_Bestellen_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_homeLayout.createSequentialGroup()
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jP_landing_Bestellen_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_Bestellen_diavolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_Bestellen_speciale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_Bestellen_salami, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jP_landing_Bestellen_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_Bestellen_caprese, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_Bestellen_rucula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_Bestellen_stagioni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jP_landing_Bestellen_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jP_Bestellen_funghi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jP_Bestellen_margherita, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jP_Bestellen_pepperoni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jP_landing_Bestellen.add(jP_landing_Bestellen_home, "card4");

        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_salami.png"))); // NOI18N

        jLabel41.setBackground(new java.awt.Color(0, 0, 0));
        jLabel41.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(250, 130, 49));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel41.setText("Anzahl:");

        cB_salami_Rucola.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_salami_Rucola.setForeground(new java.awt.Color(255, 173, 109));
        cB_salami_Rucola.setText("Rucola");

        cB_salami_Salami.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_salami_Salami.setForeground(new java.awt.Color(255, 173, 109));
        cB_salami_Salami.setSelected(true);
        cB_salami_Salami.setText("Salami");

        cB_salami_Kaese.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_salami_Kaese.setForeground(new java.awt.Color(255, 173, 109));
        cB_salami_Kaese.setSelected(true);
        cB_salami_Kaese.setText("Käse");

        cB_salami_Zwiebeln.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_salami_Zwiebeln.setForeground(new java.awt.Color(255, 173, 109));
        cB_salami_Zwiebeln.setText("Zwiebeln");

        cB_salami_Tomatensosse.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_salami_Tomatensosse.setForeground(new java.awt.Color(255, 173, 109));
        cB_salami_Tomatensosse.setSelected(true);
        cB_salami_Tomatensosse.setText("Tomatensoße");

        jLabel44.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 22)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 173, 109));
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("<html><p style=\"text-align:center;\">Unsere Pizza Salami wird mit feinster Salami, frischen Tomaten und Käse belegt.</p></html>");

        jP_landing_Bestellen_salami_Back.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_salami_Back.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_salami_Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_BackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_BackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_BackMouseExited(evt);
            }
        });

        jLabel45.setBackground(new java.awt.Color(0, 0, 0));
        jLabel45.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(250, 250, 250));
        jLabel45.setText("Zurück");
        jLabel45.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel45MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel45MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_salami_BackLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_salami_Back);
        jP_landing_Bestellen_salami_Back.setLayout(jP_landing_Bestellen_salami_BackLayout);
        jP_landing_Bestellen_salami_BackLayout.setHorizontalGroup(
            jP_landing_Bestellen_salami_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_salami_BackLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel45)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_salami_BackLayout.setVerticalGroup(
            jP_landing_Bestellen_salami_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_salami_BackLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel45)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_salami_toCart.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_salami_toCart.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_salami_toCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_toCartMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_toCartMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_toCartMouseEntered(evt);
            }
        });

        jp_landing_Bestellen_salami_toCart_Label.setBackground(new java.awt.Color(0, 0, 0));
        jp_landing_Bestellen_salami_toCart_Label.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jp_landing_Bestellen_salami_toCart_Label.setForeground(new java.awt.Color(250, 250, 250));
        jp_landing_Bestellen_salami_toCart_Label.setText("In den Warenkorb");
        jp_landing_Bestellen_salami_toCart_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_salami_toCart_LabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_salami_toCart_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_salami_toCartLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_salami_toCart);
        jP_landing_Bestellen_salami_toCart.setLayout(jP_landing_Bestellen_salami_toCartLayout);
        jP_landing_Bestellen_salami_toCartLayout.setHorizontalGroup(
            jP_landing_Bestellen_salami_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_salami_toCartLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jp_landing_Bestellen_salami_toCart_Label)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_salami_toCartLayout.setVerticalGroup(
            jP_landing_Bestellen_salami_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_salami_toCartLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jp_landing_Bestellen_salami_toCart_Label)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_salami_Plus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_salami_Plus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_salami_Plus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_PlusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_PlusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_PlusMouseExited(evt);
            }
        });

        jLabel47.setBackground(new java.awt.Color(0, 0, 0));
        jLabel47.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(250, 250, 250));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("+");
        jLabel47.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel47MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel47MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel47MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_salami_PlusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_salami_Plus);
        jP_landing_Bestellen_salami_Plus.setLayout(jP_landing_Bestellen_salami_PlusLayout);
        jP_landing_Bestellen_salami_PlusLayout.setHorizontalGroup(
            jP_landing_Bestellen_salami_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_salami_PlusLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );
        jP_landing_Bestellen_salami_PlusLayout.setVerticalGroup(
            jP_landing_Bestellen_salami_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_salami_PlusLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel47)
                .addContainerGap())
        );

        jP_landing_Bestellen_salami_Count.setBackground(new java.awt.Color(255, 206, 168));
        jP_landing_Bestellen_salami_Count.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_salami_Count.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_CountMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_CountMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_CountMouseExited(evt);
            }
        });

        jP_landing_Bestellen_salami_Counter.setBackground(new java.awt.Color(0, 0, 0));
        jP_landing_Bestellen_salami_Counter.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jP_landing_Bestellen_salami_Counter.setForeground(new java.awt.Color(250, 250, 250));
        jP_landing_Bestellen_salami_Counter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jP_landing_Bestellen_salami_Counter.setText("1");
        jP_landing_Bestellen_salami_Counter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_CounterMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_CounterMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_salami_CountLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_salami_Count);
        jP_landing_Bestellen_salami_Count.setLayout(jP_landing_Bestellen_salami_CountLayout);
        jP_landing_Bestellen_salami_CountLayout.setHorizontalGroup(
            jP_landing_Bestellen_salami_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_salami_CountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_landing_Bestellen_salami_Counter, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_Bestellen_salami_CountLayout.setVerticalGroup(
            jP_landing_Bestellen_salami_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_salami_CountLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jP_landing_Bestellen_salami_Counter)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_salami_Minus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_salami_Minus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_salami_Minus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_MinusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_MinusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_salami_MinusMouseExited(evt);
            }
        });

        jLabel49.setBackground(new java.awt.Color(0, 0, 0));
        jLabel49.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(250, 250, 250));
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("-");
        jLabel49.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel49MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel49MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel49MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_salami_MinusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_salami_Minus);
        jP_landing_Bestellen_salami_Minus.setLayout(jP_landing_Bestellen_salami_MinusLayout);
        jP_landing_Bestellen_salami_MinusLayout.setHorizontalGroup(
            jP_landing_Bestellen_salami_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_salami_MinusLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );
        jP_landing_Bestellen_salami_MinusLayout.setVerticalGroup(
            jP_landing_Bestellen_salami_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_salami_MinusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel49)
                .addContainerGap())
        );

        jLabel43.setBackground(new java.awt.Color(0, 0, 0));
        jLabel43.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 60)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(250, 130, 49));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel43.setText("Pizza Salami");

        javax.swing.GroupLayout jP_landing_Bestellen_salamiLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_salami);
        jP_landing_Bestellen_salami.setLayout(jP_landing_Bestellen_salamiLayout);
        jP_landing_Bestellen_salamiLayout.setHorizontalGroup(
            jP_landing_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_salamiLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jP_landing_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Bestellen_salamiLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jP_landing_Bestellen_salamiLayout.createSequentialGroup()
                                .addGroup(jP_landing_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cB_salami_Tomatensosse)
                                    .addComponent(cB_salami_Kaese)
                                    .addComponent(cB_salami_Rucola)
                                    .addComponent(cB_salami_Zwiebeln)
                                    .addGroup(jP_landing_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel40)
                                        .addComponent(cB_salami_Salami)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jP_landing_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_salamiLayout.createSequentialGroup()
                                        .addComponent(jLabel41)
                                        .addGap(18, 18, 18)
                                        .addComponent(jP_landing_Bestellen_salami_Minus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jP_landing_Bestellen_salami_Count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jP_landing_Bestellen_salami_Plus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jP_landing_Bestellen_salamiLayout.createSequentialGroup()
                                .addComponent(jP_landing_Bestellen_salami_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                                .addComponent(jP_landing_Bestellen_salami_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(43, 43, 43))
                    .addGroup(jP_landing_Bestellen_salamiLayout.createSequentialGroup()
                        .addGap(189, 189, 189)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jP_landing_Bestellen_salamiLayout.setVerticalGroup(
            jP_landing_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_salamiLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jP_landing_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_salamiLayout.createSequentialGroup()
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_salamiLayout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(48, 48, 48)))
                .addGroup(jP_landing_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jP_landing_Bestellen_salamiLayout.createSequentialGroup()
                        .addComponent(cB_salami_Salami)
                        .addGap(19, 19, 19)
                        .addComponent(cB_salami_Zwiebeln)
                        .addGap(18, 18, 18)
                        .addComponent(cB_salami_Rucola)
                        .addGap(18, 18, 18)
                        .addComponent(cB_salami_Kaese)
                        .addGap(18, 18, 18)
                        .addComponent(cB_salami_Tomatensosse))
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jP_landing_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Bestellen_salamiLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jP_landing_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jP_landing_Bestellen_salami_Count, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_salami_Minus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_salami_Plus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_salamiLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jP_landing_Bestellen_salamiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Bestellen_salami_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_landing_Bestellen_salami_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );

        jP_landing_Bestellen.add(jP_landing_Bestellen_salami, "card3");

        jLabel48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_speciale.png"))); // NOI18N

        jLabel50.setBackground(new java.awt.Color(0, 0, 0));
        jLabel50.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(250, 130, 49));
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel50.setText("Anzahl:");

        cB_speciale_Rucola.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_speciale_Rucola.setForeground(new java.awt.Color(255, 173, 109));
        cB_speciale_Rucola.setText("Rucola");

        cB_speciale_Salami.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_speciale_Salami.setForeground(new java.awt.Color(255, 173, 109));
        cB_speciale_Salami.setSelected(true);
        cB_speciale_Salami.setText("Salami");

        cB_speciale_Kaese.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_speciale_Kaese.setForeground(new java.awt.Color(255, 173, 109));
        cB_speciale_Kaese.setSelected(true);
        cB_speciale_Kaese.setText("Käse");

        cB_speciale_Zwiebeln.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_speciale_Zwiebeln.setForeground(new java.awt.Color(255, 173, 109));
        cB_speciale_Zwiebeln.setSelected(true);
        cB_speciale_Zwiebeln.setText("Zwiebeln");

        cB_speciale_Tomatensosse.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_speciale_Tomatensosse.setForeground(new java.awt.Color(255, 173, 109));
        cB_speciale_Tomatensosse.setSelected(true);
        cB_speciale_Tomatensosse.setText("Tomatensoße");

        jLabel51.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 22)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 173, 109));
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setText("<html><p style=\"text-align:center;\">Unsere Pizza Speciale ist ein wahres Geschmackserlebnis, belegt mit Schinken, Champignons, Peperoni und Oliven.</p></html>");

        jP_landing_Bestellen_speciale_Back.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_speciale_Back.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_speciale_Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_BackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_BackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_BackMouseExited(evt);
            }
        });

        jLabel52.setBackground(new java.awt.Color(0, 0, 0));
        jLabel52.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(250, 250, 250));
        jLabel52.setText("Zurück");
        jLabel52.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel52MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel52MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_speciale_BackLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_speciale_Back);
        jP_landing_Bestellen_speciale_Back.setLayout(jP_landing_Bestellen_speciale_BackLayout);
        jP_landing_Bestellen_speciale_BackLayout.setHorizontalGroup(
            jP_landing_Bestellen_speciale_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_speciale_BackLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel52)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_speciale_BackLayout.setVerticalGroup(
            jP_landing_Bestellen_speciale_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_speciale_BackLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel52)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_speciale_toCart.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_speciale_toCart.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_speciale_toCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_toCartMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_toCartMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_toCartMouseEntered(evt);
            }
        });

        jp_landing_Bestellen_speciale_toCart_Label.setBackground(new java.awt.Color(0, 0, 0));
        jp_landing_Bestellen_speciale_toCart_Label.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jp_landing_Bestellen_speciale_toCart_Label.setForeground(new java.awt.Color(250, 250, 250));
        jp_landing_Bestellen_speciale_toCart_Label.setText("In den Warenkorb");
        jp_landing_Bestellen_speciale_toCart_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_speciale_toCart_LabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_speciale_toCart_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_speciale_toCartLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_speciale_toCart);
        jP_landing_Bestellen_speciale_toCart.setLayout(jP_landing_Bestellen_speciale_toCartLayout);
        jP_landing_Bestellen_speciale_toCartLayout.setHorizontalGroup(
            jP_landing_Bestellen_speciale_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_speciale_toCartLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jp_landing_Bestellen_speciale_toCart_Label)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_speciale_toCartLayout.setVerticalGroup(
            jP_landing_Bestellen_speciale_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_speciale_toCartLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jp_landing_Bestellen_speciale_toCart_Label)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_speciale_Plus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_speciale_Plus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_speciale_Plus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_PlusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_PlusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_PlusMouseExited(evt);
            }
        });

        jLabel54.setBackground(new java.awt.Color(0, 0, 0));
        jLabel54.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(250, 250, 250));
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setText("+");
        jLabel54.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel54MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel54MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel54MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_speciale_PlusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_speciale_Plus);
        jP_landing_Bestellen_speciale_Plus.setLayout(jP_landing_Bestellen_speciale_PlusLayout);
        jP_landing_Bestellen_speciale_PlusLayout.setHorizontalGroup(
            jP_landing_Bestellen_speciale_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_speciale_PlusLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );
        jP_landing_Bestellen_speciale_PlusLayout.setVerticalGroup(
            jP_landing_Bestellen_speciale_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_speciale_PlusLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel54)
                .addContainerGap())
        );

        jP_landing_Bestellen_speciale_Count.setBackground(new java.awt.Color(255, 206, 168));
        jP_landing_Bestellen_speciale_Count.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_speciale_Count.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_CountMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_CountMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_CountMouseExited(evt);
            }
        });

        jP_landing_Bestellen_speciale_Counter.setBackground(new java.awt.Color(0, 0, 0));
        jP_landing_Bestellen_speciale_Counter.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jP_landing_Bestellen_speciale_Counter.setForeground(new java.awt.Color(250, 250, 250));
        jP_landing_Bestellen_speciale_Counter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jP_landing_Bestellen_speciale_Counter.setText("1");
        jP_landing_Bestellen_speciale_Counter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_CounterMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_CounterMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_speciale_CountLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_speciale_Count);
        jP_landing_Bestellen_speciale_Count.setLayout(jP_landing_Bestellen_speciale_CountLayout);
        jP_landing_Bestellen_speciale_CountLayout.setHorizontalGroup(
            jP_landing_Bestellen_speciale_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_speciale_CountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_landing_Bestellen_speciale_Counter, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_Bestellen_speciale_CountLayout.setVerticalGroup(
            jP_landing_Bestellen_speciale_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_speciale_CountLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jP_landing_Bestellen_speciale_Counter)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_speciale_Minus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_speciale_Minus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_speciale_Minus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_MinusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_MinusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_speciale_MinusMouseExited(evt);
            }
        });

        jLabel55.setBackground(new java.awt.Color(0, 0, 0));
        jLabel55.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(250, 250, 250));
        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel55.setText("-");
        jLabel55.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel55MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel55MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel55MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_speciale_MinusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_speciale_Minus);
        jP_landing_Bestellen_speciale_Minus.setLayout(jP_landing_Bestellen_speciale_MinusLayout);
        jP_landing_Bestellen_speciale_MinusLayout.setHorizontalGroup(
            jP_landing_Bestellen_speciale_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_speciale_MinusLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );
        jP_landing_Bestellen_speciale_MinusLayout.setVerticalGroup(
            jP_landing_Bestellen_speciale_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_speciale_MinusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel55)
                .addContainerGap())
        );

        jLabel56.setBackground(new java.awt.Color(0, 0, 0));
        jLabel56.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 60)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(250, 130, 49));
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel56.setText("Pizza Speciale");

        javax.swing.GroupLayout jP_landing_Bestellen_specialeLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_speciale);
        jP_landing_Bestellen_speciale.setLayout(jP_landing_Bestellen_specialeLayout);
        jP_landing_Bestellen_specialeLayout.setHorizontalGroup(
            jP_landing_Bestellen_specialeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_specialeLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jP_landing_Bestellen_specialeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jP_landing_Bestellen_specialeLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_specialeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cB_speciale_Tomatensosse)
                            .addComponent(cB_speciale_Kaese)
                            .addComponent(cB_speciale_Rucola)
                            .addComponent(cB_speciale_Zwiebeln))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jP_landing_Bestellen_specialeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_specialeLayout.createSequentialGroup()
                                .addComponent(jLabel50)
                                .addGap(18, 18, 18)
                                .addComponent(jP_landing_Bestellen_speciale_Minus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_Bestellen_speciale_Count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_Bestellen_speciale_Plus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_specialeLayout.createSequentialGroup()
                        .addComponent(jP_landing_Bestellen_speciale_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                        .addComponent(jP_landing_Bestellen_speciale_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jP_landing_Bestellen_specialeLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_specialeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel48)
                            .addComponent(cB_speciale_Salami))
                        .addGap(47, 47, 47)
                        .addComponent(jLabel56)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(43, 43, 43))
        );
        jP_landing_Bestellen_specialeLayout.setVerticalGroup(
            jP_landing_Bestellen_specialeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_specialeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jP_landing_Bestellen_specialeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_specialeLayout.createSequentialGroup()
                        .addComponent(jLabel48)
                        .addGap(48, 48, 48))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_specialeLayout.createSequentialGroup()
                        .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)))
                .addGroup(jP_landing_Bestellen_specialeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jP_landing_Bestellen_specialeLayout.createSequentialGroup()
                        .addComponent(cB_speciale_Salami)
                        .addGap(19, 19, 19)
                        .addComponent(cB_speciale_Zwiebeln)
                        .addGap(18, 18, 18)
                        .addComponent(cB_speciale_Rucola)
                        .addGap(18, 18, 18)
                        .addComponent(cB_speciale_Kaese)
                        .addGap(18, 18, 18)
                        .addComponent(cB_speciale_Tomatensosse))
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jP_landing_Bestellen_specialeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Bestellen_specialeLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jP_landing_Bestellen_specialeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jP_landing_Bestellen_speciale_Count, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_speciale_Minus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_speciale_Plus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_specialeLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jP_landing_Bestellen_specialeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Bestellen_speciale_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_landing_Bestellen_speciale_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );

        jP_landing_Bestellen.add(jP_landing_Bestellen_speciale, "card3");

        jLabel57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_diavolo.png"))); // NOI18N

        jLabel58.setBackground(new java.awt.Color(0, 0, 0));
        jLabel58.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(250, 130, 49));
        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel58.setText("Anzahl:");

        cB_diavolo_Rucola.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_diavolo_Rucola.setForeground(new java.awt.Color(255, 173, 109));
        cB_diavolo_Rucola.setText("Rucola");

        cB_diavolo_Salami.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_diavolo_Salami.setForeground(new java.awt.Color(255, 173, 109));
        cB_diavolo_Salami.setSelected(true);
        cB_diavolo_Salami.setText("Salami");

        cB_diavolo_Kaese.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_diavolo_Kaese.setForeground(new java.awt.Color(255, 173, 109));
        cB_diavolo_Kaese.setSelected(true);
        cB_diavolo_Kaese.setText("Käse");

        cB_diavolo_Zwiebeln.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_diavolo_Zwiebeln.setForeground(new java.awt.Color(255, 173, 109));
        cB_diavolo_Zwiebeln.setText("Zwiebeln");

        cB_diavolo_Tomatensosse.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_diavolo_Tomatensosse.setForeground(new java.awt.Color(255, 173, 109));
        cB_diavolo_Tomatensosse.setSelected(true);
        cB_diavolo_Tomatensosse.setText("Tomatensoße");

        jLabel59.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 22)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 173, 109));
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel59.setText("<html><p style=\"text-align:center;\">Für alle Scharfliebhaber bieten wir unsere Pizza Diavolo an, belegt mit feurigen Peperoni und Chili.</p></html>");

        jP_landing_Bestellen_diavolo_Back.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_diavolo_Back.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_diavolo_Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_BackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_BackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_BackMouseExited(evt);
            }
        });

        jLabel60.setBackground(new java.awt.Color(0, 0, 0));
        jLabel60.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(250, 250, 250));
        jLabel60.setText("Zurück");
        jLabel60.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel60MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel60MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_diavolo_BackLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_diavolo_Back);
        jP_landing_Bestellen_diavolo_Back.setLayout(jP_landing_Bestellen_diavolo_BackLayout);
        jP_landing_Bestellen_diavolo_BackLayout.setHorizontalGroup(
            jP_landing_Bestellen_diavolo_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_diavolo_BackLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel60)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_diavolo_BackLayout.setVerticalGroup(
            jP_landing_Bestellen_diavolo_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_diavolo_BackLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel60)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_diavolo_toCart.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_diavolo_toCart.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_diavolo_toCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_toCartMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_toCartMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_toCartMouseEntered(evt);
            }
        });

        jp_landing_Bestellen_diavolo_toCart_Label.setBackground(new java.awt.Color(0, 0, 0));
        jp_landing_Bestellen_diavolo_toCart_Label.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jp_landing_Bestellen_diavolo_toCart_Label.setForeground(new java.awt.Color(250, 250, 250));
        jp_landing_Bestellen_diavolo_toCart_Label.setText("In den Warenkorb");
        jp_landing_Bestellen_diavolo_toCart_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_diavolo_toCart_LabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_diavolo_toCart_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_diavolo_toCartLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_diavolo_toCart);
        jP_landing_Bestellen_diavolo_toCart.setLayout(jP_landing_Bestellen_diavolo_toCartLayout);
        jP_landing_Bestellen_diavolo_toCartLayout.setHorizontalGroup(
            jP_landing_Bestellen_diavolo_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_diavolo_toCartLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jp_landing_Bestellen_diavolo_toCart_Label)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_diavolo_toCartLayout.setVerticalGroup(
            jP_landing_Bestellen_diavolo_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_diavolo_toCartLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jp_landing_Bestellen_diavolo_toCart_Label)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_diavolo_Plus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_diavolo_Plus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_diavolo_Plus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_PlusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_PlusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_PlusMouseExited(evt);
            }
        });

        jLabel62.setBackground(new java.awt.Color(0, 0, 0));
        jLabel62.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(250, 250, 250));
        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel62.setText("+");
        jLabel62.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel62MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel62MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel62MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_diavolo_PlusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_diavolo_Plus);
        jP_landing_Bestellen_diavolo_Plus.setLayout(jP_landing_Bestellen_diavolo_PlusLayout);
        jP_landing_Bestellen_diavolo_PlusLayout.setHorizontalGroup(
            jP_landing_Bestellen_diavolo_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_diavolo_PlusLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );
        jP_landing_Bestellen_diavolo_PlusLayout.setVerticalGroup(
            jP_landing_Bestellen_diavolo_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_diavolo_PlusLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel62)
                .addContainerGap())
        );

        jP_landing_Bestellen_diavolo_Count.setBackground(new java.awt.Color(255, 206, 168));
        jP_landing_Bestellen_diavolo_Count.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_diavolo_Count.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_CountMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_CountMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_CountMouseExited(evt);
            }
        });

        jP_landing_Bestellen_diavolo_Counter.setBackground(new java.awt.Color(0, 0, 0));
        jP_landing_Bestellen_diavolo_Counter.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jP_landing_Bestellen_diavolo_Counter.setForeground(new java.awt.Color(250, 250, 250));
        jP_landing_Bestellen_diavolo_Counter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jP_landing_Bestellen_diavolo_Counter.setText("1");
        jP_landing_Bestellen_diavolo_Counter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_CounterMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_CounterMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_diavolo_CountLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_diavolo_Count);
        jP_landing_Bestellen_diavolo_Count.setLayout(jP_landing_Bestellen_diavolo_CountLayout);
        jP_landing_Bestellen_diavolo_CountLayout.setHorizontalGroup(
            jP_landing_Bestellen_diavolo_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_diavolo_CountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_landing_Bestellen_diavolo_Counter, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_Bestellen_diavolo_CountLayout.setVerticalGroup(
            jP_landing_Bestellen_diavolo_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_diavolo_CountLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jP_landing_Bestellen_diavolo_Counter)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_diavolo_Minus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_diavolo_Minus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_diavolo_Minus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_MinusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_MinusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_diavolo_MinusMouseExited(evt);
            }
        });

        jLabel63.setBackground(new java.awt.Color(0, 0, 0));
        jLabel63.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(250, 250, 250));
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel63.setText("-");
        jLabel63.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel63MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel63MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel63MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_diavolo_MinusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_diavolo_Minus);
        jP_landing_Bestellen_diavolo_Minus.setLayout(jP_landing_Bestellen_diavolo_MinusLayout);
        jP_landing_Bestellen_diavolo_MinusLayout.setHorizontalGroup(
            jP_landing_Bestellen_diavolo_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_diavolo_MinusLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );
        jP_landing_Bestellen_diavolo_MinusLayout.setVerticalGroup(
            jP_landing_Bestellen_diavolo_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_diavolo_MinusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel63)
                .addContainerGap())
        );

        TEXT9999999.setBackground(new java.awt.Color(0, 0, 0));
        TEXT9999999.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 60)); // NOI18N
        TEXT9999999.setForeground(new java.awt.Color(250, 130, 49));
        TEXT9999999.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TEXT9999999.setText("Pizza Diavolo");

        javax.swing.GroupLayout jP_landing_Bestellen_diavoloLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_diavolo);
        jP_landing_Bestellen_diavolo.setLayout(jP_landing_Bestellen_diavoloLayout);
        jP_landing_Bestellen_diavoloLayout.setHorizontalGroup(
            jP_landing_Bestellen_diavoloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_diavoloLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jP_landing_Bestellen_diavoloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jP_landing_Bestellen_diavoloLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_diavoloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cB_diavolo_Tomatensosse)
                            .addComponent(cB_diavolo_Kaese)
                            .addComponent(cB_diavolo_Rucola)
                            .addComponent(cB_diavolo_Zwiebeln))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jP_landing_Bestellen_diavoloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_diavoloLayout.createSequentialGroup()
                                .addComponent(jLabel58)
                                .addGap(18, 18, 18)
                                .addComponent(jP_landing_Bestellen_diavolo_Minus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_Bestellen_diavolo_Count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_Bestellen_diavolo_Plus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel59, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_diavoloLayout.createSequentialGroup()
                        .addComponent(jP_landing_Bestellen_diavolo_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                        .addComponent(jP_landing_Bestellen_diavolo_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jP_landing_Bestellen_diavoloLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_diavoloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel57)
                            .addComponent(cB_diavolo_Salami))
                        .addGap(39, 39, 39)
                        .addComponent(TEXT9999999)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(43, 43, 43))
        );
        jP_landing_Bestellen_diavoloLayout.setVerticalGroup(
            jP_landing_Bestellen_diavoloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_diavoloLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jP_landing_Bestellen_diavoloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_diavoloLayout.createSequentialGroup()
                        .addComponent(jLabel57)
                        .addGap(48, 48, 48))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_diavoloLayout.createSequentialGroup()
                        .addComponent(TEXT9999999, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)))
                .addGroup(jP_landing_Bestellen_diavoloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jP_landing_Bestellen_diavoloLayout.createSequentialGroup()
                        .addComponent(cB_diavolo_Salami)
                        .addGap(19, 19, 19)
                        .addComponent(cB_diavolo_Zwiebeln)
                        .addGap(18, 18, 18)
                        .addComponent(cB_diavolo_Rucola)
                        .addGap(18, 18, 18)
                        .addComponent(cB_diavolo_Kaese)
                        .addGap(18, 18, 18)
                        .addComponent(cB_diavolo_Tomatensosse))
                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jP_landing_Bestellen_diavoloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Bestellen_diavoloLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jP_landing_Bestellen_diavoloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jP_landing_Bestellen_diavolo_Count, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_diavolo_Minus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_diavolo_Plus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_diavoloLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jP_landing_Bestellen_diavoloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Bestellen_diavolo_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_landing_Bestellen_diavolo_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );

        jP_landing_Bestellen.add(jP_landing_Bestellen_diavolo, "card3");

        jLabel71.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_quattrostagioni.png"))); // NOI18N

        jLabel72.setBackground(new java.awt.Color(0, 0, 0));
        jLabel72.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(250, 130, 49));
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel72.setText("Anzahl:");

        cB_stagioni_Rucola.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_stagioni_Rucola.setForeground(new java.awt.Color(255, 173, 109));
        cB_stagioni_Rucola.setText("Rucola");

        cB_stagioni_Salami.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_stagioni_Salami.setForeground(new java.awt.Color(255, 173, 109));
        cB_stagioni_Salami.setText("Salami");

        cB_stagioni_Kaese.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_stagioni_Kaese.setForeground(new java.awt.Color(255, 173, 109));
        cB_stagioni_Kaese.setSelected(true);
        cB_stagioni_Kaese.setText("Käse");

        cB_stagioni_Zwiebeln.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_stagioni_Zwiebeln.setForeground(new java.awt.Color(255, 173, 109));
        cB_stagioni_Zwiebeln.setSelected(true);
        cB_stagioni_Zwiebeln.setText("Zwiebeln");

        cB_stagioni_Tomatensosse.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_stagioni_Tomatensosse.setForeground(new java.awt.Color(255, 173, 109));
        cB_stagioni_Tomatensosse.setSelected(true);
        cB_stagioni_Tomatensosse.setText("Tomatensoße");

        jLabel73.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 22)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 173, 109));
        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel73.setText("<html><p style=\"text-align:center;\">Die Pizza Stagioni bietet eine Auswahl saisonaler Zutaten wie z.B. Artischocken, Champignons und Oliven.</p></html>");

        jP_landing_Bestellen_stagioni_Back.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_stagioni_Back.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_stagioni_Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_BackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_BackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_BackMouseExited(evt);
            }
        });

        jLabel74.setBackground(new java.awt.Color(0, 0, 0));
        jLabel74.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(250, 250, 250));
        jLabel74.setText("Zurück");
        jLabel74.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel74MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel74MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_stagioni_BackLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_stagioni_Back);
        jP_landing_Bestellen_stagioni_Back.setLayout(jP_landing_Bestellen_stagioni_BackLayout);
        jP_landing_Bestellen_stagioni_BackLayout.setHorizontalGroup(
            jP_landing_Bestellen_stagioni_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_stagioni_BackLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel74)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_stagioni_BackLayout.setVerticalGroup(
            jP_landing_Bestellen_stagioni_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_stagioni_BackLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel74)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_stagioni_toCart.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_stagioni_toCart.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_stagioni_toCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_toCartMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_toCartMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_toCartMouseEntered(evt);
            }
        });

        jp_landing_Bestellen_stagioni_toCart_Label.setBackground(new java.awt.Color(0, 0, 0));
        jp_landing_Bestellen_stagioni_toCart_Label.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jp_landing_Bestellen_stagioni_toCart_Label.setForeground(new java.awt.Color(250, 250, 250));
        jp_landing_Bestellen_stagioni_toCart_Label.setText("In den Warenkorb");
        jp_landing_Bestellen_stagioni_toCart_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_stagioni_toCart_LabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_stagioni_toCart_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_stagioni_toCartLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_stagioni_toCart);
        jP_landing_Bestellen_stagioni_toCart.setLayout(jP_landing_Bestellen_stagioni_toCartLayout);
        jP_landing_Bestellen_stagioni_toCartLayout.setHorizontalGroup(
            jP_landing_Bestellen_stagioni_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_stagioni_toCartLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jp_landing_Bestellen_stagioni_toCart_Label)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_stagioni_toCartLayout.setVerticalGroup(
            jP_landing_Bestellen_stagioni_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_stagioni_toCartLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jp_landing_Bestellen_stagioni_toCart_Label)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_stagioni_Plus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_stagioni_Plus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_stagioni_Plus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_PlusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_PlusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_PlusMouseExited(evt);
            }
        });

        jLabel76.setBackground(new java.awt.Color(0, 0, 0));
        jLabel76.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(250, 250, 250));
        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel76.setText("+");
        jLabel76.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel76MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel76MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel76MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_stagioni_PlusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_stagioni_Plus);
        jP_landing_Bestellen_stagioni_Plus.setLayout(jP_landing_Bestellen_stagioni_PlusLayout);
        jP_landing_Bestellen_stagioni_PlusLayout.setHorizontalGroup(
            jP_landing_Bestellen_stagioni_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_stagioni_PlusLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );
        jP_landing_Bestellen_stagioni_PlusLayout.setVerticalGroup(
            jP_landing_Bestellen_stagioni_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_stagioni_PlusLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel76)
                .addContainerGap())
        );

        jP_landing_Bestellen_stagioni_Count.setBackground(new java.awt.Color(255, 206, 168));
        jP_landing_Bestellen_stagioni_Count.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_stagioni_Count.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_CountMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_CountMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_CountMouseExited(evt);
            }
        });

        jP_landing_Bestellen_stagioni_Counter.setBackground(new java.awt.Color(0, 0, 0));
        jP_landing_Bestellen_stagioni_Counter.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jP_landing_Bestellen_stagioni_Counter.setForeground(new java.awt.Color(250, 250, 250));
        jP_landing_Bestellen_stagioni_Counter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jP_landing_Bestellen_stagioni_Counter.setText("1");
        jP_landing_Bestellen_stagioni_Counter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_CounterMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_CounterMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_stagioni_CountLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_stagioni_Count);
        jP_landing_Bestellen_stagioni_Count.setLayout(jP_landing_Bestellen_stagioni_CountLayout);
        jP_landing_Bestellen_stagioni_CountLayout.setHorizontalGroup(
            jP_landing_Bestellen_stagioni_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_stagioni_CountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_landing_Bestellen_stagioni_Counter, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_Bestellen_stagioni_CountLayout.setVerticalGroup(
            jP_landing_Bestellen_stagioni_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_stagioni_CountLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jP_landing_Bestellen_stagioni_Counter)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_stagioni_Minus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_stagioni_Minus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_stagioni_Minus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_MinusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_MinusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_stagioni_MinusMouseExited(evt);
            }
        });

        jLabel77.setBackground(new java.awt.Color(0, 0, 0));
        jLabel77.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(250, 250, 250));
        jLabel77.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel77.setText("-");
        jLabel77.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel77MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel77MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel77MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_stagioni_MinusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_stagioni_Minus);
        jP_landing_Bestellen_stagioni_Minus.setLayout(jP_landing_Bestellen_stagioni_MinusLayout);
        jP_landing_Bestellen_stagioni_MinusLayout.setHorizontalGroup(
            jP_landing_Bestellen_stagioni_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_stagioni_MinusLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );
        jP_landing_Bestellen_stagioni_MinusLayout.setVerticalGroup(
            jP_landing_Bestellen_stagioni_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_stagioni_MinusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel77)
                .addContainerGap())
        );

        jLabel78.setBackground(new java.awt.Color(0, 0, 0));
        jLabel78.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 60)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(250, 130, 49));
        jLabel78.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel78.setText("Pizza Stagioni");

        javax.swing.GroupLayout jP_landing_Bestellen_stagioniLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_stagioni);
        jP_landing_Bestellen_stagioni.setLayout(jP_landing_Bestellen_stagioniLayout);
        jP_landing_Bestellen_stagioniLayout.setHorizontalGroup(
            jP_landing_Bestellen_stagioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_stagioniLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jP_landing_Bestellen_stagioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jP_landing_Bestellen_stagioniLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_stagioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cB_stagioni_Tomatensosse)
                            .addComponent(cB_stagioni_Kaese)
                            .addComponent(cB_stagioni_Rucola)
                            .addComponent(cB_stagioni_Zwiebeln))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jP_landing_Bestellen_stagioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_stagioniLayout.createSequentialGroup()
                                .addComponent(jLabel72)
                                .addGap(18, 18, 18)
                                .addComponent(jP_landing_Bestellen_stagioni_Minus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_Bestellen_stagioni_Count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_Bestellen_stagioni_Plus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel73, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_stagioniLayout.createSequentialGroup()
                        .addComponent(jP_landing_Bestellen_stagioni_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                        .addComponent(jP_landing_Bestellen_stagioni_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jP_landing_Bestellen_stagioniLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_stagioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel71)
                            .addComponent(cB_stagioni_Salami))
                        .addGap(52, 52, 52)
                        .addComponent(jLabel78)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(43, 43, 43))
        );
        jP_landing_Bestellen_stagioniLayout.setVerticalGroup(
            jP_landing_Bestellen_stagioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_stagioniLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jP_landing_Bestellen_stagioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_stagioniLayout.createSequentialGroup()
                        .addComponent(jLabel71)
                        .addGap(48, 48, 48))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_stagioniLayout.createSequentialGroup()
                        .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64)))
                .addGroup(jP_landing_Bestellen_stagioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jP_landing_Bestellen_stagioniLayout.createSequentialGroup()
                        .addComponent(cB_stagioni_Salami)
                        .addGap(19, 19, 19)
                        .addComponent(cB_stagioni_Zwiebeln)
                        .addGap(18, 18, 18)
                        .addComponent(cB_stagioni_Rucola)
                        .addGap(18, 18, 18)
                        .addComponent(cB_stagioni_Kaese)
                        .addGap(18, 18, 18)
                        .addComponent(cB_stagioni_Tomatensosse))
                    .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jP_landing_Bestellen_stagioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Bestellen_stagioniLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jP_landing_Bestellen_stagioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jP_landing_Bestellen_stagioni_Count, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_stagioni_Minus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_stagioni_Plus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_stagioniLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jP_landing_Bestellen_stagioniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Bestellen_stagioni_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_landing_Bestellen_stagioni_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );

        jP_landing_Bestellen.add(jP_landing_Bestellen_stagioni, "card3");

        jLabel94.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_rucula.png"))); // NOI18N

        jLabel95.setBackground(new java.awt.Color(0, 0, 0));
        jLabel95.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(250, 130, 49));
        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel95.setText("Anzahl:");

        cB_rucula_Rucola.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_rucula_Rucola.setForeground(new java.awt.Color(255, 173, 109));
        cB_rucula_Rucola.setSelected(true);
        cB_rucula_Rucola.setText("Rucola");

        cB_rucula_Salami.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_rucula_Salami.setForeground(new java.awt.Color(255, 173, 109));
        cB_rucula_Salami.setText("Salami");

        cB_rucula_Kaese.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_rucula_Kaese.setForeground(new java.awt.Color(255, 173, 109));
        cB_rucula_Kaese.setSelected(true);
        cB_rucula_Kaese.setText("Käse");

        cB_rucula_Zwiebeln.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_rucula_Zwiebeln.setForeground(new java.awt.Color(255, 173, 109));
        cB_rucula_Zwiebeln.setSelected(true);
        cB_rucula_Zwiebeln.setText("Zwiebeln");

        cB_rucula_Tomatensosse.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_rucula_Tomatensosse.setForeground(new java.awt.Color(255, 173, 109));
        cB_rucula_Tomatensosse.setSelected(true);
        cB_rucula_Tomatensosse.setText("Tomatensoße");

        jLabel96.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 22)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(255, 173, 109));
        jLabel96.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel96.setText("<html><p style=\"text-align:center;\">Unsere Pizza Rucula wird mit frischem Rucola, Tomaten und Parmesan belegt und ist ein echter Klassiker.</p></html>");

        jP_landing_Bestellen_rucula_Back.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_rucula_Back.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_rucula_Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_BackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_BackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_BackMouseExited(evt);
            }
        });

        jLabel97.setBackground(new java.awt.Color(0, 0, 0));
        jLabel97.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(250, 250, 250));
        jLabel97.setText("Zurück");
        jLabel97.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel97MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel97MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_rucula_BackLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_rucula_Back);
        jP_landing_Bestellen_rucula_Back.setLayout(jP_landing_Bestellen_rucula_BackLayout);
        jP_landing_Bestellen_rucula_BackLayout.setHorizontalGroup(
            jP_landing_Bestellen_rucula_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_rucula_BackLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel97)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_rucula_BackLayout.setVerticalGroup(
            jP_landing_Bestellen_rucula_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_rucula_BackLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel97)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_rucula_toCart.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_rucula_toCart.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_rucula_toCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_toCartMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_toCartMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_toCartMouseEntered(evt);
            }
        });

        jp_landing_Bestellen_rucula_toCart_Label.setBackground(new java.awt.Color(0, 0, 0));
        jp_landing_Bestellen_rucula_toCart_Label.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jp_landing_Bestellen_rucula_toCart_Label.setForeground(new java.awt.Color(250, 250, 250));
        jp_landing_Bestellen_rucula_toCart_Label.setText("In den Warenkorb");
        jp_landing_Bestellen_rucula_toCart_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_rucula_toCart_LabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_rucula_toCart_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_rucula_toCartLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_rucula_toCart);
        jP_landing_Bestellen_rucula_toCart.setLayout(jP_landing_Bestellen_rucula_toCartLayout);
        jP_landing_Bestellen_rucula_toCartLayout.setHorizontalGroup(
            jP_landing_Bestellen_rucula_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_rucula_toCartLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jp_landing_Bestellen_rucula_toCart_Label)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_rucula_toCartLayout.setVerticalGroup(
            jP_landing_Bestellen_rucula_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_rucula_toCartLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jp_landing_Bestellen_rucula_toCart_Label)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_rucula_Plus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_rucula_Plus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_rucula_Plus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_PlusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_PlusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_PlusMouseExited(evt);
            }
        });

        jLabel99.setBackground(new java.awt.Color(0, 0, 0));
        jLabel99.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(250, 250, 250));
        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel99.setText("+");
        jLabel99.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel99MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel99MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel99MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_rucula_PlusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_rucula_Plus);
        jP_landing_Bestellen_rucula_Plus.setLayout(jP_landing_Bestellen_rucula_PlusLayout);
        jP_landing_Bestellen_rucula_PlusLayout.setHorizontalGroup(
            jP_landing_Bestellen_rucula_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_rucula_PlusLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel99, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );
        jP_landing_Bestellen_rucula_PlusLayout.setVerticalGroup(
            jP_landing_Bestellen_rucula_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_rucula_PlusLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel99)
                .addContainerGap())
        );

        jP_landing_Bestellen_rucula_Count.setBackground(new java.awt.Color(255, 206, 168));
        jP_landing_Bestellen_rucula_Count.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_rucula_Count.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_CountMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_CountMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_CountMouseExited(evt);
            }
        });

        jP_landing_Bestellen_rucula_Counter.setBackground(new java.awt.Color(0, 0, 0));
        jP_landing_Bestellen_rucula_Counter.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jP_landing_Bestellen_rucula_Counter.setForeground(new java.awt.Color(250, 250, 250));
        jP_landing_Bestellen_rucula_Counter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jP_landing_Bestellen_rucula_Counter.setText("1");
        jP_landing_Bestellen_rucula_Counter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_CounterMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_CounterMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_rucula_CountLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_rucula_Count);
        jP_landing_Bestellen_rucula_Count.setLayout(jP_landing_Bestellen_rucula_CountLayout);
        jP_landing_Bestellen_rucula_CountLayout.setHorizontalGroup(
            jP_landing_Bestellen_rucula_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_rucula_CountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_landing_Bestellen_rucula_Counter, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_Bestellen_rucula_CountLayout.setVerticalGroup(
            jP_landing_Bestellen_rucula_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_rucula_CountLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jP_landing_Bestellen_rucula_Counter)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_rucula_Minus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_rucula_Minus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_rucula_Minus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_MinusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_MinusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_rucula_MinusMouseExited(evt);
            }
        });

        jLabel100.setBackground(new java.awt.Color(0, 0, 0));
        jLabel100.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(250, 250, 250));
        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel100.setText("-");
        jLabel100.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel100MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel100MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel100MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_rucula_MinusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_rucula_Minus);
        jP_landing_Bestellen_rucula_Minus.setLayout(jP_landing_Bestellen_rucula_MinusLayout);
        jP_landing_Bestellen_rucula_MinusLayout.setHorizontalGroup(
            jP_landing_Bestellen_rucula_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_rucula_MinusLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel100, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );
        jP_landing_Bestellen_rucula_MinusLayout.setVerticalGroup(
            jP_landing_Bestellen_rucula_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_rucula_MinusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel100)
                .addContainerGap())
        );

        jLabel101.setBackground(new java.awt.Color(0, 0, 0));
        jLabel101.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 60)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(250, 130, 49));
        jLabel101.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel101.setText("Pizza Rucula");

        javax.swing.GroupLayout jP_landing_Bestellen_ruculaLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_rucula);
        jP_landing_Bestellen_rucula.setLayout(jP_landing_Bestellen_ruculaLayout);
        jP_landing_Bestellen_ruculaLayout.setHorizontalGroup(
            jP_landing_Bestellen_ruculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_ruculaLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jP_landing_Bestellen_ruculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jP_landing_Bestellen_ruculaLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_ruculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cB_rucula_Tomatensosse)
                            .addComponent(cB_rucula_Kaese)
                            .addComponent(cB_rucula_Rucola)
                            .addComponent(cB_rucula_Zwiebeln))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jP_landing_Bestellen_ruculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_ruculaLayout.createSequentialGroup()
                                .addComponent(jLabel95)
                                .addGap(18, 18, 18)
                                .addComponent(jP_landing_Bestellen_rucula_Minus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_Bestellen_rucula_Count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_Bestellen_rucula_Plus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel96, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_ruculaLayout.createSequentialGroup()
                        .addComponent(jP_landing_Bestellen_rucula_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                        .addComponent(jP_landing_Bestellen_rucula_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jP_landing_Bestellen_ruculaLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_ruculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel94)
                            .addComponent(cB_rucula_Salami))
                        .addGap(52, 52, 52)
                        .addComponent(jLabel101)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(43, 43, 43))
        );
        jP_landing_Bestellen_ruculaLayout.setVerticalGroup(
            jP_landing_Bestellen_ruculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_ruculaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jP_landing_Bestellen_ruculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_ruculaLayout.createSequentialGroup()
                        .addComponent(jLabel94)
                        .addGap(48, 48, 48))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_ruculaLayout.createSequentialGroup()
                        .addComponent(jLabel101, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64)))
                .addGroup(jP_landing_Bestellen_ruculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jP_landing_Bestellen_ruculaLayout.createSequentialGroup()
                        .addComponent(cB_rucula_Salami)
                        .addGap(19, 19, 19)
                        .addComponent(cB_rucula_Zwiebeln)
                        .addGap(18, 18, 18)
                        .addComponent(cB_rucula_Rucola)
                        .addGap(18, 18, 18)
                        .addComponent(cB_rucula_Kaese)
                        .addGap(18, 18, 18)
                        .addComponent(cB_rucula_Tomatensosse))
                    .addComponent(jLabel96, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jP_landing_Bestellen_ruculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Bestellen_ruculaLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jP_landing_Bestellen_ruculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jP_landing_Bestellen_rucula_Count, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_rucula_Minus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_rucula_Plus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_ruculaLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel95, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jP_landing_Bestellen_ruculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Bestellen_rucula_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_landing_Bestellen_rucula_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );

        jP_landing_Bestellen.add(jP_landing_Bestellen_rucula, "card3");

        jLabel102.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_caprese.png"))); // NOI18N

        jLabel103.setBackground(new java.awt.Color(0, 0, 0));
        jLabel103.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel103.setForeground(new java.awt.Color(250, 130, 49));
        jLabel103.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel103.setText("Anzahl:");

        cB_caprese_Rucola.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_caprese_Rucola.setForeground(new java.awt.Color(255, 173, 109));
        cB_caprese_Rucola.setText("Rucola");

        cB_caprese_Salami.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_caprese_Salami.setForeground(new java.awt.Color(255, 173, 109));
        cB_caprese_Salami.setSelected(true);
        cB_caprese_Salami.setText("Salami");

        cB_caprese_Kaese.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_caprese_Kaese.setForeground(new java.awt.Color(255, 173, 109));
        cB_caprese_Kaese.setSelected(true);
        cB_caprese_Kaese.setText("Käse");

        cB_caprese_Zwiebeln.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_caprese_Zwiebeln.setForeground(new java.awt.Color(255, 173, 109));
        cB_caprese_Zwiebeln.setSelected(true);
        cB_caprese_Zwiebeln.setText("Zwiebeln");

        cB_caprese_Tomatensosse.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_caprese_Tomatensosse.setForeground(new java.awt.Color(255, 173, 109));
        cB_caprese_Tomatensosse.setSelected(true);
        cB_caprese_Tomatensosse.setText("Tomatensoße");

        jLabel104.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 22)); // NOI18N
        jLabel104.setForeground(new java.awt.Color(255, 173, 109));
        jLabel104.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel104.setText("<html><p style=\"text-align:center;\">Für alle Fans der mediterranen Küche empfehlen wir unsere Pizza Caprese, belegt mit Tomaten, Mozzarella und Basilikum.</p></html>");

        jP_landing_Bestellen_caprese_Back.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_caprese_Back.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_caprese_Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_BackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_BackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_BackMouseExited(evt);
            }
        });

        jLabel105.setBackground(new java.awt.Color(0, 0, 0));
        jLabel105.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel105.setForeground(new java.awt.Color(250, 250, 250));
        jLabel105.setText("Zurück");
        jLabel105.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel105MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel105MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_caprese_BackLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_caprese_Back);
        jP_landing_Bestellen_caprese_Back.setLayout(jP_landing_Bestellen_caprese_BackLayout);
        jP_landing_Bestellen_caprese_BackLayout.setHorizontalGroup(
            jP_landing_Bestellen_caprese_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_caprese_BackLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel105)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_caprese_BackLayout.setVerticalGroup(
            jP_landing_Bestellen_caprese_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_caprese_BackLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel105)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_caprese_toCart.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_caprese_toCart.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_caprese_toCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_toCartMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_toCartMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_toCartMouseEntered(evt);
            }
        });

        jp_landing_Bestellen_caprese_toCart_Label.setBackground(new java.awt.Color(0, 0, 0));
        jp_landing_Bestellen_caprese_toCart_Label.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jp_landing_Bestellen_caprese_toCart_Label.setForeground(new java.awt.Color(250, 250, 250));
        jp_landing_Bestellen_caprese_toCart_Label.setText("In den Warenkorb");
        jp_landing_Bestellen_caprese_toCart_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_caprese_toCart_LabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_caprese_toCart_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_caprese_toCartLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_caprese_toCart);
        jP_landing_Bestellen_caprese_toCart.setLayout(jP_landing_Bestellen_caprese_toCartLayout);
        jP_landing_Bestellen_caprese_toCartLayout.setHorizontalGroup(
            jP_landing_Bestellen_caprese_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_caprese_toCartLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jp_landing_Bestellen_caprese_toCart_Label)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_caprese_toCartLayout.setVerticalGroup(
            jP_landing_Bestellen_caprese_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_caprese_toCartLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jp_landing_Bestellen_caprese_toCart_Label)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_caprese_Plus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_caprese_Plus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_caprese_Plus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_PlusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_PlusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_PlusMouseExited(evt);
            }
        });

        jLabel107.setBackground(new java.awt.Color(0, 0, 0));
        jLabel107.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel107.setForeground(new java.awt.Color(250, 250, 250));
        jLabel107.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel107.setText("+");
        jLabel107.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel107MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel107MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel107MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_caprese_PlusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_caprese_Plus);
        jP_landing_Bestellen_caprese_Plus.setLayout(jP_landing_Bestellen_caprese_PlusLayout);
        jP_landing_Bestellen_caprese_PlusLayout.setHorizontalGroup(
            jP_landing_Bestellen_caprese_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_caprese_PlusLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel107, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );
        jP_landing_Bestellen_caprese_PlusLayout.setVerticalGroup(
            jP_landing_Bestellen_caprese_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_caprese_PlusLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel107)
                .addContainerGap())
        );

        jP_landing_Bestellen_caprese_Count.setBackground(new java.awt.Color(255, 206, 168));
        jP_landing_Bestellen_caprese_Count.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_caprese_Count.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_CountMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_CountMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_CountMouseExited(evt);
            }
        });

        jP_landing_Bestellen_caprese_Counter.setBackground(new java.awt.Color(0, 0, 0));
        jP_landing_Bestellen_caprese_Counter.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jP_landing_Bestellen_caprese_Counter.setForeground(new java.awt.Color(250, 250, 250));
        jP_landing_Bestellen_caprese_Counter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jP_landing_Bestellen_caprese_Counter.setText("1");
        jP_landing_Bestellen_caprese_Counter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_CounterMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_CounterMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_caprese_CountLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_caprese_Count);
        jP_landing_Bestellen_caprese_Count.setLayout(jP_landing_Bestellen_caprese_CountLayout);
        jP_landing_Bestellen_caprese_CountLayout.setHorizontalGroup(
            jP_landing_Bestellen_caprese_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_caprese_CountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_landing_Bestellen_caprese_Counter, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_Bestellen_caprese_CountLayout.setVerticalGroup(
            jP_landing_Bestellen_caprese_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_caprese_CountLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jP_landing_Bestellen_caprese_Counter)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_caprese_Minus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_caprese_Minus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_caprese_Minus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_MinusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_MinusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_caprese_MinusMouseExited(evt);
            }
        });

        jLabel108.setBackground(new java.awt.Color(0, 0, 0));
        jLabel108.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel108.setForeground(new java.awt.Color(250, 250, 250));
        jLabel108.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel108.setText("-");
        jLabel108.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel108MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel108MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel108MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_caprese_MinusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_caprese_Minus);
        jP_landing_Bestellen_caprese_Minus.setLayout(jP_landing_Bestellen_caprese_MinusLayout);
        jP_landing_Bestellen_caprese_MinusLayout.setHorizontalGroup(
            jP_landing_Bestellen_caprese_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_caprese_MinusLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel108, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );
        jP_landing_Bestellen_caprese_MinusLayout.setVerticalGroup(
            jP_landing_Bestellen_caprese_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_caprese_MinusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel108)
                .addContainerGap())
        );

        jLabel109.setBackground(new java.awt.Color(0, 0, 0));
        jLabel109.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 60)); // NOI18N
        jLabel109.setForeground(new java.awt.Color(250, 130, 49));
        jLabel109.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel109.setText("Pizza Caprese");

        javax.swing.GroupLayout jP_landing_Bestellen_capreseLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_caprese);
        jP_landing_Bestellen_caprese.setLayout(jP_landing_Bestellen_capreseLayout);
        jP_landing_Bestellen_capreseLayout.setHorizontalGroup(
            jP_landing_Bestellen_capreseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_capreseLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jP_landing_Bestellen_capreseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jP_landing_Bestellen_capreseLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_capreseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cB_caprese_Tomatensosse)
                            .addComponent(cB_caprese_Kaese)
                            .addComponent(cB_caprese_Rucola)
                            .addComponent(cB_caprese_Zwiebeln))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jP_landing_Bestellen_capreseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_capreseLayout.createSequentialGroup()
                                .addComponent(jLabel103)
                                .addGap(18, 18, 18)
                                .addComponent(jP_landing_Bestellen_caprese_Minus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_Bestellen_caprese_Count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_Bestellen_caprese_Plus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel104, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_capreseLayout.createSequentialGroup()
                        .addComponent(jP_landing_Bestellen_caprese_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                        .addComponent(jP_landing_Bestellen_caprese_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jP_landing_Bestellen_capreseLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_capreseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel102)
                            .addComponent(cB_caprese_Salami))
                        .addGap(39, 39, 39)
                        .addComponent(jLabel109)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(43, 43, 43))
        );
        jP_landing_Bestellen_capreseLayout.setVerticalGroup(
            jP_landing_Bestellen_capreseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_capreseLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jP_landing_Bestellen_capreseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_capreseLayout.createSequentialGroup()
                        .addComponent(jLabel109, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_capreseLayout.createSequentialGroup()
                        .addComponent(jLabel102)
                        .addGap(48, 48, 48)))
                .addGroup(jP_landing_Bestellen_capreseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jP_landing_Bestellen_capreseLayout.createSequentialGroup()
                        .addComponent(cB_caprese_Salami)
                        .addGap(19, 19, 19)
                        .addComponent(cB_caprese_Zwiebeln)
                        .addGap(18, 18, 18)
                        .addComponent(cB_caprese_Rucola)
                        .addGap(18, 18, 18)
                        .addComponent(cB_caprese_Kaese)
                        .addGap(18, 18, 18)
                        .addComponent(cB_caprese_Tomatensosse))
                    .addComponent(jLabel104, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jP_landing_Bestellen_capreseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Bestellen_capreseLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jP_landing_Bestellen_capreseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jP_landing_Bestellen_caprese_Count, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_caprese_Minus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_caprese_Plus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_capreseLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel103, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jP_landing_Bestellen_capreseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Bestellen_caprese_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_landing_Bestellen_caprese_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );

        jP_landing_Bestellen.add(jP_landing_Bestellen_caprese, "card3");

        jLabel110.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_funghi.png"))); // NOI18N

        jLabel111.setBackground(new java.awt.Color(0, 0, 0));
        jLabel111.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel111.setForeground(new java.awt.Color(250, 130, 49));
        jLabel111.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel111.setText("Anzahl:");

        cB_funghi_Rucola.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_funghi_Rucola.setForeground(new java.awt.Color(255, 173, 109));
        cB_funghi_Rucola.setText("Rucola");

        cB_funghi_Salami.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_funghi_Salami.setForeground(new java.awt.Color(255, 173, 109));
        cB_funghi_Salami.setSelected(true);
        cB_funghi_Salami.setText("Salami");

        cB_funghi_Kaese.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_funghi_Kaese.setForeground(new java.awt.Color(255, 173, 109));
        cB_funghi_Kaese.setSelected(true);
        cB_funghi_Kaese.setText("Käse");

        cB_funghi_Zwiebeln.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_funghi_Zwiebeln.setForeground(new java.awt.Color(255, 173, 109));
        cB_funghi_Zwiebeln.setSelected(true);
        cB_funghi_Zwiebeln.setText("Zwiebeln");

        cB_funghi_Tomatensosse.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_funghi_Tomatensosse.setForeground(new java.awt.Color(255, 173, 109));
        cB_funghi_Tomatensosse.setSelected(true);
        cB_funghi_Tomatensosse.setText("Tomatensoße");

        jLabel112.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 22)); // NOI18N
        jLabel112.setForeground(new java.awt.Color(255, 173, 109));
        jLabel112.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel112.setText("<html><p style=\"text-align:center;\">Die Pizza Funghi wird mit frischen Champignons und Käse belegt und ist eine beliebte Wahl bei Pilzliebhabern.</p></html>");

        jP_landing_Bestellen_funghi_Back.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_funghi_Back.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_funghi_Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_BackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_BackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_BackMouseExited(evt);
            }
        });

        jLabel113.setBackground(new java.awt.Color(0, 0, 0));
        jLabel113.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel113.setForeground(new java.awt.Color(250, 250, 250));
        jLabel113.setText("Zurück");
        jLabel113.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel113MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel113MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_funghi_BackLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_funghi_Back);
        jP_landing_Bestellen_funghi_Back.setLayout(jP_landing_Bestellen_funghi_BackLayout);
        jP_landing_Bestellen_funghi_BackLayout.setHorizontalGroup(
            jP_landing_Bestellen_funghi_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_funghi_BackLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel113)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_funghi_BackLayout.setVerticalGroup(
            jP_landing_Bestellen_funghi_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_funghi_BackLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel113)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_funghi_toCart.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_funghi_toCart.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_funghi_toCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_toCartMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_toCartMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_toCartMouseEntered(evt);
            }
        });

        jp_landing_Bestellen_funghi_toCart_Label.setBackground(new java.awt.Color(0, 0, 0));
        jp_landing_Bestellen_funghi_toCart_Label.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jp_landing_Bestellen_funghi_toCart_Label.setForeground(new java.awt.Color(250, 250, 250));
        jp_landing_Bestellen_funghi_toCart_Label.setText("In den Warenkorb");
        jp_landing_Bestellen_funghi_toCart_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_funghi_toCart_LabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_funghi_toCart_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_funghi_toCartLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_funghi_toCart);
        jP_landing_Bestellen_funghi_toCart.setLayout(jP_landing_Bestellen_funghi_toCartLayout);
        jP_landing_Bestellen_funghi_toCartLayout.setHorizontalGroup(
            jP_landing_Bestellen_funghi_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_funghi_toCartLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jp_landing_Bestellen_funghi_toCart_Label)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_funghi_toCartLayout.setVerticalGroup(
            jP_landing_Bestellen_funghi_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_funghi_toCartLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jp_landing_Bestellen_funghi_toCart_Label)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_funghi_Plus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_funghi_Plus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_funghi_Plus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_PlusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_PlusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_PlusMouseExited(evt);
            }
        });

        jLabel115.setBackground(new java.awt.Color(0, 0, 0));
        jLabel115.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel115.setForeground(new java.awt.Color(250, 250, 250));
        jLabel115.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel115.setText("+");
        jLabel115.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel115MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel115MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel115MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_funghi_PlusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_funghi_Plus);
        jP_landing_Bestellen_funghi_Plus.setLayout(jP_landing_Bestellen_funghi_PlusLayout);
        jP_landing_Bestellen_funghi_PlusLayout.setHorizontalGroup(
            jP_landing_Bestellen_funghi_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_funghi_PlusLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel115, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );
        jP_landing_Bestellen_funghi_PlusLayout.setVerticalGroup(
            jP_landing_Bestellen_funghi_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_funghi_PlusLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel115)
                .addContainerGap())
        );

        jP_landing_Bestellen_funghi_Count.setBackground(new java.awt.Color(255, 206, 168));
        jP_landing_Bestellen_funghi_Count.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_funghi_Count.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_CountMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_CountMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_CountMouseExited(evt);
            }
        });

        jP_landing_Bestellen_funghi_Counter.setBackground(new java.awt.Color(0, 0, 0));
        jP_landing_Bestellen_funghi_Counter.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jP_landing_Bestellen_funghi_Counter.setForeground(new java.awt.Color(250, 250, 250));
        jP_landing_Bestellen_funghi_Counter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jP_landing_Bestellen_funghi_Counter.setText("1");
        jP_landing_Bestellen_funghi_Counter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_CounterMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_CounterMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_funghi_CountLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_funghi_Count);
        jP_landing_Bestellen_funghi_Count.setLayout(jP_landing_Bestellen_funghi_CountLayout);
        jP_landing_Bestellen_funghi_CountLayout.setHorizontalGroup(
            jP_landing_Bestellen_funghi_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_funghi_CountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_landing_Bestellen_funghi_Counter, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_Bestellen_funghi_CountLayout.setVerticalGroup(
            jP_landing_Bestellen_funghi_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_funghi_CountLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jP_landing_Bestellen_funghi_Counter)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_funghi_Minus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_funghi_Minus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_funghi_Minus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_MinusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_MinusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_funghi_MinusMouseExited(evt);
            }
        });

        jLabel116.setBackground(new java.awt.Color(0, 0, 0));
        jLabel116.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel116.setForeground(new java.awt.Color(250, 250, 250));
        jLabel116.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel116.setText("-");
        jLabel116.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel116MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel116MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel116MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_funghi_MinusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_funghi_Minus);
        jP_landing_Bestellen_funghi_Minus.setLayout(jP_landing_Bestellen_funghi_MinusLayout);
        jP_landing_Bestellen_funghi_MinusLayout.setHorizontalGroup(
            jP_landing_Bestellen_funghi_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_funghi_MinusLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel116, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );
        jP_landing_Bestellen_funghi_MinusLayout.setVerticalGroup(
            jP_landing_Bestellen_funghi_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_funghi_MinusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel116)
                .addContainerGap())
        );

        jLabel117.setBackground(new java.awt.Color(0, 0, 0));
        jLabel117.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 60)); // NOI18N
        jLabel117.setForeground(new java.awt.Color(250, 130, 49));
        jLabel117.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel117.setText("Pizza Funghi");

        javax.swing.GroupLayout jP_landing_Bestellen_funghiLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_funghi);
        jP_landing_Bestellen_funghi.setLayout(jP_landing_Bestellen_funghiLayout);
        jP_landing_Bestellen_funghiLayout.setHorizontalGroup(
            jP_landing_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_funghiLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jP_landing_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Bestellen_funghiLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jP_landing_Bestellen_funghiLayout.createSequentialGroup()
                                .addGroup(jP_landing_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cB_funghi_Tomatensosse)
                                    .addComponent(cB_funghi_Kaese)
                                    .addComponent(cB_funghi_Rucola)
                                    .addComponent(cB_funghi_Zwiebeln)
                                    .addGroup(jP_landing_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel110)
                                        .addComponent(cB_funghi_Salami)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jP_landing_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_funghiLayout.createSequentialGroup()
                                        .addComponent(jLabel111)
                                        .addGap(18, 18, 18)
                                        .addComponent(jP_landing_Bestellen_funghi_Minus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jP_landing_Bestellen_funghi_Count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jP_landing_Bestellen_funghi_Plus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel112, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jP_landing_Bestellen_funghiLayout.createSequentialGroup()
                                .addComponent(jP_landing_Bestellen_funghi_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                                .addComponent(jP_landing_Bestellen_funghi_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(43, 43, 43))
                    .addGroup(jP_landing_Bestellen_funghiLayout.createSequentialGroup()
                        .addGap(189, 189, 189)
                        .addComponent(jLabel117, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jP_landing_Bestellen_funghiLayout.setVerticalGroup(
            jP_landing_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_funghiLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jP_landing_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_funghiLayout.createSequentialGroup()
                        .addComponent(jLabel117, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_funghiLayout.createSequentialGroup()
                        .addComponent(jLabel110)
                        .addGap(48, 48, 48)))
                .addGroup(jP_landing_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jP_landing_Bestellen_funghiLayout.createSequentialGroup()
                        .addComponent(cB_funghi_Salami)
                        .addGap(19, 19, 19)
                        .addComponent(cB_funghi_Zwiebeln)
                        .addGap(18, 18, 18)
                        .addComponent(cB_funghi_Rucola)
                        .addGap(18, 18, 18)
                        .addComponent(cB_funghi_Kaese)
                        .addGap(18, 18, 18)
                        .addComponent(cB_funghi_Tomatensosse))
                    .addComponent(jLabel112, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jP_landing_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Bestellen_funghiLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jP_landing_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jP_landing_Bestellen_funghi_Count, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_funghi_Minus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_funghi_Plus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_funghiLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jP_landing_Bestellen_funghiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Bestellen_funghi_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_landing_Bestellen_funghi_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );

        jP_landing_Bestellen.add(jP_landing_Bestellen_funghi, "card3");

        jLabel118.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_margherita.png"))); // NOI18N

        jLabel119.setBackground(new java.awt.Color(0, 0, 0));
        jLabel119.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel119.setForeground(new java.awt.Color(250, 130, 49));
        jLabel119.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel119.setText("Anzahl:");

        cB_margherita_Rucola.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_margherita_Rucola.setForeground(new java.awt.Color(255, 173, 109));
        cB_margherita_Rucola.setText("Rucola");

        cB_margherita_Salami.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_margherita_Salami.setForeground(new java.awt.Color(255, 173, 109));
        cB_margherita_Salami.setText("Salami");

        cB_margherita_Kaese.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_margherita_Kaese.setForeground(new java.awt.Color(255, 173, 109));
        cB_margherita_Kaese.setSelected(true);
        cB_margherita_Kaese.setText("Käse");

        cB_margherita_Zwiebeln.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_margherita_Zwiebeln.setForeground(new java.awt.Color(255, 173, 109));
        cB_margherita_Zwiebeln.setText("Zwiebeln");

        cB_margherita_Tomatensosse.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_margherita_Tomatensosse.setForeground(new java.awt.Color(255, 173, 109));
        cB_margherita_Tomatensosse.setSelected(true);
        cB_margherita_Tomatensosse.setText("Tomatensoße");

        jLabel120.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 22)); // NOI18N
        jLabel120.setForeground(new java.awt.Color(255, 173, 109));
        jLabel120.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel120.setText("<html><p style=\"text-align:center;\">Unsere Pizza Margherita ist ein echter Klassiker und besteht aus Tomaten, Mozzarella und Basilikum.</p></html>");

        jP_landing_Bestellen_margherita_Back.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_margherita_Back.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_margherita_Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_BackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_BackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_BackMouseExited(evt);
            }
        });

        jLabel121.setBackground(new java.awt.Color(0, 0, 0));
        jLabel121.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel121.setForeground(new java.awt.Color(250, 250, 250));
        jLabel121.setText("Zurück");
        jLabel121.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel121MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel121MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_margherita_BackLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_margherita_Back);
        jP_landing_Bestellen_margherita_Back.setLayout(jP_landing_Bestellen_margherita_BackLayout);
        jP_landing_Bestellen_margherita_BackLayout.setHorizontalGroup(
            jP_landing_Bestellen_margherita_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_margherita_BackLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel121)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_margherita_BackLayout.setVerticalGroup(
            jP_landing_Bestellen_margherita_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_margherita_BackLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel121)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_margherita_toCart.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_margherita_toCart.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_margherita_toCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_toCartMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_toCartMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_toCartMouseEntered(evt);
            }
        });

        jp_landing_Bestellen_margherita_toCart_Label.setBackground(new java.awt.Color(0, 0, 0));
        jp_landing_Bestellen_margherita_toCart_Label.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jp_landing_Bestellen_margherita_toCart_Label.setForeground(new java.awt.Color(250, 250, 250));
        jp_landing_Bestellen_margherita_toCart_Label.setText("In den Warenkorb");
        jp_landing_Bestellen_margherita_toCart_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_margherita_toCart_LabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_margherita_toCart_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_margherita_toCartLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_margherita_toCart);
        jP_landing_Bestellen_margherita_toCart.setLayout(jP_landing_Bestellen_margherita_toCartLayout);
        jP_landing_Bestellen_margherita_toCartLayout.setHorizontalGroup(
            jP_landing_Bestellen_margherita_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_margherita_toCartLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jp_landing_Bestellen_margherita_toCart_Label)
                .addGap(28, 28, 28))
        );
        jP_landing_Bestellen_margherita_toCartLayout.setVerticalGroup(
            jP_landing_Bestellen_margherita_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_margherita_toCartLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jp_landing_Bestellen_margherita_toCart_Label)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_margherita_Plus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_margherita_Plus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_margherita_Plus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_PlusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_PlusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_PlusMouseExited(evt);
            }
        });

        jLabel123.setBackground(new java.awt.Color(0, 0, 0));
        jLabel123.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel123.setForeground(new java.awt.Color(250, 250, 250));
        jLabel123.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel123.setText("+");
        jLabel123.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel123MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel123MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel123MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_margherita_PlusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_margherita_Plus);
        jP_landing_Bestellen_margherita_Plus.setLayout(jP_landing_Bestellen_margherita_PlusLayout);
        jP_landing_Bestellen_margherita_PlusLayout.setHorizontalGroup(
            jP_landing_Bestellen_margherita_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_margherita_PlusLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel123, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );
        jP_landing_Bestellen_margherita_PlusLayout.setVerticalGroup(
            jP_landing_Bestellen_margherita_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_margherita_PlusLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel123)
                .addContainerGap())
        );

        jP_landing_Bestellen_margherita_Count.setBackground(new java.awt.Color(255, 206, 168));
        jP_landing_Bestellen_margherita_Count.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_margherita_Count.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_CountMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_CountMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_CountMouseExited(evt);
            }
        });

        jP_landing_Bestellen_margherita_Counter.setBackground(new java.awt.Color(0, 0, 0));
        jP_landing_Bestellen_margherita_Counter.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jP_landing_Bestellen_margherita_Counter.setForeground(new java.awt.Color(250, 250, 250));
        jP_landing_Bestellen_margherita_Counter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jP_landing_Bestellen_margherita_Counter.setText("1");
        jP_landing_Bestellen_margherita_Counter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_CounterMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_CounterMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_margherita_CountLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_margherita_Count);
        jP_landing_Bestellen_margherita_Count.setLayout(jP_landing_Bestellen_margherita_CountLayout);
        jP_landing_Bestellen_margherita_CountLayout.setHorizontalGroup(
            jP_landing_Bestellen_margherita_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_margherita_CountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_landing_Bestellen_margherita_Counter, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_Bestellen_margherita_CountLayout.setVerticalGroup(
            jP_landing_Bestellen_margherita_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_margherita_CountLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jP_landing_Bestellen_margherita_Counter)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_landing_Bestellen_margherita_Minus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Bestellen_margherita_Minus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Bestellen_margherita_Minus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_MinusMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_MinusMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_margherita_MinusMouseExited(evt);
            }
        });

        jLabel124.setBackground(new java.awt.Color(0, 0, 0));
        jLabel124.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel124.setForeground(new java.awt.Color(250, 250, 250));
        jLabel124.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel124.setText("-");
        jLabel124.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel124MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel124MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel124MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Bestellen_margherita_MinusLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_margherita_Minus);
        jP_landing_Bestellen_margherita_Minus.setLayout(jP_landing_Bestellen_margherita_MinusLayout);
        jP_landing_Bestellen_margherita_MinusLayout.setHorizontalGroup(
            jP_landing_Bestellen_margherita_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_margherita_MinusLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel124, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );
        jP_landing_Bestellen_margherita_MinusLayout.setVerticalGroup(
            jP_landing_Bestellen_margherita_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_margherita_MinusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel124)
                .addContainerGap())
        );

        jLabel125.setBackground(new java.awt.Color(0, 0, 0));
        jLabel125.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 60)); // NOI18N
        jLabel125.setForeground(new java.awt.Color(250, 130, 49));
        jLabel125.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel125.setText("Pizza Margherita");

        javax.swing.GroupLayout jP_landing_Bestellen_margheritaLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_margherita);
        jP_landing_Bestellen_margherita.setLayout(jP_landing_Bestellen_margheritaLayout);
        jP_landing_Bestellen_margheritaLayout.setHorizontalGroup(
            jP_landing_Bestellen_margheritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_margheritaLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jP_landing_Bestellen_margheritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jP_landing_Bestellen_margheritaLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_margheritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cB_margherita_Tomatensosse)
                            .addComponent(cB_margherita_Kaese)
                            .addComponent(cB_margherita_Rucola)
                            .addComponent(cB_margherita_Zwiebeln))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jP_landing_Bestellen_margheritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_margheritaLayout.createSequentialGroup()
                                .addComponent(jLabel119)
                                .addGap(18, 18, 18)
                                .addComponent(jP_landing_Bestellen_margherita_Minus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_Bestellen_margherita_Count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_Bestellen_margherita_Plus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel120, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_margheritaLayout.createSequentialGroup()
                        .addComponent(jP_landing_Bestellen_margherita_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                        .addComponent(jP_landing_Bestellen_margherita_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jP_landing_Bestellen_margheritaLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_margheritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel118)
                            .addComponent(cB_margherita_Salami))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel125)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(43, 43, 43))
        );
        jP_landing_Bestellen_margheritaLayout.setVerticalGroup(
            jP_landing_Bestellen_margheritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_margheritaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jP_landing_Bestellen_margheritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_margheritaLayout.createSequentialGroup()
                        .addComponent(jLabel125, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_margheritaLayout.createSequentialGroup()
                        .addComponent(jLabel118)
                        .addGap(48, 48, 48)))
                .addGroup(jP_landing_Bestellen_margheritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jP_landing_Bestellen_margheritaLayout.createSequentialGroup()
                        .addComponent(cB_margherita_Salami)
                        .addGap(19, 19, 19)
                        .addComponent(cB_margherita_Zwiebeln)
                        .addGap(18, 18, 18)
                        .addComponent(cB_margherita_Rucola)
                        .addGap(18, 18, 18)
                        .addComponent(cB_margherita_Kaese)
                        .addGap(18, 18, 18)
                        .addComponent(cB_margherita_Tomatensosse))
                    .addComponent(jLabel120, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jP_landing_Bestellen_margheritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Bestellen_margheritaLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jP_landing_Bestellen_margheritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jP_landing_Bestellen_margherita_Count, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_margherita_Minus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_Bestellen_margherita_Plus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_margheritaLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel119, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jP_landing_Bestellen_margheritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_Bestellen_margherita_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_landing_Bestellen_margherita_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );

        jP_landing_Bestellen.add(jP_landing_Bestellen_margherita, "card3");

        jLabel126.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_pepperoni.png"))); // NOI18N

        jLabel127.setBackground(new java.awt.Color(0, 0, 0));
        jLabel127.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel127.setForeground(new java.awt.Color(250, 130, 49));
        jLabel127.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel127.setText("Anzahl:");

        cB_pepperoni_Rucola.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_pepperoni_Rucola.setForeground(new java.awt.Color(255, 173, 109));
        cB_pepperoni_Rucola.setText("Rucola");

        cB_pepperoni_Salami.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_pepperoni_Salami.setForeground(new java.awt.Color(255, 173, 109));
        cB_pepperoni_Salami.setSelected(true);
        cB_pepperoni_Salami.setText("Salami");

        cB_pepperoni_Kaese.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_pepperoni_Kaese.setForeground(new java.awt.Color(255, 173, 109));
        cB_pepperoni_Kaese.setSelected(true);
        cB_pepperoni_Kaese.setText("Käse");

        cB_pepperoni_Zwiebeln.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_pepperoni_Zwiebeln.setForeground(new java.awt.Color(255, 173, 109));
        cB_pepperoni_Zwiebeln.setText("Zwiebeln");

        cB_pepperoni_Tomatensosse.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        cB_pepperoni_Tomatensosse.setForeground(new java.awt.Color(255, 173, 109));
        cB_pepperoni_Tomatensosse.setSelected(true);
        cB_pepperoni_Tomatensosse.setText("Tomatensoße");

        jLabel128.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 22)); // NOI18N
        jLabel128.setForeground(new java.awt.Color(255, 173, 109));
        jLabel128.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel128.setText("<html><p style=\"text-align:center;\">Die Pizza Pepperoni wird mit feinster Pepperoni, Tomaten und Käse belegt und ist eine beliebte Wahl für alle Fleischliebhaber.</p></html>");

        jP_landing_pepperoni_salami_Back.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_pepperoni_salami_Back.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_pepperoni_salami_Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_BackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_BackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_BackMouseExited(evt);
            }
        });

        jLabel129.setBackground(new java.awt.Color(0, 0, 0));
        jLabel129.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel129.setForeground(new java.awt.Color(250, 250, 250));
        jLabel129.setText("Zurück");
        jLabel129.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel129MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel129MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_pepperoni_salami_BackLayout = new javax.swing.GroupLayout(jP_landing_pepperoni_salami_Back);
        jP_landing_pepperoni_salami_Back.setLayout(jP_landing_pepperoni_salami_BackLayout);
        jP_landing_pepperoni_salami_BackLayout.setHorizontalGroup(
            jP_landing_pepperoni_salami_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_pepperoni_salami_BackLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel129)
                .addGap(28, 28, 28))
        );
        jP_landing_pepperoni_salami_BackLayout.setVerticalGroup(
            jP_landing_pepperoni_salami_BackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_pepperoni_salami_BackLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel129)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_pepperoni_salami_toCart.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_pepperoni_salami_toCart.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_pepperoni_salami_toCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_toCartMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_toCartMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_toCartMouseEntered(evt);
            }
        });

        jp_landing_Bestellen_pepperoni_toCart_Label.setBackground(new java.awt.Color(0, 0, 0));
        jp_landing_Bestellen_pepperoni_toCart_Label.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jp_landing_Bestellen_pepperoni_toCart_Label.setForeground(new java.awt.Color(250, 250, 250));
        jp_landing_Bestellen_pepperoni_toCart_Label.setText("In den Warenkorb");
        jp_landing_Bestellen_pepperoni_toCart_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_pepperoni_toCart_LabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jp_landing_Bestellen_pepperoni_toCart_LabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_pepperoni_salami_toCartLayout = new javax.swing.GroupLayout(jP_landing_pepperoni_salami_toCart);
        jP_landing_pepperoni_salami_toCart.setLayout(jP_landing_pepperoni_salami_toCartLayout);
        jP_landing_pepperoni_salami_toCartLayout.setHorizontalGroup(
            jP_landing_pepperoni_salami_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_pepperoni_salami_toCartLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jp_landing_Bestellen_pepperoni_toCart_Label)
                .addGap(28, 28, 28))
        );
        jP_landing_pepperoni_salami_toCartLayout.setVerticalGroup(
            jP_landing_pepperoni_salami_toCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_pepperoni_salami_toCartLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jp_landing_Bestellen_pepperoni_toCart_Label)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jP_landing_pepperoni_salami_Plus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_pepperoni_salami_Plus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_pepperoni_salami_Plus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_PlusMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_PlusMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_PlusMouseEntered(evt);
            }
        });

        jLabel131.setBackground(new java.awt.Color(0, 0, 0));
        jLabel131.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel131.setForeground(new java.awt.Color(250, 250, 250));
        jLabel131.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel131.setText("+");
        jLabel131.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel131MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel131MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel131MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_pepperoni_salami_PlusLayout = new javax.swing.GroupLayout(jP_landing_pepperoni_salami_Plus);
        jP_landing_pepperoni_salami_Plus.setLayout(jP_landing_pepperoni_salami_PlusLayout);
        jP_landing_pepperoni_salami_PlusLayout.setHorizontalGroup(
            jP_landing_pepperoni_salami_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_pepperoni_salami_PlusLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel131, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );
        jP_landing_pepperoni_salami_PlusLayout.setVerticalGroup(
            jP_landing_pepperoni_salami_PlusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_pepperoni_salami_PlusLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel131)
                .addContainerGap())
        );

        jP_landing_pepperoni_salami_Count.setBackground(new java.awt.Color(255, 206, 168));
        jP_landing_pepperoni_salami_Count.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_pepperoni_salami_Count.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_CountMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_CountMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_CountMouseEntered(evt);
            }
        });

        jP_landing_Bestellen_pepperoni_Counter.setBackground(new java.awt.Color(0, 0, 0));
        jP_landing_Bestellen_pepperoni_Counter.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jP_landing_Bestellen_pepperoni_Counter.setForeground(new java.awt.Color(250, 250, 250));
        jP_landing_Bestellen_pepperoni_Counter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jP_landing_Bestellen_pepperoni_Counter.setText("1");
        jP_landing_Bestellen_pepperoni_Counter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_pepperoni_CounterMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Bestellen_pepperoni_CounterMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_pepperoni_salami_CountLayout = new javax.swing.GroupLayout(jP_landing_pepperoni_salami_Count);
        jP_landing_pepperoni_salami_Count.setLayout(jP_landing_pepperoni_salami_CountLayout);
        jP_landing_pepperoni_salami_CountLayout.setHorizontalGroup(
            jP_landing_pepperoni_salami_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_pepperoni_salami_CountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jP_landing_Bestellen_pepperoni_Counter, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );
        jP_landing_pepperoni_salami_CountLayout.setVerticalGroup(
            jP_landing_pepperoni_salami_CountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_pepperoni_salami_CountLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jP_landing_Bestellen_pepperoni_Counter)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jP_landing_pepperoni_salami_Minus.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_pepperoni_salami_Minus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_pepperoni_salami_Minus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_MinusMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_MinusMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_pepperoni_salami_MinusMouseEntered(evt);
            }
        });

        jLabel132.setBackground(new java.awt.Color(0, 0, 0));
        jLabel132.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel132.setForeground(new java.awt.Color(250, 250, 250));
        jLabel132.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel132.setText("-");
        jLabel132.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel132MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel132MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel132MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_pepperoni_salami_MinusLayout = new javax.swing.GroupLayout(jP_landing_pepperoni_salami_Minus);
        jP_landing_pepperoni_salami_Minus.setLayout(jP_landing_pepperoni_salami_MinusLayout);
        jP_landing_pepperoni_salami_MinusLayout.setHorizontalGroup(
            jP_landing_pepperoni_salami_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_pepperoni_salami_MinusLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel132, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );
        jP_landing_pepperoni_salami_MinusLayout.setVerticalGroup(
            jP_landing_pepperoni_salami_MinusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_pepperoni_salami_MinusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel132)
                .addContainerGap())
        );

        jLabel133.setBackground(new java.awt.Color(0, 0, 0));
        jLabel133.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 60)); // NOI18N
        jLabel133.setForeground(new java.awt.Color(250, 130, 49));
        jLabel133.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel133.setText("Pizza Pepperoni");

        javax.swing.GroupLayout jP_landing_Bestellen_pepperoniLayout = new javax.swing.GroupLayout(jP_landing_Bestellen_pepperoni);
        jP_landing_Bestellen_pepperoni.setLayout(jP_landing_Bestellen_pepperoniLayout);
        jP_landing_Bestellen_pepperoniLayout.setHorizontalGroup(
            jP_landing_Bestellen_pepperoniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_pepperoniLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jP_landing_Bestellen_pepperoniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jP_landing_Bestellen_pepperoniLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_pepperoniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cB_pepperoni_Tomatensosse)
                            .addComponent(cB_pepperoni_Kaese)
                            .addComponent(cB_pepperoni_Rucola)
                            .addComponent(cB_pepperoni_Zwiebeln))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jP_landing_Bestellen_pepperoniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_pepperoniLayout.createSequentialGroup()
                                .addComponent(jLabel127)
                                .addGap(18, 18, 18)
                                .addComponent(jP_landing_pepperoni_salami_Minus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_pepperoni_salami_Count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP_landing_pepperoni_salami_Plus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel128, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_pepperoniLayout.createSequentialGroup()
                        .addComponent(jP_landing_pepperoni_salami_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                        .addComponent(jP_landing_pepperoni_salami_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jP_landing_Bestellen_pepperoniLayout.createSequentialGroup()
                        .addGroup(jP_landing_Bestellen_pepperoniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel126)
                            .addComponent(cB_pepperoni_Salami))
                        .addGap(17, 17, 17)
                        .addComponent(jLabel133)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(43, 43, 43))
        );
        jP_landing_Bestellen_pepperoniLayout.setVerticalGroup(
            jP_landing_Bestellen_pepperoniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Bestellen_pepperoniLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jP_landing_Bestellen_pepperoniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_pepperoniLayout.createSequentialGroup()
                        .addComponent(jLabel133, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Bestellen_pepperoniLayout.createSequentialGroup()
                        .addComponent(jLabel126)
                        .addGap(48, 48, 48)))
                .addGroup(jP_landing_Bestellen_pepperoniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jP_landing_Bestellen_pepperoniLayout.createSequentialGroup()
                        .addComponent(cB_pepperoni_Salami)
                        .addGap(19, 19, 19)
                        .addComponent(cB_pepperoni_Zwiebeln)
                        .addGap(18, 18, 18)
                        .addComponent(cB_pepperoni_Rucola)
                        .addGap(18, 18, 18)
                        .addComponent(cB_pepperoni_Kaese)
                        .addGap(18, 18, 18)
                        .addComponent(cB_pepperoni_Tomatensosse))
                    .addComponent(jLabel128, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jP_landing_Bestellen_pepperoniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP_landing_Bestellen_pepperoniLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jP_landing_Bestellen_pepperoniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jP_landing_pepperoni_salami_Count, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_pepperoni_salami_Minus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jP_landing_pepperoni_salami_Plus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jP_landing_Bestellen_pepperoniLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel127, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jP_landing_Bestellen_pepperoniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jP_landing_pepperoni_salami_Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP_landing_pepperoni_salami_toCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );

        jP_landing_Bestellen.add(jP_landing_Bestellen_pepperoni, "card3");

        jP_main.add(jP_landing_Bestellen, "card7");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jP_control, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(776, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(282, 282, 282)
                    .addComponent(jP_main, javax.swing.GroupLayout.PREFERRED_SIZE, 769, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jP_control, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jP_main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateGUI() {
        if (angemeldeterBenutzer != null) {
            aktualisiereBenutzer();
            aktualisiereKasse();
        } else {
            System.err.println("Kein User gefunden.");
        }
    }

    private void jP_adminMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_adminMouseExited
        // TODO addPizza your handling code here:
        if (isLogin)
            jP_admin.setBackground(main);
    }//GEN-LAST:event_jP_adminMouseExited

    private void jP_adminMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_adminMouseEntered
        // TODO addPizza your handling code here:
        if (isLogin)
            jP_admin.setBackground(hover_main);
    }//GEN-LAST:event_jP_adminMouseEntered

    private void jP_adminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_adminMouseClicked
        // TODO addPizza your handling code here:
        if (isLogin) {
            status_Bestellen(false);
            status_Home(false);
            status_Benutzer(false);
            status_Admin(true);
            status_Cart(false);
            aktualisiereAdmin();
        }
    }//GEN-LAST:event_jP_adminMouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        // TODO addPizza your handling code here:
        jP_adminMouseClicked(evt);
    }//GEN-LAST:event_jLabel9MouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        // TODO addPizza your handling code here:
        jP_adminMouseClicked(evt);
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jP_userMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_userMouseExited
        // TODO addPizza your handling code here:
        jP_user.setBackground(main);
    }//GEN-LAST:event_jP_userMouseExited

    private void jP_userMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_userMouseEntered
        // TODO addPizza your handling code here:
        jP_user.setBackground(hover_main);
    }//GEN-LAST:event_jP_userMouseEntered

    private void jP_userMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_userMouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen(false);
        status_Home(false);
        status_Benutzer(true);
        status_Admin(false);
        status_Cart(false);
    }//GEN-LAST:event_jP_userMouseClicked

    private void txt_BenutzerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_BenutzerMouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen(false);
        status_Home(false);
        status_Benutzer(true);
        status_Admin(false);
        status_Cart(false);
    }//GEN-LAST:event_txt_BenutzerMouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen(false);
        status_Home(false);
        status_Benutzer(true);
        status_Admin(false);
        status_Cart(false);
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jP_orderMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_orderMouseExited
        // TODO addPizza your handling code here:
        if (isLogin) {
            jP_order.setBackground(main);
        }
    }//GEN-LAST:event_jP_orderMouseExited

    private void jP_orderMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_orderMouseEntered
        // TODO addPizza your handling code here:
        if (isLogin) {
            jP_order.setBackground(hover_main);
        }
    }//GEN-LAST:event_jP_orderMouseEntered

    private void jP_orderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_orderMouseClicked
        // TODO addPizza your handling code here:
        if (isLogin) {
            status_Bestellen(true);
            status_Home(false);
            status_Benutzer(false);
            status_Admin(false);
            status_Cart(false);
        }
    }//GEN-LAST:event_jP_orderMouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO addPizza your handling code here:
        jP_orderMouseClicked(evt);
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // TODO addPizza your handling code here:
        jP_orderMouseClicked(evt);
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jP_homeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_homeMouseExited
        // TODO addPizza your handling code here:
        jP_home.setBackground(main);
    }//GEN-LAST:event_jP_homeMouseExited

    private void jP_homeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_homeMouseEntered
        // TODO addPizza your handling code here:
        jP_home.setBackground(hover_main);
    }//GEN-LAST:event_jP_homeMouseEntered

    private void jP_homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_homeMouseClicked
        // TODO addPizza your handling code here:        
        status_Bestellen(false);
        status_Home(true);
        status_Benutzer(false);
        status_Admin(false);
        status_Cart(false);
        aktualisiereHome();
    }//GEN-LAST:event_jP_homeMouseClicked

    private void txt_HomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_HomeMouseClicked
        // TODO addPizza your handling code here:
        jP_homeMouseClicked(evt);
    }//GEN-LAST:event_txt_HomeMouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO addPizza your handling code here:
        jP_homeMouseClicked(evt);
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_salami.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_salami(true);
    }//GEN-LAST:event_jLabel18MouseClicked

    private void jP_Bestellen_salamiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_salamiMouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_salami.setBackground(hover_main);
    }//GEN-LAST:event_jP_Bestellen_salamiMouseEntered

    private void jP_Bestellen_salamiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_salamiMouseExited
        // TODO addPizza your handling code here:
        jP_Bestellen_salami.setBackground(standart);
    }//GEN-LAST:event_jP_Bestellen_salamiMouseExited

    private void jLabel18MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_salami.setBackground(hover_main);
    }//GEN-LAST:event_jLabel18MouseEntered

    private void jLabel20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_speciale.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_speciale(true);
    }//GEN-LAST:event_jLabel20MouseClicked

    private void jLabel20MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_speciale.setBackground(hover_main);
    }//GEN-LAST:event_jLabel20MouseEntered

    private void jP_Bestellen_specialeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_specialeMouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_speciale.setBackground(hover_main);
    }//GEN-LAST:event_jP_Bestellen_specialeMouseEntered

    private void jP_Bestellen_specialeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_specialeMouseExited
        // TODO addPizza your handling code here:
        jP_Bestellen_speciale.setBackground(standart);
    }//GEN-LAST:event_jP_Bestellen_specialeMouseExited

    private void jLabel22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel22MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_speciale.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_diavolo(true);
    }//GEN-LAST:event_jLabel22MouseClicked

    private void jLabel22MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel22MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_diavolo.setBackground(hover_main);
    }//GEN-LAST:event_jLabel22MouseEntered

    private void jP_Bestellen_diavoloMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_diavoloMouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_diavolo.setBackground(hover_main);
    }//GEN-LAST:event_jP_Bestellen_diavoloMouseEntered

    private void jP_Bestellen_diavoloMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_diavoloMouseExited
        // TODO addPizza your handling code here:
        jP_Bestellen_diavolo.setBackground(standart);
    }//GEN-LAST:event_jP_Bestellen_diavoloMouseExited

    private void jLabel24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel24MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_stagioni.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_stagioni(true);
    }//GEN-LAST:event_jLabel24MouseClicked

    private void jLabel24MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel24MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_stagioni.setBackground(hover_main);
    }//GEN-LAST:event_jLabel24MouseEntered

    private void jP_Bestellen_stagioniMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_stagioniMouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_stagioni.setBackground(hover_main);
    }//GEN-LAST:event_jP_Bestellen_stagioniMouseEntered

    private void jP_Bestellen_stagioniMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_stagioniMouseExited
        // TODO addPizza your handling code here:
        jP_Bestellen_stagioni.setBackground(standart);
    }//GEN-LAST:event_jP_Bestellen_stagioniMouseExited

    private void jLabel26MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel26MouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel26MouseClicked

    private void jLabel26MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel26MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_rucula.setBackground(hover_main);
    }//GEN-LAST:event_jLabel26MouseEntered

    private void jP_Bestellen_ruculaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_ruculaMouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_rucula.setBackground(hover_main);
    }//GEN-LAST:event_jP_Bestellen_ruculaMouseEntered

    private void jP_Bestellen_ruculaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_ruculaMouseExited
        // TODO addPizza your handling code here:
        jP_Bestellen_rucula.setBackground(standart);
    }//GEN-LAST:event_jP_Bestellen_ruculaMouseExited

    private void jLabel28MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel28MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_caprese.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_caprese(true);
    }//GEN-LAST:event_jLabel28MouseClicked

    private void jLabel28MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel28MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_caprese.setBackground(hover_main);
    }//GEN-LAST:event_jLabel28MouseEntered

    private void jP_Bestellen_capreseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_capreseMouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_caprese.setBackground(hover_main);
    }//GEN-LAST:event_jP_Bestellen_capreseMouseEntered

    private void jP_Bestellen_capreseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_capreseMouseExited
        // TODO addPizza your handling code here:
        jP_Bestellen_caprese.setBackground(standart);
    }//GEN-LAST:event_jP_Bestellen_capreseMouseExited

    private void jLabel30MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel30MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_funghi.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_funghi(true);
    }//GEN-LAST:event_jLabel30MouseClicked

    private void jLabel30MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel30MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_funghi.setBackground(hover_main);
    }//GEN-LAST:event_jLabel30MouseEntered

    private void jP_Bestellen_funghiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_funghiMouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_funghi.setBackground(hover_main);
    }//GEN-LAST:event_jP_Bestellen_funghiMouseEntered

    private void jP_Bestellen_funghiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_funghiMouseExited
        // TODO addPizza your handling code here:
        jP_Bestellen_funghi.setBackground(standart);
    }//GEN-LAST:event_jP_Bestellen_funghiMouseExited

    private void jLabel32MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel32MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_margherita.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_margherita(true);
    }//GEN-LAST:event_jLabel32MouseClicked

    private void jLabel32MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel32MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_margherita.setBackground(hover_main);
    }//GEN-LAST:event_jLabel32MouseEntered

    private void jP_Bestellen_margheritaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_margheritaMouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_margherita.setBackground(hover_main);
    }//GEN-LAST:event_jP_Bestellen_margheritaMouseEntered

    private void jP_Bestellen_margheritaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_margheritaMouseExited
        // TODO addPizza your handling code here:
        jP_Bestellen_margherita.setBackground(standart);
    }//GEN-LAST:event_jP_Bestellen_margheritaMouseExited

    private void jLabel34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseClicked
        // TODO addPizza your handling code here
        jP_Bestellen_pepperoni.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_pepperoni(true);
    }//GEN-LAST:event_jLabel34MouseClicked

    private void jLabel34MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_pepperoni.setBackground(hover_main);
    }//GEN-LAST:event_jLabel34MouseEntered

    private void jP_Bestellen_pepperoniMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_pepperoniMouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_pepperoni.setBackground(hover_main);
    }//GEN-LAST:event_jP_Bestellen_pepperoniMouseEntered

    private void jP_Bestellen_pepperoniMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_pepperoniMouseExited
        // TODO addPizza your handling code here:
        jP_Bestellen_pepperoni.setBackground(standart);
    }//GEN-LAST:event_jP_Bestellen_pepperoniMouseExited

    private void jLabel37MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel37MouseClicked
        // TODO addPizza your handling code here:
        jP_cartMouseClicked(evt);
    }//GEN-LAST:event_jLabel37MouseClicked

    private void jLabel38MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel38MouseClicked
        // TODO addPizza your handling code here:
        jP_cartMouseClicked(evt);
    }//GEN-LAST:event_jLabel38MouseClicked

    private void jP_cartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_cartMouseClicked
        // TODO addPizza your handling code here:
        if (isLogin) {
            status_Bestellen(false);
            status_Home(false);
            status_Benutzer(false);
            status_Admin(false);
            status_Cart(true);
            for (int i = 0; i < warenkorb.getWarenkorbItems().size(); i++) {
                aktualisiereWarenkorb(i);
            }
            aktualisiereKasse();
        }

    }//GEN-LAST:event_jP_cartMouseClicked

    private void jP_cartMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_cartMouseEntered
        // TODO addPizza your handling code here:
        if (isLogin)
            jP_cart.setBackground(hover_main);
    }//GEN-LAST:event_jP_cartMouseEntered

    private void jP_cartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_cartMouseExited
        // TODO addPizza your handling code here:
        if (isLogin)
            jP_cart.setBackground(main);
    }//GEN-LAST:event_jP_cartMouseExited

    private void jLabel38MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel38MouseEntered
        // TODO addPizza your handling code here:
        if (isLogin)
            jP_cart.setBackground(hover_main);
    }//GEN-LAST:event_jLabel38MouseEntered

    private void jLabel37MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel37MouseEntered
        // TODO addPizza your handling code here:
        if (isLogin)
            jP_cart.setBackground(hover_main);
    }//GEN-LAST:event_jLabel37MouseEntered

    private void jP_landing_Bestellen_salami_BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_BackMouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_home(true);
        status_Bestellen_salami(false);
    }//GEN-LAST:event_jP_landing_Bestellen_salami_BackMouseClicked

    private void jP_landing_Bestellen_salami_BackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_BackMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_Back.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_salami_BackMouseEntered

    private void jP_landing_Bestellen_salami_BackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_BackMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_Back.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_salami_BackMouseExited

    private void jp_landing_Bestellen_salami_toCart_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_salami_toCart_LabelMouseClicked
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_toCartMouseClicked(evt);
    }//GEN-LAST:event_jp_landing_Bestellen_salami_toCart_LabelMouseClicked

    private void jP_landing_Bestellen_salami_toCartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_toCartMouseClicked
        // TODO addPizza your handling code here:
        if (!warenkorb.isVoll()) {
            boolean salami = cB_salami_Salami.isSelected();
            boolean zwiebeln = cB_salami_Zwiebeln.isSelected();
            boolean rucola = cB_salami_Rucola.isSelected();
            boolean kaese = cB_salami_Kaese.isSelected();
            boolean tomatensoße = cB_salami_Tomatensosse.isSelected();

            int anzahl = Integer.parseInt(jP_landing_Bestellen_salami_Counter.getText());
            String typ = "Salami";

            Pizza salamiPizza = new Pizza(typ, anzahl, tomatensoße, kaese, rucola, salami, zwiebeln);
            JOptionPane.showMessageDialog(rootPane, "Preis der Pizza: " + kovertiereZuEuro(salamiPizza.berechnePizzaPreis()) + "€");
            int index = warenkorb.addPizza(salamiPizza);
            aktualisiereWarenkorb(index);

            status_Bestellen_salami(false);
            status_Bestellen_home(true);

            cB_salami_Salami.setSelected(true);
            cB_salami_Zwiebeln.setSelected(false);
            cB_salami_Rucola.setSelected(false);
            cB_salami_Kaese.setSelected(true);
            cB_salami_Tomatensosse.setSelected(true);
            jP_landing_Bestellen_salami_Counter.setText(Integer.toString(orderSalami = 1));
        } else {
            JOptionPane.showMessageDialog(rootPane, "Sie haben nur 4 Pizzaslots im Warenkorb zur Verfügung.");
            System.out.println("Maximale Warenkorbgröße erreicht.");
        }
    }//GEN-LAST:event_jP_landing_Bestellen_salami_toCartMouseClicked

    private void jP_landing_Bestellen_salami_toCartMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_toCartMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_salami_toCartMouseEntered

    private void jP_landing_Bestellen_salami_toCartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_toCartMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_toCart.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_salami_toCartMouseExited

    private void jLabel45MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel45MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_Back.setBackground(hover_main);
    }//GEN-LAST:event_jLabel45MouseEntered

    private void jLabel45MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel45MouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_home(true);
        status_Bestellen_salami(false);
    }//GEN-LAST:event_jLabel45MouseClicked

    private void jP_Bestellen_salamiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_salamiMouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_salami.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_salami(true);
    }//GEN-LAST:event_jP_Bestellen_salamiMouseClicked

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_salami.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_salami(true);
    }//GEN-LAST:event_jLabel16MouseClicked

    private void jLabel16MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_salami.setBackground(hover_main);
    }//GEN-LAST:event_jLabel16MouseEntered

    private void jp_landing_Bestellen_salami_toCart_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_salami_toCart_LabelMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jp_landing_Bestellen_salami_toCart_LabelMouseEntered

    private void jLabel47MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel47MouseClicked
        // TODO addPizza your handling code here:
        orderSalami++;
        jP_landing_Bestellen_salami_Counter.setText(Integer.toString(orderSalami));
    }//GEN-LAST:event_jLabel47MouseClicked

    private void jLabel47MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel47MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel47MouseEntered

    private void jP_landing_Bestellen_salami_PlusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_PlusMouseClicked
        // TODO addPizza your handling code here:
        orderSalami++;
        jP_landing_Bestellen_salami_Counter.setText(Integer.toString(orderSalami));
    }//GEN-LAST:event_jP_landing_Bestellen_salami_PlusMouseClicked

    private void jP_landing_Bestellen_salami_PlusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_PlusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_salami_PlusMouseEntered

    private void jP_landing_Bestellen_salami_PlusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_PlusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_Plus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_salami_PlusMouseExited

    private void jP_landing_Bestellen_salami_CounterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_CounterMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_salami_CounterMouseClicked

    private void jP_landing_Bestellen_salami_CounterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_CounterMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_salami_CounterMouseEntered

    private void jP_landing_Bestellen_salami_CountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_CountMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_salami_CountMouseClicked

    private void jP_landing_Bestellen_salami_CountMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_CountMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_salami_CountMouseEntered

    private void jP_landing_Bestellen_salami_CountMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_CountMouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_salami_CountMouseExited

    private void jLabel49MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel49MouseClicked
        // TODO addPizza your handling code here:
        if (orderSalami > 1) {
            orderSalami--;
            jP_landing_Bestellen_salami_Counter.setText(Integer.toString(orderSalami));
        }
    }//GEN-LAST:event_jLabel49MouseClicked

    private void jLabel49MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel49MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel49MouseEntered

    private void jP_landing_Bestellen_salami_MinusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_MinusMouseClicked
        // TODO addPizza your handling code here:
        if (orderSalami > 1) {
            orderSalami--;
            jP_landing_Bestellen_salami_Counter.setText(Integer.toString(orderSalami));
        }
    }//GEN-LAST:event_jP_landing_Bestellen_salami_MinusMouseClicked

    private void jP_landing_Bestellen_salami_MinusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_MinusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_salami_MinusMouseEntered

    private void jP_landing_Bestellen_salami_MinusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_salami_MinusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_Minus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_salami_MinusMouseExited

    private void jLabel49MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel49MouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_Minus.setBackground(main);
    }//GEN-LAST:event_jLabel49MouseExited

    private void jLabel47MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel47MouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_salami_Plus.setBackground(main);
    }//GEN-LAST:event_jLabel47MouseExited

    private void jLabel52MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel52MouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_home(true);
        status_Bestellen_speciale(false);
    }//GEN-LAST:event_jLabel52MouseClicked

    private void jLabel52MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel52MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_speciale_Back.setBackground(hover_main);
    }//GEN-LAST:event_jLabel52MouseEntered

    private void jP_landing_Bestellen_speciale_BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_BackMouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_home(true);
        status_Bestellen_speciale(false);
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_BackMouseClicked

    private void jP_landing_Bestellen_speciale_BackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_BackMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_speciale_Back.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_BackMouseEntered

    private void jP_landing_Bestellen_speciale_BackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_BackMouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_BackMouseExited

    private void jp_landing_Bestellen_speciale_toCart_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_speciale_toCart_LabelMouseClicked
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_speciale_toCartMouseClicked(evt);
    }//GEN-LAST:event_jp_landing_Bestellen_speciale_toCart_LabelMouseClicked

    private void jp_landing_Bestellen_speciale_toCart_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_speciale_toCart_LabelMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_speciale_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jp_landing_Bestellen_speciale_toCart_LabelMouseEntered

    private void jP_landing_Bestellen_speciale_toCartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_toCartMouseClicked
        // TODO addPizza your handling code here:
        if (!warenkorb.isVoll()) {
            boolean salami = cB_speciale_Salami.isSelected();
            boolean zwiebeln = cB_speciale_Zwiebeln.isSelected();
            boolean rucola = cB_speciale_Rucola.isSelected();
            boolean kaese = cB_speciale_Kaese.isSelected();
            boolean tomatensoße = cB_speciale_Tomatensosse.isSelected();

            int anzahl = Integer.parseInt(jP_landing_Bestellen_speciale_Counter.getText());
            String typ = "Speciale";

            Pizza specialePizza = new Pizza(typ, anzahl, tomatensoße, kaese, rucola, salami, zwiebeln);
            JOptionPane.showMessageDialog(rootPane, "Preis der Pizza: " + kovertiereZuEuro(specialePizza.berechnePizzaPreis()) + "€");
            int index = warenkorb.addPizza(specialePizza);
            JOptionPane.showMessageDialog(rootPane, "Preis der Pizza: " + kovertiereZuEuro(specialePizza.berechnePizzaPreis()) + "€");
            aktualisiereWarenkorb(index);

            status_Bestellen_speciale(false);
            status_Bestellen_home(true);

            cB_speciale_Salami.setSelected(true);
            cB_speciale_Zwiebeln.setSelected(true);
            cB_speciale_Rucola.setSelected(false);
            cB_speciale_Kaese.setSelected(true);
            cB_speciale_Tomatensosse.setSelected(true);
            jP_landing_Bestellen_speciale_Counter.setText(Integer.toString(orderSpeciale = 1));
        } else {
            JOptionPane.showMessageDialog(rootPane, "Sie haben nur 4 Pizzaslots im Warenkorb zur Verfügung.");
            System.out.println("Maximale Warenkorbgröße erreicht.");
        }
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_toCartMouseClicked

    private void jP_landing_Bestellen_speciale_toCartMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_toCartMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_speciale_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_toCartMouseEntered

    private void jP_landing_Bestellen_speciale_toCartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_toCartMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_speciale_toCart.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_toCartMouseExited

    private void jLabel54MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel54MouseClicked
        // TODO addPizza your handling code here:
        orderSpeciale++;
        jP_landing_Bestellen_speciale_Counter.setText(Integer.toString(orderSpeciale));
    }//GEN-LAST:event_jLabel54MouseClicked

    private void jLabel54MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel54MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_speciale_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel54MouseEntered

    private void jLabel54MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel54MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel54MouseExited

    private void jP_landing_Bestellen_speciale_PlusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_PlusMouseClicked
        // TODO addPizza your handling code here:
        orderSpeciale++;
        jP_landing_Bestellen_speciale_Counter.setText(Integer.toString(orderSpeciale));
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_PlusMouseClicked

    private void jP_landing_Bestellen_speciale_PlusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_PlusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_speciale_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_PlusMouseEntered

    private void jP_landing_Bestellen_speciale_PlusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_PlusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_speciale_Plus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_PlusMouseExited

    private void jP_landing_Bestellen_speciale_CounterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_CounterMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_CounterMouseClicked

    private void jP_landing_Bestellen_speciale_CounterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_CounterMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_CounterMouseEntered

    private void jP_landing_Bestellen_speciale_CountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_CountMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_CountMouseClicked

    private void jP_landing_Bestellen_speciale_CountMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_CountMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_CountMouseEntered

    private void jP_landing_Bestellen_speciale_CountMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_CountMouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_CountMouseExited

    private void jLabel55MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel55MouseClicked
        // TODO addPizza your handling code here:
        if (orderSpeciale > 1) {
            orderSpeciale--;
            jP_landing_Bestellen_speciale_Counter.setText(Integer.toString(orderSpeciale));
        }
    }//GEN-LAST:event_jLabel55MouseClicked

    private void jLabel55MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel55MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_speciale_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel55MouseEntered

    private void jLabel55MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel55MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel55MouseExited

    private void jP_landing_Bestellen_speciale_MinusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_MinusMouseClicked
        // TODO addPizza your handling code here:
        if (orderSpeciale > 1) {
            orderSpeciale--;
            jP_landing_Bestellen_speciale_Counter.setText(Integer.toString(orderSpeciale));
        }
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_MinusMouseClicked

    private void jP_landing_Bestellen_speciale_MinusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_MinusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_speciale_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_MinusMouseEntered

    private void jP_landing_Bestellen_speciale_MinusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_speciale_MinusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_speciale_Minus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_speciale_MinusMouseExited

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_speciale.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_speciale(true);
    }//GEN-LAST:event_jLabel19MouseClicked

    private void jP_Bestellen_specialeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_specialeMouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_speciale.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_speciale(true);
    }//GEN-LAST:event_jP_Bestellen_specialeMouseClicked

    private void jLabel19MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_speciale.setBackground(hover_main);
    }//GEN-LAST:event_jLabel19MouseEntered

    private void jLabel60MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel60MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_diavolo.setBackground(standart);
        status_Bestellen_home(true);
        status_Bestellen_diavolo(false);
    }//GEN-LAST:event_jLabel60MouseClicked

    private void jLabel60MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel60MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_diavolo_Back.setBackground(hover_main);
    }//GEN-LAST:event_jLabel60MouseEntered

    private void jP_landing_Bestellen_diavolo_BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_BackMouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_diavolo.setBackground(standart);
        status_Bestellen_home(true);
        status_Bestellen_diavolo(false);
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_BackMouseClicked

    private void jP_landing_Bestellen_diavolo_BackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_BackMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_diavolo_Back.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_BackMouseEntered

    private void jP_landing_Bestellen_diavolo_BackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_BackMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_diavolo_Back.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_BackMouseExited

    private void jp_landing_Bestellen_diavolo_toCart_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_diavolo_toCart_LabelMouseClicked
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_diavolo_toCartMouseClicked(evt);
    }//GEN-LAST:event_jp_landing_Bestellen_diavolo_toCart_LabelMouseClicked

    private void jp_landing_Bestellen_diavolo_toCart_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_diavolo_toCart_LabelMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_diavolo_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jp_landing_Bestellen_diavolo_toCart_LabelMouseEntered

    private void jP_landing_Bestellen_diavolo_toCartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_toCartMouseClicked
        // TODO addPizza your handling code here:
        if (!warenkorb.isVoll()) {
            boolean salami = cB_diavolo_Salami.isSelected();
            boolean zwiebeln = cB_diavolo_Zwiebeln.isSelected();
            boolean rucola = cB_diavolo_Rucola.isSelected();
            boolean kaese = cB_diavolo_Kaese.isSelected();
            boolean tomatensoße = cB_diavolo_Tomatensosse.isSelected();

            int anzahl = Integer.parseInt(jP_landing_Bestellen_diavolo_Counter.getText());
            String typ = "Diavolo";

            Pizza diavoloPizza = new Pizza(typ, anzahl, tomatensoße, kaese, rucola, salami, zwiebeln);
            JOptionPane.showMessageDialog(rootPane, "Preis der Pizza: " + kovertiereZuEuro(diavoloPizza.berechnePizzaPreis()) + "€");
            int index = warenkorb.addPizza(diavoloPizza);
            aktualisiereWarenkorb(index);

            status_Bestellen_diavolo(false);
            status_Bestellen_home(true);
            jP_Bestellen_diavolo.setBackground(standart);

            cB_diavolo_Salami.setSelected(true);
            cB_diavolo_Zwiebeln.setSelected(false);
            cB_diavolo_Rucola.setSelected(false);
            cB_diavolo_Kaese.setSelected(true);
            cB_diavolo_Tomatensosse.setSelected(true);
            jP_landing_Bestellen_diavolo_Counter.setText(Integer.toString(orderDiavolo = 1));
        } else {
            JOptionPane.showMessageDialog(rootPane, "Sie haben nur 4 Pizzaslots im Warenkorb zur Verfügung.");
            System.out.println("Maximale Warenkorbgröße erreicht.");
        }

    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_toCartMouseClicked

    private void jP_landing_Bestellen_diavolo_toCartMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_toCartMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_diavolo_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_toCartMouseEntered

    private void jP_landing_Bestellen_diavolo_toCartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_toCartMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_diavolo_toCart.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_toCartMouseExited

    private void jLabel62MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel62MouseClicked
        // TODO addPizza your handling code here:
        orderDiavolo++;
        jP_landing_Bestellen_diavolo_Counter.setText(Integer.toString(orderDiavolo));
    }//GEN-LAST:event_jLabel62MouseClicked

    private void jLabel62MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel62MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_diavolo_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel62MouseEntered

    private void jLabel62MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel62MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel62MouseExited

    private void jP_landing_Bestellen_diavolo_PlusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_PlusMouseClicked
        // TODO addPizza your handling code here:
        orderDiavolo++;
        jP_landing_Bestellen_diavolo_Counter.setText(Integer.toString(orderDiavolo));
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_PlusMouseClicked

    private void jP_landing_Bestellen_diavolo_PlusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_PlusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_diavolo_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_PlusMouseEntered

    private void jP_landing_Bestellen_diavolo_PlusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_PlusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_diavolo_Plus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_PlusMouseExited

    private void jP_landing_Bestellen_diavolo_CounterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_CounterMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_CounterMouseClicked

    private void jP_landing_Bestellen_diavolo_CounterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_CounterMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_CounterMouseEntered

    private void jP_landing_Bestellen_diavolo_CountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_CountMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_CountMouseClicked

    private void jP_landing_Bestellen_diavolo_CountMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_CountMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_CountMouseEntered

    private void jP_landing_Bestellen_diavolo_CountMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_CountMouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_CountMouseExited

    private void jLabel63MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel63MouseClicked
        // TODO addPizza your handling code here:
        if (orderDiavolo > 1) {
            orderDiavolo--;
            jP_landing_Bestellen_diavolo_Counter.setText(Integer.toString(orderDiavolo));
        }
    }//GEN-LAST:event_jLabel63MouseClicked

    private void jLabel63MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel63MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_diavolo_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel63MouseEntered

    private void jLabel63MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel63MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel63MouseExited

    private void jP_landing_Bestellen_diavolo_MinusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_MinusMouseClicked
        // TODO addPizza your handling code here:
        if (orderDiavolo > 1) {
            orderDiavolo--;
            jP_landing_Bestellen_diavolo_Counter.setText(Integer.toString(orderDiavolo));
        }
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_MinusMouseClicked

    private void jP_landing_Bestellen_diavolo_MinusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_MinusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_diavolo_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_MinusMouseEntered

    private void jP_landing_Bestellen_diavolo_MinusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_diavolo_MinusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_diavolo_Minus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_diavolo_MinusMouseExited

    private void jLabel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_speciale.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_diavolo(true);
    }//GEN-LAST:event_jLabel21MouseClicked

    private void jP_Bestellen_diavoloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_diavoloMouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_speciale.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_diavolo(true);
    }//GEN-LAST:event_jP_Bestellen_diavoloMouseClicked

    private void jLabel21MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_diavolo.setBackground(hover_main);
    }//GEN-LAST:event_jLabel21MouseEntered

    private void jLabel74MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel74MouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_stagioni(false);
        status_Bestellen_home(true);
    }//GEN-LAST:event_jLabel74MouseClicked

    private void jLabel74MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel74MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_Back.setBackground(hover_main);
    }//GEN-LAST:event_jLabel74MouseEntered

    private void jP_landing_Bestellen_stagioni_BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_BackMouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_stagioni(false);
        status_Bestellen_home(true);
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_BackMouseClicked

    private void jP_landing_Bestellen_stagioni_BackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_BackMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_Back.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_BackMouseEntered

    private void jP_landing_Bestellen_stagioni_BackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_BackMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_Back.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_BackMouseExited

    private void jp_landing_Bestellen_stagioni_toCart_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_stagioni_toCart_LabelMouseClicked
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_toCartMouseClicked(evt);
    }//GEN-LAST:event_jp_landing_Bestellen_stagioni_toCart_LabelMouseClicked

    private void jp_landing_Bestellen_stagioni_toCart_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_stagioni_toCart_LabelMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jp_landing_Bestellen_stagioni_toCart_LabelMouseEntered

    private void jP_landing_Bestellen_stagioni_toCartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_toCartMouseClicked
        // TODO addPizza your handling code here:
        if (!warenkorb.isVoll()) {
            boolean salami = cB_stagioni_Salami.isSelected();
            boolean zwiebeln = cB_stagioni_Zwiebeln.isSelected();
            boolean rucola = cB_stagioni_Rucola.isSelected();
            boolean kaese = cB_stagioni_Kaese.isSelected();
            boolean tomatensoße = cB_stagioni_Tomatensosse.isSelected();

            int anzahl = Integer.parseInt(jP_landing_Bestellen_stagioni_Counter.getText());
            String typ = "Stagioni";

            Pizza stagioniPizza = new Pizza(typ, anzahl, tomatensoße, kaese, rucola, salami, zwiebeln);
            JOptionPane.showMessageDialog(rootPane, "Preis der Pizza: " + kovertiereZuEuro(stagioniPizza.berechnePizzaPreis()) + "€");
            int index = warenkorb.addPizza(stagioniPizza);
            aktualisiereWarenkorb(index);

            status_Bestellen_stagioni(false);
            status_Bestellen_home(true);

            cB_stagioni_Salami.setSelected(false);
            cB_stagioni_Zwiebeln.setSelected(true);
            cB_stagioni_Rucola.setSelected(false);
            cB_stagioni_Kaese.setSelected(true);
            cB_stagioni_Tomatensosse.setSelected(true);
            jP_landing_Bestellen_stagioni_Counter.setText(Integer.toString(orderStagioni = 1));
        } else {
            JOptionPane.showMessageDialog(rootPane, "Sie haben nur 4 Pizzaslots im Warenkorb zur Verfügung.");
            System.out.println("Maximale Warenkorbgröße erreicht.");
        }
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_toCartMouseClicked

    private void jP_landing_Bestellen_stagioni_toCartMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_toCartMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_toCartMouseEntered

    private void jP_landing_Bestellen_stagioni_toCartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_toCartMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_toCart.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_toCartMouseExited

    private void jLabel76MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel76MouseClicked
        // TODO addPizza your handling code here:
        orderStagioni++;
        jP_landing_Bestellen_stagioni_Counter.setText(Integer.toString(orderStagioni));
    }//GEN-LAST:event_jLabel76MouseClicked

    private void jLabel76MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel76MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel76MouseEntered

    private void jLabel76MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel76MouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_Plus.setBackground(main);
    }//GEN-LAST:event_jLabel76MouseExited

    private void jP_landing_Bestellen_stagioni_PlusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_PlusMouseClicked
        // TODO addPizza your handling code here:
        orderStagioni++;
        jP_landing_Bestellen_stagioni_Counter.setText(Integer.toString(orderStagioni));
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_PlusMouseClicked

    private void jP_landing_Bestellen_stagioni_PlusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_PlusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_PlusMouseEntered

    private void jP_landing_Bestellen_stagioni_PlusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_PlusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_Plus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_PlusMouseExited

    private void jP_landing_Bestellen_stagioni_CounterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_CounterMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_CounterMouseClicked

    private void jP_landing_Bestellen_stagioni_CounterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_CounterMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_CounterMouseEntered

    private void jP_landing_Bestellen_stagioni_CountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_CountMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_CountMouseClicked

    private void jP_landing_Bestellen_stagioni_CountMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_CountMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_CountMouseEntered

    private void jP_landing_Bestellen_stagioni_CountMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_CountMouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_CountMouseExited

    private void jLabel77MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel77MouseClicked
        // TODO addPizza your handling code here:
        if (orderStagioni > 1) {
            orderStagioni--;
            jP_landing_Bestellen_stagioni_Counter.setText(Integer.toString(orderStagioni));
        }
    }//GEN-LAST:event_jLabel77MouseClicked

    private void jLabel77MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel77MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel77MouseEntered

    private void jLabel77MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel77MouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_Minus.setBackground(main);
    }//GEN-LAST:event_jLabel77MouseExited

    private void jP_landing_Bestellen_stagioni_MinusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_MinusMouseClicked
        // TODO addPizza your handling code here:
        if (orderStagioni > 1) {
            orderStagioni--;
            jP_landing_Bestellen_stagioni_Counter.setText(Integer.toString(orderStagioni));
        }
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_MinusMouseClicked

    private void jP_landing_Bestellen_stagioni_MinusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_MinusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_MinusMouseEntered

    private void jP_landing_Bestellen_stagioni_MinusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_stagioni_MinusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_stagioni_Minus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_stagioni_MinusMouseExited

    private void jP_Bestellen_stagioniMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_stagioniMouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_stagioni.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_stagioni(true);
    }//GEN-LAST:event_jP_Bestellen_stagioniMouseClicked

    private void jLabel23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_stagioni.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_stagioni(true);
    }//GEN-LAST:event_jLabel23MouseClicked

    private void jLabel23MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_stagioni.setBackground(hover_main);
    }//GEN-LAST:event_jLabel23MouseEntered

    private void jP_user_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_user_btnMouseExited
        // TODO addPizza your handling code here:
        jP_user_btn.setBackground(main);
    }//GEN-LAST:event_jP_user_btnMouseExited

    private void jP_user_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_user_btnMouseEntered
        // TODO addPizza your handling code here:
        jP_user_btn.setBackground(hover_main);
    }//GEN-LAST:event_jP_user_btnMouseEntered

    private void jP_user_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_user_btnMouseClicked
        // TODO addPizza your handling code here:
        try {
            String vorname = txt_user_vorname.getText();
            String nachname = txt_user_nachname.getText();
            String straße = txt_user_straße.getText();
            int hausnummer = Integer.parseInt(txt_user_hausnummer.getText());
            int plz = Integer.parseInt(txt_user_plz.getText());
            String ort = txt_user_ort.getText();
            String email = txt_user_email.getText();
            String pwd = String.valueOf(this.txt_user_passwort.getPassword());

            if (db.getBenutzer(email) == null) {
                // Benutzer registrieren
                if (email.contains("@")) {
                    Adresse adresse1 = new Adresse(straße, hausnummer, plz, ort);
                    Passwort passwort1 = new Passwort(pwd, true);
                    angemeldeterBenutzer = new Benutzer(vorname, nachname, email, passwort1, adresse1, db, true);

                    status_login(true);

                    updateGUI();
                    aktualisiereHome();
                    status_Bestellen(false);
                    status_Home(true);
                    status_Benutzer(false);
                    status_Admin(false);
                    status_Cart(false);

                    Tutorial tutorial = new Tutorial();
                    tutorial.setVisible(true);
                    tutorial.setEnabled(true);
                } else {
                    throw new NumberFormatException();
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Emailadresse bereits in Verwendung.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Fehler bei einer lokalen Dateiabfrage.");
            System.err.println(ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Fehler bei einer Datenbankabfrage.");
            System.err.println(ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(rootPane, "Bitte geben Sie richtige Werte ein und füllen Sie alle Felder aus.");
            System.err.println(ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(rootPane, "Interner Fehler.");
            System.err.println(ex.getMessage());
        }
    }//GEN-LAST:event_jP_user_btnMouseClicked

    private void jP_landing_Cart_pizza_Name2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Name2MouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Cart_pizza_Name2MouseClicked

    private void jP_landing_Cart_pizza_Bearbeiten2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Bearbeiten2MouseClicked
        // TODO addPizza your handling code here:
        if (warenkorb.getWarenkorbItems().get(1).getTyp().equals("noch frei")) {
            JOptionPane.showMessageDialog(rootPane, "Fügen Sie erst eine Pizza zu diesem Slot hinzu.");
        } else {
            aktualisiereCartBearbeiten(1);
            satuts_Cart_bearbeiten(true);
            status_Cart_main(false);
        }
    }//GEN-LAST:event_jP_landing_Cart_pizza_Bearbeiten2MouseClicked

    private void jLabel68MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel68MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Cart_pizza_zurKasse.setBackground(hover_main);
    }//GEN-LAST:event_jLabel68MouseEntered

    private void jP_landing_Cart_pizza_zurKasseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_zurKasseMouseClicked
        // TODO addPizza your handling code here:
        boolean leer = true;
        for (int i = 0; i < warenkorb.getWarenkorbItems().size(); i++) {
            if (!warenkorb.getWarenkorbItems().get(i).getTyp().equals("noch frei")) {
                leer = false;
            }
        }
        if (!leer) {
            status_Cart_main(false);
            status_Cart_kasse(true);
            warenkorbGesamtBetrag = warenkorbGesamtBetrag - pruefeCoupon(txt_landing_kasse_coupon.getText());
            kassenGesamtBetrag = warenkorbGesamtBetrag;
            aktualisiereKasse();
        } else {
            JOptionPane.showMessageDialog(rootPane, "Ihr Warenkorb ist leer.");
        }


    }//GEN-LAST:event_jP_landing_Cart_pizza_zurKasseMouseClicked

    private void jP_landing_Cart_pizza_zurKasseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_zurKasseMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Cart_pizza_zurKasse.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Cart_pizza_zurKasseMouseEntered

    private void jP_landing_Cart_pizza_zurKasseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_zurKasseMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Cart_pizza_zurKasse.setBackground(main);
    }//GEN-LAST:event_jP_landing_Cart_pizza_zurKasseMouseExited

    private void jLabel68MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel68MouseClicked
        // TODO addPizza your handling code here:
        jP_landing_Cart_pizza_zurKasseMouseClicked(evt);
    }//GEN-LAST:event_jLabel68MouseClicked

    private void jP_landing_Cart_kasse_OrderMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_kasse_OrderMouseExited
        // TODO addPizza your handling code here:
        if (jP_landing_Cart_kasse_Order_enable == true) {
            jP_landing_Cart_kasse_Order.setBackground(main);
        }
    }//GEN-LAST:event_jP_landing_Cart_kasse_OrderMouseExited

    private void jP_landing_Cart_kasse_OrderMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_kasse_OrderMouseEntered
        // TODO addPizza your handling code here:
        if (jP_landing_Cart_kasse_Order_enable == true) {
            jP_landing_Cart_kasse_Order.setBackground(hover_main);
        }
    }//GEN-LAST:event_jP_landing_Cart_kasse_OrderMouseEntered

    private void jLabel70MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel70MouseEntered
        // TODO addPizza your handling code here
        if (jP_landing_Cart_kasse_Order_enable == true) {
            jP_landing_Cart_kasse_Order.setBackground(hover_main);
        }
    }//GEN-LAST:event_jLabel70MouseEntered

    private void jP_landing_Cart_kasse_BackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_kasse_BackMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Cart_kasse_Back.setBackground(main);
    }//GEN-LAST:event_jP_landing_Cart_kasse_BackMouseExited

    private void jP_landing_Cart_kasse_BackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_kasse_BackMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Cart_kasse_Back.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Cart_kasse_BackMouseEntered

    private void jP_landing_Cart_kasse_BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_kasse_BackMouseClicked
        // TODO addPizza your handling code here:
        status_Cart_main(true);
        status_Cart_kasse(false);
        txt_landing_kasse_coupon.setText("");
        kassenGesamtBetrag = warenkorbGesamtBetrag;
    }//GEN-LAST:event_jP_landing_Cart_kasse_BackMouseClicked

    private void jLabel69MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel69MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Cart_kasse_Back.setBackground(hover_main);
    }//GEN-LAST:event_jLabel69MouseEntered

    private void jLabel69MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel69MouseClicked
        // TODO addPizza your handling code here:
        jP_landing_Cart_kasse_BackMouseClicked(evt);
    }//GEN-LAST:event_jLabel69MouseClicked

    private void cB_login_showPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cB_login_showPasswordActionPerformed
        // TODO addPizza your handling code here:
        if (cB_login_showPassword.isSelected()) {
            txt_login_passwort.setEchoChar('\u0000');
        } else {
            txt_login_passwort.setEchoChar('\u2022');
        }
    }//GEN-LAST:event_cB_login_showPasswordActionPerformed

    private void jP_login_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_login_btnMouseClicked
        // TODO addPizza your handling code here:
        try {
            String email = txt_login_email.getText();
            String pwd = String.valueOf(txt_login_passwort.getPassword());

            if ((angemeldeterBenutzer = db.anmeldenBenutzer(email, pwd)) != null) {
                status_login(true);
                updateGUI();
                aktualisiereHome();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Emailadresse oder Passwort falsch.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Fehler bei einer Datenbankabfrage.");
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Fehler bei einer lokalen Dateiabfrage.");
            System.err.println(ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(rootPane, "Bitte geben Sie richtige Werte ein und füllen Sie alle Felder aus.");
            System.err.println(ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(rootPane, "Interner Fehler.");
            System.err.println(ex.getMessage());
        }
    }//GEN-LAST:event_jP_login_btnMouseClicked

    private void jP_login_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_login_btnMouseEntered
        // TODO addPizza your handling code here:
        jP_login_btn.setBackground(hover_main);
    }//GEN-LAST:event_jP_login_btnMouseEntered

    private void jP_login_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_login_btnMouseExited
        // TODO addPizza your handling code here:
        jP_login_btn.setBackground(main);
    }//GEN-LAST:event_jP_login_btnMouseExited

    private void txt_login_gotoRegisterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_login_gotoRegisterMouseClicked
        // TODO addPizza your handling code here:
        status_Benutzer(true);
        status_Home(false);
    }//GEN-LAST:event_txt_login_gotoRegisterMouseClicked

    private void jLabel87MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel87MouseEntered
        // TODO addPizza your handling code here:
        jP_login_btn.setBackground(hover_main);
    }//GEN-LAST:event_jLabel87MouseEntered

    private void txt_login_gotoRegisterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_login_gotoRegisterMouseEntered
        // TODO addPizza your handling code here:
        txt_login_gotoRegister.setForeground(hover_main);
    }//GEN-LAST:event_txt_login_gotoRegisterMouseEntered

    private void txt_login_gotoRegisterMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_login_gotoRegisterMouseExited
        // TODO addPizza your handling code here:
        txt_login_gotoRegister.setForeground(pizza_main);
    }//GEN-LAST:event_txt_login_gotoRegisterMouseExited

    private void jP_landing_Cart_kasse_OrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_kasse_OrderMouseClicked
        // TODO addPizza your handling code here:
        if (db.getLetzteBestellungsinfo(angemeldeterBenutzer) != null) {
            if (db.getLetzteBestellungsinfo(angemeldeterBenutzer).isFertig()) {
                if (cB_Cart_Bar.isSelected()) {
                    String coupon = "kein Coupon";
                    String extra = txa_landing_kasse_Extra.getText();
                    double betrag = Double.parseDouble(lbl_landing_Cart_kasse_Gesamtbetrag.getText().replace(",", "."));

                    DateTimeFormatter dtf4 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                    String time = (dtf4.format(LocalDateTime.now()));

                    if (!txt_landing_kasse_coupon.getText().equals("") && pruefeCoupon(txt_landing_kasse_coupon.getText()) != 0) {
                        coupon = txt_landing_kasse_coupon.getText();
                    }

                    Bestellung bestellung = new Bestellung(warenkorb, angemeldeterBenutzer, kassenGesamtBetrag, coupon, extra, time, db);
                    leereWarenkorb();
                    status_Cart_ordered(true);
                    status_Cart_kasse(false);
                    status_Cart_main(false);

                    txt_landing_kasse_coupon.setText("");
                    cB_Cart_Bar.setSelected(false);

                    aktualisiereHome();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Akzeptieren Sie bitte noch, dass Sie das Bargeld dem Lieferanten bei Lieferung übergeben werden.");
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Warten Sie bitte noch, bis Ihre vorherige Bestellung angekommen ist.");
            }
        } else {
            if (cB_Cart_Bar.isSelected()) {
                String coupon = "kein Coupon";
                String extra = txa_landing_kasse_Extra.getText();
                double betrag = Double.parseDouble(lbl_landing_Cart_kasse_Gesamtbetrag.getText().replace(",", "."));

                DateTimeFormatter dtf4 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                String time = (dtf4.format(LocalDateTime.now()));

                if (!txt_landing_kasse_coupon.getText().equals("") && pruefeCoupon(txt_landing_kasse_coupon.getText()) != 0) {
                    coupon = txt_landing_kasse_coupon.getText();
                }

                Bestellung bestellung = new Bestellung(warenkorb, angemeldeterBenutzer, kassenGesamtBetrag, coupon, extra, time, db);
                leereWarenkorb();
                status_Cart_ordered(true);
                status_Cart_kasse(false);
                status_Cart_main(false);

                txt_landing_kasse_coupon.setText("");
                cB_Cart_Bar.setSelected(false);
                jP_landing_Cart_kasse_Order.setBackground(disabled);
                aktualisiereHome();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Akzeptieren Sie bitte noch, dass Sie das Bargeld dem Lieferanten bei Lieferung übergeben werden.");
            }
        }
    }//GEN-LAST:event_jP_landing_Cart_kasse_OrderMouseClicked

    private void jLabel70MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel70MouseClicked
        // TODO addPizza your handling code here:
        jP_landing_Cart_kasse_OrderMouseClicked(evt);
    }//GEN-LAST:event_jLabel70MouseClicked

    private void jP_landing_Cart_ordermoreMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_ordermoreMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Cart_ordermore.setBackground(main);
    }//GEN-LAST:event_jP_landing_Cart_ordermoreMouseExited

    private void jP_landing_Cart_ordermoreMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_ordermoreMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Cart_ordermore.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Cart_ordermoreMouseEntered

    private void jP_landing_Cart_ordermoreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_ordermoreMouseClicked
        // TODO addPizza your handling code here:
        status_Cart_main(true);
        status_Cart_ordered(false);
        status_Cart_kasse(false);
        status_Cart(false);
        status_Home(true);
    }//GEN-LAST:event_jP_landing_Cart_ordermoreMouseClicked

    private void jLabel89MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel89MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Cart_ordermore.setBackground(hover_main);
    }//GEN-LAST:event_jLabel89MouseEntered

    private void jLabel89MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel89MouseClicked
        // TODO addPizza your handling code here:
        status_Cart_main(true);
        status_Cart_ordered(false);
        status_Cart_kasse(false);
        status_Cart(false);
        status_Home(true);
    }//GEN-LAST:event_jLabel89MouseClicked

    private void jLabel97MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel97MouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_rucula(false);
        status_Bestellen_home(true);
    }//GEN-LAST:event_jLabel97MouseClicked

    private void jLabel97MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel97MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_rucula_Back.setBackground(hover_main);
    }//GEN-LAST:event_jLabel97MouseEntered

    private void jP_landing_Bestellen_rucula_BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_BackMouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_rucula(false);
        status_Bestellen_home(true);
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_BackMouseClicked

    private void jP_landing_Bestellen_rucula_BackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_BackMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_rucula_Back.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_BackMouseEntered

    private void jP_landing_Bestellen_rucula_BackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_BackMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_rucula_Back.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_BackMouseExited

    private void jp_landing_Bestellen_rucula_toCart_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_rucula_toCart_LabelMouseClicked
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_rucula_toCartMouseClicked(evt);
    }//GEN-LAST:event_jp_landing_Bestellen_rucula_toCart_LabelMouseClicked

    private void jp_landing_Bestellen_rucula_toCart_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_rucula_toCart_LabelMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_rucula_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jp_landing_Bestellen_rucula_toCart_LabelMouseEntered

    private void jP_landing_Bestellen_rucula_toCartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_toCartMouseClicked
        // TODO addPizza your handling code here:
        if (!warenkorb.isVoll()) {
            boolean salami = cB_rucula_Salami.isSelected();
            boolean zwiebeln = cB_rucula_Zwiebeln.isSelected();
            boolean rucola = cB_rucula_Rucola.isSelected();
            boolean kaese = cB_rucula_Kaese.isSelected();
            boolean tomatensoße = cB_rucula_Tomatensosse.isSelected();

            int anzahl = Integer.parseInt(jP_landing_Bestellen_rucula_Counter.getText());
            String typ = "Rucula";

            Pizza ruculaPizza = new Pizza(typ, anzahl, tomatensoße, kaese, rucola, salami, zwiebeln);
            JOptionPane.showMessageDialog(rootPane, "Preis der Pizza: " + kovertiereZuEuro(ruculaPizza.berechnePizzaPreis()) + "€");
            int index = warenkorb.addPizza(ruculaPizza);
            aktualisiereWarenkorb(index);

            status_Bestellen_rucula(false);
            status_Bestellen_home(true);

            cB_rucula_Salami.setSelected(false);
            cB_rucula_Zwiebeln.setSelected(true);
            cB_rucula_Rucola.setSelected(true);
            cB_rucula_Kaese.setSelected(true);
            cB_rucula_Tomatensosse.setSelected(true);
            jP_landing_Bestellen_rucula_Counter.setText(Integer.toString(orderRucula = 1));
        } else {
            JOptionPane.showMessageDialog(rootPane, "Sie haben nur 4 Pizzaslots im Warenkorb zur Verfügung.");
            System.out.println("Maximale Warenkorbgröße erreicht.");
        }
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_toCartMouseClicked

    private void jP_landing_Bestellen_rucula_toCartMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_toCartMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_rucula_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_toCartMouseEntered

    private void jP_landing_Bestellen_rucula_toCartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_toCartMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_rucula_toCart.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_toCartMouseExited

    private void jLabel99MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel99MouseClicked
        // TODO addPizza your handling code here:
        orderRucula++;
        jP_landing_Bestellen_rucula_Counter.setText(Integer.toString(orderRucula));
    }//GEN-LAST:event_jLabel99MouseClicked

    private void jLabel99MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel99MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_rucula_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel99MouseEntered

    private void jLabel99MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel99MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel99MouseExited

    private void jP_landing_Bestellen_rucula_PlusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_PlusMouseClicked
        // TODO addPizza your handling code here:
        orderRucula++;
        jP_landing_Bestellen_rucula_Counter.setText(Integer.toString(orderRucula));
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_PlusMouseClicked

    private void jP_landing_Bestellen_rucula_PlusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_PlusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_rucula_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_PlusMouseEntered

    private void jP_landing_Bestellen_rucula_PlusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_PlusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_rucula_Plus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_PlusMouseExited

    private void jP_landing_Bestellen_rucula_CounterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_CounterMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_CounterMouseClicked

    private void jP_landing_Bestellen_rucula_CounterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_CounterMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_CounterMouseEntered

    private void jP_landing_Bestellen_rucula_CountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_CountMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_CountMouseClicked

    private void jP_landing_Bestellen_rucula_CountMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_CountMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_CountMouseEntered

    private void jP_landing_Bestellen_rucula_CountMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_CountMouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_CountMouseExited

    private void jLabel100MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel100MouseClicked
        // TODO addPizza your handling code here:
        if (orderRucula > 1) {
            orderRucula--;
            jP_landing_Bestellen_rucula_Counter.setText(Integer.toString(orderRucula));
        }
    }//GEN-LAST:event_jLabel100MouseClicked

    private void jLabel100MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel100MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_rucula_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel100MouseEntered

    private void jLabel100MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel100MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel100MouseExited

    private void jP_landing_Bestellen_rucula_MinusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_MinusMouseClicked
        // TODO addPizza your handling code here:
        if (orderRucula > 1) {
            orderRucula--;
            jP_landing_Bestellen_rucula_Counter.setText(Integer.toString(orderRucula));
        }
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_MinusMouseClicked

    private void jP_landing_Bestellen_rucula_MinusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_MinusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_rucula_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_MinusMouseEntered

    private void jP_landing_Bestellen_rucula_MinusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_rucula_MinusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_rucula_Minus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_rucula_MinusMouseExited

    private void jLabel25MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel25MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_rucula.setBackground(hover_main);
    }//GEN-LAST:event_jLabel25MouseEntered

    private void jLabel25MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel25MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_rucula.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_rucula(true);
    }//GEN-LAST:event_jLabel25MouseClicked

    private void jLabel105MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel105MouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_caprese(false);
        status_Bestellen_home(true);
    }//GEN-LAST:event_jLabel105MouseClicked

    private void jLabel105MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel105MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_caprese_Back.setBackground(hover_main);
    }//GEN-LAST:event_jLabel105MouseEntered

    private void jP_landing_Bestellen_caprese_BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_BackMouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_caprese(false);
        status_Bestellen_home(true);
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_BackMouseClicked

    private void jP_landing_Bestellen_caprese_BackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_BackMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_caprese_Back.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_BackMouseEntered

    private void jP_landing_Bestellen_caprese_BackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_BackMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_caprese_Back.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_BackMouseExited

    private void jp_landing_Bestellen_caprese_toCart_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_caprese_toCart_LabelMouseClicked
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_caprese_toCartMouseClicked(evt);
    }//GEN-LAST:event_jp_landing_Bestellen_caprese_toCart_LabelMouseClicked

    private void jp_landing_Bestellen_caprese_toCart_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_caprese_toCart_LabelMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_caprese_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jp_landing_Bestellen_caprese_toCart_LabelMouseEntered

    private void jP_landing_Bestellen_caprese_toCartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_toCartMouseClicked
        // TODO addPizza your handling code here:
        if (!warenkorb.isVoll()) {
            boolean salami = cB_caprese_Salami.isSelected();
            boolean zwiebeln = cB_caprese_Zwiebeln.isSelected();
            boolean rucola = cB_caprese_Rucola.isSelected();
            boolean kaese = cB_caprese_Kaese.isSelected();
            boolean tomatensoße = cB_caprese_Tomatensosse.isSelected();

            int anzahl = Integer.parseInt(jP_landing_Bestellen_caprese_Counter.getText());
            String typ = "Caprese";

            Pizza capresePizza = new Pizza(typ, anzahl, tomatensoße, kaese, rucola, salami, zwiebeln);
            JOptionPane.showMessageDialog(rootPane, "Preis der Pizza: " + kovertiereZuEuro(capresePizza.berechnePizzaPreis()) + "€");
            int index = warenkorb.addPizza(capresePizza);
            aktualisiereWarenkorb(index);

            status_Bestellen_caprese(false);
            status_Bestellen_home(true);

            cB_caprese_Salami.setSelected(true);
            cB_caprese_Zwiebeln.setSelected(true);
            cB_caprese_Rucola.setSelected(false);
            cB_caprese_Kaese.setSelected(true);
            cB_caprese_Tomatensosse.setSelected(true);
            jP_landing_Bestellen_caprese_Counter.setText(Integer.toString(orderCaprese = 1));
        } else {
            JOptionPane.showMessageDialog(rootPane, "Sie haben nur 4 Pizzaslots im Warenkorb zur Verfügung.");
            System.out.println("Maximale Warenkorbgröße erreicht.");
        }
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_toCartMouseClicked

    private void jP_landing_Bestellen_caprese_toCartMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_toCartMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_caprese_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_toCartMouseEntered

    private void jP_landing_Bestellen_caprese_toCartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_toCartMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_caprese_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_toCartMouseExited

    private void jLabel107MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel107MouseClicked
        // TODO addPizza your handling code here:
        orderCaprese++;
        jP_landing_Bestellen_caprese_Counter.setText(Integer.toString(orderCaprese));
    }//GEN-LAST:event_jLabel107MouseClicked

    private void jLabel107MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel107MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_caprese_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel107MouseEntered

    private void jLabel107MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel107MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel107MouseExited

    private void jP_landing_Bestellen_caprese_PlusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_PlusMouseClicked
        // TODO addPizza your handling code here:
        orderCaprese++;
        jP_landing_Bestellen_caprese_Counter.setText(Integer.toString(orderCaprese));
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_PlusMouseClicked

    private void jP_landing_Bestellen_caprese_PlusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_PlusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_caprese_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_PlusMouseEntered

    private void jP_landing_Bestellen_caprese_PlusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_PlusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_caprese_Plus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_PlusMouseExited

    private void jP_landing_Bestellen_caprese_CounterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_CounterMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_CounterMouseClicked

    private void jP_landing_Bestellen_caprese_CounterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_CounterMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_CounterMouseEntered

    private void jP_landing_Bestellen_caprese_CountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_CountMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_CountMouseClicked

    private void jP_landing_Bestellen_caprese_CountMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_CountMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_CountMouseEntered

    private void jP_landing_Bestellen_caprese_CountMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_CountMouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_CountMouseExited

    private void jLabel108MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel108MouseClicked
        // TODO addPizza your handling code here:
        if (orderCaprese > 1) {
            orderCaprese--;
            jP_landing_Bestellen_caprese_Counter.setText(Integer.toString(orderCaprese));
        }
    }//GEN-LAST:event_jLabel108MouseClicked

    private void jLabel108MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel108MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_caprese_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel108MouseEntered

    private void jLabel108MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel108MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel108MouseExited

    private void jP_landing_Bestellen_caprese_MinusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_MinusMouseClicked
        // TODO addPizza your handling code here:
        if (orderCaprese > 1) {
            orderCaprese--;
            jP_landing_Bestellen_caprese_Counter.setText(Integer.toString(orderCaprese));
        }
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_MinusMouseClicked

    private void jP_landing_Bestellen_caprese_MinusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_MinusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_caprese_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_MinusMouseEntered

    private void jP_landing_Bestellen_caprese_MinusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_caprese_MinusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_caprese_Minus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_caprese_MinusMouseExited

    private void jLabel27MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel27MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_caprese.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_caprese(true);

    }//GEN-LAST:event_jLabel27MouseClicked

    private void jP_Bestellen_ruculaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_ruculaMouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_rucula.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_rucula(true);
    }//GEN-LAST:event_jP_Bestellen_ruculaMouseClicked

    private void jLabel113MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel113MouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_funghi(false);
        status_Bestellen_home(true);
    }//GEN-LAST:event_jLabel113MouseClicked

    private void jLabel113MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel113MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_funghi_Back.setBackground(hover_main);
    }//GEN-LAST:event_jLabel113MouseEntered

    private void jP_landing_Bestellen_funghi_BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_BackMouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_funghi(false);
        status_Bestellen_home(true);
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_BackMouseClicked

    private void jP_landing_Bestellen_funghi_BackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_BackMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_funghi_Back.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_BackMouseEntered

    private void jP_landing_Bestellen_funghi_BackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_BackMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_funghi_Back.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_BackMouseExited

    private void jp_landing_Bestellen_funghi_toCart_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_funghi_toCart_LabelMouseClicked
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_funghi_toCartMouseClicked(evt);
    }//GEN-LAST:event_jp_landing_Bestellen_funghi_toCart_LabelMouseClicked

    private void jp_landing_Bestellen_funghi_toCart_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_funghi_toCart_LabelMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_funghi_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jp_landing_Bestellen_funghi_toCart_LabelMouseEntered

    private void jP_landing_Bestellen_funghi_toCartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_toCartMouseClicked
        // TODO addPizza your handling code here:
        if (!warenkorb.isVoll()) {
            boolean salami = cB_funghi_Salami.isSelected();
            boolean zwiebeln = cB_funghi_Zwiebeln.isSelected();
            boolean rucola = cB_funghi_Rucola.isSelected();
            boolean kaese = cB_funghi_Kaese.isSelected();
            boolean tomatensoße = cB_funghi_Tomatensosse.isSelected();

            int anzahl = Integer.parseInt(jP_landing_Bestellen_funghi_Counter.getText());
            String typ = "Funghi";

            Pizza funghiPizza = new Pizza(typ, anzahl, tomatensoße, kaese, rucola, salami, zwiebeln);
            JOptionPane.showMessageDialog(rootPane, "Preis der Pizza: " + kovertiereZuEuro(funghiPizza.berechnePizzaPreis()) + "€");
            int index = warenkorb.addPizza(funghiPizza);
            aktualisiereWarenkorb(index);

            status_Bestellen_funghi(false);
            status_Bestellen_home(true);

            cB_funghi_Salami.setSelected(true);
            cB_funghi_Zwiebeln.setSelected(true);
            cB_funghi_Rucola.setSelected(false);
            cB_funghi_Kaese.setSelected(true);
            cB_funghi_Tomatensosse.setSelected(true);
            jP_landing_Bestellen_funghi_Counter.setText(Integer.toString(orderFunghi = 1));
        } else {
            JOptionPane.showMessageDialog(rootPane, "Sie haben nur 4 Pizzaslots im Warenkorb zur Verfügung.");
            System.out.println("Maximale Warenkorbgröße erreicht.");
        }
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_toCartMouseClicked

    private void jP_landing_Bestellen_funghi_toCartMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_toCartMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_funghi_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_toCartMouseEntered

    private void jP_landing_Bestellen_funghi_toCartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_toCartMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_funghi_toCart.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_toCartMouseExited

    private void jLabel115MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel115MouseClicked
        // TODO addPizza your handling code here:
        orderFunghi++;
        jP_landing_Bestellen_funghi_Counter.setText(Integer.toString(orderFunghi));
    }//GEN-LAST:event_jLabel115MouseClicked

    private void jLabel115MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel115MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_funghi_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel115MouseEntered

    private void jLabel115MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel115MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel115MouseExited

    private void jP_landing_Bestellen_funghi_PlusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_PlusMouseClicked
        // TODO addPizza your handling code here:
        orderFunghi++;
        jP_landing_Bestellen_funghi_Counter.setText(Integer.toString(orderFunghi));
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_PlusMouseClicked

    private void jP_landing_Bestellen_funghi_PlusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_PlusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_funghi_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_PlusMouseEntered

    private void jP_landing_Bestellen_funghi_PlusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_PlusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_funghi_Plus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_PlusMouseExited

    private void jP_landing_Bestellen_funghi_CounterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_CounterMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_CounterMouseClicked

    private void jP_landing_Bestellen_funghi_CounterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_CounterMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_CounterMouseEntered

    private void jP_landing_Bestellen_funghi_CountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_CountMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_CountMouseClicked

    private void jP_landing_Bestellen_funghi_CountMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_CountMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_CountMouseEntered

    private void jP_landing_Bestellen_funghi_CountMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_CountMouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_CountMouseExited

    private void jLabel116MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel116MouseClicked
        // TODO addPizza your handling code here:
        if (orderFunghi > 1) {
            orderFunghi--;
            jP_landing_Bestellen_funghi_Counter.setText(Integer.toString(orderFunghi));
        }
    }//GEN-LAST:event_jLabel116MouseClicked

    private void jLabel116MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel116MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_funghi_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel116MouseEntered

    private void jLabel116MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel116MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel116MouseExited

    private void jP_landing_Bestellen_funghi_MinusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_MinusMouseClicked
        // TODO addPizza your handling code here:
        if (orderFunghi > 1) {
            orderFunghi--;
            jP_landing_Bestellen_funghi_Counter.setText(Integer.toString(orderFunghi));
        }
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_MinusMouseClicked

    private void jP_landing_Bestellen_funghi_MinusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_MinusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_funghi_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_MinusMouseEntered

    private void jP_landing_Bestellen_funghi_MinusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_funghi_MinusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_funghi_Minus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_funghi_MinusMouseExited

    private void jLabel27MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel27MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_caprese.setBackground(hover_main);
    }//GEN-LAST:event_jLabel27MouseEntered

    private void jLabel29MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel29MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_funghi.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_funghi(true);
    }//GEN-LAST:event_jLabel29MouseClicked

    private void jP_Bestellen_funghiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_funghiMouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_funghi.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_funghi(true);
    }//GEN-LAST:event_jP_Bestellen_funghiMouseClicked

    private void jLabel29MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel29MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_funghi.setBackground(hover_main);
    }//GEN-LAST:event_jLabel29MouseEntered

    private void jLabel121MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel121MouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_margherita(false);
        status_Bestellen_home(true);
    }//GEN-LAST:event_jLabel121MouseClicked

    private void jLabel121MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel121MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_margherita_Back.setBackground(hover_main);
    }//GEN-LAST:event_jLabel121MouseEntered

    private void jP_landing_Bestellen_margherita_BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_BackMouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_margherita(false);
        status_Bestellen_home(true);
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_BackMouseClicked

    private void jP_landing_Bestellen_margherita_BackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_BackMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_margherita_Back.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_BackMouseEntered

    private void jP_landing_Bestellen_margherita_BackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_BackMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_margherita_Back.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_BackMouseExited

    private void jp_landing_Bestellen_margherita_toCart_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_margherita_toCart_LabelMouseClicked
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_margherita_toCartMouseClicked(evt);
    }//GEN-LAST:event_jp_landing_Bestellen_margherita_toCart_LabelMouseClicked

    private void jp_landing_Bestellen_margherita_toCart_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_margherita_toCart_LabelMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_margherita_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jp_landing_Bestellen_margherita_toCart_LabelMouseEntered

    private void jP_landing_Bestellen_margherita_toCartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_toCartMouseClicked
        // TODO addPizza your handling code here:
        if (!warenkorb.isVoll()) {
            boolean salami = cB_margherita_Salami.isSelected();
            boolean zwiebeln = cB_margherita_Zwiebeln.isSelected();
            boolean rucola = cB_margherita_Rucola.isSelected();
            boolean kaese = cB_margherita_Kaese.isSelected();
            boolean tomatensoße = cB_margherita_Tomatensosse.isSelected();

            int anzahl = Integer.parseInt(jP_landing_Bestellen_margherita_Counter.getText());
            String typ = "Margherita";

            Pizza margheritaPizza = new Pizza(typ, anzahl, tomatensoße, kaese, rucola, salami, zwiebeln);
            JOptionPane.showMessageDialog(rootPane, "Preis der Pizza: " + kovertiereZuEuro(margheritaPizza.berechnePizzaPreis()) + "€");
            int index = warenkorb.addPizza(margheritaPizza);
            aktualisiereWarenkorb(index);

            status_Bestellen_margherita(false);
            status_Bestellen_home(true);

            cB_margherita_Salami.setSelected(false);
            cB_margherita_Zwiebeln.setSelected(false);
            cB_margherita_Rucola.setSelected(false);
            cB_margherita_Kaese.setSelected(true);
            cB_margherita_Tomatensosse.setSelected(true);
            jP_landing_Bestellen_margherita_Counter.setText(Integer.toString(orderMargherita = 1));
        } else {
            JOptionPane.showMessageDialog(rootPane, "Sie haben nur 4 Pizzaslots im Warenkorb zur Verfügung.");
            System.out.println("Maximale Warenkorbgröße erreicht.");
        }
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_toCartMouseClicked

    private void jP_landing_Bestellen_margherita_toCartMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_toCartMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_margherita_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_toCartMouseEntered

    private void jP_landing_Bestellen_margherita_toCartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_toCartMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_margherita_toCart.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_toCartMouseExited

    private void jLabel123MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel123MouseClicked
        // TODO addPizza your handling code here:
        orderMargherita++;
        jP_landing_Bestellen_margherita_Counter.setText(Integer.toString(orderMargherita));
    }//GEN-LAST:event_jLabel123MouseClicked

    private void jLabel123MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel123MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_margherita_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel123MouseEntered

    private void jLabel123MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel123MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel123MouseExited

    private void jP_landing_Bestellen_margherita_PlusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_PlusMouseClicked
        // TODO addPizza your handling code here:
        orderMargherita++;
        jP_landing_Bestellen_margherita_Counter.setText(Integer.toString(orderMargherita));
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_PlusMouseClicked

    private void jP_landing_Bestellen_margherita_PlusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_PlusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_margherita_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_PlusMouseEntered

    private void jP_landing_Bestellen_margherita_PlusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_PlusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_margherita_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_PlusMouseExited

    private void jP_landing_Bestellen_margherita_CounterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_CounterMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_CounterMouseClicked

    private void jP_landing_Bestellen_margherita_CounterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_CounterMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_CounterMouseEntered

    private void jP_landing_Bestellen_margherita_CountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_CountMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_CountMouseClicked

    private void jP_landing_Bestellen_margherita_CountMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_CountMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_CountMouseEntered

    private void jP_landing_Bestellen_margherita_CountMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_CountMouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_CountMouseExited

    private void jLabel124MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel124MouseClicked
        // TODO addPizza your handling code here:
        if (orderMargherita > 1) {
            orderMargherita--;
            jP_landing_Bestellen_margherita_Counter.setText(Integer.toString(orderMargherita));
        }
    }//GEN-LAST:event_jLabel124MouseClicked

    private void jLabel124MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel124MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_margherita_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel124MouseEntered

    private void jLabel124MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel124MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel124MouseExited

    private void jP_landing_Bestellen_margherita_MinusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_MinusMouseClicked
        // TODO addPizza your handling code here:
        if (orderMargherita > 1) {
            orderMargherita--;
            jP_landing_Bestellen_margherita_Counter.setText(Integer.toString(orderMargherita));
        }
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_MinusMouseClicked

    private void jP_landing_Bestellen_margherita_MinusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_MinusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_margherita_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_MinusMouseEntered

    private void jP_landing_Bestellen_margherita_MinusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_margherita_MinusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Bestellen_margherita_Minus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Bestellen_margherita_MinusMouseExited

    private void jLabel31MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_margherita.setBackground(hover_main);
    }//GEN-LAST:event_jLabel31MouseEntered

    private void jLabel31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_margherita.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_margherita(true);
    }//GEN-LAST:event_jLabel31MouseClicked

    private void jP_Bestellen_margheritaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_margheritaMouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_margherita.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_margherita(true);
    }//GEN-LAST:event_jP_Bestellen_margheritaMouseClicked

    private void jLabel129MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel129MouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_pepperoni(false);
        status_Bestellen_home(true);
    }//GEN-LAST:event_jLabel129MouseClicked

    private void jLabel129MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel129MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_pepperoni_salami_Back.setBackground(hover_main);
    }//GEN-LAST:event_jLabel129MouseEntered

    private void jP_landing_pepperoni_salami_BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_BackMouseClicked
        // TODO addPizza your handling code here:
        status_Bestellen_pepperoni(false);
        status_Bestellen_home(true);
    }//GEN-LAST:event_jP_landing_pepperoni_salami_BackMouseClicked

    private void jP_landing_pepperoni_salami_BackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_BackMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_pepperoni_salami_Back.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_pepperoni_salami_BackMouseEntered

    private void jP_landing_pepperoni_salami_BackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_BackMouseExited
        // TODO addPizza your handling code here:
        jP_landing_pepperoni_salami_Back.setBackground(main);
    }//GEN-LAST:event_jP_landing_pepperoni_salami_BackMouseExited

    private void jp_landing_Bestellen_pepperoni_toCart_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_pepperoni_toCart_LabelMouseClicked
        // TODO addPizza your handling code here:
        jP_landing_pepperoni_salami_toCartMouseClicked(evt);
    }//GEN-LAST:event_jp_landing_Bestellen_pepperoni_toCart_LabelMouseClicked

    private void jp_landing_Bestellen_pepperoni_toCart_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Bestellen_pepperoni_toCart_LabelMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_pepperoni_salami_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jp_landing_Bestellen_pepperoni_toCart_LabelMouseEntered

    private void jP_landing_pepperoni_salami_toCartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_toCartMouseClicked
        // TODO addPizza your handling code here:
        if (!warenkorb.isVoll()) {
            boolean salami = cB_pepperoni_Salami.isSelected();
            boolean zwiebeln = cB_pepperoni_Zwiebeln.isSelected();
            boolean rucola = cB_pepperoni_Rucola.isSelected();
            boolean kaese = cB_pepperoni_Kaese.isSelected();
            boolean tomatensoße = cB_pepperoni_Tomatensosse.isSelected();

            int anzahl = Integer.parseInt(jP_landing_Bestellen_pepperoni_Counter.getText());
            String typ = "Pepperoni";

            Pizza pepperoniPizza = new Pizza(typ, anzahl, tomatensoße, kaese, rucola, salami, zwiebeln);
            JOptionPane.showMessageDialog(rootPane, "Preis der Pizza: " + kovertiereZuEuro(pepperoniPizza.berechnePizzaPreis()) + "€");
            int index = warenkorb.addPizza(pepperoniPizza);
            aktualisiereWarenkorb(index);

            status_Bestellen_pepperoni(false);
            status_Bestellen_home(true);

            cB_pepperoni_Salami.setSelected(true);
            cB_pepperoni_Zwiebeln.setSelected(false);
            cB_pepperoni_Rucola.setSelected(false);
            cB_pepperoni_Kaese.setSelected(true);
            cB_pepperoni_Tomatensosse.setSelected(true);
            jP_landing_Bestellen_pepperoni_Counter.setText(Integer.toString(orderPepperoni = 1));
        } else {
            JOptionPane.showMessageDialog(rootPane, "Sie haben nur 4 Pizzaslots im Warenkorb zur Verfügung.");
            System.out.println("Maximale Warenkorbgröße erreicht.");
        }
    }//GEN-LAST:event_jP_landing_pepperoni_salami_toCartMouseClicked

    private void jP_landing_pepperoni_salami_toCartMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_toCartMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_pepperoni_salami_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_pepperoni_salami_toCartMouseEntered

    private void jP_landing_pepperoni_salami_toCartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_toCartMouseExited
        // TODO addPizza your handling code here:
        jP_landing_pepperoni_salami_toCart.setBackground(main);
    }//GEN-LAST:event_jP_landing_pepperoni_salami_toCartMouseExited

    private void jLabel131MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel131MouseClicked
        // TODO addPizza your handling code here:
        orderPepperoni++;
        jP_landing_Bestellen_pepperoni_Counter.setText(Integer.toString(orderPepperoni));
    }//GEN-LAST:event_jLabel131MouseClicked

    private void jLabel131MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel131MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_pepperoni_salami_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel131MouseEntered

    private void jLabel131MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel131MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel131MouseExited

    private void jP_landing_pepperoni_salami_PlusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_PlusMouseClicked
        // TODO addPizza your handling code here:
        orderPepperoni++;
        jP_landing_Bestellen_pepperoni_Counter.setText(Integer.toString(orderPepperoni));
    }//GEN-LAST:event_jP_landing_pepperoni_salami_PlusMouseClicked

    private void jP_landing_pepperoni_salami_PlusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_PlusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_pepperoni_salami_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_pepperoni_salami_PlusMouseEntered

    private void jP_landing_pepperoni_salami_PlusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_PlusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_pepperoni_salami_Plus.setBackground(main);
    }//GEN-LAST:event_jP_landing_pepperoni_salami_PlusMouseExited

    private void jP_landing_Bestellen_pepperoni_CounterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_pepperoni_CounterMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_pepperoni_CounterMouseClicked

    private void jP_landing_Bestellen_pepperoni_CounterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Bestellen_pepperoni_CounterMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Bestellen_pepperoni_CounterMouseEntered

    private void jP_landing_pepperoni_salami_CountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_CountMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_pepperoni_salami_CountMouseClicked

    private void jP_landing_pepperoni_salami_CountMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_CountMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_pepperoni_salami_CountMouseEntered

    private void jP_landing_pepperoni_salami_CountMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_CountMouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_pepperoni_salami_CountMouseExited

    private void jLabel132MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel132MouseClicked
        // TODO addPizza your handling code here:
        if (orderPepperoni > 1) {
            orderPepperoni--;
            jP_landing_Bestellen_pepperoni_Counter.setText(Integer.toString(orderPepperoni));
        }
    }//GEN-LAST:event_jLabel132MouseClicked

    private void jLabel132MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel132MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_pepperoni_salami_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel132MouseEntered

    private void jLabel132MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel132MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel132MouseExited

    private void jP_landing_pepperoni_salami_MinusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_MinusMouseClicked
        // TODO addPizza your handling code here:
        if (orderPepperoni > 1) {
            orderPepperoni--;
            jP_landing_Bestellen_pepperoni_Counter.setText(Integer.toString(orderPepperoni));
        }
    }//GEN-LAST:event_jP_landing_pepperoni_salami_MinusMouseClicked

    private void jP_landing_pepperoni_salami_MinusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_MinusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_pepperoni_salami_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_pepperoni_salami_MinusMouseEntered

    private void jP_landing_pepperoni_salami_MinusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_pepperoni_salami_MinusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_pepperoni_salami_Minus.setBackground(main);
    }//GEN-LAST:event_jP_landing_pepperoni_salami_MinusMouseExited

    private void jLabel33MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel33MouseEntered
        // TODO addPizza your handling code here:
        jP_Bestellen_pepperoni.setBackground(hover_main);
    }//GEN-LAST:event_jLabel33MouseEntered

    private void jLabel33MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel33MouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_pepperoni.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_pepperoni(true);
    }//GEN-LAST:event_jLabel33MouseClicked

    private void jP_Bestellen_pepperoniMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_Bestellen_pepperoniMouseClicked
        // TODO addPizza your handling code here:
        jP_Bestellen_pepperoni.setBackground(standart);
        status_Bestellen_home(false);
        status_Bestellen_pepperoni(true);
    }//GEN-LAST:event_jP_Bestellen_pepperoniMouseClicked

    private void jLabel144MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel144MouseClicked
        // TODO addPizza your handling code here:
        satuts_Cart_bearbeiten(false);
        status_Cart_main(true);
    }//GEN-LAST:event_jLabel144MouseClicked

    private void jLabel144MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel144MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Cart_bearbeiten_Back.setBackground(hover_main);
    }//GEN-LAST:event_jLabel144MouseEntered

    private void jP_landing_Cart_bearbeiten_BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_BackMouseClicked
        // TODO addPizza your handling code here:
        satuts_Cart_bearbeiten(false);
        status_Cart_main(true);
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_BackMouseClicked

    private void jP_landing_Cart_bearbeiten_BackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_BackMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Cart_bearbeiten_Back.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_BackMouseEntered

    private void jP_landing_Cart_bearbeiten_BackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_BackMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Cart_bearbeiten_Back.setBackground(main);
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_BackMouseExited

    private void jLabel145MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel145MouseClicked
        // TODO addPizza your handling code here:
        jP_landing_Cart_bearbeiten_toCartMouseClicked(evt);
    }//GEN-LAST:event_jLabel145MouseClicked

    private void jLabel145MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel145MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Cart_bearbeiten_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jLabel145MouseEntered

    private void jP_landing_Cart_bearbeiten_toCartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_toCartMouseClicked
        // TODO addPizza your handling code here:
        boolean salami = false;
        boolean kaese = false;
        boolean zwiebeln = false;
        boolean tomatensosse = false;
        boolean rocula = false;
        if (cB_bearbeiten_Salami.isSelected()) {
            salami = true;
        }
        if (cB_bearbeiten_Kaese.isSelected()) {
            kaese = true;
        }
        if (cB_bearbeiten_Zwiebeln.isSelected()) {
            zwiebeln = true;
        }
        if (cB_bearbeiten_Tomatensosse.isSelected()) {
            tomatensosse = true;
        }
        if (cB_bearbeiten_Rucola.isSelected()) {
            rocula = true;
        }

        int anzahl = Integer.parseInt(jP_landing_Cart_bearbeiten_Counter.getText());
        warenkorb.getWarenkorbItems().get(aktuellerWarenkorbIndex).edit(anzahl, tomatensosse, kaese, rocula, salami, zwiebeln);
        JOptionPane.showMessageDialog(rootPane, "Neuer Preis der Pizza: " + kovertiereZuEuro(warenkorb.getWarenkorbItems().get(aktuellerWarenkorbIndex).berechnePizzaPreis()) + "€");
        System.out.println("Pizza im Slot " + aktuellerWarenkorbIndex + " aktualisiert ");
        satuts_Cart_bearbeiten(false);
        status_Cart_main(true);
        aktualisiereWarenkorb(aktuellerWarenkorbIndex);
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_toCartMouseClicked

    private void jP_landing_Cart_bearbeiten_toCartMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_toCartMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Cart_bearbeiten_toCart.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_toCartMouseEntered

    private void jP_landing_Cart_bearbeiten_toCartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_toCartMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Cart_bearbeiten_toCart.setBackground(main);
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_toCartMouseExited

    private void jLabel146MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel146MouseClicked
        // TODO addPizza your handling code here:
        bearbeiten++;
        jP_landing_Cart_bearbeiten_Counter.setText(Integer.toString(bearbeiten));
    }//GEN-LAST:event_jLabel146MouseClicked

    private void jLabel146MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel146MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Cart_bearbeiten_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel146MouseEntered

    private void jLabel146MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel146MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel146MouseExited

    private void jP_landing_Cart_bearbeiten_PlusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_PlusMouseClicked
        // TODO addPizza your handling code here:
        bearbeiten++;
        jP_landing_Cart_bearbeiten_Counter.setText(Integer.toString(bearbeiten));
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_PlusMouseClicked

    private void jP_landing_Cart_bearbeiten_PlusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_PlusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Cart_bearbeiten_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_PlusMouseEntered

    private void jP_landing_Cart_bearbeiten_PlusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_PlusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Cart_bearbeiten_Plus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_PlusMouseExited

    private void jP_landing_Cart_bearbeiten_CounterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_CounterMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_CounterMouseClicked

    private void jP_landing_Cart_bearbeiten_CounterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_CounterMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_CounterMouseEntered

    private void jP_landing_Cart_bearbeiten_CountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_CountMouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_CountMouseClicked

    private void jP_landing_Cart_bearbeiten_CountMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_CountMouseEntered
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_CountMouseEntered

    private void jP_landing_Cart_bearbeiten_CountMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_CountMouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_CountMouseExited

    private void jLabel147MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel147MouseClicked
        // TODO addPizza your handling code here:
        if (bearbeiten > 1) {
            bearbeiten--;
            jP_landing_Cart_bearbeiten_Counter.setText(Integer.toString(bearbeiten));
        }
    }//GEN-LAST:event_jLabel147MouseClicked

    private void jLabel147MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel147MouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Cart_bearbeiten_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jLabel147MouseEntered

    private void jLabel147MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel147MouseExited
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jLabel147MouseExited

    private void jP_landing_Cart_bearbeiten_MinusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_MinusMouseClicked
        // TODO addPizza your handling code here:
        if (bearbeiten > 1) {
            bearbeiten--;
            jP_landing_Cart_bearbeiten_Counter.setText(Integer.toString(bearbeiten));
        }
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_MinusMouseClicked

    private void jP_landing_Cart_bearbeiten_MinusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_MinusMouseEntered
        // TODO addPizza your handling code here:
        jP_landing_Cart_bearbeiten_Minus.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_MinusMouseEntered

    private void jP_landing_Cart_bearbeiten_MinusMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_bearbeiten_MinusMouseExited
        // TODO addPizza your handling code here:
        jP_landing_Cart_bearbeiten_Minus.setBackground(main);
    }//GEN-LAST:event_jP_landing_Cart_bearbeiten_MinusMouseExited

    private void jp_landing_Cart_Edit2_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_Edit2_LabelMouseClicked
        // TODO addPizza your handling code here:
        jP_landing_Cart_pizza_Bearbeiten2MouseClicked(evt);
    }//GEN-LAST:event_jp_landing_Cart_Edit2_LabelMouseClicked

    private void jP_landing_Cart_pizza_Bearbeiten1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Bearbeiten1MouseClicked
        // TODO addPizza your handling code here:
        if (warenkorb.getWarenkorbItems().get(0).getTyp().equals("noch frei")) {
            JOptionPane.showMessageDialog(rootPane, "Fügen Sie erst eine Pizza zu diesem Slot hinzu.");
        } else {
            aktualisiereCartBearbeiten(0);
            satuts_Cart_bearbeiten(true);
            status_Cart_main(false);
        }
    }//GEN-LAST:event_jP_landing_Cart_pizza_Bearbeiten1MouseClicked

    private void jP_landing_Cart_pizza_Name1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Name1MouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Cart_pizza_Name1MouseClicked

    private void jP_landing_Cart_pizza_Bearbeiten3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Bearbeiten3MouseClicked
        // TODO addPizza your handling code here:
        if (warenkorb.getWarenkorbItems().get(2).getTyp().equals("noch frei")) {
            JOptionPane.showMessageDialog(rootPane, "Fügen Sie erst eine Pizza zu diesem Slot hinzu.");
        } else {
            aktualisiereCartBearbeiten(2);
            satuts_Cart_bearbeiten(true);
            status_Cart_main(false);
        }
    }//GEN-LAST:event_jP_landing_Cart_pizza_Bearbeiten3MouseClicked

    private void jP_landing_Cart_pizza_Name3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Name3MouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Cart_pizza_Name3MouseClicked

    private void jP_landing_Cart_pizza_Bearbeiten4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Bearbeiten4MouseClicked
        // TODO addPizza your handling code here:
        if (warenkorb.getWarenkorbItems().get(3).getTyp().equals("noch frei")) {
            JOptionPane.showMessageDialog(rootPane, "Fügen Sie erst eine Pizza zu diesem Slot hinzu.");
        } else {
            aktualisiereCartBearbeiten(3);
            satuts_Cart_bearbeiten(true);
            status_Cart_main(false);
        }
    }//GEN-LAST:event_jP_landing_Cart_pizza_Bearbeiten4MouseClicked

    private void jP_landing_Cart_pizza_Name4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Name4MouseClicked
        // TODO addPizza your handling code here:
    }//GEN-LAST:event_jP_landing_Cart_pizza_Name4MouseClicked

    private void jp_landing_Cart_Edit1_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_Edit1_LabelMouseClicked
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten1MouseClicked(evt);
    }//GEN-LAST:event_jp_landing_Cart_Edit1_LabelMouseClicked

    private void jp_landing_Cart_Edit3_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_Edit3_LabelMouseClicked
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten3MouseClicked(evt);
    }//GEN-LAST:event_jp_landing_Cart_Edit3_LabelMouseClicked

    private void jp_landing_Cart_Edit4_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_Edit4_LabelMouseClicked
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten4MouseClicked(evt);
    }//GEN-LAST:event_jp_landing_Cart_Edit4_LabelMouseClicked

    private void jp_landing_Cart_pizza_Loeschen1_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_pizza_Loeschen1_LabelMouseClicked
        // TODO add your handling code here:
        if (!warenkorb.getWarenkorbItems().get(0).getTyp().equals("noch frei")) {
            if (JOptionPane.showConfirmDialog(rootPane, "Willst du diesen Pizzaslot wirklich löschen?", "Pizzaslot 1", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == 0) {
                Pizza leerePizza = new Pizza("noch frei", 0, false, false, false, false, false);
                warenkorb.setPizza(leerePizza, 0);
                aktualisiereWarenkorb(0);
                System.out.println("Pizzaslot an Stelle 1 gelöscht");
            }
        }
    }//GEN-LAST:event_jp_landing_Cart_pizza_Loeschen1_LabelMouseClicked

    private void jp_landing_Cart_pizza_Loeschen2_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_pizza_Loeschen2_LabelMouseClicked
        // TODO add your handling code here:
        if (!warenkorb.getWarenkorbItems().get(1).getTyp().equals("noch frei")) {
            if (JOptionPane.showConfirmDialog(rootPane, "Willst du diesen Pizzaslot wirklich löschen?", "Pizzaslot 2", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == 0) {
                Pizza leerePizza = new Pizza("noch frei", 0, false, false, false, false, false);
                warenkorb.setPizza(leerePizza, 1);
                aktualisiereWarenkorb(1);
                System.out.println("Pizzaslot an Stelle 2 gelöscht");
            }
        }
    }//GEN-LAST:event_jp_landing_Cart_pizza_Loeschen2_LabelMouseClicked

    private void jp_landing_Cart_pizza_Loeschen3_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_pizza_Loeschen3_LabelMouseClicked
        // TODO add your handling code here:
        if (!warenkorb.getWarenkorbItems().get(2).getTyp().equals("noch frei")) {
            if (JOptionPane.showConfirmDialog(rootPane, "Willst du diesen Pizzaslot wirklich löschen?", "Pizzaslot 3", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == 0) {
                Pizza leerePizza = new Pizza("noch frei", 0, false, false, false, false, false);
                warenkorb.setPizza(leerePizza, 2);
                aktualisiereWarenkorb(2);
                System.out.println("Pizzaslot an Stelle 3 gelöscht");
            }
        }
    }//GEN-LAST:event_jp_landing_Cart_pizza_Loeschen3_LabelMouseClicked

    private void jp_landing_Cart_pizza_Loeschen4_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_pizza_Loeschen4_LabelMouseClicked
        // TODO add your handling code here:
        if (!warenkorb.getWarenkorbItems().get(3).getTyp().equals("noch frei")) {
            if (JOptionPane.showConfirmDialog(rootPane, "Willst du diesen Pizzaslot wirklich löschen?", "Pizzaslot 4", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == 0) {
                Pizza leerePizza = new Pizza("noch frei", 0, false, false, false, false, false);
                warenkorb.setPizza(leerePizza, 3);
                aktualisiereWarenkorb(3);
                System.out.println("Pizzaslot an Stelle 4 gelöscht");
            }
        }
    }//GEN-LAST:event_jp_landing_Cart_pizza_Loeschen4_LabelMouseClicked

    private void cB_Cart_BarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cB_Cart_BarActionPerformed
        // TODO add your handling code here:
        if (!cB_Cart_Bar.isSelected()) {
            jP_landing_Cart_kasse_Order.setBackground(disabled);
        } else {
            jP_landing_Cart_kasse_Order.setBackground(main);
        }
    }//GEN-LAST:event_cB_Cart_BarActionPerformed

    private void lbl_landing_Cart_kasse_Coupon_PrüfenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_landing_Cart_kasse_Coupon_PrüfenMouseClicked
        // TODO add your handling code here:
        jP_landing_Cart_kasse_Coupon_PrüfenMouseClicked(evt);
    }//GEN-LAST:event_lbl_landing_Cart_kasse_Coupon_PrüfenMouseClicked

    private void lbl_landing_Cart_kasse_Coupon_PrüfenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_landing_Cart_kasse_Coupon_PrüfenMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_landing_Cart_kasse_Coupon_PrüfenMouseEntered

    private void jP_landing_Cart_kasse_Coupon_PrüfenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_kasse_Coupon_PrüfenMouseClicked
        // TODO add your handling code here:
        if (pruefeCoupon(txt_landing_kasse_coupon.getText()) != 0 && !couponWarningVerstanden) {
            if (JOptionPane.showConfirmDialog(rootPane, "Beim Verlassen der Kasse müssen Sie ihren Coupon erneut eingeben.", "Coupon", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE) == 0) {
                couponWarningVerstanden = true;
                kassenGesamtBetrag = warenkorbGesamtBetrag - pruefeCoupon(txt_landing_kasse_coupon.getText());
                aktualisiereKasse();
            }
        } else if (pruefeCoupon(txt_landing_kasse_coupon.getText()) != 0) {
            kassenGesamtBetrag = warenkorbGesamtBetrag - pruefeCoupon(txt_landing_kasse_coupon.getText());
            aktualisiereKasse();
        } else {
            JOptionPane.showMessageDialog(rootPane, "Coupon ungültig.", "Coupon", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jP_landing_Cart_kasse_Coupon_PrüfenMouseClicked

    private void jP_landing_Cart_kasse_Coupon_PrüfenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_kasse_Coupon_PrüfenMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jP_landing_Cart_kasse_Coupon_PrüfenMouseExited

    private void jP_landing_Cart_kasse_Coupon_PrüfenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_kasse_Coupon_PrüfenMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jP_landing_Cart_kasse_Coupon_PrüfenMouseEntered

    private void btn_benutzer_ausloggenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_benutzer_ausloggenMouseClicked
        try {
            // TODO add your handling code here:
            angemeldeterBenutzer.abmelden();
        } catch (IOException | URISyntaxException ex) {
            JOptionPane.showMessageDialog(rootPane, "Fehler beim ausloggen. Starte das Programm neu.");
        }
    }//GEN-LAST:event_btn_benutzer_ausloggenMouseClicked

    private void btn_benutzer_ausloggenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_benutzer_ausloggenMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_benutzer_ausloggenMouseExited

    private void btn_benutzer_ausloggenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_benutzer_ausloggenMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_benutzer_ausloggenMouseEntered

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        jP_admin_bestellung_aktualisierenMouseClicked(evt);
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jP_admin_bestellung_aktualisierenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_admin_bestellung_aktualisierenMouseClicked
        // TODO add your handling code here:
        aktualisiereAdmin();
    }//GEN-LAST:event_jP_admin_bestellung_aktualisierenMouseClicked

    private void list_admin_bestellungenValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list_admin_bestellungenValueChanged
        // TODO add your handling code here:
        try {
            int bestellId = Integer.parseInt(list_admin_bestellungen.getSelectedValue().replace("Bestellung ", ""));
            int index = list_admin_bestellungen.getSelectedIndex();
            System.err.println(bestellId);

            if (index >= 0) {
                aktuellAusgewählteBestellungImAdminMenu = alleNochOffenenBestellungen.get(index);

                lbl_landing_admin_bestellinfos.setText("<html><p style=\"text-align:left;\">" + db.getBestellungDetails(bestellId) + "</p></html>");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Fehler bei einer Datenbankabfrage.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Fehler bei einer Dateiabfrage.");
        } catch (NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(rootPane, "Interner Fehler. Starten Sie das Programm neu.");
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(rootPane, "Keine weiteren offenen Bestellungen gefunden.");
        }
    }//GEN-LAST:event_list_admin_bestellungenValueChanged

    private void jp_landing_Cart_pizza_Loeschen1_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_pizza_Loeschen1_LabelMouseEntered
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Loeschen1.setBackground(hover_del);
    }//GEN-LAST:event_jp_landing_Cart_pizza_Loeschen1_LabelMouseEntered

    private void jp_landing_Cart_pizza_Loeschen1_LabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_pizza_Loeschen1_LabelMouseExited
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Loeschen1.setBackground(del);
    }//GEN-LAST:event_jp_landing_Cart_pizza_Loeschen1_LabelMouseExited

    private void jp_landing_Cart_pizza_Loeschen2_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_pizza_Loeschen2_LabelMouseEntered
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Loeschen2.setBackground(hover_del);
    }//GEN-LAST:event_jp_landing_Cart_pizza_Loeschen2_LabelMouseEntered

    private void jp_landing_Cart_pizza_Loeschen2_LabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_pizza_Loeschen2_LabelMouseExited
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Loeschen2.setBackground(del);
    }//GEN-LAST:event_jp_landing_Cart_pizza_Loeschen2_LabelMouseExited

    private void jp_landing_Cart_pizza_Loeschen3_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_pizza_Loeschen3_LabelMouseEntered
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Loeschen3.setBackground(hover_del);
    }//GEN-LAST:event_jp_landing_Cart_pizza_Loeschen3_LabelMouseEntered

    private void jp_landing_Cart_pizza_Loeschen3_LabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_pizza_Loeschen3_LabelMouseExited
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Loeschen3.setBackground(del);
    }//GEN-LAST:event_jp_landing_Cart_pizza_Loeschen3_LabelMouseExited

    private void jp_landing_Cart_pizza_Loeschen4_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_pizza_Loeschen4_LabelMouseEntered
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Loeschen4.setBackground(hover_del);
    }//GEN-LAST:event_jp_landing_Cart_pizza_Loeschen4_LabelMouseEntered

    private void jp_landing_Cart_pizza_Loeschen4_LabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_pizza_Loeschen4_LabelMouseExited
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Loeschen4.setBackground(del);
    }//GEN-LAST:event_jp_landing_Cart_pizza_Loeschen4_LabelMouseExited

    private void jP_landing_Cart_pizza_Bearbeiten1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Bearbeiten1MouseEntered
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten1.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Cart_pizza_Bearbeiten1MouseEntered

    private void jp_landing_Cart_Edit1_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_Edit1_LabelMouseEntered
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten1MouseEntered(evt);
    }//GEN-LAST:event_jp_landing_Cart_Edit1_LabelMouseEntered

    private void jp_landing_Cart_Edit1_LabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_Edit1_LabelMouseExited
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten1.setBackground(main);
    }//GEN-LAST:event_jp_landing_Cart_Edit1_LabelMouseExited

    private void jP_landing_Cart_pizza_Bearbeiten1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Bearbeiten1MouseExited
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten1.setBackground(main);
    }//GEN-LAST:event_jP_landing_Cart_pizza_Bearbeiten1MouseExited

    private void jP_landing_Cart_pizza_Bearbeiten2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Bearbeiten2MouseEntered
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten2.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Cart_pizza_Bearbeiten2MouseEntered

    private void jP_landing_Cart_pizza_Bearbeiten2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Bearbeiten2MouseExited
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten2.setBackground(main);
    }//GEN-LAST:event_jP_landing_Cart_pizza_Bearbeiten2MouseExited

    private void jp_landing_Cart_Edit2_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_Edit2_LabelMouseEntered
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten2MouseEntered(evt);
    }//GEN-LAST:event_jp_landing_Cart_Edit2_LabelMouseEntered

    private void jp_landing_Cart_Edit2_LabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_Edit2_LabelMouseExited
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten2MouseExited(evt);
    }//GEN-LAST:event_jp_landing_Cart_Edit2_LabelMouseExited

    private void jP_landing_Cart_pizza_Bearbeiten3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Bearbeiten3MouseEntered
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten3.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Cart_pizza_Bearbeiten3MouseEntered

    private void jP_landing_Cart_pizza_Bearbeiten3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Bearbeiten3MouseExited
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten3.setBackground(main);
    }//GEN-LAST:event_jP_landing_Cart_pizza_Bearbeiten3MouseExited

    private void jp_landing_Cart_Edit3_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_Edit3_LabelMouseEntered
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten3MouseEntered(evt);
    }//GEN-LAST:event_jp_landing_Cart_Edit3_LabelMouseEntered

    private void jp_landing_Cart_Edit3_LabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_Edit3_LabelMouseExited
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten3MouseExited(evt);
    }//GEN-LAST:event_jp_landing_Cart_Edit3_LabelMouseExited

    private void jP_landing_Cart_pizza_Bearbeiten4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Bearbeiten4MouseEntered
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten4.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Cart_pizza_Bearbeiten4MouseEntered

    private void jP_landing_Cart_pizza_Bearbeiten4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Cart_pizza_Bearbeiten4MouseExited
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten4.setBackground(main);
    }//GEN-LAST:event_jP_landing_Cart_pizza_Bearbeiten4MouseExited

    private void jp_landing_Cart_Edit4_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jp_landing_Cart_Edit4_LabelMouseEntered
        // TODO add your handling code here:
        jP_landing_Cart_pizza_Bearbeiten4MouseEntered(evt);
    }//GEN-LAST:event_jp_landing_Cart_Edit4_LabelMouseEntered

    private void jLabel46MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel46MouseClicked
        // TODO add your handling code here:
        jP_admin_bestellung_checkMouseClicked(evt);
    }//GEN-LAST:event_jLabel46MouseClicked

    private void jP_admin_bestellung_checkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_admin_bestellung_checkMouseClicked
        // TODO add your handling code here:
        if (list_admin_bestellungen.getSelectedIndex() >= 0) {
            aktuellAusgewählteBestellungImAdminMenu.setBestellungAlsFertig(db);
            aktualisiereAdmin();
        } else {
            JOptionPane.showMessageDialog(rootPane, "Wählen Sie zuerst eine Bestellung aus.");
        }
    }//GEN-LAST:event_jP_admin_bestellung_checkMouseClicked

    private void aktualisiereAdmin() {
        System.out.println("Aktualisiere Admin...");
        String query = "SELECT id, benutzer, betrag, coupon, bestelldetails, anmerkung, zeit, fertig FROM bestellung WHERE fertig = 0";
        alleNochOffenenBestellungen = db.queryMehrereBestellungen(query);
        String bestellungen[] = new String[alleNochOffenenBestellungen.size()];

        for (int i = 0; i < alleNochOffenenBestellungen.size(); i++) {
            bestellungen[i] = ("Bestellung " + alleNochOffenenBestellungen.get(i).getId());
        }
        list_admin_bestellungen.setListData(bestellungen);
        lbl_landing_admin_bestellinfos.setText("<html><p style=\"text-align:left;\">Bitte wählen Sie eine Bestellung aus.</p></html>");
    }

    private void aktualisiereWarenkorb(int index) {
        System.out.println("Warenkorb wird aktualisiert...");
        int i = index;
        switch (i) {
            case 0:
                jP_landing_Cart_pizza_Name1.setText(warenkorb.getWarenkorbItems().get(i).getTyp());
                jP_landing_Cart_pizza_Anzahl_nummer1.setText(Integer.toString(warenkorb.getWarenkorbItems().get(i).getAnzahl()) + "x");
                switch (warenkorb.getWarenkorbItems().get(i).getTyp()) {
                    case "Salami":
                        jP_landing_Cart_pizza_Icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_salami.png")));
                        break;
                    case "Speciale":
                        jP_landing_Cart_pizza_Icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_speciale.png")));
                        break;
                    case "Diavolo":
                        jP_landing_Cart_pizza_Icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_diavolo.png")));
                        break;
                    case "Stagioni":
                        jP_landing_Cart_pizza_Icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_quattrostagioni.png")));
                        break;
                    case "Rucula":
                        jP_landing_Cart_pizza_Icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_rucula.png")));
                        break;
                    case "Caprese":
                        jP_landing_Cart_pizza_Icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_caprese.png")));
                        break;
                    case "Funghi":
                        jP_landing_Cart_pizza_Icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_funghi.png")));
                        break;
                    case "Margherita":
                        jP_landing_Cart_pizza_Icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_margherita.png")));
                        break;
                    case "Pepperoni":
                        jP_landing_Cart_pizza_Icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_pepperoni.png")));
                        break;
                    case "noch frei":
                        jP_landing_Cart_pizza_Icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_leer.png")));
                        break;
                    default:
                        JOptionPane.showMessageDialog(rootPane, "Fehler beim aktualisieren des Warenkorbes aufgetreten.");
                }
                break;
            case 1:
                jP_landing_Cart_pizza_Name2.setText(warenkorb.getWarenkorbItems().get(i).getTyp());
                jP_landing_Cart_pizza_Anzahl_nummer2.setText(Integer.toString(warenkorb.getWarenkorbItems().get(i).getAnzahl()) + "x");
                switch (warenkorb.getWarenkorbItems().get(i).getTyp()) {
                    case "Salami":
                        jP_landing_Cart_pizza_Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_salami.png")));
                        break;
                    case "Speciale":
                        jP_landing_Cart_pizza_Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_speciale.png")));
                        break;
                    case "Diavolo":
                        jP_landing_Cart_pizza_Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_diavolo.png")));
                        break;
                    case "Stagioni":
                        jP_landing_Cart_pizza_Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_quattrostagioni.png")));
                        break;
                    case "Rucula":
                        jP_landing_Cart_pizza_Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_rucula.png")));
                        break;
                    case "Caprese":
                        jP_landing_Cart_pizza_Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_caprese.png")));
                        break;
                    case "Funghi":
                        jP_landing_Cart_pizza_Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_funghi.png")));
                        break;
                    case "Margherita":
                        jP_landing_Cart_pizza_Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_margherita.png")));
                        break;
                    case "Pepperoni":
                        jP_landing_Cart_pizza_Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_pepperoni.png")));
                        break;
                    case "noch frei":
                        jP_landing_Cart_pizza_Icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_leer.png")));
                        break;
                    default:
                        JOptionPane.showMessageDialog(rootPane, "Fehler beim aktualisieren des Warenkorbes aufgetreten.");
                }
                break;
            case 2:
                jP_landing_Cart_pizza_Name3.setText(warenkorb.getWarenkorbItems().get(i).getTyp());
                jP_landing_Cart_pizza_Anzahl_nummer3.setText(Integer.toString(warenkorb.getWarenkorbItems().get(i).getAnzahl()) + "x");
                switch (warenkorb.getWarenkorbItems().get(i).getTyp()) {
                    case "Salami":
                        jP_landing_Cart_pizza_Icon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_salami.png")));
                        break;
                    case "Speciale":
                        jP_landing_Cart_pizza_Icon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_speciale.png")));
                        break;
                    case "Diavolo":
                        jP_landing_Cart_pizza_Icon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_diavolo.png")));
                        break;
                    case "Stagioni":
                        jP_landing_Cart_pizza_Icon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_quattrostagioni.png")));
                        break;
                    case "Rucula":
                        jP_landing_Cart_pizza_Icon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_rucula.png")));
                        break;
                    case "Caprese":
                        jP_landing_Cart_pizza_Icon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_caprese.png")));
                        break;
                    case "Funghi":
                        jP_landing_Cart_pizza_Icon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_funghi.png")));
                        break;
                    case "Margherita":
                        jP_landing_Cart_pizza_Icon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_margherita.png")));
                        break;
                    case "Pepperoni":
                        jP_landing_Cart_pizza_Icon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_pepperoni.png")));
                        break;
                    case "noch frei":
                        jP_landing_Cart_pizza_Icon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_leer.png")));
                        break;
                    default:
                        JOptionPane.showMessageDialog(rootPane, "Fehler beim aktualisieren des Warenkorbes aufgetreten.");
                }
                break;
            case 3:
                jP_landing_Cart_pizza_Name4.setText(warenkorb.getWarenkorbItems().get(i).getTyp());
                jP_landing_Cart_pizza_Anzahl_nummer4.setText(Integer.toString(warenkorb.getWarenkorbItems().get(i).getAnzahl()) + "x");
                switch (warenkorb.getWarenkorbItems().get(i).getTyp()) {
                    case "Salami":
                        jP_landing_Cart_pizza_Icon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_salami.png")));
                        break;
                    case "Speciale":
                        jP_landing_Cart_pizza_Icon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_speciale.png")));
                        break;
                    case "Diavolo":
                        jP_landing_Cart_pizza_Icon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_diavolo.png")));
                        break;
                    case "Stagioni":
                        jP_landing_Cart_pizza_Icon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_quattrostagioni.png")));
                        break;
                    case "Rucula":
                        jP_landing_Cart_pizza_Icon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_rucula.png")));
                        break;
                    case "Caprese":
                        jP_landing_Cart_pizza_Icon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_caprese.png")));
                        break;
                    case "Funghi":
                        jP_landing_Cart_pizza_Icon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_funghi.png")));
                        break;
                    case "Margherita":
                        jP_landing_Cart_pizza_Icon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_margherita.png")));
                        break;
                    case "Pepperoni":
                        jP_landing_Cart_pizza_Icon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_pepperoni.png")));
                        break;
                    case "noch frei":
                        jP_landing_Cart_pizza_Icon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_leer.png")));
                        break;
                    default:
                        JOptionPane.showMessageDialog(rootPane, "Fehler beim aktualisieren des Warenkorbes aufgetreten.");
                }
                break;
            default:
                JOptionPane.showMessageDialog(rootPane, "Fehler beim aktualisieren des Warenkorbes aufgetreten.");
        }

        double grundbetrag = 7;
        warenkorbGesamtBetrag = 0;
        for (int j = 0; j < warenkorb.getWarenkorbItems().size(); j++) {
            if (!warenkorb.getWarenkorbItems().get(j).getTyp().equals("noch frei")) {
                int pizzenAnzahl = warenkorb.getWarenkorbItems().get(j).getAnzahl();
                int toppingAnzahl = warenkorb.getWarenkorbItems().get(j).getToppingAnzahl();
                warenkorbGesamtBetrag += (grundbetrag * pizzenAnzahl) + (0.5 * toppingAnzahl);
            }
        }

        System.out.println("Neuer Warenkorbgesamtbetrag: " + warenkorbGesamtBetrag);
        DecimalFormat format = new DecimalFormat("#0.00");
        format.setMaximumFractionDigits(2);
        lbl_landing_Cart_Warenkorbbetrag.setText(format.format(warenkorbGesamtBetrag) + " €");
        kassenGesamtBetrag = warenkorbGesamtBetrag;
        txt_landing_kasse_coupon.setText("");
        aktualisiereKasse();
    }

    private void aktualisiereCartBearbeiten(int warenkorbslot) {
        System.out.println("Bearbeitungsscreen wird aktualisiert...");
        aktuellerWarenkorbIndex = warenkorbslot;
        jp_landing_cart_bearbeiten_title.setText("Pizza " + warenkorb.getWarenkorbItems().get(warenkorbslot).getTyp());
        switch (warenkorb.getWarenkorbItems().get(warenkorbslot).getTyp()) {
            case "Salami":
                jp_landing_cart_bearbeiten_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_salami.png")));
                break;
            case "Speciale":
                jp_landing_cart_bearbeiten_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_speciale.png")));
                break;
            case "Diavolo":
                jp_landing_cart_bearbeiten_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_diavolo.png")));
                break;
            case "Stagioni":
                jp_landing_cart_bearbeiten_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_quattrostagioni.png")));
                break;
            case "Rucula":
                jp_landing_cart_bearbeiten_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_rucula.png")));
                break;
            case "Caprese":
                jp_landing_cart_bearbeiten_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_caprese.png")));
                break;
            case "Funghi":
                jp_landing_cart_bearbeiten_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_funghi.png")));
                break;
            case "Margherita":
                jp_landing_cart_bearbeiten_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_margherita.png")));
                break;
            case "Pepperoni":
                jp_landing_cart_bearbeiten_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pizza_pepperoni.png")));
                break;
            default:
                JOptionPane.showMessageDialog(rootPane, "Fehler beim Aufrufen des Bearbeitungsscreens aufgetreten.");
        }

        cB_bearbeiten_Salami.setSelected(warenkorb.getWarenkorbItems().get(warenkorbslot).isSalami());
        cB_bearbeiten_Rucola.setSelected(warenkorb.getWarenkorbItems().get(warenkorbslot).isRucula());
        cB_bearbeiten_Tomatensosse.setSelected(warenkorb.getWarenkorbItems().get(warenkorbslot).isTomatensoße());
        cB_bearbeiten_Kaese.setSelected(warenkorb.getWarenkorbItems().get(warenkorbslot).isKaese());
        cB_bearbeiten_Zwiebeln.setSelected(warenkorb.getWarenkorbItems().get(warenkorbslot).isZwiebeln());

        bearbeiten = warenkorb.getWarenkorbItems().get(warenkorbslot).getAnzahl();
        jP_landing_Cart_bearbeiten_Counter.setText(Integer.toString(bearbeiten));
    }

    private void aktualisiereKasse() {
        System.out.println("Kasse wird aktualisiert...");
        lbl_landing_Cart_pizza_Adresse.setText("<html><p style=\"text-align:left;\"> Straße: " + angemeldeterBenutzer.getAdresse().getStraße() + " " + angemeldeterBenutzer.getAdresse().getHausnummer() + " <br> Ort: " + angemeldeterBenutzer.getAdresse().getOrt() + " " + angemeldeterBenutzer.getAdresse().getPlz() + " <br> Name: " + angemeldeterBenutzer.getVorname() + " " + angemeldeterBenutzer.getNachname() + " <br> E-Mail: " + angemeldeterBenutzer.getEmail() + " <br> </p></html>");

        DecimalFormat format = new DecimalFormat("#0.00");
        format.setMaximumFractionDigits(2);
        lbl_landing_Cart_kasse_Gesamtbetrag.setText(format.format(kassenGesamtBetrag));
    }

    private void aktualisiereHome() {
        System.out.println("Home wird aktualisiert...");
        Bestellungsinfo letzteBestellung;
        if ((letzteBestellung = db.getLetzteBestellungsinfo(angemeldeterBenutzer)) != null && !letzteBestellung.isFertig()) {
            txt_landing_home_aktuelleBestellung.setText("<html><p style=\"text-align:left;\">" + letzteBestellung.getBestelldetails() + "</p></html>");
        } else {
            txt_landing_home_aktuelleBestellung.setText("<html><p style=\"text-align:left;\">keine Bestellung gefunden</p></html>");
        }
    }

    private void aktualisiereBenutzer() {
        System.out.println("Benutzer wird aktualisiert...");
        txt_user_vorname.setText(angemeldeterBenutzer.getVorname());
        txt_user_nachname.setText(angemeldeterBenutzer.getNachname());
        txt_user_straße.setText(angemeldeterBenutzer.getAdresse().getStraße());
        txt_user_hausnummer.setText(String.valueOf(angemeldeterBenutzer.getAdresse().getHausnummer()));
        txt_user_plz.setText(String.valueOf(angemeldeterBenutzer.getAdresse().getPlz()));
        txt_user_ort.setText(angemeldeterBenutzer.getAdresse().getOrt());
        txt_user_email.setText(angemeldeterBenutzer.getEmail());
        txt_user_passwort.setText(null);

        jP_user_btn.setEnabled(false);
        jP_user_btn.setVisible(false);

        txt_user_vorname.setEditable(false);
        txt_user_nachname.setEditable(false);
        txt_user_straße.setEditable(false);
        txt_user_hausnummer.setEditable(false);
        txt_user_plz.setEditable(false);
        txt_user_ort.setEditable(false);
        txt_user_email.setEditable(false);
        txt_user_passwort.setEditable(false);

        btn_benutzer_ausloggen.setVisible(true);
        btn_benutzer_ausloggen.setEnabled(true);

        txt_user_passwort.setVisible(false);
        txt_user_passwort.setEnabled(false);

        lbl_landing_benutzer_passwort.setVisible(false);
        lbl_landing_benutzer_passwort.setEnabled(false);

        String query = "SELECT id FROM benutzer WHERE email = " + "\"" + angemeldeterBenutzer.getEmail() + "\"";
        angemeldeterBenutzer.setId(Integer.parseInt(db.query(query, 1).get(0).toString()));
    }

    public double pruefeCoupon(String coupon) {
        if (coupon.equals("Pizza5") && couponHinzugefügt == false) {
            return warenkorbGesamtBetrag * 0.05;
        } else {
            return 0;
        }
    }

    public void leereWarenkorb() {
        Pizza leerePizza = new Pizza("noch frei", 0, false, false, false, false, false);
        for (int i = 0; i < warenkorb.getWarenkorbItems().size(); i++) {
            warenkorb.getWarenkorbItems().set(i, leerePizza);
            aktualisiereWarenkorb(i);
        }
        aktualisiereKasse();
    }

    public String kovertiereZuEuro(double i) {
        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2); // max. 2 stellen hinter komma
        n.setMinimumFractionDigits(2); // max. 2 stellen hinter komma
        return n.format(i);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Pizzarando.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pizzarando.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pizzarando.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pizzarando.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pizzarando().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel TEXT9999999;
    private javax.swing.ButtonGroup bG_landing_Cart_kasse_Zahlungsmethoden;
    private javax.swing.JLabel btn_benutzer_ausloggen;
    private javax.swing.JCheckBox cB_Cart_Bar;
    private javax.swing.JCheckBox cB_bearbeiten_Kaese;
    private javax.swing.JCheckBox cB_bearbeiten_Rucola;
    private javax.swing.JCheckBox cB_bearbeiten_Salami;
    private javax.swing.JCheckBox cB_bearbeiten_Tomatensosse;
    private javax.swing.JCheckBox cB_bearbeiten_Zwiebeln;
    private javax.swing.JCheckBox cB_caprese_Kaese;
    private javax.swing.JCheckBox cB_caprese_Rucola;
    private javax.swing.JCheckBox cB_caprese_Salami;
    private javax.swing.JCheckBox cB_caprese_Tomatensosse;
    private javax.swing.JCheckBox cB_caprese_Zwiebeln;
    private javax.swing.JCheckBox cB_diavolo_Kaese;
    private javax.swing.JCheckBox cB_diavolo_Rucola;
    private javax.swing.JCheckBox cB_diavolo_Salami;
    private javax.swing.JCheckBox cB_diavolo_Tomatensosse;
    private javax.swing.JCheckBox cB_diavolo_Zwiebeln;
    private javax.swing.JCheckBox cB_funghi_Kaese;
    private javax.swing.JCheckBox cB_funghi_Rucola;
    private javax.swing.JCheckBox cB_funghi_Salami;
    private javax.swing.JCheckBox cB_funghi_Tomatensosse;
    private javax.swing.JCheckBox cB_funghi_Zwiebeln;
    private javax.swing.JCheckBox cB_login_showPassword;
    private javax.swing.JCheckBox cB_margherita_Kaese;
    private javax.swing.JCheckBox cB_margherita_Rucola;
    private javax.swing.JCheckBox cB_margherita_Salami;
    private javax.swing.JCheckBox cB_margherita_Tomatensosse;
    private javax.swing.JCheckBox cB_margherita_Zwiebeln;
    private javax.swing.JCheckBox cB_pepperoni_Kaese;
    private javax.swing.JCheckBox cB_pepperoni_Rucola;
    private javax.swing.JCheckBox cB_pepperoni_Salami;
    private javax.swing.JCheckBox cB_pepperoni_Tomatensosse;
    private javax.swing.JCheckBox cB_pepperoni_Zwiebeln;
    private javax.swing.JCheckBox cB_rucula_Kaese;
    private javax.swing.JCheckBox cB_rucula_Rucola;
    private javax.swing.JCheckBox cB_rucula_Salami;
    private javax.swing.JCheckBox cB_rucula_Tomatensosse;
    private javax.swing.JCheckBox cB_rucula_Zwiebeln;
    private javax.swing.JCheckBox cB_salami_Kaese;
    private javax.swing.JCheckBox cB_salami_Rucola;
    private javax.swing.JCheckBox cB_salami_Salami;
    private javax.swing.JCheckBox cB_salami_Tomatensosse;
    private javax.swing.JCheckBox cB_salami_Zwiebeln;
    private javax.swing.JCheckBox cB_speciale_Kaese;
    private javax.swing.JCheckBox cB_speciale_Rucola;
    private javax.swing.JCheckBox cB_speciale_Salami;
    private javax.swing.JCheckBox cB_speciale_Tomatensosse;
    private javax.swing.JCheckBox cB_speciale_Zwiebeln;
    private javax.swing.JCheckBox cB_stagioni_Kaese;
    private javax.swing.JCheckBox cB_stagioni_Rucola;
    private javax.swing.JCheckBox cB_stagioni_Salami;
    private javax.swing.JCheckBox cB_stagioni_Tomatensosse;
    private javax.swing.JCheckBox cB_stagioni_Zwiebeln;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jP_Bestellen_caprese;
    private javax.swing.JPanel jP_Bestellen_diavolo;
    private javax.swing.JPanel jP_Bestellen_funghi;
    private javax.swing.JPanel jP_Bestellen_margherita;
    private javax.swing.JPanel jP_Bestellen_pepperoni;
    private javax.swing.JPanel jP_Bestellen_rucula;
    private javax.swing.JPanel jP_Bestellen_salami;
    private javax.swing.JPanel jP_Bestellen_speciale;
    private javax.swing.JPanel jP_Bestellen_stagioni;
    private javax.swing.JPanel jP_admin;
    private javax.swing.JPanel jP_admin_bestellung_aktualisieren;
    private javax.swing.JPanel jP_admin_bestellung_check;
    private javax.swing.JPanel jP_cart;
    private javax.swing.JPanel jP_control;
    private javax.swing.JPanel jP_home;
    private javax.swing.JPanel jP_landing_Admin;
    private javax.swing.JPanel jP_landing_Admin_main;
    private javax.swing.JPanel jP_landing_Benutzer;
    private javax.swing.JPanel jP_landing_Bestellen;
    private javax.swing.JPanel jP_landing_Bestellen_caprese;
    private javax.swing.JPanel jP_landing_Bestellen_caprese_Back;
    private javax.swing.JPanel jP_landing_Bestellen_caprese_Count;
    private javax.swing.JLabel jP_landing_Bestellen_caprese_Counter;
    private javax.swing.JPanel jP_landing_Bestellen_caprese_Minus;
    private javax.swing.JPanel jP_landing_Bestellen_caprese_Plus;
    private javax.swing.JPanel jP_landing_Bestellen_caprese_toCart;
    private javax.swing.JPanel jP_landing_Bestellen_diavolo;
    private javax.swing.JPanel jP_landing_Bestellen_diavolo_Back;
    private javax.swing.JPanel jP_landing_Bestellen_diavolo_Count;
    private javax.swing.JLabel jP_landing_Bestellen_diavolo_Counter;
    private javax.swing.JPanel jP_landing_Bestellen_diavolo_Minus;
    private javax.swing.JPanel jP_landing_Bestellen_diavolo_Plus;
    private javax.swing.JPanel jP_landing_Bestellen_diavolo_toCart;
    private javax.swing.JPanel jP_landing_Bestellen_funghi;
    private javax.swing.JPanel jP_landing_Bestellen_funghi_Back;
    private javax.swing.JPanel jP_landing_Bestellen_funghi_Count;
    private javax.swing.JLabel jP_landing_Bestellen_funghi_Counter;
    private javax.swing.JPanel jP_landing_Bestellen_funghi_Minus;
    private javax.swing.JPanel jP_landing_Bestellen_funghi_Plus;
    private javax.swing.JPanel jP_landing_Bestellen_funghi_toCart;
    private javax.swing.JPanel jP_landing_Bestellen_home;
    private javax.swing.JPanel jP_landing_Bestellen_margherita;
    private javax.swing.JPanel jP_landing_Bestellen_margherita_Back;
    private javax.swing.JPanel jP_landing_Bestellen_margherita_Count;
    private javax.swing.JLabel jP_landing_Bestellen_margherita_Counter;
    private javax.swing.JPanel jP_landing_Bestellen_margherita_Minus;
    private javax.swing.JPanel jP_landing_Bestellen_margherita_Plus;
    private javax.swing.JPanel jP_landing_Bestellen_margherita_toCart;
    private javax.swing.JPanel jP_landing_Bestellen_pepperoni;
    private javax.swing.JLabel jP_landing_Bestellen_pepperoni_Counter;
    private javax.swing.JPanel jP_landing_Bestellen_rucula;
    private javax.swing.JPanel jP_landing_Bestellen_rucula_Back;
    private javax.swing.JPanel jP_landing_Bestellen_rucula_Count;
    private javax.swing.JLabel jP_landing_Bestellen_rucula_Counter;
    private javax.swing.JPanel jP_landing_Bestellen_rucula_Minus;
    private javax.swing.JPanel jP_landing_Bestellen_rucula_Plus;
    private javax.swing.JPanel jP_landing_Bestellen_rucula_toCart;
    private javax.swing.JPanel jP_landing_Bestellen_salami;
    private javax.swing.JPanel jP_landing_Bestellen_salami_Back;
    private javax.swing.JPanel jP_landing_Bestellen_salami_Count;
    private javax.swing.JLabel jP_landing_Bestellen_salami_Counter;
    private javax.swing.JPanel jP_landing_Bestellen_salami_Minus;
    private javax.swing.JPanel jP_landing_Bestellen_salami_Plus;
    private javax.swing.JPanel jP_landing_Bestellen_salami_toCart;
    private javax.swing.JPanel jP_landing_Bestellen_speciale;
    private javax.swing.JPanel jP_landing_Bestellen_speciale_Back;
    private javax.swing.JPanel jP_landing_Bestellen_speciale_Count;
    private javax.swing.JLabel jP_landing_Bestellen_speciale_Counter;
    private javax.swing.JPanel jP_landing_Bestellen_speciale_Minus;
    private javax.swing.JPanel jP_landing_Bestellen_speciale_Plus;
    private javax.swing.JPanel jP_landing_Bestellen_speciale_toCart;
    private javax.swing.JPanel jP_landing_Bestellen_stagioni;
    private javax.swing.JPanel jP_landing_Bestellen_stagioni_Back;
    private javax.swing.JPanel jP_landing_Bestellen_stagioni_Count;
    private javax.swing.JLabel jP_landing_Bestellen_stagioni_Counter;
    private javax.swing.JPanel jP_landing_Bestellen_stagioni_Minus;
    private javax.swing.JPanel jP_landing_Bestellen_stagioni_Plus;
    private javax.swing.JPanel jP_landing_Bestellen_stagioni_toCart;
    private javax.swing.JPanel jP_landing_Cart;
    private javax.swing.JPanel jP_landing_Cart_bearbeiten;
    private javax.swing.JPanel jP_landing_Cart_bearbeiten_Back;
    private javax.swing.JPanel jP_landing_Cart_bearbeiten_Count;
    private javax.swing.JLabel jP_landing_Cart_bearbeiten_Counter;
    private javax.swing.JPanel jP_landing_Cart_bearbeiten_Minus;
    private javax.swing.JPanel jP_landing_Cart_bearbeiten_Plus;
    private javax.swing.JPanel jP_landing_Cart_bearbeiten_toCart;
    private javax.swing.JPanel jP_landing_Cart_kasse;
    private javax.swing.JPanel jP_landing_Cart_kasse_Adresse;
    private javax.swing.JPanel jP_landing_Cart_kasse_Back;
    private javax.swing.JPanel jP_landing_Cart_kasse_Coupon_Prüfen;
    private javax.swing.JPanel jP_landing_Cart_kasse_Order;
    private javax.swing.JPanel jP_landing_Cart_kasse_main;
    private javax.swing.JPanel jP_landing_Cart_main;
    private javax.swing.JPanel jP_landing_Cart_ordered;
    private javax.swing.JPanel jP_landing_Cart_ordermore;
    private javax.swing.JPanel jP_landing_Cart_pizza1;
    private javax.swing.JPanel jP_landing_Cart_pizza2;
    private javax.swing.JPanel jP_landing_Cart_pizza3;
    private javax.swing.JPanel jP_landing_Cart_pizza4;
    private javax.swing.JPanel jP_landing_Cart_pizza_Anzahl1;
    private javax.swing.JPanel jP_landing_Cart_pizza_Anzahl2;
    private javax.swing.JPanel jP_landing_Cart_pizza_Anzahl3;
    private javax.swing.JPanel jP_landing_Cart_pizza_Anzahl4;
    private javax.swing.JLabel jP_landing_Cart_pizza_Anzahl_nummer1;
    private javax.swing.JLabel jP_landing_Cart_pizza_Anzahl_nummer2;
    private javax.swing.JLabel jP_landing_Cart_pizza_Anzahl_nummer3;
    private javax.swing.JLabel jP_landing_Cart_pizza_Anzahl_nummer4;
    private javax.swing.JPanel jP_landing_Cart_pizza_Bearbeiten1;
    private javax.swing.JPanel jP_landing_Cart_pizza_Bearbeiten2;
    private javax.swing.JPanel jP_landing_Cart_pizza_Bearbeiten3;
    private javax.swing.JPanel jP_landing_Cart_pizza_Bearbeiten4;
    private javax.swing.JLabel jP_landing_Cart_pizza_Icon1;
    private javax.swing.JLabel jP_landing_Cart_pizza_Icon2;
    private javax.swing.JLabel jP_landing_Cart_pizza_Icon3;
    private javax.swing.JLabel jP_landing_Cart_pizza_Icon4;
    private javax.swing.JPanel jP_landing_Cart_pizza_Loeschen1;
    private javax.swing.JPanel jP_landing_Cart_pizza_Loeschen2;
    private javax.swing.JPanel jP_landing_Cart_pizza_Loeschen3;
    private javax.swing.JPanel jP_landing_Cart_pizza_Loeschen4;
    private javax.swing.JLabel jP_landing_Cart_pizza_Name1;
    private javax.swing.JLabel jP_landing_Cart_pizza_Name2;
    private javax.swing.JLabel jP_landing_Cart_pizza_Name3;
    private javax.swing.JLabel jP_landing_Cart_pizza_Name4;
    private javax.swing.JPanel jP_landing_Cart_pizza_zurKasse;
    private javax.swing.JPanel jP_landing_Home;
    private javax.swing.JPanel jP_landing_Home_login;
    private javax.swing.JPanel jP_landing_Home_main;
    private javax.swing.JPanel jP_landing_pepperoni_salami_Back;
    private javax.swing.JPanel jP_landing_pepperoni_salami_Count;
    private javax.swing.JPanel jP_landing_pepperoni_salami_Minus;
    private javax.swing.JPanel jP_landing_pepperoni_salami_Plus;
    private javax.swing.JPanel jP_landing_pepperoni_salami_toCart;
    private javax.swing.JPanel jP_login_btn;
    private javax.swing.JPanel jP_main;
    private javax.swing.JPanel jP_order;
    private javax.swing.JPanel jP_user;
    private javax.swing.JPanel jP_user_btn;
    private javax.swing.JPanel jP_user_login;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jp_landing_Bestellen_caprese_toCart_Label;
    private javax.swing.JLabel jp_landing_Bestellen_diavolo_toCart_Label;
    private javax.swing.JLabel jp_landing_Bestellen_funghi_toCart_Label;
    private javax.swing.JLabel jp_landing_Bestellen_margherita_toCart_Label;
    private javax.swing.JLabel jp_landing_Bestellen_pepperoni_toCart_Label;
    private javax.swing.JLabel jp_landing_Bestellen_rucula_toCart_Label;
    private javax.swing.JLabel jp_landing_Bestellen_salami_toCart_Label;
    private javax.swing.JLabel jp_landing_Bestellen_speciale_toCart_Label;
    private javax.swing.JLabel jp_landing_Bestellen_stagioni_toCart_Label;
    private javax.swing.JLabel jp_landing_Cart_Edit1_Label;
    private javax.swing.JLabel jp_landing_Cart_Edit2_Label;
    private javax.swing.JLabel jp_landing_Cart_Edit3_Label;
    private javax.swing.JLabel jp_landing_Cart_Edit4_Label;
    private javax.swing.JLabel jp_landing_Cart_pizza_Loeschen1_Label;
    private javax.swing.JLabel jp_landing_Cart_pizza_Loeschen2_Label;
    private javax.swing.JLabel jp_landing_Cart_pizza_Loeschen3_Label;
    private javax.swing.JLabel jp_landing_Cart_pizza_Loeschen4_Label;
    private javax.swing.JLabel jp_landing_cart_bearbeiten_icon;
    private javax.swing.JLabel jp_landing_cart_bearbeiten_title;
    private javax.swing.JLabel lbl_for_kassegesamt;
    private javax.swing.JLabel lbl_landing_Cart_Warenkorbbetrag;
    private javax.swing.JLabel lbl_landing_Cart_kasse_Coupon_Prüfen;
    private javax.swing.JLabel lbl_landing_Cart_kasse_Gesamtbetrag;
    private javax.swing.JLabel lbl_landing_Cart_pizza_Adresse;
    private javax.swing.JLabel lbl_landing_admin_bestellinfos;
    private javax.swing.JLabel lbl_landing_benutzer_passwort;
    private javax.swing.JList<String> list_admin_bestellungen;
    private javax.swing.JTextArea txa_landing_kasse_Extra;
    private javax.swing.JLabel txt_Benutzer;
    private javax.swing.JLabel txt_Home;
    private javax.swing.JScrollPane txt_admin_Infos_Bestellung;
    private javax.swing.JLabel txt_landing_home_aktuelleBestellung;
    private javax.swing.JTextField txt_landing_kasse_coupon;
    private javax.swing.JTextField txt_login_email;
    private javax.swing.JLabel txt_login_gotoRegister;
    private javax.swing.JPasswordField txt_login_passwort;
    private javax.swing.JTextField txt_user_email;
    private javax.swing.JTextField txt_user_hausnummer;
    private javax.swing.JTextField txt_user_nachname;
    private javax.swing.JTextField txt_user_ort;
    private javax.swing.JPasswordField txt_user_passwort;
    private javax.swing.JTextField txt_user_plz;
    private javax.swing.JTextField txt_user_straße;
    private javax.swing.JTextField txt_user_vorname;
    // End of variables declaration//GEN-END:variables

}
