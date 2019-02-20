package objects;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Danya on 26.10.2015.
 */
public class Department
{
    private Integer id;
    private Employee head;
    private String name;
    private String description;

    private Set<Employee> employees = new HashSet<>(0);

    public Department() {
        //Used by Hibernate
    }

    public Department(String name)
    {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getHead() {
        return head;
    }

    public void setHead(Employee head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
