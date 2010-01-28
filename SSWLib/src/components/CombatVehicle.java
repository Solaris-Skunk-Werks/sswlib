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
import common.Constants;
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
    private int Era,
                Year,
                RulesLevel,
                HeatSinks = 10,
                Tonnage = 20,
                CruiseMP;
    private double JJMult;
    private boolean Omni = false,
                    YearSpecified = false,
                    YearRestricted = false,
                    HasEnviroSealing = false,
                    FractionalAccounting = false,
                    Changed = false;
    private static ifCombatVehicle Wheeled = new stCVWheeled(),
                                 Tracked = new stCVTracked(),
                                 Hover = new stCVHover(),
                                 VTOL = new stCVVTOL(),
                                 WiGE = new stCVWiGE(),
                                 Displacement = new stCVDisplacement(),
                                 Hydrofoil = new stCVHydrofoil(),
                                 Submarine = new stCVSubmarine();
    private Engine CurEngine = new Engine( this );
    private Cockpit CurCockpit = new Cockpit( this );
    private ifCombatVehicle CurConfig = Tracked;
    private ifMechLoadout MainLoadout = new CVLoadout( Constants.BASELOADOUT_NAME, this ),
                    CurLoadout = MainLoadout;
    private Vector<ifMechLoadout> Loadouts = new Vector<ifMechLoadout>();
    private CVArmor CurArmor = new CVArmor( this );
    private AvailableCode FHESAC = new AvailableCode( AvailableCode.TECH_BOTH );
    private Hashtable Lookup = new Hashtable();
    private AvailableCode OmniAvailable = new AvailableCode( AvailableCode.TECH_BOTH );
    private Preferences Prefs;

    public CombatVehicle() {
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

    public boolean CanUseJump() {
        return CurConfig.CanUseJumpMP();
    }

    public boolean CanBeTrailer() {
        return CurConfig.CanBeTrailer();
    }

    public int GetMaxTonnage() {
        return CurConfig.GetMaxTonnage();
    }

    public int GetTonnage() {
        return Tonnage;
    }

    public HeatSinkFactory GetHeatSinks() {
        return CurLoadout.GetHeatSinks();
    }

    public JumpJetFactory GetJumpJets() {
        return CurLoadout.GetJumpJets();
    }

    public CVArmor GetArmor() {
        return CurArmor;
    }

    public Cockpit GetCockpit() {
        return CurCockpit;
    }

    public ifMechLoadout GetLoadout() {
        return CurLoadout;
    }

    public boolean IsOmni() {
        return Omni;
    }

    public int GetBaseRulesLevel() {
        return RulesLevel;
    }
    
    public Engine GetEngine() {
        return CurEngine;
    }

    public int GetYear() {
        return Year;
    }

    public int GetCurrentBV() {
        // returns the final battle value of the mech
        //TODO Fix this!
        return (int) Math.floor( CurCockpit.BVMod() * ( 0 + 0 ) + 0.5 );
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

    public int GetUnitType() {
        return AvailableCode.UNIT_COMBATVEHICLE;
    }

    public int GetRulesLevel() {
        return GetLoadout().GetRulesLevel();
    }

    public int GetTechbase() {
        return GetLoadout().GetTechBase();
    }

    public int GetBaseTechbase() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int GetEra() {
        return Era;
    }

    public boolean IsYearRestricted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean HasFHES() {
        //No such thing for a Vehicle
        return false;
    }
}
