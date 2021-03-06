package love.target.config;

import com.utils.ObjectUtils;
import com.utils.file.FileUtils;
import love.target.Wrapper;
import love.target.designer.Designer;
import love.target.designer.designers.*;
import love.target.eventapi.EventManager;
import love.target.mod.Mod;
import love.target.mod.ModManager;
import love.target.mod.value.Value;
import love.target.mod.value.ValueType;
import love.target.mod.value.values.*;
import love.target.other.ClientSettings;
import love.target.render.screen.designer.GuiDesigner;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConfigManager {
    private static final List<Config> configs = new CopyOnWriteArrayList<>();

    public static void init() {
        String[] files = new File(Wrapper.getClientFilePath() + "configs").list();
        if (files != null) {
            for (String listFile : files) {
                if (!listFile.startsWith("start")) continue;
                addConfigs(new Config(listFile.split("start")[1]));
            }
        }
    }

    public static void saveConfig(Config configIn) {
        String path = Wrapper.getClientFilePath() + configIn.getDir();

        StringBuilder modBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();
        StringBuilder designerBuilder = new StringBuilder();
        StringBuilder clientSettingBuilder = new StringBuilder();

        for (Mod m : ModManager.getMods()) {
            modBuilder.append(m.getName()).append(":").append(m.isEnabled()).append(":").append(m.getKeyCode()).append("\n");
            for (Value<?> value : m.getValues()) {
                valueBuilder.append(m.getName()).append(":").append(value.getName()).append(":").append(value.getValue()).append(":").append(value.getValueType().toString()).append("\n");
            }
        }

        for (Designer designer : GuiDesigner.designers) {
            designerBuilder.append(designer.getDesignerType()).append(":").append(designer.getX()).append(":").append(designer.getY()).append("\n");
        }

        for (BooleanValue setting : Wrapper.getClientSettingList()) {
            clientSettingBuilder.append(setting.getName()).append(":").append(setting.getValue()).append("\n");
        }

        FileUtils.writeStringTo(modBuilder.toString(),path,"mod_enable_bind.txt");
        FileUtils.writeStringTo(valueBuilder.toString(),path,"value.txt");
        FileUtils.writeStringTo(designerBuilder.toString(),path,"designer.txt");
        FileUtils.writeStringTo(clientSettingBuilder.toString(),path,"client_settings.txt");

        if (configIn != Wrapper.getNormalConfig()) {
            addConfigs(configIn);
            FileUtils.writeInputStreamTo(ObjectUtils.getResource("target_resources/image/config_head.png"),path + "config_head.jpg");
        }
    }

    public static void loadConfig(Config configIn) {
        String path = Wrapper.getClientFilePath() + configIn.getDir();

        try {
            if (configIn != Wrapper.getNormalConfig()) {
                for (Mod mod : ModManager.getMods()) {
                    mod.setEnabled(false);
                }
            }
            for (String s : FileUtils.readLine(new FileInputStream(path + "mod_enable_bind.txt"))) {
                String modName = s.split(":")[0];
                boolean modEnable = Boolean.parseBoolean(s.split(":")[1]);
                int modKeyCode = Integer.parseInt(s.split(":")[2]);

                Mod mod = ModManager.getModByName(modName);

                if (mod != null) {
                    mod.setEnabled(modEnable);
                    mod.setKeyCode(modKeyCode);
                }
            }

            for (String s : FileUtils.readLine(new FileInputStream(path + "value.txt"))) {
                String modName = s.split(":")[0];
                String valueName = s.split(":")[1];
                String value = s.split(":")[2];
                ValueType valueType = ValueType.get(s.split(":")[3]);

                if (!(valueType == ValueType.NULL)) {
                    Mod mod = ModManager.getModByName(modName);

                    if (mod != null) {
                        for (Value<?> v : mod.getValues()) {
                            if (v.getName().equalsIgnoreCase(valueName)) {
                                switch (valueType) {
                                    case BOOLEAN_VALUE:
                                        ((BooleanValue) v).setValue(Boolean.parseBoolean(value));
                                        break;
                                    case NUMBER_VALUE:
                                        ((NumberValue) v).setValue(Double.parseDouble(value));
                                        break;
                                    case MODE_VALUE:
                                        ((ModeValue) v).setValue(value);
                                        break;
                                    case TEXT_VALUE:
                                        ((TextValue) v).setValue(value);
                                        ((TextValue) v).getTextField().setText(value);
                                        break;
                                    case COLOR_VALUE:
                                        ((ColorValue) v).setValue(Integer.parseInt(value));
                                        ((ColorValue) v).getColorPalette().setNowRGB(Integer.parseInt(value));
                                        break;
                                }
                                break;
                            }
                        }
                    }
                }
            }

            if (configIn != Wrapper.getNormalConfig()) {
                GuiDesigner.designers.clear();
            }

            for (String s : FileUtils.readLine(new FileInputStream(path + "designer.txt"))) {
                Designer.DesignerType designerType = Designer.DesignerType.toDesignerTypeByString(s.split(":")[0]);
                int designerX = Integer.parseInt(s.split(":")[1]);
                int designerY = Integer.parseInt(s.split(":")[2]);

                switch (designerType) {
                    case LOGO:
                        GuiDesigner.addDesigner(new LogoDesigner(designerX,designerY));
                        break;
                    case ARRAY_LIST:
                        GuiDesigner.addDesigner(new ArrayListDesigner(designerX,designerY));
                        break;
                    case PLAYER_LIST:
                        GuiDesigner.addDesigner(new PlayerListDesigner(designerX,designerY));
                        break;
                    case SPEED_LIST:
                        GuiDesigner.addDesigner(new SpeedListDesigner(designerX,designerY));
                        break;
                    case INVENTORY_HUD:
                        GuiDesigner.addDesigner(new InventoryHUDDesigner(designerX,designerY));
                        break;
                    case TAB_GUI:
                        GuiDesigner.addDesigner(new TabGuiDesigner(designerX,designerY));
                        break;
                }
            }

            for (String s : FileUtils.readLine(new FileInputStream(path + "client_settings.txt"))) {
                BooleanValue value = ClientSettings.getValueByName(s.split(":")[0]);
                if (value != null) {
                    value.setValue(Boolean.parseBoolean(s.split(":")[1]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addConfigs(Config ... configs) {
        getConfigs().addAll(Arrays.asList(configs));
    }

    public static void removeConfig(Config configIn) {
        configs.remove(configIn);
        FileUtils.deleteFile(new File(Wrapper.getClientFilePath() + configIn.getDir()));
    }

    public static Config getConfigByName(String name) {
        for (Config c : configs) {
            if (!c.getName().equalsIgnoreCase(name)) continue;
            return c;
        }
        return null;
    }

    public static List<Config> getConfigs() {
        return configs;
    }
}
