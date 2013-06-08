/*
 */
package getafix;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public enum TransportProtocol {
    TCP, UDP, NOT_IMPLEMENTED;
    
    public static TransportProtocol fromValue(int v){
        switch(v){
            case 6: return TCP;
            case 17: return UDP;
            default: return NOT_IMPLEMENTED;
        }
    }
}
