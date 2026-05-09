package orange.wz.mcp.tool.impl;

import orange.wz.mcp.service.McpWorkspaceService;
import orange.wz.mcp.session.McpSessionManager;
import orange.wz.mcp.tool.support.BaseSessionTool;
import orange.wz.mcp.tool.support.ToolParamHelper;

import java.util.Map;

public final class UnloadNodeTool extends BaseSessionTool {
    private final McpWorkspaceService service;

    public UnloadNodeTool(McpSessionManager sessionManager, McpWorkspaceService service) {
        super(sessionManager);
        this.service = service;
    }

    @Override
    public String name() {
        return "unload_node";
    }

    @Override
    public Map<String, Object> invoke(Map<String, Object> params) {
        String path = ToolParamHelper.requireString(params, "path");
        var session = session(params);
        service.unloadByPath(session, path);
        return Map.of("ok", true, "rootCount", session.getRoots().size());
    }
}
