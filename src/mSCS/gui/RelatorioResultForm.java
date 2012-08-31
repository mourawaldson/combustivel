package mSCS.gui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import mSCS.utils.Utils;

public class RelatorioResultForm extends Form implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandCancelar = new Command("Cancelar", Command.CANCEL, 0);

	private StringItem stringItemGasolina = new StringItem(Utils.GASOLINA, "");
	private StringItem stringItemGasolinaDados = new StringItem("\nLitros       Valor       Média", "");
	private StringItem stringItemAlcool = new StringItem(Utils.ALCOOL, "");
	private StringItem stringItemAlcoolDados = new StringItem("\nLitros       Valor       Média", "");
	private StringItem stringItemGNV = new StringItem(Utils.GNV, "");
	private StringItem stringItemGNVDados = new StringItem("Metros       Valor       Média", "");
	private StringItem stringItemNenhumResultado = new StringItem("Nenhum registro encontrado!","");

	private String combustivel,valorVolume,precoCombustivel,tipoAbastecimento = null;

	private DisplayController displayControler = DisplayController.getInstance();
	private Alert alertAtencao = new Alert("Atenção", "", null, AlertType.WARNING);

	public RelatorioResultForm(String[] dados)
	{
		super("Relatório");
		int qtdRegistros = dados.length;

		this.append(this.stringItemGasolina);
		this.append(this.stringItemGasolinaDados);
		if(qtdRegistros > 0)
		{
			for(int i = 0; i < qtdRegistros; i++)
			{
				String dadosCadastro[] = Utils.split(dados[i], "|".charAt(0));
				this.combustivel = dadosCadastro[1].substring(12);
				this.precoCombustivel = dadosCadastro[2].substring(18);
				this.tipoAbastecimento = dadosCadastro[3].substring(19);
				this.valorVolume = dadosCadastro[4].substring(13);
	
				if(this.combustivel.equals(Utils.GASOLINA_UPPER))
				{
					if(this.tipoAbastecimento.equals("VOLUME"))
					{
						double valorVolumeDouble,precoCombustivelDouble = 0.0;
						valorVolumeDouble = Double.parseDouble(this.valorVolume);
						precoCombustivelDouble = Double.parseDouble(this.precoCombustivel);
						valorVolumeDouble = valorVolumeDouble * precoCombustivelDouble;
						this.valorVolume = Double.toString(valorVolumeDouble);
						this.valorVolume += this.valorVolume;
					}
				}
			}
			//StringItem stringItemRegistro = new StringItem("",  + "   " + this.valorVolume + "   " + );
			//this.append(stringItemRegistro);
		}
		else
			this.append(this.stringItemNenhumResultado);

		this.append(this.stringItemAlcool);
		this.append(this.stringItemAlcoolDados);
		this.append(this.stringItemGNV);
		this.append(this.stringItemGNVDados);
		this.alertAtencao.setString(dados.toString());
		this.alertAtencao.setTimeout(Alert.FOREVER);
		this.displayControler.exibirAlert(this.alertAtencao, this);
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