package dev.xardas.nekorogenerationscpufix.mixin;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/**
 * Generations-Core starts one worker thread for compiling its models. In 1.2.8 the thread loops on
 * {@code taskQueue.isEmpty()} without ever waiting, so it keeps one CPU core at 100% for the whole session.
 * This replaces the loop with {@link BlockingQueue#take()}, which sleeps until a task arrives.
 */
@Mixin(targets = "generations.gg.generations.core.generationscore.common.util.TaskQueue", remap = false)
public abstract class TaskQueueMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("Nekoro Generations CPU Fix");

    @Shadow @Final private BlockingQueue<Runnable> taskQueue;

    @Unique private int failures;

    /**
     * @author Alolan Xardas
     * @reason The original busy-waits; this waits for the next task instead.
     */
    @Overwrite
    private void processTasks() {
        try {
            while (true) {
                Runnable task = taskQueue.take();
                try {
                    task.run();
                } catch (Exception e) {
                    // when the game closes, every model still waiting fails at once ("zip file closed");
                    // the full error is logged once, the rest only count
                    if (failures++ == 0) LOGGER.error("Generations-Core model task failed", e);
                    else LOGGER.debug("Generations-Core model task failed ({} so far): {}", failures, e.toString());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
