package love.target.render.screen.designer.menu.menus;

import love.target.designer.designers.*;
import love.target.render.screen.designer.GuiDesigner;
import love.target.render.screen.designer.menu.DesignerMenu;

public class DesignerMainMenu extends DesignerMenu {
    public DesignerMainMenu() {
        super("Main Menu");
        addButton(new DesignerMenuButton("Create Logo",() -> GuiDesigner.addDesigner(new LogoDesigner())));
        addButton(new DesignerMenuButton("Create ArrayList",() -> GuiDesigner.addDesigner(new ArrayListDesigner())));
        addButton(new DesignerMenuButton("Create PlayerList",() -> GuiDesigner.addDesigner(new PlayerListDesigner())));
        addButton(new DesignerMenuButton("Create SpeedList",() -> GuiDesigner.addDesigner(new SpeedListDesigner())));
        addButton(new DesignerMenuButton("Create InventoryHUD",() -> GuiDesigner.addDesigner(new InventoryHUDDesigner())));
        addButton(new DesignerMenuButton("Create TabGui",() -> GuiDesigner.addDesigner(new TabGuiDesigner())));
    }
}
