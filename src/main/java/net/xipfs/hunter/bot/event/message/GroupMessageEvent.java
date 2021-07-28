package net.xipfs.hunter.bot.event.message;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author xie hui
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GroupMessageEvent extends MessageEvent {

    @JSONField(name = "sub_type")
    private String subType;

    @JSONField(name = "group_id")
    private Long groupId;

    @JSONField(name = "anonymous")
    private JSONObject anonymous;

    @JSONField(name = "message")
    private String message;

    @JSONField(name = "raw_message")
    private String rawMessage;

    @JSONField(name = "font")
    private Integer font;

    @JSONField(name = "sender")
    private JSONObject sender;

    public static boolean isSupport(JSONObject jsonObject) {
        return ("message".equals(jsonObject.getString("post_type"))
                && "group".equals(jsonObject.getString("message_type")));
    }

}
