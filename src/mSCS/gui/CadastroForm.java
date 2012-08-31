package mSCS.gui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import mSCS.persistencia.PersistenciaRecordStore;
import mSCS.utils.Utils;

public class CadastroForm extends Form implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandCancelar = new Command("Cancelar", Command.CANCEL, 0);

	private String[] itensChoiceGroup = {Utils.GASOLINA,Utils.ALCOOL,Utils.GNV};
	private TextField textFieldPlaca = new TextField("Placa","" , 7, TextField.ANY);
	private TextField textFieldMarca = new TextField("Marca","" , 20, TextField.ANY);
	private TextField textFieldModelo = new TextField("Modelo","" , 20, TextField.ANY);
	private ChoiceGroup choiceGroupCombustiveis = new ChoiceGroup("Combustíveis", Choice.MULTIPLE, itensChoiceGroup, null);

	private Alert alertAtencao = new Alert("Atenção", "", null, AlertType.WARNING);
	private Alert alertInfo = new Alert("Atenção", "", null, AlertType.INFO);
	private Alert alertErro = new Alert("Erro", "", null, AlertType.ERROR);

	private DisplayController displayControler = DisplayController.getInstance();
	private PersistenciaRecordStore persistenciaRecordStore = new PersistenciaRecordStore();

	public CadastroForm()
	{
		super("Cadastro");
		this.append(this.textFieldPlaca);
		this.append(this.textFieldMarca);
		this.append(this.textFieldModelo);
		this.append(this.choiceGroupCombustiveis);
		this.addCommand(this.commandCancelar);
		this.addCommand(this.commandOk);
		this.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandOk))
		{
			String placa = this.textFieldPlaca.getString().toUpperCase().trim();
			String marca = this.textFieldMarca.getString().toUpperCase().trim();
			String modelo = this.textFieldModelo.getString().toUpperCase().trim();

			long data = System.currentTimeMillis();
			String dataString = Long.toString(data);

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

			String dados = "PLACA:" + placa + "|MARCA:" + marca + "|MODELO:" + modelo + "|COMBUSTIVEIS:"
			+ combustiveis + "|DATA:" + dataString;

			if(placa.equals(""))
			{
				this.alertAtencao.setString("Preencha a placa!");
				this.displayControler.exibirAlert(alertAtencao,this);
			}
			else if(marca.equals(""))
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
				String[] cadastros = this.persistenciaRecordStore.listar(PersistenciaRecordStore.recordStoremSCSCadastro);
				int qtdCadastros = cadastros.length;
				boolean flag = false;
				for (int i = 0; i < qtdCadastros; i++)
				{
					String placaCadastrada = cadastros[i].substring(6, 13);
					if(!placaCadastrada.equals(placa))
						flag = false;
					else
					{
						flag = true;
						this.alertErro.setString("Placa já cadastrada!");
						this.displayControler.exibirAlert(alertErro, this);
						break;
					}
				}

				if(flag == false)
				{
					int idRetorno = this.persistenciaRecordStore.cadastrar(PersistenciaRecordStore.recordStoremSCSCadastro,dados.getBytes());
					if(idRetorno > 0)
					{
						this.alertInfo.setString("Cadastrado com sucesso!");
						this.displayControler.exibirAlert(alertInfo,this);
						this.displayControler.voltar(1);
					}
					else if(idRetorno == PersistenciaRecordStore.RecordStoreException)
					{
						this.alertErro.setString("Erro ao cadastrar!");
						this.displayControler.exibirAlert(alertErro,this);
					}
					else if(idRetorno == PersistenciaRecordStore.RecordStoreFullException)
					{
						this.alertErro.setString("Record store cheio!");
						this.displayControler.exibirAlert(alertErro,this);
					}
					else if(idRetorno == PersistenciaRecordStore.RecordStoreNotFoundException)
					{
						this.alertErro.setString("Record store não encontrado!");
						this.displayControler.exibirAlert(alertErro,this);
					}
				}
			}
		}
		else if (c.equals(this.commandCancelar))
			this.displayControler.voltar(1);
	}
}