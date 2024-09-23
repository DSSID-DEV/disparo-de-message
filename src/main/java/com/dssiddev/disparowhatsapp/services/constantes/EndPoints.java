package com.dssiddev.disparowhatsapp.services.constantes;

import java.util.HashMap;
import java.util.Map;

public class EndPoints {

    public static final String HOST = "http://localhost:3000/";
    public static final String SEND_MESSAGE = HOST + "client/sendMessage/";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String X_API_KEY = "x-api-key";

    public static final String ACCEPT = "accept";

    public static final String SESSION_ID = "{sessionId}";

    public static final String START_NEW_INSTANCE = "session/start/{sessionId}";
    public static final String STATUS_OF_INSTANCE = "session/status/{sessionId}";
    public static final String GET_RQCODE_INSTANCE = "session/qr/{sessionId}";
    public static final String GET_IMAGE_QR_CODE = "session/qr/{sessionId}/image";
    public static final String TERMINATE_SESSION = "session/terminate/{sessionId}";
    public static final String TERMINATE_AND_INACTIVE_SESSION = "/session/terminateInactive";
    public static final String TERMINATE_ALL_SESSIONS = "/session/terminateAll";

    public static final String CLIENT_ACCEPT_INVETE = "/client/acceptInvite/{sessionId}";
    public static final String CLIENT_CHAT_BY_ID = "/client/getChatById/{sessionId}";
    public static final String CLIENT_CHAT_LABELS = "/client/getChatLabels/{sessionId}";
    public static final String CLIENT_CHATS = "/client/getChats/{sessionId}";
    public static final String CLIENT_CHATS_BY_LABEL_ID = "/client/getChatsByLabelId/{sessionId}";
    public static final String CLIENT_LABEL_BY_ID = "/client/getLabelById/{sessionId}";
    public static final String CLIENT_LABELS = "/client/getLabels/{sessionId}";
    public static final String CLIENT_STATE = "/client/getState/{sessionId}";
    public static final String CLIENT_SEND_MESSAGE = "/client/sendMessage/{sessionId}";
    public static final String CLIENT_SEND_SEEN = "/client/sendSeen/{sessionId}";
    public static final String CLIENT_W_WEB_VERSION = "/client/getWWebVersion/{sessionId}";

}
