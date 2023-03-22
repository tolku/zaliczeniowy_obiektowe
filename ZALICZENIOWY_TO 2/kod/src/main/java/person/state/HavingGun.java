package person.state;

import entity.*;
import view.View;

import javax.persistence.EntityManager;

public class HavingGun implements PersonState{
    @Override
    public void getPhysicalCertificate(EntityManager entityManager, int peselNumber) {
        View.printNotAllowed();
    }

    @Override
    public void getPsychicalCertificate(EntityManager entityManager, int peselNumber) {
        View.printNotAllowed();
    }

    @Override
    public void getGunCertificate(EntityManager entityManager, PersonEntity personEntity) {
        View.printNotAllowed();
    }

    @Override
    public void registerGun(EntityManager entityManager, PersonEntity personEntity) {
        GunRegistryEntity.addGunRegistryEntry(entityManager, personEntity,
                GunCertificateEntity.getGunCertificateByCriteria(entityManager, personEntity).get());
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
