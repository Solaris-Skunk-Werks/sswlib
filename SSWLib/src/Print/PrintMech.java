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

import common.*;
import components.*;
import filehandlers.FileCommon;
import filehandlers.ImageTracker;
import filehandlers.Media;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Vector;
import java.util.prefs.Preferences;

public class PrintMech implements Printable {
    public Mech CurMech;
    private Image MechImage = null,
                  LogoImage = null,
                  RecordSheet = null,
                  ChartImage = null;
    private boolean Advanced = false,
                    Charts = false,
                    PrintPilot = true,
                    UseA4Paper = false,
                    Canon = false,
                    TRO = false,
                    printMech = false,
                    printLogo = false;
    private String PilotName = "",
                    GroupName = "",
                    currentAmmoFormat = "";
    private int Piloting = 5,
                Gunnery = 4,
                MiniConvRate = 1;
    private double BV = 0.0;
    private ifPrintPoints points = null;
    private Color Black = new Color( 0, 0, 0 ),
                  Grey = new Color( 128, 128, 128 );
    private Media media = new Media();
    private ImageTracker imageTracker;
    private Preferences Prefs = Preferences.userRoot().node( "/ssw/gui" );

    private Vector<PlaceableInfo> Items;
    private PIPPrinter ap;
    private Vector<AmmoData> AmmoList;

    // <editor-fold desc="Constructors">
    public PrintMech( Mech m, Image i, boolean adv, boolean A4, ImageTracker images) {
        CurMech = m;
        imageTracker = images;
        if ( !m.GetSSWImage().equals("../Images/No_Image.png")  ) MechImage = imageTracker.getImage(m.GetSSWImage());
        Advanced = adv;
        BV = CommonTools.GetAdjustedBV(CurMech.GetCurrentBV(), Gunnery, Piloting);
        UseA4Paper = A4;
        GetRecordSheet(imageTracker);
        AmmoList = GetAmmo();
    }

    public PrintMech( Mech m, ImageTracker images ) {
        this( m, null, false, false, images);
    }

    public PrintMech( Mech m, String Warrior, int Gun, int Pilot, ImageTracker images) {
        this( m, null, false, false, images);
        SetPilotData(Warrior, Gun, Pilot);
    }
    // </editor-fold>
    
    // <editor-fold desc="Settor Methods">
    public void SetPilotData( String pname, int pgun, int ppilot ) {
        PilotName = pname;
        Piloting = ppilot;
        Gunnery = pgun;
        setBV(CommonTools.GetAdjustedBV(BV, Gunnery, Piloting));
    }

    public void SetOptions( boolean charts, boolean PrintP, double UseBV ) {
        Charts = charts;
        setBV(UseBV);
        PrintPilot = PrintP;
    }

    public void SetMiniConversion( int conv ) {
        MiniConvRate = conv;
    }

    public void setMechwarrior(String name) {
        PilotName = name;
    }

    public void setGunnery(int gunnery) {
        Gunnery = gunnery;
    }

    public void setPiloting(int piloting) {
        Piloting = piloting;
    }

    public void setCharts(Boolean b) {
        Charts = b;
    }

    public void setPrintPilot(Boolean b) {
        PrintPilot = b;
    }

    public void setMechImage(Image MechImage) {
        if ( MechImage != null) {
            this.MechImage = MechImage;
            this.printMech = true;
        }
    }

    public void setPrintMech( Boolean PrintMech ) {
        this.printMech = PrintMech;
    }

    public void setLogoImage(Image LogoImage) {
        if ( LogoImage != null) {
            this.LogoImage = LogoImage;
            this.printLogo = true;
        }
    }

    public void setPrintLogo( Boolean PrintLogo ) {
        this.printLogo = PrintLogo;
    }

    public void setBV(double BV) {
        this.BV = BV;
    }

    public void setCanon( boolean Canon ) {
        this.Canon = Canon;
    }

    public void setTRO(boolean TRO) {
        this.TRO = TRO;
        setCanon(true);
        setCharts(false);
        SetMiniConversion(1);
        setPrintPilot(false);
        currentAmmoFormat = Prefs.get( "AmmoNamePrintFormat", "" );
        Prefs.put( "AmmoNamePrintFormat", "Ammo (%P) %L" );
    }

    // </editor-fold>

    // <editor-fold desc="Gettor Methods">
    public String getMechwarrior(){
        return PilotName;
    }

    public int getGunnery(){
        return Gunnery;
    }

    public int getPiloting(){
        return Piloting;
    }
    
    public Image getMechImage() {
        return MechImage;
    }
    
    public Image getLogoImage() {
        return LogoImage;
    }

    public boolean isTRO() {
        return TRO;
    }
    // </editor-fold>

    public int print( Graphics graphics, PageFormat pageFormat, int pageIndex ) throws PrinterException {
        ((Graphics2D) graphics).translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
        if( RecordSheet == null ) {
            return Printable.NO_SUCH_PAGE;
        } else {
            PreparePrint( (Graphics2D) graphics );
            if ( !currentAmmoFormat.isEmpty() ) { Prefs.put( "AmmoNamePrintFormat", currentAmmoFormat); }
            return Printable.PAGE_EXISTS;
        }
    }
    
    private void PreparePrint( Graphics2D graphics ) {
        Items = SortEquipmentByLocation();
        ap = new PIPPrinter(graphics, CurMech, Canon, imageTracker);
        this.BV = CommonTools.GetAdjustedBV(CurMech.GetCurrentBV(), Gunnery, Piloting);

        //DrawImages( graphics );
        DrawSheet( graphics );
        DrawPips( graphics );
        DrawCriticals( graphics );
        DrawMechData( graphics );

        if( Charts ) {
            // reset the scale and add the charts
            graphics.scale( 1.25d, 1.25d );
            graphics.drawImage( ChartImage, 0, 0, 576, 756, null );
            //AddCharts( graphics );
        }
        //DrawGrid( graphics );
    }

    private void DrawSheet( Graphics2D graphics ) {
        // adjust the printable area for A4 paper size
        if( UseA4Paper ) {
            graphics.scale( 0.9705d, 0.9705d );
        }

        // adjust the printable area for use with helpful charts
        if( Charts ) {
            graphics.scale( 0.8d, 0.8d );
        }
        
        graphics.drawImage( RecordSheet, 0, 0, 576, 756, null );
        //graphics.drawImage( RecordSheet, 0, 0, 560, 757, null );
        CheckShields( graphics );
        
        Point start = points.GetMechImageLoc();
        start.x -= 3;
        start.y -= 6;
        if ( printMech ) {
            if ( MechImage == null ) {
                String imagePath = media.FindMatchingImage(CurMech.GetName(), CurMech.GetModel());
                if ( !imagePath.isEmpty() ) MechImage = media.GetImage(imagePath);
            }
            if( MechImage != null ) {
                //graphics.drawRect(start.x, start.y, 160, 200);
                Dimension d = media.reSize(getMechImage(), 160, 200);
                Point offset = media.offsetImageCenter( new Dimension(160, 200), d);
                graphics.drawImage( getMechImage(), start.x + offset.x, start.y + offset.y, d.width, d.height, null );
            }
        }

        if ( LogoImage != null ) {
            graphics.drawImage( LogoImage, points.GetLogoImageLoc().x, points.GetLogoImageLoc().y, 50, 50, null );
        }
    }

    private void DrawPips( Graphics2D graphics ) {
        ap.Render();
    }

    private void DrawCriticals( Graphics2D graphics ) {
        graphics.setFont( PrintConsts.CritFont );
        DrawLocationCrits( graphics, CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_HD ), points.GetCritHDPoints() );
        DrawLocationCrits( graphics, CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_CT ), points.GetCritCTPoints() );
        DrawLocationCrits( graphics, CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_RT ), points.GetCritRTPoints() );
        DrawLocationCrits( graphics, CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_LT ), points.GetCritLTPoints() );
        DrawLocationCrits( graphics, CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_RA ), points.GetCritRAPoints() );
        DrawLocationCrits( graphics, CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_LA ), points.GetCritLAPoints() );
        DrawLocationCrits( graphics, CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_RL ), points.GetCritRLPoints() );
        DrawLocationCrits( graphics, CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_LL ), points.GetCritLLPoints() );
    }

    private void DrawLocationCrits( Graphics2D graphics, abPlaceable[] a, Point[] p ) {
        for( int i = 0; i < a.length && i < p.length; i++ ) {
            if( a[i].NumCrits() > 1 && a[i].Contiguous() &! ( a[i] instanceof Engine ) &! ( a[i] instanceof Gyro ) ) {
                // print the multi-slot indicator before the item
                abPlaceable Current = a[i];
                int j = i;
                int End = Current.NumCrits() + j;
                if( End > a.length ) {
                    End = a.length - 1;
                }
                for( ; j < End; j++ ) {
                    if( j == i ) {
                        // starting out
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x + 2, p[j].y - 3 );
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x, p[j].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else if( j == End - 1 ) {
                        // end the line
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x + 2, p[j].y - 2 );
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else {
                        // continue the line
                        graphics.drawLine( p[j].x, p[j].y, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    }
                }
                i = j - 1;
            } else {
                // single slot item
                if( ! a[i].IsCritable() ) {
                    DrawNonCritable( graphics, GetPrintName( a[i] ), p[i].x + 3, p[i].y );
                } else {
                    if( a[i].IsArmored() ) {
                        graphics.drawOval( p[i].x, p[i].y - 5, 5, 5 );
                        graphics.drawString( GetPrintName( a[i] ), p[i].x + 7, p[i].y );
                    } else if( a[i] instanceof Ammunition ) {
                        graphics.drawString( FileCommon.FormatAmmoPrintName( (Ammunition) a[i], 1, TRO ), p[i].x + 3, p[i].y );
                    } else {
                        graphics.drawString( GetPrintName( a[i] ), p[i].x + 3, p[i].y );
                    }
                }
            }
        }
    }

    private void DrawNonCritable( Graphics2D graphics, String Item, int X, int Y ) {
        // save the old font
        Font OldFont = graphics.getFont();
        graphics.setFont( PrintConsts.NonCritFont );
        if ( !TRO ) {
            graphics.setColor( Grey );
        }
        graphics.drawString( Item, X, Y );
        graphics.setFont( OldFont );
        graphics.setColor( Black );
    }

    private void DrawMechData( Graphics2D graphics ) {
        Point[] p = null;

        //Vector<PlaceableInfo> a = SortEquipmentByLocation();
        p = points.GetWeaponChartPoints();
        graphics.setFont( PrintConsts.ReallySmallFont );
        if (Items.size() > 10) { graphics.setFont( PrintConsts.XtraSmallFont ); }
        int offset = 0,
            xoffset = 0;
        for ( PlaceableInfo item : Items ) {
            xoffset = 0;
            graphics.drawString( item.Count + "", p[0].x+1, p[0].y + offset );
            graphics.drawString( item.name, p[1].x-3, p[1].y + offset );
            graphics.drawString( item.locName, p[2].x, p[2].y + offset );
            graphics.drawString( item.heat, p[3].x, p[3].y + offset );
            if ( item.damage.length() > 3 ) xoffset = (int)Math.ceil(item.damage.length());
            graphics.drawString( item.damage, p[4].x - xoffset, p[4].y + offset );
            graphics.drawString( item.min, p[5].x, p[5].y + offset );
            graphics.drawString( item.rShort, p[6].x, p[6].y + offset );
            graphics.drawString( item.rMed, p[7].x, p[7].y + offset );
            graphics.drawString( item.rLong, p[8].x, p[8].y + offset );

            offset += graphics.getFont().getSize();

            // check to see now if we need to print our special codes or more of the name
            if ( (item.specials.replace("-", "").length() > 0) || (item.name2.length() > 0) ) {
                int lineoffset = 0;
                if ( item.name2.length() > 0 ) {
                    graphics.drawString( item.name2, p[1].x, p[1].y + offset );
                    lineoffset = graphics.getFont().getSize();
                }
                if ( item.specials.replace("-", "").length() > 0 ) {
                    xoffset = (int)Math.ceil(item.specials.length());
                    graphics.drawString( item.specials, p[4].x - xoffset, p[4].y + offset );
                    lineoffset = graphics.getFont().getSize();
                }
                offset += lineoffset;
            }

            offset += 2;
        }

        //Output the list of Ammunition
        if ( !TRO ) {
            if ( AmmoList.size() > 0 ) {
                offset += 2;
                graphics.drawString("Ammunition Type", p[0].x, p[0].y + offset);
                graphics.drawString("Rounds", p[3].x, p[3].y + offset);
                offset += 2;
                graphics.drawLine(p[0].x, p[0].y + offset, p[8].x + 8, p[8].y + offset);
                offset += graphics.getFont().getSize();
            }
            for ( int index=0; index < AmmoList.size(); index++ ) {
                AmmoData CurAmmo = (AmmoData) AmmoList.get(index);
                graphics.drawString( CurAmmo.Format(), p[0].x, p[0].y + offset);
                graphics.drawString( CurAmmo.LotSize + "", p[3].x, p[3].y + offset);
                offset += graphics.getFont().getSize();
            }
        }

        graphics.setFont( PrintConsts.DesignNameFont );
        p = points.GetDataChartPoints();
        graphics.drawString( CurMech.GetFullName(), p[PrintConsts.MECHNAME].x, p[PrintConsts.MECHNAME].y );

        // have to hack the movement to print the correct stuff here.
        graphics.setFont( PrintConsts.Small8Font );
        if( CurMech.GetAdjustedWalkingMP( false, true ) != CurMech.GetWalkingMP() ) {
            graphics.drawString( ( CurMech.GetWalkingMP() * MiniConvRate ) + " (" + ( CurMech.GetAdjustedWalkingMP( false, true ) * MiniConvRate ) + ")", p[PrintConsts.WALKMP].x, p[PrintConsts.WALKMP].y );
        } else {
            graphics.drawString( ( CurMech.GetWalkingMP() * MiniConvRate ) + "", p[PrintConsts.WALKMP].x, p[PrintConsts.WALKMP].y );
        }
        if( CurMech.GetAdjustedRunningMP( false, true ) != CurMech.GetRunningMP() ) {
            if( CurMech.GetAdjustedRunningMP( false, true ) < CurMech.GetRunningMP() ) {
                graphics.drawString( CurMech.GetAdjustedRunningMP( false, true, MiniConvRate ) + "", p[PrintConsts.RUNMP].x, p[PrintConsts.RUNMP].y );
            } else {
                graphics.drawString( CurMech.GetRunningMP( MiniConvRate ) + " (" + CurMech.GetAdjustedRunningMP( false, true, MiniConvRate ) + ")", p[PrintConsts.RUNMP].x, p[PrintConsts.RUNMP].y );
            }
        } else {
            graphics.drawString( CurMech.GetRunningMP( MiniConvRate ) + "", p[PrintConsts.RUNMP].x, p[PrintConsts.RUNMP].y );
        }
        if( CurMech.GetAdjustedJumpingMP( false ) != CurMech.GetJumpJets().GetNumJJ() ) {
            graphics.drawString( ( CurMech.GetJumpJets().GetNumJJ() * MiniConvRate ) + " (" + ( CurMech.GetAdjustedJumpingMP( false ) * MiniConvRate ) + ")", p[PrintConsts.JUMPMP].x, p[PrintConsts.JUMPMP].y );
        } else {
            graphics.drawString( ( CurMech.GetJumpJets().GetNumJJ() * MiniConvRate ) + "", p[PrintConsts.JUMPMP].x, p[PrintConsts.JUMPMP].y );
        }
        // end hacking of movement.

        //Tonnage
        graphics.drawString( CurMech.GetTonnage() + "", p[PrintConsts.TONNAGE].x, p[PrintConsts.TONNAGE].y );

        //Cost
        graphics.setFont( PrintConsts.Small8Font );
        graphics.drawString( String.format( "%1$,.0f C-Bills", Math.floor( CurMech.GetTotalCost() + 0.5f ) ), p[PrintConsts.COST].x, p[PrintConsts.COST].y );

        //BV
        if ( !TRO ) {
            graphics.drawString( String.format( "%1$,.0f (Base: %2$,d)", BV, CurMech.GetCurrentBV() ), p[PrintConsts.BV2].x, p[PrintConsts.BV2].y );
            graphics.drawString( "Weapon Heat (" + CurMech.GetWeaponHeat() + ")", p[PrintConsts.MAX_HEAT].x-1, p[PrintConsts.MAX_HEAT].y );
            graphics.setFont( PrintConsts.SmallFont );
            graphics.drawString( "Armor Pts: " + CurMech.GetArmor().GetArmorValue(), p[PrintConsts.TOTAL_ARMOR].x, p[PrintConsts.TOTAL_ARMOR].y );
            graphics.setFont( PrintConsts.BoldFont );
        } else {
            graphics.drawString( String.format( "%1$,d", CurMech.GetCurrentBV() ), p[PrintConsts.BV2].x, p[PrintConsts.BV2].y );
        }

        //Mechwarrior
        graphics.setFont( PrintConsts.PlainFont );
        if ( TRO ) {
            graphics.setFont( PrintConsts.BoldFont );
            graphics.drawLine(p[PrintConsts.PILOT_NAME].x+1, p[PrintConsts.PILOT_NAME].y+1, p[PrintConsts.PILOT_NAME].x + 107, p[PrintConsts.PILOT_NAME].y+1);
            graphics.drawLine(p[PrintConsts.PILOT_GUN].x, p[PrintConsts.PILOT_GUN].y+1, p[PrintConsts.PILOT_GUN].x + 14, p[PrintConsts.PILOT_GUN].y+1);
            graphics.drawLine(p[PrintConsts.PILOT_PILOT].x-4, p[PrintConsts.PILOT_PILOT].y+1, p[PrintConsts.PILOT_PILOT].x + 10, p[PrintConsts.PILOT_PILOT].y+1);
        } else if( PrintPilot ) {
            graphics.setFont( PrintConsts.SmallFont );
            if ( !GroupName.isEmpty() ) {
                graphics.drawString( PilotName, p[PrintConsts.PILOT_NAME].x, p[PrintConsts.PILOT_NAME].y-4 );
                graphics.drawString( GroupName, p[PrintConsts.PILOT_NAME].x, p[PrintConsts.PILOT_NAME].y+3 );
            } else {
                graphics.drawString( PilotName, p[PrintConsts.PILOT_NAME].x, p[PrintConsts.PILOT_NAME].y );
            }
            graphics.setFont( PrintConsts.PlainFont );
            graphics.drawString( Gunnery + "", p[PrintConsts.PILOT_GUN].x, p[PrintConsts.PILOT_GUN].y );
            graphics.drawString( Piloting + "", p[PrintConsts.PILOT_PILOT].x, p[PrintConsts.PILOT_PILOT].y );
        }

        // check boxes
        graphics.setFont( PrintConsts.PlainFont );
        String temp;
        temp = CommonTools.GetTechbaseString( CurMech.GetLoadout().GetTechBase() );
        graphics.drawString( temp, p[PrintConsts.TECH_IS].x, p[PrintConsts.TECH_IS].y );

        graphics.drawString( CurMech.GetYear() + "", p[PrintConsts.TECH_IS].x, p[PrintConsts.TECH_IS].y + 10 );

        if ( !CurMech.GetArmor().CritName().contains("Standard") ) {
            //Armor Type
            graphics.setFont( PrintConsts.SmallFont );
            if ( CurMech.IsQuad() ) { graphics.setFont( PrintConsts.XtraSmallFont ); }

            int baseX = points.GetArmorInfoPoints()[LocationIndex.MECH_LOC_CT].x;
            int baseY = points.GetArmorInfoPoints()[LocationIndex.MECH_LOC_CT].y + 15;

            if ( CurMech.GetArmor().RequiresExtraRules() ) {
                graphics.setFont( PrintConsts.SmallBoldFont );
                if ( CurMech.IsQuad() ) { graphics.setFont( PrintConsts.XtraSmallBoldFont ); }
            }

            String[] parts = PrintConsts.wrapText(CurMech.GetArmor().CritName().trim(), 8, true); //CurMech.GetArmor().CritName().trim().split(" ");
            for (String part: parts) {
                if ( !part.trim().isEmpty() ) {
                    //int xCoord = baseX - ((part.trim().length() / 2) * 3);
                    graphics.drawString( part, baseX - part.trim().length(), baseY );
                    baseY += graphics.getFont().getSize();
                }
            }
            graphics.setFont( PrintConsts.PlainFont );
        }

        if ( !TRO ) {
            //Availability Codes
            graphics.drawString(CurMech.GetAvailability().GetBestCombinedCode(), p[PrintConsts.TECH_IS].x, p[PrintConsts.TECH_IS].y+20);
        }

        //heat sinks
        Point startingPoint = new Point(507, 615),
              currentPoint = (Point) startingPoint.clone();

        int counter = 0,
            ovalSize = 7,
            spacer = 2;
        if ( CurMech.GetHeatSinks().GetNumHS() > 10 && CurMech.GetHeatSinks().GetNumHS() <= 20 )
            currentPoint.x -= 4;
        else if ( CurMech.GetHeatSinks().GetNumHS() > 20 )
            currentPoint.x -= 9;

        for( int i = 0; i < CurMech.GetHeatSinks().GetNumHS(); i++ ) {
            counter++;
            graphics.drawOval( currentPoint.x, currentPoint.y, ovalSize, ovalSize );
            currentPoint.y += ovalSize + spacer;
            if ( counter >= 10 ) {
                currentPoint.x += ovalSize + spacer;
                currentPoint.y = startingPoint.y;
                counter = 0;
            }
        }

        graphics.setFont( PrintConsts.PlainFont );
        offset = 4;
        String HS = CurMech.GetHeatSinks().GetNumHS() + "";
        if ( CurMech.GetHeatSinks().TotalDissipation() > CurMech.GetHeatSinks().GetNumHS() ) {
            HS += " (" + CurMech.GetHeatSinks().TotalDissipation() + ")";
            offset = 0;
        }

        graphics.setFont(PrintConsts.SmallFont);
        //HS Number
        graphics.drawString( HS, p[PrintConsts.HEATSINK_NUMBER].x + offset, p[PrintConsts.HEATSINK_NUMBER].y );
        //HS Type
        graphics.drawString( CurMech.GetHeatSinks().LookupName().split( " " )[0], p[PrintConsts.HEATSINK_NUMBER].x+2, p[PrintConsts.HEATSINK_NUMBER].y + 8 );

        // internal information
        graphics.setFont( PrintConsts.ReallySmallFont );
        p = points.GetInternalInfoPoints();
        graphics.drawString( "(" + CurMech.GetIntStruc().GetCTPoints() + ")", p[LocationIndex.MECH_LOC_CT].x, p[LocationIndex.MECH_LOC_CT].y );
        graphics.drawString( "(" + CurMech.GetIntStruc().GetSidePoints() + ")", p[LocationIndex.MECH_LOC_LT].x, p[LocationIndex.MECH_LOC_LT].y );
        graphics.drawString( "(" + CurMech.GetIntStruc().GetSidePoints() + ")", p[LocationIndex.MECH_LOC_RT].x, p[LocationIndex.MECH_LOC_RT].y );
        graphics.drawString( "(" + CurMech.GetIntStruc().GetArmPoints() + ")", p[LocationIndex.MECH_LOC_LA].x, p[LocationIndex.MECH_LOC_LA].y );
        graphics.drawString( "(" + CurMech.GetIntStruc().GetArmPoints() + ")", p[LocationIndex.MECH_LOC_RA].x, p[LocationIndex.MECH_LOC_RA].y );
        graphics.drawString( "(" + CurMech.GetIntStruc().GetLegPoints() + ")", p[LocationIndex.MECH_LOC_LL].x, p[LocationIndex.MECH_LOC_LL].y );
        graphics.drawString( "(" + CurMech.GetIntStruc().GetLegPoints() + ")", p[LocationIndex.MECH_LOC_RL].x, p[LocationIndex.MECH_LOC_RL].y );

        // armor information
        p = points.GetArmorInfoPoints();
        graphics.drawString( "(" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_HD ) + ")", p[LocationIndex.MECH_LOC_HD].x, p[LocationIndex.MECH_LOC_HD].y );
        graphics.drawString( "(" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_CT ) + ")", p[LocationIndex.MECH_LOC_CT].x, p[LocationIndex.MECH_LOC_CT].y );
        graphics.drawString( "(" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_LT ) + ")", p[LocationIndex.MECH_LOC_LT].x, p[LocationIndex.MECH_LOC_LT].y );
        graphics.drawString( "(" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_RT ) + ")", p[LocationIndex.MECH_LOC_RT].x, p[LocationIndex.MECH_LOC_RT].y );
        graphics.drawString( "(" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_LA ) + ")", p[LocationIndex.MECH_LOC_LA].x, p[LocationIndex.MECH_LOC_LA].y );
        graphics.drawString( "(" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_RA ) + ")", p[LocationIndex.MECH_LOC_RA].x, p[LocationIndex.MECH_LOC_RA].y );
        graphics.drawString( "(" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_LL ) + ")", p[LocationIndex.MECH_LOC_LL].x, p[LocationIndex.MECH_LOC_LL].y );
        graphics.drawString( "(" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_RL ) + ")", p[LocationIndex.MECH_LOC_RL].x, p[LocationIndex.MECH_LOC_RL].y );
        graphics.drawString( "(" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_CTR ) + ")", p[LocationIndex.MECH_LOC_CTR].x, p[LocationIndex.MECH_LOC_CTR].y );
        graphics.drawString( "(" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_LTR ) + ")", p[LocationIndex.MECH_LOC_LTR].x, p[LocationIndex.MECH_LOC_LTR].y );
        graphics.drawString( "(" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_RTR ) + ")", p[LocationIndex.MECH_LOC_RTR].x, p[LocationIndex.MECH_LOC_RTR].y );
        if( CurMech.GetArmor().GetBAR() < 10 ) {
            graphics.setFont( PrintConsts.XtraSmallFont );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_HD].x, p[LocationIndex.MECH_LOC_HD].y + 7 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_CT].x - 5, p[LocationIndex.MECH_LOC_CT].y + 8 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_LT].x - 4, p[LocationIndex.MECH_LOC_LT].y + 7 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_RT].x - 4, p[LocationIndex.MECH_LOC_RT].y + 7 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_LA].x - 4, p[LocationIndex.MECH_LOC_LA].y + 8 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_RA].x - 5, p[LocationIndex.MECH_LOC_RA].y + 8 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_LL].x - 4, p[LocationIndex.MECH_LOC_LL].y + 8 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_RL].x - 4, p[LocationIndex.MECH_LOC_RL].y + 8 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_CTR].x + 2, p[LocationIndex.MECH_LOC_CTR].y + 8 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_LTR].x + 13, p[LocationIndex.MECH_LOC_LTR].y );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_RTR].x - 22, p[LocationIndex.MECH_LOC_RTR].y );
            graphics.setFont( PrintConsts.SmallFont );
        }
    }

    private void DrawImages( Graphics2D graphics ) {
        //PrintMech Image
        Point start = points.GetMechImageLoc();
        start.x -= 3;
        start.y -= 6;
        if( getMechImage() != null ) {
            //graphics.drawRect(start.x, start.y, 160, 200);
            Dimension d = media.reSize(getMechImage(), 160, 200);
            Point offset = media.offsetImageCenter( new Dimension(160, 200), d);
            graphics.drawImage( getMechImage(), start.x + offset.x, start.y + offset.y, d.width, d.height, null );
        }

        if ( LogoImage != null ) {
            graphics.drawImage( LogoImage, points.GetLogoImageLoc().x, points.GetLogoImageLoc().y, 50, 50, null );
        }
    }

    private void DrawGrid( Graphics2D graphics ) {
        graphics.setFont( PrintConsts.ReallySmallFont );
        boolean bPrint = true;
        for (int x = 0; x <= 576; x += 10) {
            if (bPrint) { graphics.drawString(x+"", x-5, 5); }
            bPrint = !bPrint;
            graphics.drawLine(x, 0, x, 756);
        }
        bPrint = false;
        for (int y = 0; y <= 756; y += 10) {
            if (bPrint) { graphics.drawString(y+"", 0, y+5); }
            bPrint = !bPrint;
            graphics.drawLine(0, y, 576, y);
        }
    }

    private Vector<AmmoData> GetAmmo() {
        //Output the list of Ammunition
        Vector all = CurMech.GetLoadout().GetNonCore();
        Vector<AmmoData> AmmoList = new Vector<AmmoData>();
        for ( int index=0; index < all.size(); index++ ) {
            if(  all.get( index ) instanceof Ammunition ) {
                AmmoData CurAmmo = new AmmoData((Ammunition) all.get(index));

                boolean found = false;
                for ( int internal=0; internal < AmmoList.size(); internal++ ) {
                    AmmoData existAmmo = (AmmoData) AmmoList.get(internal);
                    if ( CurAmmo.ActualName.equals( existAmmo.ActualName ) ) {
                        existAmmo.LotSize += CurAmmo.LotSize;
                        found = true;
                        break;
                    }
                }
                if ( !found ) {
                    AmmoList.add(CurAmmo);
                }
            }
        }
        return AmmoList;
    }

    private boolean AmmoContains( Vector<AmmoData> AmmoList, String CheckExpr ) {
        for ( AmmoData data : AmmoList ) {
            if ( data.Format().contains(CheckExpr) ) return true;
        }
        return false;
    }

    private void CheckShields( Graphics2D graphics ) {
        Image shieldImage;
        Point startingLocation = new Point(0,0);
        for ( PlaceableInfo item : Items ) {
            if ( item.Item instanceof PhysicalWeapon ) {
                if ( ((PhysicalWeapon) item.Item).GetPWClass() == PhysicalWeapon.PW_CLASS_SHIELD ) {
                    switch ( item.Location ) {
                        case LocationIndex.MECH_LOC_LA:
                            shieldImage = imageTracker.getImage(PrintConsts.LA_Shield);
                            startingLocation = new Point(375, 7);
                            graphics.drawImage(shieldImage, startingLocation.x, startingLocation.y, 57, 116, null);
                            startingLocation.x += 1;
                            startingLocation.y += 2;
                            break;
                        case LocationIndex.MECH_LOC_RA:
                            shieldImage = imageTracker.getImage(PrintConsts.RA_Shield);
                            startingLocation = new Point(511, 7);
                            graphics.drawImage(shieldImage, startingLocation.x, startingLocation.y, 57, 116, null);
                            startingLocation.x += 22;
                            startingLocation.y += 2;
                            break;
                    }

                    if ( item.Item.ActualName().contains("Small") ) {
                        ap.AddArmor(FileCommon.EncodeLocation(item.Location, CurMech.IsQuad()) + "_SH_" , startingLocation, new Point(34, 101), 11);
                    }

                    if ( item.Item.ActualName().contains("Medium") ) {
                        ap.AddArmor(FileCommon.EncodeLocation(item.Location, CurMech.IsQuad()) + "_SH_", startingLocation, new Point(34, 101), 18);
                    }

                    if ( item.Item.ActualName().contains("Large") ) {
                        ap.AddArmor(FileCommon.EncodeLocation(item.Location, CurMech.IsQuad()) + "_SH_", startingLocation, new Point(34, 101), 25);
                    }
                }
            }
        }
    }

    private Vector<PlaceableInfo> SortEquipmentByLocation() {
        Vector v = (Vector) CurMech.GetLoadout().GetNonCore().clone();
                // add in MASC and the targeting computer if needed.
        if( CurMech.GetPhysEnhance().IsMASC() ) v.add( CurMech.GetPhysEnhance() );
        if( CurMech.UsingTC() ) v.add( CurMech.GetTC() );
        if( CurMech.HasCommandConsole() ) v.add( CurMech.GetCommandConsole() );
        if( CurMech.UsingPartialWing() ) v.add( CurMech.GetPartialWing() );
        if( CurMech.GetLoadout().HasSupercharger() ) v.add( CurMech.GetLoadout().GetSupercharger() );
        if( CurMech.IsQuad() ) {
            if( CurMech.HasLegAES() ) {
                v.add( CurMech.GetRAAES() );
                v.add( CurMech.GetLAAES() );
                v.add( CurMech.GetRLAES() );
                v.add( CurMech.GetLLAES() );
            }
        } else {
            if( CurMech.HasRAAES() ) v.add( CurMech.GetRAAES() );
            if( CurMech.HasLAAES() ) v.add( CurMech.GetLAAES() );
            if( CurMech.HasLegAES() ) {
                v.add( CurMech.GetRLAES() );
                v.add( CurMech.GetLLAES() );
            }
        }
        Vector Equip = CurMech.SortLoadout(v);
        Vector<abPlaceable> sorted = FileCommon.SortEquipmentForStats(CurMech, Equip, true, false);

        abPlaceable[] a = new abPlaceable[sorted.size()];
        for( int i = 0; i < sorted.size(); i++ ) {
            if( ! ( sorted.get( i ) instanceof Ammunition ) ) {
                a[i] = (abPlaceable) sorted.get( i );
            }
        }

        // now group them by location
        int count = 0;
        PlaceableInfo p = null;
        Vector<PlaceableInfo> temp = new Vector<PlaceableInfo>();
        for( int i = 0; i < a.length; i++ ) {
            if( a[i] != null ) {
                p = new PlaceableInfo(a[i], CurMech.GetLoadout().Find( a[i] ));
                a[i] = null;
                count ++;
                // search for other matching weapons in the same location
                for( int j = 0; j < a.length; j++ ) {
                    if( a[j] != null ) {
                        if( a[j].CritName().equals( p.Item.CritName() ) ) {
                            if( CurMech.GetLoadout().Find( a[j] ) == p.Location ) {
                                count++;
                                a[j] = null;
                            }
                        }
                    }
                }

                if ( p.name.equals("Targeting Computer") ||
                     p.name.equals("AES") ) count = 1;

                // set the weapon count and add it to the temp vector
                p.Count = count + "";
                temp.add( p );
                count = 0;

                // Parse the items and add extra line items as necessary
                // ATM, MML, Artemis, TC, etc
                PlaceableInfo factory = new PlaceableInfo();
                if ( p.Item instanceof ifWeapon ) {
                    if( ((ifWeapon) p.Item).GetWeaponClass() == ifWeapon.W_MISSILE ) {
                        if ( ((ifWeapon) p.Item).CritName().contains("ATM") ) {
                            Vector<PlaceableInfo> t = new Vector<PlaceableInfo>();
                            if ( AmmoContains(AmmoList, "ER") )
                                t.add(factory.ATMERAmmo(p));
                            if ( AmmoContains(AmmoList, "HE") )
                                t.add(factory.ATMHEAmmo(p));

                            if ( t.size() > 0 ) p.specials = "-";
                            if ( t.size() == 2 ) t.get(0).specials = "-";
                            for ( PlaceableInfo am : t ) {
                                temp.add(am);
                            }
                        } else if ( ((ifWeapon) p.Item).CritName().contains("MML") ) {
                            p.Clean();
                            temp.add(factory.MMLLRMAmmo(p));
                            temp.add(factory.MMLSRMAmmo(p));
                        } else if ( ((ifWeapon) p.Item).IsFCSCapable() ) {
                            if ( CurMech.GetLoadout().UsingArtemisIV()) {
                                temp.add(factory.ArtemisIV(p));
                            } else if ( CurMech.GetLoadout().UsingArtemisV()) {
                                temp.add(factory.ArtemisV(p));
                            } else if ( CurMech.GetLoadout().UsingApollo() ) {
                                temp.add(factory.Apollo(p));
                            }
                            p.specials = "-";
                        }
                    } else if ( ((ifWeapon) p.Item).GetWeaponClass() == ifWeapon.W_ENERGY ) {
                        if ( ((RangedWeapon) p.Item).IsUsingCapacitor() ) {
                            p.name.replace(" + PPC Capacitor", "");
                            p.name2.replace(" + PPC Capacitor", "");
                            temp.add( new PlaceableInfo( (abPlaceable)((RangedWeapon) p.Item).GetCapacitor(), p.Location ) );
                        }
                    }
                }
            }
        }
        if ( CurMech.HasBlueShield() ) temp.add(new PlaceableInfo( CurMech.GetBlueShield() ));

        return temp;
    }

    private void GetRecordSheet( ImageTracker images ) {
        // loads the correct record sheet and points based on the information given
        RecordSheet = images.getImage( PrintConsts.RS_TW_BP );
        ChartImage = images.getImage(PrintConsts.BP_ChartImage );
        points = new TWBipedPoints();

        if ( CurMech.IsQuad() ) {
            RecordSheet = images.getImage( PrintConsts.RS_TW_QD );
            if ( CurMech.IsQuad() ) { ChartImage = images.getImage(PrintConsts.QD_ChartImage); }
            points = new TWQuadPoints();
        }

        if ( Advanced ) {
            RecordSheet = images.getImage( PrintConsts.RS_TO_BP );
        }
    }

    private String GetPrintName( abPlaceable a ) {
        // returns a modified PrintName, useful for special situations such as
        // mixed-tech mechs.
        String retval = a.CritName();
        if( a instanceof RangedWeapon && CurMech.GetLoadout().GetTechBase() == AvailableCode.TECH_BOTH ) {
            switch( ((RangedWeapon) a).GetTechBase() ) {
                case AvailableCode.TECH_INNER_SPHERE:
                    retval = retval + " (IS)";
                    break;
                case AvailableCode.TECH_CLAN:
                    retval = retval + " (C)";
                    break;
            }
        }
        return retval;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    private class PlaceableInfo {
        public int Location,
                    NameLength = 25;
        public String   Count = "",
                        name = "",
                        name2 = "",
                        locName = "",
                        heat = "--",
                        min = "--",
                        damage = "--",
                        specials = "--",
                        rShort = "--",
                        rMed = "--",
                        rLong = "--";
        public abPlaceable Item;

        public PlaceableInfo() {
        }

        public PlaceableInfo( String Count, int Location, String LocationName, String Name, String Heat, String Damage, String Min, String Short, String Medium, String Long, String Specials) {
            this.Count = Count;
            this.Location = Location;
            this.locName = LocationName;
            this.name = Name;
            this.heat = Heat;
            this.damage = Damage;
            this.min = Min;
            this.rShort = Short;
            this.rMed = Medium;
            this.rLong = Long;
            this.specials = Specials;
        }

        public PlaceableInfo( MultiSlotSystem item ) {
            this.name = item.CritName();
            this.damage = "[E]";
        }

        public PlaceableInfo( abPlaceable item, int Location ) {
            this.Item = item;
            this.name = GetPrintName(item).trim();
                    //.replace("Medium Pulse", "Med. Pulse")
                    //.replace("Beagle Active Probe", "Beagle Active Prb")
                    //.replace("Guardian ECM Suite", "Guardian ECM")
                    //.replace("Targeting Computer", "Targeting Comp.");
            String[] names = PrintConsts.wrapText(this.name, NameLength, false);
            if ( names.length > 1 ) {
                name = names[0];
                name2 = names[1];
            }
            this.Location = Location;
            this.locName = FileCommon.EncodeLocation( Location, CurMech.IsQuad() );

            if( item instanceof Equipment ) {
                Equipment e = (Equipment) item;
                this.heat = e.GetHeat() + "";
                if( e.GetSpecials().equals( "-" ) ) 
                    this.damage = "[" + e.GetType() + "]";
                else
                    this.specials = ("[" + e.GetType() + ", " + e.GetSpecials() + "]").replace(", -", "");
            } else if( item instanceof ifWeapon ) {
                ifWeapon weap = (ifWeapon) item;
                this.heat = weap.GetHeat() + "";
                if( weap.IsUltra() || weap.IsRotary() ) this.heat += "/s";
                this.damage = weap.GetDamageShort() + "";
                if( weap.GetWeaponClass() == ifWeapon.W_MISSILE ) this.damage += "/m";
                if( weap.GetDamageShort() != weap.GetDamageMedium() ||
                     weap.GetDamageShort() != weap.GetDamageLong() ||
                     weap.GetDamageMedium() != weap.GetDamageLong() )
                    this.damage = weap.GetDamageShort() + "/" + weap.GetDamageMedium() + "/" + weap.GetDamageLong();
                if( weap.GetSpecials().equals( "-" ) ) 
                    this.damage += " [" + weap.GetType() + "]";
                else
                    this.specials = ("[" + weap.GetType() + ", " + weap.GetSpecials() + "]").replace(", -", "");
                if( weap.GetRangeMin() > 0 ) this.min = (weap.GetRangeMin() * MiniConvRate ) + "";
                this.rShort = (weap.GetRangeShort() * MiniConvRate) + "";
                this.rMed = (weap.GetRangeMedium() * MiniConvRate) + "";
                this.rLong = (weap.GetRangeLong() * MiniConvRate) + "";
            }
        }

        public void Clean() {
            this.damage = this.specials;
            this.min = "";
            this.rShort = "";
            this.rMed = "";
            this.rLong = "";
            this.specials = "-";
        }

        public PlaceableInfo ATMERAmmo( PlaceableInfo item ) {
            return new PlaceableInfo("", 0, "", "ER", "", "1/m", (4 * MiniConvRate ) + "", (9 * MiniConvRate ) + "", (18 * MiniConvRate ) + "", (27 * MiniConvRate ) + "", item.specials);
        }

        public PlaceableInfo ATMHEAmmo( PlaceableInfo item ) {
            return new PlaceableInfo("", 0, "", "HE", "", "3/m", "-", (3 * MiniConvRate ) + "", (6 * MiniConvRate ) + "", (9 * MiniConvRate ) + "", item.specials);
        }

        public PlaceableInfo MMLLRMAmmo( PlaceableInfo item ) {
            return new PlaceableInfo("", 0, "", "LRM", "", "1/Msl.", (6 * MiniConvRate ) + "", (7 * MiniConvRate ) + "", (14 * MiniConvRate ) + "", (21 * MiniConvRate ) + "", "-");
        }

        public PlaceableInfo MMLSRMAmmo( PlaceableInfo item ) {
            return new PlaceableInfo("", 0, "", "SRM", "", "2/Msl.", "--", (3 * MiniConvRate ) + "", (6 * MiniConvRate ) + "", (9 * MiniConvRate ) + "", "-");
        }

        public PlaceableInfo ArtemisIV( PlaceableInfo item ) {
            return new PlaceableInfo("", 0, "", "w/Artemis IV FCS", "", item.specials, "", "", "", "", "-");
        }

        public PlaceableInfo ArtemisV( PlaceableInfo item ) {
            return new PlaceableInfo("", 0, "", "w/Artemis V FCS", "", item.specials, "", "", "", "", "-");
        }

        public PlaceableInfo Apollo( PlaceableInfo item ) {
            return new PlaceableInfo("", 0, "", "w/Apollo FCS", "", item.specials, "", "", "", "", "-");
        }

        public PlaceableInfo TargetingComputer() {
            TargetingComputer tc = CurMech.GetTC();
            return new PlaceableInfo("1", CurMech.GetLoadout().Find((abPlaceable) tc), FileCommon.EncodeLocation( CurMech.GetLoadout().Find((abPlaceable) tc), CurMech.IsQuad() ), "", tc.CritName(), "", "-", "-", "-", "-", "-");
        }
    }

    private class AmmoData {
        public String ActualName,
                      ChatName,
                      CritName,
                      LookupName;
        public int LotSize;

        public AmmoData( Ammunition ammo ) {
            this.ActualName = ammo.ActualName();
            this.ChatName = ammo.ChatName();
            this.CritName = ammo.CritName().replace("@", "").trim();
            this.LookupName = ammo.LookupName();

            this.LotSize = ammo.GetLotSize();
        }

        public String Format() {
            return ("@%P").replace("%P", CritName).replace("%F", LookupName).replace("%L", "");
        }
    }
}
