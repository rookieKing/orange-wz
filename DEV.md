# script-win 脚本说明

目录：[script-win/](./script-win)

## 1) build.bat

文件：[build.bat](./script-win/build.bat)

作用：本地开发构建（轻量包）。

执行内容：
- 切到项目根目录
- 执行 `mvnw.cmd -DskipTests -Plocal clean package`
- 停留窗口（`pause`）

说明：
- `-Plocal` 会跳过复制 JRE、跳过 zip 打包，适合本机开发。

## 2) start.bat

文件：[start.bat](./script-win/start.bat)

作用：以 Maven 方式直接启动应用（开发模式）。

执行内容：
- 切到项目根目录
- 执行 `mvnw.cmd spring-boot:run`
- 停留窗口（`pause`）

说明：
- 适合改代码后快速启动验证。
- 首次运行会下载依赖，耗时会更长。

## 3) run.target.bat

文件：[run.target.bat](./script-win/run.target.bat)

作用：运行 `target` 下已打包产物（不走 Maven）。

执行内容：
- 切到 `target` 目录
- 执行 `java -cp "OrzRepacker.jar.original;lib\\*" orange.wz.OrangeWzApplication`
- 停留窗口（`pause`）

前置条件：
- 已先执行 [build.bat](./script-win/build.bat) 或手动打包生成：
  - `target/OrzRepacker.jar.original`
  - `target/lib/*`
- 本机已安装并可直接使用 `java` 命令。

## 推荐流程

1. 开发构建：运行 [build.bat](./script-win/build.bat)  
2. 运行产物：运行 [run.target.bat](./script-win/run.target.bat)  
3. 仅调试启动：运行 [start.bat](./script-win/start.bat)

## Xml2ImgCli 使用说明

源码位置：[Xml2ImgCli.java](./src/main/java/orange/wz/cli/Xml2ImgCli.java)

作用：将 XML 目录导出为 `.img` 目录（支持并行处理）。

### 参数

- `--input <dir>`：输入目录（必填）
- `--output <dir>`：输出目录（必填）
- `--key-id <int>`：密钥 ID（必填）
- `--jobs <int>`：并行线程数，可选，范围 `1~64`
- `--overwrite`：输出目录存在时覆盖
- `--help`：显示帮助

### 密钥说明

密钥读取自项目根目录下的 `keys.dat`。默认密钥由 [WzKeyStorage.java](./src/main/java/orange/wz/provider/tools/wzkey/WzKeyStorage.java) 初始化：

- `1`：国际服务器(低版本)
- `2`：亚洲服务器(低版本)
- `3`：新版本客户端

### 先构建

在项目根目录执行：

```bat
mvnw.cmd -DskipTests -Plocal clean package
```

### 运行示例

在项目根目录执行：

```bat
java -cp "target\classes;target\lib\*" orange.wz.cli.Xml2ImgCli --input "D:\wz-xml" --output "D:\wz-img" --key-id 3 --jobs 8 --overwrite
```

或仅查看帮助：

```bat
java -cp "target\classes;target\lib\*" orange.wz.cli.Xml2ImgCli --help
```

### 常见报错

- `缺少必填参数: --input/--output/--key-id`：补齐必填参数
- `输入目录不存在`：检查 `--input` 路径
- `输出目录已存在，请加 --overwrite`：追加 `--overwrite`
- `找不到密钥，请使用 --key-id`：确认 `keys.dat` 存在且 `--key-id` 正确