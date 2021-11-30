package xyz.tuxinal.silkyHands.mixin;

import org.apache.commons.lang3.ArrayUtils;
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
        // make sure the player has the required tag
        if (!player.getScoreboardTags().contains(ConfigParser.getTag())) {
            return;
        }
        if (!player.getMainHandStack().isEmpty()) {
            return;
        }
        // make sure the broken block is not ignored
        // this check gets ran twice (once when checking harvestability and once when
        // breaking block (BlockMixin))
        // ideally this should only be checked once
        if (ArrayUtils.contains(ConfigParser.getIgnoredBlocks(),
                Registry.BLOCK.getId(blockState.getBlock()).toString())) {
            return;
        }
        ci.setReturnValue(true);
    }
}
