package com.github.daweizhou89.reqlist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by daweizhou89 on 2017/2/18.
 */
public class ReqList {

    private static IDataCacheManager sDataCacheManager = new EmptyDataCacheManager();

    private static int[] sSwipeColorSchemeColors;

    public static InitHelper customize() {
        return new InitHelper();
    }

    public static int[] getSwipeColorSchemeColors() {
        return sSwipeColorSchemeColors;
    }

    public static IDataCacheManager getDataCacheManager() {
        return sDataCacheManager;
    }

    static class EmptyDataCacheManager implements IDataCacheManager {
        @Override
        public String get(@NonNull String key) {
            return null;
        }

        @Override
        public void put(@NonNull String key, @Nullable String data) {

        }
    }

    public static class InitHelper {

        int[] swipeColorSchemeColors;
        IDataCacheManager dataCacheManager;

        private InitHelper() {
        }

        public InitHelper setSwipeColorSchemeColors(int[] swipeColorSchemeColors) {
            this.swipeColorSchemeColors = swipeColorSchemeColors;
            return this;
        }

        public InitHelper setDataCacheManager(IDataCacheManager dataCacheManager) {
            this.dataCacheManager = dataCacheManager;
            return this;
        }

        public void init() {
            sSwipeColorSchemeColors = swipeColorSchemeColors;
            sDataCacheManager = dataCacheManager;
        }
    }

}
