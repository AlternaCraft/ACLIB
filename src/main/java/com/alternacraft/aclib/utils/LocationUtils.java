/*
 * Copyright (C) 2016 AlternaCraft
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.alternacraft.aclib.utils;

import org.bukkit.block.BlockFace;

/**
 * This class contains some utils for Location objects.
 *
 * @author AlternaCraft
 */
public class LocationUtils {

    private static final short VARIATION = 45;
    private static final double CIRCUNFERENCE = 360;
    private static final double BEGIN = 360;
    private static final double END = CIRCUNFERENCE - VARIATION;

    private static final BlockFace[] BFACES = {
        BlockFace.NORTH,
        BlockFace.NORTH_EAST,
        BlockFace.EAST,
        BlockFace.SOUTH_EAST,
        BlockFace.SOUTH,
        BlockFace.SOUTH_WEST,
        BlockFace.WEST,
        BlockFace.NORTH_WEST,
        BlockFace.NORTH
    };

    /**
     * Returns the facing direction from an specified yaw.
     *
     * @param bf
     * @return
     */
    public static float blockFaceToYaw(BlockFace bf) {
        switch (bf) {
            case NORTH:
                return 180f;
            case NORTH_EAST:
                return 135f;
            case EAST:
                return 90f;
            case SOUTH_EAST:
                return 45f;
            case SOUTH:
                return 360f;
            case SOUTH_WEST:
                return 315f;
            case WEST:
                return 270f;
            case NORTH_WEST:
                return 225f;
            default:
                return 0f;
        }
    }

    /**
     * Returns the facing direction from an specified yaw.
     *
     * @param yaw
     * @return
     */
    public static BlockFace yawToFace(float yaw) {
        double rotation = (yaw - 90) % CIRCUNFERENCE;
        if (rotation < 0) {
            rotation += ((double) CIRCUNFERENCE);
        }

        int j = 0;
        if (0 <= rotation && rotation < BEGIN) {
            return BFACES[j];
        }

        for (double i = BEGIN; i <= END; i += VARIATION) {
            if (i <= rotation && rotation < (i + VARIATION)) {
                return BFACES[++j];
            }
        }

        return null;
    }
}
