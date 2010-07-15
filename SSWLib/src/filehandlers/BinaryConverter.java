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
 * Converts a CSV text file of Physical Weapons data into a binary file to conserve
 * space and prevent unwanted user changes.
 *
 * @param input The canonical input CSV filename.
 * @param output The canonical output binary filename.
 * @param delim The text delimiter used in the CSV file.  One character only.
 */
    public boolean ConvertPhysicalWeaponCSVtoBin( String input, String output, String delim ) {
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
                        ProcessPhysicalWeaponString( FW, read, delim );
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
        Messages += "Wrote " + NumConverted + " physical weapons to " + output + "\n";
        return true;
    }

/**
 * Converts a CSV text file of Equipment data into a binary file to conserve
 * space and prevent unwanted user changes.
 *
 * @param input The canonical input CSV filename.
 * @param output The canonical output binary filename.
 * @param delim The text delimiter used in the CSV file.  One character only.
 */
    public boolean ConvertEquipmentCSVtoBin( String input, String output, String delim ) {
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
                        ProcessEquipmentString( FW, read, delim );
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
        Messages += "Wrote " + NumConverted + " equipments to " + output + "\n";
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
        ProcessAvailableCodeString( FW, data, 7 );
        // Meat of the weapon stats start here
        FW.writeDouble( Double.parseDouble( data[45] ) ); // Tons
        FW.writeInt( Integer.parseInt( data[46] ) ); // Mspc
        FW.writeInt( Integer.parseInt( data[47] ) ); // Vspc
        FW.writeDouble( Double.parseDouble( data[48] ) ); // Cost
        FW.writeDouble( Double.parseDouble( data[49] ) ); // OBV
        FW.writeDouble( Double.parseDouble( data[50] ) ); // DBV
        FW.writeInt( Integer.parseInt( data[51] ) ); // heat
        FW.writeInt( Integer.parseInt( data[52] ) ); // To-hit S
        FW.writeInt( Integer.parseInt( data[53] ) ); // To-hit M
        FW.writeInt( Integer.parseInt( data[54] ) ); // To-hit L
        FW.writeInt( Integer.parseInt( data[55] ) ); // Dam S
        FW.writeInt( Integer.parseInt( data[56] ) ); // M
        FW.writeInt( Integer.parseInt( data[57] ) ); // L
        FW.writeBoolean( Boolean.parseBoolean( data[58] ) ); // Cluster
        FW.writeInt( Integer.parseInt( data[59] ) ); // Size
        FW.writeInt( Integer.parseInt( data[60] ) ); // Group
        FW.writeInt( Integer.parseInt( data[61] ) ); // Cluster Mod Short
        FW.writeInt( Integer.parseInt( data[62] ) ); // Cluster Mod Medium
        FW.writeInt( Integer.parseInt( data[63] ) ); // Cluster Mod Long
        FW.writeInt( Integer.parseInt( data[64] ) ); // Range Min
        FW.writeInt( Integer.parseInt( data[65] ) ); // S
        FW.writeInt( Integer.parseInt( data[66] ) ); // M
        FW.writeInt( Integer.parseInt( data[67] ) ); // L
        FW.writeBoolean( Boolean.parseBoolean( data[68] ) ); // Has Ammo
        FW.writeInt( Integer.parseInt( data[69] ) ); // Lot
        FW.writeInt( Integer.parseInt( data[70] ) ); // Idx
        FW.writeBoolean( Boolean.parseBoolean( data[71] ) ); // Switch
        FW.writeBoolean( Boolean.parseBoolean( data[72] ) ); // HD
        FW.writeBoolean( Boolean.parseBoolean( data[73] ) ); // CT
        FW.writeBoolean( Boolean.parseBoolean( data[74] ) ); // Torso
        FW.writeBoolean( Boolean.parseBoolean( data[75] ) ); // Arms
        FW.writeBoolean( Boolean.parseBoolean( data[76] ) ); // Legs
        FW.writeBoolean( Boolean.parseBoolean( data[77] ) ); // Split
        FW.writeBoolean( Boolean.parseBoolean( data[78] ) ); // OmniArm
        FW.writeBoolean( Boolean.parseBoolean( data[79] ) ); // CV Front
        FW.writeBoolean( Boolean.parseBoolean( data[80] ) ); // CV Sides
        FW.writeBoolean( Boolean.parseBoolean( data[81] ) ); // CV Rear
        FW.writeBoolean( Boolean.parseBoolean( data[82] ) ); // CV Turret
        FW.writeBoolean( Boolean.parseBoolean( data[83] ) ); // CV Body
        FW.writeBoolean( Boolean.parseBoolean( data[84] ) ); // Fusion
        FW.writeBoolean( Boolean.parseBoolean( data[85] ) ); // Nuclear
        FW.writeBoolean( Boolean.parseBoolean( data[86] ) ); // Power Amps
        FW.writeBoolean( Boolean.parseBoolean( data[87] ) ); // OS
        FW.writeBoolean( Boolean.parseBoolean( data[88] ) ); // Streak
        FW.writeBoolean( Boolean.parseBoolean( data[89] ) ); // Ultra
        FW.writeBoolean( Boolean.parseBoolean( data[90] ) ); // Rotary
        FW.writeBoolean( Boolean.parseBoolean( data[91] ) ); // Explode
        FW.writeBoolean( Boolean.parseBoolean( data[92] ) ); // TC
        FW.writeBoolean( Boolean.parseBoolean( data[93] ) ); // Array
        FW.writeBoolean( Boolean.parseBoolean( data[94] ) ); // Capacitor
        FW.writeBoolean( Boolean.parseBoolean( data[95] ) ); // Insulator
        FW.writeBoolean( Boolean.parseBoolean( data[96] ) );
        FW.writeInt( Integer.parseInt( data[97] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[98] ) ); // A-IV
        FW.writeInt( Integer.parseInt( data[99] ) ); // A-IV Type
        FW.writeUTF( data[100] ); // ChatName
        FW.writeUTF( data[101] ); // BookReference
        FW.writeUTF( data[102] ); // Battleforce Abilities
    }

/**
 * Processes the given physical weapons string from a CSV file into binary data.
 * Output is fed to the given DataOutputStream.
 *
 * @param FW The DataOutputStream to write to.
 * @param read The delimited string to read from.  One full line of data.
 * @param delim The text delimiter used in the CSV file.  One character only.
 * @throws java.lang.Exception
 */
    private void ProcessPhysicalWeaponString( DataOutputStream FW, String read, String delim ) throws Exception {
        // here we're going to read the data string and output it to binary form
        // Assuming semi-colon delimited.
        String[] data = read.split( delim );
        // this is very unsafe, but we're going to assume that all the information
        // is correct and in the proper order.
        FW.writeUTF( data[0] ); // Lookup Name
        FW.writeUTF( data[1] ); // Actual Name
        FW.writeUTF( data[2] ); // Crit Name
        FW.writeUTF( data[3] ); // Chat Name
        FW.writeUTF( data[4] ); // MM Name
        FW.writeUTF( data[5] ); // Type
        FW.writeUTF( data[6] ); // Specials
        ProcessAvailableCodeString( FW, data, 7 );
        // weapon stats start here
        FW.writeDouble( Double.parseDouble( data[45] ) ); // Tonnage Multiplier
        FW.writeDouble( Double.parseDouble( data[46] ) ); // Tonnage Adder
        FW.writeBoolean( Boolean.parseBoolean( data[47] ) ); // Round to Half Ton
        FW.writeDouble( Double.parseDouble( data[48] ) ); // Cost Multiplier
        FW.writeDouble( Double.parseDouble( data[49] ) ); // Cost Adder
        FW.writeDouble( Double.parseDouble( data[50] ) ); // BV Multiplier
        FW.writeDouble( Double.parseDouble( data[51] ) ); // BV Adder
        FW.writeDouble( Double.parseDouble( data[52] ) ); // Def BV
        FW.writeDouble( Double.parseDouble( data[53] ) ); // Crit Multiplier
        FW.writeInt( Integer.parseInt( data[54] ) ); // Crit Adder
        FW.writeInt( Integer.parseInt( data[55] ) ); // Heat
        FW.writeInt( Integer.parseInt( data[56] ) ); // To-Hit Short
        FW.writeInt( Integer.parseInt( data[57] ) ); // To-Hit Medium
        FW.writeInt( Integer.parseInt( data[58] ) ); // To-Hit Long
        FW.writeDouble( Double.parseDouble( data[59] ) ); // Damage Multiplier
        FW.writeInt( Integer.parseInt( data[60] ) ); // Damage Adder
        FW.writeBoolean( Boolean.parseBoolean( data[61] ) ); // Can Alloc HD
        FW.writeBoolean( Boolean.parseBoolean( data[62] ) ); // Can Alloc CT
        FW.writeBoolean( Boolean.parseBoolean( data[63] ) ); // Can Alloc Torso
        FW.writeBoolean( Boolean.parseBoolean( data[64] ) ); // Can Alloc Arms
        FW.writeBoolean( Boolean.parseBoolean( data[65] ) ); // Can Alloc Legs
        FW.writeBoolean( Boolean.parseBoolean( data[66] ) ); // Can Split
        FW.writeBoolean( Boolean.parseBoolean( data[67] ) ); // Requires Lower Arm
        FW.writeBoolean( Boolean.parseBoolean( data[68] ) ); // Requires Hand
        FW.writeBoolean( Boolean.parseBoolean( data[69] ) ); // Replaces Lower Arm
        FW.writeBoolean( Boolean.parseBoolean( data[70] ) ); // Replaces Hand
        FW.writeBoolean( Boolean.parseBoolean( data[71] ) ); // Requires Fusion
        FW.writeBoolean( Boolean.parseBoolean( data[72] ) ); // Requires Nuclear
        FW.writeBoolean( Boolean.parseBoolean( data[73] ) ); // Requires Power Amps
        FW.writeInt( Integer.parseInt( data[74] ) ); // Physical Weapon Class
        FW.writeUTF( data[75] ); // Book Reference
        FW.writeUTF( data[76] ); // Battleforce Abilities
        if( Boolean.parseBoolean( data[77] ) ) {
            FW.writeBoolean( true ); // has a mech modifier
            FW.writeInt( Integer.parseInt( data[78] ) ); // Walking Adder
            FW.writeInt( Integer.parseInt( data[79] ) ); // Running Adder
            FW.writeInt( Integer.parseInt( data[80] ) ); // Jumping Adder
            FW.writeDouble( Double.parseDouble( data[81] ) ); // Running Multiplier
            FW.writeInt( Integer.parseInt( data[82] ) ); // PSR Modifier
            FW.writeInt( Integer.parseInt( data[83] ) ); // GSR Modifier
            FW.writeInt( Integer.parseInt( data[84] ) ); // Heat Adder
            FW.writeDouble( Double.parseDouble( data[85] ) ); // Defensive BV Bonus
            FW.writeDouble( Double.parseDouble( data[86] ) ); // Minimum Defensive BV Bonus
            FW.writeDouble( Double.parseDouble( data[87] ) ); // Armor Multiplier
            FW.writeDouble( Double.parseDouble( data[88] ) ); // Internal Multiplier
            FW.writeBoolean( Boolean.parseBoolean( data[89] ) ); // Use for BV Movement
            FW.writeBoolean( Boolean.parseBoolean( data[90] ) ); // Use for BV Heat
        } else {
            FW.writeBoolean( false ); // doesn't have a mech modifier
        }
    }

/**
 * Processes the given equipment string from a CSV file into binary data.
 * Output is fed to the given DataOutputStream.
 *
 * @param FW The DataOutputStream to write to.
 * @param read The delimited string to read from.  One full line of data.
 * @param delim The text delimiter used in the CSV file.  One character only.
 * @throws java.lang.Exception
 */
    private void ProcessEquipmentString( DataOutputStream FW, String read, String delim ) throws Exception {
        // here we're going to read the data string and output it to binary form
        // Assuming semi-colon delimited.
        String[] data = read.split( delim );
        // this is very unsafe, but we're going to assume that all the information
        // is correct and in the proper order.
        FW.writeUTF( data[0] ); // Lookup Name
        FW.writeUTF( data[1] ); // Actual Name
        FW.writeUTF( data[2] ); // Crit Name
        FW.writeUTF( data[3] ); // Chat Name
        FW.writeUTF( data[4] ); // MM Name
        FW.writeUTF( data[5] ); // Type
        FW.writeUTF( data[6] ); // Specials
        ProcessAvailableCodeString( FW, data, 7 );
        FW.writeDouble( Double.parseDouble( data[45] ) ); // Tonnage
        FW.writeBoolean( Boolean.parseBoolean( data[46] ) ); // Is Variable Tonnage
        FW.writeDouble( Double.parseDouble( data[47] ) ); // Variable Increment
        FW.writeDouble( Double.parseDouble( data[48] ) ); // Minimum Tonnage
        FW.writeDouble( Double.parseDouble( data[49] ) ); // Maximum Tonnage
        FW.writeDouble( Double.parseDouble( data[50] ) ); // Cost
        FW.writeDouble( Double.parseDouble( data[51] ) ); // Cost Per Ton
        FW.writeDouble( Double.parseDouble( data[52] ) ); // OBV
        FW.writeDouble( Double.parseDouble( data[53] ) ); // DBV
        FW.writeInt( Integer.parseInt( data[54] ) ); // NumCrits
        FW.writeDouble( Double.parseDouble( data[55] ) ); // Tons Per Crit
        FW.writeInt( Integer.parseInt( data[56] ) ); // Vspc
        FW.writeInt( Integer.parseInt( data[57] ) ); // Heat
        FW.writeInt( Integer.parseInt( data[58] ) ); // Short Range
        FW.writeInt( Integer.parseInt( data[59] ) ); // Medium Range
        FW.writeInt( Integer.parseInt( data[60] ) ); // Long Range
        FW.writeBoolean( Boolean.parseBoolean( data[61] ) ); // Has Ammo
        FW.writeInt( Integer.parseInt( data[62] ) ); // Lot Size
        FW.writeInt( Integer.parseInt( data[63] ) ); // Ammo Index
        FW.writeBoolean( Boolean.parseBoolean( data[64] ) ); // Can Alloc HD
        FW.writeBoolean( Boolean.parseBoolean( data[65] ) ); // Can Alloc CT
        FW.writeBoolean( Boolean.parseBoolean( data[66] ) ); // Can Alloc Torso
        FW.writeBoolean( Boolean.parseBoolean( data[67] ) ); // Can Alloc Arms
        FW.writeBoolean( Boolean.parseBoolean( data[68] ) ); // Can Alloc Legs
        FW.writeBoolean( Boolean.parseBoolean( data[69] ) ); // Can Split
        FW.writeBoolean( Boolean.parseBoolean( data[70] ) ); // Requires Quad
        FW.writeInt( Integer.parseInt( data[71] ) ); // Number Allowed Per Mech
        FW.writeBoolean( Boolean.parseBoolean( data[72] ) ); // Can alloc front
        FW.writeBoolean( Boolean.parseBoolean( data[73] ) ); // Can alloc sides
        FW.writeBoolean( Boolean.parseBoolean( data[74] ) ); // Can alloc rear
        FW.writeBoolean( Boolean.parseBoolean( data[75] ) ); // Can alloc turret
        FW.writeBoolean( Boolean.parseBoolean( data[76] ) ); // Can alloc body
        FW.writeBoolean( Boolean.parseBoolean( data[77] ) ); // Can Mount Rear
        FW.writeBoolean( Boolean.parseBoolean( data[78] ) ); // Explosive
        FW.writeUTF( data[79] ); // Book Reference
        FW.writeUTF( data[80] ); // Battleforce Abilities
        int numexceptions = Integer.parseInt( data[81] );
        FW.writeInt( numexceptions ); // number of exceptions to read
        for( int i = 0; i < numexceptions; i++ ) {
            FW.writeUTF( data[82+i] ); // each exception
        }
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
        ProcessAvailableCodeString( FW, data, 5 );
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

    private void ProcessAvailableCodeString( DataOutputStream FW, String[] data, int sindex ) throws Exception {
        FW.writeInt( Integer.parseInt( data[sindex] ) ); // Tbase
        FW.writeInt( Integer.parseInt( data[sindex+1] ) ); // BM
        FW.writeInt( Integer.parseInt( data[sindex+2] ) ); // IM
        FW.writeInt( Integer.parseInt( data[sindex+3] ) ); // CV
        FW.writeInt( Integer.parseInt( data[sindex+4] ) ); // AF
        FW.writeInt( Integer.parseInt( data[sindex+5] ) ); // CF
        // Inner Sphere Availability
        FW.writeChar( data[sindex+6].charAt( 0 ) ); // Tech
        FW.writeChar( data[sindex+7].charAt( 0 ) ); // SL
        FW.writeChar( data[sindex+8].charAt( 0 ) ); // SW
        FW.writeChar( data[sindex+9].charAt( 0 ) ); // CI
        FW.writeInt( Integer.parseInt( data[sindex+10] ) ); // Intro
        FW.writeUTF( data[sindex+11] ); // Ifac
        FW.writeBoolean( Boolean.parseBoolean( data[sindex+12] ) ); // Extinct
        FW.writeInt( Integer.parseInt( data[sindex+13] ) ); // Eyear
        FW.writeBoolean( Boolean.parseBoolean( data[sindex+14] ) ); // ReIntro
        FW.writeInt( Integer.parseInt( data[sindex+15] ) ); // RIYear
        FW.writeUTF( data[sindex+16] ); // RIFac
        FW.writeBoolean( Boolean.parseBoolean( data[sindex+17] ) ); // Prototype
        FW.writeInt( Integer.parseInt( data[sindex+18] ) ); // R&DYear
        FW.writeUTF( data[sindex+19] ); // R&DFac
        FW.writeInt( Integer.parseInt( data[sindex+20] ) ); // Pyear
        FW.writeUTF( data[sindex+21] ); // Pfac
        // Clan Availability
        FW.writeChar( data[sindex+22].charAt( 0 ) ); // Tech
        FW.writeChar( data[sindex+23].charAt( 0 ) ); // SL
        FW.writeChar( data[sindex+24].charAt( 0 ) ); // SW
        FW.writeChar( data[sindex+25].charAt( 0 ) ); // CI
        FW.writeInt( Integer.parseInt( data[sindex+26] ) ); // Intro
        FW.writeUTF( data[sindex+27] ); // Ifac
        FW.writeBoolean( Boolean.parseBoolean( data[sindex+28] ) ); // Extinct
        FW.writeInt( Integer.parseInt( data[sindex+29] ) ); // Eyear
        FW.writeBoolean( Boolean.parseBoolean( data[sindex+30] ) ); // ReIntro
        FW.writeInt( Integer.parseInt( data[sindex+31] ) ); // RIYear
        FW.writeUTF( data[sindex+32] ); // RIFac
        FW.writeBoolean( Boolean.parseBoolean( data[sindex+33] ) ); // Prototype
        FW.writeInt( Integer.parseInt( data[sindex+34] ) ); // R&DYear
        FW.writeUTF( data[sindex+35] ); // R&DFac
        FW.writeInt( Integer.parseInt( data[sindex+36] ) ); // Pyear
        FW.writeUTF( data[sindex+37] ); // Pfac
    }
}