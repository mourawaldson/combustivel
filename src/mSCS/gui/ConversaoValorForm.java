package mSCS.gui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import mSCS.utils.Utils;

public class ConversaoValorForm extends Form implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandCancelar = new Command("Cancelar", Command.CANCEL, 0);

	private TextField textFieldPrecoLitroGasolina = new TextField("Preço por litro - " + Utils.GASOLINA, "", 5, TextField.DECIMAL);
	private TextField textFieldLitroAlcool = new TextField("Preço por litro - " + Utils.ALCOOL, "",5, TextField.DECIMAL);
	private TextField textFieldMetroGNV = new TextField("Preço por metro - " + Utils.GNV, "", 5, TextField.DECIMAL);
	private TextField textFieldValorAbastecimento = new TextField("Valor do abastecimento", "", 5, TextField.DECIMAL);
	private TextField textFieldConsumoGasolina = new TextField("Consumo na gasolina - KM/L", "", 5, TextField.DECIMAL);

	private String gasolina = "";
	private String alcool = "";
	private String gnv = "";
	private String valorAbastecimento = "";
	private String consumoGNV = "";

	private boolean vazio = false;

	private DisplayController displayControler = DisplayController.getInstance();

	private Alert alertAtencao = new Alert("Atenção", "", null, AlertType.WARNING);

	public ConversaoValorForm()
	{
		super("Conversão por valor");
		this.append(this.textFieldPrecoLitroGasolina);
		this.append(this.textFieldLitroAlcool);
		this.append(this.textFieldMetroGNV);
		this.append(this.textFieldValorAbastecimento);
		this.append(this.textFieldConsumoGasolina);
		this.addCommand(this.commandOk);
		this.addCommand(this.commandCancelar);
		this.setCommandListener(this);
	}
	
	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandOk))
		{
			this.alertAtencao.setTimeout(Alert.FOREVER);

			this.gasolina = this.textFieldPrecoLitroGasolina.getString();
			this.alcool = this.textFieldLitroAlcool.getString();
			this.gnv = this.textFieldMetroGNV.getString();
			this.valorAbastecimento = this.textFieldValorAbastecimento.getString();
			this.consumoGNV = this.textFieldConsumoGasolina.getString();

			if(this.gasolina.equals("") && this.alcool.equals("") && this.gnv.equals("") || this.gasolina.equals("0") && this.alcool.equals("0") && this.gnv.equals("0"))
				this.vazio = true;
			else if(this.valorAbastecimento.equals("") || this.valorAbastecimento.equals("0"))
				this.vazio = true;
			else if(this.consumoGNV.equals("") || this.consumoGNV.equals("0"))
				this.vazio = true;
			else
				this.vazio = false;

			if(this.vazio == true)
			{
				this.alertAtencao.setString("Preencha ao menos o valor de 1 combustível, o valor do abastecimento e o consumo de seu veículo na gasolina!");
				this.displayControler.exibirAlert(alertAtencao, this);
			}
			else
			{
				if(this.gasolina.equals(""))
					this.gasolina = "0";
				if(this.alcool.equals(""))
					this.alcool = "0";
				if(this.gnv.equals(""))
					this.gnv = "0";

				double gasolinaDouble = Double.parseDouble(this.gasolina);
				double alcoolDouble = Double.parseDouble(this.alcool);
				double gnvDouble = Double.parseDouble(this.gnv);
				double valorAbastecimentoDouble = Double.parseDouble(this.valorAbastecimento);
				double consumoGasolinaDouble = Double.parseDouble(this.consumoGNV);
				
				double litrosGasolina = valorAbastecimentoDouble/gasolinaDouble;
				double autonomiaGasolina = (valorAbastecimentoDouble/gasolinaDouble) * consumoGasolinaDouble;
				
				double litrosAlcool = valorAbastecimentoDouble/alcoolDouble;
				double autonomiaAlcool = (valorAbastecimentoDouble/alcoolDouble) * (consumoGasolinaDouble * 0.7);
				
				double metrosGnv = valorAbastecimentoDouble/gnvDouble;
				double consumoGnv = consumoGasolinaDouble + (consumoGasolinaDouble * 0.4);
				double autonomiaGnv = (valorAbastecimentoDouble/gnvDouble) * consumoGnv;
				
				double qtdGasosa = Math.floor(litrosGasolina);
				double autonGasosa = Math.floor(autonomiaGasolina);
				double qtdAlcool = Math.floor(litrosAlcool);
				double autonAlcool = Math.floor(autonomiaAlcool);
				double qtdGnv = Math.floor(metrosGnv);
				double autonGnv = Math.floor(autonomiaGnv);

				String gasosa = Double.toString(qtdGasosa);
				String autoGasosa = Double.toString(autonGasosa);
				String qAlcool = Double.toString(qtdAlcool);
				String autoAlcool = Double.toString(autonAlcool);
				String qGnv = Double.toString(qtdGnv);
				String autoGnv = Double.toString(autonGnv);

				String[] dadosValor = {this.valorAbastecimento, gasosa, autoGasosa, qAlcool, autoAlcool, qGnv, autoGnv};
				ConversaoValorResultForm formResultValor = new ConversaoValorResultForm(dadosValor);
				this.displayControler.exibir(formResultValor);
			}
			
		}else if (c.equals(this.commandCancelar))
		{
			this.displayControler.voltar(1);
		}
	}

}
