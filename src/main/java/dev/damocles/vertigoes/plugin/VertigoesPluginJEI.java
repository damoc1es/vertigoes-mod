package dev.damocles.vertigoes.plugin;

import dev.damocles.vertigoes.Config;
import dev.damocles.vertigoes.Vertigoes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class VertigoesPluginJEI implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Vertigoes.MODID, "default");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        Integer animalPearlReq = Config.ANIMAL_PEARL_REQ.getAsInt();
        Integer aquaticPearlReq = Config.AQUATIC_PEARL_REQ.getAsInt();
        Integer deathPearlReq = Config.DEATH_PEARL_REQ.getAsInt();

        Boolean animalPearlCanBeDisabled = Config.ANIMAL_PEARL_CAN_BE_DISABLED.getAsBoolean();
        Boolean aquaticPearlCanBeDisabled = Config.AQUATIC_PEARL_CAN_BE_DISABLED.getAsBoolean();
        Boolean deathPearlCanBeDisabled = Config.DEATH_PEARL_CAN_BE_DISABLED.getAsBoolean();

        Integer heartOfGlassAoE = Config.GLASS_HEART_RADIUS.getAsInt()*2+1;

        registry.addIngredientInfo(Vertigoes.MYOSOTIS_ITEM.get(),
            Component.literal("Retains the coordinates of the last time it was placed."));

        registry.addIngredientInfo(Vertigoes.ENDER_MYOSOTIS.get(),
            Component.literal("One-use teleport with the condition that you are in the same dimension as the saved coordinates."));

        registry.addIngredientInfo(Vertigoes.PRIMAL_PEARL.get(),
            Component.literal(String.format("""
                While in inventory, completing certain tasks may transform this and give you some benefits:
                Plant life Pearl - drown in a swamp biome
                Animal life Pearl - breed %d animals
                Aquatic life Pearl - kill %d Drowned
                Death Pearl - kill %d Villagers""",
                animalPearlReq, aquaticPearlReq, deathPearlReq)));

        registry.addIngredientInfo(Vertigoes.PLANT_PEARL.get(),
            Component.literal("""
                Obtained by drowning in a swamp biome with a Primal Pearl in inventory.
                On right-click you can put a special Plant Essence block with the pearl indefinitely."""));

        String animalPearlText = """
            Obtained by breeding %d animals with a Primal Pearl in inventory.
            While in hotbar, you deal extra damage to undead mobs (equal to Smite IV).""";
        if(animalPearlCanBeDisabled) {
            animalPearlText += " Turns back into a Primal Pearl if you kill a Villager or an animal.";
        }

        registry.addIngredientInfo(Vertigoes.ANIMAL_PEARL.get(),
            Component.literal(String.format(animalPearlText, animalPearlReq)));

        String aquaticPearlText = """
            Obtained by killing %d Drowned with a Primal Pearl in inventory.
            While in main hand, gain Water Breathing.""";
        if(aquaticPearlCanBeDisabled) {
            aquaticPearlText += " Turns back into a Primal Pearl if you kill any fish/water friendly creature.";
        }

        registry.addIngredientInfo(Vertigoes.AQUATIC_PEARL.get(),
            Component.literal(String.format(aquaticPearlText, aquaticPearlReq)));

        String deathPearlText = """
            Obtained by killing %d Villagers with a Primal Pearl in inventory.
            While in main hand, undead creatures can't damage you (still take knockback).""";
        if(deathPearlCanBeDisabled) {
            deathPearlText += " Turns back into a Primal Pearl if you cure a Zombie Villager.";
        }

        registry.addIngredientInfo(Vertigoes.DEATH_PEARL.get(),
            Component.literal(String.format(deathPearlText, deathPearlReq)));

        registry.addIngredientInfo(Vertigoes.PLANT_ESSENCE_ITEM.get(),
            Component.literal("Only obtainable by using the Plant Life Primal Pearl."));

        registry.addIngredientInfo(Vertigoes.UNSTOPPABLE_FORCE.get(),
            Component.literal("""
                Using it on Bedrock teleports you to The End.
                Breaking a Beacon with it drops instead a Heart of Glass."""));

        registry.addIngredientInfo(Vertigoes.GLASS_HEART_ITEM.get(),
            Component.literal(String.format("""
                In a %dx%dx%d area of effect every player receives Regeneration II. Glows and can be waterlogged.
                The potion effect can be changed by using a Lingering Potion of the desired effect on it.""",
                heartOfGlassAoE, heartOfGlassAoE, heartOfGlassAoE)));
    }
}
