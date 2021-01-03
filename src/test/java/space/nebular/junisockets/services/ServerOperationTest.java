package space.nebular.junisockets.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import space.nebulark.junisockets.addresses.IPAddress;
import space.nebulark.junisockets.addresses.TCPAddress;
import space.nebulark.junisockets.operations.ESignalingOperationCode;
import space.nebulark.junisockets.services.SignalingServer;
import space.nebulark.junisockets.services.SignalingServerBuilder;

@RunWith(MockitoJUnitRunner.class)
public class ServerOperationTest {

    @Test
    public void testHandleKnock() throws URISyntaxException, InterruptedException, IOException {

        PropertyConfigurator.configure("log4j.properties");
        int port = 8892;
        String host = "localhost";
        Logger logger = Logger.getLogger(SignalingServer.class);

        SignalingServerBuilder builder = new SignalingServerBuilder();

        SignalingServer s = builder.setHost(host).setLogger(logger).setPort(port).build();

        s.start();
        WebSocketClient cc = new WebSocketClient(new URI("ws://localhost:8892")) {

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
            }

            @Override
            public void onMessage(String message) {
                Assert.assertEquals("{\"data\":{\"id\":\"127.0.0.0\",\"rejected\":false},\"opcode\":\"acknowledged\"}",
                        message);
                close();
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                send("{\"data\":{\"subnet\":\"127.0.0\"},\"opcode\":\"knock\"}");
            }
        };

        cc.run();
        s.stop();
    }

    @Test
    public void testHandleBind() throws URISyntaxException, IOException, InterruptedException {
        PropertyConfigurator.configure("log4j.properties");
        int port = 8892;
        String host = "localhost";
        Logger logger = Logger.getLogger(SignalingServer.class);

        SignalingServerBuilder builder = new SignalingServerBuilder();

        SignalingServer s = builder.setHost(host).setLogger(logger).setPort(port).build();

        ReentrantLock mutex = new ReentrantLock();
        IPAddress ip = new IPAddress(logger, mutex, s.subnets);
        TCPAddress tcp = new TCPAddress(logger, mutex, s.subnets, ip);

        String tcpAddress = "127.0.0.0:1234";

        String[] partsTCPAddress = tcp.parseTCPAddress(tcpAddress);
        String[] partsIPAddress = ip.parseIPAddress(partsTCPAddress[0]);

        String subnet = String.join(".", partsIPAddress[0], partsIPAddress[1], partsIPAddress[2]);

        ip.createIPAddress(subnet);

        s.start();
        WebSocketClient cc = new WebSocketClient(new URI("ws://localhost:8892")) {

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
            }

            @Override
            public void onMessage(String message) {
                JSONParser parser = new JSONParser();
                Object jsonObj = null;

                try {
                    jsonObj = parser.parse(message);
                } catch (org.json.simple.parser.ParseException e) {
                    e.printStackTrace();
                }

                JSONObject operation = (JSONObject) jsonObj;

                if (operation.get("opcode").equals(ESignalingOperationCode.ALIAS.getValue())) {
                    System.out.println("HIERER");
                    Assert.assertEquals(
                            "{\"data\":{\"id\":\"127.0.0.0\",\"alias\":\"127.0.0.0:1234\",\"set\":true},\"opcode\":\"alias\"}",
                            message);
                    close();
                }
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                send("{\"data\":{\"subnet\":\"127.0.0\"},\"opcode\":\"knock\"}");
                send("{\"data\":{\"id\":\"127.0.0.0\",\"alias\":\"127.0.0.0:1234\"},\"opcode\":\"bind\"}");
            }
        };

        cc.run();
        s.stop();
    }

    @Test
    public void testHandleOffer() throws URISyntaxException, IOException, InterruptedException {
        
        PropertyConfigurator.configure("log4j.properties");
        int port = 8892;
        String host = "localhost";
        Logger logger = Logger.getLogger(SignalingServer.class);

        SignalingServerBuilder builder = new SignalingServerBuilder();

        SignalingServer s = builder.setHost(host).setLogger(logger).setPort(port).build();

        s.start();

        //manchmal bekommt cc die offer, wenn die falschrum acknowledged wurden. Erst muss cc knocken und dann cc2
        WebSocketClient cc = new WebSocketClient(new URI("ws://localhost:8892")) {

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
            }

            @Override
            public void onMessage(String message) {

                JSONParser parser = new JSONParser();
                Object jsonObj = null;

                try {
                    jsonObj = parser.parse(message);
                } catch (org.json.simple.parser.ParseException e) {
                    e.printStackTrace();
                }

                JSONObject operation = (JSONObject) jsonObj;

                System.out.println("cc " + message);
                if (operation.get("opcode").equals(ESignalingOperationCode.ACKNOWLEDGED.getValue())) {
                    send("{\"data\":{\"offererId\":\"127.0.0.0\", \"answererId\": \"127.0.0.1\", \"offer\": \"o1\"},\"opcode\":\"offer\"}");
                  
                };
                if (operation.get("opcode").equals(ESignalingOperationCode.GOODBYE.getValue())) {
                    close();
                };

            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                send("{\"data\":{\"subnet\":\"127.0.0\"},\"opcode\":\"knock\"}");
            }
        };

        WebSocketClient cc2 = new WebSocketClient(new URI("ws://localhost:8892")) {

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
            }

            @Override
            public void onMessage(String message) {
                JSONParser parser = new JSONParser();
                Object jsonObj = null;

                try {
                    jsonObj = parser.parse(message);
                } catch (org.json.simple.parser.ParseException e) {
                    e.printStackTrace();
                }

                JSONObject operation = (JSONObject) jsonObj;
                System.out.println("cc2" + message);
                if (operation.get("opcode").equals(ESignalingOperationCode.OFFER.getValue())) {
                    System.out.println("HIERER");
                    Assert.assertEquals(
                            "{\"data\":{\"offererId\":\"127.0.0.0\",\"answererId\":\"127.0.0.1\",\"offer\":\"o1\"},\"opcode\":\"offer\"}",
                            message);
                    close();
                };;
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                send("{\"data\":{\"subnet\":\"127.0.0\"},\"opcode\":\"knock\"}");
            }
        };

        cc2.connect();

        cc.run(); 

        s.stop();
    }

    public void handleAccepting() {
    }

    public void handleShutdown() {
    }

    public void handleConnect() {
    }

    public void testHandleAnswer() {
    }

    public void testHandleCandidate() {
    }
}
