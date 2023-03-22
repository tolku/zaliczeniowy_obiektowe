package person.state;

import entity.PersonEntity;
import view.View;

import javax.persistence.EntityManager;

public class Dead implements PersonState{
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
    public void addToPoliceReg(EntityManager entityManager, PersonEntity personEntity){
        View.printNotAllowed();
    }

    @Override
    public void buyAGun(EntityManager entityManager, PersonEntity personEntity) {
        View.printNotAllowed();
    }
}
