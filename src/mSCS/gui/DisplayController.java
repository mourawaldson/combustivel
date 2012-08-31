package mSCS.gui;

import java.util.Stack;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

public class DisplayController
{
	private Display display;
	private Stack pilha;
	private MIDlet midlet;

	private DisplayController()
	{
		pilha = new Stack();
	}

	private static DisplayController instance;

	public static DisplayController getInstance()
	{
		if (instance==null)
			instance = new DisplayController();

		return instance;
	}

	public void setMIDlet(MIDlet midlet)
	{
		this.midlet = midlet;
		this.display = Display.getDisplay(midlet);
	}

	public void sair()
	{
		this.midlet.notifyDestroyed();
	}

	public void exibir(Displayable displayable)
	{
		this.display.setCurrent(displayable);
		this.pilha.push(displayable);
	}

	public void exibirAlert(Alert alert,Displayable displayable)
	{
		this.display.setCurrent(alert,displayable);
	}

	public void fecharAlert(Alert alert, Displayable displayable)
	{
		this.display.setCurrent(displayable);
		alert = null;
	}

	public void voltar(int qtd)
	{
		for(int i = 0; i < qtd; i++)
			this.pilha.pop();

		this.display.setCurrent((Displayable)this.pilha.peek());
	}
}