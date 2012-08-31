package mSCS.gui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class ConversaoMenuList extends List implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandCancelar = new Command("Cancelar", Command.CANCEL, 0);

	private static String[] itensList = {"Combustível","Valor","Média do consumo"};

	private DisplayController displayControler = DisplayController.getInstance();

	public ConversaoMenuList()
	{
		super("Conversão por:", Choice.IMPLICIT, itensList, null);
		this.addCommand(this.commandOk);
		this.addCommand(this.commandCancelar);
		this.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandOk))
		{
			switch (this.getSelectedIndex())
			{
				case 0:
					ConversaoCombustivelForm conversaoCombustivelForm = new ConversaoCombustivelForm();
					this.displayControler.exibir(conversaoCombustivelForm);
					break;
				case 1:
					ConversaoValorForm conversaoValorForm = new ConversaoValorForm();
					this.displayControler.exibir(conversaoValorForm);
					break;
				case 2:
					ConversaoMediaForm conversaoMediaForm = new ConversaoMediaForm();
					this.displayControler.exibir(conversaoMediaForm);
					break;
			}
		}
		else if (c.equals(this.commandCancelar))
			this.displayControler.voltar(1);
	}
}