package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.ai.attributes.*;
import net.minecraft.server.management.LowerStringMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.google.common.collect.Sets;

@Mixin(BaseAttributeMap.class)
public class MixinServersideAttributeMap {

    @Unique
    private BaseAttributeMap baseAttributeMap;
    @Unique
    private final Set attributeInstanceSet = Sets.newHashSet();
    @Shadow
    protected final Map attributesByName = new LowerStringMap();
    @Unique
    protected final Map attributes = new HashMap();

    public MixinServersideAttributeMap(BaseAttributeMap baseAttributeMap) {
        this.baseAttributeMap = baseAttributeMap;
    }

    /**
     * @reason
     */
    @Overwrite
    public IAttributeInstance registerAttribute(IAttribute attribute) {
        if (attributesByName.containsKey(attribute.getAttributeUnlocalizedName())) {
            throw new IllegalArgumentException("Attribute is already registered!");
        } else {
            ModifiableAttributeInstance attributeInstance = new ModifiableAttributeInstance(
                baseAttributeMap,
                attribute);
            attributesByName.put(attribute.getAttributeUnlocalizedName(), attributeInstance);

            if (attribute instanceof RangedAttribute) {
                RangedAttribute rangedAttribute = (RangedAttribute) attribute;
                String description = rangedAttribute.getDescription();
                if (description != null) {
                    attributes.put(description, attributeInstance);
                }
            }

            attributes.put(attribute, attributeInstance);
            attributeInstanceSet.add(attributeInstance);
            return attributeInstance;
        }
    }
}
