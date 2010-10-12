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
                   SSWImage = "";
    private int HeatSinks = 0,
                Tonnage = 20,
                CruiseMP;
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
    private Vector<ifCVLoadout> Loadouts = new Vector<ifCVLoadout>();
    private ifCVLoadout MainLoadout,
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
    }

    public void Visit( ifVisitor v ) throws Exception {
        v.Visit( this );
    }

    public void Validate() {
        if ( Tonnage > CurConfig.GetMaxTonnage() ) {
            Tonnage = CurConfig.GetMaxTonnage();
        }
    }

    public void SetWheeled() {
        CurConfig = Wheeled;
        SetChanged(true);
    }

    public void SetTracked() {
        CurConfig = Tracked;
        SetChanged(true);
    }

    public void SetHover() {
        CurConfig = Hover;
        SetChanged(true);
    }

    public void setVTOL() {
        CurConfig = VTOL;
        SetChanged(true);
    }

    public void SetWiGE() {
        CurConfig = WiGE;
        SetChanged(true);
    }

    public void SetDisplacement() {
        CurConfig = Displacement;
        SetChanged(true);
    }

    public void SetHydrofoil() {
        CurConfig = Hydrofoil;
        SetChanged(true);
    }

    public void SetSubmarine() {
        CurConfig = Submarine;
        SetChanged(true);
    }

    public String GetMotiveLookupName() {
        return CurConfig.GetMotiveLookupName();
    }

    public int GetMaxTonnage() {
        return CurConfig.GetMaxTonnage();
    }

    public int GetTonnage() {
        return Tonnage;
    }

    public boolean CanUseJump() {
        return CurConfig.CanUseJumpMP();
    }

    public boolean CanBeTrailer() {
        return CurConfig.CanBeTrailer();
    }

    public boolean CanBeDuneBuggy() {
        return CurConfig.CanBeDuneBuggy();
    }

    public int GetSuspensionFactor( int Tonnage ) {
        return CurConfig.GetSuspensionFactor( Tonnage );
    }

    public float GetMinEngineWeight( int Tonnage ) {
        return CurConfig.GetMinEngineWeight( Tonnage );
    }

    public float GetLiftEquipmentCostMultiplier() {
        return CurConfig.GetLiftEquipmentCostMultiplier();
    }

    public boolean RequiresLiftEquipment() {
        return CurConfig.RequiresLiftEquipment();
    }

    public boolean IsVTOL() {
        return CurConfig.IsVTOL();
    }

    public boolean CanUseTurret() {
        if( IsVTOL() && CommonTools.IsAllowed( ChinTurretAC, this ) ) { return true; }
        return CurConfig.CanUseTurret();
    }

    public boolean CanUseDualTurret() {
        if( CommonTools.IsAllowed( DualTurretAC, this ) ) { return true; }
        return false;
    }

    public boolean CanUseSponsoon() {
        if( CommonTools.IsAllowed( SponsoonAC, this ) ) { return true; }
        return false;
    }

    public boolean CanUseFlotationHull() {
        return CurConfig.CanUseFlotationHull();
    }

    public boolean CanUseArmoredMotiveSystem() {
        return CurConfig.CanUseArmoredMotiveSystem();
    }

    public boolean CanUseAmphibious() {
        return CurConfig.CanUseAmphibious();
    }

    public boolean CanUseMinesweeper() {
        return CurConfig.CanUseMinesweeper();
    }

    public CVArmor GetArmor() {
        return CurArmor;
    }

    public ifCVLoadout GetLoadout() {
        return CurLoadout;
    }

    public ifCVLoadout GetMainLoadout() {
        return MainLoadout;
    }

    public boolean IsOmni() {
        return Omni;
    }

    public int GetBaseRulesLevel() {
        return MainLoadout.GetRulesLevel();
    }
    
    public Engine GetEngine() {
        return CurEngine;
    }

    public int GetYear() {
        return CurLoadout.GetYear();
    }

    public int GetCurrentBV() {
        // returns the final battle value of the mech
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
        // returns the current total tonnage of the mech
        return GetTonnage();
    }

    public boolean UsingFractionalAccounting() {
        return FractionalAccounting;
    }

    public void SetFractionalAccounting( boolean b ) {
        FractionalAccounting = b;
    }

    public void SetChanged( boolean b ) {
        Changed = b;
    }

    public boolean HasChanged() {
        return Changed;
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
        return HasTurret1;
    }

    public boolean UsingTurret2() {
        return HasTurret2;
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
        return CurLoadout.GetRulesLevel();
    }

    public int GetTechbase() {
        return CurLoadout.GetTechBase();
    }

    public int GetBaseTechbase() {
        return MainLoadout.GetTechBase();
    }

    public int GetEra() {
        return CurLoadout.GetEra();
    }

    public boolean IsYearRestricted() {
        return CurLoadout.IsYearRestricted();
    }

    public boolean HasFHES() {
        return false;
    }
}