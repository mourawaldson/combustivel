package mSCS.gui;

import java.util.Date;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import mSCS.utils.Utils;

public class HistoricoResultForm extends Form implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandCancelar = new Command("Cancelar", Command.CANCEL, 0);

	private StringItem stringItemTotalRegistros = new StringItem("Qtd de registros", "");
	private StringItem stringItemDados = new StringItem("\nData   Comb.   Valor", "");

	private DisplayController displayControler = DisplayController.getInstance();

	private String data,combustivel,valorVolume,precoCombustivel,tipoAbastecimento = null;

	public HistoricoResultForm(String[] dados)
	{
		super("Histórico");
		int qtdRegistros = dados.length;

		this.append(this.stringItemTotalRegistros);
		this.stringItemTotalRegistros.setText(Integer.toString(qtdRegistros));
		this.append(this.stringItemDados);

		for(int i = 0; i < qtdRegistros; i++)
		{
			String dadosCadastro[] = Utils.split(dados[i], "|".charAt(0));
			this.combustivel = dadosCadastro[1].substring(12);
			this.precoCombustivel = dadosCadastro[2].substring(18);
			this.tipoAbastecimento = dadosCadastro[3].substring(19);
			this.valorVolume = dadosCadastro[4].substring(13);
			this.data = dadosCadastro[6].substring(5);
			long dataLong = Long.parseLong(this.data);
			Date dt = new Date(dataLong);

			if(this.combustivel.equals(Utils.ALCOOL_UPPER))
				this.combustivel = Utils.ALCOOL;
			else if(this.combustivel.equals(Utils.GASOLINA_UPPER))
				this.combustivel = Utils.GASOLINA;

			if(this.tipoAbastecimento.equals("VOLUME"))
			{
				double valorVolumeDouble,precoCombustivelDouble = 0.0;
				valorVolumeDouble = Double.parseDouble(this.valorVolume);
				precoCombustivelDouble = Double.parseDouble(this.precoCombustivel);
				valorVolumeDouble = valorVolumeDouble * precoCombustivelDouble;
				this.valorVolume = Double.toString(valorVolumeDouble);
			}
			StringItem stringItemRegistro = new StringItem("", Utils.data(dt) + "   " + this.combustivel + "   " + this.valorVolume);
			this.append(stringItemRegistro);
		}

		this.addCommand(this.commandOk);
		this.addCommand(this.commandCancelar);
		this.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandCancelar))
			this.displayControler.voltar(1);
	}
}