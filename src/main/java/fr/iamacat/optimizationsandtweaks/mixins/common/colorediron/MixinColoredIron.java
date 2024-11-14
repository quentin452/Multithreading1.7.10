package fr.iamacat.optimizationsandtweaks.mixins.common.colorediron;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.ColoredIron.forge.mods.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mixin(ColoredIron.class)
public class MixinColoredIron {

    @Shadow
    public static Block RedIronBlock;
    @Shadow
    public static Block OrangeIronBlock;
    @Shadow
    public static Block YellowIronBlock;
    @Shadow
    public static Block LimeIronBlock;
    @Shadow
    public static Block GreenIronBlock;
    @Shadow
    public static Block CyanIronBlock;
    @Shadow
    public static Block LightBlueIronBlock;
    @Shadow
    public static Block BlueIronBlock;
    @Shadow
    public static Block PurpleIronBlock;
    @Shadow
    public static Block MagentaIronBlock;
    @Shadow
    public static Block PinkIronBlock;
    @Shadow
    public static Block BrownIronBlock;
    @Shadow
    public static Block BlackIronBlock;
    @Shadow
    public static Block LightGreyIronBlock;
    @Shadow
    public static Block GreyIronBlock;
    @Shadow
    public static Item RedIron;
    @Shadow
    public static Item OrangeIron;
    @Shadow
    public static Item YellowIron;
    @Shadow
    public static Item LimeIron;
    @Shadow
    public static Item GreenIron;
    @Shadow
    public static Item CyanIron;
    @Shadow
    public static Item LightBlueIron;
    @Shadow
    public static Item BlueIron;
    @Shadow
    public static Item PurpleIron;
    @Shadow
    public static Item MagentaIron;
    @Shadow
    public static Item PinkIron;
    @Shadow
    public static Item BrownIron;
    @Shadow
    public static Item BlackIron;
    @Shadow
    public static Item GreyIron;
    @Shadow
    public static Item LightGreyIron;

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        RedIronBlock = new RedIronBlock();
        GameRegistry.registerBlock(RedIronBlock, "RedIronBlock");
        Item RedIronBlockItem = GameRegistry.findItem("RedIronBlock", "RedIronBlock");
        OrangeIronBlock = new OrangeIronBlock();
        GameRegistry.registerBlock(OrangeIronBlock, "OrangeIronBlock");
        Item OrangeIronBlockItem = GameRegistry.findItem("OrangeIronBlock", "OrangeIronBlock");
        YellowIronBlock = new YellowIronBlock();
        GameRegistry.registerBlock(YellowIronBlock, "YellowIronBlock");
        Item YellowIronBlockItem = GameRegistry.findItem("YellowIronBlock", "YellowIronBlock");
        LimeIronBlock = new LimeIronBlock();
        GameRegistry.registerBlock(LimeIronBlock, "LimeIronBlock");
        Item LimeIronBlockItem = GameRegistry.findItem("LimeIronBlock", "LimeIronBlock");
        GreenIronBlock = new GreenIronBlock();
        GameRegistry.registerBlock(GreenIronBlock, "GreenIronBlock");
        Item GreenIronBlockItem = GameRegistry.findItem("GreenIronBlock", "GreenIronBlock");
        CyanIronBlock = new CyanIronBlock();
        GameRegistry.registerBlock(CyanIronBlock, "CyanIronBlock");
        Item CyanIronBlockItem = GameRegistry.findItem("CyanIronBlock", "CyanIronBlock");
        LightBlueIronBlock = new LightBlueIronBlock();
        GameRegistry.registerBlock(LightBlueIronBlock, "LightBlueIronBlock");
        Item LightBlueIronBlockItem = GameRegistry.findItem("LightBlueIronBlock", "LightBlueIronBlock");
        BlueIronBlock = new BlueIronBlock();
        GameRegistry.registerBlock(BlueIronBlock, "BlueIronBlock");
        Item BlueIronBlockItem = GameRegistry.findItem("BlueIronBlock", "BlueIronBlock");
        PurpleIronBlock = new PurpleIronBlock();
        GameRegistry.registerBlock(PurpleIronBlock, "PurpleIronBlock");
        Item PurpleIronBlockItem = GameRegistry.findItem("PurpleIronBlock", "PurpleIronBlock");
        MagentaIronBlock = new MagentaIronBlock();
        GameRegistry.registerBlock(MagentaIronBlock, "MagentaIronBlock");
        Item MagentaIronBlockItem = GameRegistry.findItem("MagentaIronBlock", "MagentaIronBlock");
        PinkIronBlock = new PinkIronBlock();
        GameRegistry.registerBlock(PinkIronBlock, "PinkIronBlock");
        Item PinkIronBlockItem = GameRegistry.findItem("PinkIronBlock", "PinkIronBlock");
        BrownIronBlock = new BrownIronBlock();
        GameRegistry.registerBlock(BrownIronBlock, "BrownIronBlock");
        Item BrownIronBlockItem = GameRegistry.findItem("BrownIronBlock", "BrownIronBlock");
        BlackIronBlock = new BlackIronBlock();
        GameRegistry.registerBlock(BlackIronBlock, "BlackIronBlock");
        Item BlackIronBlockItem = GameRegistry.findItem("BlackIronBlock", "BlackIronBlock");
        LightGreyIronBlock = new LightGreyIronBlock();
        GameRegistry.registerBlock(LightGreyIronBlock, "LightGreyIronBlock");
        Item LightGreyIronBlockItem = GameRegistry.findItem("LightGreyIronBlock", "LightGreyIronBlock");
        GreyIronBlock = new GreyIronBlock();
        GameRegistry.registerBlock(GreyIronBlock, "GreyIronBlock");
        Item GreyIronBlockItem = GameRegistry.findItem("GreyIronBlock", "GreyIronBlock");
        RedIron = new RedIron();
        GameRegistry.registerItem(RedIron, "RedIron");
        Item RedIronItem = GameRegistry.findItem("RedIron", "RedIron");
        OrangeIron = new OrangeIron();
        GameRegistry.registerItem(OrangeIron, "OrangeIron");
        Item OrangeIronItem = GameRegistry.findItem("OrangeIron", "OrangeIron");
        YellowIron = new YellowIron();
        GameRegistry.registerItem(YellowIron, "YellowIron");
        Item YellowIronItem = GameRegistry.findItem("YellowIron", "YellowIron");
        LimeIron = new LimeIron();
        GameRegistry.registerItem(LimeIron, "LimeIron");
        Item LimeIronItem = GameRegistry.findItem("LimeIron", "LimeIron");
        GreenIron = new GreenIron();
        GameRegistry.registerItem(GreenIron, "GreenIron");
        Item GreenIronItem = GameRegistry.findItem("GreenIron", "GreenIron");
        CyanIron = new CyanIron();
        GameRegistry.registerItem(CyanIron, "CyanIron");
        Item CyanIronItem = GameRegistry.findItem("CyanIron", "CyanIron");
        LightBlueIron = new LightBlueIron();
        GameRegistry.registerItem(LightBlueIron, "LightBlueIron");
        Item LightBlueIronItem = GameRegistry.findItem("LightBlueIron", "LightBlueIron");
        BlueIron = new BlueIron();
        GameRegistry.registerItem(BlueIron, "BlueIron");
        Item BlueIronItem = GameRegistry.findItem("BlueIron", "BlueIron");
        PurpleIron = new PurpleIron();
        GameRegistry.registerItem(PurpleIron, "PurpleIron");
        Item PurpleIronItem = GameRegistry.findItem("PurpleIron", "PurpleIron");
        MagentaIron = new MagentaIron();
        GameRegistry.registerItem(MagentaIron, "MagentaIron");
        Item MagentaIronItem = GameRegistry.findItem("MagentaIron", "MagentaIron");
        PinkIron = new PinkIron();
        GameRegistry.registerItem(PinkIron, "PinkIron");
        Item PinkIronItem = GameRegistry.findItem("PinkIron", "PinkIron");
        BrownIron = new BrownIron();
        GameRegistry.registerItem(BrownIron, "BrownIron");
        Item BrownIronItem = GameRegistry.findItem("BrownIron", "BrownIron");
        BlackIron = new BlackIron();
        GameRegistry.registerItem(BlackIron, "BlackIron");
        Item BlackIronItem = GameRegistry.findItem("BlackIron", "BlackIron");
        GreyIron = new GreyIron();
        GameRegistry.registerItem(GreyIron, "GreyIron");
        Item GreyIronItem = GameRegistry.findItem("GreyIron", "GreyIron");
        LightGreyIron = new LightGreyIron();
        GameRegistry.registerItem(LightGreyIron, "LightGreyIron");
        Item LightGreyIronItem = GameRegistry.findItem("LightGreyIron", "LightGreyIron");
        GameRegistry.addShapelessRecipe(
            new ItemStack(RedIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 1));
        GameRegistry.addShapelessRecipe(
            new ItemStack(OrangeIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 14));
        GameRegistry.addShapelessRecipe(
            new ItemStack(YellowIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 11));
        GameRegistry.addShapelessRecipe(
            new ItemStack(LimeIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 10));
        GameRegistry.addShapelessRecipe(
            new ItemStack(GreenIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 2));
        GameRegistry.addShapelessRecipe(
            new ItemStack(CyanIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 6));
        GameRegistry.addShapelessRecipe(
            new ItemStack(LightBlueIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 12));
        GameRegistry.addShapelessRecipe(
            new ItemStack(BlueIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 4));
        GameRegistry.addShapelessRecipe(
            new ItemStack(PurpleIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 5));
        GameRegistry.addShapelessRecipe(
            new ItemStack(MagentaIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 13));
        GameRegistry.addShapelessRecipe(
            new ItemStack(PinkIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 9));
        GameRegistry.addShapelessRecipe(
            new ItemStack(BlackIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 0));
        GameRegistry.addShapelessRecipe(
            new ItemStack(BrownIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 3));
        GameRegistry.addShapelessRecipe(
            new ItemStack(LightGreyIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 7));
        GameRegistry.addShapelessRecipe(
            new ItemStack(GreyIron, 1),
            new ItemStack(Items.iron_ingot, 1),
            new ItemStack(Items.dye, 1, 8));
        GameRegistry.addShapelessRecipe(new ItemStack(RedIronBlock, 1), new ItemStack(RedIron, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(OrangeIronBlock, 1), new ItemStack(OrangeIron, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(YellowIronBlock, 1), new ItemStack(YellowIron, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(LimeIronBlock, 1), new ItemStack(LimeIron, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(GreenIronBlock, 1), new ItemStack(GreenIron, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(CyanIronBlock, 1), new ItemStack(CyanIron, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(LightBlueIronBlock, 1), new ItemStack(LightBlueIron, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(BlueIronBlock, 1), new ItemStack(BlueIron, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(PurpleIronBlock, 1), new ItemStack(PurpleIron, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(MagentaIronBlock, 1), new ItemStack(MagentaIron, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(PinkIronBlock, 1), new ItemStack(PinkIron, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(BlackIronBlock, 1), new ItemStack(BlackIron, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(BrownIronBlock, 1), new ItemStack(BrownIron, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(LightGreyIronBlock, 1), new ItemStack(LightGreyIronBlock, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(GreyIronBlock, 1), new ItemStack(GreyIron, 9));
        GameRegistry.addRecipe(
            new ItemStack(RedIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 1),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(
            new ItemStack(OrangeIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 14),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(
            new ItemStack(YellowIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 11),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(
            new ItemStack(LimeIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 10),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(
            new ItemStack(GreenIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 2),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(
            new ItemStack(CyanIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 6),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(
            new ItemStack(LightBlueIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 12),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(
            new ItemStack(BlueIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 4),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(
            new ItemStack(PurpleIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 5),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(
            new ItemStack(MagentaIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 13),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(
            new ItemStack(PinkIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 9),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(
            new ItemStack(BlackIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 0),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(
            new ItemStack(BrownIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 3),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(
            new ItemStack(LightGreyIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 7),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(
            new ItemStack(GreyIronBlock, 1),
            "ccc",
            "cbc",
            "ccc",
            'c',
            new ItemStack(Items.dye, 1, 8),
            'b',
            Blocks.iron_block);
        GameRegistry.addRecipe(new ItemStack(RedIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(RedIron, 8));
        GameRegistry
            .addRecipe(new ItemStack(OrangeIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(OrangeIron, 8));
        GameRegistry
            .addRecipe(new ItemStack(YellowIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(YellowIron, 8));
        GameRegistry.addRecipe(new ItemStack(LimeIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(LimeIron, 8));
        GameRegistry.addRecipe(new ItemStack(GreenIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(GreenIron, 8));
        GameRegistry.addRecipe(new ItemStack(CyanIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(CyanIron, 8));
        GameRegistry
            .addRecipe(new ItemStack(LightBlueIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(LightBlueIron, 8));
        GameRegistry.addRecipe(new ItemStack(BlueIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(BlueIron, 8));
        GameRegistry
            .addRecipe(new ItemStack(PurpleIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(PurpleIron, 8));
        GameRegistry
            .addRecipe(new ItemStack(MagentaIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(MagentaIron, 8));
        GameRegistry.addRecipe(new ItemStack(PinkIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(PinkIron, 8));
        GameRegistry.addRecipe(new ItemStack(BlackIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(BlackIron, 8));
        GameRegistry.addRecipe(new ItemStack(BrownIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(BrownIron, 8));
        GameRegistry
            .addRecipe(new ItemStack(LightGreyIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(LightGreyIron, 8));
        GameRegistry.addRecipe(new ItemStack(GreyIronBlock, 1), "ccc", "ccc", "ccc", 'c', new ItemStack(GreyIron, 1));
        // GameRegistry.registerItem(grenade, grenade.func_77658_a()); prevent null by disabling this line
    }
}
