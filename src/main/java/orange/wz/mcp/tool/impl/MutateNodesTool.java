package orange.wz.mcp.tool.impl;

import orange.wz.mcp.service.McpWorkspaceService;
import orange.wz.mcp.session.McpSessionManager;
import orange.wz.mcp.tool.support.BaseSessionTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MutateNodesTool extends BaseSessionTool {
    private final McpWorkspaceService service;

    public MutateNodesTool(McpSessionManager sessionManager, McpWorkspaceService service) {
        super(sessionManager);
        this.service = service;
    }

    @Override
    public String name() {
        return "mutate_nodes";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> invoke(Map<String, Object> params) {
        var session = session(params);
        Object operations = params.get("operations");
        List<Map<String, Object>> list;
        if (operations instanceof List<?> raw) {
            list = (List<Map<String, Object>>) raw;
        } else {
            Map<String, Object> operation = new HashMap<>(params);
            operation.remove("sessionId");
            list = List.of(operation);
        }
        return Map.of("results", service.batchUpdateNodes(session, list));
    }
}
