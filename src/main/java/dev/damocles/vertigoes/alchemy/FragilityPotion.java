package dev.damocles.vertigoes.alchemy;

import dev.damocles.vertigoes.Const;
import dev.damocles.vertigoes.Vertigoes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class FragilityPotion extends Potion {
    public FragilityPotion() {
        super(new MobEffectInstance(Vertigoes.FRAGILITY_EFFECT, Const.NORMAL_POTION_LENGTH, 0));
    }
}
