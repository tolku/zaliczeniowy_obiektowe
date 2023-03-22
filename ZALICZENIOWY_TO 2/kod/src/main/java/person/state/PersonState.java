package person.state;

import entity.PersonEntity;


import javax.persistence.EntityManager;

public interface PersonState {
    void getPhysicalCertificate(EntityManager entityManager, int peselNumber);
    void getPsychicalCertificate(EntityManager entityManager, int peselNumber);
    void getGunCertificate(EntityManager entityManager, PersonEntity personEntity);
    void registerGun(EntityManager entityManager, PersonEntity personEntity);
    void addToPoliceReg(EntityManager entityManager, PersonEntity personEntity);
    void buyAGun(EntityManager entityManager, PersonEntity personEntity);
}
