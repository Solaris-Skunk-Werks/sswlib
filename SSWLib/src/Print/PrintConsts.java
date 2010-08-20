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

import components.Ammunition;
import components.AvailableCode;
import components.Mech;
import components.PlaceableInfo;
import components.RangedWeapon;
import components.abPlaceable;
import components.ifWeapon;
import filehandlers.FileCommon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Vector;

public class PrintConsts {
    public final static int MECHNAME = 0,
                            WALKMP = 1,
                            RUNMP = 2,
                            JUMPMP = 3,
                            TONNAGE = 4,
                            TECH_CLAN = 5,
                            TECH_IS = 6,
                            PILOT_NAME = 7,
                            PILOT_GUN = 8,
                            PILOT_PILOT = 9,
                            COST = 10,
                            BV2 = 11,
                            HEATSINK_NUMBER = 12,
                            HEATSINK_DISSIPATION = 13,
                            MAX_HEAT = 16,
                            TOTAL_ARMOR = 17,
                            STATS = 18;

    public final static String RS_TW_BP = "./Data/Printing/RS_TW_BP.png",
                               RS_TW_QD = "./Data/Printing/RS_TW_QD.png",
                               RS_TO_BP = "./Data/Printing/RS_TO_BP.png",
                               RS_TO_QD = "./Data/Printing/RS_TO_QD.png",
                               BP_ChartImage = "./Data/Printing/Charts.png",
                               QD_ChartImage = "./Data/Printing/ChartsQD.png",
                               BF_BG = "./Data/Printing/BF_BG.png",
                               BF_IS_Unit = "./Data/Printing/BF_IS_Unit.png",
                               BF_CS_Unit = "./Data/Printing/BF_CS_Unit.png",
                               BF_CL_Unit = "./Data/Printing/BF_CL_Unit.png",
                               BF_Card = "./Data/Printing/BF_Card.png",
                               BF_Card2 = "./Data/Printing/BF_Card2.png",
                               BF_Chart = "./Data/Printing/BF_Chart.png",
                               BF_Chart2 = "./Data/Printing/BF_Chart2.png",
                               QS_Card = "./Data/Printing/QSCard.png",
                               BFB_BG = "./Data/Printing/bfb_bg.png",
                               BT_LOGO = "./Data/Printing/BT_Logo.png",
                               LA_Shield = "./Data/Printing/LA_Shield.png",
                               RA_Shield = "./Data/Printing/RA_Shield.png",
                               COLOR_VERT_QS_CARD = "./Data/Printing/QS_Card/QSVertCard.png",
                               COLOR_VERT_QS_CARD_BACK = "./Data/Printing/QS_Card/QSVertCardBack.png",
                               BW_VERT_QS_CARD = "./Data/Printing/QS_Card/QSBWVertCard.png",
                               BW_VERT_QS_CARD_BACK = "./Data/Printing/QS_Card/QSBWVertCardBack.png",
                               COLOR_HORIZ_QS_CARD = "./Data/Printing/QS_Card/QSHorizCard.png",
                               COLOR_HORIZ_QS_CARD_BACK = "./Data/Printing/QS_Card/QSHorizCardBack.png",
                               BW_HORIZ_QS_CARD = "./Data/Printing/QS_Card/QSBWHorizCard.png",
                               BW_HORIZ_QS_CARD_BACK = "./Data/Printing/QS_Card/QSBWHorizCardBack.png",
                               BF_ICON_LIGHT = "./Data/Printing/QS_Card/QS_Light.png",
                               BF_ICON_MEDIUM = "./Data/Printing/QS_Card/QS_Medium.png",
                               BF_ICON_HEAVY = "./Data/Printing/QS_Card/QS_Heavy.png",
                               BF_ICON_ASSAULT = "./Data/Printing/QS_Card/QS_Assault.png",
                               BF_ICON_INDUSTRIAL = "./Data/Printing/QS_Card/QS_Industrial.png",
                               BF_ICON_LIGHT_BW = "./Data/Printing/QS_Card/QS_Light_bw.png",
                               BF_ICON_MEDIUM_BW = "./Data/Printing/QS_Card/QS_Medium_bw.png",
                               BF_ICON_HEAVY_BW = "./Data/Printing/QS_Card/QS_Heavy_bw.png",
                               BF_ICON_ASSAULT_BW = "./Data/Printing/QS_Card/QS_Assault_bw.png",
                               BF_ICON_INDUSTRIAL_BW = "./Data/Printing/QS_Card/QS_Industrial_bw.png",
                               PATTERNS = "./Data/Printing/patterns.zip";

    //public final static Font BaseFont = FontLoader.getFont("Eurosti.ttf").deriveFont(Font.PLAIN, 20);
    //public final static Font BaseBoldFont = FontLoader.getFont("Eurostib.ttf").deriveFont(Font.PLAIN, 20);
    
    public final static Font BaseFont = FontLoader.getFont("EurostileLTStd.ttf").deriveFont(Font.PLAIN, 20);
    public final static Font BaseBoldFont = FontLoader.getFont("EurostileLTStd-Demi.ttf").deriveFont(Font.PLAIN, 20);
    public final static Font BaseCritFont = FontLoader.getFont("LiberationSans-Regular.ttf").deriveFont(Font.PLAIN, 10);

    public final static Font DesignNameFont  = BaseBoldFont.deriveFont(Font.PLAIN, 10);
    public final static Font CritFont = BaseBoldFont.deriveFont(Font.PLAIN, 6);
    public final static Font NonCritFont = BaseFont.deriveFont(Font.PLAIN, 6);

    public final static Font OVFont = BaseBoldFont.deriveFont(Font.PLAIN, 11);

    public final static Font TitleFont = BaseBoldFont.deriveFont(Font.PLAIN, 16);
    public final static Font BoldFont = BaseBoldFont.deriveFont(Font.PLAIN, 10);
    public final static Font PlainFont = BaseFont.deriveFont(Font.PLAIN, 9);
    public final static Font RegularFont = BaseFont.deriveFont(Font.PLAIN, 10);
    public final static Font Regular9Font = BaseFont.deriveFont(Font.PLAIN, 17);
    public final static Font ItalicFont = BaseFont.deriveFont(Font.ITALIC, 9);
    public final static Font Small8Font = BaseFont.deriveFont(Font.PLAIN, 8);
    public final static Font SmallFont = BaseFont.deriveFont(Font.PLAIN, 7);
    public final static Font SmallItalicFont = BaseFont.deriveFont(Font.ITALIC, 7);
    public final static Font SmallBoldFont = BaseBoldFont.deriveFont(Font.PLAIN, 7);
    public final static Font ReallySmallFont = BaseFont.deriveFont(Font.PLAIN, 6);
    public final static Font XtraSmallBoldFont = BaseBoldFont.deriveFont(Font.PLAIN, 6);
    public final static Font XtraSmallFont = BaseFont.deriveFont(Font.PLAIN, 6);
    public final static Font TinyFont = BaseFont.deriveFont(Font.PLAIN, 5);
    public final static Font CrazyTinyFont = BaseFont.deriveFont(Font.PLAIN, 4);
    public final static Font SectionHeaderFont = BaseFont.deriveFont(Font.PLAIN, 11);

    public static void ShadowText( Graphics2D graphic, Font font, Color foreColor, Color backColor, String Text, int X, int Y ) {
        ShadowText( graphic, font, foreColor, backColor, Text, (double) X, (double) Y);
    }

    public static void ShadowText( Graphics2D graphic, Font font, Color foreColor, Color backColor, String Text, double X, double Y ) {
        graphic.setFont(font);
        graphic.setColor(backColor);
        graphic.drawString(Text, (float) (X+.5), (float) (Y+.5) );
        graphic.setColor(foreColor);
        graphic.drawString(Text, (float) X, (float) Y);
        graphic.setColor(Color.BLACK);
    }

    public static void FilledCircle( Graphics2D graphic, Color foreColor, Color backColor, int Size, int X, int Y ) {
        graphic.setColor(backColor);
        graphic.fillOval(X, Y, Size, Size);
        graphic.setColor(foreColor);
        graphic.drawOval(X, Y, Size, Size);
        graphic.setColor(Color.BLACK);
    }

    public static String [] wrapText (String text, int len, boolean ignoreLineBreaks) {
        // return empty array for null text
        if (text == null)
            return new String [] {};

        // return text if len is zero or less
        if (len <= 0)
            return new String [] {text};

        // return text if less than length
        if (text.length() <= len)
            return new String [] {text};

        // before the wrapping, replace any special characters
        text = text.replace( "\t", "    " );
        text = text.replace("-", " ");
        if ( ignoreLineBreaks ) {
            text = text.replace("\n", "");
        }

        char [] chars = text.toCharArray();
        Vector lines = new Vector();
        StringBuffer line = new StringBuffer();
        StringBuffer word = new StringBuffer();

        for (int i = 0; i < chars.length; i++) {
            word.append(chars[i]);

            if ( (chars[i] == ' ') || (chars[i] == '-') ) {
                if ((line.length() + word.length()) > len) {
                    lines.add(line.toString());
                    line.delete(0, line.length());
                }

                line.append(word);
                word.delete(0, word.length());
            }
            if (chars[i] == '\n') {
                line.append(word);
                lines.add(line.toString());
                
                line.delete(0, line.length());
                word.delete(0, word.length());
            }
        }

        // handle any extra chars in current word
        if (word.length() > 0) {
            if ((line.length() + word.length()) > len) {
                lines.add(line.toString());
                line.delete(0, line.length());
            }
            line.append(word);
        }

        // handle extra line
        if (line.length() > 0) {
            lines.add(line.toString());
        }

        String [] ret = new String[lines.size()];
        int c = 0; // counter
        for (Enumeration e = lines.elements(); e.hasMoreElements(); c++) {
            ret[c] = (String) e.nextElement();
        }

        return ret;
    }

    public static String[] getCopyright() {
        return new String[]{
            "Copyright " + (Calendar.getInstance()).get(Calendar.YEAR) + " WizKids, Inc.  Classic Battletech, 'Mech and BattleMech are trademarks of WizKids, Inc.  All Rights reserved.",
            "Catalyst Game Labs and the Catalyst Game Labs logo are trademarks of InMediaRes Productions, LLC. Permission to photocopy for personal use."};
    }
    
    public static String GetPrintName( abPlaceable a, Mech CurMech, int Loc ) {
        // returns a modified PrintName, useful for special situations such as
        // mixed-tech mechs.
        String retval = a.CritName( Loc );
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

    public static Vector<PlaceableInfo> SortEquipmentByLocation( Mech CurMech, int MiniConvRate ) {
        boolean HasAmmoData = false;

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
                p = new PlaceableInfo(CurMech, MiniConvRate, a[i], CurMech.GetLoadout().Find( a[i] ));
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
                PlaceableInfo factory = new PlaceableInfo(CurMech, MiniConvRate);
                if ( p.Item instanceof ifWeapon ) {
                    if( ((ifWeapon) p.Item).GetWeaponClass() == ifWeapon.W_MISSILE ) {
                        if ( ((ifWeapon) p.Item).CritName().contains("ATM") ) {
                            Vector<PlaceableInfo> t = new Vector<PlaceableInfo>();
                            if ( !HasAmmoData ) {
                                t.add(factory.ATMERAmmo(p));
                                t.add(factory.ATMHEAmmo(p));
                                HasAmmoData = true;
                            }

                            if ( t.size() > 0 ) p.specials = "-";
                            if ( t.size() == 2 ) t.get(0).specials = "-";
                            for ( PlaceableInfo am : t ) {
                                temp.add(am);
                            }
                        } else if ( ((ifWeapon) p.Item).CritName().contains("MML") ) {
                            p.Clean();
                            if ( !HasAmmoData ) {
                                temp.add(factory.MMLLRMAmmo(p));
                                temp.add(factory.MMLSRMAmmo(p));
                                HasAmmoData = true;
                            }
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
                            temp.add( new PlaceableInfo( CurMech, MiniConvRate, (abPlaceable)((RangedWeapon) p.Item).GetCapacitor(), p.Location ) );
                        }
                    }
                }
            }
        }
        if ( CurMech.HasBlueShield() ) temp.add(new PlaceableInfo( CurMech, MiniConvRate, CurMech.GetBlueShield() ));

        return temp;
    }
}
