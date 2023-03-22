package entity;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "psychical_certificate", schema = "zaliczeniowy_to", catalog = "")
public class PsychicalCertificateEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "psychical_certificate_id")
    private int psychicalCertificateId;
    @OneToOne
    @JoinColumn(name = "person_id_fk", referencedColumnName = "person_id")
    private PersonEntity personIdFk;
    @Basic
    @Column(name = "release_date")
    private Date releaseDate;
    @Basic
    @Column(name = "decision")
    private int decision;
    @OneToOne(mappedBy = "psychicalCertificateNumberFk", fetch = FetchType.LAZY)
    private GunCertificateEntity gunCertificateEntity;

    public PersonEntity getPersonIdFk() {
        return personIdFk;
    }

    public void setPersonIdFk(PersonEntity personIdFk) {
        this.personIdFk = personIdFk;
    }

    public int getPsychicalCertificateId() {
        return psychicalCertificateId;
    }

    public void setPsychicalCertificateId(int psychicalCertificateId) {
        this.psychicalCertificateId = psychicalCertificateId;
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

    public GunCertificateEntity getGunCertificateEntity() {
        return gunCertificateEntity;
    }

    public void setGunCertificateEntity(GunCertificateEntity gunCertificateEntity) {
        this.gunCertificateEntity = gunCertificateEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsychicalCertificateEntity that = (PsychicalCertificateEntity) o;
        return psychicalCertificateId == that.psychicalCertificateId && personIdFk == that.personIdFk && decision == that.decision && Objects.equals(releaseDate, that.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(psychicalCertificateId, personIdFk, releaseDate, decision);
    }

    public static void createPsychicalCertificate(EntityManager entityManager, PersonEntity personEntity) {
        performCleanupOfExistingCertificates(entityManager, personEntity);
        PsychicalCertificateEntity psychicalCertificate = new PsychicalCertificateEntity();
        entityManager.getTransaction().begin();
        psychicalCertificate.setPersonIdFk(personEntity);
        psychicalCertificate.setReleaseDate(Date.valueOf(LocalDate.now()));
        psychicalCertificate.setDecision(1);
        entityManager.persist(psychicalCertificate);
        entityManager.getTransaction().commit();
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    public static Optional<PsychicalCertificateEntity> getPsychicalCertificateByCriteria(EntityManager entityManager,
                                                                                         PersonEntity personEntity) {
        try {
            return Optional.of((PsychicalCertificateEntity) entityManager.createQuery(
                            "SELECT certificate " +
                                    "FROM PsychicalCertificateEntity certificate " +
                                    "WHERE certificate.personIdFk = :personPesel")
                    .setParameter("personPesel", personEntity)
                    .getSingleResult());
        } catch (NoResultException noResultException) {
            return Optional.empty();
        }
    }

    private static void performCleanupOfExistingCertificates(EntityManager entityManager,
                                                             PersonEntity personEntity) {
        Optional<PsychicalCertificateEntity> oldPhysicalCertificate = getPsychicalCertificateByCriteria(entityManager, personEntity);
        oldPhysicalCertificate.ifPresent(physicalCertificateEntity -> removeExistingCertificate(entityManager, physicalCertificateEntity));
    }

    private static void removeExistingCertificate(EntityManager entityManager,
                                                  PsychicalCertificateEntity psychicalCertificateEntity) {
        entityManager.getTransaction().begin();
        entityManager.remove(psychicalCertificateEntity);
        entityManager.getTransaction().commit();
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

}
