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
package com.alternacraft.aclib.listeners;

import com.alternacraft.aclib.PluginBase;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * This class is useful to register all your handlers by using an enum.
 *
 * @author AlternaCraft
 */
public class HandlersRegisterer {

    /**
     * Register listeners.
     *
     * @param <T> HandlersInterface
     * @param e Enum class of type T
     *
     * @since 1.0.1
     */
    public static <T extends HandlersInterface> void load(Class<T> e) {
        for (T handler : e.getEnumConstants()) {
            Bukkit.getServer().getPluginManager().registerEvents(
                    handler.getListener(), PluginBase.INSTANCE.plugin());
        }
    }

    /**
     * Register listeners.
     *
     * @param <T> Listener
     * @param listeners Listener objects
     *
     * @since 1.7.2
     */
    public static <T extends Listener> void load(T... listeners) {
        for (T listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, 
                    PluginBase.INSTANCE.plugin());
        }
    }
}
