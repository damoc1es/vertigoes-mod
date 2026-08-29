package dev.damocles.vertigoes.setup;

import java.util.UUID;

import dev.damocles.vertigoes.Vertigoes;
import dev.damocles.vertigoes.item.pearl.AnimalPearlItem;
import dev.damocles.vertigoes.item.pearl.AquaticPearlItem;
import dev.damocles.vertigoes.item.pearl.DeathPearlItem;
import dev.damocles.vertigoes.item.pearl.PrimalPearlItem;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingConversionEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = Vertigoes.MODID)
public class VertigoesEventHandler {
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if(event.getEntity() instanceof Player) {
            PrimalPearlItem.tryTransformToPlantPearl((Player) event.getEntity(), event.getSource());
        }
    }

    @SubscribeEvent
    public static void onPlayerKill(LivingDeathEvent event) {
        if(event.getSource().getEntity() instanceof Player) {
            MobCategory killedCategory = event.getEntity().getType().getCategory();
            if(event.getEntity() instanceof Villager || killedCategory == MobCategory.CREATURE) {
                AnimalPearlItem.disablePearl((Player)event.getSource().getEntity());
            }

            if(killedCategory == MobCategory.WATER_CREATURE || killedCategory == MobCategory.WATER_AMBIENT)
                AquaticPearlItem.disablePearl((Player)event.getSource().getEntity());

            PrimalPearlItem.tryTransformToDeathPearl((Player) event.getSource().getEntity(), event.getEntity());
            PrimalPearlItem.tryTransformToAquaticPearl((Player) event.getSource().getEntity(), event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerDamaged(LivingDamageEvent.Pre event) {
        if(event.getEntity() instanceof Player) {
            if(DeathPearlItem.tryCancelUndeadAttackUponPlayer((Player) event.getEntity(), event.getSource())) {
                event.setNewDamage(0);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCausingDamage(LivingDamageEvent.Pre event) {
        if(event.getSource().getEntity() instanceof Player) {
            float bonus = AnimalPearlItem.inHotbarGetUndeadDamage((Player)event.getSource().getEntity(), event.getEntity(), event.getOriginalDamage());
            if(bonus != event.getOriginalDamage()) {
                event.setNewDamage(bonus);
            }
        }
    }

    @SubscribeEvent
    public static void onAnimalsBreeding(BabyEntitySpawnEvent event) {
        if(event.getCausedByPlayer() != null) {
            PrimalPearlItem.tryTransformToAnimalPearl(event.getCausedByPlayer());
        }
    }

    @SubscribeEvent
    public static void onVillagersConversion(LivingConversionEvent.Post event) {
        if (event.getEntity() instanceof ZombieVillager && event.getOutcome() instanceof Villager) {
            try {
                UUID playerID = ObfuscationReflectionHelper.getPrivateValue(ZombieVillager.class, (ZombieVillager) event.getEntity(), "conversionStarter");
                if (playerID != null) {
                    Player player = event.getEntity().level().getPlayerByUUID(playerID);
                    if (player != null) {
                        DeathPearlItem.disablePearl(player);
                    }
                }
            } catch(ObfuscationReflectionHelper.UnableToAccessFieldException ignored) {}
        }
    }
}
