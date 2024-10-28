import java.io.*;

public class ResponseHandler {
    public static void sendErrorResponse(OutputStream out, String status) throws IOException {
        PrintWriter writer = new PrintWriter(out, true);
        writer.println("HTTP/1.0 " + status);
        writer.println("Content-Type: text/html");
        writer.println();
        writer.println("<html><body><h1>" + status + "</h1></body></html>");
    }
}
