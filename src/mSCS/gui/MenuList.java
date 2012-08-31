package mSCS.gui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class MenuList extends List implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandSair = new Command("Sair", Command.EXIT, 1);

	private static String[] itensList = {"Cadastro","Busca","Conversão"};
	private DisplayController displayControler = DisplayController.getInstance();

	public MenuList()
	{
		super("mSCS", Choice.IMPLICIT, itensList, null);
		this.addCommand(this.commandOk);
		this.addCommand(this.commandSair);
		this.setCommandListener(this);
		this.addCommand(this.commandOk);
		this.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandOk))
		{
			switch (this.getSelectedIndex())
			{
				case 0:
					CadastroForm cadastroForm = new CadastroForm();
					this.displayControler.exibir(cadastroForm);
					break;
				case 1:
					BuscaForm buscaVeiculo = new BuscaForm();
					this.displayControler.exibir(buscaVeiculo);
					break;
				case 2:
					ConversaoMenuList conversao = new ConversaoMenuList();
					this.displayControler.exibir(conversao);
					break;
			}
		}
		else if(c.equals(this.commandSair))
			this.displayControler.sair();
	}
}