package dev.damocles.vertigoes;

import java.util.function.Supplier;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dev.damocles.vertigoes.block.GlassHeartBlock;
import dev.damocles.vertigoes.block.GlassHeartBlockEntity;
import dev.damocles.vertigoes.block.MyosotisBlock;
import dev.damocles.vertigoes.block.PlantEssenceBlock;
import dev.damocles.vertigoes.block.PottedMyosotisBlock;
import dev.damocles.vertigoes.item.EnderMyosotisItem;
import dev.damocles.vertigoes.item.UnstoppableForceItem;
import dev.damocles.vertigoes.item.pearl.AnimalPearlItem;
import dev.damocles.vertigoes.item.pearl.AquaticPearlItem;
import dev.damocles.vertigoes.item.pearl.DeathPearlItem;
import dev.damocles.vertigoes.item.pearl.PlantPearlItem;
import dev.damocles.vertigoes.item.pearl.PrimalPearlItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Vertigoes.MODID)
public class Vertigoes {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "vertigoes";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // Deferred Registries
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Myosotis
    public static final DeferredBlock<Block> MYOSOTIS = BLOCKS.registerBlock("myosotis", MyosotisBlock::new);
    public static final DeferredItem<BlockItem> MYOSOTIS_ITEM = ITEMS.registerSimpleBlockItem("myosotis", MYOSOTIS);
    public static final DeferredBlock<FlowerPotBlock> POTTED_MYOSOTIS = BLOCKS.registerBlock("potted_myosotis", PottedMyosotisBlock::new);
    public static final DeferredItem<Item> ENDER_MYOSOTIS = ITEMS.registerItem("ender_myosotis", EnderMyosotisItem::new);

    // Primal Pearl and its variants
    public static final DeferredItem<Item> PRIMAL_PEARL = ITEMS.registerItem("primal_pearl", PrimalPearlItem::new);
    public static final DeferredItem<Item> PLANT_PEARL = ITEMS.registerItem("plant_pearl", PlantPearlItem::new);
    public static final DeferredItem<Item> ANIMAL_PEARL = ITEMS.registerItem("animal_pearl", AnimalPearlItem::new);
    public static final DeferredItem<Item> AQUATIC_PEARL = ITEMS.registerItem("aquatic_pearl", AquaticPearlItem::new);
    public static final DeferredItem<Item> DEATH_PEARL = ITEMS.registerItem("death_pearl", DeathPearlItem::new);

    // Plant Essence
    public static final DeferredBlock<Block> PLANT_ESSENCE = BLOCKS.registerBlock("plant_essence", PlantEssenceBlock::new);
    public static final DeferredItem<BlockItem> PLANT_ESSENCE_ITEM = ITEMS.registerSimpleBlockItem("plant_essence", PLANT_ESSENCE);

    // The Unstoppable Force
    public static final DeferredItem<Item> UNSTOPPABLE_FORCE = ITEMS.registerItem("unstoppable_force", UnstoppableForceItem::new);

    // Heart of Glass
    public static final DeferredBlock<Block> GLASS_HEART = BLOCKS.registerBlock("glass_heart", GlassHeartBlock::new);
    public static final DeferredItem<BlockItem> GLASS_HEART_ITEM = ITEMS.registerSimpleBlockItem("glass_heart", GLASS_HEART);
    public static final Supplier<BlockEntityType<GlassHeartBlockEntity>> GLASS_HEART_ENTITY = BLOCK_ENTITIES.register("glass_heart",
             () -> BlockEntityType.Builder.of(GlassHeartBlockEntity::new, GLASS_HEART.get()).build(null));

    // Creates a creative tab with the id "vertigoes:vertigoes_tab"
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VERTIGOES_TAB = CREATIVE_MODE_TABS.register("vertigoes_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.vertigoes"))
            .icon(() -> MYOSOTIS_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                for(DeferredHolder<Item, ? extends Item> entry : ITEMS.getEntries()) {
                    output.accept(entry.get());
                }
            }).build());

    public Vertigoes(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the blocks, items, block entities, and creative tabs
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
        pot.addPlant(MYOSOTIS.getId(), POTTED_MYOSOTIS);

        // Register items to creative tabs
        modEventBus.addListener(this::addCreative);

        // Register ModConfigSpec so that FML can create and load the config file
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        if (Config.ENABLE_DEBUGGING.getAsBoolean()) {
            LOGGER.info("DEBUGGING IS ENABLED");
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(MYOSOTIS_ITEM);
            event.accept(PLANT_ESSENCE_ITEM);
        }
    }
}
