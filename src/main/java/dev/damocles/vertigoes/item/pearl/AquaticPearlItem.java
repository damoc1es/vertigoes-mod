package dev.damocles.vertigoes.item.pearl;

import dev.damocles.vertigoes.Vertigoes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

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
    public InteractionResult useOn(UseOnContext context) {
        if(context.getPlayer() == null)
            return InteractionResult.FAIL;
        return Items.WATER_BUCKET.getDefaultInstance().use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
    }

    public static void disablePearl(Player player) {
        // TODO: Make disabling configurable
        for(int i=0; i<player.getInventory().getContainerSize(); i++) {
            if(player.getInventory().getItem(i).is(Vertigoes.AQUATIC_PEARL.get())) {
                player.getInventory().setItem(i, Vertigoes.PRIMAL_PEARL.get().getDefaultInstance());
            }
        }
    }
}
