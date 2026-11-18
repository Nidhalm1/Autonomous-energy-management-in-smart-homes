package fr.sorbonne_u.components.hem2026e1.equipments.cooktop;

import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.PreconditionException;

public interface CookTopConfigurationI {
    // description de la configuration physique de l'appareil de cuisson 
    // Elle ne décrit pas le comportement mais seulement la configuration 
    // elle doit contenir les unités de mesure utilisées pour les grandeurs physiques manipulées par l'appareil de cuisson, constantes , etc.

    public static final MeasurementUnit	POWER_UNIT = MeasurementUnit.WATTS;
    public static final MeasurementUnit	TENSION_UNIT = MeasurementUnit.VOLTS;
    // Elle vérifie donc que les unités ont été bien définies ( la configuration existe)
    public static final Measure<Double>	ACTIVATION_MEASURE = new Measure<>(5.0, POWER_UNIT);
    public static final Measure<Double>	POWER_1_MAX = new Measure<>(1500.0, POWER_UNIT);
    public static final Measure<Double>	POWER_23_MAX = new Measure<>(2000.0, POWER_UNIT);
    public static final Measure<Double>	POWER_4_MAX = new Measure<>(2500.0, POWER_UNIT);
    public static final Measure<Double> TENSION = new Measure<>(230.0, TENSION_UNIT);

    public static final int NUMBER_ZONE = 4; //nombre de foyer
    public static final int MAX_HEATING_POWER = 9; // la puissance maximale de chauffe du cooktop est de 9

    public static Measure<Double> getPowerMax(int zone) {
        switch(zone){
            case 1:
                return POWER_1_MAX;
            case 2:
            case 3:
                return POWER_23_MAX;
            default :
                return POWER_4_MAX;
        }
    }

    public static double getZonePower(int indexZone, int powerLevel) {
        assert powerLevel >= 0 && powerLevel <= MAX_HEATING_POWER : new PreconditionException("Power level must be between 0 and " + MAX_HEATING_POWER);
        return (getPowerMax(indexZone).getData() / MAX_HEATING_POWER) * powerLevel;
    }

    public static boolean staticInvariants() {
        boolean ret = true;
        ret &= AssertionChecking.checkStaticInvariant(
                POWER_UNIT != null,
                CookTopConfigurationI.class,
                "POWER_UNIT != null");
        ret &= AssertionChecking.checkStaticInvariant(
                TENSION_UNIT != null,
                CookTopConfigurationI.class,
                "TENSION_UNIT != null");
        ret &= AssertionChecking.checkStaticInvariant(
                TENSION != null && TENSION.getMeasurementUnit().equals(TENSION_UNIT) && TENSION.getData() == 230.0,
                CookTopConfigurationI.class,
                "TENSION != null && TENSION.getMeasurementUnit().equals(TENSION_UNIT) && TENSION.getData() == 230.0");  
        ret &= AssertionChecking.checkStaticInvariant(
                ACTIVATION_MEASURE != null && ACTIVATION_MEASURE.getMeasurementUnit().equals(POWER_UNIT) && ACTIVATION_MEASURE.getData() > 0.0,
                CookTopConfigurationI.class,
                "ACTIVATION_MEASURE != null && ACTIVATION_MEASURE.getMeasurementUnit().equals(POWER_UNIT) && ACTIVATION_MEASURE.getData() > 0.0");
        ret &= AssertionChecking.checkStaticInvariant(
                POWER_1_MAX != null && POWER_1_MAX.getMeasurementUnit().equals(POWER_UNIT) && POWER_1_MAX.getData() > 0.0,
                CookTopConfigurationI.class,
                "POWER_1_MAX != null && POWER_1_MAX.getMeasurementUnit().equals(POWER_UNIT) && POWER_1_MAX.getData() > 0.0");
        ret &= AssertionChecking.checkStaticInvariant(
                POWER_23_MAX != null && POWER_23_MAX.getMeasurementUnit().equals(POWER_UNIT) && POWER_23_MAX.getData() > 0.0,
                CookTopConfigurationI.class,
                "POWER_23_MAX != null && POWER_23_MAX.getMeasurementUnit().equals(POWER_UNIT) && POWER_23_MAX.getData() > 0.0");
        ret &= AssertionChecking.checkStaticInvariant(
                POWER_4_MAX != null && POWER_4_MAX.getMeasurementUnit().equals(POWER_UNIT) && POWER_4_MAX.getData() > 0.0,
                CookTopConfigurationI.class,
                "POWER_4_MAX != null && POWER_4_MAX.getMeasurementUnit().equals(POWER_UNIT) && POWER_4_MAX.getData() > 0.0");
        ret &= AssertionChecking.checkStaticInvariant(
                NUMBER_ZONE > 0 && MAX_HEATING_POWER > 0,
                CookTopConfigurationI.class,
                "NUMBER_ZONE > 0 && MAX_HEATING_POWER   > 0");
        return ret;
    }
}

/** Cours
 * BCM est un modèle de programmation par composants.
 * L'idée principale est de ne pas faire communiquer directement les objets entre eux
 * Les composants communiquent avec des interfaces et des ports ( composant -> connector -> composant)
 * Un composant BCM contient généralement :
 * un état interne , des interfaces offertes et requieses , des ports entrants et sortants , un cycle de vie 
 * 
 * Les interfaces CI : elle décrit les services visibles par les autres composants ( elle décrit cee qu'un utilisateur peut demander a l'équipement)
 * L'interret de l'interface de configuration est de pouvoir partager les mêmes constantes entre plusieurs fichier qui compose l'équipement
 * Cela permet d'évite de redéclarer les constantes
 * 
 */