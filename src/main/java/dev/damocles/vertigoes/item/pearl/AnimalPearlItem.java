package dev.damocles.vertigoes.item.pearl;

import dev.damocles.vertigoes.Config;
import dev.damocles.vertigoes.Const;
import dev.damocles.vertigoes.Vertigoes;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class AnimalPearlItem extends PearlItem {

    public AnimalPearlItem(Properties properties) {
        super(properties, PearlType.ANIMAL);
    }

    public static float inHotbarGetUndeadDamage(Player player, LivingEntity entity, float amount) {
        if(entity.getType().is(EntityTypeTags.SENSITIVE_TO_SMITE)) {
            for(int i=0; i<10; i++) {
                if(player.getInventory().getItem(i).is(Vertigoes.ANIMAL_PEARL.get())) {
                    return amount + Const.DEATH_PEARL_DMG_MODIFIER;
                }
            }
            if(player.getInventory().getItem(Inventory.SLOT_OFFHAND).is(Vertigoes.ANIMAL_PEARL.get())) {
                return amount + Const.DEATH_PEARL_DMG_MODIFIER;
            }
        }
        return amount;
    }

    public static void disablePearl(Player player) {
        if(Config.ANIMAL_PEARL_CAN_BE_DISABLED.getAsBoolean()) {
            PearlItem.disablePearl(player, Vertigoes.ANIMAL_PEARL.get());
        }
    }
}
