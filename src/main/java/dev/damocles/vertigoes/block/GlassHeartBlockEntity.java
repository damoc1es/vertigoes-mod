package dev.damocles.vertigoes.block;

import java.util.List;

import dev.damocles.vertigoes.Config;
import dev.damocles.vertigoes.Const;
import dev.damocles.vertigoes.Vertigoes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class GlassHeartBlockEntity extends BlockEntity {
    public MobEffectInstance effectInstance = null;
    public int tickCount;

    public GlassHeartBlockEntity(BlockPos pos, BlockState blockState) {
        super(Vertigoes.GLASS_HEART_ENTITY.get(), pos, blockState);
    }

    private static void applyEffects(Level level, BlockPos pos, MobEffectInstance effectInstance) {
        if (!level.isClientSide) {
            Holder<MobEffect> effect;
            int amplifier;

            if(effectInstance != null) {
                effect = effectInstance.getEffect();
                amplifier = effectInstance.getAmplifier();
            } else {
                effect = MobEffects.REGENERATION;
                amplifier = 1;
            }

            AABB aabb = (new AABB(pos)).inflate(Config.GLASS_HEART_RADIUS.getAsInt()).expandTowards(0.0D, 0.0D, 0.0D);
            List<Entity> list = level.getEntities(null, aabb);

            for(Entity entity : list) {
                if(entity instanceof Player player) {
                    player.addEffect(new MobEffectInstance(effect, 10*20, amplifier, true, true));
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if(this.effectInstance != null) {
            tag.put(Const.GLASS_HEART_EFFECT, MobEffectInstance.CODEC.encodeStart(NbtOps.INSTANCE, this.effectInstance).getOrThrow());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if(tag.contains(Const.GLASS_HEART_EFFECT)) {
            this.effectInstance = MobEffectInstance.CODEC.parse(NbtOps.INSTANCE, tag.get(Const.GLASS_HEART_EFFECT)).getOrThrow();
        }
    }

    public void serverTick(Level level, BlockPos pos, BlockState state, GlassHeartBlockEntity blockEntity) {
        ++blockEntity.tickCount;
        long i = level.getGameTime();
        if (i % 40L == 0L) {
            applyEffects(level, pos, blockEntity.effectInstance);
        }
    }
}