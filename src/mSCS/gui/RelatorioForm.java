package mSCS.gui;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import mSCS.persistencia.PersistenciaRecordStore;
import mSCS.utils.Utils;

public class RelatorioForm extends Form implements CommandListener
{
	private Command commandOk = new Command("Ok", Command.OK, 0);
	private Command commandCancelar = new Command("Cancelar", Command.CANCEL, 0);

	private String[] itensChoiceGroup = {Utils.GASOLINA,Utils.ALCOOL,Utils.GNV};
	private StringItem stringItemPeriodo = new StringItem("Período","");
	private DateField dateFieldDe = new DateField("de", DateField.DATE);
	private DateField dateFieldAte = new DateField("até", DateField.DATE);
	private ChoiceGroup choiceGroupCombustiveis = new ChoiceGroup("Combustível:", Choice.MULTIPLE, itensChoiceGroup, null);

	private Alert alertAtencao = new Alert("Atenção", "", null, AlertType.WARNING);

	private DisplayController displayControler = DisplayController.getInstance();
	private PersistenciaRecordStore persistenciaRecordStore = new PersistenciaRecordStore();

	private String placa = null;
	private int itensVerificacao = 0;
	private Vector abastecimentosVector = new Vector();
	private Calendar calendarRegistro = Calendar.getInstance();
	private Calendar calendarAte = Calendar.getInstance();
	private Calendar calendarDe = Calendar.getInstance();

	public RelatorioForm(String placa)
	{
		super("Relatório");
		this.placa = placa;
		this.append(this.stringItemPeriodo);
		this.append(this.dateFieldDe);
		this.append(this.dateFieldAte);
		this.append(this.choiceGroupCombustiveis);
		this.addCommand(this.commandOk);
		this.addCommand(this.commandCancelar);
		this.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.equals(this.commandOk))
		{
			Date dataDe = this.dateFieldDe.getDate();
			Date dataAte = this.dateFieldAte.getDate();
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

			String[] abastecimentos = this.persistenciaRecordStore.listarAbastecimentosVeiculo(this.placa);
			if(abastecimentos != null)
			{
				int qtdAbastecimentos = abastecimentos.length;

				if(qtdAbastecimentos > 0)
				{
					//VERIFICAÇÃO DOS PARAMETROS PREENCHIDOS PARA FILTRAR!!
					//somente dataAte está preenchido
					if(dataAte != null && dataDe == null && combustiveis.equals(""))
						this.itensVerificacao = 1;
					//somente dataDe está preenchido 
					else if (dataDe != null && dataAte == null && combustiveis.equals(""))
						this.itensVerificacao = 2;
					//somente combustiveis está preenchido
					else if(dataDe == null && dataAte == null && !combustiveis.equals(""))
						this.itensVerificacao = 3;
					//somente dataDe e dataAte estão preencidos
					else if(dataDe != null && dataAte != null && combustiveis.equals(""))
						this.itensVerificacao = 4;
					//somente dataDe e combustiveis estão preencidos
					else if(dataDe != null && dataAte == null && !combustiveis.equals(""))
						this.itensVerificacao = 5;
					//somente dataAte e combustiveis estão preenchidos
					else if(dataAte != null && dataDe == null && !combustiveis.equals(""))
						this.itensVerificacao = 6;

					//String combustivel = dadosCadastro[1].substring(12);
					
					switch (this.itensVerificacao)
					{
					case 0:
						for(int i = 0; i < qtdAbastecimentos; i++)
							this.abastecimentosVector.addElement(abastecimentos[i]);
						break;
					case 1:
						for(int i = 0; i < qtdAbastecimentos; i++)
						{
							String dadosCadastro[] = Utils.split(abastecimentos[i], "|".charAt(0));
							String dataString = dadosCadastro[6].substring(5);
							long dataLong = Long.parseLong(dataString);
							this.calendarRegistro.setTime(new Date(dataLong));
							this.calendarAte.setTime(dataAte);

							if(this.calendarRegistro.before(this.calendarAte))
								this.abastecimentosVector.addElement(abastecimentos[i]);
						}
						break;
					case 2:
						for(int i = 0; i < qtdAbastecimentos; i++)
						{
							String dadosCadastro[] = Utils.split(abastecimentos[i], "|".charAt(0));
							String dataString = dadosCadastro[6].substring(5);
							long dataLong = Long.parseLong(dataString);
							this.calendarRegistro.setTime(new Date(dataLong));
							this.calendarDe.setTime(dataDe);

							if(this.calendarRegistro.before(this.calendarDe))
								this.abastecimentosVector.addElement(abastecimentos[i]);
						}
						break;
					case 4:
						for(int i = 0; i < qtdAbastecimentos; i++)
						{
							String dadosCadastro[] = Utils.split(abastecimentos[i], "|".charAt(0));
							String dataString = dadosCadastro[6].substring(5);
							long dataLong = Long.parseLong(dataString);
							this.calendarRegistro.setTime(new Date(dataLong));
							this.calendarDe.setTime(dataDe);
							this.calendarAte.setTime(dataAte);

							if(this.calendarRegistro.after(this.calendarDe) && this.calendarRegistro.before(this.calendarAte))
								this.abastecimentosVector.addElement(abastecimentos[i]);
						}
						break;
					}

					int qtdAbastecimentosFiltrados = this.abastecimentosVector.size();
					if(qtdAbastecimentosFiltrados > 0)
					{
						String relatorio[] = new String[qtdAbastecimentosFiltrados];
						for(int j = 0; j < qtdAbastecimentosFiltrados; j++)
							relatorio[j] = (String)this.abastecimentosVector.elementAt(j);

						RelatorioResultForm relatorioResultForm = new RelatorioResultForm(relatorio);
						this.displayControler.exibir(relatorioResultForm);
					}
					else
					{
						this.alertAtencao.setString("Nenhum registro encontrado com esse filtro!");
						this.displayControler.exibirAlert(alertAtencao, this);
					}
				}
				else
				{
					this.alertAtencao.setString("Nenhum registro encontrado!");
					this.displayControler.exibirAlert(alertAtencao, this);
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