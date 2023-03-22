package person.state;

import entity.GunBuyingCertificateEntity;
import entity.GunCertificateEntity;
import entity.PersonEntity;
import entity.PoliceRegisterEntity;
import view.View;

import javax.persistence.EntityManager;

public class HavingPermissionToBuyGun implements PersonState{
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
        View.printNotAllowed();
    }

    @Override
    public void addToPoliceReg(EntityManager entityManager, PersonEntity personEntity) {
        PoliceRegisterEntity.createPoliceRegisterEntry(entityManager, personEntity);
    }

    @Override
    public void buyAGun(EntityManager entityManager, PersonEntity personEntity) {
        GunBuyingCertificateEntity.addGunBuyingCertificate(entityManager, personEntity,
                GunCertificateEntity.getGunCertificateByCriteria(entityManager, personEntity).get());
    }
}
