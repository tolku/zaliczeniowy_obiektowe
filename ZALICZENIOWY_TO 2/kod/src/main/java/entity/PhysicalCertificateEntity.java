package entity;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "physical_certificate", schema = "zaliczeniowy_to", catalog = "")
public class PhysicalCertificateEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "physical_certificate_id")
    private int physicalCertificateId;
    @OneToOne
    @JoinColumn(name = "person_id_fk", referencedColumnName = "person_id")
    private PersonEntity personIdFk;
    @Basic
    @Column(name = "release_date")
    private Date releaseDate;
    @Basic
    @Column(name = "decision")
    private int decision;
    @OneToOne(mappedBy = "physicalCertificateNumberFk", fetch = FetchType.LAZY)
    private GunCertificateEntity gunCertificateEntity;

    public void setPersonIdFk(PersonEntity personIdFk) {
        this.personIdFk = personIdFk;
    }

    public PersonEntity getPersonIdFk() {
        return personIdFk;
    }

    public int getPhysicalCertificateId() {
        return physicalCertificateId;
    }

    public void setPhysicalCertificateId(int physicalCertificateId) {
        this.physicalCertificateId = physicalCertificateId;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getDecision() {
        return decision;
    }

    public void setDecision(int decision) {
        this.decision = decision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhysicalCertificateEntity that = (PhysicalCertificateEntity) o;
        return physicalCertificateId == that.physicalCertificateId && personIdFk == that.personIdFk && decision == that.decision && Objects.equals(releaseDate, that.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(physicalCertificateId, personIdFk, releaseDate, decision);
    }

    public static void createPhysicalCertificate(EntityManager entityManager, PersonEntity personEntity) {
        performCleanupOfExistingCertificates(entityManager, personEntity);
        PhysicalCertificateEntity physicalCertificate = new PhysicalCertificateEntity();
        entityManager.getTransaction().begin();
        physicalCertificate.setPersonIdFk(personEntity);
        physicalCertificate.setDecision(1);
        physicalCertificate.setReleaseDate(Date.valueOf(LocalDate.now()));
        entityManager.persist(physicalCertificate);
        entityManager.getTransaction().commit();
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    public static Optional<PhysicalCertificateEntity> getPhysicalCertificateByCriteria(EntityManager entityManager,
                                                                                       PersonEntity personEntity) {
        try {
            return Optional.of((PhysicalCertificateEntity) entityManager.createQuery(
                            "SELECT certificate " +
                                    "FROM PhysicalCertificateEntity certificate " +
                                    "WHERE certificate.personIdFk = :personEntityId")
                    .setParameter("personEntityId", personEntity)
                    .getSingleResult());
        } catch (NoResultException noResultException) {
            return Optional.empty();
        }
    }

    private static void removeExistingCertificate(EntityManager entityManager,
                                                  PhysicalCertificateEntity physicalCertificateEntity) {
        entityManager.getTransaction().begin();
        entityManager.remove(physicalCertificateEntity);
        entityManager.getTransaction().commit();
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    private static void performCleanupOfExistingCertificates(EntityManager entityManager,
                                                             PersonEntity personEntity) {
        Optional<PhysicalCertificateEntity> oldPhysicalCertificate = getPhysicalCertificateByCriteria(entityManager, personEntity);
        oldPhysicalCertificate.ifPresent(physicalCertificateEntity -> removeExistingCertificate(entityManager, physicalCertificateEntity));
    }

}
