package orange.wz.mcp.tool.impl;

import orange.wz.mcp.service.McpWorkspaceService;
import orange.wz.mcp.session.McpSessionManager;
import orange.wz.mcp.tool.support.BaseSessionTool;
import orange.wz.mcp.tool.support.ToolParamHelper;

import java.util.Map;

public final class SaveNodeAsTool extends BaseSessionTool {
    private final McpWorkspaceService service;

    public SaveNodeAsTool(McpSessionManager sessionManager, McpWorkspaceService service) {
        super(sessionManager);
        this.service = service;
    }

    @Override
    public String name() {
        return "save_as";
    }

    @Override
    public Map<String, Object> invoke(Map<String, Object> params) {
        var session = session(params);
        String path = ToolParamHelper.requireString(params, "path");
        String filePath = ToolParamHelper.requireString(params, "filePath");
        boolean autoParse = ToolParamHelper.getBoolean(params, "autoParse", true);
        service.saveNodeAs(session, path, filePath, autoParse);
        return Map.of("ok", true);
    }
}
