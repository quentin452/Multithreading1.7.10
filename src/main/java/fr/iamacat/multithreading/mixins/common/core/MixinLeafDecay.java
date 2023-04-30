/*
 * FalseTweaks
 * Copyright (C) 2022 FalsePattern
 * All Rights Reserved
 * The above copyright notice, this permission notice and the word "SNEED"
 * shall be included in all copies or substantial portions of the Software.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package fr.iamacat.multithreading.mixins.common.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.ChunkCoordinates;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.iamacat.multithreading.SharedThreadPool;
import fr.iamacat.multithreading.config.MultithreadingandtweaksMultithreadingConfig;

@Mixin(value = BlockLeavesBase.class, priority = 900)
public abstract class MixinLeafDecay {

    private BlockingQueue<ChunkCoordinates> decayQueue;

    private static final int BATCH_SIZE = MultithreadingandtweaksMultithreadingConfig.batchsize;;

    public abstract boolean func_147477_a(Block block, int x, int y, int z, boolean decay);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        SharedThreadPool.getExecutorService();
    }

    @Inject(method = "updateLeafDecay", at = @At("RETURN"))
    private void onUpdateEntities(WorldClient world, CallbackInfo ci) {
        if (!MultithreadingandtweaksMultithreadingConfig.enableMixinLeafDecay) {
            // Initialize decay queue if it doesn't exist
            if (decayQueue == null) {
                decayQueue = new LinkedBlockingQueue<>(1000);
            }

            // Add leaf blocks that need to decay to the queue
            for (int x = -64; x <= 64; x++) {
                for (int z = -64; z <= 64; z++) {
                    for (int y = 0; y <= 255; y++) {
                        ChunkCoordinates pos = new ChunkCoordinates(x, y, z);
                        if (world.getChunkProvider()
                            .chunkExists(pos.posX >> 4, pos.posZ >> 4)) {
                            Block block = world.getBlock(pos.posX, pos.posY, pos.posZ);
                            if (block instanceof BlockLeavesBase) {
                                decayQueue.offer(pos);
                            }
                        }
                    }
                }
            }

            // Process leaf blocks in batches using executor service
            int numThreads = MultithreadingandtweaksMultithreadingConfig.numberofcpus;
            ExecutorService executorService = SharedThreadPool.getExecutorService();
            while (!decayQueue.isEmpty()) {
                List<ChunkCoordinates> batch = new ArrayList<>();
                int batchSize = Math.max(Math.min(decayQueue.size(), BATCH_SIZE), 1);
                for (int i = 0; i < batchSize; i++) {
                    batch.add(decayQueue.poll());
                }
                if (!batch.isEmpty()) {
                    executorService.submit(new Runnable() {

                        @Override
                        public void run() {
                            for (ChunkCoordinates pos : batch) {}
                        }
                    });
                }
            }
            // Shutdown decay executor service after it is used
            executorService.shutdown();
        }
    }
}
