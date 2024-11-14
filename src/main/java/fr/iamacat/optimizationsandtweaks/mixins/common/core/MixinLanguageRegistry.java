package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringTranslate;

import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import fr.iamacat.optimizationsandtweaks.utils.trove.map.hash.THashMap;

@Mixin(LanguageRegistry.class)
public class MixinLanguageRegistry {

    @Shadow
    private static final LanguageRegistry INSTANCE = new LanguageRegistry();
    @Unique
    private THashMap<String, Properties> optimizationsAndTweaks$modLanguageData = new THashMap<>();
    @Shadow
    private static final Pattern assetENUSLang = Pattern.compile("assets/(.*)/lang/(?:.+/|)([\\w_-]+).lang");

    @Shadow
    public static LanguageRegistry instance() {
        return INSTANCE;
    }

    @Shadow
    public String getStringLocalization(String key) {
        return getStringLocalization(
            key,
            FMLCommonHandler.instance()
                .getCurrentLanguage());
    }

    @Overwrite(remap = false)
    public String getStringLocalization(String key, String lang) {
        String localizedString = "";
        Properties langPack = optimizationsAndTweaks$modLanguageData.get(lang);

        if (langPack != null) {
            if (langPack.getProperty(key) != null) {
                localizedString = langPack.getProperty(key);
            }
        }

        return localizedString;
    }

    /**
     * Deprecated for removal in 1.8. Use the assets lang system
     */
    @Overwrite(remap = false)
    @Deprecated
    public void addStringLocalization(String key, String value) {
        addStringLocalization(key, "en_US", value);
    }

    /**
     * Deprecated for removal in 1.8. Use the assets lang system
     */
    @Overwrite(remap = false)
    @Deprecated
    public void addStringLocalization(String key, String lang, String value) {
        Properties langPack = optimizationsAndTweaks$modLanguageData.get(lang);
        if (langPack == null) {
            langPack = new Properties();
            optimizationsAndTweaks$modLanguageData.put(lang, langPack);
        }
        langPack.put(key, value);
    }

    /**
     * Deprecated for removal in 1.8. Use the assets lang system
     */
    @Shadow
    @Deprecated
    public void addStringLocalization(Properties langPackAdditions) {
        addStringLocalization(langPackAdditions, "en_US");
    }

    /**
     * Deprecated for removal in 1.8. Use the assets lang system
     */
    @Overwrite(remap = false)

    @Deprecated
    public void addStringLocalization(Properties langPackAdditions, String lang) {
        Properties langPack = optimizationsAndTweaks$modLanguageData.get(lang);
        if (langPack == null) {
            langPack = new Properties();
            optimizationsAndTweaks$modLanguageData.put(lang, langPack);
        }
        if (langPackAdditions != null) {
            langPack.putAll(langPackAdditions);
        }
    }

    /**
     * Deprecated for removal in 1.8. Use the assets lang system
     */
    @Shadow
    @Deprecated
    public void addNameForObject(Object objectToName, String lang, String name) {
        String objectName;
        if (objectToName instanceof Item) {
            objectName = ((Item) objectToName).getUnlocalizedName();
        } else if (objectToName instanceof Block) {
            objectName = ((Block) objectToName).getUnlocalizedName();
        } else if (objectToName instanceof ItemStack) {
            objectName = Objects.requireNonNull(((ItemStack) objectToName).getItem())
                .getUnlocalizedName((ItemStack) objectToName);
        } else {
            throw new IllegalArgumentException(String.format("Illegal object for naming %s", objectToName));
        }
        objectName += ".name";
        addStringLocalization(objectName, lang, name);
    }

    /**
     * Deprecated for removal in 1.8. Use the assets lang system
     */
    @Shadow
    @Deprecated
    public static void addName(Object objectToName, String name) {
        instance().addNameForObject(objectToName, "en_US", name);
    }

    /**
     * Deprecated for removal in 1.8. Use the assets lang system
     */
    @Overwrite(remap = false)

    @SuppressWarnings("unchecked")
    @Deprecated
    public void mergeLanguageTable(@SuppressWarnings("rawtypes") Map field_135032_a, String lang) {
        Properties langPack = optimizationsAndTweaks$modLanguageData.get(lang);
        if (langPack != null) {
            mergeWithoutOverwrite(langPack, field_135032_a);
        }
        Properties usPack = optimizationsAndTweaks$modLanguageData.get("en_US");
        if (usPack != null) {
            mergeWithoutOverwrite(usPack, field_135032_a);
        }
    }

    @Shadow
    @Deprecated
    private <K, V> void mergeWithoutOverwrite(Map<? extends K, ? extends V> from, Map<K, V> to) {
        for (Map.Entry<? extends K, ? extends V> e : from.entrySet()) {
            if (!to.containsKey(e.getKey())) {
                to.put(e.getKey(), e.getValue());
            }
        }
    }

    /**
     * Deprecated for removal in 1.8. Use the assets lang system
     */
    @Shadow
    @Deprecated
    public void loadLocalization(String localizationFile, String lang, boolean isXML) {
        URL urlResource = this.getClass()
            .getResource(localizationFile);
        if (urlResource != null) {
            loadLocalization(urlResource, lang, isXML);
        } else {
            ModContainer activeModContainer = Loader.instance()
                .activeModContainer();
            if (activeModContainer != null) {
                FMLLog.log(
                    activeModContainer.getModId(),
                    Level.ERROR,
                    "The language resource %s cannot be located on the classpath. This is a programming error.",
                    localizationFile);
            } else {
                FMLLog.log(
                    Level.ERROR,
                    "The language resource %s cannot be located on the classpath. This is a programming error.",
                    localizationFile);
            }
        }
    }

    /**
     * Deprecated for removal in 1.8. Use the assets lang system
     */
    @Overwrite(remap = false)
    @Deprecated
    public void loadLocalization(URL localizationFile, String lang, boolean isXML) {

        Properties langPack = new Properties();
        try (InputStream langStream = localizationFile.openStream()) {

            if (isXML) {
                langPack.loadFromXML(langStream);
            } else {
                langPack.load(new InputStreamReader(langStream, StandardCharsets.UTF_8));
            }

            addStringLocalization(langPack, lang);
        } catch (IOException e) {
            FMLLog.log(Level.ERROR, e, "Unable to load localization from file %s", localizationFile);
        }
        // HUSH
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void injectLanguage(String language, HashMap<String, String> parsedLangFile) {

        Properties p = optimizationsAndTweaks$modLanguageData.get(language);
        if (p == null) {
            p = new Properties();
            optimizationsAndTweaks$modLanguageData.put(language, p);
        }
        p.putAll(parsedLangFile);
    }

    @Shadow
    public void loadLanguagesFor(ModContainer container, Side side) {
        File source = container.getSource();
        try {
            if (source.isDirectory()) {
                searchDirForLanguages(source, "", side);
            } else {
                searchZipForLanguages(source, side);
            }
        } catch (IOException ioe) {

        }
    }

    @Shadow
    private void searchZipForLanguages(File source, Side side) throws IOException {
        ZipFile zf = new ZipFile(source);
        List<String> added = Lists.newArrayList();
        for (ZipEntry ze : Collections.list(zf.entries())) {
            Matcher matcher = assetENUSLang.matcher(ze.getName());
            if (matcher.matches()) {
                String lang = matcher.group(2);
                // FMLLog.fine("Injecting found translation data for lang %s in zip file %s at %s into language system",
                // lang, source.getName(), ze.getName());
                added.add(lang);
                LanguageRegistry.instance()
                    .injectLanguage(lang, StringTranslate.parseLangFile(zf.getInputStream(ze)));
                // Ensure en_US is available to StringTranslate on the server
                if ("en_US".equals(lang) && side == Side.SERVER) {
                    StringTranslate.inject(zf.getInputStream(ze));
                }
            }
        }
        if (!added.isEmpty()) FMLLog.fine(
            "Found translations in %s [%s]",
            source.getName(),
            Joiner.on(", ")
                .join(added));
        zf.close();
    }

    @Overwrite(remap = false)
    private void searchDirForLanguages(File source, String path, Side side) throws IOException {
        for (File file : Objects.requireNonNull(source.listFiles())) {
            String currPath = path + file.getName();
            if (file.isDirectory()) {
                searchDirForLanguages(file, currPath + '/', side);
            }
            Matcher matcher = assetENUSLang.matcher(currPath);
            if (matcher.matches()) {
                String lang = matcher.group(2);
                FMLLog
                    .fine("Injecting found translation assets for lang %s at %s into language system", lang, currPath);
                LanguageRegistry.instance()
                    .injectLanguage(lang, StringTranslate.parseLangFile(Files.newInputStream(file.toPath())));
                // Ensure en_US is available to StringTranslate on the server
                if ("en_US".equals(lang) && side == Side.SERVER) {
                    StringTranslate.inject(new FileInputStream(file));
                }
            }
        }
    }
}
