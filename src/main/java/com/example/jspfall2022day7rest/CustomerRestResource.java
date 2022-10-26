package com.example.jspfall2022day7rest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Customer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.List;

@Path("/customer")
public class CustomerRestResource {
    @GET
    @Path("/getallcustomers")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllCustomers() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager em = factory.createEntityManager();
        Query query = em.createQuery("select c from Customer c");
        List<Customer> list = query.getResultList();

        Gson gson = new Gson();

        return gson.toJson(list);
    }

    @GET
    @Path("/getcustomer/{ customerid }")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCustomer(@PathParam("customerid") int customerId) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager em = factory.createEntityManager();
        Customer customer = em.find(Customer.class, customerId);
        Gson gson = new Gson();

        return gson.toJson(customer);
    }

    @PUT
    @Path("/putcustomer")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces(MediaType.APPLICATION_JSON)
    public String putCustomer(String jsonString) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager em = factory.createEntityManager();
        Gson gson = new Gson();
        Type type = new TypeToken<Customer>(){}.getType();
        Customer customer = gson.fromJson(jsonString, type);
        //validate the data here

        em.getTransaction().begin();
        Customer updatedCustomer = em.merge(customer);
        if (updatedCustomer != null)
        {
            em.getTransaction().commit();
            em.close();
            factory.close();
            return "{ message: 'Customer updated successfully' }";
        }
        else
        {
            em.getTransaction().rollback();
            em.close();
            factory.close();
            return "{ message: 'Customer update failed' }";
        }
    }

    @POST
    @Path("/postcustomer")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces(MediaType.APPLICATION_JSON)
    public String postCustomer(String jsonString) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager em = factory.createEntityManager();
        Gson gson = new Gson();
        Type type = new TypeToken<Customer>(){}.getType();
        Customer customer = gson.fromJson(jsonString, type);
        //validate the data here

        em.getTransaction().begin();
        em.persist(customer);
        if (em.contains(customer))
        {
            em.getTransaction().commit();
            em.close();
            factory.close();
            return "{ message: 'Customer inserted successfully' }";
        }
        else
        {
            em.getTransaction().rollback();
            em.close();
            factory.close();
            return "{ message: 'Customer insert failed' }";
        }
    }

    @DELETE
    @Path("/deletecustomer/{ customerid }")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteCustomer(@PathParam("customerid") int customerId) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager em = factory.createEntityManager();
        Customer customer = em.find(Customer.class, customerId);

        em.getTransaction().begin();
        em.remove(customer);
        if (em.contains(customer))
        {
            em.getTransaction().rollback();
            em.close();
            factory.close();
            return "{ message: 'Customer delete failed' }";
        }
        else
        {
            em.getTransaction().commit();
            em.close();
            factory.close();
            return "{ message: 'Customer deleted' }";
        }
    }

}