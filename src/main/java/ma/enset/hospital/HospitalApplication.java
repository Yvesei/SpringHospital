package ma.enset.hospital;

import ma.enset.hospital.entities.*;
import ma.enset.hospital.repositories.ConsultationRepository;
import ma.enset.hospital.repositories.MedecinRepository;
import ma.enset.hospital.repositories.PatientRepository;
import ma.enset.hospital.repositories.RendezVousRepository;
import ma.enset.hospital.service.IHospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class HospitalApplication {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RendezVousRepository rdvRepository;

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }


    @Bean
    CommandLineRunner start(IHospital iHospital, PatientRepository patientRepository,
                            MedecinRepository medecinRepository, RendezVousRepository rdvRepository,
                            ConsultationRepository consultationRepository) {
        return args -> {
            // Patient
            patientRepository.save(new Patient(null, "Nazih", new Date(), true, 360, null));
            patientRepository.save(new Patient(null, "ahmed", new Date(), true, 200, null));
            patientRepository.save(new Patient(null, "oussama", new Date(), false, 220, null));

            // Medecin
            Stream.of("youness", "ibrahim", "who").forEach(m -> {
                Medecin medecin = new Medecin();
                medecin.setNom(m);
                medecin.setEmail(m + "@protonmail.com");
                medecin.setSpecialite(Math.random() > 0.5 ? "general" : "specialist");
                iHospital.saveMedecin(medecin);
            });

            // RendezVous
            RendezVous rdv = new RendezVous();
            rdv.setDate(new Date());
            rdv.setStatus(StatusRDV.PENDING);
            rdv.setPatient(patientRepository.findById(1L).orElse(null));
            rdv.setMedecin(medecinRepository.findById(1L).orElse(null));
            rdvRepository.save(rdv); // Corrected

            RendezVous rdv1 = rdvRepository.findById(1L).orElse(null);

            // Consultation
            Consultation consultation = new Consultation();
            consultation.setDateConsultation(new Date());
            consultation.setRendezVous(rdv1);
            consultation.setRapport("Rapport de consultation");
            iHospital.saveConsultation(consultation);
        };

    }
}
