package orange.wz.mcp.tool.impl;

import orange.wz.mcp.service.McpWorkspaceService;
import orange.wz.mcp.session.McpSessionManager;
import orange.wz.mcp.tool.support.BaseSessionTool;
import orange.wz.mcp.tool.support.ToolParamHelper;

import java.util.Map;

public final class GetNodeDetailTool extends BaseSessionTool {
    private final McpWorkspaceService service;

    public GetNodeDetailTool(McpSessionManager sessionManager, McpWorkspaceService service) {
        super(sessionManager);
        this.service = service;
    }

    @Override
    public String name() {
        return "get_node_detail";
    }

    @Override
    public Map<String, Object> invoke(Map<String, Object> params) {
        var session = session(params);
        String path = ToolParamHelper.requireString(params, "path");
        boolean autoParse = ToolParamHelper.getBoolean(params, "autoParse", true);
        return Map.of("detail", service.getNodeDetail(session, path, autoParse));
    }
}
