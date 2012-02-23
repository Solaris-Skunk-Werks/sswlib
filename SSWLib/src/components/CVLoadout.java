/*
Copyright (c) 2010, Justin R. Bengtson (poopshotgun@yahoo.com)
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
import common.Constants;
import java.util.ArrayList;
import visitors.VFCSApolloLoader;
import visitors.VFCSArtemisIVLoader;
import visitors.VFCSArtemisVLoader;

public class CVLoadout implements ifCVLoadout, ifLoadout {
    private CombatVehicle Owner;
    private ifCVLoadout BaseLoadout = null;
    private CVHeatSinkFactory HeatSinks;
    private CVJumpJetFactory Jumps;
    private TargetingComputer CurTC = new TargetingComputer( this, false );
    private Supercharger SCharger = new Supercharger( this );
    private String Name = Constants.BASELOADOUT_NAME,
                   Source = "";
    private ArrayList<abPlaceable> Queue = new ArrayList<abPlaceable>(),
                                Equipment = new ArrayList<abPlaceable>(),
                                FrontItems = new ArrayList<abPlaceable>(),
                                LeftItems = new ArrayList<abPlaceable>(),
                                RightItems = new ArrayList<abPlaceable>(),
                                RearItems = new ArrayList<abPlaceable>(),
                                Turret1Items = new ArrayList<abPlaceable>(),
                                RotorItems = new ArrayList<abPlaceable>(),
                                Turret2Items = new ArrayList<abPlaceable>(),
                                BodyItems = new ArrayList<abPlaceable>(),
                                NonCore = new ArrayList<abPlaceable>(),
                                TCList = new ArrayList<abPlaceable>();

    private ArrayList MechMods = new ArrayList();
    private boolean UseAIVFCS = false,
                    UseAVFCS = false,
                    UseApollo = false,
                    Use_TC = false,
                    UsingClanCASE = false,
                    UsingSupercharger = false,
                    YearSpecified = false,
                    YearRestricted = false;
    private Turret Turret1 = new Turret(this, false),
                    Turret2 = new Turret(this, false);
    private CVPowerAmplifier PowerAmplifier = new CVPowerAmplifier(this);
    
    //private TargetingComputer CurTC = new TargetingComputer( this, false );
    private int RulesLevel = AvailableCode.RULES_TOURNAMENT,
                TechBase = AvailableCode.TECH_INNER_SPHERE,
                Era = AvailableCode.ERA_STAR_LEAGUE,
                ProductionEra = AvailableCode.PRODUCTION_ERA_AGE_OF_WAR,
                Year = 2750;

    public CVLoadout( CombatVehicle c ) {
        Owner = c;

        Jumps = new CVJumpJetFactory( this );
        HeatSinks = new CVHeatSinkFactory( this );
    }
    
    public CVLoadout( String name, CombatVehicle m, int BaseNumHS, CVHeatSinkFactory hs, CVJumpJetFactory jump ) {
        // provided for cloning purposes
        Name = name;
        Owner = m;
        Jumps = new CVJumpJetFactory( this, jump );
        HeatSinks = new CVHeatSinkFactory( this, BaseNumHS, hs.CurrentConfig(), hs.GetPlacedHeatSinks() );
    }
    
    public CombatVehicle GetOwner() {
        return Owner;
    }

    public void SetName(String s) {
        Name = s;
        Owner.SetChanged( true );
    }

    public String GetName() {
        return Name;
    }

    public void SetSource(String s) {
        Source = s;
        Owner.SetChanged( true );
    }

    public String GetSource() {
        return Source;
    }

    public int GetRulesLevel() {
        return RulesLevel;
    }

    public boolean SetRulesLevel(int NewLevel) {
        if( Owner.IsOmni() ) {
            if( NewLevel < Owner.GetBaseRulesLevel() ) {
                return false;
            } else {
                RulesLevel = NewLevel;
                Owner.SetChanged( true );
                return true;
            }
        } else {
            RulesLevel = NewLevel;
            Owner.SetChanged( true );
            return true;
        }
    }

    public int GetTechBase() {
        return TechBase;
    }

    public void SetTechBase(int NewLevel) {
        this.TechBase = NewLevel;
        Owner.SetChanged( true );
    }

    public int GetEra() {
        return Era;
    }

    public boolean SetEra(int era) {
        this.Era = era;
        Owner.SetChanged( true );
        return true;
    }

    public int GetYear() {
        return Year;
    }

    public void SetYear(int year, boolean specified) {
        this.Year = year;
        this.YearSpecified = specified;
        Owner.SetChanged( true );
    }

    public boolean YearWasSpecified() {
        return YearSpecified;
    }

    public void SetYearRestricted(boolean b) {
        YearRestricted = b;
        Owner.SetChanged( true );
    }

    public boolean IsYearRestricted() {
        return YearRestricted;
    }

    public void AddToQueue(abPlaceable p) {
        Queue.add(p);
        Owner.SetChanged( true );
    }

    public void RemoveFromQueue(abPlaceable p) {
        UnallocateAll(p, true);
    }

    public abPlaceable GetFromQueueByIndex(int Index) {
        return Queue.get(Index);
    }

    public boolean QueueContains(abPlaceable p) {
        return Queue.contains(p);
    }

    public EquipmentCollection GetCollection(abPlaceable p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList GetQueue() {
        return Queue;
    }

    public ArrayList GetNonCore() {
        ArrayList all = new ArrayList();
        all.addAll(FrontItems);
        all.addAll(LeftItems);
        all.addAll(RightItems);
        all.addAll(RearItems);
        all.addAll(Turret1Items);
        all.addAll(Turret2Items);
        all.addAll(BodyItems);
        all.addAll(Queue);
        return all;
    }

    public ArrayList GetEquipment() {
        ArrayList equip = new ArrayList();
        for ( abPlaceable a : (ArrayList<abPlaceable>) GetNonCore() ) {
            if ( !(a instanceof Ammunition) ) 
                equip.add(a);
        }

        return equip;
    }

    public ArrayList GetTCList() {
        return TCList;
    }

    public void FullUnallocate() {
        Queue = new ArrayList<abPlaceable>();
        Equipment = new ArrayList<abPlaceable>();
        FrontItems = new ArrayList<abPlaceable>();
        LeftItems = new ArrayList<abPlaceable>();
        RightItems = new ArrayList<abPlaceable>();
        RearItems = new ArrayList<abPlaceable>();
        Turret1Items = new ArrayList<abPlaceable>();
        RotorItems = new ArrayList<abPlaceable>();
        Turret2Items = new ArrayList<abPlaceable>();
        BodyItems = new ArrayList<abPlaceable>();
        NonCore = new ArrayList<abPlaceable>();
        TCList = new ArrayList<abPlaceable>();
        Owner.SetChanged( true );
    }

    public void ClearLoadout() {
        FullUnallocate();
        Owner.SetChanged( true );
    }

    public void SafeClearLoadout() {
        FullUnallocate();
        Owner.SetChanged( true );
    }

    public void SafeMassUnallocate() {
        FullUnallocate();
        Owner.SetChanged( true );
    }

    public void Transfer(ifCVLoadout l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void AddTo(abPlaceable p, int Loc) throws Exception {
        switch( Loc ) {
            case LocationIndex.CV_LOC_BODY:
                BodyItems.add(p);
                break;
            case LocationIndex.CV_LOC_FRONT:
                FrontItems.add(p);
                break;
            case LocationIndex.CV_LOC_LEFT:
                LeftItems.add(p);
                break;
            case LocationIndex.CV_LOC_REAR:
                RearItems.add(p);
                break;
            case LocationIndex.CV_LOC_RIGHT:
                RightItems.add(p);
                break;
            case LocationIndex.CV_LOC_TURRET1:
                Turret1Items.add(p);
                break;
            case LocationIndex.CV_LOC_TURRET2:
                Turret2Items.add(p);
                break;
            default:
                throw new Exception( "Location not recognized or not an integer\nwhile placing " + p.CritName() );
        }
        if (GetHeatSinks().GetNumHS() < GetTotalHeat())
            GetHeatSinks().SetNumHS(GetTotalHeat());
    }
    
    public int GetTotalHeat() {
        int total = 0;
        for ( abPlaceable a : (ArrayList<abPlaceable>)GetNonCore() ) {
            if ( a instanceof RangedWeapon && ((RangedWeapon)a).RequiresPowerAmps() ) {
                total += ((RangedWeapon)a).GetHeat();
            }
        }
        return total;
    }

    public void AddTo(abPlaceable p, abPlaceable[] Loc) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void AddToFront(abPlaceable p) throws Exception {
        FrontItems.add(p);
        Owner.SetChanged( true );
    }

    public void AddToLeft(abPlaceable p) throws Exception {
        LeftItems.add(p);
        Owner.SetChanged( true );
    }

    public void AddToRight(abPlaceable p) throws Exception {
        RightItems.add(p);
        Owner.SetChanged( true );
    }

    public void AddToRear(abPlaceable p) throws Exception {
        RearItems.add(p);
        Owner.SetChanged( true );
    }

    public void AddToBody(abPlaceable p) throws Exception {
        BodyItems.add(p);
        Owner.SetChanged( true );
    }

    public void AddToTurret1(abPlaceable p) throws Exception {
        Turret1Items.add(p);
        Owner.SetChanged( true );
    }

    public void AddToTurret2(abPlaceable p) throws Exception {
        Turret2Items.add(p);
        Owner.SetChanged( true );
    }

    public ArrayList<abPlaceable> GetFrontItems() {
        return FrontItems;
    }

    public ArrayList<abPlaceable> GetLeftItems() {
        return LeftItems;
    }

    public ArrayList<abPlaceable> GetRightItems() {
        return RightItems;
    }

    public ArrayList<abPlaceable> GetRearItems() {
        return RearItems;
    }

    public ArrayList<abPlaceable> GetBodyItems() {
        return BodyItems;
    }

    public ArrayList GetTurret1Items() {
        return Turret1Items;
    }

    public ArrayList GetTurret2Items() {
        return Turret2Items;
    }

    public abPlaceable[] GetItems(int Loc) {
        switch(Loc) {
            case LocationIndex.CV_LOC_BODY:
                return (abPlaceable[]) BodyItems.toArray();
            case LocationIndex.CV_LOC_FRONT:
                return (abPlaceable[]) FrontItems.toArray();
            case LocationIndex.CV_LOC_LEFT:
                return (abPlaceable[]) LeftItems.toArray();
            case LocationIndex.CV_LOC_REAR:
                return (abPlaceable[]) RearItems.toArray();
            case LocationIndex.CV_LOC_RIGHT:
                return (abPlaceable[]) RearItems.toArray();
            case LocationIndex.CV_LOC_TURRET1:
                return (abPlaceable[]) Turret1Items.toArray();
            case LocationIndex.CV_LOC_TURRET2:
                return (abPlaceable[]) Turret2Items.toArray();
        }
        return null;
    }

    public int Find(abPlaceable p) {
        if ( FrontItems.contains(p) )
            return LocationIndex.CV_LOC_FRONT;
        if ( LeftItems.contains(p) )
            return LocationIndex.CV_LOC_LEFT;
        if ( RightItems.contains(p) )
            return LocationIndex.CV_LOC_RIGHT;
        if ( RearItems.contains(p) )
            return LocationIndex.CV_LOC_REAR;
        if ( Turret1Items.contains(p) )
            return LocationIndex.CV_LOC_TURRET1;
        if ( Turret2Items.contains(p) )
            return LocationIndex.CV_LOC_TURRET2;
        if ( BodyItems.contains(p) )
            return LocationIndex.CV_LOC_BODY;
        return 11;
    }

    public LocationIndex FindIndex(abPlaceable p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int[] FindInstances(abPlaceable p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList FindIndexes(abPlaceable p) {
        return new ArrayList();
    }

    public int[] FindModularArmor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void FlushIllegal() {
        // since most everything else is taken care of during mech recalculates,
        // this method is provided for non-core equipment
        AvailableCode AC;
        abPlaceable p;
        int Rules = Owner.GetRulesLevel();

        //Owner.CheckArmoredComponents();

        // see if there's anything to flush out
        if( NonCore.size() <= 0 ) { return; }

        for( int i = NonCore.size() - 1; i >= 0; i-- ) {
            p = (abPlaceable) NonCore.get( i );
            AC = p.GetAvailability();
            try {
                CheckExclusions( p );
                if( ! CommonTools.IsAllowed( AC, Owner ) ) {
                    Remove( p );
                }
            } catch( Exception e ) {
                Remove( p );
            }
            if( NonCore.contains( p ) ) {
                if( Rules < AvailableCode.RULES_EXPERIMENTAL ) {
                    p.ArmorComponent( false );
                }
            }
        }
    }

    public boolean UnallocateAll(abPlaceable p, boolean override) {
        FrontItems.remove(p);
        LeftItems.remove(p);
        RightItems.remove(p);
        RearItems.remove(p);
        Turret1Items.remove(p);
        RotorItems.remove(p);
        Turret2Items.remove(p);
        BodyItems.remove(p);
        NonCore.remove(p);
        TCList.remove(p);
        Owner.SetChanged( true );
        return true;
    }

    public void Remove(abPlaceable p) {
        // removes the item completely from the loadout.
        // first, unallocate it.
        UnallocateAll( p, true );

        // Now remove it from the queue
        RemoveFromQueue( p );

        // check to see if this is a core component
        if( ! p.CoreComponent() ) {
            // remove it to the non core list
            if( NonCore.contains( p ) ) {
                NonCore.remove( p );
            }

            if( Equipment.contains( p ) ) {
                Equipment.remove( p );
            }

            if( TCList.contains( p ) ) {
                TCList.remove( p );
            }
        }

        Owner.SetChanged( true );
    }

    public void Unallocate(abPlaceable[] a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void AutoAllocate(abPlaceable p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void AutoAllocate(EquipmentCollection e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean IsAllocated(abPlaceable p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int UnplacedItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int FreeItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void LockChassis() {
        for (abPlaceable a : (ArrayList<abPlaceable>)GetNonCore()) {
            a.SetLocked(true);
        }
    }

    public void UnlockChassis() {
        for (abPlaceable a : (ArrayList<abPlaceable>)GetNonCore()) {
            a.SetLocked(false);
        }
    }

    public ifCVLoadout Clone() {
        // Returns a clone of this loadout.  Normally used for omnimechs.
        ifCVLoadout clone = new CVLoadout( "", Owner, HeatSinks.GetNumHS(), HeatSinks, Jumps );
        clone.SetRulesLevel( RulesLevel );
        clone.SetTechBase( TechBase );
        clone.SetEra( Era );
        clone.SetYear( Year, false );
        clone.SetClanCASE( UsingClanCASE );
        try {
            clone.SetFCSArtemisIV( UseAIVFCS );
            clone.SetFCSArtemisV( UseAVFCS );
            clone.SetFCSApollo( UseApollo );
        } catch( Exception e ) {
            // there shouldn't get any exceptions here since the non-core list
            // will be empty.  Write an event to stderr
            System.err.println( "Could not set Artemis IV for an empty loadout." );
        }
        if( NonCore.size() > 0 ) {
            // have to move the none-core items
            clone.SetNonCore( (ArrayList) NonCore.clone() );
        }
        if( TCList.size() > 0 ) {
            clone.SetTCList( (ArrayList) TCList.clone() );
        }
        if( Equipment.size() > 0 ) {
            clone.SetEquipment( (ArrayList) Equipment.clone() );
        }
        if( HasSupercharger() ) {
            clone.SetSupercharger( SCharger );
        }
        if( Owner.IsOmni() ) {
            clone.SetBaseLoadout( this );
        }
        return clone;
    }

    public void SetBaseLoadout(ifCVLoadout l) {
        BaseLoadout = l;
        Owner.SetChanged( true );
    }

    public ifCVLoadout GetBaseLoadout() {
        return BaseLoadout;
    }

    public void SetFrontItems(abPlaceable[] c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetLeftItems(abPlaceable[] c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetRightItems(abPlaceable[] c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetRearItems(abPlaceable[] c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetBodyItems(abPlaceable[] c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetTurret1(abPlaceable[] c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetTurret2(abPlaceable[] c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetNonCore(ArrayList v) {
        NonCore = v;
    }

    public void SetTCList(ArrayList v) {
        TCList = v;
    }

    public void SetEquipment(ArrayList v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean CanUseClanCASE() {
        return true;
    }

    public boolean IsUsingClanCASE() {
        return UsingClanCASE;
    }

    public void SetClanCASE(boolean b) {
        UsingClanCASE = b;
        Owner.SetChanged( true );
    }

    public void SetISCASE(CASE c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean HasISCASE() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CASE GetISCase() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    // handlers for Artemis IV operations.
    public void SetFCSArtemisIV( boolean b ) throws Exception {
        if( b != UseAIVFCS ) {
            if( UseAVFCS && b ) {
                throw new Exception( "Artemis IV is not compatible with Artemis V.\nDisable Artemis V before enabling Artemis IV." );
            }
            UseAIVFCS = b;
            VFCSArtemisIVLoader k = new VFCSArtemisIVLoader();
            Owner.Visit( k );
            if( k.GetResult() == false ) {
                UseAIVFCS = ( ! b );
                if( UseAIVFCS ) {
                    throw new Exception( "Could not disable Artemis IV because a\nlocked launcher is using an Artemis system." );
                } else {
                    throw new Exception( "Could not enable Artemis IV because a locked\nlauncher did not have space for it's Artemis system." );
                }
            }
        }

        Owner.SetChanged( true );
    }

    public void SetFCSArtemisV( boolean b ) throws Exception {
        if( b != UseAVFCS ) {
            if( UseAIVFCS && b ) {
                throw new Exception( "Artemis V is not compatible with Artemis IV.\nDisable Artemis IV before enabling Artemis V." );
            }
            UseAVFCS = b;
            VFCSArtemisVLoader k = new VFCSArtemisVLoader();
            Owner.Visit( k );
            if( k.GetResult() == false ) {
                UseAVFCS = ( ! b );
                if( UseAVFCS ) {
                    throw new Exception( "Could not disable Artemis V because a\nlocked launcher is using an Artemis V system." );
                } else {
                    throw new Exception( "Could not enable Artemis V because a locked\nlauncher did not have space for it's Artemis V system." );
                }
            }
        }

        Owner.SetChanged( true );
    }

    public void SetFCSApollo( boolean b ) throws Exception {
        if( b != UseApollo ) {
            UseApollo = b;
            VFCSApolloLoader k = new VFCSApolloLoader();
            Owner.Visit( k );
            if( k.GetResult() == false ) {
                UseApollo = ( ! b );
                if( UseApollo ) {
                    throw new Exception( "Could not disable Apollo FCS because a\nlocked launcher is using an Apollo system." );
                } else {
                    throw new Exception( "Could not enable Apollo because a locked\nlauncher did not have space for it's Apollo system." );
                }
            }
        }

        Owner.SetChanged( true );
    }

    public boolean UsingArtemisIV() {
        return UseAIVFCS;
    }

    public boolean UsingArtemisV() {
        return UseAVFCS;
    }

    public boolean UsingApollo() {
        return UseApollo;
    }

    public boolean UsingTC() {
        return Use_TC;
    }

    public TargetingComputer GetTC() {
        return CurTC;
    }

    public void UseTC(boolean use, boolean clan) {
        if( use == Use_TC ) {
            return;
        } else {
            Use_TC = use;
        }

        CurTC.SetClan( clan );
        CheckTC();
        Owner.SetChanged( true );
    }

    public void CheckTC() {
        // this routine checks to see if the targeting computer can be allocated
        // and does so if needed.  It will also remove the TC if it has to.
        if( ! Use_TC ) {
            // remove the TC from the loadout
            Remove( CurTC );
            return;
        }

        if( ! QueueContains( CurTC ) ) {
            if( ! IsAllocated( CurTC ) ) {
                // TC not allocated or in the queue, let's see if we can add it
                if( CurTC.NumCrits() > 0 ) {
                    AddToQueue( CurTC );
                } else {
                    Remove( CurTC );
                }
            }
        }

        // lastly, see if we need to remove it altogether
        if( CurTC.NumCrits() <= 0 ) { Remove( CurTC ); }

        Owner.SetChanged( true );
    }

    public void SetSupercharger(boolean b) throws Exception {
        UsingSupercharger = b;
        Owner.SetChanged( true );
    }

    public void SetSupercharger(Supercharger s) {
        SCharger = s;
        Owner.SetChanged( true );
    }

    public boolean HasSupercharger() {
        return UsingSupercharger;
    }

    public Supercharger GetSupercharger() {
        return SCharger;
    }

    public CVPowerAmplifier GetPowerAmplifier() {
        return PowerAmplifier;
    }

    public void CheckExclusions(abPlaceable p) throws Exception {
              // this checks all the items in the loadout vs. the placeable's exclusions
        // not worried about a return value since we're tossing exceptions

        // check basic requirements first
        if( p instanceof RangedWeapon ) {
            if( ((RangedWeapon) p).RequiresNuclear() &! Owner.GetEngine().IsNuclear() ) {
                throw new Exception( p.CritName() + " may not be mounted as it requires a nuclear engine." );
            }
            if( ((RangedWeapon) p).RequiresFusion() &! Owner.GetEngine().IsFusion() ) {
                throw new Exception( p.CritName() + " may not be mounted as it requires a fusion engine." );
            }
        }
        if( p instanceof ExtendedFuelTank ) {
            if( ! Owner.GetEngine().IsICE() &! Owner.GetEngine().isFuelCell() ) {
                throw new Exception( p.CritName() + " may not be mounted on this Vehicle because the engine is incompatible." );
            }
        }
        if( p.GetExclusions() == null ) { return; }
        String[] exclude = p.GetExclusions().GetExclusions();

        for( int i = 0; i < exclude.length; i++ ) {
            // queue first
            abPlaceable test;
            for( int j = 0; j < GetNonCore().size(); j++ ) {
                test = (abPlaceable) GetNonCore().get( j );
                if( test.CritName().contains( exclude[i] ) ) {
                    throw new Exception( "A Vehicle may not mount an " + p.CritName() + " if it\nalready mounts an " + ((abPlaceable) Queue.get( j )).CritName() );
                }
            }
            // special addition for a targeting computer that is not in the loadout yet
            if( Use_TC ) {
                if( CurTC.CritName().contains( exclude[i] ) ) {
                    throw new Exception( "A Vehicle may not mount an " + p.CritName() + " if it\nalready mounts an " + CurTC.CritName() );
                }
            }
        }
    }

    public int GetProductionEra() {
        return ProductionEra;
    }

    public boolean SetProductionEra(int era) {
        ProductionEra = era;
        Owner.SetChanged( true );
        return true;
    }

    public CVHeatSinkFactory GetHeatSinks() {
        return HeatSinks;
    }

    public CVJumpJetFactory GetJumpJets() {
        return Jumps;
    }

    public Engine GetEngine() {
        return Owner.GetEngine();
    }

    public boolean UsingFractionalAccounting() {
        return Owner.UsingFractionalAccounting();
    }

    public void AddMechModifier( MechModifier mod ) {
        if( mod == null ) { return; }
        if( ! MechMods.contains( mod ) &! Owner.GetMechMods().contains( mod ) ) {
            MechMods.add( mod );
        }
    }

    public void RemoveMechMod( MechModifier m ) {
        if( m == null ) { return; }
        MechMods.remove( m );
    }

    public ArrayList GetMechMods() {
        return MechMods;
    }

    public void UnallocateTC() {
        // unallocates the TC from the loadout and then performs a TC check
        Remove( CurTC );
        Owner.SetChanged( true );
        CheckTC();
    }

    public Turret GetTurret(){
        return Turret1;
    }

    public Turret GetRearTurret() {
        return Turret2;
    }

    public void MoveToQueue( int loc ) {
        switch (loc) {
            case LocationIndex.CV_LOC_TURRET1:
                Queue.addAll(Turret1Items);
                Turret1Items.clear();;
                break;
            case LocationIndex.CV_LOC_TURRET2:
                Queue.addAll(Turret2Items);
                Turret2Items.clear();
                break;
        }
        Owner.SetChanged( true );
    }
    
    public ifUnit GetUnit() {
        return Owner;
    }
    
    public double GetTurretTonnage() {
        Turret1.SetItems(Turret1Items);
        return Turret1.GetTonnage();
    }
    
    public double GetRearTurretTonnage() {
        Turret2.SetItems(Turret2Items);
        return Turret2.GetTonnage();
    }
    
}