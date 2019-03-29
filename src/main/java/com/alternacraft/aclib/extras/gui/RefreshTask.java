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
package com.alternacraft.aclib.extras.gui;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginBase;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @author AlternaCraft
 */
public class RefreshTask {

    private static final Map<String, Integer> TASKS = new HashMap();

    private final int NON_LIMIT = -1;

    private final String uuid;
    private final int delay;

    public RefreshTask(String uuid, int delay) {
        this.uuid = uuid;
        this.delay = delay;
    }

    /**
     * Registers a function to execute a custom update.
     *
     * @param f Function which should return "true" to continue and "false" to
     *          stop
     */
    public void registerFunction(Function<String, Boolean> f) {
        this.registerUpdate(NON_LIMIT, f);
    }

    /**
     * The default update task will replace the players skulls for new ones.
     *
     * @param limit Maximum repetitions
     * @param f     Custom update.
     */
    public void registerUpdate(int limit, Function<String, Boolean> f) {
        if (f == null) return;
        AtomicInteger counter = new AtomicInteger(0);
        MessageManager.logDebug("Task " + uuid + " registered!");
        TASKS.put(uuid, new BukkitRunnable() {
            @Override
            public void run() {
                if (counter.getAndIncrement() == limit || !f.apply(uuid)) {
                    MessageManager.logDebug("Task " + uuid + " stopped!");
                    cancel();
                }
            }
        }.runTaskTimer(PluginBase.INSTANCE.plugin(), 0, delay).getTaskId());
    }

    public static void cancel(String uuid) {
        Integer t = TASKS.get(uuid);
        if (t != null) {
            Bukkit.getServer().getScheduler().cancelTask(t);
            TASKS.remove(uuid);
        }
    }
}
