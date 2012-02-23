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

package utilities;

import java.util.ArrayList;
import common.*;
import components.*;

public class CVCostBVBreakdown {
    private CombatVehicle CurUnit = null;
    private String NL = System.getProperty( "line.separator" );

    public CVCostBVBreakdown(CombatVehicle v) {
        CurUnit = v;
    }

    public String Render() {
        /*
        // this method returns a formated string with the cost/bv breakdown
        //  ----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+
        String retval = "";
        retval += String.format( "Vehicle Name:   %1$-41s Tonnage:    %2$d", CurUnit.GetName() + " " + CurUnit.GetModel(), CurUnit.GetTonnage() ) + NL;
        retval += String.format( "Rules Level: %1$-41s Total Cost: %2$,.0f", CommonTools.GetRulesLevelString( CurUnit.GetRulesLevel() ), CurUnit.GetTotalCost() ) + NL;
        retval += String.format( "Tech Base:   %1$-41s Total BV:   %2$,d", CommonTools.GetTechbaseString( CurUnit.GetLoadout().GetTechBase() ), CurUnit.GetCurrentBV() ) + NL;
        retval += NL;
        retval +=                "Item                                            DefBV     OffBV             Cost" + NL;
        retval += String.format( "Internal Structure - %1$-22s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetIntStruc().toString(), CurUnit.GetIntStruc().GetDefensiveBV(), CurUnit.GetIntStruc().GetOffensiveBV(), CurUnit.GetIntStruc().GetCost() ) + NL;
        retval += String.format( "Engine - %1$-34s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetEngine().CritName(), CurUnit.GetEngine().GetDefensiveBV(), CurUnit.GetEngine().GetOffensiveBV(), CurUnit.GetEngine().GetCost() ) + NL;
        retval += String.format( "Heat Sinks - %1$-30s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetHeatSinks().LookupName(), CurUnit.GetHeatSinks().GetDefensiveBV(), CurUnit.GetHeatSinks().GetOffensiveBV(), CurUnit.GetHeatSinks().GetCost() ) + NL;
        if( CurUnit.GetJumpJets().GetNumJJ() > 0 ) {
            retval += String.format( "Jump Jets - %1$-31s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetJumpJets().LookupName(), CurUnit.GetJumpJets().GetDefensiveBV(), CurUnit.GetJumpJets().GetOffensiveBV(), CurUnit.GetJumpJets().GetCost() ) + NL;
        }
        if( ! CurUnit.GetEngine().IsNuclear() ) {
            retval += String.format( "%1$-43s %2$,6.0f    %3$,6.0f    %4$,16.2f", "Power Amplifiers", 0.0f, 0.0f, CurUnit.GetLoadout().GetPowerAmplifier().GetCost() ) + NL;
        }
        if( CurUnit.IsPrimitive() && CurUnit.GetYear() < 2450 ) {
            retval += String.format( "Armor - %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().LookupName() + " (early)", CurUnit.GetArmor().GetDefensiveBV(), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetCost() ) + NL;
            if( CurUnit.GetArmor().IsPatchwork() ) {
                int[] ModArmor = CurUnit.GetLoadout().FindModularArmor();
                retval += String.format( "    FR: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetFrontArmorType().LookupName(), CurUnit.GetArmor().GetHDDefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetHDCost() ) + NL;
                retval += String.format( "    LT: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetLeftArmorType().LookupName(), CurUnit.GetArmor().GetCTDefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetCTCost() ) + NL;
                retval += String.format( "    RT: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetRightArmorType().LookupName(), CurUnit.GetArmor().GetLTDefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetLTCost() ) + NL;
                retval += String.format( "    RR: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetRearArmorType().LookupName(), CurUnit.GetArmor().GetRTDefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetRTCost() ) + NL;
                retval += String.format( "    TR: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetTurret1ArmorType().LookupName(), CurUnit.GetArmor().GetLADefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetLACost() ) + NL;
                retval += String.format( "    RT: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetTurret2ArmorType().LookupName(), CurUnit.GetArmor().GetRADefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetRACost() ) + NL;
                retval += String.format( "    RO: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetRotorArmorType().LookupName(), CurUnit.GetArmor().GetLLDefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetLLCost() ) + NL;
            }
        } else {
            retval += String.format( "Armor - %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().LookupName(), CurUnit.GetArmor().GetDefensiveBV(), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetCost() ) + NL;
            if( CurUnit.GetArmor().IsPatchwork() ) {
                int[] ModArmor = CurUnit.GetLoadout().FindModularArmor();
                retval += String.format( "    HD: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetHDArmorType().LookupName(), CurUnit.GetArmor().GetHDDefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetHDCost() ) + NL;
                retval += String.format( "    CT: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetCTArmorType().LookupName(), CurUnit.GetArmor().GetCTDefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetCTCost() ) + NL;
                retval += String.format( "    LT: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetLTArmorType().LookupName(), CurUnit.GetArmor().GetLTDefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetLTCost() ) + NL;
                retval += String.format( "    RT: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetRTArmorType().LookupName(), CurUnit.GetArmor().GetRTDefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetRTCost() ) + NL;
                retval += String.format( "    LA: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetLAArmorType().LookupName(), CurUnit.GetArmor().GetLADefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetLACost() ) + NL;
                retval += String.format( "    RA: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetRAArmorType().LookupName(), CurUnit.GetArmor().GetRADefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetRACost() ) + NL;
                retval += String.format( "    LL: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetLLArmorType().LookupName(), CurUnit.GetArmor().GetLLDefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetLLCost() ) + NL;
                retval += String.format( "    RL: %1$-35s %2$,6.0f    %3$,6.0f    %4$,16.2f", CurUnit.GetArmor().GetRLArmorType().LookupName(), CurUnit.GetArmor().GetRLDefensiveBV( ModArmor ), CurUnit.GetArmor().GetOffensiveBV(), CurUnit.GetArmor().GetRLCost() ) + NL;
            }
        }
        retval += NL;
        retval += GetEquipmentCostLines();
        retval += NL;
        retval += String.format( "Cost Multiplier                                                            %1$,1.3f", CurUnit.GetCostMult() ) + NL;
        retval += String.format( "Dry Cost                                                           %1$,13.0f", CurUnit.GetDryCost() ) + NL;
        retval += String.format( "Total Cost                                                         %1$,13.0f", CurUnit.GetTotalCost() ) + NL;
        retval += NL + NL;
        retval += "Defensive BV Calculation Breakdown" + NL;
        if( CurUnit.GetRulesLevel() == AvailableCode.RULES_EXPERIMENTAL ) {
            retval += "(Note: BV Calculations include defensive BV for armored components.)" + NL;
        }
        retval += "________________________________________________________________________________" + NL;
        if( CurUnit.GetArmor().IsPatchwork() ) {
            retval += "Patchwork Armor Calculations" + NL;
            int[] ModArmor = CurUnit.GetLoadout().FindModularArmor();
            retval += String.format( "    %1$-67s %2$,8.2f", "HD Armor Factor (" + ( CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_HD) + ModArmor[LocationIndex.MECH_LOC_HD] * 10 ) + ") * Armor Type Modifier (" + CurUnit.GetArmor().GetHDArmorType().GetBVTypeMult() + ")", CurUnit.GetArmor().GetHDDefensiveBV( ModArmor ) ) + NL;
            retval += String.format( "    %1$-67s %2$,8.2f", "LT Armor Factor (" + ( CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_LT ) + ModArmor[LocationIndex.MECH_LOC_LT] * 10 + CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_LTR ) + ModArmor[LocationIndex.MECH_LOC_LTR] * 10 ) + ") * Armor Type Modifier (" + CurUnit.GetArmor().GetLTArmorType().GetBVTypeMult() + ")", CurUnit.GetArmor().GetLTDefensiveBV( ModArmor ) ) + NL;
            retval += String.format( "    %1$-67s %2$,8.2f", "RT Armor Factor (" + ( CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_RT ) + ModArmor[LocationIndex.MECH_LOC_RT] * 10 + CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_RTR ) + ModArmor[LocationIndex.MECH_LOC_RTR] * 10 ) + ") * Armor Type Modifier (" + CurUnit.GetArmor().GetRTArmorType().GetBVTypeMult() + ")", CurUnit.GetArmor().GetRTDefensiveBV( ModArmor ) ) + NL;
            if( CurUnit.IsQuad() ) {
                retval += String.format( "    %1$-67s %2$,8.2f", "FLL Armor Factor (" + ( CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_LA ) + ModArmor[LocationIndex.MECH_LOC_LA] * 10 ) + ") * Armor Type Modifier (" + CurUnit.GetArmor().GetLAArmorType().GetBVTypeMult() + ")", CurUnit.GetArmor().GetLADefensiveBV( ModArmor ) ) + NL;
                retval += String.format( "    %1$-67s %2$,8.2f", "FRL Armor Factor (" + ( CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_RA ) + ModArmor[LocationIndex.MECH_LOC_RA] * 10 ) + ") * Armor Type Modifier (" + CurUnit.GetArmor().GetRAArmorType().GetBVTypeMult() + ")", CurUnit.GetArmor().GetRADefensiveBV( ModArmor ) ) + NL;
                retval += String.format( "    %1$-67s %2$,8.2f", "RLL Armor Factor (" + ( CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_LL ) + ModArmor[LocationIndex.MECH_LOC_LL] * 10 ) + ") * Armor Type Modifier (" + CurUnit.GetArmor().GetLLArmorType().GetBVTypeMult() + ")", CurUnit.GetArmor().GetLLDefensiveBV( ModArmor ) ) + NL;
                retval += String.format( "    %1$-67s %2$,8.2f", "RRL Armor Factor (" + ( CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_RA ) + ModArmor[LocationIndex.MECH_LOC_RL] * 10 ) + ") * Armor Type Modifier (" + CurUnit.GetArmor().GetRLArmorType().GetBVTypeMult() + ")", CurUnit.GetArmor().GetRLDefensiveBV( ModArmor ) ) + NL;
            } else {
                retval += String.format( "    %1$-67s %2$,8.2f", "LA Armor Factor (" + ( CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_LA ) + ModArmor[LocationIndex.MECH_LOC_LA] * 10 ) + ") * Armor Type Modifier (" + CurUnit.GetArmor().GetLAArmorType().GetBVTypeMult() + ")", CurUnit.GetArmor().GetLADefensiveBV( ModArmor ) ) + NL;
                retval += String.format( "    %1$-67s %2$,8.2f", "RA Armor Factor (" + ( CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_RA ) + ModArmor[LocationIndex.MECH_LOC_RA] * 10 ) + ") * Armor Type Modifier (" + CurUnit.GetArmor().GetRAArmorType().GetBVTypeMult() + ")", CurUnit.GetArmor().GetRADefensiveBV( ModArmor ) ) + NL;
                retval += String.format( "    %1$-67s %2$,8.2f", "LL Armor Factor (" + ( CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_LL ) + ModArmor[LocationIndex.MECH_LOC_LL] * 10 ) + ") * Armor Type Modifier (" + CurUnit.GetArmor().GetLLArmorType().GetBVTypeMult() + ")", CurUnit.GetArmor().GetLLDefensiveBV( ModArmor ) ) + NL;
                retval += String.format( "    %1$-67s %2$,8.2f", "RL Armor Factor (" + ( CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_RL ) + ModArmor[LocationIndex.MECH_LOC_RL] * 10 ) + ") * Armor Type Modifier (" + CurUnit.GetArmor().GetRLArmorType().GetBVTypeMult() + ")", CurUnit.GetArmor().GetRLDefensiveBV( ModArmor ) ) + NL;
            }
            retval += String.format( "%1$-71s %2$,8.2f", "    Total Location Armor BV (" + ( CurUnit.GetArmor().GetHDDefensiveBV( ModArmor ) + CurUnit.GetArmor().GetCTDefensiveBV( ModArmor ) + CurUnit.GetArmor().GetRTDefensiveBV( ModArmor ) + CurUnit.GetArmor().GetLTDefensiveBV( ModArmor ) + CurUnit.GetArmor().GetRADefensiveBV( ModArmor ) + CurUnit.GetArmor().GetLADefensiveBV( ModArmor ) + CurUnit.GetArmor().GetRLDefensiveBV( ModArmor ) + CurUnit.GetArmor().GetLLDefensiveBV( ModArmor ) ) + ") * 2.5", CurUnit.GetArmor().GetDefensiveBV() ) + NL;
        } else {
            if( CurUnit.GetCockpit().IsTorsoMounted() ) {
                retval += "( Total Armor Factor (" + ( CurUnit.GetArmor().GetArmorValue() + CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_CT) + CurUnit.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_CTR) ) + ") * Armor Type Modifier (" + CurUnit.GetArmor().GetBVTypeMult() + ")" + NL;
                retval += String.format( "%1$-71s %2$,8.2f", "    + Modular Armor Value (" + CurUnit.GetArmor().GetModularArmorValue() + ") ) * 2.5", CurUnit.GetArmor().GetDefensiveBV() ) + NL;
                retval += "    (Front and Rear CT armor value doubled due to Torso-Mounted Cockpit)" + NL;
            } else {
                retval += String.format( "%1$-71s %2$,8.2f", "Total Armor Factor (" + CurUnit.GetArmor().GetArmorValue() + ") * Armor Type Modifier (" + CurUnit.GetArmor().GetBVTypeMult() + ") * 2.5", CurUnit.GetArmor().GetDefensiveBV() ) + NL;
            }
        }
        retval += "Total Structure Points (" + CurUnit.GetIntStruc().GetTotalPoints() + ") * Structure Type Modifier (" + CurUnit.GetIntStruc().GetBVTypeMult() + ")" + NL;
        retval += String.format( "%1$-71s %2$,8.2f", "    * Engine Type Modifier (" + CurUnit.GetEngine().GetBVMult() + ") * 1.5", CurUnit.GetIntStruc().GetDefensiveBV() ) + NL;
        retval += String.format( "%1$-71s %2$,8.2f", "Mech Tonnage (" + CurUnit.GetTonnage() + ") * Gyro Type Modifer (" + CurUnit.GetGyro().GetBVTypeMult() + ")", CurUnit.GetGyro().GetDefensiveBV() ) + NL;
        retval += String.format( "%1$-71s %2$,8.2f", "Total Defensive BV of all Equipment", CurUnit.GetDefensiveEquipBV() ) + NL;
        retval += String.format( "%1$-71s %2$,8.2f", "Excessive Ammunition Penalty", CurUnit.GetDefensiveExcessiveAmmoPenalty() ) + NL;
        retval += String.format( "%1$-71s %2$,8.2f", "Explosive Ammunition Penalty", CurUnit.GetExplosiveAmmoPenalty() ) + NL;
        retval += String.format( "%1$-71s %2$,8.2f", "Explosive Item Penalty  ", CurUnit.GetExplosiveWeaponPenalty() ) + NL;
        retval += String.format( "%1$-71s %2$,8.2f", "Subtotal", CurUnit.GetUnmodifiedDefensiveBV() ) + NL;
        retval += "Defensive Speed Factor Breakdown:" + NL;
        retval += PrintDefensiveFactorCalculations();
        retval += String.format( "%1$-71s %2$,8.2f", "Total DBV (Subtotal * Defensive Speed Factor (" + String.format( "%1$,4.2f", CurUnit.GetDefensiveFactor() ) + "))", CurUnit.GetDefensiveBV() ) + NL;
        retval += NL + NL;
        retval += "Offensive BV Calculation Breakdown" + NL;
        retval += "________________________________________________________________________________" + NL;
        if( HasBonusFromCP() ) {
            retval += "Heat Efficiency (6 + " + CurUnit.GetHeatSinks().TotalDissipation() + " - " + CurUnit.GetBVMovementHeat() + " + " + GetBonusFromCP() + ") = " + ( 6 + CurUnit.GetHeatSinks().TotalDissipation() - CurUnit.GetBVMovementHeat() + GetBonusFromCP() ) + NL;
            retval += "    (Heat Efficiency calculation includes bonus from Coolant Pods)" + NL;
        } else {
            retval += "Heat Efficiency (6 + " + CurUnit.GetHeatSinks().TotalDissipation() + " - " + CurUnit.GetBVMovementHeat() + ") = "+ ( 6 + CurUnit.GetHeatSinks().TotalDissipation() - CurUnit.GetBVMovementHeat() ) + NL;
        }
        retval += String.format( "%1$-71s %2$,8.2f", "Adjusted Weapon BV Total WBV", CurUnit.GetHeatAdjustedWeaponBV() ) + NL;
        retval += PrintHeatAdjustedWeaponBV();
        retval += String.format( "%1$-71s %2$,8.2f", "Non-Heat Equipment Total NHBV", CurUnit.GetNonHeatEquipBV() ) + NL;
        retval += PrintNonHeatEquipBV();
        retval += String.format( "%1$-71s %2$,8.2f", "Excessive Ammunition Penalty", CurUnit.GetExcessiveAmmoPenalty() ) + NL;
        retval += String.format( "%1$-71s %2$,8.2f", "Mech Tonnage Bonus", CurUnit.GetTonnageBV() ) + NL;
        retval += String.format( "%1$-71s %2$,8.2f", "Subtotal (WBV + NHBV - Excessive Ammo + Tonnage Bonus)", CurUnit.GetUnmodifiedOffensiveBV() ) + NL;
        retval += "Offensive Speed Factor Breakdown:" + NL;
        retval += PrintOffensiveFactorCalculations();
        retval += String.format( "%1$-71s %2$,8.2f", "Total OBV (Subtotal * Offensive Speed Factor (" + CurUnit.GetOffensiveFactor() + "))", CurUnit.GetOffensiveBV() ) + NL;
        retval += NL + NL;
        if( CurUnit.GetCockpit().BVMod() != 1.0f ) {
            retval += String.format( "%1$-71s %2$,8.2f", CurUnit.GetCockpit().CritName() + " modifier", CurUnit.GetCockpit().BVMod() ) + NL;
            retval += String.format( "%1$-73s %2$,6d", "Total Battle Value ((DBV + OBV) * cockpit modifier, round off)", CurUnit.GetCurrentBV() );
        } else {
            retval += String.format( "%1$-73s %2$,6d", "Total Battle Value (DBV + OBV, round off)", CurUnit.GetCurrentBV() );
        }
        return retval;
    }

    private String GetEquipmentCostLines() {
        // returns a block of lines for the cost breakdown
        String retval = "";
        ArrayList v = CurUnit.GetLoadout().GetNonCore();
        abPlaceable a;
        for( int i = 0; i < v.size(); i++ ) {
            a = (abPlaceable) v.get( i );
            if( a instanceof RangedWeapon ) {
                if( ((RangedWeapon) a).IsUsingFCS() ) {
                    retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", a.CritName() + " w/ " + ((abPlaceable) ((RangedWeapon) a).GetFCS()).CritName(), a.GetDefensiveBV(), a.GetOffensiveBV(), a.GetCost() ) + NL;
                } else {
                    retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", a.CritName(), a.GetDefensiveBV(), a.GetOffensiveBV(), a.GetCost() ) + NL;
                }
            } else {
                retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", a.CritName(), a.GetDefensiveBV(), a.GetOffensiveBV(), a.GetCost() ) + NL;
            }
        }
        if( CurUnit.HasCommandConsole() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetCommandConsole().CritName(), CurUnit.GetCommandConsole().GetDefensiveBV(), CurUnit.GetCommandConsole().GetOffensiveBV(), CurUnit.GetCommandConsole().GetCost() ) + NL;
        }
        if( CurUnit.UsingTC() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", "Targeting Computer", CurUnit.GetTC().GetDefensiveBV(), CurUnit.GetTC().GetOffensiveBV(), CurUnit.GetTC().GetCost() ) + NL;
        }
        if( CurUnit.HasNullSig() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetNullSig().CritName(), CurUnit.GetNullSig().GetDefensiveBV(), CurUnit.GetNullSig().GetOffensiveBV(), CurUnit.GetNullSig().GetCost() ) + NL;
        }
        if( CurUnit.HasChameleon() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetChameleon().CritName(), CurUnit.GetChameleon().GetDefensiveBV(), CurUnit.GetChameleon().GetOffensiveBV(), CurUnit.GetChameleon().GetCost() ) + NL;
        }
        if( CurUnit.HasVoidSig() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetVoidSig().CritName(), CurUnit.GetVoidSig().GetDefensiveBV(), CurUnit.GetVoidSig().GetOffensiveBV(), CurUnit.GetVoidSig().GetCost() ) + NL;
        }
        if( CurUnit.HasBlueShield() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetBlueShield().CritName(), CurUnit.GetBlueShield().GetDefensiveBV(), CurUnit.GetBlueShield().GetOffensiveBV(), CurUnit.GetBlueShield().GetCost() ) + NL;
        }
        if( CurUnit.UsingPartialWing() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetPartialWing().CritName(), CurUnit.GetPartialWing().GetDefensiveBV(), CurUnit.GetPartialWing().GetOffensiveBV(), CurUnit.GetPartialWing().GetCost() ) + NL;
        }
        if( CurUnit.UsingJumpBooster() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetJumpBooster().CritName(), CurUnit.GetJumpBooster().GetDefensiveBV(), CurUnit.GetJumpBooster().GetOffensiveBV(), CurUnit.GetJumpBooster().GetCost() ) + NL;
        }
        if( CurUnit.GetLoadout().HasSupercharger() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetLoadout().GetSupercharger().CritName(), CurUnit.GetLoadout().GetSupercharger().GetDefensiveBV(), CurUnit.GetLoadout().GetSupercharger().GetOffensiveBV(), CurUnit.GetLoadout().GetSupercharger().GetCost() ) + NL;
        }
        if( CurUnit.HasLAAES() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetLAAES().CritName(), CurUnit.GetLAAES().GetDefensiveBV(), CurUnit.GetLAAES().GetOffensiveBV(), CurUnit.GetLAAES().GetCost() ) + NL;
        }
        if( CurUnit.HasRAAES() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetRAAES().CritName(), CurUnit.GetRAAES().GetDefensiveBV(), CurUnit.GetRAAES().GetOffensiveBV(), CurUnit.GetRAAES().GetCost() ) + NL;
        }
        if( CurUnit.HasLegAES() ) {
            if( CurUnit.IsQuad() ) {
                retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetRLAES().CritName(), CurUnit.GetRLAES().GetDefensiveBV(), CurUnit.GetRLAES().GetOffensiveBV(), CurUnit.GetRLAES().GetCost() ) + NL;
                retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetRLAES().CritName(), CurUnit.GetRLAES().GetDefensiveBV(), CurUnit.GetRLAES().GetOffensiveBV(), CurUnit.GetRLAES().GetCost() ) + NL;
                retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetRLAES().CritName(), CurUnit.GetRLAES().GetDefensiveBV(), CurUnit.GetRLAES().GetOffensiveBV(), CurUnit.GetRLAES().GetCost() ) + NL;
                retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetRLAES().CritName(), CurUnit.GetRLAES().GetDefensiveBV(), CurUnit.GetRLAES().GetOffensiveBV(), CurUnit.GetRLAES().GetCost() ) + NL;
            } else {
                retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetRLAES().CritName(), CurUnit.GetRLAES().GetDefensiveBV(), CurUnit.GetRLAES().GetOffensiveBV(), CurUnit.GetRLAES().GetCost() ) + NL;
                retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetRLAES().CritName(), CurUnit.GetRLAES().GetDefensiveBV(), CurUnit.GetRLAES().GetOffensiveBV(), CurUnit.GetRLAES().GetCost() ) + NL;
            }
        }
        if( CurUnit.GetLoadout().HasHDTurret() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetLoadout().GetHDTurret().CritName(), CurUnit.GetLoadout().GetHDTurret().GetDefensiveBV(), CurUnit.GetLoadout().GetHDTurret().GetOffensiveBV(), CurUnit.GetLoadout().GetHDTurret().GetCost() ) + NL;
        }
        if( CurUnit.GetLoadout().HasLTTurret() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetLoadout().GetLTTurret().CritName(), CurUnit.GetLoadout().GetLTTurret().GetDefensiveBV(), CurUnit.GetLoadout().GetLTTurret().GetOffensiveBV(), CurUnit.GetLoadout().GetLTTurret().GetCost() ) + NL;
        }
        if( CurUnit.GetLoadout().HasRTTurret() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", CurUnit.GetLoadout().GetRTTurret().CritName(), CurUnit.GetLoadout().GetRTTurret().GetDefensiveBV(), CurUnit.GetLoadout().GetRTTurret().GetOffensiveBV(), CurUnit.GetLoadout().GetRTTurret().GetCost() ) + NL;
        }
        if( CurUnit.HasCTCase() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", "CASE", 0.0, 0.0, CurUnit.GetLoadout().GetCTCase().GetCost() ) + NL;
        }
        if( CurUnit.HasLTCase() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", "CASE", 0.0, 0.0, CurUnit.GetLoadout().GetLTCase().GetCost() ) + NL;
        }
        if( CurUnit.HasRTCase() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", "CASE", 0.0, 0.0, CurUnit.GetLoadout().GetRTCase().GetCost() ) + NL;
        }
        if( CurUnit.GetLoadout().HasHDCASEII() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", "CASE II", 0.0, 0.0, CurUnit.GetLoadout().GetHDCaseII().GetCost() ) + NL;
        }
        if( CurUnit.GetLoadout().HasCTCASEII() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", "CASE II", 0.0, 0.0, CurUnit.GetLoadout().GetCTCaseII().GetCost() ) + NL;
        }
        if( CurUnit.GetLoadout().HasLTCASEII() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", "CASE II", 0.0, 0.0, CurUnit.GetLoadout().GetLTCaseII().GetCost() ) + NL;
        }
        if( CurUnit.GetLoadout().HasRTCASEII() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", "CASE II", 0.0, 0.0, CurUnit.GetLoadout().GetRTCaseII().GetCost() ) + NL;
        }
        if( CurUnit.GetLoadout().HasLACASEII() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", "CASE II", 0.0, 0.0, CurUnit.GetLoadout().GetLACaseII().GetCost() ) + NL;
        }
        if( CurUnit.GetLoadout().HasRACASEII() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", "CASE II", 0.0, 0.0, CurUnit.GetLoadout().GetRACaseII().GetCost() ) + NL;
        }
        if( CurUnit.GetLoadout().HasLLCASEII() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", "CASE II", 0.0, 0.0, CurUnit.GetLoadout().GetLLCaseII().GetCost() ) + NL;
        }
        if( CurUnit.GetLoadout().HasRLCASEII() ) {
            retval += String.format( "%1$-46s %2$,6.0f    %3$,6.0f    %4$,13.2f", "CASE II", 0.0, 0.0, CurUnit.GetLoadout().GetRLCaseII().GetCost() ) + NL;
        }
        * 
        */
        return ""; //retval;
    }

    /*
    public String PrintNonHeatEquipBV() {
        // return the BV of all offensive equipment
        ArrayList v = CurUnit.GetLoadout().GetNonCore();
        abPlaceable a = null;
        String retval = "";

        for( int i = 0; i < v.size(); i++ ) {
            if( ! ( v.get( i ) instanceof ifWeapon ) ) {
                a = ((abPlaceable) v.get( i ));
                retval += String.format( "%1$-71s %2$,8.2f", "    -> " + a.CritName(), a.GetOffensiveBV() ) + NL;
            }
        }
        return retval;
    }

    public String PrintHeatAdjustedWeaponBV() {
        ArrayList v = CurUnit.GetLoadout().GetNonCore(), wep = new ArrayList();
        double foreBV = 0.0, rearBV = 0.0;
        boolean UseRear = false, TC = CurUnit.UsingTC(), UseAESMod = false;
        String retval = "";
        abPlaceable a;

        // is it even worth performing all this?
        if( v.size() <= 0 ) {
            // nope
            return retval;
        }

        // trim out the other equipment and get a list of offensive weapons only.
        for( int i = 0; i < v.size(); i++ ) {
            if( v.get( i ) instanceof ifWeapon ) {
                wep.add( v.get( i ) );
            }
        }

        // just to save us a headache if there are no weapons
        if( wep.size() <= 0 ) { return retval; }

        // now get the mech's heat efficiency and the total heat from weapons
        double heff = 6 + CurUnit.GetHeatSinks().TotalDissipation() - CurUnit.GetBVMovementHeat();
        heff += GetBonusFromCP();
        double wheat = CurUnit.GetBVWeaponHeat();

        // find out the total BV of rear and forward firing weapons
        for( int i = 0; i < wep.size(); i++ ) {
            a = ((abPlaceable) wep.get( i ));
            // arm mounted weapons always count their full BV, so ignore them.
            int loc = CurUnit.GetLoadout().Find( a );
            if( loc != LocationIndex.MECH_LOC_LA && loc != LocationIndex.MECH_LOC_RA ) {
                UseAESMod = CurUnit.UseAESModifier( a );
                if( a.IsMountedRear() ) {
                    rearBV += a.GetCurOffensiveBV( true, TC, UseAESMod, Robotic );
                } else {
                    foreBV += a.GetCurOffensiveBV( false, TC, UseAESMod, Robotic );
                }
            }
        }
        if( rearBV > foreBV ) { UseRear = true; }

        // see if we need to run heat calculations
        if( heff - wheat >= 0 ) {
            // no need for extensive calculations, return the weapon BV
            for( int i = 0; i < wep.size(); i++ ) {
                a = ((abPlaceable) wep.get( i ));
                int loc = CurUnit.GetLoadout().Find( a );
                UseAESMod = CurUnit.UseAESModifier( ((abPlaceable) wep.get( i )) );
                if( loc != LocationIndex.MECH_LOC_LA && loc != LocationIndex.MECH_LOC_RA ) {
                    retval += String.format( "%1$-71s %2$,8.2f", "    -> " + a.CritName(), a.GetCurOffensiveBV( UseRear, TC, UseAESMod ) ) + NL;
                } else {
                    retval += String.format( "%1$-71s %2$,8.2f", "    -> " + a.CritName(), a.GetCurOffensiveBV( false, TC, UseAESMod ) ) + NL;
                }
            }
            return retval;
        }

        // Sort the weapon list
        abPlaceable[] sorted = CurUnit.SortWeapons( wep, UseRear );

        // calculate the BV of the weapons based on heat
        int curheat = 0;
        for( int i = 0; i < sorted.length; i++ ) {
            boolean DoRear = UseRear;
            a = sorted[i];
            int loc = CurUnit.GetLoadout().Find( a );
            //changed below to DoRear = false because it would set Rear to true if it was false to begin with.
            if( loc == LocationIndex.MECH_LOC_LA || loc == LocationIndex.MECH_LOC_RA ) { DoRear = false; } //!DoRear; }
            UseAESMod = CurUnit.UseAESModifier( a );
            if( curheat < heff ) {
                retval += String.format( "%1$-71s %2$,8.2f", "    -> " + a.CritName(), a.GetCurOffensiveBV( DoRear, TC, UseAESMod ) ) + NL;
            } else {
                if( ((ifWeapon) sorted[i]).GetBVHeat() <= 0 ) {
                    retval += String.format( "%1$-71s %2$,8.2f", "    -> " + a.CritName(), a.GetCurOffensiveBV( DoRear, TC, UseAESMod ) ) + NL;
                } else {
                    retval += String.format( "%1$-71s %2$,8.2f", "    -> " + a.CritName(), a.GetCurOffensiveBV( DoRear, TC, UseAESMod ) * 0.5 ) + NL;
                }
            }
            curheat += ((ifWeapon) sorted[i]).GetBVHeat();
        }
        * 
        return ""; //retval;
    }

    public String PrintDefensiveFactorCalculations() {
        // returns the defensive factor for this mech based on it's highest
        // target number for speed.
        String retval = "";

        // subtract one since we're indexing an array
        int RunMP = CurUnit.GetAdjustedRunningMP( true, true ) - 1;
        int JumpMP = 0;

        // this is a safeguard for using MASC on an incredibly speedy chassis
        // there is currently no way to get a bonus higher anyway.
        if( RunMP > 29 ) { RunMP = 29; }
        // safeguard for low walk mp (Modular Armor, for instance)
        if( RunMP < 0 ) { RunMP = 0; }

        // Get the defensive factors for jumping and running movement
        double ground = Mech.DefensiveFactor[RunMP];
        double jump = 0.0;
        if( CurUnit.GetJumpJets().GetNumJJ() > 0 ) {
            JumpMP = CurUnit.GetAdjustedJumpingMP( true ) - 1;
                jump = Mech.DefensiveFactor[JumpMP] + 0.1;
        }
        if( CurUnit.UsingJumpBooster() ) {
            int boostMP = CurUnit.GetJumpBoosterMP();
            if( boostMP > JumpMP ) {
                JumpMP = boostMP;
                jump = Mech.DefensiveFactor[JumpMP] + 0.1;
            }
        }

        MechModifier m = CurUnit.GetTotalModifiers( true, true );

        retval += "    Maximum Ground Movement Modifier: " + String.format( "%1$,.2f", ground ) + NL;
        retval += "    Maximum Jump Movement Modifier:   " + String.format( "%1$,.2f", jump ) + NL;
        retval += "    Defensive Speed Factor Bonus from Equipment: " + String.format( "%1$,.2f", m.DefensiveBonus() ) + NL;
        retval += "    Minimum Defensive Speed Factor:   " + String.format( "%1$,.2f", m.MinimumDefensiveBonus() ) + NL;
        retval += "    (Max of Run or Jump) + DSF Bonus = " + String.format( "%1$,.2f", CurUnit.GetDefensiveFactor() ) + NL;

        return retval;
    }

    public String PrintOffensiveFactorCalculations() {
        String retval = "";

        double temp;
        if( CurUnit.UsingJumpBooster() ) {
            int boost = CurUnit.GetAdjustedBoosterMP( true );
            int jump = CurUnit.GetAdjustedJumpingMP( true );
            if( jump >= boost ) {
                temp = (double) (CurUnit.GetAdjustedRunningMP(true, true) + (Math.floor(CurUnit.GetAdjustedJumpingMP(true) * 0.5 + 0.5)) - 5.0);
                retval += "    Adjusted Running MP (" + CurUnit.GetAdjustedRunningMP( true, true ) + ") + ( Adjusted Jumping MP (" + CurUnit.GetAdjustedJumpingMP( true ) + ") / 2 ) - 5 = " + String.format( "%1$,.2f", CurUnit.GetAdjustedRunningMP( true, true ) + ( Math.floor( CurUnit.GetAdjustedJumpingMP( true ) * 0.5 + 0.5 )  ) - 5.0 ) + NL;
            } else {
                temp = (double) (CurUnit.GetAdjustedRunningMP(true, true) + (Math.floor(CurUnit.GetAdjustedBoosterMP(true) * 0.5 + 0.5)) - 5.0);
                retval += "    Adjusted Running MP (" + CurUnit.GetAdjustedRunningMP( true, true ) + ") + ( Adjusted Jumping MP (" + CurUnit.GetAdjustedBoosterMP( true ) + ") / 2 ) - 5 = " + String.format( "%1$,.2f", CurUnit.GetAdjustedRunningMP( true, true ) + ( Math.floor( CurUnit.GetAdjustedBoosterMP( true ) * 0.5 + 0.5 )  ) - 5.0 ) + NL;
            }
        } else {
            temp = (double) (CurUnit.GetAdjustedRunningMP(true, true) + (Math.floor(CurUnit.GetAdjustedJumpingMP(true) * 0.5 + 0.5)) - 5.0);
            retval += "    Adjusted Running MP (" + CurUnit.GetAdjustedRunningMP( true, true ) + ") + ( Adjusted Jumping MP (" + CurUnit.GetAdjustedJumpingMP( true ) + ") / 2 ) - 5 = " + String.format( "%1$,.2f", CurUnit.GetAdjustedRunningMP( true, true ) + ( Math.floor( CurUnit.GetAdjustedJumpingMP( true ) * 0.5 + 0.5 )  ) - 5.0 ) + NL;
        }
        retval += "    " + String.format( "%1$,.2f", temp ) + " / 10 + 1 = " + String.format( "%1$.3f", ( temp * 0.1 + 1.0 ) ) + NL;
        temp = temp * 0.1 + 1.0;
        retval += "    " + String.format( "%1$,.2f", temp ) + " ^ 1.2 = " + (double) Math.floor( ( Math.pow( temp, 1.2 ) ) * 100 + 0.5 ) / 100 + " (rounded off to two digits)" + NL;

        return retval;
    }

    private boolean HasBonusFromCP() {
        ArrayList v = CurUnit.GetLoadout().GetNonCore();
        abPlaceable a;
        if( CurUnit.GetRulesLevel() == AvailableCode.RULES_EXPERIMENTAL ) {
            // check for coolant pods
            for( int i = 0; i < v.size(); i++ ) {
                a = (abPlaceable) v.get( i );
                if( a instanceof Equipment ) {
                    if( ((Equipment) a).LookupName().equals( "Coolant Pod" ) ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int GetBonusFromCP() {
        int BonusFromCP, retval = 0;
        int NumHS = CurUnit.GetHeatSinks().GetNumHS(), MaxHSBonus = NumHS * 2, NumPods = 0;
        ArrayList v = CurUnit.GetLoadout().GetNonCore();
        abPlaceable a;

        if( CurUnit.GetRulesLevel() == AvailableCode.RULES_EXPERIMENTAL ) {
            // check for coolant pods
            for( int i = 0; i < v.size(); i++ ) {
                a = (abPlaceable) v.get( i );
                if( a instanceof Equipment ) {
                    if( ((Equipment) a).LookupName().equals( "Coolant Pod" ) ) {
                        NumPods++;
                    }
                }
            }
            // get the heat sink bonus
            BonusFromCP = (int) Math.ceil( (double) NumHS * ( (double) NumPods * 0.2f ) );
            if( BonusFromCP > MaxHSBonus ) { BonusFromCP = MaxHSBonus; }
            retval += BonusFromCP;
        }
        return retval;
    }
    */
}
