package dev.damocles.vertigoes.item.pearl;

import dev.damocles.vertigoes.Vertigoes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PlantPearlItem extends PearlItem {

    public PlantPearlItem(Properties properties) {
        super(properties, PearlType.PLANT);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if(state.is(Vertigoes.PLANT_ESSENCE.get()))
            return 16.0F;
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockHitResult hitResult = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), context.isInside());
        BlockPlaceContext newContext = new BlockPlaceContext(context.getLevel(), context.getPlayer(), context.getHand(), Vertigoes.PLANT_ESSENCE_ITEM.asItem().getDefaultInstance(), hitResult);

        return Vertigoes.PLANT_ESSENCE_ITEM.asItem().useOn(newContext);
    }
}
