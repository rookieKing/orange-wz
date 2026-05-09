package orange.wz.mcp.tool.impl;

import orange.wz.mcp.service.McpWorkspaceService;
import orange.wz.mcp.session.McpSessionManager;
import orange.wz.mcp.tool.support.BaseSessionTool;
import orange.wz.mcp.tool.support.ToolParamHelper;

import java.util.Map;

public final class SearchNodeTool extends BaseSessionTool {
    private final McpWorkspaceService service;

    public SearchNodeTool(McpSessionManager sessionManager, McpWorkspaceService service) {
        super(sessionManager);
        this.service = service;
    }

    @Override
    public String name() {
        return "search_node";
    }

    @Override
    public Map<String, Object> invoke(Map<String, Object> params) {
        var session = session(params);
        String startPath = ToolParamHelper.requireString(params, "startPath");
        String keyword = ToolParamHelper.requireString(params, "keyword");
        boolean autoParse = ToolParamHelper.getBoolean(params, "autoParse", true);
        String searchIn = String.valueOf(params.getOrDefault("searchIn", "name"));
        if ("value".equalsIgnoreCase(searchIn)) {
            var matches = service.searchNodeByValue(session, startPath, keyword, autoParse);
            return matches.isEmpty()
                    ? Map.of("searchIn", "value", "matches", matches)
                    : Map.of("searchIn", "value", "matches", matches, "node", matches.get(0));
        }
        var matches = service.searchNodeByName(session, startPath, keyword, autoParse);
        return matches.isEmpty()
                ? Map.of("searchIn", "name", "matches", matches)
                : Map.of("searchIn", "name", "matches", matches, "node", matches.get(0));
    }
}
