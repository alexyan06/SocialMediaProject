/**
 * CS 180 Group Project: Server
 * @author Alex Yan, yan517
 * @author Sripoorna Modugula, smodugul
 * @version 1.0 11/17/2024
 * Server that connects to multiple clients and
 * does the processing of data.
 */

public class InvalidTargetException extends Exception {
    public InvalidTargetException(String message) {
        super(message);
    }
}
