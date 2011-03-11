package battleforce;

import java.util.Vector;

public class BattleForceData {
    public DataSet  Base = new DataSet(false);
    public DataSet  AdjBase = new DataSet(false);
    public DataSet  AC = new DataSet(true);
    public DataSet  LRM = new DataSet(true);
    public DataSet  SRM = new DataSet(true);
    public DataSet  IF = new DataSet(false);
    public DataSet  FLK = new DataSet(false);
    private int TotalHeatGenerated = 0;
    private int TotalHeatDissipation = 0;
    private Vector<String> Notes = new Vector<String>();

    public BattleForceData() {
        IF.setNormalRound(true);
    }

    public void SetHeat( int TotalDiss ) {
        this.TotalHeatDissipation = TotalDiss;
        Base.SetHeat(TotalHeatGenerated, TotalHeatDissipation);
        AC.SetHeat(TotalHeatGenerated, TotalHeatDissipation);
        LRM.SetHeat(TotalHeatGenerated, TotalHeatDissipation);
        SRM.SetHeat(TotalHeatGenerated, TotalHeatDissipation);
        IF.SetHeat(TotalHeatGenerated, TotalHeatDissipation);
        FLK.SetHeat(TotalHeatGenerated, TotalHeatDissipation);
        AdjBase.SetHeat(TotalHeatGenerated, TotalHeatDissipation);

        //System.out.println("Heat Set " + TotalHeatGenerated + " [" + TotalHeatDissipation + "]");
    }

    public void SetHeat( int TotalHeat, int TotalDiss ) {
        this.TotalHeatGenerated = TotalHeat;
        SetHeat(TotalDiss);
    }
        
    public int BaseMaxShort() {
        return Base.BattleForceValue(AdjBase.baseShort); //+
                //Base.BattleForceValue(AC.baseShort) +
                //Base.BattleForceValue(SRM.baseShort) +
                //Base.BattleForceValue(LRM.baseShort);
    }

    public int BaseMaxMedium() {
        //System.out.println(AdjBase.BattleForceValue(AdjBase.baseMedium) + "/" + AC.BattleForceValue(AC.baseMedium) + "/" + SRM.BattleForceValue(SRM.baseMedium) + "/" + LRM.BattleForceValue(LRM.baseMedium));
        return Base.BattleForceValue(AdjBase.baseMedium); //+
                //Base.BattleForceValue(AC.baseMedium) +
                //Base.BattleForceValue(SRM.baseMedium) +
                //Base.BattleForceValue(LRM.baseMedium);
    }

    public int AdjustedMaxMedium() {
        return (int) AdjBase.heatMedium + (int) AC.heatMedium + (int) SRM.heatMedium + (int) LRM.heatMedium;
    }

    public void CheckSpecials() {
        AdjBase.baseShort = Base.baseShort;
        AdjBase.baseMedium = Base.baseMedium;
        AdjBase.baseLong = Base.baseLong;
        AdjBase.baseExtreme = Base.baseExtreme;

        if ( AC.CheckSpecial() ) Adjust(AC);
        if ( SRM.CheckSpecial() ) Adjust(SRM);
        if ( LRM.CheckSpecial() ) Adjust(LRM);
        
        AdjBase.SetHeat(TotalHeatGenerated, TotalHeatDissipation);
        AdjBase.BattleForceValues();
    }

    public void Adjust( DataSet special ) {
        AdjBase.baseShort -= special.baseShort;
        AdjBase.baseMedium -= special.baseMedium;
        AdjBase.baseLong -= special.baseLong;
        AdjBase.baseExtreme -= special.baseExtreme;
    }

    public void AddBase( double[] vals ) {
        Base.AddBase(vals);
        TotalHeatGenerated += (int)vals[BFConstants.BF_OV];
    }

    public void AddHeat( int Heat ) {
        this.TotalHeatGenerated += Heat;
    }

    public void AddNote( String note ) {
        Notes.add(note);
    }

    @Override
    public String toString() {
        String data = "";
        for ( String note : Notes ) {
            data += note + "\n";
        }
        data += "\n";
        data += "Base\n" + Base.toString();
        data += "AC\n" + AC.toString();
        data += "LRM\n" + LRM.toString();
        data += "SRM\n" + SRM.toString();
        data += "IF\n" + IF.toString();
        data += "FLK\n" + FLK.toString();
        data += "Heat: Dissipation (" + TotalHeatDissipation + ") < Max (" + TotalHeatGenerated + ") [Max-4]";
        data += "\nAdjusted\n" + AdjBase.toString();
        return data;
    }

    /**
     * @return the TotalHeatGenerated
     */
    public int getTotalHeatGenerated() {
        return TotalHeatGenerated;
    }

    /**
     * @return the TotalHeatDissipation
     */
    public int getTotalHeatDissipation() {
        return TotalHeatDissipation;
    }

    public class DataSet {
        private double baseShort = 0.0;
        private double baseMedium = 0.0;
        private double baseLong = 0.0;
        private double baseExtreme = 0.0;
        private double heatShort = 0.0;
        private double heatMedium = 0.0;
        private double heatLong = 0.0;
        private double heatExtreme = 0.0;
        private int BFBaseShort = 0;
        private int BFBaseMedium = 0;
        private int BFBaseLong = 0;
        private int BFBaseExtreme = 0;
        private int BFShort = 0;
        private int BFMedium = 0;
        private int BFLong = 0;
        private int BFExtreme = 0;
        private int TotalHeatGenerated = 0;
        private int TotalHeatDissipation = 0;
        private int LauncherCount = 0;
        private int AmmoCount = 0;
        private boolean hasOverheat = false,
                        isSpecial = false,
                        SpecialDamage = false,
                        useNormalRound = false,
                        NotEnoughAmmo = false;

        public DataSet() {
            this(false);
        }

        public DataSet( boolean isSpecial ) {
            this.isSpecial = isSpecial;
        }

        public boolean CheckSpecial() {
            boolean retval = false;
            if ( TotalHeatDissipation < TotalHeatGenerated ) {
                hasOverheat = true;
                if ( heatMedium > 9.0 ) retval = true;
            } else {
                if ( baseMedium > 9.0 ) retval = true;
            }
            if ( LauncherCount > 0 ) {
                if ( AmmoCount - (LauncherCount * 10) <= 0 ) {
                    NotEnoughAmmo = true;
                    retval = false;
                }
            }
            SpecialDamage = retval;
            BattleForceValues();
            return retval;
        }

        public void Clear() {
            baseShort = 0;
            baseMedium = 0;
            baseLong = 0;
        }

        public String GetAbility() {
            return BFShort + "/" + BFMedium + "/" + BFLong; //+ "/" + BFExtreme;
        }

        public void AddBase( double[] vals ) {
            this.baseShort += vals[BFConstants.BF_SHORT];
            this.baseMedium += vals[BFConstants.BF_MEDIUM];
            this.baseLong += vals[BFConstants.BF_LONG];
            this.baseExtreme += vals[BFConstants.BF_EXTREME];
            this.TotalHeatGenerated += (int)vals[BFConstants.BF_OV];
            LauncherCount += 1;
        }

        public void AddAmmo( int lotsize ) {
            this.AmmoCount += lotsize;
        }

        public void SetHeat( int TotalHeatGenerated, int TotalHeatDissipation ) {
            this.TotalHeatGenerated = TotalHeatGenerated;
            this.TotalHeatDissipation = TotalHeatDissipation;
            if ( TotalHeatDissipation < TotalHeatGenerated ) hasOverheat = true;
            HeatAdjustments();
            BattleForceValues();
        }

        public double HeatAdjustment( double base ) {
            if ( TotalHeatGenerated > 0 && TotalHeatDissipation > 0 && base > 0 ) {
                return (base * TotalHeatDissipation) / TotalHeatGenerated;
            }
            return 0.0;
        }

        private void HeatAdjustments() {
            this.heatShort = HeatAdjustment( baseShort );
            this.heatMedium = HeatAdjustment( baseMedium );
            this.heatLong = HeatAdjustment( baseLong );
            this.heatExtreme = HeatAdjustment( baseExtreme );
        }

        public int BattleForceValue( double base ) {
            if ( base > 9.0 || SpecialDamage || !isSpecial )
                if ( isSpecial || useNormalRound)
                    return (int) Math.round(base / 10);
                else
                    return (int) Math.ceil(base / 10);
            return 0;
        }

        private void BattleForceValues() {
            if ( hasOverheat ) {
                this.BFShort = BattleForceValue( heatShort );
                this.BFMedium = BattleForceValue( heatMedium );
                this.BFLong = BattleForceValue( heatLong );
                this.BFExtreme = BattleForceValue( heatExtreme );
            } else {
                this.BFShort = BattleForceValue( baseShort );
                this.BFMedium = BattleForceValue( baseMedium );
                this.BFLong = BattleForceValue( baseLong );
                this.BFExtreme = BattleForceValue( baseExtreme );
            }
        }

        @Override
        public String toString() {
            String data = "";
            data += " Base: " + String.format( "%1$,.2f", baseShort ) + "/" + String.format( "%1$,.2f", baseMedium ) + "/" + String.format( "%1$,.2f", baseLong ) + "/" + String.format( "%1$,.2f", baseExtreme ) + "\n";
            if ( hasOverheat) data += " Heat: " + String.format( "%1$,.2f", heatShort ) + "/" + String.format( "%1$,.2f", heatMedium ) + "/" + String.format( "%1$,.2f", heatLong ) + "/" + String.format( "%1$,.2f", heatExtreme ) + "\n";
            data += "   BF: " + BFShort + "/" + BFMedium + "/" + BFLong + "/" + BFExtreme + "\n";
            data += " Separate Damage: " + SpecialDamage + "\n";
            if ( NotEnoughAmmo ) { data += "Not enough ammo for all of the launchers (" + LauncherCount + " launchers with " + AmmoCount + " total ammo; needs " + (LauncherCount * 10) + ")\n"; }
            return data;
        }

        /**
         * @return the baseShort
         */
        public double getBaseShort() {
            return baseShort;
        }

        /**
         * @param baseShort the baseShort to set
         */
        public void setBaseShort(double baseShort) {
            this.baseShort = baseShort;
        }

        /**
         * @return the baseMedium
         */
        public double getBaseMedium() {
            return baseMedium;
        }

        /**
         * @param baseMedium the baseMedium to set
         */
        public void setBaseMedium(double baseMedium) {
            this.baseMedium = baseMedium;
        }

        /**
         * @return the baseLong
         */
        public double getBaseLong() {
            return baseLong;
        }

        /**
         * @param baseLong the baseLong to set
         */
        public void setBaseLong(double baseLong) {
            this.baseLong = baseLong;
        }

        /**
         * @return the baseExtreme
         */
        public double getBaseExtreme() {
            return baseExtreme;
        }

        /**
         * @param baseExtreme the baseExtreme to set
         */
        public void setBaseExtreme(double baseExtreme) {
            this.baseExtreme = baseExtreme;
        }

        /**
         * @return the heatShort
         */
        public double getHeatShort() {
            return heatShort;
        }

        /**
         * @return the heatMedium
         */
        public double getHeatMedium() {
            return heatMedium;
        }

        /**
         * @return the heatLong
         */
        public double getHeatLong() {
            return heatLong;
        }

        /**
         * @return the heatExtreme
         */
        public double getHeatExtreme() {
            return heatExtreme;
        }

        /**
         * @return the BFShort
         */
        public int getBFShort() {
            return BFShort;
        }

        /**
         * @return the BFMedium
         */
        public int getBFMedium() {
            return BFMedium;
        }

        /**
         * @return the BFLong
         */
        public int getBFLong() {
            return BFLong;
        }

        /**
         * @return the BFExtreme
         */
        public int getBFExtreme() {
            return BFExtreme;
        }

        /**
         * @return the TotalHeatGenerated
         */
        public int getTotalHeatGenerated() {
            return TotalHeatGenerated;
        }

        /**
         * @param TotalHeatGenerated the TotalHeatGenerated to set
         */
        public void setTotalHeatGenerated(int TotalHeatGenerated) {
            this.TotalHeatGenerated = TotalHeatGenerated;
            HeatAdjustments();
        }

        /**
         * @return the TotalHeatDissipation
         */
        public int getTotalHeatDissipation() {
            return TotalHeatDissipation;
        }

        /**
         * @param TotalHeatDissipation the TotalHeatDissipation to set
         */
        public void setTotalHeatDissipation(int TotalHeatDissipation) {
            this.TotalHeatDissipation = TotalHeatDissipation;
            HeatAdjustments();
        }

        public void setNormalRound( boolean useNormal ) {
            this.useNormalRound = useNormal;
        }
    }
}
