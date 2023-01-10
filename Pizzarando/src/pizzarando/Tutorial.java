/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pizzarando;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author dominikknaup
 */
public class Tutorial extends javax.swing.JFrame {

    Color hover_main = new Color(255, 164, 92);
    Color main = new Color(253, 150, 68);

    public Tutorial() {
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel135 = new javax.swing.JLabel();
        lbl_landing_Tutorial_Text = new javax.swing.JLabel();
        jP_landing_Tutorial_verstanden = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Tutorial"); // NOI18N
        setResizable(false);

        jLabel135.setBackground(new java.awt.Color(0, 0, 0));
        jLabel135.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel135.setForeground(new java.awt.Color(250, 130, 49));
        jLabel135.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel135.setText("Tutorial Pizzarando");

        lbl_landing_Tutorial_Text.setBackground(new java.awt.Color(0, 0, 0));
        lbl_landing_Tutorial_Text.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        lbl_landing_Tutorial_Text.setForeground(new java.awt.Color(125, 125, 125));
        lbl_landing_Tutorial_Text.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_landing_Tutorial_Text.setText("<html><p style=\"text-align:center;\"> Jede Pizza kostet standartmäßig 7€.<br>Je nach Pizzasorte sind unterschiedliche Toppings ausgewählt, welche zusätzlich verändert werden können.<br>Jedes Topping kostet extra 0,50€. Der Preis je Pizza wird also mit 7€ + Anzahl ausgewählter Toppings x 0,50€ berechnet. Aufgrund der Bestellmenge haben Sie 4 Pizzaslots zur Verfügung</p></html>");

        jP_landing_Tutorial_verstanden.setBackground(new java.awt.Color(253, 150, 68));
        jP_landing_Tutorial_verstanden.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jP_landing_Tutorial_verstanden.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP_landing_Tutorial_verstandenMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jP_landing_Tutorial_verstandenMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jP_landing_Tutorial_verstandenMouseEntered(evt);
            }
        });

        jLabel71.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(255, 255, 255));
        jLabel71.setText("Verstanden!");
        jLabel71.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel71MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel71MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel71MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jP_landing_Tutorial_verstandenLayout = new javax.swing.GroupLayout(jP_landing_Tutorial_verstanden);
        jP_landing_Tutorial_verstanden.setLayout(jP_landing_Tutorial_verstandenLayout);
        jP_landing_Tutorial_verstandenLayout.setHorizontalGroup(
            jP_landing_Tutorial_verstandenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP_landing_Tutorial_verstandenLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jP_landing_Tutorial_verstandenLayout.setVerticalGroup(
            jP_landing_Tutorial_verstandenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP_landing_Tutorial_verstandenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel135)
                        .addGap(59, 59, 59))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jP_landing_Tutorial_verstanden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lbl_landing_Tutorial_Text, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel135, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 179, Short.MAX_VALUE)
                .addComponent(jP_landing_Tutorial_verstanden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(75, 75, 75)
                    .addComponent(lbl_landing_Tutorial_Text, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(47, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel71MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel71MouseClicked
        // TODO add your handling code here
        jP_landing_Tutorial_verstandenMouseClicked(evt);
    }//GEN-LAST:event_jLabel71MouseClicked

    private void jLabel71MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel71MouseEntered
        // TODO add your handling code here:
        jP_landing_Tutorial_verstandenMouseEntered(evt);
    }//GEN-LAST:event_jLabel71MouseEntered

    private void jLabel71MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel71MouseExited
        // TODO add your handling code here:
        jP_landing_Tutorial_verstandenMouseExited(evt);
    }//GEN-LAST:event_jLabel71MouseExited

    private void jP_landing_Tutorial_verstandenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Tutorial_verstandenMouseClicked
        // TODO add your handling code here:
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_jP_landing_Tutorial_verstandenMouseClicked

    private void jP_landing_Tutorial_verstandenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Tutorial_verstandenMouseEntered
        // TODO add your handling code here:
        jP_landing_Tutorial_verstanden.setBackground(hover_main);
    }//GEN-LAST:event_jP_landing_Tutorial_verstandenMouseEntered

    private void jP_landing_Tutorial_verstandenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP_landing_Tutorial_verstandenMouseExited
        // TODO add your handling code here:
        jP_landing_Tutorial_verstanden.setBackground(main);
    }//GEN-LAST:event_jP_landing_Tutorial_verstandenMouseExited

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
            java.util.logging.Logger.getLogger(Tutorial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tutorial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tutorial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tutorial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tutorial().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JPanel jP_landing_Cart_kasse_Back;
    private javax.swing.JPanel jP_landing_Cart_kasse_Back1;
    private javax.swing.JPanel jP_landing_Tutorial_verstanden;
    private javax.swing.JLabel lbl_landing_Tutorial_Text;
    // End of variables declaration//GEN-END:variables
}