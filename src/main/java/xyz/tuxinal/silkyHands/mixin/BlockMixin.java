package xyz.tuxinal.silkyHands.mixin;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import xyz.tuxinal.silkyHands.utils.ConfigParser;

@Mixin(Block.class)
public class BlockMixin {
    // @formatter:off 
    @Inject(
        method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V",
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/block/Block;getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;"
        ),
        cancellable = true
    )
    // @formatter:on
    private static void injected(BlockState blockState, World world, BlockPos blockPos, BlockEntity blockEntity,
            Entity entity, ItemStack stack, CallbackInfo ci) {
        if (!(entity instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) entity;
        if (!player.getScoreboardTags().contains(ConfigParser.getTag())) {
            return;
        }
        if (!player.getMainHandStack().isEmpty()) {
            return;
        }
        if (Arrays.stream(ConfigParser.getIgnoredBlocks())
                .anyMatch(Registry.BLOCK.getId(blockState.getBlock()).toString()::equals)) {
            return;
        }
        Block.dropStack(world, blockPos, new ItemStack(blockState.getBlock()));
        ci.cancel();
    }
}
