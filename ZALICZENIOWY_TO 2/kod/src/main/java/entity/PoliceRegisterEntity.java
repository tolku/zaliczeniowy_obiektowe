package entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "police_register", schema = "zaliczeniowy_to", catalog = "")
public class PoliceRegisterEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "police_register_id")
    private int policeRegisterId;
    @OneToOne
    @JoinColumn(name = "person_id_fk", referencedColumnName = "person_id")
    private PersonEntity personIdFk;

    public PersonEntity getPersonIdFk() {
        return personIdFk;
    }

    public void setPersonIdFk(PersonEntity personIdFk) {
        this.personIdFk = personIdFk;
    }

    public int getPoliceRegisterId() {
        return policeRegisterId;
    }

    public void setPoliceRegisterId(int policeRegisterId) {
        this.policeRegisterId = policeRegisterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PoliceRegisterEntity that = (PoliceRegisterEntity) o;
        return policeRegisterId == that.policeRegisterId && personIdFk == that.personIdFk;
    }

    @Override
    public int hashCode() {
        return Objects.hash(policeRegisterId, personIdFk);
    }

    public static void createPoliceRegisterEntry(EntityManager entityManager, PersonEntity personEntity) {
        performCleanupOfExistingCertificates(entityManager, personEntity);
        PoliceRegisterEntity policeRegister = new PoliceRegisterEntity();
        entityManager.getTransaction().begin();
        policeRegister.setPersonIdFk(personEntity);
        entityManager.persist(policeRegister);
        entityManager.getTransaction().commit();
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    public static Optional<PoliceRegisterEntity> getPoliceRegisterEntryByCriteria(EntityManager entityManager,
                                                                                  PersonEntity personEntity) {
        try {
            return Optional.of((PoliceRegisterEntity) entityManager.createQuery("SELECT registryEntry FROM PoliceRegisterEntity registryEntry WHERE registryEntry.personIdFk = :personEntity")
                    .setParameter("personEntity", personEntity)
                    .getSingleResult());
        } catch (NoResultException noResultException) {
            return Optional.empty();
        }
    }

    private static void removeExistingPoliceRegisterEntry(EntityManager entityManager,
                                                          PoliceRegisterEntity policeRegisterEntity) {
        entityManager.getTransaction().begin();
        entityManager.remove(policeRegisterEntity);
        entityManager.getTransaction().commit();
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    private static void performCleanupOfExistingCertificates(EntityManager entityManager,
                                                             PersonEntity personEntity) {
        Optional<PoliceRegisterEntity> oldPoliceRegistryEntry = getPoliceRegisterEntryByCriteria(entityManager, personEntity);
        oldPoliceRegistryEntry.ifPresent(policeRegisterEntity -> removeExistingPoliceRegisterEntry(entityManager, policeRegisterEntity));
    }
}
