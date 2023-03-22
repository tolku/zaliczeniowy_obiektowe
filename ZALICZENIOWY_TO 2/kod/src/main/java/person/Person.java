package person;

import entity.*;
import exception.PersonNotFoundException;
import person.state.*;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;

public class Person {
    private String name;
    private String surname;
    private int peselNumber;
    private Date birthDate;
    private Optional<Date> dateOfDeath;
    private boolean isCitizenOfRP;
    private PersonState personState;

    private Person(PersonBuilder personBuilder) {
        this.name = personBuilder.name;
        this.surname = personBuilder.surname;
        this.peselNumber = personBuilder.peselNumber;
        this.birthDate = personBuilder.birthDate;
        this.dateOfDeath = Optional.ofNullable(personBuilder.dateOfDeath);
        this.isCitizenOfRP = personBuilder.isCitizenOfRP;
        this.personState = personBuilder.personState;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public int getIsCitizenOfRP() {
        return isCitizenOfRP ? 1 : 0;
    }

    public PersonState getPersonState() {
        return personState;
    }

    public void setPersonState(PersonState personState) {
        this.personState = personState;
    }

    public void setPersonState(EntityManager entityManager, int peselNumber){
        PersonEntity personEntity = PersonEntity.queryPersonByCriteria(entityManager, peselNumber)
                .stream()
                .findAny()
                .orElseThrow(PersonNotFoundException::new);
        this.personState = decideWhichState(PhysicalCertificateEntity.getPhysicalCertificateByCriteria(entityManager, personEntity).isPresent(),
                PsychicalCertificateEntity.getPsychicalCertificateByCriteria(entityManager, personEntity).isPresent(),
                PoliceRegisterEntity.getPoliceRegisterEntryByCriteria(entityManager, personEntity).isPresent(),
                GunCertificateEntity.getGunCertificateByCriteria(entityManager, personEntity).isPresent(),
                GunBuyingCertificateEntity.getGunBuyingCertificate(entityManager, personEntity).isPresent());
    }

    private PersonState decideWhichState(boolean isPhysicalPresent, boolean isPsychicalPresent,
                                         boolean isPresentInPoliceRegistry, boolean isGunCertificatePresent,
                                         boolean isGunBuyingCertificatePresent){
        PersonState state;
        if (this.dateOfDeath.isPresent()){
            return new Dead();
        }
        if (!this.isCitizenOfRP || isPresentInPoliceRegistry || isUnderAgeOf18()){
            return new NotEligibleForInspection();
        }
        state = new EligibleForInspection();
        if (isPhysicalPresent && isPsychicalPresent && !isPresentInPoliceRegistry){
            state = new HavingPermission();
            if (isGunCertificatePresent) {
                state = new HavingPermissionToBuyGun();
                if (isGunBuyingCertificatePresent){
                    state = new HavingGun();
                }
            }
        }
        return state;
    }

    private boolean isUnderAgeOf18(){
        String[] date = this.birthDate.toString().split("-");
        String[] currentDate = Date.valueOf(LocalDate.now()).toString().split("-");
        return ((Integer.parseInt(currentDate[0]) - Integer.parseInt(date[0])) < 18);
    }

    @Override
    public String toString() {
            return "Name: " + this.name +
                    " Surname: " + this.surname +
                    " Pesel number: " + this.peselNumber +
                    " Birth date: " + this.birthDate +
                    " Is RP citizen: " + this.isCitizenOfRP +
                    " Date of death: " + (this.dateOfDeath.isPresent() ? this.dateOfDeath.get() : "-");
    }

    public static class PersonBuilder {
        private String name;
        private String surname;
        private int peselNumber;
        private Date birthDate;
        private Date dateOfDeath;
        private boolean isCitizenOfRP;
        private PersonState personState;

        public PersonBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public PersonBuilder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public PersonBuilder setPeselNumber(int peselNumber) {
            this.peselNumber = peselNumber;
            return this;
        }

        public PersonBuilder setBirthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public PersonBuilder setDateOfDeath(Date dateOfDeath) {
            this.dateOfDeath = dateOfDeath;
            return this;
        }

        public PersonBuilder setCitizenOfRP(boolean citizenOfRP) {
            this.isCitizenOfRP = citizenOfRP;
            return this;
        }

        public PersonBuilder setPersonState(PersonState personState) {
            this.personState = personState;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }
}
