package server.Handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import server.EpicDeserializer;
import tasks.Epic;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HandlerMethods {
    Gson gson;

    HandlerMethods(){
        gson= new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Epic.class,new EpicDeserializer())
                .create();

    }


    static void writeStringResponse(HttpExchange exchange,
                                    String responseString,
                                    int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    static int getIdFromQuery(String query) {
        String substringId = query.substring(3);
        try {
            int id = Integer.parseInt(substringId);
            return id;
        } catch (NumberFormatException e) {
            //задачи с id 0 быть не может
            return 0;
        }
    }
}
