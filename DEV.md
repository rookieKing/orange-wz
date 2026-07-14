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