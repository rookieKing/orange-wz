package orange.wz.mcp.tool.impl;

import orange.wz.mcp.service.McpWorkspaceService;
import orange.wz.mcp.session.McpSessionManager;
import orange.wz.mcp.tool.support.BaseSessionTool;
import orange.wz.mcp.tool.support.ToolParamHelper;
import orange.wz.provider.tools.wzkey.WzKey;

import java.util.Map;

public final class CreateWzFileTool extends BaseSessionTool {
    private final McpWorkspaceService service;

    public CreateWzFileTool(McpSessionManager sessionManager, McpWorkspaceService service) {
        super(sessionManager);
        this.service = service;
    }

    @Override
    public String name() {
        return "create_wz_file";
    }

    @Override
    public Map<String, Object> invoke(Map<String, Object> params) {
        var session = session(params);
        String fileName = ToolParamHelper.requireString(params, "fileName");
        short version = ToolParamHelper.getShort(params, "version", (short) 95);
        WzKey key = ToolParamHelper.getWzKey(params, "key");
        return Map.of("node", service.createWz(session, fileName, version, key));
    }
}
