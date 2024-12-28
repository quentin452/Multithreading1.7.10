package fr.iamacat.optimizationsandtweaks.asm;

import static com.falsepattern.lib.mixin.ITargetedMod.PredicateHelpers.*;

import java.util.function.Predicate;

import com.falsepattern.lib.mixin.ITargetedMod;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TargetedMod implements ITargetedMod {
    MOVINGWORLD("movingworld", false, startsWith("movingworld")), // DO NOT REMOVE , EVEN IF THE IDE SAY "no usage" , if you remove it a crash ClassNotFoundException can appear when launching with moving worl mod
    ESSENCEOFTHEGOD("Essence_of_the_Gods", false, startsWith("Essence_of_the_Gods")),// DO NOT REMOVE , EVEN IF THE IDE SAY "no usage" , if you remove it a crash ClassNotFoundException can appear when launching with moving worl mod
    ESSENCEOFTHEGODFORK("eotg", false, startsWith("eotg")),// DO NOT REMOVE , EVEN IF THE IDE SAY "no usage" , if you remove it a crash ClassNotFoundException can appear when launching with moving worl mod
    WITCHERY("witchery-", false, startsWith("witchery-")),// DO NOT REMOVE , EVEN IF THE IDE SAY "no usage" , if you remove it a crash ClassNotFoundException can appear when launching with moving worl mod
    WITCHERYY("witchery", false, startsWith("witchery")),// DO NOT REMOVE , EVEN IF THE IDE SAY "no usage" , if you remove it a crash ClassNotFoundException can appear when launching with moving worl mod
    ZOMBIEAWARENESS("zombieawareness", false, startsWith("zombieawareness")),// DO NOT REMOVE , EVEN IF THE IDE SAY "no usage" , if you remove it a crash ClassNotFoundException can appear when launching with moving worl mod
    GANYSNETHER("Ganys_Nether", false, startsWith("Ganys_Nether")),// DO NOT REMOVE , EVEN IF THE IDE SAY "no usage" , if you remove it a crash ClassNotFoundException can appear when launching with moving worl mod
    LOTRIMPROVEMENTS("LOTR Improvements", false, startsWith("LOTR Improvements")),// DO NOT REMOVE , EVEN IF THE IDE SAY "no usage" , if you remove it a crash ClassNotFoundException can appear when launching with moving worl mod
    BUILDCRAFTOILTWEAK("BuildCraftOilTweak", false, startsWith("BuildCraftOilTweak")),// DO NOT REMOVE , EVEN IF THE IDE SAY "no usage" , if you remove it a crash ClassNotFoundException can appear when launching with moving worl mod
    MYTICALCREATURES("MythicalC", false, startsWith("MythicalC")),// DO NOT REMOVE , EVEN IF THE IDE SAY "no usage" , if you remove it a crash ClassNotFoundException can appear when launching with moving worl mod

    SMOOTHFONT("SmoothFont", false, startsWith("SmoothFont")),
    EXTRAUTILS("ExtraUtilities", false, startsWith("extrautilities")),
    COLOREDIRON("Colored+Iron", false, startsWith("Colored+Iron")),
    PACKAGEDAUTO("PackagedAuto", false, startsWith("PackagedAuto")),
    RUNICDUNGEONS("RunicDungeons", false, startsWith("RunicDungeons")),
    ORESPIDERS("Ore+Spiders", false, startsWith("Ore+Spiders")),
    TRAINCRAFT("Traincraft", false, startsWith("Traincraft")),
    GRIM3212("Grim3212", false, startsWith("Grim3212")),

    MINENAUTICA("Minenautica", false, startsWith("Minenautica")),

    INSTRUMENTUS("Instrumentus", false, startsWith("Instrumentus")),
    FASTCRAFT("FastCraft", false, startsWith("fastcraft")),
    BETTERBURNING("BetterBurning", false, startsWith("BetterBurning")),
    FALSETWEAKS("Falsetweaks", false, startsWith("Falsetweaks")),
    DAVINCIVESSELS("archimedesshipsplus", false, startsWith("archimedesshipsplus")),
    INDUSTRIALCRAFT("industrialcraft", false, startsWith("industrialcraft")),

    HARDCOREWITHER("Hardcore Wither", false, startsWith("Hardcore Wither")),
    THEREALKETER("Real.Kether", false, startsWith("Real.Kether")),
    EASYBREEDING("easybreading", false, startsWith("easybreading")),
    PORTALGUN("PortalGun", false, startsWith("PortalGun")),
    CODECHICKENCORE("CodeChickenCore", false, startsWith("CodeChickenCore")),

    OPTIFINE("OptiFine", false, startsWith("Optifine")),
    NEI("NotEnoughItems", false, startsWith("nei")),
    ADVENTURERS_AMULETS("Adventurer's Amulets", false, startsWith("AdventurersAmulets")),
    GARDENSTUFF("GardenStuff", false, startsWith("GardenStuff")),

    ATUM2("Atum", false, startsWith("Atum")),

    THAUMCRAFT4("Thaumcraft", false, startsWith("thaumcraft")),
    GEMSNJEWELS("gemsnjewels", false, startsWith("gemsnjewels")),
    ETFUTURMREQUIEM("etfuturum", false, startsWith("etfuturum")),
    FAMILIARSAPI("FamiliarsAPI", false, startsWith("FamiliarsAPI")),
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
    GROWTHCRAFT("growthcraft", false, startsWith("growthcraft")),
    DISEASECRAFT("DiseaseCraft", false, startsWith("DiseaseCraft")),
    ELDRITCHEMPIRE("eldritch-empire", false, startsWith("eldritch-empire")),
    FANTASTICFISH("Fantastic_Fish", false, startsWith("Fantastic_Fish")),
    MINEGICKA("minegicka", false, startsWith("minegicka")),
    MASTERCHEF("MasterChef", false, startsWith("MasterChef")),

    DISASTERCRAFT("disaster_craft", false, startsWith("disaster_craft")),
    AKATSUKI("Akatsuk", false, startsWith("Akatsuk")),
    INDUSTRIALUPGRADE("IndustrialUpgrade", false, startsWith("IndustrialUpgrade")),
    FARLANDERS("The Farlanders", false, startsWith("farlanders")),
    MYTHANDMONSTERS("mythandmonsters", false, startsWith("mythandmonsters")),

    SLIMECARNAGE("SlimeCarnage", false, startsWith("SlimeCarnage")),
    ATOMICSTYKERSBATTLETOWERS("AtomicStryker's Battletowers", false, startsWith("battletower")),
    BUILDCRAFT("buildcraft", false, startsWith("buildcraft")),
    BUILDCRAFTOILTWEAKS("BuildCraftOilTweak", false, startsWith("BuildCraftOilTweak")),
    GOBLINS("goblins", false, startsWith("goblins")),
    REMOTEIO("RemoteIO", false, startsWith("RemoteIO")),
    BIRDSNEST("BirdsNests", false, startsWith("BirdsNests")),
    WEATHERCARPET("Weather", false, startsWith("Weather")),
    HAMSTERIFIC("hamsterrific", false, startsWith("hamsterrific")),
    THAUMICREVELEATION("ThaumicRevelations", false, startsWith("ThaumicRevelations")),
    ANGELICA("angelica", false, startsWith("angelica")),

    TCONSTRUCT("TConstruct", false, startsWith("TConstruct")),
    KINGDOMSOFTHEOVERWORLD("Kingdoms of the Overworld Mod", false, startsWith("Kingdoms of the Overworld")),
    LORDOFTHERINGS("LOTRMod", false, startsWith("LOTRMod")),
    LORDOFTHERINGSFORK("LOTRModfork", false, startsWith("LOTRModfork")),
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
    CATWALK2OFFICIAL("catwalksof", false, startsWith("catwalks")),
    CATWALK2UNOFFICIAL("catwalksunof", false, matches("catwalks-1.7.10-2.1.4-GTNH")),
    BLOCKLINGS("Blocklings", false, startsWith("Blocklings")),
    FLAXBEARDSTEAMPOWER("FSP", false, startsWith("FSP")),
    SKINPORT("SkinPort", false, startsWith("SkinPort")),
    MINESTONES("minestones", false, startsWith("minestones")),
    BLENDTRONIC("blendtronic", false, startsWith("blendtronic")),

    JEWELRYCRAFT2("Jewelrycraft2", false, startsWith("Jewelrycraft2")),
    RAGDOLLCORPSE("ragdollCorpses", false, startsWith("ragdollCorpses")),
    OPIS("Opis", false, startsWith("Opis")),
    KORINBLUEBEDROCK("1-7-10-005-KoRIN", false, startsWith("1-7-10-005-KoRIN")),
    OBSGREENERY("obsgreenery", false, startsWith("obsgreenery")),
    MOWZIESMOBS("MowziesMobs", false, startsWith("MowziesMobs")),
    PPAPMOD("PPAP", false, startsWith("PPAP")),
    TOOMUCHTNT("Too-Much-TNT", false, startsWith("Too-Much-TNT")),
    DRAGONAPI("DragonAPI", false, startsWith("DragonAPI")),
    COROUTIL("coroutil", false, startsWith("coroutil")),
    ANIMALSPLUS("animalsPlus", false, startsWith("animalsPlus")),
    SGSTREASURE("Treasure", false, startsWith("Treasure")),
    ALFHEIM("Alfheim", false, startsWith("Alfheim")),
    AUTOMAGY("Automagy", false, startsWith("Automagy")),
    XTRACRAFT("xtracraft", false, startsWith("xtracraft")),
    ENTITYCULLING("entityculling", false, startsWith("entityculling")),
    AETHER("aether", false, startsWith("aether")),;

    @Getter
    private final String modName;
    @Getter
    private final boolean loadInDevelopment;
    @Getter
    private final Predicate<String> condition;
}
