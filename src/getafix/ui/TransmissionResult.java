package getafix.ui;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class TransmissionResult {
    
    private int totalPackets;
    private long totalTime;
    private long totalPayload;
    private double averagePayloadPerPacket;
    private double averageTimePerPacket;

    public TransmissionResult(int totalPackets, long totalTime, long totalPayload, double averagePayloadPerPacket, double averageTimePerPacket) {
        this.totalPackets = totalPackets;
        this.totalTime = totalTime;
        this.totalPayload = totalPayload;
        this.averagePayloadPerPacket = averagePayloadPerPacket;
        this.averageTimePerPacket = averageTimePerPacket;
    }

    /**
     * @return the totalPackets
     */
    public int getTotalPackets() {
        return totalPackets;
    }

    /**
     * @return the totalTime
     */
    public long getTotalTime() {
        return totalTime;
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

    /**
     * @return the averageTimePerPacket
     */
    public double getAverageTimePerPacket() {
        return averageTimePerPacket;
    }
    
    
    
}
