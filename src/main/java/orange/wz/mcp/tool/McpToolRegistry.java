package orange.wz.mcp.tool;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class McpToolRegistry {
    private final Map<String, McpTool> tools = new ConcurrentHashMap<>();

    public void register(McpTool tool) {
        tools.put(tool.name(), tool);
    }

    public McpTool get(String name) {
        return tools.get(name);
    }

    public Collection<McpTool> all() {
        return tools.values();
    }
}
