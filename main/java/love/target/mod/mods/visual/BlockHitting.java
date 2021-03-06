package love.target.mod.mods.visual;

import love.target.mod.Mod;
import love.target.mod.ModManager;
import love.target.mod.value.values.ModeValue;
import love.target.mod.value.values.NumberValue;
import net.minecraft.potion.Potion;

import java.util.Locale;

/**
 * @author yalan : {Vanilla}
 * @author Maki(maybe) : {Exhibition,Jello,Slide,Circle}
 * @see net.minecraft.client.renderer.ItemRenderer -> doSwordBlock()
 */

public class BlockHitting extends Mod {
    public static final ModeValue mode = new ModeValue("Mode","Vanilla",new String[]{
            "Vanilla",
            "E4h1b1t10n",
            "Je110",
            "Slide",
            "Circle"
    });
    public static final NumberValue translatedX = new NumberValue("TranslatedX",0,-1,1,0.1);
    public static final NumberValue translatedY = new NumberValue("TranslatedY",0,-1,1,0.1);
    public static final NumberValue translatedZ = new NumberValue("TranslatedZ",0,-1,1,0.1);
    public static final NumberValue swingSpeed = new NumberValue("SwingSpeed",1,0.1,1,0.1);

    public BlockHitting() {
        super("BlockHitting",Category.VISUAL);
        addValues(mode,translatedX,translatedY,translatedZ,swingSpeed);
    }
}
