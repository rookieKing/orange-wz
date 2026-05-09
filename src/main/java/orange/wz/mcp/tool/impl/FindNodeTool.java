package orange.wz.mcp.tool.impl;

import orange.wz.mcp.dto.NodeSummary;
import orange.wz.mcp.service.McpWorkspaceService;
import orange.wz.mcp.session.McpSessionManager;
import orange.wz.mcp.tool.support.BaseSessionTool;
import orange.wz.mcp.tool.support.ToolParamHelper;

import java.util.Map;

public final class FindNodeTool extends BaseSessionTool {
    private final McpWorkspaceService service;

    public FindNodeTool(McpSessionManager sessionManager, McpWorkspaceService service) {
        super(sessionManager);
        this.service = service;
    }

    @Override
    public String name() {
        return "find_node";
    }

    @Override
    public Map<String, Object> invoke(Map<String, Object> params) {
        var session = session(params);
        String path = ToolParamHelper.requireString(params, "path");
        boolean autoParse = ToolParamHelper.getBoolean(params, "autoParse", true);
        var node = service.findByPath(session, path, autoParse);
        return Map.of("node", NodeSummary.from(node));
    }
}
