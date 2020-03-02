package zrock.application.engine.view.menu.v2;

import zrock.application.scriptme.R;

public class RadialMenuItem {

	private String mMenuID;
	private String mMenuName;
	private OnRadialMenuClick mCallback;
	
	/**
	 * @param mMenuID
	 * @param mMenuName
	 */
	public RadialMenuItem(String mMenuID, String mMenuName) {
		this.mMenuID = mMenuID;
		this.mMenuName = mMenuName;
	}

	/**
	 * @return the mMenuID
	 */
	public String getMenuID() {
		return mMenuID;
	}
	
	/**
	 * @return the mMenuName
	 */
	public String getMenuName() {
		return mMenuName;
	}
	
	/**
	 * 
	 * @param onRadailMenuClick
	 */
	public void setOnRadialMenuClickListener(OnRadialMenuClick onRadailMenuClick) {
		this.mCallback = onRadailMenuClick;
	}
	
	/**
	 * 
	 * @return
	 */
	public OnRadialMenuClick getOnRadialMenuClick() {
		return mCallback;
	}
}
