package dev.damocles.vertigoes.block;

import static dev.damocles.vertigoes.Vertigoes.MYOSOTIS;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PottedMyosotisBlock extends FlowerPotBlock {
    public PottedMyosotisBlock(Properties properties) {
        super(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MYOSOTIS, properties);
    }

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState blockState, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        MyosotisBlock.handlePlayerDestroy(level, player, pos, blockState, blockEntity, tool);
    }
}
