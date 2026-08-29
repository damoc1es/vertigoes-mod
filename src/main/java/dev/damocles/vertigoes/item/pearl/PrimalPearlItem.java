package dev.damocles.vertigoes.item.pearl;

import java.util.List;

import dev.damocles.vertigoes.Vertigoes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.common.Tags.Biomes;

public class PrimalPearlItem extends PearlItem {

    public PrimalPearlItem(Properties properties) {
        super(properties, PearlType.PRIMAL);
    }

    public static void tryTransformToGeneralPearl(Player player, String progressTag, int requirement, ItemStack replacement) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);

            // TODO: Use custom DataComponents
            if (item.is(Vertigoes.PRIMAL_PEARL.get())) {
                CompoundTag tags;

                if (item.has(DataComponents.CUSTOM_DATA)) { // has tags
                    tags = item.get(DataComponents.CUSTOM_DATA).copyTag();

                    if(tags.contains(progressTag)) { // has the progress tag
                        tags.putInt(progressTag, tags.getInt(progressTag) + 1);

                        if(tags.getInt(progressTag) >= requirement) { // requirement met
                            player.getInventory().setItem(i, replacement);
                        }
                        else {
                            item.set(DataComponents.CUSTOM_DATA, CustomData.of(tags));
                        }
                    } else {
                        tags.putInt(progressTag, 1);
                        item.set(DataComponents.CUSTOM_DATA, CustomData.of(tags));
                    }
                }
                else { // doesn't have any tag
                    tags = new CompoundTag();
                    tags.putInt(progressTag, 1);
                    item.set(DataComponents.CUSTOM_DATA, CustomData.of(tags));
                }
            }
        }
    }

    public static void tryTransformToPlantPearl(Player player, DamageSource damage) {
        // if player died in swamp while drowning
        if(player.level().getBiome(new BlockPos((int)player.getX(), (int)player.getY(), (int)player.getZ())).is(Biomes.IS_SWAMP) && damage.getMsgId().equals("drown")) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack item = player.getInventory().getItem(i);
                if (item.is(Vertigoes.PRIMAL_PEARL.get())) {
                    player.getInventory().removeItem(i, 1);
                    player.getInventory().setItem(i, Vertigoes.PLANT_PEARL.get().getDefaultInstance());
                }
            }
        }
    }

    public static void tryTransformToAnimalPearl(Player player) {
        // TODO: Make configurable
        tryTransformToGeneralPearl(player, "vertigoes.animalprogress", 20, Vertigoes.ANIMAL_PEARL.get().getDefaultInstance());
    }

    public static void tryTransformToDeathPearl(Player player, Entity entity) {
        if(entity instanceof Villager) {
            // TODO: Make configurable
            tryTransformToGeneralPearl(player, "vertigoes.deathprogress", 13, Vertigoes.DEATH_PEARL.get().getDefaultInstance());
        }
    }

    public static void tryTransformToAquaticPearl(Player player, Entity entity) {
        if(entity instanceof Drowned) {
            // TODO: Make configurable
            tryTransformToGeneralPearl(player, "vertigoes.aquaticprogress", 25, Vertigoes.AQUATIC_PEARL.get().getDefaultInstance());
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        if(stack.has(DataComponents.CUSTOM_DATA)) {
            CompoundTag tags = stack.get(DataComponents.CUSTOM_DATA).copyTag();
            if(tags.contains("vertigoes.animalprogress")) {
                // TODO: Make configurable
                tooltipComponents.add(Component.literal(String.format("%d / %d Bred animals",
                                                        tags.getInt("vertigoes.animalprogress"),
                                                        20))
                                                        .withStyle(ChatFormatting.RED));
            }

            if(tags.contains("vertigoes.deathprogress")) {
                // TODO: Make configurable
                tooltipComponents.add(Component.literal(String.format("%d / %d Villagers killed",
                                                        tags.getInt("vertigoes.deathprogress"),
                                                        13))
                                                        .withStyle(ChatFormatting.DARK_GRAY));
            }

            if(tags.contains("vertigoes.aquaticprogress")) {
                // TODO: Make configurable
                tooltipComponents.add(Component.literal(String.format("%d / %d Drowned killed",
                                                        tags.getInt("vertigoes.aquaticprogress"),
                                                        25))
                                                        .withStyle(ChatFormatting.DARK_AQUA));
            }
        }
    }
}
