package csc435.app;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CountWords {
    public long dataset_size = 0;
    public double execution_time = 0.0;

    public void count_words(String input_dir, String output_dir) {
        long startTime = System.currentTimeMillis();

        final Path inputPath = Paths.get(input_dir);
        final Path outputPath = Paths.get(output_dir);

        try {
            Files.walkFileTree(inputPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".txt")) {
                        dataset_size += attrs.size();

                        String content = new String(Files.readAllBytes(file));
                        String[] words = content.split("[ \t\n]+");
                        Map<String, Integer> frequencyMap = new HashMap<>();

                        for (String word : words) {
                            if (!word.isEmpty()) {
                                frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
                            }
                        }

                        // Write word counts to the output file
                        Path relativePath = inputPath.relativize(file);
                        Path outputFile = outputPath.resolve(relativePath);
                        Files.createDirectories(outputFile.getParent());

                        StringBuilder outputContent = new StringBuilder();
                        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
                            outputContent.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
                        }

                        Files.write(outputFile, outputContent.toString().getBytes());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        execution_time = endTime - startTime;
        dataset_size /= (1024.0 * 1024.0); 
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Improper number of arguments");
            System.exit(1);
        }

        CountWords countWords = new CountWords();

        countWords.count_words(args[0], args[1]);

        System.out.print("Finished counting " + countWords.dataset_size + " MiB of words");
        System.out.println(" in " + countWords.execution_time + " milliseconds");
    }
}

