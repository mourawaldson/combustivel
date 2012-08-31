package mSCS.gui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import mSCS.utils.Utils;

public class ConversaoCombustivelForm extends Form implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandCancelar = new Command("Cancelar", Command.CANCEL, 0);

	private TextField textFieldLitroGasolina = new TextField("Preço por litro - " + Utils.GASOLINA, "" , 5, TextField.DECIMAL);
	private TextField textFieldLitroAlcool = new TextField("Preço por litro - " + Utils.ALCOOL, "" , 5, TextField.DECIMAL);
	private TextField textFieldMetroGNV = new TextField("Preço por metro - " + Utils.GNV, "" , 5, TextField.DECIMAL);

	private Alert alertAtencao = new Alert("Atenção", "", null, AlertType.WARNING);

	private DisplayController displayControler = DisplayController.getInstance();

	private static final int GASOLINA_ALCOOL = 1;
	private static final int GNV_GASOLINA = 2;
	private static final int GNV_ALCOOL = 3;
	private static final int GASOLINA_ALCOOL_GNV = 4;

	public ConversaoCombustivelForm()
	{
		super("Conversão por combustível");
		this.append(this.textFieldLitroGasolina);
		this.append(this.textFieldLitroAlcool);
		this.append(this.textFieldMetroGNV);
		this.addCommand(this.commandOk);
		this.addCommand(this.commandCancelar);
		this.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandOk))
		{	
			String gasolina = this.textFieldLitroGasolina.getString();
			String alcool = this.textFieldLitroAlcool.getString();
			String gnv = this.textFieldMetroGNV.getString();

			String combustivelIdeal = null;
			boolean camposVazios = false;

			if(gasolina.equals("") && alcool.equals("") || gasolina.equals("0") && alcool.equals("0"))
				camposVazios = true;
			else if(gasolina.equals("") && gnv.equals("") || gasolina.equals("0") && gnv.equals("0"))
				camposVazios = true;
			else if(alcool.equals("") && gnv.equals("") || alcool.equals("0") && gnv.equals("0"))
				camposVazios = true;
			else
				camposVazios = false;

			if(camposVazios == true)
			{
				this.alertAtencao.setString("Preencha ao menos 2 campos com valores válidos!");
				this.displayControler.exibirAlert(alertAtencao, this);
			}
			else
			{
				if(gasolina.equals(""))
					gasolina = "0";
				else if(alcool.equals(""))
					alcool = "0";
				else if(gnv.equals(""))
					gnv = "0";

				double precoGasolina = Double.parseDouble(gasolina);
				double precoAlcool = Double.parseDouble(alcool);
				double precoGNV = Double.parseDouble(gnv);

				int combustiveis = 0;

				if(precoGasolina != 0.0 && precoAlcool != 0.0 && precoGNV == 0.0)
					combustiveis = GASOLINA_ALCOOL;
				else if(precoGasolina != 0.0 && precoGNV != 0.0 && precoAlcool == 0.0)
					combustiveis = GNV_GASOLINA;
				else if(precoAlcool != 0.0 && precoGNV != 0.0 && precoGasolina == 0.0)
					combustiveis = GNV_ALCOOL;
				else if(precoGasolina != 0.0 && precoAlcool != 0.0 && precoGNV != 0.0)
					combustiveis = GASOLINA_ALCOOL_GNV;

				double percentualGasolina = precoGasolina * 0.7;
				double percentualGNVGasolina = precoGNV * 0.75;
				double percentualGNVAlcool = precoGNV * 0.5;

				switch (combustiveis)
				{
					case GASOLINA_ALCOOL:
						combustivelIdeal = (precoAlcool >= percentualGasolina) ? Utils.GASOLINA : Utils.ALCOOL;
						break;
					case GNV_GASOLINA:
						combustivelIdeal = (precoGasolina >= percentualGNVGasolina) ? Utils.GNV : Utils.GASOLINA;
						break;
					case GNV_ALCOOL:
						combustivelIdeal = (precoAlcool >= percentualGNVAlcool) ? Utils.GNV : Utils.ALCOOL;
						break;
					case GASOLINA_ALCOOL_GNV:
						combustivelIdeal = (precoAlcool >= percentualGasolina) ? Utils.GASOLINA : Utils.ALCOOL;

						if(combustivelIdeal.equals(Utils.GASOLINA))
							combustivelIdeal = (precoGasolina >= percentualGNVGasolina) ? Utils.GNV : Utils.GASOLINA;
						else if(combustivelIdeal.equals(Utils.ALCOOL))
							combustivelIdeal = (precoAlcool >= percentualGNVAlcool) ? Utils.GNV : Utils.ALCOOL;
						break;
				}

				String[] dados = {gasolina,alcool,gnv,combustivelIdeal};
				ConversaoCombustivelResultForm formResult = new ConversaoCombustivelResultForm(dados);
				this.displayControler.exibir(formResult);
			}
		}
		else if (c.equals(this.commandCancelar))
			this.displayControler.voltar(1);
	}
}