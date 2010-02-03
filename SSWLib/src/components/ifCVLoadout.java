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

import java.util.Vector;

public interface ifCVLoadout {
    public CombatVehicle GetOwner();
    public void SetName( String s );
    public String GetName();
    public void SetSource( String s );
    public String GetSource();
    public int GetRulesLevel();
    public boolean SetRulesLevel( int NewLevel );
    public int GetTechBase();
    public void SetTechBase( int NewLevel );
    public int GetEra();
    public boolean SetEra( int era );
    public int GetYear();
    public void SetYear( int year, boolean specified );
    public boolean YearWasSpecified();
    public void SetYearRestricted( boolean b );
    public boolean IsYearRestricted();
    public void AddToQueue( abPlaceable p );
    public void RemoveFromQueue( abPlaceable p );
    public abPlaceable GetFromQueueByIndex( int Index );
    public boolean QueueContains( abPlaceable p );
    public EquipmentCollection GetCollection( abPlaceable p );
    public Vector GetQueue();
    public Vector GetNonCore();
    public Vector GetEquipment();
    public Vector GetTCList();
    public void FullUnallocate();
    public void ClearLoadout();
    public void SafeClearLoadout();
    public void SafeMassUnallocate();
    public void Transfer( ifCVLoadout l );
    public void AddTo( abPlaceable p, int Loc ) throws Exception;
    public void AddTo( abPlaceable p, abPlaceable[] Loc ) throws Exception;
    public void AddToFront( abPlaceable p ) throws Exception;
    public void AddToLeft( abPlaceable p ) throws Exception;
    public void AddToRight( abPlaceable p ) throws Exception;
    public void AddToRear( abPlaceable p ) throws Exception;
    public void AddToBody( abPlaceable p ) throws Exception;
    public void AddToTurret1( abPlaceable p ) throws Exception;
    public void AddToTurret2( abPlaceable p ) throws Exception;
    public abPlaceable[] GetFrontItems();
    public abPlaceable[] GetLeftItems();
    public abPlaceable[] GetRightItems();
    public abPlaceable[] GetRearItems();
    public abPlaceable[] GetBodyItems();
    public abPlaceable[] GetTurret1Items();
    public abPlaceable[] GetTurret2Items();
    public abPlaceable[] GetItems( int Loc );
    public int Find( abPlaceable p );
    public LocationIndex FindIndex( abPlaceable p );
    public int[] FindInstances( abPlaceable p );
    public Vector FindIndexes( abPlaceable p );
    public int[] FindModularArmor();
    public void FlushIllegal();
    public boolean UnallocateAll( abPlaceable p, boolean override );
    public void Remove( abPlaceable p );
    public void Unallocate( abPlaceable[] a );
    public void AutoAllocate( abPlaceable p );
    public void AutoAllocate( EquipmentCollection e );
    public boolean IsAllocated( abPlaceable p );
    public int UnplacedItems();
    public int FreeItems();
    public void LockChassis();
    public void UnlockChassis();
    public ifCVLoadout Clone();
    public void SetBaseLoadout( ifCVLoadout l );
    public ifCVLoadout GetBaseLoadout();
    public void SetFrontItems( abPlaceable[] c );
    public void SetLeftItems( abPlaceable[] c );
    public void SetRightItems( abPlaceable[] c );
    public void SetRearItems( abPlaceable[] c );
    public void SetBodyItems( abPlaceable[] c );
    public void SetTurret1( abPlaceable[] c );
    public void SetTurret2( abPlaceable[] c );
    public void SetNonCore( Vector v );
    public void SetTCList( Vector v );
    public void SetEquipment( Vector v );
    public boolean CanUseClanCASE();
    public boolean IsUsingClanCASE();
    public void SetClanCASE( boolean b );
    public void SetISCASE( CASE c );
    public boolean HasISCASE();
    public CASE GetISCase();
    public void SetFCSArtemisIV( boolean b ) throws Exception;
    public void SetFCSArtemisV( boolean b ) throws Exception;
    public void SetFCSApollo( boolean b ) throws Exception;
    public boolean UsingArtemisIV();
    public boolean UsingArtemisV();
    public boolean UsingApollo();
    public boolean UsingTC();
    public TargetingComputer GetTC();
    public void UseTC( boolean use, boolean clan );
    public void CheckTC();
    public void SetSupercharger( boolean b ) throws Exception;
    public void SetSupercharger( Supercharger s );
    public boolean HasSupercharger();
    public Supercharger GetSupercharger();
    public PowerAmplifier GetPowerAmplifier();
    public void CheckExclusions( abPlaceable a ) throws Exception;
/*
    public void AddMechModifier( MechModifier m );
    public void RemoveMechMod( MechModifier m );
    public Vector GetMechMods();
 */
}
