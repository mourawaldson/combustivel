package mSCS.gui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

public class AbastecimentoForm extends Form implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandCancelar = new Command("Cancelar", Command.CANCEL, 0);

	private String[] itensTipoAbastecimento = {"Valor R$","Volume em Litros/M³"};

	private StringItem stringItemPlaca = new StringItem("Placa","");
	private ChoiceGroup choiceGroupCombustivel = new ChoiceGroup("Combustível", Choice.EXCLUSIVE);
	private TextField textFieldPrecoCombustivel = new TextField("Preço do combustível","" , 5, TextField.DECIMAL);
	private TextField textFieldKMAtual = new TextField("KM atual","" , 8, TextField.NUMERIC);
	private ChoiceGroup choiceGroupTipoAbastecimento = new ChoiceGroup("Abastecimento por:", Choice.EXCLUSIVE, itensTipoAbastecimento, null);
	private TextField textFieldValorVolume = new TextField("","" , 8, TextField.DECIMAL);

	private Alert alertAtencao = new Alert("Atenção", "", null, AlertType.WARNING);

	private DisplayController displayControler = DisplayController.getInstance();

	String placa = null;

	public AbastecimentoForm(String placa, String[] combustiveis)
	{
		super("Abastecimento");
		this.placa = placa;
		this.append(this.stringItemPlaca);
		this.stringItemPlaca.setText(this.placa);
		for(int i = 0; i < combustiveis.length; i++)
			this.choiceGroupCombustivel.append(combustiveis[i], null);

		this.append(this.choiceGroupCombustivel);
		this.append(this.textFieldPrecoCombustivel);
		this.append(this.textFieldKMAtual);
		this.append(this.choiceGroupTipoAbastecimento);
		this.append(this.textFieldValorVolume);
		this.addCommand(this.commandOk);
		this.addCommand(this.commandCancelar);
		this.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandOk))
		{
			String combustivel = this.choiceGroupCombustivel.getString(this.choiceGroupCombustivel.getSelectedIndex()).toUpperCase();
			String precoCombustivel = this.textFieldPrecoCombustivel.getString().trim();
			String KMAtual = this.textFieldKMAtual.getString().trim();

			String tipoAbastecimento = "";
			if(this.choiceGroupTipoAbastecimento.isSelected(0))
				tipoAbastecimento = "valor";
			else if(this.choiceGroupTipoAbastecimento.isSelected(1))
				tipoAbastecimento = "volume";

			String valorVolume = this.textFieldValorVolume.getString().trim();

			if(precoCombustivel.equals(""))
			{
				this.alertAtencao.setString("Preencha preço do combustível!");
				this.displayControler.exibirAlert(alertAtencao,this);
			}
			else if(KMAtual.equals(""))
			{
				this.alertAtencao.setString("Preencha a KM atual do veículo!");
				this.displayControler.exibirAlert(alertAtencao,this);
			}
			else if(valorVolume.equals(""))
			{
				String palavra = null;
				if(this.choiceGroupTipoAbastecimento.isSelected(0))
					palavra = itensTipoAbastecimento[0].toLowerCase();
				else if(this.choiceGroupTipoAbastecimento.isSelected(1))
					palavra = itensTipoAbastecimento[1].toLowerCase();

				this.alertAtencao.setString("Preencha o " + palavra + " do abastecimento!");
				this.displayControler.exibirAlert(alertAtencao,this);
			}
			else
			{
				String[] dados = {this.placa,combustivel,precoCombustivel,KMAtual,tipoAbastecimento,valorVolume};
				AbastecimentoConfirmForm abastecimentoConfirmForm = new AbastecimentoConfirmForm(dados);
				this.displayControler.exibir(abastecimentoConfirmForm);
			}
		}
		else if (c.equals(this.commandCancelar))
			this.displayControler.voltar(1);
	}
}