package neeedo.imimaprx.htw.de.neeedo.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

@Root(name = "users")
public class Users implements Serializable, BaseEntity {

    @Element
    private ArrayList<User> users;

    public Users() {
    }


    public boolean isEmpty() {
        if (users == null || users.size() == 0) {
            return true;
        }
        return false;
    }

    public void addSingleDemand(User user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Users users1 = (Users) o;

        return !(users != null ? !users.equals(users1.users) : users1.users != null);

    }

    @Override
    public int hashCode() {
        return users != null ? users.hashCode() : 0;
    }
}
