package net.xipfs.hunter.bot.message.support;

import com.alibaba.fastjson.JSON;
import net.xipfs.hunter.bot.message.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xie hui
 */
@Data
@NoArgsConstructor
public class TextMessage implements Message {

    private String text;

    public TextMessage(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public String toMessageString() {
        return String.format("{\"type\":\"%s\",\"data\":%s}", "text", JSON.toJSONString(this));
    }

}
