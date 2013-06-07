package getafix.ui;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class TransmissionResult {
    
    private int totalPackets;
    private long totalPayload;
    private double averagePayloadPerPacket;

    public TransmissionResult(int totalPackets, long totalPayload, double averagePayloadPerPacket) {
        this.totalPackets = totalPackets;
        this.totalPayload = totalPayload;
        this.averagePayloadPerPacket = averagePayloadPerPacket;
    }

    /**
     * @return the totalPackets
     */
    public int getTotalPackets() {
        return totalPackets;
    }

    /**
     * @return the totalPayload
     */
    public long getTotalPayload() {
        return totalPayload;
    }

    /**
     * @return the averagePayloadPerPacket
     */
    public double getAveragePayloadPerPacket() {
        return averagePayloadPerPacket;
    }
    
}
