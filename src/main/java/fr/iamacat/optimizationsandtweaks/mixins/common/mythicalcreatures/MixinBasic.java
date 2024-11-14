package fr.iamacat.optimizationsandtweaks.mixins.common.mythicalcreatures;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.EnumHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import twilightsparkle.basic.*;
import twilightsparkle.basic.TwilightStar;
import twilightsparkle.basic.mob.*;

@Mixin(Basic.class)
public class MixinBasic {

    @Shadow
    public static int Snap;
    @Shadow
    public static int FishSize;
    @Shadow
    public static Random TwilightRand;
    @Shadow
    public static Random TwilightRand2;
    @Shadow
    public static int entityId;
    @Shadow
    public static int hydraspawned;
    @Shadow
    static Item.ToolMaterial betterbone = EnumHelper.addToolMaterial("Reinforced", 3, 22800, 26.0F, 596.0F, 22);
    @Shadow
    static Item.ToolMaterial twilight = EnumHelper.addToolMaterial("Twilight", 3, 12500, 32.0F, 696.0F, 250);
    @Shadow
    static Item.ToolMaterial WOW = EnumHelper.addToolMaterial("WOW", 3, 7000, 22.0F, 746.0F, 16);
    @Shadow
    static Item.ToolMaterial apple = EnumHelper.addToolMaterial("Apple", 3, 2000, 12.0F, 121.0F, 12);
    @Shadow
    static Item.ToolMaterial applejack = EnumHelper.addToolMaterial("AppleJack", 3, 22500, 30.0F, 846.0F, 10);
    @Shadow
    static Item.ToolMaterial bear = EnumHelper.addToolMaterial("Bear", 2, 820, 3.9F, 8.0F, 8);
    @Shadow
    static Item.ToolMaterial dash = EnumHelper.addToolMaterial("Dash", 3, 3750, 14.0F, 171.0F, 18);
    @Shadow
    static Item.ToolMaterial crag = EnumHelper.addToolMaterial("Crag", 3, 6500, 14.0F, 321.0F, 8);
    @Shadow
    static Item.ToolMaterial alicorn = EnumHelper.addToolMaterial("Alicorn", 3, 37500, 48.0F, 1746.0F, 250);
    @Shadow
    static Item.ToolMaterial crystal = EnumHelper.addToolMaterial("CrystalSet", 3, 27000, 15.0F, 171.0F, 250);
    @Shadow
    static Item.ToolMaterial ursa = EnumHelper.addToolMaterial("UrsaWep", 3, 42000, 50.0F, 1996.0F, 2);
    @Shadow
    static Item.ToolMaterial random2 = EnumHelper.addToolMaterial("Random2", 2, 750, 4.2F, 5.0F, 10);
    @Shadow
    static Item.ToolMaterial random4 = EnumHelper.addToolMaterial("Random4", 3, 1150, 6.5F, 10.0F, 12);
    @Shadow
    static Item.ToolMaterial dirt = EnumHelper.addToolMaterial("DirtT", 1, -1, 1.0F, -2.0F, 1);
    @Shadow
    static ItemArmor.ArmorMaterial armorbetterbone = EnumHelper
        .addArmorMaterial("Reinforced", 2275, new int[] { 12, 32, 28, 12 }, 18);
    @Shadow
    static ItemArmor.ArmorMaterial armortwilicorn = EnumHelper
        .addArmorMaterial("Twilight", 1775, new int[] { 16, 40, 32, 16 }, 275);
    @Shadow
    static ItemArmor.ArmorMaterial armordash = EnumHelper
        .addArmorMaterial("Dash2", 1725, new int[] { 14, 36, 28, 14 }, 25);
    @Shadow
    static ItemArmor.ArmorMaterial armorWOW = EnumHelper.addArmorMaterial("WOW", 958, new int[] { 9, 21, 18, 9 }, 22);
    @Shadow
    static ItemArmor.ArmorMaterial armordark = EnumHelper
        .addArmorMaterial("Dark", 6245, new int[] { 14, 38, 32, 14 }, 10);
    @Shadow
    static ItemArmor.ArmorMaterial armorapple = EnumHelper
        .addArmorMaterial("Apple2", 675, new int[] { 7, 15, 14, 7 }, 10);
    @Shadow
    static ItemArmor.ArmorMaterial armorbear = EnumHelper.addArmorMaterial("Bear2", 175, new int[] { 3, 7, 6, 2 }, 15);
    @Shadow
    static ItemArmor.ArmorMaterial armorapplejack = EnumHelper
        .addArmorMaterial("AppleJack2", 2250, new int[] { 20, 50, 40, 20 }, 8);
    @Shadow
    public static CommonProxy proxy;
    @Shadow
    public static int MobId1 = 0;
    @Shadow
    public static int MobId2 = 0;
    @Shadow
    public static int MobId3 = 0;
    @Shadow
    public static int MobId4 = 0;
    @Shadow
    public static int MobId5 = 0;
    @Shadow
    public static int MobId6 = 0;
    @Shadow
    public static int MobId7 = 0;
    @Shadow
    public static int MobId8 = 0;
    @Shadow
    public static int MobId9 = 0;
    @Shadow
    public static int MobId10 = 0;
    @Shadow
    public static int MobId11 = 0;
    @Shadow
    public static int MobId12 = 0;
    @Shadow
    public static int MobId13 = 0;
    @Shadow
    public static int MobId14 = 0;
    @Shadow
    public static int MobId15 = 0;
    @Shadow
    public static int MobId16 = 0;
    @Shadow
    public static int MobId17 = 0;
    @Shadow
    public static int MobId18 = 0;
    @Shadow
    public static int MobId19 = 0;
    @Shadow
    public static int MobId20 = 0;
    @Shadow
    public static int MobId21 = 0;
    @Shadow
    public static int MobId22 = 0;
    @Shadow
    public static int MobId23 = 0;
    @Shadow
    public static int MobId24 = 0;
    @Shadow
    public static int MobId25 = 0;
    @Shadow
    public static int MobId26 = 0;
    @Shadow
    public static int MobId27 = 0;
    @Shadow
    public static int MobId28 = 0;
    @Shadow
    public static int MobId29 = 0;
    @Shadow
    public static int MobId30 = 0;
    @Shadow
    public static int MobId31 = 0;
    @Shadow
    public static int MobId32 = 0;
    @Shadow
    public static int MobId33 = 0;
    @Shadow
    public static int MobId34 = 0;
    @Shadow
    public static int MobId35 = 0;
    @Shadow
    public static int MobId36 = 0;
    @Shadow
    public static int MobId37 = 0;
    @Shadow
    public OreGenerator OreGen = new OreGenerator();
    @Shadow
    public static final Item boneSword;
    @Shadow
    public static final Item bonePick;
    @Shadow
    public static final Item boneHoe;
    @Shadow
    public static final Item boneAxe;
    @Shadow
    public static final Item boneSpade;
    @Shadow
    public static final Item twilicornSword;
    @Shadow
    public static final Item twilicornPick;
    @Shadow
    public static final Item twilicornHoe;
    @Shadow
    public static final Item twilicornAxe;
    @Shadow
    public static final Item twilicornSpade;
    @Shadow
    public static final Item yoshiSword;
    @Shadow
    public static final Item appleSword;
    @Shadow
    public static final Item applePick;
    @Shadow
    public static final Item appleHoe;
    @Shadow
    public static final Item appleAxe;
    @Shadow
    public static final Item appleSpade;
    @Shadow
    public static final Item dashSword;
    @Shadow
    public static final Item bearSword;
    @Shadow
    public static final Item bearPick;
    @Shadow
    public static final Item bearHoe;
    @Shadow
    public static final Item bearAxe;
    @Shadow
    public static final Item bearSpade;
    @Shadow
    public static final Item cragHammer;
    @Shadow
    public static final Item alicornSword;
    @Shadow
    public static final Item dagger;
    @Shadow
    public static final Item digger;
    @Shadow
    public static final Item ajSword;
    @Shadow
    public static final Item ajPick;
    @Shadow
    public static final Item ajHoe;
    @Shadow
    public static final Item ajAxe;
    @Shadow
    public static final Item ajSpade;
    @Shadow
    public static final Item crystalSword;
    @Shadow
    public static final Item ursaClaws;
    @Shadow
    public static final Item dirtSword;
    @Shadow
    public static final Block boneBlock;
    @Shadow
    public static final Block boneOre;
    @Shadow
    public static final Block boneBrick;
    @Shadow
    public static final Block glowBone;
    @Shadow
    public static final Block phoenixBlock;
    @Shadow
    public static final Block boneWall;
    @Shadow
    public static final Block twilicornBlock;
    @Shadow
    public static final Block robotBlock;
    @Shadow
    public static final Block bearFurBlock;
    @Shadow
    public static final Block appleBlock;
    @Shadow
    public static final Block arcticBlock;
    @Shadow
    public static final Block hardAppleBlock;
    @Shadow
    public static final Block crystalOre;
    @Shadow
    public static final Block crystalBlock;
    @Shadow
    public static final Block darkCrystalBlock;
    @Shadow
    public static final Item boneItem;
    @Shadow
    public static final Item twilicornItem;
    @Shadow
    public static Item lunaEclipse;
    @Shadow
    public static Item bowserRod;
    @Shadow
    public static Item phoenixBow;
    @Shadow
    public static Item twilicornBow;
    @Shadow
    public static Item bowserBone;
    @Shadow
    public static Item bowserEye;
    @Shadow
    public static Item friendShip;
    @Shadow
    public static final Item twilightStar;
    @Shadow
    public static final Item lightningCloud;
    @Shadow
    public static final Item diamondGems;
    @Shadow
    public static final Item butterFlies;
    @Shadow
    public static final Item apples;
    @Shadow
    public static final Item balloons;
    @Shadow
    public static final ItemFood dashMeat;
    @Shadow
    public static final ItemFood twiMeat;
    @Shadow
    public static final Item mavisOrb;
    @Shadow
    public static final Item eggTwilightMob;
    @Shadow
    public static final Item eggMyself;
    @Shadow
    public static final Item eggCroc;
    @Shadow
    public static final Item eggGiantChick;
    @Shadow
    public static final Item eggChubbyMoron;
    @Shadow
    public static final Item eggDemonSpider;
    @Shadow
    public static final Item eggLeviathan;
    @Shadow
    public static final Item eggKingbowser;
    @Shadow
    public static final Item eggCentipede;
    @Shadow
    public static final Item eggHydra;
    @Shadow
    public static final Item eggWindigo;
    @Shadow
    public static final Item eggMoose;
    @Shadow
    public static final Item eggBuffalo;
    @Shadow
    public static final Item eggPowerOrb;
    @Shadow
    public static final Item eggFlameDragon;
    @Shadow
    public static final Item eggMooseBig;
    @Shadow
    public static final Item eggBear;
    @Shadow
    public static final Item eggToughGuy;
    @Shadow
    public static final Item eggMavis;
    @Shadow
    public static final Item eggUrsaMAJOR;
    @Shadow
    public static final Item eggDash;
    @Shadow
    public static final Item eggManticore;
    @Shadow
    public static final Item bearFur;
    @Shadow
    public static final Item bearTooth;
    @Shadow
    public static final Item phoenixFeather;
    @Shadow
    public static final Item cragScale;
    @Shadow
    public static final Item eggRainbowCentipede;
    @Shadow
    public static final Item twilicornBone;
    @Shadow
    public static final Item eggAj;
    @Shadow
    public static final Item eggScorpion;
    @Shadow
    public static final Item darkCrystal;
    @Shadow
    public static final Item eggTimberWolf;
    @Shadow
    public static final Item eggParasprite;
    @Shadow
    public static final Item stinger;
    @Shadow
    public static final Item eggTMagic;
    @Shadow
    public static final Item eggCock;
    @Shadow
    public static final Item unstableItem;
    @Shadow
    public static final Item eggCrabzilla;
    @Shadow
    public static final Item eggMinotaur;
    @Shadow
    public static final ItemFood derpMuffin;
    @Shadow
    public static final ItemFood cupCakes;
    @Shadow
    public static final ItemFood rainbow;
    @Shadow
    public static final Item antenna;
    @Shadow
    public static final ItemFood hardApple;
    @Shadow
    public static final Item eggrdCloud;
    @Shadow
    public static final Item auroraGem;
    @Shadow
    public static final Item eggTrevor;
    @Shadow
    public static final Item eggSkullBoss;
    @Shadow
    public static final Item eggSkull;
    @Shadow
    public static final Item yakHorn;
    @Shadow
    public static final Item eggYakPrince;
    @Shadow
    public static final Item eggSpikey;
    @Shadow
    public static final Item eggRhino;
    @Shadow
    public static final Item rhinoHorn;
    @Shadow
    public static final Item boneHelmet;
    @Shadow
    public static final Item boneChest;
    @Shadow
    public static final Item boneLegs;
    @Shadow
    public static final Item boneBoots;
    @Shadow
    public static final Item twilicornHelmet;
    @Shadow
    public static final Item twilicornChest;
    @Shadow
    public static final Item twilicornLegs;
    @Shadow
    public static final Item twilicornBoots;
    @Shadow
    public static final Item darkHelmet;
    @Shadow
    public static final Item darkChest;
    @Shadow
    public static final Item darkLegs;
    @Shadow
    public static final Item darkBoots;
    @Shadow
    public static final Item appleHelmet;
    @Shadow
    public static final Item appleChest;
    @Shadow
    public static final Item appleLegs;
    @Shadow
    public static final Item appleBoots;
    @Shadow
    public static final Item bearHelmet;
    @Shadow
    public static final Item bearChest;
    @Shadow
    public static final Item bearLegs;
    @Shadow
    public static final Item bearBoots;
    @Shadow
    public static final Item ajHelmet;
    @Shadow
    public static final Item ajChest;
    @Shadow
    public static final Item ajLegs;
    @Shadow
    public static final Item ajBoots;
    @Shadow
    public static final Item rdHelmet;
    @Shadow
    public static final Item rdChest;
    @Shadow
    public static final Item rdLegs;
    @Shadow
    public static final Item rdBoots;

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        int entityId;
        do {
            entityId = EntityRegistry.findGlobalUniqueEntityId();
        } while (entityId < 1001);
        proxy.registerRenderers();
        GameRegistry.registerWorldGenerator(this.OreGen, 10);
        GameRegistry.registerBlock(boneBlock, "boneBlock");
        GameRegistry.registerBlock(boneOre, "boneOre");
        GameRegistry.registerBlock(boneBrick, "boneBrick");
        GameRegistry.registerBlock(boneWall, "boneWall");
        GameRegistry.registerBlock(glowBone, "glowBone");
        GameRegistry.registerBlock(robotBlock, "robotBlock");
        GameRegistry.registerBlock(phoenixBlock, "phoenixBlock");
        GameRegistry.registerBlock(twilicornBlock, "twilicornBlock");
        GameRegistry.registerBlock(bearFurBlock, "bearFurBlock");
        GameRegistry.registerBlock(appleBlock, "appleBlock");
        GameRegistry.registerBlock(arcticBlock, "arcticBlock");
        GameRegistry.registerBlock(hardAppleBlock, "hardAppleBlock");
        GameRegistry.registerBlock(crystalOre, "crystalOre");
        GameRegistry.registerBlock(crystalBlock, "crystalBlock");
        GameRegistry.registerBlock(darkCrystalBlock, "darkCrystalBlock");
        GameRegistry.registerItem(twilicornSword, "twilicornSword");
        GameRegistry.registerItem(twilicornPick, "twilicornPick");
        GameRegistry.registerItem(twilicornHoe, "twilicornHoe");
        GameRegistry.registerItem(twilicornAxe, "twilicornAxe");
        GameRegistry.registerItem(twilicornSpade, "twilicornSpade");
        GameRegistry.registerItem(twilicornItem, "twilicornItem");
        GameRegistry.registerItem(bowserBone, "bowserBone");
        GameRegistry.registerItem(bowserEye, "bowserEye");
        GameRegistry.registerItem(boneHelmet, "boneHelmet");
        GameRegistry.registerItem(boneChest, "boneChest");
        GameRegistry.registerItem(boneLegs, "boneLegs");
        GameRegistry.registerItem(boneBoots, "boneBoots");
        GameRegistry.registerItem(boneSword, "boneSword");
        GameRegistry.registerItem(lunaEclipse, "lunaEclipse");
        GameRegistry.registerItem(bonePick, "bonepick");
        GameRegistry.registerItem(boneAxe, "boneaxe");
        GameRegistry.registerItem(boneHoe, "bonehoe");
        GameRegistry.registerItem(boneSpade, "bonespade");
        GameRegistry.registerItem(boneItem, "boneItem");
        GameRegistry.registerItem(twilicornHelmet, "twilicornHelmet");
        GameRegistry.registerItem(twilicornChest, "twilicornChest");
        GameRegistry.registerItem(twilicornLegs, "twilicornLegs");
        GameRegistry.registerItem(twilicornBoots, "twilicornBoots");
        GameRegistry.registerItem(twilicornBow, "twilicornBow");
        GameRegistry.registerItem(darkHelmet, "darkHelmet");
        GameRegistry.registerItem(darkChest, "darkChest");
        GameRegistry.registerItem(darkLegs, "darkLegs");
        GameRegistry.registerItem(darkBoots, "darkBoots");
        GameRegistry.registerItem(twilightStar, "twilightStar");
        GameRegistry.registerItem(yoshiSword, "yoshiSword");
        GameRegistry.registerItem(lightningCloud, "lightningCloud");
        GameRegistry.registerItem(diamondGems, "diamondGems");
        GameRegistry.registerItem(butterFlies, "butterFlies");
        GameRegistry.registerItem(apples, "apples");
        GameRegistry.registerItem(balloons, "ballons");
        GameRegistry.registerItem(dashMeat, "dashMeat");
        GameRegistry.registerItem(twiMeat, "twiMeat");
        GameRegistry.registerItem(appleHelmet, "appleHelmet");
        GameRegistry.registerItem(appleChest, "appleChest");
        GameRegistry.registerItem(appleLegs, "appleLegs");
        GameRegistry.registerItem(appleBoots, "appleBoots");
        GameRegistry.registerItem(bearHelmet, "bearHelmet");
        GameRegistry.registerItem(bearChest, "bearChest");
        GameRegistry.registerItem(bearLegs, "bearLegs");
        GameRegistry.registerItem(bearBoots, "bearBoots");
        GameRegistry.registerItem(phoenixBow, "phoenixBow");
        GameRegistry.registerItem(appleSword, "appleSword");
        GameRegistry.registerItem(applePick, "applePick");
        GameRegistry.registerItem(appleHoe, "appleHoe");
        GameRegistry.registerItem(appleAxe, "appleAxe");
        GameRegistry.registerItem(appleSpade, "appleSpade");
        GameRegistry.registerItem(bearSword, "bearSword");
        GameRegistry.registerItem(bearPick, "bearPick");
        GameRegistry.registerItem(bearHoe, "bearHoe");
        GameRegistry.registerItem(bearAxe, "bearAxe");
        GameRegistry.registerItem(bearSpade, "bearSpade");
        GameRegistry.registerItem(friendShip, "friendShip");
        GameRegistry.registerItem(mavisOrb, "mavisOrb");
        GameRegistry.registerItem(eggTwilightMob, "eggTwilightMob");
        GameRegistry.registerItem(eggMyself, "eggMyself");
        GameRegistry.registerItem(eggCroc, "eggCroc");
        GameRegistry.registerItem(eggGiantChick, "eggGiantChick");
        GameRegistry.registerItem(eggChubbyMoron, "eggChuddyMoron");
        GameRegistry.registerItem(eggDemonSpider, "eggDemonSpider");
        GameRegistry.registerItem(eggLeviathan, "eggLeviathan");
        GameRegistry.registerItem(eggKingbowser, "eggKingbowser");
        GameRegistry.registerItem(eggCentipede, "eggCentipede");
        GameRegistry.registerItem(eggHydra, "eggHydra");
        GameRegistry.registerItem(eggWindigo, "eggWindigo");
        GameRegistry.registerItem(eggMoose, "eggMoose");
        GameRegistry.registerItem(eggBuffalo, "eggBuffalo");
        GameRegistry.registerItem(eggPowerOrb, "eggPowerOrb");
        GameRegistry.registerItem(eggFlameDragon, "eggFlameDragon");
        GameRegistry.registerItem(eggMooseBig, "eggMooseBig");
        GameRegistry.registerItem(eggBear, "eggBear");
        GameRegistry.registerItem(eggToughGuy, "eggToughGuy");
        GameRegistry.registerItem(eggMavis, "eggMavis");
        GameRegistry.registerItem(eggUrsaMAJOR, "eggUrsaMAJOR");
        GameRegistry.registerItem(eggDash, "eggDash");
        GameRegistry.registerItem(eggManticore, "eggManticore");
        GameRegistry.registerItem(dashSword, "dashSword");
        GameRegistry.registerItem(bearFur, "bearFur");
        GameRegistry.registerItem(bearTooth, "bearTooth");
        GameRegistry.registerItem(phoenixFeather, "phoenixFeather");
        GameRegistry.registerItem(cragScale, "cragScale");
        GameRegistry.registerItem(cragHammer, "cragHammer");
        GameRegistry.registerItem(eggRainbowCentipede, "cragRainbowCentipede");
        GameRegistry.registerItem(twilicornBone, "twilicornBone");
        GameRegistry.registerItem(eggAj, "eggAj");
        GameRegistry.registerItem(eggScorpion, "eggScorpion");
        GameRegistry.registerItem(darkCrystal, "darkCrystal");
        GameRegistry.registerItem(alicornSword, "alicornSword");
        GameRegistry.registerItem(eggTimberWolf, "eggTimberWolf");
        GameRegistry.registerItem(eggParasprite, "eggParasprite");
        GameRegistry.registerItem(stinger, "stinger");
        GameRegistry.registerItem(eggTMagic, "eggTMagic");
        GameRegistry.registerItem(eggCock, "eggCock");
        GameRegistry.registerItem(unstableItem, "unstableItem");
        GameRegistry.registerItem(dagger, "dagger");
        GameRegistry.registerItem(digger, "digger");
        GameRegistry.registerItem(eggCrabzilla, "eggCrabzilla");
        GameRegistry.registerItem(eggMinotaur, "eggMinotaur");
        GameRegistry.registerItem(derpMuffin, "derpMuffin");
        GameRegistry.registerItem(cupCakes, "cupCakes");
        GameRegistry.registerItem(rainbow, "rainbow");
        GameRegistry.registerItem(antenna, "antenna");
        GameRegistry.registerItem(ajSword, "ajSword");
        GameRegistry.registerItem(ajPick, "ajPick");
        GameRegistry.registerItem(ajHoe, "ajHoe");
        GameRegistry.registerItem(ajAxe, "ajAxe");
        GameRegistry.registerItem(ajSpade, "ajSpade");
        GameRegistry.registerItem(ajHelmet, "ajHelmet");
        GameRegistry.registerItem(ajChest, "ajChest");
        GameRegistry.registerItem(ajLegs, "ajLegs");
        GameRegistry.registerItem(ajBoots, "ajBoots");
        GameRegistry.registerItem(hardApple, "hardApple");
        GameRegistry.registerItem(eggrdCloud, "eggrdCloud");
        GameRegistry.registerItem(rdHelmet, "rdHelmet");
        GameRegistry.registerItem(rdChest, "rdChest");
        GameRegistry.registerItem(rdLegs, "rdLegs");
        GameRegistry.registerItem(rdBoots, "rdBoots");
        GameRegistry.registerItem(auroraGem, "auroraGem");
        GameRegistry.registerItem(crystalSword, "crystalSword");
        GameRegistry.registerItem(ursaClaws, "ursaClaws");
        GameRegistry.registerItem(eggSkullBoss, "eggSkullBoss");
        GameRegistry.registerItem(eggSkull, "eggSkull");
        GameRegistry.registerItem(dirtSword, "dirtSword");
        GameRegistry.registerItem(yakHorn, "yakHorn");
        GameRegistry.registerItem(eggYakPrince, "eggYakPrince");
        GameRegistry.registerItem(eggSpikey, "eggSpikey");
        GameRegistry.registerItem(eggRhino, "eggRhino");
        GameRegistry.registerItem(rhinoHorn, "rhinoHorn");
        LanguageRegistry.addName(boneBlock, "Bone Block");
        LanguageRegistry.addName(boneOre, "Bone Ore");
        LanguageRegistry.addName(boneItem, "Bone Wand");
        LanguageRegistry.addName(lunaEclipse, "Luna Eclipse");
        LanguageRegistry.addName(twilicornItem, "Twilicane");
        LanguageRegistry.addName(bowserRod, "Bowser's Rod");
        LanguageRegistry.addName(phoenixBow, "Phoenix Bow");
        LanguageRegistry.addName(bowserBone, "Reinforced Bone");
        LanguageRegistry.addName(bowserEye, "Eye Of Bowser");
        LanguageRegistry.addName(boneSword, "Bowser's Sword");
        LanguageRegistry.addName(boneBrick, "Bone Brick");
        LanguageRegistry.addName(glowBone, "Glow Bone");
        LanguageRegistry.addName(bonePick, "Bowser's Pickaxe");
        LanguageRegistry.addName(boneHoe, "Bowser's Hoe {LOL}");
        LanguageRegistry.addName(boneAxe, "Bowser's Axe");
        LanguageRegistry.addName(boneSpade, "Bowser's Shovel");
        LanguageRegistry.addName(phoenixBlock, "Phoenix Block");
        LanguageRegistry.addName(boneHelmet, "Bowser's Helmet");
        LanguageRegistry.addName(boneChest, "Bowser's ChestPlate");
        LanguageRegistry.addName(boneLegs, "Bowser's Leggings");
        LanguageRegistry.addName(boneBoots, "Bowser's Boots");
        LanguageRegistry.addName(friendShip, "Mane 6");
        LanguageRegistry.addName(twilicornSword, "Twilight Sparkle Sword");
        LanguageRegistry.addName(twilicornPick, "Twilight Sparkle Pickaxe");
        LanguageRegistry.addName(twilicornHoe, "Twilight Sparkle Hoe");
        LanguageRegistry.addName(twilicornAxe, "Twilight Sparkle Axe");
        LanguageRegistry.addName(twilicornSpade, "Twilight Sparkle Shovel");
        LanguageRegistry.addName(twilicornHelmet, "Twilight Sparkle Helmet");
        LanguageRegistry.addName(twilicornChest, "Twilight Sparkle Chestplate");
        LanguageRegistry.addName(twilicornLegs, "Twilight Sparkle Leggings");
        LanguageRegistry.addName(twilicornBoots, "Twilight Sparkle Boots");
        LanguageRegistry.addName(boneWall, "Bone Wall");
        LanguageRegistry.addName(twilicornBow, "Twilight Sparkle Bow");
        LanguageRegistry.addName(twilightStar, "Twilight Star");
        LanguageRegistry.addName(yoshiSword, "Random Explosive Item");
        LanguageRegistry.addName(lightningCloud, "Rainbow Cloud");
        LanguageRegistry.addName(diamondGems, "Diamond Gems");
        LanguageRegistry.addName(butterFlies, "Butterflies");
        LanguageRegistry.addName(apples, "Apples");
        LanguageRegistry.addName(balloons, "Balloons");
        LanguageRegistry.addName(twilicornBlock, "Twilight Block");
        LanguageRegistry.addName(dashMeat, "Dash Chop");
        LanguageRegistry.addName(twiMeat, "Twi Chop");
        LanguageRegistry.addName(darkHelmet, "Dark Crystal Helmet");
        LanguageRegistry.addName(darkChest, "Dark Crystal Chestplate");
        LanguageRegistry.addName(darkLegs, "Dark Crystal Leggings");
        LanguageRegistry.addName(darkBoots, "Dark Crystal Boots");
        LanguageRegistry.addName(appleHelmet, "Apple Helmet");
        LanguageRegistry.addName(appleChest, "Apple Chestplate");
        LanguageRegistry.addName(appleLegs, "Apple Leggings");
        LanguageRegistry.addName(appleBoots, "Apple Boots");
        LanguageRegistry.addName(bearHelmet, "Bear Fur Helmet");
        LanguageRegistry.addName(bearChest, "Bear Fur Chestplate");
        LanguageRegistry.addName(bearLegs, "Bear Fur Leggings");
        LanguageRegistry.addName(bearBoots, "Bear Fur Boots");
        LanguageRegistry.addName(appleSword, "Apple Sword");
        LanguageRegistry.addName(applePick, "Apple Pickaxe");
        LanguageRegistry.addName(appleHoe, "Apple Hoe");
        LanguageRegistry.addName(appleAxe, "Apple Axe");
        LanguageRegistry.addName(appleSpade, "Apple Shovel");
        LanguageRegistry.addName(bearSword, "Bear Claw Sword");
        LanguageRegistry.addName(bearPick, "Bear Claw Pickaxe");
        LanguageRegistry.addName(bearHoe, "Bear Claw Hoe");
        LanguageRegistry.addName(bearAxe, "Bear Claw Axe");
        LanguageRegistry.addName(bearSpade, "Bear Claw Shovel");
        LanguageRegistry.addName(mavisOrb, "Mavis Orbs");
        LanguageRegistry.addName(dashSword, "Rainbow Dash Sword");
        LanguageRegistry.addName(eggTwilightMob, "Spawn Robot Sombra");
        LanguageRegistry.addName(eggMyself, "Spawn Twilight Sparkle");
        LanguageRegistry.addName(eggCroc, "Spawn Cragadile");
        LanguageRegistry.addName(eggGiantChick, "Spawn Phoenix");
        LanguageRegistry.addName(eggChubbyMoron, "Spawn Chief ThunderHooves");
        LanguageRegistry.addName(eggDemonSpider, "Spawn Black Widow Spider");
        LanguageRegistry.addName(eggLeviathan, "Spawn Leviathan");
        LanguageRegistry.addName(eggKingbowser, "Spawn KingBowser9000");
        LanguageRegistry.addName(eggCentipede, "Spawn Centipede");
        LanguageRegistry.addName(eggHydra, "Spawn Hydra");
        LanguageRegistry.addName(eggWindigo, "Spawn Windigo");
        LanguageRegistry.addName(eggMoose, "Spawn Baby Moose");
        LanguageRegistry.addName(eggBuffalo, "Spawn Buffalo");
        LanguageRegistry.addName(eggPowerOrb, "Spawn Leech");
        LanguageRegistry.addName(eggFlameDragon, "Spawn Garble");
        LanguageRegistry.addName(eggMooseBig, "Spawn Adult Moose");
        LanguageRegistry.addName(eggBear, "Spawn Bear");
        LanguageRegistry.addName(eggToughGuy, "Spawn Tough Guy");
        LanguageRegistry.addName(eggMavis, "Spawn Mavis");
        LanguageRegistry.addName(eggUrsaMAJOR, "Spawn Ursa MAJOR!!!!");
        LanguageRegistry.addName(eggDash, "Spawn Rainbow Dash");
        LanguageRegistry.addName(eggManticore, "Spawn Manticore");
        LanguageRegistry.addName(bearFur, "Bear Fur");
        LanguageRegistry.addName(bearTooth, "Bear Claw");
        LanguageRegistry.addName(robotBlock, "Robot Block");
        LanguageRegistry.addName(bearFurBlock, "Bear Fur");
        LanguageRegistry.addName(phoenixFeather, "Phoenix Feather");
        LanguageRegistry.addName(cragScale, "Cragadile Scale");
        LanguageRegistry.addName(cragHammer, "The Crag Hammer");
        LanguageRegistry.addName(eggRainbowCentipede, "Spawn Rainbow Centipede");
        LanguageRegistry.addName(twilicornBone, "Twilight Bone");
        LanguageRegistry.addName(appleBlock, "Sweet Delicious Tasty Apple Block");
        LanguageRegistry.addName(eggAj, "Spawn AppleJack");
        LanguageRegistry.addName(eggScorpion, "Spawn Arctic Scorpion");
        LanguageRegistry.addName(darkCrystal, "Dark Crystal");
        LanguageRegistry.addName(alicornSword, "The Alicorn Sword");
        LanguageRegistry.addName(eggTimberWolf, "Spawn Timber Wolf");
        LanguageRegistry.addName(eggParasprite, "Spawn Parasprite");
        LanguageRegistry.addName(stinger, "Arctic Stinger");
        LanguageRegistry.addName(eggTMagic, "Spawn Twilicorn Magic");
        LanguageRegistry.addName(eggCock, "Spawn Cockatrice");
        LanguageRegistry.addName(unstableItem, "Unstable Item");
        LanguageRegistry.addName(dagger, "Dagger");
        LanguageRegistry.addName(digger, "Digger");
        LanguageRegistry.addName(eggCrabzilla, "Spawn Crabzilla!!!");
        LanguageRegistry.addName(eggMinotaur, "Spawn Iron Will");
        LanguageRegistry.addName(derpMuffin, "Muffin");
        LanguageRegistry.addName(cupCakes, "Cupcake!");
        LanguageRegistry.addName(rainbow, "Rainbow");
        LanguageRegistry.addName(antenna, "Centipede Antenna");
        LanguageRegistry.addName(arcticBlock, "Arctic Block");
        LanguageRegistry.addName(ajSword, "AppleJack Sword");
        LanguageRegistry.addName(ajPick, "AppleJack Pickaxe");
        LanguageRegistry.addName(ajHoe, "AppleJack Hoe");
        LanguageRegistry.addName(ajAxe, "AppleJack Axe");
        LanguageRegistry.addName(ajSpade, "AppleJack Shovel");
        LanguageRegistry.addName(ajHelmet, "AppleJack Helmet");
        LanguageRegistry.addName(ajChest, "AppleJack Chestplate");
        LanguageRegistry.addName(ajLegs, "AppleJack Leggings");
        LanguageRegistry.addName(ajBoots, "AppleJack Boots");
        LanguageRegistry.addName(hardApple, "Hardened Apple");
        LanguageRegistry.addName(hardAppleBlock, "Hardened Apple Block");
        LanguageRegistry.addName(eggrdCloud, "Spawn Lightning Cloud");
        LanguageRegistry.addName(rdHelmet, "Rainbow Dash Helmet");
        LanguageRegistry.addName(rdChest, "Rainbow Dash Chestplate");
        LanguageRegistry.addName(rdLegs, "Rainbow Dash Leggings");
        LanguageRegistry.addName(rdBoots, "Rainbow Dash Boots");
        LanguageRegistry.addName(crystalOre, "Crystal Ore");
        LanguageRegistry.addName(crystalBlock, "Crystal Block");
        LanguageRegistry.addName(auroraGem, "Crystal Gem");
        LanguageRegistry.addName(crystalSword, "Crystal Sword");
        LanguageRegistry.addName(ursaClaws, "The Ursa's Claw");
        LanguageRegistry.addName(eggSkullBoss, "Spawn Skull of Doom");
        LanguageRegistry.addName(eggSkull, "Spawn Skull");
        LanguageRegistry.addName(dirtSword, "Dirt Sword");
        LanguageRegistry.addName(yakHorn, "Yak Horn");
        LanguageRegistry.addName(eggYakPrince, "Spawn Prince Rutherford");
        LanguageRegistry.addName(eggSpikey, "Spawn Spikezilla");
        LanguageRegistry.addName(eggRhino, "Spawn Rhinoceros");
        LanguageRegistry.addName(rhinoHorn, "Rhino Horn");
        LanguageRegistry.addName(bowserRod, "Bowser's Rod");
        LanguageRegistry.addName(darkCrystalBlock, "Dark Crystal Block");
        GameRegistry.addShapelessRecipe(new ItemStack(twilightStar, 9), new ItemStack(twilicornBlock));
        GameRegistry.addShapelessRecipe(new ItemStack(phoenixFeather, 9), new ItemStack(phoenixBlock));
        GameRegistry.addShapelessRecipe(new ItemStack(apples, 9), new ItemStack(appleBlock));
        GameRegistry.addShapelessRecipe(new ItemStack(boneBrick, 1), new ItemStack(boneBlock));
        GameRegistry.addShapelessRecipe(new ItemStack(boneWall, 1), new ItemStack(boneBrick));
        GameRegistry.addShapelessRecipe(new ItemStack(glowBone, 1), new ItemStack(boneWall));
        GameRegistry.addShapelessRecipe(new ItemStack(darkCrystal, 9), new ItemStack(darkCrystalBlock));
        GameRegistry
            .addRecipe(new ItemStack(twilicornSword), " T ", " T ", " D ", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornSword), "T  ", "T  ", "D  ", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornSword), "  T", "  T", "  D", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornPick), "TTT", " D ", " D ", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornSpade), " T ", " D ", " D ", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornSpade), "T  ", "D  ", "D  ", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornSpade), "  T", "  D", "  D", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornAxe), "TT ", "TD ", " D ", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornAxe), " TT", " TD", "  D", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornHoe), "TT ", " D ", " D ", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornHoe), " TT", "  D", "  D", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornHelmet), "TTT", "T T", "   ", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornHelmet), "   ", "TTT", "T T", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornChest), "T T", "TTT", "TTT", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornLegs), "TTT", "T T", "T T", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornBoots), "T T", "T T", "   ", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornBoots), "   ", "T T", "T T", 'D', twilicornBone, 'T', twilicornBlock);
        GameRegistry
            .addRecipe(new ItemStack(twilicornBlock), "TTT", "TTT", "TTT", 'D', twilicornBone, 'T', twilightStar);
        GameRegistry
            .addRecipe(new ItemStack(twilicornBow), " TD", "T D", " TD", 'D', twilightStar, 'T', twilicornBlock);
        GameRegistry.addRecipe(new ItemStack(phoenixBlock), "TTT", "TTT", "TTT", 'T', phoenixFeather);
        GameRegistry.addRecipe(new ItemStack(boneSword), " T ", " T ", " D ", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(boneSword), "T  ", "T  ", "D  ", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(boneSword), "  T", "  T", "  D", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(bonePick), "TTT", " D ", " D ", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(boneSpade), " T ", " D ", " D ", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(boneSpade), "T  ", "D  ", "D  ", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(boneSpade), "  T", "  D", "  D", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(boneAxe), "TT ", "TD ", " D ", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(boneAxe), " TT", " TD", "  D", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(boneHoe), "TT ", " D ", " D ", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(boneHoe), " TT", "  D", "  D", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(
            new ItemStack(boneHelmet),
            "BCB",
            "ACA",
            "CCC",
            'A',
            bowserEye,
            'B',
            phoenixBlock,
            'C',
            bowserBone);
        GameRegistry.addRecipe(new ItemStack(boneChest), "T T", "TTT", "TTT", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(boneLegs), "TTT", "T T", "T T", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(boneBoots), "T T", "T T", "   ", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(boneBoots), "   ", "T T", "T T", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(boneBlock), "DDD", "DDD", "DDD", 'D', bowserBone, 'T', phoenixBlock);
        GameRegistry.addRecipe(new ItemStack(phoenixBow), " TD", "T D", " TD", 'T', phoenixBlock, 'D', phoenixFeather);
        GameRegistry.addRecipe(new ItemStack(cragHammer), "TTT", "TDT", " D ", 'D', Items.stick, 'T', cragScale);
        GameRegistry.addRecipe(new ItemStack(bearHelmet), "TTT", "T T", "   ", 'D', twilicornBone, 'T', bearFur);
        GameRegistry.addRecipe(new ItemStack(bearHelmet), "   ", "TTT", "T T", 'D', twilicornBone, 'T', bearFur);
        GameRegistry.addRecipe(new ItemStack(bearChest), "T T", "TTT", "TTT", 'D', twilicornBone, 'T', bearFur);
        GameRegistry.addRecipe(new ItemStack(bearLegs), "TTT", "T T", "T T", 'D', twilicornBone, 'T', bearFur);
        GameRegistry.addRecipe(new ItemStack(bearBoots), "T T", "T T", "   ", 'D', twilicornBone, 'T', bearFur);
        GameRegistry.addRecipe(new ItemStack(bearBoots), "   ", "T T", "T T", 'D', twilicornBone, 'T', bearFur);
        GameRegistry.addRecipe(new ItemStack(appleSword), " T ", " T ", " D ", 'D', Items.stick, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleSword), "T  ", "T  ", "D  ", 'D', Items.stick, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleSword), "  T", "  T", "  D", 'D', Items.stick, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(applePick), "TTT", " D ", " D ", 'D', Items.stick, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleSpade), " T ", " D ", " D ", 'D', Items.stick, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleSpade), "T  ", "D  ", "D  ", 'D', Items.stick, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleSpade), "  T", "  D", "  D", 'D', Items.stick, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleAxe), "TT ", "TD ", " D ", 'D', Items.stick, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleAxe), " TT", " TD", "  D", 'D', Items.stick, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleHoe), "TT ", " D ", " D ", 'D', Items.stick, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleHoe), " TT", "  D", "  D", 'D', Items.stick, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleHelmet), "TTT", "T T", "   ", 'D', twilicornBone, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleHelmet), "   ", "TTT", "T T", 'D', twilicornBone, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleChest), "T T", "TTT", "TTT", 'D', twilicornBone, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleLegs), "TTT", "T T", "T T", 'D', twilicornBone, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleBoots), "T T", "T T", "   ", 'D', twilicornBone, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleBoots), "   ", "T T", "T T", 'D', twilicornBone, 'T', appleBlock);
        GameRegistry.addRecipe(new ItemStack(appleBlock), "TTT", "TTT", "TTT", 'T', apples);
        GameRegistry.addRecipe(new ItemStack(darkHelmet), "TTT", "T T", "   ", 'D', twilicornBone, 'T', darkCrystal);
        GameRegistry.addRecipe(new ItemStack(darkHelmet), "   ", "TTT", "T T", 'D', twilicornBone, 'T', darkCrystal);
        GameRegistry.addRecipe(new ItemStack(darkChest), "T T", "TTT", "TTT", 'D', twilicornBone, 'T', darkCrystal);
        GameRegistry.addRecipe(new ItemStack(darkLegs), "TTT", "T T", "T T", 'D', twilicornBone, 'T', darkCrystal);
        GameRegistry.addRecipe(new ItemStack(darkBoots), "T T", "T T", "   ", 'D', twilicornBone, 'T', darkCrystal);
        GameRegistry.addRecipe(new ItemStack(darkBoots), "   ", "T T", "T T", 'D', twilicornBone, 'T', darkCrystal);
        GameRegistry.addRecipe(new ItemStack(bearSword), " T ", " T ", " D ", 'D', Items.stick, 'T', bearTooth);
        GameRegistry.addRecipe(new ItemStack(bearSword), "T  ", "T  ", "D  ", 'D', Items.stick, 'T', bearTooth);
        GameRegistry.addRecipe(new ItemStack(bearSword), "  T", "  T", "  D", 'D', Items.stick, 'T', bearTooth);
        GameRegistry.addRecipe(new ItemStack(bearPick), "TTT", " D ", " D ", 'D', Items.stick, 'T', bearTooth);
        GameRegistry.addRecipe(new ItemStack(bearSpade), " T ", " D ", " D ", 'D', Items.stick, 'T', bearTooth);
        GameRegistry.addRecipe(new ItemStack(bearSpade), "T  ", "D  ", "D  ", 'D', Items.stick, 'T', bearTooth);
        GameRegistry.addRecipe(new ItemStack(bearSpade), "  T", "  D", "  D", 'D', Items.stick, 'T', bearTooth);
        GameRegistry.addRecipe(new ItemStack(bearAxe), "TT ", "TD ", " D ", 'D', Items.stick, 'T', bearTooth);
        GameRegistry.addRecipe(new ItemStack(bearAxe), " TT", " TD", "  D", 'D', Items.stick, 'T', bearTooth);
        GameRegistry.addRecipe(new ItemStack(bearHoe), "TT ", " D ", " D ", 'D', Items.stick, 'T', bearTooth);
        GameRegistry.addRecipe(new ItemStack(bearHoe), " TT", "  D", "  D", 'D', Items.stick, 'T', bearTooth);
        GameRegistry.addRecipe(new ItemStack(dashSword), " T ", " T ", " D ", 'D', stinger, 'T', lightningCloud);
        GameRegistry.addRecipe(new ItemStack(dashSword), "T  ", "T  ", "D  ", 'D', stinger, 'T', lightningCloud);
        GameRegistry.addRecipe(new ItemStack(dashSword), "  T", "  T", "  D", 'D', stinger, 'T', lightningCloud);
        GameRegistry.addRecipe(new ItemStack(hardAppleBlock), "DDD", "DTD", "DDD", 'D', hardApple, 'T', apples);
        GameRegistry.addRecipe(new ItemStack(hardApple), "DDD", "DTD", "DDD", 'D', Blocks.gold_block, 'T', apples);
        GameRegistry.addRecipe(new ItemStack(ajSword), " T ", " T ", " D ", 'D', Items.stick, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajSword), "T  ", "T  ", "D  ", 'D', Items.stick, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajSword), "  T", "  T", "  D", 'D', Items.stick, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajPick), "TTT", " D ", " D ", 'D', Items.stick, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajSpade), " T ", " D ", " D ", 'D', Items.stick, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajSpade), "T  ", "D  ", "D  ", 'D', Items.stick, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajSpade), "  T", "  D", "  D", 'D', Items.stick, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajAxe), "TT ", "TD ", " D ", 'D', Items.stick, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajAxe), " TT", " TD", "  D", 'D', Items.stick, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajHoe), "TT ", " D ", " D ", 'D', Items.stick, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajHoe), " TT", "  D", "  D", 'D', Items.stick, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajHelmet), "TTT", "T T", "   ", 'D', twilicornBone, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajHelmet), "   ", "TTT", "T T", 'D', twilicornBone, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajChest), "T T", "TTT", "TTT", 'D', twilicornBone, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajLegs), "TTT", "T T", "T T", 'D', twilicornBone, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajBoots), "T T", "T T", "   ", 'D', twilicornBone, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(ajBoots), "   ", "T T", "T T", 'D', twilicornBone, 'T', hardAppleBlock);
        GameRegistry.addRecipe(new ItemStack(crystalSword), "T  ", "T  ", "D  ", 'D', diamondGems, 'T', auroraGem);
        GameRegistry.addRecipe(new ItemStack(dirtSword), "T  ", "T  ", "D  ", 'D', Items.stick, 'T', Blocks.dirt);
        proxy.registerSound();
        EntityRegistry.instance();
        EntityRegistry.registerGlobalEntityID(FireCannon.class, "MeteorFire", entityId);
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(FireCannon.class, "MeteorFire", entityId, this, 64, 1, true);
        EntityRegistry.instance();
        EntityRegistry.registerGlobalEntityID(MavisBall.class, "MavisBall", entityId);
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MavisBall.class, "MavisBall", entityId, this, 64, 1, true);
        EntityRegistry.instance();
        EntityRegistry.registerGlobalEntityID(TwilyMagic.class, "TwiMagic", entityId);
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(TwilyMagic.class, "TwiMagic", entityId, this, 64, 1, true);
        EntityRegistry.instance();
        EntityRegistry.registerGlobalEntityID(WindigoPower.class, "WindigoPower", entityId);
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(WindigoPower.class, "WindigoPower", entityId, this, 64, 1, true);
        EntityRegistry.instance();
        EntityRegistry.registerGlobalEntityID(DarkCrystalCannon.class, "DarkCrystal", entityId);
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(DarkCrystalCannon.class, "DarkCrystal", entityId, this, 64, 1, true);
        EntityRegistry.instance();
        EntityRegistry.registerGlobalEntityID(PhoenixArrow.class, "PhoenixArrow", entityId);
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(PhoenixArrow.class, "PhoenixArrow", entityId, this, 64, 1, true);
        EntityRegistry.instance();
        EntityRegistry.registerGlobalEntityID(TwilicornArrow.class, "TwiArrow", entityId);
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(TwilicornArrow.class, "TwiArrow", entityId, this, 64, 1, true);
        EntityRegistry.instance();
        EntityRegistry.registerGlobalEntityID(LongHit.class, "LongHit", entityId);
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(LongHit.class, "LongHit", entityId, this, 64, 1, true);
        EntityRegistry.instance();
        EntityRegistry.registerGlobalEntityID(ProjectileApples.class, "ThrownApple", entityId);
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(ProjectileApples.class, "ThrownApple", entityId, this, 64, 1, true);
        EntityRegistry.instance();
        EntityRegistry.registerGlobalEntityID(RainbowCannon.class, "RBCannon", entityId);
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(RainbowCannon.class, "RBCannon", entityId, this, 64, 1, true);
        EntityRegistry.registerGlobalEntityID(TwilightMob.class, "TwilightMob", entityId);
        EntityRegistry.instance();
        MobId1 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.TwilightMob.name", "Robot Sombra");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(TwilightMob.class, "TwilightMob", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(DemonSpiderEntity.class, "DemonSpiderEntity", entityId);
        EntityRegistry.instance();
        MobId2 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.DemonSpiderEntity.name", "Black Widow Spider");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(DemonSpiderEntity.class, "DemonSpiderEntity", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(MyLeviathan.class, "MyLeviathan", entityId);
        EntityRegistry.instance();
        MobId3 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyLeviathan.name", "Leviathan");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyLeviathan.class, "MyLeviathan", entityId, this, 72, 1, false);
        EntityRegistry.registerGlobalEntityID(MyThunderHooves.class, "MyThunderHooves", entityId);
        EntityRegistry.instance();
        MobId4 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyThunderHooves.name", "Chief ThunderHooves");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyThunderHooves.class, "MyThunderHooves", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(MyTwilicorn.class, "MyTwilicorn", entityId);
        EntityRegistry.instance();
        MobId5 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyTwilicorn.name", "Twilight Sparkle");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyTwilicorn.class, "MyTwilicorn", entityId, this, 128, 1, false);
        EntityRegistry.registerGlobalEntityID(MyKingbowser.class, "MyKingbowser", entityId);
        EntityRegistry.instance();
        MobId6 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyKingbowser.name", "KingBowser9000");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyKingbowser.class, "MyKingbowser", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(MyCentipede.class, "MyCentipede", entityId);
        EntityRegistry.instance();
        MobId7 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyCentipede.name", "{MLP} Centipede");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyCentipede.class, "MyCentipede", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(MyMLPHydra.class, "MyMLPHydra", entityId);
        EntityRegistry.instance();
        MobId8 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyMLPHydra.name", "Hydra");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyMLPHydra.class, "MyMLPHydra", entityId, this, 128, 1, false);
        EntityRegistry.registerGlobalEntityID(MyWindigo.class, "MyWindigo", entityId);
        EntityRegistry.instance();
        MobId9 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyWindigo.name", "Windigo");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyWindigo.class, "MyWindigo", entityId, this, 128, 1, false);
        EntityRegistry.registerGlobalEntityID(MyMoose.class, "MyMoose", entityId);
        EntityRegistry.instance();
        MobId10 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyMoose.name", "Baby Ramming Moose");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyMoose.class, "MyMoose", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(MyMLPBuffalo.class, "MyMLPBuffalo", entityId);
        EntityRegistry.instance();
        MobId11 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyMLPBuffalo.name", "Buffalo");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyMLPBuffalo.class, "MyMLPBuffalo", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(MyDarkness.class, "MyDarkness", entityId);
        EntityRegistry.instance();
        MobId12 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyDarkness.name", "Leech");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyDarkness.class, "MyDarkness", entityId, this, 128, 1, false);
        EntityRegistry.registerGlobalEntityID(MyFlameDragon.class, "MyFlameDragon", entityId);
        EntityRegistry.instance();
        MobId13 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyFlameDragon.name", "Garble");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyFlameDragon.class, "MyFlameDragon", entityId, this, 72, 1, false);
        EntityRegistry.registerGlobalEntityID(MyMooseBig.class, "MyMooseBig", entityId);
        EntityRegistry.instance();
        MobId14 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyMooseBig.name", "Adult Ramming Moose");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyMooseBig.class, "MyMooseBig", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(MyCragadile.class, "MyCragadile", entityId);
        EntityRegistry.instance();
        MobId15 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyCragadile.name", "Cragadile");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyCragadile.class, "MyCragadile", entityId, this, 72, 1, false);
        EntityRegistry.registerGlobalEntityID(MyBear.class, "MyBear", entityId);
        EntityRegistry.instance();
        MobId16 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyBear.name", "Bear");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyBear.class, "MyBear", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(MyToughGuy.class, "MyToughGuy", entityId);
        EntityRegistry.instance();
        MobId17 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyToughGuy.name", "Tough Guy");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyToughGuy.class, "MyToughGuy", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(MyMavis.class, "MyMavis", entityId);
        EntityRegistry.instance();
        MobId18 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyMavis.name", "Mavis");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyMavis.class, "MyMavis", entityId, this, 32, 1, false);
        EntityRegistry.registerGlobalEntityID(MyUrsaMAJOR.class, "MyUrsaMAJOR", entityId);
        EntityRegistry.instance();
        MobId19 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyUrsaMAJOR.name", "Ursa Major");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyUrsaMAJOR.class, "MyUrsaMAJOR", entityId, this, 128, 1, false);
        EntityRegistry.registerGlobalEntityID(MyPhoenix.class, "MyPhoenix", entityId);
        EntityRegistry.instance();
        MobId20 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyPhoenix.name", "Phoenix");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyPhoenix.class, "MyPhoenix", entityId, this, 72, 1, true);
        EntityRegistry.registerGlobalEntityID(MyDash.class, "MyDash", entityId);
        EntityRegistry.instance();
        MobId21 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyDash.name", "Rainbow Dash");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyDash.class, "MyDash", entityId, this, 128, 1, false);
        EntityRegistry.registerGlobalEntityID(MyManticore.class, "MyManticore", entityId);
        EntityRegistry.instance();
        MobId22 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyManticore.name", "Manticore");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyManticore.class, "MyManticore", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(MyRainbowCentipede.class, "MyRainbowCentipede", entityId);
        EntityRegistry.instance();
        MobId23 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyRainbowCentipede.name", "Rainbow Centipede");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyRainbowCentipede.class, "MyRainbowCentipede", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(MyAJ.class, "MyAJ", entityId);
        EntityRegistry.instance();
        MobId24 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyAJ.name", "AppleJack");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyAJ.class, "MyAJ", entityId, this, 72, 1, false);
        EntityRegistry.registerGlobalEntityID(MyArcticScorpion.class, "MyArcticScorpion", entityId);
        EntityRegistry.instance();
        MobId25 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyArcticScorpion.name", "Arctic Scorpion");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyArcticScorpion.class, "MyArcticScorpion", entityId, this, 72, 1, false);
        EntityRegistry.registerGlobalEntityID(MyTimberWolf.class, "MyTimberWolf", entityId);
        EntityRegistry.instance();
        MobId26 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyTimberWolf.name", "Timber Wolf");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyTimberWolf.class, "MyTimberWolf", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(MyParasprite.class, "MyParasprite", entityId);
        EntityRegistry.instance();
        MobId27 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyParasprite.name", "Parasprite");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyParasprite.class, "MyParasprite", entityId, this, 16, 1, false);
        EntityRegistry.registerGlobalEntityID(MyTwilightMagic.class, "MyTwilightMagic", entityId);
        EntityRegistry.instance();
        MobId28 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyTwilightMagic.name", "Twilicorn Magic");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyTwilightMagic.class, "MyTwilightMagic", entityId, this, 128, 1, false);
        EntityRegistry.registerGlobalEntityID(MyCockatrice.class, "MyCockatrice", entityId);
        EntityRegistry.instance();
        MobId29 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyCockatrice.name", "Cockatrice");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyCockatrice.class, "MyCockatrice", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(MyIronWill.class, "MyIronWill", entityId);
        EntityRegistry.instance();
        MobId30 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyIronWill.name", "Iron Will");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyIronWill.class, "MyIronWill", entityId, this, 64, 1, false);
        EntityRegistry.registerGlobalEntityID(MyCrabzilla.class, "MyCrabzilla", entityId);
        EntityRegistry.instance();
        MobId31 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyCrabzilla.name", "Crabzilla");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyCrabzilla.class, "MyCrabzilla", entityId, this, 128, 1, false);
        EntityRegistry.registerGlobalEntityID(MyDashCloud.class, "MyDashCloud", entityId);
        EntityRegistry.instance();
        MobId32 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyDashCloud.name", "Lightning Cloud");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyDashCloud.class, "MyDashCloud", entityId, this, 128, 1, false);
        EntityRegistry.registerGlobalEntityID(MySkullBoss.class, "MySkullBoss", entityId);
        EntityRegistry.instance();
        MobId33 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MySkullBoss.name", "Skull Of Doom");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MySkullBoss.class, "MySkullBoss", entityId, this, 128, 1, false);
        EntityRegistry.registerGlobalEntityID(MySkull.class, "MySkull", entityId);
        EntityRegistry.instance();
        MobId34 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MySkullBoss.name", "Skull");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MySkull.class, "MySkull", entityId, this, 128, 1, false);
        EntityRegistry.registerGlobalEntityID(MyYakPrince.class, "MyYakPrince", entityId);
        EntityRegistry.instance();
        MobId35 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyYakPrince.name", "Prince Rutherford");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyYakPrince.class, "MyYakPrince", entityId, this, 72, 1, false);
        EntityRegistry.registerGlobalEntityID(MySpikezilla.class, "MySpikezilla", entityId);
        EntityRegistry.instance();
        MobId36 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MySpikezilla.name", "Spikezilla");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MySpikezilla.class, "MySpikezilla", entityId, this, 256, 1, false);
        EntityRegistry.registerGlobalEntityID(MyRhinoceros.class, "MyRhinoceros", entityId);
        EntityRegistry.instance();
        MobId37 = entityId;
        LanguageRegistry.instance()
            .addStringLocalization("entity.MyRhinoceros.name", "Rhinoceros");
        EntityRegistry.instance();
        EntityRegistry.registerModEntity(MyRhinoceros.class, "MyRhinoceros", entityId, this, 64, 1, false);
        EntityRegistry.addSpawn(MyThunderHooves.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.forest);
        EntityRegistry.addSpawn(MyThunderHooves.class, 2, 1, 1, EnumCreatureType.ambient, BiomeGenBase.desert);
        EntityRegistry.addSpawn(MyMLPBuffalo.class, 5, 2, 4, EnumCreatureType.monster, BiomeGenBase.desert);
        EntityRegistry.addSpawn(MyMLPBuffalo.class, 5, 2, 3, EnumCreatureType.monster, BiomeGenBase.savanna);
        EntityRegistry.addSpawn(MyMLPBuffalo.class, 5, 2, 3, EnumCreatureType.monster, BiomeGenBase.savannaPlateau);
        EntityRegistry.addSpawn(MyMLPBuffalo.class, 5, 2, 4, EnumCreatureType.ambient, BiomeGenBase.desert);
        EntityRegistry.addSpawn(MyMLPBuffalo.class, 5, 2, 3, EnumCreatureType.ambient, BiomeGenBase.savanna);
        EntityRegistry.addSpawn(MyMLPBuffalo.class, 5, 2, 3, EnumCreatureType.ambient, BiomeGenBase.savannaPlateau);
        EntityRegistry.addSpawn(MyCragadile.class, 1, 1, 1, EnumCreatureType.waterCreature, BiomeGenBase.river);
        EntityRegistry.addSpawn(MyCragadile.class, 1, 1, 2, EnumCreatureType.waterCreature, BiomeGenBase.swampland);
        EntityRegistry.addSpawn(MyCragadile.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.swampland);
        EntityRegistry.addSpawn(MyWindigo.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.iceMountains);
        EntityRegistry.addSpawn(MyWindigo.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.extremeHillsPlus);
        EntityRegistry.addSpawn(MyMLPHydra.class, 1, 1, 1, EnumCreatureType.creature, BiomeGenBase.swampland);
        EntityRegistry.addSpawn(MyMLPHydra.class, 1, 1, 1, EnumCreatureType.creature, BiomeGenBase.stoneBeach);
        EntityRegistry.addSpawn(MyMLPHydra.class, 1, 1, 1, EnumCreatureType.monster, BiomeGenBase.hell);
        EntityRegistry.addSpawn(MyFlameDragon.class, 1, 1, 1, EnumCreatureType.monster, BiomeGenBase.hell);
        EntityRegistry.addSpawn(DemonSpiderEntity.class, 3, 1, 6, EnumCreatureType.ambient, BiomeGenBase.jungle);
        EntityRegistry.addSpawn(DemonSpiderEntity.class, 3, 1, 6, EnumCreatureType.ambient, BiomeGenBase.jungleHills);
        EntityRegistry.addSpawn(DemonSpiderEntity.class, 2, 1, 1, EnumCreatureType.ambient, BiomeGenBase.forest);
        EntityRegistry.addSpawn(MyRainbowCentipede.class, 4, 1, 4, EnumCreatureType.monster, BiomeGenBase.jungle);
        EntityRegistry.addSpawn(MyRainbowCentipede.class, 4, 1, 4, EnumCreatureType.monster, BiomeGenBase.jungleHills);
        EntityRegistry.addSpawn(MyCentipede.class, 14, 5, 12, EnumCreatureType.ambient, BiomeGenBase.jungle);
        EntityRegistry.addSpawn(MyCentipede.class, 14, 5, 12, EnumCreatureType.ambient, BiomeGenBase.jungleHills);
        EntityRegistry.addSpawn(MyCentipede.class, 4, 4, 6, EnumCreatureType.ambient, BiomeGenBase.forest);
        EntityRegistry.addSpawn(MyCentipede.class, 14, 3, 12, EnumCreatureType.ambient, BiomeGenBase.jungleEdge);
        EntityRegistry.addSpawn(MyCentipede.class, 4, 4, 6, EnumCreatureType.ambient, BiomeGenBase.birchForest);
        EntityRegistry.addSpawn(MyArcticScorpion.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.iceMountains);
        EntityRegistry.addSpawn(MyArcticScorpion.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.icePlains);
        EntityRegistry.addSpawn(MyArcticScorpion.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.frozenOcean);
        EntityRegistry.addSpawn(MyArcticScorpion.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.frozenRiver);
        EntityRegistry
            .addSpawn(MyArcticScorpion.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.extremeHillsPlus);
        EntityRegistry.addSpawn(MyBear.class, 10, 2, 20, EnumCreatureType.ambient, BiomeGenBase.forest);
        EntityRegistry.addSpawn(MyBear.class, 10, 2, 15, EnumCreatureType.ambient, BiomeGenBase.birchForest);
        EntityRegistry.addSpawn(MyBear.class, 15, 4, 25, EnumCreatureType.ambient, BiomeGenBase.roofedForest);
        EntityRegistry.addSpawn(MyBear.class, 10, 2, 15, EnumCreatureType.ambient, BiomeGenBase.taiga);
        EntityRegistry.addSpawn(MyBear.class, 15, 2, 15, EnumCreatureType.ambient, BiomeGenBase.forestHills);
        EntityRegistry.addSpawn(MyBear.class, 2, 1, 1, EnumCreatureType.monster, BiomeGenBase.sky);
        EntityRegistry.addSpawn(MyPhoenix.class, 2, 1, 5, EnumCreatureType.ambient, BiomeGenBase.extremeHills);
        EntityRegistry.addSpawn(MyPhoenix.class, 2, 1, 5, EnumCreatureType.ambient, BiomeGenBase.extremeHillsEdge);
        EntityRegistry.addSpawn(MyPhoenix.class, 2, 1, 6, EnumCreatureType.ambient, BiomeGenBase.mesa);
        EntityRegistry.addSpawn(MyPhoenix.class, 2, 2, 5, EnumCreatureType.ambient, BiomeGenBase.mesaPlateau);
        EntityRegistry.addSpawn(MyPhoenix.class, 3, 2, 5, EnumCreatureType.ambient, BiomeGenBase.mesaPlateau_F);
        EntityRegistry.addSpawn(MyDash.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.forest);
        EntityRegistry.addSpawn(MyDash.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.plains);
        EntityRegistry.addSpawn(MyDash.class, 2, 1, 1, EnumCreatureType.ambient, BiomeGenBase.beach);
        EntityRegistry.addSpawn(MyDash.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.birchForest);
        EntityRegistry.addSpawn(MyAJ.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.forest);
        EntityRegistry.addSpawn(MyAJ.class, 2, 1, 1, EnumCreatureType.ambient, BiomeGenBase.plains);
        EntityRegistry.addSpawn(MyAJ.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.beach);
        EntityRegistry.addSpawn(MyAJ.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.birchForest);
        EntityRegistry.addSpawn(MyMoose.class, 40, 1, 6, EnumCreatureType.ambient, BiomeGenBase.forest);
        EntityRegistry.addSpawn(MyMoose.class, 40, 1, 6, EnumCreatureType.ambient, BiomeGenBase.taiga);
        EntityRegistry.addSpawn(MyMoose.class, 40, 1, 6, EnumCreatureType.ambient, BiomeGenBase.birchForest);
        EntityRegistry.addSpawn(MyMoose.class, 40, 1, 6, EnumCreatureType.ambient, BiomeGenBase.taigaHills);
        EntityRegistry.addSpawn(MyMooseBig.class, 30, 1, 5, EnumCreatureType.ambient, BiomeGenBase.forest);
        EntityRegistry.addSpawn(MyMooseBig.class, 30, 1, 5, EnumCreatureType.ambient, BiomeGenBase.taiga);
        EntityRegistry.addSpawn(MyMooseBig.class, 30, 1, 5, EnumCreatureType.ambient, BiomeGenBase.taigaHills);
        EntityRegistry.addSpawn(MyMooseBig.class, 30, 1, 5, EnumCreatureType.ambient, BiomeGenBase.megaTaiga);
        EntityRegistry.addSpawn(MyMooseBig.class, 30, 1, 5, EnumCreatureType.ambient, BiomeGenBase.megaTaigaHills);
        EntityRegistry.addSpawn(MyMooseBig.class, 30, 1, 5, EnumCreatureType.ambient, BiomeGenBase.iceMountains);
        EntityRegistry.addSpawn(MyMooseBig.class, 30, 1, 5, EnumCreatureType.ambient, BiomeGenBase.forestHills);
        EntityRegistry.addSpawn(MyMooseBig.class, 30, 1, 5, EnumCreatureType.ambient, BiomeGenBase.extremeHillsPlus);
        EntityRegistry.addSpawn(MyKingbowser.class, 2, 1, 1, EnumCreatureType.ambient, BiomeGenBase.hell);
        EntityRegistry.addSpawn(MyLeviathan.class, 5, 1, 15, EnumCreatureType.waterCreature, BiomeGenBase.ocean);
        EntityRegistry.addSpawn(MyLeviathan.class, 8, 2, 20, EnumCreatureType.waterCreature, BiomeGenBase.deepOcean);
        EntityRegistry.addSpawn(MyToughGuy.class, 8, 2, 4, EnumCreatureType.ambient, BiomeGenBase.plains);
        EntityRegistry.addSpawn(MyToughGuy.class, 6, 1, 2, EnumCreatureType.ambient, BiomeGenBase.forest);
        EntityRegistry.addSpawn(MyToughGuy.class, 6, 1, 2, EnumCreatureType.ambient, BiomeGenBase.forestHills);
        EntityRegistry.addSpawn(MyToughGuy.class, 6, 1, 2, EnumCreatureType.ambient, BiomeGenBase.birchForest);
        EntityRegistry.addSpawn(MyToughGuy.class, 6, 1, 2, EnumCreatureType.ambient, BiomeGenBase.birchForestHills);
        EntityRegistry.addSpawn(MyManticore.class, 2, 1, 1, EnumCreatureType.ambient, BiomeGenBase.forest);
        EntityRegistry.addSpawn(MyManticore.class, 4, 1, 1, EnumCreatureType.ambient, BiomeGenBase.jungle);
        EntityRegistry.addSpawn(MyManticore.class, 5, 1, 2, EnumCreatureType.ambient, BiomeGenBase.jungleHills);
        EntityRegistry.addSpawn(TwilightMob.class, 2, 1, 6, EnumCreatureType.ambient, BiomeGenBase.sky);
        EntityRegistry.addSpawn(TwilightMob.class, 3, 1, 6, EnumCreatureType.ambient, BiomeGenBase.mushroomIsland);
        EntityRegistry.addSpawn(TwilightMob.class, 3, 1, 6, EnumCreatureType.ambient, BiomeGenBase.mushroomIslandShore);
        EntityRegistry.addSpawn(TwilightMob.class, 5, 1, 6, EnumCreatureType.ambient, BiomeGenBase.plains);
        EntityRegistry.addSpawn(TwilightMob.class, 4, 1, 6, EnumCreatureType.ambient, BiomeGenBase.stoneBeach);
        EntityRegistry.addSpawn(TwilightMob.class, 1, 1, 6, EnumCreatureType.ambient, BiomeGenBase.mesa);
        EntityRegistry.addSpawn(MyTimberWolf.class, 20, 1, 3, EnumCreatureType.ambient, BiomeGenBase.forest);
        EntityRegistry.addSpawn(MyTimberWolf.class, 20, 1, 3, EnumCreatureType.ambient, BiomeGenBase.forestHills);
        EntityRegistry.addSpawn(MyTimberWolf.class, 20, 1, 3, EnumCreatureType.ambient, BiomeGenBase.birchForest);
        EntityRegistry.addSpawn(MyTimberWolf.class, 20, 1, 3, EnumCreatureType.ambient, BiomeGenBase.birchForestHills);
        EntityRegistry.addSpawn(MyCockatrice.class, 4, 1, 1, EnumCreatureType.ambient, BiomeGenBase.forestHills);
        EntityRegistry.addSpawn(MyCockatrice.class, 4, 1, 1, EnumCreatureType.ambient, BiomeGenBase.jungle);
        EntityRegistry.addSpawn(MyCockatrice.class, 4, 1, 1, EnumCreatureType.ambient, BiomeGenBase.jungleHills);
        EntityRegistry.addSpawn(MyCockatrice.class, 4, 1, 1, EnumCreatureType.ambient, BiomeGenBase.forest);
        EntityRegistry.addSpawn(MyCockatrice.class, 5, 2, 2, EnumCreatureType.ambient, BiomeGenBase.birchForest);
        EntityRegistry.addSpawn(MyCockatrice.class, 5, 1, 1, EnumCreatureType.ambient, BiomeGenBase.birchForestHills);
        EntityRegistry.addSpawn(MyTimberWolf.class, 20, 1, 4, EnumCreatureType.ambient, BiomeGenBase.taiga);
        EntityRegistry.addSpawn(MyTimberWolf.class, 20, 1, 3, EnumCreatureType.ambient, BiomeGenBase.taigaHills);
        EntityRegistry.addSpawn(MyParasprite.class, 40, 4, 40, EnumCreatureType.creature, BiomeGenBase.forest);
        EntityRegistry.addSpawn(MyParasprite.class, 40, 4, 40, EnumCreatureType.creature, BiomeGenBase.roofedForest);
        EntityRegistry.addSpawn(MyParasprite.class, 40, 4, 40, EnumCreatureType.creature, BiomeGenBase.forestHills);
        EntityRegistry.addSpawn(MyParasprite.class, 40, 4, 40, EnumCreatureType.creature, BiomeGenBase.birchForest);
        EntityRegistry
            .addSpawn(MyParasprite.class, 40, 4, 40, EnumCreatureType.creature, BiomeGenBase.birchForestHills);
        EntityRegistry.addSpawn(MyParasprite.class, 30, 3, 30, EnumCreatureType.creature, BiomeGenBase.savanna);
        EntityRegistry.addSpawn(MyParasprite.class, 20, 2, 20, EnumCreatureType.creature, BiomeGenBase.savannaPlateau);
        EntityRegistry.addSpawn(MyParasprite.class, 30, 1, 36, EnumCreatureType.creature, BiomeGenBase.plains);
        EntityRegistry.addSpawn(MyParasprite.class, 20, 1, 32, EnumCreatureType.creature, BiomeGenBase.taiga);
        EntityRegistry.addSpawn(MyParasprite.class, 20, 1, 32, EnumCreatureType.creature, BiomeGenBase.taigaHills);
        EntityRegistry.addSpawn(MyParasprite.class, 50, 4, 45, EnumCreatureType.creature, BiomeGenBase.jungle);
        EntityRegistry.addSpawn(MyParasprite.class, 50, 4, 45, EnumCreatureType.creature, BiomeGenBase.jungleHills);
        EntityRegistry.addSpawn(MyParasprite.class, 45, 4, 36, EnumCreatureType.creature, BiomeGenBase.swampland);
        EntityRegistry.addSpawn(MyCrabzilla.class, 1, 1, 1, EnumCreatureType.waterCreature, BiomeGenBase.ocean);
        EntityRegistry.addSpawn(MyCrabzilla.class, 1, 1, 1, EnumCreatureType.creature, BiomeGenBase.beach);
        EntityRegistry.addSpawn(MyCrabzilla.class, 1, 1, 1, EnumCreatureType.creature, BiomeGenBase.coldBeach);
        EntityRegistry.addSpawn(MyDashCloud.class, 6, 1, 2, EnumCreatureType.waterCreature, BiomeGenBase.ocean);
        EntityRegistry.addSpawn(MyDashCloud.class, 4, 1, 1, EnumCreatureType.creature, BiomeGenBase.beach);
        EntityRegistry.addSpawn(MyIronWill.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.forest);
        EntityRegistry.addSpawn(MyIronWill.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.extremeHills);
        EntityRegistry.addSpawn(MyIronWill.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.extremeHillsEdge);
        EntityRegistry.addSpawn(MyIronWill.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.mushroomIsland);
        EntityRegistry.addSpawn(MyIronWill.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.mushroomIslandShore);
        EntityRegistry.addSpawn(MyCrabzilla.class, 1, 1, 1, EnumCreatureType.creature, BiomeGenBase.mushroomIsland);
        EntityRegistry
            .addSpawn(MyCrabzilla.class, 1, 1, 1, EnumCreatureType.creature, BiomeGenBase.mushroomIslandShore);
        EntityRegistry.addSpawn(MySkull.class, 5, 1, 2, EnumCreatureType.ambient, BiomeGenBase.hell);
        EntityRegistry.addSpawn(MySkull.class, 5, 1, 2, EnumCreatureType.ambient, BiomeGenBase.stoneBeach);
        EntityRegistry.addSpawn(MySkullBoss.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.hell);
        EntityRegistry.addSpawn(MySkullBoss.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.stoneBeach);
        EntityRegistry.addSpawn(MyYakPrince.class, 1, 1, 1, EnumCreatureType.creature, BiomeGenBase.coldTaigaHills);
        EntityRegistry.addSpawn(MySpikezilla.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.plains);
        EntityRegistry.addSpawn(MySpikezilla.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.icePlains);
        EntityRegistry.addSpawn(MySpikezilla.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.coldTaiga);
        EntityRegistry.addSpawn(MySpikezilla.class, 1, 1, 1, EnumCreatureType.ambient, BiomeGenBase.coldTaigaHills);
        EntityRegistry.addSpawn(MyRhinoceros.class, 10, 1, 2, EnumCreatureType.creature, BiomeGenBase.savanna);
        EntityRegistry.addSpawn(MyRhinoceros.class, 5, 1, 2, EnumCreatureType.creature, BiomeGenBase.savannaPlateau);
        EntityRegistry.addSpawn(MyRhinoceros.class, 6, 1, 2, EnumCreatureType.creature, BiomeGenBase.mesa);
        EntityRegistry.addSpawn(MyRhinoceros.class, 5, 1, 1, EnumCreatureType.creature, BiomeGenBase.mesaPlateau_F);
        EntityRegistry.addSpawn(MyRhinoceros.class, 4, 1, 1, EnumCreatureType.creature, BiomeGenBase.mesaPlateau);
        EntityRegistry.addSpawn(MyRhinoceros.class, 10, 1, 1, EnumCreatureType.ambient, BiomeGenBase.savanna);
        EntityRegistry.addSpawn(MyRhinoceros.class, 5, 1, 2, EnumCreatureType.ambient, BiomeGenBase.savannaPlateau);
        EntityRegistry.addSpawn(MyRhinoceros.class, 6, 1, 2, EnumCreatureType.ambient, BiomeGenBase.mesa);
        EntityRegistry.addSpawn(MyRhinoceros.class, 5, 1, 1, EnumCreatureType.ambient, BiomeGenBase.mesaPlateau_F);
        EntityRegistry.addSpawn(MyRhinoceros.class, 4, 1, 1, EnumCreatureType.ambient, BiomeGenBase.mesaPlateau);
    }

    static {
        boneSword = (new BoneSword(6107, betterbone)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("bonesword")
            .setTextureName("KingBowserMod:bonesword");
        bonePick = (new BonePick(6109, betterbone)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("bonepick")
            .setTextureName("KingBowserMod:bonepick");
        boneHoe = (new BoneHoe(6110, betterbone)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("bonehoe")
            .setTextureName("KingBowserMod:bonehoe");
        boneAxe = (new BoneAxe(6111, betterbone)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("boneaxe")
            .setTextureName("KingBowserMod:boneaxe");
        boneSpade = (new BoneSpade(6112, betterbone)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("bonespade")
            .setTextureName("KingBowserMod:bonespade");
        twilicornSword = (new TwilicornSword(6113, twilight)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("twilicornsword")
            .setTextureName("KingBowserMod:twilightsword");
        twilicornPick = (new TwilicornPick(6114, twilight)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("twilicornpick")
            .setTextureName("KingBowserMod:twilightpick");
        twilicornHoe = (new TwilicornHoe(6115, twilight)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("twilicornhoe")
            .setTextureName("KingBowserMod:twilighthoe");
        twilicornAxe = (new TwilicornAxe(6116, twilight)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("twilicornaxe")
            .setTextureName("KingBowserMod:twilightaxe");
        twilicornSpade = (new TwilicornSpade(6117, twilight)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("twilicornspade")
            .setTextureName("KingBowserMod:twilightspade");
        yoshiSword = (new YoshiSword(6121, WOW)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("yoshiSword")
            .setTextureName("KingBowserMod:yoshisword");
        appleSword = (new AppleSword(6129, apple)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("appleSword")
            .setTextureName("KingBowserMod:applesword");
        applePick = (new ApplePick(6130, apple)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("applePick")
            .setTextureName("KingBowserMod:applepick");
        appleHoe = (new AppleHoe(6131, apple)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("appleHoe")
            .setTextureName("KingBowserMod:applehoe");
        appleAxe = (new AppleAxe(6132, apple)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("appleAxe")
            .setTextureName("KingBowserMod:appleaxe");
        appleSpade = (new AppleSpade(6133, apple)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("appleSpade")
            .setTextureName("KingBowserMod:applespade");
        dashSword = (new DashSword(6134, dash)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("dashSword")
            .setTextureName("KingBowserMod:dashsword");
        bearSword = (new BearSword(6135, bear)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("bearSword")
            .setTextureName("KingBowserMod:bearsword");
        bearPick = (new BearPick(6136, bear)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("bearPick")
            .setTextureName("KingBowserMod:bearpick");
        bearHoe = (new BearHoe(6137, bear)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("bearHoe")
            .setTextureName("KingBowserMod:bearhoe");
        bearAxe = (new BearAxe(6138, bear)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("bearAxe")
            .setTextureName("KingBowserMod:bearaxe");
        bearSpade = (new BearSpade(6139, bear)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("bearSpade")
            .setTextureName("KingBowserMod:bearspade");
        cragHammer = (new CragHammer(6140, crag)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("cragHammer")
            .setTextureName("KingBowserMod:craghammer");
        alicornSword = (new ItemAlicornSword(6141, alicorn)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("alicornSword")
            .setTextureName("KingBowserMod:alicornitem");
        dagger = (new ItemDagger(6142, random2)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("dagger")
            .setTextureName("KingBowserMod:dagger");
        digger = (new ItemDigger(6143, random2)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("digger")
            .setTextureName("KingBowserMod:digger");
        ajSword = (new AJSword(6144, applejack)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("ajSword")
            .setTextureName("KingBowserMod:ajsword");
        ajPick = (new AJPick(6145, applejack)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("ajPick")
            .setTextureName("KingBowserMod:ajpick");
        ajHoe = (new AJHoe(6146, applejack)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("ajHoe")
            .setTextureName("KingBowserMod:ajhoe");
        ajAxe = (new AJAxe(6147, applejack)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("ajAxe")
            .setTextureName("KingBowserMod:ajaxe");
        ajSpade = (new AJSpade(6148, applejack)).setCreativeTab(CreativeTabs.tabTools)
            .setUnlocalizedName("ajSpade")
            .setTextureName("KingBowserMod:ajspade");
        crystalSword = (new CrystalSword(6149, crystal)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("crystalSword")
            .setTextureName("KingBowserMod:crystalsword");
        ursaClaws = (new ItemGiantClaw(6150, ursa)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("ursaClaws")
            .setTextureName("KingBowserMod:beartooth");
        dirtSword = (new DirtSword(6151, dirt)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("dirtSword")
            .setTextureName("KingBowserMod:dirtsword");
        boneBlock = (new BoneBlock(1000)).setHardness(4.0F)
            .setStepSound(Block.soundTypeMetal)
            .setBlockName("boneBlock")
            .setCreativeTab(CreativeTabs.tabBlock)
            .setResistance(40.0F);
        boneOre = new BoneOre(1001);
        boneBrick = new BoneBrick(1002);
        glowBone = new GlowBone(1003);
        phoenixBlock = new PhoenixBlock(1004);
        boneWall = new BoneWall(1005);
        twilicornBlock = new TwilicornBlock(1006);
        robotBlock = new RobotBlock(1007);
        bearFurBlock = new BearFurBlock(1008);
        appleBlock = new AppleBlock(1009);
        arcticBlock = new ArcticBlock(1010);
        hardAppleBlock = new HardAppleBlock(1011);
        crystalOre = new CrystalOre(1012);
        crystalBlock = new CrystalBlock(1013);
        darkCrystalBlock = new DarkCrystalBlock(1014);
        boneItem = new BoneItem(5000);
        twilicornItem = new TwilicornItem(5001);
        lunaEclipse = new LunaEclipse(5002);
        bowserRod = new BowserRod(5003);
        phoenixBow = new PhoenixBow(5004);
        twilicornBow = new TwilicornBow(5019);
        bowserBone = new BowserBone(5005);
        bowserEye = new BowserEye(5006);
        friendShip = new FriendShip(5008);
        twilightStar = new TwilightStar(5020);
        lightningCloud = new LightningCloud(5022);
        diamondGems = new DiamondGems(5023);
        butterFlies = new ButterFlies(5024);
        apples = new Apples(5025);
        balloons = new Balloons(5026);
        dashMeat = new DashMeat(5027, 60, 0.5F, false);
        twiMeat = new TwiMeat(5028, 80, 0.6F, false);
        mavisOrb = (new MavisOrb(5029)).setTextureName("KingBowserMod:mavisorb");
        eggTwilightMob = new EggTwilightMob(5030);
        eggMyself = new EggMyself(5049);
        eggCroc = new EggCroc(5043);
        eggGiantChick = new EggGiantChick(5048);
        eggChubbyMoron = new EggChuddyMoron(5033);
        eggDemonSpider = new EggDemonSpider(5031);
        eggLeviathan = new EggLeviathan(5032);
        eggKingbowser = new EggKingbowser(5035);
        eggCentipede = new EggCentipede(5034);
        eggHydra = new EggHydra(5036);
        eggWindigo = new EggWindigo(5037);
        eggMoose = new EggMoose(5038);
        eggBuffalo = new EggBuffalo(5039);
        eggPowerOrb = new EggPowerOrb(5040);
        eggFlameDragon = new EggFlameDragon(5041);
        eggMooseBig = new EggMooseBig(5042);
        eggBear = new EggBear(5044);
        eggToughGuy = new EggToughGuy(5045);
        eggMavis = new EggMavis(5046);
        eggUrsaMAJOR = new EggUrsaMAJOR(5047);
        eggDash = new EggDash(5050);
        eggManticore = new EggManticore(5051);
        bearFur = new BearFur(5052);
        bearTooth = new BearTooth(5053);
        phoenixFeather = new PhoenixFeather(5054);
        cragScale = new CragScale(5055);
        eggRainbowCentipede = new EggRainbowCentipede(5056);
        twilicornBone = new TwilicornBone(5057);
        eggAj = new EggAJ(5058);
        eggScorpion = new EggScorpion(5059);
        darkCrystal = new DarkCrystal(5060);
        eggTimberWolf = new EggTimberWolf(5061);
        eggParasprite = new EggParasprite(5062);
        stinger = new Stinger(5063);
        eggTMagic = new EggTMagic(5064);
        eggCock = new EggCock(5065);
        unstableItem = new WindigoItem(5067);
        eggCrabzilla = new EggCrabzilla(5068);
        eggMinotaur = new EggMinotaur(5069);
        derpMuffin = new DerpMuffin(5070, 4, 0.2F, false);
        cupCakes = new Cupcakes(5071, 8, 0.4F, false);
        rainbow = new Rainbow(5072, 1, 0.02F, false);
        antenna = new Antenna(5073);
        hardApple = new HardApple(5074, 12, 1.5F, false);
        eggrdCloud = new EggRDCloud(5075);
        auroraGem = new AuroraGem(5076);
        eggTrevor = new EggTrevor(5077);
        eggSkullBoss = new EggSkullBoss(5078);
        eggSkull = new EggSkull(5079);
        yakHorn = new ItemYakHorn(5080);
        eggYakPrince = new EggYakPrince(5081);
        eggSpikey = new EggSpikey(5082);
        eggRhino = new EggRhino(5083);
        rhinoHorn = new ItemHorn(5084);
        entityId = 100;
        TwilightRand = new Random(151L);
        TwilightRand2 = new Random(2L);
        Snap = 0;
        FishSize = 5;
        hydraspawned = 0;
        boneHelmet = (new BoneHelmet(5555, armorbetterbone, CommonProxy.addArmor("reinforced"), 0))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("boneHelmet");
        boneChest = (new BoneChest(armorbetterbone, CommonProxy.addArmor("reinforced"), 1))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("boneChest");
        boneLegs = (new BoneLegs(armorbetterbone, CommonProxy.addArmor("reinforced"), 2))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("boneLegs");
        boneBoots = (new BoneBoots(armorbetterbone, CommonProxy.addArmor("reinforced"), 3))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("boneBoots");
        twilicornHelmet = (new TwilicornHelmet(armortwilicorn, CommonProxy.addArmor("twilight"), 0))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("twilicornHelmet");
        twilicornChest = (new TwilicornChest(armortwilicorn, CommonProxy.addArmor("twilight"), 1))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("twilicornChest");
        twilicornLegs = (new TwilicornLegs(armortwilicorn, CommonProxy.addArmor("twilight"), 2))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("twilicornLegs");
        twilicornBoots = (new TwilicornBoots(armortwilicorn, CommonProxy.addArmor("twilight"), 3))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("twilicornBoots");
        darkHelmet = (new DarkHelmet(armordark, CommonProxy.addArmor("dark"), 0)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("darkHelmet");
        darkChest = (new DarkChest(armordark, CommonProxy.addArmor("dark"), 1)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("darkChest");
        darkLegs = (new DarkLegs(armordark, CommonProxy.addArmor("dark"), 2)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("darkLegs");
        darkBoots = (new DarkBoots(armordark, CommonProxy.addArmor("dark"), 3)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("darkBoots");
        appleHelmet = (new AppleHelmet(armorapple, CommonProxy.addArmor("apple2"), 0))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("appleHelmet");
        appleChest = (new AppleChest(armorapple, CommonProxy.addArmor("apple2"), 1))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("appleChest");
        appleLegs = (new AppleLegs(armorapple, CommonProxy.addArmor("apple2"), 2))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("appleLegs");
        appleBoots = (new AppleBoots(armorapple, CommonProxy.addArmor("apple2"), 3))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("appleBoots");
        bearHelmet = (new BearHelmet(armorbear, CommonProxy.addArmor("bear2"), 0))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("bearHelmet");
        bearChest = (new BearChest(armorbear, CommonProxy.addArmor("bear2"), 1)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("bearChest");
        bearLegs = (new BearLegs(armorbear, CommonProxy.addArmor("bear2"), 2)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("bearLegs");
        bearBoots = (new BearBoots(armorbear, CommonProxy.addArmor("bear2"), 3)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("bearBoots");
        ajHelmet = (new AJHelmet(armorapplejack, CommonProxy.addArmor("applejack2"), 0))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("ajHelmet");
        ajChest = (new AJChest(armorapplejack, CommonProxy.addArmor("applejack2"), 1))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("ajChest");
        ajLegs = (new AJLegs(armorapplejack, CommonProxy.addArmor("applejack2"), 2))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("ajLegs");
        ajBoots = (new AJBoots(armorapplejack, CommonProxy.addArmor("applejack2"), 3))
            .setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("ajBoots");
        rdHelmet = (new RDHelmet(armordash, CommonProxy.addArmor("dash2"), 0)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("rdHelmet");
        rdChest = (new RDChest(armordash, CommonProxy.addArmor("dash2"), 1)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("rdChest");
        rdLegs = (new RDLegs(armordash, CommonProxy.addArmor("dash2"), 2)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("rdLegs");
        rdBoots = (new RDBoots(armordash, CommonProxy.addArmor("dash2"), 3)).setCreativeTab(CreativeTabs.tabCombat)
            .setUnlocalizedName("rdBoots");
    }
}
