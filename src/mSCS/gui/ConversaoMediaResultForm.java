package mSCS.gui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

public class ConversaoMediaResultForm extends Form implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandVoltar = new Command("Voltar", Command.CANCEL, 0);

	private StringItem stringItemMedia = new StringItem("Média de consumo", "");
	private StringItem stringItemKmPercorrida = new StringItem("Km percorrida", "");
	private StringItem stringItemVolume = new StringItem("Volume Litros/M³", "");

	private DisplayController displayControler = DisplayController.getInstance();

	public ConversaoMediaResultForm(String[] dados)
	{
		super("Média de consumo");
		String kmPercorrida = dados[0];
		String volume = dados[1];
		String media = dados[2];

		this.append(this.stringItemMedia);
		this.stringItemMedia.setText(media);
		this.append(this.stringItemKmPercorrida);
		this.stringItemKmPercorrida.setText(kmPercorrida);
		this.append(this.stringItemVolume);
		this.stringItemVolume.setText(volume);
		this.addCommand(this.commandOk);
		this.addCommand(this.commandVoltar);
		this.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandVoltar))
			this.displayControler.voltar(1);

		else if(c.equals(this.commandOk))
			this.displayControler.voltar(3);
	}
}
