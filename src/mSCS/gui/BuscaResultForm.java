package mSCS.gui;

import java.util.Date;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import mSCS.persistencia.PersistenciaRecordStore;
import mSCS.utils.Utils;

public class BuscaResultForm extends Form implements CommandListener
{
	private Command commandAbastecer = new Command("Abastecer", Command.OK, 0);
	private Command commandExcluir = new Command("Excluir", Command.OK, 1);
	private Command commandEditar = new Command("Editar", Command.OK, 2);
	private Command commandRelatorio = new Command("Relatório", Command.OK, 3);
	private Command commandHistorico = new Command("Histórico", Command.OK, 4);
	private Command commandVoltar = new Command("Voltar", Command.CANCEL, 0);
	private Command commandSim = new Command ("Sim", Command.OK, 1);
	private Command commandNao = new Command("Não", Command.CANCEL, 1);

	private StringItem stringItemData = new StringItem("Data de cadastro", "");
	private StringItem stringItemPlaca = new StringItem("Placa", "");
	private StringItem stringItemMarca = new StringItem("Marca", "");
	private StringItem stringItemModelo = new StringItem("Modelo", "");
	private StringItem stringItemCombustiveis = new StringItem("Combustíveis", "");

	private Alert alertConfirmacao = new Alert("Excluir", "Confirma a exclusão?", null, AlertType.CONFIRMATION);

	private DisplayController displayControler = DisplayController.getInstance();
	private PersistenciaRecordStore persistenciaRecordStore = new PersistenciaRecordStore();

	private String placa = null;
	private int id = 0;
	private String[] combustiveis,dados = null;

	public BuscaResultForm(String[] dados)
	{
		super("Resultado da busca");
		this.dados = dados;
		this.placa = dados[0];
		String marca = dados[1];
		String modelo = dados[2];
		String combustiveis = dados[3];
		this.combustiveis = Utils.split(combustiveis, "-".charAt(0));
		combustiveis = "";
		for(int i = 0; i < this.combustiveis.length; i++)
		{
			String item = this.combustiveis[i];
			if(item.equals(Utils.GASOLINA_UPPER))
				this.combustiveis[i] = Utils.GASOLINA;
			else if(item.equals(Utils.ALCOOL_UPPER))
				this.combustiveis[i] = Utils.ALCOOL;

			combustiveis = combustiveis + this.combustiveis[i] + "\n";
		}

		String data = dados[4];
		long dataLong = Long.parseLong(data);
		Date dt = new Date(dataLong);

		this.id = Integer.parseInt(dados[5]);

		this.alertConfirmacao.addCommand(this.commandSim);
		this.alertConfirmacao.addCommand(this.commandNao);
		this.alertConfirmacao.setCommandListener(this);

		this.append(stringItemData);
		this.stringItemData.setText(Utils.data(dt));
		this.append(stringItemPlaca);
		this.stringItemPlaca.setText(this.placa);
		this.append(stringItemMarca);
		this.stringItemMarca.setText(marca);
		this.append(stringItemModelo);
		this.stringItemModelo.setText(modelo);
		this.append(stringItemCombustiveis);
		this.stringItemCombustiveis.setText(combustiveis);
		this.addCommand(this.commandAbastecer);
		this.addCommand(this.commandExcluir);
		this.addCommand(this.commandEditar);
		this.addCommand(this.commandRelatorio);
		this.addCommand(this.commandHistorico);
		this.addCommand(this.commandVoltar);
		this.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandVoltar))
			this.displayControler.voltar(2);

		else if (c.equals(this.commandExcluir))
		{
			this.alertConfirmacao.setTimeout(Alert.FOREVER);
			this.displayControler.exibirAlert(alertConfirmacao, this);
		}
		else if (c.equals(this.commandSim))
		{
			this.persistenciaRecordStore.excluirItem(PersistenciaRecordStore.recordStoremSCSCadastro, this.id);
			this.displayControler.voltar(2);
		}
		else if (c.equals(this.commandNao))
			this.displayControler.fecharAlert(this.alertConfirmacao,this);

		else if (c.equals(this.commandEditar))
		{
			EditarForm editarForm = new EditarForm(this.dados);
			this.displayControler.exibir(editarForm);
		}
		else if (c.equals(this.commandAbastecer))
		{
			AbastecimentoForm abastecimentoForm = new AbastecimentoForm(this.placa, this.combustiveis);
			this.displayControler.exibir(abastecimentoForm);
		}
		else if (c.equals(this.commandRelatorio))
		{
			RelatorioForm relatorioForm = new RelatorioForm(this.placa);
			this.displayControler.exibir(relatorioForm);
		}
		else if (c.equals(this.commandHistorico))
		{
			HistoricoForm historicoForm = new HistoricoForm(this.placa);
			this.displayControler.exibir(historicoForm);
		}
	}
}