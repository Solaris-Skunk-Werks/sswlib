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

public class QuickStrikeCardPrinter implements Printable {
    private BattleForce battleforce;
    private Media media = new Media();
    private ImageTracker imageTracker;
    private Graphics2D graphic;
    private Image RecordSheet,
                    Unit,
                    Charts;
    private int UnitSize = 4,
                UnitImageWidth = 252,
                UnitImageHeight = 180,
                ElementLimit = 2;
    private boolean printMechs = true,
                    printLogo = true;

    private int x = 0,
                y = 0;

    public QuickStrikeCardPrinter( BattleForce f, ImageTracker images) {
        battleforce = f;
        imageTracker = images;
        //RecordSheet = images.getImage( PrintConsts.QS_Card );
        Unit = images.getImage( PrintConsts.QS_Card );
        //Charts = images.getImage( PrintConsts.BF_Chart );
        setType(battleforce.Type);
    }

    public QuickStrikeCardPrinter(ImageTracker images) {
        this(new BattleForce(), images);
    }

    public void Add( BattleForceStats stat ) {
        getBattleforce().BattleForceStats.add(stat);
    }

    public void setRecordSheet( String sheet ) {
        RecordSheet = imageTracker.getImage(sheet);
    }
    
    public void setUnitSheet( String item ) {
        Unit = imageTracker.getImage( item );
    }

    public void setType( String Type ) {
        if ( Type.equals(BattleForce.InnerSphere) ) {
            setInnerSphere();
        } else if ( Type.equals(BattleForce.Clan) ) {
            setClan();
        } else {
            setComstar();
        }
    }

    public void setInnerSphere() {
        UnitSize = 4;
    }

    public void setClan() {
        UnitSize = 5;
    }

    public void setComstar() {
        UnitSize = 6;
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        //if( RecordSheet == null) { return Printable.NO_SUCH_PAGE; }
        x = 0;
        y = (int) pageFormat.getImageableY();
        ((Graphics2D) graphics).translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
        graphic = (Graphics2D) graphics;
        Render();
        return Printable.PAGE_EXISTS;
    }
    
    public void Render() {
        int elementCount = 0;
        Image icon = null;
        Dimension d = new Dimension();

        //Unit Logo
//        if ( !battleforce.LogoPath.isEmpty() && printLogo ) {
//            icon = imageTracker.getImage(getBattleforce().LogoPath);
//            d = media.reSize(icon, 20, 20);
//        }

//        graphic.setFont( PrintConsts.TitleFont );
//        if ( getBattleforce().ForceName.isEmpty() ) { getBattleforce().ForceName = getBattleforce().Type; }
//        graphic.drawString(getBattleforce().ForceName + " - " + getBattleforce().BattleForceStats.get(0).getUnit(), x, y);
//        graphic.drawString("PV: " + getBattleforce().PointValue(), (UnitImageWidth*3)-48, y);

//        y += graphic.getFont().getSize()-10;

        //Charts
//        graphic.drawImage( Charts, (UnitImageWidth*2)+2, y, 194, 562, null);

        //Output individual units
        for ( int i=0; i < getBattleforce().BattleForceStats.size(); i++ ) {
            BattleForceStats stats = (BattleForceStats) getBattleforce().BattleForceStats.get(i);

            if ( elementCount == ElementLimit ) {
                elementCount = 0;
                x = 0;
                y += UnitImageHeight + 5;
            }

            graphic.drawImage( Unit, x, y, UnitImageWidth, UnitImageHeight, null);

            elementCount += 1;

            //Image
            if ( !stats.getImage().isEmpty() && printMechs ) {
                Image image = imageTracker.getImage(stats.getImage());
                Dimension dim = media.reSize(image, 100d, 130d);
                image.getScaledInstance(dim.width, dim.height, Image.SCALE_SMOOTH);
                Point offset = media.offsetImageBottom( new Dimension(100, 130), dim);
                graphic.drawImage(image, x+UnitImageWidth-dim.width-5, y+25, dim.width, dim.height, null);

                if ( icon != null && printLogo ) {
                    graphic.drawImage(icon, x+UnitImageWidth-d.width-10, y+25, d.width, d.height, null);
                }
            }

            //graphic.setFont( new Font("Verdana", Font.PLAIN, 8) );
            graphic.setFont( PrintConsts.PlainFont );

            //PV
            graphic.setFont( new Font("Tahoma", Font.BOLD, 8) );
            graphic.drawString(stats.getPointValue()+" Points", x+205, y+15);

            //Unit Name
            graphic.setFont( PrintConsts.BoldFont );
            graphic.drawString( stats.getModel(), x+10, y+12 );
            graphic.setFont( PrintConsts.BaseFont.deriveFont( 11 ) );
            graphic.drawString( stats.getName().toUpperCase(), x+10, y+30);

            graphic.setFont( PrintConsts.BoldFont );
            /*
            //Pilot Name
            graphic.setFont( PrintConsts.SmallFont );
            graphic.drawString( stats.getWarrior(), x+5, y+62);

             */
            graphic.setFont( PrintConsts.PlainFont );
            //Weight Class
            graphic.drawString(stats.getWeight()+"", x+35, y+49);

            //Movement (MV)
            graphic.drawString(stats.getMovement(), x+75, y+49);

            //Skill
            graphic.drawString(stats.getSkill()+"", x+132, y+49);

            //Damage Values (S,M,L,E)
            graphic.drawString(stats.getShort()+"", x+28, y+78);
            graphic.drawString(stats.getMedium()+"", x+75, y+78);
            graphic.drawString(stats.getLong()+"", x+122, y+78);
            //graphic.drawString(stats.getExtreme()+"", x+92, y+70);

            //Overheat (OV)
            graphic.drawString(stats.getOverheat()+"", x+26, y+93);

            //Armor
            int xoffset = 20,
                yoffset = 104,
                indexer = 0;
            Color curColor = graphic.getColor();
            for ( int a=0; a < stats.getArmor(); a++ ) {
                //if ( indexer == 5 ) { yoffset += 9; xoffset = 132; indexer = 0; }
                graphic.setColor(Color.WHITE);
                graphic.fillOval(x+xoffset, y+yoffset, 7, 7);
                graphic.setColor(curColor);
                graphic.drawOval(x+xoffset, y+yoffset, 7, 7);
                xoffset += 8;
                indexer += 1;
            }

            //Internal Structure
            xoffset = 20;
            yoffset += 10;
            indexer = 0;
            for ( int s=0; s < stats.getInternal(); s++ ) {
                //if ( indexer == 5 ) { yoffset += 9; xoffset = 132; indexer = 0; }
                graphic.setColor(Color.LIGHT_GRAY);
                graphic.fillOval(x+xoffset, y+yoffset, 7, 7);
                graphic.setColor(curColor);
                graphic.drawOval(x+xoffset, y+yoffset, 7, 7);
                xoffset += 8;
                indexer += 1;
            }

            //Abilities
            xoffset = 50;
            yoffset = 137;
            indexer = 0;
            graphic.setFont( PrintConsts.PlainFont );
            graphic.drawString(stats.getAbilitiesString(), x+xoffset, y+yoffset);
            //for ( String ability : stats.getAbilities() ) {
            //    graphic.drawString(ability, x+xoffset, y+yoffset);
            //    yoffset += graphic.getFont().getSize();
            //}


            x += UnitImageWidth + 5;
        }


        graphic.setFont( PrintConsts.RegularFont );
        //Output Group Totals for previous group
        //graphic.drawString(PointTotal + "", x+460, y-UnitImageHeight+27);
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

}
