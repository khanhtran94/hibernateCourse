package com.teamtreehouse.contactmgr;

import com.teamtreehouse.contactmgr.model.Contact;
import com.teamtreehouse.contactmgr.model.Contact.ContactBuilder;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

/**
 * Created by janie on 11.6.2016.
 */
public class Application {

    //Hold a reusable reference to a SessionFactory (we need only one)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        //create a standardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new ContactBuilder("Corvo","Attano")
                .withEmail("dishonored@gmail.com")
                .withPhone(1234567899L)
                .build();
        int id = save(contact);

        //display a list of contacts before the update
        System.out.println("before the update%n");
        fetchAllContacts().stream().forEach(System.out::println);

        //get the persisted contact
        Contact c = findContactById(id);

        //update the contact
        c.setFirstName("James");
        c.setLastName("Bond");
        c.setEmail("james.bond@gmail.com");

        //persist the changes
        System.out.println("%nupdating%n");
        update(c);
        System.out.println("%update complete%n");

        //display a list of contacts after the update
        System.out.println("after the update%n");
        fetchAllContacts().stream().forEach(System.out::println);
        c = findContactById(1);
        //delete contact
        //if(c != null)
        //delete(c);
        //System.out.println("after the delete%n");
        //fetchAllContacts().stream().forEach(System.out::println);
        //deleteAllContacts(fetchAllContacts());
        //System.out.println("after the  full delete%n");
        //fetchAllContacts().stream().forEach(System.out::println);



    }
    private static Contact findContactById(int id){
        //open a session
        Session session = sessionFactory.openSession();
        //retrieve the persistent object(null if not found)
        Contact contact= session.get(Contact.class,id);
        //close the session
        session.close();
        //return the object
        return  contact;
    }

    private static void update(Contact contact){
        //open session
        Session session = sessionFactory.openSession();
        //begin a transaction
        session.beginTransaction();
        //use the session to update the contact
        session.update(contact);
        //commit the transaction
        session.getTransaction().commit();
        //close the session
        session.close();
    }

    private static void delete(Contact contact){
        //open session
        Session session = sessionFactory.openSession();
        //begin a transaction
        session.beginTransaction();
        //use the session to update the contact
        session.delete(contact);
        //commit the transaction
        session.getTransaction().commit();
        //close the session
        session.close();
    }

    private static void deleteAllContacts(List<Contact> contacts){
        //open session
        Session session = sessionFactory.openSession();
        //begin a transaction
        session.beginTransaction();
        //use the session to update the contact
        contacts.forEach(session::delete);
        //commit the transaction
        session.getTransaction().commit();
        //close the session
        session.close();
    }


    @SuppressWarnings("unchecked")
    private static List<Contact>fetchAllContacts(){
        //Open a session
        Session session = sessionFactory.openSession();

        //create a criteria object
        Criteria criteria = session.createCriteria(Contact.class);

        //get a list of contact objects according to the criteria object
        List<Contact>contacts = criteria.list();

        //close session
        session.close();
        return contacts;
    }

    private static int save(Contact contact){
        //open a session
        Session session = sessionFactory.openSession();

        //begin transaction
        session.beginTransaction();

        //use the session to save the contact
       int id = (int)session.save(contact);

        //commit the transaction
        session.getTransaction().commit();

        //close the session
        session.close();
        return id;
    }
}
