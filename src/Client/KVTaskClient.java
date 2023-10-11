package Client;

import manager.HttpTaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    HttpClient client;

    private String API_TOKEN;
    HttpRequest request;
    String urlSt;

    public KVTaskClient(String urlSt) {

        this.urlSt = urlSt;
        URI url = URI.create(urlSt + "register");

        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            API_TOKEN = response.body().toString();

        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }


    public void put(String key, String json) {
        URI uri = URI.create(urlSt + "save/" + key + "?API_TOKEN=" + API_TOKEN);
        request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Получен код ответа метода put: "+response.statusCode());
            System.out.println("Тело ответа: "+response.body());
        } catch (IOException | InterruptedException e) {

        }
    }

    public String load(String key) {
        URI uri = URI.create(urlSt + "load/" + key + "?API_TOKEN=" + API_TOKEN);
        request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Получен ответ от метода load: "+response.statusCode());
            System.out.println("Тело ответа: "+response.body());
            return new String(response.body().toString());
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }


}
