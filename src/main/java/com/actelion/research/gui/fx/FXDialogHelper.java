package com.actelion.research.gui.fx;

import com.actelion.research.gui.editor.DialogEventConsumer;
import com.actelion.research.gui.editor.FXEditorArea;
import com.actelion.research.gui.generic.GenericDialog;
import com.actelion.research.gui.generic.GenericDialogHelper;
import com.actelion.research.gui.generic.GenericPopupMenu;
import com.actelion.research.gui.hidpi.HiDPIHelper;
import com.actelion.research.gui.hidpi.ScaledEditorKit;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.File;

public class FXDialogHelper implements GenericDialogHelper {
	private FXEditorArea mDrawArea;
	private JDialog mHelpDialog;

	public FXDialogHelper(FXEditorArea drawArea) {
		mDrawArea = drawArea;
	}

	@Override
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(getParent(), message);
	}

	@Override
	public GenericDialog createDialog(String title, DialogEventConsumer consumer) {
		return null;
/*		Component c = mDrawArea;
		while (!(c instanceof Frame || c instanceof Dialog))
			c = c.getParent();

		GenericDialog dialog = (c instanceof Frame) ? new FXDialog((Frame)c, title) : new FXDialog((Dialog)c, title);
		dialog.setEventConsumer(consumer);
		return dialog;
*/	}

	@Override
	public GenericPopupMenu createPopupMenu(DialogEventConsumer consumer) {
		return null;
//		return new FXPopupMenu(mDrawArea, consumer);
	}

	@Override
	public void grabFocus() {
		mDrawArea.requestFocus();
	}

	@Override
	public void setCursor(int cursor) {
//		mDrawArea.setCursor(CursorHelper.getCursor(cursor));
	}

	@Override
	public File openChemistryFile(boolean isReaction) {
		return null;
/*		return isReaction ?
				FileHelper.getFile(mDrawArea, "Please select a reaction file",
						FileHelper.cFileTypeRXN | CompoundFileHelper.cFileTypeRD)
				: FileHelper.getFile(mDrawArea, "Please select a molecule file",
				FileHelper.cFileTypeMOL | CompoundFileHelper.cFileTypeMOL2);
*/	}

	@Override
	public void showHelpDialog(String url) {
		if (mHelpDialog == null || !mHelpDialog.isVisible()) {
			JEditorPane helpPane = new JEditorPane();
			helpPane.setEditorKit(HiDPIHelper.getUIScaleFactor() == 1f ? new HTMLEditorKit() : new ScaledEditorKit());
			helpPane.setEditable(false);
			try {
				helpPane.setPage(getClass().getResource(url));
			}
			catch (Exception ex) {
				helpPane.setText(ex.toString());
			}

			Component c = getParent();
			if (c instanceof Frame)
				mHelpDialog = new JDialog((Frame)c, "Idorsia Structure Editor Help", false);
			else
				mHelpDialog = new JDialog((Dialog)c, "Idorsia Structure Editor Help", false);

			mHelpDialog.setSize(HiDPIHelper.scale(520), HiDPIHelper.scale(440));
			mHelpDialog.getContentPane().add(new JScrollPane(helpPane,
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
			int x = (c.getX()>=8 + mHelpDialog.getWidth()) ? c.getX() - 8 - mHelpDialog.getWidth() : c.getX() + 8 + c.getWidth();
			mHelpDialog.setLocation(x, c.getY());
			mHelpDialog.setVisible(true);
		}
		else {
			Component c = getParent();
			int x = (mHelpDialog.getX() + mHelpDialog.getWidth() / 2>=c.getX() + c.getWidth() / 2) ?
					c.getX() - 8 - mHelpDialog.getWidth() : c.getX() + 8 + c.getWidth();
			mHelpDialog.setLocation(x, c.getY());
		}
	}

	private Component getParent() {
		return null;
/*		Component parent = mDrawArea;
		while (!(parent instanceof Frame || parent instanceof Dialog) && parent.getParent() != null)
			parent = parent.getParent();
		return parent;
*/	}
}
