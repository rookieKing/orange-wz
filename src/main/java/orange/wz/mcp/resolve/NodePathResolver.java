package orange.wz.mcp.resolve;

import orange.wz.mcp.support.McpException;
import orange.wz.provider.WzDirectory;
import orange.wz.provider.WzImage;
import orange.wz.provider.WzImageProperty;
import orange.wz.provider.WzObject;

import java.util.List;

public final class NodePathResolver {
    public WzObject resolveFromRoots(List<WzObject> roots, String path, boolean autoParse) {
        if (path == null || path.isBlank()) {
            throw new McpException("path 不能为空");
        }
        String[] parts = path.split("/");
        if (parts.length == 0) {
            throw new McpException("无效 path: " + path);
        }

        WzObject current = roots.stream()
                .filter(root -> root.getName().equals(parts[0]))
                .findFirst()
                .orElseThrow(() -> new McpException("找不到根节点: " + parts[0]));

        for (int i = 1; i < parts.length; i++) {
            String name = parts[i];
            current = findChild(current, name, autoParse);
            if (current == null) {
                throw new McpException("路径不存在: " + path);
            }
        }
        return current;
    }

    private WzObject findChild(WzObject parent, String childName, boolean autoParse) {
        if (parent instanceof WzDirectory dir) {
            if (dir.isWzFile() && autoParse && !dir.getWzFile().parse()) {
                throw new McpException("WZ 文件解析失败: " + dir.getName());
            }
            WzDirectory subDir = dir.getDirectory(childName);
            if (subDir != null) return subDir;
            return dir.getImage(childName);
        }
        if (parent instanceof WzImage image) {
            if (autoParse && !image.parse()) {
                throw new McpException("IMG 解析失败: " + image.getName());
            }
            return image.getChild(childName);
        }
        if (parent instanceof WzImageProperty prop && prop.isListProperty()) {
            return prop.getChild(childName);
        }
        return null;
    }
}
