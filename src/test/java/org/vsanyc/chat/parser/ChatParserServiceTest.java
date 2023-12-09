package org.vsanyc.chat.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.vsanyc.chat.parser.service.ChatParserService;

public class ChatParserServiceTest {

    private ChatParserService chatParserService = new ChatParserService();
    @Test
    public void testParseChat() {
        var chatInputStream = this.getClass().getClassLoader().getResourceAsStream("meeting_saved_chat.txt");
        var result = chatParserService.parseChat(chatInputStream);
        Assertions.assertFalse(result.isEmpty());
    }
}
