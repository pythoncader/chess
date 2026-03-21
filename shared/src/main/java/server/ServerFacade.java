package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameID;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData register(String username, String password, String email) throws ResponseException{
        UserData userData = new UserData(username, password, email);
        var request = buildRequest("POST", "/user", userData);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public AuthData login(String username, String password) throws ResponseException{
        UserData userData = new UserData(username, password, null);
        var request = buildRequest("POST", "/session", userData);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public GameID createGame(String gameName, String authToken) throws ResponseException{
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/game"))
                .method("POST", makeRequestBody(gameName));
        request.setHeader("authorization", authToken);
        var response = sendRequest(request.build());
        return handleResponse(response, GameID.class);
    }
    public void logout(String authToken) throws ResponseException{
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/session"))
                .method("DELETE", makeRequestBody(null));
        request.setHeader("authorization", authToken);
        sendRequest(request.build());
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

}
