package driver;

import entity.*;
import exception.PersonNotFoundException;
import mapper.PersonEntityMapper;
import person.Person;
import person.state.EligibleForInspection;
import validator.InputValidator;
import view.View;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;
import static validator.InputValidator.validateDate;

@SuppressWarnings({"java:S2189", "java:S106"})
public class Driver {

    public static void main(String... args) {
        EntityManager entityManager = Persistence.createEntityManagerFactory("default")
                .createEntityManager();
        try (Scanner scanner = new Scanner(System.in)) {
            do {
                View.printMenu();
                switch (InputValidator.validateMenuInput(scanner.nextLine())) {
                    case 1:
                        View.printHintForEnteringData();
                        View.printHintWhenDataEntering();
                        addPerson(scanner, entityManager);
                        break;
                    case 2:
                        getPersonList(entityManager);
                        break;
                    case 3:
                        View.printHintForQueryingByCriteria();
                        editPersonData(entityManager, scanner);
                        break;
                    case 4:
                        View.printHintForQueryingByCriteria();
                        deletePerson(entityManager, scanner);
                        break;
                    case 5:
                        getPhysicalCertificate(entityManager, scanner);
                        break;
                    case 6:
                        getPsychologicalCertificate(entityManager, scanner);
                        break;
                    case 7:
                        addToPoliceRegistry(entityManager, scanner);
                        break;
                    case 8:
                        getGunCertificate(entityManager, scanner);
                        break;
                    case 9:
                        getGunBuyingCertificate(entityManager, scanner);
                        break;
                    case 10:
                        registerGun(entityManager, scanner);
                        break;
                    case 11:
                        performControl(entityManager, scanner);
                        break;
                    case 0:
                        exit(0);
                }
            } while (true);
        }

    }

    private static void addPerson(Scanner scanner, EntityManager entityManager) {
        PersonEntity.createPersonInDatabase(createPerson(scanner), entityManager);
        System.out.println(PersonEntity.PERSON_ADDED_TO_DATABASE_MESSAGE);
    }

    private static Person createPerson(Scanner scanner) {
        return new Person.PersonBuilder()
                .setName(InputValidator.validateString(scanner.nextLine()))
                .setSurname(InputValidator.validateString(scanner.nextLine()))
                .setBirthDate(validateDate(scanner.nextLine()))
                .setPersonState(new EligibleForInspection())
                .setCitizenOfRP(InputValidator.validateBoolean(scanner.nextLine()))
                .build();
    }

    private static List<PersonEntity> getAllPeople(EntityManager entityManager) {
        return PersonEntity.queryPersonTable(entityManager);
    }

    private static List<PersonEntity> getPeopleByCriteria(EntityManager entityManager, int peselNumber) {
        return PersonEntity.queryPersonByCriteria(entityManager, peselNumber);
    }

    private static void editPersonData(EntityManager entityManager, Scanner scanner) {
        View.printHintForEditingData();
        PersonEntity.alterPersonData(getPeopleByCriteria(entityManager, InputValidator.validatePeselNumber(scanner.nextLine()))
                        .stream()
                        .findFirst()
                        .orElseThrow(PersonNotFoundException::new),
                entityManager,
                scanner);
    }

    private static void deletePerson(EntityManager entityManager, Scanner scanner) {
        View.printHintForPeselNumber();
        PersonEntity.deletePerson(getPeopleByCriteria(entityManager, InputValidator.validatePeselNumber(scanner.nextLine()))
                        .stream()
                        .findFirst()
                        .orElseThrow(PersonNotFoundException::new),
                entityManager);
    }

    private static void getPersonList(EntityManager entityManager) {
        PersonEntityMapper.mapToPersonList(getAllPeople(entityManager)).forEach(System.out::println);
    }

    private static void getPhysicalCertificate(EntityManager entityManager, Scanner scanner) {
        View.printHintForPeselNumber();
        int peselNumber = InputValidator.validatePeselNumber(scanner.nextLine());
        getPerson(entityManager, scanner, peselNumber).getPersonState().getPhysicalCertificate(entityManager, peselNumber);
    }

    private static void getPsychologicalCertificate(EntityManager entityManager, Scanner scanner) {
        View.printHintForPeselNumber();
        int peselNumber = InputValidator.validatePeselNumber(scanner.nextLine());
        getPerson(entityManager, scanner, peselNumber).getPersonState().getPsychicalCertificate(entityManager, peselNumber);
    }

    private static void addToPoliceRegistry(EntityManager entityManager, Scanner scanner) {
        View.printHintForPeselNumber();
        int peselNumber = InputValidator.validatePeselNumber(scanner.nextLine());
        PersonEntity personEntity = getPeopleByCriteria(entityManager, peselNumber).stream()
                .findFirst()
                .orElseThrow(PersonNotFoundException::new);
        getPerson(entityManager, scanner, peselNumber).getPersonState().addToPoliceReg(entityManager, personEntity);
    }

    private static void getGunCertificate(EntityManager entityManager, Scanner scanner) {
        View.printHintForPeselNumber();
        int peselNumber = InputValidator.validatePeselNumber(scanner.nextLine());
        PersonEntity personEntity = getPeopleByCriteria(entityManager, peselNumber).stream()
                .findFirst()
                .orElseThrow(PersonNotFoundException::new);
        getPerson(entityManager, scanner, peselNumber).getPersonState().getGunCertificate(entityManager, personEntity);
    }

    private static void getGunBuyingCertificate(EntityManager entityManager, Scanner scanner) {
        View.printHintForPeselNumber();
        int peselNumber = InputValidator.validatePeselNumber(scanner.nextLine());
        PersonEntity personEntity = getPeopleByCriteria(entityManager, peselNumber).stream()
                .findFirst()
                .orElseThrow(PersonNotFoundException::new);
        getPerson(entityManager, scanner, peselNumber).getPersonState().buyAGun(entityManager, personEntity);
    }

    private static void registerGun(EntityManager entityManager, Scanner scanner) {
        View.printHintForPeselNumber();
        int peselNumber = InputValidator.validatePeselNumber(scanner.nextLine());
        PersonEntity personEntity = getPeopleByCriteria(entityManager, peselNumber).stream()
                .findFirst()
                .orElseThrow(PersonNotFoundException::new);
        getPerson(entityManager, scanner, peselNumber).getPersonState().registerGun(entityManager, personEntity);
    }

    private static void performControl(EntityManager entityManager, Scanner scanner) {
        View.printHintForPeselNumber();
        PersonEntity personEntity = getPeopleByCriteria(entityManager, InputValidator.validatePeselNumber(scanner.nextLine())).stream()
                .findFirst()
                .orElseThrow(PersonNotFoundException::new);
        if (PoliceRegisterEntity.getPoliceRegisterEntryByCriteria(entityManager, personEntity).isPresent()) {
            View.printNotAllowedPresentInPoliceRegister();
            return;
        }
        if (personEntity.getDateOfDeath() != null) {
            View.printNotAllowedIsDead();
            return;
        }
        View.printHintForDate();
        if ((InputValidator.validateDate(scanner.nextLine()).getTime() - (GunCertificateEntity.getGunCertificateByCriteria(entityManager, personEntity).get().getReleaseDate().getTime())) >= 432000000) { //in ms
            View.printNotAllowedNotRegisteredGun();
            return;
        }
        View.okStatus();
    }

    private static Person getPerson(EntityManager entityManager, Scanner scanner, int peselNumber) {
        PersonEntity personEntity = getPeopleByCriteria(entityManager, peselNumber).stream()
                .findFirst()
                .orElseThrow(PersonNotFoundException::new);
        Person person = PersonEntityMapper.mapToPerson(personEntity);
        person.setPersonState(entityManager, peselNumber);
        return person;
    }
}
