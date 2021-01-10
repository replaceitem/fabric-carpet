package carpet.mixins;

import carpet.script.value.ScreenHandlerValue;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandler_scarpetMixin {
    @Shadow public ServerPlayerEntity player;
    @Inject(method = "onClickSlot", at = @At(value = "FIELD", target = "Lnet/minecraft/screen/ScreenHandler;syncId:I"), cancellable = true)
    public void restrictScreenHandler(ClickSlotC2SPacket packet, CallbackInfo ci) {
        if(this.player.currentScreenHandler.syncId == packet.getSyncId() && !this.player.currentScreenHandler.isNotRestricted(player)) {
            DefaultedList<ItemStack> defaultedList = DefaultedList.of();

            for (int i = 0; i < this.player.currentScreenHandler.slots.size(); ++i) {
                defaultedList.add(((Slot) this.player.currentScreenHandler.slots.get(i)).getStack());
            }

            this.player.onHandlerRegistered(this.player.currentScreenHandler, defaultedList);

            if(packet.getSlot() >= 0 && packet.getSlot() < this.player.currentScreenHandler.slots.size()) {
                ((ScreenHandler_scarpetMixin) this.player.currentScreenHandler).getListeners().forEach(screenHandlerListener -> {
                    if (screenHandlerListener instanceof ScreenHandlerValue.ScreenHandlerScarpetListener) {
                        ((ScreenHandlerValue.ScreenHandlerScarpetListener) screenHandlerListener).onSlotClick(this.player.currentScreenHandler, packet.getSlot(), this.player.currentScreenHandler.slots.get(packet.getSlot()).getStack(), packet.getActionType().toString());
                    }
                });
            }
            ci.cancel();
        } else {
            if(packet.getSlot() >= 0 && packet.getSlot() < this.player.currentScreenHandler.slots.size()) {
                ((ScreenHandler_scarpetMixin) this.player.currentScreenHandler).getListeners().forEach(screenHandlerListener -> {
                    if (screenHandlerListener instanceof ScreenHandlerValue.ScreenHandlerScarpetListener) {
                        ((ScreenHandlerValue.ScreenHandlerScarpetListener) screenHandlerListener).onSlotClick(this.player.currentScreenHandler, packet.getSlot(), packet.getStack(),packet.getActionType().toString());
                    }
                });
            }
        }
    }
}
