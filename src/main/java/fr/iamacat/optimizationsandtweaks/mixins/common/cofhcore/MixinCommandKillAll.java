package fr.iamacat.optimizationsandtweaks.mixins.common.cofhcore;

import cofh.CoFHCore;
import cofh.core.command.CommandHandler;
import cofh.core.command.CommandKillAll;
import cofh.core.command.ISubCommand;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Locale;

@Mixin(CommandKillAll.class)
public class MixinCommandKillAll {

    /**
     * @author quentin452
     * @reason Improve logging during typing /cofh killall in chat
     */
    @Overwrite(remap = false)
    public void handleCommand(ICommandSender var1, String[] var2) {
        int var3 = 0;
        TObjectIntHashMap var5 = new TObjectIntHashMap();
        String var6 = null;
        boolean var7 = false;
        if (var2.length > 1) {
            var6 = var2[1].toLowerCase(Locale.US);
            var7 = "*".equals(var6);
        }
        for (WorldServer var11 : CoFHCore.server.worldServers) {
            synchronized (var11) {
                List var13 = var11.loadedEntityList;
                int var14 = var13.size();
                while (var14-- > 0) {
                    Entity var15 = (Entity) var13.get(var14);
                    if (var15 != null && !(var15 instanceof EntityPlayer)) {
                        String var4 = EntityList.getEntityString(var15);
                        if (var6 != null || var7) {
                            if (var7 || var4 != null && var4.toLowerCase(Locale.US).contains(var6)) {
                                var5.adjustOrPutValue(var4, 1, 1);
                                ++var3;
                                var11.removeEntity(var15);
                                optimizationsAndTweaks$notifyKillInChat(var1, var15, var11);
                            }
                        } else if (var15 instanceof IMob) {
                            if (var4 == null) {
                                var4 = var15.getClass().getName();
                            }
                            var5.adjustOrPutValue(var4, 1, 1);
                            ++var3;
                            var11.removeEntity(var15);
                            optimizationsAndTweaks$notifyKillInChat(var1, var15, var11);
                        }
                    }
                }
            }
        }
        if (var3 > 0) {
            String var18 = "";

            for (TObjectIntIterator var20 = var5.iterator(); var20.hasNext(); var18 = var18 + "§c" + var20.value() + "§f" + "x" + "§e" + var20.key() + "§f" + ", ") {
                var20.advance();
            }
            var18 = var18.substring(0, var18.length() - 2);
            CommandHandler.logAdminCommand(var1, (ISubCommand) this, "info.cofh.command.killall.success" + (var6 != null ? "" : "Hostile"), var3, var18);
        } else {
            var1.addChatMessage(new ChatComponentTranslation("info.cofh.command.killall.no" + (var6 != null ? "Match" : "Hostile")));
        }
    }

    @Unique
    private void optimizationsAndTweaks$notifyKillInChat(ICommandSender sender, Entity entity, WorldServer world) {
        String dimensionName = world.provider.getDimensionName();
        String entityName = EntityList.getEntityString(entity);
        if (entityName == null) {
            entityName = entity.getClass().getSimpleName();
        }
        sender.addChatMessage(new ChatComponentTranslation("Killed entity: %s in dimension: %s", entityName, dimensionName));
    }
}
