package fr.iamacat.optimizationsandtweaks.asm;

import static com.falsepattern.lib.mixin.ITargetedMod.PredicateHelpers.*;

import java.util.function.Predicate;

import com.falsepattern.lib.mixin.ITargetedMod;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TargetedMod implements ITargetedMod {

    /*
     * CHICKENCHUNKS("ChickenChunks", false, startsWith("chickenchunks")),
     * MRTJPCORE("MrTJPCore", false, startsWith("mrtjpcore")),
     * CHUNK_PREGENERATOR("ChunkPregenerator", false, startsWith("chunk+pregen")),
     * THERMALEXPANSION("ThermalExpansion", false, startsWith("thermalexpansion")),
     * THERMALFOUNDATION("ThermalFoundation", false, startsWith("thermalfoundation")),
     * GREGTECH6("GregTech", false, startsWith("gregtech")),
     * MATTEROVERDRIVE("MatterOverdrive", false, startsWith("matteroverdrive")),
     * PROJECTE("ProjectE", false, startsWith("projecte")),
     * TC4TWEAKS("TC4Tweaks", false, startsWith("thaumcraft4tweaks")),
     * OPTIFINE("OptiFine", false, startsWith("optifine")),
     * MEKANISM("Mekanism", false, startsWith("mekanism")),
     * BOTANIA("Botania", false, startsWith("botania+").or(startsWith("botania-")).or(startsWith("botania "))),
     */
    MANAMETAL("manametalmod", false, startsWith("manametalmod")),
    SMOOTHFONT("SmoothFont", false, startsWith("SmoothFont")),
    EXTRAUTILS("ExtraUtilities", false, startsWith("extrautilities")),
    COLOREDIRON("Colored+Iron", false, startsWith("Colored+Iron")),
    PACKAGEDAUTO("PackagedAuto", false, startsWith("PackagedAuto")),
    RUNICDUNGEONS("RunicDungeons", false, startsWith("RunicDungeons")),
    ORESPIDERS("Ore+Spiders", false, startsWith("Ore+Spiders")),
    TRAINCRAFT("Traincraft", false, startsWith("Traincraft")),
    GRIM3212("Grim3212", false, startsWith("Grim3212")),
    MANTLE("Mantle", false, startsWith("Mantle")),

    MINENAUTICA("Minenautica", false, startsWith("Minenautica")),

    INSTRUMENTUS("Instrumentus", false, startsWith("Instrumentus")),
    FASTCRAFT("FastCraft", false, startsWith("fastcraft")),
    BETTERBURNING("BetterBurning", false, startsWith("BetterBurning")),
    FALSETWEAKS("Falsetweaks", false, startsWith("Falsetweaks")),
    DAVINCIVESSELS("archimedesshipsplus", false, startsWith("archimedesshipsplus")),
    MOVINGWORLD("movingworld", false, startsWith("movingworld")),
    INDUSTRIALCRAFT("industrialcraft", false, startsWith("industrialcraft")),

    HARDCOREWITHER("Hardcore Wither", false, startsWith("Hardcore Wither")),
    THEREALKETER("Real.Kether", false, startsWith("Real.Kether")),
    EASYBREEDING("easybreading", false, startsWith("easybreading")),
    PORTALGUN("PortalGun", false, startsWith("PortalGun")),
    CODECHICKENCORE("CodeChickenCore", false, startsWith("CodeChickenCore")),

    OPTIFINE("OptiFine", false, startsWith("Optifine")),
    NEI("NotEnoughItems", false, startsWith("nei")),
    ADVENTURERS_AMULETS("Adventurer's Amulets", false, startsWith("AdventurersAmulets")),
    JAS("JustAnotherSpawner", false, startsWith("JustAnotherSpawner")),
    GARDENSTUFF("GardenStuff", false, startsWith("GardenStuff")),

    DRAGONBLOCKC("DragonBlockC", false, startsWith("DragonBlockC")),
    ATUM2("Atum", false, startsWith("Atum")),

    UNTHAUMIC("Thaumcraft Minus Thaumcraft", false, startsWith("Thaumcraft Minus Thaumcraft")),

    THAUMCRAFT4("Thaumcraft", false, startsWith("thaumcraft")),
    GEMSNJEWELS("gemsnjewels", false, startsWith("gemsnjewels")),
    ETFUTURMREQUIEM("etfuturum", false, startsWith("etfuturum")),
    FAMILIARSAPI("FamiliarsAPI", false, startsWith("FamiliarsAPI")),
    ADVANCEDGENETICS("advancedgenetics", false, startsWith("advancedgenetics")),
    NETHERLICIOUS("netherlicious", false, startsWith("netherlicious")),
    CONFIGHELPER("confighelper", false, startsWith("confighelper")),
    FOSSILANDARCHEOLOGYREVIVAL("fossilsarcheology", false, startsWith("fossilsarcheology")),
    POTIONSHARDS("Potion Shard", false, startsWith("Potion Shard")),
    LAGGOOGLES("laggoggles", false, startsWith("laggoggles")),
    UNUNQUADIUM("Ununquadium", false, startsWith("Ununquadium")),

    LOTOFMOBS("Lot O Mobs", false, startsWith("lom")),
    ELIJAHSCHOCOLATEMOD("Elijah's Chocolate Mod", false, startsWith("Elijah's Chocolate Mod")),
    PAMSHARVESTCRAFT("Pam's HarvestCraft", false, contains("harvestcraft")),
    SHINCOLLE("Shinkeiseikan Collection", false, startsWith("shincolle")),
    COFHCORE("CoFHCore", false, startsWith("cofhcore")),
    MINEFACTORYRELOADED("MinefactoryReloaded", false, startsWith("minefactoryreloaded")),
    ESSENCEOFTHEGOD("Essence_of_the_Gods", false, startsWith("Essence_of_the_Gods")),
    ESSENCEOFTHEGODFORK("eotg", false, startsWith("eotg")),
    GROWTHCRAFT("growthcraft", false, startsWith("growthcraft")),
    DISEASECRAFT("DiseaseCraft", false, startsWith("DiseaseCraft")),
    ELDRITCHEMPIRE("eldritch-empire", false, startsWith("eldritch-empire")),
    FANTASTICFISH("Fantastic_Fish", false, startsWith("Fantastic_Fish")),
    MINEGICKA("minegicka", false, startsWith("minegicka")),
    FROZENLAND("Frozenland", false, startsWith("Frozenland")),
    MASTERCHEF("MasterChef", false, startsWith("MasterChef")),

    DISASTERCRAFT("disaster_craft", false, startsWith("disaster_craft")),
    AKATSUKI("Akatsuk", false, startsWith("Akatsuk")),
    INDUSTRIALUPGRADE("IndustrialUpgrade", false, startsWith("IndustrialUpgrade")),
    SMALLSTAIRS("Small Stairs", false, startsWith("Small Stairs")),
    XAEROWORLDMAP("XaerosWorldMap", false, startsWith("XaerosWorldMap")),
    XAEROMINIMAP("Xaeros_Minimap", false, startsWith("Xaeros_Minimap")),
    FARLANDERS("The Farlanders", false, startsWith("farlanders")),
    MYTHANDMONSTERS("mythandmonsters", false, startsWith("mythandmonsters")),

    SLIMECARNAGE("SlimeCarnage", false, startsWith("SlimeCarnage")),
    ATOMICSTYKERSBATTLETOWERS("AtomicStryker's Battletowers", false, startsWith("battletower")),
    SALTYMOD_EXPANDED("Salty Mod Expanded", false, startsWith("SaltMod")),
    BUILDCRAFT("buildcraft", false, startsWith("buildcraft")),
    BUILDCRAFTOILTWEAKS("BuildCraftOilTweak", false, startsWith("BuildCraftOilTweak")),
    NEWDUNGEONS("new_dungeons", false, startsWith("new_dungeons")),
    GOBLINS("goblins", false, startsWith("goblins")),
    REMOTEIO("RemoteIO", false, startsWith("RemoteIO")),
    BIRDSNEST("BirdsNests", false, startsWith("BirdsNests")),
    WEATHERCARPET("Weather", false, startsWith("Weather")),
    HAMSTERIFIC("hamsterrific", false, startsWith("hamsterrific")),
    THAUMICREVELEATION("ThaumicRevelations", false, startsWith("ThaumicRevelations")),
    ANGELICA("angelica", false, startsWith("angelica")),

    GANYSNETHER("Ganys_Nether", false, startsWith("Ganys_Nether")),
    TCONSTRUCT("TConstruct", false, startsWith("TConstruct")),
    ADVENTOFASCENSION("Advent of Ascension", false, startsWith("nevermine")),
    KINGDOMSOFTHEOVERWORLD("Kingdoms of the Overworld Mod", false, startsWith("Kingdoms of the Overworld")),
    MINERALOGY("Mineralogy", false, startsWith("mineralogy")),
    LORDOFTHERINGS("LOTRMod", false, startsWith("LOTRMod")),
    LORDOFTHERINGSFORK("LOTRModfork", false, startsWith("LOTRModfork")),
    LOTRIMPROVEMENTS("LOTR Improvements", false, startsWith("LOTR Improvements")),
    ADVANCEDWORLDSELECTION("WorldSelectionAdvanced", false, startsWith("WorldSelectionAdvanced")),
    RECURRENTCOMPLEX("RecurrentComplex", false, startsWith("RecurrentComplex")),
    MANKINI("Mankini", false, startsWith("Mankini")),
    EXPERIENCEORE("ExperienceOre", false, startsWith("ExperienceOre")),
    GADOMANCY("gadomancy", false, startsWith("gadomancy")),

    ETERNALFROST("eternalfrost", false, startsWith("eternalfrost")),
    LOOTPLUSPLUS("Loot++", false, startsWith("Loot++")),
    KITCHENCRAFT("KitchenCraft", false, startsWith("KitchenCraft")),

    ENDLESSIDS("endlessids", false, startsWith("endlessids")),
    MALCORE("MalCore", false, startsWith("MalCore")),
    MATMOS("matmos", false, startsWith("matmos")),
    SHIPEWRECK("Shipwrecks!", false, startsWith("shipwrecks")),
    STEAMCRAFT2("Steamcraft", false, startsWith("steamcraft2")),
    PNEUMATICRAFT("pneumaticCraft", false, startsWith("pneumaticCraft")),
    NOTENOUGHPETS("NotEnoughPets", false, startsWith("NotEnoughPets")),
    PRACTICALLOGISTICS("Practical-Logistics", false, startsWith("Practical")),
    SONARCORE("SonarCore", false, startsWith("SonarCore")),
    BUILDCRAFTOILTWEAK("BuildCraftOilTweak", false, startsWith("BuildCraftOilTweak")),
    CATWALK2OFFICIAL("catwalksof", false, startsWith("catwalks")),
    CATWALK2UNOFFICIAL("catwalksunof", false, matches("catwalks-1.7.10-2.1.4-GTNH")),
    BLOCKLINGS("Blocklings", false, startsWith("Blocklings")),
    FLAXBEARDSTEAMPOWER("FSP", false, startsWith("FSP")),
    SKINPORT("SkinPort", false, startsWith("SkinPort")),
    MINESTONES("minestones", false, startsWith("minestones")),
    BLENDTRONIC("blendtronic", false, startsWith("blendtronic")),
    WITCHERY("witchery-", false, startsWith("witchery-")),
    WITCHERYY("witchery", false, startsWith("witchery")),
    SYNC("Sync", false, startsWith("Sync")),
    ICHUNUTIL("iChunUtil", false, startsWith("iChunUtil")),

    JEWELRYCRAFT2("Jewelrycraft2", false, startsWith("Jewelrycraft2")),
    RAGDOLLCORPSE("ragdollCorpses", false, startsWith("ragdollCorpses")),
    OPIS("Opis", false, startsWith("Opis")),
    KORINBLUEBEDROCK("1-7-10-005-KoRIN", false, startsWith("1-7-10-005-KoRIN")),
    OBSGREENERY("obsgreenery", false, startsWith("obsgreenery")),
    SIGNPOST("signpost", false, startsWith("signpost")),
    MOWZIESMOBS("MowziesMobs", false, startsWith("MowziesMobs")),
    PPAPMOD("PPAP", false, startsWith("PPAP")),
    TOOMUCHTNT("Too-Much-TNT", false, startsWith("Too-Much-TNT")),
    DRAGONAPI("DragonAPI", false, startsWith("DragonAPI")),
    COROUTIL("coroutil", false, startsWith("coroutil")),
    ZOMBIEAWARENESS("zombieawareness", false, startsWith("zombieawareness")),
    ANIMALSPLUS("animalsPlus", false, startsWith("animalsPlus")),
    SGSTREASURE("Treasure", false, startsWith("Treasure")),
    ALFHEIM("Alfheim", false, startsWith("Alfheim")),
    AUTOMAGY("Automagy", false, startsWith("Automagy")),
    TUMBLEWEED("tumbleweed", false, startsWith("tumbleweed")),
    MYTICALCREATURES("MythicalC", false, startsWith("MythicalC")),
    APPLIEDENERGISTICS("appliedenergistics2", false, startsWith("appliedenergistics2")),
    XTRACRAFT("xtracraft", false, startsWith("xtracraft")),
    AETHER("aether", false, startsWith("aether")),;

    @Getter
    private final String modName;
    @Getter
    private final boolean loadInDevelopment;
    @Getter
    private final Predicate<String> condition;
}
