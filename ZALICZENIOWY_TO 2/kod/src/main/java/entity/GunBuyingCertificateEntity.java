package entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "gun_buying_certificate", schema = "zaliczeniowy_to", catalog = "")
public class GunBuyingCertificateEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "gun_buying_certificate_id")
    private int gunBuyingCertificateId;
    @Basic
    @Column(name = "gun_type")
    private int gunType;
    @Basic
    @Column(name = "number_of_guns")
    private int numberOfGuns;
    @Basic
    @Column(name = "ammo_type")
    private int ammoType;
    @OneToOne
    @JoinColumn(name = "person_id_fk", referencedColumnName = "person_id")
    private PersonEntity personIdFk;
    @OneToOne
    @JoinColumn(name = "gun_certificate_number_fk", referencedColumnName = "gun_certificate_id")
    private GunCertificateEntity gunCertificateNumberFk;

    public int getGunBuyingCertificateId() {
        return gunBuyingCertificateId;
    }

    public void setGunBuyingCertificateId(int gunBuyingCertificateId) {
        this.gunBuyingCertificateId = gunBuyingCertificateId;
    }

    public int getGunType() {
        return gunType;
    }

    public void setGunType(int gunType) {
        this.gunType = gunType;
    }

    public int getNumberOfGuns() {
        return numberOfGuns;
    }

    public void setNumberOfGuns(int numberOfGuns) {
        this.numberOfGuns = numberOfGuns;
    }

    public int getAmmoType() {
        return ammoType;
    }

    public void setAmmoType(int ammoType) {
        this.ammoType = ammoType;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GunBuyingCertificateEntity that = (GunBuyingCertificateEntity) o;
        return gunBuyingCertificateId == that.gunBuyingCertificateId && gunType == that.gunType && numberOfGuns == that.numberOfGuns && ammoType == that.ammoType && personIdFk == that.personIdFk && gunCertificateNumberFk == that.gunCertificateNumberFk;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gunBuyingCertificateId, gunType, numberOfGuns, ammoType, personIdFk, gunCertificateNumberFk);
    }

    public static void addGunBuyingCertificate(EntityManager entityManager, PersonEntity personEntity,
                                               GunCertificateEntity gunCertificateEntity) {
        performCleanupOfExistingCertificates(entityManager, personEntity);
        GunBuyingCertificateEntity gunBuyingCertificate = new GunBuyingCertificateEntity();
        entityManager.getTransaction().begin();
        gunBuyingCertificate.setGunType(1);
        gunBuyingCertificate.setNumberOfGuns(1);
        gunBuyingCertificate.setAmmoType(1);
        gunBuyingCertificate.setPersonIdFk(personEntity);
        gunBuyingCertificate.setGunCertificateNumberFk(gunCertificateEntity);
        entityManager.persist(gunBuyingCertificate);
        entityManager.getTransaction().commit();
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    public static Optional<GunBuyingCertificateEntity> getGunBuyingCertificate(EntityManager entityManager,
                                                                               PersonEntity personEntity) {
        try {
            return Optional.of((GunBuyingCertificateEntity) entityManager.createQuery(
                            "SELECT gunCertificate " +
                                    "FROM GunBuyingCertificateEntity gunCertificate " +
                                    "WHERE gunCertificate.personIdFk = :personEn")
                    .setParameter("personEn", personEntity)
                    .getSingleResult());
        } catch (NoResultException noResultException) {
            return Optional.empty();
        }
    }

    private static void removeExistingCertificate(EntityManager entityManager,
                                                  GunBuyingCertificateEntity gunBuyingCertificateEntity) {
        entityManager.getTransaction().begin();
        entityManager.remove(gunBuyingCertificateEntity);
        entityManager.getTransaction().commit();
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    private static void performCleanupOfExistingCertificates(EntityManager entityManager,
                                                             PersonEntity personEntity) {
        Optional<GunBuyingCertificateEntity> oldGunBuyingCertificate = getGunBuyingCertificate(entityManager, personEntity);
        oldGunBuyingCertificate.ifPresent(gunBuyingCertificateEntity -> removeExistingCertificate(entityManager, gunBuyingCertificateEntity));
    }
}
