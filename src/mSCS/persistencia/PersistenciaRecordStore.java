package mSCS.persistencia;

import java.util.Vector;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

public class PersistenciaRecordStore
{
	public static final String recordStoremSCSCadastro = "mSCSCadastro";
	public static final String recordStoremSCSAbastecimento = "mSCSAbastecimento";
	public static final int RecordStoreException = -1;
	public static final int RecordStoreFullException = -2;
	public static final int RecordStoreNotFoundException = -3;

	private RecordStore recordStore = null;
	private RecordEnumeration recordEnumeration = null;

	public int cadastrar(String nomeRecordStore, byte[] valor)
	{
		try
		{
			this.recordStore = RecordStore.openRecordStore(nomeRecordStore,true);
			int id = this.recordStore.addRecord(valor, 0, valor.length);
			this.recordStore.closeRecordStore();
			return id;
		}
		catch (RecordStoreFullException e)
		{
			return RecordStoreFullException;
		}
		catch (RecordStoreNotFoundException e)
		{
			return RecordStoreNotFoundException;
		}
		catch (RecordStoreException e)
		{
			return RecordStoreException;
		}
	}

	public boolean alterar(String nomeRecordStore, int recordId, byte[] valor)
	{
		try
		{
			this.recordStore = RecordStore.openRecordStore(nomeRecordStore, false);
			this.recordStore.setRecord(recordId, valor, 0, valor.length);
			return true;
		}
		catch(RecordStoreException e)
		{
			return false;
		}
	}

	public String[] listar(String nomeRecordStore)
	{
		try
		{
			this.recordStore = RecordStore.openRecordStore(nomeRecordStore,true);
			this.recordEnumeration = this.recordStore.enumerateRecords(null, null, true);
			int i = 0;
			String[] itens = new String[this.recordEnumeration.numRecords()];

			while (this.recordEnumeration.hasNextElement())
			{
				int id = this.recordEnumeration.nextRecordId();
				byte[] registro = this.recordStore.getRecord(id);
				itens[i] = new String(registro) + "|" + Integer.toString(id);
				i++;
			}

			this.recordStore.closeRecordStore();
			return itens;
		}
		catch (RecordStoreFullException e)
		{
			return null;
		}
		catch (RecordStoreNotFoundException e)
		{
			return null;
		}
		catch (RecordStoreException e) {
			return null;
		}
	}

	public String buscarCadastroVeiculo(String placa)
	{
		try
		{
			this.recordStore = RecordStore.openRecordStore(recordStoremSCSCadastro,true);
			this.recordEnumeration = this.recordStore.enumerateRecords(null, null, true);
			int i = 0;
			String veiculo = null;
			String placaCadastrada = null;

			while (this.recordEnumeration.hasNextElement())
			{
				int id = this.recordEnumeration.nextRecordId();
				byte[] registro = this.recordStore.getRecord(id);
				veiculo = new String(registro) + "|" + Integer.toString(id);
				placaCadastrada = veiculo.substring(6, 13);
				if(placa.equals(placaCadastrada))
					break;
				i++;
			}

			this.recordStore.closeRecordStore();
			return veiculo;
		}
		catch (RecordStoreFullException e)
		{
			return null;
		}
		catch (RecordStoreNotFoundException e)
		{
			return null;
		}
		catch (RecordStoreException e) {
			return null;
		}
	}

	public String[] listarAbastecimentosVeiculo(String placa)
	{
		try
		{
			this.recordStore = RecordStore.openRecordStore(recordStoremSCSAbastecimento,true);
			this.recordEnumeration = this.recordStore.enumerateRecords(null, null, true);
			int indiceRegistro = 0;
			String[] registros = new String[this.recordEnumeration.numRecords()];
			Vector abastecimentosVector = new Vector();
			String placaCadastrada = null;

			while (this.recordEnumeration.hasNextElement())
			{
				int id = this.recordEnumeration.nextRecordId();
				byte[] registro = this.recordStore.getRecord(id);
				registros[indiceRegistro] = new String(registro) + "|" + Integer.toString(id);
				placaCadastrada = registros[indiceRegistro].substring(6, 13);
				if(placa.equals(placaCadastrada))
					abastecimentosVector.addElement(registros[indiceRegistro]);
				indiceRegistro++;
			}

			int qtdAbastecimentos = abastecimentosVector.size();
			String[] abastecimentos = new String[qtdAbastecimentos];
			if(qtdAbastecimentos > 0)
			{
				for(int indiceAbastecimento = 0; indiceAbastecimento < qtdAbastecimentos; indiceAbastecimento++)
					abastecimentos[indiceAbastecimento] = (String)abastecimentosVector.elementAt(indiceAbastecimento);
			}
			else
				abastecimentos = null;

			this.recordStore.closeRecordStore();
			return abastecimentos;
		}
		catch (RecordStoreFullException e)
		{
			return null;
		}
		catch (RecordStoreNotFoundException e)
		{
			return null;
		}
		catch (RecordStoreException e) {
			return null;
		}
	}

	public boolean excluirItem(String nomeRecordStore, int idItem)
	{
		try
		{ 
			this.recordStore = RecordStore.openRecordStore(nomeRecordStore, false);
			this.recordStore.deleteRecord(idItem);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public boolean excluirTudo(String nomeRecordStore)
	{
		try
		{ 
			RecordStore.deleteRecordStore(nomeRecordStore);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
