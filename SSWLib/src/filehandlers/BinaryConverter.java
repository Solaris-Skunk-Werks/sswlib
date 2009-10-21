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

package filehandlers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;

public class BinaryConverter {
// Provides conversion tools for binary files, either into Java classes or from
// CSV to binary (and back again if needed).
    String Messages = "";

/**
 * Converts a CSV text file of RangedWeapon data into a binary file to conserve
 * space and prevent unwanted user changes.
 * 
 * @param input The canonical input CSV filename.
 * @param output The canonical output binary filename.
 * @param delim The text delimiter used in the CSV file.  One character only.
 */
    public boolean ConvertRangedWeaponsCSVtoBin( String input, String output, String delim ) {
        // take two filenames as input.
        Messages = "";
        int NumConverted = 0;
        BufferedReader FR;
        DataOutputStream FW;
        try {
            FR = new BufferedReader( new FileReader( input ) );
            FW = new DataOutputStream( new FileOutputStream( output ) );
            boolean EOF = false;
            String read = "";
            while( EOF == false ) {
                read = FR.readLine();
                if( read == null ) {
                    // We've hit the end of the file.
                    EOF = true;
                } else {
                    if( read.equals( "EOF" ) ) {
                        // end of file.
                        EOF = true;
                    } else {
                        ProcessWeaponString( FW, read, delim );
                        NumConverted++;
                    }
                }
            }
            FR.close();
            FW.close();
        } catch( Exception e ) {
            Messages += e.getMessage();
            Messages += e.toString();
            return false;
        }
        Messages += "Wrote " + NumConverted + " weapons to " + output + "\n";
        return true;
    }

/**
 * Converts a CSV text file of Ammunition data into a binary file to conserve
 * space and prevent unwanted user changes.
 * 
 * @param input The canonical input CSV filename.
 * @param output The canonical output binary filename.
 * @param delim The text delimiter used in the CSV file.  One character only.
 */
    public boolean ConvertAmmunitionCSVtoBin( String input, String output, String delim ) {
        // take two filenames as input.
        Messages = "";
        int NumConverted = 0;
        BufferedReader FR;
        DataOutputStream FW;
        try {
            FR = new BufferedReader( new FileReader( input ) );
            FW = new DataOutputStream( new FileOutputStream( output ) );
            boolean EOF = false;
            String read = "";
            while( EOF == false ) {
                read = FR.readLine();
                if( read == null ) {
                    // We've hit the end of the file.
                    EOF = true;
                } else {
                    if( read.equals( "EOF" ) ) {
                        // end of file.
                        EOF = true;
                    } else {
                        ProcessAmmoString( FW, read, delim );
                        NumConverted++;
                    }
                }
            }
            FR.close();
            FW.close();
        } catch( Exception e ) {
            Messages += e.getMessage();
            Messages += e.toString();
            return false;
        }
        Messages += "Wrote " + NumConverted + " ammunitions to " + output + "\n";
        return true;
    }

    public String GetMessages() {
        return Messages;
    }

/**
 * Processes the given weapons string from a CSV file into binary data.  Output
 * is fed to the given DataOutputStream.
 * 
 * @param FW The DataOutputStream to write to.
 * @param read The delimited string to read from.  One full line of data.
 * @param delim The text delimiter used in the CSV file.  One character only.
 * @throws java.lang.Exception
 */
    private void ProcessWeaponString( DataOutputStream FW, String read, String delim ) throws Exception {
        // here we're going to read the data string and output it to binary form
        // Assuming semi-colon delimited.
        String[] data = read.split( delim );
        // this is very unsafe, but we're going to assume that all the information
        // is correct and in the proper order.

        // basic weapon info
        FW.writeUTF( data[0] ); // Name
        FW.writeUTF( data[1] ); // Name
        FW.writeUTF( data[2] ); // Lookup Name
        FW.writeUTF( data[3] ); // MM Name
        FW.writeUTF( data[4] ); // Type
        FW.writeUTF( data[5] ); // Specials
        FW.writeInt( Integer.parseInt( data[6] ) ); // Class
        // Availability Code Starts Here
        FW.writeInt( Integer.parseInt( data[7] ) ); // Tbase
        FW.writeInt( Integer.parseInt( data[8] ) ); // BM
        FW.writeInt( Integer.parseInt( data[9] ) ); // IM
        FW.writeInt( Integer.parseInt( data[10] ) ); // CV
        FW.writeInt( Integer.parseInt( data[11] ) ); // AF
        FW.writeInt( Integer.parseInt( data[12] ) ); // CF
        // Inner Sphere Availability
        FW.writeChar( data[13].charAt( 0 ) ); // Tech
        FW.writeChar( data[14].charAt( 0 ) ); // SL
        FW.writeChar( data[15].charAt( 0 ) ); // SW
        FW.writeChar( data[16].charAt( 0 ) ); // CI
        FW.writeInt( Integer.parseInt( data[17] ) ); // Intro
        FW.writeUTF( data[18] ); // Ifac
        FW.writeBoolean( Boolean.parseBoolean( data[19] ) ); // Extinct
        FW.writeInt( Integer.parseInt( data[20] ) ); // Eyear
        FW.writeBoolean( Boolean.parseBoolean( data[21] ) ); // ReIntro
        FW.writeInt( Integer.parseInt( data[22] ) ); // RIYear
        FW.writeUTF( data[23] ); // RIFac
        FW.writeBoolean( Boolean.parseBoolean( data[24] ) ); // Prototype
        FW.writeInt( Integer.parseInt( data[25] ) ); // R&DYear
        FW.writeUTF( data[26] ); // R&DFac
        FW.writeInt( Integer.parseInt( data[27] ) ); // Pyear
        FW.writeUTF( data[28] ); // Pfac
        // Clan Availability
        FW.writeChar( data[29].charAt( 0 ) ); // Tech
        FW.writeChar( data[30].charAt( 0 ) ); // SL
        FW.writeChar( data[31].charAt( 0 ) ); // SW
        FW.writeChar( data[32].charAt( 0 ) ); // CI
        FW.writeInt( Integer.parseInt( data[33] ) ); // Intro
        FW.writeUTF( data[34] ); // Ifac
        FW.writeBoolean( Boolean.parseBoolean( data[35] ) ); // Extinct
        FW.writeInt( Integer.parseInt( data[36] ) ); // Eyear
        FW.writeBoolean( Boolean.parseBoolean( data[37] ) ); // ReIntro
        FW.writeInt( Integer.parseInt( data[38] ) ); // RIYear
        FW.writeUTF( data[39] ); // RIFac
        FW.writeBoolean( Boolean.parseBoolean( data[40] ) ); // Prototype
        FW.writeInt( Integer.parseInt( data[41] ) ); // R&DYear
        FW.writeUTF( data[42] ); // R&DFac
        FW.writeInt( Integer.parseInt( data[43] ) ); // Pyear
        FW.writeUTF( data[44] ); // Pfac
        // Meat of the weapon stats start here
        FW.writeDouble( Double.parseDouble( data[45] ) ); // Tons
        FW.writeInt( Integer.parseInt( data[46] ) ); // Mspc
        FW.writeDouble( Double.parseDouble( data[47] ) ); // Cost
        FW.writeDouble( Double.parseDouble( data[48] ) ); // OBV
        FW.writeDouble( Double.parseDouble( data[49] ) ); // DBV
        FW.writeInt( Integer.parseInt( data[50] ) ); // heat
        FW.writeInt( Integer.parseInt( data[51] ) ); // To-hit S
        FW.writeInt( Integer.parseInt( data[52] ) ); // To-hit M
        FW.writeInt( Integer.parseInt( data[53] ) ); // To-hit L
        FW.writeInt( Integer.parseInt( data[54] ) ); // Dam S
        FW.writeInt( Integer.parseInt( data[55] ) ); // M
        FW.writeInt( Integer.parseInt( data[56] ) ); // L
        FW.writeBoolean( Boolean.parseBoolean( data[57] ) ); // Cluster
        FW.writeInt( Integer.parseInt( data[58] ) ); // Size
        FW.writeInt( Integer.parseInt( data[59] ) ); // Group
        FW.writeInt( Integer.parseInt( data[60] ) ); // Cluster Mod Short
        FW.writeInt( Integer.parseInt( data[61] ) ); // Cluster Mod Medium
        FW.writeInt( Integer.parseInt( data[62] ) ); // Cluster Mod Long
        FW.writeInt( Integer.parseInt( data[63] ) ); // Range Min
        FW.writeInt( Integer.parseInt( data[64] ) ); // S
        FW.writeInt( Integer.parseInt( data[65] ) ); // M
        FW.writeInt( Integer.parseInt( data[66] ) ); // L
        FW.writeBoolean( Boolean.parseBoolean( data[67] ) ); // Has Ammo
        FW.writeInt( Integer.parseInt( data[68] ) ); // Lot
        FW.writeInt( Integer.parseInt( data[69] ) ); // Idx
        FW.writeBoolean( Boolean.parseBoolean( data[70] ) ); // Switch
        FW.writeBoolean( Boolean.parseBoolean( data[71] ) ); // HD
        FW.writeBoolean( Boolean.parseBoolean( data[72] ) ); // CT
        FW.writeBoolean( Boolean.parseBoolean( data[73] ) ); // Torso
        FW.writeBoolean( Boolean.parseBoolean( data[74] ) ); // Arms
        FW.writeBoolean( Boolean.parseBoolean( data[75] ) ); // Legs
        FW.writeBoolean( Boolean.parseBoolean( data[76] ) ); // Split
        FW.writeBoolean( Boolean.parseBoolean( data[77] ) ); // OmniArm
        FW.writeBoolean( Boolean.parseBoolean( data[78] ) ); // Fusion
        FW.writeBoolean( Boolean.parseBoolean( data[79] ) ); // Nuclear
        FW.writeBoolean( Boolean.parseBoolean( data[80] ) ); // Power Amps
        FW.writeBoolean( Boolean.parseBoolean( data[81] ) ); // OS
        FW.writeBoolean( Boolean.parseBoolean( data[82] ) ); // Streak
        FW.writeBoolean( Boolean.parseBoolean( data[83] ) ); // Ultra
        FW.writeBoolean( Boolean.parseBoolean( data[84] ) ); // Rotary
        FW.writeBoolean( Boolean.parseBoolean( data[85] ) ); // Explode
        FW.writeBoolean( Boolean.parseBoolean( data[86] ) ); // TC
        FW.writeBoolean( Boolean.parseBoolean( data[87] ) ); // Array
        FW.writeBoolean( Boolean.parseBoolean( data[88] ) ); // Capacitor
        FW.writeBoolean( Boolean.parseBoolean( data[89] ) ); // Insulator
        FW.writeBoolean( Boolean.parseBoolean( data[90] ) );
        FW.writeInt( Integer.parseInt( data[91] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[92] ) ); // A-IV
        FW.writeInt( Integer.parseInt( data[93] ) ); // A-IV Type
        FW.writeUTF( data[94] ); // ChatName
        FW.writeUTF( data[95] ); // BookReference
        FW.writeUTF( data[96] ); // Battleforce Abilities
    }

/**
 * Processes the given ammo string from a CSV file into binary data.  Output
 * is fed to the given DataOutputStream.
 * 
 * @param FW The DataOutputStream to write to.
 * @param read The delimited string to read from.  One full line of data.
 * @param delim The text delimiter used in the CSV file.  One character only.
 * @throws java.lang.Exception
 */
    private void ProcessAmmoString( DataOutputStream FW, String read, String delim ) throws Exception {
        // here we're going to read the data string and output it to binary form
        // Assuming semi-colon delimited.
        String[] data = read.split( delim );
        // this is very unsafe, but we're going to assume that all the information
        // is correct and in the proper order.

        // basic ammo info
        FW.writeUTF( data[0] ); // Actual Name
        FW.writeUTF( data[1] ); // Crit Name
        FW.writeUTF( data[2] ); // Lookup Name
        FW.writeUTF( data[3] ); // MM Name
        FW.writeInt( Integer.parseInt( data[4] ) ); // IDX
        // Availability Code Starts Here
        FW.writeInt( Integer.parseInt( data[5] ) ); // Tbase
        FW.writeInt( Integer.parseInt( data[6] ) ); // BM
        FW.writeInt( Integer.parseInt( data[7] ) ); // IM
        FW.writeInt( Integer.parseInt( data[8] ) ); // CV
        FW.writeInt( Integer.parseInt( data[9] ) ); // AF
        FW.writeInt( Integer.parseInt( data[10] ) ); // CF
        // Inner Sphere Availability
        FW.writeChar( data[11].charAt( 0 ) ); // Tech
        FW.writeChar( data[12].charAt( 0 ) ); // SL
        FW.writeChar( data[13].charAt( 0 ) ); // SW
        FW.writeChar( data[14].charAt( 0 ) ); // CI
        FW.writeInt( Integer.parseInt( data[15] ) ); // Intro
        FW.writeUTF( data[16] ); // Ifac
        FW.writeBoolean( Boolean.parseBoolean( data[17] ) ); // Extinct
        FW.writeInt( Integer.parseInt( data[18] ) ); // Eyear
        FW.writeBoolean( Boolean.parseBoolean( data[19] ) ); // ReIntro
        FW.writeInt( Integer.parseInt( data[20] ) ); // RIYear
        FW.writeUTF( data[21] ); // RIFac
        FW.writeBoolean( Boolean.parseBoolean( data[22] ) ); // Prototype
        FW.writeInt( Integer.parseInt( data[23] ) ); // R&DYear
        FW.writeUTF( data[24] ); // R&DFac
        FW.writeInt( Integer.parseInt( data[25] ) ); // Pyear
        FW.writeUTF( data[26] ); // Pfac
        // Clan Availability
        FW.writeChar( data[27].charAt( 0 ) ); // Tech
        FW.writeChar( data[28].charAt( 0 ) ); // SL
        FW.writeChar( data[29].charAt( 0 ) ); // SW
        FW.writeChar( data[30].charAt( 0 ) ); // CI
        FW.writeInt( Integer.parseInt( data[31] ) ); // Intro
        FW.writeUTF( data[32] ); // Ifac
        FW.writeBoolean( Boolean.parseBoolean( data[33] ) ); // Extinct
        FW.writeInt( Integer.parseInt( data[34] ) ); // Eyear
        FW.writeBoolean( Boolean.parseBoolean( data[35] ) ); // ReIntro
        FW.writeInt( Integer.parseInt( data[36] ) ); // RIYear
        FW.writeUTF( data[37] ); // RIFac
        FW.writeBoolean( Boolean.parseBoolean( data[38] ) ); // Prototype
        FW.writeInt( Integer.parseInt( data[39] ) ); // R&DYear
        FW.writeUTF( data[40] ); // R&DFac
        FW.writeInt( Integer.parseInt( data[41] ) ); // Pyear
        FW.writeUTF( data[42] ); // Pfac
        // Meat of the ammo stats start here
        FW.writeDouble( Double.parseDouble( data[43] ) ); // Tonnage
        FW.writeDouble( Double.parseDouble( data[44] ) ); // Cost
        FW.writeDouble( Double.parseDouble( data[45] ) ); // OBV
        FW.writeDouble( Double.parseDouble( data[46] ) ); // DBV
        FW.writeInt( Integer.parseInt( data[47] ) ); // To Hit S
        FW.writeInt( Integer.parseInt( data[48] ) ); // M
        FW.writeInt( Integer.parseInt( data[49] ) ); // L
        FW.writeInt( Integer.parseInt( data[50] ) ); // Damage S
        FW.writeInt( Integer.parseInt( data[51] ) ); // M
        FW.writeInt( Integer.parseInt( data[52] ) ); // L
        FW.writeBoolean( Boolean.parseBoolean( data[53] ) ); // Clustered
        FW.writeInt( Integer.parseInt( data[54] ) ); // Cluster
        FW.writeInt( Integer.parseInt( data[55] ) ); // Group
        FW.writeInt( Integer.parseInt( data[56] ) ); // Range Min
        FW.writeInt( Integer.parseInt( data[57] ) ); // S
        FW.writeInt( Integer.parseInt( data[58] ) ); // M
        FW.writeInt( Integer.parseInt( data[59] ) ); // L
        FW.writeInt( Integer.parseInt( data[60] ) ); // Lot Size
        FW.writeBoolean( Boolean.parseBoolean( data[61] ) ); // Explosive
        FW.writeInt( Integer.parseInt( data[62] ) ); // Weapon Class
        FW.writeInt( Integer.parseInt( data[63] ) ); // FCS Type
        FW.writeUTF( data[64] ); // Book Reference
    }
}