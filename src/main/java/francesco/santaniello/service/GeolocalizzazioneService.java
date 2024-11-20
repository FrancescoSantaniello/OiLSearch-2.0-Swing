package francesco.santaniello.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import francesco.santaniello.model.Coordinate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeolocalizzazioneService {

    private static class InnerClass{
        private static final GeolocalizzazioneService instance = new GeolocalizzazioneService();
    }

    public static GeolocalizzazioneService getInstance(){
        return InnerClass.instance;
    }

    public Coordinate getCoordianteFromCitta(String citta) throws Exception {
        if (citta == null || citta.isBlank()){
            throw new IllegalArgumentException("Citta non valida");
        }
        Coordinate coordinate = new Coordinate();
        try(HttpClient client = HttpClient.newHttpClient()){
            HttpRequest httpRequest = HttpRequest
                    .newBuilder(new URI("https://api.opencagedata.com/geocode/v1/json?q=%s&key=%s".formatted(citta.toLowerCase().trim().replace(" ","%20"),"TOKEN")))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200){
                throw new IllegalArgumentException("Errore durante la ricerca dei distributori dal punto inserito");
            }
            JsonNode node = JsonConvertService.getInstance().readTree(response.body()).path("results").get(0).path("geometry");
            coordinate.setLat((float)node.get("lat").asDouble());
            coordinate.setLng((float)node.get("lng").asDouble());
        }
        return coordinate;
    }
}
