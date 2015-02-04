/**
 * 
 */
package narovnanimapy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/**
 * @author veverka
 *
 */
public class JObdelnik extends JComponent {

  public double uhel;
  /**
   * 
   */
  private static final long serialVersionUID = -4370714043446396996L;


  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent(Graphics aG) {
    Graphics2D g = (Graphics2D) aG;
    int a = getWidth();
    int b = getHeight();

    // Nakresl�me k�� vodorovn� st�edov�
    g.drawLine(0, b/2, a, b/2);
    g.drawLine(a/2, 0, a/2, b);
    // Vyp�eme o jak� se jedn� �hel
    g.drawString((uhel / Math.PI * 180 ) + "", a/2, b/2);


    Dvoj d = spocti(uhel, a, b);
    // Vykresl�me bez transformac�
    g.setColor(Color.BLUE);
    draw(g, d);

    // Zaj�st�me, �e se bude ot��et kolem st�edu
    g.translate(a/2, b/2);
    g.setColor(Color.CYAN);
    draw(g, d);

    // zajist�me rotaci o zadan� �hel
    g.rotate(uhel);
    g.setColor(Color.MAGENTA);
    draw(g, d);
    g.drawString(d.a + " " + d.b + "", 0, 0);

  }

  /**
   * @param g
   * @param d
   */
  private void draw(Graphics2D g, Dvoj d) {
    // Nulov� k��, a� vid�me, kde je po��tek
    g.drawLine(-20, -20, 20, 20);
    g.drawLine(-20, 20, 20, -20);
    g.drawRect((int)-d.a/2, (int)-d.b/2, (int)d.a, (int)d.b);
  }

  /**
   * Spo��t� veliksot obd�ln�ku tak, aby nov� obd�ln�k
   * ve�el do obd�ln�ku dan�m zadan�mi stranami, kdy� je pooto�en
   * o zadan� �hel.
   * @param uhel
   * @param a
   * @param b
   * @return
   */
  public static Dvoj spocti(double uhel, double a, double b) {

    double q = Math.abs(Math.tan(uhel));
    double q2m = 1 - q * q;

    double a1 = (a - q * b) / q2m;
    double b1 = (b - q * a) / q2m;
    double a2 = a - a1;
    double b2 = b - b1;

    double ar = Math.hypot(a1, b2);
    double br = Math.hypot(b1, a2);

    Dvoj dvoj = new Dvoj();
    dvoj.a = ar;
    dvoj.b = br;


    return dvoj;
  }


  private static class Dvoj {
    double a;
    double b;
  }
}
