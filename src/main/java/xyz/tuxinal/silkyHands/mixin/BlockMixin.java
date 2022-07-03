package xyz.tuxinal.silkyHands.mixin;

import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import xyz.tuxinal.silkyHands.utils.ConfigParser;

@Mixin(Block.class)
public class BlockMixin {
    // @formatter:off 
    @Inject(
        method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;"
        ),
        cancellable = true
    )
    // @formatter:on
    private static void injected(BlockState blockState, Level world, BlockPos blockPos, BlockEntity blockEntity,
            Entity entity, ItemStack stack, CallbackInfo ci) {
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player) entity;
        if (!player.getTags().contains(ConfigParser.getTag())) {
            return;
        }
        if (!player.getMainHandItem().isEmpty()) {
            return;
        }
        if (ArrayUtils.contains(ConfigParser.getIgnoredBlocks(),
                Registry.BLOCK.getKey(blockState.getBlock()).toString())) {
            return;
        }
        Block.popResource(world, blockPos, new ItemStack(blockState.getBlock()));
        ci.cancel();
    }
}
