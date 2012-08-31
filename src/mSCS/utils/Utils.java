package mSCS.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class Utils
{
	public static final String GASOLINA = "Gasolina";
	public static final String GASOLINA_UPPER = "GASOLINA";
	public static final String ALCOOL = "Álcool";
	public static final String ALCOOL_UPPER = "ÁLCOOL";
	public static final String GNV = "GNV";

	public final static String[] split( String texto, char separador )
	{
		if (texto == null)
			return null;

		int tamanhoTexto = texto.length();
		if(tamanhoTexto == 0)
			return null;

		Vector lista = new Vector();
		int i = 0;
		int start = 0;
		boolean permite = false;
		while(i < tamanhoTexto)
		{
			if(texto.charAt(i) == separador)
			{
				if(permite)
				{
					lista.addElement(texto.substring(start, i).trim());
					permite = false;
				}
				start = ++i;
				continue;
			}
			permite = true;
			i++;
		}
		if(permite)
			lista.addElement(texto.substring( start, i).trim());

		String[] listaElementos = new String[lista.size()];
		lista.copyInto(listaElementos);
		return listaElementos;
	}

	public static final String data(Date data)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		StringBuffer sb = new StringBuffer();

		int day = calendar.get(Calendar.DAY_OF_MONTH);
		sb.append(Utils.numberToString(day));
		sb.append("/");
		int month = calendar.get(Calendar.MONTH) + 1;
		sb.append(Utils.numberToString(month));
		sb.append("/");
		sb.append(calendar.get(Calendar.YEAR));

		return sb.toString();
	}

	public static final String numberToString(int value)
	{
		String valStr = Integer.toString(value);
		return (value < 10) ? "0" + valStr: valStr;
    }
}