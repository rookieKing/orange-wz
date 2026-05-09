package orange.wz.mcp.tool.impl;

import orange.wz.mcp.service.McpWorkspaceService;
import orange.wz.mcp.session.McpSessionManager;
import orange.wz.mcp.tool.support.BaseSessionTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class QueryNodesTool extends BaseSessionTool {
    private final McpWorkspaceService service;

    public QueryNodesTool(McpSessionManager sessionManager, McpWorkspaceService service) {
        super(sessionManager);
        this.service = service;
    }

    @Override
    public String name() {
        return "query_nodes";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> invoke(Map<String, Object> params) {
        var session = session(params);
        Object queries = params.get("queries");
        List<Map<String, Object>> list;
        if (queries instanceof List<?> raw) {
            list = (List<Map<String, Object>>) raw;
        } else {
            Map<String, Object> query = new HashMap<>(params);
            query.remove("sessionId");
            list = List.of(query);
        }
        return Map.of("results", service.batchFindNodes(session, list));
    }
}
