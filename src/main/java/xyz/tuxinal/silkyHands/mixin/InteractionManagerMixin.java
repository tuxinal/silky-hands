package xyz.tuxinal.silkyHands.mixin;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.registry.Registry;
import xyz.tuxinal.silkyHands.utils.ConfigParser;

@Mixin(ServerPlayerInteractionManager.class)
public class InteractionManagerMixin {
    @Redirect(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;canHarvest(Lnet/minecraft/block/BlockState;)Z"))
    private boolean redirect(ServerPlayerEntity player, BlockState blockState) {
        if (player.getScoreboardTags().contains(ConfigParser.getTag())) {
            if (player.getMainHandStack().isEmpty()) {
                if (!Arrays.stream(ConfigParser.getIgnoredBlocks())
                        .anyMatch(Registry.BLOCK.getId(blockState.getBlock()).toString()::equals)) {
                    return true;
                }
            }
        }
        return player.canHarvest(blockState);
    }
}
