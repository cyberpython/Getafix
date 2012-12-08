package getafix.ui;

import getafix.K12TextFileParser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class TransmissionInProgressDialog extends javax.swing.JDialog {

    private File inputFile;
    private InetAddress dest;
    private int destPort;
    private int offset;
    private int delay;

    /**
     * Creates new form TransmissionInProgressDialog
     */
    public TransmissionInProgressDialog(java.awt.Frame parent, boolean modal, File inputFile, InetAddress dest, int destPort, int offset, int delay) {
        super(parent, modal);
        this.inputFile = inputFile;
        this.dest = dest;
        this.destPort = destPort;
        this.offset = offset;
        this.delay = delay;

        initComponents();
        setLocationRelativeTo(parent);
        setupProgressBar();
    }

    private void setupProgressBar() {
        jProgressBar1.setString("Status: Counting packets to send...");
        jProgressBar1.setMinimum(0);
        jProgressBar1.setMaximum(100);
        jProgressBar1.setValue(0);
        jProgressBar1.setIndeterminate(true);

        SwingWorker<Integer, Void> w = new SwingWorker<Integer, Void>() {

            @Override
            protected Integer doInBackground() throws Exception {
                int numberOfPackets = 0;
                try {
                    K12TextFileParser parser = new K12TextFileParser(inputFile, offset);
                    while (parser.getNextPacketBytes() != null) {
                        numberOfPackets++;
                    }
                    return numberOfPackets;
                } catch (FileNotFoundException fnfe) {
                    numberOfPackets = -1;
                } catch (IOException ioe) {
                    numberOfPackets = -2;
                }
                return new Integer(numberOfPackets);
            }

            @Override
            public void done() {
                jProgressBar1.setIndeterminate(false);
                try {
                    int numberOfPackets = get();
                    if (numberOfPackets >= 0) {
                        jProgressBar1.setMaximum(numberOfPackets);
                        jProgressBar1.setString("Ready");
                        transmit();
                    } else if (numberOfPackets == -1) {
                        jProgressBar1.setString("Error: File not found!");
                        jButton1.setEnabled(true);
                    } else if (numberOfPackets == -2) {
                        jProgressBar1.setString("I/O Error!");
                        jButton1.setEnabled(true);
                    }
                } catch (InterruptedException ie) {
                } catch (java.util.concurrent.ExecutionException e) {
                    String why;
                    Throwable cause = e.getCause();
                    if (cause != null) {
                        why = cause.getMessage();
                    } else {
                        why = e.getMessage();
                    }
                    System.err.println("Error: " + why);
                    jProgressBar1.setString("Error!");
                    jButton1.setEnabled(true);
                }
            }
        };

        w.execute();
    }

    private void transmit() {
        jProgressBar1.setString("Status: Transmitting...");
        jProgressBar1.setValue(0);
        
        final SwingWorker<TransmissionResult, TransmissionResult> w = new SwingWorker<TransmissionResult, TransmissionResult>() {

            @Override
            protected TransmissionResult doInBackground() throws Exception {
                TransmissionResult result = null;
                long currentTime;
                long totalDelay = 0;
                long totalTime = 0;
                long totalPayloadSent = 0;
                int totalPacketsSent = 0;
                double averagePayloadPerPacket = 0.0d;
                double averageTimePerPacket = 0.0d;
                try{
                    DatagramSocket sock = new DatagramSocket();
                    K12TextFileParser sfp = new K12TextFileParser(inputFile, offset);
                    byte[] bytes;
                    long startTime = System.currentTimeMillis();
                    while( ((bytes=sfp.getNextPacketBytes())!=null) && (!isCancelled()) ){
                        DatagramPacket packet = new DatagramPacket(bytes,bytes.length, dest, destPort);
                        sock.send(packet);
                        totalPayloadSent += bytes.length;
                        totalPacketsSent++;
                        currentTime = System.currentTimeMillis();
                        try{
                            Thread.sleep(delay);

                        }catch(InterruptedException ie){
                        }
                        totalDelay += System.currentTimeMillis()-currentTime;
                        totalTime = System.currentTimeMillis() - startTime;
                        if(totalPacketsSent>0){
                            averagePayloadPerPacket = ((double)totalPayloadSent) / totalPacketsSent;
                            averageTimePerPacket = ((double)System.currentTimeMillis()-startTime-totalDelay)/totalPacketsSent;
                        }
                        result = new TransmissionResult(totalPacketsSent, totalTime, totalPayloadSent, averagePayloadPerPacket, averageTimePerPacket);
                        publish(result);
                    }
                    return result;
                }catch(IOException ioe){
                    return null;
                }
            }

            @Override
            protected void process(List<TransmissionResult> chunks) {
                TransmissionResult interimResult = chunks.get(chunks.size()-1);
                updateControls(interimResult);
            }

            @Override
            protected void done() {
                if(isCancelled()){
                    jProgressBar1.setString("Status: Cancelled");
                }else{
                    jProgressBar1.setString("Status: Finished");
                }
                jButton1.setEnabled(true);
                jButton2.setEnabled(false);
            }
            
        };
        for(ActionListener l:jButton2.getActionListeners()){
            jButton2.removeActionListener(l);
        }
        jButton2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                w.cancel(false);
            }
        });
        jButton2.setEnabled(true);
        w.execute();
    }
    
    private void updateControls(TransmissionResult r){
        jProgressBar1.setValue(r.getTotalPackets());
        jLabel6.setText(String.valueOf(r.getTotalPackets()));
        jLabel7.setText(String.valueOf(r.getTotalPayload()));
        jLabel8.setText(String.format("%.3f", r.getTotalTime()/1000.0d));
        jLabel9.setText(String.format("%.3f", r.getAveragePayloadPerPacket()));
        jLabel10.setText(String.format("%.3f", r.getAverageTimePerPacket()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Packet transmission in progress");
        setModal(true);

        jProgressBar1.setString("Status: Idle");
        jProgressBar1.setStringPainted(true);

        jButton1.setText("Close");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Packets transmitted: ");

        jLabel2.setText("Total payload (bytes):");

        jLabel3.setText("Total time:");

        jLabel4.setText("Average payload / packet (bytes):");

        jLabel5.setText("Average time / packet (msec):");

        jLabel6.setText("0");

        jLabel7.setText("0");

        jLabel8.setText("0");

        jLabel9.setText("0");

        jLabel10.setText("0");
        jLabel10.setToolTipText("");

        jButton2.setText("Cancel");
        jButton2.setEnabled(false);
        jButton2.setPreferredSize(new java.awt.Dimension(43, 25));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)))
                        .addGap(0, 266, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (evt.getActionCommand().equals("Close")) {
            setVisible(false);
            dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TransmissionInProgressDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransmissionInProgressDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransmissionInProgressDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransmissionInProgressDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    TransmissionInProgressDialog dialog = new TransmissionInProgressDialog(new javax.swing.JFrame(), true, new File("/home/cyberpython/Desktop/dump.txt"), InetAddress.getByName("127.0.0.1"), 5555, 42, 100);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            System.exit(0);
                        }
                    });
                    dialog.setVisible(true);
                } catch (UnknownHostException uhe) {
                    System.err.println(uhe.getMessage());
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
}
