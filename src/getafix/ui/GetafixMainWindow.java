package getafix.ui;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.UIManager;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class GetafixMainWindow extends javax.swing.JFrame {

    /**
     * Creates new form GetafixMainWindow
     */
    public GetafixMainWindow() {
        initComponents();
        setLocationRelativeTo(null);
        List<Image> icons = new ArrayList<Image>();
        try{
            icons.add(ImageIO.read(GetafixMainWindow.class.getResource("/getafix/ui/resources/icon_16x16.png")));
            icons.add(ImageIO.read(GetafixMainWindow.class.getResource("/getafix/ui/resources/icon_24x24.png")));
            icons.add(ImageIO.read(GetafixMainWindow.class.getResource("/getafix/ui/resources/icon_32x32.png")));
            icons.add(ImageIO.read(GetafixMainWindow.class.getResource("/getafix/ui/resources/icon_48x48.png")));
            icons.add(ImageIO.read(GetafixMainWindow.class.getResource("/getafix/ui/resources/icon_64x64.png")));
            icons.add(ImageIO.read(GetafixMainWindow.class.getResource("/getafix/ui/resources/icon_96x96.png")));
            icons.add(ImageIO.read(GetafixMainWindow.class.getResource("/getafix/ui/resources/icon_128x128.png")));
            icons.add(ImageIO.read(GetafixMainWindow.class.getResource("/getafix/ui/resources/icon_256x256.png")));
            setIconImages(icons);
        }catch(IOException ioe){
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        getafixMainPanel1 = new getafix.ui.GetafixMainPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Getafix");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(getafixMainPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(getafixMainPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GetafixMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GetafixMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GetafixMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GetafixMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new GetafixMainWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private getafix.ui.GetafixMainPanel getafixMainPanel1;
    // End of variables declaration//GEN-END:variables
}