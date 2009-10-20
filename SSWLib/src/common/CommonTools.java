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

package common;

import components.AvailableCode;
import java.awt.Font;

public class CommonTools {
    public final static String tab = "    ";
    public static Font BoldFont = new Font( "Arial", Font.BOLD, 8 );
    public static Font PlainFont = new Font( "Arial", Font.PLAIN, 8 );
    public static Font ItalicFont = new Font( "Arial", Font.ITALIC, 8 );
    public static Font SmallFont = new Font( "Arial", Font.PLAIN, 7 );
    public static Font SmallItalicFont = new Font( "Arial", Font.ITALIC, 7 );
    public static Font TitleFont = new Font("Arial", Font.BOLD, 14);
    public static Font SectionHeaderFont = new Font("Arial", Font.BOLD, 12);
    public final static String NL = System.getProperty( "line.separator" );
    public final static String Tab = "\t";

    private final static float[][] BVMods = {
        { 2.8f,  2.63f, 2.45f, 2.28f, 2.01f, 1.82f, 1.75f, 1.67f, 1.59f },
        { 2.56f, 2.4f,  2.24f, 2.08f, 1.84f, 1.6f,  1.58f, 1.51f, 1.44f },
        { 2.24f, 2.1f,  1.96f, 1.82f, 1.61f, 1.4f,  1.33f, 1.31f, 1.25f },
        { 1.92f, 1.8f,  1.68f, 1.56f, 1.38f, 1.2f,  1.14f, 1.08f, 1.06f },
        { 1.6f,  1.5f,  1.4f,  1.3f,  1.15f, 1.0f,  0.95f, 0.9f,  0.85f },
        { 1.5f,  1.35f, 1.26f, 1.17f, 1.04f, 0.9f,  0.86f, 0.81f, 0.77f },
        { 1.43f, 1.33f, 1.19f, 1.11f, 0.98f, 0.85f, 0.81f, 0.77f, 0.72f },
        { 1.36f, 1.26f, 1.16f, 1.04f, 0.92f, 0.8f,  0.76f, 0.72f, 0.68f },
        { 1.28f, 1.19f, 1.1f,  1.01f, 0.86f, 0.75f, 0.71f, 0.68f, 0.64f }
    };
    public final static float[] BaseEngineMass = {
        0.5f,0.5f,0.5f,0.5f,1.0f,1.0f,1.0f,1.0f,1.5f,1.5f,1.5f,2.0f,2.0f,2.0f,
        2.5f,2.5f,3.0f,3.0f,3.0f,3.5f,3.5f,4.0f,4.0f,4.0f,4.5f,4.5f,5.0f,5.0f,
        5.5f,5.5f,6.0f,6.0f,6.0f,7.0f,7.0f,7.5f,7.5f,8.0f,8.5f,8.5f,9.0f,9.5f,
        10.0f,10.0f,10.5f,11.0f,11.5f,12.0f,12.5f,13.0f,13.5f,14.0f,14.5f,
        15.5f,16.0f,16.5f,17.5f,18.0f,19.0f,19.5f,20.5f,21.5f,22.5f,23.5f,24.5f,
        25.5f,27.0f,28.5f,29.5f,31.5f,33.0f,34.5f,36.5f,38.5f,41.0f,43.5f,46.0f,
        49.0f,52.5f
    };

    public static float GetAdjustedBV( float BV, int Gunnery, int Piloting ) {
        return BV * BVMods[Gunnery][Piloting];
    }

    public static String DecodeEra( int era ) {
        switch( era ) {
            case AvailableCode.ERA_STAR_LEAGUE:
                return "Age of War/Star League";
            case AvailableCode.ERA_SUCCESSION:
                return "Succession Wars";
            case AvailableCode.ERA_CLAN_INVASION:
                return "Clan Invasion";
            case AvailableCode.ERA_DARK_AGES:
                return "Dark Ages";
            case AvailableCode.ERA_ALL:
                return "All Eras (non-canon)";
            default:
                return "Unknown";
        }
    }

    public static String GetRulesLevelString( int level ) {
        switch( level ) {
            case AvailableCode.RULES_TOURNAMENT:
                return "Tournament Legal";
            case AvailableCode.RULES_ADVANCED:
                return "Advanced";
            case AvailableCode.RULES_EXPERIMENTAL:
                return "Experimental";
            case AvailableCode.RULES_UNALLOWED:
                return "Unallowed";
            default:
                return "Unknown";
        }
    }

    public static String GetTechbaseString( int tech ) {
        switch( tech ) {
            case AvailableCode.TECH_CLAN:
                return "Clan";
            case AvailableCode.TECH_INNER_SPHERE:
                return "Inner Sphere";
            case AvailableCode.TECH_BOTH:
                return "Mixed Tech";
            default:
                return "Unknown";
        }
    }

    public static String GetUnitTypeString( int unit ) {
        switch( unit ){
            case AvailableCode.UNIT_BATTLEMECH:
                return "BattleMech";
            case AvailableCode.UNIT_INDUSTRIALMECH:
                return "IndustrialMech";
            case AvailableCode.UNIT_COMBATVEHICLE:
                return "Combat Vehicle";
            case AvailableCode.UNIT_AEROFIGHTER:
                return "Aerospace Fighter";
            case AvailableCode.UNIT_CONVFIGHTER:
                return "Conventional Fighter";
            default:
                return "??";
        }
    }

    public static boolean IsAllowed( AvailableCode AC, int RulesLevel, int TechBase, boolean Primitive, boolean Industrial, int Era, boolean Restrict, int Year ) {
        // check an available code to see if it can be used legally

        // ensure it's within our rules-level first
        switch( RulesLevel ) {
            case AvailableCode.RULES_INTRODUCTORY:
                // tournament legal
                if( Industrial ) {
                    if( AC.GetRulesLevel_IM() != AvailableCode.RULES_INTRODUCTORY ) { return false; }
                } else {
                    if( AC.GetRulesLevel_BM() != AvailableCode.RULES_INTRODUCTORY ) { return false; }
                }
                break;
            case AvailableCode.RULES_TOURNAMENT:
                // tournament legal
                if( Industrial ) {
                    if( AC.GetRulesLevel_IM() > AvailableCode.RULES_TOURNAMENT || AC.GetRulesLevel_IM() < AvailableCode.RULES_INTRODUCTORY ) { return false; }
                } else {
                    if( AC.GetRulesLevel_BM() > AvailableCode.RULES_TOURNAMENT || AC.GetRulesLevel_BM() < AvailableCode.RULES_INTRODUCTORY ) { return false; }
                }
                break;
            case AvailableCode.RULES_ADVANCED:
                // advanced rules
                if( Industrial ) {
                    if( AC.GetRulesLevel_IM() > AvailableCode.RULES_ADVANCED || AC.GetRulesLevel_IM() < AvailableCode.RULES_INTRODUCTORY ) { return false; }
                } else {
                    if( AC.GetRulesLevel_BM() > AvailableCode.RULES_ADVANCED || AC.GetRulesLevel_BM() < AvailableCode.RULES_INTRODUCTORY ) { return false; }
                }
                break;
            case AvailableCode.RULES_EXPERIMENTAL:
                // experimental rules.  everything allowed.
                if( Industrial ) {
                    if( AC.GetRulesLevel_IM() > AvailableCode.RULES_EXPERIMENTAL || AC.GetRulesLevel_IM() < AvailableCode.RULES_INTRODUCTORY ) { return false; }
                } else {
                    if( AC.GetRulesLevel_BM() > AvailableCode.RULES_EXPERIMENTAL || AC.GetRulesLevel_BM() < AvailableCode.RULES_INTRODUCTORY ) { return false; }
                }
                break;
            case AvailableCode.RULES_ERA_SPECIFIC:
                if( Industrial ) {
                    if( AC.GetRulesLevel_IM() > AvailableCode.RULES_ERA_SPECIFIC || AC.GetRulesLevel_IM() < AvailableCode.RULES_INTRODUCTORY ) { return false; }
                } else {
                    if( AC.GetRulesLevel_BM() > AvailableCode.RULES_ERA_SPECIFIC || AC.GetRulesLevel_BM() < AvailableCode.RULES_INTRODUCTORY ) { return false; }
                }
                break;
            default:
                // Unallowed or Era Specific.  everything allowed until we know better.
                if( Industrial ) {
                    if( AC.GetRulesLevel_IM() > AvailableCode.RULES_EXPERIMENTAL || AC.GetRulesLevel_IM() < AvailableCode.RULES_INTRODUCTORY ) { return false; }
                } else {
                    if( AC.GetRulesLevel_BM() > AvailableCode.RULES_EXPERIMENTAL || AC.GetRulesLevel_BM() < AvailableCode.RULES_INTRODUCTORY ) { return false; }
                }
                break;
        }

        // is this within our techbase?
        switch( TechBase ) {
            case AvailableCode.TECH_INNER_SPHERE:
                if( AC.GetTechBase() == AvailableCode.TECH_CLAN ) { return false; }
                break;
            case AvailableCode.TECH_CLAN:
                if( AC.GetTechBase() == AvailableCode.TECH_INNER_SPHERE ) { return false; }
                break;
            case AvailableCode.TECH_BOTH:
                // this does nothing, put here to avoid default
                break;
            default:
                return false;
        }

        // is the 'Mech primitive and is this equipment allowed?
        if( Primitive ) {
            if( Industrial ) {
                if( ! AC.IsPIMAllowed() ) { return false; }
            } else {
                if( ! AC.IsPBMAllowed() ) { return false; }
            }
        } else {
            if( AC.IsPrimitiveOnly() ) { return false; }
        }

        // are we restricting by year?
        if( Restrict ) {
            // we are.
            switch( TechBase ) {
                case AvailableCode.TECH_INNER_SPHERE:
                    if( AC.WentExtinctIS() ) {
                        if( AC.WasReIntrodIS() ) {
                            if( ( Year >= AC.GetISIntroDate() && Year < AC.GetISExtinctDate() ) || Year >= AC.GetISReIntroDate() ) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            if( Year >= AC.GetISIntroDate() && Year < AC.GetISExtinctDate() ) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    } else {
                        if( Year >= AC.GetISIntroDate() ) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                case AvailableCode.TECH_CLAN:
                    if( AC.WentExtinctCL() ) {
                        if( AC.WasReIntrodCL() ) {
                            if( ( Year >= AC.GetCLIntroDate() && Year < AC.GetCLExtinctDate() ) || Year >= AC.GetCLReIntroDate() ) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            if( Year >= AC.GetCLIntroDate() && Year < AC.GetCLExtinctDate() ) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    } else {
                        if( Year >= AC.GetCLIntroDate() ) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                case AvailableCode.TECH_BOTH:
                    boolean Okay_IS = false, Okay_CL = false;
                    if( AC.WentExtinctIS() ) {
                        if( AC.WasReIntrodIS() ) {
                            if( ( Year >= AC.GetISIntroDate() && Year < AC.GetISExtinctDate() ) || Year >= AC.GetISReIntroDate() ) {
                                Okay_IS = true;
                            } else {
                                Okay_IS = false;
                            }
                        } else {
                            if( Year >= AC.GetISIntroDate() && Year < AC.GetISExtinctDate() ) {
                                Okay_IS = true;
                            } else {
                                Okay_IS = false;
                            }
                        }
                    } else {
                        if( Year >= AC.GetISIntroDate() ) {
                            Okay_IS = true;
                        } else {
                            Okay_IS = false;
                        }
                    }
                    if( AC.WentExtinctCL() ) {
                        if( AC.WasReIntrodCL() ) {
                            if( ( Year >= AC.GetCLIntroDate() && Year < AC.GetCLExtinctDate() ) || Year >= AC.GetCLReIntroDate() ) {
                                Okay_CL = true;
                            } else {
                                Okay_CL = false;
                            }
                        } else {
                            if( Year >= AC.GetCLIntroDate() && Year < AC.GetCLExtinctDate() ) {
                                Okay_CL = true;
                            } else {
                                Okay_CL = false;
                            }
                        }
                    } else {
                        if( Year >= AC.GetCLIntroDate() ) {
                            Okay_CL = true;
                        } else {
                            Okay_CL = false;
                        }
                    }
                    if( Okay_IS || Okay_CL ) {
                        return true;
                    } else {
                        return false;
                    }
            }
        } else {
            // we aren't, go by the era
            switch( Era ) {
            case AvailableCode.ERA_STAR_LEAGUE:
                switch( TechBase ) {
                    case AvailableCode.TECH_INNER_SPHERE: case AvailableCode.TECH_BOTH:
                        if( AC.GetISSLCode() < 'X' ) {
                            return true;
                        } else {
                            return false;
                        }
                    case AvailableCode.TECH_CLAN:
                        return false;
                }
            case AvailableCode.ERA_SUCCESSION:
                switch( TechBase ) {
                    case AvailableCode.TECH_INNER_SPHERE:
                        if( RulesLevel > AvailableCode.RULES_TOURNAMENT ) {
                            if( AC.GetISSWCode() < 'X' ) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            if( AC.GetISSWCode() < 'F' ) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    case AvailableCode.TECH_CLAN:
                        if( AC.GetCLSWCode() < 'X' ) {
                            return true;
                        } else {
                            return false;
                        }
                    case AvailableCode.TECH_BOTH:
                        if( AC.GetBestSWCode() < 'X' ) {
                            return true;
                        } else {
                            return false;
                        }
                    default:
                        return false;
                }
            case AvailableCode.ERA_CLAN_INVASION:
                switch( TechBase ) {
                    case AvailableCode.TECH_INNER_SPHERE:
                        if( AC.GetISCICode() < 'X' ) {
                            return true;
                        } else {
                            return false;
                        }
                    case AvailableCode.TECH_CLAN:
                        if( AC.GetCLCICode() < 'X' ) {
                            return true;
                        } else {
                            return false;
                        }
                    case AvailableCode.TECH_BOTH:
                        if( AC.GetBestCICode() < 'X' ) {
                            return true;
                        } else {
                            return false;
                        }
                    default:
                        return false;
                }
            case AvailableCode.ERA_DARK_AGES:
                switch( TechBase ) {
                    case AvailableCode.TECH_INNER_SPHERE:
                        if( AC.GetISCICode() < 'X' ) {
                            return true;
                        } else {
                            return false;
                        }
                    case AvailableCode.TECH_CLAN:
                        if( AC.GetCLCICode() < 'X' ) {
                            return true;
                        } else {
                            return false;
                        }
                    case AvailableCode.TECH_BOTH:
                        if( AC.GetBestCICode() < 'X' ) {
                            return true;
                        } else {
                            return false;
                        }
                    default:
                        return false;
                }
            case AvailableCode.ERA_ALL:
                // the "All" era.
                return true;
            }
        }

        return false;
    }

    public static String Tabs(int Num) {
        String tabs = "";
        for (int i=0; i < Num; i++) {
            tabs += tab;
        }
        return tabs;
    }


    public static float GetSkillBV( float BV, int Gunnery, int Piloting ) {
        return BV * BVMods[Gunnery][Piloting];
    }

    public static float GetModifierBV( float SkillBV, float Modifier) {
        return SkillBV * Modifier;
    }

    public static float GetFullAdjustedBV( float BV, int Gunnery, int Piloting, float Modifier ) {
        return BV * BVMods[Gunnery][Piloting] * Modifier;
    }

    public static float GetForceSizeMultiplier( int Force1Size, int Force2Size ) {
        if( Force1Size <= 0 || Force2Size <= 0 ) {
            return 0;
        }

        return ( ((float) Force2Size) / ((float) Force1Size) ) + ( ((float) Force1Size) / ((float) Force2Size) ) - 1.0f;
    }

    public static String SafeFileName(String filename) {
        return filename.replace(" ", "%20").replace("'", "");
    }

    public static String GetSafeFilename(String s) {
        s = s.replaceAll("%", "%25");
        s = s.replaceAll(" ", "%20");
        s = s.replaceAll("!", "%21");
        s = s.replaceAll("[{(}]", "%28");
        s = s.replaceAll("[{)}]", "%29");
        s = s.replaceAll("[{;}]", "%3B");
        s = s.replaceAll("[{@}]", "%40");
        s = s.replaceAll("[{&}]", "%26");
        s = s.replaceAll("[{=}]", "%3D");
        s = s.replaceAll("[{+}]", "%2B");
        s = s.replaceAll("[{$}]", "%24");
        s = s.replaceAll("[{?}]", "%3F");
        s = s.replaceAll("[{,}]", "%2C");
        s = s.replaceAll("[{#}]", "%23");
        s = s.replaceAll("[{\\[}]", "%5B");
        s = s.replaceAll("[{\\]}]", "%5D");
        s = s.replaceAll("[{*}]", "%2A");
        return s;
    }

    public static String FormatFileName(String filename) {
        return filename.replace("'", "").replace(" ", "_");
    }

    public static String spaceRight(String value, int length) {
        return String.format("%1$-" + length + "s", value);
    }

    public static String spaceLeft(String value, int length) {
        return String.format("%1$#" + length + "s", value);
    }

    public static String padRight(String value, int length, String character) {
                if ( value.length() < length ) {

            for ( int i=value.length(); i < length; i++) {
                value += character;
            }
        }
        return value;
    }

}
