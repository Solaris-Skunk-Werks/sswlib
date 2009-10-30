/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Print;

import Force.Scenario;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.print.*;

public class ScenarioPrinter implements Printable {
    public Graphics2D Graphic;
    private Scenario scenario = null;
    private PageFormat format = null;
    private String Title = "Scenario Information";
    private int characterWidth = 135,
                characterHalfWidth = 68,
                pageWidth = 0,
                pageHalfWidth = 0;
    private Point currentLocation = new Point(0, 0),
                    savePoint = new Point(0, 0);

    public ScenarioPrinter() {

    }

    public ScenarioPrinter( Scenario scenario ) {
        this.scenario = scenario;
    }

    public void SetScenario( Scenario scenario ) {
        this.scenario = scenario;
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if( scenario == null ) { return Printable.NO_SUCH_PAGE; }
        Graphic = (Graphics2D) graphics;
        format = pageFormat;
        pageWidth = (int) pageFormat.getImageableWidth();
        pageHalfWidth = pageWidth / 2;
        Reset();
        Graphic.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
        PreparePrint();
        return Printable.PAGE_EXISTS;
    }

    private void PreparePrint() {
        Reset();

        Graphic.setFont( PrintConsts.TitleFont);
        Graphic.drawString(scenario.getName(), currentLocation.x, currentLocation.y);
        currentLocation.y += Graphic.getFont().getSize();

        RenderTitle("SITUATION");
        RenderText( scenario.getSituation(), characterWidth );

        savePoint.setLocation(currentLocation);
        RenderTitle("GAME SETUP");
        RenderText( scenario.getSetup(), characterHalfWidth );

        currentLocation.setLocation(savePoint.x + pageHalfWidth, savePoint.y);
        RenderTitle("Attacker");
        RenderText( scenario.getAttacker(), characterHalfWidth );

        currentLocation.x = (int) format.getImageableX();
        savePoint.setLocation(currentLocation);
        RenderTitle("AFTERMATH");
        RenderText( scenario.getAftermath(), characterHalfWidth );

        currentLocation.setLocation(savePoint.x + pageHalfWidth, savePoint.y);
        RenderTitle("Defender");
        RenderText( scenario.getDefender(), characterHalfWidth );
    }

    private void RenderTitle( String title) {
        Graphic.setFont( PrintConsts.BoldFont );
        Graphic.drawString(title, currentLocation.x, currentLocation.y);
        currentLocation.y += Graphic.getFont().getSize();
    }
    private void RenderText( String text, int Width ) {
        Graphic.setFont( PrintConsts.PlainFont );
        String[] formattedText = PrintConsts.wrapText(text, Width, true);
        for ( String line : formattedText ) {
            Graphic.drawString(line, currentLocation.x, currentLocation.y);
            currentLocation.y += Graphic.getFont().getSize();
        }
        currentLocation.y += 10;
    }

    public void Reset() {
        currentLocation.setLocation((int) format.getImageableX(), (int) format.getImageableY());
    }
}
