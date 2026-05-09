package orange.wz.mcp.tool.impl;

import orange.wz.mcp.session.McpSessionManager;
import orange.wz.mcp.session.McpSessionState;
import orange.wz.mcp.tool.McpTool;

import java.util.Map;

public final class CreateSessionTool implements McpTool {
    private final McpSessionManager sessionManager;

    public CreateSessionTool(McpSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public String name() {
        return "create_session";
    }

    @Override
    public Map<String, Object> invoke(Map<String, Object> params) {
        McpSessionState session = sessionManager.createSession();
        return Map.of("sessionId", session.getSessionId().toString());
    }
}
