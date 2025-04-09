package committee.nova.mods.mc_lark.core;

import com.alibaba.fastjson.JSONObject;
import com.lark.oapi.Client;
import com.lark.oapi.service.application.v6.enums.MessageTypeEnum;
import com.lark.oapi.service.im.v1.model.CreateMessageReq;
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody;
import com.lark.oapi.service.im.v1.model.CreateMessageResp;
import com.lark.oapi.service.im.v1.model.ext.MessageTemplate;
import com.lark.oapi.service.im.v1.model.ext.MessageTemplateData;
import com.lark.oapi.service.im.v1.model.ext.MessageText;
import com.mojang.logging.LogUtils;
import committee.nova.mods.mc_lark.Config;
import lombok.Data;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Project: lark
 * @Author: cnlimiter
 * @CreateTime: 2025/4/10 01:30
 * @Description:
 */
public class LarkApi {
    private static final Logger LOGGER = LogUtils.getLogger();


    public LarkApi() {
    }

    /**
     * 发送 Text 消息到群组
     * 相关文档： <a href="https://open.feishu.cn/document/server-docs/im-v1/message/">...</a>
     * @param msg 消息
     * @return 是否发送成功
     */
    public Boolean sendMsg(String msg) {
        // 构建发送信息的Json结构体
        String content = MessageText.newBuilder()
                //.atAll() // @ 群组中所有人，可以改为 @ 某个人的 openId
                .textLine(msg)
                .build();
        // 构建请求体
        CreateMessageReq req = CreateMessageReq.newBuilder()
                .receiveIdType("chat_id")
                .createMessageReqBody(CreateMessageReqBody.newBuilder()
                        .receiveId(Config.chatId)
                        .msgType(MessageTypeEnum.TEXT.getValue())
                        .content(content)
                        .build())
                .build();
        try {
            // 发送请求
            CreateMessageResp resp = Client.newBuilder(Config.appId, Config.appSecret).build().im().v1().message().create(req);
            if (!resp.success()) {
                throw new Exception(String.format("client.im.message.create failed, code: %d, msg: %s, logId: %s",
                        resp.getCode(), resp.getMsg(), resp.getRequestId()));
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("lark send msg error:", e);
        }

        return false;
    }

    /**
     * 发送卡片信息到群组
     * @return 是否发送成功
     */
    public Boolean sendFinanceCardInfoMsg() {
        String customerValue = "test_customer_value";
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("customer_key", customerValue);

        return sendInterActiveMessage(Config.templateId, varMap);
    }


    /**
     * 发送卡片信息到群组
     * @param templateId 消息卡片的TemplateId
     * @param varMap 自定义参数
     * @return 是否发送成功
     */
    private Boolean sendInterActiveMessage(String templateId, Map<String, Object> varMap) {
        MessageTemplateData messageTemplateData = MessageTemplateData.newBuilder()
                .templateId(templateId)
                .templateVariable(varMap)
                .build();

        String content = MessageTemplate.newBuilder()
                .data(messageTemplateData)
                .build();

        CreateMessageReq req = CreateMessageReq.newBuilder()
                .receiveIdType("chat_id")
                .createMessageReqBody(CreateMessageReqBody.newBuilder()
                        .receiveId(Config.chatId)
                        .msgType(MessageTypeEnum.INTERACTIVE.getValue())
                        .content(content)
                        .build())
                .build();
        try {
            CreateMessageResp resp = Client.newBuilder(Config.appId, Config.appSecret).build().im().v1().message().create(req);
            if (!resp.success()) {
                throw new Exception(String.format("client.im.message.create failed, code: %d, msg: %s, logId: %s, content: %s",
                        resp.getCode(), resp.getMsg(), resp.getRequestId(), content));
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("lark send msg error:", e);
        }

        return false;
    }


    /**
     * 回调接口
     * 文档： <a href="https://open.feishu.cn/document/server-docs/event-subscription-guide/event-subscription-configure-/request-url-configuration-case">...</a>
     * @param request 请求体
     * @return 回调结果
     */
    public JSONObject test(LarkWebHookRequest request) {
        JSONObject jsonObject = new JSONObject();
        LOGGER.info("lark webhook get request {}", request);
        // 上面的方法是用于 通过飞书的回调订阅方式配置验证 ：在这里仅做最简单的字符串Token 校验，还可以使用签名校验
        if (Objects.nonNull(request.getToken()) && Objects.nonNull(request.getType())
                && request.getType().equals("url_verification") && request.getToken().equals(Config.verificationToken)) {
            jsonObject.put("challenge", request.getChallenge());
            return jsonObject;
        }

        // 下面的方法用于获取真实的回调信息 ： 在Java SDK 中没有找到对应的DTO，所以按照文档写了一份
        LarkWebHookResultToast larkWebHookResultToast = new LarkWebHookResultToast();
        if (Objects.nonNull(request.getHeader()) && request.getHeader().getEvent_type().equals("card.action.trigger")
                && request.getHeader().getToken().equals(Config.verificationToken)) {
            LarkWebHookEventAction action = request.getEvent().getAction();
            String tag = action.getTag();
            Object value = action.getValue();
            // 根据 value 值中的key来进一步触发的事件
            LOGGER.info("receive card action trigger msg:{}, tag:{} : value:{}", action, tag, value);
            // 返回给客户端
            larkWebHookResultToast.setContent("提交成功！");
        }

        jsonObject.put("toast", larkWebHookResultToast);
        return jsonObject;
    }

    @Data
    public static class LarkWebHookRequest {
        private String challenge;
        private String type;
        private String token;
        private String schema;
        private LarkWebHookHeader header;
        private LarkWebHookEvent event;

    }

    @Data
    public static class LarkWebHookHeader {
        private String event_id;
        private String token;
        private String create_time;
        private String event_type;
        private String tenant_key;
        private String app_id;
    }

    @Data
    public static class LarkWebHookEvent {
        private LarkWebHookEventOperator operator;
        private String token;
        private String host;
        private String delivery_type;
        private LarkWebHookEventAction action;
        private LarkWebHookEventContent context;
    }

    @Data
    public static class LarkWebHookEventOperator {
        private String tenant_key;
        private String user_id;
        private String open_id;
    }

    @Data
    public static class LarkWebHookEventAction {
        private Object value;
        private String tag;
        private Object option;
    }

    @Data
    public static class LarkWebHookEventContent {
        private String url;
        private String preview_token;
        private String open_message_id;
        private String open_chat_id;
    }

    @Data
    public static class LarkWebHookResult {
        private LarkWebHookResultToast toast;
    }

    @Data
    public static class LarkWebHookResultToast {
        private String type = "info";
        private String content = "提交成功！";
    }
}
