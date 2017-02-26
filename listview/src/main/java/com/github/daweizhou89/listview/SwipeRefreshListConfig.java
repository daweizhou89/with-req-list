package com.github.daweizhou89.listview;

/**
 * Created by daweizhou89 on 2017/2/18.
 */
public class SwipeRefreshListConfig {

    private static int[] sSwipeColorSchemeColors;

    private static int sRecyclerListViewResources = R.layout.recycler_list_view;

    public static InitHelper customize() {
        return new InitHelper();
    }

    public static int[] getSwipeColorSchemeColors() {
        return sSwipeColorSchemeColors;
    }

    public static int getRecyclerListViewResources() {
        return sRecyclerListViewResources;
    }

    public static class InitHelper {

        int[] swipeColorSchemeColors;

        int recyclerListViewResources;

        private InitHelper() {
        }

        public InitHelper setSwipeColorSchemeColors(int[] swipeColorSchemeColors) {
            this.swipeColorSchemeColors = swipeColorSchemeColors;
            return this;
        }

        public InitHelper setRecyclerListViewResources(int recyclerListViewResources) {
            this.recyclerListViewResources = recyclerListViewResources;
            return this;
        }

        public void init() {
            sSwipeColorSchemeColors = swipeColorSchemeColors;
            if (recyclerListViewResources > 0) {
                sRecyclerListViewResources = recyclerListViewResources;
            }
        }
    }

}
