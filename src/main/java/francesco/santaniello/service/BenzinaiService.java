package francesco.santaniello.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import francesco.santaniello.model.RequestMessage;
import francesco.santaniello.model.ResponseMessage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class BenzinaiService {

    private static class InnerClass{
        private static final BenzinaiService instance = new BenzinaiService();
    }

    public static BenzinaiService getInstance(){
        return InnerClass.instance;
    }

    public ResponseMessage getBenzinai(RequestMessage requestMessage) throws Exception {
        if (requestMessage == null){
            throw new IllegalArgumentException("Corpo della richiesta non valido");
        }
        try(HttpClient client = HttpClient.newHttpClient()){
            HttpRequest httpRequest = HttpRequest
                    .newBuilder(new URI("https://carburanti.mise.gov.it/ospzApi/search/zone"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(JsonConvertService.getInstance().writeValueAsString(requestMessage), StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200){
                throw new IllegalArgumentException("Errore durante la ricerca dei distrubuttori");
            }
            return JsonConvertService.getInstance().readValue(response.body(), ResponseMessage.class);
        }
    }
}
