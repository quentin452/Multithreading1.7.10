package fr.iamacat.optimizationsandtweaks.mixins.common.fantasticfish;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fantastic.FantasticIds;
import fantastic.entities.*;
import fantastic.entities.sharks.EntityBasicShark;
import fantastic.events.FantasticEvents;
import fantastic.proxies.CommonProxy;

@Mixin(FantasticEvents.class)
public class MixinFantasticEvents {

    @Shadow
    public static CommonProxy proxy = new CommonProxy();
    @Shadow
    Random rand = new Random();
    @Shadow
    private int texture = 0;

    @Unique
    boolean isBlockLoaded(World world, int x, int y, int z) {
        Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
        return chunk.isChunkLoaded;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public void onLivingSpawnEvent(LivingSpawnEvent event) {
        if (!isBlockLoaded(event.world, (int) event.entity.posX, (int) event.entity.posY, (int) event.entity.posZ)) {
            return;
        }
        if (event.entity instanceof EntityBasicShark && this.rand.nextInt(10) <= 8) {
            event.entity.setDead();
        }

        float renderSize = 0.9F;
        int waters = 0;

        for (int k = -10; k <= 10; ++k) {
            if (event.world.getBlock((int) event.entity.posX, (int) event.entity.posY, (int) event.entity.posZ + k)
                == Blocks.water) {
                ++waters;
            }
        }

        if (waters <= FantasticIds.tier1Depth) {
            if (this.rand.nextInt(101 - FantasticIds.smallSpawnRate) <= 1) {
                renderSize = 0.5F;
            } else {
                renderSize = 0.25F;
            }
        } else if (waters <= FantasticIds.tier2Depth) {
            if (this.rand.nextInt(101 - FantasticIds.mediumSpawnRate) <= 1) {
                renderSize = 0.8F;
            } else {
                renderSize = 0.5F;
            }
        } else if (waters <= FantasticIds.tier3Depth) {
            if (this.rand.nextInt(101 - FantasticIds.smallSpawnRate) <= 1) {
                renderSize = 0.5F;
            } else if (this.rand.nextInt(101 - FantasticIds.mediumSpawnRate) <= 1) {
                renderSize = 0.8F;
            } else if (this.rand.nextInt(101 - FantasticIds.bigSpawnRate) <= 1) {
                renderSize = 1.0F;
            } else {
                renderSize = 0.8F;
            }
        } else if (waters <= FantasticIds.tier4Depth) {
            if (this.rand.nextInt(101 - FantasticIds.mediumSpawnRate) <= 1) {
                renderSize = 0.8F;
            } else if (this.rand.nextInt(101 - FantasticIds.bigSpawnRate) <= 1) {
                renderSize = 1.0F;
            } else if (this.rand.nextInt(101 - FantasticIds.largeSpawnRate) <= 1) {
                renderSize = 1.3F;
            } else {
                renderSize = 0.8F;
            }
        } else if (waters > FantasticIds.tier5Depth) {
            if (this.rand.nextInt(101 - FantasticIds.mediumSpawnRate) <= 1) {
                renderSize = 0.8F;
            } else if (this.rand.nextInt(101 - FantasticIds.bigSpawnRate) <= 1) {
                renderSize = 1.0F;
            } else if (this.rand.nextInt(101 - FantasticIds.largeSpawnRate) <= 1) {
                renderSize = 1.3F;
            } else if (this.rand.nextInt(101 - FantasticIds.legendarySpawnRate) <= 1) {
                renderSize = 1.8F;
            } else {
                renderSize = 1.0F;
            }
        }

        if (event.entity instanceof EntityBasicFish) {
            EntityBasicFish Fish = (EntityBasicFish) event.entity;
            if (Fish.getRenderSize() == 0.9F) {
                Fish.setRenderSize(renderSize);
                if (Fish.getRenderSize() == 0.9F) {
                    Fish.setRenderSize(0.8F);
                }
            }
        }

        if (event.entity instanceof EntityCaveFish) {
            EntityCaveFish Fish = (EntityCaveFish) event.entity;
            if (Fish.getRenderSize() == 0.9F) {
                Fish.setRenderSize(renderSize);
                if (Fish.getRenderSize() == 0.9F) {
                    Fish.setRenderSize(0.8F);
                }
            }
        }

        if (event.entity instanceof EntitySalmon) {
            EntitySalmon Fish = (EntitySalmon) event.entity;
            if (Fish.getRenderSize() == 0.9F) {
                Fish.setRenderSize(renderSize);
                if (Fish.getRenderSize() == 0.9F) {
                    Fish.setRenderSize(0.8F);
                }
            }
        }

        if (event.entity instanceof EntityFeeder) {
            EntityFeeder Fish = (EntityFeeder) event.entity;
            if (Fish.getRenderSize() == 0.9F) {
                Fish.setRenderSize(renderSize);
                if (Fish.getRenderSize() == 0.9F) {
                    Fish.setRenderSize(0.8F);
                }
            }
        }

        if (event.entity instanceof EntityMossy) {
            EntityMossy Fish = (EntityMossy) event.entity;
            if (Fish.getRenderSize() == 0.9F) {
                Fish.setRenderSize(renderSize);
                if (Fish.getRenderSize() == 0.9F) {
                    Fish.setRenderSize(0.8F);
                }
            }
        }

        if (event.entity instanceof EntityTuna) {
            EntityTuna Fish = (EntityTuna) event.entity;
            if (Fish.getRenderSize() == 0.9F) {
                Fish.setRenderSize(renderSize);
                if (Fish.getRenderSize() == 0.9F) {
                    Fish.setRenderSize(0.8F);
                }
            }
        }

    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public void onSpawnEvent(EntityJoinWorldEvent event) {
        if (!isBlockLoaded(event.world, (int) event.entity.posX, (int) event.entity.posY, (int) event.entity.posZ)) {
            return;
        }
        float renderSize = 0.9F;
        if (event.entity instanceof EntityBasicFish) {
            EntityBasicFish Fish = (EntityBasicFish) event.entity;
            if (Fish.getHasNotSpawned()) {
                if (this.rand.nextInt(10) == 0) {
                    Fish.setRenderSize(1.8F);
                } else if (this.rand.nextInt(9) == 0) {
                    Fish.setRenderSize(1.3F);
                } else if (this.rand.nextInt(5) == 0) {
                    Fish.setRenderSize(1.0F);
                } else if (this.rand.nextInt(4) == 0) {
                    Fish.setRenderSize(0.8F);
                } else if (this.rand.nextInt(2) == 0) {
                    Fish.setRenderSize(0.5F);
                } else {
                    Fish.setRenderSize(0.25F);
                }

                BiomeGenBase spawnBiome = event.entity.worldObj.getBiomeGenForCoords((int) Fish.posX, (int) Fish.posZ);
                if (spawnBiome != BiomeGenBase.jungle && spawnBiome != BiomeGenBase.jungleHills
                    && spawnBiome != BiomeGenBase.mesa
                    && spawnBiome != BiomeGenBase.mesaPlateau
                    && spawnBiome != BiomeGenBase.mesaPlateau_F) {
                    if (spawnBiome == BiomeGenBase.swampland) {
                        if (this.rand.nextInt(11) == 0) {
                            this.texture = 0;
                        } else if (this.rand.nextInt(2) == 0) {
                            this.texture = 4;
                        } else {
                            this.texture = 1;
                        }
                    } else if (spawnBiome != BiomeGenBase.ocean && spawnBiome != BiomeGenBase.deepOcean) {
                        if (spawnBiome != BiomeGenBase.mushroomIsland
                            && spawnBiome != BiomeGenBase.mushroomIslandShore) {
                            if (this.rand.nextInt(2) == 0) {
                                this.texture = 4;
                            } else {
                                this.texture = 1;
                            }
                        } else if (this.rand.nextInt(11) == 0) {
                            this.texture = 5;
                        } else if (this.rand.nextInt(2) == 0) {
                            this.texture = 4;
                        } else {
                            this.texture = 1;
                        }
                    } else if (this.rand.nextInt(11) == 0) {
                        this.texture = 3;
                    } else if (this.rand.nextInt(2) == 0) {
                        this.texture = 4;
                    } else {
                        this.texture = 1;
                    }
                } else if (this.rand.nextInt(11) == 0) {
                    this.texture = 2;
                } else if (this.rand.nextInt(2) == 0) {
                    this.texture = 4;
                } else {
                    this.texture = 1;
                }

                Fish.setTexture(this.texture);
                Fish.setHasNotSpawned(false);
            }
        }

        if (event.entity instanceof EntityCaveFish) {
            EntityCaveFish CaveFish = (EntityCaveFish) event.entity;
            if (CaveFish.getHasNotSpawned()) {
                CaveFish.setRenderSize(renderSize);
                if (CaveFish.posY > 40.0 && event.world.getClosestPlayerToEntity(CaveFish, 5.0) == null) {
                    CaveFish.setDead();
                }
            }

            CaveFish.setHasNotSpawned(false);
        }

        if (event.entity instanceof EntitySalmon) {
            EntitySalmon Salmon = (EntitySalmon) event.entity;
            if (Salmon.getHasNotSpawned()) {
                Salmon.setRenderSize(renderSize);
            }

            Salmon.setHasNotSpawned(false);
        }

        if (event.entity instanceof EntityFeeder) {
            EntityFeeder Feeder = (EntityFeeder) event.entity;
            if (Feeder.getHasNotSpawned()) {
                Feeder.setRenderSize(renderSize);
            }

            Feeder.setHasNotSpawned(false);
        }

        if (event.entity instanceof EntityMossy) {
            EntityMossy Feeder = (EntityMossy) event.entity;
            if (Feeder.getHasNotSpawned()) {
                Feeder.setRenderSize(renderSize);
            }

            Feeder.setHasNotSpawned(false);
        }

        if (event.entity instanceof EntityTuna) {
            EntityTuna Feeder = (EntityTuna) event.entity;
            if (Feeder.getHasNotSpawned()) {
                Feeder.setRenderSize(renderSize);
            }

            Feeder.setHasNotSpawned(false);
        }

    }
}
