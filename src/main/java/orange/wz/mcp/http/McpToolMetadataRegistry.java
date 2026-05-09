package orange.wz.mcp.http;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public final class McpToolMetadataRegistry {
    public List<McpToolDescriptor> all() {
        return List.of(
                new McpToolDescriptor("load_files", "加载 wz/img/xml 文件或目录到当前 MCP 会话。", objectSchema(
                        Map.of(
                                "paths", arraySchema(stringSchema()),
                                "key", keySchema()
                        ),
                        List.of("paths", "key")
                )),
                new McpToolDescriptor("unload_node", "卸载指定路径的节点。", objectSchema(
                        Map.of("path", stringSchema()),
                        List.of("path")
                )),
                new McpToolDescriptor("unload_all", "卸载当前会话中的全部已加载对象。", objectSchema(Map.of(), List.of())),
                new McpToolDescriptor("create_wz_file", "创建新的 wz 文件根节点。", objectSchema(
                        Map.of(
                                "fileName", stringSchema(),
                                "version", numberSchema(),
                                "key", keySchema()
                        ),
                        List.of("fileName", "key")
                )),
                new McpToolDescriptor("create_img_file", "创建新的 img 文件根节点。", objectSchema(
                        Map.of(
                                "fileName", stringSchema(),
                                "key", keySchema()
                        ),
                        List.of("fileName", "key")
                )),
                new McpToolDescriptor("list_children", "列出指定节点的直接子节点。", objectSchema(
                        Map.of(
                                "path", stringSchema(),
                                "autoParse", booleanSchema()
                        ),
                        List.of("path")
                )),
                new McpToolDescriptor("find_node", "按路径查找单个节点。", objectSchema(
                        Map.of(
                                "path", stringSchema(),
                                "autoParse", booleanSchema()
                        ),
                        List.of("path")
                )),
                new McpToolDescriptor("search_node", "按关键字搜索节点名称；设置 searchIn=value 时搜索节点值。", objectSchema(
                        Map.of(
                                "startPath", stringSchema(),
                                "keyword", stringSchema(),
                                "searchIn", stringSchema(),
                                "autoParse", booleanSchema()
                        ),
                        List.of("startPath", "keyword")
                )),
                new McpToolDescriptor("get_node_detail", "获取节点详情和值。", objectSchema(
                        Map.of(
                                "path", stringSchema(),
                                "autoParse", booleanSchema()
                        ),
                        List.of("path")
                )),
                new McpToolDescriptor("get_node_tree_json", "获取指定节点及其子节点的 JSON 树数据。", objectSchema(
                        Map.of(
                                "path", stringSchema(),
                                "autoParse", booleanSchema(),
                                "maxDepth", numberSchema()
                        ),
                        List.of("path")
                )),
                new McpToolDescriptor("create_child_node", "在指定父节点下创建子节点。", objectSchema(
                        Map.of(
                                "parentPath", stringSchema(),
                                "type", stringSchema(),
                                "name", stringSchema(),
                                "value", stringSchema(),
                                "x", numberSchema(),
                                "y", numberSchema(),
                                "base64Png", stringSchema(),
                                "base64Mp3", stringSchema(),
                                "pngFormat", stringSchema(),
                                "autoParse", booleanSchema()
                        ),
                        List.of("parentPath", "type", "name")
                )),
                new McpToolDescriptor("delete_node", "删除指定路径的节点。", objectSchema(
                        Map.of(
                                "path", stringSchema(),
                                "autoParse", booleanSchema()
                        ),
                        List.of("path")
                )),
                new McpToolDescriptor("copy_nodes", "复制一个或多个节点到当前会话剪贴板。", objectSchema(
                        Map.of(
                                "paths", arraySchema(stringSchema()),
                                "autoParse", booleanSchema()
                        ),
                        List.of("paths")
                )),
                new McpToolDescriptor("batch_find_nodes", "批量执行节点查询，推荐使用 op 驱动模式。支持 find_by_path、search_by_keyword、search_by_value、match_type，可选 includeTree。", objectSchema(
                        Map.of(
                                "queries", arraySchema(objectSchema(
                                        Map.of(
                                                "op", stringSchema(),
                                                "path", stringSchema(),
                                                "startPath", stringSchema(),
                                                "keyword", stringSchema(),
                                                "searchIn", stringSchema(),
                                                "type", stringSchema(),
                                                "includeTree", booleanSchema(),
                                                "maxDepth", numberSchema(),
                                                "autoParse", booleanSchema()
                                        ),
                                        List.of()
                                ))
                        ),
                        List.of("queries")
                )),
                new McpToolDescriptor("query_nodes", "统一节点查询入口。支持单次直接查询，也支持传 queries 批量查询。推荐 op: find_by_path、search_by_keyword、search_by_value、match_type。", objectSchema(
                        Map.ofEntries(
                                Map.entry("queries", arraySchema(objectSchema(
                                        Map.of(
                                                "op", stringSchema(),
                                                "path", stringSchema(),
                                                "startPath", stringSchema(),
                                                "keyword", stringSchema(),
                                                "searchIn", stringSchema(),
                                                "type", stringSchema(),
                                                "includeTree", booleanSchema(),
                                                "maxDepth", numberSchema(),
                                                "autoParse", booleanSchema()
                                        ),
                                        List.of()
                                ))),
                                Map.entry("op", stringSchema()),
                                Map.entry("path", stringSchema()),
                                Map.entry("startPath", stringSchema()),
                                Map.entry("keyword", stringSchema()),
                                Map.entry("searchIn", stringSchema()),
                                Map.entry("type", stringSchema()),
                                Map.entry("includeTree", booleanSchema()),
                                Map.entry("maxDepth", numberSchema()),
                                Map.entry("autoParse", booleanSchema())
                        ),
                        List.of()
                )),
                new McpToolDescriptor("paste_nodes", "将会话剪贴板内容粘贴到目标节点。", objectSchema(
                        Map.of(
                                "targetPath", stringSchema(),
                                "strategy", stringSchema(),
                                "autoParse", booleanSchema()
                        ),
                        List.of("targetPath")
                )),
                new McpToolDescriptor("batch_update_nodes", "批量修改节点，推荐使用 op 驱动模式。支持 rename、set_value、set_vector、set_png、set_sound。", objectSchema(
                        Map.of(
                                "operations", arraySchema(objectSchema(
                                        Map.of(
                                                "path", stringSchema(),
                                                "op", stringSchema(),
                                                "autoParse", booleanSchema(),
                                                "name", stringSchema(),
                                                "value", stringSchema(),
                                                "x", numberSchema(),
                                                "y", numberSchema(),
                                                "base64Png", stringSchema(),
                                                "base64Mp3", stringSchema(),
                                                "pngFormat", stringSchema()
                                        ),
                                        List.of("path")
                                ))
                        ),
                        List.of("operations")
                )),
                new McpToolDescriptor("mutate_nodes", "统一节点修改入口。支持单次直接修改，也支持传 operations 批量修改。推荐 op: rename、set_value、set_vector、set_png、set_sound。", objectSchema(
                        Map.ofEntries(
                                Map.entry("operations", arraySchema(objectSchema(
                                        Map.of(
                                                "path", stringSchema(),
                                                "op", stringSchema(),
                                                "autoParse", booleanSchema(),
                                                "name", stringSchema(),
                                                "value", stringSchema(),
                                                "x", numberSchema(),
                                                "y", numberSchema(),
                                                "base64Png", stringSchema(),
                                                "base64Mp3", stringSchema(),
                                                "pngFormat", stringSchema()
                                        ),
                                        List.of("path")
                                ))),
                                Map.entry("path", stringSchema()),
                                Map.entry("op", stringSchema()),
                                Map.entry("autoParse", booleanSchema()),
                                Map.entry("name", stringSchema()),
                                Map.entry("value", stringSchema()),
                                Map.entry("x", numberSchema()),
                                Map.entry("y", numberSchema()),
                                Map.entry("base64Png", stringSchema()),
                                Map.entry("base64Mp3", stringSchema()),
                                Map.entry("pngFormat", stringSchema())
                        ),
                        List.of()
                )),
                new McpToolDescriptor("save_node", "保存指定文件节点。", objectSchema(
                        Map.of(
                                "path", stringSchema(),
                                "autoParse", booleanSchema()
                        ),
                        List.of("path")
                )),
                new McpToolDescriptor("save_as", "将指定文件节点另存为到目标路径。", objectSchema(
                        Map.of(
                                "path", stringSchema(),
                                "filePath", stringSchema(),
                                "autoParse", booleanSchema()
                        ),
                        List.of("path", "filePath")
                ))
        );
    }

    private Map<String, Object> keySchema() {
        return objectSchema(
                Map.of(
                        "name", stringSchema(),
                        "ivBase64", stringSchema(),
                        "userKeyBase64", stringSchema()
                ),
                List.of("name", "ivBase64", "userKeyBase64")
        );
    }

    private Map<String, Object> objectSchema(Map<String, Object> properties, List<String> required) {
        return Map.of(
                "type", "object",
                "properties", properties,
                "required", required,
                "additionalProperties", true
        );
    }

    private Map<String, Object> stringSchema() {
        return Map.of("type", "string");
    }

    private Map<String, Object> numberSchema() {
        return Map.of("type", "number");
    }

    private Map<String, Object> booleanSchema() {
        return Map.of("type", "boolean");
    }

    private Map<String, Object> arraySchema(Map<String, Object> itemSchema) {
        return Map.of(
                "type", "array",
                "items", itemSchema
        );
    }
}
