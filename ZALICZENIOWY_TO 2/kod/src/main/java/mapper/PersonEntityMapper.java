package mapper;

import entity.PersonEntity;
import person.Person;

import java.util.LinkedList;
import java.util.List;

public class PersonEntityMapper {
    public static List<Person> mapToPersonList(List<PersonEntity> personEntities) {
        List<Person> personList = new LinkedList<>();
        personEntities.forEach(pEntity -> personList.add(new Person.PersonBuilder()
                        .setName(pEntity.getName())
                        .setSurname(pEntity.getSurname())
                        .setPeselNumber(pEntity.getPersonId())
                        .setBirthDate(pEntity.getBirthDate())
                        .setCitizenOfRP(mapIntToBoolean(pEntity.getIsPolishResident()))
                        .setDateOfDeath(pEntity.getDateOfDeath())
                        .build()));
        return personList;
    }

    public static Person mapToPerson(PersonEntity personEntity){
        return new Person.PersonBuilder()
                .setName(personEntity.getName())
                .setSurname(personEntity.getSurname())
                .setPeselNumber(personEntity.getPersonId())
                .setBirthDate(personEntity.getBirthDate())
                .setCitizenOfRP(mapIntToBoolean(personEntity.getIsPolishResident()))
                .setDateOfDeath(personEntity.getDateOfDeath())
                .build();
    }

    private static boolean mapIntToBoolean(int value) {
        return value == 1;
    }
}
