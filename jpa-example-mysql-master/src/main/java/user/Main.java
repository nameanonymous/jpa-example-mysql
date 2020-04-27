package user;

import com.github.javafaker.Faker;
import user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    private static Faker faker = new Faker();

    public static void main(String[] args) {
      EntityManagerFactory emf = Persistence.createEntityManagerFactory("legoset-mysql");
      EntityManager em = emf.createEntityManager();

      User user = User.builder().username("First").password("123456").build();
      em.getTransaction().begin();
      em.persist(user);
      em.getTransaction().commit();
      System.out.println(user);

      em.getTransaction().begin();
      user.setPassword("secret");
      em.getTransaction().commit();
      System.out.println(user);

      System.out.println(em.find(User.class,"First"));
      System.out.println(user == em.find(User.class,"First"));

      em.clear();

      System.out.println(em.find(User.class,"First"));
      System.out.println(user == em.find(User.class,"First"));

      em.getTransaction().begin();
      User managedUser = em.merge(user);
      managedUser.setPassword("98765");
      em.getTransaction().commit();
      System.out.println(em.find(User.class,"FIrst"));

      em.close();
      emf.close();
    }

}
