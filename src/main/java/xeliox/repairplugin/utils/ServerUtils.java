package xeliox.repairplugin.utils;

import xeliox.repairplugin.RepairPlugin;

public class ServerUtils {

    public static boolean VersionIsNew() {
        VersionUtils versionUtils = RepairPlugin.versionUtils;
        if (versionUtils.serverVersionGreaterEqualThan(versionUtils,VersionUtils.v1_16_R1)) {
            return true;
        }
        return false;
    }
}
