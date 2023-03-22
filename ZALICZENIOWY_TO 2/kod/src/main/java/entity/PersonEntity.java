package entity;

import person.Person;
import validator.InputValidator;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.exit;

@Entity
@Table(name = "person", schema = "zaliczeniowy_to", catalog = "")
public class PersonEntity {
    public static final String PERSON_ADDED_TO_DATABASE_MESSAGE = "SUCCESFULLY ADDED PERSON TO THE DATABASE";

    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "surname")
    private String surname;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "person_id")
    private int personId;
    @Basic
    @Column(name = "birth_date")
    private Date birthDate;
    @Basic
    @Column(name = "date_of_death")
    private Date dateOfDeath;
    @Basic
    @Column(name = "is_polish_resident")
    private int isPolishResident;
    @OneToOne(mappedBy = "personIdFk", fetch = FetchType.LAZY)
    private GunCertificateEntity gunCertificate;
    @OneToOne(mappedBy = "personIdFk", fetch = FetchType.LAZY)
    private GunRegistryEntity gunRegistry;
    @OneToOne(mappedBy = "personIdFk", fetch = FetchType.LAZY)
    private PhysicalCertificateEntity physicalCertificate;
    @OneToOne(mappedBy = "personIdFk", fetch = FetchType.LAZY)
    private PoliceRegisterEntity policeRegister;
    @OneToOne(mappedBy = "personIdFk", fetch = FetchType.LAZY)
    private PsychicalCertificateEntity psychicalCertificate;
    @OneToOne(mappedBy = "personIdFk", fetch = FetchType.LAZY)
    private GunBuyingCertificateEntity gunBuyingCertificate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public int getIsPolishResident() {
        return isPolishResident;
    }

    public void setIsPolishResident(int isPolishResident) {
        this.isPolishResident = isPolishResident;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonEntity that = (PersonEntity) o;
        return personId == that.personId && isPolishResident == that.isPolishResident && Objects.equals(name, that.name) && Objects.equals(surname, that.surname) && Objects.equals(birthDate, that.birthDate) && Objects.equals(dateOfDeath, that.dateOfDeath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, personId, birthDate, dateOfDeath, isPolishResident);
    }

    public static void createPersonInDatabase(Person person, EntityManager entityManager) {
        try {
            PersonEntity personEntity = new PersonEntity();
            entityManager.getTransaction().begin();
            personEntity.setName(person.getName());
            personEntity.setSurname(person.getSurname());
            personEntity.setBirthDate(person.getBirthDate());
            personEntity.setIsPolishResident(person.getIsCitizenOfRP());
            entityManager.persist(personEntity);
            entityManager.getTransaction().commit();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        } catch (Exception exception) {
            exception.getCause();
        }

    }

    public static List<PersonEntity> queryPersonTable(EntityManager entityManager) {
        return (List<PersonEntity>) entityManager.createQuery("SELECT person FROM PersonEntity person").getResultList();
    }

    public static List<PersonEntity> queryPersonByCriteria(EntityManager entityManager, int peselNumber) {
        return (List<PersonEntity>) entityManager.createQuery("SELECT person FROM PersonEntity person WHERE person.personId = :peselNumber")
                .setParameter("peselNumber", peselNumber)
                .getResultList();
    }

    public static void alterPersonData(PersonEntity personEntity, EntityManager entityManager, Scanner scanner) {
        try {
            entityManager.getTransaction().begin();
            PersonEntity entity = entityManager.find(PersonEntity.class, personEntity.getPersonId());
            entity.setName(InputValidator.validateString(scanner.nextLine()));
            entity.setSurname(InputValidator.validateString(scanner.nextLine()));
            entity.setBirthDate(InputValidator.validateDate(scanner.nextLine()));
            entity.setIsPolishResident(InputValidator.validateInt(scanner.nextLine()));
            entity.setDateOfDeath(InputValidator.validateDeathDate(scanner.nextLine()));
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        } catch (Exception exception) {
            exception.getCause();
            exit(-1);
        }
    }

    public static void deletePerson(PersonEntity personEntity, EntityManager entityManager) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.find(PersonEntity.class, personEntity.getPersonId()));
            entityManager.getTransaction().commit();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        } catch (Exception exception) {
            exception.getCause();
            exit(-1);
        }
    }

}
