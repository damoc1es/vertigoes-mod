package dev.damocles.vertigoes.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.damocles.vertigoes.Const;


public class MyosotisBlock extends FlowerBlock {

    public MyosotisBlock(Properties properties) {
        super(SuspiciousStewEffects.EMPTY, properties.noCollission().instabreak().sound(SoundType.GRASS).mapColor(MapColor.PLANT));
    }

    static public void handlePlayerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState blockState, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {
        // Block.dropResources
        if (level instanceof ServerLevel) {
            getDrops(blockState, (ServerLevel)level, pos, blockEntity, player, tool).forEach((itemDrop) -> {
                // Set tag of drop with the coordinates and dimension of last placement
                CompoundTag tags = new CompoundTag();
                tags.putDouble(Const.MYOSOTIS_COORD_X_TAG, pos.getX());
                tags.putDouble(Const.MYOSOTIS_COORD_Y_TAG, pos.getY());
                tags.putDouble(Const.MYOSOTIS_COORD_Z_TAG, pos.getZ());
                tags.putString(Const.MYOSOTIS_DIMENSION_TAG, level.dimension().location().toString());

                itemDrop.set(DataComponents.CUSTOM_DATA, CustomData.of(tags));
                popResource(level, pos, itemDrop);
            });
            blockState.spawnAfterBreak((ServerLevel)level, pos, tool, true);
        }
    }

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState blockState, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        handlePlayerDestroy(level, player, pos, blockState, blockEntity, tool);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        // Setting the tooltip depending on if it was ever placed
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

                tooltip.add(Component.literal(String.format("(x=%d, y=%d, z=%d) in %s", (int)coordX, (int)coordY, (int)coordZ, dim)).withStyle(ChatFormatting.GRAY));
            }
        } else {
            tooltip.add(Component.literal("Forget me not..").withStyle(ChatFormatting.GRAY));
        }
    }
}
