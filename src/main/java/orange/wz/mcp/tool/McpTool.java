package orange.wz.mcp.tool;

import java.util.Map;

public interface McpTool {
    String name();

    Map<String, Object> invoke(Map<String, Object> params);
}
