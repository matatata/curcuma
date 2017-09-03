package de.ceruti.curcuma.appkit.widgets.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.EdgedBalloonStyle;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

public class BalloonTipSupport implements TipInterface {
	private static Category logger = Logger
			.getInstance(BalloonTipSupport.class);

	private static EdgedBalloonStyle style = new EdgedBalloonStyle(Color.WHITE,
			Color.GRAY);
	private BalloonTip balloonTip;

	@Override
	public void disconnect(JComponent w) {
		logger.error("unimplemented");
	}

	@Override
	public void connect(JComponent w) {

		balloonTip = new BalloonTip(w, "<html><b>Hello</b></html>", style,
				BalloonTip.Orientation.LEFT_BELOW,
				BalloonTip.AttachLocation.ALIGNED, 5, 5, true);

		balloonTip.setVisible(false);

		// Don't close the balloon when clicking the close-button, you just need
		// to hide it
		balloonTip.setCloseButtonActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				balloonTip.setVisible(false);
			}
		});
	}

	@Override
	public void notifyMessage(String m) {

		balloonTip.setText("<html><i>" + m + "</i></html>");
		balloonTip.setVisible(true);
//		Toolkit.getDefaultToolkit().beep();

		final Timer timer = new Timer();
		TimerTask t = new TimerTask() {

			@Override
			public void run() {
				balloonTip.setVisible(false);
				timer.cancel();
			}
		};
		
		timer.schedule(t, 2000);
	}

}
