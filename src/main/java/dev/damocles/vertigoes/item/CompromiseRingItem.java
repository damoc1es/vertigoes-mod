package dev.damocles.vertigoes.item;

import java.util.List;

import dev.damocles.vertigoes.Const;
import dev.damocles.vertigoes.Vertigoes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class CompromiseRingItem extends Item {
    public CompromiseRingItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    private static void triggerCompromise(Level level, Player player) {
        player.hurt(level.damageSources().magic(), 2);
        // Strength IV
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Const.ONE_MINUTE * (int)level.tickRateManager().tickrate(), 3, true, true));
        // Fragility II
        player.addEffect(new MobEffectInstance(Vertigoes.FRAGILITY_EFFECT, Const.ONE_MINUTE * (int)level.tickRateManager().tickrate(), 1, true, true));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if(entity instanceof Player player) {
            if(Inventory.isHotbarSlot(slotId) && stack.get(DataComponents.CUSTOM_DATA) != null) {
                CompoundTag currentTags = stack.get(DataComponents.CUSTOM_DATA).copyTag();

                if(currentTags.contains(Const.COMPROMISE_RING_NEXT_TRIGGER)) {
                    int storedTime = currentTags.getInt(Const.COMPROMISE_RING_NEXT_TRIGGER);

                    if(storedTime >= Const.ONE_MINUTE * level.tickRateManager().tickrate()) {
                        // trigger
                        triggerCompromise(level, player);
                        currentTags.putInt(Const.COMPROMISE_RING_NEXT_TRIGGER, 0);
                        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(currentTags));
                    } else {
                        // increment
                        currentTags.putInt(Const.COMPROMISE_RING_NEXT_TRIGGER, storedTime+1);
                        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(currentTags));
                    }
                } else {
                    // start timer
                    currentTags.putInt(Const.COMPROMISE_RING_NEXT_TRIGGER, 0);
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(currentTags));
                }
            } else {
                // start/reset timer
                CompoundTag newTag = new CompoundTag();
                newTag.putInt(Const.COMPROMISE_RING_NEXT_TRIGGER, 0);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(newTag));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.vertigoes.compromise_ring.despair").withStyle(ChatFormatting.DARK_RED));
    }
}
