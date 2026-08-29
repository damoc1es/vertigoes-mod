package dev.damocles.vertigoes.item.pearl;

import dev.damocles.vertigoes.Vertigoes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class AnimalPearlItem extends PearlItem {

    public AnimalPearlItem(Properties properties) {
        super(properties, PearlType.ANIMAL);
    }

    public static float inHotbarGetUndeadDamage(Player player, LivingEntity entity, float amount) {
        // TODO: Check if MONSTER <=> UNDEAD
        // TODO: Check for better ways of checking inventory
        if(entity.getType().getCategory() == MobCategory.MONSTER) {
            for(int i=0; i<10; i++) {
                if(player.getInventory().getItem(i).is(Vertigoes.ANIMAL_PEARL.get()))
                    return amount + (4 * 2.5F);
            }
            if(player.getInventory().getItem(Inventory.SLOT_OFFHAND).is(Vertigoes.ANIMAL_PEARL.get()))
                return amount + (4 * 2.5F);
        }
        return amount;
    }

    public static void disablePearl(Player player) {
        // TODO: Make disabling configurable
        // TODO: Move disabling in parent class
        for(int i=0; i<player.getInventory().getContainerSize(); i++) {
            if(player.getInventory().getItem(i).is(Vertigoes.ANIMAL_PEARL.get())) {
                player.getInventory().setItem(i, Vertigoes.PRIMAL_PEARL.get().getDefaultInstance());
            }
        }
    }
}
