/*
Copyright (c) 2008, George Blouin Jr. (skyhigh@solaris7.com)
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of
conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice, this list
of conditions and the following disclaimer in the documentation and/or other materials
provided with the distribution.
    * Neither the name of George Blouin Jr nor the names of contributors may be
used to endorse or promote products derived from this software without specific prior
written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package Force;

import common.CommonTools;
import battleforce.BattleForceStats;
import components.Mech;
import list.MechListData;
import filehandlers.MechReader;
import Print.ForceListPrinter;

import Print.PrintConsts;
import filehandlers.FileCommon;
import filehandlers.MechWriter;
import filehandlers.Media;
import java.io.BufferedWriter;
import java.io.IOException;
import list.view.Column;
import org.w3c.dom.Node;

public class Unit implements ifSerializable {
    public String TypeModel = "",
                  Type = "",
                  Model = "",
                  Info = "",
                  C3Type = "";
    private String Mechwarrior = "";
    public String Filename = "",
                  Configuration = "";
    private String Group = "",
                    prevGroup = "";
    private String MechwarriorQuirks = "";
    public String UnitQuirks = "";
    public float BaseBV = 0.0f,
                 MiscMod = 1.0f,
                 Tonnage = 20.0f,
                 SkillsBV = 0.0f,
                 ModifierBV = 0.0f,
                 C3BV = 0.0f;
    private float ForceC3BV = 0.0f;
    public float TotalBV = 0.0f;
    private int Piloting = 5;
    private int Gunnery = 4;
    public int UnitType = CommonTools.BattleMech;
    public Warrior warrior = new Warrior();
    public boolean UsingC3 = false,
                    C3Available = false;
    private boolean isOmni = false;
    public Mech m = null;
    private BattleForceStats BFStats = new BattleForceStats();

    public Unit(){
    }

    public Unit( MechListData m ) {
        this.Type = m.getName();
        this.Model = m.getModel();
        this.TypeModel = m.getName() + " "  + m.getModel();
        this.Tonnage = m.getTonnage();
        this.BaseBV = m.getBV();
        this.Filename = m.getFilename();
        this.Configuration = m.getConfig();
        this.Info = m.getInfo();
        this.BFStats = m.getBattleForceStats();
        if ( Info.contains("C3") ) {
            C3Available = true;
            for ( String Item : Info.split(" ") ) {
                if ( Item.contains("C3") ) C3Type = Item.trim();
            }
        }
        Refresh();
    }

    public Unit( Mech m ) {
        Type = m.GetName();
        Model = m.GetModel();
        TypeModel = m.GetFullName();
        Tonnage = m.GetTonnage();
        BaseBV = m.GetCurrentBV();
        Info = m.GetChatInfo();

        if ( m.HasC3() ) {
            C3Available = true;
            for ( String Item : Info.split(",") ) {
                if ( Item.contains("C3") ) C3Type = Item.trim();
            }
        }

        if ( m.IsOmnimech() ) {
            setOmni(true);
            Configuration = m.GetLoadout().GetName();
        }
        this.m = m;
        Refresh();
    }

    public Unit( Node n ) throws Exception {
        for (int i=0; i < n.getChildNodes().getLength(); i++) {
            String nodeName = n.getChildNodes().item(i).getNodeName();

            if ( !nodeName.equals("#text") ) {
                //Previous File structure
                if (nodeName.equals("type")) {Type = FileCommon.DecodeFluff(n.getChildNodes().item(i).getTextContent().trim());}
                if (nodeName.equals("model")) {Model = FileCommon.DecodeFluff(n.getChildNodes().item(i).getTextContent().trim());}
                if (nodeName.equals("config")) {Configuration = n.getChildNodes().item(i).getTextContent().trim();}
                if (nodeName.equals("tonnage")) {Tonnage = Float.parseFloat(n.getChildNodes().item(i).getTextContent());}
                if (nodeName.equals("basebv")) {BaseBV = Float.parseFloat(n.getChildNodes().item(i).getTextContent());}
                if (nodeName.equals("modifier")) {MiscMod = Float.parseFloat(n.getChildNodes().item(i).getTextContent());}
                if (nodeName.equals("piloting")) {Piloting = Integer.parseInt(n.getChildNodes().item(i).getTextContent());}
                if (nodeName.equals("gunnery")) {Gunnery = Integer.parseInt(n.getChildNodes().item(i).getTextContent());}
                if (nodeName.equals("unittype")) {UnitType = Integer.parseInt(n.getChildNodes().item(i).getTextContent());}
                if (nodeName.equals("usingc3")) {UsingC3 = Boolean.parseBoolean(n.getChildNodes().item(i).getTextContent());}
                if (nodeName.equals("mechwarrior")) {Mechwarrior = n.getChildNodes().item(i).getTextContent().trim();}
                if (nodeName.equals("ssw")) {Filename = n.getChildNodes().item(i).getTextContent().trim();}
                if (nodeName.equals("group")) {Group = n.getChildNodes().item(i).getTextContent().trim();}
                if (nodeName.equals("mechwarriorquirks")) {MechwarriorQuirks = n.getChildNodes().item(i).getTextContent().trim();}
                if (nodeName.equals("unitquirks")) {UnitQuirks = n.getChildNodes().item(i).getTextContent().trim();}
            }
        }
        this.Refresh();
        TypeModel = Type + " " + Model;
        this.warrior.setGunnery(Gunnery);
        this.warrior.setPiloting(Piloting);
        this.warrior.setName(Mechwarrior);
        this.warrior.setQuirks(MechwarriorQuirks);
    }

    public Unit(Node n, int Version) throws Exception {
        try {
            this.Type = FileCommon.DecodeFluff(n.getAttributes().getNamedItem("type").getTextContent().trim());
            this.Model = FileCommon.DecodeFluff(n.getAttributes().getNamedItem("model").getTextContent().trim());
            TypeModel = Type + " " + Model;
            this.Configuration = n.getAttributes().getNamedItem("config").getTextContent().trim();
            if ( !Configuration.isEmpty() ) isOmni = true;
            this.Tonnage = Float.parseFloat(n.getAttributes().getNamedItem("tonnage").getTextContent().trim());
            this.BaseBV = Float.parseFloat(n.getAttributes().getNamedItem("bv").getTextContent().trim());
            this.UnitType = Integer.parseInt(n.getAttributes().getNamedItem("design").getTextContent().trim());
            this.Filename = n.getAttributes().getNamedItem("file").getTextContent().trim();
            this.UsingC3 = Boolean.parseBoolean(n.getAttributes().getNamedItem("c3status").getTextContent().trim());

            for (int i = 0; i < n.getChildNodes().getLength(); i++) {
                Node node = n.getChildNodes().item(i);
                if (node.getNodeName().equals("quirks")) {
                    this.UnitQuirks = node.getTextContent().trim();
                }
                if (node.getNodeName().equals("warrior")) {
                    try {
                        this.warrior = new Warrior(node);
                        this.Gunnery = warrior.getGunnery();
                        this.Piloting = warrior.getPiloting();
                        this.BFStats.setGunnery(Gunnery);
                        this.BFStats.setPiloting(Piloting);
                        this.MechwarriorQuirks = warrior.getQuirks();
                        this.Mechwarrior = (warrior.getRank() + " " + warrior.getName()).trim();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        throw ex;
                    }
                }
                if (node.getNodeName().equals("battleforce")) {
                    this.BFStats = new BattleForceStats(node);
                    this.BFStats.setElement(this.TypeModel);
                    this.BFStats.setPiloting(Piloting);
                    this.BFStats.setGunnery(Gunnery);
                    this.BFStats.setWarrior(warrior.getName());
                    this.BFStats.setName(this.Type);
                    this.BFStats.setModel(this.Model);
                }
                if (node.getNodeName().equals("info")) {
                    this.Info = node.getTextContent().trim();
                    if (Info.contains("C3")) {
                        C3Available = true;
                        for ( String s : Info.split(" ") ) {
                            if ( s.startsWith("C3") ) C3Type = s.trim();
                        }
                    }
                }

                if ( node.getNodeName().equals("mech")) {
                    try {
                        MechReader mread = new MechReader();
                        m = mread.ReadMech(node);
                        if ( !Configuration.isEmpty() ) m.SetCurLoadout(Configuration);
                        BFStats = new BattleForceStats(m);
                    } catch (Exception e) {
                        Media.Messager("Error loading Mech " + e.getMessage());
                    }
                }
                this.Refresh();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void Refresh() {
        SkillsBV = 0;
        ModifierBV = 0;
        TotalBV = 0;
        //C3BV = 0;
        //if (UsingC3) { C3BV += BaseBV * .05;}
        SkillsBV = CommonTools.GetSkillBV((BaseBV + ForceC3BV), getGunnery(), getPiloting());
        ModifierBV = CommonTools.GetModifierBV(SkillsBV, MiscMod);
        TotalBV = CommonTools.GetFullAdjustedBV((BaseBV + ForceC3BV), getGunnery(), getPiloting(), MiscMod);
    }

    public void UpdateByMech() {
        LoadMech();
        if ( m != null ) {
            TypeModel = m.GetFullName();
            Configuration = m.GetLoadout().GetName();
            BaseBV = m.GetCurrentBV();
            Info = m.GetChatInfo();
            if (Info.contains("C3")) {
                C3Available = true;
                for ( String s : Info.split(" ") ) {
                    if ( s.startsWith("C3") ) C3Type = s.trim();
                }
            }
            BFStats = new BattleForceStats(m);
        }
        Refresh();
    }

    public String GetSkills(){
        return getGunnery() + "/" + getPiloting();
    }

    public void RenderPrint(ForceListPrinter p) {
        p.setFont(PrintConsts.PlainFont);
        p.WriteStr(TypeModel, 120);
        p.WriteStr(getMechwarrior(), 140);
        p.WriteStr(CommonTools.UnitTypes[UnitType], 60);
        p.WriteStr(String.format("%1$,.2f", Tonnage), 50);
        p.WriteStr(String.format("%1$,.0f", BaseBV), 40);
        p.WriteStr(GetSkills(), 30);
        p.WriteStr(String.format("%1$,.2f", MiscMod), 40);
        p.WriteStr(Boolean.valueOf(UsingC3).toString(), 30);
        p.WriteStr(String.format("%1$,.0f", TotalBV), 0);
        p.NewLine();
    }

    public void SerializeXML(BufferedWriter file) throws IOException {
        LoadMech();
        MechWriter mwrite = new MechWriter(m);
        file.write(CommonTools.Tabs(4) + "<unit type=\"" + FileCommon.EncodeFluff(this.Type) + "\" model=\"" + FileCommon.EncodeFluff(this.Model) + "\" config=\"" + this.Configuration + "\" tonnage=\"" + this.Tonnage + "\" bv=\"" + this.BaseBV + "\" design=\"" + this.UnitType + "\" file=\"" + this.Filename + "\" c3status=\"" + this.UsingC3 + "\">");
        file.newLine();
        BFStats.SerializeXML(file, 5);
        file.newLine();
        file.write(CommonTools.Tabs(5) + "<info>" + this.Info + "</info>");
        file.newLine();
        file.write(CommonTools.Tabs(5) + "<quirks>" + this.UnitQuirks + "</quirks>");
        file.newLine();
        warrior.SerializeXML(file);
        mwrite.WriteXML(file);
        file.write(CommonTools.Tabs(4) + "</unit>");
        file.newLine();
    }

    public void SerializeMUL(BufferedWriter file) throws IOException {
        if ( this.Type.contains("(") && this.Type.contains(")") ) {
            this.Type = this.Type.substring(0, this.Type.indexOf(" (")).trim();
        }

        this.Model.replace("Alternate Configuration", "");
        this.Model.replace("Alternate", "");
        this.Model.replace("Alt", "");
        this.Model.trim();

        file.write(CommonTools.tab + "<entity chassis=\"" + this.Type + "\" model=\"" + this.Model + "\">");
        file.newLine();
        file.write(CommonTools.tab + CommonTools.tab + "<pilot name=\"" + this.getMechwarrior() + "\" gunnery=\"" + this.getGunnery() + "\" piloting=\"" + this.getPiloting() + "\" />");
        file.newLine();
        file.write(CommonTools.tab + "</entity>");
        file.newLine();
    }

    public String SerializeClipboard() {
        String data = "";

        for ( Column c : Force.ScenarioClipboardColumns() ) {
            data += CommonTools.spaceRight(convertColumn(c), c.preferredWidth) + CommonTools.Tab;
        }
        return data;
    }

    private String convertColumn( Column c ) {
        if ( c.Title.equals("Unit") ) {
            return this.TypeModel.trim();
        } else if ( c.Title.equals("Tons") ) {
            return String.format("%1$,.0f", Tonnage);
        } else if ( c.Title.equals("BV") ) {
            return String.format("%1$,.0f", BaseBV);
        } else if ( c.Title.equals("Mechwarrior") ) {
            return this.getMechwarrior();
        } else if ( c.Title.equals("Lance/Star") ) {
            return this.Group;
        } else if ( c.Title.equals("G/P") ) {
            return this.GetSkills();
        } else if ( c.Title.equals("Adj BV") ) {
            return String.format("%1$,.0f", TotalBV);
        } else {
            return "";
        }
    }

    public String SerializeData() {
        return "";
    }

    @Override
    public String toString() {
        return TypeModel + " (" + warrior.getName() + " " + warrior.getGunnery() + "/" + warrior.getPiloting() + ")";
    }

    public void LoadMech() {
        if ( m == null ) {
            try {
                MechReader reader = new MechReader();
                this.m = reader.ReadMech( this.Filename );
                if ( ! this.Configuration.isEmpty() ) {
                    this.m.SetCurLoadout(this.Configuration.trim());
                }
                if ( BFStats.getPointValue() == 0 ) {
                    BFStats = new BattleForceStats(m);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public BattleForceStats getBFStats() {
        if ( BFStats != null ) {
            BFStats.setName(this.Type);
            BFStats.setModel(this.Model);
            BFStats.setWarrior(warrior.getName());
            BFStats.setGunnery(warrior.getGunnery());
            BFStats.setPiloting(warrior.getPiloting());
            return BFStats;
        }

        LoadMech();
        if ( m != null ) {
            BFStats = new BattleForceStats(m, Group, getGunnery(), getPiloting());
            BFStats.setWarrior(warrior.getName());
        }
        return BFStats;
    }

    public String getMechwarrior() {
        return warrior.getName();
    }

    public void setMechwarrior(String Mechwarrior) {
        warrior.setName(Mechwarrior);
        BFStats.setWarrior(Mechwarrior);
    }

    public String getMechwarriorQuirks() {
        return warrior.getQuirks();
    }

    public void setMechwarriorQuirks(String MechwarriorQuirks) {
        warrior.setQuirks(MechwarriorQuirks);
    }

    public int getPiloting() {
        return warrior.getPiloting();
    }

    public void setPiloting(int Piloting) {
        warrior.setPiloting(Piloting);
        BFStats.setPiloting(Piloting);
    }

    public int getGunnery() {
        return warrior.getGunnery();
    }

    public void setGunnery(int Gunnery) {
        warrior.setGunnery(Gunnery);
        BFStats.setGunnery(Gunnery);
    }

    public boolean IsOmni() {
        return isOmni;
    }

    public void setOmni(boolean isOmni) {
        this.isOmni = isOmni;
    }
    
    public String getInfo() {
        return Info;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String Group) {
        prevGroup = this.Group;
        this.Group = Group;
    }

    public String getPrevGroup() {
        return prevGroup;
    }

    public String isUsingC3() {
        String val = "N/A";
        if ( C3Available ) {
            if ( UsingC3 ) {
                val = "Yes";
            } else {
                val = "No";
            }
            val += " (" + C3Type + ")";
        }
        return val;
    }

    public float getForceC3BV() {
        return ForceC3BV;
    }

    public void setForceC3BV(float ForceC3BV) {
        this.ForceC3BV = ForceC3BV;
        this.Refresh();
    }
}