package com.alternacraft.aclib.extras.gui;

import java.util.List;

/**
 *
 * @author AlternaCraft
 */
public class GUIUtils {

    public static final String KEY_META = "key";

    public static final String CI_META = "ci";
    public static final String CI_PAGINATION_META = "cip";

    public static final String CLOSE_ACTION_META = "cam";
    public static final String TOWN_TO_BATTLE_META = "ttbm";
    public static final String SELECT_FIGHTERS_META = "sfm";
    public static final String SELECT_TOWN_META = "stm";
    public static final String ACCEPT_META = "am";
    public static final String DECLINE_META = "dm";
    public static final String NEXT_PAGE_META = "npm";
    public static final String PREVIOUS_PAGE_META = "ppm";

    public static int calculateSlot(int i, int j, int ROWS, int COLS) {
        if (i > 0 && i <= ROWS && j > 0 && j <= COLS) {
            return i * COLS - (COLS - j) - 1;
        } else {
            return -1;
        }
    }

    public static <T> List<T> paginate(List<T> list, int page, int max_slots) {
        int from = page * (max_slots - 1);
        int to = (page + 1) * (max_slots - 1);
        if (to > list.size()) {
            to = list.size();
        }
        return list.subList(from, to);
    }
}
