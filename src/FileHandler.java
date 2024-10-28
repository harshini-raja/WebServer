import java.io.*;
import java.nio.file.Files;

public class FileHandler {
    public static boolean isFileAccessible(File file) {
        return file.exists() && file.isFile() && file.canRead();
    }

    public static void sendFile(OutputStream clientOut, File file) throws IOException {
        PrintWriter writer = new PrintWriter(clientOut, true);
        writer.println("HTTP/1.0 200 OK");
        writer.println("Content-Type: " + getMimeType(file));
        writer.println();

        try (FileInputStream fileIn = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                clientOut.write(buffer, 0, bytesRead);
            }
        }
    }

    private static String getMimeType(File file) {
        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return "application/octet-stream";
        }
    }
}
