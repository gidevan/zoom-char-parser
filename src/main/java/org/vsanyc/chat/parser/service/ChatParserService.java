package org.vsanyc.chat.parser.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.vsanyc.chat.parser.domain.ParseDto;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatParserService {

    private static final String AUTHOR_PATTERN_EN = "^\\d{2}.\\d{2}.\\d{2} From.*";
    private static final String AUTHOR_PATTERN_RU = "^\\d{2}.\\d{2}.\\d{2} От.*";
    private static final String DATE_TIME_FORMAT = "yyyy_MM_dd_HH_mm_ss";

    private static final String FROM_EN = "From";
    private static final String TO_EN = "To";

    private static final String FROM_RU = "От";
    private static final String TO_RU = "кому";

    public Map<String, List<String>> parseChat(ParseDto parseDto) throws IOException {
        var is = parseDto.getMultipartFile().getInputStream();
        var parseResult = parseChat(is);
        saveChatByUser(parseDto, parseResult);
        return parseResult;
    }

    public Map<String, List<String>> parseChat(InputStream inputStream) {
        var bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        var lines = bufferedReader.lines().collect(Collectors.toList());
        var result = new HashMap<String, List<String>>();
        var author = "";
        for(var line: lines) {
            if (line.matches(AUTHOR_PATTERN_EN)) {
                author = parseAuthor(line, FROM_EN, TO_EN);
            } else if (line.matches(AUTHOR_PATTERN_RU)) {
                author = parseAuthor(line, FROM_RU, TO_RU);
            } else {
                var message = line.trim();
                if (result.containsKey(author)) {
                    result.get(author).add(message);
                } else {
                    var messages = new ArrayList<String>();
                    messages.add(message);
                    result.put(author, messages);
                }
            }
        }
        return result;
    }

    private String parseAuthor(String line, String from, String to) {
        var startIndex = line.indexOf(from) + from.length();
        var endIndex = line.indexOf(to);
        return line.substring(startIndex, endIndex).trim();
    }

    private void saveChatByUser(ParseDto parseDto, Map<String, List<String>> userMessages) throws IOException{
        var outputFolder = Path.of(parseDto.getOutputFolder());
        if (!Files.exists(outputFolder)) {
            outputFolder = Files.createDirectories(Paths.get(parseDto.getOutputFolder()));
        } else {
            FileUtils.cleanDirectory(new File(parseDto.getOutputFolder()));
        }
        createFileByUser(parseDto.getOutputFolder(), userMessages);
    }

    private void createFileByUser(String outputFolder, Map<String, List<String>> userMessages) throws IOException {
        for (Map.Entry<String, List<String>> messages : userMessages.entrySet()) {
            var ldt = LocalDateTime.now();
            var fileName = outputFolder + "/" + messages.getKey() + "_"
                    + ldt.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
            var file = Files.createFile(Path.of(fileName));
            for(String message: messages.getValue()) {
                Files.writeString(file, message + System.lineSeparator(), StandardOpenOption.APPEND);
            }

        }
    }


}
