/*
    This file is part of Numbermat: Math Problem Generator.
    Copyright © 2014 Valdemar Svabensky

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package cz.muni.fi.Numbermat.GUI;

import cz.muni.fi.Numbermat.Utils;
import java.awt.event.WindowEvent;
import java.util.Arrays;

/**
 * GUI window opened from MainFrame to display help.
 * 
 * @author Valdemar Svabensky <395868(at)mail(dot)muni(dot)cz>
 */
public class HelpFrame extends javax.swing.JFrame {
    
    /**
     * Creates new form HelpFrame.
     */
    public HelpFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inputFormatLabel = new javax.swing.JLabel();
        inputFormatLabelSeparator1 = new javax.swing.JSeparator();
        inputFormatLabelSeparator2 = new javax.swing.JSeparator();
        problemTypeSelection = new javax.swing.JComboBox();
        helpScrollPane = new javax.swing.JScrollPane();
        helpTextPane = new javax.swing.JTextPane();
        verticalSeparator = new javax.swing.JSeparator();
        mnemonicKeysLabel = new javax.swing.JLabel();
        mnemonicKeysLabelSeparator1 = new javax.swing.JSeparator();
        mnemonicKeysLabelSeparator2 = new javax.swing.JSeparator();
        mnemonicKeysTable1 = new javax.swing.JLabel();
        mnemonicKeysTable2 = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        copyrightLabel = new javax.swing.JLabel();
        licenseLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Numbermat: Nápověda");
        setAlwaysOnTop(true);
        setName("HelpFrame"); // NOI18N
        setPreferredSize(new java.awt.Dimension(750, 420));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        inputFormatLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        inputFormatLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inputFormatLabel.setText("POPIS PŘÍKLADŮ");
        inputFormatLabel.setPreferredSize(new java.awt.Dimension(120, 14));
        getContentPane().add(inputFormatLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 24, -1, -1));

        inputFormatLabelSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        inputFormatLabelSeparator1.setPreferredSize(new java.awt.Dimension(80, 2));
        getContentPane().add(inputFormatLabelSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 30, -1, -1));

        inputFormatLabelSeparator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        inputFormatLabelSeparator2.setPreferredSize(new java.awt.Dimension(80, 2));
        getContentPane().add(inputFormatLabelSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, -1, -1));

        problemTypeSelection.setMaximumRowCount(15);
        problemTypeSelection.setModel(new javax.swing.DefaultComboBoxModel<>(cz.muni.fi.Numbermat.GUI.Config.PROBLEMS));
        problemTypeSelection.setPreferredSize(new java.awt.Dimension(275, 30));
        problemTypeSelection.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                problemTypeSelectionItemStateChanged(evt);
            }
        });
        getContentPane().add(problemTypeSelection, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 52, -1, -1));

        helpScrollPane.setPreferredSize(new java.awt.Dimension(275, 220));

        helpTextPane.setEditable(false);
        helpTextPane.setContentType("text/html"); // NOI18N
        helpTextPane.setText(generateHelp(0));
        helpScrollPane.setViewportView(helpTextPane);

        getContentPane().add(helpScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 98, -1, -1));

        verticalSeparator.setOrientation(javax.swing.SwingConstants.VERTICAL);
        verticalSeparator.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        verticalSeparator.setPreferredSize(new java.awt.Dimension(2, 360));
        getContentPane().add(verticalSeparator, new org.netbeans.lib.awtextra.AbsoluteConstraints(315, 10, -1, -1));

        mnemonicKeysLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        mnemonicKeysLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mnemonicKeysLabel.setText("KLÁVESOVÉ ZKRATKY");
        mnemonicKeysLabel.setPreferredSize(new java.awt.Dimension(120, 14));
        getContentPane().add(mnemonicKeysLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 24, -1, -1));

        mnemonicKeysLabelSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        mnemonicKeysLabelSeparator1.setPreferredSize(new java.awt.Dimension(125, 2));
        getContentPane().add(mnemonicKeysLabelSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(333, 30, -1, -1));

        mnemonicKeysLabelSeparator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        mnemonicKeysLabelSeparator2.setPreferredSize(new java.awt.Dimension(125, 2));
        getContentPane().add(mnemonicKeysLabelSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(595, 30, -1, -1));

        mnemonicKeysTable1.setText("<html>\n<style type=\"text/css\">\ntable.sample {\n\tborder-width: 1px;\n\tborder-spacing: 0px;\n\tborder-style: none;\n\tborder-collapse: collapse;\n}\ntable.sample td {\n\tborder-width: 1px;\n\tpadding: 4px;\n\tborder-style: solid;\n\tborder-color: black;\n}\n</style>\n\n<table class=\"sample\">\n<tr>\n  <td>Lehká obtížnost</td>\n  <td>Alt + L</td> \n</tr>\n<tr>\n  <td>Střední obtížnost</td>\n  <td>Alt + S</td> \n</tr>\n<tr>\n  <td>Těžká obtížnost</td>\n  <td>Alt + T</td> \n</tr>\n<tr>\n  <td>Generovat náhodně</td>\n  <td>Alt + G</td> \n</tr>\n<tr>\n  <td>Nastavit parametry</td>\n  <td>Alt + N</td> \n</tr>\n<tr>\n  <td>Generovat</td>\n  <td>Alt + V</td> \n</tr>\n<tr>\n  <td>Nastavení</td>\n  <td>Alt + A</td> \n</tr>\n<tr>\n  <td>Nápověda</td>\n  <td>Alt + H</td> \n</tr>\n</table>\n</html>");
        mnemonicKeysTable1.setPreferredSize(new java.awt.Dimension(180, 225));
        getContentPane().add(mnemonicKeysTable1, new org.netbeans.lib.awtextra.AbsoluteConstraints(338, 54, -1, -1));
        mnemonicKeysTable1.getAccessibleContext().setAccessibleName("<html>\n<style type=\"text/css\">\ntable.sample {\n\tborder-width: 1px;\n\tborder-spacing: 0px;\n\tborder-style: none;\n\tborder-collapse: collapse;\n}\ntable.sample td {\n\tborder-width: 2px;\n\tpadding: 4px;\n\tborder-style: solid;\n\tborder-color: black;\n}\n</style>\n\n<table class=\"sample\">\n<tr>\n  <td>Lehká obtížnost</td>\n  <td>ALT+L</td> \n</tr>\n<tr>\n  <td>Střední obtížnost</td>\n  <td>ALT+S</td> \n</tr>\n<tr>\n  <td>Těžká obtížnost</td>\n  <td>ALT+T</td> \n</tr>\n<tr>\n  <td>Generovat náhodně</td>\n  <td>ALT+G</td> \n</tr>\n<tr>\n  <td>Nastavit parametry</td>\n  <td>ALT+N</td> \n</tr>\n<tr>\n  <td>Generovat</td>\n  <td>ALT+V</td> \n</tr>\n<tr>\n  <td>Nastavení</td>\n  <td>ALT+A</td> \n</tr>\n<tr>\n  <td>Nápověda</td>\n  <td>ALT+H</td> \n</tr>\n</table>\n</html>");

        mnemonicKeysTable2.setText("<html>\n<style type=\"text/css\">\ntable.sample {\n\tborder-width: 1px;\n\tborder-spacing: 0px;\n\tborder-style: none;\n\tborder-collapse: collapse;\n}\ntable.sample td {\n\tborder-width: 1px;\n\tpadding: 4px;\n\tborder-style: solid;\n\tborder-color: black;\n}\n</style>\n\n<table class=\"sample\">\n<tr>\n  <td>Zkontrolovat</td>\n  <td>Enter</td> \n</tr>\n<tr>\n  <td>Zobrazit řešení (LaTeX)</td>\n  <td>Alt + X</td> \n</tr>\n<tr>\n  <td>Zobrazit řešení (text)</td>\n  <td>Alt + B</td> \n</tr>\n<tr>\n  <td>Kopírovat řešení</td>\n  <td>Alt + K</td> \n</tr>\n<tr>\n  <td>Export do PDF</td>\n  <td>Alt + E</td> \n</tr>\n<tr>\n  <td>Restart</td>\n  <td>Alt + R</td> \n</tr>\n</table>\n</html>");
        mnemonicKeysTable2.setPreferredSize(new java.awt.Dimension(190, 169));
        getContentPane().add(mnemonicKeysTable2, new org.netbeans.lib.awtextra.AbsoluteConstraints(525, 54, -1, -1));

        okButton.setText("OK");
        okButton.setPreferredSize(new java.awt.Dimension(80, 30));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        getContentPane().add(okButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(635, 285, -1, -1));

        copyrightLabel.setText("Copyright © 2014 Valdemar Švábenský");
        copyrightLabel.setPreferredSize(new java.awt.Dimension(250, 14));
        getContentPane().add(copyrightLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 330, -1, -1));

        licenseLabel.setText("Licencováno pod podmínkami GNU GPLv3.");
        licenseLabel.setPreferredSize(new java.awt.Dimension(250, 14));
        getContentPane().add(licenseLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 350, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void problemTypeSelectionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_problemTypeSelectionItemStateChanged
        final int problemIndex = getSelectedProblemIndex();
        helpTextPane.setText(generateHelp(problemIndex));
    }//GEN-LAST:event_problemTypeSelectionItemStateChanged

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_okButtonActionPerformed

    private static String generateHelp(final int problemIndex) {
        final String lineBreak = "<br/>" + Utils.NEWLINE;
        final StringBuilder help = new StringBuilder(256);
        
        help.append("<b>Vstup:</b>").append(lineBreak);
        final String[] paramDescriptions = Config.PARAMS[problemIndex];
        final int numberOfParams = paramDescriptions.length;
        for (int i = 0; i < numberOfParams; ++i) {
            help.append(i + 1).append(".) ").append(paramDescriptions[i]);
            if (!Utils.lastForCycle(i, numberOfParams))
                help.append(",");
            else
                help.append(".");
            help.append(lineBreak);
        }
        help.append("<b>Výstup:</b>").append(lineBreak);
        help.append(Config.RESULTS[problemIndex]).append(lineBreak);
        help.append("<b>Formát výsledku:</b>").append(lineBreak);
        help.append(Config.RESULT_FORMATS[problemIndex]);
        return help.toString();
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
            java.util.logging.Logger.getLogger(HelpFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HelpFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HelpFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HelpFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HelpFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel copyrightLabel;
    private javax.swing.JScrollPane helpScrollPane;
    private javax.swing.JTextPane helpTextPane;
    private javax.swing.JLabel inputFormatLabel;
    private javax.swing.JSeparator inputFormatLabelSeparator1;
    private javax.swing.JSeparator inputFormatLabelSeparator2;
    private javax.swing.JLabel licenseLabel;
    private javax.swing.JLabel mnemonicKeysLabel;
    private javax.swing.JSeparator mnemonicKeysLabelSeparator1;
    private javax.swing.JSeparator mnemonicKeysLabelSeparator2;
    private javax.swing.JLabel mnemonicKeysTable1;
    private javax.swing.JLabel mnemonicKeysTable2;
    private javax.swing.JButton okButton;
    private javax.swing.JComboBox problemTypeSelection;
    private javax.swing.JSeparator verticalSeparator;
    // End of variables declaration//GEN-END:variables

    private int getSelectedProblemIndex() {
        final String selectedProblem = problemTypeSelection.getSelectedItem().toString();
        return Arrays.asList(Config.PROBLEMS).indexOf(selectedProblem); 
    }
}
