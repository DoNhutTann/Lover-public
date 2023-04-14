package dev.blackhig.zhebushigudu.lover.features.gui.components.items.buttons;

import dev.blackhig.zhebushigudu.lover.features.setting.Bind;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.blackhig.zhebushigudu.lover.features.gui.loverGui;
import dev.blackhig.zhebushigudu.lover.util.RenderUtil;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.ColorUtil;
import dev.blackhig.zhebushigudu.lover.features.modules.client.ClickGui;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;

public class BindButton
        extends Button {
    private final Setting setting;
    public boolean isListening;

    public BindButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int color = ColorUtil.toARGB(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 255);
        RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515) : (!this.isHovering(mouseX, mouseY) ? lover.colorManager.getColorWithAlpha(lover.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : lover.colorManager.getColorWithAlpha(lover.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())));
        if (this.isListening) {
            lover.textManager.drawStringWithShadow("Press a Key...", this.x + 2.3f, this.y - 1.7f - (float) loverGui.getClickGui().getTextOffset(), -1);
        } else {
            lover.textManager.drawStringWithShadow(this.setting.getName() + " " + ChatFormatting.GRAY + this.setting.getValue().toString().toUpperCase(), this.x + 2.3f, this.y - 1.7f - (float) loverGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (this.isListening) {
            Bind bind = new Bind(keyCode);
            if (bind.toString().equalsIgnoreCase("Escape")) {
                return;
            }
            if (bind.toString().equalsIgnoreCase("Delete")) {
                bind = new Bind(-1);
            }
            this.setting.setValue(bind);
            this.onMouseClick();
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }

    @Override
    public boolean getState() {
        return !this.isListening;
    }
}