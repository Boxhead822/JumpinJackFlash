package com.example.jjf;

import com.example.jjf.registry.ModSounds;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import com.example.jjf.registry.ModEntities;
import com.example.jjf.stand.powers.PowersJumpinJackFlash;
import net.hydra.jojomod.item.StandDiscItem;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.hydra.jojomod.registry.ForgeCreativeTab;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import com.example.jjf.client.models.stand.renderers.JumpinJackFlashRenderer;
import com.example.jjf.registry.ModEntities;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import com.example.jjf.client.models.stand.JumpinJackFlashModel;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraft.world.item.CrossbowItem;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.example.jjf.zero_gravity.ZeroGravityAccess;
import com.example.jjf.zero_gravity.ZeroGravitySystem;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(JJFMod.MODID)
public class JJFMod
{
    public static final String MODID = "jjf";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "jjf" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "jjf" namespace

    // Creates a new Block with the id "jjf:example_block", combining the namespace and path
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    // Creates a new BlockItem with the id "jjf:example_block", combining the namespace and path
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));


    public static final RegistryObject<Item> JJF_STAND_DISC = ITEMS.register(
            "jumpin_jack_flash_disc",
            () -> new StandDiscItem(
                    new Item.Properties().stacksTo(1),
                    new PowersJumpinJackFlash(null)
            )
    );
    public static final RegistryObject<Item> JJF_STAND_DISC_MAX = ITEMS.register(
            "jumpin_jack_flash_disc_max",
            () -> new MaxStandDiscItem(
                    new Item.Properties().stacksTo(1),
                    new PowersJumpinJackFlash(null),
                    (StandDiscItem) JJF_STAND_DISC.get()
            )
    );

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(
                ModEntities.JUMPIN_JACK_FLASH.get(),
                Mob.createMobAttributes().build()
        );
    }

    public JJFMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();


        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerAttributes);

        ITEMS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);

        BLOCKS.register(modEventBus);

        ModSounds.SOUND_EVENTS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTab() == ForgeCreativeTab.WIP_GROUP.get()) {
            event.accept(JJF_STAND_DISC);
            event.accept(JJF_STAND_DISC_MAX);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void registerRenderers(
                EntityRenderersEvent.RegisterRenderers event
        ) {
            event.registerEntityRenderer(
                    ModEntities.JUMPIN_JACK_FLASH.get(),
                    JumpinJackFlashRenderer::new
            );
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(
                EntityRenderersEvent.RegisterLayerDefinitions event
        ) {
            event.registerLayerDefinition(
                    JumpinJackFlashModel.LAYER_LOCATION,
                    JumpinJackFlashModel::createBodyLayer
            );
        }

    }
}
