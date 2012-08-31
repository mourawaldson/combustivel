package mSCS.gui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import mSCS.persistencia.PersistenciaRecordStore;
import mSCS.utils.Utils;

public class AbastecimentoConfirmForm extends Form implements CommandListener
{
	private Command commandVoltar = new Command("Voltar", Command.CANCEL, 0);
	private Command commandConfirmar = new Command ("Confirmar", Command.OK, 1);

	private StringItem stringItemPlaca = new StringItem("Placa", "");
	private StringItem stringItemCombustivel = new StringItem("Combustível", "");
	private StringItem stringItemPrecoCombustivel = new StringItem("Preço do combustível", "");
	private StringItem stringItemKMAtual = new StringItem("KM atual", "");

	private Alert alertInfo = new Alert("Atenção", "", null, AlertType.INFO);
	private Alert alertErro = new Alert("Erro", "", null, AlertType.ERROR);

	private DisplayController displayControler = DisplayController.getInstance();
	private PersistenciaRecordStore persistenciaRecordStore = new PersistenciaRecordStore();

	private String dados = null;

	public AbastecimentoConfirmForm(String[] dados)
	{
		super("Confirmação do abastecimento");
		String placa = dados[0];
		String combustivel = dados[1];
		String medida = "";
		medida = (!combustivel.equals(Utils.GNV)) ? "litros" : "m³";

		String precoCombustivel = dados[2];
		String kmAtual = dados[3];
		String tipoAbastecimento = dados[4].toUpperCase();
		String valorVolume = dados[5];

		double precoCombustivelDouble = Double.parseDouble(precoCombustivel);
		double valorVolumeDouble = Double.parseDouble(valorVolume);

		//String data = Utils.data(new Date());
		long data = System.currentTimeMillis();
		String dataString = Long.toString(data);

		this.append(stringItemPlaca);
		this.stringItemPlaca.setText(placa);
		this.append(stringItemCombustivel);
		this.stringItemCombustivel.setText(combustivel);
		this.append(stringItemPrecoCombustivel);
		this.stringItemPrecoCombustivel.setText(precoCombustivel);
		this.append(stringItemKMAtual);
		this.stringItemKMAtual.setText(kmAtual + "\n");

		if(tipoAbastecimento.equals("VALOR"))
		{
			double volumeTotalDouble = valorVolumeDouble / precoCombustivelDouble;
			String volumeTotal = Double.toString(volumeTotalDouble);
			StringItem stringItemValor = new StringItem("",
					"Com R$ " + valorVolume + " você abastece " + volumeTotal
					+ " " + medida + " de " + combustivel.toLowerCase() );
			this.append(stringItemValor);
		}
		else if (tipoAbastecimento.equals("VOLUME"))
		{
			double valorTotalDouble = valorVolumeDouble * precoCombustivelDouble;
			String valorTotal = Double.toString(valorTotalDouble);
			StringItem stringItemVolume = new StringItem("",
					"Abastecendo " + valorVolume + " " + medida + " de "
					+ combustivel + " o valor a pagar será R$ " + valorTotal);
			this.append(stringItemVolume);
		}

		this.addCommand(this.commandConfirmar);
		this.addCommand(this.commandVoltar);
		this.setCommandListener(this);

		this.dados = "PLACA:" + placa + "|COMBUSTIVEL:" + combustivel + "|PRECO_COMBUSTIVEL:" + precoCombustivel
		+ "|TIPO_ABASTECIMENTO:" + tipoAbastecimento + "|VALOR_VOLUME:" + valorVolume
		+ "|KM_ATUAL:" + kmAtual +"|DATA:" + dataString;
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandConfirmar))
		{
			int idRetorno = this.persistenciaRecordStore.cadastrar(PersistenciaRecordStore.recordStoremSCSAbastecimento, dados.getBytes());
			if(idRetorno > 0)
			{
				this.alertInfo.setString("Abastecido com sucesso!");
				this.displayControler.exibirAlert(alertInfo,this);
				this.displayControler.voltar(2);
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
		else if (c.equals(this.commandVoltar))
			this.displayControler.voltar(1);
	}
}