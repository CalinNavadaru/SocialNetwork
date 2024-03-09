package cs.ubbcluj.lab7_8_9map.domain.dto;

public class DTOUtilizator{

    private final String firstName;
    private final String lastName;

    private final String password;
    private Long id;

    public Long getId() {
        return id;
    }

    public DTOUtilizator(String f, String l, String password, Long i) {
        firstName = f;
        lastName = l;
        this.password = password;
        id = i;
    }

    @Override
    public String toString() {
        return firstName + ' ' + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setId(Long id) {
    }
}
