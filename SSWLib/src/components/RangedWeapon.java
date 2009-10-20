/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package components;

/**
 *
 * @author justin
 */
public class RangedWeapon extends abPlaceable {
    public final static int W_BALLISTIC = 0,
                            W_ENERGY = 1,
                            W_MISSILE = 2,
                            W_ARTILLERY = 3,
                            A4_NONE = 0,
                            A4_ARTEMIS4 = 1,
                            A4_APOLLO = 2,
                            AERO_RANGEPB = 0,
                            AERO_RANGESHORT = 1,
                            AERO_RANGEMEDIUM = 2,
                            AERO_RANGELONG = 3,
                            AERO_RANGEEXTREME = 4;
    public final static String[] AeroRanges = {
        "Point Blank", "Short", "Medium", "Long", "Extreme" };

    private AvailableCode AC;
    private String CritName = "",
                   MegaMekName = "",
                   PrintName = "",
                   Type = "",
                   Specials = "";
    private int WeaponClass = 0,
                Heat = 0,
                HeatAero = 0,
                DamageShort = 0,
                DamageMedium = 0,
                DamageLong = 0,
                AeroDamShort = 0,
                AeroDamMedium = 0,
                AeroDamLong = 0,
                AeroDamExtreme = 0,
                ClusterSize = 0,
                ClusterGroup = 0,
                RangeMinimum = 0,
                RangeShort = 0,
                RangeMedium = 0,
                RangeLong = 0,
                RangeAero = 0,
                ToHitShort = 0,
                ToHitMedium = 0,
                ToHitLong = 0,
                AmmoIndex = 0,
                AmmoLotSize = 0,
                MechSpace = 0,
                CVSpace = 0,
                AeroSpace = 0,
                Artemis4Type = A4_NONE;
    private boolean Clustered = false,
                    HasAmmo = false,
                    SwitchableAmmo = false,
                    RequiresFusion = false,
                    RequiresNuclear = false,
                    RequiresHeatSinks = false,
                    RequiresPowerAmps = false,
                    CanSplit = false,
                    Alloc_MechRear = true,
                    Alloc_HD  = true,
                    Alloc_CT = true,
                    Alloc_Torso = true,
                    Alloc_Arm = true,
                    Alloc_Leg = true,
                    OmniActuatorRestrict = false,
                    Alloc_Front = true,
                    Alloc_Body = true,
                    Alloc_Sides = true,
                    Alloc_Rear = true,
                    Alloc_Turret = true,
                    Alloc_Nose = true,
                    Alloc_Wings = true,
                    Alloc_Aft = true,
                    Alloc_Fuselage = true,
                    OneShot = false,
                    Streak = false,
                    Ultra = false,
                    Rotary = false,
                    Explosive = false,
                    CanUseTC = false,
                    CanUseCapacitor = false,
                    ArrayCapable = false,
                    CanUseArtemis = false;
    private float Tonnage = 0.0f,
                  Cost = 0.0f,
                  OffensiveBV = 0.0f,
                  DefensiveBV = 0.0f;

/*
 * Constructor
 */

    public RangedWeapon( String name, String mmname, String printname, AvailableCode a, int wepclass ) {
        CritName = name;
        MegaMekName = mmname;
        PrintName = printname;
        AC = a;
        WeaponClass = wepclass;
    }

/*
 * Setters
 */

    public void SetHeat( int h, int a ) {
        Heat = h;
        HeatAero = a;
    }

    public void SetToHit( int s, int m, int l ) {
        ToHitShort = s;
        ToHitMedium = m;
        ToHitLong = l;
    }

    public void SetDamage( int s, int m, int l, int as, int am, int al, int ae, boolean clust, int cs, int cg ) {
        DamageShort = s;
        DamageMedium = m;
        DamageLong = l;
        AeroDamShort = as;
        AeroDamMedium = am;
        AeroDamLong = al;
        AeroDamExtreme = ae;
        Clustered = clust;
        ClusterSize = cs;
        ClusterGroup = cg;
    }

    public void SetRange( int min, int s, int m, int l, int a ) {
        RangeMinimum = min;
        RangeShort = s;
        RangeMedium = m;
        RangeLong = l;
        RangeAero = a;
    }

    public void SetSpecials( String type, String specials ) {
        Type = type;
        Specials = specials;
    }

    public void SetAmmo( boolean has, int size, int idx, boolean Switch ) {
        HasAmmo = has;
        AmmoIndex = idx;
        AmmoLotSize = size;
        SwitchableAmmo = Switch;
    }

    public void SetStats( float tons, int crits, int vspc, int aspc, float cost, float obv, float dbv ) {
        MechSpace = crits;
        CVSpace = vspc;
        AeroSpace = aspc;
        Tonnage = tons;
        Cost = cost;
        OffensiveBV = obv;
        DefensiveBV = dbv;
    }

    public void SetArtemis( boolean canuse, int a4type ) {
        CanUseArtemis = canuse;
        Artemis4Type = a4type;
    }

    public void SetRequirements( boolean fus, boolean nuc, boolean needshs, boolean needspa ) {
        RequiresFusion = fus;
        RequiresNuclear = nuc;
        RequiresHeatSinks = needshs;
        RequiresPowerAmps = needspa;
    }

    public void SetWeapon( boolean os, boolean streak, boolean ultra, boolean rotary, boolean explode, boolean tc, boolean array, boolean capacitor ) {
        OneShot = os;
        Streak = streak;
        Ultra = ultra;
        Rotary = rotary;
        Explosive = explode;
        CanUseTC = tc;
        CanUseCapacitor = capacitor;
        ArrayCapable = array;
    }

    public void SetMechAlloc( boolean split, boolean rear, boolean hd, boolean ct, boolean st, boolean arm, boolean leg, boolean omniarm ) {
        CanSplit = split;
        Alloc_MechRear = rear;
        Alloc_HD = hd;
        Alloc_CT = ct;
        Alloc_Torso = st;
        Alloc_Arm = arm;
        Alloc_Leg = leg;
        OmniActuatorRestrict = omniarm;
    }

    public void SetCVAlloc( boolean fr, boolean rear, boolean sides, boolean body, boolean turret ) {
        Alloc_Front = fr;
        Alloc_Body = body;
        Alloc_Rear = rear;
        Alloc_Sides = sides;
        Alloc_Turret = turret;
    }

    public void SetAeroAlloc( boolean nose, boolean wings, boolean aft, boolean fuse ) {
        Alloc_Nose = nose;
        Alloc_Wings = wings;
        Alloc_Aft = aft;
        Alloc_Fuselage = fuse;
    }

/*
 * Getters
 */

    public int GetWeaponClass() {
        return WeaponClass;
    }

    public String GetCritName() {
        return CritName;
    }

    public String GetMegaMekName( boolean UseRear ) {
        return MegaMekName;
    }

    @Override
    public String GetPrintName() {
        return PrintName;
    }

    public AvailableCode GetAvailability() {
        return AC;
    }

    public String GetType() {
        return Type;
    }

    public String GetSpecials() {
        return Specials;
    }

    public int GetHeat() {
        return Heat;
    }

    public int GetHeatAero() {
        return HeatAero;
    }

    public int GetToHitShort() {
        return ToHitShort;
    }

    public int GetToHitMedium() {
        return ToHitMedium;
    }

    public int GetToHitLong() {
        return ToHitLong;
    }

    public int GetDamageShort() {
        return DamageShort;
    }

    public int GetDamageMedium() {
        return DamageMedium;
    }

    public int GetDamageLong() {
        return DamageLong;
    }

    public int GetAeroDamageShort() {
        return AeroDamShort;
    }

    public int GetAeroDamageMedium() {
        return AeroDamMedium;
    }

    public int GetAeroDamageLong() {
        return AeroDamLong;
    }

    public int GetAeroDamageExtreme() {
        return AeroDamExtreme;
    }

    public boolean IsClustered() {
        return Clustered;
    }

    public int GetClusterSize() {
        return ClusterSize;
    }

    public int GetClusterGrouping() {
        return ClusterGroup;
    }

    public int GetRangeMinimum() {
        return RangeMinimum;
    }

    public int GetRangeShort() {
        return RangeShort;
    }

    public int GetRangeMedium() {
        return RangeMedium;
    }

    public int GetRangeLong() {
        return RangeLong;
    }

    public int GetRangeAero() {
        return RangeAero;
    }

    public String GetRangeAeroString() {
        try {
            return AeroRanges[RangeAero];
        } catch( Exception e ) {
            return "Unknown";
        }
    }

    public boolean HasAmmo() {
        return HasAmmo;
    }

    public int GetAmmoIndex() {
        return AmmoIndex;
    }

    public int GetAmmoLotSize() {
        return AmmoLotSize;
    }

    public boolean SwitchableAmmo() {
        return SwitchableAmmo;
    }

    public int GetMechSpace() {
        return MechSpace;
    }

    public int GetCVSpace() {
        return CVSpace;
    }

    public int GetAeroSpace() {
        return AeroSpace;
    }

    public float GetTonnage() {
        return Tonnage;
    }

    public float GetCost() {
        return Cost;
    }

    public float GetOffensiveBV() {
        return OffensiveBV;
    }

    public float GetDefensiveBV() {
        return DefensiveBV;
    }

    public boolean RequiresFusion() {
        return RequiresFusion;
    }

    public boolean RequiresNuclear() {
        return RequiresNuclear;
    }

    public boolean RequiresHeatSinks() {
        return RequiresHeatSinks;
    }

    public boolean RequiresPowerAmps() {
        return RequiresPowerAmps;
    }

    @Override
    public boolean CanSplit() {
        return CanSplit;
    }

    @Override
    public boolean CanAllocHD() {
        return Alloc_HD;
    }

    @Override
    public boolean CanAllocCT() {
        return Alloc_CT;
    }

    @Override
    public boolean CanAllocTorso() {
        return Alloc_Torso;
    }

    @Override
    public boolean CanAllocArms() {
        return Alloc_Arm;
    }

    @Override
    public boolean CanAllocLegs() {
        return Alloc_Leg;
    }

    @Override
    public boolean OmniActuatorRestricted() {
        return OmniActuatorRestrict;
    }

    @Override
    public boolean CanAllocFront() {
        return Alloc_Front;
    }

    @Override
    public boolean CanAllocBody() {
        return Alloc_Body;
    }

    @Override
    public boolean CanAllocSides() {
        return Alloc_Sides;
    }

    @Override
    public boolean CanAllocRear() {
        return Alloc_Rear;
    }

    @Override
    public boolean CanAllocTurret() {
        return Alloc_Turret;
    }

    @Override
    public boolean CanAllocNose() {
        return Alloc_Nose;
    }

    @Override
    public boolean CanAllocWings() {
        return Alloc_Wings;
    }

    @Override
    public boolean CanAllocAft() {
        return Alloc_Aft;
    }

    @Override
    public boolean CanAllocFuselage() {
        return Alloc_Fuselage;
    }

    public boolean IsOneShot() {
        return OneShot;
    }

    public boolean IsStreak() {
        return Streak;
    }

    public boolean IsUltra() {
        return Ultra;
    }

    public boolean IsRotary() {
        return Rotary;
    }

    public boolean IsExplosive() {
        return Explosive;
    }

    public boolean CanUseTC() {
        return CanUseTC;
    }

    public boolean CanUseCapacitor() {
        return CanUseCapacitor;
    }

    public boolean ArrayCapable() {
        return ArrayCapable;
    }

    public boolean CanUseArtemis() {
        return CanUseArtemis;
    }

    public int GetArtemisType() {
        return Artemis4Type;
    }

/*
 * toString
 */

    @Override
    public String toString() {
        return CritName;
    }
}
