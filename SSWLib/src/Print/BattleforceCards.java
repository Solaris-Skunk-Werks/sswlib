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
import filehandlers.Media;
import java.awt.Font;

public class BattleforceCards implements Printable {
    private BattleForce battleforce;
    private Media media = new Media();
    private Graphics2D graphic;
    private Image RecordSheet,
                    Unit,
                    Charts;
    private int UnitSize = 4,
                UnitImageWidth = 187,
                UnitImageHeight = 234,
                ElementLimit = 3;
    private boolean printMechs = true,
                    printLogo = true;

    private int x = 0,
                y = 0;

    public BattleforceCards( BattleForce f) {
        battleforce = f;
        RecordSheet = media.GetImage( PrintConsts.BF_BG );
        Unit = media.GetImage( PrintConsts.BF_Card );
        Charts = media.GetImage( PrintConsts.BF_Chart );
        setType(battleforce.Type);
    }

    public BattleforceCards() {
        this(new BattleForce());
    }

    public void Add( BattleForceStats stat ) {
        getBattleforce().BattleForceStats.add(stat);
    }

    public void setRecordSheet( String sheet ) {
        RecordSheet = media.GetImage(sheet);
    }
    
    public void setUnitSheet( String item ) {
        Unit = media.GetImage( item );
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
        if( RecordSheet == null) { return Printable.NO_SUCH_PAGE; }
        ((Graphics2D) graphics).translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
        graphic = (Graphics2D) graphics;
        if ( UnitSize == 4 ) { ElementLimit = 2; }   //For an IS lance go ahead and split by 2 to match ups
        Render();
        return Printable.PAGE_EXISTS;
    }
    
    public void Render() {
        x = 20;
        y = 91;
        int Groups = 1,
            PointTotal = 0;
        int y2 = 0,
            elementCount = 0;
        boolean groupChanged = false;

        //Recordsheet
//        graphic.drawImage( RecordSheet, 0, 0, 576, 756, null );

        //Unit Logo
//        if ( !battleforce.LogoPath.isEmpty() && printLogo ) {
//            Image icon = media.GetImage(getBattleforce().LogoPath);
//            Dimension d = media.reSize(icon, 50, 50);
//            graphic.drawImage(icon, 300, 5, d.width, d.height, null);
//        }


        //Print the Unit Name at the top of the sheet
//        graphic.setFont( PrintConsts.TitleFont );
//
//        if ( getBattleforce().ForceName.isEmpty() ) { battleforce.ForceName = getBattleforce().Type; }
//        String title = getBattleforce().ForceName;
//        String[] pTitle = new String[]{"", "", "Record Sheet"};
//        int titleY = 23;
//
//
//        if ( title.length() <= 20 ) {
//            pTitle[0] = title;
//            pTitle[1] = "Record Sheet";
//            pTitle[2] = "";
//            titleY = 30;
//        } else if ( title.length() > 20 && title.length() <= 40 ) {
//            pTitle[0] = title.substring(0, title.lastIndexOf(" ", 20) );
//            title = title.replace(pTitle[0], "").trim();
//            pTitle[1] = title;
//            titleY = 20;
//        } else {
//            pTitle[0] = title.substring(0, title.lastIndexOf(" ", 20) );
//            title = title.replace(pTitle[0], "").trim();
//            int nextIndex = 20;
//            if ( title.length() < nextIndex ) { nextIndex = title.length(); }
//            pTitle[1] = title.substring(0, title.lastIndexOf(" ", nextIndex) );
//            title = title.replace(pTitle[1], "").trim();
//            pTitle[2] = title;
//            titleY = 20;
//        }
//
//        for ( int t=0; t < pTitle.length; t++ ) {
//            if ( !pTitle[t].isEmpty() ) {
//                graphic.drawString(pTitle[t], 360, titleY);
//                titleY += 12;
//            }
//        }


        x = 0;
//        y = 67;
        y = 0;

        graphic.setFont( PrintConsts.RegularFont );
        if ( getBattleforce().ForceName.isEmpty() ) { battleforce.ForceName = getBattleforce().Type; }
        graphic.drawString(getBattleforce().ForceName + " - " + getBattleforce().BattleForceStats.get(0).getUnit(), x, y);
        graphic.drawString("PV: " + getBattleforce().PointValue(), (UnitImageWidth*3)-20, y);
        
        y += graphic.getFont().getSize();

        ElementLimit = 2;
        
        if ( ElementLimit == 2 ) {
            graphic.drawImage( Charts, (UnitImageWidth*2)+2, y, 194, 465, null);
        }

        //Output individual units
        for ( int i=0; i < getBattleforce().BattleForceStats.size(); i++ ) {
            BattleForceStats stats = (BattleForceStats) getBattleforce().BattleForceStats.get(i);

            if ( elementCount == ElementLimit ) {
                elementCount = 0;
                x = 0;
                if ( UnitSize == 5) { x = (int) UnitImageWidth/2; }
                y += UnitImageHeight;
            }

            graphic.drawImage( Unit, x, y, UnitImageWidth, UnitImageHeight, null);

            elementCount += 1;
            PointTotal += stats.getPointValue();

            graphic.setFont( new Font("Verdana", Font.PLAIN, 8) );
            
            //Unit Name
            graphic.drawString(stats.getElement(), x+5, y+55);

            //Image
            if ( !stats.getImage().isEmpty() && printMechs ) {
                Image image = media.GetImage(stats.getImage());
                Dimension d = media.reSize(image, 108d, 140d);
                image.getScaledInstance(d.width, d.height, Image.SCALE_SMOOTH);
                graphic.drawImage(image, x+10, y+58, d.width, d.height, null);
            }

            //Movement (MV)
            int offset = 15;
            if ( stats.getMovement().length() > 2 ) { offset -= 5; }
            graphic.drawString(stats.getMovement(), x+offset, y+218);

            //Damage Values (S,M,L,E)
            graphic.drawString(stats.getShort()+"", x+34, y+218);
            graphic.drawString(stats.getMedium()+"", x+54, y+218);
            graphic.drawString(stats.getLong()+"", x+72, y+218);
            graphic.drawString(stats.getExtreme()+"", x+92, y+218);

            //Weight Class
            graphic.drawString(stats.getWeight()+"", x+111, y+218);

            //Skill
            graphic.drawString(stats.getSkill()+"", x+127, y+218);

            //Overheat (OV)
            graphic.drawString(stats.getOverheat()+"", x+144, y+218);

            //PV
            graphic.drawString(stats.getPointValue()+"", x+161, y+218);

            //Armor
            int xoffset = 132,
                yoffset = 72,
                indexer = 0;
            for ( int a=0; a < stats.getArmor(); a++ ) {
                if ( indexer == 5 ) { yoffset += 9; xoffset = 132; indexer = 0; }
                graphic.drawOval(x+xoffset, y+yoffset, 8, 8);
                xoffset += 9;
                indexer += 1;
            }

            //Internal Structure

            xoffset = 132;
            yoffset += 10;
            indexer = 0;
            Color curColor = graphic.getColor();
            for ( int s=0; s < stats.getInternal(); s++ ) {
                if ( indexer == 5 ) { yoffset += 9; xoffset = 132; indexer = 0; }
                graphic.setColor(Color.LIGHT_GRAY);
                graphic.fillOval(x+xoffset, y+yoffset, 8, 8);
                graphic.setColor(curColor);
                graphic.drawOval(x+xoffset, y+yoffset, 8, 8);
                xoffset += 9;
                indexer += 1;
            }

            //Abilities
            xoffset = 132;
            yoffset = 132;
            indexer = 0;
            graphic.setFont(new Font("Arial", Font.PLAIN, 5));
            for ( String ability : stats.getAbilities() ) {
                graphic.drawString(ability, x+xoffset, y+yoffset);
                yoffset += graphic.getFont().getSize();
            }

            x += UnitImageWidth + 1;
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
