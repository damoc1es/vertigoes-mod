package dev.damocles.vertigoes.item.pearl;

import dev.damocles.vertigoes.Config;
import dev.damocles.vertigoes.Vertigoes;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class DeathPearlItem extends PearlItem {

    public DeathPearlItem(Properties properties) {
        super(properties, PearlType.DEATH);
    }

    public static void disablePearl(Player player) {
        if(Config.DEATH_PEARL_CAN_BE_DISABLED.getAsBoolean()) {
            PearlItem.disablePearl(player, Vertigoes.DEATH_PEARL.get());
        }
    }

    public static boolean tryCancelUndeadAttackUponPlayer(Player player, DamageSource dmgSource) {
        if(dmgSource.getEntity() == null || !(dmgSource.getEntity() instanceof LivingEntity)) {
            return false;
        }

        return (dmgSource.getEntity().getType().is(EntityTypeTags.SENSITIVE_TO_SMITE)
                && player.getInventory().getSelected().is(Vertigoes.DEATH_PEARL.get()));
    }
}
