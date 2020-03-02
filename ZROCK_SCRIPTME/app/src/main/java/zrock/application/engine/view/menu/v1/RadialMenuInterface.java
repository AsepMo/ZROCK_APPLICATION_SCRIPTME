package zrock.application.engine.view.menu.v1;

import zrock.application.scriptme.R;

import java.util.List;

/**
 * Interface for radial menu item data.
 */
public interface RadialMenuInterface {
	public String getName();
	public String getLabel();
	public int getIcon();
	public List<RadialMenuItem> getChildren();
	public void menuActiviated();
}
