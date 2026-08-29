package dev.damocles.vertigoes.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class PlantEssenceBlock extends Block {

    public PlantEssenceBlock(Properties properties) {
        super(properties.strength(0.4F).sound(SoundType.GRASS));
    }

}
