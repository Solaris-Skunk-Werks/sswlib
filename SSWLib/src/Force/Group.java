
package Force;

import common.CommonTools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Vector;
import org.w3c.dom.Node;
import battleforce.BattleForce;
import battleforce.BattleForceStats;
import list.view.Column;

public class Group {
    private String Name = "",
                   Type = BattleForce.InnerSphere,
                   Logo = "";
    private Vector<Unit> Units = new Vector<Unit>();
    private Force force;
    public float TotalBV = 0.0f;
    public int TotalPV = 0;

    public Group( String Name, String Type, Force force ) {
        this.Name = Name;
        this.Type = Type;
        this.force = force;
        this.TotalBV = force.TotalForceBVAdjusted;
        this.Logo = force.LogoPath;
    }

    public Group( Node node, int Version, Force force ) {
        this.force = force;
        this.Name = node.getAttributes().getNamedItem("name").getTextContent().trim();
        if ( node.getAttributes().getNamedItem("logo") != null ) { this.Logo = node.getAttributes().getNamedItem("logo").getTextContent().trim(); }
        for (int i=0; i < node.getChildNodes().getLength(); i++) {
            Node n = node.getChildNodes().item(i);
            if ( n.getNodeName().equals("unit") ) {
                Unit u = new Unit(n, Version);
                u.setGroup(Name);
                u.Refresh();
                Units.add( u );
            }
        }
        updateBV();
    }

    @Override
    public String toString() {
        String data = force.ForceName + ": " + Name + ": " + Type + " (" + Units.size() + " Units)";
        if ( data.startsWith(":") ) { data = "Unnamed Force: Blank Group: " + Type + " (" + Units.size() + " Units)" ; }
        return data;
    }

    public void updateBV() {
        TotalBV = 0.0f;
        for ( Unit u : Units ) {
            TotalBV += u.TotalBV;
        }
    }

    public void AddUnit( Unit u ) {
        Units.add(u);
    }

    public void Copy( Group g ) {
        this.Name = g.getName();
        this.Type = g.getType();
        this.Logo = g.getLogo();
        this.Units = g.getUnits();
        this.force = g.getForce();
    }

    public Vector<BattleForce> toBattleForce( int SizeLimit ) {
        Vector<BattleForce> bforces = new Vector<BattleForce>();
        BattleForce bf = new BattleForce();
        bf.Type = getType();
        bf.ForceName = getForce().ForceName;
        bf.LogoPath = getLogo();
        for ( Unit u : getUnits() ) {
            u.LoadMech();
            BattleForceStats stat = new BattleForceStats(u.m,u.getGroup(), u.getGunnery(),u.getPiloting());
            stat.setWarrior(u.getMechwarrior());
            bf.BattleForceStats.add(stat);
            if ( bf.BattleForceStats.size() == SizeLimit ) {
                bforces.add(bf);
                bf = new BattleForce();
            }
        }
        if ( bf.BattleForceStats.size() > 0 ) {
            bforces.add(bf);
        }
        return bforces;
    }

    public void SerializeXML( BufferedWriter file ) throws IOException {
        file.write( CommonTools.Tabs(3) + "<group name=\"" + this.Name + "\" logo=\"" + this.Logo + "\" >");
        file.newLine();
        for ( Unit u : Units ) {
            u.SerializeXML(file);
        }
        file.write( CommonTools.Tabs(3) + "</group>");
        file.newLine();
    }

    public String SerializeClipboard() {
        String data = "";
        for ( Column c : Force.ScenarioClipboardColumns() ) {
            if ( c.Title.equals("Unit") && !getName().isEmpty() ) {
                data += CommonTools.spaceRight(getName(), c.preferredWidth) + CommonTools.Tab;
            } else {
                data += CommonTools.spaceRight(c.Title, c.preferredWidth) + CommonTools.Tab;
            }
        }
        data += CommonTools.NL;

        for ( Unit u : Units ) {
            data += u.SerializeClipboard() + CommonTools.NL;
        }

        if ( force.Groups.size() > 1 ) {
            for ( Column c : Force.ScenarioClipboardColumns() ) {
                if ( c.Title.equals("Adj BV") ) {
                    data += String.format("%1$,.0f", getTotalBV());
                } else {
                    data += CommonTools.spaceRight("", c.preferredWidth) + CommonTools.Tab;
                }
            }
        }
        data += CommonTools.NL + CommonTools.NL;
        return data;
    }

    public Vector<Unit> getUnits() {
        return Units;
    }

    public String getName() {
        return Name;
    }

    public String getType() {
        return Type;
    }

    public Force getForce() {
        return force;
    }

    public String getLogo() {
        if ( Logo.isEmpty() ) { Logo = force.LogoPath; }
        return Logo;
    }

    public void setLogo( String logoPath ) {
        this.Logo = logoPath;
    }

    public float getTotalTonnage() {
        float TotTons = 0.0f;
        for ( Unit u : Units ) {
            TotTons += u.Tonnage;
        }
        return TotTons;
    }

    public float getTotalBaseBV() {
        float BaseBV = 0.0f;
        for ( Unit u : Units ) {
            BaseBV += u.BaseBV;
        }
        return BaseBV;
    }

    public float getTotalBV() {
        TotalBV = 0.0f;
        for ( Unit u : Units ) {
            TotalBV += u.TotalBV;
        }
        return TotalBV;
    }
    
    public int getTotalPV() {
        TotalPV = 0;
        for ( Unit u : Units ) {
            TotalPV += u.getBFStats().getPointValue();
        }
        return TotalPV;
    }

    public void setUnits(Vector<Unit> Units) {
        this.Units = Units;
    }
}
