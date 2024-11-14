package fr.iamacat.optimizationsandtweaks.utilsformods.thaumcraft;

import static thaumcraft.common.lib.crafting.ThaumcraftCraftingManager.getObjectTags;

import java.util.*;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.*;
import thaumcraft.common.lib.utils.Utils;

public class ThaumcraftCraftingManager2 {

    public static Set optimizationsAndTweaks$history2 = new HashSet();

    public static AspectList optimizationsAndTweaks$getTags(ItemStack itemstack) {
        Item item;
        int meta;
        try {
            item = itemstack.getItem();
            meta = itemstack.getItemDamage();
        } catch (Exception e) {
            return null;
        }

        if (optimizationsAndTweaks$history2.contains(itemstack)) {
            return null;
        }

        AspectList tags;
        if (ThaumcraftApi.exists(item, meta)) {
            tags = getObjectTags(new ItemStack(item, 1, meta));
        } else {
            optimizationsAndTweaks$history2.add(itemstack);
            tags = optimizationsAndTweaks$generateTagsFromRecipes(item, meta == 32767 ? 0 : meta);
            optimizationsAndTweaks$history2.remove(itemstack);
            tags = capAspects(tags, 64);
            ThaumcraftApi.registerObjectTag(new ItemStack(item, 1, meta), tags);
        }
        return tags;
    }

    public static AspectList capAspects(AspectList sourcetags, int amount) {
        if (sourcetags == null) {
            return sourcetags;
        } else {
            AspectList out = new AspectList();
            Aspect[] arr$ = sourcetags.getAspects();
            for (Aspect aspect : arr$) {
                out.merge(aspect, Math.min(amount, sourcetags.getAmount(aspect)));
            }
            return out;
        }
    }

    public static AspectList optimizationsAndTweaks$generateTagsFromRecipes(Item item, int meta) {
        AspectList ret = null;
        int recipeType = 0;
        while (ret == null && recipeType < 4) {
            switch (recipeType) {
                case 0:
                    ret = optimizationsAndTweaks$generateTagsFromCrucibleRecipes(item, meta);
                    break;
                case 1:
                    ret = optimizationsAndTweaks$generateTagsFromArcaneRecipes(item, meta);
                    break;
                case 2:
                    ret = optimizationsAndTweaks$generateTagsFromInfusionRecipes(item, meta);
                    break;
                case 3:
                    ret = optimizationsAndTweaks$generateTagsFromCraftingRecipes(item, meta);
                    break;
                default:
                    break;
            }
            recipeType++;
        }
        return ret;
    }

    public static AspectList optimizationsAndTweaks$generateTagsFromCrucibleRecipes(Item item, int meta) {
        CrucibleRecipe cr = ThaumcraftApi.getCrucibleRecipe(new ItemStack(item, 1, meta));
        if (cr != null) {
            AspectList ot = cr.aspects.copy();
            int ss = cr.getRecipeOutput().stackSize;
            ItemStack cat = null;
            if (cr.catalyst instanceof ItemStack) {
                cat = (ItemStack) cr.catalyst;
            } else if (cr.catalyst instanceof ArrayList && !((ArrayList<?>) cr.catalyst).isEmpty()) {
                cat = (ItemStack) ((ArrayList<?>) cr.catalyst).get(0);
            }
            assert cat != null;
            AspectList ot2 = optimizationsAndTweaks$generateTags(cat.getItem(), cat.getItemDamage());
            AspectList out = new AspectList();
            Aspect[] arr$;
            int len$;
            int i$;
            Aspect as;
            if (ot2 != null && ot2.size() > 0) {
                arr$ = ot2.getAspects();
                len$ = arr$.length;

                for (i$ = 0; i$ < len$; ++i$) {
                    as = arr$[i$];
                    out.add(as, ot2.getAmount(as));
                }
            }
            arr$ = ot.getAspects();
            len$ = arr$.length;
            for (i$ = 0; i$ < len$; ++i$) {
                as = arr$[i$];
                int amt = (int) (Math.sqrt(ot.getAmount(as)) / ss);
                out.add(as, amt);
            }
            arr$ = out.getAspects();
            len$ = arr$.length;
            for (i$ = 0; i$ < len$; ++i$) {
                as = arr$[i$];
                if (out.getAmount(as) <= 0) {
                    out.remove(as);
                }
            }
            return out;
        } else {
            return null;
        }
    }

    public static AspectList optimizationsAndTweaks$generateTagsFromArcaneRecipes(Item item, int meta) {
        AspectList ret = null;
        List recipeList = ThaumcraftApi.getCraftingRecipes();
        label173: for (Object o : recipeList) {
            if (o instanceof IArcaneRecipe) {
                IArcaneRecipe recipe = (IArcaneRecipe) o;
                if (recipe.getRecipeOutput() != null) {
                    int idR = recipe.getRecipeOutput()
                        .getItemDamage() == 32767 ? 0
                            : recipe.getRecipeOutput()
                                .getItemDamage();
                    int idS = Math.max(meta, 0);
                    if (recipe.getRecipeOutput()
                        .getItem() == item && idR == idS) {
                        ArrayList<ItemStack> ingredients = new ArrayList<>();
                        new AspectList();
                        try {
                            int i;
                            ItemStack is;
                            if (o instanceof ShapedArcaneRecipe) {
                                int width = ((ShapedArcaneRecipe) o).width;
                                Object[] items = ((ShapedArcaneRecipe) o).getInput();
                                for (i = 0; i < width && i < 3; ++i) {
                                    for (int j = 0; j < i; ++j) {
                                        if (items[i + j * width] != null) {
                                            ItemStack it;
                                            if (items[i + j * width] instanceof ArrayList) {
                                                for (Object object : (ArrayList) items[i + j * width]) {
                                                    it = (ItemStack) object;
                                                    if (Utils.isEETransmutionItem(it.getItem())) {
                                                        continue label173;
                                                    }
                                                    AspectList obj = optimizationsAndTweaks$generateTags(
                                                        it.getItem(),
                                                        it.getItemDamage());
                                                    if (obj != null && obj.size() > 0) {
                                                        is = it.copy();
                                                        is.stackSize = 1;
                                                        ingredients.add(is);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                is = (ItemStack) items[i + j * width];
                                                if (Utils.isEETransmutionItem(is.getItem())) {
                                                    continue label173;
                                                }
                                                it = is.copy();
                                                it.stackSize = 1;
                                                ingredients.add(it);
                                            }
                                        }
                                    }
                                }
                            } else if (o instanceof ShapelessArcaneRecipe) {
                                ArrayList items = ((ShapelessArcaneRecipe) o).getInput();
                                for (i = 0; i < items.size() && i < 9; ++i) {
                                    if (items.get(i) != null) {
                                        ItemStack it;
                                        if (items.get(i) instanceof ArrayList) {

                                            for (Object object : (ArrayList) items.get(i)) {
                                                it = (ItemStack) object;
                                                if (Utils.isEETransmutionItem(it.getItem())) {
                                                    continue label173;
                                                }

                                                AspectList obj = optimizationsAndTweaks$generateTags(
                                                    it.getItem(),
                                                    it.getItemDamage());
                                                if (obj != null && obj.size() > 0) {
                                                    is = it.copy();
                                                    is.stackSize = 1;
                                                    ingredients.add(is);
                                                    break;
                                                }
                                            }
                                        } else {
                                            it = (ItemStack) items.get(i);
                                            if (Utils.isEETransmutionItem(it.getItem())) {
                                                continue label173;
                                            }
                                            it = it.copy();
                                            it.stackSize = 1;
                                            ingredients.add(it);
                                        }
                                    }
                                }
                            }
                            AspectList ph = optimizationsAndTweaks$getAspectsFromIngredients(
                                ingredients,
                                recipe.getRecipeOutput());
                            Aspect[] arr$;
                            int i$;
                            Aspect as;
                            if (recipe.getAspects() != null) {
                                arr$ = recipe.getAspects()
                                    .getAspects();
                                i = arr$.length;

                                for (i$ = 0; i$ < i; ++i$) {
                                    as = arr$[i$];
                                    ph.add(
                                        as,
                                        (int) (Math.sqrt(
                                            recipe.getAspects()
                                                .getAmount(as))
                                            / ((float) recipe.getRecipeOutput().stackSize)));
                                }
                            }
                            arr$ = ph.copy()
                                .getAspects();
                            i = arr$.length;

                            for (i$ = 0; i$ < i; ++i$) {
                                as = arr$[i$];
                                if (ph.getAmount(as) <= 0) {
                                    ph.remove(as);
                                }
                            }
                            ret = ph;
                        } catch (Exception var22) {
                            var22.printStackTrace();
                        }
                    }
                }
            }
        }
        return ret;
    }

    public static AspectList optimizationsAndTweaks$generateTags(Item item, int meta) {
        int tmeta = meta;
        try {
            tmeta = (!Objects.requireNonNull(new ItemStack(item, 1, meta).getItem())
                .isDamageable() && Objects.requireNonNull(new ItemStack(item, 1, meta).getItem())
                    .getHasSubtypes()) ? meta : 32767;
        } catch (Exception ignored) {}

        optimizationsAndTweaks$history2.add(item);
        optimizationsAndTweaks$history2.add(tmeta);

        if (ThaumcraftApi.exists(item, tmeta)) {
            return optimizationsAndTweaks$getTags(new ItemStack(item, 1, tmeta));
        } else if (optimizationsAndTweaks$history2.contains(item) && optimizationsAndTweaks$history2.contains(tmeta)) {
            return null;
        } else {
            AspectList ret = optimizationsAndTweaks$generateTagsFromRecipes(item, tmeta == 32767 ? 0 : meta);
            ret = capAspects(ret, 64);
            ThaumcraftApi.registerObjectTag(new ItemStack(item, 1, tmeta), ret);
            return ret;
        }
    }

    public static AspectList optimizationsAndTweaks$getAspectsFromIngredients(ArrayList<ItemStack> ingredients,
        ItemStack recipeOut) {
        AspectList out = new AspectList();
        AspectList mid = new AspectList();
        for (ItemStack is : ingredients) {
            AspectList obj = optimizationsAndTweaks$generateTags(is.getItem(), is.getItemDamage());
            if (Objects.requireNonNull(is.getItem())
                .getContainerItem() != null
                && is.getItem()
                    .getContainerItem() != is.getItem()) {
                AspectList objC = optimizationsAndTweaks$generateTags(
                    is.getItem()
                        .getContainerItem(),
                    32767);
                assert objC != null;
                Aspect[] containerAspects = objC.getAspects();
                for (Aspect as : containerAspects) {
                    out.reduce(as, objC.getAmount(as));
                }
            }
            if (obj != null) {
                Aspect[] objAspects = obj.getAspects();
                for (Aspect as : objAspects) {
                    if (as != null) {
                        mid.add(as, obj.getAmount(as));
                    }
                }
            }
        }
        Aspect[] midAspects = mid.getAspects();
        for (Aspect as : midAspects) {
            if (as != null) {
                out.add(as, (int) (mid.getAmount(as) * 0.75F / (float) recipeOut.stackSize));
            }
        }
        Aspect[] outAspects = out.getAspects();
        for (Aspect as : outAspects) {
            if (out.getAmount(as) <= 0) {
                out.remove(as);
            }
        }
        return out;
    }

    public static AspectList optimizationsAndTweaks$generateTagsFromInfusionRecipes(Item item, int meta) {
        InfusionRecipe cr = ThaumcraftApi.getInfusionRecipe(new ItemStack(item, 1, meta));
        if (cr == null) {
            return null;
        } else {
            AspectList ot = cr.getAspects()
                .copy();
            ArrayList<ItemStack> ingredients = new ArrayList<>();
            ItemStack is = cr.getRecipeInput()
                .copy();
            is.stackSize = 1;
            ingredients.add(is);

            ItemStack[] components = cr.getComponents();
            for (ItemStack cat : components) {
                ItemStack is2 = cat.copy();
                is2.stackSize = 1;
                ingredients.add(is2);
            }

            AspectList out = new AspectList();
            AspectList ot2 = optimizationsAndTweaks$getAspectsFromIngredients(
                ingredients,
                (ItemStack) cr.getRecipeOutput());

            Aspect[] aspectsFromOt2 = ot2.getAspects();
            for (Aspect as : aspectsFromOt2) {
                out.add(as, ot2.getAmount(as));
            }
            Aspect[] aspectsFromOt = ot.getAspects();
            for (Aspect as : aspectsFromOt) {
                int amt = (int) (Math.sqrt(ot.getAmount(as)) / (double) ((ItemStack) cr.getRecipeOutput()).stackSize);
                out.add(as, amt);
            }
            Aspect[] outAspects = out.getAspects();
            for (Aspect as : outAspects) {
                if (out.getAmount(as) <= 0) {
                    out.remove(as);
                }
            }
            return out;
        }
    }

    public static AspectList optimizationsAndTweaks$generateTagsFromCraftingRecipes(Item item, int meta) {
        AspectList ret = new AspectList();
        int minValue = Integer.MAX_VALUE;
        List<IRecipe> recipeList = Collections.unmodifiableList(
            new ArrayList<>(
                CraftingManager.getInstance()
                    .getRecipeList()));
        for (IRecipe object : recipeList) {
            if (optimizationsAndTweaks$isValidRecipe(object, item, meta)) {
                try {
                    AspectList ph = optimizationsAndTweaks$processRecipeAndGetAspects(object);
                    optimizationsAndTweaks$refineAndSetMinValueAspect(ph, minValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    private static boolean optimizationsAndTweaks$isValidRecipe(IRecipe recipe, Item item, int meta) {
        if (recipe == null || recipe.getRecipeOutput() == null
            || recipe.getRecipeOutput()
                .getItem() == null) {
            return false;
        }
        ItemStack outputStack = recipe.getRecipeOutput()
            .copy();
        ItemStack comparisonStack = new ItemStack(item, 1, meta);
        return ItemStack.areItemStacksEqual(outputStack, comparisonStack);
    }

    private static AspectList optimizationsAndTweaks$processRecipeAndGetAspects(IRecipe recipe) {
        AspectList aspectList = new AspectList();
        try {
            if (recipe instanceof ShapedRecipes) {
                optimizationsAndTweaks$processShapedRecipe((ShapedRecipes) recipe, aspectList);
            } else if (recipe instanceof ShapelessRecipes) {
                optimizationsAndTweaks$processShapelessRecipe((ShapelessRecipes) recipe, aspectList);
            } else if (recipe instanceof ShapedOreRecipe) {
                optimizationsAndTweaks$processShapedOreRecipe((ShapedOreRecipe) recipe, aspectList);
            } else if (recipe instanceof ShapelessOreRecipe) {
                optimizationsAndTweaks$processShapelessOreRecipe((ShapelessOreRecipe) recipe, aspectList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aspectList;
    }

    private static void optimizationsAndTweaks$processShapedRecipe(ShapedRecipes recipe, AspectList aspectList) {
        if (recipe == null || recipe.recipeItems == null || recipe.recipeItems.length == 0) {
            return;
        }
        int width = recipe.recipeWidth;
        ItemStack[] items = recipe.recipeItems;
        int calculatedWidth = Math.min(width, (int) Math.sqrt(items.length));
        for (int i = 0; i < calculatedWidth; i++) {
            for (int j = 0; j < calculatedWidth; j++) {
                int index = i + j * calculatedWidth;
                if (index >= 0 && index < items.length) {
                    ItemStack stack = items[index];
                    optimizationsAndTweaks$processItemStack(stack, aspectList);
                }
            }
        }
    }

    private static void optimizationsAndTweaks$processShapelessRecipe(ShapelessRecipes recipe, AspectList aspectList) {
        List<?> items = Collections.singletonList(recipe.recipeItems);
        for (Object item : items) {
            if (item instanceof ItemStack) {
                optimizationsAndTweaks$processItemStack((ItemStack) item, aspectList);
            } else if (item instanceof List<?>) {
                for (Object stack : (List<?>) item) {
                    if (stack instanceof ItemStack) {
                        optimizationsAndTweaks$processItemStack((ItemStack) stack, aspectList);
                    }
                }
            }
        }
    }

    private static void optimizationsAndTweaks$processShapedOreRecipe(ShapedOreRecipe recipe, AspectList aspectList) {
        Object input = recipe.getInput();
        if (input != null) {
            Object[] items = (Object[]) input;
            for (Object item : items) {
                if (item instanceof ItemStack) {
                    optimizationsAndTweaks$processItemStack((ItemStack) item, aspectList);
                } else if (item instanceof ArrayList) {
                    for (Object o : (ArrayList<?>) item) {
                        if (o instanceof ItemStack) {
                            optimizationsAndTweaks$processItemStack((ItemStack) o, aspectList);
                        }
                    }
                }
            }
        }
    }

    private static void optimizationsAndTweaks$processShapelessOreRecipe(ShapelessOreRecipe recipe,
        AspectList aspectList) {
        List<?> items = Collections.singletonList(recipe.getInput());
        for (Object item : items) {
            if (item instanceof ItemStack) {
                optimizationsAndTweaks$processItemStack((ItemStack) item, aspectList);
            } else if (item instanceof ArrayList) {
                for (Object o : (ArrayList<?>) item) {
                    if (o instanceof ItemStack) {
                        optimizationsAndTweaks$processItemStack((ItemStack) o, aspectList);
                    }
                }
            }
        }
    }

    private static void optimizationsAndTweaks$refineAndSetMinValueAspect(AspectList ph, int minValue) {
        if (ph != null && ph.visSize() > 0) {
            List<Aspect> aspectsToRemove = new ArrayList<>();
            for (Aspect aspect : ph.getAspects()) {
                if (ph.getAmount(aspect) <= 0) {
                    aspectsToRemove.add(aspect);
                }
            }
            for (Aspect aspect : aspectsToRemove) {
                ph.remove(aspect);
            }
            if (ph.visSize() < minValue) {
                ph.copy();
                ph.visSize();
            }
        }
    }

    private static void optimizationsAndTweaks$processItemStack(ItemStack stack, AspectList aspectList) {
        if (stack == null || stack.getItem() == null) {
            return;
        }
        if (!Utils.isEETransmutionItem(stack.getItem())) {
            AspectList aspects = optimizationsAndTweaks$getTags(stack);
            if (aspects != null && aspects.size() > 0) {
                ItemStack clonedStack = stack.copy();

                if (clonedStack.stackSize != 1) {
                    clonedStack.stackSize = 1;
                }
                aspectList.merge(aspects);
            }
        }
    }
}
