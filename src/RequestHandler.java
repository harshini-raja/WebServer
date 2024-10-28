import java.io.*;
import java.net.*;

public class RequestHandler implements Runnable {
    private static final String DOCUMENT_ROOT = System.getProperty("user.home") + "/Documents/Java/DS/part1/server/files";
    private Socket clientSocket;

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream clientOut = clientSocket.getOutputStream()) {

            String requestLine = clientIn.readLine();
            System.out.println("Request from client: " + requestLine);

            if (requestLine == null || !requestLine.startsWith("GET")) {
                ResponseHandler.sendErrorResponse(clientOut, "400 Bad Request");
                return;
            }

            String[] parts = requestLine.split(" ");
            if (parts.length < 2) {
                ResponseHandler.sendErrorResponse(clientOut, "400 Bad Request");
                return;
            }

            String path = parts[1].equals("/") ? "/index/index.html" : parts[1];
            File requestedFile = new File(DOCUMENT_ROOT + path);

            if (FileHandler.isFileAccessible(requestedFile)) {
                FileHandler.sendFile(clientOut, requestedFile);
            } else {
                ResponseHandler.sendErrorResponse(clientOut, "404 Not Found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
