package org.vsanyc.chat.parser.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vsanyc.chat.parser.domain.ParseDto;
import org.vsanyc.chat.parser.service.ChatParserService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class ChatParserController {

    private ChatParserService chatParserService;

    @PostMapping("/parseForm")
    public Map<String, List<String>> parseChatMultipart(@ModelAttribute ParseDto parseDto) throws IOException {
        return chatParserService.parseChat(parseDto);
    }

    @PostMapping(value = "/parse",  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, List<String>> parseChat1(@RequestParam MultipartFile multipartFile,
                                               @RequestParam String outputFolder) throws IOException {
        var parseDto = ParseDto.builder()
                .multipartFile(multipartFile)
                .outputFolder(outputFolder).build();
        return chatParserService.parseChat(parseDto);
    }

}
