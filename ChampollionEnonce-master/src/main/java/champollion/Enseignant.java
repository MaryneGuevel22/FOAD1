package champollion;

import java.util.Collection;
import java.util.HashMap;

public class Enseignant extends Personne {

	HashMap<UE, ServicePrevu> servicesPrevues;
	HashMap<Intervention, UE> interventions;

    public Enseignant(String nom, String email) {
        super(nom, email);
        this.servicesPrevues = new HashMap <UE, ServicePrevu> ();
        this.interventions = new HashMap <Intervention, UE> ();
    }

    /**
     * Calcule le nombre total d'heures prévues pour cet enseignant en "heures équivalent TD" Pour le calcul : 1 heure
     * de cours magistral vaut 1,5 h "équivalent TD" 1 heure de TD vaut 1h "équivalent TD" 1 heure de TP vaut 0,75h
     * "équivalent TD"
     *
     * @return le nombre total d'heures "équivalent TD" prévues pour cet enseignant, arrondi à l'entier le plus proche
     *
     */
    public int heuresPrevues() {
    	float heuresPrevues = 0F;
    	Collection<ServicePrevu> allServices = servicesPrevues.values();
    	for (ServicePrevu servicePrevu : allServices) {
    		heuresPrevues += servicePrevu.getVolumeCM() * 1.5F + servicePrevu.getVolumeTD() + servicePrevu.getVolumeTP() * 0.75F;
		}
        return (int) heuresPrevues;
    }

    /**
     * Calcule le nombre total d'heures prévues pour cet enseignant dans l'UE spécifiée en "heures équivalent TD" Pour
     * le calcul : 1 heure de cours magistral vaut 1,5 h "équivalent TD" 1 heure de TD vaut 1h "équivalent TD" 1 heure
     * de TP vaut 0,75h "équivalent TD"
     *
     * @param ue l'UE concernée
     * @return le nombre total d'heures "équivalent TD" prévues pour cet enseignant, arrondi à l'entier le plus proche
     *
     */
    public int heuresPrevuesPourUE(UE ue) {
    	ServicePrevu service = this.servicesPrevues.get(ue);
        return (int) (service.getVolumeCM() * 1.5F + service.getVolumeTD() + service.getVolumeTP() * 0.75F);
    }

    /**
     * Ajoute un enseignement au service prévu pour cet enseignant
     *
     * @param ue l'UE concernée
     * @param volumeCM le volume d'heures de cours magitral
     * @param volumeTD le volume d'heures de TD
     * @param volumeTP le volume d'heures de TP
     */
    public void ajouteEnseignement(UE ue, int volumeCM, int volumeTD, int volumeTP) {
    	if(this.servicesPrevues.containsKey(ue)) {
    		ServicePrevu thisService = this.servicesPrevues.get(ue);
    		thisService.setVolumeCM(volumeCM + thisService.getVolumeCM());
    		thisService.setVolumeTD(volumeTD + thisService.getVolumeTD());
    		thisService.setVolumeTP(volumeTP + thisService.getVolumeTP());
    	} else this.servicesPrevues.put(ue, new ServicePrevu(volumeTD, volumeTP, volumeCM));
    }
    
    // détermine si un enseignant est en sous-service
    
    public boolean enSousService() {
    	return this.heuresPrevues() < 192;
    }
    
    // Ajoute une intervention pour l'enseignant concernant une UE
    
    public void ajouteIntervention(UE ue, Intervention intervention) {
    	this.interventions.put(intervention, ue);
    }
    
    // Indique le nombre d'heures du type donné à planifier pour l'enseignant dans l'UE donnée
    
    public int resteAPlanifier(UE ue, TypeIntervention type) {
    	float heuresEffectuees = 0F;
    	for (Intervention intervention : this.interventions.keySet()) {
			if (this.interventions.get(intervention).equals(ue) && intervention.getType().equals(type)) {
				heuresEffectuees += intervention.getDuree();
			}
		}
    	return (int) (this.heuresPrevuesPourUE(ue) - heuresEffectuees);
    }

}
