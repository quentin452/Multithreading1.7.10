package fr.iamacat.multithreading.mixins.common.slimecarnage.classes;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import supremopete.SlimeCarnage.commom.SlimeCarnage;
import supremopete.SlimeCarnage.mobs.EntityDonatelloSlime;
import supremopete.SlimeCarnage.mobs.EntityLeonardoSlime;
import supremopete.SlimeCarnage.mobs.EntityMichelangeloSlime;
import supremopete.SlimeCarnage.mobs.EntityRaphaelSlime;

public class FixCascadingFromWorldGenSewers extends WorldGenerator {

    protected Block[] GetValidSpawnBlocks() {
        return new Block[] { Blocks.stone };
    }

    private boolean modifyLocationIsValidSpawn(World world, int i, int j, int k) {
        int distanceToAir = 0;

        for (Block checkID = world.getBlock(i, j, k); checkID
            != Blocks.air; checkID = world.getBlock(i, j + distanceToAir, k)) {
            ++distanceToAir;
        }

        if (distanceToAir > 3) {
            return false;
        } else {
            j += distanceToAir - 1;
            Block blockID = world.getBlock(i, j, k);
            Block blockIDAbove = world.getBlock(i, j + 1, k);
            Block blockIDBelow = world.getBlock(i, j - 1, k);
            Block[] var10 = this.GetValidSpawnBlocks();
            int var11 = var10.length;

            for (int var12 = 0; var12 < var11; ++var12) {
                Block x = var10[var12];
                if (blockIDAbove != Blocks.air) {
                    return false;
                }

                if (blockID == x) {
                    return true;
                }

                if (blockID == Blocks.stone_slab && blockIDBelow == x) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean modifyFunc76484a(World world, Random rand, int i, int j, int k) {
        if (this.modifyLocationIsValidSpawn(world, i, j, k) && this.modifyLocationIsValidSpawn(world, i + 8, j, k)
            && this.modifyLocationIsValidSpawn(world, i + 8, j, k + 4)
            && this.modifyLocationIsValidSpawn(world, i, j, k + 4)) {
            int j4;
            int j3;
            int i3;
            int k3;
            for (j4 = -15; j4 < 1; ++j4) {
                for (j3 = 0; j3 < 5; ++j3) {
                    for (i3 = 0; i3 < 5; ++i3) {
                        k3 = rand.nextInt(5);
                        if (k3 >= 0 || k3 <= 2) {
                            world.setBlock(i + j3, j + j4, k + i3, Blocks.water);
                        }

                        if (k3 == 3) {
                            world.notifyBlocksOfNeighborChange(i + j3, j + j4, k + i3, Blocks.water);
                        }

                        if (k3 == 4) {
                            world.notifyBlocksOfNeighborChange(i + j3, j + j4, k + i3, Blocks.water, 2);
                        }
                    }
                }
            }

            for (j4 = -12; j4 < 0; ++j4) {
                for (j3 = 1; j3 < 4; ++j3) {
                    for (i3 = 1; i3 < 4; ++i3) {
                        world.setBlock(i + j3, j + j4, k + i3, Blocks.air);
                    }
                }
            }

            for (j4 = -15; j4 < -8; ++j4) {
                for (j3 = 13; j3 < 25; ++j3) {
                    for (i3 = -4; i3 < 32; ++i3) {
                        k3 = rand.nextInt(5);
                        if (k3 >= 0 || k3 <= 2) {
                            world.setBlock(i + j3, j + j4, k + i3, Blocks.water);
                        }

                        if (k3 == 3) {
                            world.notifyBlocksOfNeighborChange(i + j3, j + j4, k + i3, Blocks.water, 1);
                        }

                        if (k3 == 4) {
                            world.notifyBlocksOfNeighborChange(i + j3, j + j4, k + i3, Blocks.water, 2);
                        }
                    }
                }
            }

            for (j4 = -15; j4 < -8; ++j4) {
                for (j3 = 0; j3 < 23; ++j3) {
                    for (i3 = -4; i3 < 9; ++i3) {
                        k3 = rand.nextInt(5);
                        if (k3 >= 0 || k3 <= 2) {
                            world.setBlock(i + j3, j + j4, k + i3, Blocks.water);
                        }

                        if (k3 == 3) {
                            world.notifyBlocksOfNeighborChange(i + j3, j + j4, k + i3, Blocks.water, 1);
                        }

                        if (k3 == 4) {
                            world.notifyBlocksOfNeighborChange(i + j3, j + j4, k + i3, Blocks.water, 2);
                        }
                    }
                }
            }

            world.setBlock(i + 0, j + 1, k + 2, Blocks.water);
            world.setBlock(i + 1, j + 0, k + 2, Blocks.air);
            world.setBlock(i + 1, j - 10, k + 1, Blocks.air);
            world.setBlock(i + 3, j - 10, k + 2, Blocks.air);
            world.setBlock(i + 1, j - 10, k + 3, Blocks.air);
            world.setBlock(i + 2, j - 10, k + 1, Blocks.air);
            world.setBlock(i + 2, j - 10, k + 2, Blocks.air);
            world.setBlock(i + 2, j - 10, k + 3, Blocks.air);
            world.setBlock(i + 3, j - 10, k + 1, Blocks.air);
            world.setBlock(i + 3, j - 10, k + 3, Blocks.air);
            world.setBlock(i + 1, j - 9, k + 1, Blocks.air);
            world.setBlock(i + 3, j - 9, k + 2, Blocks.air);
            world.setBlock(i + 1, j - 9, k + 3, Blocks.air);
            world.setBlock(i + 2, j - 9, k + 1, Blocks.air);
            world.setBlock(i + 2, j - 9, k + 2, Blocks.air);
            world.setBlock(i + 2, j - 9, k + 3, Blocks.air);
            world.setBlock(i + 3, j - 9, k + 1, Blocks.air);
            world.setBlock(i + 3, j - 9, k + 3, Blocks.air);
            world.setBlock(i + 2, j - 14, k - 0, Blocks.air);
            world.setBlock(i + 2, j - 14, k - 0, Blocks.clay);
            world.setBlock(i + 2, j - 14, k - 1, Blocks.air);
            world.setBlock(i + 2, j - 14, k - 1, Blocks.clay);
            world.setBlock(i + 2, j - 13, k - 2, Blocks.air);
            world.setBlock(i + 2, j - 13, k - 2, Blocks.clay);
            world.setBlock(i + 2, j - 13, k - 3, Blocks.air);
            world.setBlock(i + 2, j - 13, k - 3, Blocks.glowstone);
            world.setBlock(i + 2, j - 13, k - 4, Blocks.air);
            world.setBlock(i + 2, j - 13, k - 4, Blocks.iron_bars);
            TileEntityChest tileentitychest = new TileEntityChest();
            world.setTileEntity(i + 2, j - 13, k - 4, tileentitychest);

            for (j3 = 0; j3 < 6; ++j3) {
                ItemStack itemstack2 = this.pickCheckLootItem(rand);
                if (itemstack2 != null) {
                    tileentitychest
                        .setInventorySlotContents(rand.nextInt(tileentitychest.getSizeInventory()), itemstack2);
                }
            }

            world.setBlock(i + 19, j - 14, k - 0, Blocks.air);
            world.setBlock(i + 19, j - 14, k - 0, Blocks.clay);
            world.setBlock(i + 19, j - 14, k - 1, Blocks.air);
            world.setBlock(i + 19, j - 14, k - 1, Blocks.clay);
            world.setBlock(i + 19, j - 13, k - 2, Blocks.air);
            world.setBlock(i + 19, j - 13, k - 2, Blocks.clay);
            world.setBlock(i + 19, j - 13, k - 3, Blocks.air);
            world.setBlock(i + 19, j - 13, k - 3, Blocks.glowstone);
            world.setBlock(i + 19, j - 13, k - 4, Blocks.air);

            int j8;
            for (j3 = -15; j3 < -8; ++j3) {
                for (i3 = 8; i3 < 15; ++i3) {
                    for (k3 = -8; k3 < 0; ++k3) {
                        j8 = rand.nextInt(5);
                        if (j8 >= 0 || j8 <= 2) {
                            world.setBlock(i + i3, j + j3, k + k3, Blocks.water);
                        }

                        if (j8 == 3) {
                            world.notifyBlocksOfNeighborChange(i + i3, j + j3, k + k3, Blocks.water, 1);
                        }

                        if (j8 == 4) {
                            world.notifyBlocksOfNeighborChange(i + i3, j + j3, k + k3, Blocks.water, 2);
                        }
                    }
                }
            }

            for (j3 = -13; j3 < -10; ++j3) {
                for (i3 = 10; i3 < 13; ++i3) {
                    for (k3 = -5; k3 < -2; ++k3) {
                        world.setBlock(i + i3, j + j3, k + k3, Blocks.air);
                    }
                }
            }

            world.setBlock(i + 10, j - 11, k - 2, Blocks.glowstone);
            world.setBlock(i + 11, j - 11, k - 2, Blocks.glowstone);
            world.setBlock(i + 12, j - 11, k - 2, Blocks.glowstone);
            world.setBlock(i + 10, j - 12, k - 2, Blocks.glowstone);
            world.setBlock(i + 11, j - 12, k - 2, Blocks.glowstone);
            world.setBlock(i + 12, j - 12, k - 2, Blocks.glowstone);
            world.setBlock(i + 10, j - 13, k - 2, Blocks.glowstone);
            world.setBlock(i + 11, j - 13, k - 2, Blocks.glowstone);
            world.setBlock(i + 12, j - 13, k - 2, Blocks.glowstone);
            world.setBlock(i + 11, j - 14, k - 0, Blocks.air);
            world.setBlock(i + 11, j - 14, k - 0, Blocks.clay);
            world.setBlock(i + 11, j - 14, k - 1, Blocks.air);
            world.setBlock(i + 11, j - 14, k - 1, Blocks.clay);
            world.setBlock(i + 11, j - 14, k - 2, Blocks.air);
            world.setBlock(i + 11, j - 14, k - 2, Blocks.clay);
            world.setBlock(i + 11, j - 14, k - 3, Blocks.air);
            world.setBlock(i + 11, j - 14, k - 3, Blocks.clay);
            world.setBlock(i + 11, j - 14, k - 4, Blocks.air);
            world.setBlock(i + 11, j - 14, k - 4, Blocks.clay);
            world.setBlock(i + 11, j - 14, k - 5, Blocks.air);
            world.setBlock(i + 11, j - 14, k - 5, Blocks.clay);
            world.setBlock(i + 11, j - 12, k - 6, Blocks.air);
            world.setBlock(i + 11, j - 12, k - 6, Blocks.clay);
            world.setBlock(i + 11, j - 12, k - 7, Blocks.air);
            world.setBlock(i + 11, j - 12, k - 7, Blocks.glowstone);
            world.setBlock(i + 8, j - 12, k - 3, Blocks.tnt);
            TileEntityMobSpawner tileentitymobspawner1 = (TileEntityMobSpawner) world
                .getTileEntity(i + 8, j - 12, k - 3);
            tileentitymobspawner1.func_145881_a()
                .setEntityName("SlimeCarnage.FootSoldierSlime");
            world.setBlock(i + 14, j - 12, k - 3, Blocks.tnt);
            TileEntityMobSpawner tileentitymobspawner2 = (TileEntityMobSpawner) world
                .getTileEntity(i + 14, j - 12, k - 3);
            tileentitymobspawner2.func_145881_a()
                .setEntityName("SlimeCarnage.FootSoldierSlime");
            world.setBlock(i + 19, j - 13, k - 4, Blocks.iron_bars);
            TileEntityChest tileentitychest2 = new TileEntityChest();
            world.setTileEntity(i + 19, j - 13, k - 4, tileentitychest2);

            for (j8 = 0; j8 < 6; ++j8) {
                ItemStack itemstack2 = pickCheckLootItem(rand);
                if (itemstack2 != null) {
                    tileentitychest2
                        .setInventorySlotContents(rand.nextInt(tileentitychest.getSizeInventory()), itemstack2);
                }
            }

            int i8;
            for (j8 = 16; j8 < 23; ++j8) {
                for (i8 = 23; i8 < 30; ++i8) {
                    world.setBlock(i + j8, j - 10, k + i8, Blocks.air);
                }
            }

            int k8;
            int h;
            for (j8 = -20; j8 < -10; ++j8) {
                for (i8 = 21; i8 < 28; ++i8) {
                    for (k8 = -3; k8 < 8; ++k8) {
                        h = rand.nextInt(5);
                        if (h >= 0 || h <= 2) {
                            world.setBlock(i + i8, j + j8, k + k8, Blocks.water);
                        }

                        if (h == 3) {
                            world.notifyBlocksOfNeighborChange(i + i8, j + j8, k + k8, Blocks.water, 1);
                        }

                        if (h == 4) {
                            world.notifyBlocksOfNeighborChange(i + i8, j + j8, k + k8, Blocks.water, 2);
                        }
                    }
                }
            }

            for (j8 = -20; j8 < -13; ++j8) {
                for (i8 = 23; i8 < 46; ++i8) {
                    for (k8 = -4; k8 < 9; ++k8) {
                        h = rand.nextInt(5);
                        if (h >= 0 || h <= 2) {
                            world.setBlock(i + i8, j + j8, k + k8, Blocks.water);
                        }

                        if (h == 3) {
                            world.notifyBlocksOfNeighborChange(i + i8, j + j8, k + k8, Blocks.water, 1);
                        }

                        if (h == 4) {
                            world.notifyBlocksOfNeighborChange(i + i8, j + j8, k + k8, Blocks.water, 2);
                        }
                    }
                }
            }

            for (j8 = -14; j8 < -13; ++j8) {
                for (i8 = 2; i8 < 25; ++i8) {
                    for (k8 = 1; k8 < 4; ++k8) {
                        world.setBlock(i + i8, j + j8, k + k8, Blocks.air);
                        world.setBlock(i + i8, j + j8, k + k8, Blocks.clay);
                    }
                }
            }

            for (j8 = -14; j8 < -13; ++j8) {
                for (i8 = 18; i8 < 21; ++i8) {
                    for (k8 = 1; k8 < 21; ++k8) {
                        world.setBlock(i + i8, j + j8, k + k8, Blocks.air);
                        world.setBlock(i + i8, j + j8, k + k8, Blocks.clay);
                    }
                }
            }

            for (j8 = -19; j8 < -18; ++j8) {
                for (i8 = 23; i8 < 46; ++i8) {
                    for (k8 = 1; k8 < 4; ++k8) {
                        world.setBlock(i + i8, j + j8, k + k8, Blocks.air);
                        world.setBlock(i + i8, j + j8, k + k8, Blocks.clay);
                    }
                }
            }

            for (j8 = -13; j8 < -10; ++j8) {
                for (i8 = 1; i8 < 23; ++i8) {
                    for (k8 = -1; k8 < 6; ++k8) {
                        world.setBlock(i + i8, j + j8, k + k8, Blocks.air);
                    }
                }
            }

            for (j8 = -13; j8 < -10; ++j8) {
                for (i8 = 16; i8 < 23; ++i8) {
                    for (k8 = -1; k8 < 30; ++k8) {
                        world.setBlock(i + i8, j + j8, k + k8, Blocks.air);
                    }
                }
            }

            for (j8 = -18; j8 < -15; ++j8) {
                for (i8 = 23; i8 < 46; ++i8) {
                    for (k8 = -1; k8 < 6; ++k8) {
                        world.setBlock(i + i8, j + j8, k + k8, Blocks.air);
                    }
                }
            }

            for (j8 = -18; j8 < -10; ++j8) {
                for (i8 = 23; i8 < 26; ++i8) {
                    for (k8 = -1; k8 < 6; ++k8) {
                        world.setBlock(i + i8, j + j8, k + k8, Blocks.air);
                    }
                }
            }

            for (j8 = -13; j8 < 1; ++j8) {
                world.setBlock(i + 1, j + j8, k + 2, Blocks.coal_ore);
                world.updateLightByType(EnumSkyBlock.Block, i + 1, j + j8, k + 2);
            }

            world.setBlock(i, j + 1, k + 2, Blocks.water);
            world.notifyBlocksOfNeighborChange(i + 16, j - 13, k + 22, Blocks.water, 1);
            world.setBlock(i + 16, j - 12, k + 22, Blocks.water);
            world.setBlock(i + 16, j - 11, k + 22, Blocks.water);
            world.setBlock(i + 17, j - 13, k + 22, Blocks.water);
            world.setBlock(i + 17, j - 12, k + 22, Blocks.water);
            world.notifyBlocksOfNeighborChange(i + 17, j - 11, k + 22, Blocks.water, 1);
            world.setBlock(i + 18, j - 11, k + 22, Blocks.glowstone);
            world.setBlock(i + 18, j - 12, k + 22, Blocks.glowstone);
            world.setBlock(i + 18, j - 13, k + 22, Blocks.glowstone);
            world.setBlock(i + 19, j - 11, k + 22, Blocks.glowstone);
            world.setBlock(i + 19, j - 12, k + 22, Blocks.glowstone);
            world.setBlock(i + 19, j - 13, k + 22, Blocks.glowstone);
            world.setBlock(i + 20, j - 11, k + 22, Blocks.glowstone);
            world.setBlock(i + 20, j - 12, k + 22, Blocks.glowstone);
            world.setBlock(i + 20, j - 13, k + 22, Blocks.glowstone);
            world.setBlock(i + 21, j - 13, k + 22, Blocks.water);
            world.setBlock(i + 21, j - 12, k + 22, Blocks.water);
            world.notifyBlocksOfNeighborChange(i + 21, j - 11, k + 22, Blocks.water, 1);
            world.setBlock(i + 22, j - 13, k + 22, Blocks.water);
            world.setBlock(i + 22, j - 12, k + 22, Blocks.water);
            world.notifyBlocksOfNeighborChange(i + 22, j - 11, k + 22, Blocks.water, 2);
            if (!world.isRemote) {
                EntityLeonardoSlime leoslime = new EntityLeonardoSlime(world);
                leoslime.setLocationAndAngles((double) (i + 17), (double) (j - 12), (double) (k + 24), 0.0F, 0.0F);
                world.spawnEntityInWorld(leoslime);
                EntityDonatelloSlime donslime = new EntityDonatelloSlime(world);
                donslime.setLocationAndAngles((double) (i + 21), (double) (j - 12), (double) (k + 24), 0.0F, 0.0F);
                world.spawnEntityInWorld(donslime);
                EntityRaphaelSlime raphslime = new EntityRaphaelSlime(world);
                raphslime.setLocationAndAngles((double) (i + 17), (double) (j - 12), (double) (k + 26), 0.0F, 0.0F);
                world.spawnEntityInWorld(raphslime);
                EntityMichelangeloSlime micslime = new EntityMichelangeloSlime(world);
                micslime.setLocationAndAngles((double) (i + 21), (double) (j - 12), (double) (k + 26), 0.0F, 0.0F);
                world.spawnEntityInWorld(micslime);
            }

            return true;
        } else {
            return false;
        }
    }

    private static ItemStack pickCheckLootItem(Random random) {
        int i = random.nextInt(31);
        if (i == 0) {
            return new ItemStack(SlimeCarnage.PizzaSlice, random.nextInt(2) + 1);
        } else if (i == 1) {
            return new ItemStack(SlimeCarnage.Banana, random.nextInt(2) + 1);
        } else if (i == 2 && random.nextInt(200) == 0) {
            return null;
        } else if (i == 3) {
            return null;
        } else if (i == 4) {
            return new ItemStack(Items.string, 1);
        } else if (i == 5) {
            return new ItemStack(SlimeCarnage.LimeJam, random.nextInt(4) + 1);
        } else if (i == 6) {
            return new ItemStack(Items.iron_ingot, random.nextInt(20) + 1);
        } else if (i == 7 && random.nextInt(5) == 0) {
            return new ItemStack(Items.glass_bottle, 1);
        } else if (i == 8) {
            return new ItemStack(Items.coal, random.nextInt(4) + 1);
        } else if (i == 9 && random.nextInt(10) == 0) {
            return new ItemStack(Blocks.stone_button, 1);

        } else if (i == 10) {
            return new ItemStack(Blocks.planks, random.nextInt(4) + 1);
        } else if (i == 11 && random.nextInt(20) == 0) {
            return new ItemStack(SlimeCarnage.GreenGelBoots, 1);
        } else if (i == 12 && random.nextInt(20) == 0) {
            return new ItemStack(SlimeCarnage.GreenGelLeggings, 1);
        } else if (i == 13 && random.nextInt(20) == 0) {
            return new ItemStack(SlimeCarnage.GreenGelChestplate, 1);
        } else if (i == 14 && random.nextInt(20) == 0) {
            return new ItemStack(SlimeCarnage.GreenGelHelmet, 1);
        } else if (i == 15 && random.nextInt(3) == 0) {
            return new ItemStack(SlimeCarnage.BlueGel, random.nextInt(12) + 1);
        } else if (i == 16 && random.nextInt(3) == 0) {
            return new ItemStack(SlimeCarnage.RedGel, random.nextInt(12) + 1);
        } else if (i == 17 && random.nextInt(3) == 0) {
            return new ItemStack(SlimeCarnage.YellowGel, random.nextInt(12) + 1);
        } else if (i == 18 && random.nextInt(3) == 0) {
            return new ItemStack(SlimeCarnage.GreenGel, random.nextInt(12) + 1);
        } else if (i == 19) {
            return new ItemStack(SlimeCarnage.OrangeGel, random.nextInt(12) + 1);
        } else if (i == 20) {
            return new ItemStack(SlimeCarnage.ScrollField, 1);
        } else if (i == 21) {
            return new ItemStack(SlimeCarnage.ScrollChurch, 1);
        } else if (i == 22) {
            return new ItemStack(SlimeCarnage.ScrollWell, 1);
        } else if (i == 23) {
            return new ItemStack(SlimeCarnage.ScrollBlacksmith, 1);
        } else if (i == 24) {
            return new ItemStack(SlimeCarnage.ScrollHouse1, 1);
        } else if (i == 25) {
            return new ItemStack(SlimeCarnage.ScrollHouse2, 1);
        } else if (i == 26 && random.nextInt(10) == 0) {
            return new ItemStack(SlimeCarnage.ScrollHouse3, 1);
        } else if (i == 27 && random.nextInt(10) == 0) {
            return new ItemStack(SlimeCarnage.ScrollHouse4, 1);
        } else if (i == 28) {
            return new ItemStack(Items.gunpowder, 16);
        } else if (i == 29) {
            return new ItemStack(Items.carrot, 1);
        } else {
            return i == 30 ? new ItemStack(Items.wheat, 4) : null;
        }
    }

    @Override
    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
        return false;
    }
}
