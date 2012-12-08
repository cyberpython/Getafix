/*
 * The MIT License
 *
 * Copyright 2012 Georgios Migdos <cyberpython@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package getafix;

import getafix.ui.GetafixMainWindow;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.UIManager;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class Getafix {

    private static final int DOTS_PER_LINE = 5;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
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
        } else {
            File inputFile;
            InetAddress host = null;
            int port = 5555;
            int offset = 42;
            int delay = 100;
            long currentTime;
            long totalDelay = 0;
            long totalBytesSent = 0;
            long totalPacketsSent = 0;
            if (args.length != 5) {
                System.out.println("Wrong number of parameters. Usage: ");
                System.out.println("java -jar Getafix.jar <remote_ip/host_name> <port> <offset> <delay> <input_file>");
                System.out.println("The typical offset for UDP packets captured on Ethernet is 42.");
                System.out.println();
                System.exit(0);
            }
            inputFile = new File(args[4]);
            if (!inputFile.isFile()) {
                System.err.println(args[4] + " does not point to an existing file.");
                System.exit(1);
            }
            try {
                host = InetAddress.getByName(args[0]);
            } catch (UnknownHostException uhe) {
                System.err.println(args[0] + " is not a valid IP / known host name.");
                System.exit(1);
            }
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException nfe) {
                System.err.println(args[1] + " is not a valid port number.");
                System.exit(1);
            }
            try {
                offset = Integer.parseInt(args[2]);
                if (offset < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException nfe) {
                System.err.println(args[2] + " is not a valid offset value.");
                System.exit(1);
            }
            try {
                delay = Integer.parseInt(args[3]);
                if (delay < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException nfe) {
                System.err.println(args[3] + " is not a valid delay value.");
                System.exit(1);
            }
            try {
                DatagramSocket sock = new DatagramSocket();
                K12TextFileParser sfp = new K12TextFileParser(inputFile, offset);
                byte[] bytes;
                long time = System.currentTimeMillis();
                int dotCounter = 0;
                while ((bytes = sfp.getNextPacketBytes()) != null) {
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, host, port);
                    sock.send(packet);
                    totalBytesSent += bytes.length;
                    totalPacketsSent++;
                    currentTime = System.currentTimeMillis();
                    try {
                        Thread.sleep(delay);

                    } catch (InterruptedException ie) {
                    }
                    totalDelay += System.currentTimeMillis() - currentTime;
                    System.out.print(".");
                    dotCounter++;
                    if (dotCounter == DOTS_PER_LINE) {
                        dotCounter = 0;
                        System.out.println();
                    }
                }
                if (dotCounter > 0) {
                    System.out.println();
                }
                time = System.currentTimeMillis() - time;
                System.out.println("-----------------------------------------------------------------");
                System.out.println("Total packets sent: " + totalPacketsSent);
                System.out.println("Total bytes of payload sent: " + totalBytesSent);
                System.out.println("Total time: " + String.format("%.3f", time / 1000.0d) + " sec");
                if (totalPacketsSent > 0) {
                    System.out.println("Average bytes of payload per packet: " + String.format("%.3f", ((double) totalBytesSent) / totalPacketsSent));
                    System.out.println("Average time per packet: " + String.format("%.3f", (((double) time - totalDelay) / totalPacketsSent)) + " msec");
                }
            } catch (IOException ioe) {
                System.err.println("Unexpected I/O error - Bye, bye cruel world... :(");
            }
        }
    }
}