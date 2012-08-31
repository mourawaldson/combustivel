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

public class HistoricoForm extends Form implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandCancelar = new Command("Cancelar", Command.CANCEL, 0);

	private String[] itensChoiceGroup = {"Todos","Quantidade definida"};
	private ChoiceGroup choiceGroupQtdRegistros = new ChoiceGroup("", Choice.EXCLUSIVE, itensChoiceGroup, null);
	private TextField textFieldQtdRegistros = new TextField("Quantidade de registros","" , 7, TextField.NUMERIC);
	private Alert alertAtencao = new Alert("Atenção", "", null, AlertType.WARNING);

	private DisplayController displayControler = DisplayController.getInstance();
	private PersistenciaRecordStore persistenciaRecordStore = new PersistenciaRecordStore();

	private String placa = null;
	private boolean preencherCampo = false; //flag para saber se o alert para preencher o campo está sendo exibido

	public HistoricoForm(String placa)
	{
		super("Histórico");
		this.placa = placa;
		this.append(this.choiceGroupQtdRegistros);
		this.append(this.textFieldQtdRegistros);
		this.addCommand(this.commandCancelar);
		this.addCommand(this.commandOk);
		this.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandOk))
		{
			String[] abastecimentos = this.persistenciaRecordStore.listarAbastecimentosVeiculo(this.placa);
			if(abastecimentos != null)
			{
				int qtdAbastecimentos = abastecimentos.length;
	
				if(this.choiceGroupQtdRegistros.isSelected(1))
				{
					String qtdRegistrosSolicitadosString = this.textFieldQtdRegistros.getString().trim();
	
					int qtdRegistrosSolicitados = 0;

					if(qtdRegistrosSolicitadosString.equals(""))
					{
						this.alertAtencao.setString("Preencha a quantidade de registros!");
						this.displayControler.exibirAlert(alertAtencao,this);
						this.preencherCampo = true;
					}
					else
					{
						qtdRegistrosSolicitados = Integer.parseInt(qtdRegistrosSolicitadosString);
						this.preencherCampo = false;
					}

					if(qtdRegistrosSolicitados <= qtdAbastecimentos)
					{
						qtdAbastecimentos = qtdRegistrosSolicitados;
						this.preencherCampo = false;
					}
					else if(qtdRegistrosSolicitados > qtdAbastecimentos)
					{
						this.alertAtencao.setString("A quantidade deve ser até " + Integer.toString(qtdAbastecimentos) + "!");
						this.displayControler.exibirAlert(alertAtencao, this);
						this.preencherCampo = true;
					}
				}

				if(this.preencherCampo == false)
				{
					if(qtdAbastecimentos > 0)
					{
						HistoricoResultForm historicoResultForm = new HistoricoResultForm(abastecimentos);
						this.displayControler.exibir(historicoResultForm);
					}
					else
					{
						this.alertAtencao.setString("Nenhum registro encontrado!");
						this.displayControler.exibirAlert(alertAtencao, this);
					}
				}
			}
			else
			{
				this.alertAtencao.setString("Nenhum registro na base!");
				this.displayControler.exibirAlert(alertAtencao, this);
			}
		}
		else if (c.equals(this.commandCancelar))
			this.displayControler.voltar(1);
	}
}