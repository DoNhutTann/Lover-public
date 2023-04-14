package dev.blackhig.zhebushigudu.lover.features.modules.misc;

import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class NoHitBox extends Module
{
    private static NoHitBox INSTANCE;
    public Setting<Boolean> pickaxe;
    public Setting<Boolean> crystal;
    public Setting<Boolean> gapple;
    
    public NoHitBox() {
        super("NoHitBox", "NoHitBox.", Category.MISC, false, false, false);
        this.pickaxe = this.register(new Setting<>("Pickaxe", true));
        this.crystal = this.register(new Setting<>("Crystal", true));
        this.gapple = this.register(new Setting<>("Gapple", true));
        this.setInstance();
    }
    
    public static NoHitBox getINSTANCE() {
        if (NoHitBox.INSTANCE == null) {
            NoHitBox.INSTANCE = new NoHitBox();
        }
        return NoHitBox.INSTANCE;
    }
    
    private void setInstance() {
        NoHitBox.INSTANCE = this;
    }
    
    static {
        NoHitBox.INSTANCE = new NoHitBox();
    }
}
