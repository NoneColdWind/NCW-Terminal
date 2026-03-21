package cn.ncw.javafx.ncwjavafx.base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarResourceCopier {

    /**
     * 复制单个资源文件
     * @param resourcePath JAR 中的资源路径 (如: /config/settings.properties)
     * @param outputDir 输出目录
     */
    public static void copyResource(String resourcePath, Path outputDir) {
        // 获取规范化路径
        if (!resourcePath.startsWith("/")) {
            resourcePath = "/" + resourcePath;
        }

        // 创建目标文件路径
        Path outputFile = resolveOutputPath(resourcePath, outputDir);

        try {
            // 从 JAR 获取资源流
            try (InputStream in = getResourceAsStream(resourcePath)) {
                if (in == null) {
                    throw new FileNotFoundException("资源未找到: " + resourcePath);
                }

                // 创建目录（如果不存在）
                Files.createDirectories(outputFile.getParent());

                // 复制文件内容
                Files.copy(in, outputFile, StandardCopyOption.REPLACE_EXISTING);

                // 保留原始文件属性
                preserveOriginalAttributes(resourcePath, outputFile);
            }
        } catch (Exception e) {
            handleException(resourcePath, outputFile.toString(), e);
        }
    }

    /**
     * 复制整个目录
     * @param directoryPath JAR 中的目录路径 (如: /templates)
     * @param outputDir 输出目录
     */
    public static void copyDirectory(String directoryPath, Path outputDir) {
        try {
            // 确保目录路径格式正确
            directoryPath = directoryPath.endsWith("/") ? directoryPath : directoryPath + "/";
            if (!directoryPath.startsWith("/")) directoryPath = "/" + directoryPath;

            // 获取 JAR 文件位置
            URL jarUrl = JarResourceCopier.class.getResource(directoryPath);
            if (jarUrl == null) {
                throw new FileNotFoundException("目录未找到: " + directoryPath);
            }

            if ("jar".equals(jarUrl.getProtocol())) {
                // 处理 JAR 中的目录
                String jarPath = jarUrl.getPath().split("!")[0].replaceFirst("file:", "");
                try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))) {
                    // 遍历所有条目
                    String finalDirectoryPath = directoryPath;
                    String finalDirectoryPath1 = directoryPath;
                    jar.stream().filter(entry -> !entry.isDirectory())
                            .filter(entry -> entry.getName().startsWith(finalDirectoryPath.substring(1)))
                            .forEach(entry -> copyJarEntry(jar, entry, finalDirectoryPath1, outputDir));
                }
            } else {
                // 处理文件系统（开发环境）
                Path sourceDir = Paths.get(jarUrl.toURI());
                Files.walkFileTree(sourceDir, new RecursiveCopier(sourceDir, outputDir));
            }
        } catch (Exception e) {
            handleException(directoryPath, outputDir.toString(), e);
        }
    }

    // -------------------------- 辅助方法 --------------------------

    /**
     * 解析输出路径
     */
    private static Path resolveOutputPath(String resourcePath, Path outputDir) {
        // 提取文件名
        String fileName = resourcePath.substring(resourcePath.lastIndexOf('/') + 1);
        return outputDir.resolve(fileName);
    }

    /**
     * 获取资源流（兼容各种环境）
     */
    private static InputStream getResourceAsStream(String resourcePath) {
        InputStream in = JarResourceCopier.class.getResourceAsStream(resourcePath);
        if (in == null) {
            // 尝试使用类加载器
            in = ClassLoader.getSystemResourceAsStream(resourcePath.startsWith("/") ?
                    resourcePath.substring(1) : resourcePath);
        }
        return in;
    }

    /**
     * 保留原始文件属性
     */
    private static void preserveOriginalAttributes(String resourcePath, Path outputFile) {
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            try {
                // 尝试获取原始文件属性
                String jarPath = JarResourceCopier.class.getProtectionDomain()
                        .getCodeSource().getLocation().toURI().getPath();

                try (JarFile jar = new JarFile(jarPath)) {
                    JarEntry entry = jar.getJarEntry(resourcePath.substring(1));
                    if (entry != null) {
                        // 设置执行权限（如果是可执行文件）
                        if (isExecutableFile(resourcePath)) {
                            Set<PosixFilePermission> perms = new HashSet<>(
                                    Files.getPosixFilePermissions(outputFile)
                            );
                            perms.add(PosixFilePermission.OWNER_EXECUTE);
                            Files.setPosixFilePermissions(outputFile, perms);
                        }

                        // 保留最后修改时间
                        Files.setLastModifiedTime(outputFile,
                                FileTime.fromMillis(entry.getTime()));
                    }
                }
            } catch (Exception e) {
                // 忽略错误，继续执行
            }
        }
    }

    /**
     * 判断是否是可执行文件
     */
    private static boolean isExecutableFile(String resourcePath) {
        String name = resourcePath.toLowerCase();
        return name.endsWith(".sh") || name.endsWith(".py") ||
                name.endsWith(".pl") || name.endsWith(".exe") ||
                name.endsWith(".bat") || name.endsWith(".run");
    }

    /**
     * 复制 JAR 条目
     */
    private static void copyJarEntry(JarFile jar, JarEntry entry, String directoryPath, Path outputDir) {
        try {
            String entryName = entry.getName();

            // 提取相对路径
            String relativePath = entryName.substring(directoryPath.length() - 1);

            // 创建目标路径
            Path outputFile = outputDir.resolve(relativePath);
            Files.createDirectories(outputFile.getParent());

            // 复制文件内容
            try (InputStream in = jar.getInputStream(entry)) {
                Files.copy(in, outputFile, StandardCopyOption.REPLACE_EXISTING);

                // 保留最后修改时间
                Files.setLastModifiedTime(outputFile,
                        FileTime.fromMillis(entry.getTime()));
            }
        } catch (Exception e) {
            System.err.println("复制条目失败: " + entry.getName());
            e.printStackTrace();
        }
    }

    /**
     * 异常处理
     */
    private static void handleException(String resourcePath, String outputPath, Exception e) {
        System.err.printf("复制失败: %s -> %s%n", resourcePath, outputPath);
        System.err.println("原因: " + e.getMessage());

        if (e instanceof FileNotFoundException) {
            System.err.println("请检查: ");
            System.err.println("1. 资源路径是否正确（区分大小写）");
            System.err.println("2. 资源是否已打包到 JAR 中");
            System.err.println("3. 资源路径是否使用绝对路径（以 / 开头）");
        } else if (e instanceof AccessDeniedException) {
            System.err.println("请检查: ");
            System.err.println("1. 是否有目标目录的写入权限");
            System.err.println("2. 文件是否被其他进程锁定");
        } else {
            e.printStackTrace();
        }
    }

    // 用于文件系统目录的递归复制器
    private static class RecursiveCopier extends SimpleFileVisitor<Path> {
        private final Path sourceDir;
        private final Path targetDir;

        public RecursiveCopier(Path sourceDir, Path targetDir) {
            this.sourceDir = sourceDir;
            this.targetDir = targetDir;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            Path relative = sourceDir.relativize(dir);
            Path target = targetDir.resolve(relative);
            Files.createDirectories(target);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Path relative = sourceDir.relativize(file);
            Path target = targetDir.resolve(relative);
            Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
            return FileVisitResult.CONTINUE;
        }
    }
}