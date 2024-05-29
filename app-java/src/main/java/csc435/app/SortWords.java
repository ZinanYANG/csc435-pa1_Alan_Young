package csc435.app;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SortWords {
    public long num_words = 0;
    public double execution_time = 0.0;

    public void sort_words(String input_dir, String output_dir) {
        long startTime = System.currentTimeMillis();

        Path inputPath = Paths.get(input_dir);
        Path outputPath = Paths.get(output_dir);

        try {
            Files.walkFileTree(inputPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    List<String> lines = Files.readAllLines(file);
                    Map<String, Integer> wordCounts = new HashMap<>();
                    for (String line : lines) {
                        String[] parts = line.split(" ");
                        if (parts.length == 2) {
                            wordCounts.put(parts[0], Integer.parseInt(parts[1]));
                        }
                    }

                    // Sort by frequency
                    List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(wordCounts.entrySet());
                    sortedEntries.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

                    // Write to output file
                    Path relativePath = inputPath.relativize(file);
                    Path outputFile = outputPath.resolve(relativePath);
                    Files.createDirectories(outputFile.getParent());

                    List<String> sortedLines = new ArrayList<>();
                    for (Map.Entry<String, Integer> entry : sortedEntries) {
                        sortedLines.add(entry.getKey() + " " + entry.getValue());
                        num_words += entry.getValue();
                    }
                    Files.write(outputFile, sortedLines);

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        execution_time = (endTime - startTime);
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Improper number of arguments");
            System.exit(1);
        }

        SortWords sortWords = new SortWords();

        sortWords.sort_words(args[0], args[1]);

        System.out.print("Finished sorting " + sortWords.num_words + " words");
        System.out.println(" in " + sortWords.execution_time + " milliseconds");
    }
}

