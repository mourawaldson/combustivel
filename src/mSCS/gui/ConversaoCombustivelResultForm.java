package mSCS.gui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import mSCS.utils.Utils;

public class ConversaoCombustivelResultForm extends Form implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandVoltar = new Command("Voltar", Command.CANCEL, 0);

	private StringItem stringItemCombustivelIdeal = new StringItem("\nAbasteça com", "");
	private StringItem stringItemPrecoGasolina = new StringItem("Preço por litro - " + Utils.GASOLINA + ": ", "");
	private StringItem stringItemPrecoAlcool = new StringItem("Preço por litro - " + Utils.ALCOOL + ": ", "");
	private StringItem stringItemPrecoGNV = new StringItem("Preço por metro - " + Utils.GNV + ": ", "");

	private DisplayController displayControler = DisplayController.getInstance();

	public ConversaoCombustivelResultForm(String[] dados)
	{
		super("Conversão por combustível");
		String precoGasolina = dados[0];
		String precoAlcool = dados[1];
		String precoGNV = dados[2];
		String combustivelIdeal = dados[3];

		if(!precoGasolina.equals("0"))
		{
			this.append(stringItemPrecoGasolina);
			this.stringItemPrecoGasolina.setText(precoGasolina);
		}

		if(!precoAlcool.equals("0"))
		{
			this.append(stringItemPrecoAlcool);
			this.stringItemPrecoAlcool.setText(precoAlcool);
		}

		if(!precoGNV.equals("0"))
		{
			this.append(stringItemPrecoGNV);
			this.stringItemPrecoGNV.setText(precoGNV);
		}

		this.append(this.stringItemCombustivelIdeal);
		this.stringItemCombustivelIdeal.setText(combustivelIdeal);
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
