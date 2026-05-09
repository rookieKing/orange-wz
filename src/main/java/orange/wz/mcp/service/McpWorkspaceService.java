package orange.wz.mcp.service;

import orange.wz.mcp.dto.NodeSummary;
import orange.wz.mcp.dto.NodeDetail;
import orange.wz.mcp.dto.OverwriteStrategy;
import orange.wz.mcp.session.McpSessionState;
import orange.wz.provider.WzObject;
import orange.wz.provider.tools.wzkey.WzKey;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface McpWorkspaceService {
    void loadFiles(McpSessionState session, List<File> files, WzKey key);

    void unloadByPath(McpSessionState session, String path);

    void unloadAll(McpSessionState session);

    NodeSummary createWz(McpSessionState session, String fileName, short version, WzKey key);

    NodeSummary createImg(McpSessionState session, String fileName, WzKey key);

    WzObject findByPath(McpSessionState session, String path, boolean autoParse);

    List<NodeSummary> listChildren(McpSessionState session, String path, boolean autoParse);

    void copyByPaths(McpSessionState session, List<String> paths, boolean autoParse);

    List<NodeSummary> pasteToPath(McpSessionState session, String targetPath, OverwriteStrategy strategy, boolean autoParse);

    NodeSummary createChildNode(
            McpSessionState session,
            String parentPath,
            String type,
            String name,
            String value,
            Integer x,
            Integer y,
            String base64Png,
            String base64Mp3,
            String pngFormat,
            boolean autoParse
    );

    void deleteNode(McpSessionState session, String path, boolean autoParse);

    List<NodeSummary> searchNodeByName(McpSessionState session, String startPath, String keyword, boolean autoParse);

    List<Map<String, Object>> searchNodeByValue(McpSessionState session, String startPath, String keyword, boolean autoParse);

    NodeDetail getNodeDetail(McpSessionState session, String path, boolean autoParse);

    Map<String, Object> getNodeTreeJson(McpSessionState session, String path, boolean autoParse, int maxDepth);

    List<Map<String, Object>> batchFindNodes(McpSessionState session, List<Map<String, Object>> queries);

    List<Map<String, Object>> batchUpdateNodes(McpSessionState session, List<Map<String, Object>> operations);

    void saveNode(McpSessionState session, String path, boolean autoParse);

    void saveNodeAs(McpSessionState session, String path, String filePath, boolean autoParse);
}
