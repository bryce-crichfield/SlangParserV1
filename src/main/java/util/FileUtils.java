package util;

import java.util.Optional;

public interface FileUtils {

    public static Optional<String> read(String path) {
        var file = new java.io.File(path);
        if (!file.exists()) {
            return Optional.empty();
        }

        var sb = new StringBuilder();
        try (var reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            return Optional.empty();
        }

        return Optional.of(sb.toString());
    }
}
