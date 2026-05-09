package orange.wz.mcp.dto;

import orange.wz.provider.WzObject;
import orange.wz.provider.tools.WzType;

public record NodeSummary(
        String name,
        String path,
        WzType type
) {
    public static NodeSummary from(WzObject obj) {
        return new NodeSummary(obj.getName(), obj.getPath(), obj.getType());
    }
}
