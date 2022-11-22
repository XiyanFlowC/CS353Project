package org.cstp.meowtool.utils;

public class DaoUtil {
    private DaoUtil() {}

    public static Result uniqueUpdate (int ret) {
        if (ret == 1) return Result.succ(null);
        if (ret == 0) return Result.fail("unable to update database.");
        return Result.fail(-9002, "Internal server error: odd database.");
    }
}
