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

import java.awt.Font;
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
                               BF_Chart = "./Data/Printing/BF_Chart.png",
                               QS_Card = "./Data/Printing/QSCard.png",
                               BFB_BG = "./Data/Printing/bfb_bg.png",
                               BT_LOGO = "./Data/Printing/BT_Logo.png",
                               LA_Shield = "./Data/Printing/LA_Shield.png",
                               RA_Shield = "./Data/Printing/RA_Shield.png",
                               PATTERNS = "./Data/Printing/patterns.zip";

    public final static Font BaseFont = FontLoader.getFont("Eurosti.ttf").deriveFont(Font.PLAIN, 20);
    public final static Font BaseBoldFont = FontLoader.getFont("Eurostib.ttf").deriveFont(Font.PLAIN, 20);

    public final static Font TitleFont = BaseBoldFont.deriveFont(Font.BOLD, 16); //new Font( "Verdana", Font.BOLD, 12 );
    public final static Font BoldFont = BaseBoldFont.deriveFont(Font.BOLD, 10); //new Font( "Arial", Font.BOLD, 8 );
    public final static Font PlainFont = BaseFont.deriveFont(Font.PLAIN, 9); // new Font( "Arial", Font.PLAIN, 8 );
    public final static Font RegularFont = BaseFont.deriveFont(Font.PLAIN, 10); // new Font( "Arial", Font.PLAIN, 10 );
    public final static Font Regular9Font = BaseFont.deriveFont(Font.PLAIN, 17); // new Font( "Arial", Font.PLAIN, 9 );
    public final static Font ItalicFont = BaseFont.deriveFont(Font.ITALIC, 9);  //new Font( "Arial", Font.ITALIC, 8 );
    public final static Font Small8Font = BaseFont.deriveFont(Font.PLAIN, 8);  //new Font( "Arial", Font.PLAIN, 7 );s
    public final static Font SmallFont = BaseFont.deriveFont(Font.PLAIN, 7);  //new Font( "Arial", Font.PLAIN, 7 );
    public final static Font SmallItalicFont = BaseFont.deriveFont(Font.ITALIC, 7); //new Font( "Arial", Font.ITALIC, 7 );
    public final static Font SmallBoldFont = BaseBoldFont.deriveFont(Font.BOLD, 7); //new Font( "Arial", Font.BOLD, 7 );
    public final static Font ReallySmallFont = BaseFont.deriveFont(Font.PLAIN, 6); //new Font( "Arial", Font.PLAIN, 6 );
    public final static Font XtraSmallBoldFont = BaseBoldFont.deriveFont(Font.BOLD, 6); //new Font( "Arial", Font.BOLD, 6 );
    public final static Font XtraSmallFont = BaseFont.deriveFont(Font.PLAIN, 6);  //new Font( "Arial", Font.PLAIN, 6 );
    public final static Font SectionHeaderFont = BaseFont.deriveFont(Font.PLAIN, 11); //new Font("Arial", Font.BOLD, 12);

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
        if ( ignoreLineBreaks ) {
            text = text.replace("\n", "");
        }

        char [] chars = text.toCharArray();
        Vector lines = new Vector();
        StringBuffer line = new StringBuffer();
        StringBuffer word = new StringBuffer();

        for (int i = 0; i < chars.length; i++) {
            word.append(chars[i]);

            if (chars[i] == ' ') {
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
}
