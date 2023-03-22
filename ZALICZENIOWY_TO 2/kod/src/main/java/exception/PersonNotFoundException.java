package exception;

public class PersonNotFoundException extends RuntimeException{
    private static final String PERSON_NOT_FOUND = "PERSON NOT FOUND";

    public PersonNotFoundException(){
        System.out.println(PERSON_NOT_FOUND);
    }
}
