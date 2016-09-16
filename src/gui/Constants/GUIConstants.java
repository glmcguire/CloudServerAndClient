package gui.Constants;

import java.awt.GridBagConstraints;

public interface GUIConstants {

	// Enum to specify the different Layout Manager types.
	public enum LayoutType {
		flowLayout,
		gridLayout,
		borderLayout,
		gridBagLayout;
	}
	
	// GridBagConstraints constants.
	public static final int WEST = GridBagConstraints.WEST;
	public static final int EAST = GridBagConstraints.EAST;
	public static final int CENTER = GridBagConstraints.CENTER;
	public static final int NONE = GridBagConstraints.NONE;
	public static final int HORIZONTAL = GridBagConstraints.HORIZONTAL;
	public static final int VERTICAL = GridBagConstraints.VERTICAL;
	public static final int BOTH = GridBagConstraints.BOTH;

}
