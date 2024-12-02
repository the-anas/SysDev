package org.example;
import jakarta.json.JsonReader;
import jakarta.json.Json;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.json.JsonObject;
import java.io.*;
import java.net.Socket;

@Path("/sysdev")
public class TCPclient {


    @GET
    @Path("/dijkstra")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject dijkstra(
            @QueryParam("originLat") double originLat,
            @QueryParam("originLon") double originLon,
            @QueryParam("destinationLat") double destinationLat,
            @QueryParam("destinationLon") double destinationLon) {
        try {

            String response = sendRequest("dijkstra", originLat, originLon, destinationLat, destinationLon);
            JsonReader jsonReader = Json.createReader(new StringReader(response));
            JsonObject jsonObject1 = jsonReader.readObject();
            jsonReader.close();
            System.out.println("response to client in dijkstra is");
            System.out.println(jsonObject1);
            return jsonObject1;

        } catch (IOException e) {
            e.printStackTrace();
           return null;
        }
    }

    @GET
    @Path("/astar")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject aStar(
            @QueryParam("originLat") double originLat,
            @QueryParam("originLon") double originLon,
            @QueryParam("destinationLat") double destinationLat,
            @QueryParam("destinationLon") double destinationLon) {
        try {
            String response = sendRequest("astar", originLat, originLon, destinationLat, destinationLon);
            JsonReader jsonReader = Json.createReader(new StringReader(response));
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();
            return  jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private String sendRequest(String algorithm, double originLat, double originLon, double destinationLat, double destinationLon) throws IOException {
        try (Socket socket = new Socket("localhost", 1234);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Construct request
            String request = algorithm + " " + originLat + " " + originLon + " " + destinationLat + " " + destinationLon;

            // Send request to server
            out.println(request);

            // Receive response from server
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
                if (in.readLine().equals("")){
                    break;
                }
            }
            return response.toString();
        }
    }

}
