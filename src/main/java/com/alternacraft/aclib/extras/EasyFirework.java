/*
 * Copyright (C) 2017 AlternaCraft
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

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 *
 * @author AlternaCraft
 */
public class EasyFirework {

    private Color color;
    private Color fade;
    private FireworkEffect.Type effect;

    private int power;
    private boolean flicker;
    private boolean trail;

    public EasyFirework(Color color, Color fade, FireworkEffect.Type effect,
            int power, boolean flicker, boolean trail) {
        this.color = color;
        this.fade = fade;
        this.effect = effect;
        this.power = power;
        this.flicker = flicker;
        this.trail = trail;
    }

    public EasyFirework(Color color, Color fade, FireworkEffect.Type effect) {
        this(color, fade, effect, 0, true, false);
    }

    public void throwFirework(Location l) {
        Firework fw = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        FireworkEffect f_effect = FireworkEffect
                .builder()
                .flicker(this.flicker)
                .withColor(this.color)
                .withFade(this.fade)
                .with(this.effect)
                .trail(this.trail)
                .build();
        fwm.addEffect(f_effect);
        fwm.setPower(this.power);
        fw.setFireworkMeta(fwm);
    }
}
