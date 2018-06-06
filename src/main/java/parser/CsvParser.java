package parser;

import org.apache.commons.lang3.RandomUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CsvParser {

    private static final String SEPARATOR = ";";
    private List<Path> processedFiles = new ArrayList<>();
    private List<Path> newFiles;

    public CsvParser() {
        newFiles = getAllArticleNames();
    }





    public Set<String> getTagsFromRandomArticle() {

        //get random article
        Path article;
        if(newFiles.size() > 0) {
            article = newFiles.get(RandomUtils.nextInt(0, newFiles.size()));
        }
        else {
            throw new RuntimeException("We are out of new articles. All files processed.");
        }

        //get tags from each line
        //line has pattern: date;tag1;tag2;tag3;...;
        Set<String> tags = new HashSet<>();
        try {
            Files.lines(article).forEach(line -> {
                String[] splitedLine = line.split(SEPARATOR);
                for(int i = 1; i < splitedLine.length; i++) { // we don't need the date
                    tags.add(splitedLine[i]);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // move processed file from newFiles into processedFiles
        newFiles.remove(article);
        processedFiles.add(article);

        return tags;
    }


    private List<Path> getAllArticleNames() {
        List<Path> articles = new ArrayList<>();
        try {
            Files.walk(Paths.get("csv/")).forEach(path -> {
                if(path.toFile().isFile()) {
                    articles.add(path);
                }
            });
        }
        catch(IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }


        return articles;
    }




}
