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

public interface ifLoadout {
// Loadout information methods
    public void SetName( String s );
    public String GetName();
    public int GetRulesLevel();
    public boolean SetRulesLevel( int NewLevel );

// Individual item methods
    public Vector GetQueue();
    public Vector GetNonCore();
    public void AddToQueue( abPlaceable p );
    public void RemoveFromQueue( abPlaceable p );
    public abPlaceable GetFromQueueByIndex( int Index );
    public void AddTo( abPlaceable p, int Loc, int SIndex ) throws Exception;
    public void AutoAllocate( abPlaceable p );
    public boolean Unallocate( abPlaceable p, boolean override );
    public void Remove( abPlaceable p );

// informational methods
    public int FreeSpace();
    public int UnplacedSpace();
    public int Find( abPlaceable p );
    public boolean IsAllocated( abPlaceable p );    
    public void CheckExclusions( abPlaceable a ) throws Exception;

// Full loadout utilities
    public void FullUnallocate();
    public void ClearLoadout();
    public void FlushIllegal();

// Omni-specific methods
    public void LockChassis();
    public ifLoadout Clone();
    public void Transfer( ifLoadout l );
    public void SetBaseLoadout( ifLoadout l );
    public ifLoadout GetBaseLoadout();
}
