package person.state;

import entity.PersonEntity;
import entity.PhysicalCertificateEntity;

import entity.PoliceRegisterEntity;
import entity.PsychicalCertificateEntity;
import exception.PersonNotFoundException;
import view.View;

import javax.persistence.EntityManager;

public class EligibleForInspection implements PersonState {
    @Override
    public void getPhysicalCertificate(EntityManager entityManager, int peselNumber) {
        PhysicalCertificateEntity.createPhysicalCertificate(entityManager, PersonEntity.queryPersonByCriteria(entityManager, peselNumber)
                .stream()
                .findFirst()
                .orElseThrow(PersonNotFoundException::new));
    }

    @Override
    public void getPsychicalCertificate(EntityManager entityManager, int peselNumber) {
        PsychicalCertificateEntity.createPsychicalCertificate(entityManager, PersonEntity.queryPersonByCriteria(entityManager, peselNumber)
                .stream()
                .findFirst()
                .orElseThrow(PersonNotFoundException::new));
    }

    @Override
    public void getGunCertificate(EntityManager entityManager, PersonEntity personEntity) {
        View.printNotAllowed();
    }

    @Override
    public void registerGun(EntityManager entityManager, PersonEntity personEntity) {
        View.printNotAllowed();
    }

    @Override
    public void addToPoliceReg(EntityManager entityManager, PersonEntity personEntity){
        PoliceRegisterEntity.createPoliceRegisterEntry(entityManager, personEntity);
    }

    @Override
    public void buyAGun(EntityManager entityManager, PersonEntity personEntity) {
        View.printNotAllowed();
    }
}
