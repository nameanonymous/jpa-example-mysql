package legoset;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import legoset.model.LegoSet;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class LegoSetService {

    private static Logger logger = LogManager.getLogger();

    private EntityManager em;
    private LegoSetService(EntityManager em) {
        this.em = em;
    }


    public LegoSet create(String number, String name, Year year, int pieces) {
        LegoSet legoset = new LegoSet(number,name,year,pieces);
        em.persist(legoset);
       return legoset;
    }

    public Optional<LegoSet> find(String number) {
        // TODO
        return Optional.ofNullable(em.find(LegoSet.class,number));
    }

    public List<LegoSet> findAll() {
        // TODO
        return em.createQuery("SELECT l FROM LegoSet l ORDER BY l.number",LegoSet.class).getResultList();
    }

    public Long totalPieces() {
        // TODO
        return em.createQuery("SELECT SUM(pieces) FROM LegoSet l",Long.class).getSingleResult();
    }

    public void delete(String number) {
        // TODO
        find(number).ifPresent(LegoSet ->
        {
            em.remove(LegoSet);
        });
    }

    public void deleteAll() {
        // TODO
        long count = em.createQuery("DELETE FROM LegoSet").executeUpdate();
        logger.info("Deleted {} LEGO set(s)",count);
    }

    public static void main(String[] args) {
          EntityManagerFactory emf = Persistence.createEntityManagerFactory("legoset-mysql");
          EntityManager em = emf.createEntityManager();
          LegoSetService service = new LegoSetService(em);

          em.getTransaction().begin();
          service.create("60073", "Service Truck", Year.of(2015), 233);
          service.create("75211", "Imperial TIE Fighter", Year.of(2018), 519);
          service.create("21034", "London", Year.of(2017), 468);
          em.getTransaction().commit();

          logger.info("LEGO set 60073: {}", service.find("60073"));
          logger.info("LEGO set 21027: {}", service.find("21027"));

          logger.info("All LEGO sets:");
          service.findAll().forEach(logger::info);

          logger.info("Total number of LEGO pieces: {}", service.totalPieces());

          em.getTransaction().begin();
          service.delete("60073");
          em.getTransaction().commit();

          logger.info("All LEGO sets:");
          service.findAll().forEach(logger::info);

          em.getTransaction().begin();
          service.deleteAll();
          em.getTransaction().commit();

          em.close();
          emf.close();
    }

}
