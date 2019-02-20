import objects.Department;
import objects.Employee;
import objects.Vacation;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.File;
import java.util.*;

/**
 * Created by Danya on 26.10.2015.
 */
public class Loader
{
    private static SessionFactory sessionFactory;

    public static void main(String[] args)
    {
        setUp();

        // create a couple of events...
        Session session = sessionFactory.openSession();
        session.beginTransaction();


        //------------------------------------------HQL query-------------------------------------------------------

//        String hql = "SELECT e FROM Employee e\n" +
//                "JOIN e.ledDepartment ld JOIN FETCH e.ledDepartment\n" +
//                "WHERE ld != e.department";
//        Query query = session.createQuery(hql);
//        List<Object> objects = query.list();
//        for(Object object : objects)
//        {
//            Employee e = (Employee) object;
//            System.out.println("Глава отдела: " + e.getName());
//            System.out.println("Возглавляемый отдел: " + e.getLedDepartment().getName());
//            System.out.println("Отдел работы: " + e.getDepartment().getName());
//        System.out.println();
//        }


//        String hql = "SELECT e FROM Employee e\n" +
//                "JOIN e.ledDepartment ld\n" +
//                "WHERE e.salary < 115000";
//        Query query = session.createQuery(hql);
//        List<Object> objects = query.list();
//        for(Object object : objects)
//        {
//            Employee e = (Employee) object;
//            System.out.println("Глава отдела: " + e.getName());
//            System.out.println("Возглавляемый отдел: " + e.getLedDepartment().getName());
//            System.out.println("Заработная плата: " + e.getSalary());
//            System.out.println();
//        }


//        String hql = "SELECT e FROM Employee e\n" +
//            "JOIN e.ledDepartment ld\n" +
//            "WHERE e.hireDate < '2010-2-1'";
//        Query query = session.createQuery(hql);
//        List<Object> objects = query.list();
//        for(Object object : objects)
//        {
//            Employee e = (Employee) object;
//            System.out.println("Глава отдела: " + e.getName());
//            System.out.println("Возглавляемый отдел: " + e.getLedDepartment().getName());
//            System.out.println("Дата устройства: " + e.getHireDate());
//            System.out.println();
//        }

        //--------------------------------------------------------------------------------------------------------

//        vacationGeneration(session);
//
//        String hql = "SELECT v1, v2\n" +
//                "FROM Vacation v1 JOIN v1.employee e1, Vacation v2 JOIN v2.employee e2\n" +
//                "WHERE v1.vacationStart <= v2.endOfVacation AND v2.vacationStart <= v1.endOfVacation\n" +
//                "AND e1 != e2 AND v1 < v2";
//        List<Object[]> objects = session.createQuery(hql).list();
//        for (Object[] object : objects)
//        {
//            Vacation v1 = (Vacation) object[0];
//            Vacation v2 = (Vacation) object[1];
//
//            System.out.println(v1.getEmployee().getName() + " c " + v1.getVacationStart() + " до " + v1.getEndOfVacation());
//            System.out.println(v2.getEmployee().getName() + " c " + v2.getVacationStart() + " до " + v2.getEndOfVacation());
//            System.out.println();
//        }


        //---------------------------------------------------------------------------------------------------------

//        List<Department> departments = (List<Department>) session.createQuery("FROM Department").list();
//        for(Department department : departments) {
//            System.out.println(department.getName());
//        }

//        Department dept = new Department("Отдел производства");
//        session.save(dept);
//        System.out.println(dept.getId());

//        Department dept = (Department) session.createQuery("FROM Department WHERE name=:name")
//            .setParameter("name", "Отдел производства").list().get(0);
//        session.delete(dept);

        session.getTransaction().commit();
        session.close();

        //==================================================================
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    private static void vacationGeneration(Session session) {
        if(0 == (Long) session.createQuery("SELECT count(*) FROM Vacation v").uniqueResult())
        {
            Calendar hireDate;
            int year;

            List<Employee> employees = (List<Employee>) session.createQuery("FROM Employee").list();
            for(Employee e : employees)
            {
                hireDate = new GregorianCalendar();
                hireDate.setTime(e.getHireDate());
                year = hireDate.get(Calendar.YEAR);
                int[] vacationTime = {21, 28};

                if(hireDate.get(Calendar.MONTH) <= 4)
                {
                    saveVacation(session, hireDate, e, year, vacationTime, true);
                    year++;
                } else{
                    year++;
                }

                for(; year <= 2020; year++)
                {
                    saveVacation(session, hireDate, e, year, vacationTime, false);
                }
            }
        }
    }

    private static void saveVacation(Session session, Calendar hireDate, Employee e, int year, int[] vacationTime, boolean flag) {
        Calendar dateVacation = new GregorianCalendar();
        Vacation v = new Vacation();

        dateVacation.set(Calendar.YEAR, year);
        if(flag){
            dateVacation.set(Calendar.DAY_OF_YEAR, randBetween(hireDate.get(Calendar.DAY_OF_YEAR) + 60, hireDate.getActualMaximum(Calendar.DAY_OF_YEAR) - 28));
        } else {
            dateVacation.set(Calendar.DAY_OF_YEAR, randBetween(1, hireDate.getActualMaximum(Calendar.DAY_OF_YEAR) - 28));
        }

        v.setEmployee(e);
        v.setVacationStart(dateVacation.getTime());
        dateVacation.add(Calendar.DAY_OF_YEAR, randomArray(vacationTime) - 1);
        v.setEndOfVacation(dateVacation.getTime());
        session.save(v);
    }

    private static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    private static int randomArray(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    //=====================================================================

    private static void setUp()
    {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure(new File("src/config/hibernate.cfg.xml")) // configures settings from hibernate.config.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }






}
