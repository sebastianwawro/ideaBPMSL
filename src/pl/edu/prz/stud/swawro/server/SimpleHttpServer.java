package pl.edu.prz.stud.swawro.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.ErrorCode;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.RequestPacket;
import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.ResponsePacket;
import pl.edu.prz.stud.swawro.server.config.Config;
import pl.edu.prz.stud.swawro.server.controller.PacketProcessor;

public class SimpleHttpServer {

    static public void startServer() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(Integer.valueOf(Config.HTTP_PORT)), 0);
        server.createContext("/aiocontroller", new SimpleHttpServer.MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static public Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            t.getResponseHeaders().set("Content-Type", "application/json");
            Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
            String request = params.get("json");
            System.out.println("HttpServer got request" + request);
            //String response = "{\"json-returned\"=\""+params.get("json")+"\"}";
            String response = parseRequest(request);
            System.out.println("HttpServer send response: " + response);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private String parseRequest(String request) {
            String response = null;

            Gson gson = new Gson();
            RequestPacket requestPacket = new RequestPacket();
            ResponsePacket responsePacket = new ResponsePacket();
            try {
                //request = unpackString(request);
                requestPacket = gson.fromJson(request, RequestPacket.class);
                if (requestPacket.getPacketId()==0) throw new JsonSyntaxException("Inconsistent data");

                //parsowanie stringa
                response = PacketProcessor.getInstance().processPacket(request, requestPacket.getPacketId());
            }
            catch (Exception e) {
                responsePacket.setStatusCode(ErrorCode.INCONSISTENT_DATA.getErrorCode());
                System.out.println(e.toString());
            }
            if(response == null) response = gson.toJson(responsePacket);

            return response;
        }
    }

}
