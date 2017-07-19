package com.alternacraft.aclib.extras;

import java.util.Random;
import org.bukkit.util.Vector;

/**
 * This class contains some utils for generating randoms.
 *
 * @author AlternaCraft
 */
public class Randomizer {

    private Randomizer() {
    }

    /**
     * Get a random value between an interval.
     *
     * @param max Max value
     * @param min Min value
     *
     * @return int
     */
    public static int rand(int max, int min) {
        return new Random().nextInt(max) + min;
    }

    /**
     * Get a random vector contained into the two vectors.
     *
     * @param p1 Vector 1
     * @param p2 Vector 2
     *
     * @return Random vector
     * @since 0.0.9
     */
    public static Vector randBetweenVector(Vector p1, Vector p2) {
        int pos1, pos2, pos3;

        pos1 = seekMAXandGetRand(p1.getBlockX(), p2.getBlockX());
        pos2 = seekMAXandGetRand(p1.getBlockY(), p2.getBlockY());
        pos3 = seekMAXandGetRand(p1.getBlockZ(), p2.getBlockZ());

        return new Vector(pos1, pos2, pos3);
    }
    
    private static int seekMAXandGetRand(int v1, int v2) {
        int r;

        if (v1 > v2) {
            r = rand(v1, v2);
        } else if (v1 == v2) {
            r = v1;
        } else {
            r = rand(v2, v1);
        }

        return r;
    }
}
