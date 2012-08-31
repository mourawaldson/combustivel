package mSCS;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import mSCS.gui.DisplayController;
import mSCS.gui.MenuList;

public class CombustivelMIDlet extends MIDlet
{

	public CombustivelMIDlet()
	{
		
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{

	}

	protected void pauseApp()
	{
		
	}

	protected void startApp() throws MIDletStateChangeException
	{
		DisplayController displaycontroler = DisplayController.getInstance();
		displaycontroler.setMIDlet(this);
		//InicioCombustivel gauge = new InicioCombustivel();
		MenuList menuList = new MenuList();
		displaycontroler.exibir(menuList);
	}

}
