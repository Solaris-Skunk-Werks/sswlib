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

package components;

import battleforce.BattleForceStats;
import battleforce.ifBattleforce;
import common.CommonTools;
import java.util.Hashtable;
import java.util.Vector;
import java.util.prefs.Preferences;
import states.ifCombatVehicle;
import states.stCVDisplacement;
import states.stCVHover;
import states.stCVHydrofoil;
import states.stCVSubmarine;
import states.stCVTracked;
import states.stCVVTOL;
import states.stCVWheeled;
import states.stCVWiGE;
import visitors.ifVisitor;

public class CombatVehicle implements ifUnit, ifBattleforce {

    public static ifCombatVehicle getWheeled() {
        return Wheeled;
    }

    public static void setWheeled(ifCombatVehicle aWheeled) {
        Wheeled = aWheeled;
    }

    public static ifCombatVehicle getTracked() {
        return Tracked;
    }

    public static void setTracked(ifCombatVehicle aTracked) {
        Tracked = aTracked;
    }

    public static ifCombatVehicle getHover() {
        return Hover;
    }

    public static void setHover(ifCombatVehicle aHover) {
        Hover = aHover;
    }

    public static ifCombatVehicle getVTOL() {
        return VTOL;
    }

    public static void setVTOL(ifCombatVehicle aVTOL) {
        VTOL = aVTOL;
    }

    public static ifCombatVehicle getWiGE() {
        return WiGE;
    }

    public static void setWiGE(ifCombatVehicle aWiGE) {
        WiGE = aWiGE;
    }

    public static ifCombatVehicle getDisplacement() {
        return Displacement;
    }

    public static void setDisplacement(ifCombatVehicle aDisplacement) {
        Displacement = aDisplacement;
    }

    public static ifCombatVehicle getHydrofoil() {
        return Hydrofoil;
    }

    public static void setHydrofoil(ifCombatVehicle aHydrofoil) {
        Hydrofoil = aHydrofoil;
    }

    public static ifCombatVehicle getSubmarine() {
        return Submarine;
    }

    public static void setSubmarine(ifCombatVehicle aSubmarine) {
        Submarine = aSubmarine;
    }

    public static AvailableCode getOmniAC() {
        return OmniAC;
    }

    public static void setOmniAC(AvailableCode aOmniAC) {
        OmniAC = aOmniAC;
    }

    public static AvailableCode getDualTurretAC() {
        return DualTurretAC;
    }

    public static void setDualTurretAC(AvailableCode aDualTurretAC) {
        DualTurretAC = aDualTurretAC;
    }

    public static AvailableCode getChinTurretAC() {
        return ChinTurretAC;
    }

    public static void setChinTurretAC(AvailableCode aChinTurretAC) {
        ChinTurretAC = aChinTurretAC;
    }

    public static AvailableCode getSponsoonAC() {
        return SponsoonAC;
    }

    public static void setSponsoonAC(AvailableCode aSponsoonAC) {
        SponsoonAC = aSponsoonAC;
    }
    // Declares
    private String Name = "",
                   Model = "",
                   Overview = "",
                   Capabilities = "",
                   History = "",
                   Deployment = "",
                   Variants = "",
                   Notables = "",
                   Additional = "",
                   Company = "",
                   Location = "",
                   EngineManufacturer = "",
                   ArmorModel = "",
                   ChassisModel = "",
                   JJModel = "",
                   CommSystem = "",
                   TandTSystem = "",
                   Solaris7ID = "0",
                   Solaris7ImageID = "0",
                   SSWImage = "",
                   Source = "";
    private int HeatSinks = 0,
                Tonnage = 20,
                CruiseMP,
                Year;
    private double JJMult;
    private boolean Omni = false,
                    HasEnviroSealing = false,
                    FractionalAccounting = false,
                    Changed = false,
                    HasTurret1 = false,
                    HasTurret2 = false;
    private static ifCombatVehicle Wheeled = new stCVWheeled(),
                                 Tracked = new stCVTracked(),
                                 Hover = new stCVHover(),
                                 VTOL = new stCVVTOL(),
                                 WiGE = new stCVWiGE(),
                                 Displacement = new stCVDisplacement(),
                                 Hydrofoil = new stCVHydrofoil(),
                                 Submarine = new stCVSubmarine();
    private Engine CurEngine = new Engine( this );
    private ifCombatVehicle CurConfig = Tracked;
    private Vector<CVLoadout> Loadouts = new Vector<CVLoadout>();
    private CVLoadout MainLoadout,
                        CurLoadout;
    private CVArmor CurArmor = new CVArmor( this );
    private Hashtable Lookup = new Hashtable();
    private static AvailableCode OmniAC = new AvailableCode( AvailableCode.TECH_BOTH ),
                                 DualTurretAC = new AvailableCode( AvailableCode.TECH_BOTH ),
                                 ChinTurretAC = new AvailableCode( AvailableCode.TECH_BOTH ),
                                 SponsoonAC = new AvailableCode( AvailableCode.TECH_BOTH );
    private Preferences Prefs;

    public CombatVehicle() {
        OmniAC.SetCodes( 'E', 'X', 'X', 'E', 'E', 'X', 'E', 'E' );
        OmniAC.SetFactions( "", "", "", "", "", "", "", "" );
        OmniAC.SetISDates( 0, 0, false, 3052, 0, 0, false, false );
        OmniAC.SetCLDates( 0, 0, false, 2854, 0, 0, false, false );
        OmniAC.SetRulesLevels( AvailableCode.RULES_TOURNAMENT, AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_TOURNAMENT, AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_UNALLOWED );

        DualTurretAC.SetCodes( 'B', 'F', 'X', 'F', 'B', 'X', 'E', 'E' );
        DualTurretAC.SetFactions( "", "", "PS", "", "", "", "PS", "" );
        DualTurretAC.SetISDates( 0, 0, false, 1950, 0, 0, false, false );
        DualTurretAC.SetCLDates( 0, 0, false, 1950, 0, 0, false, false );
        DualTurretAC.SetRulesLevels( AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_EXPERIMENTAL, AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_UNALLOWED );

        ChinTurretAC.SetCodes( 'B', 'F', 'F', 'F', 'B', 'X', 'E', 'E' );
        ChinTurretAC.SetFactions( "", "", "PS", "", "", "", "PS", "" );
        ChinTurretAC.SetISDates( 0, 0, false, 1950, 0, 0, false, false );
        ChinTurretAC.SetCLDates( 0, 0, false, 1950, 0, 0, false, false );
        ChinTurretAC.SetRulesLevels( AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_EXPERIMENTAL, AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_UNALLOWED );

        SponsoonAC.SetCodes( 'B', 'F', 'X', 'F', 'B', 'X', 'E', 'E' );
        SponsoonAC.SetFactions( "", "", "PS", "", "", "", "PS", "" );
        SponsoonAC.SetISDates( 0, 0, false, 1950, 0, 0, false, false );
        SponsoonAC.SetCLDates( 0, 0, false, 1950, 0, 0, false, false );
        SponsoonAC.SetRulesLevels( AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_EXPERIMENTAL, AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_UNALLOWED );

        HeatSinks = CurEngine.FreeHeatSinks();

        CurLoadout = new CVLoadout(this);
        MainLoadout = CurLoadout;
    }

    public void Visit( ifVisitor v ) throws Exception {
        v.Visit( this );
    }

    public void Validate() {
        if ( getTonnage() > getCurConfig().GetMaxTonnage() ) {
            setTonnage(getCurConfig().GetMaxTonnage());
        }
    }

    public void SetWheeled() {
        setCurConfig(getWheeled());
        SetChanged(true);
    }

    public void SetTracked() {
        setCurConfig(getTracked());
        SetChanged(true);
    }

    public void SetHover() {
        setCurConfig(getHover());
        SetChanged(true);
    }

    public void setVTOL() {
        setCurConfig(getVTOL());
        SetChanged(true);
    }

    public void SetWiGE() {
        setCurConfig(getWiGE());
        SetChanged(true);
    }

    public void SetDisplacement() {
        setCurConfig(getDisplacement());
        SetChanged(true);
    }

    public void SetHydrofoil() {
        setCurConfig(getHydrofoil());
        SetChanged(true);
    }

    public void SetSubmarine() {
        setCurConfig(getSubmarine());
        SetChanged(true);
    }

    public String GetMotiveLookupName() {
        return getCurConfig().GetMotiveLookupName();
    }

    public int GetMaxTonnage() {
        return getCurConfig().GetMaxTonnage();
    }

    public int GetTonnage() {
        return getTonnage();
    }

    public boolean CanUseJump() {
        return getCurConfig().CanUseJumpMP();
    }

    public boolean CanBeTrailer() {
        return getCurConfig().CanBeTrailer();
    }

    public boolean CanBeDuneBuggy() {
        return getCurConfig().CanBeDuneBuggy();
    }

    public int GetSuspensionFactor( int Tonnage ) {
        return getCurConfig().GetSuspensionFactor( Tonnage );
    }

    public float GetMinEngineWeight( int Tonnage ) {
        return getCurConfig().GetMinEngineWeight( Tonnage );
    }

    public float GetLiftEquipmentCostMultiplier() {
        return getCurConfig().GetLiftEquipmentCostMultiplier();
    }

    public boolean RequiresLiftEquipment() {
        return getCurConfig().RequiresLiftEquipment();
    }

    public boolean IsVTOL() {
        return getCurConfig().IsVTOL();
    }

    public boolean CanUseTurret() {
        if( IsVTOL() && CommonTools.IsAllowed( getChinTurretAC(),this) ) { return true; }
        return getCurConfig().CanUseTurret();
    }

    public boolean CanUseDualTurret() {
        if( CommonTools.IsAllowed( getDualTurretAC(),this) ) { return true; }
        return false;
    }

    public boolean CanUseSponsoon() {
        if( CommonTools.IsAllowed( getSponsoonAC(),this) ) { return true; }
        return false;
    }

    public boolean CanUseFlotationHull() {
        return getCurConfig().CanUseFlotationHull();
    }

    public boolean CanUseArmoredMotiveSystem() {
        return getCurConfig().CanUseArmoredMotiveSystem();
    }

    public boolean CanUseAmphibious() {
        return getCurConfig().CanUseAmphibious();
    }

    public boolean CanUseMinesweeper() {
        return getCurConfig().CanUseMinesweeper();
    }

    public CVArmor GetArmor() {
        return getCurArmor();
    }

    public ifCVLoadout GetLoadout() {
        return getCurLoadout();
    }

    public ifCVLoadout GetMainLoadout() {
        return getMainLoadout();
    }

    public boolean IsOmni() {
        return isOmni();
    }

    public int GetBaseRulesLevel() {
        return getMainLoadout().GetRulesLevel();
    }
    
    public Engine GetEngine() {
        return getCurEngine();
    }

    public int GetYear() {
        return getCurLoadout().GetYear();
    }

    public int GetCurrentBV() {
        // returns the final battle value of the combat vehicle
        //TODO Fix this!
        return 0;
    }

    public double GetTotalCost() {
        // final cost calculations
        //TODO Fix this!
        double base = ( 0 + 0 ) * 1;
        if( base - (int) base > 0.998 ) { base = (int) base; }
        return base + 0;
    }

    public double GetCurrentTons() {
        // returns the current total tonnage of the combat vehicle
        return GetTonnage();
    }

    public boolean UsingFractionalAccounting() {
        return isFractionalAccounting();
    }

    public void SetFractionalAccounting( boolean b ) {
        setFractionalAccounting(b);
    }

    public void SetChanged( boolean b ) {
        setChanged(b);
    }

    public boolean HasChanged() {
        return isChanged();
    }

    public void AddHeatSinks( int i ) {

    }

    public void SetHeatSinks( int i ) {

    }

    public void RemoveHeatSinks( int i ) {
        // be sure to check against the engine's free heat sinks!
    }

    public int NumHeatSinks() {
        return 0;
    }

    public double GetHeatSinkTonnage() {
        // remember to not include the engine's free sinks.
        return 0.0;
    }

    public boolean UsingTurret1() {
        return isHasTurret1();
    }

    public boolean UsingTurret2() {
        return isHasTurret2();
    }

    //Battleforce Specific Methods
    public int GetBFSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int GetBFPrimeMovement() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String GetBFPrimeMovementMode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int GetBFSecondaryMovement() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String GetBFSecondaryMovementMode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int GetBFArmor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int GetBFStructure() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int[] GetBFDamage(BattleForceStats bfs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector GetBFAbilities() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String GetBFConversionStr() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int GetBFPoints() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int GetAmmoCount( int ammoIndex )
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int GetUnitType() {
        return AvailableCode.UNIT_COMBATVEHICLE;
    }

    public int GetRulesLevel() {
        return getCurLoadout().GetRulesLevel();
    }

    public int GetTechbase() {
        return getCurLoadout().GetTechBase();
    }

    public int GetBaseTechbase() {
        return getMainLoadout().GetTechBase();
    }

    public int GetEra() {
        return getCurLoadout().GetEra();
    }

    public boolean IsYearRestricted() {
        return getCurLoadout().IsYearRestricted();
    }

    public boolean HasFHES() {
        return false;
    }
    
    public String GetName() {
        return Name;
    }
    
    public void setName(String Name) {
        this.Name = Name;
    }

    public String GetModel() {
        return Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String Overview) {
        this.Overview = Overview;
    }

    public String getCapabilities() {
        return Capabilities;
    }

    public void setCapabilities(String Capabilities) {
        this.Capabilities = Capabilities;
    }

    public String getHistory() {
        return History;
    }

    public void setHistory(String History) {
        this.History = History;
    }

    public String getDeployment() {
        return Deployment;
    }

    public void setDeployment(String Deployment) {
        this.Deployment = Deployment;
    }

    public String getVariants() {
        return Variants;
    }

    public void setVariants(String Variants) {
        this.Variants = Variants;
    }

    public String getNotables() {
        return Notables;
    }

    public void SetNotables(String Notables) {
        this.Notables = Notables;
    }

    public String GetAdditional() {
        return Additional;
    }

    public void SetAdditional(String Additional) {
        this.Additional = Additional;
    }

    public String GetCompany() {
        return Company;
    }

    public void SetCompany(String Company) {
        this.Company = Company;
    }

    public String GetLocation() {
        return Location;
    }

    public void SetLocation(String Location) {
        this.Location = Location;
    }

    public String GetEngineManufacturer() {
        return EngineManufacturer;
    }

    public void SetEngineManufacturer(String EngineManufacturer) {
        this.EngineManufacturer = EngineManufacturer;
    }

    public String GetArmorModel() {
        return ArmorModel;
    }

    public void SetArmorModel(String ArmorModel) {
        this.ArmorModel = ArmorModel;
    }

    public String GetChassisModel() {
        return ChassisModel;
    }

    public void SetChassisModel(String ChassisModel) {
        this.ChassisModel = ChassisModel;
    }

    public String GetJJModel() {
        return JJModel;
    }

    public void SetJJModel(String JJModel) {
        this.JJModel = JJModel;
    }

    public String GetCommSystem() {
        return CommSystem;
    }

    public void SetCommSystem(String CommSystem) {
        this.CommSystem = CommSystem;
    }

    public String GetTandTSystem() {
        return TandTSystem;
    }

    public void SetTandTSystem(String TandTSystem) {
        this.TandTSystem = TandTSystem;
    }

    public String GetSolaris7ID() {
        return Solaris7ID;
    }

    public void SetSolaris7ID(String Solaris7ID) {
        this.Solaris7ID = Solaris7ID;
    }

    public String GetSolaris7ImageID() {
        return Solaris7ImageID;
    }

    public void SetSolaris7ImageID(String Solaris7ImageID) {
        this.Solaris7ImageID = Solaris7ImageID;
    }

    public String GetSSWImage() {
        return SSWImage;
    }

    public void SetSSWImage(String SSWImage) {
        this.SSWImage = SSWImage;
    }

    public int getHeatSinks() {
        return HeatSinks;
    }

    public void setHeatSinks(int HeatSinks) {
        this.HeatSinks = HeatSinks;
    }

    public int getTonnage() {
        return Tonnage;
    }

    public void setTonnage(int Tonnage) {
        this.Tonnage = Tonnage;
    }

    public int getCruiseMP() {
        return CruiseMP;
    }

    public void setCruiseMP(int CruiseMP) {
        this.CruiseMP = CruiseMP;
    }

    public double getJJMult() {
        return JJMult;
    }

    public void setJJMult(double JJMult) {
        this.JJMult = JJMult;
    }

    public boolean isOmni() {
        return Omni;
    }

    public void setOmni(boolean Omni) {
        this.Omni = Omni;
    }

    public boolean isHasEnviroSealing() {
        return HasEnviroSealing;
    }

    public void setHasEnviroSealing(boolean HasEnviroSealing) {
        this.HasEnviroSealing = HasEnviroSealing;
    }

    public boolean isFractionalAccounting() {
        return FractionalAccounting;
    }

    public void setFractionalAccounting(boolean FractionalAccounting) {
        this.FractionalAccounting = FractionalAccounting;
    }

    public boolean isChanged() {
        return Changed;
    }

    public void setChanged(boolean Changed) {
        this.Changed = Changed;
    }

    public boolean isHasTurret1() {
        return HasTurret1;
    }

    public void setHasTurret1(boolean HasTurret1) {
        this.HasTurret1 = HasTurret1;
    }

    public boolean isHasTurret2() {
        return HasTurret2;
    }

    public void setHasTurret2(boolean HasTurret2) {
        this.HasTurret2 = HasTurret2;
    }

    public Engine getCurEngine() {
        return CurEngine;
    }

    public void setCurEngine(Engine CurEngine) {
        this.CurEngine = CurEngine;
    }

    public ifCombatVehicle getCurConfig() {
        return CurConfig;
    }

    public void setCurConfig(ifCombatVehicle CurConfig) {
        this.CurConfig = CurConfig;
    }

    public Vector<CVLoadout> getLoadouts() {
        return Loadouts;
    }

    public void setLoadouts(Vector<CVLoadout> Loadouts) {
        this.Loadouts = Loadouts;
    }

    public CVLoadout getMainLoadout() {
        return MainLoadout;
    }

    public void setMainLoadout(CVLoadout MainLoadout) {
        this.MainLoadout = MainLoadout;
    }

    public CVLoadout getCurLoadout() {
        return CurLoadout;
    }

    public void setCurLoadout(CVLoadout CurLoadout) {
        this.CurLoadout = CurLoadout;
    }

    public CVArmor getCurArmor() {
        return CurArmor;
    }

    public void setCurArmor(CVArmor CurArmor) {
        this.CurArmor = CurArmor;
    }

    public Hashtable getLookup() {
        return Lookup;
    }

    public void setLookup(Hashtable Lookup) {
        this.Lookup = Lookup;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int Year, boolean Something) {
        this.Year = Year;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String Source) {
        this.Source = Source;
    }

    public int getFlankMP() {
        return (int) Math.floor( CruiseMP * 1.5 + 0.5 );
    }

    public void SetRulesLevel( int r ) {
        if( Omni ) {
            CurLoadout.SetRulesLevel( r );
        } else {
            MainLoadout.SetRulesLevel( r );
        }
        SetChanged( true );
    }

    public int GetBaseEra() {
        return MainLoadout.GetEra();
    }

    public int GetProductionEra() {
        return CurLoadout.GetProductionEra();
    }

    public int GetBaseProductionEra() {
        return MainLoadout.GetProductionEra();
    }

    public int GetTechBase() {
        return MainLoadout.GetTechBase();
    }

    public boolean SetTechBase( int t ) {
        if( Omni ) {
            if( t != MainLoadout.GetTechBase() && t != AvailableCode.TECH_BOTH ) {
                return false;
            } else {
                CurLoadout.SetTechBase( t );
            }
        } else {
            MainLoadout.SetTechBase( t );
        }
        SetChanged( true );
        return true;
    }

    public int GetBaseYear() {
        return MainLoadout.GetYear();
    }
}
