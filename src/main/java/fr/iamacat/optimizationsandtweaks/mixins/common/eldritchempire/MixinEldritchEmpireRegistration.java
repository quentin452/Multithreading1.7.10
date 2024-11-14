package fr.iamacat.optimizationsandtweaks.mixins.common.eldritchempire;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import eldritchempires.EldritchEmpires;
import eldritchempires.Registration;
import eldritchempires.block.BlockCollector;
import eldritchempires.block.BlockPortal;
import eldritchempires.block.BlockSpawner;
import eldritchempires.entity.*;
import eldritchempires.entity.projectile.EntityFireBolt;
import eldritchempires.entity.projectile.EntityIceBolt;
import eldritchempires.entity.projectile.EntityNiceArrow;
import eldritchempires.item.*;

@Mixin(Registration.class)
public class MixinEldritchEmpireRegistration {

    @Shadow
    public static Block collector;
    @Shadow
    public static Block spawner;
    @Shadow
    public static Block portal;
    @Shadow
    public static Block ironTrapDoor;
    @Shadow
    public static Block protectedRedstone;
    @Shadow
    public static Item golemPart;
    @Shadow
    public static Item condensedEssence;
    @Shadow
    public static Item iceCrystal;
    @Shadow
    public static Item fireCrystal;
    @Shadow
    public static Item darkCrystal;
    @Shadow
    public static Item poisonCrystal;
    @Shadow
    public static Item earthCrystal;
    @Shadow
    public static Item herosCrystal;
    @Shadow
    public static Item golemTool;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void registration() {
        collector = (new BlockCollector()).setBlockName("collector");
        GameRegistry.registerBlock(
            collector,
            ItemCollector.class,
            "eldritchempires" + collector.getUnlocalizedName()
                .substring(5));
        spawner = (new BlockSpawner(Material.ground)).setBlockName("spawner");
        GameRegistry.registerBlock(
            spawner,
            ItemSpawner.class,
            "eldritchempires" + spawner.getUnlocalizedName()
                .substring(5));
        portal = (new BlockPortal()).setBlockName("portal");
        GameRegistry.registerBlock(
            portal,
            ItemPortal.class,
            "eldritchempires" + portal.getUnlocalizedName()
                .substring(5));
        registerEntity(EntityZoblin.class, "zoblin");
        registerEntity(EntityZoblinBomber.class, "zoblinBomber");
        registerEntity(EntityZoblinBoss.class, "zoblinBoss");
        registerEntity(EntityZoblinWarrior.class, "zoblinWarrior");
        registerEntity(EntityZoblinHound.class, "zoblinHound");
        registerEntity(EntityZoblinHoundmaster.class, "zoblinHoundmaster");
        registerEntity(EntityRabidDwarf.class, "rabidDwarf");
        registerEntity(EntityRabidMiner.class, "rabidMiner");
        registerEntity(EntityRabidWarrior.class, "rabidWarrior");
        registerEntity(EntityRabidDemo.class, "rabidDemolitionist");
        registerEntity(EntityStoneArcher.class, "stoneArcher");
        registerEntity(EntityStoneMage.class, "stoneMage");
        registerEntity(EntityStoneWarrior.class, "stoneWarrior");
        registerEntity(EntityMagicEssence.class, "magicEssence");
        registerEntity(EntityBomb.class, "bomb");
        int entityID;
        do {
            entityID = EntityRegistry.findGlobalUniqueEntityId();
        } while (entityID < 1001);
        EntityRegistry.registerGlobalEntityID(EntityIceBolt.class, "Ice Bolt", entityID);
        EntityRegistry
            .registerModEntity(EntityIceBolt.class, "Ice Bolt", entityID, EldritchEmpires.instance, 64, 1, true);
        entityID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityNiceArrow.class, "Nice Arrow", entityID);
        EntityRegistry
            .registerModEntity(EntityNiceArrow.class, "Nice Arrow", entityID, EldritchEmpires.instance, 64, 1, true);
        entityID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityFireBolt.class, "Fire Bolt", entityID);
        EntityRegistry
            .registerModEntity(EntityFireBolt.class, "Fire Bolt", entityID, EldritchEmpires.instance, 64, 1, true);
        GameRegistry.registerTileEntity(TileEntityCollector.class, "collectorEntity");
        GameRegistry.registerTileEntity(TileEntitySpawner.class, "spawnerEntity");
        golemPart = (new ItemGolemPart()).setUnlocalizedName("golemPart")
            .setCreativeTab(EldritchEmpires.tabEldritch)
            .setTextureName("eldritchempires:golemPart");
        GameRegistry.registerItem(golemPart, "Golem Part");
        condensedEssence = (new ItemCondensedEssence()).setUnlocalizedName("condensedEssence")
            .setCreativeTab(EldritchEmpires.tabEldritch)
            .setTextureName("eldritchempires:condensedEssence");
        GameRegistry.registerItem(condensedEssence, "Condensed Essence");
        golemTool = (new ItemGolemTool()).setUnlocalizedName("golemTool")
            .setCreativeTab(EldritchEmpires.tabEldritch)
            .setTextureName("eldritchempires:golemTool");
        GameRegistry.registerItem(golemTool, "Golem Tool");
        iceCrystal = (new Item()).setUnlocalizedName("iceCrystal")
            .setCreativeTab(EldritchEmpires.tabEldritch)
            .setTextureName("eldritchempires:iceCrystal");
        GameRegistry.registerItem(iceCrystal, "Ice Crystal");
        fireCrystal = (new Item()).setUnlocalizedName("fireCrystal")
            .setCreativeTab(EldritchEmpires.tabEldritch)
            .setTextureName("eldritchempires:fireCrystal");
        GameRegistry.registerItem(fireCrystal, "Fire Crystal");
        darkCrystal = (new Item()).setUnlocalizedName("darkCrystal")
            .setCreativeTab(EldritchEmpires.tabEldritch)
            .setTextureName("eldritchempires:darkCrystal");
        GameRegistry.registerItem(darkCrystal, "Dark Crystal");
        poisonCrystal = (new Item()).setUnlocalizedName("poisonCrystal")
            .setCreativeTab(EldritchEmpires.tabEldritch)
            .setTextureName("eldritchempires:poisonCrystal");
        GameRegistry.registerItem(poisonCrystal, "Poison Crystal");
        earthCrystal = (new Item()).setUnlocalizedName("earthCrystal")
            .setCreativeTab(EldritchEmpires.tabEldritch)
            .setTextureName("eldritchempires:earthCrystal");
        GameRegistry.registerItem(earthCrystal, "Earth Crystal");
        herosCrystal = (new Item()).setUnlocalizedName("herosCrystal")
            .setCreativeTab(EldritchEmpires.tabEldritch)
            .setTextureName("eldritchempires:herosCrystal");
        GameRegistry.registerItem(herosCrystal, "Hero's Crystal");
        GameRegistry.addRecipe(
            new ItemStack(collector, 1, 0),
            "ROR",
            "ORO",
            "ROR",
            'R',
            Items.redstone,
            'O',
            new ItemStack(Blocks.obsidian, 1));
        GameRegistry.addRecipe(
            new ItemStack(portal, 1, 0),
            "CRC",
            "RER",
            "CRC",
            'R',
            Items.redstone,
            'E',
            Items.emerald,
            'C',
            new ItemStack(Blocks.cobblestone, 1));
        GameRegistry.addRecipe(
            new ItemStack(spawner, 1, 0),
            " E ",
            "GBG",
            "GEG",
            'G',
            golemPart,
            'B',
            Items.bow,
            'E',
            condensedEssence);
        GameRegistry.addRecipe(
            new ItemStack(spawner, 1, 1),
            " E ",
            "GRG",
            "GEG",
            'G',
            golemPart,
            'R',
            new ItemStack(Blocks.redstone_torch),
            'E',
            condensedEssence);
        GameRegistry.addRecipe(
            new ItemStack(spawner, 1, 2),
            " E ",
            "GSG",
            "GEG",
            'G',
            golemPart,
            'S',
            Items.iron_sword,
            'E',
            condensedEssence);
        GameRegistry.addRecipe(
            new ItemStack(golemPart, 1),
            "CCC",
            "III",
            "CCC",
            'C',
            new ItemStack(Blocks.cobblestone, 1),
            'I',
            Items.iron_ingot);
        GameRegistry
            .addRecipe(new ItemStack(iceCrystal, 1), "SSS", "SES", "SSS", 'S', Items.snowball, 'E', condensedEssence);
        GameRegistry.addRecipe(
            new ItemStack(fireCrystal, 1),
            " L ",
            " E ",
            " L ",
            'L',
            Items.lava_bucket,
            'E',
            condensedEssence);
        GameRegistry.addRecipe(new ItemStack(darkCrystal, 1), "EP", 'P', Items.ender_pearl, 'E', condensedEssence);
        GameRegistry.addRecipe(
            new ItemStack(poisonCrystal, 1),
            " F ",
            " E ",
            " F ",
            'F',
            Items.fermented_spider_eye,
            'E',
            condensedEssence);
        GameRegistry.addRecipe(
            new ItemStack(earthCrystal, 1),
            " C ",
            " E ",
            " C ",
            'C',
            new ItemStack(Items.dye, 1, 3),
            'E',
            condensedEssence);
        GameRegistry
            .addRecipe(new ItemStack(herosCrystal, 1), " Q ", " E ", " Q ", 'Q', Items.quartz, 'E', condensedEssence);
        GameRegistry
            .addRecipe(new ItemStack(Items.diamond, 1, 0), new Object[] { "EEE", "EEE", "EEE", 'E', condensedEssence });
        GameRegistry
            .addRecipe(new ItemStack(golemTool, 1), " EE", " SE", "S  ", 'E', condensedEssence, 'S', Items.stick);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void registerEntity(Class entityClass, String name) {
        int entityID;
        do {
            entityID = EntityRegistry.findGlobalUniqueEntityId();
        } while (entityID < 1001);
        long seed = name.hashCode();
        Random rand = new Random(seed);
        int primaryColor = rand.nextInt() * 16777215;
        int secondaryColor = rand.nextInt() * 16777215;
        EntityRegistry.registerGlobalEntityID(entityClass, name, entityID);
        EntityRegistry.registerModEntity(entityClass, name, entityID, EldritchEmpires.instance, 64, 1, true);
        EntityList.entityEggs.put(entityID, new EntityList.EntityEggInfo(entityID, primaryColor, secondaryColor));
    }
}
