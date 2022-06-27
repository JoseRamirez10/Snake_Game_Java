/*
	Carga la ventana principal, el panel y el menu
	de la interfaz
*/

import javax.swing.*;
import java.awt.event.*;

class Graficos extends JFrame{

	public JPanel panelPrincipal;
	public JMenuItem nuevoJuego;

	public Graficos(int width, int height){
		init(width, height);
	}

	private void init(int width, int height){
		creaVentana(width, height);
		creaBarMenu();
	}

	private void creaVentana(int width, int height){
		setTitle("Vibora");
		setSize(width, height);
		setResizable(false);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void creaBarMenu(){
		JMenuBar main = new JMenuBar();
		setJMenuBar(main);
		
		JMenu menu = new JMenu("Juego");
		main.add(menu);

		nuevoJuego = new JMenuItem("Nuevo Juego");
		menu.add(nuevoJuego);

		menu.addSeparator();

		JMenuItem salir = new JMenuItem("Salir");
		menu.add(salir);

		// Eventos
		salir.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
	}
}