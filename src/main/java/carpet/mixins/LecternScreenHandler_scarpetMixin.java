package carpet.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.LecternScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LecternScreenHandler.class)
public class LecternScreenHandler_scarpetMixin {
    @Inject(method = "onButtonClick", at = @At(value = "HEAD"), cancellable = true)
    private void restrictTakeBook(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir) {
        if(id == 3) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
