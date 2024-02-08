package org.vsanyc.chat.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.vsanyc.chat.parser.service.ChatParserService;

public class ChatParserServiceTest {

    private final ChatParserService chatParserService = new ChatParserService();
    @Test
    public void testParseChatEn() {
        var chatInputStream = this.getClass().getClassLoader().getResourceAsStream("meeting_saved_chat_en_delimeter.txt");
        var result = chatParserService.parseChat(chatInputStream);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void testParseChatRu() {
        var chatInputStream = this.getClass().getClassLoader().getResourceAsStream("meeting_saved_chat_ru_delimeter.txt");
        var result = chatParserService.parseChat(chatInputStream);
        Assertions.assertFalse(result.isEmpty());
    }
}
