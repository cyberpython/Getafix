/*
 */
package getafix;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class Packet {

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