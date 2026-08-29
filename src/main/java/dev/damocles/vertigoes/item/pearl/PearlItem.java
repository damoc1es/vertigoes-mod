package dev.damocles.vertigoes.item.pearl;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class PearlItem extends Item {
    public enum PearlType {
        PRIMAL, PLANT, ANIMAL, AQUATIC, DEATH
    }

    protected PearlType type;

    public PearlItem(Properties properties, PearlType type) {
        super(properties.stacksTo(1));
        this.type = type;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        switch (type) {
            case PRIMAL:
                tooltipComponents.add(Component.literal("Immaculate").withStyle(ChatFormatting.GRAY));
                break;
            case PLANT:
                tooltipComponents.add(Component.literal("Plant Life").withStyle(ChatFormatting.GREEN));
                break;
            case ANIMAL:
                tooltipComponents.add(Component.literal("Animal Life").withStyle(ChatFormatting.RED));
                break;
            case AQUATIC:
                tooltipComponents.add(Component.literal("Aquatic Life").withStyle(ChatFormatting.BLUE));
                break;
            case DEATH:
                tooltipComponents.add(Component.literal("Anti-Life").withStyle(ChatFormatting.DARK_GRAY));
                break;
            default:
                break;
        }
    }
}
