/*
 * Copyright (C) 2018 AlternaCraft
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
package com.alternacraft.aclib.extras;

import com.alternacraft.aclib.exceptions.UnknownFacingDirectionException;
import org.bukkit.block.BlockFace;
import static org.bukkit.block.BlockFace.EAST;
import static org.bukkit.block.BlockFace.NORTH;
import static org.bukkit.block.BlockFace.NORTH_EAST;
import static org.bukkit.block.BlockFace.NORTH_WEST;
import static org.bukkit.block.BlockFace.SOUTH;
import static org.bukkit.block.BlockFace.SOUTH_EAST;
import static org.bukkit.block.BlockFace.SOUTH_WEST;
import static org.bukkit.block.BlockFace.WEST;

/**
 * This class contains some utils for Location objects.
 *
 * @author AlternaCraft
 */
public class LocationUtils {
    /**
     * Returns the facing direction from an specified yaw.
     *
     * @param bf Block face
     * 
     * @return Block face to yaw
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
     * @param yaw Yaw value
     * 
     * @return Block face
     * @throws com.alternacraft.aclib.exceptions.UnknownFacingDirectionException
     */
    public static BlockFace yawToFace(float yaw) throws UnknownFacingDirectionException {
        double rotation = (yaw - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }

        BlockFace[] bfs = {NORTH_EAST, EAST, SOUTH_EAST,SOUTH, SOUTH_WEST, WEST, NORTH_WEST};
        
        // + 22.5
        if ((0 <= rotation && rotation < 22.5) || (337.5 <= rotation && rotation < 360.0)) {
            return BlockFace.NORTH;
        }
        // + 45
        double angle = 22.5;
        for (BlockFace bf : bfs) {
            if (angle <= rotation && rotation < angle + 45) {
                return bf;
            }
            angle += 45;
        }        
        throw new UnknownFacingDirectionException();
    }
}