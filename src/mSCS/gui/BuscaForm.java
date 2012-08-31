package mSCS.gui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import mSCS.persistencia.PersistenciaRecordStore;
import mSCS.utils.Utils;

public class BuscaForm extends Form implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandCancelar = new Command("Cancelar", Command.CANCEL, 0);

	private TextField textFieldPlaca = new TextField("Placa","" , 7, TextField.ANY);
	private Alert alertAtencao = new Alert("Atenção", "", null, AlertType.WARNING);

	private DisplayController displayControler = DisplayController.getInstance();
	private PersistenciaRecordStore persistenciaRecordStore = new PersistenciaRecordStore();

	public BuscaForm()
	{
		super("Busca");
		this.append(this.textFieldPlaca);
		this.addCommand(this.commandCancelar);
		this.addCommand(this.commandOk);
		this.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandOk))
		{
			String placa = this.textFieldPlaca.getString().toUpperCase();

			if(placa.equals(""))
			{
				this.alertAtencao.setString("Preencha a placa!");
				this.displayControler.exibirAlert(alertAtencao,this);
			}
			else
			{
				String cadastro = this.persistenciaRecordStore.buscarCadastroVeiculo(placa);

				if(cadastro != null)
				{
					String dadosCadastro[] = Utils.split(cadastro, "|".charAt(0));

					String marca = dadosCadastro[1].substring(6);
					String modelo = dadosCadastro[2].substring(7);
					String combustiveis = dadosCadastro[3].substring(13);
					String data = dadosCadastro[4].substring(5);
					String id = dadosCadastro[5];
					String[] dados = {placa,marca,modelo,combustiveis,data,id};

					BuscaResultForm buscaResultForm = new BuscaResultForm(dados);
					this.displayControler.exibir(buscaResultForm);
				}
				else
				{
					this.alertAtencao.setString("Nenhum registro econtrado!");
					this.displayControler.exibirAlert(alertAtencao, this);
				}
			}
		}
		else if (c.equals(this.commandCancelar))
			this.displayControler.voltar(1);
	}
}