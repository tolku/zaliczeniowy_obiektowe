package entity;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "gun_registry", schema = "zaliczeniowy_to", catalog = "")
public class GunRegistryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "gun_registry_id")
    private int gunRegistryId;
    @Basic
    @Column(name = "gun_number")
    private int gunNumber;
    @Basic
    @Column(name = "storing_place")
    private String storingPlace;
    @Basic
    @Column(name = "is_owner_alive")
    private Integer isOwnerAlive;
    @OneToOne
    @JoinColumn(name = "person_id_fk", referencedColumnName = "person_id")
    private PersonEntity personIdFk;
    @OneToOne
    @JoinColumn(name = "gun_certificate_number_fk", referencedColumnName = "gun_certificate_id")
    private GunCertificateEntity gunCertificateNumberFk;
    @Basic
    @Column(name = "release_date")
    private Date releaseDate;

    public PersonEntity getPersonIdFk() {
        return personIdFk;
    }

    public GunCertificateEntity getGunCertificateNumberFk() {
        return gunCertificateNumberFk;
    }

    public void setPersonIdFk(PersonEntity personIdFk) {
        this.personIdFk = personIdFk;
    }

    public void setGunCertificateNumberFk(GunCertificateEntity gunCertificateNumberFk) {
        this.gunCertificateNumberFk = gunCertificateNumberFk;
    }

    public int getGunRegistryId() {
        return gunRegistryId;
    }

    public void setGunRegistryId(int gunRegistryId) {
        this.gunRegistryId = gunRegistryId;
    }

    public int getGunNumber() {
        return gunNumber;
    }

    public void setGunNumber(int gunNumber) {
        this.gunNumber = gunNumber;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getStoringPlace() {
        return storingPlace;
    }

    public Integer getIsOwnerAlive() {
        return isOwnerAlive;
    }

    public void setStoringPlace(String storingPlace) {
        this.storingPlace = storingPlace;
    }

    public void setIsOwnerAlive(Integer isOwnerAlive) {
        this.isOwnerAlive = isOwnerAlive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GunRegistryEntity that = (GunRegistryEntity) o;
        return gunRegistryId == that.gunRegistryId && gunNumber == that.gunNumber && personIdFk == that.personIdFk && gunCertificateNumberFk == that.gunCertificateNumberFk && Objects.equals(releaseDate, that.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gunRegistryId, gunNumber, personIdFk, gunCertificateNumberFk, releaseDate);
    }

    public static void addGunRegistryEntry(EntityManager entityManager, PersonEntity personEntity,
                                           GunCertificateEntity gunCertificateEntity) {
        performCleanupOfExistingEntry(entityManager, personEntity);
        GunRegistryEntity gunRegistry = new GunRegistryEntity();
        entityManager.getTransaction().begin();
        gunRegistry.setGunNumber(10001);
        gunRegistry.setStoringPlace("osiedle na wzgorzach 28");
        gunRegistry.setReleaseDate(Date.valueOf(LocalDate.now()));
        gunRegistry.setPersonIdFk(personEntity);
        gunRegistry.setGunCertificateNumberFk(gunCertificateEntity);
        entityManager.persist(gunRegistry);
        entityManager.getTransaction().commit();
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    public static Optional<GunRegistryEntity> getGunRegistryEntryByCriteria(EntityManager entityManager,
                                                                            PersonEntity personEntity) {
        try {
            return Optional.of((GunRegistryEntity) entityManager.createQuery(
                            "SELECT gunRegistryEntity " +
                                    "FROM GunRegistryEntity gunRegistryEntity " +
                                    "WHERE gunRegistryEntity.personIdFk = :personEn")
                    .setParameter("personEn", personEntity)
                    .getSingleResult());
        } catch (NoResultException noResultException) {
            return Optional.empty();
        }
    }

    public static void removeGunRegistryEntry(EntityManager entityManager, PersonEntity personEntity) {
        entityManager.getTransaction().begin();
        entityManager.remove(getGunRegistryEntryByCriteria(entityManager, personEntity));
        entityManager.getTransaction().commit();
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    private static void removeExistingGunRegistryEntry(EntityManager entityManager,
                                                       GunRegistryEntity gunRegistryEntity) {
        entityManager.getTransaction().begin();
        entityManager.remove(gunRegistryEntity);
        entityManager.getTransaction().commit();
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    private static void performCleanupOfExistingEntry(EntityManager entityManager,
                                                      PersonEntity personEntity) {
        Optional<GunRegistryEntity> oldGunRegistryEntry = getGunRegistryEntryByCriteria(entityManager, personEntity);
        oldGunRegistryEntry.ifPresent(gunRegistryEntity -> removeExistingGunRegistryEntry(entityManager, gunRegistryEntity));
    }
}
