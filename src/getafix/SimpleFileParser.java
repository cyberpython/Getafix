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

import java.io.*;
import java.nio.ByteBuffer;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class SimpleFileParser {

    private File inputFile;
    private BufferedReader r;

    public SimpleFileParser(File inputFile) throws FileNotFoundException {
        this.inputFile = inputFile;
        this.r = new BufferedReader(new FileReader(inputFile));
    }

    /**
     * @return The next packet's bytes (as parsed) or null if the end of the
     * file has been reached
     *
     * @throws IOException
     */
    public byte[] getNextPacketBytes() throws IOException {
        String line;
        int byteCount;
        int value;
        while ((line = getNextLine()) != null) {
            byteCount = 0;
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
            if (byteCount > 0) { //OK, we got some bytes
                byte[] result = new byte[byteCount];
                System.arraycopy(buf.array(), 0, result, 0, byteCount);
                return result;
            }
        }

        return null;
    }

    private String getNextLine() throws IOException {
        String line;
        while ((line = r.readLine()) != null) {
            line = line.trim();
            if (!(line.equals("") || line.startsWith("+"))) {//TODO verify checks
                return line;
            }
        }
        return null;
    }
}
