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

import mSCS.persistencia.PersistenciaRecordStore;
import mSCS.utils.Utils;

public class EditarForm extends Form implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandCancelar = new Command("Cancelar", Command.CANCEL, 0);

	private String[] itensChoiceGroup = {Utils.GASOLINA,Utils.ALCOOL,Utils.GNV};
	private StringItem stringItemPlaca = new StringItem("Placa", "");
	private TextField textFieldMarca = new TextField("Marca","" , 20, TextField.ANY);
	private TextField textFieldModelo = new TextField("Modelo","" , 20, TextField.ANY);
	private ChoiceGroup choiceGroupCombustiveis = new ChoiceGroup("Combustíveis", Choice.MULTIPLE, itensChoiceGroup, null);

	private Alert alertAtencao = new Alert("Atenção", "", null, AlertType.WARNING);
	private Alert alertInfo = new Alert("Atenção", "", null, AlertType.INFO);
	private Alert alertErro = new Alert("Erro", "", null, AlertType.ERROR);

	private DisplayController displayControler = DisplayController.getInstance();
	private PersistenciaRecordStore persistenciaRecordStore = new PersistenciaRecordStore();

	private int id = 0;
	private String placa,data = null;
	private String[] combustiveis = null;

	public EditarForm(String[] dados)
	{
		super("Cadastro");
		this.placa = dados[0];
		this.append(this.stringItemPlaca);
		this.stringItemPlaca.setText(this.placa);
		this.append(this.textFieldMarca);
		this.textFieldMarca.setString(dados[1]);
		this.append(this.textFieldModelo);
		this.textFieldModelo.setString(dados[2]);
		this.append(this.choiceGroupCombustiveis);
		String combustiveis = dados[3];
		this.combustiveis = Utils.split(combustiveis, "-".charAt(0));
		for(int i = 0; i < this.combustiveis.length; i++)
		{
			String item = this.combustiveis[i];
			if(item.equals(Utils.GASOLINA_UPPER))
				this.choiceGroupCombustiveis.setSelectedIndex(i, true);
			else if(item.equals(Utils.ALCOOL_UPPER))
				this.choiceGroupCombustiveis.setSelectedIndex(i, true);
			else if(item.equals(Utils.GNV))
				this.choiceGroupCombustiveis.setSelectedIndex(i, true);
		}
		this.data = dados[4];
		this.id = Integer.parseInt(dados[5]);
		this.addCommand(this.commandCancelar);
		this.addCommand(this.commandOk);
		this.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandOk))
		{
			String marca = this.textFieldMarca.getString().toUpperCase().trim();
			String modelo = this.textFieldModelo.getString().toUpperCase().trim();

			boolean selected[] = new boolean[this.choiceGroupCombustiveis.size()];
			this.choiceGroupCombustiveis.getSelectedFlags(selected);
			int qtdItensSelecionados = 0;
			String separador = "";
			String combustiveis = "";
			for (int i = 0; i < selected.length; i++)
			{
				if(selected[i])
				{
					if(i != 0)
						separador = "-";

					combustiveis = combustiveis + separador + this.choiceGroupCombustiveis.getString(i).toUpperCase();
					qtdItensSelecionados++;
				}
			}

			String dados = "PLACA:" + this.placa + "|MARCA:" + marca + "|MODELO:" + modelo + "|COMBUSTIVEIS:"
			+ combustiveis + "|DATA:" + this.data;

			if(marca.equals(""))
			{
				this.alertAtencao.setString("Preencha a marca!");
				this.displayControler.exibirAlert(alertAtencao,this);
			}
			else if(modelo.equals(""))
			{
				this.alertAtencao.setString("Preencha o modelo!");
				this.displayControler.exibirAlert(alertAtencao,this);
			}
			else if(qtdItensSelecionados == 0)
			{
				this.alertAtencao.setString("Selecione algum combustível!");
				this.displayControler.exibirAlert(alertAtencao,this);
			}
			else
			{
				if(this.persistenciaRecordStore.alterar(PersistenciaRecordStore.recordStoremSCSCadastro, this.id, dados.getBytes()))
				{
					this.alertInfo.setString("Editado com sucesso!");
					this.displayControler.exibirAlert(alertInfo,this);
					this.displayControler.voltar(2);
				}
				else
				{
					this.alertErro.setString("Erro ao editar!");
					this.displayControler.exibirAlert(alertErro,this);
				}
			}
		}
		else if (c.equals(this.commandCancelar))
			this.displayControler.voltar(1);
	}
}