import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JFrame;
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

    Font fuente;

    public void contador() {

    }

    static pingPong juego;

    public pingPong() {
        this.x = num_Aleatorio();
        System.out.println(x);
        this.pos_x = 275;
        this.pos_y = 540;
        this.ancho = 15;
        this.largo = 55;
        this.diamtero = 20;
        this.fuente = new Font("Monospaced", Font.BOLD, 20);
        this.setSize(400,500);
    }

    public int num_Aleatorio() {
        Random r = new Random();
        return r.nextInt(380) + 20;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

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

    private void movimientoPelota() {

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

        if (y == getHeight() - diamtero) {
            y = y - 1;
            pelotaHaciaAbajo = false;
        }

        if (y == diamtero) {
            y = y + 1;
            pelotaHaciaAbajo = true;
        }

        if (x == getWidth() - diamtero) {
            x = x - 1;
            pelotaHaciaDerecha = false;
        }

        if (x == 0) {
            x = x + 1;
            pelotaHaciaDerecha = true;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Crear ventana principal
        score p2 = new score();
        JFrame Ventana_Principal = new JFrame("Plantilla básica");
        Ventana_Principal.setSize(400, 600);
        Ventana_Principal.setVisible(true);
        Ventana_Principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Ventana_Principal.setLayout(new GridLayout(2,1));
        Ventana_Principal.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                String tecla = e.getKeyText(e.getKeyCode());
                System.out.println("La letra es " + tecla);

                if (tecla.equals("Left")) {
                    juego.posicionRaqueta("Left");
                } else {
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
        Ventana_Principal.add(p2);
        Ventana_Principal.add(juego);

        while (true) {
            juego.movimientoPelota();
            juego.repaint();
            p2.repaint();
            Thread.sleep(10);
        }
    }

}
