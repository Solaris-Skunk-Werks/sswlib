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

package battleforce;

import common.*;
import java.util.Vector;
import org.w3c.dom.Node;
import components.Mech;
import java.io.BufferedWriter;
import java.io.IOException;

public class BattleForceStats {
    private String Element = "",
                    Name = "",
                    Model = "",
                    MV = "",
                    TerrainMV = "",
                    Unit = "",
                    Image = "",
                    Warrior = "";
    private double[] Mods = {2.63, 2.24, 1.82, 1.38, 1.00, 0.86, 0.77, 0.68};

    private boolean isTerrainModified = false;
    private Vector<String> Abilities = new Vector<String>();
    private Vector<String> AltMunitions = new Vector<String>();

    private int S = 0,
                M = 0,
                L = 0,
                E = 0,
                Wt = 0,
                Skill = 9,
                OV = 0,
                Armor = 0,
                Internal = 0,
                BasePV = 0,
                PV = 0,
                Gunnery = 4,
                Piloting = 5;

    public BattleForceStats() {

    }

    public BattleForceStats( Mech m ) {
        Element = m.GetFullName();
        Name = m.GetName();
        Model = m.GetModel();
        if ( m.IsOmnimech() ) { Model = m.GetLoadout().GetName(); }
        Abilities = m.GetBFAbilities();
        int[] Data = m.GetBFDamage( this );
        S = Data[BFConstants.BF_SHORT];
        M = Data[BFConstants.BF_MEDIUM];
        L = Data[BFConstants.BF_LONG];
        E = Data[BFConstants.BF_EXTREME];
        OV = Data[BFConstants.BF_OV];
        BasePV = m.GetBFPoints();
        PV = BasePV;

        Wt = m.GetBFSize();
        Armor = m.GetBFArmor();
        Internal = m.GetBFStructure();

        MV = m.GetBFPrimeMovement() + m.GetBFPrimeMovementMode();
        TerrainMV = (m.GetBFPrimeMovement() * 2) + m.GetBFPrimeMovementMode();
        if ( m.GetBFSecondaryMovement() != 0 ) {
            if ( !m.GetBFSecondaryMovementMode().isEmpty() &&
                 !m.GetBFSecondaryMovementMode().isEmpty() ) { MV = m.GetBFPrimeMovement() + ""; }
            MV += "/" + m.GetBFSecondaryMovement() + m.GetBFSecondaryMovementMode();
            TerrainMV += "/" + ( m.GetBFSecondaryMovement() * 2 ) + m.GetBFSecondaryMovementMode();
        }

        Image = m.GetSSWImage();
    }

    public BattleForceStats( Mech m, String Unit, int Gunnery, int Piloting ) {
        this(m);
        this.Unit = Unit;
        setGunnery(Gunnery);
        setPiloting(Piloting);
    }

    public BattleForceStats( Node n ) throws Exception {
        try {
            BasePV = Integer.parseInt(n.getAttributes().getNamedItem("pv").getTextContent());
            PV = BasePV;
            MV = n.getAttributes().getNamedItem("mv").getTextContent();
            setTerrain();
            Wt = Integer.parseInt(n.getAttributes().getNamedItem("wt").getTextContent());
            S = Integer.parseInt(n.getAttributes().getNamedItem("s").getTextContent());
            M = Integer.parseInt(n.getAttributes().getNamedItem("m").getTextContent());
            L = Integer.parseInt(n.getAttributes().getNamedItem("l").getTextContent());
            E = Integer.parseInt(n.getAttributes().getNamedItem("e").getTextContent());
            OV = Integer.parseInt(n.getAttributes().getNamedItem("ov").getTextContent());
            Armor = Integer.parseInt(n.getAttributes().getNamedItem("armor").getTextContent());
            Internal = Integer.parseInt(n.getAttributes().getNamedItem("internal").getTextContent());
            String abilities = n.getAttributes().getNamedItem("abilities").getTextContent();
            if ( !abilities.isEmpty() && abilities.contains(",") ) {
                String[] ability = abilities.split(",");
                for ( String item : ability ) {
                    Abilities.add(item.trim());
                }
            }
        } catch ( Exception e ) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public BattleForceStats( String[] items ) {
        Element = items[0];
        BasePV = Integer.parseInt(items[1]);
        Wt = Integer.parseInt(items[2]);
        MV = items[3];
        setTerrain();
        S = Integer.parseInt(items[4]);
        M = Integer.parseInt(items[5]);
        L = Integer.parseInt(items[6]);
        E = Integer.parseInt(items[7]);
        OV = Integer.parseInt(items[8]);
        Armor = Integer.parseInt(items[9]);
        Internal = Integer.parseInt(items[10]);
        if ( items[11].contains("~") ) {
            String[] Ability = items[11].split("~");
            for ( String item : Ability ) {
                Abilities.add(item.trim());
            }
        }
        PV = BasePV;
    }

    private void updateSkill() {
        int Total = Gunnery + Piloting;
        if ( Total <= 1 ) {
            Skill = 0;
        } else if ( Total <= 3 ) {
            Skill = 1;
        } else if ( Total <= 5 ) {
            Skill = 2;
        } else if ( Total <= 7 ) {
            Skill = 3;
        } else if ( Total <= 9 ) {
            Skill = 4;
        } else if ( Total <= 11 ) {
            Skill = 5;
        } else if ( Total <= 13 ) {
            Skill = 6;
        } else {
            Skill = 7;
        }
        updatePointValue();
    }

    private void updatePointValue() {
        PV = (int)((int) BasePV * Mods[Skill]);
    }

    public void SerializeXML( BufferedWriter file, int Tabs ) throws IOException {
        file.write( CommonTools.Tabs(Tabs) +
                    "<battleforce pv=\"" + getBasePV() + "\" " +
                    "wt=\"" + getWeight() + "\" " +
                    "mv=\"" + getMovement() + "\" " +
                    "s=\"" + getShort() + "\" " +
                    "m=\"" + getMedium() + "\" " +
                    "l=\"" + getLong() + "\" " +
                    "e=\"" + getExtreme() + "\" " +
                    "ov=\"" + getOverheat() + "\" " +
                    "armor=\"" + getArmor() + "\" " +
                    "internal=\"" + getInternal() + "\" " +
                    "abilities=\"" + getAbilitiesString() + "\" />");
    }

    public String SerializeCSV() {
        String data = "";

        data += CSVFormat(getElement());
        data += CSVFormat(PV);
        data += CSVFormat(Wt);
        data += CSVFormat(MV);
        data += CSVFormat(getShort());
        data += CSVFormat(getMedium());
        data += CSVFormat(getLong());
        data += CSVFormat(getExtreme());
        data += CSVFormat(getOverheat());
        data += CSVFormat(getArmor());
        data += CSVFormat(getInternal());
        data += CSVFormat(getAbilities().toString().replace("[", "").replace("]", ""));

        return data.substring(0, data.length()-2);
    }

    public String CSVFormat( String data ) {
        return "\"" + data + "\", ";
    }

    public String CSVFormat( int data ) {
        return data + ",";
    }

    public Vector<String> getAbilities() {
        return Abilities;
    }


    public String getAbilitiesString() {
        String retval = "";

        for ( int i = 0; i < Abilities.size(); i++ )
        {
            retval += Abilities.get(i);
            if ( i != Abilities.size() - 1 )
                retval += ", ";
        }

        return retval.replace("[", "").replace("]", "");
    }

    public Vector<String> getFilteredAbilities() {
        Vector<String> filtered = new Vector<String>();
         for ( String ability : Abilities ) {
            if ( !ability.contains("AC") && !ability.contains("SRM") && !ability.contains("LRM") ) {
                filtered.add(ability);
            }
         }
         return filtered;
    }

    public Vector<String[]> getDamageAbilities() {
        Vector<String[]> list = new Vector<String[]>();
        for ( String ability : Abilities ) {
            if ( ability.contains("AC") || ability.contains("SRM") || ability.contains("LRM") ) {
                String[] info = new String[5];
                info[0] = ability.substring(0, 3).trim();
                if (info[0].length() == 2) info[0] = "  " + info[0];
                String[] data = ability.replace("AC ", "").replace("SRM ", "").replace("LRM ", "").split("/");
                info[1] = data[0];
                info[2] = data[1];
                info[3] = data[2];
                if ( data.length == 4 ) {
                    info[4] = data[3];
                } else {
                    info[4] = "0";
                }
                list.add(info);
            }
        }
        return list;
    }

    public void addAbility(String s)
    {
        if ( !Abilities.contains(s) ) { Abilities.add(s); }
    }

    public Vector<String> getAltMunitions() {
        return AltMunitions;
    }

    public String getAltMunitionsString() {
        String retval = "";
        for ( String munition : AltMunitions ) {
            retval += munition;
        }
        return retval;
    }

    public void addAltMunition(String s) {
        AltMunitions.add(s);
    }

    public int getShort() {
        return S;
    }

    public int getMedium() {
        return M;
    }

    public int getLong() {
        return L;
    }

    public int getExtreme() {
        return E;
    }

    public int getWeight() {
        return Wt;
    }

    public int getOverheat() {
        return OV;
    }

    public int getArmor() {
        return Armor;
    }

    public int getInternal() {
        return Internal;
    }

    public String getElement() {
        return Element;
    }

    public String getMovement() {
        return MV;
    }

    public String getMovement(boolean useTerrain) {
        if ( useTerrain ) {
            if ( TerrainMV.isEmpty() ) { setTerrain(); }
            return TerrainMV;
        } else {
            return MV;
        }
    }

    public int getPointValue() {
        return PV;
    }

    public int getSkill() {
        return Skill;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }

    public int getGunnery() {
        return Gunnery;
    }

    public void setGunnery(int Gunnery) {
        this.Gunnery = Gunnery;
        updateSkill();
    }

    public int getPiloting() {
        return Piloting;
    }

    public void setPiloting(int Piloting) {
        this.Piloting = Piloting;
        updateSkill();
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public int getBasePV() {
        return BasePV;
    }

    public void setElement(String Element) {
        this.Element = Element;
    }

    public String getWarrior() {
        return Warrior;
    }

    public void setWarrior(String Warrior) {
        this.Warrior = Warrior;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public void setTerrain() {
        if ( MV.length() == 0 ) { return; }
        for ( String s : MV.split("/") ) {
            TerrainMV = (Integer.parseInt(s.replace("j", "")) * 2) + "";
            if (s.contains("j")) TerrainMV += "j";
            TerrainMV += "/";
        }
        TerrainMV = TerrainMV.substring(0, TerrainMV.length()-1);
    }
}
