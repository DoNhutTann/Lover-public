package dev.blackhig.zhebushigudu.lover.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.blackhig.zhebushigudu.lover.features.modules.player.LiquidInteract;
import net.minecraft.block.properties.IProperty;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockLiquid;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.block.Block;

@Mixin({ BlockLiquid.class })
public class MixinBlockLiquid extends Block
{
    protected MixinBlockLiquid(final Material materialIn) {
        super(materialIn);
    }
    
    @Inject(method = { "canCollideCheck" }, at = { @At("HEAD") }, cancellable = true)
    public void canCollideCheckHook(final IBlockState blockState, final boolean hitIfLiquid, final CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue((Boolean) ((hitIfLiquid && (int)blockState.getValue((IProperty)BlockLiquid.LEVEL) == 0) || LiquidInteract.getInstance().isOn()));
    }
}
