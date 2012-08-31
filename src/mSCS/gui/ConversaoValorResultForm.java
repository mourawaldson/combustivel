package mSCS.gui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

public class ConversaoValorResultForm extends Form implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandVoltar = new Command("Voltar", Command.CANCEL, 0);

	private DisplayController displayControler = DisplayController.getInstance();

	public ConversaoValorResultForm(String[] dados)
	{
		super("Conversão por valor");

		StringItem texto = new StringItem("Com R$"+dados[0]+" você abastece", "");
		StringItem gasosa = new StringItem("\nGasolina"+dados[1]+" litros e percorre "+dados[2]+" KMs", "");
		StringItem alcool = new StringItem("\nÁlcool"+dados[3]+" litros e percorre "+dados[4]+" KMs", "");
		StringItem gnv = new StringItem("\nGNV"+dados[5]+" metros e percorre "+dados[6]+" KMs", "");

		this.append(texto);

		if(!dados[1].equals("Infinity"))
			this.append(gasosa);

		if(!dados[3].equals("Infinity"))
			this.append(alcool);

		if(!dados[5].equals("Infinity"))
			this.append(gnv);

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