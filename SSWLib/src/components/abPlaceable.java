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

/**
 * Provides a class for items that can be placed into a loadout.  This is very
 * generic, but should work for Vehicles and Aerospace fighter as-is.  'Mech
 * items may need to create or implement even more methods.
 * 
 * @author Justin Bengtson
 */
public abstract class abPlaceable {
    // An abstract class for items that can be placed inside a loadout.
    private boolean Locked = false;
    private String Manufacturer = "Unknown";
    private Exclusion Exclusions = null;

    public abstract String GetCritName();

    public String GetPrintName(){
        return GetCritName();
    }

    public abstract String GetMegaMekName( boolean UseRear );
    public abstract float GetTonnage();
    public abstract float GetCost();
    public abstract float GetOffensiveBV();
    public abstract float GetDefensiveBV();
    public abstract int GetMechSpace();
    public abstract int GetCVSpace();
    public abstract int GetAeroSpace();

    public boolean CanSplit() {
        return false;
    }

    public boolean CanAllocHD() {
        return true;
    }

    public boolean CanAllocCT() {
        return true;
    }

    public boolean CanAllocTorso() {
        return true;
    }

    public boolean CanAllocArms() {
        return true;
    }

    public boolean CanAllocLegs() {
        return true;
    }

    public boolean OmniActuatorRestricted() {
        return false;
    }

    public boolean CanAllocBody() {
        return true;
    }

    public boolean CanAllocFront() {
        return true;
    }

    public boolean CanAllocRear() {
        return true;
    }

    public boolean CanAllocSides() {
        return true;
    }

    public boolean CanAllocTurret() {
        return true;
    }

    public boolean CanAllocNose() {
        return true;
    }

    public boolean CanAllocWings() {
        return true;
    }

    public boolean CanAllocAft() {
        return true;
    }

    public boolean CanAllocFuselage() {
        return true;
    }

/**
 * Most Placeables will use the default method here.  This can be overridden for
 * certain items to creatively place themselves (like Stealth Armor in a 'Mech).
 * 
 * @param l The Loadout to add this item to.
 */
    public void Place(ifLoadout l) {
        l.AddToQueue( this );
    }

    public void Remove(ifLoadout l) {
        l.Remove( this );
    }

    public boolean LocationLocked() {
        return Locked;
    }

    public void SetLocked(boolean l) {
        Locked = l;
    }

    public boolean LocationLinked() {
        return false;
    }

    public boolean CoreComponent() {
        return false;
    }

    public String GetManufacturer() {
        return Manufacturer;
    }

    public void SetManufacturer( String n ) {
        Manufacturer = n;
    }

    public void SetExclusions( Exclusion e ) {
        Exclusions = e;
    }

    public Exclusion GetExclusions() {
        return Exclusions;
    }

    public abstract AvailableCode GetAvailability();
}
