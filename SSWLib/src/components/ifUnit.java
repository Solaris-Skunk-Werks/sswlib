/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package components;

public interface ifUnit {
    public int GetUnitType();
    public int GetRulesLevel();
    public int GetTechbase();
    public int GetBaseTechbase();
    public int GetEra();
    public int GetProductionEra();
    public int GetYear();
    public int GetTonnage();
    public boolean IsYearRestricted();
    public boolean UsingFractionalAccounting();
    public void SetChanged( boolean b );
    public boolean HasFHES();
}
