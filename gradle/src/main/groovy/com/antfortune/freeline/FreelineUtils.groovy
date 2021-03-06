package com.antfortune.freeline

import groovy.json.JsonSlurper

/**
 * Created by huangyong on 16/7/19.
 */
class FreelineUtils {

    public static String getRelativePath(File root, File target) {
        String path = target.absolutePath.replace(root.absolutePath, "")
        while (path.startsWith("/") || (path.startsWith("\\"))) {
            path = path.substring(1)
        }
        return path
    }

    public static String getFreelineCacheDir(String rootDirPath) {
        String projectCacheDir = FreelineGenerator.generateStringMD5(rootDirPath)
        def freelineCacheDir = new File(FreelineUtils.joinPath(System.properties['user.home'].toString(), ".freeline", "cache", projectCacheDir))
        if (!freelineCacheDir.exists() || !freelineCacheDir.isDirectory()) {
            freelineCacheDir.mkdirs()
        }
        return freelineCacheDir.absolutePath
    }

    public static String getBuildCacheDir(String buildDirPath) {
        def buildCacheDir = new File(buildDirPath, Constants.FREELINE_BUILD_CACHE_DIR)
        if (!buildCacheDir.exists() || !buildCacheDir.isDirectory()) {
            buildCacheDir.mkdirs()
        }
        return buildCacheDir.absolutePath
    }

    public static String getBuildAssetsDir(String buildDirPath) {
        def buildAssetsDir = new File(getBuildCacheDir(buildDirPath), "freeline-assets")
        if (!buildAssetsDir.exists() || !buildAssetsDir.isDirectory()) {
            buildAssetsDir.mkdirs()
        }
        return buildAssetsDir.absolutePath
    }

    public static def getJson(String url) {
        return new JsonSlurper().parseText(new URL(url).text)
    }

    public static boolean saveJson(String json, String fileName, boolean override) {
        def pending = new File(fileName)
        if (pending.exists() && pending.isFile()) {
            if (override) {
                println "Old file $pending.absolutePath removed."
                pending.delete()
            } else {
                println "File $pending.absolutePath exists."
                return false
            }
        }

        pending << json
        println "Save to $pending.absolutePath"
        return true
    }

    public static String joinPath(String... sep) {
        if (sep.length == 0) {
            return "";
        }
        if (sep.length == 1) {
            return sep[0];
        }

        return new File(sep[0], joinPath(Arrays.copyOfRange(sep, 1, sep.length))).getPath();
    }

    public static String getOsName() {
        return System.getProperty("os.name");
    }

    public static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }

}
