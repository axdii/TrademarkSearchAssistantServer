package top.atzlt.domain;

import org.junit.Test;
import top.atzlt.cons.CommonConstant;

import java.util.Date;
import java.util.UUID;

public class UserSession {

    private UUID sessionId = null;
    private long lastConnectTime = CommonConstant.DEFAULT_NUMBER;

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public long getLastConnectTime() {
        return lastConnectTime;
    }

    public void setLastConnectTime(long lastConnectTime) {
        this.lastConnectTime = lastConnectTime;
    }

}
