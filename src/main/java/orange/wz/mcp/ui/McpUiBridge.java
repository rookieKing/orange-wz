package orange.wz.mcp.ui;

import orange.wz.gui.MainFrame;
import orange.wz.gui.component.panel.EditPane;
import orange.wz.mcp.dto.NodeDetail;
import orange.wz.mcp.dto.NodeSummary;
import orange.wz.mcp.session.McpSessionState;
import orange.wz.provider.WzObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public final class McpUiBridge {
    @Value("${orange.gui.enabled:true}")
    private boolean guiEnabled;

    public void syncSessionRoots(McpSessionState session) {
        if (!isAvailable()) {
            return;
        }
        runOnEdt(() -> {
            EditPane editPane = leftPane();
            session.lock();
            try {
                session.getRoots().clear();
                session.getRoots().addAll(editPane.snapshotRootObjects());
            } finally {
                session.unlock();
            }
        });
    }

    public void beforeToolCall(String toolName, Map<String, Object> arguments) {
        if (!isAvailable()) {
            return;
        }
        String message = switch (toolName) {
            case "load_files" -> "MCP 正在加载文件...";
            case "unload_node" -> "MCP 正在卸载节点...";
            case "unload_all" -> "MCP 正在清空工作区...";
            case "create_wz_file" -> "MCP 正在创建 WZ 文件...";
            case "create_img_file" -> "MCP 正在创建 IMG 文件...";
            case "create_child_node" -> "MCP 正在创建子节点...";
            case "delete_node" -> "MCP 正在删除节点...";
            case "copy_nodes" -> "MCP 正在复制节点...";
            case "paste_nodes" -> "MCP 正在粘贴节点...";
            case "save_node" -> "MCP 正在保存文件...";
            case "save_as" -> "MCP 正在另存为...";
            case "search_node" -> "MCP 正在搜索节点...";
            case "find_node" -> "MCP 正在查找节点...";
            case "query_nodes" -> "MCP 正在执行统一查询...";
            case "get_node_detail" -> "MCP 正在读取节点详情...";
            case "list_children" -> "MCP 正在列出子节点...";
            case "batch_update_nodes", "mutate_nodes" -> "MCP 正在修改节点...";
            default -> "MCP 正在执行 " + toolName + "...";
        };
        runOnEdt(() -> {
            prepareNodeTargets(leftPane(), arguments);
            MainFrame frame = frame();
            frame.updateProgress(0, 0);
            frame.setStatusTextDirect(message);
        });
    }

    @SuppressWarnings("unchecked")
    public void afterToolCall(String toolName, Map<String, Object> arguments, Map<String, Object> result) {
        if (!isAvailable()) {
            return;
        }
        runOnEdt(() -> {
            EditPane editPane = leftPane();
            MainFrame frame = frame();
            switch (toolName) {
                case "load_files", "create_wz_file", "create_img_file" -> syncAllRoots(editPane, arguments);
                case "unload_all" -> editPane.unloadAll();
                case "unload_node", "delete_node" -> {
                    removeNode(editPane, (String) arguments.get("path"));
                    focusParent(editPane, (String) arguments.get("path"));
                }
                case "create_child_node" -> {
                    insertChild(editPane, extractNode(result), (String) arguments.get("parentPath"));
                    focusPath(editPane, extractNodePath(result));
                }
                case "paste_nodes" -> {
                    insertPasted(editPane, (List<Object>) result.get("pasted"), (String) arguments.get("targetPath"));
                    focusFirstPasted(editPane, result);
                }
                case "find_node", "search_node" -> focusPath(editPane, extractNodePath(result));
                case "get_node_detail" -> focusPath(editPane, extractNodePath(result) != null ? extractNodePath(result) : stringArg(arguments, "path"));
                case "list_children" -> focusPath(editPane, stringArg(arguments, "path"));
                case "batch_update_nodes", "mutate_nodes" -> focusFirstUpdated(editPane, result);
                case "query_nodes" -> focusFirstNodeFromResults(editPane, result);
                case "save_node", "save_as" -> editPane.reloadFilePreservingState(stringArg(arguments, "path"));
                default -> {
                }
            }
            frame.updateProgress(0, 0);
            frame.setStatusTextDirect(successMessage(toolName, result));
            editPane.getTree().updateUI();
        });
    }

    private void syncAllRoots(EditPane editPane, Map<String, Object> arguments) {
        Object sessionRoots = arguments.get("__sessionRoots");
        if (!(sessionRoots instanceof List<?> roots)) {
            return;
        }
        editPane.unloadAll();
        for (Object obj : roots) {
            if (obj instanceof WzObject wzObject) {
                editPane.insertNodeToTree(editPane.getTreeRoot(), wzObject, true);
            }
        }
    }

    private void removeNode(EditPane editPane, String path) {
        if (path == null) return;
        editPane.ensureRootNodeParsed(path);
        DefaultMutableTreeNode node = editPane.findTreeNodeByPath(path);
        if (node != null) {
            editPane.removeNodeFromTree(node);
            editPane.resetValueForm();
        }
    }

    private void insertChild(EditPane editPane, NodeSummary nodeSummary, String parentPath) {
        if (nodeSummary == null || parentPath == null) {
            return;
        }
        editPane.ensureRootNodeParsed(parentPath);
        DefaultMutableTreeNode parentNode = editPane.findTreeNodeByPath(parentPath);
        if (parentNode == null) {
            return;
        }
        WzObject parentObj = (WzObject) parentNode.getUserObject();
        WzObject childObj = findChildObject(parentObj, nodeSummary.name());
        if (childObj != null && editPane.findTreeNodeByPath(nodeSummary.path()) == null) {
            editPane.insertNodeToTree(parentNode, childObj, true);
        }
    }

    private void insertPasted(EditPane editPane, List<Object> pastedItems, String targetPath) {
        if (pastedItems == null || targetPath == null) {
            return;
        }
        for (Object item : pastedItems) {
            if (item instanceof NodeSummary nodeSummary) {
                insertChild(editPane, nodeSummary, targetPath);
                continue;
            }
            if (item instanceof Map<?, ?> map) {
                Object name = map.get("name");
                Object path = map.get("path");
                if (name instanceof String n && path instanceof String p) {
                    insertChild(editPane, new NodeSummary(n, p, null), targetPath);
                }
            }
        }
    }

    private void focusPath(EditPane editPane, String path) {
        if (path == null || path.isBlank()) {
            return;
        }
        editPane.focusNodeByPath(path);
    }

    private void focusParent(EditPane editPane, String path) {
        if (path == null) {
            return;
        }
        int index = path.lastIndexOf('/');
        if (index <= 0) {
            return;
        }
        focusPath(editPane, path.substring(0, index));
    }

    private NodeSummary extractNode(Map<String, Object> result) {
        Object raw = result.get("node");
        if (raw instanceof NodeSummary nodeSummary) {
            return nodeSummary;
        }
        if (raw instanceof Map<?, ?> map) {
            Object name = map.get("name");
            Object path = map.get("path");
            if (name instanceof String n && path instanceof String p) {
                return new NodeSummary(n, p, null);
            }
        }
        Object detail = result.get("detail");
        if (detail instanceof NodeDetail nodeDetail) {
            return nodeDetail.node();
        }
        if (detail instanceof Map<?, ?> detailMap) {
            Object rawNode = detailMap.get("node");
            if (rawNode instanceof NodeSummary nodeSummary) {
                return nodeSummary;
            }
            if (rawNode instanceof Map<?, ?> nodeMap) {
                Object name = nodeMap.get("name");
                Object path = nodeMap.get("path");
                if (name instanceof String n && path instanceof String p) {
                    return new NodeSummary(n, p, null);
                }
            }
        }
        return null;
    }

    private String extractNodePath(Map<String, Object> result) {
        NodeSummary node = extractNode(result);
        return node == null ? null : node.path();
    }

    private String successMessage(String toolName, Map<String, Object> result) {
        return switch (toolName) {
            case "load_files" -> "MCP 文件加载完成";
            case "unload_node" -> "MCP 节点卸载完成";
            case "unload_all" -> "MCP 工作区已清空";
            case "create_wz_file" -> "MCP WZ 文件创建完成";
            case "create_img_file" -> "MCP IMG 文件创建完成";
            case "create_child_node" -> "MCP 子节点创建完成";
            case "delete_node" -> "MCP 节点删除完成";
            case "copy_nodes" -> "MCP 节点复制完成";
            case "paste_nodes" -> "MCP 节点粘贴完成";
            case "save_node" -> "MCP 文件保存完成";
            case "save_as" -> "MCP 文件另存为完成";
            case "search_node" -> "MCP 节点搜索完成";
            case "find_node" -> "MCP 节点查找完成";
            case "query_nodes" -> "MCP 统一查询完成";
            case "get_node_detail" -> "MCP 节点详情读取完成";
            case "list_children" -> "MCP 子节点列出完成";
            case "batch_update_nodes" -> "MCP 批量修改完成";
            case "mutate_nodes" -> "MCP 统一修改完成";
            default -> "MCP 执行完成";
        };
    }

    @SuppressWarnings("unchecked")
    private void focusFirstNodeFromResults(EditPane editPane, Map<String, Object> result) {
        Object rawResults = result.get("results");
        if (!(rawResults instanceof List<?> results)) {
            return;
        }
        for (Object item : results) {
            if (!(item instanceof Map<?, ?> map)) {
                continue;
            }
            Object matchesObj = map.get("matches");
            if (!(matchesObj instanceof List<?> matches) || matches.isEmpty()) {
                continue;
            }
            Object first = matches.get(0);
            if (first instanceof NodeSummary nodeSummary) {
                focusPath(editPane, nodeSummary.path());
                return;
            }
            if (first instanceof Map<?, ?> nodeMap) {
                Object name = nodeMap.get("name");
                Object path = nodeMap.get("path");
                if (name instanceof String n && path instanceof String p) {
                    focusPath(editPane, new NodeSummary(n, p, null).path());
                    return;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void focusFirstUpdated(EditPane editPane, Map<String, Object> result) {
        Object rawResults = result.get("results");
        if (!(rawResults instanceof List<?> results)) {
            return;
        }
        for (Object item : results) {
            if (!(item instanceof Map<?, ?> map)) {
                continue;
            }
            Object rawNode = map.get("node");
            if (rawNode instanceof NodeSummary nodeSummary) {
                focusPath(editPane, nodeSummary.path());
                return;
            }
            Object path = map.get("path");
            if (path instanceof String p) {
                focusPath(editPane, p);
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void focusFirstPasted(EditPane editPane, Map<String, Object> result) {
        Object rawPasted = result.get("pasted");
        if (!(rawPasted instanceof List<?> pasted) || pasted.isEmpty()) {
            return;
        }
        Object first = pasted.get(0);
        if (first instanceof NodeSummary nodeSummary) {
            focusPath(editPane, nodeSummary.path());
            return;
        }
        if (first instanceof Map<?, ?> map) {
            Object path = map.get("path");
            if (path instanceof String p) {
                focusPath(editPane, p);
            }
        }
    }

    private WzObject findChildObject(WzObject parent, String childName) {
        if (parent == null || childName == null) {
            return null;
        }
        if (parent instanceof orange.wz.provider.WzDirectory dir) {
            WzObject subDir = dir.getDirectory(childName);
            if (subDir != null) return subDir;
            return dir.getImage(childName);
        }
        if (parent instanceof orange.wz.provider.WzImage image) {
            return image.getChild(childName);
        }
        if (parent instanceof orange.wz.provider.WzImageProperty prop) {
            return prop.getChild(childName);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void prepareNodeTargets(EditPane editPane, Map<String, Object> arguments) {
        Set<String> paths = new LinkedHashSet<>();
        collectPaths(arguments, paths);
        for (String path : paths) {
            editPane.ensureRootNodeParsed(path);
        }
    }

    @SuppressWarnings("unchecked")
    private void collectPaths(Map<String, Object> source, Set<String> collector) {
        if (source == null) {
            return;
        }
        addPath(collector, source.get("path"));
        addPath(collector, source.get("startPath"));
        addPath(collector, source.get("parentPath"));
        addPath(collector, source.get("targetPath"));

        Object rawPaths = source.get("paths");
        if (rawPaths instanceof List<?> paths) {
            for (Object item : paths) {
                addPath(collector, item);
            }
        }

        Object rawQueries = source.get("queries");
        if (rawQueries instanceof List<?> queries) {
            for (Object query : queries) {
                if (query instanceof Map<?, ?> map) {
                    collectPaths((Map<String, Object>) map, collector);
                }
            }
        }

        Object rawOperations = source.get("operations");
        if (rawOperations instanceof List<?> operations) {
            for (Object operation : operations) {
                if (operation instanceof Map<?, ?> map) {
                    collectPaths((Map<String, Object>) map, collector);
                }
            }
        }
    }

    private void addPath(Set<String> collector, Object value) {
        if (value instanceof String path && !path.isBlank()) {
            collector.add(path);
        }
    }

    private String stringArg(Map<String, Object> arguments, String key) {
        Object value = arguments.get(key);
        return value instanceof String text ? text : null;
    }

    private boolean isAvailable() {
        return guiEnabled && !java.awt.GraphicsEnvironment.isHeadless();
    }

    private MainFrame frame() {
        return MainFrame.getInstance();
    }

    private EditPane leftPane() {
        return frame().getCenterPane().getLeftEditPane();
    }

    private void runOnEdt(Runnable action) {
        if (SwingUtilities.isEventDispatchThread()) {
            action.run();
            return;
        }
        try {
            SwingUtilities.invokeAndWait(action);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }
}
