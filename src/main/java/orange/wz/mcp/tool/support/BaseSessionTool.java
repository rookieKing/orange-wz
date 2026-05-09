package orange.wz.mcp.tool.support;

import orange.wz.mcp.session.McpSessionManager;
import orange.wz.mcp.session.McpSessionState;
import orange.wz.mcp.tool.McpTool;

import java.util.Map;

public abstract class BaseSessionTool implements McpTool {
    protected final McpSessionManager sessionManager;

    protected BaseSessionTool(McpSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    protected McpSessionState session(Map<String, Object> params) {
        return sessionManager.getOrCreate(ToolParamHelper.getSessionId(params));
    }
}
