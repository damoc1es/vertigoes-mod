package dev.damocles.vertigoes.item.pearl;

import dev.damocles.vertigoes.Config;
import dev.damocles.vertigoes.Vertigoes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class AquaticPearlItem extends PearlItem {

    public AquaticPearlItem(Properties properties) {
        super(properties, PearlType.AQUATIC);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if(isSelected) {
            Player player = (Player)entity;
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 260));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // look into water bucket implementation
        ItemStack pearlItemStack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

        if (blockhitresult.getType() == HitResult.Type.MISS || blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(pearlItemStack);
        }

        BlockPos blockpos = blockhitresult.getBlockPos();
        Direction direction = blockhitresult.getDirection();
        BlockPos blockpos1 = blockpos.relative(direction);

        if (!level.mayInteract(player, blockpos) || !player.mayUseItemAt(blockpos1, direction, pearlItemStack)) {
            return InteractionResultHolder.fail(pearlItemStack);
        }

        BlockState blockstate = level.getBlockState(blockpos);
        BlockPos blockpos2 = canBlockContainFluid(player, level, blockpos, blockstate) ? blockpos : blockpos1;

        // piggyback on the water bucket functions
        if(Items.WATER_BUCKET instanceof BucketItem waterBucket) {
            if (waterBucket.emptyContents(player, level, blockpos2, blockhitresult, pearlItemStack)) {
                waterBucket.checkExtraContent(player, level, pearlItemStack, blockpos2);
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos2, pearlItemStack);
                }

                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.sidedSuccess(pearlItemStack, level.isClientSide());
            }
        }

        return InteractionResultHolder.fail(pearlItemStack);
    }

    public static void disablePearl(Player player) {
        if(Config.AQUATIC_PEARL_CAN_BE_DISABLED.getAsBoolean()) {
            PearlItem.disablePearl(player, Vertigoes.AQUATIC_PEARL.get());
        }
    }

    private boolean canBlockContainFluid(Player player, Level worldIn, BlockPos posIn, BlockState blockstate)
    {
        return blockstate.getBlock() instanceof LiquidBlockContainer &&
                ((LiquidBlockContainer)blockstate.getBlock()).canPlaceLiquid(player, worldIn, posIn, blockstate, Fluids.WATER);
    }
}
