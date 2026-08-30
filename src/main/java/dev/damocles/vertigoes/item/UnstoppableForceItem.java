package dev.damocles.vertigoes.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.EndPlatformFeature;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

public class UnstoppableForceItem extends Item {
    public UnstoppableForceItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    private DimensionTransition getDimensionTeleport(ResourceKey<Level> nextDimension, ServerLevel nextLevel, Player player) {
        // see EndPortalBlock

        if (nextLevel == null || player == null) {
            return null;
        }

        BlockPos blockPos = nextDimension == Level.END ? ServerLevel.END_SPAWN_POINT : nextLevel.getSharedSpawnPos();
        Vec3 vec3 = blockPos.getBottomCenter();

        if (nextDimension == Level.END) {
            EndPlatformFeature.createEndPlatform(nextLevel, BlockPos.containing(vec3).below(), true);
            if (player instanceof ServerPlayer) {
                vec3 = vec3.subtract(0.0, 1.0, 0.0);
            }
        } else {
            if (player instanceof ServerPlayer serverPlayer) {
                return serverPlayer.findRespawnPositionAndUseSpawnBlock(false, DimensionTransition.DO_NOTHING);
            }

            vec3 = player.adjustSpawnLocation(nextLevel, blockPos).getBottomCenter();
        }

        return new DimensionTransition(
            nextLevel, vec3, player.getDeltaMovement(), player.getYRot(), player.getXRot(),
            DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET)
        );
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if(level.getBlockState(context.getClickedPos()).is(Blocks.BEDROCK) && level.getServer() != null) {
            ResourceKey<Level> nextDimension = context.getLevel().dimension() == Level.OVERWORLD ? Level.END : Level.OVERWORLD;
            ServerLevel nextLevel = context.getLevel().getServer().getLevel(nextDimension);
            Player player = context.getPlayer();

            DimensionTransition teleport = getDimensionTeleport(nextDimension, nextLevel, player);

            if(teleport != null) {
                context.getPlayer().changeDimension(teleport);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return stack.copy();
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // TODO: When breaking a beacon, drop instead the Heart of Glass
        if(state.is(Blocks.BEACON)) {
            return 6.0F;
        }
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("What would happen if it met The Immovable Object?")
                                .withStyle(ChatFormatting.GRAY)
                                .withStyle(ChatFormatting.ITALIC));
    }
}
