package xyz.tuxinal.silkyHands.mixin;

import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import xyz.tuxinal.silkyHands.utils.ConfigParser;

@Mixin(Block.class)
public class BlockMixin {
    @Redirect(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;"))
    private static List<ItemStack> inject(BlockState blockState, ServerLevel serverLevel,
            BlockPos blockPos,
            @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack itemStack) {
        var originalDrops = Block.getDrops(blockState, serverLevel, blockPos, blockEntity, entity,
                itemStack);
        if (!(entity instanceof Player)) {
            return originalDrops;
        }
        Player player = (Player) entity;
        if (!player.getTags().contains(ConfigParser.getTag())) {
            return originalDrops;
        }
        if (!player.getMainHandItem().isEmpty()) {
            return originalDrops;
        }
        if (ArrayUtils.contains(ConfigParser.getIgnoredBlocks(),
                Registry.BLOCK.getKey(blockState.getBlock()).toString())) {
            return originalDrops;
        }
        if (originalDrops.stream().anyMatch(stack -> stack.getItem() == blockState.getBlock().asItem())) {
            return originalDrops;
        }

        // really hacky solution to multiblocks having dropping multiple times when
        // broken
        // as far as i can tell all vanilla multiblocks work fine with this
        var block = blockState.getBlock();
        var lootContext = block.getLootTable();
        var lootTable = serverLevel.getServer().getLootTables().get(lootContext);
        if (Stream.of(lootTable.pools[0].entries[0].conditions)
                .anyMatch(condition -> condition.getType() == LootItemConditions.BLOCK_STATE_PROPERTY)) {
            return originalDrops;
        }

        return List.of(new ItemStack(blockState.getBlock()));
    }
}
