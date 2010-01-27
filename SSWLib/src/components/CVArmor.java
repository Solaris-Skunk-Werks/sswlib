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

import common.CommonTools;
import java.text.DecimalFormat;
import states.*;

public class CVArmor extends abPlaceable {
    // the armor of the mech
    public final static int DEFAULT_CTR_ARMOR_PERCENT = 25,
                            DEFAULT_STR_ARMOR_PERCENT = 25,
                            ARMOR_PRIORITY_FRONT = 0,
                            ARMOR_PRIORITY_TURRET = 1,
                            ARMOR_PRIORITY_REAR = 2;

    // Declares
    private CombatVehicle Owner;
    private int Placed = 0;
    private int[] ArmorPoints = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private int[] MaxArmor = { 0, 0, 0, 0, 0, 0, 0, 0 };
    private ifArmor Industrial = new stArmorIN(),
                    Standard = new stArmorMS(),
                    ISFF = new stArmorISFF(),
                    ISST = new stArmorISST(),
                    ISLF = new stArmorISLF(),
                    ISHF = new stArmorISHF(),
                    Hardened = new stArmorHA(),
                    ISLR = new stArmorISLR(),
                    ISRE = new stArmorISRE(),
                    PBM = new stArmorPBM(),
                    Commercial = new stArmorCM(),
                    CLFF = new stArmorCLFF(),
                    CLFL = new stArmorCLFL(),
                    CLLR = new stArmorCLLR(),
                    CLRE = new stArmorCLRE();
    private ifArmor Config = Standard;

    public CVArmor( CombatVehicle c ) {
        Owner = c;
        SetMaxArmor();
    }

    public ifState GetCurrentState() {
        return (ifState) Config;
    }

    public void SetIndustrial() {
        // set the armor to Inner Sphere Industrial
        Config = Industrial;
    }

    public void SetStandard() {
        // set the armor to Inner Sphere Military Standard
        Config = Standard;
    }

    public void SetISFF() {
        // set the armor to Inner Sphere Ferro-Fibrous
        Config = ISFF;
    }

    public void SetISST() {
        // set the armor to Inner Sphere Stealth
        Config = ISST;
    }

    public void SetISLF() {
        // set the armor to Inner Sphere Light Ferro-Fibrous
        Config = ISLF;
    }

    public void SetISHF() {
        // set the armor to Inner Sphere Heavy Ferro-Fibrous
        Config = ISHF;
    }

    public void SetHardened() {
        // set the armor to Inner Sphere Hardened
        Config = Hardened;
    }

    public void SetISLR() {
        // set the armor to Inner Sphere Laser-Reflective
        Config = ISLR;
    }

    public void SetISRE() {
        // set the armor to Inner Sphere Reactive
        Config = ISRE;
    }

    public void SetCommercial() {
        Config = Commercial;
    }

    public void SetPrimitive() {
        Config = PBM;
    }

    public void SetCLFF() {
        // set the armor to Clan Ferro-Fibrous
        Config = CLFF;
    }

    public void SetCLFL() {
        // set the armor to Clan Ferro-Lamellor
        Config = CLFL;
    }

    public void SetCLLR() {
        // set the armor to Inner Sphere Laser-Reflective
        Config = CLLR;
    }

    public void SetCLRE() {
        // set the armor to Inner Sphere Reactive
        Config = CLRE;
    }

    public void Recalculate() {
        // recalculates the armor if mech tonnage or motive type changes
        SetMaxArmor();

        // now that we've set the maximums, make sure we're not exceeding them

        Owner.SetChanged( true );
    }

    public void SetMaxArmor() {
        // this sets the maximum array when tonnage changes.
        return;
    }

    public void IncrementArmor( int Loc ) {
        // Check the location and see what we have to do
        IncrementSingle( Loc );
    }

    private void IncrementSingle( int Loc ) {
        // Make sure we're not exceeding the max
        if( ArmorPoints[Loc] < MaxArmor[Loc] ) {
            ArmorPoints[Loc]++;
        }
        Owner.SetChanged( true );
    }

    public void DecrementArmor( int Loc ) {
        // Make sure we're not going below 0
        if( ArmorPoints[Loc] <= 0 ) {
            ArmorPoints[Loc] = 0;
        } else {
            ArmorPoints[Loc]--;
        }
        Owner.SetChanged( true );
    }

    public void SetArmor( int Loc, int av ) {
        // Check the location and see what we have to do
        SetSingle( Loc, av );
        Owner.SetChanged( true );
    }

    private void SetSingle( int Loc, int av ) {
        // make sure we're within bounds
        if( av > MaxArmor[Loc] ) {
            ArmorPoints[Loc] = MaxArmor[Loc];
        } else if( av < 0 ) {
            ArmorPoints[Loc] = 0;
        } else {
            ArmorPoints[Loc] = av;
        }
        Owner.SetChanged( true );
    }

    public int GetLocationArmor( int Loc ) {
        return ArmorPoints[Loc];
    }

    public int GetLocationMax( int Loc ) {
        return MaxArmor[Loc];
    }

    public int GetMaxArmor() {
        // returns the maximum amount of armor allowed.\
        return (int) (Owner.GetTonnage() * 3.5) + 40;
    }

    public int GetArmorValue() {
        int result = 0;
        result += ArmorPoints[0];
        result += ArmorPoints[1];
        result += ArmorPoints[2];
        result += ArmorPoints[3];
        result += ArmorPoints[4];
        result += ArmorPoints[5];
        result += ArmorPoints[6];
        result += ArmorPoints[7];
        result += ArmorPoints[8];
        result += ArmorPoints[9];
        result += ArmorPoints[10];
        return result;
    }

    public int GetModularArmorValue() {
        int result = 0;
        return result;
    }

    public boolean IsCommercial() {
        if ( Config == Commercial )
            return true;
        else
            return false;
    }

    public boolean IsFerroLamellor() {
        if ( Config == CLFL )
            return true;
        else
            return false;
    }

    public boolean IsHardened() {
        if ( Config == Hardened )
            return true;
        else
            return false;
    }

    public boolean IsReflective() {
        if ( Config == ISLR || Config == CLLR )
            return true;
        else
            return false;
    }

    public boolean IsReactive() {
        if ( Config == ISRE || Config == CLRE )
            return true;
        else
            return false;
    }

    public boolean RequiresExtraRules() {
        if (IsHardened() || IsReactive() || IsReflective() || IsStealth() ) {
            return true;
        } else {
            return false;
        }
    }

    public int GetTechBase() {
        return Config.GetAvailability().GetTechBase();
    }

    public boolean IsStealth() {
        return Config.IsStealth();
    }

    @Override
    public boolean Place( ifCVLoadout l ) {
        return true;
    }

    @Override
    public boolean Place( ifCVLoadout l, LocationIndex[] a ) {
        return true;
    }

    @Override
    public boolean CanArmor() {
        // armor is always roll again, so no armoring
        return false;
    }

    public String ActualName() {
        return Config.ActualName();
    }

    public String CritName() {
        return Config.CritName();
    }

    public String LookupName() {
        return Config.LookupName();
    }

    public String ChatName() {
        return Config.ChatName();
    }

    public String MegaMekName( boolean UseRear ) {
        return Config.MegaMekName( UseRear );
    }

    public String BookReference() {
        return Config.BookReference();
    }

    @Override
    public int NumCrits() {
        return Config.NumCrits();
    }

    @Override
    public int NumPlaced() {
        return Placed;
    }

    @Override
    public void IncrementPlaced() {
        Placed++;
    }

    @Override
    public void DecrementPlaced() {
        Placed--;
    }

    @Override
    public double GetTonnage() {
        // this has to return the nearest half-ton.
        if( Owner.UsingFractionalAccounting() ) {
            return CommonTools.RoundFractionalTons( GetArmorValue() * Config.GetPointsPerTon() );
//            return Math.ceil( GetArmorValue() * Config.GetPointsPerTon() * 1000 ) * 0.001;
        }
        double result = GetArmorValue() / ( 8 * Config.GetAVMult() );
        int mid = (int) Math.floor( result + 0.9999 );
        result = mid * 0.5;
        return result;
    }

    public double GetWastedTonnage() {
        // returns the amount of tonnage wasted due to unspent armor points
        if( Owner.UsingFractionalAccounting() ) { return 0.0; }
        double result = GetTonnage() - GetArmorValue() / ( 16 * Config.GetAVMult() );
        if( result < 0.0 ) { result = 0.0; }
        return (double) Math.floor( result * 100 ) / 100;
    }

    public int GetWastedAV() {
        // returns the amount of armor points left in the current half-ton lot
        // get the amount of wasted tonnage
        if( Owner.UsingFractionalAccounting() ) { return 0; }
        double Waste = 0.5 - ( GetTonnage() - GetArmorValue() / ( 16 * Config.GetAVMult() ) );
        int result = (int) Math.floor( ( 8 * Config.GetAVMult() ) - ( Waste * 16 * Config.GetAVMult() ) );
        if( result < 0 ) { result = 0; }
        return result;
    }

    public double GetCoverage() {
        // returns the amount of max armor coverage on this mech as a percentage
        double result = (double) GetArmorValue() / (double) GetMaxArmor();
        return (double) Math.floor( result * 10000 ) / 100;
    }

    public double GetMaxTonnage() {
        // returns the maximum armor tonnage supported by this mech.
        if( Owner.UsingFractionalAccounting() ) {
            return CommonTools.RoundFractionalTons( GetMaxArmor() * Config.GetPointsPerTon() );
//            return Math.ceil( GetMaxArmor() * Config.GetPointsPerTon() * 1000 ) * 0.001;
        }
        double result = GetMaxArmor() / ( 8 * Config.GetAVMult() );
        int mid = (int) Math.round( result + 0.4999 );
        result = mid * 0.5;
        return result;
    }

    public double GetAVMult() {
        // convenience method for armor placement
        return Config.GetAVMult();
    }

    public double GetPointsPerTon() {
        return Config.GetPointsPerTon();
    }

    public double GetBVTypeMult() {
        return Config.GetBVTypeMult();
    }

    public ifState[] GetStates() {
        ifState[] retval = { (ifState) Industrial, (ifState) Commercial, (ifState) PBM, (ifState) Standard, (ifState) ISFF, (ifState) CLFF,
            (ifState) ISLF, (ifState) ISHF, (ifState) ISST, (ifState) Hardened, (ifState) ISLR, (ifState) CLLR, (ifState) ISRE, (ifState) CLRE,
             (ifState) CLFL };
        return retval;
    }

    @Override
    public double GetCost() {
        if( Owner.GetYear() < 2450 ) {
            return GetTonnage() * Config.GetCostMult() * 2.0;
        } else {
            return GetTonnage() * Config.GetCostMult();
        }
    }

    public double GetOffensiveBV() {
        return 0.0f;
    }

    public double GetCurOffensiveBV( boolean UseRear, boolean UseTC, boolean UseAES ) {
        return GetOffensiveBV();
    }

    public double GetDefensiveBV() {
        return ( GetArmorValue() + GetModularArmorValue() ) * Config.GetBVTypeMult() * 2.5;
    }

    public int GetBAR() {
        return Config.GetBAR();
    }

    @Override
    public void ResetPlaced() {
        Placed = 0;
    }

    @Override
    public boolean Contiguous() {
        return false;
    }

    @Override
    public boolean LocationLocked() {
        return Config.LocationLocked();
    }

    @Override
    public void SetLocked( boolean l ) {
        Config.SetLocked( l );
    }

    @Override
    public boolean CoreComponent() {
        return true;
    }

    @Override
    public AvailableCode GetAvailability() {
        return Config.GetAvailability();
    }

    @Override
    public boolean IsCritable() {
        return false;
    }

    @Override
    public MechModifier GetMechModifier() {
        return Config.GetMechModifier();
    }

    @Override
    public String toString() {
        if( Config.NumCrits() > 0 ) {
            if( Config.NumCrits() > Placed ) {
                if( Config.IsStealth() ) {
                    return Config.CritName();
                } else {
                    return Config.CritName() + " (" + ( Config.NumCrits() - Placed ) + ")";
                }
            } else {
                return Config.CritName();
            }
        }
        return Config.CritName();
    }
}
