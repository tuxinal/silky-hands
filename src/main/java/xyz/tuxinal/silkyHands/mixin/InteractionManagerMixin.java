package xyz.tuxinal.silkyHands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import xyz.tuxinal.silkyHands.utils.ConfigParser;

@Mixin(ServerPlayerInteractionManager.class)
public class InteractionManagerMixin {
    @Shadow
    private ServerPlayerEntity player;

    @ModifyVariable(method = "tryBreakBlock", at = @At("STORE"), ordinal = 1)
    private boolean redirect(boolean canHarvest) {
        if (player.getScoreboardTags().contains(ConfigParser.getTag())) {
            if (player.getMainHandStack().isEmpty()) {
                return true;
            }
        }
        return canHarvest;
    }
}
