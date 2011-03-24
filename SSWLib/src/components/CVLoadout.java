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
import java.util.Vector;

public class CVLoadout implements ifCVLoadout {
    private CombatVehicle Owner;
    private CVLoadout BaseLoadout = null;
    private String Name = "",
                   Source = "";
    private Vector<abPlaceable> Queue = new Vector<abPlaceable>(),
                                FrontItems = new Vector<abPlaceable>(),
                                LeftItems = new Vector<abPlaceable>(),
                                RightItems = new Vector<abPlaceable>(),
                                RearItems = new Vector<abPlaceable>(),
                                Turret1Items = new Vector<abPlaceable>(),
                                RotorItems = new Vector<abPlaceable>(),
                                Turret2Items = new Vector<abPlaceable>(),
                                BodyItems = new Vector<abPlaceable>(),
                                NonCore = new Vector<abPlaceable>(),
                                TCList = new Vector<abPlaceable>();
    private boolean UseAIVFCS = false,
                    UseAVFCS = false,
                    UseApollo = false,
                    Use_TC = false,
                    UsingClanCASE = false,
                    YearSpecified = false,
                    YearRestricted = false;
    //private TargetingComputer CurTC = new TargetingComputer( this, false );
    private int RulesLevel = AvailableCode.RULES_TOURNAMENT,
                TechBase = AvailableCode.TECH_INNER_SPHERE,
                Era = AvailableCode.ERA_STAR_LEAGUE,
                ProductionEra = AvailableCode.PRODUCTION_ERA_AGE_OF_WAR,
                Year = 2750;

    public CVLoadout( CombatVehicle c ) {
        Owner = c;
    }

    public CombatVehicle GetOwner() {
        return Owner;
    }

    public void SetName(String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String GetName() {
        return Name;
    }

    public void SetSource(String s) {
        throw new UnsupportedOperationException("Not supported yet.");
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
                return true;
            }
        } else {
            RulesLevel = NewLevel;
            return true;
        }
    }

    public int GetTechBase() {
        return TechBase;
    }

    public void SetTechBase(int NewLevel) {
        this.TechBase = NewLevel;
    }

    public int GetEra() {
        return Era;
    }

    public boolean SetEra(int era) {
        this.Era = era;
        return true;
    }

    public int GetYear() {
        return Year;
    }

    public void SetYear(int year, boolean specified) {
        this.Year = year;
    }

    public boolean YearWasSpecified() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetYearRestricted(boolean b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean IsYearRestricted() {
        return YearSpecified;
    }

    public void AddToQueue(abPlaceable p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void RemoveFromQueue(abPlaceable p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abPlaceable GetFromQueueByIndex(int Index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean QueueContains(abPlaceable p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EquipmentCollection GetCollection(abPlaceable p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector GetQueue() {
        return Queue;
    }

    public Vector GetNonCore() {
        return NonCore;
    }

    public Vector GetEquipment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector GetTCList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void FullUnallocate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void ClearLoadout() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SafeClearLoadout() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SafeMassUnallocate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void Transfer(ifCVLoadout l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void AddTo(abPlaceable p, int Loc) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void AddTo(abPlaceable p, abPlaceable[] Loc) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void AddToFront(abPlaceable p) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void AddToLeft(abPlaceable p) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void AddToRight(abPlaceable p) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void AddToRear(abPlaceable p) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void AddToBody(abPlaceable p) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void AddToTurret1(abPlaceable p) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void AddToTurret2(abPlaceable p) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abPlaceable[] GetFrontItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abPlaceable[] GetLeftItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abPlaceable[] GetRightItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abPlaceable[] GetRearItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abPlaceable[] GetBodyItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abPlaceable[] GetTurret1Items() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abPlaceable[] GetTurret2Items() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abPlaceable[] GetItems(int Loc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int Find(abPlaceable p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public LocationIndex FindIndex(abPlaceable p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int[] FindInstances(abPlaceable p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector FindIndexes(abPlaceable p) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void Remove(abPlaceable p) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void UnlockChassis() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ifCVLoadout Clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetBaseLoadout(ifCVLoadout l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ifCVLoadout GetBaseLoadout() {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public void SetNonCore(Vector v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetTCList(Vector v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetEquipment(Vector v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean CanUseClanCASE() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean IsUsingClanCASE() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetClanCASE(boolean b) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public void SetFCSArtemisIV(boolean b) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetFCSArtemisV(boolean b) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetFCSApollo(boolean b) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean UsingArtemisIV() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean UsingArtemisV() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean UsingApollo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean UsingTC() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TargetingComputer GetTC() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void UseTC(boolean use, boolean clan) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void CheckTC() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetSupercharger(boolean b) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void SetSupercharger(Supercharger s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean HasSupercharger() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Supercharger GetSupercharger() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PowerAmplifier GetPowerAmplifier() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void CheckExclusions(abPlaceable a) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int GetProductionEra() {
        return ProductionEra;
    }

    public boolean SetProductionEra(int era) {
        ProductionEra = era;
        return true;
    }
}