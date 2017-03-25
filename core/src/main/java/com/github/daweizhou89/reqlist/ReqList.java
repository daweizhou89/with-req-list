package com.github.daweizhou89.reqlist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.daweizhou89.reqlist.interfaces.IMemoryCacheManager;

/**
 * Created by daweizhou89 on 2017/2/18.
 */
public class ReqList {

    private static IMemoryCacheManager sDataCacheManager ;

    private static int[] sSwipeColorSchemeColors;

    public static InitHelper customize() {
        return new InitHelper();
    }

    public static int[] getSwipeColorSchemeColors() {
        return sSwipeColorSchemeColors;
    }

    public static IMemoryCacheManager getDataCacheManager() {
        return sDataCacheManager;
    }

    static class EmptyMemoryCacheManager implements IMemoryCacheManager {
        @Override
        public String get(@NonNull String key) {
            return null;
        }

        @Override
        public void put(@NonNull String key, @Nullable String data) {}
    }

    public static class InitHelper {

        int[] swipeColorSchemeColors;
        IMemoryCacheManager dataCacheManager;

        private InitHelper() {
        }

        public InitHelper setSwipeColorSchemeColors(int[] swipeColorSchemeColors) {
            this.swipeColorSchemeColors = swipeColorSchemeColors;
            return this;
        }

        public InitHelper setDataCacheManager(IMemoryCacheManager dataCacheManager) {
            this.dataCacheManager = dataCacheManager;
            return this;
        }

        public void init() {
            sSwipeColorSchemeColors = swipeColorSchemeColors;
            sDataCacheManager = dataCacheManager;
        }
    }

}
