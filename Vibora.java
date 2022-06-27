import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.*;

class Vibora{

	public int width = 600;
	public int height = 400;
	
	private Point snake; // Coordenadas de la vibora
	private Point comida;
	private int widthPoint = 10;
	private int heightPoint = 10;

	private int velocidad = 70;

	Graficos juego;
	ImagenVibora imageSnake;
	BotonRestart boton;
	MovimientoVibora movimiento;
	Teclado teclado;
	ArrayList<Point> colaVibora;

	private int puntos;
	private boolean pausa = false;
	private int direccion = 0;
	private boolean pintPausa;
	private JLabel labelPausa;

	public boolean gameOver = false;
	public MouseListener mouse;

	Clip theme;

	public Vibora(){
		cargaVentana();
		cargaVibora();
		cargarTemaPrincipal();
		iniciarJuego();
		actualizarVibora();
	}

	private void cargaVentana(){
		juego = new Graficos(this.width, this.height);
		juego.nuevoJuego.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				reiniciarVariables();
			}
		});
	}

	public void cargarTemaPrincipal(){
		try{
			AudioInputStream clip0 = AudioSystem.getAudioInputStream(new File("Audios/Theme.wav"));
			theme = AudioSystem.getClip();
			theme.open(clip0);
		}catch(Exception e){}
	}

	public void cargarSonidos(String sonido){
		try{
			if(sonido.equals("food")){
				BufferedInputStream clip1 = new BufferedInputStream(getClass().getResourceAsStream("Audios/food.wav"));
				Clip eat = AudioSystem.getClip();
				eat.open(AudioSystem.getAudioInputStream(clip1));
				eat.start();
			}

			if(sonido.equals("gameOver")){
				BufferedInputStream clip2 = new BufferedInputStream(getClass().getResourceAsStream("Audios/GameOver.wav"));
				Clip game_over = AudioSystem.getClip();
				game_over.open(AudioSystem.getAudioInputStream(clip2));
				game_over.start();
			}
			
		}catch(Exception e){}
	}

	private void cargaVibora(){
		snake = new Point(width/2, height/2); // Se coloca el punto de la serpiente a la mitad del frame
		comida = new Point();
		imageSnake = new ImagenVibora();
		imageSnake.setBounds(0,0, width, height);

		boton = new BotonRestart();
		boton.setBounds((width/2)-100, 175, (width/2)-100, (height/2)-100);
		accionMouse();
		boton.addMouseListener(mouse);
		//boton.setVisible(false);
		
		colaVibora= new ArrayList<Point>();
		colaVibora.add(snake);
		crearComida();

		// Mensaje de pausa
		labelPausa = new JLabel("Pausa", SwingConstants.CENTER);
		labelPausa.setForeground(new Color(210,205,19));
		labelPausa.setFont(new Font("consolas", 1,20));
		labelPausa.setVisible(false);
		imageSnake.add(labelPausa);

		juego.add(imageSnake);
		juego.add(boton);
	}

	public void crearComida(){
		Random random = new Random();
		comida.x = random.nextInt(width-100) + 50;
		if(comida.x % 10 > 0){
			comida.x = comida.x - (comida.x % 10);
		}
		comida.y = random.nextInt(height-100) + 50;
		if(comida.y % 10 > 0){
			comida.y = comida.y - (comida.y % 10);
		}

	}

	private void iniciarJuego(){
		juego.setVisible(true);
	}

	private void actualizarVibora(){
		movimiento = new MovimientoVibora();
		teclado = new Teclado();
		juego.addKeyListener(teclado);
		Thread hilo = new Thread(movimiento);
		hilo.start();
	}

	public void repintar(){
		imageSnake.repaint();
		
		colaVibora.add(0, new Point(snake.x, snake.y));
		colaVibora.remove(colaVibora.size()-1);

		for(int i = 1; i < colaVibora.size(); i++){
			Point punto = colaVibora.get(i);
			if(snake.x == punto.x && snake.y == punto.y){
				gameOver = true;
			}
		}
		
		if((snake.x == comida.x) && (snake.y == comida.y)){
			colaVibora.add(0, new Point(snake.x, snake.y));
			crearComida();
			puntos++;
			cargarSonidos("food");
			
		} 
	}

	public void reiniciarVariables(){
		puntos = 0;
		direccion = 0;
		colaVibora.clear();
		snake.x = width/2;
		snake.y = height/2;
		colaVibora.add(snake);
	}

	public class ImagenVibora extends JPanel{
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);

			// Pintar vibora
			g.setColor(Color.blue);
			for(int i = 0; i < colaVibora.size(); i++){
				Point point = (Point)colaVibora.get(i);
				g.fillRect(point.x, point.y, widthPoint, heightPoint);
			}
			
			// Pintar comida
			g.setColor(Color.red);
			g.fillRect(comida.x, comida.y, widthPoint, heightPoint);

			// Pintar puntos
			g.setColor(new Color(210,205,19));
			g.setFont(new Font("consolas",1,15));
			g.drawString("Puntos: "+puntos,10, 20);
			
			// Pintar GameOver
			if(gameOver){
				g.setColor(new Color(210,205,19));
				g.setFont(new Font("consolas",1,50));
				g.drawString("GAME OVER",147, 150);

				boton.repaint();
			}
		}
	}

	public class BotonRestart extends JPanel{
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			g.setColor(Color.yellow);
			g.fillRect(0,0,(width/2)-100, (height/2)-100);
			g.setColor(Color.white);
			g.setFont(new Font("consolas",1,50));
			g.drawString("Play",45 ,70) ;
		}
	}

	public void accionMouse(){
		mouse = new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e){
				if(gameOver){
					reiniciarVariables();
					movimiento.restarGame();
				}
			}

			@Override
			public void mousePressed(MouseEvent e){
		
			}

			@Override
			public void mouseReleased(MouseEvent e){

			}
			
			@Override
			public void mouseEntered(MouseEvent e){

			}

			@Override 
			public void mouseExited(MouseEvent e){

			}
		};
	}

	public void pintarPausa(){
		if(pintPausa){
			labelPausa.setVisible(true);
		}else{
			labelPausa.setVisible(false);
		}
	}

	public class MovimientoVibora extends Thread{

		public void run(){
			puntos = 0;
			long last = 0;
		
			while(true){

				theme.loop(Clip.LOOP_CONTINUOUSLY);
				
				if(pausa){
					synchronized(this){
						try{
							wait();
						}catch(InterruptedException e){}
					}
				}
				if (gameOver){
					cargarSonidos("gameOver");
					synchronized(this){
						try{
							wait();
						}catch(InterruptedException e){}
					}
				}					
				
				if((java.lang.System.currentTimeMillis() - last) > velocidad){
					if(direccion == KeyEvent.VK_UP){
						snake.y = snake.y - heightPoint;
						if(snake.y < 0){
							snake.y = height - heightPoint;
						}
					}
					else if(direccion == KeyEvent.VK_DOWN){
						snake.y = snake.y + heightPoint;
						if(snake.y == height){
							snake.y = 0;
						}
					}
					else if(direccion == KeyEvent.VK_RIGHT){
						snake.x = snake.x + widthPoint;
						if(snake.x == width){
							snake.x = 0;
						}
					}
					else if(direccion == KeyEvent.VK_LEFT){
						snake.x = snake.x - widthPoint;
						if(snake.x < 0){
							snake.x = width - widthPoint;
						}
					}
					repintar();
					last = 	java.lang.System.currentTimeMillis();
				}
			}
		}

		public synchronized void resumeGame(){
			pausa = false;
			notify();
		}

		public synchronized void restarGame(){
			gameOver = false;
			notify();
		}
	}

	public class Teclado extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				System.exit(0);
			}
			else if(e.getKeyCode() == KeyEvent.VK_UP){
				if(direccion != KeyEvent.VK_DOWN){
					direccion = KeyEvent.VK_UP;
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN){
				if(direccion != KeyEvent.VK_UP){
					direccion = KeyEvent.VK_DOWN;
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				if(direccion != KeyEvent.VK_LEFT){
					direccion = KeyEvent.VK_RIGHT;
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_LEFT){
				if(direccion != KeyEvent.VK_RIGHT){
					direccion = KeyEvent.VK_LEFT;
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_ENTER){
				if(!pausa){
					pintPausa = true;
					pausa = true;
					pintarPausa();
				}
				else{
					pintPausa = false;
					pintarPausa();
					movimiento.resumeGame();
				}
			}

		}
	}

}