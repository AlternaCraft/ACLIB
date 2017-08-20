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
