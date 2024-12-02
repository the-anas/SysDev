package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.glassfish.jersey.client.JerseyClientBuilder;
import jakarta.json.JsonObject;

import java.io.StringReader;


/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/sysdev/orsdirections")
public class MyResource {

    private static final String OPENROUTESERVICE_KEY = "5b3ce3597851110001cf62483450ffec38d94d24b7034e72b83bcd01";
    final static JerseyClient client = new JerseyClientBuilder().build();

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public static JsonObject getPath(@QueryParam("originLat") String originLat,
                                     @QueryParam("originLon") String originLon,
                                     @QueryParam("destinationLat") String destinationLat,
                                         @QueryParam("destinationLon") String destinationLon) {


        final String OPENROUTESERVICE_URL = String.format("https://api.openrouteservice.org/v2/directions/driving-car?api_key=%s&start=%s,%s&end=%s,%s", OPENROUTESERVICE_KEY, originLon, originLat,  destinationLon, destinationLat);
        final JerseyWebTarget webTarget = client.target(OPENROUTESERVICE_URL);
        final Response response = webTarget
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8")
                .header("Content-Type", "application/json; charset=utf-8")
                .get();

        //parsing the string
        final String responseString = response.readEntity(String.class);
        final JsonObject jsonObject = Json.createReader(new StringReader(responseString)).readObject();
        return jsonObject;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject postPath(JsonObject json){

        double originLat = json.getJsonNumber("originLat").doubleValue();
        double originLon = json.getJsonNumber("originLon").doubleValue();
        double destinationLat = json.getJsonNumber("destinationLat").doubleValue();
        double destinationLon = json.getJsonNumber("destinationLon").doubleValue();

        //Call other API
        final String OPENROUTESERVICE_URL = String.format("https://api.openrouteservice.org/v2/directions/driving-car?api_key=%s&start=%s,%s&end=%s,%s", OPENROUTESERVICE_KEY, originLon, originLat,  destinationLon, destinationLat);
        final JerseyWebTarget webTarget = client.target(OPENROUTESERVICE_URL);
        final Response response = webTarget
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8")
                .header("Content-Type", "application/json; charset=utf-8")
                .get();

        //parsing the string
        final String responseString = response.readEntity(String.class);
        final JsonObject jsonObject = Json.createReader(new StringReader(responseString)).readObject();
        return jsonObject;

    }
}
