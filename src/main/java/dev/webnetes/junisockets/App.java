package dev.webnetes.junisockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import dev.webnetes.junisockets.services.SignalingServer;
import dev.webnetes.junisockets.services.SignalingServerBuilder;

/**
 * A WebRTC signaling server for unisockets to allow nodes to discover each
 * other and exchange candidates
 * 
 * @author Jakob Waibel and contributors
 * @version 1.0
 * @since 1.0
 */
public class App {

    /**
     * Executes SignalingServer. Possible environment variables are args[0] for port
     * and args[1] for host
     * 
     * @param args args args[0] = port, args[1] = host
     * @throws InterruptedException Thrown if interrupted
     * @throws IOException          Thrown if there is a problem with the input
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        PropertyConfigurator.configure("log4j.properties");

        int port = 8892;
        String host = "localhost";
        Logger logger = Logger.getLogger(SignalingServer.class);

        try {
            port = Integer.parseInt(System.getenv("PORT"));
        } catch (Exception ex) {
            logger.trace("No custom port was set. Default port: 8892");
        }

        try {
            host = System.getenv("HOST");
        } catch (Exception ex) {
            logger.trace("No custom host was set. Default host: localhost");
        }

        SignalingServer s = new SignalingServerBuilder().setHost(host).setLogger(logger).setPort(port).build();
        s.start();
        System.out.println("SignalingServer started on port: " + s.getPort());

        // If exit is entered in the console, the application will terminate
        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String in = sysin.readLine();
            s.broadcast(in);
            if (in.equals("exit")) {
                s.stop(1000);
                break;
            }
        }
    }
}
