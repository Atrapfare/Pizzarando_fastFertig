/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzarando;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author dominikknaup
 */
public class Passwort {
    private String hashedPasswort;

    public Passwort(String passwort, boolean hash) throws NoSuchAlgorithmException {
        if (hash) {
            this.hashedPasswort = hash(passwort);
        } else {
            this.hashedPasswort = passwort;
        }
    }

    public String getHashedPasswort() {
        return hashedPasswort;
    }

    public void setHashedPasswort(String hashedPasswort) {
        this.hashedPasswort = hashedPasswort;
    }
    
    public String hash(String passwort) throws NoSuchAlgorithmException {
        // Create MessageDigest instance for MD5
        MessageDigest md = MessageDigest.getInstance("MD5");

        // Add password bytes to digest
        md.update(passwort.getBytes());

        // Get the hash's bytes
        byte[] bytes = md.digest();

        // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        // Get complete hashed password in hex format
        return sb.toString();
    }
}
