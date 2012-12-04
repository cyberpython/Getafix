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

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class Getafix {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        File inputFile;
        InetAddress host = null;
        int port = 5555;
        long totalBytesSent = 0;
        long totalPacketsSent = 0;
        if(args.length!=3){
            System.out.println("Wrong number of parameters. Usage: ");
            System.out.println("java -jar UDPParseAndTx.jar <remote_ip/host_name> <port> <input_file>");
            System.out.println();
            System.exit(0);
        }
        inputFile = new File(args[2]);
        if(!inputFile.isFile()){
            System.err.println(args[2]+" does not point to an existing file.");
            System.exit(1);
        }
        try{
            host = InetAddress.getByName(args[0]);
        }catch(UnknownHostException uhe){
            System.err.println(args[0]+" is not a valid IP / known host name.");
            System.exit(1);
        }
        try{
            port = Integer.parseInt(args[1]);
        }catch(NumberFormatException nfe){
            System.err.println(args[1]+" is not a valid port number.");
            System.exit(1);
        }
        try{
            DatagramSocket sock = new DatagramSocket();
            SimpleFileParser sfp = new SimpleFileParser(inputFile);
            byte[] bytes;
            while( (bytes=sfp.getNextPacketBytes())!=null){
                DatagramPacket packet = new DatagramPacket(bytes,bytes.length, host, port);
                sock.send(packet);
                totalBytesSent += bytes.length;
                totalPacketsSent++;
            }
            System.out.println("-----------------------------------------------------------------");
            System.out.println("Total packets sent: "+totalPacketsSent);
            System.out.println("Total bytes sent: "+totalBytesSent);
            if(totalPacketsSent>0){
                System.out.println("Average number of bytes per packet: "+(((double)totalBytesSent) / totalPacketsSent));
            }
        }catch(IOException ioe){
            System.err.println("Unexpected I/O error - Bye, bye cruel world... :(");
        }
    }
}
