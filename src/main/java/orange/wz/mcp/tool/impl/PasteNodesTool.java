package orange.wz.mcp.tool.impl;

import orange.wz.mcp.dto.OverwriteStrategy;
import orange.wz.mcp.service.McpWorkspaceService;
import orange.wz.mcp.session.McpSessionManager;
import orange.wz.mcp.tool.support.BaseSessionTool;
import orange.wz.mcp.tool.support.ToolParamHelper;

import java.util.Locale;
import java.util.Map;

public final class PasteNodesTool extends BaseSessionTool {
    private final McpWorkspaceService service;

    public PasteNodesTool(McpSessionManager sessionManager, McpWorkspaceService service) {
        super(sessionManager);
        this.service = service;
    }

    @Override
    public String name() {
        return "paste_nodes";
    }

    @Override
    public Map<String, Object> invoke(Map<String, Object> params) {
        var session = session(params);
        String targetPath = ToolParamHelper.requireString(params, "targetPath");
        boolean autoParse = ToolParamHelper.getBoolean(params, "autoParse", true);
        String strategyText = ToolParamHelper.getString(params, "strategy", OverwriteStrategy.ERROR.name());
        OverwriteStrategy strategy = OverwriteStrategy.valueOf(strategyText.toUpperCase(Locale.ROOT));
        return Map.of("pasted", service.pasteToPath(session, targetPath, strategy, autoParse));
    }
}
