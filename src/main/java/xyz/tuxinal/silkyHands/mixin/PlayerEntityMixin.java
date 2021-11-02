package xyz.tuxinal.silkyHands.mixin;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.registry.Registry;
import xyz.tuxinal.silkyHands.utils.ConfigParser;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "canHarvest(Lnet/minecraft/block/BlockState;)Z", at = @At("RETURN"), cancellable = true)
    private void injected(BlockState blockState, CallbackInfoReturnable<Boolean> ci) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        if (player.getScoreboardTags().contains(ConfigParser.getTag())) {
            if (player.getMainHandStack().isEmpty()) {
                if (!Arrays.stream(ConfigParser.getIgnoredBlocks())
                        .anyMatch(Registry.BLOCK.getId(blockState.getBlock()).toString()::equals)) {
                    ci.setReturnValue(true);
                }
            }
        }
    }
}
