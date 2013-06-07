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

import getafix.exceptions.InvalidTimestampException;
import java.io.*;
import java.nio.ByteBuffer;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class K12TextFileParser {
    
    public class Packet{
        private int timestamp;
        private byte[] bytes;

        public Packet(int timestamp, byte[] bytes) {
            this.timestamp = timestamp;
            this.bytes = bytes;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }
    }

    private int offset;
    private BufferedReader r;

    public K12TextFileParser(File inputFile, int offset) throws FileNotFoundException {
        this.offset = offset;
        this.r = new BufferedReader(new FileReader(inputFile));
    }

    /**
     * @return The next packet (as parsed) or null if the end of the
     * file has been reached
     *
     * @throws IOException
     */
    public Packet getNextPacket() throws IOException {
        String line;
        int mode = 0;
        int timestamp = 0;
        while ((line = r.readLine()) != null) {
            line = line.trim();
            switch(mode){
                case 0:
                    if (line.startsWith("+---------+---------------+----------+")) { //Found the packet delimeter
                        mode = 1;
                    }
                    break;
                case 1:
                   //TODO:
                    if (line.matches("^([0-1][0-9]|[2][0-3]):[0-5][0-9]:[0-5][0-9],[0-9]{3},[0-9]{3}.*ETHER$")) { //Found the packet time
                        try{
                            timestamp = parseTime(line.split(" ")[0]);
                            mode = 2;
                        }catch(InvalidTimestampException ite){
                        }
                    }
                    break;
                case 2:
                    int byteCount = 0;
                    int value;
                    if (line.startsWith("|")) {
                        String[] bytes = line.split("\\|");
                        ByteBuffer buf = ByteBuffer.allocate(bytes.length);
                        for (String byteNum : bytes) {
                            if (byteNum.length() == 2) {
                                try {
                                    value = Integer.parseInt(byteNum, 16);
                                    buf.put((byte) value);
                                    byteCount++;
                                } catch (NumberFormatException nfe) {
                                    //not a valid hex number - ignore it
                                }
                            }
                        }
                        if (byteCount > offset) { //OK, we got some bytes we can use
                            byte[] result = new byte[byteCount-offset];
                            System.arraycopy(buf.array(), offset, result, 0, byteCount-offset);
                            return new Packet(timestamp, result);
                        }
                    }
                    mode = 0;
                    break;
            }
        }
        return null;
    }
    
    /**
     * Takes the time in HH:MM:SS,mmm,nnn format and returns milliseconds since
     * midnight
     */
    private int parseTime(String time) throws InvalidTimestampException {
        if(time.matches("^([0-1][0-9]|[2][0-3]):[0-5][0-9]:[0-5][0-9],[0-9]{3},[0-9]{3}$")){
            String[] hms = time.split(":");
            String[] smsns = hms[2].split(",");
            return Integer.parseInt(hms[0])*3600000 + Integer.parseInt(hms[1])*60000 + Integer.parseInt(smsns[0]) * 1000 + Integer.parseInt(smsns[1]);
        }else{
            throw new InvalidTimestampException("Invalid time stamp: "+time);
        }
    }
    
}
