package mSCS.gui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

public class ConversaoMediaForm extends Form implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandCancelar = new Command("Cancelar", Command.CANCEL, 0);

	private TextField textFieldKmPercorrida = new TextField("Km percorrida", "" , 8, TextField.DECIMAL);
	private TextField textFieldVolume = new TextField("Volume em Litros ou M³", "" , 5, TextField.DECIMAL);

	private Alert alertAtencao = new Alert("Atenção", "", null, AlertType.WARNING);

	private DisplayController displayControler = DisplayController.getInstance();

	public ConversaoMediaForm()
	{
		super("Média de consumo");
		this.append(this.textFieldKmPercorrida);
		this.append(this.textFieldVolume);
		this.addCommand(this.commandOk);
		this.addCommand(this.commandCancelar);
		this.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandOk))
		{
			String kmPercorrida = this.textFieldKmPercorrida.getString();
			String volume = this.textFieldVolume.getString();

			if(kmPercorrida.equals(""))
			{
				this.alertAtencao.setString("Preencha a KM percorrida!");
				this.displayControler.exibirAlert(alertAtencao, this);
			}
			else if(volume.equals(""))
			{
				this.alertAtencao.setString("Preencha o volume em Litros ou M³!");
				this.displayControler.exibirAlert(alertAtencao, this);
			}
			else
			{
				double kmPercorridaDouble = Double.parseDouble(kmPercorrida);
				double volumeDouble = Double.parseDouble(volume);
				double mediaDouble = kmPercorridaDouble / volumeDouble;
	
				String media = Double.toString(mediaDouble);
	
				String[] dados = {kmPercorrida,volume,media};
				ConversaoMediaResultForm formResult = new ConversaoMediaResultForm(dados);
				this.displayControler.exibir(formResult);
			}
		}
		else if (c.equals(this.commandCancelar))
			this.displayControler.voltar(1);
	}
}