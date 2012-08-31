package mSCS.gui;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;

public class InicioCombustivel extends Form
{
	public Gauge gauge;
	private Timer timer;
	private DownloadTimer downloadTimer;
	private Image image = null;
	private ImageItem imageItem;

	public InicioCombustivel()
	{
		super("Carregando...");

		try {
			image = Image.createImage("/logo.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		imageItem = new ImageItem("", image, ImageItem.LAYOUT_CENTER, "mCSC");
		gauge = new Gauge("",false,20,1);
		gauge.setLayout(3);

		this.append(imageItem);
		this.append(gauge);
		timer = new Timer();
		downloadTimer = new DownloadTimer();
		timer.scheduleAtFixedRate(downloadTimer,0, 200);
	}

	private class DownloadTimer extends TimerTask
	{	
		private DisplayController displayControler = DisplayController.getInstance();

		public final void run()
		{
			if(gauge.getValue() < gauge.getMaxValue())
				gauge.setValue(gauge.getValue() + 1);
			else
			{		
				MenuList menu = new MenuList();
				this.displayControler.exibir(menu);
			}
		}
	}
}