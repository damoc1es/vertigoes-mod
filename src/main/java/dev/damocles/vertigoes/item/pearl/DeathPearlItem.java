package dev.damocles.vertigoes.item.pearl;

import dev.damocles.vertigoes.Vertigoes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;

public class DeathPearlItem extends PearlItem {

    public DeathPearlItem(Properties properties) {
        super(properties, PearlType.DEATH);
    }

    public static void disablePearl(Player player) {
        // TODO: Make disabling configurable
        for(int i=0; i<player.getInventory().getContainerSize(); i++) {
            if(player.getInventory().getItem(i).is(Vertigoes.DEATH_PEARL.get())) {
                player.getInventory().setItem(i, Vertigoes.PRIMAL_PEARL.get().getDefaultInstance());
            }
        }
    }

    public static boolean tryCancelUndeadAttackUponPlayer(Player player, DamageSource dmgSource) {
        if(dmgSource.getEntity() == null || !(dmgSource.getEntity() instanceof LivingEntity))
            return false;
        return (dmgSource.getEntity().getType().getCategory() == MobCategory.MONSTER && player.getInventory().getSelected().is(Vertigoes.DEATH_PEARL.get()));
    }
}
