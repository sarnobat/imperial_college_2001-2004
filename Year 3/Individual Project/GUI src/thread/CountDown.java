package thread;

import java.applet.*;

public class CountDown extends Applet implements Runnable {
	Thread counter;
	int i;
	final static int N = 10;
	AudioClip beepSound, tickSound;

	public void init() {
		/*add(display = new NumberCanvas("CountDown"));
		display.setSize(150, 100);
		tickSound = getAudioClip(getDocumentBase(), "sound/tick.au");
		beepSound = getAudioClip(getDocumentBase(), "sound/beep.au");*/
	}

	public void start() {
		counter = new Thread(this);
		i = N;
		counter.start();
	}

	public void stop() {
		counter = null;
	}

	public void run() {
		while (true) {
//			if (counter == null)
//				return;
			if (i > 0) {
				tick();
				--i;
			}
			if (i == 0) {
				beep();
				return;
			}
		}
	}

	private void tick() {
		//display.setvalue(i);
		System.out.println(i);
		
		//tickSound.play();
//		try {
//			Thread.sleep(10000);
//		}
//		catch (InterruptedException e) {
//		}
	}

	private void beep() {
		/*display.setvalue(i);
		beepSound.play();*/
		System.out.println("beep");
		
	}
}
