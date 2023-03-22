package view;

public class View {
    private View() {
    }

    public static void printMenu() {
        System.out.println("1. DODAJ OSOBĘ\n" +
                "2. WYŚWIETL WSZYSTKIE OSOBY\n" +
                "3. EDYTUJ OSOBĘ\n" +
                "4. USUN OSOBE\n" +
                "5. WYDAJ ZAŚWIADCZENIE LEKARSKIE\n" +
                "6. WYDAJ ZASWIADCZENIE PSYCHOLOGICZNE\n" +
                "7. DODAJ DO REJESTRU POLICYJNEGO\n" +
                "8. WYDAJ POZWOLENIE NA BRON\n" +
                "9. WYDAJ ZASWIADCZENIE UPRAWNIAJACE DO POSIADANIA BRONI\n" +
                "10. ZAREJESTRUJ BRON\n" +
                "11. SKONTROLUJ DANA OSOBE\n" +
                "0. WYJŚCIE");
    }

    public static void printHintWhenDataEntering() {
        System.out.println("FOR DATA: \n" +
                "data should be provided in specific format: yyyy-[m]m-[d]d\n" +
                "year should be greater than 1800, and lesser than current year\n" +
                "single digit month should be prefixed with 0, should be greater than 0 and lesser than 12\n" +
                "single digit day should be prefixed with 0, should be greater than 0 and lesser than maximum number of days available per certain month\n");
    }

    public static void printHintForEnteringData() {
        System.out.println("write name then enter\n" +
                "write surname then enter\n" +
                "write birthdate then enter\n" +
                "write TRUE if citizen of RP or FALSE if not\n");

    }

    public static void printHintForQueryingByCriteria() {
        System.out.println("write the pesel number by which you want to find certain person");
    }

    public static void printHintForEditingData(){
        System.out.println("write name then enter\n" +
                "write surname then enter\n" +
                "write birthdate then enter\n" +
                "write TRUE if citizen of RP or FALSE if not\n" +
                "write date of death if relevant, if not hit enter\n");
    }

    public static void printHintForPeselNumber(){
        System.out.println("write pesel number: ");
    }

    public static void printNotAllowedPresentInPoliceRegister(){
        System.out.println("this person is not allowed to poses gun, because is present in Police Registry");
    }

    public static void printNotAllowedIsDead(){
        System.out.println("this person is not allowed to poses gun, because its dead");
    }

    public static void printNotAllowedNotRegisteredGun(){
        System.out.println("this person is not allowed to poses gun, because its not registered after 5days");
    }

    public static void okStatus(){
        System.out.println("CONTROL OK");
    }

    public static void printHintForDate(){
        System.out.println("WRITE CURRENT DATE:");
    }

    public static void printNotAllowed(){
        System.out.println("NOT ALLOWED");
    }
}
