package dev.damocles.vertigoes.alchemy;

import dev.damocles.vertigoes.Const;
import dev.damocles.vertigoes.Vertigoes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public class VelocityPotion extends Potion {
    private VelocityPotion(int effectLength, int level) {
        super(
            new MobEffectInstance(Vertigoes.FRAGILITY_EFFECT, (int)(1.2 * effectLength), level -1),
            new MobEffectInstance(MobEffects.DIG_SPEED, effectLength, 2*level - 1),
            new MobEffectInstance(MobEffects.MOVEMENT_SPEED, effectLength, level - 1)
        );
    }

    public static VelocityPotion normal() {
        return new VelocityPotion(Const.NORMAL_POTION_LENGTH,  1);
    }

    public static VelocityPotion longer() {
        return new VelocityPotion(Const.LONGER_POTION_LENGTH, 1);
    }

    public static VelocityPotion enhanced() {
        return new VelocityPotion(Const.ENHANCED_POTION_LENGTH, 2);
    }
}
