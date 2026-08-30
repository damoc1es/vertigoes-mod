package dev.damocles.vertigoes.item;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import dev.damocles.vertigoes.Const;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class EnderMyosotisItem extends Item {
    public EnderMyosotisItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(stack.get(DataComponents.CUSTOM_DATA) != null) {
            CompoundTag currentTags = stack.get(DataComponents.CUSTOM_DATA).copyTag();
            if(currentTags.contains(Const.MYOSOTIS_COORD_X_TAG)) {
                double coordX = currentTags.getDouble(Const.MYOSOTIS_COORD_X_TAG);
                double coordY = currentTags.getDouble(Const.MYOSOTIS_COORD_Y_TAG);
                double coordZ = currentTags.getDouble(Const.MYOSOTIS_COORD_Z_TAG);
                String dim = currentTags.getString(Const.MYOSOTIS_DIMENSION_TAG);
                if(dim.contains(":")) {
                    dim = dim.substring(dim.indexOf(":")+1);
                }

                tooltipComponents.add(Component.literal(String.format("RIGHT-CLICK to teleport to (x=%.2f, y=%.2f, z=%.2f)", coordX, coordY, coordZ)).withStyle(ChatFormatting.GRAY));
                tooltipComponents.add(Component.literal(String.format("IF you're in %s", dim)).withStyle(ChatFormatting.GRAY));
                tooltipComponents.add(Component.literal("SNEAK+RIGHT-CLICK to change coords").withStyle(ChatFormatting.GRAY));
            }
        } else {
            tooltipComponents.add(Component.literal("Forget me not..").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        if(player.isShiftKeyDown()) {
            CompoundTag tags = new CompoundTag();
            tags.putDouble(Const.MYOSOTIS_COORD_X_TAG, player.getX());
            tags.putDouble(Const.MYOSOTIS_COORD_Y_TAG, player.getY());
            tags.putDouble(Const.MYOSOTIS_COORD_Z_TAG, player.getZ());
            tags.putString(Const.MYOSOTIS_DIMENSION_TAG, level.dimension().location().toString());
            ItemStack itemstack = player.getItemInHand(hand);
            itemstack.set(DataComponents.CUSTOM_DATA, CustomData.of(tags));
            return InteractionResultHolder.success(itemstack);
        }
        else {
            ItemStack itemstack = player.getItemInHand(hand);
            CompoundTag currentTags = itemstack.get(DataComponents.CUSTOM_DATA).copyTag();
            if(currentTags != null) {
                if (!level.isClientSide) {
                    String dim = currentTags.getString(Const.MYOSOTIS_DIMENSION_TAG);

                    if(player.level().dimension().location().toString().equals(dim)) {
                        double coordX = currentTags.getDouble(Const.MYOSOTIS_COORD_X_TAG);
                        double coordY = currentTags.getDouble(Const.MYOSOTIS_COORD_Y_TAG);
                        double coordZ = currentTags.getDouble(Const.MYOSOTIS_COORD_Z_TAG);
                        player.teleportTo(coordX, coordY, coordZ);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
            }
            return InteractionResultHolder.fail(itemstack);
        }
    }
}
