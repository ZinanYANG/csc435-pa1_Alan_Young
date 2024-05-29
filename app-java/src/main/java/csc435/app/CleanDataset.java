package csc435.app;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

public class CleanDataset {

    public long dataset_size = 0; 
    public double execution_time = 0.0;

    public void clean_dataset(String input_dir, String output_dir) throws IOException {
        long startTime = System.nanoTime();

        final Path inputPath = Paths.get(input_dir);
        final Path outputPath = Paths.get(output_dir);

        Files.walkFileTree(inputPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".txt")) {

                    dataset_size += attrs.size();

                    String content = new String(Files.readAllBytes(file));

                    // Clean content
                    String cleanedContent = cleanContent(content);

                    // Write to output file
                    Path relativePath = inputPath.relativize(file);
                    Path outputFile = outputPath.resolve(relativePath);
                    Files.createDirectories(outputFile.getParent());
                    Files.write(outputFile, cleanedContent.getBytes());
                }
                return FileVisitResult.CONTINUE;
            }
        });

        long endTime = System.nanoTime();
        execution_time = (endTime - startTime) / 1e6;
    }

    private String cleanContent(String content) {
        // Remove '\r' characters
        content = content.replace("\r", "");

        // Replace sequences of delimiters with a single space
        content = Pattern.compile("[ \t\n]+").matcher(content).replaceAll(" ");

        // Remove any non-alphanumeric characters (except delimiters)
        content = content.replaceAll("[^a-zA-Z0-9 \t\n]", "");

        return content;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Improper number of arguments");
            System.exit(1);
        }

        CleanDataset cleanDataset = new CleanDataset();

        try {
            cleanDataset.clean_dataset(args[0], args[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print("Finished cleaning " + cleanDataset.dataset_size + " bytes of data");
        System.out.println(" in " + cleanDataset.execution_time + " milliseconds");
    }
}
