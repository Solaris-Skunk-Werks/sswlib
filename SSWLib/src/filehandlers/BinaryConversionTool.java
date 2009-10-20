/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package filehandlers;

import components.AvailableCode;
import components.RangedWeapon;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Vector;

public class BinaryConversionTool {
// Provides conversion tools for binary files, either into Java classes or from
// CSV to binary (and back again if needed).

/**
 * Reads in weapons from the spcified binary file.  The weapon data must be in a
 * specific format, see weaponinfo.txt in SSWlib/docs for the correct format.
 * You can build a CSV file and then convert it using ConvertCSVtoBin()
 * 
 * @param inputfile The canonical file name to be read from.
 * @return A vector of RangedWeapons.
 */
    public Vector ReadWeapons( String inputfile ) {
        // just a test to see if we can read the file correctly
        Vector finished = new Vector();
        DataInputStream FR;
        String[] strings = { "", "", "", "" };
        int[] ints = { 0, 0, 0, 0, 0 };
        boolean[] bools = { false, false, false };
        int[] rules = { 0, 0, 0, 0, 0 };
        char[] codes = { 'X', 'X', 'X', 'X' };
        String s1, s2, name, mmname, pname;
        int i1, i2, i3, i4, i5, i6, i7, i8, i9, wclass, techbase;
        boolean b1, b2, b3, b4, b5, b6, b7, b8;
        float f1, f2, f3, f4;
        try {
            FR = new DataInputStream( new FileInputStream( inputfile ) );
            while( true ) {
                try {
                    name = FR.readUTF();
                    mmname = FR.readUTF();
                    pname = FR.readUTF();
                    wclass = FR.readInt();
                    techbase = FR.readInt();

                    AvailableCode AC = new AvailableCode( techbase );

                    rules[0] = FR.readInt();
                    rules[1] = FR.readInt();
                    rules[2] = FR.readInt();
                    rules[3] = FR.readInt();
                    rules[4] = FR.readInt();
                    AC.SetRulesLevels( rules[0], rules[1], rules[2], rules[3], rules[4] );

                    // Inner Sphere first
                    AC.SetISCodes( FR.readChar(), FR.readChar(), FR.readChar(), FR.readChar() );
                    ints[0] = FR.readInt();        // intro year
                    strings[0] = FR.readUTF();     // intro faction
                    bools[0] = FR.readBoolean();   // extinct
                    ints[1] = FR.readInt();        // extinct year
                    bools[1] = FR.readBoolean();   // reintro
                    ints[2] = FR.readInt();        // reintro year
                    strings[1] = FR.readUTF();     // reintro faction
                    bools[2] = FR.readBoolean();   // prototype
                    ints[3] = FR.readInt();        // r&d year
                    strings[2] = FR.readUTF();     // r&d faction
                    ints[4] = FR.readInt();        // prototype year
                    strings[3] = FR.readUTF();     // prototype faction
                    AC.SetISFactions( strings[2], strings[3], strings[0], strings[1] );
                    AC.SetISDates( ints[3], ints[4], bools[2], ints[0], ints[1], ints[2], bools[0], bools[1] );

                   // Clan next
                    codes[0] = FR.readChar();
                    codes[1] = FR.readChar();
                    codes[2] = FR.readChar();
                    codes[3] = FR.readChar();
                    AC.SetCLCodes( codes[0], codes[1], codes[2], codes[3] );
                    ints[0] = FR.readInt();        // intro year
                    strings[0] = FR.readUTF();     // intro faction
                    bools[0] = FR.readBoolean();   // extinct
                    ints[1] = FR.readInt();        // extinct year
                    bools[1] = FR.readBoolean();   // reintro
                    ints[2] = FR.readInt();        // reintro year
                    strings[1] = FR.readUTF();     // reintro faction
                    bools[2] = FR.readBoolean();   // prototype
                    ints[3] = FR.readInt();        // r&d year
                    strings[2] = FR.readUTF();     // r&d faction
                    ints[4] = FR.readInt();        // prototype year
                    strings[3] = FR.readUTF();     // prototype faction
                    AC.SetCLFactions( strings[2], strings[3], strings[0], strings[1] );
                    AC.SetCLDates( ints[3], ints[4], bools[2], ints[0], ints[1], ints[2], bools[0], bools[1] );

                    RangedWeapon w = new RangedWeapon( name, mmname, pname, AC, wclass );

                    s1 = FR.readUTF();
                    s2 = FR.readUTF();
                    w.SetSpecials( s1, s2 );
                    i1 = FR.readInt();
                    i2 = FR.readInt();
                    w.SetHeat( i1, i2 );
                    i1 = FR.readInt();
                    i2 = FR.readInt();
                    i3 = FR.readInt();
                    w.SetToHit( i1, i2, i3 );
                    i1 = FR.readInt();
                    i2 = FR.readInt();
                    i3 = FR.readInt();
                    i4 = FR.readInt();
                    i5 = FR.readInt();
                    i6 = FR.readInt();
                    i7 = FR.readInt();
                    b1 = FR.readBoolean();
                    i8 = FR.readInt();
                    i9 = FR.readInt();
                    w.SetDamage( i1, i2, i3, i4, i5, i6, i7, b1, i8, i9 );
                    i1 = FR.readInt();
                    i2 = FR.readInt();
                    i3 = FR.readInt();
                    i4 = FR.readInt();
                    i5 = FR.readInt();
                    w.SetRange( i1, i2, i3, i4, i5 );
                    b1 = FR.readBoolean();
                    i1 = FR.readInt();
                    i2 = FR.readInt();
                    b2 = FR.readBoolean();
                    w.SetAmmo( b1, i1, i2, b2 );
                    f1 = FR.readFloat();
                    i1 = FR.readInt();
                    i2 = FR.readInt();
                    i3 = FR.readInt();
                    f2 = FR.readFloat();
                    f3 = FR.readFloat();
                    f4 = FR.readFloat();
                    w.SetStats( f1, i1, i2, i3, f2, f3, f4 );
                    b1 = FR.readBoolean();
                    b2 = FR.readBoolean();
                    b3 = FR.readBoolean();
                    b4 = FR.readBoolean();
                    w.SetRequirements( b1, b2, b3, b4 );
                    b1 = FR.readBoolean();
                    b2 = FR.readBoolean();
                    b3 = FR.readBoolean();
                    b4 = FR.readBoolean();
                    b5 = FR.readBoolean();
                    b6 = FR.readBoolean();
                    b7 = FR.readBoolean();
                    b8 = FR.readBoolean();
                    w.SetMechAlloc( b1, b2, b3, b4, b5, b6, b7, b8 );
                    b1 = FR.readBoolean();
                    b2 = FR.readBoolean();
                    b3 = FR.readBoolean();
                    b4 = FR.readBoolean();
                    b5 = FR.readBoolean();
                    w.SetCVAlloc( b1, b2, b3, b4, b5 );
                    b1 = FR.readBoolean();
                    b2 = FR.readBoolean();
                    b3 = FR.readBoolean();
                    b4 = FR.readBoolean();
                    w.SetAeroAlloc( b1, b2, b3, b4 );
                    b1 = FR.readBoolean();
                    b2 = FR.readBoolean();
                    b3 = FR.readBoolean();
                    b4 = FR.readBoolean();
                    b5 = FR.readBoolean();
                    b6 = FR.readBoolean();
                    b7 = FR.readBoolean();
                    b8 = FR.readBoolean();
                    w.SetWeapon( b1, b2, b3, b4, b5, b6, b7, b8 );
                    b1 = FR.readBoolean();
                    i1 = FR.readInt();
                    w.SetArtemis( b1, i1 );
                    finished.add( w );
                } catch( EOFException e1 ) {
                    break;
                }
            }
            FR.close();
        } catch( Exception e ) {
            e.printStackTrace();
        }
        return finished;
    }

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
                    }
                }
            }
            FR.close();
            FW.close();
        } catch( Exception e ) {
            e.printStackTrace();
            return false;
        }
        return true;
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
        FW.writeUTF( data[0] );
        FW.writeUTF( data[1] );
        FW.writeUTF( data[2] );
        FW.writeInt( Integer.parseInt( data[3] ) );
        FW.writeInt( Integer.parseInt( data[4] ) );
        FW.writeInt( Integer.parseInt( data[5] ) );
        FW.writeInt( Integer.parseInt( data[6] ) );
        FW.writeInt( Integer.parseInt( data[7] ) );
        FW.writeInt( Integer.parseInt( data[8] ) );
        FW.writeInt( Integer.parseInt( data[9] ) );
        FW.writeChar( data[10].charAt( 0 ) );
        FW.writeChar( data[11].charAt( 0 ) );
        FW.writeChar( data[12].charAt( 0 ) );
        FW.writeChar( data[13].charAt( 0 ) );
        FW.writeInt( Integer.parseInt( data[14] ) );
        FW.writeUTF( data[15] );
        FW.writeBoolean( Boolean.parseBoolean( data[16] ) );
        FW.writeInt( Integer.parseInt( data[17] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[18] ) );
        FW.writeInt( Integer.parseInt( data[19] ) );
        FW.writeUTF( data[20] );
        FW.writeBoolean( Boolean.parseBoolean( data[21] ) );
        FW.writeInt( Integer.parseInt( data[22] ) );
        FW.writeUTF( data[23] );
        FW.writeInt( Integer.parseInt( data[24] ) );
        FW.writeUTF( data[25] );
        FW.writeChar( data[26].charAt( 0 ) );
        FW.writeChar( data[27].charAt( 0 ) );
        FW.writeChar( data[28].charAt( 0 ) );
        FW.writeChar( data[29].charAt( 0 ) );
        FW.writeInt( Integer.parseInt( data[30] ) );
        FW.writeUTF( data[31] );
        FW.writeBoolean( Boolean.parseBoolean( data[32] ) );
        FW.writeInt( Integer.parseInt( data[33] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[34] ) );
        FW.writeInt( Integer.parseInt( data[35] ) );
        FW.writeUTF( data[36] );
        FW.writeBoolean( Boolean.parseBoolean( data[37] ) );
        FW.writeInt( Integer.parseInt( data[38] ) );
        FW.writeUTF( data[39] );
        FW.writeInt( Integer.parseInt( data[40] ) );
        FW.writeUTF( data[41] );
        FW.writeUTF( data[42] );
        FW.writeUTF( data[43] );
        FW.writeInt( Integer.parseInt( data[44] ) );
        FW.writeInt( Integer.parseInt( data[45] ) );
        FW.writeInt( Integer.parseInt( data[46] ) );
        FW.writeInt( Integer.parseInt( data[47] ) );
        FW.writeInt( Integer.parseInt( data[48] ) );
        FW.writeInt( Integer.parseInt( data[49] ) );
        FW.writeInt( Integer.parseInt( data[50] ) );
        FW.writeInt( Integer.parseInt( data[51] ) );
        FW.writeInt( Integer.parseInt( data[52] ) );
        FW.writeInt( Integer.parseInt( data[53] ) );
        FW.writeInt( Integer.parseInt( data[54] ) );
        FW.writeInt( Integer.parseInt( data[55] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[56] ) );
        FW.writeInt( Integer.parseInt( data[57] ) );
        FW.writeInt( Integer.parseInt( data[58] ) );
        FW.writeInt( Integer.parseInt( data[59] ) );
        FW.writeInt( Integer.parseInt( data[60] ) );
        FW.writeInt( Integer.parseInt( data[61] ) );
        FW.writeInt( Integer.parseInt( data[62] ) );
        FW.writeInt( Integer.parseInt( data[63] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[64] ) );
        FW.writeInt( Integer.parseInt( data[65] ) );
        FW.writeInt( Integer.parseInt( data[66] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[67] ) );
        FW.writeFloat( Float.parseFloat( data[68] ) );
        FW.writeInt( Integer.parseInt( data[69] ) );
        FW.writeInt( Integer.parseInt( data[70] ) );
        FW.writeInt( Integer.parseInt( data[71] ) );
        FW.writeFloat( Float.parseFloat( data[72] ) );
        FW.writeFloat( Float.parseFloat( data[73] ) );
        FW.writeFloat( Float.parseFloat( data[74] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[75] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[76] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[77] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[78] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[79] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[80] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[81] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[82] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[83] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[84] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[85] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[86] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[87] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[88] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[89] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[90] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[91] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[92] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[93] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[94] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[95] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[96] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[97] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[98] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[99] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[100] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[101] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[102] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[103] ) );
        FW.writeBoolean( Boolean.parseBoolean( data[104] ) );
        FW.writeInt( Integer.parseInt( data[105] ) );
    }
}