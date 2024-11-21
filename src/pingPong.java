import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class pingPong extends JPanel { // Cambiado de JFrame a JPanel

    private int x;
    private int y;

    private int pos_x;
    private int pos_y;
    private final int ancho;
    private final int largo;

    private final int diamtero;

    private boolean pelotaHaciaDerecha = true;
    private boolean pelotaHaciaAbajo = true;
    private ImageIcon imagenFondo;
    static pingPong juego;

    Font fuente;
    private int score;

    private String s = "Puntuacion: ";
    private static long velocidad = 10;

    public pingPong() {
        this.x = num_Aleatorio();
        System.out.println(x);
        this.pos_x = 275;
        this.pos_y = 540;
        this.ancho = 15;
        this.largo = 55;
        this.diamtero = 20;
        this.fuente = new Font("Monospaced", Font.BOLD, 15);
        this.imagenFondo = new ImageIcon("Assets/fondo2.gif");
        this.setSize(400, 600);
    }

    public boolean pegoEnRaqueta() {
        if (x >= pos_x && x <= pos_x + largo) {
            // System.out.println("si pego");
            score = score + 10;
            return true;
        }
        return false;
    }

    public static void audio(int eleccion) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        switch (eleccion) {
            case 1: {
                AudioInputStream audio = AudioSystem
                        .getAudioInputStream((new File("Assets/musica.wav")).getAbsoluteFile());
                Clip sonido = AudioSystem.getClip();
                sonido.open(audio);
                sonido.start();
                sonido.loop(-1);
                break;
            }
            case 2: {
                AudioInputStream colision = AudioSystem
                        .getAudioInputStream((new File("Assets/colision.wav")).getAbsoluteFile());
                Clip sonido = AudioSystem.getClip();
                sonido.open(colision);
                sonido.start();
                break;
            }
            case 3: {
                AudioInputStream perdiste = AudioSystem
                        .getAudioInputStream((new File("Assets/muerte.wav")).getAbsoluteFile());
                Clip sonido = AudioSystem.getClip();
                sonido.open(perdiste);
                sonido.start();
                break;
            }
            default:
                break;
        }

    }

    public int num_Aleatorio() {
        Random r = new Random();
        return r.nextInt(360) + 20;
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);
        g.drawImage(imagenFondo.getImage(), 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.WHITE);

        g.setFont(fuente);
        g.drawString(s + score, 250, 10);
        // g.drawLine(0, pos_y, 400, pos_y);
        g.fillRect(pos_x, pos_y, largo, ancho);
        g.fillOval(x, y, diamtero, diamtero);
    }

    public void posicionRaqueta(String op) {

        if (op.equals("Left") && pos_x > 5) {
            pos_x = pos_x - 15;
        } else if (op.equals("Right") && pos_x < 530) {
            pos_x = pos_x + 15;
        }
    }

    private void movimientoPelota() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        if (pelotaHaciaDerecha) {
            x = x + 1;
        } else {
            x = x - 1;
        }

        if (pelotaHaciaAbajo) {
            y = y + 1;
        } else {
            y = y - 1;
        }

        if (y == (pos_y - diamtero)) {
            if (pegoEnRaqueta()) {
                y = y - 1;
                pelotaHaciaAbajo = false;
                pingPong.audio(2);
                if (score >= 10) {
                    velocidad = (long) (velocidad - score * (0.025));
                }
            } else {
                JFrame ventanaPerdida = new JFrame("Ya perdiste pipipi");
                JLabel imagen = new JLabel();
                ImageIcon img = new ImageIcon("Assets/gameover.gif");
                imagen.setIcon(img);
                ventanaPerdida.setSize(300, 120);
                ventanaPerdida.setLocationRelativeTo(this);
                ventanaPerdida.add(imagen);
                ventanaPerdida.setVisible(true);
                System.out.println("perdiste");
                velocidad = 80;
                audio(3);
            }
        }

        if (y == diamtero) {
            y = y + 1;
            pelotaHaciaAbajo = true;
            pingPong.audio(2);
        }

        if (x == getWidth() - diamtero) {
            x = x - 1;
            pelotaHaciaDerecha = false;
            pingPong.audio(2);
        }

        if (x == 0) {
            x = x + 1;
            pelotaHaciaDerecha = true;
            pingPong.audio(2);
        }
    }

    public static void main(String[] args)
            throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        // Crear ventana principal
        JFrame Ventana_Principal = new JFrame("Ping Pong");
        Ventana_Principal.setSize(400, 600);
        Ventana_Principal.setVisible(true);
        Ventana_Principal.setResizable(false);
        Ventana_Principal.setLocationRelativeTo(null);
        Ventana_Principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Ventana_Principal.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                String tecla = e.getKeyText(e.getKeyCode());
                // System.out.println("La letra es " + tecla);

                if (tecla.equals("Left") || tecla.equals("A")) {
                    juego.posicionRaqueta("Left");
                } else if (tecla.equals("Right") || tecla.equals("D")) {
                    juego.posicionRaqueta("Right");
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

        });

        juego = new pingPong();
        Ventana_Principal.add(juego);

        try {
            audio(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        while (true) {
            juego.movimientoPelota();
            juego.repaint();
            Thread.sleep(velocidad);
        }
    }

}
