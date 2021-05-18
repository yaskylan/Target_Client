package love.target.mod;

import com.utils.ObjectUtils;
import love.target.command.CommandManager;
import love.target.command.commands.ModValueSetCommand;
import love.target.mod.mods.fight.*;
import love.target.mod.mods.item.ChestStealer;
import love.target.mod.mods.move.*;
import love.target.mod.mods.other.Teams;
import love.target.mod.mods.visual.ESP;
import love.target.mod.mods.visual.HUD;
import love.target.mod.mods.visual.NightVision;
import love.target.mod.mods.visual.PlayerList;
import love.target.mod.mods.world.Scaffold;
import love.target.render.screen.clickgui.ClickGui;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModManager {
    private static final List<Mod> mods = new CopyOnWriteArrayList<>();

    /**
     * 在此注册Mod
     * 使用registerMods() || registerMod()
     */
    public static void init() {
        /* FIGHT */
        registerMods(new KillAura(),new Velocity(),new AntiBot(),new AutoPot(),new AutoSoup());
        /* VISUAL */
        registerMods(new HUD(),new NightVision(),new ESP(),new PlayerList());
        /* MOVE */
        registerMods(new Sprint(),new Speed(),new NoSlow(),new TargetStrafe(),new ScreenMove(),new Fly());
        /* ITEM */
        registerMods(new ChestStealer(),new AutoArmor());
        /* OTHER */
        registerMods(new Teams());
        /* WORLD */
        registerMods(new Scaffold());

        registerModCommand();
    }

    /**
     * @see net.minecraft.client.Minecraft
     */
    public static void onKey(int keyCode) {
        if (keyCode == 54) {
            Minecraft.getMinecraft().displayGuiScreen(new ClickGui());
            return;
        }

        if (keyCode != 0) {
            for (Mod m : getMods()) {
                if (m.getKeyCode() == keyCode) {
                    m.toggle();
                }
            }
        }
    }

    private static void registerMods(Mod... mods) {
        getMods().addAll(Arrays.asList(mods));
    }

    private static void registerMod(Mod mod) {
        getMods().add(mod);
    }

    /**
     * 注册Mod的Command
     * @see Mod
     */
    private static void registerModCommand() {
        for (Mod m : getMods()) {
            CommandManager.registerCommands(new ModValueSetCommand(m));
        }
    }

    /**
     * 返回Mod是否开启
     * 如果Mod为null 返回false
     * 否则返回Mod的enable
     * @param modName -> Mod的名字
     * @see Mod
     * @return Mod enable
     */
    public static boolean getModEnableByName(String modName) {
        Mod m = getModByName(modName);

        if (ObjectUtils.isNull(m)) {
            return false;
        } else {
            return m.isEnabled();
        }
    }

    /**
     * 迭代Mods 如果Mod的名称 equalsIgnoreCase 输入的modName 则返回迭代的mod 否则返回null
     * @param modName -> Mod的名字
     * @see Mod
     * @return 与modName相等的Mod
     */
    public static Mod getModByName(String modName) {
        for (Mod m : getMods()) {
            if (m.getName().equalsIgnoreCase(modName)) {
                return m;
            }
        }

        return null;
    }

    public static List<Mod> getModsByCategory(Mod.Category category) {
        CopyOnWriteArrayList<Mod> mods = new CopyOnWriteArrayList<>();
        for (Mod m : getMods()) {
            if (m.getCategory() != category) continue;
            mods.add(m);
        }
        return mods;
    }

    /**
     * @return 注册的Mods
     */
    public static List<Mod> getMods() {
        return mods;
    }
}