/*
Copyright (c) 2008~2009, Justin R. Bengtson (poopshotgun@yahoo.com)
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
        this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.
    * Neither the name of Justin R. Bengtson nor the names of contributors may
        be used to endorse or promote products derived from this software
        without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package Print;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import battleforce.BattleForce;
import battleforce.BattleForceStats;
import filehandlers.ImageTracker;
import filehandlers.Media;
import java.awt.Font;
import java.awt.Point;

public class QSVerticalCardPrinter implements Printable {
    private BattleForce battleforce;
    private Media media = new Media();
    private ImageTracker imageTracker;
    private Graphics2D graphic;
    private Image   Background, CardBack;
    private Image[] BFIcons;
    private int UnitImageWidth = PaperSize.getInchesInPixels(2.5),
                UnitImageHeight = PaperSize.getInchesInPixels(3.5),
                HorizontalLimit = 4,
                MaxUnits = 8;

    private boolean printMechs = true,
                    printLogo = true,
                    useTerrainMod = false,
                    printCardBack = false;

    private int x = 0,
                y = 0;

    private Color   Shadow = Color.DARK_GRAY,
                    DarkShadow = Color.BLACK,
                    OVColor = new Color(204, 0, 0),
                    PVColor = Color.WHITE,
                    MoveColor = Color.ORANGE,
                    SkillColor = new Color(238, 216, 0),
                    NameColor = Color.ORANGE,
                    PilotColor = Color.ORANGE,
                    SizeColor = new Color(242, 242, 242);



    public QSVerticalCardPrinter( BattleForce f, ImageTracker images) {
        battleforce = f;
        imageTracker = images;
        Background = imageTracker.getImage( PrintConsts.COLOR_VERT_QS_CARD );
        CardBack = imageTracker.getImage( PrintConsts.COLOR_VERT_QS_CARD_BACK );
        BFIcons = new Image[]{ imageTracker.getImage( PrintConsts.BF_ICON_INDUSTRIAL ),
                        imageTracker.getImage( PrintConsts.BF_ICON_LIGHT ),
                        imageTracker.getImage( PrintConsts.BF_ICON_MEDIUM ),
                        imageTracker.getImage( PrintConsts.BF_ICON_HEAVY ),
                        imageTracker.getImage( PrintConsts.BF_ICON_ASSAULT ) };
    }

    public QSVerticalCardPrinter(ImageTracker images) {
        this(new BattleForce(), images);
    }

    public void Add( BattleForceStats stat ) {
        getBattleforce().BattleForceStats.add(stat);
    }

    public void setCardBack( boolean Value ) {
        printCardBack = Value;
    }

    public void setBlackAndWhite() {
        Background = imageTracker.getImage( PrintConsts.BW_VERT_QS_CARD );
        CardBack =imageTracker.getImage( PrintConsts.BW_VERT_QS_CARD_BACK );
        OVColor = Color.BLACK;
        PVColor = OVColor;
        MoveColor = OVColor;
        SkillColor = OVColor;
        NameColor = OVColor;
        PilotColor = OVColor;
        SizeColor = OVColor;
        Shadow = Color.WHITE;
        DarkShadow = Color.WHITE;

        BFIcons = new Image[]{ imageTracker.getImage( PrintConsts.BF_ICON_INDUSTRIAL_BW ),
                        imageTracker.getImage( PrintConsts.BF_ICON_LIGHT_BW ),
                        imageTracker.getImage( PrintConsts.BF_ICON_MEDIUM_BW ),
                        imageTracker.getImage( PrintConsts.BF_ICON_HEAVY_BW ),
                        imageTracker.getImage( PrintConsts.BF_ICON_ASSAULT_BW ) };
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        //if( RecordSheet == null) { return Printable.NO_SUCH_PAGE; }
        x = 25;
        y = 22;
        ((Graphics2D) graphics).translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
        graphic = (Graphics2D) graphics;
        //setBlackAndWhite();
        Render();
        return Printable.PAGE_EXISTS;
    }
    
    public void Render() {
        int elementCount = 0;

        for ( BattleForceStats stats : getBattleforce().BattleForceStats ) {
            if ( elementCount == HorizontalLimit ) {
                //if ( !printCardBack ) elementCount = 0;
                x = 25;
                y += UnitImageHeight;
            }
            elementCount += 1;

            printCardFront( stats );
            x += UnitImageWidth + 2;
        }

        if ( printCardBack ) {
            x = 25;
            y += UnitImageHeight;


            for ( BattleForceStats stats : getBattleforce().BattleForceStats ) {
                printCardBack( stats );
                x += UnitImageWidth + 2;
            }
        }

        graphic.setFont( PrintConsts.RegularFont );
    }
    
    private void printCardBack( BattleForceStats stats ) {
        Point p = new Point(0,0);

        graphic.drawImage( CardBack, x, y, UnitImageWidth, UnitImageHeight, null);

        //Unit Name
        ShadowText( PrintConsts.SmallBoldFont, NameColor, DarkShadow, stats.getModel(), x+16, y+48);
        p.y = y + 58;
        for ( String line : PrintConsts.wrapText(stats.getName().toUpperCase(), 12, false) ) {
            ShadowText( PrintConsts.BoldFont, NameColor, DarkShadow, line, x+16, p.y);
            p.y += graphic.getFont().getSize();
        }

        graphic.drawImage( BFIcons[stats.getWeight()], x+112, y+40, 54, 28, null);

        graphic.setFont(PrintConsts.SmallBoldFont);
        graphic.drawString("Notes:", x+20, y+82);
    }

    private void printCardFront( BattleForceStats stats ) {
        Point p = new Point(0,0);

        //Image
        if ( !stats.getImage().isEmpty() && printMechs ) {
            p.x = 16;
            p.y = 43;
            Image image = imageTracker.getImage(stats.getImage());
            Dimension dim = media.reSize(image, 85d, 128d);
            image.getScaledInstance(dim.width, dim.height, Image.SCALE_SMOOTH);
            Point offset = media.offsetImageCenter( new Dimension(85, 128), dim);
            graphic.drawImage(image, x+p.x+offset.x, y+p.y+offset.y, dim.width, dim.height, null);
        }

        graphic.drawImage( Background, x, y, UnitImageWidth, UnitImageHeight, null);

        //Overheat (OV)
        ShadowText( PrintConsts.OVFont, OVColor, Shadow, stats.getOverheat()+"", x+122, y+55);

        //PV
        ShadowText( PrintConsts.BoldFont, PVColor, DarkShadow, stats.getPointValue()+"", x+128, y+14);

        //Unit Name
        ShadowText( PrintConsts.SmallBoldFont, NameColor, DarkShadow, stats.getModel(), x+4, y+10);
        p.y = y + 20;
        for ( String line : PrintConsts.wrapText(stats.getName().toUpperCase(), 12, false) ) {
            ShadowText( PrintConsts.BoldFont, NameColor, DarkShadow, line, x+4, p.y);
            p.y += graphic.getFont().getSize();
        }

        //Pilot Name
        ShadowText( PrintConsts.XtraSmallBoldFont, PilotColor, DarkShadow, stats.getWarrior(), x+4, p.y-4);

        p.y = 208;

        //Movement (MV)
        p.x = x + 21;
        if ( stats.getMovement(useTerrainMod).length() > 1 ) p.x -= stats.getMovement(useTerrainMod).length() * 2;
        ShadowText( PrintConsts.OVFont, MoveColor, Shadow, stats.getMovement(useTerrainMod), p.x, y+p.y );

        //Skill
        ShadowText( PrintConsts.OVFont, SkillColor, Shadow, stats.getSkill()+"", x+42, y+p.y);

        //Weight Class
        ShadowText( PrintConsts.OVFont, SizeColor, Shadow, stats.getWeight()+"", x+62, y+p.y);

        int[] data = {78, 93, 114, 135, 154};
        p.y = 201;

        //Damage Values (S,M,L,E)
        ShadowText( PrintConsts.SmallBoldFont, PVColor, Shadow, stats.getShort()+"", x+data[1], y+p.y);
        ShadowText( PrintConsts.SmallBoldFont, PVColor, Shadow, stats.getMedium()+"", x+data[2], y+p.y);
        ShadowText( PrintConsts.SmallBoldFont, PVColor, Shadow, stats.getLong()+"", x+data[3], y+p.y);
        ShadowText( PrintConsts.SmallBoldFont, PVColor, Shadow, stats.getExtreme()+"", x+data[4], y+p.y);
        p.y += graphic.getFont().getSize();

        for ( String[] ability : stats.getDamageAbilities() ) {
            ShadowText( PrintConsts.XtraSmallBoldFont, PVColor, Shadow, ability[0]+"", x+data[0], y+p.y);
            ShadowText( PrintConsts.XtraSmallBoldFont, PVColor, Shadow, ability[1]+"", x+data[1]+1, y+p.y);
            ShadowText( PrintConsts.XtraSmallBoldFont, PVColor, Shadow, ability[2]+"", x+data[2]+1, y+p.y);
            ShadowText( PrintConsts.XtraSmallBoldFont, PVColor, Shadow, ability[3]+"", x+data[3]+1, y+p.y);
            ShadowText( PrintConsts.XtraSmallBoldFont, PVColor, Shadow, ability[4]+"", x+data[4]+1, y+p.y);
            p.y += graphic.getFont().getSize();
        }

        //Armor
        p.setLocation(120, 65);
        int indexer = 0;
        for ( int a=0; a < stats.getArmor(); a++ ) {
            if ( indexer == 5 ) { p.setLocation(120, p.y+9); indexer = 0; }
            FilledCircle( Color.BLACK, Color.WHITE, 8, x+p.x, y+p.y);
            indexer += 1;
            p.x += 9;
        }

        //Internal Structure
        indexer = 0;
        p.setLocation(120, p.y+10);
        for ( int s=0; s < stats.getInternal(); s++ ) {
            if ( indexer == 5 ) { p.setLocation(120, p.y+9); indexer = 0; }
            FilledCircle( Color.BLACK, Color.LIGHT_GRAY, 8, x+p.x, y+p.y);
            indexer += 1;
            p.x += 9;
        }

        //Abilities
        p.setLocation(120, 130);
        graphic.setFont( PrintConsts.XtraSmallFont );
        for ( String ability : stats.getFilteredAbilities() ) {
            //ShadowText( PrintConsts.XtraSmallFont, Color.BLACK, Color.LIGHT_GRAY, ability, x+p.x, y+p.y);
            graphic.drawString(ability, x+p.x, y+p.y);
            p.y += graphic.getFont().getSize();
        }
    }

    public void ShadowText( Font font, Color foreColor, Color backColor, String Text, int X, int Y ) {
        graphic.setFont(font);
        graphic.setColor(backColor);
        graphic.drawString(Text, X, Y+1);
        graphic.setColor(foreColor);
        graphic.drawString(Text, X, Y);
        graphic.setColor(Color.BLACK);
    }

    public void FilledCircle( Color foreColor, Color backColor, int Size, int X, int Y ) {
        graphic.setColor(backColor);
        graphic.fillOval(X, Y, Size, Size);
        graphic.setColor(foreColor);
        graphic.drawOval(X, Y, Size, Size);
        graphic.setColor(Color.BLACK);
    }

    public BattleForce getBattleforce() {
        return battleforce;
    }

    public void setBattleforce(BattleForce battleforce) {
        this.battleforce = battleforce;
    }

    public void setPrintMechs(boolean printMechs) {
        this.printMechs = printMechs;
    }

    public void setPrintLogo(boolean printLogo) {
        this.printLogo = printLogo;
    }

    public boolean UseTerrain() {
        return useTerrainMod;
    }

    public void setTerrain(boolean useTerrainMod) {
        this.useTerrainMod = useTerrainMod;
    }
}
