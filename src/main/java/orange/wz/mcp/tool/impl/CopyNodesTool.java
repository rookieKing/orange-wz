package orange.wz.mcp.tool.impl;

import orange.wz.mcp.service.McpWorkspaceService;
import orange.wz.mcp.session.McpSessionManager;
import orange.wz.mcp.tool.support.BaseSessionTool;
import orange.wz.mcp.tool.support.ToolParamHelper;

import java.util.List;
import java.util.Map;

public final class CopyNodesTool extends BaseSessionTool {
    private final McpWorkspaceService service;

    public CopyNodesTool(McpSessionManager sessionManager, McpWorkspaceService service) {
        super(sessionManager);
        this.service = service;
    }

    @Override
    public String name() {
        return "copy_nodes";
    }

    @Override
    public Map<String, Object> invoke(Map<String, Object> params) {
        var session = session(params);
        List<String> paths = ToolParamHelper.getStringList(params, "paths");
        boolean autoParse = ToolParamHelper.getBoolean(params, "autoParse", true);
        service.copyByPaths(session, paths, autoParse);
        return Map.of("copiedCount", paths.size());
    }
}
